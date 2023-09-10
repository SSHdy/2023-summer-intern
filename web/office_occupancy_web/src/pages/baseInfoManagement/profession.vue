<template>
  <div id="baseInfoProfession">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">职称：</div>
          <div class="query-input">
            <el-input v-model.trim="name" placeholder="请输入职称"></el-input>
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
          :show-overflow-tooltip="true"
          prop="professionalName"
          label="职称"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="professionalLevelName"
          label="等级"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="specifiedArea"
          label="规定面积(m²)"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="90" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDetail="false"
              :isShowDelete="false"
              @updateClick="singleUpdate(scope.row)"
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
      width="780px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{dialogTitle}}</span>
      </div>
      <el-form :inline="true" :model="Form" :rules="rules" ref="Form">
        <el-form-item label="职称" label-width="120px" prop="professionalName">
          <el-input
            v-model.trim="Form.professionalName"
            size="small"
            placeholder="请输入职称"
            :disabled="operateType==='edit'"
          ></el-input>
        </el-form-item>
        <el-form-item label="等级" label-width="120px" prop="professionalLevel">
          <el-select
            v-model="Form.professionalLevel"
            placeholder="请选择等级"
            style="width:200px"
            size="small"
            :disabled="operateType==='edit'"
          >
            <el-option
              v-for="item in professionalLevelOpts"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="规定面积(m²)" label-width="120px" prop="specifiedArea">
          <el-input
            v-model.trim="Form.specifiedArea"
            size="small"
            @keyup.native="limitFloat('specifiedArea')"
            @blur="limitFloat('specifiedArea', true)"
            placeholder="请输入规定面积"
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
  getProfessionalInfoList,
  deleteProfession,
  batchDeleteProfession,
  saveOrUpdateProfession
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
      name: "",
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
      Form: {
        professionalId: "",
        professionalLevel: "",
        professionalName: "",
        specifiedArea: null
      },
      professionalLevelOpts: [
        {
          id: 1,
          name: '中央机关'
        },
        {
          id: 2,
          name: '省级机关'
        },
        {
          id: 3,
          name: '市级机关'
        },
        {
          id: 4,
          name: '县级机关'
        },
        {
          id: 5,
          name: '乡级机关'
        }
      ],
      rules: {
        professionalLevel: [
          { required: true, message: "职称不能为空", trigger: "blur" }
        ],
        professionalName: [
          { required: true, message: "等级不能为空", trigger: "blur" }
        ],
        specifiedArea: [
          { required: true, message: "规定面积不能为空", trigger: "blur" }
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
      getProfessionalInfoList(this.currentPage, this.pageSize, this.name)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach(item => {
            this.professionalLevelOpts.forEach(professional => {
              if (item.professionalLevel === professional.id) {
                item.professionalLevelName = professional.name;
              }
            })
            if(item.professionalLevelName === undefined) {
              item.professionalLevelName = '-'
            }
          })
          this.loadingEnd();
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '职称信息获取失败';
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
    reset() {
      this.currentPage = 1;
      this.name = '';
      this.search();
    },
    // 新增
    addUnit() {
      this.dialogVisible = true;
      this.dialogTitle = "新增职称信息";
      this.operateType = "add";
    },
    // 单个删除
    singleDetle(row) {
      this.$confirm("此操作将永久删除该职称信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteProfession(parseInt(row.professionalId))
            .then(res => {
              this.$message({
                message: "删除职称信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : '删除职称信息失败';
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
        deleteArray.push(item.professionalId);
      });
      this.$confirm("此操作将永久删除这些职称信息, 是否继续?", "提示", {
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
          batchDeleteProfession(deleteArray)
            .then(res => {
              this.$message({
                message: "批量删除职称信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : '批量删除职称信息失败';
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
      this.dialogTitle = "修改职称信息";
      this.operateType = "edit";
      for ([key, value] of Object.entries(row)) {
        this.Form[key] = value;
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
          let msg = this.operateType === "add" ? "新增" : "修改";
          saveOrUpdateProfession(this.Form)
            .then(res => {
              this.$message({
                message: `${msg}职称信息成功`,
                type: "success"
              });
              this.dialogVisible = false;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : `${msg}职称信息失败`;
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
    // 只能输入正浮点数
    limitFloat(key, flag) {
      this.Form[key] = this.Form[key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '')
      if (flag && this.Form[key] !== '') {
        this.Form[key] = (this.Form[key] - 0).toFixed(2);
      }
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
#baseInfoProfession .query-box {
  margin-bottom: 20px;
}
#baseInfoProfession .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#baseInfoProfession .query-item > div {
  float: left;
}
#baseInfoProfession .query-item > .query-input {
  width: calc(100% - 80px);
}
#baseInfoProfession .query-item .el-date-editor.el-input,
#baseInfoProfession .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#baseInfoProfession .query-btn {
  float: left;
  margin-left: 50px;
}
#baseInfoProfession .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#baseInfoProfession .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#baseInfoProfession .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
</style>


