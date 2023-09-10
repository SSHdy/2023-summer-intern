package com.ryzw.houseauthority.controller;


import com.ryzw.houseauthority.service.IUserRepairService;
import com.ryzw.houseauthority.vo.BasicVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

/**
 * <p>
 * 用户维修权限表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-04-15
 */
@RestController
@RequestMapping("/houseauthority/user-repair")
@Api(value = "用户维修权限管理", description = "用户维修权限管理")
public class UserRepairController {

    @Autowired
    IUserRepairService userRepairService;


    /**
     * 根据权限Id查询对应的用户
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "selectRepairById", method = RequestMethod.POST)
    @ApiOperation(value = "根据权限Id查询对应的用户", notes = "根据权限Id查询对应的用户")
    public Set selectRepairById(@RequestBody @Valid BasicVo<Long> basicVo) {
        return userRepairService.selectRepairList(basicVo.getId());
    }

    /**
     * 删除用户
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "deleteRepairById", method = RequestMethod.POST)
    @ApiOperation(value = "删除用户", notes = "删除用户")
    public boolean deleteRepairById(@RequestBody @Valid BasicVo<Long> basicVo) {
        return userRepairService.deleteRepairById(basicVo.getId());
    }

}
