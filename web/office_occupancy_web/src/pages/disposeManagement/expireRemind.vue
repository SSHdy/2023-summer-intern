<template>
  <div id="expireRemind">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">单位：</div>
          <div class="query-input">
            <el-input v-model="renter" placeholder="请输入单位" v-enter-trim></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">类型：</div>
          <div class="query-input">
            <el-select filterable v-model="type" placeholder="请选择类型" style="width:100%">
              <el-option
                v-for="item in typeOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
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
        <el-table-column :show-overflow-tooltip="true" prop="renter" label="租/借用单位" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="lesseeYears"
          label="年限(年)"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="rentMoney"
          label="租金(元)"
          align="center"
        ></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="deposit" label="押金(元)" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="rentStateText"
          label="状态"
          align="center"
        ></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="typeName" label="类型" align="center"></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="rentBegin" label="起始时间" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="rentEnd"
          label="截止时间"
          align="center"
          class-name="paymentTime-col"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="paymentTime"
          label="付款时间"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="90" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDetail="false"
              :isShowDelete="false"
              :isShowAlter="false"
              :isShowHaveRead="true"
              @haveReadClick="singleHaveRead(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group
            :batchDelBtnShow="false"
            :addBtnShow="false"
            :haveReadBtnShow="true"
            @haveReadClick="haveReadClick"
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
  </div>
</template>

<script>
import {
  getRentExpireList,
  updateIsRead
} from "./api";
import { uploadUrl } from "@/axios/commonApi";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import { type } from 'os';
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
      renter: "",
      type: '',
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
      typeOptions: [
        {
          id: '',
          name: '全部'
        },
        {
          id: 1,
          name: '出租'
        },
        {
          id: 2,
          name: '出借'
        }
      ],
      rentStateOptions: [
        {
          id: '',
          name: '全部'
        },
        {
          id: 1,
          name: '出租中'
        },
        {
          id: 0,
          name: '已退租'
        }
      ]
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
      getRentExpireList(this.currentPage, this.pageSize, this.renter, this.type)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach(item => {
            item.typeName = item.type === 1 ? '出租' : '出借';
            if (item.rentState !== undefined) {
              this.rentStateOptions.forEach(opt => {
                if (opt.id === item.rentState) {
                  item.rentStateText = opt.name;
                  if (item.rentState === 1 && item.type === 2) {
                    item.rentStateText = '出借中';
                  }
                }
              })
            }
            if (item.rentMoney !== undefined) {
              item.rentMoney = String(item.rentMoney.toFixed(2));
            }
            if (item.deposit !== undefined) {
              item.deposit = String(item.deposit.toFixed(2));
            }
            if (item.rentBegin) {
              item.rentBegin = this.formatDate(item.rentBegin);
            }
            if (item.rentEnd) {
              item.rentEnd = this.formatDate(item.rentEnd);
            }
            if (item.paymentTime) {
              item.paymentTime = this.formatDate(item.paymentTime);
            }
          })
          this.loadingEnd();
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '租借到期提醒列表获取失败';
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
    // 重置
    reset() {
      this.currentPage = 1;
      this.renter = '';
      this.type = '';
      this.search();
    },
    // 批量关闭
    haveReadClick() {
      if (this.selectData.length === 0) {
        this.$message({
          message: '请先选择一条提醒',
          type: "warning"
        });
        return false;
      }
      this.$confirm("批量关闭, 是否继续?", "提示", {
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let temp = [];
          this.selectData.forEach(item => {
            temp.push(item.rentId);
          })
          updateIsRead(temp, 2).then(res => {
            this.$message({
              message: '关闭成功',
              type: "success"
            });
            eventBus.$emit("updateRemindTips");
          }).catch(err => {
            const msg = err.result ? err.result : '关闭失败';
            this.$message({
              message: msg,
              type: "error"
            });
          });
          this.getList();
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消关闭"
          });
        });
    },
    // 单个关闭
    singleHaveRead(row) {
      this.$confirm("关闭, 是否继续?", "提示", {
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          updateIsRead([row.rentId], 2).then(res => {
            this.$message({
              message: '关闭成功',
              type: "success"
            });
            this.getList();
            eventBus.$emit("updateRemindTips");
          }).catch(err => {
            const msg = err.result ? err.result : '关闭失败';
            this.$message({
              message: msg,
              type: "error"
            });
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消关闭"
          });
        });
    },
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
    },
    // 格式化时间
    formatDate(time) {
      var date = new Date(time);
      var fmt = "yyyy-MM-dd";
      var o = {
        "M+": date.getMonth() + 1, //月份
        "d+": date.getDate(), //日
        "h+": date.getHours(), //小时
        "m+": date.getMinutes(), //分
        "s+": date.getSeconds(), //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        S: date.getMilliseconds() //毫秒
      };
      if (/(y+)/.test(fmt))
        fmt = fmt.replace(
          RegExp.$1,
          (date.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length == 1
              ? o[k]
              : ("00" + o[k]).substr(("" + o[k]).length)
          );
      return fmt;
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
#expireRemind .query-box {
  margin-bottom: 20px;
}
#expireRemind .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#expireRemind .query-item > div {
  float: left;
}
#expireRemind .query-item > .query-input {
  width: calc(100% - 80px);
}
#expireRemind .query-item .el-date-editor.el-input,
#expireRemind .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#expireRemind .query-btn {
  float: left;
  margin-left: 50px;
}
#expireRemind .el-textarea__inner {
  width: 868px;
  line-height: 40px;
  padding-top: 0;
}
#expireRemind .el-textarea {
  vertical-align: middle !important;
}
#expireRemind .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#expireRemind .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#expireRemind .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#expireRemind .upload_box .el-upload--picture-card {
  width: 100px;
  height: 100px;
  line-height: 100px;
}
#expireRemind .upload_box .el-upload-list--picture-card .el-upload-list__item {
  width: 100px;
  height: 100px;
}
#expireRemind .el-upload--picture-card {
  margin-bottom: 8px;
}
#expireRemind .el-upload-list > li {
  margin-bottom: 0;
}
#expireRemind .detail-form-item .el-input__inner {
  border: 0;
  padding-left: 0;
  width: 215px;
}
#expireRemind .detail-form-item .el-textarea__inner {
  border: 0;
  padding-left: 0;
  width: 550px;
}
#expireRemind .img-preview-item {
  float: left;
  margin-right: 20px;
  margin-bottom: 15px;
  width: 100px;
  height: 100px;
  border: 1px solid #c0ccda;
  position: relative;
}
#expireRemind .img-preview-item > img {
  width: 100%;
  height: 100%;
}
#expireRemind .img-preview-item > .bigger-view-warpper {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}
#expireRemind
  .img-preview-item
  > .bigger-view-warpper
  > .bigger-view-warpper-inner {
  width: 100%;
  height: 100%;
  background: #000;
  opacity: 0.5;
  display: none;
}
#expireRemind
  .img-preview-item
  > .bigger-view-warpper:hover
  > .bigger-view-warpper-inner {
  display: block;
}
#expireRemind .img-preview-item > .bigger-view-warpper > i {
  position: absolute;
  font-size: 26px;
  color: #ffffff;
  cursor: pointer;
  display: none;
  top: 50%;
  left: 50%;
  margin-left: -13px;
  margin-top: -13px;
}
#expireRemind .img-preview-item > .bigger-view-warpper > i:hover {
  color: #3891e0;
}
#expireRemind .img-preview-item > .bigger-view-warpper:hover > i {
  display: block;
}
#expireRemind .img-preview-item > .el-checkbox {
  position: absolute;
  bottom: -13px;
  right: 0;
}
#expireRemind .download-btn {
  padding: 7px 22px;
}
#expireRemind .img-preview-bigger {
  width: 100%;
  height: 100%;
  position: fixed;
  z-index: 100000;
  top: 0;
  left: 0;
  display: none;
}
#expireRemind .img-preview-bigger > .img-preview-bigger-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
}
#expireRemind .img-preview-bigger > img {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
}
#expireRemind .img-preview-bigger > i {
  position: absolute;
  font-size: 50px;
  top: 30px;
  right: 30px;
  color: #ffffff;
  cursor: pointer;
}
#expireRemind .img-preview-bigger > i:hover {
  color: #3891e0;
}
#expireRemind .paymentTime-col {
  color: #ff0000;
}
</style>


