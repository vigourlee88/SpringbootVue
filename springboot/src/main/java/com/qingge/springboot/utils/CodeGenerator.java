package com.qingge.springboot.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Paths;
import java.sql.Types;
import java.util.Collections;

/**
 * mp代码生成器
 * by 清哥哥
 * @since 2024-07-14
 *
 */
public class CodeGenerator {
    public static void main(String[] args) {
        generator();

    }
    private static void generator() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/qing?serverTimeZone=GMT%2b8", "root", "123456") //这里需要重新设置url，username,password
                .globalConfig(builder -> {
                    builder.author("青哥哥") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\IdeaProject\\springboot\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.qingge.springboot") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\IdeaProject\\springboot\\src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
//                    builder.mapperBuilder().enableMapperAnnotation().build(); //自动在Mapper上添加注解@Mapper
                    builder.controllerBuilder().enableHyphenStyle()  // 开启驼峰转连字符
                            .enableRestStyle();  // 开启生成@RestController 控制器

                    builder.addInclude("sys_user") // 设置需要生成的表名 查看数据库的表名
                            .addTablePrefix("t_", "sys_"); // 设置过滤表前缀 忽略掉sys_
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
