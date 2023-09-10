package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 下载附件信息
 * </p>
 *
 * @author zf
 * @since 2018/3/25
 */
@Data
public class DownLoadEnclosureVo {

    /**
     * 附件Id集合
     */
    private List<Long> enclosureIdList;

    /**
     * 出租出借Id
     */
    private Long rentId;

    /**
     * 下载类型
     */
    private Integer downLoadType;



}
