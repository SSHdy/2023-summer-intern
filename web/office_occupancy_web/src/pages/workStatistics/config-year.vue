<template>
  <div id="config_year">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">选择年份：</div>
          <div class="query-input">
            <el-date-picker
              v-model="year"
              type="year"
              placeholder="选择年份"
              :picker-options="pickerOptions"
              format="yyyy 年"
              value-format="yyyy"
              @change="dateChange"
              :disabled="datePickerDisabled"
            ></el-date-picker>
          </div>
        </div>
      </div>
    </div>
    <div class="table-box">
      <table-component :tableData="tableData">
        <el-table-column
          :show-overflow-tooltip="true"
          prop="allocationPlan"
          label="分配方案"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="approvalNum"
          label="审批数量"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="approvalNumHb"
          label="审批数量环比"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="approvalArea"
          label="审批面积(m²)"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="approvalAreaHb"
          label="审批面积环比(m²)"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="approvalMoney"
          label="审批金额(元)"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="approvalMoneyHb"
          label="审批金额环比(元)"
          align="center"
        ></el-table-column>
      </table-component>
    </div>
  </div>
</template>

<script>
import {
  getConfigureStatisticsByYear,
  getStatisticsDateRange
} from "./api";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table1.vue";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
export default {
  components: {
    tableComponent,
    cancelBtn,
    confirmBtn,
    searchBtn,
    resetBtn
  },
  data() {
    return {
      year: '',
      loading: null,
      tableData: [],
      Form: {
        allocationPlan: '',
        approvalNum: '',
        approvalNumTb: '',
        approvalNumHb: '',
        approvalMoney: '',
        approvalMoneyHb: '',
        approvalArea: '',
        approvalAreaHb: ''
      },
      pickerOptions: {
      },
      datePickerDisabled: false,
      currentTime: ''
    };
  },
  created() {
    this.getStatisticsDateRange();
  },
  mounted() {
    this.year = this.formatDate(new Date().getTime(), "yyyy");
    this.currentTime = this.year;
    this.getList(this.year);
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
    // 获取列表
    getList(time) {
      this.loadingStart('.table-box');
      getConfigureStatisticsByYear(time)
        .then(res => {
          res.forEach(item => {
            item.allocationPlan = this.getReviewAuditState(item.allocationPlan);
            for (let key in this.Form) {
              if (!item[key]) {
                item[key] = '-';
              }
            }
          })
          this.tableData = res;
          this.loadingEnd();
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '配置工作统计信息获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取审核状态
    getReviewAuditState(status) {
      switch (status) {
        case 1:
          return '调剂';
          break;
        case 2:
          return '置换';
          break;
        case 3:
          return '租用';
          break;
        case 4:
          return '建设';
          break;
        case 5:
          return '合计';
          break;
      }
    },
    // 格式化时间
    formatDate(time, fmt) {
      var date = new Date(time);
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
    // 时间改变
    dateChange(val) {
      if (val === null) {
        this.$message({
          message: '年份不能为空！',
          type: "warning"
        });
        setTimeout(() => {
          this.year = this.currentTime;
        }, 1)
        return;
      }
      this.currentTime = val;
      this.getList(val);
    },
    // 获取可选择的时间区间
    getStatisticsDateRange() {
      getStatisticsDateRange(3).then(res => {
        if (!res) {
          this.datePickerDisabled = true;
          return;
        }
        const maxYear = res.maxYear + '-12-31 23:59:59';
        const minYear = res.minYear + '-01-01 00:00:00';
        this.pickerOptions.disabledDate = (time) => {
          return time.getTime() > new Date(maxYear).getTime() || time.getTime() < new Date(minYear).getTime();
        }
      }).catch(err => {
        const msg = err.result ? err.result : '工作统计日期范围获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    }
  }
};
</script>

<style>
#config_year .query-box {
  margin-bottom: 20px;
}
#config_year .query-item {
  font-size: 16px;
  height: 40px;
  line-height: 40px;
  float: left;
  width: 20%;
}
#config_year .query-item > div {
  float: left;
}
#config_year .query-item > .query-input {
  width: calc(100% - 80px);
}
#config_year .query-input .el-input__inner {
  height: 32px;
}
</style>


