package com.ltr.mymall.util;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
 
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 /**
  * 
  * @author 刘添瑞
  * ！！！！！！！！！！警告！！！！！！！！！！！
  *本段代码将会使用MybatisGenerator根据数据库中表的情况生成相应的pojo.java、mapper.java、mapper.xml
  *
  *配置文件位于src/main/resources/generatorConfig.xml
  *
  *但是无法完全反应实体类之间的关系，所以相应的类还需要具体关系具体修改
  *
  *且本程序只应该执行一次，若再次执行会覆盖上一次的文件，也会覆盖其中的修改
  *
  *若要再次执行需要修改{@param today}为当天的时间
  *
  */
public class MybatisGenerator {
 
    public static void main(String[] args) throws Exception {
        String today = "2019-4-2";
 
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        Date now =sdf.parse(today);
        Date d = new Date();
 
        if(d.getTime()>now.getTime()+1000*60*60*24){
            System.err.println("——————未成成功运行——————");
            System.err.println("——————未成成功运行——————");
            System.err.println("!!本程序具有副作用作用!!，如果必须再次运行，需要修改today变量为今天，如:" + sdf.format(new Date()));
            return;
        }
 
        if(false)
            return;
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        InputStream is= MybatisGenerator.class.getClassLoader().getResource("generatorConfig.xml").openStream();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(is);
        is.close();
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
 
        System.out.println("！！本程序具有副作用作用!!，如果必须再次运行，需要修改today变量为今天，如:" + sdf.format(new Date()));
        System.out.println("！！警告！！生成代码成功，只能执行一次，以后执行会覆盖掉mapper,pojo,xml 等文件上做的修改！！");
 
    }
}
