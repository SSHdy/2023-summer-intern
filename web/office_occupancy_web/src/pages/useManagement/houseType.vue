<template>
  <div id="houseType">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">使用单位：</div>
          <div class="query-input">
            <el-select v-model="unitIdList" filterable multiple placeholder="请选择使用单位">
              <el-option
                v-for="item in unitOptions"
                :key="item.unitId"
                :label="item.unitName"
                :value="item.unitId"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">楼栋：</div>
          <div class="query-input">
            <el-select v-model="buildId" filterable placeholder="请选择楼栋">
              <el-option
                v-for="item in buildOpts"
                :key="item.buildId"
                :label="item.buildName"
                :value="item.buildId"
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
        <el-table-column type="index" width="50" label="编号" align="center"></el-table-column>
        <el-table-column prop="houseTypeName" label="房屋类型" align="center"></el-table-column>
        <el-table-column prop="houseNum" label="房间数量" align="center"></el-table-column>
        <el-table-column prop="totalBuildArea" label="建筑面积(m²)" align="center"></el-table-column>
        <el-table-column prop="totalUseArea" label="使用面积(m²)" align="center"></el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          总计&nbsp;&nbsp;
          <span>房间数量:{{unitNums}}</span>
          <span>使用面积:{{houseUseAreaSum}}m²</span>
          <span>建筑面积:{{houseBuildAreaSum}}m²</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { getUnitList, selectHouseType, getBuildPositon } from "./api";
import { getYardTree } from "../home/api";
import { eventBus } from "@/eventBus/eventBus";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import treeSelect from "@/components/treeSelect/treeSelect_2";
import tableComponent from "@/components/table/table2.vue";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
export default {
  components: {
    searchBtn,
    resetBtn,
    treeSelect,
    tableBtnGroup,
    tablePagination,
    tableComponent
  },
  data() {
    return {
      unitIdList: [],
      buildId: null,
      unitOptions: [],
      treeShow: true,
      tableData: [],
      tableHeight: 100,
      loading: null,
      buildOpts: [],
      unitNums: 0,
      houseUseAreaSum: 0,
      houseBuildAreaSum: 0,
    };
  },
  created() {
    this.getUnit();
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
    GetBuildPositon() {
      getBuildPositon().then(res => {
        this.buildOpts = [];
        res.forEach(element => {
          if (element.type === 1) {
            this.buildOpts.push(element);
          }
        });
        const obj = {
          buildId: '',
          buildName: '全部'
        }
        this.buildOpts.unshift(obj);
      }).catch(err => {
        const msg = err.result ? err.result : '楼座名称获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    search() {
      this.getList()
    },
    reset() {
      this.unitIdList = []
      this.buildId = null
      this.getList()
    },
    //   获取使用单位列表
    getUnit() {
      getUnitList().then(res => {
        this.unitOptions = res;
      });
    },
    getList() {
      this.loadingStart('.table-box');
      selectHouseType(this.buildId, this.unitIdList)
        .then(res => {
          let unitNum = 0
          this.unitNums = 0
          this.houseUseAreaSum = 0
          this.houseBuildAreaSum = 0
          res.forEach(e => {
            if (e.useBuildArea === 1) {
              e.totalBuildArea = e.totalUseArea;
              e.totalUseArea = '-'
            }
          })
          res.forEach(e => {
            if (typeof e.totalUseArea === 'number') {
              this.houseUseAreaSum += e.totalUseArea
            }
            if (typeof e.totalBuildArea === 'number') {
              this.houseBuildAreaSum += e.totalBuildArea
            }
          })
          unitNum = res.length
          for (let i = 0; i < unitNum; i++) {
            this.unitNums += res[i].houseNum
          }
          this.houseUseAreaSum = this.houseUseAreaSum ? this.houseUseAreaSum.toFixed(2) : this.houseUseAreaSum
          this.houseBuildAreaSum = this.houseBuildAreaSum ? this.houseBuildAreaSum.toFixed(2) : this.houseBuildAreaSum

          this.tableData = res
          this.loadingEnd();
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '获取超标房间信息列表失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
  },
  mounted() {
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - queryBoxH - operateBoxH;
      this.GetBuildPositon()
      this.getList()
    }, 1);
  }
};
</script>
<style>
#houseType .query-box {
  position: relative;
  padding-bottom: 20px;
}
#houseType .query-item {
  font-size: 16px;
  /* height: 32px; */
  line-height: 32px;
  float: left;
  width: 20%;
}
#houseType .query-item > div {
  float: left;
}
#houseType .query-item > .query-input {
  width: calc(100% - 80px);
}
#houseType .query-item .el-select {
  width: 100%;
}
#houseType .query-item .el-date-editor.el-input,
#houseType .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#houseType .query-btn {
  float: left;
  margin-left: 50px;
}
#houseType .table-box .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#houseType .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
  height: 32px;
  line-height: 32px;
}
#houseType .operate-btn span {
  display: inline-block;
  margin-right: 40px;
}
#houseType .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
</style>
