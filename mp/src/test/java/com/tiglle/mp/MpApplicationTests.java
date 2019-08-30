package com.tiglle.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiglle.mp.entity.Plan;
import com.tiglle.mp.mapper.PlanMapper;
import com.tiglle.mp.service.PlanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MpApplicationTests {

    //service 封装了 mapper，方法名字可能不一样
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanMapper planMapper;

    /*查询所有*/
    @Test
    public void mpTest() {
        List<Plan> plans = planMapper.selectList(null);
        System.out.println(plans.size()+"==========================");
    }

    /*新增,删除*/
    @Test
    public void mpTest1() {
        Plan plan = getPlan();
        //会自动填充自增长的id
        int i = planMapper.insert(plan);
        System.out.println(i+"="+plan);
        //删除
        Map<String,Object> attrAndField = new HashMap<>();
        attrAndField.put("locno",plan.getLocno());
        int i1 = planMapper.deleteByMap(attrAndField);
        System.out.println("成功删除="+i1);
    }

    /*1.查询为List<Map<String, Object>>结构*/
    /*k为表字段（非实体属性），v为表值*/
    /*selectObjs:只返回实体类的第一个地段的值*/
    @Test
    public void mpTest2() {
        Plan plan = insertPlan();
        QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("locno",plan.getLocno());
        List<Map<String, Object>> maps = planMapper.selectMaps(queryWrapper);
        maps.forEach(map->map.forEach((k,v)-> System.out.print("k="+k+",v="+v+"||")));
        List<Object> objects = planMapper.selectObjs(queryWrapper);
        System.out.println(objects);
    }

    @Test
    public void mpTest3() {
        IPage<Plan> page = planMapper.selectPage(new Page<>(1, 10), new QueryWrapper<>());
        System.out.println(page);
    }


    private Plan getPlan(){
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

    private Plan insertPlan(){
        Plan plan = getPlan();
        //会自动填充自增长的id
        planMapper.insert(plan);
        return plan;
    }

}
