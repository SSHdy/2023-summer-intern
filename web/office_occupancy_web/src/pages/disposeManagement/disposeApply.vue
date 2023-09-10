<template>
  <div id="disposeApply">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">处置位置：</div>
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
          prop="yardPosition"
          label="处置院落"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="buildPosition"
          label="处置楼座"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="handleDate"
          label="申请时间"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="handleArea"
          label="处置面积(m²)"
          align="center"
        ></el-table-column>
        <el-table-column label="申请状态" width="140" align="center">
          <template slot-scope="scope">
            <span
              :class="{'status-unreviewed' : scope.row.applyStatus===0,'status-pass' : scope.row.applyStatus===1,'status-notPass' : scope.row.applyStatus===2,'status-reviewimg' : scope.row.applyStatus===3}"
            >{{scope.row.applyStatusText}}</span>
          </template>
        </el-table-column>
        <el-table-column label="审批" width="140" align="center">
          <template slot-scope="scope">
            <div class="view-box" @click="stepView(scope.row)">
              <span class="iconfont view-icon">&#xe62b;</span>
              <span class="view-text">查看</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDelete="false"
              :isShowDetail="true"
              :isShowAlter="scope.row.applyStatus === 0"
              @updateClick="singleUpdate(scope.row)"
              @detailClick="singleDetail(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group :batchDelBtnShow="false" @addClick="addUnit"></table-btn-group>
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
    <!-- 新增修改 -->
    <el-dialog
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="780px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{dialogTitle}}</span>
      </div>
      <el-form :model="Form" :rules="rules" ref="Form">
        <el-form-item label="处置位置" label-width="120px" prop="handleModuleList">
          <tree-select
            @handleCheckChange="handleCheckChange"
            :treeData="treeData"
            :treeShow="treeShow"
            :inputWidth="'100%'"
            :inputHeight="'34px'"
            :treeName="treeName"
          ></tree-select>
        </el-form-item>
        <el-form-item label="申请原因" label-width="120px" prop="applyReason">
          <el-input v-model.trim="Form.applyReason" type="textarea" :rows="8" placeholder="请输入申请原因"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /新增修改 -->
    <!-- 详情 -->
    <el-dialog
      :visible.sync="detailDialogVisible"
      :close-on-click-modal="false"
      width="780px"
      @closed="detailDialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>处置申请详情</span>
      </div>
      <div class="detail-box">
        <div class="detail-item clearfix" v-if="detailForm.handleYard">
          <span>处置院落：</span>
          <span>{{detailForm.handleYard}}</span>
        </div>
        <div class="detail-item clearfix">
          <span>处置位置：</span>
          <span>{{detailForm.handlePosition}}</span>
        </div>
        <div class="detail-item clearfix">
          <span>处置面积：</span>
          <span>{{detailForm.handleArea ? detailForm.handleArea : 0}}m²</span>
        </div>
        <div class="detail-item clearfix" v-if="detailForm.applyStatus">
          <span>申请状态：</span>
          <span>{{detailForm.applyStatus}}</span>
        </div>
        <div class="detail-item clearfix" v-if="detailForm.handleDate">
          <span>申请时间：</span>
          <span>{{detailForm.handleDate}}</span>
        </div>
        <div class="detail-item clearfix" v-if="detailForm.applyReason">
          <span>申请原因：</span>
          <span>{{detailForm.applyReason}}</span>
        </div>
      </div>
    </el-dialog>
    <!-- /详情 -->
    <!-- 审批进度流程 -->
    <el-dialog
      :visible.sync="stepDialogVisible"
      :close-on-click-modal="false"
      width="780px"
      @closed="stepDialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>申请审批进度</span>
      </div>
      <div class="step-box">
        <el-steps
          :active="applyStepActive"
          align-center
          finish-status="success"
          :space="100"
          direction="vertical"
        >
          <el-step v-for="(item,index) in applyProcess" :key="index" :title="item.stepName">
            <div slot="description" v-show="index < applyStepActive">
              <div>
                <span>操作人：</span>
                {{item.transactors}}
              </div>
              <div>
                <span>申请时间：</span>
                {{item.processingTime}}
              </div>
            </div>
          </el-step>
        </el-steps>
      </div>
    </el-dialog>
    <!-- /审批进度流程 -->
  </div>
</template>

<script>
import {
  getHandleList,
  addHandle,
  updateHandle,
  getHandleDetail,
  selectHandlePo,
  selectHandlePoUp,
  selectAllHandlePo,
  selectProcessHistory
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
import searchTreeSelect from "@/components/treeSelect/treeSelect_3"
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
    searchTreeSelect,
  },
  data() {
    return {
      userId: '',
      searchTreeName: 'disposeSearchTree',
      treeName: 'dispose',
      houseIdList: [],
      yardIdList: [],
      startTime: "",
      endTime: "",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      selectData: [],
      tableData: [],
      dialogTitle: "",
      operateType: "add",
      dialogVisible: false,
      detailDialogVisible: false,
      stepDialogVisible: false,
      treeShow: false,
      treeData: [],
      allTreeData: [],
      Form: {
        handleId: null,
        handleApplicant: '',
        handleApplicantId: null,
        handleModuleList: [],
        applyReason: '',
        isHouse: null,
      },
      detailForm: {
        handleYard: '',
        handlePosition: '',
        applyReason: '',
        applyStatus: '',
        handleApplicant: '',
        handleArea: '',
        handleDate: ''
      },
      applyStepActive: 0,
      applyProcess: [],
      rules: {
        handleModuleList: [
          { required: true, message: "处置位置不能为空", trigger: "blur" }
        ],
        applyReason: [
          { required: true, message: "申请原因不能为空", trigger: "blur" }
        ]
      },
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
      this.userId = sessionStorage.getItem('userId') - 0;
      getHandleList(this.currentPage, this.pageSize, this.houseIdList, this.yardIdList, this.startTime, this.endTime, null, this.userId)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach(item => {
            item.buildPosition = '';
            item.yardPosition = '';
            item.buildPositionDtoList.forEach((position, index) => {
              if (index === 0) {
                item.yardPosition = position.yardName;
              }
              if (item.buildPositionDtoList.length === 1) {
                item.buildPosition = position.buildName;
              } else if (item.buildPositionDtoList.length === (index + 1)) {
                item.buildPosition += position.buildName;
              } else {
                item.buildPosition += `${position.buildName}、`;
              }
            });
            item.applyStatusText = this.getApplyStatus(item.applyStatus);
            item.handleArea = item.handleArea ? item.handleArea : 0;
          })
          this.loadingEnd();
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '处置申请列表获取失败';
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
      this.houseIdList = [];
      this.yardIdList = [];
      this.startTime = "";
      this.endTime = "";
      eventBus.$emit(`${this.searchTreeName}_clear`);
      this.getList();
    },
    // 新增
    addUnit() {
      this.selectHandlePo();
      this.dialogVisible = true;
      this.dialogTitle = "新增处置申请";
      this.operateType = "add";
      this.treeShow = true;
    },
    // 单个修改
    singleUpdate(row) {
      this.selectHandlePoUp(row.handleId);
      this.dialogVisible = true;
      this.dialogTitle = "修改处置申请";
      this.operateType = "edit";
      this.treeShow = true;
      getHandleDetail(row.handleId).then(res => {
        for ([key, value] of Object.entries(res)) {
          if (this.Form[key] !== undefined) {
            this.Form[key] = value;
          }
        }
        this.Form.handleModuleList = res.handleModuleList;
        let nameList = [];
        let checkedIdList = [];
        let expandIdList = [];
        if (res.isHouse === 0) {
          const yardId = res.buildPositionDtoList[0].yardId;
          expandIdList.push(yardId);
          res.buildPositionDtoList.forEach(build => {
            const buildTreeId = `${yardId}:${build.buildId}`
            checkedIdList.push(buildTreeId);
            nameList.push(build.buildName);
          })
        } else {
          const yardId = res.buildPositionDtoList[0].yardId;
          expandIdList.push(yardId);
          res.buildPositionDtoList.forEach((build, index) => {
            const buildId = build.buildId;
            const buildTreeId = `${yardId}:${build.buildId}`
            expandIdList.push(buildTreeId);
            build.floorNameDtoList.forEach(floor => {
              const floorTreeId = `${yardId}:${build.buildId}:${floor.floorName}`
              expandIdList.push(floorTreeId);
              floor.houseNumber.forEach(house => {
                const houseTreeId = `${yardId}:${build.buildId}:${floor.floorName}:${house.houseId}`
                nameList.push(house.houseNumber);
                checkedIdList.push(houseTreeId);
              })
            })
          })
        }
        eventBus.$emit(`${this.treeName}_select`, nameList, checkedIdList, expandIdList);
      }).catch(err => {
        const msg = err.result ? err.result : `处置申请详情获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 单个详情
    singleDetail(row) {
      this.detailDialogVisible = true;
      getHandleDetail(row.handleId).then(res => {
        for ([key, value] of Object.entries(res)) {
          if (this.detailForm[key] !== undefined) {
            this.detailForm[key] = value;
          }
        }
        let positionArr = [];
        if (res.isHouse === 0) {
          res.buildPositionDtoList.forEach(build => {
            const position = `${build.yardName}-${build.buildName}`
            positionArr.push(position);
          })
        } else {
          const yardName = res.buildPositionDtoList[0].yardName;
          res.buildPositionDtoList.forEach(build => {
            const buildName = build.buildName;
            const floorNameDtoList = res.buildPositionDtoList[0].floorNameDtoList;
            floorNameDtoList.forEach(floor => {
              const floorName = floor.floorName;
              floor.houseNumber.forEach((house => {
                const houseNumber = house.houseNumber;
                const position = `${yardName}-${buildName}-${floorName}层-${houseNumber}`
                positionArr.push(position);
              }))
            });
          })
        }
        this.detailForm.handlePosition = positionArr.join('，');
        this.detailForm.applyStatus = this.getApplyStatus(this.detailForm.applyStatus);
      }).catch(err => {
        const msg = err.result ? err.result : `处置申请详情获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 审批流程
    stepView(row) {
      this.stepDialogVisible = true;
      selectProcessHistory(row.processInstanceId).then(res => {
        this.applyProcess = res;
        let endFlag = false;
        for (let i = this.applyProcess.length - 1; i >= 0; i--) {
          if (this.applyProcess[i].stepName === 'EndEvent') {
            endFlag = true;
          }
          if (this.applyProcess[i].stepName === 'StartEvent' || this.applyProcess[i].stepName === 'ExclusiveGateway' || this.applyProcess[i].stepName === 'EndEvent') {
            this.applyProcess.splice(i, 1);
          }
        }
        if (endFlag) {
          this.applyStepActive = this.applyProcess.length;
        } else {
          this.applyStepActive = this.applyProcess.length - 1;
        }
      }).catch(err => {
        const msg = err.result ? err.result : `审批流程获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.Form.handleApplicant = sessionStorage.getItem('name');
          this.Form.handleApplicantId = sessionStorage.getItem('userId');
          if (this.operateType === 'add') {
            addHandle(this.Form).then(res => {
              this.$message({
                message: '处置申请新增成功',
                type: "success"
              });
              this.getList();
              this.dialogVisible = false;
              eventBus.$emit('updateBacklogTips');
            }).catch(err => {
              const msg = err.result ? err.result : `处置申请新增失败`;
              this.$message({
                message: msg,
                type: "error"
              });
            })
          } else if (this.operateType === 'edit') {
            updateHandle(this.Form).then(res => {
              this.$message({
                message: '处置申请修改成功',
                type: "success"
              });
              this.getList();
              this.dialogVisible = false;
            }).catch(err => {
              const msg = err.result ? err.result : `处置申请修改失败`;
              this.$message({
                message: msg,
                type: "error"
              });
            })
          }
        } else {
          return false;
        }
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
    // dialog打开的回调
    dialogOpen() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
      })
    },
    // dialog关闭的回调
    dialogClosed() {
      this.treeShow = false;
      eventBus.$emit(`${this.treeName}_clearInput`);
      this.clearForm(this.Form);
    },
    detailDialogClosed() {
      this.clearForm(this.detailForm);
    },
    stepDialogClosed() {
      this.applyStepActive = 0;
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
    // 选择树
    handleCheckChange(checkedNodes, flag) {
      this.Form.isHouse = flag ? 0 : 1;
      this.Form.handleModuleList = [];
      if (checkedNodes.length > 0) {
        checkedNodes.forEach(node => {
          if (node.type === 3) {
            let buildName = '';
            const yardId = parseInt(node.id.split(':')[0]);
            const buildId = parseInt(node.id.split(':')[1]);
            for (let i = 0; i < checkedNodes.length; i++) {
              if (checkedNodes[i].type === 1 && checkedNodes[i].value === buildId) {
                buildName = checkedNodes[i].name;
                break;
              }
            }
            const temp = {
              handleId: this.Form.handleId,
              buildId: buildId,
              yardId: yardId,
              buildName: buildName,
              houseId: node.value
            }
            this.Form.handleModuleList.push(temp);
          } else if (node.type === 1 && !node.children) {
            const buildName = node.name;
            const buildId = node.value;
            const yardId = parseInt(node.id.split(':')[0]);
            const temp = {
              handleId: this.Form.handleId,
              buildId: buildId,
              yardId: yardId,
              buildName: buildName
            }
            this.Form.handleModuleList.push(temp);
          }
        })
      }
    },
    handleSearchCheckChange(checkedNodes, halfCheckedNodes) {
      this.houseIdList = [];
      this.yardIdList = [];
      const nodes = checkedNodes.concat(halfCheckedNodes);
      if (nodes.length > 0) {
        nodes.forEach(node => {
          if (node.type === 3) {
            this.houseIdList.push(node.value);
          } else if (node.type === 0) {
            this.yardIdList.push(node.value);
          }
        })
      }
    },
    // 获取新增树的数据
    selectHandlePo() {
      selectHandlePo().then(res => {
        this.treeData = res;
      }).catch(err => {
        const msg = err.result ? err.result : '处置位置获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 获取修改树的数据
    selectHandlePoUp(id) {
      selectHandlePoUp(id).then(res => {
        this.treeData = res;
      }).catch(err => {
        const msg = err.result ? err.result : '处置位置获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 获取搜索树的数据
    selectAllHandlePo() {
      selectAllHandlePo().then(res => {
        this.allTreeData = res;
      }).catch(err => {
        const msg = err.result ? err.result : '处置位置获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    }
  },
  mounted() {
    this.getList();
    this.selectAllHandlePo();
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
#disposeApply .query-box {
  margin-bottom: 20px;
}
#disposeApply .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 18%;
}
#disposeApply .query-item > div {
  float: left;
}
#disposeApply .query-item > .query-input {
  width: calc(100% - 80px);
}
#disposeApply .query-item .el-select {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#disposeApply .query-item .el-date-editor.el-input,
#disposeApply .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#disposeApply .query-btn {
  float: right;
}
#disposeApply .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#disposeApply .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#disposeApply .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#disposeApply .status-unreviewed {
  color: #ff9f02;
}
#disposeApply .status-pass {
  color: #3891e0;
}
#disposeApply .view-box {
  cursor: pointer;
  color: #3891e0;
  font-size: 0;
  height: 28px;
  line-height: 28px;
}
#disposeApply .view-box:hover > .view-icon,
#disposeApply .view-box:hover > .view-text {
  color: #035cac;
}
#disposeApply .view-icon {
  font-size: 14px;
  position: relative;
  top: 1px;
}
#disposeApply .view-text {
  font-size: 14px;
}
#disposeApply .status-notPass {
  color: #f9463f;
}
#disposeApply .status-reviewimg {
  color: #1aa97b;
}
#disposeApply .detail-item {
  margin-bottom: 25px;
}
#disposeApply .detail-item > span {
  float: left;
}
#disposeApply .detail-item > span:last-child {
  display: inline-block;
  width: calc(100% - 70px);
}
</style>


