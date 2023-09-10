<template>
  <div id="repair_month">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">选择月份：</div>
          <div class="query-input">
            <el-date-picker
              v-model="month"
              type="month"
              placeholder="选择月份"
              :picker-options="pickerOptions"
              format="yyyy 年 MM 月"
              value-format="yyyy-MM"
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
          prop="repairType"
          label="维修类型"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="auditCount"
          label="审批数量"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="auditCountTb"
          label="审批数量同比"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="auditCountHb"
          label="审批数量环比"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="repairModuleArea"
          label="维修面积(m²)"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="repairModuleAreaTb"
          label="维修面积同比(m²)"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="repairModuleAreaHb"
          label="维修面积环比(m²)"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="repairPaymentMoney"
          label="付款金额(元)"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="repairPaymentMoneyTb"
          label="付款金额同比(元)"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="repairPaymentMoneyHb"
          label="付款金额环比(元)"
          align="center"
        ></el-table-column>
      </table-component>
    </div>
  </div>
</template>

<script>
import {
  getSelectRepairMothCount,
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
      month: '',
      loading: null,
      tableData: [],
      Form: {
        repairType: '',
        auditCount: '',
        auditCountTb: '',
        auditCountHb: '',
        repairModuleArea: '',
        repairModuleAreaTb: '',
        repairModuleAreaHb: '',
        repairPaymentMoney: '',
        repairPaymentMoneyTb: '',
        repairPaymentMoneyHb: ''
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
    this.month = this.formatDate(new Date().getTime(), "yyyy-MM");
    this.currentTime = this.month;
    this.getList(this.month);
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
      getSelectRepairMothCount(time)
        .then(res => {
          res.forEach(item => {
            item.repairType = this.getReviewAuditState(item.repairType);
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
          const msg = err.result ? err.result : '维修工作统计信息获取失败';
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
          return '零星维修';
          break;
        case 2:
          return '申请单位部门维修';
          break;
        case 3:
          return '机关管理局费用维修';
          break;
        case 4:
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
          message: '月份不能为空！',
          type: "warning"
        });
        setTimeout(() => {
          this.month = this.currentTime;
        }, 1)
        return;
      }
      this.currentTime = val;
      this.getList(val);
    },
    // 获取可选择的时间区间
    getStatisticsDateRange() {
      getStatisticsDateRange(1).then(res => {
        if (!res) {
          this.datePickerDisabled = true;
          return;
        }
        const maxMonth = res.maxMonth + '-31 23:59:59';
        const minMonth = res.minMonth + '-01 00:00:00';
        this.pickerOptions.disabledDate = (time) => {
          return time.getTime() > new Date(maxMonth).getTime() || time.getTime() < new Date(minMonth).getTime();
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
#repair_month .query-box {
  margin-bottom: 20px;
}
#repair_month .query-item {
  font-size: 16px;
  height: 40px;
  line-height: 40px;
  float: left;
  width: 20%;
}
#repair_month .query-item > div {
  float: left;
}
#repair_month .query-item > .query-input {
  width: calc(100% - 80px);
}
#repair_month .query-input .el-input__inner {
  height: 32px;
}
</style>


