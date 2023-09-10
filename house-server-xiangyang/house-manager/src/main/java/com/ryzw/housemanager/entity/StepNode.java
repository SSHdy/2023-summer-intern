package com.ryzw.housemanager.entity;

/**
 * <p>
 *     流程结点
 * </p>
 *
 * @author wj
 * @since 2019/3/28 0028
 */
public enum StepNode {

    /**
     * 用房单位申请
     */
    HUAPPLICATION("用房单位申请",1),

    /**
     * 维修收纳箱
     */
    MRBOX("维修收纳箱",2),

    /**
     * 业务科室审批
     */
    AODEPARTMENTS("业务科室审批",3),

    /**
     * 线下党组会审批
     */
    EXAPPROVAL("审批意见",4),

    /**
     * 自费维修上传维修方案
     */
    UMSCHEME("自费维修上传维修方案",5),

    /**
     * 审批维修方案
     */
    EXMPAPPROVE("审批维修方案",6),

    /**
     * 维修完成上传资料
     */
    UDAMAINTENANCE("维修完成上传资料",27),

    /**
     * 项目方案设计可行性研究
     */
    FSDESIGN("项目方案设计可行性研究",8),

    /**
     * 领导审批
     */
    LEADERAPPROVAL("领导审批",9),

    /**
     * 提交文件
     */
    SUBMITDOCUMENT("提交文件",10),

    /**
     * 内控采购流程
     */
    ICPROCESS("内控采购流程",11),

    /**
     * 合同执行
     */
    CONTRACTEXECUTION("合同执行",12),

    /**
     * 进度付款
     */
    PROGRESSPAYMENT("进度付款",13),

    /**
     * 项目施工管理（项目日志）
     */
    PCMANAGEMENT("项目施工管理（项目日志）",14),

    /**
     * 线下审批增减项，并上传资料（审批结果，审计报告）
     */
    ASOAPPROVAL("线下审批增减项，并上传资料（审批结果，审计报告）",15),

    /**
     * 竣工验收（竣工图纸）
     */
    CNACCEPTANCE("竣工验收（竣工图纸）",16),

    /**
     * 对质保期进行量化
     */
    QWPERIOD("对质保期进行量化",17),

    /**
     * 内控采购流程（项目事前审批）
     */
    POPROJECTS("内控采购流程（项目事前审批）",18),

    /**
     * 自费维修上传预算方案
     */
    USMAINTENANCE("自费维修上传预算方案",19),

    /**
     * 审批预算方案
     */
    BPAPPROVAL("审批预算方案",20),

    /**
     * 报政府或发改委
     */
    GOVERNMENT("报政府或发改委",21),

    /**
     * 项目施工设计
     */
    CONSTRUCTIONDESIGN("项目施工设计",22),

    /**
     * 维修预算
     */
    PROJECTBUDGET("维修预算",23),

    /**
     * 预算评审
     */
    PROJECTREVIEW("预算评审",24),

    /**
     * 指定项目负责人
     */
    PROJECTLEADER("指定项目负责人",25);



    StepNode(String name, Integer value){
        this.name = name;
        this.value=value;
    }

    private String name;

    private Integer value;



    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

}
