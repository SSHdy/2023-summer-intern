<template>
  <div id="assetDevice">
    <div class="table-box">
      <tableComponent
        :tableData="tableData"
        :tableHeight="tableHeight"
        @select="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center"></el-table-column>
        <el-table-column prop="equipmentName" label="类别" align="center"></el-table-column>
        <el-table-column prop="equipmentNum" label="数量" align="center"></el-table-column>
        <el-table-column prop="equipmentCharge" label="负责人" align="center"></el-table-column>
        <el-table-column prop="equipmentContact" label="负责人联系方式" align="center"></el-table-column>
        <el-table-column label="操作" width="140" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDetail="false"
              @deleteClick="singleDetle(scope.row)"
              @updateClick="singleUpdate(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </tableComponent>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group @batchDeleteClick="batchDelete" @addClick="addDevice"></table-btn-group>
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
        <el-form-item label="设备名称" label-width="130px" prop="equipmentName">
          <el-input v-model.trim="Form.equipmentName" size="small"></el-input>
        </el-form-item>
        <el-form-item label="设备数量" label-width="130px" prop="equipmentNum">
          <el-input
            v-model.trim="Form.equipmentNum"
            size="small"
            @keyup.native="limitInt('equipmentNum')"
          ></el-input>
        </el-form-item>
        <el-form-item label="负责人" label-width="130px" prop="equipmentCharge">
          <el-input v-model.trim="Form.equipmentCharge" size="small"></el-input>
        </el-form-item>
        <el-form-item label="负责人联系方式" label-width="130px" prop="equipmentContact">
          <el-input v-model.trim="Form.equipmentContact" size="small"></el-input>
        </el-form-item>
        <el-form-item label="设备购入时间" label-width="130px" prop="equipmentTime">
          <el-date-picker
            v-model="Form.equipmentTime"
            type="date"
            placeholder="选择设备购入时间"
            size="small"
            style="width:200px;"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="设备唯一编号" label-width="130px" prop="equipmentCode">
          <el-input v-model.trim="Form.equipmentCode" size="small"></el-input>
        </el-form-item>
        <el-form-item label="设备型号" label-width="130px" prop="equipmentType">
          <el-input v-model.trim="Form.equipmentType" size="small"></el-input>
        </el-form-item>
        <el-form-item label="设备品牌" label-width="130px" prop="equipmentBrand">
          <el-input v-model.trim="Form.equipmentBrand" size="small"></el-input>
        </el-form-item>
        <el-form-item label="设备所属院落" label-width="130px" prop="yardId" class="select-item">
          <el-select v-model="Form.yardId" placeholder="请选择设备所属院落" @change="yardSelectChangeHandle">
            <el-option
              v-for="item in yardOptions"
              :key="item.yardId"
              :label="item.yardName"
              :value="item.yardId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="设备所属楼座" label-width="130px" prop="buildIdList" class="select-item">
          <el-select
            multiple
            v-model="Form.buildIdList"
            placeholder="请选择设备所属楼座"
            :disabled="!Form.yardId"
            no-data-text="该院落下暂无楼座"
          >
            <el-option
              v-for="item in buildOptions"
              :key="item.buildId"
              :label="item.buildName"
              :value="item.buildId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" label-width="130px" prop="equipmentRemark">
          <el-input
            v-model.trim="Form.equipmentRemark"
            size="small"
            type="textarea"
            style="width:544px"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" v-show="!(operateType==='detail')">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addDevice,
  updateDevice,
  getDeviceInfoList,
  deleteDevice,
  batchDeleteDevice,
  getDeviceDetailInfo
} from "./api";
import { getAllYardList, getSpecificbuildList } from "@/axios/commonApi";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";

export default {
  components: {
    singleBtnGroup,
    tableBtnGroup,
    tablePagination,
    tableComponent,
    cancelBtn,
    confirmBtn
  },
  data() {
    return {
      name: "",
      type: "",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      selectData: [],
      tableData: [],
      dialogTitle: "",
      operateType: "add",
      yardOptions: [],
      buildOptions: [],
      Form: {
        buildIdList: [],
        equipmentBrand: '',
        equipmentCharge: '',
        equipmentCode: '',
        equipmentContact: '',
        equipmentId: null,
        equipmentName: '',
        equipmentNum: null,
        equipmentRemark: '',
        equipmentTime: null,
        equipmentType: '',
        yardId: null,
        idList: []
      },
      dialogVisible: false,
      rules: {
        equipmentName: [
          { required: true, message: "设备名称不能为空", trigger: "blur" }
        ],
        equipmentNum: [
          { required: true, message: "设备数量不能为空", trigger: "blur" }
        ],
        equipmentCharge: [
          { required: true, message: "负责人不能为空", trigger: "blur" }
        ],
        equipmentContact: [
          { required: true, message: "联系方式不能为空", trigger: "blur" }
        ],
        yardId: [
          { required: true, message: "设备所属院落不能为空", trigger: "change" }
        ],
        buildIdList: [
          { required: true, message: "设备所属楼座不能为空", trigger: "change" }
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
    // 获取设备信息列表
    getList() {
      this.loadingStart('.table-box');
      getDeviceInfoList(this.currentPage, this.pageSize)
        .then(res => {
          this.loadingEnd();
          this.tableData = res.records;
          this.total = res.total;
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '设备信息列表获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 新增
    addDevice() {
      this.dialogTitle = "新增设备";
      this.operateType = "add";
      this.dialogVisible = true;
      this.getYardList();
    },
    // 单个删除
    singleDetle(row) {
      this.$confirm(`此操作将永久删除类别为${row.equipmentName}的设备, 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteDevice(parseInt(row.equipmentId))
          .then(res => {
            this.$message({
              message: "删除设备信息成功!",
              type: "success"
            })
            this.currentPage = 1
            this.getList()
          }).catch(err => {
            const msg = err.result ? err.result : '删除设备信息失败';
            this.$message({
              message: msg,
              type: "error"
            })
          })
      }).catch(() => {
        this.$message({
          type: "info",
          message: "已取消删除"
        })
      })
    },
    // 批量删除
    batchDelete() {
      let deleteArray = [];
      this.selectData.forEach((item, index) => {
        deleteArray.push(item.equipmentId);
      });
      this.$confirm("此操作将永久删除这些设备信息, 是否继续?", "提示", {
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
          batchDeleteDevice(deleteArray)
            .then(res => {
              this.$message({
                message: "批量删除设备信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : '批量删除设备信息失败';
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
      this.dialogTitle = "修改设备";
      this.operateType = "edit";
      this.dialogVisible = true;
      this.getYardList();
      getDeviceDetailInfo(row.equipmentId).then(res => {
        for (var key in this.Form) {
          this.Form[key] = res[key] ? res[key] : '';
        }
        if (this.Form.yardId) {
          this.yardSelectChangeHandle(this.Form.yardId, true)
        }
      }).catch(err => {
        const msg = err.result ? err.result : '设备详情获取失败';
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
          if (this.operateType === 'add') {
            addDevice(this.Form)
              .then(res => {
                this.$message({
                  message: `新增设备成功`,
                  type: "success"
                });
                this.dialogVisible = false;
                this.getList();
              })
              .catch(err => {
                const msg = err.result ? err.result : '新增设备失败';
                this.$message({
                  message: msg,
                  type: "error"
                });
              });
          } else if (this.operateType === 'edit') {
            updateDevice(this.Form)
              .then(res => {
                this.$message({
                  message: `修改设备成功`,
                  type: "success"
                });
                this.dialogVisible = false;
                this.getList();
              })
              .catch(err => {
                const msg = err.result ? err.result : '修改设备失败';
                this.$message({
                  message: msg,
                  type: "error"
                });
              });
          }
        } else {
          return false;
        }
      });
    },
    // 取消
    cancel() {
      this.dialogVisible = false;
    },
    // dialog打开的回调
    dialogOpen() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
      })
    },
    // dialog关闭的回调
    dialogClosed() {
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
      this.yardOptions = [];
      this.buildOptions = [];
    },
    // 获取院落列表
    getYardList() {
      getAllYardList(1)
        .then(res => {
          this.yardOptions = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : '院落下拉选项获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 选择院落
    yardSelectChangeHandle(val, flag) {
      if (!flag || flag === undefined) {
        this.Form.buildIdList = [];
      }
      getSpecificbuildList(val).then(res => {
        this.buildOptions = res;
      }).catch(err => {
        const msg = err.result ? err.result : '楼座下拉选项获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 只能输入正整数
    limitInt(key) {
      this.Form[key] = this.Form[key].replace(/[^0-9]/g, "");
    },
    // 获取数据类型
    getDataType(obj) {
      var typeArr = ["String", "Object", "Number", "Array", "Date"];
      for (let name of typeArr) {
        if (Object.prototype.toString.call(obj) === `[object ${name}]`) {
          return name;
        }
      }
    }
  },
  mounted() {
    this.getList();
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - operateBoxH;
    }, 100);
  }
};
</script>

<style>
#assetDevice .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#assetDevice .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#assetDevice .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#assetDevice .select-item .el-input__inner {
  height: 32px !important;
  line-height: 32px !important;
  width: 200px;
}
</style>



