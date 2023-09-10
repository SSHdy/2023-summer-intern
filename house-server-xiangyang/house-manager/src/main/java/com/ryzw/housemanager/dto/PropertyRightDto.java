package com.ryzw.housemanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 *产权信息输出
 * </p>
 *
 * @author zf
 * @since 2019/3/11
 */
@Data
@Document("propertyRight")
public class PropertyRightDto {

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
    private String registrationTime;

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
    private String startUseLand;

    /**
     * 建筑面积
     */
    private Float buildArea;

    /**
     * 发证日期
     */
    private String certificationTime;

    /**
     *土地使用结束年限
     */
    private String endUseLand;

    /**
     * 套内建筑面积
     */
    private Float innerBuildArea;

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




    /**
     * 土地证号
     */
    private String landCertificateId;

    /**
     * 地类
     */
    private String landType;

    /**
     * 取得价格
     */
    private Double getPrice;

    /**
     * 图号
     */
    private String pictureNum;

    /**
     * 使用权类型
     */
    private String usePropertyType;

    /**
     * 使用权面积
     */
    private Float usePropertyArea;

    /**
     * 使用权开始日期
     */
    private String startUseProperty;

    /**
     * 使用权结束日期
     */
    private String endUseProperty;

    /**
     * 住宅面积
     */
    private Float livingArea;

    /**
     * 分摊面积
     */
    private Float shareArea;

    /**
     * 商业面积
     */
    private Float businessArea;

    /**
     * 独用面积
     */
    private Float soleArea;




    /**
     * 权证编号
     */
    private String estateId;

    /**
     * 单元号
     */
    private String unitNum;

    /**
     *面积
     */
    private Float area;

    /**
     *使用期限
     */
    private Float accessTime;

    /**
     * 权利性质
     */
    private String propertyCharacter;

    /**
     * 用途
     */
    private String applicationTo;





















}
