/*
合并时需要；
1，增加方法
2.自定义sql的方法写在自定义分页查询的前面
3.可以把文件拆分一下，根据select,update,insert等拆分多个文件，或者在一个文件用下划线分开
*/
/*
 * 文件名：TempAppTest
 * 版权：Copyright by 启海云仓 qihaiyun.com
 * 描述：
 * 创建人：Administrator
 * 创建时间：2019/9/21
 * 修改理由：
 * 修改内容：
 */
package com.tiglle.mp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.tiglle.mp.entity.Plan;
import com.tiglle.mp.mapper.PlanMapper;
import com.tiglle.mp.service.PlanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话简述该类/接口的功能〉
 * 〈功能详细描述〉
 *
 * @author Administrator
 * @version 1.0
 * @see TempAppTest
 * @since JDK1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TempAppTest {

    @Autowired
    private PlanService planService;

    @Autowired
    private PlanMapper planMapper;

    /*
    -----------------------------------------------------------------------------Lambda 条件构造器
                                                                                                                        and()等使用
                                                                                                                        特殊的Lambda构造器
     and(),or()等
    */
    @Test
    public void mpTest() {
        /*3种方式*/
        //1.
        LambdaQueryWrapper<Object> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        //2.
        LambdaQueryWrapper lambdaQueryWrapper2 = new QueryWrapper<Plan>().lambda();
        //3.
        LambdaQueryWrapper<Plan> lambdaQueryWrapper3 = Wrappers.<Plan>lambdaQuery();
        //此时可以通过方法引用的方式，防止单词误写，例：
        lambdaQueryWrapper3.eq(Plan::getLocno,"101").and(lambdaQueryWrapper->lambdaQueryWrapper.eq(Plan::getOrderNo,"123"));
        //普通quertWrapper可能会写成：quertWrapper.eq("locco","101");，单词写错
        List<Plan> plans = planMapper.selectList(lambdaQueryWrapper3);

        /*特殊的Lambda构造器,可以直接用LambdaQueryWrapper对象调用crud方法，源码还是调用了planMapper的crud方法*/
        LambdaQueryChainWrapper<Plan> planLambdaQueryChainWrapper = new LambdaQueryChainWrapper<>(planMapper);
        List<Plan> list = planLambdaQueryChainWrapper.eq(Plan::getLocno, "101").list();
        plans.forEach(System.out::println);
    }

    /*
    -----------------------------------------------------------------------------自定义sql时：customSqlSegment属性:翻译后的mysql的where和后面的条件
                                                                                                                                            ：sqlSelect属性：需要查询的字段
     */
    @Test
    public void mpTest1() {
        QueryWrapper<Plan> wrapper = new QueryWrapper();
        wrapper.select("id");//由于
        wrapper.eq("locno","101");
        List<Plan> list = planMapper.customSelectList(wrapper);
        list.forEach(System.out::println);
    }

    /*
    -----------------------------------------------------------------------------更新
    1.int updateById(T entity); 默认null不会set值
    2.int update(T entity, Wrapper<T> updateWrapper); entity为set后的语句，默认null不set，updateWrapper为where后的条件
    3.updateWrapper直接包揽set后的语句和where后的语句
    4.update同样适用   Lambda 条件构造器   和   特殊的Lambda构造器
    */
    @Test
    public void mpTest2() {
        /*1.*/
        Plan plan = new Plan();
        plan.setId(22L);
        plan.setLocno("102");
        plan.setOrderNo(null);
        int i = planMapper.updateById(plan);

        /*2*/
        UpdateWrapper<Plan> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("locno","102");
        int update = planMapper.update(plan, updateWrapper);

        /*3*/
        UpdateWrapper<Plan> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.set("locno","102").eq("locno","103");
        int update1 = planMapper.update(plan, updateWrapper);

        /*4*/
        LambdaUpdateChainWrapper<Plan> lambdaUpdateChainWrapper = new LambdaUpdateChainWrapper<Plan>(planMapper);
        boolean update2 = lambdaUpdateChainWrapper.set(Plan::getLocno, "103").eq(Plan::getLocno, "102").update();
    }

    /*
    -----------------------------------------------------------------------------删除
    跟更新是一样的
    */
    @Test
    public void mpTest3() {
        /*不演示*/
    }

    /*
    -----------------------------------------------------------------------------mybatis的配置：可以看官网首页右上角的配置，和文档同级
*/
    @Test
    public void mpTest5() {
        /*mybatis的配置：可以看官网首页右上角的配置，和文档同级*/
    }

    /*
    -----------------------------------------------------------------------------通用Service
    封装了很多好用的方法
    1.批量插入
    Service.saveBatch(list, 2000);

    2.封装特殊Lambda条件构造器
    Service.lambdaUpdate()；

    3.等......
    */
    @Test
    public void mpTest4() {
        List<Plan> list = new ArrayList<>();
        /*批量插入,默认最大1000条执行一次，数据量太大的单挑执行sql会内存泄漏等问题*/
        boolean b = planService.saveBatch(list);
        //可以调用此方法设置最大执行条数
        boolean b1 = planService.saveBatch(list, 2000);

        /*封装特殊Lambda条件构造器*/
        boolean update = planService.lambdaUpdate().set(Plan::getLocno, "102").eq(Plan::getLocno, "103").update();
    }
}
