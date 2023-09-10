<template>
  <div id="qualityAssuranceDate">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">维修地点：</div>
          <div class="query-input">
            <el-select v-model="Form.repairModuleId" placeholder="请选择维修地点" size="small">
              <el-option
                v-for="item in repairModuleOpts"
                :key="item.repairModuleId"
                :label="item.repairModulePosition"
                :value="item.repairModuleId"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="query-item">
          <div class="query-title">维修部位：</div>
          <div class="query-input">
            <el-select v-model="Form.repairPartsId" placeholder="请选择维修部位" size="small">
              <el-option
                v-for="item in repairPartsOpts"
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
      <table-component :tableData="tableData" :tableHeight="tableHeight">
        <el-table-column type="index" label="序号" width="50" align="center"></el-table-column>
        <el-table-column
          prop="repairModulePosition"
          label="维修地点"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="repairPartName"
          label="维修部位"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="repairQuality"
          label="质保日期"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="repairPartQuality"
          label="质保期(年)"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="completionTime"
          label="竣工时间"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="repairModuleArea"
          label="维修面积(m²)"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
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
  </div>
</template>
<script>
import {
  getRepairModule,
  getRepairParts,
  getRepairModulePageList
} from "./api";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tableComponent from "@/components/table/table2.vue";
import tablePagination from "@/components/tableGroup/tablePagination";
export default {
  components: {
    tableComponent,
    tableBtnGroup,
    tablePagination,
    searchBtn,
    resetBtn
  },
  data() {
    return {
      loading: null,
      tableData: [],
      tableHeight: 0,
      selectData: [],
      currentPage: 1,
      pageSize: 15,
      total: 1,
      Form: {
        repairModuleId: "",
        repairPartsId: ""
      },
      repairPartsOpts: [],
      repairModuleOpts: []
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
    //获取维修部位列表
    getRepairModuleList() {
      getRepairModule()
        .then(res => {
          this.repairModuleOpts = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : '获取维修部位列表失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取维修部位信息列表
    getRepairPartsList() {
      getRepairParts()
        .then(res => {
          this.repairPartsOpts = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : '获取维修部位信息列表失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取维修模块信息管理列表
    getRepairModulePageLists() {
      this.loadingStart(".table-box")
      getRepairModulePageList(
        this.currentPage,
        this.pageSize,
        this.Form.repairModuleId,
        this.Form.repairPartsId
      ).then(res => {
        this.total = res.total
        res.records.forEach(item => {
          if (item["completionTime"]) {
            item["completionTime"] = this.formatDate(item["completionTime"])
          }
          if (item["repairQuality"]) {
            item["repairQuality"] = this.formatDate(item["repairQuality"])
          }
          if (item["repairModuleArea"]) {
            item["repairModuleArea"] = item["repairModuleArea"].toFixed(2)
          }
        })
        this.tableData = res.records
        this.loadingEnd()
      }).catch(err => {
        const msg = err.result ? err.result : '获取维修模块信息管理列表失败';
        this.$message({
          message: msg,
          type: "error"
        });
        this.loadingEnd()
      });
    },
    // 改变页面条数
    changeSize(size) {
      this.currentPage = 1
      this.pageSize = size;
      this.getRepairModulePageLists()
    },
    // 改变页面页数
    changePage(page) {
      this.currentPage = page;
      this.getRepairModulePageLists()
    },
    //   查询
    search() {
      this.currentPage = 1;
      this.getRepairModulePageLists()
    },
    // 重置
    reset() {
      this.currentPage = 1;
      this.Form.repairModuleId = ""
      this.Form.repairPartsId = ""
      this.getRepairModulePageLists()
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
    },
  },
  created() {
    this.getRepairModuleList();
    this.getRepairPartsList();
    this.getRepairModulePageLists();
  },
  mounted() {
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
#qualityAssuranceDate .query-box {
  margin-bottom: 20px;
}
#qualityAssuranceDate .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 18%;
  margin-right: 50px;
}
#qualityAssuranceDate .query-item > div {
  float: left;
}
#qualityAssuranceDate .query-item > .query-input {
  width: calc(100% - 80px);
}
#qualityAssuranceDate .query-btn {
  float: right;
}
#qualityAssuranceDate .query-item .el-date-editor.el-input,
#qualityAssuranceDate .query-item .el-input__inner,
#qualityAssuranceDate .query-item .el-select {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#qualityAssuranceDate .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#qualityAssuranceDate .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#qualityAssuranceDate .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
</style>
