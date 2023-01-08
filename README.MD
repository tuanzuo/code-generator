## 代码生成器

### 功能介绍

- 支持生成xml，mapper，po，repository
- 用于替代[MyBatis Generator](http://mybatis.org/generator/index.html)

### 如何使用

- 修改config/application.properties中的数据库配置
- 运行com.tz.code.generator.CodeGeneratorApplication#main方法即可生成代码

### 配置说明

| 配置项                                   | 说明                                                         | 是否必须 | 类型    | 默认值                                                     | 配置值示例                                                 |
| ---------------------------------------- | ------------------------------------------------------------ | -------- | ------- | ---------------------------------------------------------- | ---------------------------------------------------------- |
| code.gen.mysqlKeyWords                   | mysql关键字配置，当表的字段存在于该配置中时生成xml的sql中的字段会自动加上\`\`，例如status会变成\`status\` | 否       | Set     | name,status,type,key,value,code,valid                      | name,status,type,key,value,code,valid                      |
| code.gen.allTableFlag                    | 是否对数据库中的所有表都生成代码文件                         | 否       | Boolean | true                                                       | true                                                       |
| code.gen.tableNames                      | 指定需要生成代码文件的表名称集合；当code.gen.allTableFlag=true并且该配置项未配置时则会对数据库中的所有表都生成代码文件 | 否       | List    |                                                            | t_config,t_user                                            |
| code.gen.removeTableNamePres             | 生成代码文件和类名称时需要去掉的表名前缀                     | 否       | Set     |                                                            | t\_,tab\_,m\_                                              |
| code.gen.removeTableNameSufs             | 生成代码文件和类名称时需要去掉的表名后缀                     | 否       | Set     |                                                            | \_0,\_1,\_2022,\_2023                                      |
| code.gen.javaRepositoryPath              | java仓库接口的路径                                           | 是       | String  |                                                            | D:\\gendercode                                             |
| code.gen.javaRepositoryImplPath          | java仓库实现类的路径                                         | 是       | String  |                                                            | D:\\gendercode                                             |
| code.gen.javaRepositoryPackage           | java仓库接口的包路径                                         | 是       | String  |                                                            | com.tz.gen.code.repository                                 |
| code.gen.javaRepositoryImplPackage       | java仓库实现类的包路径                                       | 是       | String  |                                                            | com.tz.gen.code.repository.impl                            |
| code.gen.javaRepositoryTemplates         | java仓库的模板集合                                           | 否       | List    | /repository/IRepository.ftl,/repository/RepositoryImpl.ftl | /repository/IRepository.ftl,/repository/RepositoryImpl.ftl |
| code.gen.javaModelPath                   | java模型(PO)的路径                                           | 是       | String  |                                                            | D:\\gendercode                                             |
| code.gen.javaModelPackage                | java模型(PO)的包路径                                         | 是       | String  |                                                            | com.tz.gen.code.po                                         |
| code.gen.useGetSetterAnnotation          | java的PO是否使用lombok的@Getter和@Setter注解                 | 否       | Boolean | false                                                      | false                                                      |
| code.gen.useBuilderAnnotation            | java的PO是否使用lombok的@Builder注解                         | 否       | Boolean | false                                                      | false                                                      |
| code.gen.useAllArgsConstructorAnnotation | java的PO是否使用lombok的@AllArgsConstructor注解              | 否       | Boolean | false                                                      | false                                                      |
| code.gen.useNoArgsConstructorAnnotation  | java的PO是否使用lombok的@NoArgsConstructor注解               | 否       | Boolean | false                                                      | false                                                      |
| code.gen.javaModelTemplates              | java模型的模板集合                                           | 否       | List    | /model/po.ftl                                              | /model/po.ftl                                              |
| code.gen.javaMapperPath                  | java Mapper的路径                                            | 是       | String  |                                                            | D:\\gendercode                                             |
| code.gen.javaUdfMapperPath               | java UdfMapper的路径                                         | 是       | String  |                                                            | D:\\gendercode                                             |
| code.gen.javaMapperPackage               | java Mapper的包路径                                          | 是       | String  |                                                            | com.tz.gen.code.mapper.base                                |
| code.gen.javaUdfMapperPackage            | java UdfMapper的包路径                                       | 是       | String  |                                                            | com.tz.gen.code.mapper.udf                                 |
| code.gen.javaMapperTemplates             | java Mapper的模板集合                                        | 否       | List    | /mapper/BaseMapper.ftl,/mapper/UdfMapper.ftl               | /mapper/BaseMapper.ftl,/mapper/UdfMapper.ftl               |
| code.gen.mapperXmlPath                   | xml的路径                                                    | 是       | String  |                                                            | D:\\gendercode                                             |
| code.gen.mapperUdfXmlPath                | udf xml的路径                                                | 是       | String  |                                                            | D:\\gendercode                                             |
| code.gen.mapperXmlPackage                | xml的包路径                                                  | 是       | String  |                                                            | xml.base                                                   |
| code.gen.mapperUdfXmlPackage             | udf xml的包路径                                              | 是       | String  |                                                            | xml.udf                                                    |
| code.gen.mapperXmlRemoveTableNamePres    | 生成的xml中对应sql中的表名称需要去掉的表名前缀               | 否       | Set     |                                                            | t\_,tab\_,m\_                                              |
| code.gen.mapperXmlRemoveTableNameSufs    | 生成的xml中对应sql中的表名称需要去掉的表名后缀               | 否       | Set     |                                                            | \_0,\_1,\_2022,\_2023                                      |
| code.gen.mapperXmlTemplates              | xml的模板集合                                                | 否       | List    | /xml/BaseMapperXml.ftl,/xml/UdfMapperXml.ftl               | /xml/BaseMapperXml.ftl,/xml/UdfMapperXml.ftl               |
| code.gen.useJSR310Types                  | [属性文档](http://mybatis.org/generator/configreference/javaTypeResolver.html) | 否       | Boolean | false                                                      | false                                                      |
| code.gen.author                          | 代码作者                                                     | 否       | String  | code generator                                             | tuanzuo                                                    |

### 背景

在使用[MyBatis Generator](http://mybatis.org/generator/index.html)生成代码时遇到了以下一些问题，所以自定义一个代码生成器来解决这些问题，从而替代[MyBatis Generator](http://mybatis.org/generator/index.html)用于代码的生成。

- 生成的代码文件中有一堆无用的注释，看着不简洁清晰

  ```java
  /**
   *
   * This class was generated by MyBatis Generator.
   * This class corresponds to the database table t_config
   *
   * @mbg.generated do_not_delete_during_merge Sun Jan 08 15:31:44 CST 2023
   */
  public class ConfigPO {
      /**
       * Database Column Remarks:
       *   主键
       *
       * This field was generated by MyBatis Generator.
       * This field corresponds to the database column t_config.id
       *
       * @mbg.generated Sun Jan 08 15:31:44 CST 2023
       */
      private Long id;
  
      /**
       * Database Column Remarks:
       *   服务名
       *
       * This field was generated by MyBatis Generator.
       * This field corresponds to the database column t_config.service_name
       *
       * @mbg.generated Sun Jan 08 15:31:44 CST 2023
       */
      private String serviceName;
      
      //其他代码省略
  }
  ```

- 表中tinyint类型的字段生成的PO的属性对应的是Byte的类型，不方便和Integer类型的数据进行比较或者设置等操作

  ```sql
  CREATE TABLE `t_config` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `config_type` tinyint(2) NOT NULL COMMENT '类型[10=生效缓存配置,20=失效缓存配置]',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='配置表';
  ```

  ```java
  public class ConfigPO {
  	/**
       * Database Column Remarks:
       *   主键
       *
       * This field was generated by MyBatis Generator.
       * This field corresponds to the database column t_config_test_1.id
       *
       * @mbg.generated Sun Jan 08 15:50:31 CST 2023
       */
      private Long id;
      
      /**
       * Database Column Remarks:
       *   类型[10=生效缓存配置,20=失效缓存配置]
       *
       * This field was generated by MyBatis Generator.
       * This field corresponds to the database column t_config.config_type
       *
       * @mbg.generated Sun Jan 08 15:50:31 CST 2023
       */
      private Byte configType;
  }
  ```

- 生成的代码文件是通过在代码中进行拼接实现，修改起来不直观和方便

  例如生成selectByPrimaryKey方法的sql代码类：

  ```java
  // org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByPrimaryKeyElementGenerator
  public class SelectByPrimaryKeyElementGenerator extends
          AbstractXmlElementGenerator {
  
      public SelectByPrimaryKeyElementGenerator() {
          super();
      }
  
      @Override
      public void addElements(XmlElement parentElement) {
          XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
  
          answer.addAttribute(new Attribute(
                  "id", introspectedTable.getSelectByPrimaryKeyStatementId())); //$NON-NLS-1$
          if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
              answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                      introspectedTable.getResultMapWithBLOBsId()));
          } else {
              answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                      introspectedTable.getBaseResultMapId()));
          }
  
          String parameterType;
          if (introspectedTable.getRules().generatePrimaryKeyClass()) {
              parameterType = introspectedTable.getPrimaryKeyType();
          } else {
              // PK fields are in the base class. If more than on PK
              // field, then they are coming in a map.
              if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
                  parameterType = "map"; //$NON-NLS-1$
              } else {
                  parameterType = introspectedTable.getPrimaryKeyColumns().get(0)
                          .getFullyQualifiedJavaType().toString();
              }
          }
  
          answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                  parameterType));
  
          context.getCommentGenerator().addComment(answer);
  
          StringBuilder sb = new StringBuilder();
          sb.append("select "); //$NON-NLS-1$
  
          if (stringHasValue(introspectedTable
                  .getSelectByPrimaryKeyQueryId())) {
              sb.append('\'');
              sb.append(introspectedTable.getSelectByPrimaryKeyQueryId());
              sb.append("' as QUERYID,"); //$NON-NLS-1$
          }
          answer.addElement(new TextElement(sb.toString()));
          answer.addElement(getBaseColumnListElement());
          if (introspectedTable.hasBLOBColumns()) {
              answer.addElement(new TextElement(",")); //$NON-NLS-1$
              answer.addElement(getBlobColumnListElement());
          }
  
          sb.setLength(0);
          sb.append("from "); //$NON-NLS-1$
          sb.append(introspectedTable
                  .getAliasedFullyQualifiedTableNameAtRuntime());
          answer.addElement(new TextElement(sb.toString()));
  
          boolean and = false;
          for (IntrospectedColumn introspectedColumn : introspectedTable
                  .getPrimaryKeyColumns()) {
              sb.setLength(0);
              if (and) {
                  sb.append("  and "); //$NON-NLS-1$
              } else {
                  sb.append("where "); //$NON-NLS-1$
                  and = true;
              }
  
              sb.append(MyBatis3FormattingUtilities
                      .getAliasedEscapedColumnName(introspectedColumn));
              sb.append(" = "); //$NON-NLS-1$
              sb.append(MyBatis3FormattingUtilities
                      .getParameterClause(introspectedColumn));
              answer.addElement(new TextElement(sb.toString()));
          }
  
          if (context.getPlugins()
                  .sqlMapSelectByPrimaryKeyElementGenerated(answer,
                          introspectedTable)) {
              parentElement.addElement(answer);
          }
      }
  }
  ```
