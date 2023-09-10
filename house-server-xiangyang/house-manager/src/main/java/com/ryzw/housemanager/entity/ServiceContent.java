package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 物业服务内容表
 * </p>
 *
 * @author mfl
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ServiceContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物业服务内容编号
     */
    @TableId(value = "service_content_id", type = IdType.AUTO)
    private Long serviceContentId;

    /**
     * 院落物业编号
     */
    private Long yardEstateId;

    /**
     * 服务内容
     */
    private Long serviceContent;


}
