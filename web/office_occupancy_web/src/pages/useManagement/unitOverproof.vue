<template>
  <div id="unitOverproof">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">单位名称：</div>
          <div class="query-input">
            <el-select
              filterable
              multiple
              v-model="unitIdList"
              placeholder="请选择单位名称"
              style="width: 100%"
            >
              <el-option
                v-for="item in unitOptions"
                :key="item.unitId"
                :label="item.unitName"
                :value="item.unitId"
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
        <el-table-column
          prop="number"
          width="50"
          label="序号"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="unitName"
          label="单位名称"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="buildAreaSum"
          label="总建筑面积(m²)"
          align="center"
          width="70"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="houseUseAreaSum"
          label="总使用面积(m²)"
          align="center"
          width="70"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="fuShuAraeSum"
          label="附属用房建筑面积(m²)"
          align="center"
          width="100"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          label="基本办公用房"
          align="center"
          :show-overflow-tooltip="true"
        >
          <el-table-column
            v-for="item in baseOffice"
            :label="item.label"
            align="center"
            :prop="item.prop"
            :width="item.width"
            :key="item.id"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              <div
                v-if="item.prop === 'jiBenExcessRatio'"
                title="超标比例=核定使用面积/实际使用面积"
              >
                {{ scope.row.jiBenExcessRatio }}
              </div>
              <div v-else>
                {{ scope.row[item.prop] }}
              </div>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column
          label="办公室"
          align="center"
          :show-overflow-tooltip="true"
        >
          <el-table-column
            v-for="item in office"
            :label="item.label"
            align="center"
            :prop="item.prop"
            :width="item.width"
            :key="item.id"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              <div
                v-if="item.prop === 'excessRatioByBanGong'"
                title="超标面积/办公室实际使用面积=超标比例"
              >
                {{ scope.row.excessRatioByBanGong }}
              </div>
              <div v-else>
                {{ scope.row[item.prop] }}
              </div>
            </template></el-table-column
          >
        </el-table-column>
        <el-table-column
          label="服务用房"
          align="center"
          :show-overflow-tooltip="true"
        >
          <el-table-column
            v-for="item in service"
            :label="item.label"
            align="center"
            :prop="item.prop"
            :width="item.width"
            :key="item.id"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              <div
                v-if="item.prop === 'excessRatioByFuWu'"
                title="超标面积/服务实际使用面积=超标比例"
              >
                {{ scope.row.excessRatioByFuWu }}
              </div>
              <div v-else>
                {{ scope.row[item.prop] }}
              </div>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column
          prop="pspecifiedArea"
          label="设备用房"
          align="center"
          :show-overflow-tooltip="true"
        >
          <el-table-column
            v-for="item in equip"
            :label="item.label"
            align="center"
            :prop="item.prop"
            :width="item.width"
            :key="item.id"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              <div
                v-if="item.prop === 'excessRatioBySheBei'"
                title="超标面积/设备实际使用面积=超标比例"
              >
                {{ scope.row.excessRatioBySheBei }}
              </div>
              <div v-else>
                {{ scope.row[item.prop] }}
              </div>
            </template>
          </el-table-column>
        </el-table-column>
      </table-component>
    </div>
  </div>
</template>
<script>
import { getUnitExcessInfo, getUnitList } from "./api";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import tableComponent from "@/components/table/table2.vue";
import tablePagination from "@/components/tableGroup/tablePagination";
import { log } from "util";
export default {
  components: {
    searchBtn,
    resetBtn,
    tablePagination,
    tableComponent,
  },
  data() {
    return {
      unitIdList: [],
      unitOptions: [],
      loading: null,
      tableData: [],
      tableHeight: 0,
      baseOffice: [
        {
          id: 1,
          label: "实际使用面积(m²)",
          prop: "jiBenUseArea",
          width: 100,
        },
        {
          id: 2,
          label: "核定使用面积(m²)",
          prop: "jiBenStandardArea",
          width: 100,
        },
        {
          id: 3,
          label: "超标面积(m²)",
          prop: "jiBenExcessArea",
        },
        {
          id: 4,
          label: "超标比例(%)",
          prop: "jiBenExcessRatio",
        },
      ],
      office: [
        {
          id: 1,
          label: "实际使用面积(m²)",
          prop: "useAreaByBanGong",
          width: 100,
        },
        {
          id: 2,
          label: "核定使用面积(m²)",
          prop: "standardAreaByBanGong",
          width: 100,
        },
        {
          id: 3,
          label: "超标面积(m²)",
          prop: "excessAreaByBanGong",
        },
        {
          id: 4,
          label: "超标比例(%)",
          prop: "excessRatioByBanGong",
        },
      ],
      service: [
        {
          id: 1,
          label: "实际使用面积(m²)",
          prop: "useAreaByFuWu",
          width: 100,
        },
        {
          id: 2,
          label: "核定使用面积(m²)",
          prop: "standardAreaByFuWu",
          width: 100,
        },
        {
          id: 3,
          label: "超标面积(m²)",
          prop: "excessAreaByFuWu",
        },
        {
          id: 4,
          label: "超标比例(%)",
          prop: "excessRatioByFuWu",
        },
      ],
      equip: [
        {
          id: 1,
          label: "实际使用面积(m²)",
          prop: "useAreaBySheBei",
          width: 100,
        },
        {
          id: 2,
          label: "核定使用面积(m²)",
          prop: "standardAreaBySheBei",
          width: 100,
        },
        {
          id: 3,
          label: "超标面积(m²)",
          prop: "excessAreaBySheBei",
        },
        {
          id: 4,
          label: "超标比例(%)",
          prop: "excessRatioBySheBei",
        },
      ],
    };
  },
  methods: {
    // 开始加载
    loadingStart(classname) {
      if (!this.loading) {
        this.loading = this.$loading({
          lock: true,
          text: "正在加载...",
          spinner: "el-icon-loading",
          target: classname,
          customClass: "loading-box",
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
    search() {
      this.getList();
    },
    reset() {
      this.unitIdList = [];
      this.getList();
    },
    // 用户权限下的单位字典项
    getUserUnitDic() {
      getUnitList()
        .then((res) => {
          this.unitOptions = res;
        })
        .catch((err) => {
          const msg = err.result ? err.result : "单位下拉选项获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    getList() {
      this.loadingStart(".table-box");
      getUnitExcessInfo(this.unitIdList)
        .then((res) => {
          this.tableData = res;
          if (res.length > 0) {
            res.forEach((item) => {
              for (let key in item) {
                if (key !== "number" && key !== "unitName") {
                  item[key] = item[key] ? item[key].toFixed(2) : 0;
                }
              }
            });

            let obj = {
              number: "合计",
              unitName: "-",
              buildAreaSum: 0,
              houseUseAreaSum: 0,
              fuShuAraeSum: 0,
              jiBenUseArea: 0,
              jiBenStandardArea: 0,
              jiBenExcessArea: 0,
              jiBenExcessRatio: 0,
              useAreaByBanGong: 0,
              standardAreaByBanGong: 0,
              excessAreaByBanGong: 0,
              excessRatioByBanGong: 0,
              useAreaByFuWu: 0,
              standardAreaByFuWu: 0,
              excessAreaByFuWu: 0,
              excessRatioByFuWu: 0,
              useAreaBySheBei: 0,
              standardAreaBySheBei: 0,
              excessAreaBySheBei: 0,
              excessRatioBySheBei: 0,
            };

            res.forEach((es) => {
              for (let key in obj) {
                if (
                  key !== "number" &&
                  key !== "unitName" &&
                  key !== "jiBenExcessRatio" &&
                  key !== "excessRatioByBanGong" &&
                  key !== "excessRatioByFuWu" &&
                  key !== "excessRatioBySheBei"
                ) {
                  obj[key] += es[key] - 0;
                }
              }
            });
            obj.excessRatioBySheBei = obj.useAreaBySheBei
              ? (obj.excessAreaBySheBei / obj.useAreaBySheBei) * 100
              : "-";
            obj.excessRatioByFuWu = obj.useAreaByFuWu
              ? (obj.excessAreaByFuWu / obj.useAreaByFuWu) * 100
              : "-";
            obj.excessRatioByBanGong = obj.useAreaByBanGong
              ? (obj.excessAreaByBanGong / obj.useAreaByBanGong) * 100
              : "-";
            obj.jiBenExcessRatio = obj.jiBenUseArea
              ? (obj.jiBenExcessArea / obj.jiBenUseArea) * 100
              : "-";
            for (let key in obj) {
              if (key !== "number" && key !== "unitName") {
                obj[key] = obj[key].toFixed(2);
              }
            }
            this.tableData.push(obj);
          }
          this.loadingEnd();
        })
        .catch((err) => {
          this.loadingEnd();
          const msg = err.result ? err.result : "超标单位列表获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
  },
  mounted() {
    this.getUserUnitDic();
    this.getList();
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      this.tableHeight = mainDivH - queryBoxH;
    }, 1);
  },
};
</script>
<style>
#unitOverproof .query-box {
  margin-bottom: 20px;
}
#unitOverproof .query-item {
  font-size: 16px;
  /* height: 32px; */
  line-height: 32px;
  float: left;
  width: 20%;
}
#unitOverproof .query-item > div {
  float: left;
}
#unitOverproof .query-item > .query-input {
  width: calc(100% - 80px);
}
#unitOverproof .query-item .el-date-editor.el-input,
#unitOverproof .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#unitOverproof .query-btn {
  float: right;
}
#unitOverproof .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#unitOverproof .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#unitOverproof .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#unitOverproof .el-table .cell {
  line-height: inherit;
  padding-left: 5px;
  padding-right: 5px;
}
#unitOverproof .el-table th div {
  line-height: inherit;
}
</style>
