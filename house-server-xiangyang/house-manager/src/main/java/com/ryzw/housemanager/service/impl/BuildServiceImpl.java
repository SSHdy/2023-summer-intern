package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.*;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.*;
import com.ryzw.housemanager.service.IBuildService;
import com.ryzw.housemanager.service.IConfigureRoomService;
import com.ryzw.housemanager.service.IConfigureService;
import com.ryzw.housemanager.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 楼座基本信息表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
@Service
@Slf4j
public class BuildServiceImpl extends ServiceImpl<BuildMapper, Build> implements IBuildService {

    @Autowired
    BuildMapper buildMapper;
    @Autowired
    FloorImgMapper floorImgMapper;
    @Autowired
    HouseMapper houseMapper;
    @Autowired
    HousingUseMapper housingUseMapper;
    @Autowired
    UseRelationshipMapper useRelationshipMapper;
    @Autowired
    LeadingOfficeMapper leadingOfficeMapper;
    @Autowired
    UnitMapper unitMapper;
    @Autowired
    BuildServiceImpl buildService;
    @Autowired
    OutRentMapper outRentMapper;
    @Autowired
    FloorDrawingMapper floorDrawingMapper;
    @Autowired
    RepairRemindMapper repairRemindMapper;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    ConfigureBuildMapper configureBuildMapper;
    @Autowired
    @Value("${uploadUrl.url}")
    private String url;

    /**
     * 楼栋列表
     *
     * @param buildListVo
     * @return
     */
    @Override
    public Page<BuildDto> list(BuildListVo buildListVo) {
        try {
            Page<BuildDto> page = new Page<>(buildListVo.getCurrentPage(), buildListVo.getPageSize());
            return buildMapper.list(page, buildListVo);
        } catch (Exception ex) {
            log.error("listError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 楼栋详情
     *
     * @param buildId
     * @return
     */
    @Override
    public Object detail(Long buildId) {
        try {
            String redisKey = "build:" + buildId + ":detail";
            if (redisTemplate.hasKey(redisKey)) {
                return redisTemplate.opsForValue().get(redisKey);
            } else {
                BuildDetailDto buildDetailDto = buildMapper.detail(buildId);
                redisTemplate.opsForValue().set(redisKey, buildDetailDto, 7, TimeUnit.DAYS);
                return buildDetailDto;
            }
        } catch (Exception ex) {
            log.error("detailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 新增build,并新增build下面的楼座图片
     *
     * @param build
     * @param newFloorImgUrlList
     * @param outRent
     * @param floorDrawingList
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addBuild(Build build, List<Map<String, Object>> newFloorImgUrlList, OutRent outRent, List<FloorDrawing> floorDrawingList) {
        try {
            //新增build信息
            buildMapper.insert(build);
            //获取新增build的id
            Long buildId = build.getBuildId();
            if (outRent != null) {
                outRent.setBuildId(buildId);
                //新增出租信息
                outRentMapper.insert(outRent);
            }
            if (CollectionUtils.isNotEmpty(floorDrawingList)) {
                for (FloorDrawing floorDrawing : floorDrawingList) {
                    //循环新增楼座cad
                    floorDrawing.setBuildId(buildId);
                    floorDrawingMapper.insert(floorDrawing);
                }
            }
            //循环添加imgUrl
            Optional.ofNullable(newFloorImgUrlList).ifPresent(s -> s.stream()
                    .map(p -> mapToFloorImg(buildId, p)).forEach(floorImgMapper::insert));
        } catch (Exception ex) {
            log.error("addBuildError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 新增build,并新增build下面的楼座图片
     *
     * @param build
     * @param newFloorImgUrlList
     */
    @Transactional(rollbackFor = Exception.class)
    public Long addConfigureBuild(Build build, List<Map<String, Object>> newFloorImgUrlList, OutRent outRent,List<FloorDrawing> floorDrawingList) {
        try {
            //新增build信息
            buildMapper.insert(build);
            //获取新增build的id
            Long buildId = build.getBuildId();
            if (outRent != null) {
                outRent.setBuildId(buildId);
                //新增出租信息
                outRentMapper.insert(outRent);
            }
            if (CollectionUtils.isNotEmpty(floorDrawingList)) {
                for (FloorDrawing floorDrawing : floorDrawingList) {
                    //循环新增楼座cad
                    floorDrawing.setBuildId(buildId);
                    floorDrawingMapper.insert(floorDrawing);
                }
            }
            //循环添加imgUrl
            Optional.ofNullable(newFloorImgUrlList).ifPresent(s -> s.stream()
                    .map(p -> mapToFloorImg(buildId, p)).forEach(floorImgMapper::insert));
            return buildId;
        } catch (Exception ex) {
            log.error("addConfigureBuildError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改build
     *
     * @param build
     * @param buildVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateFloorImgByBuild(Build build, BuildVo buildVo) {
        try {
            //通过楼座id获取需要删除数据库图片信息，删除服务器图片
            Long buildId = build.getBuildId();

            //删除旧楼座照片的数据库数据和删除服务器所在楼座图片
            if (CollectionUtils.isNotEmpty(buildVo.getOldFloorImgIdList())) {
                List<FloorImg> floorImgs = floorImgMapper.selectBatchIds(buildVo.getOldFloorImgIdList());
                if (CollectionUtils.isNotEmpty(floorImgs)) {
                    buildService.deleteFloorImg(floorImgs);
                }
            }
            if (CollectionUtils.isNotEmpty(buildVo.getFloorDrawingList())) {
                for (FloorDrawing floorDrawing : buildVo.getFloorDrawingList()) {
                    //循环新增新的楼座cad
                    floorDrawingMapper.insert(floorDrawing);
                }
            }
            if (CollectionUtils.isNotEmpty(buildVo.getOldCadIdList())) {
                List<FloorDrawing> floorDrawings = floorDrawingMapper.selectBatchIds(buildVo.getOldCadIdList());
                //删除数据库cad
                floorDrawingMapper.deleteBatchIds(buildVo.getOldCadIdList());
                if (CollectionUtils.isNotEmpty(floorDrawings)) {
                    for (FloorDrawing floorDrawing : floorDrawings) {
                        //删除服务器上cad
                        this.deleteCad(floorDrawing.getFloordrawingUrl());
                    }
                }
            }
            //删除数据库与服务器--楼层上的cad
            if (CollectionUtils.isNotEmpty(buildVo.getDelFloorList())) {
                List<Integer> delFloorList = buildVo.getDelFloorList();
                List<FloorDrawing> floorDrawings = floorDrawingMapper.selectList(new QueryWrapper<FloorDrawing>().eq("build_id", buildId).in("floor", delFloorList));
                if (CollectionUtils.isNotEmpty(floorDrawings)) {
                    for (FloorDrawing floorDrawing : floorDrawings) {
                        //删除服务器上cad
                        this.deleteCad(floorDrawing.getFloordrawingUrl());
                        //删数据库cad
                        floorDrawingMapper.deleteById(floorDrawing.getFloordrawingId());
                    }
                }
            }

            //新增新的图片
            Optional.ofNullable(buildVo.getNewFloorImgUrlList()).ifPresent(s -> s.stream()
                    .map(p -> mapToFloorImg(buildId, p)).forEach(floorImgMapper::insert));

            //修改
            buildMapper.updateById(build);
            if (buildVo.getOutRent() != null && buildVo.getOutRent().getOutRentId() != null) {
                outRentMapper.updateById(buildVo.getOutRent());
            } else {
                outRentMapper.insert(buildVo.getOutRent());
            }

            deleteBuildRedis(buildId);

            return true;
        } catch (Exception ex) {
            log.error("updateFloorImgByBuildError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 转换为FloorImg对象
     *
     * @param buildId
     * @param objectMap
     * @return
     */
    private FloorImg mapToFloorImg(Long buildId, Map<String, Object> objectMap) {
        try {
            FloorImg floorImg = new FloorImg();
            floorImg.setFloorId(buildId);
            floorImg.setFloorimgUrl(String.valueOf(objectMap.get("floorimgUrl")));
            floorImg.setFloorVirtualUrl(String.valueOf(objectMap.get("floorVirtualUrl")));
            return floorImg;
        } catch (Exception ex) {
            log.error("mapToFloorImgError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 删除楼座和楼座照片
     *
     * @param build
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBuildAndFloorImg(Build build) {
        try {
            //通过bulid查id
            Long buildId = build.getBuildId();
            //通过楼座id查询下面是否有房屋
            Integer count = houseMapper.selectCount(new QueryWrapper<House>().eq("build_id", buildId));
            if (count > 0) {
                throw new ServiceException("楼座下面挂有房屋信息，无法删除");
            }
            int RemindCount = repairRemindMapper.selectCount(new QueryWrapper<RepairRemind>().eq("build_id", buildId)
                    .gt("remaind_time", new Date()));
            if (RemindCount > 0) {
                throw new ServiceException("楼座有未到期维修提醒，无法删除!");
            }
            int configureBuildCount = configureBuildMapper.selectCount(new QueryWrapper<ConfigureBuild>().eq("build_id", buildId));
            if (configureBuildCount > 0) {
                throw new ServiceException("该楼座是配置关联楼座，无法删除!");
            }
            //删除出租信息
            outRentMapper.delete(new QueryWrapper<OutRent>().eq("build_id", build.getBuildId()));
            //删除楼座信息
            buildMapper.deleteById(build);
            //查询楼座下的相关楼座照片
            List<FloorImg> floorImgList = floorImgMapper.selectByBuildId(buildId);
            //删除服务器上对应的楼座照片
            if (CollectionUtils.isNotEmpty(floorImgList)) {
                buildService.deleteFloorImg(floorImgList);
            }
            List<FloorDrawing> floorDrawingList = floorDrawingMapper.selectList(new QueryWrapper<FloorDrawing>().eq("build_id", buildId));
            if (CollectionUtils.isNotEmpty(floorDrawingList)) {
                for (FloorDrawing floorDrawing : floorDrawingList) {
                    //删除服务器上cad
                    this.deleteCad(floorDrawing.getFloordrawingUrl());
                }
                //删除数据库cad
                floorDrawingMapper.delete(new QueryWrapper<FloorDrawing>().eq("build_id", buildId));
            }
            deleteBuildRedis(buildId);
            return true;
        } catch (Exception ex) {
            log.error("deleteBuildAndFloorImgError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除Redis缓存
     *
     * @param buildId
     */
    private void deleteBuildRedis(Long buildId) {
        try {
            String detailKey = "build:" + buildId + ":detail";
            String assertKey = "build:" + buildId + ":assert";
            String buildCoeKey = "buildCoe:" + buildId;
            if (redisTemplate.hasKey(detailKey)) {
                redisTemplate.delete(detailKey);
            }

            if (redisTemplate.hasKey(assertKey)) {
                redisTemplate.delete(assertKey);
            }

            if (redisTemplate.hasKey(buildCoeKey)) {
                redisTemplate.delete(buildCoeKey);
            }
        } catch (Exception ex) {
            log.error("deleteBuildRedisError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除公共服务器上的楼座图片方法
     *
     * @param floorImgs
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteFloorImg(List<FloorImg> floorImgs) {
        floorImgMapper.deleteBatchIds(floorImgs.stream().map(FloorImg::getFloorimgId).collect(Collectors.toList()));

        List<String> imagePaths = floorImgs.stream().map(p -> p.getFloorimgUrl()).collect(Collectors.toList());

        try {
            if (CollectionUtils.isNotEmpty(imagePaths)) {
                for (String imagePath : imagePaths) {
                    if (StringUtils.isNotEmpty(imagePath)) {
                        synchronized (this) {
                            Files.deleteIfExists(Paths.get(imagePath));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("delete Floor Image error", ex);
            throw new ServiceException("删除楼座图片有错！");
        }
    }

    @Override
    public List<EquipmentBuildDto> selectBuildAndEquipment(BuildVo buildVo) {
        try {
            return buildMapper.selectBuildAndEquipment(buildVo);
        } catch (Exception ex) {
            log.error("selectBuildAndEquipmentError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询某个楼栋下的单位信息和人员职称信息
     *
     * @param basicVo
     * @return
     */
    @Override
    public Map<Object, Object> unitPersonInfo(BasicVo basicVo) {
        try {
            String redisKey = "build:" + basicVo.getId() + ":assert";

            if (redisTemplate.hasKey(redisKey)) {
                return redisTemplate.opsForHash().entries(redisKey);
            } else {

                HashMap<Object, Object> hashMap = new HashMap<>();
//        //查询某个楼栋下所有的单位名称
                List<String> unitNameList = buildMapper.unitNameList(basicVo);

//            查询某个楼栋下承租方名称列表
                List<String> renterNameList = buildMapper.renterNameList(basicVo);
                if (CollectionUtils.isNotEmpty(renterNameList)) {
                    unitNameList.addAll(renterNameList);
                }
//            //查询某个楼栋下的单位数量
                Integer countUnits = CollectionUtils.isNotEmpty(unitNameList) ? unitNameList.size() : 0;

                //查询某栋楼入驻人员数量
                Integer roomNums = buildMapper.buildPersonNum(basicVo);

                //查询某栋楼办公室数量
                Integer buildOfficeNum = buildMapper.buildOfficeNum(basicVo);

//            查询某栋楼出租房的面积
                Float rent = buildMapper.rentArea(basicVo);

//            查询闲置用房的面积
                Float spareArea = buildMapper.spareArea(basicVo);

//            查询自用面积
                Float selfUseArea = buildMapper.selfUseArea(basicVo);

//        拼接单位名称
                String unitNames = String.join(",", unitNameList);

                //把数据添加到dto
                hashMap.put("useUnitName", unitNames);
                hashMap.put("useUnitNum", countUnits);
                hashMap.put("enterPeopleNum", Optional.ofNullable(roomNums).orElse(0));
                hashMap.put("officeNum", buildOfficeNum);
                hashMap.put("rentArea", Optional.ofNullable(rent).orElse(0F));
                hashMap.put("spareArea", Optional.ofNullable(spareArea).orElse(0F));
                hashMap.put("selfUseArea", Optional.ofNullable(selfUseArea).orElse(0F));

                redisTemplate.opsForHash().putAll(redisKey, hashMap);
                if (redisTemplate.hasKey(redisKey)) {
                    redisTemplate.expire(redisKey, 7, TimeUnit.DAYS);
                }
                return hashMap;
            }
        } catch (Exception ex) {
            log.error("unitPersonInfoError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询单位集合下的所有楼栋
     *查询用户权限下关联合同的所有楼栋
     * @param unitIds
     * @return
     */
    @Override
    public List<BuildUnitDto> buildUnit(Long yardId, List<Long> unitIds) {
        return buildMapper.buildUnit(yardId, unitIds);
    }

    /**
     * 查询用户权限下关联合同的所有楼栋
     *
     * @param unitIds
     * @return
     */
    @Override
    public List<BuildUnitDto> buildContract(Long yardId, List<Long> unitIds) {
        return buildMapper.buildContract(yardId, unitIds);
    }

    /**
     * 查询用户权限下的所有关于处置的楼栋
     *
     * @param yardId
     * @param unitIds
     * @return
     */
    @Override
    public List<BuildUnitDto> buildUnitHandle(Long yardId, List<Long> unitIds) {
        return buildMapper.buildUnitHandle(yardId, unitIds);
    }

    /**
     * 查询一个用户单位集合下所有的楼层
     *
     * @param unitIds
     * @return
     */
    @Override
    public List<BuildUnitDto> floorList(Long buildId, List<Long> unitIds) {
        return buildMapper.floorList(buildId, unitIds);
    }

    /**
     * 查询一个用户单位集合下所有处置的楼层
     *
     * @param unitIds
     * @return
     */
    @Override
    public List<BuildUnitDto> floorListHandle(Long buildId, List<Long> unitIds) {
        return buildMapper.floorListHandle(buildId, unitIds);
    }

    /**
     * 查询一个用户单位集合下所有房间
     *
     * @param buildId
     * @param id
     * @param unitIds
     * @return
     */
    @Override
    public List<BuildUnitDto> houseList(Long id, Long buildId, List<Long> unitIds) {
        return buildMapper.houseList(id, buildId, unitIds);
    }

    /**
     * 查询一个用户单位集合下所有处置的房间
     *
     * @param id
     * @param buildId
     * @param unitIds
     * @return
     */
    @Override
    public List<BuildUnitDto> houseListHandle(Long id, Long buildId, List<Long> unitIds) {
        return buildMapper.houseListHandle(id, buildId, unitIds);
    }

    /**
     * 查询用户权限下的楼栋位置信息查询
     *
     * @param unitIds
     * @return
     */
    @Override
    public List<BuildPositionDto> buildPosition(Long yardId, List<Long> unitIds) {
        return buildMapper.buildPosition(yardId, unitIds);
    }


    /**
     * 根据楼座查询楼层信息deleteBuildAndFloorImg
     *
     * @param buildId
     * @return
     */
    @Override
    public BuildFloorDto getBuildFloor(Long buildId) {
        return buildMapper.getBuildFloor(buildId);
    }

    /**
     * 查询某个楼栋下的单位信息
     *
     * @param basicVo
     * @return
     */
    @Override
    public List<String> unitNameList(BasicVo<Long> basicVo) {
        return buildMapper.unitNameList(basicVo);
    }

    /**
     * 产权楼栋树
     *
     * @return
     */
    @Override
    public List<HandlePositionDto> rightBuildTree() {
        try {
            return buildMapper.rightBuildTree();
        } catch (Exception ex) {
            log.error("rightBuildTreeError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除公共服务器上的cad
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteCad(String physicalAddress) throws Exception {
        // 文件路径
        String dataPath = url + physicalAddress;
        if (StringUtils.isNotEmpty(dataPath)) {
            synchronized (this) {
                Files.deleteIfExists(Paths.get(dataPath));
            }
        }
    }


    /**
     * 判断楼层集合中是否存在房间，如果有，就不能减少楼栋的楼层
     * @param floorNameVo
     * @return
     */
    @Override
    public boolean noHouseCharNull( FloorNameVo floorNameVo){
        try{
            QueryWrapper<House> wrapper = new QueryWrapper<>();
            wrapper.eq("build_id",floorNameVo.getBuildId());
            wrapper.in("floor_name",floorNameVo.getFloorName());
            //通过楼座id查询下面是否有房屋
            Integer count = houseMapper.selectCount(wrapper);
            if (count > 0) {
                throw new ServiceException("楼层集合中存在房间，请先删除房间，再修改楼层");
            }
            return true;
        }catch (Exception ex) {
            log.error("build/noHouseCharNull error", ex);
            throw new ServiceException(ex.getMessage());
        }

    }
}
