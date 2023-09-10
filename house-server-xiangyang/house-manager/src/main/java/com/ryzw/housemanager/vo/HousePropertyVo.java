package com.ryzw.housemanager.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * 添加房产信息，前端需要传输的参数
 * @author zf
 * @since 2019/3/11
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HousePropertyVo {

    /**
     * 房产证号
     */
    private String houseId;

    /**
     * 楼/栋号
     */
    private String buildId;

    /**
     * 登记时间
     */
    private Date registrationTime;

    /**
     * 所有权人
     */
    private String owner;

    /**
     * 其他
     */
    private String other;

    /**
     * 房屋性质
     */
    private String houseCharacter;

    /**
     * 共有情况
     */
    private String common;

    /**
     * 地号
     */
    private String landNum;

    /**
     * 规划用途
     */
    private String planningPurpose;

    /**
     * 总层数
     */
    private String totalLayerNum;

    /**
     * 土地取得方式
     */
    private String landMethod;

    /**
     *土地使用开始年限
     */
    private Date startUseLand;

    /**
     * 建筑面积
     */
    private String buildArea;

    /**
     * 发证日期
     */
    private Date certification;

    /**
     *土地使用结束年限
     */
    private Date endUseLand;

    /**
     * 套内建筑面积
     */
    private String innerBuildArea;

    /**
     * 发证机关
     */
    private String sendOffice;

    /**
     * 附记
     */
    private String attachment;

    /**
     * 备注
     */
    private String remark;




















}
