package com.tz.code.generator.gencode.impl;

import com.tz.code.generator.domain.GenCodeFileContext;
import com.tz.code.generator.domain.TemplateModelInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * java Mapper代码生成实现类
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-27 0:20
 **/
@Service
public class GenMapperCodeFileImpl extends AbstractGenCodeFile {

    @Override
    public void genCodeFile(GenCodeFileContext context) {
        TemplateModelInfo templateModelInfo = context.getTemplateModelInfo();
        for (int i = 0; i < codeGeneratorProperties.getJavaMapperTemplates().size(); i++) {
            String fileName = null;
            if (i == 0) {
                fileName = StringUtils.join(templateModelInfo.getTableInfo().getHumpName(), "Mapper.java");
                this.doGenCodeFile(templateModelInfo, codeGeneratorProperties.getJavaMapperTemplates().get(i), codeGeneratorProperties.getJavaMapperPath(), codeGeneratorProperties.getJavaMapperPackage(), fileName);
            } else if (i == 1) {
                fileName = StringUtils.join(templateModelInfo.getTableInfo().getHumpName(), "UdfMapper.java");
                this.doGenCodeFile(templateModelInfo, codeGeneratorProperties.getJavaMapperTemplates().get(i), codeGeneratorProperties.getJavaUdfMapperPath(), codeGeneratorProperties.getJavaUdfMapperPackage(), fileName);
            }
        }
    }
}
