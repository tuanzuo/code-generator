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
     * 列名
     */
    private String name;

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
     * java类型名称
     */
    private String javaTypeName;

    /**
     * java属性名称
     */
    private String javaFieldName;

}
