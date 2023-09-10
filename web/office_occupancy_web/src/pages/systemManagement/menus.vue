<template>
  <div id="menus">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">菜单名称：</div>
          <div class="query-input">
            <el-input v-model="menuTitle" placeholder="请输入菜单名称" v-enter-trim></el-input>
          </div>
        </div>
        <div class="query-btn">
          <search-btn @clickBtn="search"></search-btn>
          <reset-btn @clickBtn="reset"></reset-btn>
        </div>
      </div>
    </div>
    <div class="table-box">
      <table-component :tableData="tableData" :tableHeight="tableHeight" rowKey="menuId">
        <el-table-column :show-overflow-tooltip="true" prop="menuTitle" label="菜单名称" align="left"></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="menuUrl" label="菜单链接" align="center"></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="menuKey" label="菜单编码" align="center"></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="menuLevel" label="菜单级别" align="center"></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="state" label="是否冻结" align="center"></el-table-column>
        <el-table-column label="操作" width="240" align="center">
          <template slot-scope="scope">
            <single-btn-group
              type="menu"
              :menuPid="scope.row.menuPid"
              @addClick="tableAdd(scope.row.menuId)"
              @updateClick="tableEdit(scope.row.menuId)"
              @deleteClick="tableDelete(scope.row)"
              @detailClick="tableDetail(scope.row.menuId)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group :batchDelBtnShow="false" @addClick="addMenu"></table-btn-group>
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
        <el-form-item
          :label="operateType==='detail'? '菜单名称:' : '菜单名称'"
          label-width="120px"
          prop="menuTitle"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.menuTitle"
            size="small"
            placeholder="请输入菜单名称"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '菜单序号:' : '菜单序号'"
          label-width="120px"
          prop="menuOrder"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.menuOrder"
            size="small"
            placeholder="请输入菜单序号"
            :readonly="operateType==='detail'"
            @keyup.native="limitInt('menuOrder')"
            @blur="limitInt('menuOrder')"
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '菜单链接:' : '菜单链接'"
          label-width="120px"
          prop="menuUrl"
          :class="{'detail-form-item' : operateType==='detail'}"
          v-if="Form.menuPid !== 0"
        >
          <el-input
            v-model="Form.menuUrl"
            size="small"
            placeholder="请输入菜单链接"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '菜单编码:' : '菜单编码'"
          label-width="120px"
          prop="menuKey"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.menuKey"
            size="small"
            placeholder="请输入菜单编码"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '是否冻结:' : '是否冻结'"
          label-width="120px"
          prop="isFreeze"
          class="isFreeze-item"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-radio
            v-model="Form.isFreeze"
            :label="1"
            name="isFreeze"
            v-show="!(operateType==='detail')"
          >是</el-radio>
          <el-radio
            v-model="Form.isFreeze"
            :label="0"
            name="isFreeze"
            v-show="!(operateType==='detail')"
          >否</el-radio>
          <span
            v-show="operateType==='detail'"
            class="detail-isFreeze-text"
          >{{Form.isFreeze===1? '冻结':'非冻结'}}</span>
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
import { BASE_URL, url_name_1 } from "@/axios/global"
import { uploadUrl } from "@/axios/commonApi";
import {
  getMenuInfoList,
  getMenuInfoList2,
  menuNameUnique,
  addMenu,
  updateMenu,
  findOneMenuInfo,
  deleteMenu
} from "./api";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup_2";
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
    tableComponent,
    tablePagination,
    cancelBtn,
    confirmBtn,
    searchBtn,
    resetBtn
  },
  data() {
    const validator_1 = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error('菜单名称不能为空'));
      } else {
        menuNameUnique(this.Form.menuTitle, this.Form.menuId).then(res => {
          callback();
        }).catch(err => {
          callback(new Error('菜单名称不能重复'));
        })
      }
    };
    return {
      menuTitle: "",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      tableHeight: 0,
      tableData: [],
      dialogTitle: "",
      operateType: "add",
      dialogVisible: false,
      loading: null,
      Form: {
        menuId: null,
        menuTitle: '',
        menuOrder: '',
        menuUrl: '',
        menuPid: '',
        menuKey: '',
        isFreeze: 0
      },
      rules: {
        menuTitle: [
          { required: true, validator: validator_1, trigger: "blur" }
        ],
        menuOrder: [
          { required: true, message: "菜单序号不能为空", trigger: "blur" }
        ],
        menuUrl: [
          { required: true, message: "菜单链接不能为空", trigger: "blur" }
        ],
        menuKey: [
          { required: true, message: "菜单编码不能为空", trigger: "blur" }
        ],
        isFreeze: [
          { required: true, message: "是否冻结不能为空", trigger: "change" }
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
    // 获取单位信息列表
    getList() {
      this.loadingStart('.table-box');
      if (this.menuTitle) {
        getMenuInfoList2(this.currentPage, this.pageSize, this.menuTitle)
          .then(res => {
            if (res) {
              this.tableData = res.records;
              this.tableData.forEach(item => {
                if (item.menuPid === 0) {
                  item.menuLevel = '一级菜单';
                  item.menuUrl = '-'
                } else {
                  item.menuLevel = '二级菜单';
                }
                if (item.isFreeze === 0) {
                  item.state = '非冻结';
                } else {
                  item.state = '冻结';
                }
              })
              this.total = res.total;
              this.$nextTick(() => {
                eventBus.$emit('resetTable', this.tableHeight);
              });
              this.loadingEnd();
            }
          })
          .catch(err => {
            const msg = err.result ? err.result : '获取菜单信息失败';
            this.$message({
              message: msg,
              type: "error"
            });
            this.loadingEnd();
          })
      } else if (this.menuTitle === '') {
        getMenuInfoList(this.currentPage, this.pageSize)
          .then(res => {
            if (res) {
              this.tableData = res.records;
              this.tableData.forEach(item => {
                item.menuLevel = '一级菜单';
                item.menuUrl = '-'
                if (item.isFreeze === 0) {
                  item.state = '非冻结';
                } else {
                  item.state = '冻结';
                }
                if (item.children && item.children.length > 0) {
                  item.children.forEach(ele => {
                    ele.menuLevel = '二级菜单';
                    if (ele.isFreeze === 0) {
                      ele.state = '非冻结';
                    } else {
                      ele.state = '冻结';
                    }
                  })
                }
              })
              this.total = res.total;
              this.$nextTick(() => {
                eventBus.$emit('resetTable', this.tableHeight);
              });
              this.loadingEnd();
            }
          })
          .catch(err => {
            const msg = err.result ? err.result : '获取菜单信息失败';
            this.$message({
              message: msg,
              type: "error"
            });
            this.loadingEnd();
          });
      }
    },
    // 查询单位信息
    search() {
      this.currentPage = 1;
      this.getList();
    },
    reset() {
      this.currentPage = 1;
      this.menuTitle = '';
      this.search();
    },
    // 新增一级菜单
    addMenu() {
      this.dialogVisible = true;
      this.dialogTitle = "新增菜单";
      this.operateType = "add";
      this.Form.menuPid = 0;
    },
    // 新增二级菜单
    tableAdd(id) {
      this.dialogVisible = true;
      this.dialogTitle = "新增菜单";
      this.operateType = "add";
      this.Form.menuPid = parseInt(id);
    },
    // 单个删除
    tableDelete(row) {
      this.$confirm(`此操作将永久删除菜单${row.menuTitle}, 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteMenu(parseInt(row.menuId))
            .then(res => {
              this.$message({
                message: "删除菜单信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : '删除菜单信息失败!';
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
    tableEdit(id) {
      this.dialogVisible = true;
      this.dialogTitle = "修改菜单";
      this.operateType = "edit";
      findOneMenuInfo(id).then(res => {
        Object.keys(res).map(item => {
          this.Form[item] = res[item]
        })
      }).catch(err => {
        const msg = err.result ? err.result : '菜单信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查看详情
    tableDetail(id) {
      this.dialogVisible = true;
      this.dialogTitle = "菜单详情";
      this.operateType = "detail";
      findOneMenuInfo(id).then(res => {
        Object.keys(res).map(item => {
          this.Form[item] = res[item]
        })
      }).catch(err => {
        const msg = err.result ? err.result : '菜单信息获取失败';
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
          this.saveForm();
        } else {
          return false;
        }
      });
    },
    // 保存表单
    saveForm() {
      let msg = this.operateType === "add" ? "新增" : "修改";
      if (this.operateType === "add") {
        addMenu(this.Form)
          .then(res => {
            this.$message({
              message: `${msg}菜单成功`,
              type: "success"
            });
            this.dialogVisible = false;
            this.getList();
          })
          .catch(err => {
            const msg = err.result ? err.result : `${msg}菜单失败`;
            this.$message({
              message: msg,
              type: "error"
            });
          });
      } else {
        updateMenu(this.Form)
          .then(res => {
            this.$message({
              message: `${msg}菜单成功`,
              type: "success"
            });
            this.dialogVisible = false;
            this.getList();
          })
          .catch(err => {
            const msg = err.result ? err.result : `${msg}菜单失败`;
            this.$message({
              message: msg,
              type: "error"
            });
          });
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
    // dialog打开的回调
    dialogOpen() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
      })
    },
    // dialog关闭的回调
    dialogClosed() {
      this.clearForm(this.Form);
      this.Form.menuPid = 0;
      this.Form.isFreeze = 0;
    },
    // 获取数据类型
    getDataType(obj) {
      var typeArr = ["String", "Object", "Number", "Array", "Date"];
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
#menus .query-box {
  margin-bottom: 20px;
}
#menus .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#menus .query-item > div {
  float: left;
}
#menus .query-item > .query-input {
  width: calc(100% - 80px);
}
#menus .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#menus .query-btn {
  float: left;
  margin-left: 50px;
}
#menus .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#menus .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#menus .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#menus .el-radio + .el-radio {
  margin-left: 20px;
}
#menus .detail-form-item .el-input__inner,
#menus .detail-form-item .el-textarea__inner {
  border: 0;
  padding-left: 0;
}
#menus .isFreeze-item .el-form-item__content {
  width: 200px;
}
</style>


