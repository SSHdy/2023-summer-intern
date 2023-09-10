<template>
  <div id="client">
    <div class="table-box">
      <table-component :tableData="tableData" :tableHeight="tableHeight">
        <el-table-column type="index" label="序号" width="50"></el-table-column>
        <el-table-column prop="clientId" label="客户端编号" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column
          prop="authorizedGrantNames"
          label="认证授权类型"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="webServerRedirectUri"
          label="web服务重定向地址"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column prop="scope" label="范围" align="center" :show-overflow-tooltip="true"></el-table-column>
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
          :label="operateType==='detail'? '客户端编号:' : '客户端编号'"
          label-width="120px"
          prop="clientId"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.clientId"
            placeholder="请输入客户端编号"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '资源编号:' : '资源编号'"
          label-width="120px"
          prop="resourceIds"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.resourceIds"
            placeholder="请输入资源编号"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '认证方式:' : '认证方式'"
          label-width="120px"
          prop="authorizedGrantTypes"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-select
            v-model="Form.authorizedGrantTypes"
            filterable
            multiple
            placeholder="请选择认证方式"
            v-if="operateType!=='detail'"
            @change="authorizedChange"
          >
            <el-option
              v-for="item in authorizedGrantTypesArr"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
          <div v-else style="width:200px">{{authorizedGrantNames}}</div>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '重定向地址:' : '重定向地址'"
          label-width="120px"
          prop="webServerRedirectUri"
          :class="{'detail-form-item' : operateType==='detail'}"
          v-if="showAuthorization"
        >
          <el-input
            v-model="Form.webServerRedirectUri"
            placeholder="请输入重定向地址"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '公司名称:' : '公司名称'"
          label-width="120px"
          prop="companyName"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.companyName"
            placeholder="请输入公司名称"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '是否授权:' : '是否授权'"
          label-width="120px"
          prop="autoapprove"
        >
          <div style="width:200px">
            <el-radio
              v-model="Form.autoapprove"
              :label="0"
              name="autoapprove"
              v-if="!(operateType==='detail')"
            >是</el-radio>
            <el-radio
              v-model="Form.autoapprove"
              :label="1"
              name="autoapprove"
              v-if="!(operateType==='detail')"
            >否</el-radio>
          </div>
          <div style="width:200px">
            <span v-if="operateType==='detail'">{{Form.autoapprove===0? '是':'否'}}</span>
          </div>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '密码:' : '密码'"
          label-width="120px"
          prop="clientSecret"
          :class="{'detail-form-item' : operateType==='detail'}"
          v-if="operateType!=='detail'"
        >
          <el-input
            v-model="Form.clientSecret"
            placeholder="请输入密码"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
          <span v-if="operateType==='edit'" class="secret-tips">若要修改密码，请填写，不修改不填写。</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '范围:' : '范围'"
          label-width="120px"
          prop="scope"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.scope"
            placeholder="请输入范围"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" v-show="!(operateType==='detail')">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form')" :fontSize="14"></confirm-btn>
        <div class="bottom"></div>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import {
  selectClientList,
  addClient,
  deleteClient,
  updateClient,
  clientUnique
} from "./api";
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
      if (value === "" || value === null) {
        callback(new Error("客户端id不能为空"));
      } else if (reg.test(value)) {
        callback(new Error("客户端id只能是英文，数字或符号"));
      } else {
        clientUnique(this.Form.clientId, this.Form.oauthDetailId)
          .then(res => {
            callback();
          })
          .catch(err => {
            callback(new Error("客户端id重复"));
          });
      }
    };
    return {
      clientName: "",
      tableData: [],
      tableHeight: 0,
      currentPage: 1,
      pageSize: 15,
      total: 1,
      loading: false,
      authorizedGrantTypesArr: [
        {
          id: "authorization_code",
          name: "授权码模式"
        },
        {
          id: "implicit",
          name: "简化模式"
        },
        {
          id: "password",
          name: "密码模式"
        },
        {
          id: "client_credentials",
          name: "客户端模式"
        },
        {
          id: "refresh_token",
          name: "刷新令牌"
        }
      ],
      operateType: "",
      dialogVisible: false,
      dialogTitle: "新增",
      showAuthorization: false,
      authorizedGrantNames: [],
      rules: {
        clientId: [{ required: true, validator: validator_1, trigger: "blur" }],
        resourceIds: [
          { required: true, message: "资源编号不能为空", trigger: "blur" }
        ],
        clientSecret: [],
        scope: [{ required: true, message: "范围不能为空", trigger: "blur" }],
        webServerRedirectUri: [
          { required: true, message: "重定向地址不能为空", trigger: "blur" }
        ],
        autoapprove: [
          { required: true, message: "授权不能为空", trigger: "blur" }
        ],
        companyName: [
          { required: true, message: "公司名称不能为空", trigger: "blur" }
        ],
        authorizedGrantTypes: [
          { required: true, message: "认证方式不能为空", trigger: "blur" }
        ]
      },
      Form: {
        authorizedGrantTypes: [],
        clientId: "",
        resourceIds: "",
        clientSecret: "",
        scope: "",
        webServerRedirectUri: "",
        autoapprove: 1,
        companyName: ""
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
    changeSize(size) {
      this.pageSize = size;
      this.getList();
    },
    changePage(page) {
      this.currentPage = page;
      this.getList();
    },
    getList() {
      this.loadingStart('.table-box');
      selectClientList(this.currentPage, this.pageSize)
        .then(res => {
          this.loadingEnd();
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach(item => {
            item.webServerRedirectUri = item.webServerRedirectUri ? item.webServerRedirectUri : '-'
            const arr = item.authorizedGrantTypes.split(',');
            item.authorizedGrantNames = [];
            for (let j = 0; j < arr.length; j++) {
              for (let i = 0; i < this.authorizedGrantTypesArr.length; i++) {
                if (this.authorizedGrantTypesArr[i].id === arr[j]) {
                  item.authorizedGrantNames.push(this.authorizedGrantTypesArr[i].name)
                }
              }
            }
            item.authorizedGrantNames = item.authorizedGrantNames.join(',')
          })
        })
        .catch(err => {
          this.loadingEnd();
          let msg = err.result ? err.result : "查询授权列表失败";
          this.$message({
            type: "error",
            message: msg
          });
        });
    },
    // 删除
    singleDetle(row) {
      this.$confirm(`此操作将永久删除客户端编号为${row.clientId}授权信息, 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteClient(parseInt(row.oauthDetailId))
            .then(res => {
              this.$message({
                message: "删除授权信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : "删除授权信息失败!";
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
    // 修改
    singleUpdate(row) {
      this.operateType = "edit";
      this.dialogVisible = true;
      this.dialogTitle = "修改客户端授权";
      this.rules.clientSecret = [];
      this.Form.authorizedGrantTypes = row.authorizedGrantTypes.split(",");
      this.Form.autoapprove = parseInt(row.autoapprove);
      this.Form.clientId = row.clientId;
      this.Form.companyName = row.companyName;
      this.Form.oauthDetailId = row.oauthDetailId;
      this.Form.resourceIds = row.resourceIds;
      this.Form.scope = row.scope;
      if (row.authorizedGrantTypes.indexOf('authorization_code') === -1) {
        this.showAuthorization = false
      } else {
        this.showAuthorization = true
        this.Form.webServerRedirectUri = row.webServerRedirectUri;
      }
    },
    // 详情
    singleDetail(row) {
      this.operateType = "detail";
      this.dialogVisible = true;
      this.dialogTitle = "客户端授权详情";
      this.Form.authorizedGrantTypes = row.authorizedGrantTypes;
      this.Form.autoapprove = row.autoapprove ? 1 : 0;
      this.Form.clientId = row.clientId;
      this.Form.companyName = row.companyName;
      this.Form.oauthDetailId = row.oauthDetailId;
      this.Form.resourceIds = row.resourceIds ? row.resourceIds : '暂无';
      this.Form.scope = row.scope;
      this.Form.webServerRedirectUri = row.webServerRedirectUri;
      const arr = this.Form.authorizedGrantTypes.split(',');
      this.authorizedGrantNames = [];
      for (let j = 0; j < arr.length; j++) {
        for (let i = 0; i < this.authorizedGrantTypesArr.length; i++) {
          if (this.authorizedGrantTypesArr[i].id === arr[j]) {
            this.authorizedGrantNames.push(this.authorizedGrantTypesArr[i].name)
          }
        }
      }
      this.authorizedGrantNames = this.authorizedGrantNames.join(',')
      if (row.authorizedGrantTypes.indexOf('authorization_code') === -1) {
        this.showAuthorization = false
      } else {
        this.showAuthorization = true
      }
    },
    // 新增
    add() {
      this.operateType = "add";
      this.dialogVisible = true;
      this.dialogTitle = "新增客户端授权";
      if (this.rules.clientSecret.length === 0) {
        this.rules.clientSecret.push({ required: true, message: "客户端密钥不能为空", trigger: "blur" })
      }
    },
    submitForm(formName) {
      if (this.operateType != "detail") {
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.saveForm();
          } else {
            return false;
          }
        });
      }
    },
    saveForm() {
      if (!this.showAuthorization) this.Form.webServerRedirectUri = '';
      this.Form.authorizedGrantTypes = this.Form.authorizedGrantTypes.join(",");
      if (this.operateType == "add") {
        addClient(this.Form)
          .then(res => {
            this.dialogVisible = false;
            this.$message({
              type: "success",
              message: "新增授权成功"
            });
            this.getList();
          })
          .catch(err => {
            let msg = err.result ? err.result : "新增授权失败";
            this.$message({
              type: "error",
              message: msg
            });
          });
      } else if (this.operateType == "edit") {
        updateClient(this.Form)
          .then(res => {
            this.dialogVisible = false;
            this.$message({
              type: "success",
              message: "修改授权成功"
            });
            this.getList();
          })
          .catch(err => {
            let msg = err.result ? err.result : "修改授权失败";
            this.$message({
              type: "error",
              message: msg
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
      this.clearForm(this.Form);
      this.selectMenuTreeDate = [];
      this.treeShow = false;
      this.Form.isFreeze = 0;
      this.Form.isSubscibe = 1;
      this.Form.autoapprove = 1;
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
    authorizedChange(selected) {
      if (selected.indexOf('authorization_code') === -1) {
        this.showAuthorization = false
      } else {
        this.showAuthorization = true
      }
    }
  },
  mounted() {
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - operateBoxH;
    }, 1);
  },
  created() {
    this.getList();
  }
};
</script>
<style>
#client .query-box {
  margin-bottom: 20px;
}
#client .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#client .query-item > div {
  float: left;
}
#client .query-item > .query-input {
  width: calc(100% - 80px);
}
#client .query-item .el-date-editor.el-input,
#client .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#client .query-btn {
  float: left;
  margin-left: 50px;
}
#client .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#client .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#client .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#client .el-form-item .el-input__inner {
  height: 32px;
}
#client .detail-form-item .el-input__inner {
  border: 0;
  padding-left: 0;
}
#client .detail-form-item .transfer-panel {
  margin-left: 0 !important;
}
#client .bottom {
  height: 30px;
  margin-top: 20px;
  background-color: #f8f8f8;
}
.el-select__tags {
  flex-wrap: nowrap;
  overflow: hidden;
}
#client .el-form-item__content .el-input--suffix .el-input__inner {
  height: 32px !important;
}
#client .el-form-item__content .el-select {
  width: 200px;
}
#client .el-form-item {
  position: relative;
}
#client .el-form-item__content span.secret-tips {
  font-size: 12px;
  color: #ff0000;
  position: absolute;
  bottom: -30px;
  width: 220px;
  left: 0;
}
</style>
