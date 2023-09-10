<template>
  <div id="log">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">操作人：</div>
          <div class="query-input">
            <el-input v-model="userName" placeholder="请输入操作人" v-enter-trim></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">操作时间：</div>
          <div class="query-input">
            <el-date-picker
              v-model="beginTime"
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
        <el-table-column type="index" width="50" align="center"></el-table-column>
        <el-table-column prop="userName" label="操作人" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="url" label="操作的接口URL" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column
          prop="operationTime"
          label="操作时间"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column prop="method" label="访问方式" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="body" label="消息体" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="操作" width="90" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowAlter="false"
              :isShowDelete="false"
              :isShowDetail="true"
              @detailClick="singleDetail(scope.row)"
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
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{dialogTitle}}</span>
      </div>
      <div class="dialog-content">
        <div class="content-item clearfix" v-for="(item, index) in logDetail" :key="index">
          <span>{{item.name}}</span>
          <span>：</span>
          <span>{{item.value}}</span>
        </div>
      </div>
      <span slot="footer" class="dialog-footer"></span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getSystemLog
} from "./api";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
export default {
  components: {
    singleBtnGroup,
    tablePagination,
    tableComponent,
    searchBtn,
    resetBtn
  },
  data() {
    return {
      loading: null,
      userName: "",
      beginTime: "",
      endTime: "",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      tableHeight: 0,
      tableData: [],
      dialogTitle: "详情",
      dialogVisible: false,
      logDetail: [],
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
    // 获取信息列表
    getList() {
      this.loadingStart('.table-box');
      getSystemLog(this.currentPage, this.pageSize, this.userName, this.beginTime, this.endTime)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.loadingEnd();
        })
        .catch(err => {
          const msg = err.result ? err.result : '日志列表获取失败';
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
    // 重置
    reset() {
      this.currentPage = 1;
      this.pageSize = 15;
      this.userName = "";
      this.beginTime = "";
      this.endTime = "";
      this.search();
    },
    // 单个详情
    singleDetail(row) {
      this.dialogVisible = true;
      for ([key, value] of Object.entries(row)) {
        if (key !== 'id') {
          let titleName = '';
          switch (key) {
            case 'userName':
              titleName = '操作人';
              break;
            case 'url':
              titleName = '操作的接口URL';
              break;
            case 'operationTime':
              titleName = '操作时间';
              break;
            case 'method':
              titleName = '访问方式';
              break;
            case 'body':
              titleName = '消息体';
              break;
          }
          const obj = {
            name: titleName,
            value: value ? value : '暂无'
          }
          this.logDetail.push(obj);
        }
      }
    },
    // dialog关闭的回调
    dialogClosed() {
      this.logDetail = [];
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

    this.pickerOptions_start.disabledDate = (time) => {
      if (this.endTime) {
        return new Date(this.endTime).getTime() < time.getTime();
      }
    }

    this.pickerOptions_end.disabledDate = (time) => {
      if (this.beginTime) {
        return new Date(this.beginTime).getTime() > time.getTime();
      }
    }
  }
};
</script>

<style>
#log .query-box {
  margin-bottom: 20px;
}
#log .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 18%;
}
#log .query-item > div {
  float: left;
}
#log .query-item > .query-input {
  width: calc(100% - 80px);
}
#log .query-item .el-select {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#log .query-item .el-date-editor.el-input,
#log .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#log .query-btn {
  float: right;
}
#log .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#log .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#log .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#log .el-form-item__content {
  width: 200px;
}
#log .detail-form-item .el-input__inner {
  border: 0;
}
#log .content-item {
  padding-bottom: 15px;
}
#log .content-item > span {
  float: left;
  line-height: 15px;
}
#log .content-item > span:first-child {
  width: 100px;
  text-align: justify;
  text-justify: distribute-all-lines;
  -moz-text-align-last: justify;
  text-align-last: justify;
}
#log .content-item > span:nth-child(3) {
  width: calc(100% - 115px);
}
</style>


