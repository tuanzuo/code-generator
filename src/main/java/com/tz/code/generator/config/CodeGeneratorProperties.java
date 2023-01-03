package com.tz.code.generator.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
     * mysql关键字
     */
    private Set<String> mysqlKeyWords = Sets.newHashSet("name", "status", "type", "key", "value", "code", "valid");

    /**
     * 是否生成所有表，true:是，false:否
     */
    private Boolean allTableFlag = true;

    /**
     * 生成代码的表名称集合
     */
    private List<String> tableNames = new ArrayList<>();

    /**
     * 需要去掉的表名前缀，示例：t_,tab_,m_
     */
    private Set<String> tableNamePres = new HashSet<>();

    /**
     * 需要去掉的表名后缀，示例：_0,_1,_2022,_2023
     */
    private Set<String> tableNameSufs = new HashSet<>();

    /**
     * java仓库接口的路径
     */
    private String javaRepositoryPath;

    /**
     * java仓库实现类的路径
     */
    private String javaRepositoryImplPath;

    /**
     * java仓库接口的包路径
     */
    private String javaRepositoryPackage;

    /**
     * java仓库实现类的包路径
     */
    private String javaRepositoryImplPackage;

    /**
     * java仓库的模板集合
     */
    private List<String> javaRepositoryTemplates = Lists.newArrayList("/repository/IRepository.ftl", "/repository/RepositoryImpl.ftl");

    /**
     * java模型的路径
     */
    private String javaModelPath;

    /**
     * java模型的包路径
     */
    private String javaModelPackage;

    /**
     * 是否使用lombok的@Getter和@Setter注解生成getter和setter方法，true:是，false:否
     */
    private Boolean useGetSetterAnnotation = false;

    /**
     * 是否使用lombok的@Builder注解，true:是，false:否
     */
    private Boolean useBuilderAnnotation = false;

    /**
     * 是否使用lombok的@AllArgsConstructor注解，true:是，false:否
     */
    private Boolean useAllArgsConstructorAnnotation = false;

    /**
     * 是否使用lombok的@NoArgsConstructor注解，true:是，false:否
     */
    private Boolean useNoArgsConstructorAnnotation = false;

    /**
     * java模型的模板集合
     */
    private List<String> javaModelTemplates = Lists.newArrayList("/model/po.ftl");

    /**
     * javaMapper的路径
     */
    private String javaMapperPath;

    /**
     * javaUdfMapper的路径
     */
    private String javaUdfMapperPath;

    /**
     * javaMapper的包路径
     */
    private String javaMapperPackage;

    /**
     * javaUdfMapper的包路径
     */
    private String javaUdfMapperPackage;

    /**
     * javaMapper的模板集合
     */
    private List<String> javaMapperTemplates = Lists.newArrayList("/mapper/BaseMapper.ftl", "/mapper/UdfMapper.ftl");

    /**
     * xml的路径
     */
    private String mapperXmlPath;

    /**
     * udf xml的路径
     */
    private String mapperUdfXmlPath;

    /**
     * xml的包路径
     */
    private String mapperXmlPackage;

    /**
     * udf xml的包路径
     */
    private String mapperUdfXmlPackage;

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
