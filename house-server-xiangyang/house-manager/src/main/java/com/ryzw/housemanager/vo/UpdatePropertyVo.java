package com.ryzw.housemanager.vo;


import com.mongodb.BasicDBObject;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 修改产权证参数
 * </p>
 *
 * @author zf
 * @since 2019/4/12
 */
@Data
public class UpdatePropertyVo {

    /**
     * basicDBObject对象
     */
    private BasicDBObject basicDBObject;

    /**
     * 物理路径集合
     */
    private List<String> physicalAddressList;
}
