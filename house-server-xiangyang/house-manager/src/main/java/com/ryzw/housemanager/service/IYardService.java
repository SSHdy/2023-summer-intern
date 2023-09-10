package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.Yard;
import com.ryzw.housemanager.vo.*;

import java.util.List;

/**
 * <p>
 * 院落信息表 服务类
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
public interface IYardService extends IService<Yard> {

    /**
     * 院落信息列表
     *
     * @param yard
     * @return
     */
    Page<Yard> list(YardVo yard);


    /**
     * 按权限查询所有院落
     *
     * @param unitIds
     * @return
     */
    List<YardResultVo> getAllByUnitID(List<Long> unitIds, Long searchYardId);

    /**
     * 查询人工分房的院落信息
     *
     * @param unitIds
     * @return
     */
    List<YardResultVo> getYardHandle(List<Long> unitIds, Long searchYardId);


    /**
     * 按权限查询所有院落
     *
     * @param unitIds
     * @return
     */
    List<YardResultVo> getAllByUnitID(List<Long> unitIds);

    /**
     * 查询所有院落信息
     *
     * @return
     */
    List<YardResultVo> getAll(Long searchId,List<Long> unitIds);


    /**
     * 删除院落，查询下面是否有楼座
     *
     * @param yardId
     * @return
     */
    boolean deleteYard(Long yardId);

    /**
     * 查询单位下的院落信息
     *
     * @param unitTreeVo
     * @return
     */
    List<UnitYardDto> unitYard(UnitTreeVo unitTreeVo);

    /**
     * 查询院落关联的楼座
     *
     * @param basicVo
     * @return
     */
    YardBuildDto yardBuilds(BasicVo<Long> basicVo);

    /**
     * 查询用户权限下的院落位置信息
     *
     * @param unitIds
     * @return
     */
    List<YardPositionDto> yardPosition(List<Long> unitIds);

    /**
     * 添加院落信息
     *
     * @param yardAddVo
     * @return
     */
    boolean addYard(YardAddVo yardAddVo);

    /**
     * 修改院落信息
     *
     * @param yardAddVo
     * @return
     */
    boolean updateYard(YardAddVo yardAddVo);

    /**
     * 根据院落编号查询物业关系
     *
     * @param yardId
     * @return
     */
    YardEstateDto selectEstate(Long yardId);

    /**
     * 查询院落详情
     *
     * @param basicVo
     * @return
     */
    YardDetailDto yardDetail(BasicVo<Long> basicVo);

    /**
     * 判断院落/土地下是否有出租出借
     *
     * @param yardId
     * @return
     */
    Integer isHaveRent(Long yardId);



    /**
     * 市（州、盟）办公用房报表信息查询
     *
     * @return
     */
    List<YardUnitProfessionalDto> yardUnitReport();
}
