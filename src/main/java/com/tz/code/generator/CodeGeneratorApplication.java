package com.tz.code.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 代码生成器启动类
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2022-12-11 18:03
 **/
@SpringBootApplication
public class CodeGeneratorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(CodeGeneratorApplication.class, args);
        System.exit(SpringApplication.exit(applicationContext));
    }

}
