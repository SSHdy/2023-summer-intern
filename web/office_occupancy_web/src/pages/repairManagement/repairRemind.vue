<template>
  <div id="serviceRemind">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">使用单位：</div>
          <div class="query-input">
            <el-select v-model="unitIdList" placeholder="请选择使用单位">
              <el-option
                v-for="item in unitOptions"
                :key="item.unitId"
                :label="item.unitName"
                :value="item.unitId"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="query-item">
          <div class="query-title">具体方位：</div>
          <div class="query-input">
            <Tree-select
              @loadNodes="loadNodes"
              @handleCheckChanges="handleCheckChanges"
              @handleNodeExpands="handleNodeExpands"
              :treeShow="treeShows"
              :inputHeight="'34px'"
              style="font-size:14px"
            ></Tree-select>
          </div>
        </div>
        <div class="query-item">
          <div class="query-title">提醒状态：</div>
          <div class="query-input">
            <el-select v-model="remaid" placeholder="请选择提醒状态">
              <el-option
                v-for="item in remaidOpts"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
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
        <el-table-column label="具体方位" align="center" prop="position" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column
          prop="responsiblePerson"
          label="负责人"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="repairParts"
          label="维修部位"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="contactPhone"
          label="联系方式"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="lastRepairTime"
          label="上次维修时间"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column label="提醒时间" align="center">
          <template slot-scope="scope">
            <div :class="{'warning': scope.row.remindState == 1}">
              <span>{{scope.row.remaindTime}}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowAlter="false"
              :isShowRemind="true"
              @remindClick="remindClick(scope.row)"
              @deleteClick="singleDetle(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group
            @batchDeleteClick="batchDelete"
            @addClick="add"
            :dealBtnShow="true"
            @dealClick="deal"
          ></table-btn-group>
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
      class="add"
      :visible.sync="dialogVisibles.add"
      :close-on-click-modal="false"
      width="1100px"
      @open="Open"
      @closed="Closed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>新增</span>
      </div>
      <div class="repair-info-box">
        <div class="repair-info-base clearfix">
          <div class="query-box" id="queryBox">
            <div class="clearfix">
              <div class="query-item">
                <div class="query-title" style="margin-right:7px">使用单位</div>
                <div class="query-input">
                  <el-select v-model="userUnit" multiple placeholder="请选择使用单位">
                    <el-option
                      v-for="item in unitOptions"
                      :key="item.unitId"
                      :label="item.unitName"
                      :value="item.unitId"
                    ></el-option>
                  </el-select>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-form :inline="true" :model="Form" ref="Form" :rules="rules" style="text-align:left">
          <el-form-item label="具体方位" label-width="100px" prop="repairAddress">
            <tree-select
              @loadNode="loadNode"
              @handleCheckChange="handleCheckChange"
              @handleNodeExpand="handleNodeExpand"
              :treeShow="treeShow"
              :inputWidth="'200px'"
              :inputHeight="'34px'"
            ></tree-select>
          </el-form-item>
          <el-form-item label="维修部位" label-width="100px" prop="repairPart">
            <el-select
              v-model="Form.repairPart"
              placeholder="请选择维修部位"
              style="width:200px"
              size="small"
            >
              <el-option
                v-for="item in repairPartsOpts"
                :key="item.repairPartsId"
                :label="item.repairPartName"
                :value="item.repairPartsId"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="提醒时间" label-width="100px" prop="remaindTime">
            <el-date-picker
              v-model="Form.remaindTime"
              type="date"
              placeholder="选择提醒时间"
              size="small"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width:200px;"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="负责人" label-width="100px" prop="responsiblePerson">
            <el-input placeholder="请输入负责人" v-model="Form.responsiblePerson" size="small"></el-input>
          </el-form-item>
          <el-form-item label="联系电话" label-width="100px" prop="contactPhone">
            <el-input
              placeholder="请输入联系电话"
              @keyup.native="limitInt('contactPhone')"
              @blur="limitInt('contactPhone')"
              v-model="Form.contactPhone"
              size="small"
            ></el-input>
          </el-form-item>
          <el-form-item label="维修内容" label-width="100px" prop="repairGoods">
            <el-input placeholder="请输入维修内容" v-model="Form.repairGoods" size="small"></el-input>
          </el-form-item>
          <el-form-item label="备注" label-width="100px" prop="remaindRemark" class="remaindRemark">
            <el-input type="textarea" placeholder="请输入备注" v-model="Form.remaindRemark" size="small"></el-input>
          </el-form-item>
        </el-form>
        <cancel-btn @clickBtn="dialogVisibles.add = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitIgnoreForm('Form')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <el-dialog
      :visible.sync="dialogVisibles.remind"
      :close-on-click-modal="false"
      width="1100px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>维修提醒</span>
      </div>
      <div class="repair-info-box">
        <div class="repair-info-base clearfix">
          <div>
            <span>具体方位：</span>
            <span :title="baseInfo.position">{{baseInfo.position}}</span>
          </div>
          <div style="padding-right:33px">
            <span>使用单位：</span>
            <span :title="baseInfo.unitNameList">{{baseInfo.unitNameList}}</span>
          </div>
          <div>
            <span style="display:inline-block;width:84px;text-align:right">负责人：</span>
            <span>{{baseInfo.responsiblePerson}}</span>
          </div>
          <div>
            <span>联系电话：</span>
            <span>{{baseInfo.contactPhone}}</span>
          </div>
          <div style="padding-right:33px">
            <span>维修部位：</span>
            <span>{{baseInfo.repairPartName}}</span>
          </div>
          <div>
            <span>是否已处理：</span>
            <span>{{baseInfo.remaindHandle}}</span>
          </div>
          <div>
            <span>维修内容：</span>
            <span>{{baseInfo.repairGoods}}</span>
          </div>
          <div style="width:900px;">
            <span>备注：</span>
            <span>{{baseInfo.remaindRemark}}</span>
          </div>
        </div>
        <div class="repair-info-detail" style="margin-top:20px">
          <div class="info-detail-title clearfix">
            <span>维修记录：</span>
            <div class="info-detail-btn" v-if="repairInfoMore.length > 0">
               <el-popover
              class="detail"
              placement="bottom-end"
              width="300"
              trigger="click"
              popper-class="more-infos"
              :visible-arrow="false"
            >
              <ul class="repairRemaind-info-more">
                <li
                  v-for="(item, index) in repairInfoMore"
                  :key="index"
                  :class="{'info-active': activeInfo === index}"
                  @click="handleCommand(index)"
                >{{item.positionStr}}</li>
              </ul>
              <span class="more-btn" slot="reference" v-show="repairInfoMore.length > 0">
                更多
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-popover>
            </div>
          </div>
          <div class="section2-content" v-if="state">
            <div>
              <span>地址：</span>
              <span>{{repairPositionTarget}}</span>
            </div>
            <div>
              <span>维修部位：</span>
              <span>{{repairePartName}}</span>
            </div>
            <div>
              <span>维修单位：</span>
              <span>{{repairUnitNameList}}</span>
            </div>
            <!-- <div>
              <span>联系电话：</span>
              <span>{{repairPhone}}</span>
            </div>-->

            <div>
              <span>竣工时间：</span>
              <span>{{completionTime}}</span>
            </div>
            <div>
              <span>质保期(年)：</span>
              <span>{{repairPartQuality}}</span>
            </div>
          </div>
          <div class="section2-content" v-else>
            <div style="text-align:center;margin-top:30px">暂无维修记录</div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <!-- <div class="box">
          <div class="remaindDeal">
            <div>提醒处理：</div>
          </div>
        </div>-->
        <template>
          <div class="clearfix" style="padding-left:27px;padding-bottom:10px">
            <div class="remaindDeal">提醒处理：</div>
            <el-radio-group v-model="radio">
              <el-radio :label="1" @click.native.prevent="radioChange(1)" name="approve">忽略</el-radio>
              <el-radio :label="3" @click.native.prevent="radioChange(3)" name="approve">暂不处理</el-radio>
            </el-radio-group>
          </div>
        </template>
        <div class="query-box" id="queryBox">
          <div class="clearfix">
            <div class="query-item" style="width:30%;font-size:14px;">
              <div class="query-title">提醒时间：</div>
              <div class="query-input" :class="{'edit':edit}">
                <el-date-picker
                  :readonly="edit"
                  v-model="remaindTime"
                  type="date"
                  placeholder="选择提醒时间"
                  size="small"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  style="width:200px;
    margin-left: -40px;"
                ></el-date-picker>
              </div>
            </div>
            <div
              class="query-item edit-btn"
              style="width:20%;font-size:14px"
              @click="Edit"
              v-show="radio == 1"
            >
              <i class="iconfont" style="vertical-align: middle;">&#xe61a;</i>修改下次提醒时间
            </div>
          </div>
        </div>
        <cancel-btn @clickBtn="dialogVisibles.remind = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submit" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getRepairRemindInfoList,
  saveOrUpdateRepairRemind,
  updateRepairRemindState,
  getRepairParts,
  getUnitList,
  getRepairApprovalDetail,
  getRepairRemindList,
  addRepairRemind,
  deleteRepairRemind,
  selectRepairRemindDetail,
  selectRepairRemind,
  updateRepairRemind,
  notRemind,
  batchDeleteRepairRemind
} from "./api";
import { getYardTree } from "../home/api";
import { getAllUnitList } from "@/axios/commonApi";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import treeSelect from "@/components/treeSelect/treeSelect_2";
import TreeSelect from "@/components/treeSelect/treeSelect_4";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import ignoreBtn from "@/components/button/ignoreBtn";
import dealBtn from "@/components/button/dealBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import temporaryDealBtn from "@/components/button/temporaryDealBtn";
import { log } from "util";
export default {
  components: {
    singleBtnGroup,
    tableBtnGroup,
    tablePagination,
    tableComponent,
    cancelBtn,
    confirmBtn,
    searchBtn,
    ignoreBtn,
    dealBtn,
    resetBtn,
    temporaryDealBtn,
    TreeSelect,
    treeSelect
  },
  data() {
    return {
      activeInfo:0,
      radio: 1,
      remaid: "",
      userUnit: [],
      unitIdList: "",
      unitOptions: [],
      repairPartsId: "",
      repairPartsOpts: [],
      currentPage: 1,
      pageSize: 15,
      total: 1,
      loading: null,
      tableHeight: 0,
      selectData: [],
      tableData: [],
      dialogTitle: "",
      operateType: "add",
      dialogVisible: false,
      dialogVisibles: {
        add: false,
        remind: false
      },
      ignoreDialogVisible: false,
      handleOption: [
        {
          text: "未提醒",
          id: 1
        },
        {
          text: "忽略",
          id: 2
        },
        {
          text: "处理",
          id: 3
        }
      ],
      treeName:'selectTree2',
      Form: {
        contactPhone: "",
        responsiblePerson: "",
        repairPart: "",
        remaindTime: "",
        yardId: 0,
        yardName:'',
        buildId: 0,
        buildName:'',
        floorname:'',
        floorName: 0,
        houseId: "",
        houseName:'',
        repairGoods: "",
        remaindRemark: "",
        repairAddress: '',
      },
      form: {
        yardId: null,
        buildId: null,
        floorName: null,
        houseId: null
      },
      RemindTimeForm: {
        repairRemind: "",
        remaindTime: "",
        remaindHandle: 2
      },
      treeId: {},
      currentLevel: null,
      treeShow: false,
      uersUnit: "",
      repairInfoMore: [],
      repairUnitName: "",
      repairPhone: "",
      repairQualityDate: "",
      lastRepairTime: "",
      baseInfo: {
        position: "",
        unitNameList: "",
        responsiblePerson: "",
        contactPhone: "",
        repairPartName: "",
        remaindTime: "",
        remaindHandle: ""
      },
      remaindTime: "",
      oldRemaindTime: "",
      edit: true,
      repairPositionTarget: "",
      completionTime: "",
      repairModulePosition: "",
      repairePartName: "",
      repairPartQuality: "",
      repairUnitNameList: "",
      repairRemind: "",
      // treeData: [],
      remaidOpts: [
        {
          id: null,
          name: "全部"
        },
        {
          id: 0,
          name: "不提醒"
        },
        {
          id: 1,
          name: "提醒"
        }
      ],
      state: true,
      rules: {
        remaindTime: [
          { required: true, message: "提醒时间不能为空", trigger: "blur" }
        ],
        responsiblePerson: [
          { required: true, message: "负责人不能为空", trigger: "blur" }
        ],
        repairPart: [
          { required: true, message: "维修部位不能为空", trigger: "blur" }
        ],
        repairAddress: [
          { required: true, message: "具体方位不能为空", trigger: "blur" }
        ]
      },
    };
  },
  watch: {
    $route: {
      handler(to, from) {
        sessionStorage.removeItem("repairRemind_buildId");
      },
      immediate: false
    }
  },
  created() {
    this.treeShows = true;
    this.getUnit();
    this.getRepairPartsList();
    this.getList();
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
    // 获取维修部位信息列表
    getRepairPartsList() {
      getRepairParts()
        .then(res => {
          this.repairPartsOpts = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取维修部位信息列表失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取单位列表
    getUnit() {
      getUnitList().then(res => {
        this.unitOptions = res;
      });
    },
    // 改变页面条数
    changeSize(size) {
      this.currentPage = 1
      this.pageSize = size;
      this.getList();
    },
    // 改变页面页数
    changePage(page) {
      this.currentPage = page;
      this.getList();
    },
    // 获取职称信息列表
    getList() {
      this.loadingStart(".table-box");
      getRepairRemindList(
        this.currentPage,
        this.pageSize,
        this.form,
        this.unitIdList,
        this.remaid
      )
        .then(res => {
          res.records.forEach(item => {
            this.repairPartsOpts.forEach(items => {
              if (item.repairPart == items.repairPartsId) {
                item["repairParts"] = items.repairPartName;
              }
            });
          });
          res.records.forEach(item=>{
            if(item.remaindTime && item.remaindTime.indexOf(' 00:00:00') != -1){
              item.remaindTime = item.remaindTime.split(' 00:00:00')[0]
            }
            if(item.lastRepairTime && item.lastRepairTime.indexOf(' 00:00:00')){
              item.lastRepairTime = item.lastRepairTime.split(' 00:00:00')[0]
            }
          })
          this.total = res.total;
          this.tableData = res.records;
          this.loadingEnd();
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取维修提醒列表失败";
          this.$message({
            message: msg,
            type: "error"
          });
          this.loadingEnd();
        });
    },
    // 输入框失去焦点
    getRepaireList() {
    },
    // 查询信息
    search() {
      this.currentPage = 1;
      this.getList();
    },
    reset() {
      this.clearForm(this.form);
      this.currentPage = 1;
      this.unitIdList = "";
      this.remaid = "";
      this.search();
      // this.treeShows = false
      this.$nextTick(()=>{
        eventBus.$emit("ClearTreeSelectVal");
      })
    },
    // 新增
    add() {
      this.dialogVisibles.add = true;
      this.operateType = "add";
      this.treeShow = true;
    },
    // 批量删除
    batchDelete() {
      let ids = []
      this.selectData.forEach(item => {
        ids.push(item.repairRemind)
      })
      if (ids.length > 0) {
        this.$confirm("此操作将永久删除该维修提醒信息, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          batchDeleteRepairRemind(ids).then(res => {
            this.$message({
              message: "批量删除维修提醒信息成功!",
              type: "success"
            });
            this.currentPage = 1;
            this.getList()
          }).catch(err => {
            const msg = err.result ? err.result : "批量删除维修提醒信息失败";
            this.$message({
              message: msg,
              type: "error"
            });
          })
        }).catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
      } else {
        this.$message({
          message: "请选择需要删除的维修提醒",
          type: "info"
        });
      }
    },
    // 单个删除
    singleDetle(row) {
      this.$confirm("此操作将永久删除该维修提醒信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteRepairRemind(row.repairRemind)
            .then(res => {
              this.$message({
                message: "删除维修提醒信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
              eventBus.$emit("updateRemindTips");
            })
            .catch(err => {
              const msg = err.result ? err.result : "删除维修提醒信息失败";
              this.$message({
                message: msg,
                type: "error"
              });
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    // 获取维修提醒基本信息
    getRepairRemindDetail(id) {
      selectRepairRemindDetail(id)
        .then(res => {
          if (res.length != 0) {
            let arr = ["未提醒", "忽略", "处理"];
            this.baseInfo.contactPhone = res.contactPhone;
            this.baseInfo.position = res.position;
            this.baseInfo.repairPartName = res.repairPartName;
            this.baseInfo.responsiblePerson = res.responsiblePerson;
            this.baseInfo.unitNameList = res.unitNameList.join(",");
            this.baseInfo.remaindHandle = arr[res.remaindHandle - 1];
            this.baseInfo.repairGoods = res.repairGoods;
            this.baseInfo.remaindRemark = res.remaind_remark;
            this.remaindTime = res.remaindTime;
            this.oldRemaindTime = res.remaindTime;
          }
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取维修基本信息失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取相关维修记录
    getSelectRepairRemind(row) {
      selectRepairRemind(row)
        .then(res => {
          if (res.length == 0) {
            this.state = false;
          }
          if (res.length != 0) {
            res.forEach(item=>{
              if(item.completionTime.indexOf(' 00:00:00') != -1){
                item.completionTime = item.completionTime.split(' 00:00:00')[0]
              }
            })
          this.repairInfoMore = res;
            this.repairPositionTarget = res[0].positionStr
            this.completionTime = res[0].completionTime;
            this.repairePartName = res[0].repairPartName;
            this.repairPartQuality = res[0].repairPartQuality;
            this.repairUnitNameList = res[0].repairUnitNameList
              ? res[0].repairUnitNameList.join(",")
              : "";
            this.responsiblePerson = res[0].responsiblePerson;
          }
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取相关维修记录失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 维修提醒
    remindClick(row) {
      this.dialogVisibles.remind = true;
      this.repairRemind = row.repairRemind;
      // todus
      this.getRepairRemindDetail(this.repairRemind);
      this.getSelectRepairRemind(row);
    },
    handleCommand(index) {
      this.activeInfo = index
      this.repairInfoMore.forEach((item,indexs) => {
        if (indexs== index) {
          this.repairPositionTarget = item.positionStr
          this.completionTime = item.completionTime;
          this.repairePartName = item.repairPartName;
          this.repairPartQuality = item.repairPartQuality;
          this.repairUnitNameList = item.repairUnitNameList.length>0
            ? item.repairUnitNameList.join(",")
            : "";
          this.responsiblePerson = item.responsiblePerson;
        }
      });
    },
    // 提交表单
    submitForm(formName) {
      if (this.currentLevel === 1) {
        this.Form.buildId = null;
        this.Form.floorName = null;
        this.Form.houseId = null;
      } else if (this.currentLevel === 2) {
        this.Form.floorName = null;
        this.Form.houseId = null;
      } else if (this.currentLevel === 3) {
        this.Form.houseId = null;
      } else {
        this.Form.yardId = null;
        this.Form.buildId = null;
        this.Form.floorName = null;
        this.Form.houseId = null;
      }
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.operateType === "edit") {
            delete this.Form.password;
          }
          saveOrUpdateRepairRemind(this.Form)
            .then(res => {
              this.$message({
                message: `提醒成功`,
                type: "success"
              });
              this.dialogVisible = false;
              this.getList();
              eventBus.$emit("updateRemindTips");
              if (this.Form.password === undefined) {
                this.Form.password = "";
              }
            })
            .catch(err => {
              const msg = err.result ? err.result : "提醒失败";
              this.$message({
                message: msg,
                type: "error"
              });
            });
        } else {
          return false;
        }
      });
    },
    // dialog打开的回调
    dialogOpen() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
      });
    },
    Open() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
      });
    },
    // dialog关闭的回调
    dialogClosed() {
      this.activeInfo = 0
      for (let key in this.Form) {
        var type = this.getDataType(this.Form[key]);
        if (type === "String") {
          this.Form[key] = "";
        } else if (type === "Array") {
          this.Form[key] = [];
        } else if (type === "Object") {
          this.Form[key] = {};
        } else {
          this.Form[key] = null;
        }
      }
      this.userUnit = [];
      this.treeShow = false;
      this.edit = true;
      this.contactPhone = "";
      this.position = "";
      this.remaindTime = "";
      this.repairPartName = "";
      this.responsiblePerson = "";
      this.unitNameList = "";
      this.remaindTime = "";
      this.oldRemaindTime = "";
      this.repairPositionTarget = "";
      this.completionTime = "";
      this.repairPartName = "";
      this.repairPartQuality = "";
      this.repairUnitNameList = "";
      this.responsiblePerson = "";
      this.repairRemind = "";
      this.repairInfoMore = [];
      // this.treeData = []
      this.state = true;
      this.radio = 1;
    },

    Closed() {
      for (let key in this.Form) {
        var type = this.getDataType(this.Form[key]);
        if (type === "String") {
          this.Form[key] = "";
        } else if (type === "Array") {
          this.Form[key] = [];
        } else if (type === "Object") {
          this.Form[key] = {};
        } else {
          this.Form[key] = null;
        }
      }
      this.userUnit = [];
      this.treeShow = false;
      this.edit = true;
      eventBus.$emit("clearTreeSelectVal");
      this.radio = 1;
    },
    radioChange(value) {
      this.radio = value;
      if (value == 1) {
        this.edit = true;
      }
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
    // 获取数据类型
    getDataType(obj) {
      var typeArr = ["String", "Object", "Number", "Array", "Function"];
      for (let name of typeArr) {
        if (Object.prototype.toString.call(obj) === `[object ${name}]`) {
          return name;
        }
      }
    },
    // 只能输入正浮点数
    limitFloat(key, flag) {
      this.Form[key] = this.Form[key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '');
      if (flag) {
        this.Form[key] = (this.Form[key] - 0).toFixed(2);
      }
    },
    // 只能输入正整数
    limitInt(key, flag) {
      this.Form[key] = this.Form[key].replace(/[^0-9]/g, "");
    },
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
    // 提交（添加）表单
    submitIgnoreForm(formName) {
      this.$refs[formName].validate(valid => {
        this.submitFlag = true;
        if (valid) {
          this.repairPartsOpts.forEach(item => {
            if (this.Form.repairPart == item.repairPartsId) {
              this.Form.repairParts = item.repairPartName;
            }
          });
          addRepairRemind(this.Form, this.userUnit)
            .then(res => {
              this.$message({
                message: `添加成功`,
                type: "success"
              });
              this.dialogVisibles.add = false;
              this.tableData.unshift(this.Form);
              this.getList();
              eventBus.$emit("updateRemindTips");
            })
            .catch(err => {
              const msg = err.result ? err.result : "添加失败";
              this.$message({
                message: msg,
                type: "error"
              });
            });
        } else {
          return false;
        }
      });
    },
    submit() {
      if (this.radio == 1) {
        this.submitRemind();
      } else {
        this.temporaryDeal();
      }
    },
    // 忽略的方法
    submitRemind() {
      if(this.remaindTime){
        if (
        new Date(this.remaindTime).valueOf() >=
        new Date(this.oldRemaindTime).valueOf()
      ) {
        updateRepairRemind(this.remaindTime, this.repairRemind)
          .then(res => {
            this.dialogVisibles.remind = false;
            this.getList();
            eventBus.$emit("updateRemindTips");
          })
          .catch(err => {
            const msg = err.result ? err.result : "修改提醒时间失败";
            this.$message({
              message: msg,
              type: "error"
            });
          });
      } else {
        this.$message({
          message: `修改提醒的时间不能提前与当前的提醒时间`,
          type: "info"
        });
      }
      }else{
        this.$message({
          message: `请填写大于当前的提醒时间`,
          type: "info"
        });
      }
    },
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
    },
    // 处理
    deal() {
      this.dialogVisibles.remind = false;
      if(this.selectData.length>0){
        this.$store.commit('getRepaireList',this.selectData)
        this.$router.push({ path: "repairApply" });
      }else{
        this.$message({
          type:"info",
          message:'请选择需处理的维修提醒'
        })
      }
    },
    // 暂不处理
    temporaryDeal() {
      notRemind(this.repairRemind)
        .then(res => {
          this.getList();
          eventBus.$emit("updateRemindTips");
          this.dialogVisibles.remind = false;
        })
        .catch(err => {
          const msg = err.result ? err.result : "暂不提醒失败失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
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
            const msg = err.result ? err.result : "树获取失败";
            this.$message({
              message: msg,
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
            tempObj.id_floor = `${this.Form.yardId}:${this.Form.buildId}:${
              this.Form.floorName
              }`;
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
            tempObj.id_floor = `${this.Form.yardId}:${this.Form.buildId}:${
              this.Form.floorName
              }`;
            tempObj.floorName = this.Form.floorName;
            tempObj.id_house = this.Form.houseId;
            eventBus.$emit("setTreeDefaultVal", tempObj);
          }
        }, 1000);
      } else if (node.level === 3) {
        const repairRemind_buildId =
          sessionStorage.getItem("repairRemind_buildId") - 0;
        getYardTree(node.data.value, 3, null, repairRemind_buildId)
          .then(res => {
            res.forEach(item => {
              if (!item.expand || item.expand === undefined) {
                item.leaf = true;
              }
            });
            return resolve(res);
          })
          .catch(err => {
            const msg = err.result ? err.result : "树获取失败";
            this.$message({
              message: msg,
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
            const msg = err.result ? err.result : "树获取失败";
            this.$message({
              message: msg,
              type: "error"
            });
          });
      }
    },
    loadNodes(node, resolve) {
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
            const msg = err.result ? err.result : "树获取失败";
            this.$message({
              message: msg,
              type: "error"
            });
          });
        setTimeout(() => {
          if (
            this.form.yardId &&
            !this.form.buildId &&
            !this.form.floorName &&
            !this.form.houseId
          ) {
            let tempObj = {};
            tempObj.level = 1;
            tempObj.id_yard = this.form.yardId;
            eventBus.$emit("setTreeDefaultVal", tempObj);
          } else if (
            this.form.yardId &&
            this.form.buildId &&
            !this.form.floorName &&
            !this.form.houseId
          ) {
            let tempObj = {};
            tempObj.level = 2;
            tempObj.id_yard = this.form.yardId;
            tempObj.id_build = `${this.form.yardId}:${this.form.buildId}`;
            eventBus.$emit("setTreeDefaultVal", tempObj);
          } else if (
            this.form.yardId &&
            this.form.buildId &&
            this.form.floorName &&
            !this.form.houseId
          ) {
            let tempObj = {};
            tempObj.level = 3;
            tempObj.id_yard = this.form.yardId;
            tempObj.id_build = `${this.form.yardId}:${this.form.buildId}`;
            tempObj.buildId = this.form.buildId;
            tempObj.id_floor = `${this.form.yardId}:${this.form.buildId}:${
              this.form.floorName
              }`;
            eventBus.$emit("setTreeDefaultVal", tempObj);
          } else if (
            this.form.yardId &&
            this.form.buildId &&
            this.form.floorName &&
            this.form.houseId
          ) {
            let tempObj = {};
            tempObj.level = 4;
            tempObj.id_yard = this.form.yardId;
            tempObj.id_build = `${this.form.yardId}:${this.form.buildId}`;
            tempObj.id_floor = `${this.form.yardId}:${this.form.buildId}:${
              this.form.floorName
              }`;
            tempObj.floorName = this.form.floorName;
            tempObj.id_house = this.form.houseId;
            eventBus.$emit("setTreeDefaultVal", tempObj);
          }
        }, 1000);
      } else if (node.level === 3) {
        const repairRemind_buildId =
          sessionStorage.getItem("repairRemind_buildId") - 0;
        getYardTree(node.data.value, 3, null, repairRemind_buildId)
          .then(res => {
            res.forEach(item => {
              if (!item.expand || item.expand === undefined) {
                item.leaf = true;
              }
            });
            return resolve(res);
          })
          .catch(err => {
            const msg = err.result ? err.result : "树获取失败";
            this.$message({
              message: msg,
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
            const msg = err.result ? err.result : "树获取失败";
            this.$message({
              message: msg,
              type: "error"
            });
          });
      }
    },
    // 选中树节点
    handleCheckChange(data) {
      if (data.address) {
        this.Form.repairAddress = 'true';
        this.$refs['Form'].clearValidate(['repairAddress']);
      }
      this.currentLevel = data.type;
      if (data.type === 1) {
        this.Form.yardId = data.value;
        this.Form.yardName = data.text;
      } else if (data.type === 2) {
        this.Form.yardName = data.address.split('-')[0];
        this.Form.yardId = data.id.split(":")[0];
        this.Form.buildId = data.value;
        this.Form.buildName = data.text;
      } else if (data.type === 3) {
        this.Form.yardName = data.address.split('-')[0];
        this.Form.buildName = data.address.split('-')[1];
        this.Form.yardId = data.id.split(":")[0];
        this.Form.buildId = data.id.split(":")[1];
        this.Form.floorName = data.value;
        this.Form.floorname = data.text;
      } else {
        this.Form.yardName = data.address.split('-')[0];
        this.Form.buildName = data.address.split('-')[1];
        this.Form.floorname = data.address.split('-')[2];
        this.Form.yardId = data._id.split(":")[0];
        this.Form.buildId = data._id.split(":")[1];
        this.Form.floorName = data._id.split(":")[2];
        this.Form.houseId = data.value;
        this.Form.houseName = data.text;
      }
      this.Form.position = data.address;
    },
    handleCheckChanges(data) {
      this.currentLevel = data.type;
      if (data.type === 1) {
        this.form.yardId = data.value;
      } else if (data.type === 2) {
        this.form.buildId = data.value;
      } else if (data.type === 3) {
        this.form.floorName = data.value;
      } else {
        this.form.houseId = data.value;
      }
    },
    // 展开树节点
    handleNodeExpand(data, node) {
      if (node.level === 1) {
        this.Form.yardId = data.value;
      } else if (node.level === 2) {
        this.Form.buildId = data.value;
        sessionStorage.setItem("repairRemind_buildId", data.value);
      } else if (node.level === 3) {
        this.Form.floorName = data.value;
      }
    },
    handleNodeExpands(data, node) {
      if (node.level === 1) {
        this.form.yardId = data.value;
      } else if (node.level === 2) {
        this.form.buildId = data.value;
        // sessionStorage.setItem("repairRemind_buildId", data.value);
      } else if (node.level === 3) {
        this.form.floorName = data.value;
      }
    },
    // 编辑
    Edit() {
      this.edit = false;
    }
  },
  mounted() {
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
#serviceRemind .query-box {
  margin-bottom: 20px;
}
#serviceRemind .query-item {
  font-size: 16px;
  line-height: 32px;
  float: left;
  width: 18%;
}
#serviceRemind .query-item + .query-item {
  margin-left: 50px;
}
#serviceRemind .add .query-item {
  width: 100%;
}
#serviceRemind .query-item > div {
  float: left;
}
#serviceRemind .query-item > .query-input {
  width: calc(100% - 80px);
}
#serviceRemind .query-item .edit .el-input__inner {
  border: none;
}
#serviceRemind .query-item .el-date-editor.el-input,
#serviceRemind .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#serviceRemind .query-item .el-select {
  line-height: 32px;
  width: 100%;
}
#serviceRemind .query-item .content-box-dropdown,
#serviceRemind .query-item #selectTree_select_tree,
#serviceRemind .query-item #selectTree_select_tree .selectInput-box,
#serviceRemind
  .query-item
  #selectTree_select_tree
  .selectInput-box
  .el-input__inner {
  width: 100% !important;
}
#serviceRemind .query-btn {
  float: right;
}
#serviceRemind .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#serviceRemind .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#serviceRemind .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#serviceRemind .el-form-item__content {
  width: 200px;
}
#serviceRemind .remaindRemark .el-form-item__content {
  width: 820px;
  padding-top: 4px;
}
#serviceRemind .remaindRemark .el-form-item__label {
  text-align-last: justify;
  padding-left: 30px;
}
#serviceRemind .detail-form-item .el-input__inner {
  border: 0;
  padding-left: 0;
  width: 215px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
#serviceRemind .detail-form-item .el-textarea__inner {
  border: 0;
  padding-left: 0;
  width: 550px;
}
#serviceRemind .repair-info-box,
#serviceRemind .repair-records-box {
  padding: 25px 25px 30px;
  padding-right: 0;
}
#serviceRemind .el-dialog__footer .dialog-footer .remaindDeal {
  float: left;
  font-size: 14px;
}
#serviceRemind .el-dialog__footer .dialog-footer .el-radio-group {
  float: left;
  padding-top: 4px;
}
#serviceRemind .el-dialog__footer .dialog-footer .box {
  width: 100%;
  padding-left: 27px;
  box-sizing: border-box;
  padding-bottom: 20px;
}
/* #serviceRemind .el-dialog__footer .remaindDeal  {
  
  width: 100%;
  border-bottom: 1px solid #d8d8d8;
  font-size: 16px;
  height: 36px;
  line-height: 36px;
  text-align: left;
} */
#serviceRemind .el-dialog__footer .remaindDeal > div {
  border-bottom: 1px solid #498fea;
  width: 80px;
}
#serviceRemind .el-dialog__footer .query-box {
  margin-bottom: 60px;
  padding-left: 27px;
}
#serviceRemind .el-dialog__footer .query-box .query-item + .query-item {
  margin-left: -60px;
}
#serviceRemind .el-dialog__footer .el-date-editor--date {
  padding-top: 2px;
}
#serviceRemind .repair-info-base > div {
  float: left;
  font-size: 14px;
  line-height: 36px;
  width: 30%;
  padding-right: 20px;
}
#serviceRemind .repair-info-base > div:nth-child(1),
#serviceRemind .repair-info-base > div:nth-child(2) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
#serviceRemind .info-detail-title {
  border-bottom: 1px solid #d8d8d8;
}
#serviceRemind .info-detail-title > span {
  font-size: 16px;
  line-height: 36px;
  float: left;
  border-bottom: 1px solid #498fea;
  position: relative;
  top: 1px;
}
#serviceRemind .info-detail-title > .info-detail-btn {
  float: right;
  padding-right: 20px;
}
#serviceRemind .info-detail-title > .info-detail-btn .el-dropdown {
  line-height: 36px;
  font-size: 16px;
}
#serviceRemind .info-detail-btn > span {
  display: inline-block;
  line-height: 36px;
  font-size: 16px;
}
#serviceRemind .info-detail-btn > span + span {
  margin-left: 20px;
}
#serviceRemind .delete-btn {
  color: #f65c5f;
  cursor: pointer;
}
#serviceRemind .delete-btn:hover {
  color: #d82a2d;
}
#serviceRemind .edit-btn,
#serviceRemind .more-btn {
  color: #3891e0;
  cursor: pointer;
  /* vertical-align: middle; */
}
#serviceRemind .edit-btn:hover,
#serviceRemind .more-btn:hover {
  color: #035cac;
}
#serviceRemind .section2-content > div {
  height: 40px;
  line-height: 40px;
}
#serviceRemind .section2-content .el-form-item__content {
  width: 70%;
}
#serviceRemind .section2-content .el-textarea,
#serviceRemind .section2-content .upload_box {
  margin-top: 5px;
}
#serviceRemind .dialog-footer .edit {
  border: 0;
}
#serviceRemind .warning {
  color: #ff3040;
}
#serviceRemind .el-dialog--center .el-dialog__body {
  padding-right: 0;
}
/* #serviceRemind .el-radio-group {
  text-align: left;
  padding-left: 113px;
  display: block;
  line-height: 32px;
} */
.el-popper.more-infos {
  padding: 0 !important;
}
.el-popper.more-infos .repairRemaind-info-more{
  margin-top: -2px; 
}
.el-popper.more-infos .repairRemaind-info-more li {
  line-height: 30px;
  padding: 0 10px;
  font-size: 13px;
}
.el-popper.more-infos .repairRemaind-info-more .info-active {
  background-color: #498fea;
  color: #fff;
}
#serviceRemind  .query-input #selectTree2_select_tree{
  width: 100%  !important;
}
#serviceRemind  .query-input #selectTree2_select_tree .selectInput-box{
  width:100% !important;
}
#serviceRemind  .query-input #selectTree2_select_tree .selectInput-box .el-input__inner{
  width:100% !important;
}
</style>


