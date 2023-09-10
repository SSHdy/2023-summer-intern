<template>
  <div id="repairApply">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item" style="margin-right:50px">
          <div class="query-title">申请编号：</div>
          <div class="query-input">
            <el-input v-model="repairApplyNo" placeholder="请输入申请编号"></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-right:50px">
          <div class="query-title">申请状态：</div>
          <div class="query-input">
            <el-select v-model="applyStatus" placeholder="请选择申请状态" size="small">
              <el-option
                v-for="item in applyStatusOpts"
                :key="item.value"
                :label="item.text"
                :value="item.value"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="query-item">
          <div class="query-title">维修时间：</div>
          <div class="query-input">
            <el-date-picker
              v-model="startApplicationTime"
              type="date"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择日期"
            ></el-date-picker>
          </div>
        </div>
        <div class="query-item">
          <div class="query-title" style="padding-right: 10px">至</div>
          <div class="query-input">
            <el-date-picker
              v-model="endApplicationTime"
              type="date"
              value-format="yyyy-MM-dd 23:59:59"
              placeholder="选择日期"
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
      <table-component
        :tableData="tableData"
        :tableHeight="tableHeight"
        @select="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center"></el-table-column>
        <el-table-column
          prop="repairApplyNo"
          label="申请编号"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          label="使用单位"
          prop="userUnits"
          align="center"
          title="userUnits"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="repairModuleArea"
          label="维修面积(m²)"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="repairApplyDate"
          label="申请时间"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
<!--        <el-table-column
          prop="repairProress"
          label="施工进度"
          align="center"
          :show-overflow-tooltip="true"
        >
          &lt;!&ndash; <template slot-scope="scope">
            <el-progress :percentage="scope.row.repairProgress"></el-progress>
          </template>&ndash;&gt;
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.repairProgress"
              :text-inside="true"
              :stroke-width="12"
            ></el-progress>
          </template>
        </el-table-column>-->
        <el-table-column prop="applyStatus" label="申请状态" align="center">
          <template slot-scope="scope">
            <div
              :class="{'notAudit' : scope.row.applyStatus==='未审核','passAudit' : scope.row.applyStatus==='审核通过','failedAudit' : scope.row.applyStatus==='审核未通过','Auditing' : scope.row.applyStatus==='审核中'}"
            >
              <span>{{scope.row.applyStatus}}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="审批" width="90" align="center" v-if="false">
          <template slot-scope="scope">
            <single-btn-group
              :isShowLook="true"
              :isShowAlter="false"
              :isShowDetail="false"
              :isShowDelete="false"
              @lookClick="singleLook(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :class="{'pl':scope.row.applyStatus!=='未审核'}"
              :isShowAlter="scope.row.applyStatus==='未审核'"
              :isShowDetail="true"
              :isShowDelete="false"
              @updateClick="singleUpdate(scope.row)"
              @detailClick="singleDetail(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group @addClick="addUnit" :batchDelBtnShow="false"></table-btn-group>
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
    <!-- 新增、修改弹出框 -->
    <el-dialog
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="1100px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{title}}</span>
      </div>

      <div v-show="lookFlag">
        <div class="query-box" id="queryBox">
          <div class="clearfix dialog">
            <div class="query-item">
              <div class="query-title">{{operateType == 'detail'?'使用单位：':'使用单位'}}</div>
              <div class="query-input">
                <el-select
                  v-if="operateType!=='detail'"
                  v-model="unitIdList"
                  multiple
                  popper-class="unit"
                  placeholder="请选择使用单位"
                >
                  <el-option
                    v-for="item in unitOptions"
                    :key="item.unitId"
                    :label="item.unitName"
                    :value="item.unitId"
                  ></el-option>
                </el-select>
                <el-input
                  v-model="unitIdLists"
                  v-if="operateType=='detail'"
                  :readonly="operateType==='detail'"
                  :title="unitIdLists"
                  :class="{'detail-form-item' : operateType==='detail'}"
                  style="width:200px"
                ></el-input>
              </div>
            </div>
            <div class="query-item" v-if="operateType=='detail'">
              <div class="query-title">申请人：</div>
              <div class="query-input">
                <el-input
                  v-model="repairApplicant"
                  :readonly="operateType==='detail'"
                  :class="{'detail-form-item' : operateType==='detail'}"
                  style="width:200px"
                ></el-input>
              </div>
            </div>
            <div class="query-item" v-if="operateType=='detail'">
              <div class="query-title">申请时间：</div>
              <div class="query-input">
                <el-input
                  v-model="repairApplyDate"
                  :readonly="operateType==='detail'"
                  :class="{'detail-form-item' : operateType==='detail'}"
                  style="width:200px"
                ></el-input>
              </div>
            </div>
            <div class="query-item" v-if="operateType=='detail'">
              <div class="query-title">维修状态：</div>
              <div class="query-input">
                <el-input
                  v-model="applyStatus"
                  :readonly="operateType==='detail'"
                  :class="{'detail-form-item' : operateType==='detail'}"
                  style="width:200px"
                ></el-input>
              </div>
            </div>
          </div>
        </div>
        <div class="query-box" id="queryBox">
          <div class="clearfix" style="border-bottom:1px solid #ccc">
            <div class="query-left" v-if="operateType !=='detail'">添加维修：</div>
            <div class="query-left" v-else>维修记录：</div>
            <div class="query-right">
              <span @click="resetRepaireContent('Form')" v-if="operateType !=='detail' ">
                <i class="iconfont">&#xe767;</i> 重置
              </span>
              <span @click="addRepaireContent('Form')" v-if="operateType =='add'">
                <i class="el-icon-circle-plus-outline"></i> 新增
              </span>
              <el-popover
                class="detail"
                v-model="popoverRepairInfo"
                placement="bottom-end"
                width="300"
                trigger="click"
                popper-class="More-info"
                :visible-arrow="false"
              >
                <ul class="repair-info-more">
                  <li
                    v-for="(item, index) in repairModuleList"
                    :key="index"
                    :class="{'info-active': activeInfo === index}"
                    @click="showInfoDetail(index)"
                  >{{item.repairModule.positionStr}}</li>
                </ul>
                <span class="more-btn" slot="reference" v-show="repairModuleList.length > 0">
                  更多
                  <i class="el-icon-arrow-down"></i>
                </span>
              </el-popover>
            </div>
          </div>
        </div>
        <el-form :inline="true" :model="Form" ref="Form" :rules="rules">
          <el-form-item
            :label="operateType==='detail'? '维修部位：' : '维修部位'"
            label-width="120px"
            prop="repairPartId"
            :class="{'detail-form-item' : operateType==='detail'}"
          >
            <el-select
              v-if="operateType!=='detail'"
              v-model="Form.repairPartId"
              placeholder="请选择维修部位"
              style="width:200px"
            >
              <el-option
                v-for="(item,index) in repairePartOpts"
                :key="index"
                :label="item.repairPartName"
                :value="item.repairPartsId"
              ></el-option>
            </el-select>
            <el-input
              v-if="operateType==='detail'"
              v-model="Form.repairPartId"
              size="small"
              :readonly="operateType==='detail'"
            ></el-input>
          </el-form-item>
          <el-form-item
            :label="operateType==='detail'? '坐落及楼栋：' : '坐落及楼栋'"
            label-width="120px"
            prop="repairModulePosition"
            :class="{'detail-form-item' : operateType==='detail'}"
          >
            <el-input
              v-model="Form.repairModulePosition"
              size="small"
              :placeholder="operateType =='detail' ? '':'请输入坐落及楼栋' "
              :readonly="operateType==='detail'"
            ></el-input>
          </el-form-item>
          <el-form-item
            :label="operateType==='detail'? '维修内容：' : '维修内容'"
            label-width="120px"
            prop="repairModuleTarget"
            :class="{'detail-form-item' : operateType==='detail' }"
          >
            <el-input
              v-model="Form.repairModuleTarget"
              size="small"
              :placeholder="operateType =='detail' ? '':'请输入维修内容'"
              :readonly="operateType==='detail'"
            ></el-input>
          </el-form-item>
          <el-form-item
            :label="operateType==='detail'? '具体方位：' : '具体方位'"
            label-width="120px"
            prop="RepaireYard"
            :class="{'detail-form-item' : operateType==='detail'}"
          >
            <tree-select
              @loadNode="loadNode"
              @handleCheckChange="handleCheckChange"
              @handleNodeExpand="handleNodeExpand"
              :treeShow="treeShow"
              :inputWidth="'200px'"
              :inputHeight="'34px'"
              v-if="operateType!=='detail'"
            ></tree-select>
            <el-input
              v-model="Form.RepairYard"
              size="small"
              :readonly="operateType==='detail'"
              v-if="operateType==='detail'"
            ></el-input>
          </el-form-item>
          <el-form-item
            :label="operateType==='detail'? '维修面积(m²)：' : '维修面积(m²)'"
            label-width="120px"
            prop="repairModuleArea"
            :class="{'detail-form-item' : operateType==='detail'}"
          >
            <el-input
              v-model="Form.repairModuleArea"
              size="small"
              @keyup.native="limitFloat('repairModuleArea')"
              @blur="limitFloat('repairModuleArea',true)"
              :placeholder="operateType =='detail' ? '':'请输入维修面积'"
              :readonly="operateType==='detail' "
            ></el-input>
          </el-form-item>
          <el-form-item
            :label="(operateType==='detail')? '照片：' : '照片'"
            label-width="120px"
            :class="{'detail-form-item' : operateType==='detail'}"
            style="display: block;"
            prop="picture"
          >
            <div class="upload_box" v-if="operateType!=='detail'">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="uploadImgUrl"
                list-type="picture-card"
                :file-list="fileList"
                :on-remove="handleRemove"
                :auto-upload="false"
                :on-success="handleSuccess"
                :on-change="handleChange"
                :on-error="handleError"
                :accept="acceptImgType"
                :limit="limitImgNum"
                :on-exceed="handleExceed"
                :before-upload="beforeAvatarUpload"
                multiple
              >
                <i class="el-icon-plus"></i>
              </el-upload>
            </div>
            <div
              class="img-preview-box clearfix"
              v-if="operateType==='detail'"
              style="padding-top:13px"
            >
              <div
                class="img-preview-item"
                v-for="(item, index) in detailFileList"
                :key="index"
                @click="preview(item.virtualAddress)"
              >
                <img :src="getImgUrl(item.virtualAddress)" alt="已上传证件" />
                <div class="bigger-view-warpper">
                  <i class="bigger-view-icon"></i>
                </div>
                <el-checkbox @change="imgCheckboxChange($event, item.physicalAddress)"></el-checkbox>
              </div>
            </div>
            <el-button
              type="primary"
              icon="el-icon-download"
              @click="downloadImg"
              v-if="detailFileList.length"
            >下载图片</el-button>
          </el-form-item>
        </el-form>
        <div class="operate_btn">
          <!-- <span class="save"><i class="iconfont">&#xe936;</i>保存</span> -->
          <span
            v-show="editFlag || operateType == 'edit' ||dealFlag "
            @click="editRepaireContent('Form')"
          >
            <i class="iconfont">&#xe610;</i>修改
          </span>
        </div>
      </div>
      <div v-show="!lookFlag" style="text-align:center;font-size:20px;color:#ff3040">该维修记录已被删除</div>
      <span slot="footer" class="dialog-footer" v-show="operateType!=='detail'&&lookFlag">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <el-dialog
      :visible.sync="DialogVisible"
      :close-on-click-modal="false"
      width="780px"
      @closed="Closed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{title}}</span>
      </div>
      <el-steps
        :active="applyStepActive"
        align-center
        finish-status="success"
        :space="100"
        direction="vertical"
      >
        <el-step v-for="(item,index) in applyProcess" :key="index">
          <div slot="description">
            <div>
              <span>流程：</span>
              {{item.stepName}}
            </div>
            <div :class="{'hidden':!item.transactors}">
              <span>操作人：</span>
              {{item.transactors}}
            </div>
            <div :class="{'hidden':!item.processingTime}">
              <span>操作时间：</span>
              {{item.processingTime}}
            </div>
          </div>
        </el-step>
      </el-steps>
    </el-dialog>
  </div>
</template>
<script>
import { BASE_URL, url_name_1 } from "@/axios/global";
import { uploadUrl } from "@/axios/commonApi";
import {
  getRepairApplyList,
  getRepairApplyDetail,
  getUnitList,
  // getSpareRoomTree,
  addRepair,
  getRepairPartsList,
  updateRepair,
  download,
  selectProcessHistory
} from "./api";
import { getYardTree } from "../home/api.js";
import { eventBus } from "@/eventBus/eventBus";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tableComponent from "@/components/table/table2.vue";
import tablePagination from "@/components/tableGroup/tablePagination";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import { loadavg } from "os";
import { fail } from "assert";
import treeSelect from "@/components/treeSelect/treeSelect_2";
import { setTimeout } from "timers";
import { log } from "util";
export default {
  components: {
    treeSelect,
    singleBtnGroup,
    tableComponent,
    tableBtnGroup,
    tablePagination,
    cancelBtn,
    confirmBtn,
    searchBtn,
    resetBtn
  },
  data() {
    let validUpload = (rule, value, callback) => {
      if (!this.Form.picture) {
        callback(new Error("请先上传图片"));
      } else {
        callback();
      }
    };
    let validRepairePart = (rule, value, callback) => {
      if (this.operateType != "detail") {
        if (!this.Form.RepaireYard) {
          callback(new Error("具体方位不能为空"));
        } else {
          callback();
        }
      }
    };
    return {
      editFlag: false, //修改按钮的显示与隐藏
      dealFlag: false,
      activeInfo: 0,
      popoverRepairInfo: false,
      currentLevel: null,
      flag: false,
      Form: {
        positionStr: "",
        repairModuleId: "",
        repairModuleTarget: "",
        repairModuleArea: "",
        repairPartId: "",
        buildId: "",
        floorName: "",
        houseId: "",
        yardId: "",
        addressList: [],
        RepairYard: "",
        RepaireYard: "",
        repairModulePosition: "",
        picture: ""
      },
      deleteRepairImgIdList: [],
      treeShow: false,
      treeData: [],
      checkedNodes: [],
      houseIdList: [],
      houseNameList: [],
      expandIdList: [],
      houseNumber: "",
      uploadImgNum: 0,
      uploadedImgNum: 0,
      uploadSuccessImgNum: 0,
      detailFileList: [],
      fileList: [],
      oldFileList: [],
      token: "",
      postHeaders: {
        Authorization: ""
      },
      limitImgNum: 5,
      acceptImgType: "image/jpeg,image/gif,image/png,image/bmp",
      uploadImgUrl: `${uploadUrl}?uploadType=propertyRights`,
      operateType: "add",
      userUnit: "",
      dialogTitle: "",
      dialogVisible: false,
      dialogVisibles: false,
      DialogVisible: false,
      notAudit: false,
      passAudit: false,
      failedAudit: false,
      Auditing: false,
      currentPage: 1,
      pageSize: 15,
      total: 1,
      repairApplyNo: "",
      applyStatus: "",
      startApplicationTime: "",
      endApplicationTime: "",
      userId: null,
      loading: false,
      tableHeight: 0,
      tableData: [],
      RepaireContentList: [],
      applyStatusOpts: [
        { text: "未审核", value: 0 },
        { text: "审核通过", value: 1 },
        { text: "审核未通过", value: 2 },
        { text: "审核中", value: 3 }
      ],
      userIds: ["13"],
      removeUserIds: [],
      deleteUnitIdList: [],
      unitIdList: [],
      unitIdLists: "",
      repairApplicantId: [],
      repairModuleList: [],
      repairModule: [],
      unitOptions: [],
      repairApplyNum: "",
      repairApplicant: "",
      repairApplyDate: "",
      repaireStatus: "",
      repairePartOpts: [],
      title: "",
      downloadList: [],
      submitFlag: false,
      applyStepActive: 0,
      rules: {
        repairPartId: [
          { required: true, message: "维修部位不能为空", trigger: "blur" }
        ],
        RepaireYard: [
          { required: true, validator: validRepairePart, trigger: "blur" }
        ],
        repairModulePosition: [
          { required: true, message: "坐落及楼栋不能为空", trigger: "blur" }
        ],
        repairModuleTarget: [
          { required: true, message: "维修内容不能为空", trigger: "blur" }
        ],
        repairModuleArea: [
          { required: true, message: "维修面积不能为空", trigger: "blur" }
        ],
        picture: [{ required: true, validator: validUpload, trigger: "blur" }]
      },
      applyProcess: [],
      active: null,
      indexs: 0,
      lookFlag: true
    };
  },
  methods: {
    // 开始加载
    loadingStart(classname) {
      this.loading = this.$loading({
        lock: true,
        text: "正在加载...",
        spinner: "el-icon-loading",
        target: classname,
        customClass: "loading-box"
      });
    },
    // 结束加载
    loadingEnd() {
      this.loading.close();
    },
    getList() {
      this.loadingStart(".table-box");
      if (
        this.startApplicationTime &&
        this.endApplicationTime &&
        new Date(this.endApplicationTime) < new Date(this.startApplicationTime)
      ) {
        this.$message({
          message: "开始日期不能大于结束日期!",
          type: "warning"
        });
      } else {
        this.userId = parseInt(sessionStorage.getItem("userId"));
        getRepairApplyList(
          this.currentPage,
          this.endApplicationTime,
          this.pageSize,
          this.repairApplyNo,
          this.startApplicationTime,
          this.userId,
          this.applyStatus
        )
          .then(res => {
            this.total = res.total;
            res.records.forEach(item => {
              if (item.applyStatus == 0) {
                item.applyStatus = "未审核";
              }
              if (item.applyStatus == 1) {
                item.applyStatus = "审核通过";
              }
              if (item.applyStatus == 2) {
                item.applyStatus = "审核未通过";
              }
              if (item.applyStatus == 3) {
                item.applyStatus = "审核中";
              }
              if (item.repairApplyDate) {
                item.repairApplyDate = this.formatDate(item.repairApplyDate);
              }
              if (item.unitNameList) {
                item["userUnits"] = item.unitNameList.join(",");
              }
            });
            this.tableData = res.records;
            this.loadingEnd();
          })
          .catch(err => {
            const msg = err.result ? err.result : "获取列表失败";
            this.$message({
              message: msg,
              type: "error"
            });
            this.loadingEnd();
          });
      }
    },
    //   查询
    search() {
      this.currentPage = 1;
      this.getList();
    },
    // 重置
    reset() {
      this.currentPage = 1;
      this.repairApplyNo = "";
      this.applyStatus = null;
      this.startApplicationTime = "";
      this.endApplicationTime = "";
      this.getList();
    },
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
    },
    // 新增
    addUnit() {
      this.operateType = "add";
      this.title = "新增维修信息";
      this.dialogVisible = true;
      this.getUnit();
      this.RepaireContentList = [];
      // this.getSpareRoomTreeList();
      this.treeShow = true;
      this.getRepairPart();
    },
    // 改变页面条数
    changeSize(size) {
      this.currentPage = 1;
      this.pageSize = size;
      this.getList();
    },
    // 改变页面页数
    changePage(page) {
      this.currentPage = page;
      this.getList();
    },
    // 获取单位信息
    getUnit() {
      getUnitList()
        .then(res => {
          this.unitOptions = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取详情列表失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取用房
    getRepairPart() {
      getRepairPartsList()
        .then(res => {
          this.repairePartOpts = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取闲置住房失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 单个详情
    singleDetail(row) {
      this.repairApplyId = row.repairApplyId;
      this.operateType = "detail";
      this.title = "维修信息详情";
      this.dialogVisible = true;
      this.getUnit();
      this.getRepairPart();
      getRepairApplyDetail(row.repairApplyId)
        .then(res => {
          if (res) {
            res.repairModuleDetailList.forEach(item => {
              let obj = {
                repairModule: {}
              };
              obj["addressList"] = item.repairImgList;
              obj["deleteRepairImgIdList"] = this.deleteRepairImgIdList;
              obj.repairModule.houseId = item.houseId ? item.houseId : "";
              obj.repairModule.floorName = item.floorName ? item.floorName : "";
              obj.repairModule.buildId = item.buildId ? item.buildId : "";
              obj.repairModule.yardId = item.yardId ? item.yardId : "";
              obj.repairModule.repairModuleTarget = item.repairModuleTarget;
              obj.repairModule.repairModuleArea = item.repairModuleArea;
              obj.repairModule.repairModuleId = item.repairModuleId;
              obj.repairModule.repairPartName = item.repairPartName;
              obj.repairModule.positionStr = item.positionStr;
              obj.repairModule.repairModulePosition = item.repairModulePosition;
              this.repairModuleList.push(obj);
            });
            let arr = ["未审核", "审核通过", "审核不通过", "审核中"];
            this.unitIdLists =
              res.unitNameList.length > 0
                ? res.unitNameList.join(",")
                : res.unitNameList[0];
            this.repairApplicant = res.repairApplicant;
            this.applyStatus = arr[res.applyStatus];
            this.repairApplyDate = res.repairApplyDate;
            this.Form.repairModuleTarget =
              res.repairModuleDetailList[0].repairModuleTarget;
            this.Form.repairModulePosition =
              res.repairModuleDetailList[0].repairModulePosition;
            this.repairePartOpts.forEach(item => {
              if (
                item.repairPartsId ===
                res.repairModuleDetailList[0].repairPartId
              ) {
                this.Form.repairPartId = item.repairPartName;
              }
            });
            this.Form.RepairYard = res.repairModuleDetailList[0].positionStr;
            this.Form.repairModuleArea =
              res.repairModuleDetailList[0].repairModuleArea;
            if (res.repairModuleDetailList[0].repairImgList.length > 0) {
              res.repairModuleDetailList[0].repairImgList.forEach(
                (item, index) => {
                  const ext = item.virtualAddress.split(".")[1];
                  if (ext === "rar" || ext === "zip") {
                    item.virtualAddress = "/images/compress.png";
                  }
                  this.detailFileList.push(item);
                }
              );
            }
          } else {
            this.lookFlag = false;
          }
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取维修申请详情失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 单个修改
    singleUpdate(row) {
      this.repairApplyId = row.repairApplyId;
      this.operateType = "edit";
      this.title = "修改维修信息";
      this.dialogVisible = true;
      this.dialogTitle = "修改维修信息";
      this.getUnit();
      this.RepaireContentList = [];
      this.treeShow = true;
      // this.getSpareRoomTreeList(row.repairApplyId);
      this.getRepairPart();
      getRepairApplyDetail(row.repairApplyId)
        .then(res => {
          if (res) {
            res.repairModuleDetailList.forEach(item => {
              let obj = {
                repairModule: {}
              };
              obj["addressList"] = [];
              obj["addressImgList"] = item.repairImgList;
              obj["deleteRepairImgIdList"] = [];
              obj.repairModule.houseId = item.houseId ? item.houseId : "";
              obj.repairModule.houseNumber = item.houseNumber
                ? item.houseNumber
                : "";
              obj.repairModule.floorName = item.floorName ? item.floorName : "";
              obj.repairModule.buildId = item.buildId ? item.buildId : "";
              obj.repairModule.buildName = item.buildName ? item.buildName : "";
              obj.repairModule.yardId = item.yardId ? item.yardId : "";
              obj.repairModule.yardName = item.yardName ? item.yardName : "";
              obj.repairModule.repairModuleTarget = item.repairModuleTarget;
              obj.repairModule.repairModuleArea = item.repairModuleArea;
              obj.repairModule.repairModuleId = item.repairModuleId;
              obj.repairModule.repairPartId = item.repairPartId
                ? item.repairPartId
                : "";
              obj.repairModule.positionStr = item.positionStr
                ? item.positionStr
                : "";
              // if(item.positionStr.indexOf('-') == -1){
              //    obj.repairModule.houseId = "";
              // obj.repairModule.houseNumber = item.houseNumber
              //   ? item.houseNumber
              //   : "";
              // obj.repairModule.floorName = item.floorName ? item.floorName : "";
              // obj.repairModule.buildId = item.buildId ? item.buildId : "";
              // obj.repairModule.buildName = item.buildName ? item.buildName : "";
              // }
              obj.repairModule.repairModulePosition = item.repairModulePosition
                ? item.repairModulePosition
                : "";
              this.repairModuleList.push(obj);
            });
            this.unitOptions.forEach(item => {
              res.unitNameList.forEach((items, index) => {
                if (item.unitName == res.unitNameList[index]) {
                  this.unitIdList.push(item.unitId);
                }
              });
            });
            this.showForm(this.indexs);
          } else {
            this.lookFlag = false;
          }
        })
        .catch(err => {
          const msg = err.result ? err.result : "维修申请详情获取失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 修改页面的form表单数据展示
    showForm(index) {
      this.Form.repairModuleId = this.repairModuleList[index].repairModule
        .repairModuleId
        ? this.repairModuleList[index].repairModule.repairModuleId
        : "";
      this.Form.repairModuleTarget = this.repairModuleList[index].repairModule
        .repairModuleTarget
        ? this.repairModuleList[index].repairModule.repairModuleTarget
        : "";
      this.Form.repairModuleArea = this.repairModuleList[index].repairModule
        .repairModuleArea
        ? this.repairModuleList[index].repairModule.repairModuleArea
        : "";
      this.Form.repairModulePosition = this.repairModuleList[index].repairModule
        .repairModulePosition
        ? this.repairModuleList[index].repairModule.repairModulePosition
        : "";
      this.Form.repairPartId = this.repairModuleList[index].repairModule
        .repairPartId
        ? this.repairModuleList[index].repairModule.repairPartId
        : "";
      this.Form.positionStr = this.repairModuleList[index].repairModule
        .positionStr
        ? this.repairModuleList[index].repairModule.positionStr
        : "";
      if (
        this.repairModuleList[index].addressImgList &&
        this.repairModuleList[index].addressImgList.length > 0
      ) {
        let imgNum = 0;
        if (this.repairModuleList[index].addressImgList.length > 0) {
          this.Form.submitFlag = "true";
        }
        this.repairModuleList[index].addressImgList.forEach(item => {
          imgNum++;
          const ext = item.virtualAddress.split(".")[1];
          if (ext === "rar" || ext === "zip") {
            item.virtualAddress = "/images/compress.png";
          }
          const obj = {
            name: item.repairImgId,
            url:
              BASE_URL +
              url_name_1 +
              item.virtualAddress +
              "?access_token=" +
              this.token,
            physics: item.physicalAddress
          };
          this.fileList.push(obj);
          this.oldFileList.push(obj);
        });
        if (this.fileList.length > 0) {
          this.Form.picture = "true";
        }
        this.uploadedImgNum = imgNum;
      }
      if (this.repairModuleList[index].repairModule.houseId) {
        this.Form.yardId = this.repairModuleList[index].repairModule.yardId;
        this.Form.buildId = this.repairModuleList[index].repairModule.buildId;
        this.Form.floorName = this.repairModuleList[
          index
        ].repairModule.floorName;
        this.Form.houseId = this.repairModuleList[index].repairModule.houseId;
        this.houseNameList.push(
          this.repairModuleList[index].repairModule.houseNumber
        );
        this.houseIdList.push(
          `${this.repairModuleList[index].repairModule.houseId}`
        );
        this.expandIdList.push(
          this.repairModuleList[index].repairModule.yardId,
          this.repairModuleList[index].repairModule.buildId,
          this.repairModuleList[index].repairModule.floorName
        );
        eventBus.$emit(
          "setTreeSelectVal",
          this.houseNameList,
          this.houseIdList,
          this.expandIdList
        );
      } else if (this.repairModuleList[index].repairModule.floorName) {
        this.Form.buildId = this.repairModuleList[index].repairModule.buildId;
        this.Form.floorName = this.repairModuleList[
          index
        ].repairModule.floorName;
        this.houseNameList.push(
          `${this.repairModuleList[index].repairModule.floorName}层`
        );
        this.houseIdList.push(
          `${this.repairModuleList[index].repairModule.yardId}:${this.repairModuleList[index].repairModule.buildId}:${this.repairModuleList[index].repairModule.floorName}`
        );
        this.expandIdList.push(
          this.repairModuleList[index].repairModule.buildId
        );
        eventBus.$emit(
          "setTreeSelectVal",
          this.houseNameList,
          this.houseIdList,
          this.expandIdList
        );
      } else if (this.repairModuleList[index].repairModule.buildId) {
        this.Form.buildId = this.repairModuleList[index].repairModule.buildId;
        this.houseNameList.push(
          this.repairModuleList[index].repairModule.buildName
        );
        this.houseIdList.push(
          `${this.repairModuleList[index].repairModule.buildId}`
        );
        this.expandIdList.push(
          this.repairModuleList[index].repairModule.buildId
        );
        eventBus.$emit(
          "setTreeSelectVal",
          this.houseNameList,
          this.houseIdList,
          this.expandIdList
        );
      } else if (this.repairModuleList[index].repairModule.yardId) {
        this.Form.yardId = this.repairModuleList[index].repairModule.yardId;
        this.houseNameList.push(
          this.repairModuleList[index].repairModule.yardName
        );
        this.houseIdList.push(
          `${this.repairModuleList[index].repairModule.yardId}`
        );
        eventBus.$emit(
          "setTreeSelectVal",
          this.houseNameList,
          this.houseIdList
        );
      }
      if (this.houseNameList.length > 0) {
        this.Form.RepaireYard = "true";
      } else {
        this.Form.RepaireYard = "";
      }
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        this.submitFlag = true;
        if (valid) {
          this.uploadImgNum === 0
            ? this.saveRepaireContent()
            : this.submitUpload();
        } else {
          return false;
        }
      });
    },
    // 保存表单
    saveForm(formName) {
      if (this.operateType == "add") {
        let repairModuleList = [];
        // let houseNodes = {};
        // if (this.checkedNodes.type) {
        this.repairApplicant = sessionStorage.getItem("name");
        this.repairApplicantId = sessionStorage.getItem("userId");
        this.repairModuleList.forEach(item => {
          if (item.addressList.length > 0) {
            repairModuleList.push(item);
          }
        });
        addRepair(
          this.removeUserIds,
          this.repairApplicant,
          this.repairApplicantId,
          repairModuleList,
          this.unitIdList,
          this.userIds
        )
          .then(res => {
            this.dialogVisible = false;
            this.getList();
            this.$message({
              message: "新增维修申请成功!",
              type: "success"
            });
            eventBus.$emit("updateBacklogTips");
          })
          .catch(err => {
            const msg = err.result ? err.result : "新增维修申请失败";
            this.$message({
              message: msg,
              type: "error"
            });
          });
        this.fileList = this.oldFileList;
      } else if (this.operateType == "edit") {
        let repairModuleList = [];
        this.repairModuleList.forEach(item => {
          repairModuleList.push({
            addressList: item.repairModule.addressList,
            deleteRepairImgIdList: item.deleteRepairImgIdList,
            repairModule: item.repairModule
          });
        });
        updateRepair(repairModuleList, this.repairApplyId, this.unitIdList)
          .then(res => {
            this.dialogVisible = false;
            this.getList();
            this.$message({
              message: "修改维修申请成功!",
              type: "success"
            });
          })
          .catch(err => {
            const msg = err.result ? err.result : "维修申请修改失败";
            this.$message({
              message: msg,
              type: "error"
            });
          });
      }
    },
    // 添加维修内容
    addRepaireContent(formName) {
      sessionStorage.removeItem("index");
      this.submitFlag = false;
      this.flag = false;
      if (this.operateType != "detail") {
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.uploadImgNum === 0
              ? this.saveRepaireContent()
              : this.submitUpload();
          } else {
            return false;
          }
        });
      }
    },
    // 重置维修内容
    resetRepaireContent() {
      eventBus.$emit("clearTreeSelectVal");
      this.clearForm(this.Form);
      this.fileList = [];
      this.checkedNodes = {};
      this.houseIdList = [];
      this.houseNameList = [];
      this.expandIdList = [];
      this.houseNumber = "";
      this.uploadSuccessImgNum = 0;
      this.uploadImgNum = 0;
      this.submitFlag = false;
    },
    // 修改维修内容
    editRepaireContent(formName) {
      if (this.operateType != "detail") {
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.uploadImgNum === 0
              ? this.saveRepaireContent()
              : this.submitUpload();
          } else {
            return false;
          }
        });
      }
    },
    // 保存添加的维修内容
    saveRepaireContent() {
      if (this.operateType == "add") {
        if (!this.editFlag) {
          if (!this.checkedNodes.type) {
            let obj = {};
            let Arr = [];
            this.repairModuleList[this.activeInfo] = {
              addressList: []
            }
            if (this.Form.addressList.length > 0) {
              this.Form.addressList.forEach(item => {
                this.repairModuleList[this.activeInfo]['addressList'].push(item);
              });
            }
            this.repairModuleList[this.activeInfo].addressList.forEach(
              (item, index) => {
                this.deleteRepairImgIdList.forEach((items, indexs) => {
                  if (item.physicalAddress == items.physics) {
                    if (!items.name) {
                      this.deleteRepairImgIdList.splice(indexs, 1);
                      this.repairModuleList[this.activeInfo].addressList.splice(
                        index,
                        1
                      );
                    } else {
                      Arr.push(items.name);
                    }
                  }
                });
              }
            );
            this.deleteRepairImgIdList = Arr;
            obj["addressList"] = JSON.parse(
              JSON.stringify(this.repairModuleList[this.activeInfo].addressList)
            );
            obj["deleteRepairImgIdList"] = JSON.parse(
              JSON.stringify(this.deleteRepairImgIdList)
            );
            this.repairePartOpts.forEach(item => {
              if (item.repairPartsId == this.Form.repairPartId) {
                this.Form["repairPartName"] = item.repairPartName;
              }
            });
            obj["repairModule"] = JSON.parse(JSON.stringify(this.Form));
            this.repairModuleList[this.activeInfo] = obj;
            if (this.submitFlag) {
              this.saveForm();
            }
          } else {
            let houseNodes = {};
            houseNodes = this.checkedNodes;
            if (houseNodes.type == 1) {
              this.Form.positionStr = houseNodes.address;
              this.Form.yardId = houseNodes.id;
            } else if (houseNodes.type == 2) {
              this.Form.positionStr = houseNodes.address;
              let arr = houseNodes["id"].split(":");
              this.Form.yardId = arr[0];
              this.Form.buildId = arr[1];
            } else if (houseNodes.type == 3) {
              this.Form.positionStr = houseNodes.address;
              let arr = houseNodes["id"].split(":");
              this.Form.yardId = arr[0];
              this.Form.buildId = arr[1];
              this.Form.floorName = arr[2];
            } else if (houseNodes.type == 4) {
              this.Form.positionStr = houseNodes.address;
              let arr = (houseNodes["_id"].split(":")[
                (this.Form.yardId,
                  this.Form.buildId,
                  this.Form.floorName,
                  this.Form.houseId)
              ] = arr);
            }
            let obj = {};
            obj["addressList"] = JSON.parse(
              JSON.stringify(this.Form.addressList)
            );
            obj["deleteRepairImgIdList"] = JSON.parse(
              JSON.stringify(this.deleteRepairImgIdList)
            );
            this.repairePartOpts.forEach(item => {
              if (item.repairPartsId == this.Form.repairPartId) {
                this.Form["repairPartName"] = item.repairPartName;
              }
            });
            obj["repairModule"] = JSON.parse(JSON.stringify(this.Form));
            if (this.dealFlag) {
              this.repairModuleList[this.indexs] = obj;
            } else {
              this.repairModuleList.push(obj);
            }
            if (this.submitFlag) {
              this.saveForm();
            }
            eventBus.$emit("clearTreeSelectVal");
            this.clearForm(this.Form);
            this.fileList = [];
            this.checkedNodes = {};
            this.houseIdList = [];
            this.houseNameList = [];
            this.expandIdList = [];
            this.houseNumber = "";
            this.uploadSuccessImgNum = 0;
            this.uploadImgNum = 0;
            this.uploadedImgNum = 0;
            this.submitFlag = false;
          }
        } else {
          // 没有修改树形结构
          if (!this.checkedNodes.type) {
            let obj = {};
            let Arr = [];
            // this.repairModuleList[this.activeInfo].addressList.push(...this.f)
            if (this.Form.addressList.length > 0) {
              this.Form.addressList.forEach(item => {
                this.repairModuleList[this.activeInfo].addressList.push(item);
              });
            }
            this.repairModuleList[this.activeInfo].addressList.forEach(
              (item, index) => {
                this.deleteRepairImgIdList.forEach((items, indexs) => {
                  if (item.physicalAddress == items.physics) {
                    if (!items.name) {
                      this.deleteRepairImgIdList.splice(indexs, 1);
                      this.repairModuleList[this.activeInfo].addressList.splice(
                        index,
                        1
                      );
                    } else {
                      Arr.push(items.name);
                    }
                  }
                });
              }
            );
            this.deleteRepairImgIdList = Arr;
            obj["addressList"] = JSON.parse(
              JSON.stringify(this.repairModuleList[this.activeInfo].addressList)
            );
            obj["deleteRepairImgIdList"] = JSON.parse(
              JSON.stringify(this.deleteRepairImgIdList)
            );
            this.repairePartOpts.forEach(item => {
              if (item.repairPartsId == this.Form.repairPartId) {
                this.Form["repairPartName"] = item.repairPartName;
              }
            });
            obj["repairModule"] = JSON.parse(JSON.stringify(this.Form));
            this.repairModuleList[this.activeInfo] = obj;
            if (this.submitFlag) {
              this.saveForm();
            }
          } else if (this.checkedNodes.type) {
            //修改了维修院落
            this.Form.positionStr = this.checkedNodes.address;
            let obj = {};
            let Arr = [];
            if (this.Form.addressList.length > 0) {
              this.Form.addressList.forEach(item => {
                this.repairModuleList[this.activeInfo].addressList.push(item);
              });
            }
            this.repairModuleList[this.activeInfo].addressList.forEach(
              (item, index) => {
                this.deleteRepairImgIdList.forEach((items, indexs) => {
                  if (item.physicalAddress == items.physics) {
                    if (!items.name) {
                      this.deleteRepairImgIdList.splice(indexs, 1);
                      this.repairModuleList[this.activeInfo].addressList.splice(
                        index,
                        1
                      );
                    } else {
                      Arr.push(items.name);
                    }
                  }
                });
              }
            );
            this.deleteRepairImgIdList = Arr;
            obj["addressList"] = JSON.parse(
              JSON.stringify(this.repairModuleList[this.activeInfo].addressList)
            );
            obj["deleteRepairImgIdList"] = JSON.parse(
              JSON.stringify(this.deleteRepairImgIdList)
            );
            this.repairePartOpts.forEach(item => {
              if (item.repairPartsId == this.Form.repairPartId) {
                this.Form["repairPartName"] = item.repairPartName;
              }
            });
            obj["repairModule"] = JSON.parse(JSON.stringify(this.Form));
            this.repairModuleList[this.activeInfo] = obj;
          }
          this.editFlag = false;
          eventBus.$emit("clearTreeSelectVal");
          this.clearForm(this.Form);
          this.fileList = [];
          this.checkedNodes = {};
          this.houseIdList = [];
          this.houseNameList = [];
          this.expandIdList = [];
          this.houseNumber = "";
          this.uploadSuccessImgNum = 0;
          this.uploadImgNum = 0;
          this.uploadedImgNum = 0;
          this.submitFlag = false;
        }
      } else {
        if (!this.checkedNodes.type) {
          //没有修改与院落位置
          let Obj = {
            deleteRepairImgIdList: []
          };
          Obj["addressImgList"] = this.repairModuleList[
            this.indexs
          ].addressImgList;
          if (this.Form.addressList.length == 0) {
            Obj["addressList"] = [];
          } else {
            this.Form.addressList.forEach(item => {
              Obj["addressImgList"].push(item);
            });
            // Obj["addressList"] = JSON.parse(
            //   JSON.stringify(this.Form.addressList)
            // );
          }
          this.deleteRepairImgIdList.forEach(item => {
            Obj["deleteRepairImgIdList"].push(item.name);
          });
          Obj["repairModule"] = JSON.parse(JSON.stringify(this.Form));
          this.repairModuleList[this.indexs] = Obj;
          if (this.submitFlag) {
            this.saveForm();
          }
        } else {
          //修改了坐落及楼栋
          if (this.checkedNodes.type) {
            let obj = this.checkedNodes;
            if (obj["type"] == 1) {
              this.Form.yardId = obj.id;
              this.Form.buildId = "";
              this.Form.floorName = "";
              this.Form.houseId = "";
              this.Form.positionStr = obj.address;
            } else if (obj["type"] == 2) {
              this.Form.positionStr = this.checkedNodes.address;
              this.Form.yardId = obj.pid;
              this.Form.buildId = obj.id.split(":")[1];
              this.Form.floorName = "";
              this.Form.houseId = "";
            } else if (obj["type"] == 3) {
              this.Form.positionStr = this.checkedNodes.address;
              this.Form.yardId = obj.id.split(":")[0];
              this.Form.buildId = obj.id.split(":")[1];
              this.Form.floorName = obj.id.split(":")[2];
              this.Form.houseId = "";
            } else {
              this.Form.positionStr = this.checkedNodes.address;
              this.Form.yardId = obj._id.split(":")[0];
              this.Form.buildId = obj._id.split(":")[1];
              this.Form.floorName = obj._id.split(":")[2];
              this.Form.houseId = obj._id.split(":")[3];
            }
          }
          let Obj = {};
          Obj["addressList"] = JSON.parse(
            JSON.stringify(this.Form.addressList)
          );
          Obj["deleteRepairImgIdList"] = this.deleteRepairImgIdList;
          // delete this.Form.addressList
          Obj["repairModule"] = JSON.parse(JSON.stringify(this.Form));
          // this.repairModuleList.splice(this.indexs, 1);
          // this.repairModuleList.push(Obj);
          this.repairModuleList[this.indexs] = Obj;
          // this.repairModuleList.replace(this.repairModuleList[this.indexs],Obj)
          if (this.submitFlag) {
            this.saveForm();
          }
          // eventBus.$emit("setTreeSelectVal", [], []);
          // this.clearForm(this.Form);
          // this.fileList = [];
          this.checkedNodes = {};
          this.uploadSuccessImgNum = 0;
          // this.houseIdList = [];
          // this.houseNameList = [];
          // this.expandIdList = [];
          // this.houseNumber = "";
          // this.uploadSuccessImgNum = 0;
          // this.uploadImgNum = 0;
          // this.submitFlag = false;
        }
      }
    },
    // 点击查看
    singleLook(row) {
      this.DialogVisible = true;
      this.title = "维修申请查看";
      selectProcessHistory(row.processInstanceId)
        .then(res => {
          this.applyProcess = res;
          let endFlag = false;
          for (let i = this.applyProcess.length - 1; i >= 0; i--) {
            if (this.applyProcess[i].stepName === "EndEvent") {
              endFlag = true;
            }
            if (
              this.applyProcess[i].stepName === "StartEvent" ||
              this.applyProcess[i].stepName === "ExclusiveGateway" ||
              this.applyProcess[i].stepName === "EndEvent"
            ) {
              this.applyProcess.splice(i, 1);
            }
          }
          if (endFlag) {
            this.applyStepActive = this.applyProcess.length;
          } else {
            this.applyStepActive = this.applyProcess.length - 1;
          }
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取维修申请流程失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取闲置用房树
    // getSpareRoomTreeList(id) {
    //   const _id = id ? id : null;
    //   getSpareRoomTree(_id)
    //     .then(res => {
    //       this.treeData = res;
    //     })
    //     .catch(err => {
    //       const msg = err.result ? err.result : "获取闲置用房树形菜单失败";
    //       this.$message({
    //         message: msg,
    //         type: "error"
    //       });
    //     });
    // },
    // 格式化时间
    formatDate(time) {
      var date = new Date(time);
      var fmt = "yyyy-MM-dd hh:mm:ss";
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
    },
    // 上传文件之前
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 200;
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 200MB!");
      }
      return isLt2M;
    },
    // 开始上传文件
    submitUpload() {
      this.$refs.upload.submit();
    },
    // 删除上传文件时钩子
    handleRemove(file, fileList) {
      if (file.status === "success") {
        this.deleteRepairImgIdList.push({
          name: file.name,
          physics: file.physics
        });
        this.uploadedImgNum--;
      }
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
      if (fileList.length == 0) {
        this.Form.picture = "";
      }
    },
    // 上传文件成功时钩子
    handleSuccess(response, file, fileList) {
      this.uploadSuccessImgNum++;
      let arr = file.name.split(".");
      if (arr[arr.length - 1] == "rar" || arr[arr.length - 1] == "zip") {
        this.Form.addressList.push({
          // 1图片 2文件 3压缩包
          type: 3,
          physicalAddress: response.imgList[0].imgPath,
          virtualAddress: response.imgList[0].imgUrl
        });
      } else {
        this.Form.addressList.push({
          // 1图片 2文件 3压缩包
          type: 1,
          physicalAddress: response.imgList[0].imgPath,
          virtualAddress: response.imgList[0].imgUrl
        });
      }
      if (this.uploadSuccessImgNum === this.uploadImgNum) {
        this.saveRepaireContent();
      }
    },
    // 上传文件改变时钩子
    handleChange(file, fileList) {
      const fileType = file.raw.type.split("/")[0];
      if (fileType !== "image") {
        file.url = `${BASE_URL}${url_name_1}/images/compress.png`;
      }
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
      if (fileList.length > 0) {
        this.Form.picture = "true";
      }
    },
    // 上传文件超出限制个数
    handleExceed(file, fileList) {
      this.$message.warning(`最多只能上传五张照片！`);
    },
    // 上传文件失败
    handleError(err, file, fileList) {
      if (err.status === 401) {
        this.$store.dispatch("Logout", this.token);
        this.$router.push({ path: "/login" });
      }
      const msg = err.result ? err.result : "上传照片失败";
      this.$message({
        message: err.message,
        type: "error"
      });
    },
    // dialog打开的回调
    dialogOpen() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
      });
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
      this.lookFlag = true;
      this.editFlag = false;
      this.dealFlag = false;
      this.activeInfo = 0;
      sessionStorage.removeItem("index");
      this.flag = false;
      this.clearForm(this.Form);
      this.fileList = [];
      this.oldFileList = [];
      this.detailFileList = [];
      this.uploadImgNum = 0;
      this.uploadedImgNum = 0;
      this.uploadSuccessImgNum = 0;
      this.submitFlag = false;
      this.treeShow = false;
      this.checkedNodes = {};
      this.houseIdList = [];
      this.houseNameList = [];
      this.expandIdList = [];
      this.houseNumber = "";
      this.unitIdList = [];
      this.downloadList = [];
      this.deleteRepairImgIdList = [];
      this.repairModuleList = [];
      this.repairApplyId = "";
      this.repairModuleId = "";
      if (this.operateType != "detail") {
        eventBus.$emit("clearTreeSelectVal");
      }
      this.applyProcess = [];
      this.active = null;
      this.applyStatus = "";
      this.indexs = 0;
      this.applyStepActive = 0;
    },
    Closed() {
      this.clearForm(this.Form);
      this.fileList = [];
      this.oldFileList = [];
      this.detailFileList = [];
      this.uploadImgNum = 0;
      this.uploadedImgNum = 0;
      this.uploadSuccessImgNum = 0;
      this.submitFlag = false;
      this.treeShow = false;
      this.checkedNodes = {};
      this.houseIdList = [];
      this.houseNameList = [];
      this.expandIdList = [];
      this.houseNumber = "";
      this.unitIdList = [];
      this.downloadList = [];
      this.deleteRepairImgIdList = [];
      this.repairModuleList = [];
      this.repairApplyId = "";
      this.repairModuleId = "";
      this.applyProcess = [];
      this.active = null;
      this.applyStatus = "";
      this.indexs = 0;
      this.applyStepActive = 0;
    },
    // 获取图片链接
    getImgUrl(url) {
      return `${BASE_URL}${url_name_1}${url}?access_token=${this.token}`;
    },
    // 预览
    preview(url) {
      $(".img-preview-bigger").css("display", "block");
      const img = $("#biggerImg");
      img.attr(
        "src",
        BASE_URL + url_name_1 + url + "?access_token=" + this.token
      );
    },
    // 关闭预览
    closePreview() {
      $(".img-preview-bigger").css("display", "none");
      const img = $("#biggerImg");
      img.css("width", "auto");
    },
    // 图片加载完成
    loadImage() {
      const window_width = $(window).width();
      const img = $("#biggerImg");
      const img_width = img.width();
      if (img_width >= window_width) {
        img.css("width", "80%");
      }
    },
    // 获取数据类型
    getDataType(obj) {
      var typeArr = ["String", "Object", "Number", "Array", "Function"];
      for (let name of typeArr) {
        if (Object.prototype.toString.call(obj) === `[object ${name}]`) {
          return name;
        }
      }
    },
    // // 选择树
    // handleCheckChange(checkedNodes) {
    //   this.checkedNodes = checkedNodes;
    // },
    // 加载树节点
    loadNode(node, resolve) {
      if (node.level === 0) {
        getYardTree(null, 0, null)
          .then(res => {
            res.forEach(item => {
              if (!item.expand || item.expand === undefined) {
                item.leaf = true;
              }
            });
            return resolve(res);
          })
          .catch(err => {
            this.$message({
              message: `树获取失败`,
              type: "error"
            });
          });
        setTimeout(() => {
          if (
            this.Form.yardId &&
            !this.Form.buildId &&
            !this.Form.floorName &&
            !this.Form.houseId
          ) {
            let tempObj = {};
            tempObj.level = 1;
            tempObj.id_yard = this.Form.yardId;
            eventBus.$emit("setTreeDefaultVal", tempObj);
          } else if (
            this.Form.yardId &&
            this.Form.buildId &&
            !this.Form.floorName &&
            !this.Form.houseId
          ) {
            let tempObj = {};
            tempObj.level = 2;
            tempObj.id_yard = this.Form.yardId;
            tempObj.id_build = `${this.Form.yardId}:${this.Form.buildId}`;
            eventBus.$emit("setTreeDefaultVal", tempObj);
          } else if (
            this.Form.yardId &&
            this.Form.buildId &&
            this.Form.floorName &&
            !this.Form.houseId
          ) {
            let tempObj = {};
            tempObj.level = 3;
            tempObj.id_yard = this.Form.yardId;
            tempObj.id_build = `${this.Form.yardId}:${this.Form.buildId}`;
            tempObj.buildId = this.Form.buildId;
            tempObj.id_floor = `${this.Form.yardId}:${this.Form.buildId}:${this.Form.floorName}`;
            eventBus.$emit("setTreeDefaultVal", tempObj);
          } else if (
            this.Form.yardId &&
            this.Form.buildId &&
            this.Form.floorName &&
            this.Form.houseId
          ) {
            let tempObj = {};
            tempObj.level = 4;
            tempObj.id_yard = this.Form.yardId;
            tempObj.id_build = `${this.Form.yardId}:${this.Form.buildId}`;
            tempObj.id_floor = `${this.Form.yardId}:${this.Form.buildId}:${this.Form.floorName}`;
            tempObj.floorName = this.Form.floorName;
            tempObj.id_house = this.Form.houseId;
            eventBus.$emit("setTreeDefaultVal", tempObj);
          }
        }, 1000);
      } else if (node.level === 3) {
        const repairApply_buildId =
          sessionStorage.getItem("repairApply_buildId") - 0;
        getYardTree(node.data.value, 3, null, repairApply_buildId)
          .then(res => {
            res.forEach(item => {
              if (!item.expand || item.expand === undefined) {
                item.leaf = true;
              }
            });
            return resolve(res);
          })
          .catch(err => {
            this.$message({
              message: `树获取失败`,
              type: "error"
            });
          });
      } else {
        getYardTree(node.data.value, node.data.type, null)
          .then(res => {
            res.forEach(item => {
              if (!item.expand || item.expand === undefined) {
                item.leaf = true;
              }
            });
            return resolve(res);
          })
          .catch(err => {
            this.$message({
              message: `树获取失败`,
              type: "error"
            });
          });
      }
    },
    // 选中树节点
    handleCheckChange(data) {
      if (data.type) {
        this.checkedNodes = data;
        this.Form.RepaireYard = "true";
        this.currentLevel = data.type;
        if (data.type === 1) {
          this.Form.yardId = data.value;
        } else if (data.type === 2) {
          this.Form.buildId = data.value;
        } else if (data.type === 3) {
          this.Form.floorName = data.value;
        } else {
          this.Form.houseId = data.value;
        }
      } else {
        this.Form.RepaireYard = "";
      }
    },
    // 展开树节点
    handleNodeExpand(data, node) {
      if (node.level === 1) {
        this.Form.yardId = data.value;
      } else if (node.level === 2) {
        this.Form.buildId = data.value;
        sessionStorage.setItem("repairApply_buildId", data.value);
      } else if (node.level === 3) {
        this.Form.floorName = data.value;
      }
    },
    // 图片多选框选择
    imgCheckboxChange($event, id) {
      if ($event) {
        if (this.downloadList.indexOf(id) === -1) {
          this.downloadList.push(id);
        }
      } else {
        const index = this.downloadList.indexOf(id);
        this.downloadList.splice(index, 1);
      }
    },
    // 下载图片
    downloadImg() {
      if (this.downloadList.length > 0) {
        download(this.downloadList, 20, this.repairApplyId)
          .then(res => {
            let fileDownload = require("js-file-download");
            let name = decodeURIComponent(
              sessionStorage.getItem("CADdownloadFileName")
            );
            fileDownload(res, name);
          })
          .catch(err => {
            const msg = err.result ? err.result : "证件下载失败";
            this.$message({
              message: msg,
              type: "error"
            });
          });
      } else {
        this.$message({
          message: "请先选中一个证件",
          type: "info"
        });
      }
    },
    showInfoDetail(index) {
      this.houseNameList = [];
      this.houseIdList = [];
      this.expandIdList = [];
      this.detailFileList = [];
      this.downloadList = [];
      this.deleteRepairImgIdList = [];
      this.indexs = index;
      if (this.operateType == "detail") {
        sessionStorage.setItem("index", index);
        this.activeInfo = index;
        this.Form = this.repairModuleList[index].repairModule;
        this.Form.repairPartId = this.repairModuleList[
          index
        ].repairModule.repairPartName;
        this.Form.RepairYard = this.repairModuleList[
          index
        ].repairModule.positionStr;
        this.repairModuleList[index].addressList.forEach((item, index) => {
          const ext = item.virtualAddress.split(".")[1];
          if (ext === "rar" || ext === "zip") {
            item.virtualAddress = "/images/compress.png";
          }
          this.detailFileList.push(item);
        });
      } else if (this.operateType == "edit") {
        this.uploadImgNum = 0;
        this.editFlag = true;
        this.activeInfo = index;
        this.fileList = [];
        this.oldFileList = [];
        this.Form = this.repairModuleList[index].repairModule;
        this.Form.addressList = [];
        if (
          this.repairModuleList[index].addressImgList &&
          this.repairModuleList[index].addressImgList.length > 0
        ) {
          let imgNum = 0;
          this.repairModuleList[index].addressImgList.forEach(item => {
            imgNum++;
            const ext = item.virtualAddress.split(".")[1];
            if (ext === "rar" || ext === "zip") {
              item.virtualAddress = "/images/compress.png";
            }
            const obj = {
              name: item.repairImgId,
              url:
                BASE_URL +
                url_name_1 +
                item.virtualAddress +
                "?access_token=" +
                this.token,
              physics: item.physicalAddress
            };
            this.fileList.push(obj);
            this.oldFileList.push(obj);
          });
          this.uploadedImgNum = imgNum;
          this.uploadSuccessImgNum = 0;
        }
        if (this.uploadedImgNum > 0) {
          this.Form.picture = "true";
        }
        this.houseNameList = [];
        let obj = {};
        if (this.repairModuleList[index].repairModule.houseId) {
          this.Form.yardId = this.repairModuleList[index].repairModule.yardId;
          this.Form.buildId = this.repairModuleList[index].repairModule.buildId;
          this.Form.floorName = this.repairModuleList[
            index
          ].repairModule.floorName;
          this.Form.houseId = this.repairModuleList[index].repairModule.houseId;
          this.Form.positionStr = this.repairModuleList[
            index
          ].repairModule.positionStr;
          let arr = this.Form.positionStr.split("-");
          let houseNumber = arr[arr.length - 1];
          this.houseNameList.push(houseNumber);
          this.houseIdList.push(
            this.repairModuleList[index].repairModule.houseId
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.yardId,
            this.repairModuleList[index].repairModule.buildId,
            `${this.repairModuleList[index].repairModule.buildId}:${this.repairModuleList[index].repairModule.floorName}`
          );
          eventBus.$emit(
            "setTreeSelectVal",
            this.houseNameList,
            this.houseIdList,
            this.expandIdList
          );
        } else if (this.repairModuleList[index].repairModule.floorName) {
          this.Form.yardId = this.repairModuleList[index].repairModule.yardId;
          this.Form.buildId = this.repairModuleList[index].repairModule.buildId;
          this.Form.floorName = this.repairModuleList[
            index
          ].repairModule.floorName;
          this.houseNameList.push(
            `${this.repairModuleList[index].repairModule.floorName}层`
          );
          this.houseIdList.push(
            `${this.repairModuleList[index].repairModule.yardId}:${this.repairModuleList[index].repairModule.buildId}:${this.repairModuleList[index].repairModule.floorName}`
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.yardId
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.buildId
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.floorName
          );
          eventBus.$emit(
            "setTreeSelectVal",
            this.houseNameList,
            this.houseIdList,
            this.expandIdList
          );
        } else if (this.repairModuleList[index].repairModule.buildId) {
          this.Form.yardId = this.repairModuleList[index].repairModule.yardId;
          this.Form.buildId = this.repairModuleList[index].repairModule.buildId;
          this.Form.positionStr = this.repairModuleList[
            index
          ].repairModule.positionStr;
          let arr = this.Form.positionStr.split("-");
          let houseNumber = arr[arr.length - 1];
          this.houseNameList.push(houseNumber);
          this.houseIdList.push(
            `${this.repairModuleList[index].repairModule.yardId}:${this.repairModuleList[index].repairModule.buildId}`
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.yardId
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.buildId
          );
          eventBus.$emit(
            "setTreeSelectVal",
            this.houseNameList,
            this.houseIdList,
            this.expandIdList
          );
        } else if (this.repairModuleList[index].repairModule.yardId) {
          this.Form.yardId = this.repairModuleList[index].repairModule.yardId;
          this.Form.positionStr = this.repairModuleList[
            index
          ].repairModule.positionStr;
          this.houseNameList.push(this.Form.positionStr);
          this.houseIdList.push(
            `${this.repairModuleList[index].repairModule.yardId}`
          );
          eventBus.$emit(
            "setTreeSelectVal",
            this.houseNameList,
            this.houseIdList,
            this.expandIdList
          );
        }
        if (this.houseNameList.length > 0) {
          this.Form.RepaireYard = "true";
        }
      } else {
        this.uploadImgNum = 0;
        this.uploadSuccessImgNum = 0;
        this.editFlag = true;
        this.activeInfo = index;
        this.fileList = [];
        this.oldFileList = [];
        this.Form = this.repairModuleList[index].repairModule;
        this.Form.addressList = [];
        if (
          this.repairModuleList[index].addressList &&
          this.repairModuleList[index].addressList.length > 0
        ) {
          let imgNum = 0;
          this.repairModuleList[index].addressList.forEach(item => {
            imgNum++;
            const ext = item.virtualAddress.split(".")[1];
            if (ext === "rar" || ext === "zip") {
              item.virtualAddress = "/images/compress.png";
            }
            const obj = {
              name: item.repairImgId,
              url:
                BASE_URL +
                url_name_1 +
                item.virtualAddress +
                "?access_token=" +
                this.token,
              physics: item.physicalAddress
            };
            this.fileList.push(obj);
            this.oldFileList.push(obj);
          });
          this.uploadedImgNum = imgNum;
        }
        if (this.uploadedImgNum > 0) {
          this.Form.picture = "true";
        }
        this.houseNameList = [];
        let obj = {};
        if (this.repairModuleList[index].repairModule.houseId) {
          this.Form.yardId = this.repairModuleList[index].repairModule.yardId;
          this.Form.buildId = this.repairModuleList[index].repairModule.buildId;
          this.Form.floorName = this.repairModuleList[
            index
          ].repairModule.floorName;
          this.Form.houseId = this.repairModuleList[index].repairModule.houseId;
          this.Form.positionStr = this.repairModuleList[
            index
          ].repairModule.positionStr;
          let arr = this.Form.positionStr.split("-");
          let houseNumber = arr[arr.length - 1];
          this.houseNameList.push(houseNumber);
          this.houseIdList.push(
            this.repairModuleList[index].repairModule.houseId
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.yardId,
            this.repairModuleList[index].repairModule.buildId,
            `${this.repairModuleList[index].repairModule.buildId}:${this.repairModuleList[index].repairModule.floorName}`
          );
          eventBus.$emit(
            "setTreeSelectVal",
            this.houseNameList,
            this.houseIdList,
            this.expandIdList
          );
        } else if (this.repairModuleList[index].repairModule.floorName) {
          this.Form.yardId = this.repairModuleList[index].repairModule.yardId;
          this.Form.buildId = this.repairModuleList[index].repairModule.buildId;
          this.Form.floorName = this.repairModuleList[
            index
          ].repairModule.floorName;
          this.houseNameList.push(
            `${this.repairModuleList[index].repairModule.floorName}层`
          );
          this.houseIdList.push(
            `${this.repairModuleList[index].repairModule.yardId}:${this.repairModuleList[index].repairModule.buildId}:${this.repairModuleList[index].repairModule.floorName}`
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.yardId
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.buildId
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.floorName
          );
          eventBus.$emit(
            "setTreeSelectVal",
            this.houseNameList,
            this.houseIdList,
            this.expandIdList
          );
        } else if (this.repairModuleList[index].repairModule.buildId) {
          this.Form.yardId = this.repairModuleList[index].repairModule.yardId;
          this.Form.buildId = this.repairModuleList[index].repairModule.buildId;
          this.Form.positionStr = this.repairModuleList[
            index
          ].repairModule.positionStr;
          let arr = this.Form.positionStr.split("-");
          let houseNumber = arr[arr.length - 1];
          this.houseNameList.push(houseNumber);
          this.houseIdList.push(
            `${this.repairModuleList[index].repairModule.yardId}:${this.repairModuleList[index].repairModule.buildId}`
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.yardId
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.buildId
          );
          eventBus.$emit(
            "setTreeSelectVal",
            this.houseNameList,
            this.houseIdList,
            this.expandIdList
          );
        } else if (this.repairModuleList[index].repairModule.yardId) {
          this.Form.yardId = this.repairModuleList[index].repairModule.yardId;
          this.Form.positionStr = this.repairModuleList[
            index
          ].repairModule.positionStr;
          this.houseNameList.push(this.Form.positionStr);
          this.houseIdList.push(
            `${this.repairModuleList[index].repairModule.yardId}`
          );
          eventBus.$emit(
            "setTreeSelectVal",
            this.houseNameList,
            this.houseIdList,
            this.expandIdList
          );
        }
        if (this.houseNameList.length > 0) {
          this.Form.RepaireYard = "true";
        }
      }
    },
    // 更多下拉框点击效果
    handleCommand(index) {
      this.checkedNodes = {};
      this.indexs = index;
      if (this.operateType == "detail") {
        this.detailFileList = [];
        this.Form.RepairYard = this.repairModuleList[
          index
        ].repairModule.positionStr;
        this.Form.repairModuleTarget = this.repairModuleList[
          index
        ].repairModule.repairModuleTarget;
        this.Form.repairPartId = this.repairModuleList[
          index
        ].repairModule.repairPartName;
        this.Form.repairModulePosition = this.repairModuleList[
          index
        ].repairModule.repairModulePosition;
        this.Form.repairModuleArea = this.repairModuleList[
          index
        ].repairModule.repairModuleArea;
        if (this.repairModuleList[index].addressList.length > 0) {
          this.repairModuleList[index].addressList.forEach((item, index) => {
            const ext = item.virtualAddress.split(".")[1];
            if (ext === "rar" || ext === "zip") {
              item.virtualAddress = "/images/compress.png";
            }
            this.detailFileList.push(item);
          });
        }
      } else {
        if (!this.flag) {
          this.addRepaireContent("Form");
        }
        setTimeout(() => {
          this.flag = true;
        }, 200);
        this.detailFileList = [];
        this.Form.repairPartId = this.repairModuleList[
          index
        ].repairModule.repairPartId;
        this.Form.repairModuleTarget = this.repairModuleList[
          index
        ].repairModule.repairModuleTarget;

        this.Form.repairModulePosition = this.repairModuleList[
          index
        ].repairModule.repairModulePosition;
        this.Form.repairModuleArea = this.repairModuleList[
          index
        ].repairModule.repairModuleArea;
        this.fileList = [];
        this.oldFileList = [];
        if (
          this.repairModuleList[index].addressList &&
          this.repairModuleList[index].addressList.length > 0
        ) {
          let imgNum = 0;
          this.repairModuleList[index].addressList.forEach(item => {
            imgNum++;
            const ext = item.virtualAddress.split(".")[1];
            if (ext === "rar" || ext === "zip") {
              item.virtualAddress = "/images/compress.png";
            }
            const obj = {
              name: item.repairImgId,
              url:
                BASE_URL +
                url_name_1 +
                item.virtualAddress +
                "?access_token=" +
                this.token
            };
            this.fileList.push(obj);
            this.oldFileList.push(obj);
          });
          this.uploadedImgNum = imgNum;
          this.uploadedImgNum = this.repairModuleList[index].addressList.length;
        }
        this.houseNameList = [];
        if (this.repairModuleList[index].repairModule.houseId) {
          this.Form.yardId = this.repairModuleList[index].repairModule.yardId;
          this.Form.buildId = this.repairModuleList[index].repairModule.buildId;
          this.Form.floorName = this.repairModuleList[
            index
          ].repairModule.floorName;
          this.Form.houseId = this.repairModuleList[index].repairModule.houseId;
          this.houseNameList.push(
            this.repairModuleList[index].repairModule.houseNumber
          );
          this.houseIdList.push(
            `${this.repairModuleList[index].repairModule.yardId}:${this.repairModuleList[index].repairModule.buildId}:${this.repairModuleList[index].repairModule.floorName}:${this.repairModuleList[index].repairModule.houseId}`
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.yardId,
            this.repairModuleList[index].repairModule.buildId,
            `${this.repairModuleList[index].repairModule.buildId}:${this.repairModuleList[index].repairModule.floorName}`
          );
          eventBus.$emit(
            "setTreeSelectVal",
            this.houseNameList,
            this.houseIdList,
            this.expandIdList
          );
        } else if (this.repairModuleList[index].repairModule.floorName) {
          this.Form.buildId = this.repairModuleList[index].repairModule.buildId;
          this.Form.floorName = this.repairModuleList[
            index
          ].repairModule.floorName;
          this.houseNameList.push(
            this.repairModuleList[index].repairModule.repairModule.floorName
          );
          this.houseIdList.push(
            `${this.repairModuleList[index].repairModule.buildId}:${this.repairModuleList[index].repairModule.floorName}`
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.buildId
          );
          eventBus.$emit(
            "setTreeSelectVal",
            this.houseNameList,
            this.houseIdList,
            this.expandIdList
          );
        } else if (this.repairModuleList[index].repairModule.buildId) {
          this.Form.buildId = this.repairModuleList[index].repairModule.buildId;
          this.houseNameList.push(
            this.repairModuleList[index].repairModule.buildName
          );
          this.houseIdList.push(
            `${this.repairModuleList[index].repairModule.buildId}`
          );
          this.expandIdList.push(
            this.repairModuleList[index].repairModule.buildId
          );
          eventBus.$emit(
            "setTreeSelectVal",
            this.houseNameList,
            this.houseIdList,
            this.expandIdList
          );
        } else if (this.repairModuleList[index].repairModule.yardId) {
          this.Form.yardId = this.repairModuleList[index].repairModule.yardId;
          this.houseNameList.push(
            this.repairModuleList[index].repairModule.yardName
          );
          this.houseIdList.push(
            `${this.repairModuleList[index].repairModule.yardId}`
          );
          eventBus.$emit(
            "setTreeSelectVal",
            this.houseNameList,
            this.houseIdList
          );
        }
      }
    },
    // 只能输入正浮点数
    limitFloat(key, flag) {
      this.Form[key] = this.Form[key]
        .replace(/[^\d.]/g, "")
        .replace(/\.{2,}/g, ".")
        .replace(".", "$#$")
        .replace(/\./g, "")
        .replace("$#$", ".")
        .replace(/^(\-)*(\d+)\.(\d\d).*$/, "$1$2.$3")
        .replace(/^\./g, "");
      if (flag && this.Form[key] !== "") {
        this.Form[key] = (this.Form[key] - 0).toFixed(2);
      }
    },
    // 只能输入正整数
    limitInt(key) {
      this.Form[key] = this.Form[key].replace(/[^0-9]/g, "");
    }
  },
  mounted() {
    this.getList();
    if (this.$store.state.repaire.repaireArray.length) {
      this.dialogVisible = true;
      this.title = "新增维修信息";
      this.treeShow = true;
      this.getUnit();
      this.getRepairPart();
      let repairModuleList = this.$store.state.repaire.repaireArray;
      repairModuleList.forEach(item => {
        let obj = {};
        obj["repairModule"] = {};
        obj["addressList"] = [];
        obj["deleteRepairImgIdList"] = [];
        obj.repairModule["houseId"] = item.houseId ? item.houseId : "";
        obj.repairModule["floorName"] = item.floorName ? item.floorName : "";
        obj.repairModule["buildId"] = item.buildId ? item.buildId : "";
        obj.repairModule["yardId"] = item.yardId ? item.yardId : "";
        obj.repairModule["positionStr"] = item.position;
        obj.repairModule["repairPartId"] = item.repairPart;
        let arr = item.position.split("-");
        if (arr.length == 5) {
          obj.repairModule["yardName"] = arr[0];
          obj.repairModule["buildName"] = arr[1];
          obj.repairModule["houseNumber"] = arr[4];
        } else if (arr.length == 4) {
          if (arr.indexOf("") != -1) {
            obj.repairModule["yardName"] = arr[0];
            obj.repairModule["buildName"] = arr[1];
          } else {
            obj.repairModule["yardName"] = arr[0];
            obj.repairModule["buildName"] = arr[1];
            obj.repairModule["houseNumber"] = arr[3];
          }
        } else if (arr.length == 3) {
          obj.repairModule["yardName"] = arr[0];
          obj.repairModule["buildName"] = arr[1];
        } else if (arr.length == 2) {
          obj.repairModule["yardName"] = arr[0];
          obj.repairModule["buildName"] = arr[1];
        } else {
          obj.repairModule["yardName"] = arr[0];
        }
        this.repairModuleList.push(obj);
      });
      this.dealFlag = true;
      this.$nextTick(() => {
        this.showForm(this.indexs);
      });
      this.$store.commit("getRepaireList", []);
    }
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - queryBoxH - operateBoxH;
    }, 1);
    this.token = sessionStorage.getItem("TOKEN");
    if (this.token) {
      this.postHeaders.Authorization = `Bearer ${this.token}`;
    }
  }
};
</script>
<style>
#repairApply .query-box {
  margin-bottom: 20px;
}
#repairApply .el-dialog__body .query-box {
  padding-left: 30px;
}
#repairApply #queryBox .dialog .query-item {
  width: 60%;
  margin: 10px 0;
}
#repairApply #queryBox .dialog .query-item .el-select {
  width: 35%;
  margin-left: 15px;
}
#repairApply #queryBox .query-item {
  font-size: 14px;
  line-height: 32px;
  float: left;
  width: 18%;
}
#repairApply .el-form.el-form--inline {
  padding-left: 20px;
}
#repairApply .el-form .el-form-item {
  width: 100%;
  margin-bottom: 15px;
}
#repairApply .el-form .el-form-item__content .selectInput-box .el-input__inner {
  height: 32px !important ;
}
#repairApply .el-input--suffix .el-input__inner {
  height: 32px;
  line-height: 32px;
}
#repairApply .query-item > div {
  float: left;
}
#repairApply .query-item > .query-input {
  width: calc(100% - 80px);
}
#repairApply .query-item .el-select {
  line-height: 32px;
  width: 100%;
}
#repairApply .query-item .el-date-editor.el-input,
#repairApply .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#repairApply .query-left {
  float: left;
  line-height: 33px;
  font-size: 16px;
  margin-bottom: -1px;
  border-bottom: 1px solid #4490f1;
}
#repairApply .query-right {
  float: right;
  font-size: 16px;
}
#repairApply .query-right span {
  cursor: pointer;
  display: inline-block;
  line-height: 32px;
  padding: 0 10px;
  color: #3891e0;
  font-size: 16px;
}
#repairApply .query-btn {
  float: right;
}
#repairApply .el-dialog--center .el-dialog__body {
  max-height: 500px;
  overflow-y: auto;
}
#repairApply .el-textarea__inner {
  width: 868px;
  height: 48px;
}
#repairApply .el-textarea {
  vertical-align: middle !important;
}
#repairApply .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#repairApply .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#repairApply .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#repairApply .upload_box {
  padding-top: 10px;
}
#repairApply .upload_box .el-upload--picture-card {
  width: 100px;
  height: 100px;
  line-height: 100px;
}
#repairApply .upload_box .el-upload-list--picture-card .el-upload-list__item {
  width: 100px;
  height: 100px;
}
#repairApply .el-upload--picture-card {
  margin-bottom: 8px;
}
#repairApply .el-upload-list > li {
  margin-bottom: 0;
}
#repairApply .detail-form-item .el-input__inner {
  border: 0;
  padding-left: 0;
  width: 350px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
#repairApply .detail-form-item .el-textarea__inner {
  border: 0;
  padding-left: 0;
  width: 550px;
}
#repairApply .img-preview-item {
  float: left;
  margin-right: 20px;
  margin-bottom: 15px;
  width: 100px;
  height: 100px;
  border: 1px dashed #c0ccda;
  position: relative;
}
#repairApply .img-preview-item > img {
  width: 100%;
  height: 100%;
}
#repairApply .img-preview-item > .bigger-view-warpper {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}
#repairApply
  .img-preview-item
  > .bigger-view-warpper
  > .bigger-view-warpper-inner {
  width: 100%;
  height: 100%;
  background: #000;
  opacity: 0.5;
  display: none;
}
#repairApply
  .img-preview-item
  > .bigger-view-warpper:hover
  > .bigger-view-warpper-inner {
  display: block;
}
#repairApply .img-preview-item > .bigger-view-warpper > i {
  position: absolute;
  font-size: 26px;
  color: #ffffff;
  cursor: pointer;
  display: none;
  top: 50%;
  left: 50%;
  margin-left: -13px;
  margin-top: -13px;
}
#repairApply .img-preview-item > .bigger-view-warpper > i:hover {
  color: #3891e0;
}
#repairApply .img-preview-item > .bigger-view-warpper:hover > i {
  display: block;
}
#repairApply .img-preview-item > .el-checkbox {
  position: absolute;
  bottom: -13px;
  right: 0;
}
#repairApply .download-btn {
  padding: 7px 22px;
}
#repairApply .img-preview-bigger {
  width: 100%;
  height: 100%;
  position: fixed;
  z-index: 100000;
  top: 0;
  left: 0;
  display: none;
}
#repairApply .img-preview-bigger > .img-preview-bigger-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
}
#repairApply .img-preview-bigger > img {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
}
#repairApply .img-preview-bigger > i {
  position: absolute;
  font-size: 50px;
  top: 30px;
  right: 30px;
  color: #ffffff;
  cursor: pointer;
}
#repairApply .img-preview-bigger > i:hover {
  color: #3891e0;
}
#repairApply .table-box .notAudit {
  color: #ff9e00;
}
#repairApply .table-box .passAudit {
  color: #3891e0;
}
#repairApply .table-box .failedAudit {
  color: #f9463f;
}
#repairApply .table-box .Auditing {
  color: #1aa97b;
}
#repairApply .detail .query-item {
  margin: 10px 50px;
}
#repairApply .singleBtn.pl {
  padding-left: 25px;
}
#repairApply .el-step__title {
  line-height: 20px;
  font-size: 14px;
}
#repairApply .el-steps--horizontal {
  width: 5000px;
  margin-bottom: 30px;
}
#repairApply .el-progress-bar__innerText {
  margin-top: -5px;
  font-weight: 700;
  color: #7ddbff;
}
#repairApply #selectTree_select_tree .selectInput-box .el-input__inner {
  height: 32px !important;
}
#repairApply .hidden {
  display: none;
}
#repairApply .el-step__description {
  font-size: 14px;
}
#repairApply .operate_btn {
  padding-left: 134px;
}
#repairApply .operate_btn span {
  display: inline-block;
  color: #3891e0;
  padding: 0 4px;
  cursor: pointer;
}
.el-popper.More-info {
  padding: 0 !important;
}
.el-popper.More-info .repair-info-more li:nth-of-type(1) {
  line-height: 30px;
}
.el-popper.More-info .repair-info-more li {
  line-height: 30px;
  padding: 0 10px;
  font-size: 13px;
}
.el-popper.More-info .repair-info-more .info-active {
  background-color: #498fea;
  color: #fff;
}
.el-popper[x-placement^="bottom"] {
  margin-top: 0;
}
</style>
