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
     * java仓库接口的路径
     */
    private String javaRepositoryPackage;

    /**
     * java仓库实现类的包路径
     */
    private String javaRepositoryImplPackage;

    /**
     * java模型的包
     */
    private String javaModelPackage;

    /**
     * 是否使用lombok的@Getter和@Setter注解生成getter和setter方法，true:是，false:否
     */
    private Boolean useGetSetterAnnotation;

    /**
     * 是否使用lombok的@Builder注解，true:是，false:否
     */
    private Boolean useBuilderAnnotation;

    /**
     * 是否使用lombok的@AllArgsConstructor注解，true:是，false:否
     */
    private Boolean useAllArgsConstructorAnnotation;

    /**
     * 是否使用lombok的@NoArgsConstructor注解，true:是，false:否
     */
    private Boolean useNoArgsConstructorAnnotation;

    /**
     * javaMapper的包
     */
    private String javaMapperPackage;

    /**
     * javaUdfMapper的包路径
     */
    private String javaUdfMapperPackage;

    /**
     * 表信息
     */
    private TableInfo tableInfo;
}
