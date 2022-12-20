package com.tz.code.generator.domain;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

/**
 * 模板对应的模型信息
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-20 20:46
 **/
@Setter
@Getter
public class TemplateModelInfo {

    /**
     * 代码作者
     */
    private String author;

    /**
     * 代码生成时间
     */
    private String time = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");

    /**
     * java模型的包
     */
    private String javaModelPackage;

    /**
     * javaMapper的包
     */
    private String javaMapperPackage;

    /**
     * 表信息
     */
    private TableInfo tableInfo;
}
