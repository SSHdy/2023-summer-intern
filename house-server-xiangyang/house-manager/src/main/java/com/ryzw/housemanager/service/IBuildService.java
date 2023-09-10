package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.Build;
import com.ryzw.housemanager.entity.FloorDrawing;
import com.ryzw.housemanager.entity.OutRent;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.BuildListVo;
import com.ryzw.housemanager.vo.BuildVo;
import com.ryzw.housemanager.vo.FloorNameVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 楼座基本信息表 服务类
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
public interface IBuildService extends IService<Build> {

    /**
     * 楼栋列表
     *
     * @param buildListVo
     * @return
     */
    Page<BuildDto> list(BuildListVo buildListVo);

    /**
     * 楼栋详情
     *
     * @param buildId
     * @return
     */
    Object detail(Long buildId);

    /**
     * 添加楼栋信息
     *
     * @param build
     * @param floorImgUrls
     * @param outRent
     * @param floorDrawingList
     */
    void addBuild(Build build, List<Map<String, Object>> floorImgUrls, OutRent outRent, List<FloorDrawing> floorDrawingList);

    /**
     * 删除楼座信息和楼座照片
     *
     * @param build
     * @return
     */
    boolean deleteBuildAndFloorImg(Build build);

    /**
     * 修改build
     *
     * @param build
     * @param buildVo
     * @return
     */
    boolean updateFloorImgByBuild(Build build, BuildVo buildVo);

    /**
     * 根据院落id楼座id查询对应的重大设备信息
     *
     * @param buildVo
     * @return
     */
    List<EquipmentBuildDto> selectBuildAndEquipment(BuildVo buildVo);

    /**
     * 查询某个楼栋下的单位信息和人员职称信息
     *
     * @param basicVo
     * @return
     */
    Map<Object, Object> unitPersonInfo(BasicVo<Long> basicVo);


    /**
     * 查询单位集合下的所有楼栋
     *
     * @param yardId
     * @param unitIds
     * @return
     */
    List<BuildUnitDto> buildUnit(Long yardId, List<Long> unitIds);

    /**
     * 查询用户权限下关联合同的所有楼栋
     *
     * @param yardId
     * @param unitIds
     * @return
     */
    List<BuildUnitDto> buildContract(Long yardId, List<Long> unitIds);

    /**
     * 查询用户权限下的所有关于处置的楼栋
     *
     * @param yardId
     * @param unitIds
     * @return
     */
    List<BuildUnitDto> buildUnitHandle(Long yardId, List<Long> unitIds);

    /**
     * 查询一个用户单位集合下所有房间
     *
     * @param buildId
     * @param id
     * @param unitIds
     * @return
     */
    List<BuildUnitDto> houseList(Long id, Long buildId, List<Long> unitIds);

    /**
     * 查询一个用户单位集合下所有处置的房间
     *
     * @param buildId
     * @param id
     * @param unitIds
     * @return
     */
    List<BuildUnitDto> houseListHandle(Long id, Long buildId, List<Long> unitIds);

    /**
     * 查询一个用户单位集合下所有的楼层
     *
     * @param unitIds
     * @return
     */
    List<BuildUnitDto> floorList(Long buildId, List<Long> unitIds);

    /**
     * 查询一个用户单位集合下所有的楼层
     *
     * @param unitIds
     * @return
     */
    List<BuildUnitDto> floorListHandle(Long buildId, List<Long> unitIds);

    /**
     * 查询用户权限下的楼栋位置信息查询
     *
     * @param unitIds
     * @return
     */
    List<BuildPositionDto> buildPosition(Long yardId, List<Long> unitIds);


    /**
     * 根据楼座查询楼层信息
     *
     * @param buildId
     * @return
     */
    BuildFloorDto getBuildFloor(Long buildId);

    /**
     * 查询某个楼栋下的所有单位名称
     *
     * @param basicVo
     * @return
     */
    List<String> unitNameList(BasicVo<Long> basicVo);

    /**
     * 产权楼栋树
     *
     * @return
     */
    List<HandlePositionDto> rightBuildTree();

    /**
     * 判断楼层集合中是否存在房间，如果有，就不能减少楼栋的楼层
     * @param floorNameVo
     * @return
     */
    boolean noHouseCharNull( FloorNameVo floorNameVo);
}
