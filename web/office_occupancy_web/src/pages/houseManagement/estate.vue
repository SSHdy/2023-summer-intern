<template>
  <div id="estate">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">物业类型：</div>
          <div class="query-input">
            <el-select v-model="estateType" placeholder="请选择物业类型">
              <el-option
                v-for="item in estateTypeOpts"
                :key="item.id"
                :label="item.text"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">管理人：</div>
          <div class="query-input">
            <el-input v-model="manager" placeholder="请输入管理人" size="small" v-enter-trim></el-input>
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
          prop="estateName"
          label="物业名称"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="estateTypeName"
          label="物业类型"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column prop="manager" label="管理人" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column
          prop="estatePhone"
          label="物业电话"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column label="操作" width="130" align="center">
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
          <table-btn-group @batchDeleteClick="batchDelete" @addClick="add"></table-btn-group>
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
      @closed="dialogClosed"
      @open="dialogOpen"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{dialogTitle}}</span>
      </div>
      <el-form :inline="true" :model="Form" :rules="rules" ref="Form">
        <el-form-item label="物业名称" label-width="120px" prop="estateName">
          <el-input v-model="Form.estateName" placeholder="请输入物业名称" size="small" v-enter-trim></el-input>
        </el-form-item>
        <el-form-item label="物业类型" label-width="120px" prop="estateType">
          <el-select
            v-model="Form.estateType"
            placeholder="请选择物业类型"
            style="width:200px;"
            size="small"
          >
            <el-option
              v-for="item in estateTypeOpts"
              :key="item.id"
              :label="item.text"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="管理人" label-width="120px" prop="manager">
          <el-input v-model="Form.manager" placeholder="请输入管理人" size="small" v-enter-trim></el-input>
        </el-form-item>
        <el-form-item label="物业电话" label-width="120px" prop="estatePhone">
          <el-input
            v-model="Form.estatePhone"
            placeholder="请输入物业电话"
            size="small"
            @keyup.native="limitInt('estatePhone')"
            @blur="limitInt('estatePhone')"
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
  getEstateList,
  addEstate,
  updateEstate,
  deleteEstate,
  batchDeleteEstate
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
      estateType: "",
      manager: "",
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
      estateTypeOpts: [
        {
          id: 1,
          text: '自管'
        },
        {
          id: 2,
          text: '第三方物业'
        }
      ],
      Form: {
        estateId: null,
        estateType: "",
        manager: "",
        estatePhone: "",
        estateName: ''
      },
      rules: {
        estateName: [
          { required: true, message: '物业名称不能为空', trigger: "change" }
        ],
        estateType: [
          { required: true, message: '物业类型不能为空', trigger: "change" }
        ],
        manager: [
          { required: true, message: "管理人不能为空", trigger: "blur" }
        ],
        estatePhone: [
          { required: true, message: "物业电话不能为空", trigger: "blur" }
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
    // 获取信息列表
    getList() {
      this.loadingStart('.table-box');
      getEstateList(this.currentPage, this.pageSize, this.estateType, this.manager)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach(item => {
            if (item.estateType === 1) {
              item.estateTypeName = '自管'
            } else {
              item.estateTypeName = '第三方物业'
            }
          })
          this.loadingEnd();
        })
        .catch(err => {
          const msg = err.result ? err.result : '获取物业信息失败';
          this.$message({
            message: msg,
            type: "error"
          });
          this.loadingEnd();
        });
    },
    // 查询房屋信息
    search() {
      this.currentPage = 1;
      this.getList();
    },
    reset() {
      this.currentPage = 1;
      this.estateType = '';
      this.manager = '';
      this.search();
    },
    // 新增
    add() {
      this.dialogVisible = true;
      this.dialogTitle = "新增物业信息";
      this.operateType = "add";
    },
    // 单个删除
    singleDetle(row) {
      this.$confirm(`此操作将永久删除物业${row.estateName}, 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteEstate(parseInt(row.estateId))
            .then(res => {
              this.$message({
                message: "删除物业信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : '删除物业信息失败';
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
        deleteArray.push(item.estateId);
      });
      this.$confirm("此操作将永久删除这些物业信息, 是否继续?", "提示", {
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
          batchDeleteEstate(deleteArray)
            .then(res => {
              this.$message({
                message: "批量删除物业信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : '批量删除物业信息失败';
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
      this.dialogTitle = "修改物业信息";
      this.operateType = "edit";
      for ([key, value] of Object.entries(row)) {
        if (this.Form[key] !== undefined) {
          this.Form[key] = value;
        }
      }
    },
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.operateType === 'add') {
            addEstate(this.Form).then(res => {
              this.$message({
                message: `新增物业信息成功`,
                type: "success"
              });
              this.dialogVisible = false;
              this.getList();
            }).catch(err => {
              const msg = err.result ? err.result : '新增物业信息失败';
              this.$message({
                message: msg,
                type: "error"
              });
            })
          } else {
            updateEstate(this.Form).then(res => {
              this.$message({
                message: `修改物业信息成功`,
                type: "success"
              });
              this.dialogVisible = false;
              this.getList();
            }).catch(err => {
              const msg = err.result ? err.result : '修改物业信息失败';
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
    // dialog打开的回调
    dialogOpen() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
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
#estate .query-box {
  margin-bottom: 20px;
}
#estate .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#estate .query-item > div {
  float: left;
}
#estate .query-item > .query-input {
  width: calc(100% - 80px);
}
#estate .query-item .el-date-editor.el-input,
#estate .query-item .el-input__inner,
#estate .query-item .el-select {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#estate .query-btn {
  float: left;
  margin-left: 50px;
}
#estate .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#estate .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#estate .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#estate .el-form-item__content {
  width: 200px;
}
#estate .detail-form-item .el-input__inner {
  border: 0;
}
</style>


