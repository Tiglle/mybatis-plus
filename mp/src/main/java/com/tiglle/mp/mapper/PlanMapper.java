package com.tiglle.mp.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiglle.mp.entity.Plan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
     * 自定义分页 关联查询(只返回主表的字段)
     * @param page
     * @param state
     * @return
     */
    IPage<Plan> selectPageRelation(Page page, @Param(Constants) Integer state);

}
