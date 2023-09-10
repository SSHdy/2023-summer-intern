package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 查询某条维修申请的所有审批文件
 * </p>
 *
 * @author zf
 * @since 2019/8/2
 */
@Data
public class RepairApplyFilesDto {

    /**
     * 文件对应key
     */
    private String fileKey;

    /**
     * 文件集合
     */
    private List<Object> fileList;
}
