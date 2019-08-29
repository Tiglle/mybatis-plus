package com.tiglle.mp.entity;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import java.time.LocalDateTime;
    import java.io.Serializable;

    import com.baomidou.mybatisplus.annotation.TableName;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 增值服务计划单明细表
    * </p>
*
* @author tiglle
* @since 2019-08-29
*/
    @Data
    public class PlanDtl implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 主键id
            */
            @TableId(value = "id", type = IdType.AUTO)
    private Long id;

            /**
            * 增值服务计划单主表编码
            */
    private String planOrderNo;

            /**
            * 货主编码
            */
    private String customerNo;

            /**
            * 库区名称
            */
    private String zoneNo;

            /**
            * 巷道名称
            */
    private String roadway;

            /**
            * 货位
            */
    private String location;

            /**
            * 托盘号
            */
    private String palNo;

            /**
            * 箱号
            */
    private String boxNo;

            /**
            * 原SKU
            */
    private String oldSku;

            /**
            * 新SKU
            */
    private String newSku;

            /**
            * 库存id
            */
    private Long contentId;

            /**
            * 库存批次号
            */
    private String contentPropertyNo;

            /**
            * 库存子件码
            */
    private String subCode;

    private String productName;

            /**
            * 商品品质 0正品 1次品 2残品
            */
    private String quality;

            /**
            * 计划数量
            */
    private Integer planNum;

            /**
            * 实际数量
            */
    private Integer actualNum;

            /**
            * 差异数量
            */
    private Integer differenceNum;

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
    private LocalDateTime updateTime;

            /**
            * 删除标识(0-正常,1-删除)
            */
    private Integer delFlag;

            /**
            * 日志跟踪id
            */
    private String traceId;


}
