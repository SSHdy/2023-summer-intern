package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.entity.Yard;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 新增院落与物业信息参数
 * </p>
 *
 * @author zf
 * @since 2018/05/13
 */
@Data
public class YardAddVo {

    /**
     * 院落对象
     */
    private Yard yard;

    /**
     * 院落物业集合
     */
    private List<YardAndestateVo> yardAndestateVoList;

    /**
     * 被删除的院落物业关系
     */
    private List<Long> delYardEstateIdList;
}
