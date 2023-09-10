package com.ryzw.houseauthority.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.houseauthority.entity.OauthClientDetails;
import com.ryzw.houseauthority.exception.ServiceException;
import com.ryzw.houseauthority.service.IOauthClientDetailsService;
import com.ryzw.houseauthority.vo.BasicVo;
import com.ryzw.houseauthority.vo.ClientVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyx
 * @since 2019-01-10
 */
@RestController
@RequestMapping("/client")
@Api(value = "权限客户端详情管理", description = "权限客户端详情管理")
public class OauthClientDetailsController {
    @Autowired
    IOauthClientDetailsService iOauthClientDetailsService;

    /**
     * 查询角色
     * @param client
     * @return
     */
    @RequestMapping(value = "clientList", method = RequestMethod.POST)
    @ApiOperation(value = "查询角色", notes = "查询角色")
    public IPage<OauthClientDetails> clientList(@RequestBody OauthClientDetails client){
        Page<OauthClientDetails> page = new Page<>(client.getCurrentPage(), client.getPageSize());
        QueryWrapper<OauthClientDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("role_id");
        return iOauthClientDetailsService.page(page, queryWrapper);
    }

    /**
     * 用于新增
     * @param oauthClientDetails
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ApiOperation(value = "用于新增", notes = "用于新增")
    public boolean add(@RequestBody OauthClientDetails oauthClientDetails){
        String clientId = oauthClientDetails.getClientId();
        int count = iOauthClientDetailsService.count(new QueryWrapper<OauthClientDetails>().eq("client_id", clientId));
        if (count > 0) {
            throw new ServiceException("客户端id已存在!");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(oauthClientDetails.getClientSecret());
        oauthClientDetails.setClientSecret(encode);
        return iOauthClientDetailsService.save(oauthClientDetails);
    }

    /**
     * 用于删除
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ApiOperation(value = "用于删除", notes = "用于删除")
    public boolean delete(@RequestBody @Valid BasicVo<Long> basicVo) {
        return iOauthClientDetailsService.removeById(basicVo.getId());
    }

    /**
     * 修改授权
     *
     * @param oauthClientDetails
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ApiOperation(value = "修改授权", notes = "修改授权")
    public boolean update(@RequestBody OauthClientDetails oauthClientDetails) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(oauthClientDetails.getClientSecret());
        oauthClientDetails.setClientSecret(encode);
        return iOauthClientDetailsService.updateById(oauthClientDetails);
    }

    /**
     * 查询授权列表
     *
     * @param client
     * @return
     */
    @RequestMapping(value = "selectClientList", method = RequestMethod.POST)
    @ApiOperation(value = "查询授权列表", notes = "查询授权列表")
    public IPage<OauthClientDetails> selectClientList(@RequestBody OauthClientDetails client) {
        Page<OauthClientDetails> page = new Page<>(client.getCurrentPage(), client.getPageSize());
        QueryWrapper<OauthClientDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("oauth_detail_id","client_id","resource_ids","scope","authorized_grant_types",
                "web_server_redirect_uri","company_name");
        queryWrapper.orderByDesc("oauth_detail_id");
        return iOauthClientDetailsService.page(page, queryWrapper);
    }

    /**
     * 查询授权详情
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "clientDetail", method = RequestMethod.POST)
    @ApiOperation(value = "查询授权详情", notes = "查询授权详情")
    public OauthClientDetails clientDetail(@RequestBody @Valid BasicVo<Long> basicVo) {
        QueryWrapper<OauthClientDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("oauth_detail_id","client_id","resource_ids","scope","authorized_grant_types",
                "web_server_redirect_uri","company_name");
        queryWrapper.eq("oauth_detail_id",basicVo.getId());
        return iOauthClientDetailsService.getOne(queryWrapper);
    }

    /**
     * 判断客户端Id是否重复
     *
     * @param clientVo
     * @return
     */
    @RequestMapping(value = "unique", method = RequestMethod.POST)
    @ApiOperation(value = "判断客户端Id是否重复", notes = "判断客户端Id是否重复")
    public boolean unique(@RequestBody @Valid ClientVo clientVo) {
        Long oauthDetailId = clientVo.getOauthDetailId();
        QueryWrapper<OauthClientDetails> qw = new QueryWrapper<>();
        qw = qw.eq("client_id", clientVo.getClientId());
        int count = (oauthDetailId == null) ? iOauthClientDetailsService.count(qw) : iOauthClientDetailsService.count(qw.ne("oauth_detail_id", oauthDetailId));
        if (count > 0) {
            throw new ServiceException("客户端id重复");
        }
        return true;
    }
}
