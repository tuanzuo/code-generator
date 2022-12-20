package com.tz.code.generator.config;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 代码生成器配置
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-11 18:31
 **/
@Configuration
@ConfigurationProperties(prefix = "code.gen")
@Getter
@Setter
public class CodeGeneratorProperties {

    /**
     * 生成代码的表名称集合
     */
    private List<String> tableNames = new ArrayList<>();

    /**
     * 需要去掉的表名前缀，示例：t_，tab_
     */
    private Set<String> tableNamePres = new HashSet<>();

    /**
     * java模型的路径
     */
    private String javaModelPath;

    /**
     * java模型的包路径
     */
    private String javaModelPackage;

    /**
     * java模型的模板集合
     */
    private List<String> javaModelTemplates = Lists.newArrayList("/model/po.ftl");

    /**
     * javaMapper的路径
     */
    private String javaMapperPath;

    /**
     * javaMapper的包路径
     */
    private String javaMapperPackage;

    /**
     * javaMapper的模板集合
     */
    private List<String> javaMapperTemplates = Lists.newArrayList("/mapper/BaseMapper.ftl", "/mapper/UdfMapper.ftl");

    /**
     * xml的路径
     */
    private String mapperXmlPath;

    /**
     * xml的包路径
     */
    private String mapperXmlPackage;

    /**
     * xml的模板集合
     */
    private List<String> mapperXmlTemplates = Lists.newArrayList("/xml/BaseMapperXml.ftl", "/xml/UdfMapperXml.ftl");

    /**
     * 属性文档：http://mybatis.org/generator/configreference/javaTypeResolver.html
     */
    private Boolean useJSR310Types = false;

    /**
     * 代码作者
     */
    private String author = "tuanzuo code generator";

}
