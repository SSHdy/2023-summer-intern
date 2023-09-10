package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 下载模板表
 * </p>
 *
 * @author yz
 * @since 2019-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DownloadTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板编号
     */
    @TableId(value = "download_template_id", type = IdType.AUTO)
    private Long downloadTemplateId;

    /**
     * 模板文件名称
     */
    private String downloadTemplateName;

    /**
     * 模板文件虚拟地址
     */
    private String downloadTemplateVirtualUrl;


}
