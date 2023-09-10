<template>
  <div id="specialCheck">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">检查地点：</div>
          <div class="query-input">
            <search-tree-select
              @handleCheckChange="handleSearchCheckChange"
              :treeData="allTreeData"
              :treeShow="true"
              :inputWidth="'100%'"
              :inputHeight="'32px'"
              :treeName="searchTreeName"
            ></search-tree-select>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">相关单位：</div>
          <div class="query-input">
            <el-select v-model="unitId" placeholder="请选择相关单位">
              <el-option
                v-for="item in unitOptions"
                :key="item.unitId"
                :label="item.unitName"
                :value="item.unitId"
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
        <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="yardName" label="检查地点" align="center"></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="unitName" label="相关单位" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="specialInspectionResult"
          label="检查结果"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="specialInspectionTime"
          label="检查时间"
          align="center"
        ></el-table-column>
        <el-table-column label="检查状态" align="center">
          <template slot-scope="scope">
            <div :class="{'danger':scope.row.specialInspectionProblem==1,'primary':true}">
              <span>{{scope.row.specialInspectionProblem==1?'有问题':'没问题'}}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="specialInspectionUserName"
          label="检查人"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="240" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDetail="true"
              :isShowrectify="scope.row.specialInspectionProblem == 1?true:false"
              @updateClick="singleUpdate(scope.row)"
              @deleteClick="singleDelete(scope.row)"
              @detailClick="singleDetail(scope.row)"
              @rectifyClick="singleRectify(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group @addClick="addUnit" @batchDeleteClick="batchDelete"></table-btn-group>
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
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{dialogTitle}}</span>
      </div>
      <el-form :model="Form" :inline="true" :rules="rules" ref="Form">
        <el-form-item
          :label="operateType==='detail'? '相关单位:' : '相关单位'"
          label-width="120px"
          prop="specialInspectionUnitId"
        >
          <el-select
            v-show="operateType!=='detail'"
            v-model="Form.specialInspectionUnitId"
            placeholder="请选择相关单位"
          >
            <el-option
              v-for="item in unitOptions"
              :key="item.unitId"
              :label="item.unitName"
              :value="item.unitId"
            ></el-option>
          </el-select>
          <span v-show="operateType=='detail'">{{Form.specialInspectionUnitName}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '检查地点:' : '检查地点'"
          label-width="120px"
          prop="handleModuleList"
        >
          <tree-select
            v-show="operateType!=='detail'"
            @handleCheckChange="handleCheckChange"
            :treeData="treeData"
            :treeShow="treeShow"
            :inputWidth="'100%'"
            :inputHeight="'34px'"
            :treeName="treeName"
          ></tree-select>
          <span v-show="operateType=='detail'">{{Form.position}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '检查人:' : '检查人'"
          label-width="120px"
          prop="specialInspectionUserName"
        >
          <el-input
            v-show="operateType!=='detail'"
            :placeholder="operateType !='detail'?'请输入检查人':''"
            v-model="Form.specialInspectionUserName"
            size="small"
            v-enter-trim
          ></el-input>
          <span v-show="operateType=='detail'">{{Form.specialInspectionUserName}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '检查时间:' : '检查时间'"
          label-width="120px"
          prop="specialInspectionTime"
        >
          <el-date-picker
            v-show="operateType!=='detail'"
            v-model="Form.specialInspectionTime"
            type="date"
            placeholder="选择检查时间"
            size="small"
            value-format="yyyy-MM-dd"
            style="width:200px;"
          ></el-date-picker>
          <span v-show="operateType=='detail'">{{Form.specialInspectionTime}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '发现问题:' : '发现问题'"
          label-width="120px"
          prop="specialInspectionProblem"
        >
          <el-radio
            v-model="Form.specialInspectionProblem"
            :label="0"
            name="specialInspectionProblem"
            v-if="!(operateType==='detail')"
          >否</el-radio>
          <el-radio
            v-model="Form.specialInspectionProblem"
            :label="1"
            name="specialInspectionProblem"
            v-if="!(operateType==='detail')"
          >是</el-radio>
          <span v-if="operateType==='detail'">{{Form.specialInspectionProblem===0? '否':'是'}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '检查结果:' : '检查结果'"
          label-width="120px"
          :class="{'detail':operateType=='detail'}"
          prop="specialInspectionResult"
        >
          <el-input
            :placeholder="operateType !='detail'?'请输入检查结果':''"
            type="textarea"
            :readonly="operateType=='detail'"
            v-model="Form.specialInspectionResult"
            size="small"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          v-show="Form.specialInspectionProblem && !rectify"
          :class="{'detail':operateType=='detail'}"
          :label="operateType==='detail'? '整改意见:' : '整改意见'"
          label-width="120px"
          prop="specialInspectionRectifyComments"
        >
          <el-input
            :placeholder="operateType !='detail'?'请输入整改意见':''"
            :readonly="operateType=='detail'"
            type="textarea"
            v-model="Form.specialInspectionRectifyComments"
            size="small"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <!-- <el-form-item
          v-show="Form.specialInspectionProblem"
          :label="operateType==='detail'? '整改时限:' : '整改时限'"
          label-width="120px"
          prop="specialInspectionTime"
        >
          <el-date-picker
            v-model="Form.specialInspectionTime"
            type="date"
            placeholder="选择整改时限"
            size="small"
            value-format="yyyy-MM-dd"
            style="width:200px;"
          ></el-date-picker>
        </el-form-item>-->
        <el-form-item
          v-show="Form.specialInspectionProblem && !rectify"
          :label="operateType==='detail'? '整改时间:' : '整改时间'"
          label-width="120px"
          prop="specialInspectionRectifyTime"
        >
          <el-date-picker
            v-show="operateType!=='detail'"
            v-model="Form.specialInspectionRectifyTime"
            type="date"
            placeholder="选择整改时间"
            size="small"
            value-format="yyyy-MM-dd"
            style="width:200px;"
          ></el-date-picker>
          <span v-show="operateType=='detail'">{{Form.specialInspectionRectifyTime}}</span>
        </el-form-item>
        <el-form-item
          v-show="Form.specialInspectionProblem && !rectify"
          :label="operateType==='detail'? '所扣分值:' : '所扣分值'"
          label-width="120px"
          prop="specialInspectionPoints"
        >
          <el-input
            v-show="operateType!=='detail'"
            @keyup.native="limitInt('specialInspectionPoints')"
            :placeholder="operateType !='detail'?'请输入所扣分值':''"
            v-model="Form.specialInspectionPoints"
            size="small"
          ></el-input>
          <span v-show="operateType=='detail'">{{Form.specialInspectionPoints}}</span>
        </el-form-item>
        <!-- <el-form-item
          v-show="Form.specialInspectionProblem"
          :label="operateType==='detail'? '所扣分值:' : '所扣分值'"
          label-width="120px"
          prop="specialInspectionPoints"
        >
          <el-input
           v-show="operateType!=='detail'"
            @keyup.native="limitInt('specialInspectionPoints')"
            :placeholder="operateType !='detail'?'请输入所扣分值':''"
            v-model="Form.specialInspectionPoints"
            size="small"
          ></el-input>
          <span v-show="operateType=='detail'">{{Form.specialInspectionPoints}}</span>
        </el-form-item>-->
        <el-form-item
          v-show="rectify"
          label="整改状态"
          label-width="120px"
          prop="specialInspectionRectifyState"
        >
          <el-radio
            v-model="Form.specialInspectionRectifyState"
            :label="0"
            name="specialInspectionRectifyState"
          >未完成</el-radio>
          <el-radio
            v-model="Form.specialInspectionRectifyState"
            :label="1"
            name="specialInspectionRectifyState"
          >完成</el-radio>
        </el-form-item>
        <el-form-item
          v-show="rectify"
          label="整改结果"
          label-width="120px"
          prop="specialInspectionRectifyResult"
        >
          <el-input
            placeholder="请输入整改结果"
            type="textarea"
            v-model="Form.specialInspectionRectifyResult"
            size="small"
            v-enter-trim
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" v-if="operateType!=='detail'||rectify">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import {
  selectAllHandlePo,
  getUnitList,
  selectHandlePo,
  getSpecialList,
  delSpecialList,
  batchDelSpecialList,
  addOrUpdate,
  specialDetail,
  Update,
  reform
} from "./api";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import treeSelect from "@/components/treeSelect/treeSelect_1";
import searchTreeSelect from "@/components/treeSelect/treeSelect_3";
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
    treeSelect,
    searchTreeSelect
  },
  created() {
    this.getUnit();
    this.getList();
  },
  data() {
    let validComments = (rule, value, callback) => {
      if (
        this.Form.specialInspectionProblem &&
        !this.Form.specialInspectionRectifyComments
      ) {
        callback(new Error("整改意见不能为空"));
      } else {
        callback();
      }
    };
      let validModuleList = (rule, value, callback) => {
      if (
        !this.Form.addFlag
      ) {
        callback(new Error("检查地点不能为空"));
      } else {
        callback();
      }
    };
    return {
      unitId: null,
      unitOptions: [],
      startTime: "",
      allTreeData: [],
      buildIdList: [],
      yardIdList: [],
      searchTreeName: "specialSearchTree",
      treeName: "special",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      tableData: [],
      selectData: [],
      operateType: "",
      dialogTitle: "",
      dialogVisible: false,
      rules: {
        specialInspectionRectifyComments: [
          { required: true, validator: validComments, trigger: "blur" }
        ],
        handleModuleList:[
          { required: true, validator: validModuleList, trigger: "blur" }
        ],
        specialInspectionUnitId:[
          { required: true, message: '相关单位不能为空', trigger: "blur" }
        ],
        specialInspectionUserName:[
          { required: true, message: '检查人不能为空', trigger: "blur" }
        ],
        specialInspectionTime:[
          { required: true, message: '检查时间不能为空', trigger: "blur" }
        ],
        specialInspectionResult:[
          { required: true, message: '检查结果不能为空', trigger: "blur" }
        ],
      },
      Form: {
        specialInspectionUnitId: null,
        handleModuleList: [],
        specialInspectionBuildId: null,
        specialInspectionYardId: null,
        specialInspectionUserName: "",
        specialInspectionProblem: 0,
        specialInspectionTime: "",
        specialInspectionResult: "",
        specialInspectionRectifyComments: "",
        specialInspectionRectifyTime: "",
        specialInspectionPoints: null,
        specialInspectionRectifyState: 0,
        specialInspectionRectifyResult: "",
        addFlag:false
      },
      treeData: [],
      treeShow: false,
      rectify: false
    };
  },
  methods: {
    // 开始加载
    loadingStart(classname) {
      if (!this.loading) {
        this.loading = this.$loading({
          lock: true,
          text: "正在加载...",
          spinner: "el-icon-loading",
          target: classname,
          customClass: "loading-box"
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
    search() {
      this.currentPage = 1;
      this.getList();
    },
    reset() {
      this.currentPage = 1;
      this.buildIdList = [];
      this.yardIdList = [];
      this.unitId = null;
      eventBus.$emit(`${this.searchTreeName}_clear`);
      this.getList();
    },
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
    },
    //   获取使用单位列表
    getUnit() {
      getUnitList().then(res => {
        this.unitOptions = res;
      });
    },
    getList() {
      getSpecialList(
        this.currentPage,
        this.pageSize,
        this.unitId,
        this.yardIdList,
        this.buildIdList
      )
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
        })
        .catch(err => {
          const msg = err.result ? err.result : "专项巡检信息列表获取失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取新增树的数据
    selectHandlePo() {
      selectHandlePo()
        .then(res => {
          res.forEach(e => {
            e.disabled = true;
            e.children.forEach(es => {
              es.children = [];
            });
          });
          this.treeData = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : "检查地点获取失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取修改树的数据
    selectHandlePoUp(id) {
      selectHandlePoUp(id)
        .then(res => {
          this.treeData = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : "检查地点获取失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取搜索树的数据
    selectAllHandlePo() {
      selectAllHandlePo()
        .then(res => {
          res.forEach(e => {
            e.children = [];
            // e.disabled = true;
            // e.children.forEach(es => {
            //   es.children = [];
            //   es.disabled = true;
            // });
          });
          this.allTreeData = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : "处置位置获取失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 选择树
    handleCheckChange(checkedNodes, flag) {
      this.Form.handleModuleList = [];
      if (checkedNodes.length > 0) {
        this.Form.specialInspectionYardId = checkedNodes[0].value;
        this.Form.specialInspectionBuildId = checkedNodes[1].value;
        this.Form.addFlag = true
      }else {
        this.Form.addFlag = false
      }
    },
    handleSearchCheckChange(checkedNodes, halfCheckedNodes) {
      this.houseIdList = [];
      this.floorIdList = [];
      this.buildIdList = [];
      this.yardIdList = [];
      const nodes = checkedNodes.concat(halfCheckedNodes);
      if (nodes.length > 0) {
        nodes.forEach(node => {
          if (node.type === 3) {
            this.houseIdList.push(node.value);
          } else if (node.type === 2) {
            this.floorIdList.push(node.value);
          } else if (node.type === 1) {
            this.buildIdList.push(node.value);
          } else if (node.type === 0) {
            this.yardIdList.push(node.value);
          }
        });
      }
    },
    // 修改
    singleUpdate(row) {
      this.selectHandlePo();
      this.treeShow = true;
      this.operateType = "edit";
      this.dialogVisible = true;
      this.dialogTitle = "修改专项巡检";
      specialDetail(row.specialInspectionId)
        .then(res => {
          this.Form = res;
          if (JSON.stringify(res) != "{}") {
            this.Form["specialInspectionId"] = res.specialInspectionId;
            if (res.unitName) {
              this.Form.specialInspectionUnitId = this.unitOptions.filter(e => {
                return e.unitName == res.unitName;
              })[0].unitId;
            }
            let nameList = [];
            let checkedIdList = [];
            let expandIdList = [];
            nameList.push(res.buildName);
            this.Form.addFlag = true
            checkedIdList.push(`${res.yardId}:${res.buildId}`);
            expandIdList.push(`${res.yardId}`);
            eventBus.$emit(
              `${this.treeName}_select`,
              nameList,
              checkedIdList,
              expandIdList
            );
          }
        })
        .catch(err => {
          const msg = err.result ? err.result : "专项检查详情获取失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 详情
    singleDetail(row) {
      this.operateType = "detail";
      this.dialogVisible = true;
      this.dialogTitle = "专项巡检详情";
      specialDetail(row.specialInspectionId)
        .then(res => {
          this.Form = res;
          this.Form["specialInspectionUnitName"] = res.unitName;
          this.Form["position"] = res.yardName + "-" + res.buildName;
        })
        .catch(err => {
          const msg = err.result ? err.result : "专项检查详情获取失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    singleDelete(row) {
      // console.log(row.specialInspectionId);
      this.$confirm("此操作将永久删除该专项巡检, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          delSpecialList(parseInt(row.specialInspectionId))
            .then(res => {
              this.$message({
                message: "删除专项巡检成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : "删除专项巡检失败";
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
    //整改
    singleRectify(row) {
      this.operateType = "detail";
      this.dialogVisible = true;
      this.rectify = true;
      this.dialogTitle = "整改专项巡检";
      specialDetail(row.specialInspectionId)
        .then(res => {
          this.Form["specialInspectionId"] = res.specialInspectionId;
          this.Form["specialInspectionUnitName"] = res.unitName;
          if (res.yardName || res.buildName) {
            this.Form["position"] = res.yardName + "-" + res.buildName;
          } else {
            this.Form["position"] = "";
          }
          this.Form["specialInspectionRectifyResult"] =
            res.specialInspectionRectifyResult;
          this.Form["specialInspectionTime"] = res.specialInspectionTime;
          this.Form["specialInspectionUserName"] =
            res.specialInspectionUserName;
          this.Form["specialInspectionResult"] = res.specialInspectionResult;
          this.Form.specialInspectionRectifyState = 0;
          this.Form["specialInspectionProblem"] = res.specialInspectionProblem;
          this.Form["specialInspectionRectifyComments"] =
            res.specialInspectionRectifyComments;
        })
        .catch(err => {
          const msg = err.result ? err.result : "专项检查详情获取失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    //新增
    addUnit() {
      this.selectHandlePo();
      this.treeShow = true;
      this.operateType = "add";
      this.dialogTitle = "新增专项巡检";
      this.dialogVisible = true;
    },
    //批量删除
    batchDelete() {
      let deleteArray = [];
      this.selectData.forEach((item, index) => {
        deleteArray.push(item.specialInspectionId);
      });
      this.$confirm("此操作将永久删除这些专项巡检, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          if (deleteArray.length === 0) {
            this.$message({
              message: "请先勾选删除项！",
              type: "warning"
            });
            return false;
          }
          batchDelSpecialList(deleteArray)
            .then(res => {
              this.$message({
                message: "批量删除专项巡检成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : "批量删除专项巡检失败";
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
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.saveForm();
        } else {
          return false;
        }
      });
    },
    saveForm() {
      if (this.operateType == "add") {
        addOrUpdate(this.Form)
          .then(res => {
            if (res) {
              this.dialogVisible = false;
              this.getList();
            }
          })
          .catch(err => {
            const msg = err.result ? err.result : "新增专项巡检失败";
            this.$message({
              message: msg,
              type: "error"
            });
          });
      } else if (this.operateType == "edit") {
        Update(this.Form)
          .then(res => {
            if (res) {
              this.dialogVisible = false;
              this.getList();
            }
          })
          .catch(err => {
            const msg = err.result ? err.result : "修改专项巡检失败";
            this.$message({
              message: msg,
              type: "error"
            });
          });
      } else if (this.rectify) {
        reform(this.Form)
          .then(res => {
            if (res) {
              this.dialogVisible = false;
              this.getList();
            }
          })
          .catch(err => {
            const msg = err.result ? err.result : "整改专项巡检失败";
            this.$message({
              message: msg,
              type: "error"
            });
          });
      }
    },
    // dialog打开的回调
    dialogOpen() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
      });
    },
    // dialog关闭的回调
    dialogClosed() {
      this.treeShow = false;
      eventBus.$emit(`${this.treeName}_clearInput`);
      this.clearForm(this.Form);
      this.rectify = false;
      this.Form.specialInspectionProblem = 0;
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
    // 只能输入正整数
    limitInt(key) {
      this.Form[key] = this.Form[key].replace(/[^0-9]/g, "");
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
    }
  },
  mounted() {
    this.selectAllHandlePo();
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - queryBoxH - operateBoxH;
    }, 1);
  }
};
</script>
<style lang="">
#specialCheck .query-box {
  margin-bottom: 20px;
}
#specialCheck .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 18%;
}
#specialCheck .query-item > div {
  float: left;
}
#specialCheck .query-item > .query-input {
  width: calc(100% - 80px);
}
#specialCheck .query-item .el-select {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#specialCheck .query-item .el-date-editor.el-input,
#specialCheck .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#specialCheck .query-btn {
  float: right;
}
#specialCheck .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#specialCheck .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#specialCheck .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#specialCheck .el-form-item__content {
  width: 200px;
}
#specialCheck .el-form-item__content .el-input__inner {
  height: 32px !important;
  left: 32px;
}
#specialCheck .el-form-item:nth-of-type(7),
#specialCheck .el-form-item:nth-of-type(6),
#specialCheck .el-form-item:nth-of-type(11) {
  width: 100%;
}
#specialCheck .el-form-item:nth-of-type(7) .el-form-item__content,
#specialCheck .el-form-item:nth-of-type(6) .el-form-item__content,
#specialCheck .el-form-item:nth-of-type(11) .el-form-item__content {
  width: calc(100% - 180px);
  padding-top: 5px;
  box-sizing: border-box;
}
#specialCheck td .primary {
  color: #409eff;
}
#specialCheck td .primary.danger {
  color: #f56c6c;
}
#specialCheck .detail .el-textarea__inner {
  border: none;
  padding: 5px 0;
}
</style>