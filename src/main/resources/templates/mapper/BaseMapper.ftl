<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${objectName}XML" >
    <resultMap id="${objectName}Map" type="com.${companyName}.model.${model}.${objectName}" >
    <#if columnFieldList??>
    <#list columnFieldList as item>
        <#if item.column=='${primaryKeys[0]}'><id column="${item.column}" property="${item.field}" jdbcType="${item.jdbcType}" /></#if>
        <#if item.column!='${primaryKeys[0]}'><result column="${item.column}" property="${item.field}" jdbcType="${item.jdbcType}" /></#if>
    </#list>
    </#if>
    </resultMap>
    
    <sql id="Base_Column_List" >
        <#if columnFieldList??>
        <#list columnFieldList as item>
        ${item.column}<#if item_has_next>,</#if>
        </#list>
        </#if>
    </sql>
    
    <sql id="whereCommon">
        <#if columnFieldList??>
        <#list columnFieldList as item>
        <if test="${item.field} != null<#if (item.jdbcType)!='INTEGER' && (item.jdbcType)!='DOUBLE' && (item.jdbcType)!='FLOAT'> and ${item.field} != ''</#if>">
        and ${item.column} = ${"#{"}${item.field},jdbcType=${item.jdbcType}${"}"}
        </if>
		</#list>
    	</#if>
    </sql>
    <sql id="where">
        <include refid="whereCommon"/>
    </sql>
    <sql id="whereForLike">
        <include refid="whereCommon"/>
        <#noparse>
        <!-- <if test="title != null and title != ''">
        and title like CONCAT(#{title,jdbcType=VARCHAR},'%')
        </if> -->
        </#noparse>
    </sql>
    
    <#assign primaryKeyTypeJava="String"/> 
    <#assign primaryKeyTypeJdbc="VARCHAR"/>
	<#if columnFieldList??>  
		<#list columnFieldList as item>
			<#if item.column=='${primaryKeys[0]}' && item.jdbcType=='INTEGER'>
				<#assign primaryKeyTypeJava="Integer"/>
				<#assign primaryKeyTypeJdbc="INTEGER"/>
			</#if>
		</#list>
    </#if>
    <select id="get${objectName}By${primaryKeyMerger}" resultMap="${objectName}Map" parameterType="java.lang.${primaryKeyTypeJava}" >
        select 
        <include refid="Base_Column_List" />
        from ${tableName}
        where ${primaryKeys[0]} = ${"#{"}${primaryKeysConvert[0]},jdbcType=${primaryKeyTypeJdbc}${"}"}
    </select>
    
    <select id="get${objectName}By${primaryKeyMerger}s" resultMap="${objectName}Map" parameterType="java.lang.String" >
        select 
        <include refid="Base_Column_List" />
        from ${tableName}
        where ${primaryKeys[0]} in(${"$"+"{"}${primaryKeysConvert[0]}s${"}"}) 
    </select>
    
    <select id="get${objectName}By${primaryKeyMerger}sList" resultMap="${objectName}Map" parameterType="java.util.List" >
        select 
        <include refid="Base_Column_List" />
        from ${tableName}
        where ${primaryKeys[0]} in
        <foreach collection="list" item="item" index="index" open="(" separator="," close=")" >  
	        ${"#{"}item${"}"}
	    </foreach>
    </select>
    
    <!--没有进行模糊查询-->
    <select id="getAll" parameterType="com.${companyName}.model.${model}.${objectName}" resultMap="${objectName}Map">
        select * from ${tableName} where 1=1 
        <include refid="where" />
    </select>

	<!--没有进行模糊的多表关联查询-->
	<select id="getAllForUnion" parameterType="com.${companyName}.model.${model}.${objectName}" resultMap="${objectName}Map">
        <!--根据业务需要自己实现，例如：
        select ware_ware.*,b.cn_name,c.cat_name from ware_ware 
        INNER JOIN brand b on b.id=ware_ware.brand_id
        INNER JOIN category c on c.cat_id=ware_ware.category_id
        where 1=1 
        <include refid="where" />
        -->
    </select>
    
    <!--模糊查询-->
    <select id="getAllForLike" parameterType="com.${companyName}.model.${model}.${objectName}" resultMap="${objectName}Map">
        select * from ${tableName} where 1=1 
        <include refid="whereForLike" />
    </select>
    
    <#noparse>
    <!--通过编码查询邮编:返回Map
    <select id="getPostCodeByAreaCode" parameterType="java.util.List" resultMap="AreaMap">
		select code,post
		from tbl_cms_area 
		where code in 
		<foreach collection="array" item="item" index="index" open="(" separator="," close=")" >  
	        #{item}
	    </foreach>
	</select>
	-->
	</#noparse>
    
	<!--分页查询-->
    <select id="getPagerModelByQuery" parameterType="com.${companyName}.model.${model}.${objectName}" resultMap="${objectName}Map">
        select * from ${tableName} where 1=1 
        <include refid="where" />
    </select>

	<!--分页模糊查询-->
	<select id="getPagerModelByQueryForLike" parameterType="com.${companyName}.model.${model}.${objectName}" resultMap="${objectName}Map">
        select * from ${tableName} where 1=1 
        <include refid="whereForLike" />
    </select>
    
    <!-- 查询个数 -->
    <select id="queryCount" parameterType="com.${companyName}.model.${model}.${objectName}" resultType="int">
        select count(1) from ${tableName} where 1=1 
        <include refid="where" />
    </select>
   
    <insert id="insert${objectName}" parameterType="com.${companyName}.model.${model}.${objectName}" >
        insert into ${tableName} (<include refid="Base_Column_List" />)
        values (
        <#if columnFieldList??>
        <#list columnFieldList as item>
        ${"#{"}${item.field},jdbcType=${item.jdbcType}${"}"}<#if item_has_next>,</#if>
        </#list>
        </#if>)
    </insert>
    
    <insert id="insert${objectName}Batch" parameterType="java.util.List">
        insert into ${tableName} (<include refid="Base_Column_List" />)
        values 
        <foreach collection="list" item="item" index="index" separator="," >  
	        (
	        <#if columnFieldList??>
	        <#list columnFieldList as item>
	        ${"#{"}item.${item.field},jdbcType=${item.jdbcType}${"}"}<#if item_has_next>,</#if>
	        </#list>
	        </#if>)
        </foreach>
    </insert>
    
    <delete id="del${objectName}By${primaryKeyMerger}" parameterType="java.lang.${primaryKeyTypeJava}" >
        delete from ${tableName}
        where ${primaryKeys[0]} = ${"#{"}${primaryKeysConvert[0]},jdbcType=${primaryKeyTypeJdbc}${"}"}
    </delete>
    
    <delete id="del${objectName}By${primaryKeyMerger}s" parameterType="java.lang.String" >
        delete from ${tableName}
        where ${primaryKeys[0]} in(${"$"+"{"}${primaryKeysConvert[0]}s${"}"}) 
    </delete>
    
    <delete id="del${objectName}By${primaryKeyMerger}sList" parameterType="java.util.List" >
        delete from ${tableName}
        where ${primaryKeys[0]} in
        <foreach collection="list" item="item" index="index" open="(" separator="," close=")" >  
	        ${"#{"}item${"}"}
	    </foreach>
    </delete>
    
    <update id="update${objectName}" parameterType="com.${companyName}.model.${model}.${objectName}" >
        update ${tableName}
        <set>
      <#if columnFieldList??>
      <#list columnFieldList as item>
            <if test="${item.field} != null" >
                ${item.column} = ${"#{"}${item.field},jdbcType=${item.jdbcType}${"}"}<#if item_has_next>,</#if>
            </if>
      </#list>
      </#if>
        </set>
        where ${primaryKeys[0]} = ${"#{"}${primaryKeysConvert[0]},jdbcType=${primaryKeyTypeJdbc}${"}"}
    </update>
    
    <update id="update${objectName}By${primaryKeyMerger}s" parameterType="java.util.Map">
        update ${tableName}
        <set>
      <#if columnFieldList??>
      <#list columnFieldList as item>
            <if test="${fLowerObjectName}.${item.field} != null" >
                ${item.column} = ${"#{"}${fLowerObjectName}.${item.field},jdbcType=${item.jdbcType}${"}"}<#if item_has_next>,</#if>
            </if>
      </#list>
      </#if>
        </set>
        where ${primaryKeys[0]} in(${"$"+"{"}${primaryKeysConvert[0]}s${"}"}) 
    </update>
    
    <update id="update${objectName}By${primaryKeyMerger}sList" parameterType="java.util.Map">
        update ${tableName}
        <set>
      <#if columnFieldList??>
      <#list columnFieldList as item>
            <if test="${fLowerObjectName}.${item.field} != null" >
                ${item.column} = ${"#{"}${fLowerObjectName}.${item.field},jdbcType=${item.jdbcType}${"}"}<#if item_has_next>,</#if>
            </if>
      </#list>
      </#if>
        </set>
        where ${primaryKeys[0]} in
        <foreach collection="${primaryKeysConvert[0]}s" item="item" index="index" open="(" separator="," close=")" >  
	        ${"#{"}item${"}"}
	    </foreach>
    </update>
    
    <update id="update${objectName}List" parameterType="java.util.List">
    	<foreach collection="list" item="item" index="index" open="" close=";" separator=";">
		        update ${tableName}
		        <set>
		      <#if columnFieldList??>
		      <#list columnFieldList as item>
		            <if test="item.${item.field} != null" >
		                ${item.column} = ${"#{"}item.${item.field},jdbcType=${item.jdbcType}${"}"}<#if item_has_next>,</#if>
		            </if>
		      </#list>
		      </#if>
		        </set>
		        where ${primaryKeys[0]} = ${"#{"}item.${primaryKeysConvert[0]},jdbcType=${primaryKeyTypeJdbc}${"}"}
	     </foreach>
    </update>
    
</mapper>