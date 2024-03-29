package com.tiglle.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeneratorApplicationTests {

    @Test
    public void testGenerator() {

    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator generator = new AutoGenerator();

        // 修改默认模板Velocity为Freemarker
        generator.setTemplateEngine(new FreemarkerTemplateEngine());

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        //projectPath=user.dir=工作空间的文件夹路径
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/generator/src/test/resources");
        //默认会多个I：IUserService，此方法去掉多余的I
        gc.setServiceName("%sService");
        gc.setAuthor("tiglle");
        gc.setOpen(false);
        //Mapper.xml中是否生产基础ResultMap
        gc.setBaseResultMap(true);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        generator.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://154.212.129.2:3306/mp?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull&serverTimezone=UTC");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("Hateyou75!");
        generator.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("mp");
        pc.setParent("com.tiglle");
        generator.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

         //自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
         //自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/generator/src/test/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir(filePath);
                return false;
            }
        });*/
        cfg.setFileOutConfigList(focList);
        generator.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
//         templateConfig.setEntity("templates/entity2.java");
//         templateConfig.setService();
//         templateConfig.setController();

        templateConfig.setXml(null);
        generator.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //表名下划线转驼峰
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //字段吗下划线转驼峰
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //实体类公共父类
//        strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        //实体是否使用Lombok模式
        strategy.setEntityLombokModel(true);
        //控制器是否使用Rest风格
        strategy.setRestControllerStyle(true);
        // 控制器公共父类
//        strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
        // 写于父类中的公共字段
//        strategy.setSuperEntityColumns("id");
        //包含的表名
//        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        //驼峰转连字符 @RequestMapping("/mp/planDtl") 转 @RequestMapping("/mp/plan-dtl")
        strategy.setControllerMappingHyphenStyle(false);
        //表前缀
//        strategy.setTablePrefix(pc.getModuleName() + "_");
        generator.setStrategy(strategy);
        generator.execute();
    }

}
