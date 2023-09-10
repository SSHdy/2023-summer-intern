<template>
  <div id="repairRecord">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">位置：</div>
          <div class="query-input">
            <search-tree-select
              :showSelectTree="showSelectTree"
              :inputHeight="'32px'"
              :inputWidth="'100%'"
              :placeholderText="`请选择位置`"
              :isMultipleSelect="true"
              :checkStrictly="false"
              treeName="recordSearchTree"
              @select-tree-checked="searchTreeChecked"
            ></search-tree-select>
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
        <el-table-column type="index" label="序号" width="50" align="center"></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="yardName" label="院落名称" align="center"></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="buildName" label="楼座名称" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="repairCount"
          label="维修次数"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="repairArea"
          label="维修总面积(m²)"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowAlter="false"
              :isShowDelete="false"
              :isRecord="true"
              recordText="维修档案"
              @recordViewClick="recordView(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
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
        <span>{{dialogTitle}}</span>
      </div>
      <div class="dialog-content">
        <div class="content-left">
          <div class="content-title">
            <span>维修清单</span>
            <span class="line"></span>
          </div>
          <div class="search-date-box" v-if="repairApplyInfoList.length > 0">
            <el-date-picker
              v-model="time"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              @change="searchDateChange"
              value-format="yyyy-MM-dd"
            ></el-date-picker>
          </div>
          <div class="content-list" v-if="repairApplyInfoList.length > 0">
            <div
              class="repair-apply-item"
              :class="{'active': activeRepairApply === index}"
              v-for="(item, index) in repairApplyInfoList"
              :key="index"
              @click="getRepairApplyInfo(index, item.repairApplyId, item.yardName, item.buildName)"
            >
              <span>{{item.title}}</span>
            </div>
          </div>
          <div class="content-tips" v-if="repairApplyInfoList.length === 0">暂无维修清单信息</div>
        </div>
        <div class="content-right">
          <div class="base-info-box">
            <div class="content-title">
              <span>维修信息</span>
              <span class="line"></span>
              <el-popover
                v-model="popoverRepairInfo"
                placement="bottom-end"
                width="300"
                trigger="click"
                popper-class="more-info"
                :visible-arrow="false"
                v-if="repairInfoList.length > 0"
              >
                <ul class="repair-info-more">
                  <li
                    v-for="(item, index) in repairInfoList"
                    :key="index"
                    :class="{'info-active': activeInfo === index}"
                    @click="showInfoDetail(item.id, index)"
                  >{{item.info}}</li>
                </ul>
                <span class="more-btn" slot="reference">
                  更多
                  <i class="el-icon-arrow-down"></i>
                </span>
              </el-popover>
            </div>
            <div class="repair-base-info" v-if="repairInfoList.length > 0">
              <el-row :gutter="20">
                <el-col :span="8" v-if="currentYardName">
                  <div class="grid-content">
                    <span class="repair-info-title">院落名称：</span>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="currentYardName"
                      placement="top-start"
                    >
                      <span class="repair-info-content">{{currentYardName}}</span>
                    </el-tooltip>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content" v-if="currentBuildName">
                    <span class="repair-info-title">楼座名称：</span>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="currentBuildName"
                      placement="top-start"
                    >
                      <span class="repair-info-content">{{currentBuildName}}</span>
                    </el-tooltip>
                  </div>
                </el-col>
                <el-col :span="8" v-if="repairInfoDetail.positionStr">
                  <div class="grid-content">
                    <span class="repair-info-title">维修面积：</span>
                    <span class="repair-info-content">{{repairInfoDetail.repairModuleArea}}m²</span>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8" v-if="repairInfoDetail.repairPartName">
                  <div class="grid-content">
                    <span class="repair-info-title">维修部位：</span>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="repairInfoDetail.repairPartName"
                      placement="top-start"
                    >
                      <span class="repair-info-content">{{repairInfoDetail.repairPartName}}</span>
                    </el-tooltip>
                  </div>
                </el-col>
                <el-col :span="8" v-if="repairInfoDetail.repairModuleTarget">
                  <div class="grid-content">
                    <span class="repair-info-title">维修内容：</span>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="repairInfoDetail.repairModuleTarget"
                      placement="top-start"
                    >
                      <span class="repair-info-content">{{repairInfoDetail.repairModuleTarget}}</span>
                    </el-tooltip>
                  </div>
                </el-col>
                <el-col :span="8" v-if="repairInfoDetail.repairModuleArea">
                  <div class="grid-content">
                    <span class="repair-info-title">维修地点：</span>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="repairInfoDetail.positionStr"
                      placement="top-start"
                    >
                      <span class="repair-info-content">{{repairInfoDetail.positionStr}}</span>
                    </el-tooltip>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8" v-if="repairInfoDetail.completionQuality">
                  <div class="grid-content">
                    <span class="repair-info-title">保质期：</span>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="repairInfoDetail.completionQuality"
                      placement="top-start"
                    >
                      <span class="repair-info-content">{{repairInfoDetail.completionQuality}}m²</span>
                    </el-tooltip>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div class="repair-base-tips" v-if="repairInfoList.length === 0">暂无维修信息</div>
          </div>
          <div class="file-downLoad-box">
            <div class="content-title">
              <span>文件下载</span>
              <span class="line"></span>
            </div>
            <div class="file-downLoad" v-if="haveRepairFile || haveDBFile">
              <div
                class="file-downLoad-category"
                v-if="allDownLoadFileList.dzhApprovalDb.length > 0"
              >
                <div class="category-name">审批原件：</div>
                <div class="category-content">
                  <el-checkbox-group v-model="allCheckedList.dzhApprovalCheckedList">
                    <div
                      v-for="(file, index) in allDownLoadFileList.dzhApprovalDb"
                      :key="index"
                      class="file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                </div>
              </div>
              <div
                class="file-downLoad-category"
                v-if="allDownLoadFileList.repairPlanDb.length > 0"
              >
                <div class="category-name">部门维修审批文件：</div>
                <div class="category-content">
                  <el-checkbox-group v-model="allCheckedList.repairPlanCheckedList">
                    <div
                      v-for="(file, index) in allDownLoadFileList.repairPlanDb"
                      :key="index"
                      class="file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                </div>
              </div>
              <div
                class="file-downLoad-category"
                v-if="allDownLoadFileList.ysRepairPlanDb.length > 0"
              >
                <div class="category-name">预算维修审批文件：</div>
                <div class="category-content">
                  <el-checkbox-group v-model="allCheckedList.ysRepairPlanCheckedList">
                    <div
                      v-for="(file, index) in allDownLoadFileList.ysRepairPlanDb"
                      :key="index"
                      class="file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                </div>
              </div>
              <div
                class="file-downLoad-category"
                v-if="allDownLoadFileList.researchApprovalDb.file.length > 0"
              >
                <div class="category-name">可行性研究报告：</div>
                <div class="category-content">
                  <el-checkbox-group v-model="allCheckedList.researchApprovalFileCheckedList">
                    <div
                      v-for="(file, index) in allDownLoadFileList.researchApprovalDb.file"
                      :key="index"
                      class="file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                </div>
              </div>
              <div
                class="file-downLoad-category"
                v-if="allDownLoadFileList.researchApprovalDb.image.length > 0"
              >
                <div class="category-name">可行性研究效果图：</div>
                <div class="category-content">
                  <el-checkbox-group v-model="allCheckedList.researchApprovalImageCheckedList">
                    <div
                      v-for="(file, index) in allDownLoadFileList.researchApprovalDb.image"
                      :key="index"
                      class="file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                </div>
              </div>
              <div class="file-downLoad-category" v-if="allDownLoadFileList.ldaDb.length > 0">
                <div class="category-name">领导审批：</div>
                <div class="category-content">
                  <el-checkbox-group v-model="allCheckedList.ldaCheckedList">
                    <div
                      v-for="(file, index) in allDownLoadFileList.ldaDb"
                      :key="index"
                      class="file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                </div>
              </div>
              <div
                class="file-downLoad-category"
                v-if="allDownLoadFileList.galCommentDb.length > 0"
              >
                <div class="category-name">发改委批示文件：</div>
                <div class="category-content">
                  <el-checkbox-group v-model="allCheckedList.galCommentCheckedList">
                    <div
                      v-for="(file, index) in allDownLoadFileList.galCommentDb"
                      :key="index"
                      class="file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                </div>
              </div>
              <div
                class="file-downLoad-category"
                v-if="allDownLoadFileList.detailDesignDb.image.length > 0"
              >
                <div class="category-name">施工设计图纸：</div>
                <div class="category-content">
                  <el-checkbox-group v-model="allCheckedList.detailDesignImageCheckedList">
                    <div
                      v-for="(file, index) in allDownLoadFileList.detailDesignDb.image"
                      :key="index"
                      class="file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                </div>
              </div>
              <div
                class="file-downLoad-category"
                v-if="allDownLoadFileList.detailDesignDb.file.length > 0"
              >
                <div class="category-name">施工设计规划许可：</div>
                <div class="category-content">
                  <el-checkbox-group v-model="allCheckedList.detailDesignFileCheckedList">
                    <div
                      v-for="(file, index) in allDownLoadFileList.detailDesignDb.file"
                      :key="index"
                      class="file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                </div>
              </div>
              <div
                class="file-downLoad-category"
                v-if="allDownLoadFileList.projectBudgetDb.length > 0"
              >
                <div class="category-name">维修预算文件：</div>
                <div class="category-content">
                  <el-checkbox-group v-model="allCheckedList.projectBudgetCheckedList">
                    <div
                      v-for="(file, index) in allDownLoadFileList.projectBudgetDb"
                      :key="index"
                      class="file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                </div>
              </div>
              <div
                class="file-downLoad-category"
                v-if="allDownLoadFileList.projectReviewDb.length > 0"
              >
                <div class="category-name">预算评审文件：</div>
                <div class="category-content">
                  <el-checkbox-group v-model="allCheckedList.projectApproveCheckedList">
                    <div
                      v-for="(file, index) in allDownLoadFileList.projectReviewDb"
                      :key="index"
                      class="file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                </div>
              </div>
              <div class="file-downLoad-category" v-if="allDownLoadFileList.auditReport.length > 0">
                <div class="category-name">竣工报告：</div>
                <div class="category-content">
                  <el-checkbox-group v-model="allCheckedList.auditReportCheckedList">
                    <div
                      v-for="(file, index) in allDownLoadFileList.auditReport"
                      :key="index"
                      class="file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                </div>
              </div>
              <div
                class="file-downLoad-category"
                v-if="allDownLoadFileList.buildDrawings.length > 0"
              >
                <div class="category-name">竣工图纸：</div>
                <div class="category-content">
                  <el-checkbox-group v-model="allCheckedList.buildDrawingsCheckedList">
                    <div
                      v-for="(file, index) in allDownLoadFileList.buildDrawings"
                      :key="index"
                      class="file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                </div>
              </div>
              <div class="file-downLoad-category" v-for="(item, index) in dbFileList" :key="index">
                <div class="category-name">{{item.name}}：</div>
                <div class="category-content">
                  <el-checkbox-group v-model="allCheckedList[item.checkedListName]">
                    <div
                      v-for="(file, index) in item.approvalFileList"
                      :key="index"
                      class="file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.title}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                </div>
              </div>
              <el-button size="small" type="primary" @click="fileDownLoad">下载</el-button>
            </div>
            <div class="file-downLoad-tips" v-if="!haveRepairFile && !haveDBFile">暂无文件下载</div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getRepairFilesList,
  getBuildRepairApplyList,
  getRepairApplyFiles,
  getDBAllFiles
} from "./api";
import {
  downloadFile,
  getRepairApprovalDetail,
  getRepairMessageDetail
} from "@/pages/repairManagement/api_2";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import searchTreeSelect from "@/components/treeSelect/treeSelect_6"
export default {
  components: {
    singleBtnGroup,
    tableBtnGroup,
    tablePagination,
    tableComponent,
    cancelBtn,
    confirmBtn,
    searchBtn,
    resetBtn,
    searchTreeSelect
  },
  data() {
    return {
      time: '',
      buildIdList: [],
      yardIdList: [],
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      selectData: [],
      tableData: [],
      dialogTitle: "维修档案",
      operateType: "add",
      dialogVisible: false,
      options: [],
      repairInfoDetail: {
        positionStr: '',
        repairPartName: '',
        repairModuleTarget: '',
        repairModuleArea: '',
        completionQuality: ''
      },
      allDownLoadFileList: {
        dzhApprovalDb: [],
        researchApprovalDb: {
          image: [],
          file: []
        },
        repairPlanDb: [],
        ysRepairPlanDb: [],
        galCommentDb: [],
        detailDesignDb: {
          image: [],
          file: []
        },
        projectBudgetDb: [],
        projectReviewDb: [],
        auditReport: [],
        buildDrawings: [],
        ldaDb: []
      },
      allCheckedList: {
        dzhApprovalCheckedList: [],
        repairPlanCheckedList: [],
        ysRepairPlanCheckedList: [],
        researchApprovalFileCheckedList: [],
        researchApprovalImageCheckedList: [],
        galCommentCheckedList: [],
        detailDesignImageCheckedList: [],
        detailDesignFileCheckedList: [],
        projectBudgetCheckedList: [],
        projectApproveCheckedList: [],
        auditReportCheckedList: [],
        buildDrawingsCheckedList: [],
        ldaCheckedList: []
      },
      currentRepairApplyId: null,
      repairApplyInfoList: [],
      activeRepairApply: 0,
      haveRepairFile: false,
      haveDBFile: false,
      currentYardName: '',
      currentBuildName: '',
      repairInfoList: [],
      activeInfo: 0,
      popoverRepairInfo: false,
      currentBuildId: '',
      showSelectTree: true,
      dbFileList: []
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
      getRepairFilesList(this.currentPage, this.pageSize, this.buildIdList, this.yardIdList).then(res => {
        this.tableData = res.records;
        this.total = res.total;
        this.loadingEnd();
      }).catch(err => {
        this.loadingEnd();
        const msg = err.result ? err.result : '维修档案信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      });
    },
    // 查询信息
    search() {
      this.currentPage = 1;
      this.getList();
    },
    // 重置
    reset() {
      this.buildIdList = [];
      this.yardIdList = [];
      this.currentPage = 1;
      this.showSelectTree = false;
      eventBus.$emit('clearRecordPosition')
      setTimeout(() => {
        this.showSelectTree = true;
      }, 1000);
      this.getList();
    },
    // 查看维修档案
    recordView(row) {
      this.dialogVisible = true;
      this.currentBuildId = row.buildId;
      let startTime = '', endTime = '';
      if (this.time) {
        startTime = `${this.time[0]} 00:00:00`;
        endTime = `${this.time[1]} 23:59:59`;
      }
      const buildId = row ? row.buildId : this.currentBuildId;
      getBuildRepairApplyList(buildId, startTime, endTime).then(res => {
        if (res && res.length > 0) {
          res.forEach(item => {
            item.repairApplyDate = this.formatDate(item.repairApplyDate);
            item.title = `${item.repairApplicant},${item.repairApplyDate}`
          });
          this.repairApplyInfoList = res;
          this.getRepairApplyInfo(0, res[0].repairApplyId, res[0].yardName, res[0].buildName);
        } else {
          this.activeRepairApply = 0;
          this.haveRepairFile = false;
          this.haveDBFile = false;
          this.repairApplyInfoList = [];
          this.repairInfoList = [];
          this.dbFileList = [];
          this.activeInfo = 0;
          this.clearForm(this.allDownLoadFileList);
          this.clearForm(this.allCheckedList);
          this.clearForm(this.repairInfoDetail);
          this.loadingEnd();
        }
      }).catch(err => {
        this.loadingEnd();
        const msg = err.result ? err.result : '维修申请信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
      this.$nextTick(() => {
        this.loadingStart('.dialog-content');
      })
    },
    // 选择日期范围
    searchDateChange(val) {
      this.recordView();
    },
    // 获取维修申请信息
    getRepairApplyInfo(index, id, yardName, buildName) {
      this.loadingStart('.dialog-content');
      this.clearForm(this.allDownLoadFileList);
      this.clearForm(this.allCheckedList);
      this.activeRepairApply = index;
      this.currentRepairApplyId = id;
      this.currentYardName = yardName;
      this.currentBuildName = buildName;
      this.repairInfoList = [];
      this.dbFileList = [];
      this.getRepairApprovalDetail(id);
      this.getRepairApplyFiles(id);
    },
    // 获取维修信息
    getRepairApprovalDetail(repairApplyId) {
      getRepairApprovalDetail(repairApplyId).then(res => {
        if (res.repairModuleDetailDtoList && res.repairModuleDetailDtoList.length > 0) {
          res.repairModuleDetailDtoList.forEach(item => {
            const temp = {
              info: item.positionStr,
              id: item.repairModuleId
            }
            this.repairInfoList.push(temp);
          })
          this.showInfoDetail(res.repairModuleDetailDtoList[0].repairModuleId, 0);
        }
      }).catch(err => {
        const msg = err.result ? err.result : '维修信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 维修信息详情
    showInfoDetail(id, index) {
      this.clearForm(this.repairInfoDetail);
      this.activeInfo = index;
      this.popoverRepairInfo = false;
      getRepairMessageDetail(id).then(res => {
        for ([key, value] of Object.entries(res)) {
          if (this.repairInfoDetail[key] !== undefined) {
            this.repairInfoDetail[key] = value;
          }
        }
      }).catch(err => {
        const msg = err.result ? err.result : '维修信息详情获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    //  查询某条维修申请的所有审批文件 
    getRepairApplyFiles(id) {
      let i = 0;
      getRepairApplyFiles(id).then(res => {
        if (res && res.length > 0) {
          this.haveRepairFile = true;
          res.forEach(Files => {
            if (this.allDownLoadFileList[Files.fileKey] !== undefined) {
              if (Files.fileKey === 'researchApprovalDb') {
                Files.fileList.forEach(item => {
                  const name = item.name.split('_')[1];
                  if (name === 'img') {
                    this.allDownLoadFileList.researchApprovalDb.image.push(item);
                  } else if (name === 'file') {
                    this.allDownLoadFileList.researchApprovalDb.file.push(item);
                  }
                })
                this.allDownLoadFileList.researchApprovalDb.image.forEach(img => {
                  img.detail = `${img.physicalAddress},${img.repairFileName}`;
                })
                this.allDownLoadFileList.researchApprovalDb.file.forEach(file => {
                  file.detail = `${file.physicalAddress},${file.repairFileName}`;
                })
              } else if (Files.fileKey === 'detailDesignDb') {
                Files.fileList.forEach(item => {
                  const name = item.name.split('_')[1];
                  if (name === 'img') {
                    this.allDownLoadFileList.detailDesignDb.image.push(item);
                  } else if (name === 'file') {
                    this.allDownLoadFileList.detailDesignDb.file.push(item);
                  }
                })
                this.allDownLoadFileList.detailDesignDb.image.forEach(img => {
                  img.detail = `${img.physicalAddress},${img.repairFileName}`;
                })
                this.allDownLoadFileList.detailDesignDb.file.forEach(file => {
                  file.detail = `${file.physicalAddress},${file.repairFileName}`;
                })
              } else {
                this.allDownLoadFileList[Files.fileKey] = Files.fileList;
                this.allDownLoadFileList[Files.fileKey].forEach(file => {
                  file.detail = `${file.physicalAddress},${file.repairFileName}`;
                })
              }
            }
          });
        } else {
          this.haveRepairFile = false;
        }
        i++
        if (i === 2) {
          this.loadingEnd();
        }
      }).catch(err => {
        i++
        if (i === 2) {
          this.loadingEnd();
        }
        this.haveRepairFile = false;
        const msg = err.result ? err.result : '审批文件获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
      getDBAllFiles(id).then(dbFiles => {
        if (dbFiles && dbFiles.length > 0) {
          this.haveDBFile = true;
        } else {
          this.haveDBFile = false;
        }
        if (dbFiles && dbFiles.length > 0) {
          this.dbFileList = dbFiles;
          this.dbFileList.forEach((item, index) => {
            item.checkedListName = `checkedListName_${index}`
            this.$set(this.allCheckedList, item.checkedListName, [])
            item.approvalFileList.forEach(file => {
              const arr = file.path.split('/');
              file.title = arr[arr.length - 1];
              file.detail = `${file.path},${file.title}`;
            })
          });
        }
        i++
        if (i === 2) {
          this.loadingEnd();
        }
      }).catch(err => {
        i++
        if (i === 2) {
          this.loadingEnd();
        }
        this.haveDBFile = false;
        const msg = err.result ? err.result : '内控采购流程审批文件获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 搜索树选中
    searchTreeChecked(checked) {
      this.yardIdList = [];
      this.buildIdList = [];
      if (checked && checked.length > 0) {
        checked.forEach(item => {
          if (item.type === 1) {
            this.yardIdList.push(item.value);
          } else if (item.type === 2) {
            this.buildIdList.push(item.value);
          }
        })
      }
    },
    // 文件下载
    fileDownLoad() {
      let downloadList = [];
      for (let key in this.allCheckedList) {
        if (this.allCheckedList[key].length > 0) {
          this.allCheckedList[key].forEach(item => {
            const arr = item.split(',');
            const obj = {
              address: arr[0],
              fileName: arr[1]
            }
            downloadList.push(obj);
          })
        }
      }
      if (downloadList.length === 0) {
        this.$message({
          message: "请先选择一个文件",
          type: "warning"
        });
        return;
      }
      let obj = {};
      // 去重
      downloadList = downloadList.reduce((item, next) => {
        obj[next.address] ? '' : obj[next.address] = true && item.push(next);
        return item;
      }, []);
      downloadFile(downloadList, 20, this.currentRepairApplyId).then(res => {
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
      this.currentRepairApplyId = null;
      this.activeRepairApply = 0;
      this.haveRepairFile = false;
      this.haveDBFile = false;
      this.currentYardName = '';
      this.currentBuildName = '';
      this.repairApplyInfoList = [];
      this.repairInfoList = [];
      this.activeInfo = 0;
      this.time = '';
      this.dbFileList = [];
      this.clearForm(this.allDownLoadFileList);
      this.clearForm(this.allCheckedList);
      this.clearForm(this.repairInfoDetail);
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
    // 只能输入正浮点数
    limitFloat(key, flag) {
      this.Form[key] = this.Form[key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '')
      if (flag && this.Form[key] !== '') {
        this.Form[key] = (this.Form[key] - 0).toFixed(2);
      }
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
    this.getList();
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - queryBoxH - operateBoxH;
    }, 1);
  }
};
</script>

<style>
#repairRecord .query-box {
  margin-bottom: 20px;
}
#repairRecord .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#repairRecord .query-item > div {
  float: left;
}
#repairRecord .query-item > .query-input {
  width: calc(100% - 80px);
}
#repairRecord .query-item .el-date-editor.el-input,
#repairRecord .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#repairRecord .query-btn {
  float: left;
  margin-left: 50px;
}
#repairRecord .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#repairRecord .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#repairRecord .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#repairRecord .el-dialog__body {
  background-color: #efefef;
  padding: 15px;
}
#repairRecord .dialog-content {
  height: 500px;
}
#repairRecord .content-title {
  line-height: 30px;
  padding-left: 20px;
  position: relative;
}
#repairRecord .content-left {
  width: 230px;
  height: 100%;
  background-color: #ffffff;
  float: left;
}
#repairRecord .content-title > .line {
  position: absolute;
  bottom: -1px;
  left: 18px;
  width: 60px;
  height: 2px;
  background-color: #4490f1;
}
#repairRecord .content-right {
  width: calc(100% - 245px);
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  background-color: #ffffff;
  float: right;
}
#repairRecord .content-right .content-title {
  border-bottom: 1px solid #d8d8d8;
  margin-left: 20px;
  margin-right: 5px;
  padding-left: 0px;
}
#repairRecord .content-right .content-title > .line {
  left: -2px;
}
#repairRecord .repair-base-info {
  padding-left: 20px;
  padding-right: 5px;
  margin-top: 20px;
}
#repairRecord .repair-base-info .grid-content .repair-info-title {
  width: 70px;
  text-align: right;
}
#repairRecord .repair-base-info .grid-content .repair-info-content {
  width: calc(100% - 70px);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
#repairRecord .grid-content {
  line-height: 40px;
  overflow: hidden;
}
#repairRecord .grid-content > span {
  float: left;
}
#repairRecord .file-downLoad-box {
  margin-top: 30px;
  margin-bottom: 30px;
}
#repairRecord .file-downLoad {
  margin-left: 20px;
}
#repairRecord .file-download-item {
  line-height: 40px;
}
#repairRecord .repair-apply-item span {
  line-height: 40px;
  border-bottom: 1px solid #d8d8d8;
  display: inline-block;
  margin-left: 20px;
  margin-right: 5px;
  width: calc(100% - 25px);
  cursor: pointer;
}
#repairRecord .repair-apply-item.active span {
  color: #4490f1;
}
#repairRecord .file-downLoad-category {
  margin-top: 20px;
}
#repairRecord .content-list {
  height: calc(100% - 90px);
  overflow-y: auto;
  overflow-x: hidden;
}
#repairRecord .search-date-box {
  margin-top: 10px;
  margin-bottom: 10px;
}
#repairRecord .search-date-box .el-date-editor {
  width: 224px;
  margin-left: 3px;
}
#repairRecord .more-btn {
  color: #3891e0;
  cursor: pointer;
  float: right;
  margin-right: 10px;
}
#repairRecord .more-btn:hover {
  color: #035cac;
}
#repairRecord .content-tips,
#repairRecord .repair-base-tips,
#repairRecord .file-downLoad-tips {
  padding-left: 20px;
  padding-top: 30px;
}
.more-info {
  background: #f5f5f5;
  padding: 0;
}
.more-info .repair-info-more > li {
  line-height: 30px;
  padding: 0 10px;
  cursor: pointer;
}
.more-info .repair-info-more > li:hover {
  background: #498fea;
  color: #fff;
}
.more-info .repair-info-more > .info-active {
  background: #498fea;
  color: #fff;
}
</style>