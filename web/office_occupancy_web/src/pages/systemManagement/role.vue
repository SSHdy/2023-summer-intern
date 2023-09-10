<template>
  <div id="role">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">角色名称：</div>
          <div class="query-input">
            <el-input v-model="roleName" placeholder="请输入角色名称"></el-input>
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
        <el-table-column prop="serialNum" label="序号" width="50" align="center"></el-table-column>
        <el-table-column prop="roleName" label="角色名称" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column
          prop="FreezeStatus"
          label="是否冻结"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <!-- <el-table-column
          prop="subscibeStatus"
          label="是否订阅消息"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>-->
        <el-table-column label="操作" width="190" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDetail="true"
              @deleteClick="singleDetle(scope.row)"
              @updateClick="singleUpdate(scope.row)"
              @detailClick="singleDetail(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group :batchDelBtnShow="false" @addClick="add"></table-btn-group>
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
          :label="operateType==='detail'? '角色名称:' : '角色名称'"
          label-width="120px"
          prop="roleName"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.roleName"
            placeholder="请输入角色名称"
            :readonly="operateType==='detail'"
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '是否冻结:' : '是否冻结'"
          label-width="120px"
          prop="isFreeze"
          class="freeze-box"
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
          <span v-show="operateType==='detail'">{{Form.isFreeze===1? '冻结':'非冻结'}}</span>
        </el-form-item>
        <!-- <el-form-item
          :label="operateType==='detail'? '是否订阅消息:' : '是否订阅消息'"
          label-width="120px"
          prop="isSubscibe"
          :style="operateType==='detail' ? 'display:block' : 'display:inline-block'"
        >
          <el-radio
            v-model="Form.isSubscibe"
            :label="0"
            name="isSubscibe"
            v-show="!(operateType==='detail')"
          >是</el-radio>
          <el-radio
            v-model="Form.isSubscibe"
            :label="1"
            name="isSubscibe"
            v-show="!(operateType==='detail')"
          >否</el-radio>
          <span v-show="operateType==='detail'">{{Form.isSubscibe===0? '已订阅':'未订阅'}}</span>
        </el-form-item>-->
        <el-form-item
          :label="operateType==='detail'? '已选单位:' : '选择单位'"
          label-width="120px"
          prop="unitIdList"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <div class="menuTransfer clearfix">
            <div
              class="transfer-panel"
              style="margin-right:20px;"
              v-show="!(operateType==='detail')"
            >
              <div class="transfer-panel-header">全部单位</div>
              <div class="transfer-panel-body">
                <el-tree
                  ref="unitTree"
                  :props="unitProps"
                  show-checkbox
                  :data="unitTreeDate"
                  node-key="unitId"
                  v-if="treeShow"
                ></el-tree>
              </div>
            </div>
            <div style="padding-top: 100px" v-show="!(operateType==='detail')">
              <el-button
                type="primary"
                icon="el-icon-caret-right"
                circle
                @click="getCheckedKeys('unit')"
              ></el-button>
            </div>
            <div class="transfer-panel" style="margin-left:20px;">
              <div class="transfer-panel-header">已选单位</div>
              <div class="transfer-panel-body">
                <ul class="selected-menu">
                  <li v-for="item in selectUnitTreeDate" :key="item.unitId">
                    <el-tooltip class="item" effect="dark" :content="item.unitName" placement="top">
                      <span>{{item.unitName}}</span>
                    </el-tooltip>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '已选菜单:' : '选择菜单'"
          label-width="120px"
          prop="menuIdList"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <div class="menuTransfer clearfix">
            <div
              class="transfer-panel"
              style="margin-right:20px;"
              v-show="!(operateType==='detail')"
            >
              <div class="transfer-panel-header">全部菜单</div>
              <div class="transfer-panel-body">
                <el-tree
                  ref="menuTree"
                  :props="props"
                  show-checkbox
                  :data="menuTreeDate"
                  node-key="menuId"
                  v-if="treeShow"
                ></el-tree>
              </div>
            </div>
            <div style="padding-top: 100px" v-show="!(operateType==='detail')">
              <el-button
                type="primary"
                icon="el-icon-caret-right"
                circle
                @click="getCheckedKeys('menu')"
              ></el-button>
            </div>
            <div class="transfer-panel" style="margin-left:20px;">
              <div class="transfer-panel-header">已选菜单</div>
              <div class="transfer-panel-body">
                <ul class="selected-menu">
                  <li v-for="item in selectMenuTreeDate" :key="item.menuId">
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="item.menuTitle"
                      placement="top"
                    >
                      <span>{{item.menuTitle}}</span>
                    </el-tooltip>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '已选标题:' : '选择标题'"
          label-width="120px"
          prop="titroleBannerle"
          :style="operateType==='detail' ? 'display:block' : 'display:inline-block'"
          v-if="PRO_TYPE === 'yc'"
        >
          <el-radio-group v-model="Form.roleBanner" v-show="!(operateType==='detail')">
            <div class="title-item">
              <el-radio :label="0">
                <img :src="bannner_1" alt="title-banner" />
              </el-radio>
            </div>
            <div class="title-item">
              <el-radio :label="1">
                <img :src="bannner_2" alt="title-banner" />
              </el-radio>
            </div>
          </el-radio-group>
          <div v-show="operateType==='detail'" class="bannner-detail">
            <img :src="bannnerDetail" alt="title-banner" />
          </div>
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
import uuidv1 from 'uuid/v1';
import { BASE_URL, url_name_1, PRO_TYPE } from "@/axios/global"
import {
  getRoleInfoList,
  addRole,
  updateRole,
  roleNameUnique,
  deleteRole,
  batchDeleteRole,
  getMenuInfoList,
  findOneRoleInfo,
  findOneRoleInfo_2,
  getMenuInfoList3,
  getInfinityUnitTree
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
    const validator_1 = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error('角色名称不能为空'));
      } else {
        roleNameUnique(this.Form.roleName, this.Form.roleId).then(res => {
          callback();
        }).catch(err => {
          callback(new Error('角色名称不能重复'));
        })
      }
    };
    return {
      PRO_TYPE: PRO_TYPE,
      roleName: "",
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
      unitOptions: [],
      menuTreeDate: [],
      unitTreeDate: [],
      selectMenuTreeDate: [],
      selectUnitTreeDate: [],
      treeShow: false,
      Form: {
        roleId: "",
        roleName: "",
        isFreeze: 0,
        isSubscibe: 1,
        roleBanner: 0,
        menuId: [],
        unitId: []
      },
      props: {
        label: 'menuTitle',
        children: 'children'
      },
      unitProps: {
        children: 'childrens',
        label: 'unitName'
      },
      bannner_1: `${BASE_URL}${url_name_1}/images/inside.png`,
      bannner_2: `${BASE_URL}${url_name_1}/images/without.png`,
      bannnerDetail: '',
      rules: {
        roleName: [
          { required: true, validator: validator_1, trigger: "blur" }
        ],
        // isSubscibe: [
        //   { required: true, message: "请选择是否订阅消息", trigger: "change" }
        // ],
        isFreeze: [
          { required: true, message: "请选择是否冻结", trigger: "change" }
        ],
        unitId: [
          { required: true, message: "所属单位不能为空", trigger: "blur" }
        ],
        menuId: [
          { required: true, message: "菜单不能为空", trigger: "blur" }
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
      getRoleInfoList(this.currentPage, this.pageSize, this.roleName)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          // this.tableData.forEach(item => {
          //   if (item.isSubscibe === 0) {
          //     item.subscibeStatus = '已订阅';
          //   } else {
          //     item.subscibeStatus = '非订阅';
          //   }
          // })
          this.tableData.forEach((item, index) => {
            item.serialNum = index + 1;
            if (item.isFreeze === 0) {
              item.FreezeStatus = '非冻结';
            } else {
              item.FreezeStatus = '冻结';
            }
          })
          this.loadingEnd();
        })
        .catch(err => {
          const msg = err.result ? err.result : '角色信息获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 查询房屋信息
    search() {
      this.currentPage = 1;
      this.getList();
    },
    reset() {
      this.currentPage = 1;
      this.roleName = '';
      this.search();
    },
    // 新增
    add() {
      this.dialogVisible = true;
      this.dialogTitle = "新增角色信息";
      this.operateType = "add";
      this.treeShow = true;
    },
    // 单个删除
    singleDetle(row) {
      this.$confirm(`此操作将永久删除角色${row.roleName}, 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteRole(parseInt(row.roleId))
            .then(res => {
              this.$message({
                message: "删除角色信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : '删除角色信息失败!';
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
      this.dialogTitle = "修改角色信息";
      this.operateType = "edit";
      this.treeShow = true;
      this.loadingStart('.el-dialog');
      findOneRoleInfo(row.roleId).then(res => {
        this.Form.roleBanner = res.roleBanner;
        this.Form.isFreeze = res.isFreeze;
        // this.Form.isSubscibe = res.isSubscibe;
        this.Form.roleId = res.roleId;
        this.Form.roleName = res.roleName;
        this.selectMenuTreeDate = res.menuDetail;
        this.selectUnitTreeDate = res.unitList;
        if (res.menuDetail && res.menuDetail.length > 0) {
          res.menuDetail.forEach(item => {
            this.Form.menuId.push(item.menuId);
          });
        }
        if (res.unitList && res.unitList.length > 0) {
          res.unitList.forEach(item => {
            this.Form.unitId.push(item.unitId);
          });
        }
        this.$refs.unitTree.setCheckedKeys(this.Form.unitId);
        findOneRoleInfo_2(row.roleId).then(res_2 => {
          const tempArr = [];
          if (res_2.menuDetail && res_2.menuDetail.length > 0) {
            res_2.menuDetail.forEach(item_2 => {
              tempArr.push(item_2.menuId);
            });
            this.$refs.menuTree.setCheckedKeys(tempArr);
          }
          this.loadingEnd();
        }).catch(err => {
          const msg = err.result ? err.result : '角色菜单信息获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        })
      }).catch(err => {
        const msg = err.result ? err.result : '角色信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 单个详情
    singleDetail(row) {
      this.dialogVisible = true;
      this.dialogTitle = "角色详情";
      this.operateType = "detail";
      findOneRoleInfo(row.roleId).then(res => {
        this.Form.isFreeze = res.isFreeze;
        // this.Form.isSubscibe = res.isSubscibe;
        this.Form.roleId = res.roleId;
        this.Form.roleName = res.roleName;
        this.selectMenuTreeDate = res.menuDetail;
        this.selectUnitTreeDate = res.unitList;
        if (res.roleBanner === 0) {
          this.bannnerDetail = this.bannner_1;
        } else {
          this.bannnerDetail = this.bannner_2;
        }
      }).catch(err => {
        const msg = err.result ? err.result : '角色详情获取失败';
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
          if (this.operateType === 'add') {
            addRole(this.Form)
              .then(res => {
                this.$message({
                  message: `新增角色信息成功`,
                  type: "success"
                });
                this.dialogVisible = false;
                this.getList();
              })
              .catch(err => {
                const msg = err.result ? err.result : '新增角色信息失败';
                this.$message({
                  message: msg,
                  type: "error"
                });
              });
          } else {
            updateRole(this.Form)
              .then(res => {
                this.$message({
                  message: `修改角色信息成功`,
                  type: "success"
                });
                this.dialogVisible = false;
                this.getList();
              })
              .catch(err => {
                const msg = err.result ? err.result : '修改角色信息失败';
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
      this.selectMenuTreeDate = [];
      this.selectUnitTreeDate = [];
      this.treeShow = false;
      this.Form.isFreeze = 0;
      this.Form.isSubscibe = 1;
      this.bannnerDetail = '';
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
    // 树checked事件
    getCheckedKeys(type) {
      if (type === 'menu') {
        this.Form.menuId = [];
        this.selectMenuTreeDate = [];
        const key_a = this.$refs.menuTree.getCheckedKeys();
        const key_b = this.$refs.menuTree.getHalfCheckedKeys();
        this.Form.menuId = key_b.concat(key_a);
        const node_a = this.$refs.menuTree.getCheckedNodes();
        const node_b = this.$refs.menuTree.getHalfCheckedNodes();
        this.selectMenuTreeDate = node_b.concat(node_a);
      } else {
        this.Form.unitId = [];
        this.selectUnitTreeDate = [];
        const arr_data = this.$refs.unitTree.getCheckedNodes();
        arr_data.forEach(item => {
          if (item.unitClassify === 1 || item.unitClassify === 2) {
            this.Form.unitId.push(item.unitId);
            this.selectUnitTreeDate.push(item);
          }
        })
      }
    },
    // 获取菜单树
    getMenuTree() {
      getMenuInfoList3()
        .then(res => {
          this.menuTreeDate = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : '获取菜单信息失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取单位树
    getUnitTree() {
      getInfinityUnitTree().then(res => {
        this.unitTreeDate = res;
      }).catch(err => {
        const msg = err.result ? err.result : '单位信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    }
  },
  mounted() {
    this.getList();
    this.getMenuTree();
    this.getUnitTree();
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
#role .query-box {
  margin-bottom: 20px;
}
#role .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#role .query-item > div {
  float: left;
}
#role .query-item > .query-input {
  width: calc(100% - 80px);
}
#role .query-item .el-date-editor.el-input,
#role .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#role .query-btn {
  float: left;
  margin-left: 50px;
}
#role .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#role .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#role .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#role .transfer-panel {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  display: inline-block;
  vertical-align: middle;
  width: 200px;
  max-height: 100%;
  box-sizing: border-box;
  position: relative;
}
#role .transfer-panel-header {
  height: 40px;
  line-height: 40px;
  background: #f5f7fa;
  margin: 0;
  padding-left: 15px;
  border-bottom: 1px solid #ebeef5;
  box-sizing: border-box;
  color: #000;
}
#role .transfer-panel-body {
  height: 246px;
}
#role .menuTransfer > div {
  float: left;
}
#role .freeze-box .el-form-item__content {
  width: 200px;
}
#role .selected-menu {
  height: 100%;
  overflow: auto;
}
#role .selected-menu > li {
  line-height: 26px;
  padding-left: 15px;
  line-height: 26px;
  padding-left: 15px;
  width: 100%;
  box-sizing: border-box;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  cursor: pointer;
}
#role .el-tree {
  height: 100%;
  overflow: auto;
}
#role .detail-form-item .el-input__inner {
  border: 0;
  padding-left: 0;
}
#role .detail-form-item .transfer-panel {
  margin-left: 0 !important;
}
#role .title-item {
  margin-bottom: 10px;
}
#role .title-item label {
  position: relative;
  top: -7px;
}
#role .title-item img {
  width: 460px;
  height: 32px;
  position: relative;
  top: 10px;
}
#role .bannner-detail > img {
  width: 540px;
}
</style>


