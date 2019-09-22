package com.tiglle.mp;

import com.tiglle.mp.entity.Plan;
import com.tiglle.mp.mapper.PlanMapper;
import com.tiglle.mp.service.PlanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonTestApp {

    //service 封装了 mapper，方法名字可能不一样
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanMapper planMapper;

    /*
        ------------------------------------------------------------------------------------------排除非表字段的三种方式(实体类拥有的自断并不想与表中的自断关联)
        1.加 static关键字
        2.加上 transient 关键字
        2.加上注解：TableField(exist=false) exist：是否要在表中存在，默认为true
    */

    /*
    ---------------------------------------------------------------------------------------------新增,删除
    */
    @Test
    public void mpTest1() {
        Plan plan = getPlan();
        //会自动填充自增长的id
        int i = planMapper.insert(plan);
        System.out.println(i + "=" + plan);
        //删除
        Map<String, Object> attrAndField = new HashMap<>();
        attrAndField.put("locno", plan.getLocno());
        int i1 = planMapper.deleteByMap(attrAndField);
        System.out.println("成功删除=" + i1);
    }
    /*
    --------------------------------------------------------------------------------------------通用Service
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

    /*
    --------------------------------------------------------------------------------------------mybatis的配置：可以看官网首页右上角的配置，和文档同级
    */
    @Test
    public void mpTest5() {
        /*mybatis的配置：可以看官网首页右上角的配置，和文档同级*/
    }


    public static Plan getPlan() {
        Plan plan = new Plan();
        plan.setLocno("1234567810");
        plan.setCreateFlag("222");
        plan.setOrderNo("333");
        plan.setServiceDealCode("1");
        plan.setCreateFlag("1");
        plan.setInitiator("1");
        plan.setServiceSource("1");
        plan.setServiceReasonCode("444");
        plan.setServiceDealCode("555");
        plan.setExchangeSku("0");
        plan.setProductQuality("1");
        plan.setIsInWarehouse("0");
        plan.setTaskType("1");
        plan.setSourceOrderNo("666");
        plan.setPlanBoxNum(10);
        plan.setPlanPiecesNum(20);
        plan.setActualPiecesNum(30);
        plan.setDifferenceNum(40);
        plan.setProductType("1");
        plan.setVerifyUserId("777");
        plan.setVerifyUserName("小明");
        plan.setVerifyTime(LocalDateTime.now());
        plan.setCreateUserId("999");
        plan.setCreateUserName("小明");
        plan.setCreateTime(LocalDateTime.now());
        plan.setUpdateUserId("101");
        plan.setUpdateUserName("小明");
        plan.setUpdateTime(LocalDateTime.now());
        plan.setDelFlag(0);
        plan.setTraceId("1");
        plan.setRemarks("测试小明");
        return plan;
    }

    public Plan insertPlan() {
        Plan plan = getPlan();
        //会自动填充自增长的id
        planMapper.insert(plan);
        return plan;
    }
}
