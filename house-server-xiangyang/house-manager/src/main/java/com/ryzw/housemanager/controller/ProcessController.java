package com.ryzw.housemanager.controller;

import com.ryzw.housemanager.dto.ProcessHistoryDto;
import com.ryzw.housemanager.dto.ProcessNumDto;
import com.ryzw.housemanager.service.IProcessService;
import com.ryzw.housemanager.vo.BasicVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *     公共流程 前端控制器
 * </p>
 *
 * @author wj
 * @since 2019-05-29 0029
 */
@RestController
@RequestMapping("/process")
@Slf4j
@Api(value = "公共流程管理", description = "公共流程管理")
public class ProcessController {

    @Autowired
    IProcessService processService;



    /**
     * 查询审批流程
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/selectProcessHistory", method = RequestMethod.POST)
    @ApiOperation(value = "查询审批流程", notes = "查询审批流程")
    public List<ProcessHistoryDto> selectProcessHistory(@RequestBody @Valid BasicVo<String> basicVo) {
        return processService.selectProcessHistory(basicVo.getId());
    }


    /**
     * 统计待办事项数量
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/todoListNum", method = RequestMethod.POST)
    @ApiOperation(value = "统计待办事项数量", notes = "统计待办事项数量")
    public Long todoListNum(@RequestBody @Valid BasicVo<Long> basicVo) {
        return processService.todoListNum(basicVo.getId());
    }


    /**
     * 统计每个待办事项数量
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/todoListProcessNum", method = RequestMethod.POST)
    @ApiOperation(value = "统计每个待办事项数量", notes = "统计每个待办事项数量")
    public ProcessNumDto todoListProcessNum(@RequestBody @Valid BasicVo<Long> basicVo) {
        return processService.todoListProcessNum(basicVo.getId());
    }

}
