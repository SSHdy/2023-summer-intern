<template>
  <div id="user">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">用户姓名：</div>
          <div class="query-input">
            <el-input v-model="name" placeholder="请输入用户姓名" v-enter-trim></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">登录名：</div>
          <div class="query-input">
            <el-input v-model="username" placeholder="请输入登录名" v-enter-trim></el-input>
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
        <el-table-column prop="name" label="用户姓名" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="username" label="登录名" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="sexName" label="性别" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column
          prop="FreezeStatus"
          label="是否冻结"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
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
        <el-form-item
          :label="operateType==='detail'? '用户姓名:' : '用户姓名'"
          label-width="120px"
          prop="name"
        >
          <el-input
            v-model="Form.name"
            placeholder="请输入用户姓名"
            v-if="operateType!=='detail'"
            v-enter-trim
          ></el-input>
          <span v-if="operateType==='detail'">{{Form.name}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '登录名:' : '登录名'"
          label-width="120px"
          prop="username"
        >
          <el-input
            v-model="Form.username"
            placeholder="请输入登录名"
            v-if="operateType!=='detail'"
            v-enter-trim
          ></el-input>
          <span v-if="operateType==='detail'">{{Form.username}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '手机号:' : '手机号'"
          label-width="120px"
          prop="phoneNum"
        >
          <el-input
            v-model="Form.phoneNum"
            placeholder="手机号"
            v-if="operateType!=='detail'"
            @keyup.native="limitInt('phoneNum')"
            @blur="limitInt('phoneNum')"
          ></el-input>
          <span v-if="operateType==='detail'">{{Form.phoneNum}}</span>
        </el-form-item>
        <el-form-item :label="operateType==='detail'? '性别:' : '性别'" label-width="120px" prop="sex">
          <el-radio v-model="Form.sex" :label="0" name="sex" v-if="!(operateType==='detail')">男</el-radio>
          <el-radio v-model="Form.sex" :label="1" name="sex" v-if="!(operateType==='detail')">女</el-radio>
          <span v-if="operateType==='detail'">{{Form.sex===0? '男':'女'}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '是否冻结:' : '是否冻结'"
          label-width="120px"
          prop="isFreeze"
        >
          <el-radio
            v-model="Form.isFreeze"
            :label="1"
            name="isFreeze"
            v-if="!(operateType==='detail')"
          >是</el-radio>
          <el-radio
            v-model="Form.isFreeze"
            :label="0"
            name="isFreeze"
            v-if="!(operateType==='detail')"
          >否</el-radio>
          <span v-if="operateType==='detail'">{{Form.isFreeze===1? '冻结':'非冻结'}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '所属单位:' : '所属单位'"
          label-width="120px"
          class="longer"
          prop="unitId"
        >
          <el-select
            v-model="Form.unitId"
            filterable
            placeholder="请选择"
            v-if="operateType!=='detail'"
            style="width:535px;"
          >
            <el-option
              v-for="item in unitOpts"
              :key="item.unitId"
              :label="item.unitName"
              :value="item.unitId"
            ></el-option>
          </el-select>
          <span v-if="operateType==='detail'" style="display:block; width:100%;">{{unitSeletedVal}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '用户角色:' : '用户角色'"
          label-width="120px"
          class="longer"
          prop="roleIdList"
        >
          <el-select
            v-model="Form.roleIdList"
            multiple
            filterable
            placeholder="请选择"
            v-if="operateType!=='detail'"
            style="width:535px;"
          >
            <el-option
              v-for="item in roleOptions"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId"
            ></el-option>
          </el-select>
          <span v-if="operateType==='detail'" style="display:block; width:100%;">{{roleIdListVal}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '维修流程:' : '维修流程'"
          label-width="120px"
          class="longer"
          v-if="showRepair"
        >
          <el-select
            v-model="Form.repairIdList"
            multiple
            filterable
            placeholder="请选择"
            v-if="operateType!=='detail'"
            style="width:535px;"
          >
            <el-option
              v-for="item in repairOpts"
              :key="item.value"
              :label="item.key"
              :value="item.value"
            ></el-option>
          </el-select>
          <span
            v-if="operateType==='detail'"
            style="display:block; width:100%;"
          >{{repairSeletedVal}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '处置流程:' : '处置流程'"
          label-width="120px"
          class="longer"
          v-if="showDispose"
        >
          <el-select
            v-model="Form.handleIdList"
            multiple
            filterable
            placeholder="请选择"
            v-if="operateType!=='detail'"
            style="width:535px;"
          >
            <el-option
              v-for="item in handleOpts"
              :key="item.value"
              :label="item.key"
              :value="item.value"
            ></el-option>
          </el-select>
          <span
            v-if="operateType==='detail'"
            style="display:block; width:100%;"
          >{{handleSeletedVal}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '配置流程:' : '配置流程'"
          label-width="120px"
          class="longer"
          v-if="showConfig"
        >
          <el-select
            v-model="Form.configIdList"
            multiple
            filterable
            placeholder="请选择"
            v-if="operateType!=='detail'"
            style="width:535px;"
          >
            <el-option
              v-for="item in configOpts"
              :key="item.value"
              :label="item.key"
              :value="item.value"
            ></el-option>
          </el-select>
          <span
            v-if="operateType==='detail'"
            style="display:block; width:100%;"
          >{{configSeletedVal}}</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" v-if="!(operateType==='detail')">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getUserInfoList,
  addUser,
  updateUser,
  userNameUnique,
  deleteUser,
  batchDeleteUser,
  findOneUserInfo,
  getRoleInfoList_2,
  getRepairList,
  gethandleList,
  getConfigList,
  uniquePhoneName
} from "./api";
import { getAllUnitList, getRouterList } from "@/axios/commonApi";
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
      const reg = /^[\u4e00-\u9fa5]+$/;
      if (value === '' || value === null) {
        callback(new Error('登录名不能为空'));
      } else if (reg.test(value)) {
        callback(new Error('登录名只能是英文，数字或符号'));
      } else {
        userNameUnique(this.Form.username, this.Form.userId).then(res => {
          callback();
        }).catch(err => {
          callback(new Error('登录名不能重复'));
        })
      }
    };
    const validator_2 = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error('手机号不能为空'));
      } else if (!(/^1[3456789]\d{9}$/.test(value))) {
        callback(new Error('手机号有误'));
      } else {
        uniquePhoneName(value, this.Form.userId).then(res => {
          callback();
        }).catch(err => {
          callback(new Error('手机号不能重复'));
        })
      }
    };
    return {
      name: "",
      username: "",
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
      roleOptions: [],
      repairOpts: [],
      handleOpts: [],
      configOpts: [],
      unitOpts: [],
      oldRepairIdList: [],
      oldRoleIdList: [],
      oldHanleList: [],
      oldConfigList: [],
      unitSeletedVal: '',
      roleIdListVal: '',
      repairSeletedVal: '暂无',
      handleSeletedVal: '暂无',
      configSeletedVal: '暂无',
      showRepair: false,
      showDispose: false,
      showConfig: false,
      Form: {
        userId: "",
        name: "",
        username: "",
        password: "",
        sex: 0,
        isFreeze: 0,
        unitId: '',
        roleIdList: [],
        repairIdList: [],
        handleIdList: [],
        configIdList: [],
        phoneNum: ''
      },
      rules: {
        username: [
          { required: true, validator: validator_1, trigger: "blur" }
        ],
        name: [
          { required: true, message: "用户姓名不能为空", trigger: "blur" }
        ],
        sex: [
          { required: true, message: "请选择性别", trigger: "change" }
        ],
        isFreeze: [
          { required: true, message: "请选择是否冻结", trigger: "change" }
        ],
        unitId: [
          { required: true, message: "请选择所属单位", trigger: "change" }
        ],
        roleIdList: [
          { required: true, message: "请选择用户角色", trigger: "change" }
        ],
        phoneNum: [
          { required: true, validator: validator_2, trigger: "blur" }
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
      getUserInfoList(this.currentPage, this.pageSize, this.name, this.username)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach(item => {
            if (item.isFreeze === 0) {
              item.FreezeStatus = '非冻结';
            } else {
              item.FreezeStatus = '冻结';
            }
            if (item.sex === 0) {
              item.sexName = '男';
            } else {
              item.sexName = '女';
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
      this.name = '';
      this.username = '';
      this.search();
    },
    // 新增
    add() {
      this.dialogVisible = true;
      this.dialogTitle = "新增用户信息";
      this.operateType = "add";
      this.Form.password = '123456';
    },
    // 单个删除
    singleDetle(row) {
      this.$confirm(`此操作将永久删除用户${row.name}, 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteUser(parseInt(row.userId))
            .then(res => {
              this.$message({
                message: "删除用户信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : '删除用户信息失败';
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
        deleteArray.push(item.userId);
      });
      this.$confirm("此操作将永久删除这些用户信息, 是否继续?", "提示", {
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
          batchDeleteUser(deleteArray)
            .then(res => {
              this.$message({
                message: "批量删除用户信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : '批量删除用户信息失败';
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
      this.dialogTitle = "修改用户信息";
      this.operateType = "edit";
      findOneUserInfo(row.userId).then(res => {
        this.Form.userId = res.user.userId;
        this.Form.isFreeze = res.user.isFreeze;
        this.Form.name = res.user.name;
        this.Form.sex = res.user.sex;
        this.Form.username = res.user.username;
        this.Form.unitId = res.user.unitId;
        this.Form.phoneNum = res.user.phoneNum;
        this.Form.repairIdList = res.repairIdList || [];
        this.Form.handleIdList = res.handleIdList || [];
        this.Form.configIdList = res.configIdList || [];
        if (res.roleList && res.roleList.length > 0) {
          res.roleList.forEach(item => {
            this.Form.roleIdList.push(item.roleId);
          })
        }
        this.oldRepairIdList = JSON.parse(JSON.stringify(this.Form.repairIdList)) || [];
        this.oldHanleList = JSON.parse(JSON.stringify(this.Form.handleIdList)) || [];
        this.oldConfigList = JSON.parse(JSON.stringify(this.Form.configIdList)) || [];
        this.oldRoleIdList = this.Form.roleIdList;
      }).catch(err => {
        const msg = err.result ? err.result : '用户信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 单个详情
    singleDetail(row) {
      this.dialogVisible = true;
      this.dialogTitle = "用户详情";
      this.operateType = "detail";
      let repairIdList = [];
      let roleIdList = [];
      let handleIdList = [];
      let configIdList = [];
      findOneUserInfo(row.userId).then(res => {
        this.Form.userId = res.user.userId;
        this.Form.isFreeze = res.user.isFreeze;
        this.Form.name = res.user.name;
        this.Form.sex = res.user.sex;
        this.Form.username = res.user.username;
        this.Form.userRoleIdList = res.userRoleIdList
        this.Form.phoneNum = res.user.phoneNum;
        res.roleList.forEach(item => {
          roleIdList.push(item.roleName);
        })
        this.roleIdListVal = roleIdList.join('、');
        if (res.repairIdList && res.repairIdList.length > 0) {
          res.repairIdList.forEach(item => {
            this.repairOpts.forEach(opt => {
              if (opt.value === item) {
                repairIdList.push(opt.key);
              }
            })
          })
          this.repairSeletedVal = repairIdList.join('、');
        }
        if (res.handleIdList && res.handleIdList.length > 0) {
          res.handleIdList.forEach(item => {
            this.handleOpts.forEach(opt => {
              if (opt.value === item) {
                handleIdList.push(opt.key);
              }
            })
          })
          this.handleSeletedVal = handleIdList.join('、');
        }
        if (res.configIdList && res.configIdList.length > 0) {
          res.configIdList.forEach(item => {
            this.configOpts.forEach(opt => {
              if (opt.value === item) {
                configIdList.push(opt.key);
              }
            })
          })
          this.configSeletedVal = configIdList.join('、');
        }
        this.unitOpts.forEach(item => {
          if (item.unitId === res.user.unitId) {
            this.unitSeletedVal = item.unitName;
          }
        })
      }).catch(err => {
        const msg = err.result ? err.result : '用户详情获取失败';
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
          if (this.operateType === 'edit') {
            this.Form.deleteRepairRight = [];
            this.Form.addRepairRight = [];
            this.Form.deleteRoleRight = [];
            this.Form.addRoleRight = [];
            this.Form.addHandleRight = [];
            this.Form.deleteHandleRight = [];
            this.Form.addConfigRight = [];
            this.Form.deleteConfigRight = [];
            this.oldRepairIdList.forEach(item => {
              if (this.Form.repairIdList.indexOf(item) === -1) {
                this.Form.deleteRepairRight.push(item);
              }
            });
            this.Form.repairIdList.forEach(item => {
              if (this.oldRepairIdList.indexOf(item) === -1) {
                this.Form.addRepairRight.push(item);
              }
            });
            this.oldHanleList.forEach(item => {
              if (this.Form.handleIdList.indexOf(item) === -1) {
                this.Form.deleteHandleRight.push(item);
              }
            });
            this.Form.handleIdList.forEach(item => {
              if (this.oldHanleList.indexOf(item) === -1) {
                this.Form.addHandleRight.push(item);
              }
            });
            this.oldConfigList.forEach(item => {
              if (this.Form.configIdList.indexOf(item) === -1) {
                this.Form.deleteConfigRight.push(item);
              }
            });
            this.Form.configIdList.forEach(item => {
              if (this.oldConfigList.indexOf(item) === -1) {
                this.Form.addConfigRight.push(item);
              }
            });
            this.oldRoleIdList.forEach(item => {
              if (this.Form.roleIdList.indexOf(item) === -1) {
                this.Form.deleteRoleRight.push(item);
              }
            });
            this.Form.roleIdList.forEach(item => {
              if (this.oldRoleIdList.indexOf(item) === -1) {
                this.Form.addRoleRight.push(item);
              }
            });
            delete this.Form.password;
          } else if (this.operateType === 'add') {
            this.Form.addRepairRight = this.Form.repairIdList;
            this.Form.addRoleRight = this.Form.roleIdList;
            this.Form.addHandleRight = this.Form.handleIdList;
          }
          if (this.operateType === 'add') {
            addUser(this.Form).then(res => {
              this.$message({
                message: `新增用户成功`,
                type: "success"
              });
              this.dialogVisible = false;
              this.getList();
            }).catch(err => {
              const msg = err.result ? err.result : '新增用户失败';
              this.$message({
                message: msg,
                type: "error"
              });
            })
          } else {
            updateUser(this.Form).then(res => {
              this.$message({
                message: `修改用户成功`,
                type: "success"
              });
              this.Form.password = '';
              this.dialogVisible = false;
              this.getList();
            }).catch(err => {
              const msg = err.result ? err.result : '修改用户失败';
              this.$message({
                message: msg,
                type: "error"
              });
              this.Form.password = '';
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
      this.clearForm(this.Form);
      this.Form.sex = 0;
      this.Form.isFreeze = 0;
      this.oldRepairIdList = [];
      this.oldRoleIdList = [];
      this.oldHanleList = [];
      this.oldConfigList = [];
      this.repairSeletedVal = '暂无';
      this.handleSeletedVal = '暂无';
      this.configSeletedVal = '暂无';
    },
    // 只能输入正整数
    limitInt(key) {
      this.Form[key] = this.Form[key].replace(/[^0-9]/g, "");
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
    getRepairList().then(res => {
      this.repairOpts = res;
    }).catch(err => {
      const msg = err.result ? err.result : '维修流程下拉选项获取失败';
      this.$message({
        message: msg,
        type: "error"
      });
    })
    getRoleInfoList_2().then(res => {
      this.roleOptions = res;
    }).catch(err => {
      const msg = err.result ? err.result : '用户角色下拉选项获取失败';
      this.$message({
        message: msg,
        type: "error"
      });
    })
    gethandleList().then(res => {
      this.handleOpts = res;
    }).catch(err => {
      const msg = err.result ? err.result : '处置流程下拉选项获取失败';
      this.$message({
        message: msg,
        type: "error"
      });
    })
    getConfigList().then(res => {
      this.configOpts = res;
    }).catch(err => {
      const msg = err.result ? err.result : '配置流程下拉选项获取失败';
      this.$message({
        message: msg,
        type: "error"
      });
    })
    getAllUnitList().then(res => {
      this.unitOpts = res
    }).catch(err => {
      const msg = err.result ? err.result : '所属单位下拉选项获取失败';
      this.$message({
        message: msg,
        type: "error"
      });
    })
    getRouterList().then(res => {
      for (let i = 0; i < res.length; i++) {
        const key = res[i].key;
        const children = res[i].children;
        if (key === 'repairManagement') {
          this.showRepair = true;
        }
        if (key === 'disposeManagement') {
          this.showDispose = true;
        }
        if (key === 'configManagement') {
          this.showConfig = true;
        }
      }
    }).catch(err => {
      const msg = err.result ? err.result : '路由信息获取失败！';
      this.$message({
        message: msg,
        type: "error"
      });
    });
  }
};
</script>

<style>
#user .query-box {
  margin-bottom: 20px;
}
#user .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#user .query-item > div {
  float: left;
}
#user .query-item > .query-input {
  width: calc(100% - 80px);
}
#user .query-item .el-date-editor.el-input,
#user .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#user .query-btn {
  float: left;
  margin-left: 50px;
}
#user .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#user .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#user .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#user .el-form-item__content .el-input__inner {
  width: 100%;
}
#user .detail-form-item .el-input__inner {
  border: 0;
}
#user .el-form-item__content {
  width: 200px;
}
#user .longer {
  width: 100%;
}
#user .longer .el-form-item__content {
  width: calc(100% - 120px);
}
</style>


