package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * <p>
 *  json平面图对象
 * </p>
 *
 * @author lyx
 * @since 2018/12/10
 */
@Data
@Document(collection = "jsonPlanDB")
@CompoundIndexes({
        @CompoundIndex(name = "build_floor_index",def = "{'buildId':1,'floorName':1}",unique = true)
})
public class JsonPlanVo {
    /**
     * 院落
     */
    @ApiModelProperty(value = "院落",example = "1")
    private Long yardId;

    /**
     * 楼栋id
     */
    @ApiModelProperty(value = "楼栋id",example = "1")
    @NotNull(message = "楼栋id不能为空")
    private Long buildId;

    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层",example = "1")
    @NotNull(message = "楼层不能为空")
    private Integer floorName;

    /**
     * jsonPlan
     */
    @ApiModelProperty(value = "jsonPlan",example = "*****")
    @NotNull(message = "jsonPlan不能为空")
    private Map<String,Object> jsonPlan;
}
