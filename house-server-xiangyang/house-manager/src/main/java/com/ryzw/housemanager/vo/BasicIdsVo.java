package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 *基类，前端需要传输的id集合
 * </p>
 *
 * @author mfl
 * @since 2019/3/6
 */
@Data
public class BasicIdsVo<T> {
    @NotNull(message = "ID不能为空")
    private List<T> ids;
}
