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

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.tiglle.mp.entity.Plan;
import com.tiglle.mp.mapper.PlanMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 〈一句话简述该类/接口的功能〉
 * 〈功能详细描述〉
 *
 * @author Administrator
 * @version 1.0
 * @see UpdateOrDeleteTestApp
 * @since JDK1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateOrDeleteTestApp {

    @Autowired
    private PlanMapper planMapper;

    /*
    -----------------------------------------------------------------------------更新
    1.int updateById(T entity); 默认null不会set值
    2.int update(T entity, Wrapper<T> updateWrapper); entity为set后的语句，默认null不set，updateWrapper为where后的条件
    3.updateWrapper.set().eq():直接包揽set后的语句和where后的语句
    4.updateLambda同样适用   Lambda 条件构造器   和   特殊的Lambda构造器
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




}
