package com.tz.code.generator.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * 表信息
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-11 20:37
 **/
@Setter
@Getter
public class TableInfo {

    /**
     * 表名，示例：t_config_info
     */
    private String name;

    /**
     * 表名对应的驼峰名，示例：ConfigInfo
     */
    private String humpName;

    /**
     * 表注释
     */
    private String comment;

    /**
     * 列的集合
     */
    private List<FieldInfo> columnInfos;

    /**
     * java类型名称集合
     */
    private Set<String> javaTypeNames;
}
