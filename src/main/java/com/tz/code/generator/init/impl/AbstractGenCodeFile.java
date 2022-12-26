package com.tz.code.generator.init.impl;

import com.google.common.collect.Lists;
import com.tz.code.generator.config.CodeGeneratorProperties;
import com.tz.code.generator.domain.TemplateModelInfo;
import com.tz.code.generator.init.IGenCodeFile;
import com.tz.code.generator.resolver.ITemplateResolver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;

/**
 * 生成代码文件抽象类
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-27 0:24
 **/
public abstract class AbstractGenCodeFile implements IGenCodeFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGenCodeFile.class);

    @Autowired
    public CodeGeneratorProperties codeGeneratorProperties;
    @Autowired
    private ITemplateResolver templateResolver;

    /**
     * 处理代码文件生成
     *
     * @param templateModelInfo
     * @param template
     * @param filePath
     * @param filePackage
     * @param fileName
     */
    protected void doGenCodeFile(TemplateModelInfo templateModelInfo, String template, String filePath, String filePackage, String fileName) {
        //1、得到解析后的模板内容
        String templateContent = templateResolver.resolve(template, templateModelInfo);
        //2、生成代码文件
        String[] packageArrays = StringUtils.split(filePackage, ".");
        String pathName = StringUtils.join(Lists.newArrayList(filePath, StringUtils.join(packageArrays, File.separator), fileName).toArray()
                , File.separator);
        try {
            File file = new File(pathName);
            if (!(file.getParentFile().exists())) {
                file.getParentFile().mkdirs();
            }
            FileCopyUtils.copy(templateContent.getBytes(), file);
            LOGGER.info("[生成代码] {}", pathName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
