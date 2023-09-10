package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.DownLoadDto;
import com.ryzw.housemanager.entity.ReportEnclosure;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.ReportEnclosureMapper;
import com.ryzw.housemanager.service.IReportEnclosureService;
import com.ryzw.housemanager.vo.AdressAndNameVo;
import com.ryzw.housemanager.vo.DownLoadFiletVo;
import com.ryzw.housemanager.vo.ReportUnitYearVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 上报附件表 服务实现类
 * </p>
 *
 * @author yz
 * @since 2019-08-29
 */
@Service
@Slf4j
public class ReportEnclosureServiceImpl extends ServiceImpl<ReportEnclosureMapper, ReportEnclosure> implements IReportEnclosureService {

    @Autowired
    private ReportEnclosureMapper reportEnclosureMapper;

    @Value("${uploadUrl.url}")
    private String url;

    /**
     * 单位上报
     *
     * @param reportEnclosure
     * @return
     */
    @Override
    public boolean reportUnit(ReportEnclosure reportEnclosure) {
        try {
            Date currDate = new Date();
            reportEnclosure.setReportDate(currDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String yearFormat = sdf.format(currDate);
            ReportEnclosure delReport = reportEnclosureMapper.selectOne(new QueryWrapper<ReportEnclosure>().eq("report_id", reportEnclosure.getReportId()).eq("report_year", Integer.valueOf(yearFormat)));
            //如果上传文件已经存在就删除已存在文件,再重新保存
            if (delReport != null) {
                reportEnclosureMapper.deleteById(delReport.getReportEnclosureId());
                this.deleteEnclosure(delReport.getReportEnclosurePhysicsUrl());
            }
            //新增单位上报文件
            reportEnclosure.setReportYear(Integer.valueOf(yearFormat));
            reportEnclosureMapper.insert(reportEnclosure);
            return true;
        } catch (Exception ex) {
            log.error("reportUnitError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 单位上报文件下载
     *
     * @param downLoadFiletVo
     * @return
     */
    @Override
    public List<DownLoadDto> downLoad(DownLoadFiletVo downLoadFiletVo) {
        try {
            List<AdressAndNameVo> addressList = downLoadFiletVo.getAddressList();
            List<DownLoadDto> addressListDto = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(addressList)) {
                for (AdressAndNameVo adressAndNameVo : addressList) {
                    if (addressList.size() > 1) {
                        DownLoadDto downLoadDto = new DownLoadDto();
                        downLoadDto.setDownLoadName(adressAndNameVo.getFileName());
                        downLoadDto.setPath("file:///" + adressAndNameVo.getAddress());
                        addressListDto.add(downLoadDto);
                    } else {
                        DownLoadDto downLoadDto = new DownLoadDto();
                        downLoadDto.setDownLoadName(adressAndNameVo.getFileName());
                        downLoadDto.setPath(adressAndNameVo.getAddress());
                        addressListDto.add(downLoadDto);
                    }
                }
            }
            return addressListDto;
        } catch (Exception ex) {
            log.error("downLoadError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询所有单位上报文件按年份
     *
     * @param reportUnitYearVo
     * @return
     */
    @Override
    public List<AdressAndNameVo> allReportEnclosure(ReportUnitYearVo reportUnitYearVo) {
        try {
            return reportEnclosureMapper.allReportEnclosure(reportUnitYearVo);
        } catch (Exception ex) {
            log.error("allReportEnclosureError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 删除公共服务器上的单位上报文件
     *
     * @param physicsUrl
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteEnclosure(String physicsUrl) {
        try {
            //文件路径
            String imagePath = physicsUrl;
            if (StringUtils.isNotEmpty(imagePath)) {
                synchronized (this) {
                    Files.deleteIfExists(Paths.get(imagePath));
                }
            }
        } catch (Exception ex) {
            log.error("deleteEnclosureError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
}
