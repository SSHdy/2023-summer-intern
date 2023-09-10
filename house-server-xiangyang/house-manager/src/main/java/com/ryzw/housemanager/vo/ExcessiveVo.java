package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *超标房间信息输入
 * </p>
 *
 * @author mfl
 * @since 2019/4/25
 */
@Data
public class ExcessiveVo {
   /**
    * 超标面积比较符
    */
   private String compareArea;

   /**
    * 超标面积
    */
   private float excessiveArea;

   /**
    * 超标比例比较符
    */
   private String compareProportion;

   /**
    * 超标比例
    */
   private float excessiveProportion;

   /**
    * 院落id
    */
   private Long yardId;

   /**
    * 楼栋id
    */
   private Long buildId;

   /**
    * 楼层
    */
   private Integer floorName;

   /**
    * 房间号
    */
   private String houseNumber;

   /**
    * 房间编号
    */
   private String houseId;

   /**
    * 是否备案(0未备案  1已备案)
    */
   private Integer isRecord;

}
