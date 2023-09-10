package com.ryzw.housemanager.entity;

/**
 * <p>
 * 流程结点
 * </p>
 *
 * @author wj
 * @since 2019/3/28 0028
 */
public enum SelectStepNode {

    /**
     * 业务科室审批
     */
    AODEPARTMENTS("科室审批", 3, 3, 3),

    /**
     * 线下党组会审批
     */
    EXAPPROVAL("审批意见", 4, 3, 4),

    /**
     * 部门维修上传
     */
    UMSCHEME("部门维修上传", 5, 0, 5),

    /**
     * 部门维修审批
     */
    EXMPAPPROVE("部门维修审批", 6, 0, 6),

    /**
     * 维修完成上传资料
     */
    UDAMAINTENANCE("维修完成上传资料", 27, 0, 27),

    /**
     * 项目方案设计可行性研究
     */
    FSDESIGN("可行性研究", 8, 2, 8),

    /**
     * 领导审批
     */
    LEADERAPPROVAL("领导审批", 9, 2, 9),

    /**
     * 提交文件
     */
    SUBMITDOCUMENT("提交文件", 10, 2, 10),

    /**
     * 报政府或发改委
     */
    GOVERNMENT("报发改委", 21, 2, 11),

    /**
     * 项目施工设计
     */
    CONSTRUCTIONDESIGN("施工设计", 22, 2, 12),

    /**
     * 维修预算
     */
    PROJECTBUDGET("维修预算", 23, 2, 13),

    /**
     * 预算评审
     */
    PROJECTREVIEW("预算评审", 24, 2, 14),

    /**
     * 内控采购流程
     */
    ICPROCESS("内控采购流程", 11, 2, 15),

    /**
     * 指定项目负责人
     */
    PROJECTLEADER("指定项目负责人", 25, 2, 16),

    /**
     * 进度付款
     */
    PROGRESSPAYMENT("进度付款", 13, 2, 17),

    /**
     * 项目施工管理（项目日志）
     */
    PCMANAGEMENT("施工管理", 14, 2, 17),

    /**
     * 竣工验收（竣工图纸）
     */
    CNACCEPTANCE("竣工验收", 16, 2, 18),

    /**
     * 对质保期进行量化
     */
    QWPERIOD("质保期量化", 17, 2, 19),

    /**
     * 维修预算上传
     */
    USMAINTENANCE("预算维修上传", 19, 0, 19),

    /**
     * 维修预算审批
     */
    BPAPPROVAL("预算维修审批", 20, 0, 20);


    SelectStepNode(String name, Integer value, Integer type, Integer flag) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.flag = flag;
    }

    private String name;

    private Integer value;

    private Integer type;

    private Integer flag;


    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getType() {
        return type;
    }

    public Integer getFlag() {
        return flag;
    }
}
