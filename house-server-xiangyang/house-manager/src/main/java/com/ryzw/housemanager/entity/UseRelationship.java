package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 房间人员使用关系
 * </p>
 *
 * @author mfl
 * @since 2018-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UseRelationship implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "job_id", type = IdType.AUTO)
    private Long jobId;

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 职称编号
     */
    private Integer professionalId;

    /**
     * 单位序号
     */
    private Long unitId;

    /**
     * 房间使用人数
     */
    private Integer roomNum;

    public UseRelationship(){
    }

    public UseRelationship(Long jobId, String houseId, Integer professionalId, Long unitId, Integer roomNum) {
        this.jobId = jobId;
        this.houseId = houseId;
        this.professionalId = professionalId;
        this.unitId = unitId;
        this.roomNum = roomNum;
    }
}
