<template>
  <div id="configQuery">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">配置编号：</div>
          <div class="query-input">
            <el-input v-model.trim="configureNo" placeholder="请输入配置编号"></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">申请单位：</div>
          <div class="query-input">
            <el-select filterable v-model="unitId" placeholder="请选择" style="width:100%">
              <el-option
                v-for="item in unitOptions"
                :key="item.unitId"
                :label="item.unitName"
                :value="item.unitId"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">申请时间：</div>
          <div class="query-input">
            <el-date-picker
              v-model="startTime"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd HH:mm:ss"
              :picker-options="pickerOptions_start"
            ></el-date-picker>
          </div>
        </div>
        <div class="query-item">
          <div class="query-title" style="margin:0 5px">至</div>
          <div class="query-input">
            <el-date-picker
              v-model="endTime"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd 23:59:59"
              :picker-options="pickerOptions_end"
            ></el-date-picker>
          </div>
        </div>
        <div class="query-btn">
          <search-btn @clickBtn="search"></search-btn>
          <reset-btn @clickBtn="reset"></reset-btn>
        </div>
      </div>
    </div>
    <div class="table-box">
      <table-component :tableData="tableData" :tableHeight="tableHeight">
        <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="configureNo"
          label="配置编号"
          align="center"
        ></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="unitName" label="申请单位" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="configureApplicant"
          label="申请人"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="unitComplement"
          label="编制人数"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="actualNumber"
          label="实有人数"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="totalArea"
          label="现有面积(m²)"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="configureDate"
          label="申请时间"
          align="center"
        ></el-table-column>
        <el-table-column label="申请状态" width="140" align="center">
          <template slot-scope="scope">
            <span
              :class="{'status-unreviewed' : scope.row.applyStatus===0,'status-pass' : scope.row.applyStatus===1,'status-notPass' : scope.row.applyStatus===2,'status-reviewimg' : scope.row.applyStatus===3}"
            >{{scope.row.applyStatusText}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="90" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDelete="false"
              :isShowDetail="true"
              :isShowAlter="false"
              @detailClick="singleDetail(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group :batchDelBtnShow="false" :addBtnShow="false"></table-btn-group>
        </div>
        <div class="operate-pager">
          <table-pagination
            @size-change="changeSize"
            @page-change="changePage"
            :currentPage="currentPage"
            :pageSize="pageSize"
            :total="total"
          ></table-pagination>
        </div>
      </div>
    </div>
    <el-dialog
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>配置详情</span>
      </div>
      <div class="dialog-content">
        <div class="title-box clearfix">
          <span
            v-for="item in processStepList"
            :key="item.step"
            :class="{'processActive': activeStep === item.value}"
            @click="changeProcess(item.value)"
          >{{item.key}}</span>
        </div>
        <div class="common-info">
          <div class="clearfix">
            <div class="content-item">
              <span>申请单位：</span>
              <span>{{baseInfoForm.unitName}}</span>
            </div>
            <div class="content-item">
              <span>单位负责人：</span>
              <span>{{baseInfoForm.unitCharger}}</span>
            </div>
            <div class="content-item">
              <span>编制人数：</span>
              <span>{{baseInfoForm.unitComplement}}人</span>
            </div>
            <div class="content-item">
              <span>实有人数：</span>
              <span>{{baseInfoForm.actualNumber}}人</span>
            </div>
            <div class="content-item">
              <span>现有面积：</span>
              <span>{{baseInfoForm.totalArea ? baseInfoForm.totalArea : '暂无'}}</span>
            </div>
            <div class="content-item">
              <span>人均面积：</span>
              <span>{{baseInfoForm.PerCapitaArea ? baseInfoForm.PerCapitaArea : '暂无'}}</span>
            </div>
            <div class="content-item">
              <span>联系电话：</span>
              <span>{{baseInfoForm.contactPhone}}</span>
            </div>
            <div class="content-item">
              <span>联系人：</span>
              <span>{{baseInfoForm.contacts}}</span>
            </div>
            <div class="content-item">
              <span>单位性质：</span>
              <span>{{baseInfoForm.unitProperty}}</span>
            </div>
            <div class="content-item">
              <span>单位地址：</span>
              <span>{{baseInfoForm.unitAddress}}</span>
            </div>
            <div class="content-item" style="width:100%">
              <span>申请原因：</span>
              <span>{{baseInfoForm.applyReason}}</span>
            </div>
            <div
              class="content-item"
              style="width:100%"
              v-if="baseInfoForm.enclosureList.length > 0"
            >
              <span>下载附件：</span>
              <div class="file-download">
                <el-checkbox-group
                  v-model="fileDownLoadCheckedList"
                  @change="handleCheckedFileChange"
                >
                  <div
                    v-for="(file, index) in baseInfoForm.enclosureList"
                    :key="index"
                    class="file-download-item"
                  >
                    <el-checkbox :label="file.detail">{{file.enclosureName}}</el-checkbox>
                  </div>
                </el-checkbox-group>
                <el-button
                  size="small"
                  type="primary"
                  @click="fileDownLoad(downloadCheckedList, 1)"
                >点击下载</el-button>
              </div>
            </div>
          </div>
        </div>
        <div v-if="activeStep===2" class="project-info">
          <div class="clearfix">
            <div class="content-item">
              <span>操作人：</span>
              <span>{{Form_unit.unitReviewUser}}</span>
            </div>
            <div class="content-item">
              <span>操作时间：</span>
              <span>{{Form_unit.unitReviewTime}}</span>
            </div>
            <div class="content-item">
              <span>审查结果：</span>
              <span>{{Form_unit.unitReviewAuditState}}</span>
            </div>
            <div class="content-item">
              <span>电梯台数：</span>
              <span>{{Form_unit.elevatorsNumber}}台</span>
            </div>
            <div class="content-item newUnitName" v-if="Form_unit.newUnitName">
              <span>参与统筹调剂单位</span>
              <span>：</span>
              <span>{{Form_unit.newUnitName}}</span>
            </div>
            <div class="content-item" style="width:100%;">
              <span>区域：</span>
              <span>{{Form_unit.regionName}}</span>
            </div>
          </div>
          <div class="house-info">
            <span>房间信息：</span>
            <div class="house-info-table">
              <table-component :tableData="Form_unit.configureRoomList" :tableHeight="200">
                <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
                <el-table-column label="房间数量" prop="roomNumber" align="center"></el-table-column>
                <el-table-column label="房间面积范围(m²)" prop="roomAreaRange" align="center"></el-table-column>
              </table-component>
            </div>
          </div>
        </div>
        <div v-if="activeStep===3" class="project-info">
          <div class="clearfix">
            <div class="content-item">
              <span>操作人：</span>
              <span>{{Form_project.drawPlanUser}}</span>
            </div>
            <div class="content-item">
              <span>操作时间：</span>
              <span>{{Form_project.drawPlanTime}}</span>
            </div>
            <div class="content-item">
              <span>分配方案：</span>
              <span>{{Form_project.allocationPlan}}</span>
            </div>
            <div class="content-item" v-if="this.allocationPlan === 1">
              <span>选择方案：</span>
              <span>{{Form_project.selectionPlanName}}</span>
            </div>
            <div
              class="content-item"
              v-if="Form_project.selectionPlan === 2 && this.allocationPlan === 1"
            >
              <span>方案名称：</span>
              <span>{{Form_project.planName}}</span>
            </div>
          </div>
          <div class="room-distribute-table" v-if="this.allocationPlan === 1">
            <div class="allocation-plan">
              <div class="allocation-title">方案详情：</div>
              <table-component :tableData="distributeTableData" :tableHeight="200">
                <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
                <el-table-column label="房间位置" prop="position" align="center"></el-table-column>
                <el-table-column label="房间面积(m²)" prop="houseUseArea" align="center"></el-table-column>
                <el-table-column label="原使用单位" prop="oldUnitName" align="center"></el-table-column>
                <el-table-column
                  label="新使用单位"
                  prop="newUnitName"
                  align="center"
                  v-if="Form_project.selectionPlan === 2"
                ></el-table-column>
              </table-component>
              <div class="distribution-table-pagination-box">
                <div class="distribution-table-pagination">
                  <el-pagination
                    small
                    layout="prev, pager, next"
                    :current-page="distributeCurrentPage"
                    :page-size="distributePageSize"
                    :total="distributeTotal"
                    @current-change="distributeCurrentChange"
                  ></el-pagination>
                </div>
              </div>
            </div>
            <div class="allocation-statistics" v-if="Form_project.selectionPlan === 2">
              <div class="allocation-title">分配统计：</div>
              <table-component :tableData="distributeChangeList" :tableHeight="200">
                <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
                <el-table-column label="单位名称" prop="unitNames" align="center"></el-table-column>
                <el-table-column label="房间数量" prop="houseNumber" align="center"></el-table-column>
                <el-table-column label="面积(m²)" prop="useArea" align="center"></el-table-column>
              </table-component>
            </div>
          </div>
        </div>
        <div v-if="activeStep===4" class="project-info">
          <div class="clearfix">
            <div class="content-item">
              <span>审批人：</span>
              <span>{{Form_leader.leaderApprovalUser}}</span>
            </div>
            <div class="content-item">
              <span>审批时间：</span>
              <span>{{Form_leader.leaderApprovalTime}}</span>
            </div>
            <div class="content-item">
              <span>审批结果：</span>
              <span>{{Form_leader.auditState}}</span>
            </div>
            <div class="content-item">
              <span>审批意见：</span>
              <span>{{Form_leader.comment}}</span>
            </div>
          </div>
        </div>
        <div v-if="activeStep===5" class="project-info">
          <div class="clearfix">
            <div class="content-item">
              <span>审批人：</span>
              <span>{{Form_addInfo.improvingDataUser}}</span>
            </div>
            <div class="content-item">
              <span>审批时间：</span>
              <span>{{Form_addInfo.improvingDataTime}}</span>
            </div>
            <div class="content-item" v-if="this.allocationPlan===2">
              <span>补钱：</span>
              <span>{{Form_addInfo.makeMoney}}元</span>
            </div>
            <div class="content-item" v-if="this.allocationPlan===2">
              <span>收益：</span>
              <span>{{Form_addInfo.profit}}元</span>
            </div>
            <div class="content-item" v-if="this.allocationPlan===3">
              <span>付款周期：</span>
              <span>{{Form_addInfo.rentalMode}}</span>
            </div>
            <div class="content-item" v-if="this.allocationPlan===3">
              <span>租金：</span>
              <span>{{Form_addInfo.rent}}元</span>
            </div>
            <div class="content-item" v-if="this.allocationPlan===3">
              <span>期限：</span>
              <span>{{Form_addInfo.term}}年</span>
            </div>
            <div class="content-item" v-if="this.allocationPlan===4">
              <span>财政支出：</span>
              <span>{{Form_addInfo.expenditure}}元</span>
            </div>
            <div class="content-item" v-if="this.allocationPlan===4">
              <span>项目名称：</span>
              <span>{{Form_addInfo.projectName}}</span>
            </div>
            <div class="content-item">
              <span>面积：</span>
              <span>{{Form_addInfo.area}}㎡</span>
            </div>
          </div>
          <div class="download-box clearfix" v-if="Form_addInfo.improvingDataList.length > 0">
            <div class="download-title">文件下载：</div>
            <div class="download-content">
              <el-checkbox-group
                v-model="improvingDownLoadCheckedList"
                @change="handleCheckedImprovingChange"
              >
                <div
                  v-for="(item, index) in Form_addInfo.improvingDataList"
                  :key="index"
                  class="file-download-item"
                >
                  <el-checkbox :label="item.detail">{{item.improvingDrawingFileName}}</el-checkbox>
                </div>
              </el-checkbox-group>
              <el-button
                size="small"
                type="primary"
                @click="fileDownLoad(improvingCheckedList, 2)"
              >点击下载</el-button>
            </div>
          </div>
          <div class="yard-info-box" v-if="Form_addInfo.yardInfo.length > 0">
            <span>院落信息：</span>
            <div class="yard-info-table">
              <table-component :tableData="Form_addInfo.yardInfo" :tableHeight="73">
                <el-table-column label="院落名称" prop="yardName" align="center"></el-table-column>
                <el-table-column label="院落地址" prop="yardPosition" align="center"></el-table-column>
                <el-table-column label="经度(°)" prop="yardLatitude" align="center"></el-table-column>
                <el-table-column label="纬度(°)" prop="yardLongitude" align="center"></el-table-column>
              </table-component>
            </div>
          </div>
          <div class="build-info-box">
            <span>楼座信息：</span>
            <div class="build-info-table">
              <table-component :tableData="Form_addInfo.buildDetailDtoList" :tableHeight="200">
                <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
                <el-table-column label="楼座名称" prop="buildName" align="center"></el-table-column>
                <el-table-column label="建筑面积(m²)" prop="buildArea" align="center"></el-table-column>
                <el-table-column label="建成时间" prop="buildDate" align="center"></el-table-column>
                <el-table-column label="经度(°)" prop="buildLongitude" align="center"></el-table-column>
                <el-table-column label="纬度(°)" prop="buildLatitude" align="center"></el-table-column>
              </table-component>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getConfigureList,
  getConfigureDetail,
  leaderReviewDetail,
  selectConfigureStep,
  getPlanDataDetail,
  getBasicConfigure,
  getSchemeName,
  getArtificialAllocation,
  downloadFile,
  getChangeData
} from "./api";
import { getUserUnitList } from "@/axios/commonApi";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import { truncate } from 'fs';
export default {
  components: {
    singleBtnGroup,
    tableBtnGroup,
    tablePagination,
    tableComponent,
    cancelBtn,
    confirmBtn,
    searchBtn,
    resetBtn
  },
  data() {
    return {
      userId: '',
      unitId: "",
      configureNo: '',
      startTime: "",
      endTime: "",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableData: [],
      tableHeight: 0,
      unitOptions: [],
      dialogVisible: false,
      isShow: {
        initApplyDb: false, // 申请初审
        financeApprovalDb: false, // 财政或人民政府审批
      },
      baseInfoForm: {
        unitName: '',
        unitCharger: '',
        unitComplement: null,
        actualNumber: null,
        totalArea: null,
        PerCapitaArea: null,
        contactPhone: null,
        contacts: '',
        unitProperty: '',
        unitAddress: '',
        applyReason: '',
        enclosureList: []
      },
      Form_unit: {
        unitReviewAuditState: '',
        elevatorsNumber: null,
        regionName: '',
        newUnitName: '',
        unitReviewUser: '',
        unitReviewTime: '',
        configureRoomList: []
      },
      Form_project: {
        allocationPlan: null,
        drawPlanTime: '',
        drawPlanUser: '',
        selectionPlan: null,
        selectionPlanName: '',
        planId: null,
        planName: ''
      },
      Form_leader: {
        auditState: '',
        comment: '',
        leaderApprovalTime: null,
        leaderApprovalUser: ''
      },
      Form_addInfo: {
        improvingDataUser: '',
        improvingDataTime: null,
        improvingDataList: [],
        area: '',
        makeMoney: '',
        profit: '',
        rentalMode: '',
        rent: '',
        term: '',
        expenditure: '',
        projectName: '',
        buildDetailDtoList: [],
        yardInfo: []
      },
      processStepList: [],
      activeStep: 1,
      activeProcessId: '',
      distributeTableData: [],
      distributeCurrentPage: 1,
      distributePageSize: 10,
      distributeTotal: 0,
      allocationPlan: null,
      fileDownLoadCheckedList: [],
      improvingDownLoadCheckedList: [],
      downloadCheckedList: [],
      improvingCheckedList: [],
      distributeChangeList: [],
      pickerOptions_start: {
      },
      pickerOptions_end: {
      }
    };
  },
  methods: {
    // 开始加载
    loadingStart(classname) {
      if (!this.loading) {
        this.loading = this.$loading({
          lock: true,
          text: '正在加载...',
          spinner: 'el-icon-loading',
          target: classname,
          customClass: 'loading-box'
        });
      }
    },
    // 结束加载
    loadingEnd() {
      if (this.loading) {
        this.loading.close();
        this.loading = null;
      }
    },
    // 改变页面条数
    changeSize(size) {
      this.pageSize = size;
      this.getList();
    },
    // 改变页面页数
    changePage(page) {
      this.currentPage = page;
      this.getList();
    },
    // 获取列表
    getList() {
      this.loadingStart('.table-box');
      getConfigureList(this.currentPage, this.pageSize, this.configureNo, this.startTime, this.endTime, null, this.unitId)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach(item => {
            item.applyStatusText = this.getApplyStatus(item.applyStatus);
            if (!item.totalArea) {
              item.totalArea = '无';
            }
            if (!item.actualNumber) {
              item.actualNumber = '无';
            }
          })
          this.loadingEnd();
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '配置查询列表获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取申请状态
    getApplyStatus(status) {
      switch (status) {
        case 0:
          return '未审核';
          break;
        case 1:
          return '审核通过';
          break;
        case 2:
          return '审核未通过';
          break;
        case 3:
          return '审核中';
          break;
      }
    },
    // 查询信息
    search() {
      this.currentPage = 1;
      this.getList();
    },
    // 重置
    reset() {
      this.currentPage = 1;
      this.configureNo = "";
      this.unitId = "";
      this.startTime = "";
      this.endTime = "";
      this.getList();
    },
    // 查询详情
    singleDetail(row) {
      this.dialogVisible = true;
      this.activeProcessId = row.configureId;
      this.allocationPlan = row.allocationPlan;
      selectConfigureStep(row.configureId, row.step).then(res => {
        res.shift();
        if (res.length > 0) {
          this.activeStep = 2;
        } else {
          this.activeStep = 1;
        }
        if (this.activeStep !== 1) {
          this.leaderReviewDetail(row.configureId, this.activeStep);
        }
        this.processStepList = res;
      }).catch(err => {
        const msg = err.result ? err.result : '配置步骤获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
      getBasicConfigure(row.configureId).then(res => {
        for ([key, value] of Object.entries(res)) {
          if (this.baseInfoForm[key] !== undefined) {
            this.baseInfoForm[key] = value;
          }
        }
        if (this.baseInfoForm.totalArea) {
          if (this.baseInfoForm.actualNumber !== 0) {
            this.baseInfoForm.PerCapitaArea = (this.baseInfoForm.totalArea / this.baseInfoForm.actualNumber).toFixed(2);
            this.baseInfoForm.PerCapitaArea = `${this.baseInfoForm.PerCapitaArea}m²`;
          }
          this.baseInfoForm.totalArea = this.baseInfoForm.totalArea.toFixed(2)
          this.baseInfoForm.totalArea = `${this.baseInfoForm.totalArea}m²`;
        }
      }).catch(err => {
        const msg = err.result ? err.result : '配置申请基础数据获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
      getConfigureDetail(row.configureId).then(res => {
        for ([key, value] of Object.entries(res.configure)) {
          if (this.baseInfoForm[key] !== undefined) {
            if (value) {
              this.baseInfoForm[key] = value;
            } else {
              this.baseInfoForm[key] = '暂无';
            }
          }
        }
        if (res.configureEnclosureList && res.configureEnclosureList.length > 0) {
          res.configureEnclosureList.forEach(item => {
            item.detail = `${item.enclosurePhysicsUrl},${item.enclosureName}`;
          });
          this.baseInfoForm.enclosureList = res.configureEnclosureList;
        }
      }).catch(err => {
        const msg = err.result ? err.result : '配置详情获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 改变房间分配方案表格的页码
    distributeCurrentChange(pageNum) {
      this.distributeCurrentPage = pageNum;
      if (this.Form_project.selectionPlan === 1) {
        this.getPlanDataDetail();
      } else {
        this.getSchemeDataDetail();
      }
    },
    // 查看智能分配方案数据
    getPlanDataDetail() {
      getPlanDataDetail(this.distributeCurrentPage, this.distributePageSize, this.activeProcessId).then(res => {
        this.distributeTotal = res.total;
        this.distributeTableData = res.records[0].planDataDtoList;
        this.distributeTableData.forEach(item => {
          item.position = `${item.yardName}-${item.buildName}-${item.houseNumber}`;
          if (item.oldUnitNameList && item.oldUnitNameList.length > 0) {
            item.oldUnitName = item.oldUnitNameList.join(',');
          } else {
            item.oldUnitName = '无';
          }
        })
      }).catch(err => {
        const msg = err.result ? err.result : '房间分配方案数据获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查看人工分配方案名称
    getSchemeName() {
      getSchemeName(this.activeProcessId).then(res => {
        this.Form_project.planName = res;
      }).catch(err => {
        const msg = err.result ? err.result : '方案名称获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查看人工分配方案数据
    getSchemeDataDetail() {
      getArtificialAllocation(this.distributeCurrentPage, this.distributePageSize, this.Form_project.planId).then(res => {
        this.distributeTotal = res.total;
        this.distributeTableData = res.records;
        this.distributeTableData.forEach(item => {
          let oldUnitArr = [];
          let newUnitArr = [];
          item.position = `${item.yardName}-${item.buildName}-${item.houseNumber}`;
          item.newUnitList.forEach(newUnit => {
            if (newUnit.newUnitId !== 0) {
              newUnitArr.push(newUnit.newUnitName);
            }
          });
          item.oldUnitList.forEach(oldUnit => {
            if (oldUnit.oldUnitId !== 0) {
              oldUnitArr.push(oldUnit.oldUnitName);
            }
          });
          item.oldUnitName = oldUnitArr.join(',');
          item.newUnitName = newUnitArr.join(',');
        })
      }).catch(err => {
        const msg = err.result ? err.result : '该方案房间信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 用户权限下的单位字典项
    getUserUnitDic() {
      getUserUnitList().then(res => {
        this.unitOptions = res;
      }).catch(err => {
        const msg = err.result ? err.result : '单位下拉选项获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 切换流程
    changeProcess(step) {
      this.activeStep = step
      this.fileDownLoadCheckedList = [];
      this.improvingDownLoadCheckedList = [];
      this.leaderReviewDetail(this.activeProcessId, step);
    },
    // 获取审核状态
    getReviewAuditState(status) {
      if (this.activeStep === 2) {
        switch (status) {
          case 1:
            return '同意申请';
            break;
          case 2:
            return '不同意申请';
            break;
        }
      } else if (this.activeStep === 3) {
        switch (status) {
          case 1:
            return '调剂';
            break;
          case 2:
            return '置换';
            break;
          case 3:
            return '租用';
            break;
          case 4:
            return '建设';
            break;
        }
      }
    },
    // 获取配置流程数据
    leaderReviewDetail(id, step) {
      leaderReviewDetail(id, step).then(res => {
        if (step === 2) {
          this.Form_unit.unitReviewAuditState = this.getReviewAuditState(res.documentList[0].unitReviewDb.unitReviewAuditState);
          this.Form_unit.elevatorsNumber = res.elevatorsNumber;
          this.Form_unit.regionName = res.regionList.join(',');
          this.Form_unit.newUnitName = res.newUnitName || '';
          this.Form_unit.unitReviewTime = res.documentList[0].unitReviewDb.unitReviewTime;
          this.Form_unit.unitReviewUser = res.documentList[0].unitReviewDb.unitReviewUser;
          res.configureRoomList.forEach(item => {
            item.roomAreaRange = `${item.roomAreaMin}(m²)-${item.roomAreaMax}(m²)`;
          });
          this.Form_unit.configureRoomList = res.configureRoomList;
        } else if (step === 3) {
          this.allocationPlan = res.allocationPlan;
          this.Form_project.allocationPlan = this.getReviewAuditState(res.allocationPlan);
          this.Form_project.drawPlanTime = res.documentList[0].drawPlanDb.drawPlanTime;
          this.Form_project.drawPlanUser = res.documentList[0].drawPlanDb.drawPlanUser;
          this.Form_project.selectionPlan = res.selectionPlan;
          this.Form_project.selectionPlanName = res.selectionPlan === 1 ? '智能分配' : '人工分配';
          this.Form_project.planId = res.planId;
          if (res.allocationPlan === 1) {
            if (res.selectionPlan === 1) {
              this.getPlanDataDetail();
            } else {
              this.getSchemeDataDetail();
              this.getSchemeName();
              this.getChangeData(res.planId);
            }
          }
        } else if (step === 4) {
          this.Form_leader.leaderApprovalTime = res.documentList[0].leaderApprovalDb.leaderApprovalTime;
          this.Form_leader.leaderApprovalUser = res.documentList[0].leaderApprovalDb.leaderApprovalUser;
          this.Form_leader.auditState = res.documentList[0].leaderApprovalDb.ldaAuditState === 1 ? '同意' : '不同意';
          this.Form_leader.comment = res.documentList[0].leaderApprovalDb.ldaComment || '暂无';
        } else if (step === 5) {
          res.buildDetailDtoList.forEach(item => {
            item.buildDate = this.formatDate(item.buildDate);
          })
          this.Form_addInfo.improvingDataUser = res.documentList[0].improvingDataDb.improvingDataUser;
          this.Form_addInfo.improvingDataTime = res.documentList[0].improvingDataDb.improvingDataTime;
          this.Form_addInfo.buildDetailDtoList = res.buildDetailDtoList;
          this.Form_addInfo.makeMoney = res.documentList[0].improvingDataDb.improvingDataRRC.makeMoney || '';
          this.Form_addInfo.profit = res.documentList[0].improvingDataDb.improvingDataRRC.profit || '';
          this.Form_addInfo.rentalMode = res.documentList[0].improvingDataDb.improvingDataRRC.rentalMode || '';
          this.Form_addInfo.rent = res.documentList[0].improvingDataDb.improvingDataRRC.rent || '';
          this.Form_addInfo.term = res.documentList[0].improvingDataDb.improvingDataRRC.term || '';
          this.Form_addInfo.expenditure = res.documentList[0].improvingDataDb.improvingDataRRC.expenditure || '';
          this.Form_addInfo.improvingDataList = res.documentList[0].improvingDataDb.improvingDataRRC.improvingDataList || [];
          this.Form_addInfo.projectName = res.documentList[0].improvingDataDb.improvingDataRRC.projectName || '';
          this.Form_addInfo.area = res.documentList[0].improvingDataDb.improvingDataRRC.area || '';
          if (this.Form_addInfo.improvingDataList.length > 0) {
            this.Form_addInfo.improvingDataList.forEach(item => {
              item.detail = `${item.improvingDrawingUrl},${item.improvingDrawingFileName}`;
            });
          }
          if (res.yard) {
            this.Form_addInfo.yardInfo = [];
            this.Form_addInfo.yardInfo.push(res.yard);
          }
        }
      }).catch(err => {
        const msg = err.result ? err.result : '配置详情获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 获取改变的数据
    getChangeData(id) {
      getChangeData(id).then(res => {
        if (res) {
          this.distributeChangeList = res;
        }
      }).catch(err => {
        const msg = err.result ? err.result : '分配统计数据获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 清空表单
    clearForm(form) {
      for (let key in form) {
        var type = this.getDataType(form[key]);
        if (type === "String") {
          form[key] = "";
        } else if (type === "Array") {
          form[key] = [];
        } else if (type === "Object") {
          const keys = Object.keys(form[key]);
          if (keys.length > 0) {
            this.clearForm(form[key]);
          } else {
            form[key] = {};
          }
        } else {
          form[key] = null;
        }
      }
    },
    // dialog关闭的回调
    dialogClosed() {
      this.activeStep = 2;
      this.activeProcessId = '';
      this.processStepList = [];
      this.distributeTableData = [];
      this.distributeCurrentPage = 1;
      this.distributeTotal = 0;
      this.allocationPlan = null;
      this.fileDownLoadCheckedList = [];
      this.improvingDownLoadCheckedList = [];
      this.downloadCheckedList = [];
      this.improvingCheckedList = [];
      this.distributeChangeList = [];
      this.clearForm(this.baseInfoForm);
      this.clearForm(this.Form_unit);
      this.clearForm(this.Form_leader);
    },
    // 获取数据类型
    getDataType(obj) {
      var typeArr = [
        "String",
        "Object",
        "Number",
        "Array",
        "Function"
      ];
      for (let name of typeArr) {
        if (Object.prototype.toString.call(obj) === `[object ${name}]`) {
          return name;
        }
      }
    },
    // 文件下载
    fileDownLoad(list, type) {
      if (!list || list.length === 0) {
        this.$message({
          message: "请先选择一个文件",
          type: "warning"
        });
        return;
      }
      const downloadObj = {};
      downloadObj.addressList = list;
      downloadObj.configureId = this.activeProcessId;
      downloadObj.downLoadType = type;
      downloadFile(downloadObj).then(res => {
        let fileDownload = require('js-file-download');
        let name = decodeURIComponent(sessionStorage.getItem('CADdownloadFileName'));
        fileDownload(res, name);
      }).catch(err => {
        const msg = err.result ? err.result : '文件下载失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 改变checkbox
    handleCheckedFileChange(val) {
      this.downloadCheckedList = [];
      this.fileDownLoadCheckedList.forEach(item => {
        const arr = item.split(',');
        const obj = {
          address: arr[0],
          fileName: arr[1]
        }
        this.downloadCheckedList.push(obj);
      })
    },
    handleCheckedImprovingChange(val) {
      this.improvingCheckedList = [];
      this.improvingDownLoadCheckedList.forEach(item => {
        const arr = item.split(',');
        const obj = {
          address: arr[0],
          fileName: arr[1]
        }
        this.improvingCheckedList.push(obj);
      })
    },
    // 格式化时间
    formatDate(time, format) {
      var date = new Date(time);
      var fmt = format ? format : "yyyy-MM-dd";
      var o = {
        "M+": date.getMonth() + 1, //月份
        "d+": date.getDate(), //日
        "h+": date.getHours(), //小时
        "m+": date.getMinutes(), //分
        "s+": date.getSeconds(), //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        S: date.getMilliseconds() //毫秒
      };
      if (/(y+)/.test(fmt))
        fmt = fmt.replace(
          RegExp.$1,
          (date.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length == 1
              ? o[k]
              : ("00" + o[k]).substr(("" + o[k]).length)
          );
      return fmt;
    }
  },
  mounted() {
    this.userId = sessionStorage.getItem('userId');
    this.getList();
    this.getUserUnitDic();
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - queryBoxH - operateBoxH;
    }, 1);

    this.pickerOptions_start.disabledDate = (time) => {
      if (this.endTime) {
        return new Date(this.endTime).getTime() < time.getTime();
      }
    }

    this.pickerOptions_end.disabledDate = (time) => {
      if (this.startTime) {
        return new Date(this.startTime).getTime() > time.getTime();
      }
    }
  }
};
</script>

<style>
#configQuery .query-box {
  margin-bottom: 20px;
}
#configQuery .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 18%;
}
#configQuery .query-item > div {
  float: left;
}
#configQuery .query-item > .query-input {
  width: calc(100% - 80px);
}
#configQuery .query-item .el-select {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#configQuery .query-item .el-date-editor.el-input,
#configQuery .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#configQuery .query-btn {
  float: right;
}
#configQuery .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#configQuery .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#configQuery .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#configQuery .status-unreviewed {
  color: #ff9f02;
}
#configQuery .status-pass {
  color: #3891e0;
}
#configQuery .view-box {
  cursor: pointer;
  color: #3891e0;
  font-size: 0;
  height: 28px;
  line-height: 28px;
}
#configQuery .view-box:hover > .view-icon,
#configQuery .view-box:hover > .view-text {
  color: #035cac;
}
#configQuery .view-icon {
  font-size: 14px;
  position: relative;
  top: 1px;
}
#configQuery .view-text {
  font-size: 14px;
}
#configQuery .status-notPass {
  color: #f9463f;
}
#configQuery .status-reviewimg {
  color: #1aa97b;
}
#configQuery .content-item {
  width: 50%;
  float: left;
  margin-bottom: 22px;
  overflow: hidden;
}
#configQuery .content-item > span {
  float: left;
  line-height: 20px;
}
#configQuery .content-item > span:first-child {
  width: 84px;
  text-align: right;
}
#configQuery .content-item > span:last-child {
  width: calc(100% - 84px);
}
#configQuery .download-box {
  margin-bottom: 22px;
}
#configQuery .download-box > div {
  float: left;
}
#configQuery .download-box > div.download-title {
  width: 84px;
  text-align: right;
}
#configQuery .download-box > div.download-content {
  width: calc(100% - 84px);
}
#configQuery .title-box > span {
  width: 154px;
  padding: 5px 20px;
  text-align: center;
  border: 1px solid #cbdbef;
  float: left;
  color: #4a8eec;
  margin-left: 13px;
  margin-bottom: 20px;
  cursor: pointer;
}
#configQuery .title-box > span.processActive {
  background: #4a8eec;
  color: #ffffff;
}
#configQuery .house-info > span {
  width: 84px;
  text-align: right;
  display: inline-block;
  margin-bottom: 5px;
}
#configQuery .project-info {
  margin-top: 20px;
  margin-bottom: 20px;
}
#configQuery .distribution-table-pagination-box {
  position: relative;
  height: 30px;
  border-left: 1px solid #c8c8c8;
  border-right: 1px solid #c8c8c8;
  border-bottom: 1px solid #c8c8c8;
}
#configQuery
  .distribution-table-pagination-box
  > .distribution-table-pagination {
  position: absolute;
  right: 10px;
  top: 3px;
}
#configQuery .file-download {
  float: left;
}
#configQuery .file-download-item {
  line-height: 20px;
  margin-bottom: 10px;
}
#configQuery .build-info-box > span,
#configQuery .yard-info-box > span {
  display: inline-block;
  width: 84px;
  text-align: right;
  margin-bottom: 5px;
}
#configQuery .yard-info-box {
  margin-bottom: 20px;
}
#configQuery .allocation-statistics {
  margin-top: 20px;
}
#configQuery .newUnitName > span {
  float: left;
}
#configQuery .newUnitName > span:first-child {
  letter-spacing: 3.5px;
  width: 70px;
}
#configQuery .newUnitName > span:last-child {
  position: relative;
  left: 2px;
}
.allocation-title {
  width: 84px;
  text-align: right;
  margin-bottom: 5px;
}
</style>


