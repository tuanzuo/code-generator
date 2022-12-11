package com.tz.code.generator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 代码生成器配置
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-11 18:31
 **/
@Configuration
@ConfigurationProperties(prefix = "code.gen")
@Getter
@Setter
public class CodeGeneratorProperties {


}
