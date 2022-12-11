package com.tz.code.generator.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
     * 表名
     */
    private String name;

    /**
     * 表名对应的驼峰名
     */
    private String humpName;

    /**
     * 表注释
     */
    private String comment;

    /**
     * 列的集合
     */
    private List<FieldInfo> columnInfoList;

}
