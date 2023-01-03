package com.tz.code.generator.runner;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.collect.Sets;
import com.tz.code.generator.config.CodeGeneratorProperties;
import com.tz.code.generator.constant.CodeGenConstant;
import com.tz.code.generator.converter.TypeConverter;
import com.tz.code.generator.domain.FieldInfo;
import com.tz.code.generator.domain.GenCodeFileContext;
import com.tz.code.generator.domain.TableInfo;
import com.tz.code.generator.domain.TemplateModelInfo;
import com.tz.code.generator.gencode.IGenCodeFile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(CodeGeneratorRunner.class);

    //sql中的参数
    private static final String REPLCE_TABLE_NAME_PARAM = "${tableNameParam}";
    private static final String DB_NAME_PARAM = "dbName";
    private static final String TABLE_NAME_PARAM = "tableName";

    //sql中的字段名称
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
     * 数据库下的所有表
     */
    private static final String SQL_DB_TABLE = "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = :dbName";
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

    /**
     * 数据库名称
     */
    @Value("${db.name}")
    private String dbName;

    @Autowired
    private CodeGeneratorProperties codeGeneratorProperties;
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private TypeConverter typeConverter;
    @Autowired
    private List<IGenCodeFile> genCodeFiles;

    @Override
    public void run(String... args) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //1、设置表的名称
        this.setTableNames();
        for (String tableName : codeGeneratorProperties.getTableNames()) {
            //2.1、获取模板model数据
            TemplateModelInfo templateModelInfo = this.getModelInfo(tableName);
            //2.2、生成代码文件
            GenCodeFileContext context = GenCodeFileContext.builder().templateModelInfo(templateModelInfo).build();
            genCodeFiles.forEach(temp -> temp.genCodeFile(context));
        }
        stopWatch.stop();
        LOGGER.info("[生成代码] [完成] 数据库：{}，表：{}个，耗时：{}s", dbName, codeGeneratorProperties.getTableNames().size(), stopWatch.getTotalTimeSeconds());
    }

    /**
     * 设置表名称列表
     */
    private void setTableNames() {
        if (codeGeneratorProperties.getAllTableFlag() && CollectionUtils.isEmpty(codeGeneratorProperties.getTableNames())) {
            //查询数据库中的所有表名称
            Map<String, String> paramMap = new HashMap<>(1);
            paramMap.put(DB_NAME_PARAM, dbName);
            List<String> allTableNames = namedParameterJdbcTemplate.query(SQL_DB_TABLE, paramMap, (rs, rowNum) -> rs.getString(TABLE_NAME_FIELD));
            //重新设置表名称
            codeGeneratorProperties.setTableNames(Sets.newHashSet(Optional.ofNullable(allTableNames).orElse(new ArrayList<>())));
            LOGGER.info("[生成代码] [未配置生成表的名称-tableNames参数] [已开启生成所有表的配置] 数据库：{}，所有表：{}个，{}", dbName, allTableNames.size(), JSONUtils.toJSONString(allTableNames));
        }
    }

    /**
     * 得到模板对应的模型信息
     *
     * @param tableName 表名称
     * @return
     */
    private TemplateModelInfo getModelInfo(String tableName) {
        Map<String, String> paramMap = new HashMap<>(2);
        //1、查询表信息
        TableInfo tableInfo = this.queryTableInfo(tableName, paramMap);
        //2、查询表的元数据信息
        SqlRowSetMetaData metaData = this.queryTableMetaData(paramMap);
        //3、查询表的字段信息
        List<Map<String, Object>> columnList = this.queryColumnList(paramMap);
        //java类型名称集合
        Set<String> javaTypeNames = new HashSet<>();
        //4、构建字段信息
        List<FieldInfo> fieldInfos = this.buildFieldInfoList(columnList, metaData, javaTypeNames);
        //5、设置表信息
        tableInfo.setColumnInfos(fieldInfos);
        tableInfo.setJavaTypeNames(javaTypeNames);
        //6、构建模板对应的模型信息
        TemplateModelInfo templateModelInfo = this.buildTemplateModelInfo(tableInfo);
        return templateModelInfo;
    }

    /**
     * 构建模板对应的模型信息
     *
     * @param tableInfo 表信息
     * @return
     */
    private TemplateModelInfo buildTemplateModelInfo(TableInfo tableInfo) {
        TemplateModelInfo templateModelInfo = new TemplateModelInfo();
        templateModelInfo.setAuthor(codeGeneratorProperties.getAuthor());
        templateModelInfo.setJavaRepositoryPackage(codeGeneratorProperties.getJavaRepositoryPackage());
        templateModelInfo.setJavaRepositoryImplPackage(codeGeneratorProperties.getJavaRepositoryImplPackage());
        templateModelInfo.setJavaModelPackage(codeGeneratorProperties.getJavaModelPackage());
        templateModelInfo.setUseGetSetterAnnotation(codeGeneratorProperties.getUseGetSetterAnnotation());
        templateModelInfo.setUseBuilderAnnotation(codeGeneratorProperties.getUseBuilderAnnotation());
        templateModelInfo.setUseAllArgsConstructorAnnotation(codeGeneratorProperties.getUseAllArgsConstructorAnnotation());
        templateModelInfo.setUseNoArgsConstructorAnnotation(codeGeneratorProperties.getUseNoArgsConstructorAnnotation());
        templateModelInfo.setJavaMapperPackage(codeGeneratorProperties.getJavaMapperPackage());
        templateModelInfo.setJavaUdfMapperPackage(codeGeneratorProperties.getJavaUdfMapperPackage());
        templateModelInfo.setTableInfo(tableInfo);
        return templateModelInfo;
    }

    /**
     * 查询表信息
     *
     * @param tableName 表名
     * @param paramMap  参数
     * @return
     */
    private TableInfo queryTableInfo(String tableName, Map<String, String> paramMap) {
        paramMap.put(DB_NAME_PARAM, dbName);
        paramMap.put(TABLE_NAME_PARAM, tableName);
        return namedParameterJdbcTemplate.queryForObject(SQL_TABLE, paramMap, (resultSet, rowNum) -> this.mapperTableInfo(resultSet, rowNum));
    }

    /**
     * 查询表的字段信息
     *
     * @param paramMap 参数
     * @return
     */
    private List<Map<String, Object>> queryColumnList(Map<String, String> paramMap) {
        return namedParameterJdbcTemplate.queryForList(SQL_TABLE_COLUMN, paramMap);
    }

    /**
     * 查询表的元数据信息
     *
     * @param paramMap 参数
     * @return
     */
    private SqlRowSetMetaData queryTableMetaData(Map<String, String> paramMap) {
        String querySql = SQL_TABLE_INFO.replace(REPLCE_TABLE_NAME_PARAM, paramMap.get(TABLE_NAME_PARAM));
        SqlRowSet sqlRowSet = namedParameterJdbcTemplate.queryForRowSet(querySql, paramMap);
        return sqlRowSet.getMetaData();
    }

    /**
     * 构建字段信息
     *
     * @param columnList    字段列表
     * @param metaData      表的元数据信息
     * @param javaTypeNames java类型名称集合
     * @return
     */
    private List<FieldInfo> buildFieldInfoList(List<Map<String, Object>> columnList, SqlRowSetMetaData metaData, Set<String> javaTypeNames) {
        //字段和注释的对应Map，key:字段名，value:字段对应的注释
        Map<String, String> fieldToCommentMap = new HashMap<>();
        //主键集合
        Set<String> primaryKeyFieldSet = new HashSet<>();
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

        List<FieldInfo> fieldInfoList = new ArrayList<>();
        Set<String> mysqlKeyWords = Optional.ofNullable(codeGeneratorProperties.getMysqlKeyWords()).orElse(new HashSet<>());
        for (int column = 1; column <= metaData.getColumnCount(); column++) {
            String columnName = metaData.getColumnName(column);
            int columnType = metaData.getColumnType(column);
            String convertName = columnName;
            if (mysqlKeyWords.contains(StringUtils.lowerCase(columnName))) {
                convertName = StringUtils.join(CodeGenConstant.SYMBOL_OBLIQUE_SINGLE_QUOTES, convertName, CodeGenConstant.SYMBOL_OBLIQUE_SINGLE_QUOTES);
            }

            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setName(columnName);
            fieldInfo.setConvertName(convertName);
            fieldInfo.setHumpName(this.getFieldHumpName(columnName));
            fieldInfo.setComment(fieldToCommentMap.get(columnName));
            fieldInfo.setPrimaryKeyFlag(primaryKeyFieldSet.contains(columnName) ? true : false);
            fieldInfo.setJdbcTypeName(typeConverter.getJdbcTypeName(columnType));
            String javaTypeName = typeConverter.getJavaTypeName(columnType);
            fieldInfo.setJavaTypeName(javaTypeName);
            String[] javaTypeNameSplit = StringUtils.split(javaTypeName, CodeGenConstant.SYMBOL_SPOT);
            fieldInfo.setJavaTypeSimpleName(javaTypeNameSplit[javaTypeNameSplit.length - 1]);
            fieldInfo.setJavaFieldName(this.getJavaFieldName(columnName));
            fieldInfoList.add(fieldInfo);

            javaTypeNames.add(fieldInfo.getJavaTypeName());
        }
        return fieldInfoList;
    }

    /**
     * 映射表信息
     *
     * @param resultSet
     * @param rowNum
     * @return
     * @throws SQLException
     */
    private TableInfo mapperTableInfo(ResultSet resultSet, int rowNum) throws SQLException {
        String tableName = resultSet.getString(TABLE_NAME_FIELD);
        TableInfo tableInfo = new TableInfo();
        tableInfo.setName(tableName);
        tableInfo.setHumpName(this.getTableHumpName(tableName));
        tableInfo.setUncapitalizeName(StringUtils.uncapitalize(tableInfo.getHumpName()));
        tableInfo.setComment(resultSet.getString(TABLE_COMMENT_FIELD));
        return tableInfo;
    }

    /**
     * 得到表名对应的驼峰名
     *
     * @param tableName 表名称
     * @return
     */
    private String getTableHumpName(String tableName) {
        for (String pre : Optional.ofNullable(codeGeneratorProperties.getTableNamePres()).orElse(new HashSet<>())) {
            if (StringUtils.startsWithIgnoreCase(tableName, pre)) {
                tableName = StringUtils.removeStartIgnoreCase(tableName, pre);
                break;
            }
        }
        for (String suf : Optional.ofNullable(codeGeneratorProperties.getTableNameSufs()).orElse(new HashSet<>())) {
            if (StringUtils.endsWithIgnoreCase(tableName, suf)) {
                tableName = StringUtils.removeEndIgnoreCase(tableName, suf);
                break;
            }
        }
        return this.getHumpName(tableName);
    }

    /**
     * 得到字段对应的驼峰名
     *
     * @param columnName 字段名称
     * @return
     */
    private String getFieldHumpName(String columnName) {
        return this.getHumpName(columnName);
    }

    /**
     * 得到驼峰名
     *
     * @param name 名称
     * @return
     */
    private String getHumpName(String name) {
        String[] nameSplit = name.split(CodeGenConstant.SYMBOL_UNDERLINE);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < nameSplit.length; i++) {
            stringBuilder.append(StringUtils.capitalize(StringUtils.lowerCase(nameSplit[i])));
        }
        return stringBuilder.toString();
    }

    /**
     * 得到java属性名称
     *
     * @param columnName 字段名称
     * @return
     */
    private String getJavaFieldName(String columnName) {
        String[] columnNameSplit = columnName.split(CodeGenConstant.SYMBOL_UNDERLINE);
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
