package com.tz.code.generator.converter;


import com.tz.code.generator.config.CodeGeneratorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.sql.Types;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 类型转换器
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-11 18:31
 * @see JDBCType
 * @see org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl
 **/
@Component
public class TypeConverter {

    /**
     * jdbcType和java类型的映射关系;
     * key: jdbcType，value：java类型
     */
    private static final Map<Integer, String> JDBC_TYPE_TO_JAVA_TYPE_MAP = new HashMap<>();

    @Autowired
    private CodeGeneratorProperties codeGeneratorProperties;

    /**
     * jdbcType和java类型的映射关系参考“mybatis-generator-core”包中的{@link org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl#JavaTypeResolverDefaultImpl()}
     */
    static {
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.BIT, Boolean.class.getName());

        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.TINYINT, Integer.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.SMALLINT, Integer.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.INTEGER, Integer.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.BIGINT, Long.class.getName());

        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.FLOAT, Double.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.REAL, Float.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.DOUBLE, Double.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.NUMERIC, BigDecimal.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.DECIMAL, BigDecimal.class.getName());

        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.CHAR, String.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.VARCHAR, String.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.LONGVARCHAR, String.class.getName());

        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.DATE, Date.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.TIME, Date.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.TIMESTAMP, Date.class.getName());

        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.BINARY, "byte[]");
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.VARBINARY, "byte[]");
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.LONGVARBINARY, "byte[]");

        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.NULL, Object.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.OTHER, Object.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.JAVA_OBJECT, Object.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.DISTINCT, Object.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.STRUCT, Object.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.ARRAY, Object.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.BLOB, "byte[]");
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.CLOB, String.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.REF, Object.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.DATALINK, Object.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.BOOLEAN, Boolean.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.ROWID, null);

        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.NCHAR, String.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.NVARCHAR, String.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.LONGNVARCHAR, String.class.getName());

        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.NCLOB, String.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.SQLXML, null);
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.REF_CURSOR, null);
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.TIME_WITH_TIMEZONE, OffsetTime.class.getName());
        JDBC_TYPE_TO_JAVA_TYPE_MAP.put(Types.TIMESTAMP_WITH_TIMEZONE, OffsetDateTime.class.getName());
    }

    /***
     * 通过jdbcType得到jdbcType的名称
     * @param jdbcType
     * @return
     */
    public String getJdbcTypeName(int jdbcType) {
        return Optional.ofNullable(JDBCType.valueOf(jdbcType)).map(JDBCType::getName).orElse(JDBCType.OTHER.getName());
    }

    /**
     * 通过jdbcType得到java类型
     *
     * @param jdbcType
     * @return
     */
    public String getJavaTypeName(int jdbcType) {
        String defaultJavaType = JDBC_TYPE_TO_JAVA_TYPE_MAP.get(Integer.valueOf(jdbcType));
        return this.overrideDefaultType(defaultJavaType, jdbcType);
    }

    /**
     * 覆盖默认的类型
     *
     * @param defaultJavaType 默认的javaType
     * @param jdbcType
     * @return
     */
    private String overrideDefaultType(String defaultJavaType, int jdbcType) {
        switch (jdbcType) {
            case Types.DATE:
                defaultJavaType = this.calculateJavaType(defaultJavaType, LocalDate.class.getName());
                break;
            case Types.TIME:
                defaultJavaType = this.calculateJavaType(defaultJavaType, LocalTime.class.getName());
                break;
            case Types.TIMESTAMP:
                defaultJavaType = this.calculateJavaType(defaultJavaType, LocalDateTime.class.getName());
                break;
            default:
                break;
        }
        return defaultJavaType;
    }

    /**
     * 计算javaType
     *
     * @param defaultJavaType 默认的javaType
     * @param javaType
     * @return
     */
    private String calculateJavaType(String defaultJavaType, String javaType) {
        if (codeGeneratorProperties.getUseJSR310Types()) {
            defaultJavaType = javaType;
        }
        return defaultJavaType;
    }

}
