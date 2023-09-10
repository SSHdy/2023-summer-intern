package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.housemanager.entity.ReportEnclosure;
import com.ryzw.housemanager.vo.AdressAndNameVo;
import com.ryzw.housemanager.vo.ReportUnitYearVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 上报附件表 Mapper 接口
 * </p>
 *
 * @author yz
 * @since 2019-08-29
 */
public interface ReportEnclosureMapper extends BaseMapper<ReportEnclosure> {

    /**
     * 查询所有单位上报文件按年份
     *
     * @param reportUnitVo
     * @return
     */
    List<AdressAndNameVo> allReportEnclosure(@Param("reportUnitYearVo") ReportUnitYearVo reportUnitYearVo);
}
