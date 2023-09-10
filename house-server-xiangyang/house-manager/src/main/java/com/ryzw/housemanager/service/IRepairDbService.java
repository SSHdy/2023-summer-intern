package com.ryzw.housemanager.service;

import com.ryzw.housemanager.config.AssertFeignConfiguration;
import com.ryzw.housemanager.dto.ConfigureHttpDto;
import com.ryzw.housemanager.dto.HttpResultDto;
import com.ryzw.housemanager.vo.ProcessHttpResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 调内控系统的维修相关接口
 * </p>
 *
 * @author wj
 * @since 2019/3/11 0011
 */
@FeignClient(name = "repair-service", url = "${daoBang.internal-controls}", configuration = AssertFeignConfiguration.class)
public interface IRepairDbService {

    /**
     * 触发内控接口(零星维修项目事前审批和 机关管理局的领导审批)
     *
     * @param processHttpResultVo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/repair/auditRepairState", method = RequestMethod.POST)
    HttpResultDto auditRepairState(@RequestBody ProcessHttpResultVo processHttpResultVo) throws Exception;


    /**
     * 触发内控接口
     *
     * @param configureHttpDto
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/repair/auditConfigureState", method = RequestMethod.POST)
    HttpResultDto auditConfigureState(@RequestBody ConfigureHttpDto configureHttpDto) throws Exception;


    /**
     * 触发内控接口(零星维修项目事前审批和 机关管理局的项目事前审批)
     *
     * @param name
     * @param phoneNum
     * @param title
     * @param detailid
     * @param etype
     * @param description
     * @param common_name
     * @param common_price
     * @param common_quantity
     * @param common_unit
     * @param common_total
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/api/third/prereport/create", method = RequestMethod.POST)
    HttpResultDto auditDbRepair(@RequestParam("name") String name, @RequestParam("phoneNum") String phoneNum,
                                @RequestParam("title") String title, @RequestParam("detailid") String detailid,
                                @RequestParam("etype") String etype, @RequestParam("description") String description,
                                @RequestParam("common_name") String common_name, @RequestParam("common_price") String common_price,
                                @RequestParam("common_quantity") String common_quantity, @RequestParam("common_unit") String common_unit,
                                @RequestParam("common_total") String common_total) throws Exception;

    /**
     * 获取内控采购流程完成步骤集合
     *
     * @param sano
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/api/third/prereport/getStep", method = RequestMethod.POST)
    HttpResultDto getStepList(@RequestParam("sano") String sano) throws Exception;

    /**
     * 获取内控采购流程步骤详情
     *
     * @param sano
     * @param stepTags
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/api/third/prereport/getStepInfo", method = RequestMethod.POST)
    HttpResultDto getStepInfo(@RequestParam("sano") String sano, @RequestParam("stepTags") String stepTags) throws Exception;

    /**
     * 获取内控采购流程所有审批文件
     *
     * @param sano
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/api/third/prereport/getAllFile", method = RequestMethod.POST)
    HttpResultDto getAllFile(@RequestParam("sano") String sano) throws Exception;

    /**
     * 获取内控预算事项科室预算明细
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/api/third/prereport/getSectionList", method = RequestMethod.POST)
    HttpResultDto getSectionList(@RequestParam("name") String name, @RequestParam("phoneNum") String phoneNum, @RequestParam("search") String search) throws Exception;


}
