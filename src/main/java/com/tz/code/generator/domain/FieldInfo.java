package com.tz.code.generator.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 表的字段信息
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-11 20:39
 **/
@Setter
@Getter
public class FieldInfo {

    /**
     * 列名，示例：user_name
     */
    private String name;

    /**
     * 列名对应的驼峰名，示例：UserName
     */
    private String humpName;

    /**
     * 列注释
     */
    private String comment;

    /**
     * 主键标识，true:是主键，false:不是主键
     */
    private Boolean primaryKeyFlag;

    /**
     * jdbc类型名称
     */
    private String jdbcTypeName;

    /**
     * java类型名称，示例：java.lang.String
     */
    private String javaTypeName;

    /**
     * java类型简短名称，示例：String
     */
    private String javaTypeSimpleName;

    /**
     * java属性名称
     */
    private String javaFieldName;

}
