package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 文件下载
 * </p>
 *
 * @author zf
 * @since 2018/4/12
 */
@Data
public class DownLoadFiletVo {

    /**
     * 下载路径集合
     */
    private List<AdressAndNameVo> addressList;

    /**
     * 配置申请id
     */
    private Long configureId;

    /**
     * 下载类型
     */
    private Integer downLoadType;

    /**
     * 维修申请Id
     */
    private Long repairApplyId;

    /**
     * 处置申请Id
     */
    private Long handleId;


}
