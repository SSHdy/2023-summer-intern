package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.vo.YardAndestateVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 查询某院落物业关系
 * </p>
 *
 * @author zf
 * @since 2018/05/13
 */
@Data
public class YardEstateDto {

    /**
     * 院落编号
     */
    private Long yardId;



    /**
     * 院落物业集合
     */
    private List<YardAndestateVo> yardAndestateVoList;
}
