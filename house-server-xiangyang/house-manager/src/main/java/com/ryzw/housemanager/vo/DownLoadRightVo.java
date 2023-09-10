package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 下载产权照片
 * </p>
 *
 * @author zf
 * @since 2018/3/29
 */
@Data
public class DownLoadRightVo {

    /**
     * 下载路径集合
     */
    private List<String> addressList;

    /**
     * 下载类型
     */
    private Integer downLoadType;

    /**
     * 产权证Id
     */
    private String propertyRightId;

    /**
     * 维修申请Id
     */
    private Long repairApplyId;
}
