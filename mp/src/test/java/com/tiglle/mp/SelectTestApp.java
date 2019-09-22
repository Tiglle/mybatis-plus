package com.tiglle.mp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
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
public class SelectTestApp {

    //service 封装了 mapper，方法名字可能不一样
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanMapper planMapper;




    /*
    ---------------------------------------------------------------------------------------------查询所有
    */
    @Test
    public void mpTest() {
        List<Plan> plans = planMapper.selectList(null);
        System.out.println(plans.size() + "==========================");
    }


    /*1.
    ---------------------------------------------------------------------------------------------List<Map<String, Object>>查询为此结构
    1.当查询的字段很少时，可以使用此方法，配合queryWrapper.select("字段1","字段2") 使用
    2.当特殊查询，实体类没有字段能够对应上时使用：queryWrapper.select("avg(age) as avgAge","min(age) as minAge")
    k为表字段（非实体属性），v为表值
    ---------------------------------------------------------------------------------------------selectObjs查询:如果查询多个，只返回第一个地段的值
    因为不知道那一列的具体类型，所以用Object接收
    1.当只返回一列数据的时候可以使用
    */
    @Test
    public void mpTest2() {
        Plan plan = new CommonTestApp().insertPlan();
        QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
        /*
        //1.
        queryWrapper.select("locno","order_no as orderNo");
        queryWrapper.eq("locno", plan.getLocno());
        //2.
        queryWrapper.select("avg(plan_box_num) as avgPlanBoxNum","min(plan_pieces_num) as minPlanPiecesNum").groupBy("service_reason_code").having("1={0}",1);
        List<Map<String, Object>> maps = planMapper.selectMaps(queryWrapper);
        maps.forEach(map -> map.forEach((k, v) -> System.out.print("k=" + k + ",v=" + v + "||")));
         */
        queryWrapper.select("locno");
        List<Object> objects = planMapper.selectObjs(queryWrapper);
        System.out.println(objects);
    }

    /*
   ---------------------------------------------------------------------------------------------condition的用法：为true时sql拼接此条件，false时不拼接
    方法：xxxxxx(boolean condition, R column, Object val)
   */
    @Test
    public void mpTest7() {
        QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
        String locno = "";
        queryWrapper.eq(StringUtils.isNotEmpty(locno),"locno",locno);
        List<Plan> plans = planMapper.selectList(queryWrapper);
        System.out.println(plans);
    }

    /*
    ---------------------------------------------------------------------------------------------查询部分字段的方法
    1.QueryWrapper的select(String... columns)方法,查询指定字段
    2.QueryWrapper的select(Class<T> entityClass, Predicate<TableFieldInfo> predicate)方法，不查询指定字段
    */
    @Test
    public void mpTest6() {
        QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("id,order_no");
        //不查询字段 create_time,update_time
        queryWrapper.select(Plan.class,temp->!temp.getColumn().equals("create_time")&&!temp.getColumn().equals("update_time"));
        List<Plan> plans = planMapper.selectList(queryWrapper);
        System.out.println(plans);
    }

     /*
     ---------------------------------------------------------------------------------------------QueryWrapper(T Entity)条件构造器
     1.使用public QueryWrapper(T entity)构造器，传入实体对象
     2.实体有值得字段会加入sql条件拼接
     3.实体字段可以使用@TableField(condition = SqlCondition.LIKE)或者手写条件:condition = "%s&gt;#{%s}",默认eq
     4.实体的条件和wrapper的条件会一起使用，如果实体和wrapper重复，会重复拼接条件
     */
    @Test
    public void mpTest8() {
        Plan queryPlan = new Plan();
        queryPlan.setOrderNo("101ZP201906060001");
        queryPlan.setLocno("101");
        QueryWrapper<Plan> queryWrapper = new QueryWrapper<>(queryPlan);
        queryWrapper.or().eq("locno","102");
        List<Plan> plans = planMapper.selectList(queryWrapper);
        System.out.println(plans);
    }

    /*
     --------------------------------------------------------------------------------------------allEq
    *--------------------------------------------------------------------------------------------allEq:全部eq(或个别isNull),有很多重载方法，根据需求选择
    * */
    @Test
    public void mpTest9() {
        QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
        Map<String,Object> map = new HashMap<>();
        map.put("locno","101");
        //默认null会拼接为 order_no is null
        map.put("order_no",null);
        //默认 null2IsNull 为true（拼接null为 is null），false的话，忽略null的字段
        queryWrapper.allEq(map,false);
        List<Plan> plans = planMapper.selectList(queryWrapper);
        System.out.println(plans);
    }

    /*
    ---------------------------------------------------------------------------------------------分业查询:
    必须将分页插件PaginationInterceptor注入到spring,否则查询的是所有记录(没有分页||逻辑分页)
    */
    @Test
    public void mpTest3() {
        /*searchCount:是否查询总条数*/
        IPage<Plan> page = planMapper.selectPage(new Page(1, 10).setSearchCount(false),null);
        List<Plan> records = page.getRecords();
        System.out.println(page);
    }

    /*
    --------------------------------------------------------------------------------------------自定义sql时：customSqlSegment属性:翻译后的mysql的where和后面的条件
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
    ---------------------------------------------------------------------------------------------自定义分页查询，
    注意Wrapper的getCustomSqlSegment():获取所有条件
    */
    @Test
    public void mpTest4() {
        QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("locno","101");
        IPage<Plan> page = planMapper.selectCustomPage(new Page(1,10),queryWrapper);
        List<Plan> records = page.getRecords();
        System.out.println(page);
    }

    /*
    ---------------------------------------------------------------------------------------------自定义分页关联查询：
    1.xml写sql，只查询Plan主表部分，用Plan这个Entity接收即可
    2.Mapper使用@Select注解写Sql，返回主表和次表的字段，用Map接收返回*/
    @Test
    public void mpTest5() {
        Map<String,Object> map = new HashMap<>();
        map.put("locno","101");
//        IPage<Plan> page = planMapper.selectRelationPage(new Page(1,10),map);
//        List<Plan> records = page.getRecords();
//        System.out.println(page);
        IPage<Map<String,Object>> page = planMapper.selectRelationPage1(new Page(1,10),map);
        System.out.println(page);
    }

    /*
    ---------------------------------------------------------------------------------------------Lambda 条件构造器
                                                                                                                        and()等使用
                                                                                                                        特殊的Lambda构造器
     and(),or()等
    */
    @Test
    public void mpTest10() {
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


}
