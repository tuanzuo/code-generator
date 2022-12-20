package com.tz.code.generator.resolver.impl;

import com.tz.code.generator.resolver.ITemplateResolver;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;

/**
 * Freemarker模板解析器
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-20 21:00
 **/
@Service
public class FreemarkerTemplateResolver implements ITemplateResolver {

    private static final String ENCODING_UTF8 = "UTF-8";

    /**
     * @see org.springframework.boot.autoconfigure.freemarker.FreeMarkerServletWebConfiguration#freeMarkerConfiguration(org.springframework.web.servlet.view.freemarker.FreeMarkerConfig)
     */
    @Autowired
    private freemarker.template.Configuration configuration;

    @Override
    public String resolve(String templateName, Object model) {
        try {
            Template template = configuration.getTemplate(templateName, ENCODING_UTF8);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }
}
