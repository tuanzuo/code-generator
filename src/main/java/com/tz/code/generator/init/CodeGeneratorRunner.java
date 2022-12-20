package com.tz.code.generator.init;

import com.google.common.collect.Lists;
import com.tz.code.generator.config.CodeGeneratorProperties;
import com.tz.code.generator.converter.TypeConverter;
import com.tz.code.generator.domain.FieldInfo;
import com.tz.code.generator.domain.TableInfo;
import com.tz.code.generator.domain.TemplateModelInfo;
import com.tz.code.generator.resolver.ITemplateResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 运行代码生成器
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-11 18:03
 **/
@Component
public class CodeGeneratorRunner implements CommandLineRunner {

    private static final String REPLCE_TABLE_NAME_PARAM = "${tableNameParam}";
    private static final String DB_NAME_PARAM = "dbName";
    private static final String TABLE_NAME_PARAM = "tableName";

    private static final String TABLE_NAME_FIELD = "TABLE_NAME";
    private static final String TABLE_COMMENT_FIELD = "TABLE_COMMENT";
    private static final String COLUMN_NAME_FIELD = "COLUMN_NAME";
    private static final String COLUMN_COMMENT_FIELD = "COLUMN_COMMENT";
    private static final String COLUMN_KEY_FIELD = "COLUMN_KEY";

    /**
     * 主键索引标识
     */
    private static final String PRIMARY_KEY_CONSTANT = "PRI";

    /**
     * 表的schema信息查询sql
     */
    private static final String SQL_TABLE = "SELECT TABLE_NAME,TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = :dbName AND TABLE_NAME = :tableName";
    /**
     * 表列的schema信息查询sql
     */
    private static final String SQL_TABLE_COLUMN = "SELECT COLUMN_NAME,COLUMN_COMMENT,COLUMN_KEY FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = :dbName AND TABLE_NAME = :tableName";
    /**
     * 表的信息查询sql
     */
    private static final String SQL_TABLE_INFO = "SELECT * FROM ${tableNameParam} WHERE 1=2";

    @Value("${db.name}")
    private String dbName;

    @Autowired
    private CodeGeneratorProperties codeGeneratorProperties;
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private TypeConverter typeConverter;
    @Autowired
    private ITemplateResolver templateResolver;

    @Override
    public void run(String... args) throws Exception {
        for (String tableName : codeGeneratorProperties.getTableNames()) {
            //1、获取模板model数据
            TemplateModelInfo templateModelInfo = this.getModelInfo(tableName);
            //2、生成PO
            codeGeneratorProperties.getJavaModelTemplates().forEach(template -> {
                String fileName = StringUtils.join(templateModelInfo.getTableInfo().getHumpName(), "PO.java");
                this.genCodeFile(templateModelInfo, template, codeGeneratorProperties.getJavaModelPath(), codeGeneratorProperties.getJavaModelPackage(), fileName);
            });
            //3、生成mapper
            for (int i = 0; i < codeGeneratorProperties.getJavaMapperTemplates().size(); i++) {
                String fileName = null;
                if (i == 0) {
                    fileName = StringUtils.join(templateModelInfo.getTableInfo().getHumpName(), "Mapper.java");
                } else if (i == 1) {
                    fileName = StringUtils.join(templateModelInfo.getTableInfo().getHumpName(), "UdfMapper.java");
                }
                this.genCodeFile(templateModelInfo, codeGeneratorProperties.getJavaMapperTemplates().get(i), codeGeneratorProperties.getJavaMapperPath(), codeGeneratorProperties.getJavaMapperPackage(), fileName);
            }
            //4、生成xml
            for (int i = 0; i < codeGeneratorProperties.getMapperXmlTemplates().size(); i++) {
                String fileName = null;
                if (i == 0) {
                    fileName = StringUtils.join(templateModelInfo.getTableInfo().getHumpName(), "Mapper.xml");
                } else if (i == 1) {
                    fileName = StringUtils.join(templateModelInfo.getTableInfo().getHumpName(), "UdfMapper.xml");
                }
                this.genCodeFile(templateModelInfo, codeGeneratorProperties.getMapperXmlTemplates().get(i), codeGeneratorProperties.getMapperXmlPath(), codeGeneratorProperties.getMapperXmlPackage(), fileName);
            }
        }
    }

    private void genCodeFile(TemplateModelInfo templateModelInfo, String template, String javaModelPath, String javaModelPackage, String fileName) {
        //1、得到解析后的模板内容
        String templateContent = templateResolver.resolve(template, templateModelInfo);
        //2、生成代码文件
        String[] packageArrays = StringUtils.split(javaModelPackage, ".");
        String filePath = StringUtils.join(Lists.newArrayList(javaModelPath, StringUtils.join(packageArrays, File.separator), fileName).toArray()
                , File.separator);
        try {
            File file = new File(filePath);
            if (!(file.getParentFile().exists())) {
                file.getParentFile().mkdirs();
            }
            FileCopyUtils.copy(templateContent.getBytes(), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TemplateModelInfo getModelInfo(String tableName) {
        Map<String, String> paramMap = new HashMap<>(2);
        paramMap.put(DB_NAME_PARAM, dbName);
        paramMap.put(TABLE_NAME_PARAM, tableName);
        TableInfo tableInfo = namedParameterJdbcTemplate.queryForObject(SQL_TABLE, paramMap, (resultSet, rowNum) -> this.mapperTableInfo(resultSet, rowNum));
        //字段和注释的对应Map，key:字段名，value:字段对应的注释
        Map<String, String> fieldToCommentMap = new HashMap<>();
        //主键集合
        Set<String> primaryKeyFieldSet = new HashSet<>();
        this.setFieldInfo(paramMap, fieldToCommentMap, primaryKeyFieldSet);

        SqlRowSet sqlRowSet = this.queryTableFieldInfo(paramMap);
        Set<String> javaTypeNames = new HashSet<>();
        List<FieldInfo> fieldInfos = this.buildFieldInfoList(sqlRowSet, fieldToCommentMap, primaryKeyFieldSet, javaTypeNames);
        tableInfo.setColumnInfos(fieldInfos);
        tableInfo.setJavaTypeNames(javaTypeNames);

        TemplateModelInfo templateModelInfo = new TemplateModelInfo();
        templateModelInfo.setAuthor(codeGeneratorProperties.getAuthor());
        templateModelInfo.setJavaModelPackage(codeGeneratorProperties.getJavaModelPackage());
        templateModelInfo.setJavaMapperPackage(codeGeneratorProperties.getJavaMapperPackage());
        templateModelInfo.setTableInfo(tableInfo);
        return templateModelInfo;
    }

    private void setFieldInfo(Map<String, String> paramMap, Map<String, String> fieldToCommentMap, Set<String> primaryKeyFieldSet) {
        List<Map<String, Object>> columnList = namedParameterJdbcTemplate.queryForList(SQL_TABLE_COLUMN, paramMap);
        for (Map<String, Object> columnMap : columnList) {
            //字段名
            String fieldName = String.valueOf(columnMap.get(COLUMN_NAME_FIELD));
            fieldToCommentMap.put(fieldName, String.valueOf(columnMap.get(COLUMN_COMMENT_FIELD)));

            String columnKey = String.valueOf(columnMap.get(COLUMN_KEY_FIELD));
            //判断是否是主键索引
            if (PRIMARY_KEY_CONSTANT.equals(columnKey)) {
                primaryKeyFieldSet.add(fieldName);
            }
        }
    }

    private SqlRowSet queryTableFieldInfo(Map<String, String> paramMap) {
        String querySql = SQL_TABLE_INFO.replace(REPLCE_TABLE_NAME_PARAM, paramMap.get(TABLE_NAME_PARAM));
        return namedParameterJdbcTemplate.queryForRowSet(querySql, paramMap);
    }

    private List<FieldInfo> buildFieldInfoList(SqlRowSet sqlRowSet, Map<String, String> fieldToCommentMap, Set<String> primaryKeyFieldSet, Set<String> javaTypeNames) {
        SqlRowSetMetaData metaData = sqlRowSet.getMetaData();
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        for (int column = 1; column <= metaData.getColumnCount(); column++) {
            String columnName = metaData.getColumnName(column);
            int columnType = metaData.getColumnType(column);

            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setName(columnName);
            fieldInfo.setHumpName(this.getFieldHumpName(columnName));
            fieldInfo.setComment(fieldToCommentMap.get(columnName));
            fieldInfo.setPrimaryKeyFlag(primaryKeyFieldSet.contains(columnName) ? true : false);
            fieldInfo.setJdbcTypeName(typeConverter.getJdbcTypeName(columnType));
            String javaTypeName = typeConverter.getJavaTypeName(columnType);
            fieldInfo.setJavaTypeName(javaTypeName);
            String[] javaTypeNameSplit = StringUtils.split(javaTypeName, ".");
            fieldInfo.setJavaTypeSimpleName(javaTypeNameSplit[javaTypeNameSplit.length - 1]);
            fieldInfo.setJavaFieldName(this.getJavaFieldName(columnName));
            fieldInfoList.add(fieldInfo);

            javaTypeNames.add(fieldInfo.getJavaTypeName());
        }
        return fieldInfoList;
    }

    private TableInfo mapperTableInfo(ResultSet resultSet, int rowNum) throws SQLException {
        String tableName = resultSet.getString(TABLE_NAME_FIELD);
        TableInfo tableInfo = new TableInfo();
        tableInfo.setName(tableName);
        tableInfo.setHumpName(this.getTableHumpName(tableName));
        tableInfo.setComment(resultSet.getString(TABLE_COMMENT_FIELD));
        return tableInfo;
    }

    private String getTableHumpName(String tableName) {
        for (String pre : codeGeneratorProperties.getTableNamePres()) {
            if (tableName.startsWith(pre)) {
                tableName = tableName.replaceFirst(pre, "");
                break;
            }
        }

        String[] tableNameSplit = tableName.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < tableNameSplit.length; i++) {
            stringBuilder.append(StringUtils.capitalize(StringUtils.lowerCase(tableNameSplit[i])));
        }
        return stringBuilder.toString();
    }

    private String getFieldHumpName(String columnName) {
        String[] columnNameSplit = columnName.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < columnNameSplit.length; i++) {
            stringBuilder.append(StringUtils.capitalize(StringUtils.lowerCase(columnNameSplit[i])));
        }
        return stringBuilder.toString();
    }

    private String getJavaFieldName(String columnName) {
        String[] columnNameSplit = columnName.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < columnNameSplit.length; i++) {
            if (i == 0) {
                stringBuilder.append(StringUtils.uncapitalize(StringUtils.lowerCase(columnNameSplit[i])));
            } else {
                stringBuilder.append(StringUtils.capitalize(StringUtils.lowerCase(columnNameSplit[i])));
            }
        }
        return stringBuilder.toString();
    }

}
