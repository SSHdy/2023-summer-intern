<template>
  <div id="assetSituation">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">时间范围：</div>
          <div class="query-input">
            <el-date-picker
              v-model="startTime"
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
    <div style="display:flex;justify-content:space-between;">
      <div class="table-box">
        <table-component
          style="width:900px;"
          :tableData="tableData"
          :tableHeight="tableHeight"
          @select="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" align="center"></el-table-column>
          <el-table-column prop="assetType" width="245" label="资产名称" align="center"></el-table-column>
          <el-table-column
            prop="financialAppropriationComn"
            width="145"
            label="财政拨款"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="nonFinancialAppropriationComn"
            width="175"
            label="非财政拨款"
            align="center"
          ></el-table-column>
          <el-table-column prop="num" width="140" label="数量" align="center"></el-table-column>
          <el-table-column prop="total" width="140" label="总价值" align="center"></el-table-column>
        </table-component>
      </div>
      <div id="assetECharts" style="width:600px;height:600px;margin-right:50px"></div>
    </div>
  </div>
</template>

<script>
// 引入基本模板
let echarts = require("echarts/lib/echarts");
// 引入柱状图组件
require("echarts/lib/chart/pie");
// 引入提示框和title组件
require("echarts/lib/component/tooltip");
require("echarts/lib/component/legend");
import tableComponent from "@/components/table/table2.vue";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import { assertStatistics } from "./api";
import { setTimeout } from "timers";
export default {
  name: "hello",
  components: {
    tableComponent,
    tableBtnGroup,
    singleBtnGroup,
    searchBtn,
    resetBtn
  },
  data() {
    return {
      msg: "Welcome to Your Vue.js App",
      tabPosition: "left",
      tableData: [],
      tableHeight: 0,
      pageSize: 7,
      total: 0,
      beginDate: null,
      endDate: null,
      startTime: "",
      endTime: "",
      assetType: {
        1: "车辆",
        2: "房屋",
        3: "构筑物",
        4: "家具、用具、餐具",
        5: "通用设备",
        6: "专用设备",
        7: "土地使用权"
      },
      Pie: [],
      pickerOptions_start: {
      },
      pickerOptions_end: {
      }
    };
  },

  methods: {
    handleSelectionChange(data) { },
    search() {
      this.getAssertStatisticsList();
    },
    reset() {
      this.startTime = ''
      this.endTime = ''
      this.getAssertStatisticsList();
    },
    getAssertStatisticsList() {
      assertStatistics(this.startTime, this.endTime).then(res => {
        var dataArr = [];
        res.forEach(e => {
          var obj = {};
          e["assetType"] = this.assetType[e._id];
          obj["value"] = e.total;
          obj["name"] = e["assetType"];
          dataArr.push(obj);
          e.total = parseFloat(e.total.toFixed(2)).toLocaleString()
          e.financialAppropriationComn = parseFloat(e.financialAppropriationComn.toFixed(2)).toLocaleString()
          e.nonFinancialAppropriationComn = parseFloat(e.nonFinancialAppropriationComn.toFixed(2)).toLocaleString()
        });
        this.tableData = res;

        this.Pie = dataArr;
      });
    },
    drawLine() {
      // 基于准备好的dom，初始化echarts实例
      let assetECharts = echarts.init(document.getElementById("assetECharts"));
      // 绘制图表
      // console.log(this.Pie)
      assetECharts.setOption({
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        backgroundColor: "#eee",
        legend: {
          // orient: 'vertical',
          itemWidth: 5, // 设置图例图形的宽
          itemHeight: 5,
          itemGap: 30,
          textStyle: {
            fontSize: 12 // 图例文字颜色
          },
          x: "right",
          y: "top",
          data: [
            "车辆",
            "房屋",
            "构筑物",
            "家具、用具、装具",
            "通用设备",
            "专用设备",
            "土地使用权"
          ]
        },
        series: [
          {
            name: "资产情况",
            type: "pie",
            // radius: ['30%', '70%'],
            avoidLabelOverlap: false,
            label: {
              normal: {
                show: false
              },
              emphasis: {
                show: false,
                textStyle: {
                  fontSize: "30",
                  fontWeight: "bold"
                }
              }
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: this.Pie
          }
        ]
      });
    }
  },
  created() {
    this.getAssertStatisticsList();
  },
  mounted() {
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      this.tableHeight = mainDivH - queryBoxH;
    }, 100);

    this.pickerOptions_start.disabledDate = (time) => {
      if (this.endTime) {
        return new Date(this.endTime).getTime() < time.getTime();
      }
    }

    this.pickerOptions_end.disabledDate = (time) => {
      if (this.startTime) {
        return new Date(this.startTime).getTime() > time.getTime();
      }
    }
  },
  beforeUpdate() {
    this.drawLine();
  }
};
</script>
<style>
/* #assetSituation .query-box {
  margin-bottom: 20px;
}
#assetSituation .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 38px;
  float: left;
  width: 20%;
}
#assetSituation .query-item > div {
  float: left;
}
#assetSituation .query-item > .query-input {
  width: calc(100% - 80px);
}
#assetSituation .query-item .el-date-editor.el-input,
#assetSituation .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#assetSituation .query-btn {
  float: left;
  margin-left: 50px;
  margin-top: 3px
} */
#assetSituation .query-box {
  margin-bottom: 20px;
}
#assetSituation .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 18%;
}
#assetSituation .query-item > div {
  float: left;
}
#assetSituation .query-item > .query-input {
  width: calc(100% - 80px);
}
#assetSituation .query-item .el-select {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#assetSituation .query-item .el-date-editor.el-input,
#assetSituation .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#assetSituation .query-btn {
  float: right;
}
#assetSituation .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#assetSituation .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#assetSituation .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
</style>