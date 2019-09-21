package com.tiglle.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiglle.mp.entity.Plan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 增值服务计划单表 Mapper 接口
 * </p>
 *
 * @author tiglle
 * @since 2019-08-29
 */
public interface PlanMapper extends BaseMapper<Plan> {

    /**
     * 自定义分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<Plan> selectCustomPage(Page page, @Param(Constants.WRAPPER) Wrapper<Plan> queryWrapper);

    /**
     * 自定义分页  关联查询 xml写sql方式
     * @param page
     * @param map
     * @return
     */
    IPage<Plan> selectRelationPage(Page page, @Param("map")Map<String,Object> map);

    /**
     * 自定义分页  关联查询 xml写sql方式
     * @param page
     * @param map
     * @return
     */
    @Select("select p.order_no,pd.plan_order_no from plan p left join plan_dtl pd on p.order_no = pd.plan_order_no where p.locno = #{map.locno}")
    IPage<Map<String,Object>> selectRelationPage1(Page page, Map<String, Object> map);

    /**
     * 自定义sql
     * @param wrapper
     * @return
     */
    @Select("select ${ew.sqlSelect} from plan ${ew.customSqlSegment}")
    List<Plan> customSelectList(@Param(Constants.WRAPPER) QueryWrapper<Plan> wrapper);
}
