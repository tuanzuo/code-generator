package com.tz.code.generator.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 生成代码文件上下文
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-27 0:12
 **/
@Setter
@Getter
@Builder
public class GenCodeFileContext {

    /**
     * 模板对应的模型信息
     */
    private TemplateModelInfo templateModelInfo;

}
