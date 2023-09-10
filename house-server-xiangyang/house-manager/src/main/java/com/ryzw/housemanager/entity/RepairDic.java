package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 维修提醒时间参数表
 * </p>
 *
 * @author mfl
 * @since 2019-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RepairDic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维修字典编号
     */
    @TableId(value = "repair_Dic_id", type = IdType.AUTO)
    private Long repairDicId;

    /**
     * 维修字典键
     */
    private String repairDicKey;

    /**
     * 维修字典值
     */
    private String repairDicValue;

    /**
     * 备注
     */
    private String repairDicRemark;







}
