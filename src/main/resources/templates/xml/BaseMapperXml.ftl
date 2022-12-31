<#include "/common/common.ftl" />
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${javaMapperPackage}.${tableInfo.humpName}Mapper">
    <resultMap id="BaseResultMap" type="${po}">
    <#if tableInfo.columnInfos??>
    <#list tableInfo.columnInfos as item>
        <#if item.primaryKeyFlag>
        <id column="${item.name}" property="${item.javaFieldName}" jdbcType="${item.jdbcTypeName}"/>
        </#if>
        <#if !item.primaryKeyFlag>
        <result column="${item.name}" property="${item.javaFieldName}" jdbcType="${item.jdbcTypeName}"/>
        </#if>
    </#list>
    </#if>
    </resultMap>

    <sql id="Base_Column_List">
        <#if tableInfo.columnInfos??>
        <#list tableInfo.columnInfos as item>
        ${item.convertName}<#if item_has_next>,</#if>
        </#list>
        </#if>
    </sql>

    <sql id="whereCommon">
        <#if tableInfo.columnInfos??>
        <#list tableInfo.columnInfos as item>
        <if test="${item.javaFieldName} != null<#if (item.javaTypeName)=='java.lang.String'> and ${item.javaFieldName} != ''</#if>">
            and ${item.convertName} = ${"#{"}${item.javaFieldName},jdbcType=${item.jdbcTypeName}${"}"}
        </if>
		</#list>
    	</#if>
    </sql>
    <sql id="where">
        <include refid="whereCommon"/>
    </sql>

    <select id="selectByPrimaryKey" parameterType="${primaryKeyTypeJava}" resultMap="BaseResultMap">
        select <include refid="Base_Column_List"/>
        from ${tableInfo.name}
        where ${primaryKeySqlFieldName} = ${"#{"}${primaryKeyJavaFieldName},jdbcType=${primaryKeyTypeJdbc}${"}"}
    </select>

    <select id="selectByPrimaryKeys" parameterType="java.util.Set" resultMap="BaseResultMap">
        select <include refid="Base_Column_List"/>
        from ${tableInfo.name}
        where ${primaryKeySqlFieldName} in
        <foreach collection="list" item="item" index="index" open="(" separator="," close=")">
	        ${"#{"}item,jdbcType=${primaryKeyTypeJdbc}${"}"}
	    </foreach>
    </select>

    <select id="selectList" parameterType="${po}" resultMap="BaseResultMap">
        select <include refid="Base_Column_List"/>
        from ${tableInfo.name}
        <where>
        <include refid="where"/>
        </where>
    </select>

    <select id="selectOne" parameterType="${po}" resultMap="BaseResultMap">
        select <include refid="Base_Column_List"/>
        from ${tableInfo.name}
        <where>
        <include refid="where"/>
        </where>
        limit 1
    </select>

    <select id="selectCount" parameterType="${po}" resultType="int">
        select count(1) from ${tableInfo.name}
        <where>
        <include refid="where"/>
        </where>
    </select>

    <insert id="insert" parameterType="${po}" useGeneratedKeys="true" keyColumn="${primaryKeySqlFieldName}" keyProperty="${primaryKeyJavaFieldName}">
        insert into ${tableInfo.name} (<include refid="Base_Column_List"/>)
        values (
        <#if tableInfo.columnInfos??>
        <#list tableInfo.columnInfos as item>
        ${"#{"}${item.javaFieldName},jdbcType=${item.jdbcTypeName}${"}"}<#if item_has_next>,</#if>
        </#list>
        </#if>)
    </insert>

    <insert id="insertBatch" parameterType="java.util.List">
        insert into ${tableInfo.name} (<include refid="Base_Column_List"/>)
        values 
        <foreach collection="list" item="item" index="index" separator=",">
	        (
	        <#if tableInfo.columnInfos??>
	        <#list tableInfo.columnInfos as item>
	        ${"#{"}item.${item.javaFieldName},jdbcType=${item.jdbcTypeName}${"}"}<#if item_has_next>,</#if>
	        </#list>
	        </#if>)
        </foreach>
    </insert>

    <insert id="insertSelective" parameterType="${po}" useGeneratedKeys="true" keyColumn="${primaryKeySqlFieldName}" keyProperty="${primaryKeyJavaFieldName}">
        insert into ${tableInfo.name}
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <#if tableInfo.columnInfos??>
	        <#list tableInfo.columnInfos as item>
            <if test="${item.javaFieldName} != null">
                ${item.convertName},
            </if>
            </#list>
            </#if>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            <#if tableInfo.columnInfos??>
	        <#list tableInfo.columnInfos as item>
            <if test="${item.javaFieldName} != null">
                ${"#{"}${item.javaFieldName},jdbcType=${item.jdbcTypeName}${"}"},
            </if>
            </#list>
            </#if>
        </trim>
    </insert>

    <update id="updateByPrimaryKeySelective" parameterType="${po}">
        update ${tableInfo.name}
        <set>
        <#if tableInfo.columnInfos??>
        <#list tableInfo.columnInfos as item>
            <#if !item.primaryKeyFlag>
            <if test="${item.javaFieldName} != null" >
                ${item.convertName} = ${"#{"}${item.javaFieldName},jdbcType=${item.jdbcTypeName}${"}"}<#if item_has_next>,</#if>
            </if>
            </#if>
        </#list>
        </#if>
        </set>
        where ${primaryKeySqlFieldName} = ${"#{"}${primaryKeyJavaFieldName},jdbcType=${primaryKeyTypeJdbc}${"}"}
    </update>

    <update id="updateByPrimaryKey" parameterType="${po}">
        update ${tableInfo.name}
        set
        <#if tableInfo.columnInfos??>
        <#list tableInfo.columnInfos as item>
            <#if !item.primaryKeyFlag>
            ${item.convertName} = ${"#{"}${item.javaFieldName},jdbcType=${item.jdbcTypeName}${"}"}<#if item_has_next>,</#if>
            </#if>
        </#list>
        </#if>
        where ${primaryKeySqlFieldName} = ${"#{"}${primaryKeyJavaFieldName},jdbcType=${primaryKeyTypeJdbc}${"}"}
    </update>

    <delete id="deleteByPrimaryKey" parameterType="${primaryKeyTypeJava}">
        delete from ${tableInfo.name}
        where ${primaryKeySqlFieldName} = ${"#{"}${primaryKeyJavaFieldName},jdbcType=${primaryKeyTypeJdbc}${"}"}
    </delete>

    <delete id="deleteByPrimaryKeys" parameterType="java.util.Set">
        delete from ${tableInfo.name}
        where ${primaryKeySqlFieldName} in
        <foreach collection="list" item="item" index="index" open="(" separator="," close=")">
	        ${"#{"}item,jdbcType=${primaryKeyTypeJdbc}${"}"}
	    </foreach>
    </delete>
</mapper>