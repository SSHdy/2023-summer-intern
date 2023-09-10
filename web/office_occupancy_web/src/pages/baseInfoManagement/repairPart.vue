<template>
  <div id="repairePart">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">维修部位名称：</div>
          <div class="query-input">
            <el-select filterable v-model="repairPartId" placeholder="请选择维修部位名称">
              <el-option
                v-for="item in repairPartAllList"
                :key="item.repairPartsId"
                :label="item.repairPartName"
                :value="item.repairPartsId"
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
        <el-table-column prop="serialNum" label="序号" width="50" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="repairPartName"
          label="维修部位名称"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="repairPartQuality"
          label="质保期（年）"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="140" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDetail="false"
              @deleteClick="singleDetle(scope.row)"
              @updateClick="singleUpdate(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group
            @batchDeleteClick="batchDelete"
            @addClick="addUnit"
            :batchDelBtnShow="false"
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
      <el-form :inline="true" :model="Form" :rules="rules" ref="Form">
        <el-form-item label="维修部位名称" label-width="120px" prop="repairPartName">
          <el-input v-model.trim="Form.repairPartName" size="small" placeholder="请输入维修部位名称"></el-input>
        </el-form-item>
        <el-form-item label="质保期（年）" label-width="120px" prop="repairPartQuality">
          <el-input
            v-model.trim="Form.repairPartQuality"
            size="small"
            placeholder="请输入质保期"
            @blur="limitInt('repairPartQuality')"
            @keyup.native="limitInt('repairPartQuality')"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addOrUpdateRepairParts,
  getRepairPartInfoList,
  getRepairPartAllList,
  deletetRepairPart,
  batchDeleteRepairPart
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
      repairPartId: "",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      selectData: [],
      tableData: [],
      repairPartAllList: [],
      dialogTitle: "",
      operateType: "add",
      dialogVisible: false,
      Form: {
        repairPartName: "",
        repairPartQuality: "",
        repairPartsId: ""
      },
      rules: {
        repairPartName: [
          { required: true, message: "维修部位名称不能为空", trigger: "blur" }
        ],
        repairPartQuality: [
          { required: true, message: "质保期不能为空", trigger: "blur" }
        ]
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
    // 获取职称信息列表
    getList() {
      this.loadingStart('.table-box');
      getRepairPartInfoList(this.currentPage, this.pageSize, this.repairPartId)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach((item, index) => {
            item.serialNum = index + 1;
          });
          this.loadingEnd();
        })
        .catch(err => {
          const msg = err.result ? err.result : '维修部位信息获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
          this.loadingEnd();
        });
    },
    // 查询信息
    search() {
      this.currentPage = 1;
      this.getList();
    },
    reset() {
      this.currentPage = 1;
      this.repairPartId = '';
      this.search();
    },
    // 新增
    addUnit() {
      this.dialogVisible = true;
      this.dialogTitle = "新增维修部位信息";
      this.operateType = "add";
    },
    // 单个删除
    singleDetle(row) {
      this.$confirm("此操作将永久删除该维修部位信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deletetRepairPart(parseInt(row.repairPartsId))
            .then(res => {
              this.$message({
                message: "删除维修部位信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
              this.getRepairPartAllList();
            })
            .catch(err => {
              const msg = err.result ? err.result : '删除维修部位信息失败';
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
    // 批量删除
    batchDelete() {
      let deleteArray = [];
      this.selectData.forEach((item, index) => {
        deleteArray.push(item.repairPartsId);
      });
      this.$confirm("此操作将永久删除这些维修部位信息, 是否继续?", "提示", {
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
          batchDeleteRepairPart(deleteArray)
            .then(res => {
              this.$message({
                message: "批量删除维修部位信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : '批量删除维修部位信息失败';
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
    // 单个修改
    singleUpdate(row) {
      this.dialogVisible = true;
      this.dialogTitle = "修改维修部位信息";
      this.operateType = "edit";
      for ([key, value] of Object.entries(row)) {
        this.Form[key] = value;
      }
    },
    // 获取维修部位下拉选项
    getRepairPartAllList() {
      getRepairPartAllList().then(res => {
        this.repairPartAllList = res;
      }).catch(err => {
        const msg = err.result ? err.result : '维修部位下拉选项获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          let msg = this.operateType === "add" ? "新增" : "修改";
          addOrUpdateRepairParts(this.Form)
            .then(res => {
              this.$message({
                message: `${msg}维修部位信息成功`,
                type: "success"
              });
              this.dialogVisible = false;
              this.getList();
              this.getRepairPartAllList();
            })
            .catch(err => {
              const msg = err.result ? err.result : `${msg}维修部位信息成功`;
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
      this.clearForm(this.Form);
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
    // 只能输入正整数
    limitInt(key) {
      this.Form[key] = this.Form[key].replace(/[^0-9]/g, "");
    }
  },
  mounted() {
    this.getList();
    this.getRepairPartAllList();
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
#repairePart .query-box {
  margin-bottom: 20px;
}
#repairePart .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#repairePart .query-item > div {
  float: left;
}
#repairePart .query-item > .query-input {
  width: calc(100% - 112px);
}
#repairePart .query-item .el-date-editor.el-input,
#repairePart .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#repairePart .query-item .el-select {
  width: 100%;
}
#repairePart .query-btn {
  float: left;
  margin-left: 50px;
}
#repairePart .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#repairePart .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#repairePart .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
</style>