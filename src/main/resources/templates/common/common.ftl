<#assign po=javaModelPackage+"."+tableInfo.humpName+"PO" />

<#assign primaryKeyTypeJava="java.lang.String" />
<#assign primaryKeyTypeJavaSimple="String" />
<#assign primaryKeyTypeJdbc="VARCHAR" />
<#assign primaryKeySqlFieldName="id" />
<#assign primaryKeyJavaFieldName="id" />
<#if tableInfo.columnInfos??>
    <#list tableInfo.columnInfos as item>
        <#if item.primaryKeyFlag>
            <#assign primaryKeyTypeJava=item.javaTypeName />
            <#assign primaryKeyTypeJavaSimple=item.javaTypeSimpleName />
            <#assign primaryKeyTypeJdbc=item.jdbcTypeName />
            <#assign primaryKeySqlFieldName=item.name />
            <#assign primaryKeyJavaFieldName=item.javaFieldName />
        </#if>
    </#list>
</#if>