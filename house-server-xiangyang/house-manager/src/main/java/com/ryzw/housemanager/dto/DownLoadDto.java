package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 * 附件下载参数
 * </p>
 *
 * @author zf
 * @since 2019/3/27
 */
@Data
public class DownLoadDto {

    /**
     * 附件路径
     */
    private String path;

    /**
     * 单个附件下载名称
     */
    private String downLoadName;
}
