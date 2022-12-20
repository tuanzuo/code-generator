package com.tz.code.generator.resolver;

/**
 * 模板解析器接口
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-20 20:57
 **/
public interface ITemplateResolver {

    /**
     * 模板解析
     *
     * @param templateName 模板
     * @param model        模板参数
     * @return 解析后生成的模板内容
     */
    String resolve(String templateName, Object model);
}
