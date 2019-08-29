package com.tiglle.mp;

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
public class MpApplicationTests {

    @Autowired
    private PlanService planService;

    @Autowired
    private PlanMapper planMapper;

    @Test
    public void mpTest() {
        //1.查询所有
//        planService.list();
        List<Plan> plans = planMapper.selectList(null);
        System.out.println(plans.size()+"==========================");
    }

}
