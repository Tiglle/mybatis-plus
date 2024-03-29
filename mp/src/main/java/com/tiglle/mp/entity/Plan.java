package com.tiglle.mp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 增值服务计划单表
 * </p>
 *
 * @author tiglle
 * @since 2019-08-29
 */
@Data
public class Plan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 仓库编码
     */
    @TableField(condition = SqlCondition.EQUAL)
    private String locno;

    /**
     * 货主编码
     */
    private String customerNo;

    /**
     * 单据编号
     */
    @TableField(condition = SqlCondition.LIKE)
    private String orderNo;

    /**
     * 状态(10新建、20已审核、30已取消、40已完结)
     */
    //大于
    @TableField(condition = "%s&gt;#{%s}")
    private String status;

    /**
     * 创建方式(0:接口下发 1:WMS手工创建 2:WMS自动生成 5-奇门  6-京东)
     */
    private String createFlag;

    /**
     * 发起方(1=货主、2=仓库)
     */
    private String initiator;

    /**
     * 服务来源(1=来料、2=库内)
     */
    private String serviceSource;

    /**
     * 服务原因编号
     */
    private String serviceReasonCode;

    /**
     * 处理方式编号
     */
    private String serviceDealCode;

    /**
     * 是否更换SKU(0否1是)
     */
    private String exchangeSku;

    /**
     * 处理结果
     */
    private String productQuality;

    /**
     * 在库状态(0=不在库,1=在库)
     */
    private String isInWarehouse;

    /**
     * 作业方式(1=原货位作业,2=修复台作业不上架,3=修复台作业上架)
     */
    private String taskType;

    /**
     * 来源单号
     */
    private String sourceOrderNo;

    /**
     * 计划总箱数
     */
    private Integer planBoxNum;

    /**
     * 计划总件数
     */
    private Integer planPiecesNum;

    /**
     * 实际总箱数
     */
    private Integer actualBoxNum;

    /**
     * 实际总件数
     */
    private Integer actualPiecesNum;

    /**
     * 差异总数
     */
    private Integer differenceNum;

    /**
     * 品类(1鞋，2服装，3包包，4其他)
     */
    private String productType;

    /**
     * 审核人id
     */
    private String verifyUserId;

    /**
     * 审核人名称
     */
    private String verifyUserName;

    /**
     * 复核时间时间
     */
    private LocalDateTime verifyTime;

    /**
     * 创建人id
     */
    private String createUserId;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改人id
     */
    private String updateUserId;

    /**
     * 修改人名称
     */
    private String updateUserName;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标识(0-正常,1-删除)
     */
    /*
    逻辑删除字段添加@TableLogic注解，可以配置局部 未删除value  和 已删除delval 的值
     */
    @TableLogic
    @TableField(select = false)
    private Integer delFlag;

    /**
     * 日志跟踪id
     */
    private String traceId;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 备注
     */
    @Version
    private Integer versionNum;


}
