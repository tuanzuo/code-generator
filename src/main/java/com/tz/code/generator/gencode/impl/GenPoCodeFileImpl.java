package com.tz.code.generator.gencode.impl;

import com.tz.code.generator.domain.GenCodeFileContext;
import com.tz.code.generator.domain.TemplateModelInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * java PO代码生成实现类
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-27 0:20
 **/
@Service
public class GenPoCodeFileImpl extends AbstractGenCodeFile {

    @Override
    public void genCodeFile(GenCodeFileContext context) {
        TemplateModelInfo templateModelInfo = context.getTemplateModelInfo();
        codeGeneratorProperties.getJavaModelTemplates().forEach(template -> {
            String fileName = StringUtils.join(templateModelInfo.getTableInfo().getHumpName(), "PO.java");
            this.doGenCodeFile(templateModelInfo, template, codeGeneratorProperties.getJavaModelPath(), codeGeneratorProperties.getJavaModelPackage(), fileName);
        });
    }
}
