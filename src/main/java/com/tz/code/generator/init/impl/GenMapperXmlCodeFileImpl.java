package com.tz.code.generator.init.impl;

import com.tz.code.generator.domain.GenCodeFileContext;
import com.tz.code.generator.domain.TemplateModelInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * java Mapper xml代码生成实现类
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-27 0:20
 **/
@Service
public class GenMapperXmlCodeFileImpl extends AbstractGenCodeFile {

    @Override
    public void genCodeFile(GenCodeFileContext context) {
        TemplateModelInfo templateModelInfo = context.getTemplateModelInfo();
        for (int i = 0; i < codeGeneratorProperties.getMapperXmlTemplates().size(); i++) {
            String fileName = null;
            if (i == 0) {
                fileName = StringUtils.join(templateModelInfo.getTableInfo().getHumpName(), "Mapper.xml");
                this.doGenCodeFile(templateModelInfo, codeGeneratorProperties.getMapperXmlTemplates().get(i), codeGeneratorProperties.getMapperXmlPath(), codeGeneratorProperties.getMapperXmlPackage(), fileName);
            } else if (i == 1) {
                fileName = StringUtils.join(templateModelInfo.getTableInfo().getHumpName(), "UdfMapper.xml");
                this.doGenCodeFile(templateModelInfo, codeGeneratorProperties.getMapperXmlTemplates().get(i), codeGeneratorProperties.getMapperUdfXmlPath(), codeGeneratorProperties.getMapperUdfXmlPackage(), fileName);
            }
        }
    }
}
