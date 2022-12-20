package ${javaModelPackage};

<#if tableInfo.javaTypeNames??>
<#list tableInfo.javaTypeNames as item>
import ${item};
</#list>
</#if>

/**
 * ${tableInfo.comment}PO
 *
 * @author ${author}
 * @time ${time}
 */
public class ${tableInfo.humpName}PO {
    <#if tableInfo.columnInfos??>
    <#list tableInfo.columnInfos as item>

    /**
     * <#if item.comment??>${item.comment}</#if>
     */
    private <#if item.javaTypeSimpleName??>${item.javaTypeSimpleName}</#if> <#if item.javaFieldName??>${item.javaFieldName}</#if>;
    </#list>

    <#list tableInfo.columnInfos as item>
    public <#if item.javaTypeSimpleName??>${item.javaTypeSimpleName}</#if> get<#if item.humpName??>${item.humpName}</#if>() {
        return <#if item.javaFieldName??>${item.javaFieldName}</#if>;
    }

    public void set<#if item.humpName??>${item.humpName}</#if>(<#if item.javaTypeSimpleName??>${item.javaTypeSimpleName}</#if> <#if item.javaFieldName??>${item.javaFieldName}</#if>) {
        <#if item.javaTypeName != 'java.lang.String'>
        this.${item.javaFieldName} = <#if item.javaFieldName??>${item.javaFieldName}</#if>;
        </#if>
        <#if item.javaTypeName == 'java.lang.String'>
        this.${item.javaFieldName} = ${item.javaFieldName} == null ? null : <#if item.javaFieldName??>${item.javaFieldName}.trim()</#if>;
        </#if>
    }

    </#list>
    </#if>
}
