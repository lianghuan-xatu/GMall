package com.xatu.gmall;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import javax.swing.text.TabExpander;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
       // String projectPath = System.getProperty("user.dir");
        gc.setOutputDir("D:\\IDEA WORKSPACE4\\GMall\\gmall-api\\src\\main\\java");
        gc.setAuthor("LiangHuan");
        gc.setOpen(false);
        gc.setFileOverride(false);
        //gc.setSwagger2(true); //实体属性 Swagger2 注解
        gc.setControllerName("CartController");
        gc.setServiceName("PaymentService");
        gc.setServiceImplName("PaymentServiceImpl");
        gc.setMapperName("PaymentMapper");
        gc.setXmlName("Payment  Mapper");
        gc.setIdType(IdType.AUTO);
        gc.setDateType(DateType.ONLY_DATE);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.0.106:3306/gmall?useUnicode=true&useSSL=false&characterEncoding=utf8");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("gmall");
        pc.setParent("com.xatu");
        pc.setEntity("entity");
        pc.setController("controller");
        pc.setMapper("mapper");
        pc.setService("service");
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);//全局大写命名
        strategy.setNaming(NamingStrategy.underline_to_camel);   //表名转驼峰
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);   //字段转驼峰
        strategy.setTablePrefix("");
        //strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        //strategy.setEntityLombokModel(true);
        // strategy.setRestControllerStyle(true);
        /* 公共父类
         strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");// 写于父类中的公共字段
         strategy.setSuperEntityColumns("id");*/
        strategy.setInclude("payment_info");  //设置要要映射的表名

       /* strategy.setControllerMappingHyphenStyle(true);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());*/
        /**
         * 自动填充策略
         *
         */
      /*   TableFill gmtCreate=new TableFill("create_time", FieldFill.INSERT);
         TableFill gmtModified=new TableFill("modified_time",FieldFill.INSERT_UPDATE);
         ArrayList<TableFill> tableFills=new ArrayList<>();
         tableFills.add(gmtCreate);
         tableFills.add(gmtModified);
         strategy.setTableFillList(tableFills);*/

        /**
         * 乐观锁配置
         */
       // strategy.setVersionFieldName("version");
        mpg.setStrategy(strategy);

        mpg.execute();
    }

}
