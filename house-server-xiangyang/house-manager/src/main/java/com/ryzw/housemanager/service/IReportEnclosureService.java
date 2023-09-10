package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.DownLoadDto;
import com.ryzw.housemanager.entity.ReportEnclosure;
import com.ryzw.housemanager.vo.AdressAndNameVo;
import com.ryzw.housemanager.vo.DownLoadFiletVo;
import com.ryzw.housemanager.vo.ReportUnitYearVo;

import java.util.List;

/**
 * <p>
 * 上报附件表 服务类
 * </p>
 *
 * @author yz
 * @since 2019-08-29
 */
public interface IReportEnclosureService extends IService<ReportEnclosure> {

    /**
     * 单位上报
     *
     * @param reportEnclosure
     * @return
     */
    boolean reportUnit(ReportEnclosure reportEnclosure);

    /**
     * 单位上报文件下载
     *
     * @param downLoadFiletVo
     * @return
     */
    List<DownLoadDto> downLoad(DownLoadFiletVo downLoadFiletVo);

    /**
     * 查询所有单位上报文件按年份
     *
     * @param reportUnitYearVo
     * @return
     */
    List<AdressAndNameVo> allReportEnclosure(ReportUnitYearVo reportUnitYearVo);
}
