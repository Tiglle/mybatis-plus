package com.tiglle.mp.mp2_高级;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiglle.mp.CommonTestApp;
import com.tiglle.mp.config.MybatisPlusConfiguration;
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

    /*
    ---------------------------------------------------------------------------------------------乐观锁
    1.乐观锁插件交给spring容器：com.tiglle.mp.config.MybatisPlusConfiguration.optimisticLockerInterceptor
    2.在实体类代表version的字段上加上@Version注解,支持的数据类型：int,Integer,long,Long,Date,Timestamp,LocalDateTime
    3.支持的方法：仅支持 updateById(id) 与 update(entity, wrapper) 方法，在 update(entity, wrapper) 方法下, wrapper 不能复用!!!
     */
    @Test
    public void test3(){
        Integer version = 1;
        Plan plan1 = new Plan();
        plan1.setLocno("111");
        plan1.setVersionNum(version);
        QueryWrapper<Plan> queryWrapper = Wrappers.query();
        queryWrapper.eq("id",198);
        planMapper.update(plan1,queryWrapper);
        //UPDATE plan SET locno=?, update_time=?, version_num=? WHERE del_flag=1 AND id = ? AND version_num = ?

        //在 update(entity, wrapper) 方法下, wrapper 不能复用!!!
        Integer version2 = 2;
        Plan plan2 = new Plan();
        plan2.setLocno("111");
        plan2.setVersionNum(version);
        queryWrapper.eq("id",198);
        planMapper.update(plan2,queryWrapper);
        //UPDATE plan SET locno=?, update_time=?, version_num=? WHERE del_flag=1 AND id = ? AND version_num = ? AND id = ? AND version_num = ?
        //两个version_num条件，值为1
    }

    /*
    ---------------------------------------------------------------------------------------------性能分析插件
    1.性能分析插件注入spring容器：com.tiglle.mp.config.MybatisPlusConfiguration.performanceInterceptor
     */
    @Test
    public void test4(){
        List<Plan> plans = planMapper.selectList(null);
    }

    /*
    ---------------------------------------------------------------------------------------------执行sql分析打印
    1.引入p6spy 依赖：完美的输出打印 SQL 及执行时长 需要mp的版本为3.1.0 以上版本
    2.配置
        driver-class-name 为 p6spy 提供的驱动类
        url 前缀为 jdbc:p6spy 跟着冒号为对应数据库连接地址
    3.增加配置文件spy.properties
     */
    @Test
    public void test5(){
        List<Plan> plans = planMapper.selectList(Wrappers.<Plan>lambdaQuery().select(Plan::getLocno).eq(Plan::getId,198));
        /*
        sql日志会自动把？填充
        SELECT locno FROM plan WHERE del_flag=1 AND id = 198
         */
    }

    /*
    ---------------------------------------------------------------------------------------------多租户sql解析器：多用户时自动添加区分用户的条件
    1.属于sql解析部分，依赖mp的分页插件，需要在分页插件中设置解析器，但是不是只针对分页方法有效，只是因为拦截时机问题，所以在分页插件中设置
    :com.tiglle.mp.config.MybatisPlusConfiguration.paginationInterceptor
    2.特定sql过滤：某个方法不增加sql信息
        1.方式一：分页插件中设置，缺点是对需要的每个EntityMapper都要设置：com/tiglle/mp/config/MybatisPlusConfiguration.java:78
        2.方式二：方法上增加注解@SqlParser(filter = true)：com.baomidou.mybatisplus.annotation.SqlParser
                      mp3.1.1之前的版本还需要在配置文件中加一个配置mybatis-plus.global-config.sql-parser-cache=true：application.properties:38
     */
    @Test
    public void test6(){
        Plan plan = new Plan();
        plan.setId(198L);
        plan.setLocno("234");
        List<Plan> palns = planMapper.selectList(Wrappers.<Plan>query().select("id"));
        int i = planMapper.updateById(plan);
        List<Plan> palns2 = planMapper.customSelectList(Wrappers.<Plan>query().select("id").eq("customer_no",1234));
        //2.特定sql过滤：某个方法不增加sql信息
        IPage<Plan> planIPage = planMapper.selectCustomPage(new Page(1L, 10L), Wrappers.<Plan>query().select("id").eq("customer_no",1234));
        /*
        1.查询或更新删除等，都会自动带上多租户的条件customer_no = 101
        SELECT id FROM plan WHERE plan.customer_no = 101 AND del_flag = 1
        UPDATE plan SET locno = '234', update_time = '2019-11-25T23:35:38.915' WHERE plan.customer_no = 101 AND id = 198 AND del_flag = 1
        2.自定义sql也会带上多租户条件
        SELECT id FROM plan WHERE plan.customer_no = 101
        3.wrapper条件加了多租户字段时，会重复拼接条件
        SELECT id FROM plan WHERE plan.customer_no = 101 AND customer_no = 1234
        4.特定sql过滤：某个方法不增加sql信息
        SELECT COUNT(1) FROM plan WHERE customer_no = 1234
         */
    }

    /*
    ---------------------------------------------------------------------------------------------动态表名
    ：Plan表的查询可以替换为自己想要的表，eg:plan  ---> plan_2019
    1.属于sql解析部分，依赖mp的分页插件，需要在分页插件中设置解析器:com/tiglle/mp/config/MybatisPlusConfiguration.java:83
    2.注意：
        1.要替换的表名eq:testDynamicTableName为null的时候，不会替换，会查询原表
        2.自定义方法也会被替换，特定sql过滤会同事过滤掉 多租户 和 动态表名 ：com/tiglle/mp/config/MybatisPlusConfiguration.java:107，包括@SqlParser(filter = true)注解
     */
    @Test
    public void test7(){
        //模拟用户传入的表名，如果为null，还是查询原表plan
        String tableName = "plan_2019";/*request.getPatameter("tableName")*/
        MybatisPlusConfiguration.testDynamicTableName = tableName;
        //如果是查询plan的，替换为plan_2019
        List<Plan> plans = planMapper.selectList(Wrappers.<Plan>query().select("id"));
        //自定义方法也会被处理替换
        List<Plan> plans1 = planMapper.customSelectList(Wrappers.<Plan>query().select("id").eq("locno", "101"));
        //特定sql过滤会同事过滤掉 多租户 和 动态表名 ：com/tiglle/mp/config/MybatisPlusConfiguration.java:107，包括@SqlParser(filter = true)注解
        IPage<Plan> planIPage = planMapper.selectCustomPage(new Page(1L, 10L), Wrappers.<Plan>query().select("id").eq("locno", "101"));
        /*
        SELECT id FROM plan_2019 WHERE plan_2019.customer_no = 101 AND del_flag = 1
        //自定义方法也会被处理替换
        SELECT id FROM plan_2019 WHERE plan_2019.customer_no = 101 AND locno = ?
        //特定sql过滤会同事过滤掉 多租户 和 动态表名 ：com/tiglle/mp/config/MybatisPlusConfiguration.java:107，包括@SqlParser(filter = true)注解
        select * from plan WHERE locno = ? LIMIT ?,?
        */
    }

    /*
    ---------------------------------------------------------------------------------------------sql注入器
    在mybatisplus中增加自己的通用方法
    1.创建自定义方法的类：com.tiglle.mp.mysqlscriptinjector.RemoveById
    2.创建注入器：com.tiglle.mp.component.MySqlinjector
    3.在接口加入自定义通用方法：com.tiglle.mp.mapper.PlanMapper.removeById
        注意：
            1.如果每个Entity的Mapper都要写一个removeById很麻烦，可以写一个MyBaseMapper继承BaseMapper在同一继承MyBaseMapper
            2.当前逻辑删除的注入和MySqlInjector的注入会冲突，解决方法：把MySqlInjector的实现类改为LogicInjector，即开启了逻辑删除，又能自定义通用方法
            3.带参数的自动以通用方法还不知道怎么弄
     */
    @Test
    public void test8(){
        int i = planMapper.removeById(1);
    }

    /*
    ---------------------------------------------------------------------------------------------mybatis选装件
    看文档吧
    1.InsertBatchSomeColumn：批量插入时只插入某些字段
    2.LogicDeleteByIdWithFill ：逻辑删除时顺便更新某些字段
    3.AlwaysUpdateSomeColumnById： 更新是只更新某些字段
     */
    public void test9(){}
}
