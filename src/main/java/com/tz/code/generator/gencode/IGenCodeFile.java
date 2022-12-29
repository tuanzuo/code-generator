package com.tz.code.generator.gencode;

import com.tz.code.generator.domain.GenCodeFileContext;

/**
 * 生成代码文件接口
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-27 0:09
 **/
public interface IGenCodeFile {

    /**
     * 生成代码文件
     * @param context
     */
    void genCodeFile(GenCodeFileContext context);
}
