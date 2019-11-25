package com.tiglle.mp.mp2_高级;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tiglle.mp.CommonTestApp;
import com.tiglle.mp.entity.Plan;
import com.tiglle.mp.mapper.PlanMapper;
import com.tiglle.mp.service.PlanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonTest {

    //service 封装了 mapper，方法名字可能不一样
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanMapper planMapper;


    /*
    ---------------------------------------------------------------------------------------------逻辑删除
    1.application.properties或yml中配置逻辑删除的值，缺省 0未删除，1已删除
    2.LogicSqlInjector注入spring容器：com.tiglle.mp.config.MybatisPlusConfiguration.sqlInjector
       或者在配置文件配置注入（3.0后废除此配置属性，使用@Bean的方式注入）：mybatis-plus.global-config.sql-injector=com.baomidou.mybatisplus.mapper.LogicSqlInjector
    3.实体类的逻辑删除字段加上@TableLogic注解，可以配置局部 未删除value  和 已删除delval 的值
    4.加上逻辑删除后，查询和更新都会带上是否删除为 未删除 的条件
       但是自定义查询和更新不会加
       解决方法：1.在wrapper中加上 wrapper.eq("del_flag","1")
                      2.在自定义sql中加上 del_flag=1

    5.查询时排除 删除字段
        加上@TableField(select = false)注解

     */
    @Test
    public void test1(){
        planMapper.deleteById(193);
        /*
        删除语句变为了更新语句
        UPDATE plan SET del_flag=-1 WHERE id=? AND del_flag=1
         */
        Plan plan = new Plan();
        plan.setLocno("111");
        List<Plan> plans = planMapper.selectList(null);
        planMapper.update(plan,Wrappers.<Plan>lambdaQuery().eq(Plan::getId,10));
        planMapper.customSelectList(new QueryWrapper<Plan>().select("*").eq("locno",1));
        /*
        加上逻辑删除后，查询和更新都会带上是否删除为 未删除 的条件，但是自定义查询和更新不会加
        SELECT id,...,del_flag FROM plan WHERE del_flag=1
        UPDATE plan SET locno=? WHERE del_flag=1 AND id = ?
        select * from plan WHERE locno = ?
         */
    }

    /*
    ---------------------------------------------------------------------------------------------自动填充：创建时间，更新时间，创建人，更新人等...
    1.在需要自动填充的字段上加上@TableField(fill = FieldFill.INSERT||INSERT_UPDATE)
    2.建立填充处理器:com.tiglle.mp.component.MyMetaObjectHandler
     */
    @Test
    public void test2(){
        Plan plan = CommonTestApp.getPlan();
        plan.setUpdateTime(null);
        plan.setCreateTime(null);
        int insert = planMapper.insert(plan);
    }

}
