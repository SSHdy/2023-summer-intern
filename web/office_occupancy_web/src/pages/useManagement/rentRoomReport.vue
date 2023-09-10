<template>
  <div id="rentRoomReport">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">房屋类型：</div>
          <div class="query-input">
            <el-select v-model="housetypeId" placeholder="请选择房屋类型">
              <el-option
                v-for="item in housetype"
                :key="item.housetypeId"
                :label="item.housetypeName"
                :value="item.housetypeId"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px">
          <div class="query-title">单位名称：</div>
          <div class="query-input">
            <el-select v-model="unitIdList" filterable multiple placeholder="请选择">
              <el-option
                v-for="item in unitOptions"
                :key="item.unitId"
                :label="item.unitName"
                :value="item.unitId"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px">
          <div class="query-title">位置：</div>
          <div class="query-input">
            <search-tree-select
              @handleCheckChange="handleSearchCheckChange"
              :treeData="allTreeData"
              :treeShow="true"
              :inputWidth="'100%'"
              :inputHeight="'32px'"
              :treeName="searchTreeName"
            ></search-tree-select>
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
        <el-table-column type="index" width="50" label="编号" align="center"></el-table-column>
        <el-table-column prop="position" label="位置" align="center">
          <el-table-column
            v-for="item in address"
            :label="item.label"
            align="center"
            :prop="item.prop"
            :width="item.width"
            :show-overflow-tooltip="true"
            :key="item.id"
          ></el-table-column>
        </el-table-column>
        <el-table-column prop="houseUseArea" label="使用面积(m²)" align="center"></el-table-column>
        <el-table-column prop="houseBuildArea" label="建筑面积(m²)" align="center"></el-table-column>
        <el-table-column prop="contacts" label="联系人" align="center"></el-table-column>
        <el-table-column prop="contactPhone" label="联系电话" align="center"></el-table-column>
        <el-table-column prop="lesseeYears" label="承租年限（年）" align="center"></el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          总计&nbsp;&nbsp;
          <span>房间数量:{{total}}</span>
          <span>使用面积:{{houseUseAreaSum}}m²</span>
          <span>建筑面积:{{houseBuildAreaSum}}m²</span>
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
import { getRentRoomReport, selectAllHandlePo, getUnitLists } from "./api";
import { getYardTree } from "../home/api";
import { getHouseTypeList, getUserUnitList } from "@/axios/commonApi";
import { eventBus } from "@/eventBus/eventBus";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import searchTreeSelect from "@/components/treeSelect/treeSelect_3";
import tableComponent from "@/components/table/table2.vue";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import { log } from "util";
export default {
  components: {
    searchBtn,
    resetBtn,
    searchTreeSelect,
    tableBtnGroup,
    tablePagination,
    tableComponent
  },
  data() {
    return {
      unitIdList: [],
      housetypeId: null,
      housetype: [],
      form: {
        yardId: "",
        buildId: "",
        floorName: "",
        houseId: ""
      },
      houseIds: [],
      floorNames: [],
      buildIds: [],
      yardIds: [],
      loading: null,
      tableData: [],
      tableHeight: 0,
      currentPage: 1,
      pageSize: 15,
      total: 1,
      houseUseAreaSum: 0,
      houseBuildAreaSum: 0,
      address: [
        {
          id: 1,
          label: "院落",
          prop: "yardName",
          width: 200
        },
        {
          id: 2,
          label: "楼座",
          prop: "buildName",
          width: 100
        },
        {
          id: 3,
          label: "楼层",
          prop: "floorName",
          width: 100
        },
        {
          id: 4,
          label: "房间",
          prop: "houseNumber",
          width: 100
        }
      ],
      allTreeData: [],
      searchTreeName: 'disposeSearchTree',
      unitOptions: []
    };
  },
  created() {
    this.getHouseType();
    this.getList();
    this.selectAllHandlePo();
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
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
    },
    // 改变页面条数
    changeSize(size) {
      this.currentPage = 1
      this.pageSize = size;
      this.getList();
    },
    // 改变页面页数
    changePage(page) {
      this.currentPage = page;
      this.getList();
    },
    search() {
      this.currentPage = 1;
      this.getList();
    },
    reset() {
      this.currentPage = 1;
      eventBus.$emit(`${this.searchTreeName}_clear`);
      this.housetypeId = null;
      this.yardIds = []
      this.floorNames = []
      this.buildIds = []
      this.houseIds = []
      this.form.yardId = "";
      this.form.buildId = "";
      this.form.floorName = "";
      this.form.houseId = "";
      this.unitIdList = []
      this.getList();
    },
    //获取使用房屋类型
    getHouseType() {
      getHouseTypeList().then(res => {
        this.housetype = res;
      });
    },
    getList() {
      this.loadingStart('.table-box');
      getRentRoomReport(
        this.currentPage,
        this.pageSize,
        this.yardIds,
        this.buildIds,
        this.floorNames,
        this.houseIds,
        this.housetypeId,
        this.unitIdList
      )
        .then(res => {
          this.loadingEnd();
          if (res.spareRoomDtoPage.records.length > 0) {
            this.tableData = res.spareRoomDtoPage.records;
            this.total = res.spareRoomDtoPage.total;
            this.houseUseAreaSum = res.houseUseAreaSum ? res.houseUseAreaSum.toFixed(2) : 0;
            this.houseBuildAreaSum = res.houseBuildAreaSum ? res.houseBuildAreaSum.toFixed(2) : 0;
            this.tableData.forEach(item => {
              if (item.useBuildArea) {
                if (item.houseUseArea) {
                  item.houseBuildArea = item.houseUseArea.toFixed(2);
                } else {
                  item.houseBuildArea = '-'
                }
                item.houseUseArea = '-'
              } else {
                item.houseBuildArea = item.houseBuildArea ? item.houseBuildArea : '-'
                item.houseUseArea = item.houseUseArea ? item.houseUseArea : '-'
              }
            });
          } else {
            this.tableData = [];
            this.total = 0;
          }
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : "获取超标房间信息列表失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取搜索树的数据
    selectAllHandlePo() {
      selectAllHandlePo().then(res => {
        this.allTreeData = res;
      }).catch(err => {
        const msg = err.result ? err.result : '处置位置获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    handleSearchCheckChange(checkedNodes, halfCheckedNodes) {
      this.houseIds = [];
      const nodes = checkedNodes.concat(halfCheckedNodes);
      nodes.forEach(node => {
        if (node.type === 3) {
          this.houseIds.push(node.value);
        } else if (node.type === 2) {
          this.floorNames.push(node.value);
        } else if (node.type === 1) {
          this.buildIds.push(node.value);
        } else if (node.type === 0) {
          this.yardIds.push(node.value);
        }
      });
    },
    // 用户权限下的单位列表
    getUserUnitList() {
      getUnitLists().then(res => {
        this.unitOptions = res;
      }).catch(err => {
        const msg = err.result ? err.result : '单位获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    }
  },
  mounted() {
    this.getUserUnitList();
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
#rentRoomReport .query-box {
  position: relative;
  margin-bottom: 20px;
}
#rentRoomReport .query-item {
  font-size: 16px;
  /* height: 32px; */
  line-height: 32px;
  float: left;
  width: 20%;
}
#rentRoomReport .query-item > div {
  float: left;
}
#rentRoomReport .query-item > .query-input {
  width: calc(100% - 80px);
}
#rentRoomReport .query-item .el-select {
  width: 100%;
}
#rentRoomReport .query-item .el-date-editor.el-input,
#rentRoomReport .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#rentRoomReport .query-btn {
  float: right;
}
#rentRoomReport .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#rentRoomReport .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
  height: 32px;
  line-height: 32px;
}
#rentRoomReport .operate-btn span {
  display: inline-block;
  margin-right: 40px;
}
#rentRoomReport .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
/* #rentRoomReport .query-box .el-input,
#rentRoomReport .query-box  .el-input__inner{
  width: 200px ;
} */
</style>
