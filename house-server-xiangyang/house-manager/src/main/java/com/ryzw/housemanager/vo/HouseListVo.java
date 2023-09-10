package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 房间编号集合
 * </p>
 *
 * @author zf
 * @since 2019/08/22
 */
@Data
public class HouseListVo extends PageVo{

    /**
     *房间编号集合
     */
    @NotNull(message = "房间编号集合不能为空")
    private List<String> houseIdList;

    /**
     * 一个楼栋+楼层集合对象
     */
    private FloorHouseListVo floorHouseListVo;


}
