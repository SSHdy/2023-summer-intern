package com.ryzw.housemanager.service;

import com.ryzw.housemanager.config.TokenFeignConfiguration;
import com.ryzw.housemanager.dto.MenuChildAllDto;
import com.ryzw.housemanager.service.impl.AuthorityUnitServiceImpl;
import com.ryzw.housemanager.vo.BasicVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 调用远程权限获取可以查看的单位
 * </p>
 *
 * @author yz
 * @since 2019/1/31
 */
@FeignClient(serviceId = "house-authority", name = "house-authority",
        fallback = AuthorityUnitServiceImpl.class,
        configuration = TokenFeignConfiguration.class
)
public interface IAuthorityUnitService {


    /**
     * 按Token查询用户编号
     * @return
     */
    @RequestMapping(value = "/user/getIdByToken", method = RequestMethod.POST)
    List<Long> getUnitIdByToken();


    /**
     * 按单位查询用户编号
     * @param unitId
     * @return
     */
    @RequestMapping(value = "/user/getUserIdByUnit", method = RequestMethod.POST)
    List<Long> getUserIdByUnit(Long unitId);


    /**
     * 按单位查询角色
     * @param unitId
     * @return
     */
    @RequestMapping(value = "/role/selectCountByUnit", method = RequestMethod.POST)
    int selectCountByUnit(Long unitId);

    /**
     * 跨服务调用用户列表
     * @return
     */
    @RequestMapping(value = "/user/userList", method = RequestMethod.POST)
    List<Map<String, Object>> userList(List<Long> userIds);

    /**
     * 根据TOKEN获取用户信息
     * @return
     */
    @RequestMapping(value = "/user/getUserByToken", method = RequestMethod.POST)
    Map<String, Object> getUserByToken();

    /**
     * 按step查询维修用户id
     * @return
     */
    @RequestMapping(value = "/user/selectRepairByStep", method = RequestMethod.POST)
    List<String> selectRepairByStep(@RequestBody  Integer step);

    /**
     * 按step查询处置用户id
     * @return
     */
    @RequestMapping(value = "/user/selectHandleByStep", method = RequestMethod.POST)
    List<String> selectHandleByStep(@RequestBody  Integer step);

    /**
     * 按照用户权限获取有效的菜单列表
     * @return
     */
    @RequestMapping(value = "/menu/list", method = RequestMethod.POST)
    List<MenuChildAllDto> getListMenu(@RequestBody BasicVo<Long> basicVo);

    /**
     * 按step查询配置用户id
     * @return
     */
    @RequestMapping(value = "/user/selectConfigByStep", method = RequestMethod.POST)
    List<String> selectConfigureByStep(@RequestBody  Integer step);

}
