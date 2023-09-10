package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.vo.PageVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.dozer.Mapping;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *父级单位和子级单位输出
 * </p>
 *
 * @author mfl
 * @since 2018/12/24
 */
@Data
public class UnitChildDto {

    /**
     * 单位序号
     */
    @Mapping("unitId")
    private Long unitId;

    /**
     * 单位名称
     */
    @Mapping("unitName")
    private String unitName;

    /**
     * 单位类型名称
     */
    @Mapping("unittypeName")
    private String unittypeName;

    /**
     * 编制人数
     */
    @Mapping("unitComplement")
    private Integer unitComplement;

    /**
     * 父级单位id
     */
    private Long unitPid;

    /**
     * 组织机构代码
     */
    private String unitOrganizationCode;

    /**
     * 统一社会代码
     */
    private String unitSocialCode;


    /**
     * 单位类型(0非单位  1一级单位  2二级单位)
     */
    private Integer unitClassify;

    public UnitChildDto(){
        children = new LinkedList<>();
    }
    /**
     * 父菜单下的子菜单集合
     */
    private List<UnitChildDto> children;

}
