package com.${companyName}.${projectName}.model.${model};

import java.io.Serializable;

import com.${companyName}.${projectName}.common.base.BaseDomain;
import java.util.Date;

/**
 * ${objectNameCn}类
 * @author ${author}
 * @date ${createTime}
 */
public class ${objectName} extends BaseDomain implements Serializable{
    <#if columnFieldList??>
    <#list columnFieldList as item>
    
    /**
     * <#if item.comment??>${item.comment}</#if>
     */
    private <#if item.javaType??>${item.javaType}</#if> <#if item.field??>${item.field}</#if>;
    </#list>
    
    <#list columnFieldList as item>
    public <#if item.javaType??>${item.javaType}</#if> get<#if item.cField??>${item.cField}</#if>()
    {
        return <#if item.field??>${item.field}</#if>;
    }
        
    public void set<#if item.cField??>${item.cField}</#if>(<#if item.javaType??>${item.javaType}</#if> <#if item.field??>${item.field}</#if>)
    {
        this.${item.field} = <#if item.field??>${item.field}</#if>;
    }
        
    </#list>
    </#if>
}
