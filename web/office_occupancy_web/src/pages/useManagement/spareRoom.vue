<template>
  <div id="spareRoom">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">房屋类型：</div>
          <div class="query-input">
            <el-select v-model="housetypeId" placeholder="请选择房屋类型">
              <el-option
                v-for="item in unitOptions"
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
            <el-select v-model="unitIdList" filterable multiple placeholder="请选择单位名称">
              <el-option
                v-for="item in unitArr"
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
            <tree-select
              @loadNode="loadNodes"
              @handleCheckChange="handleCheckChange"
              @handleNodeExpand="handleNodeExpand"
              :treeShow="treeShow"
              :inputWidth="'100%'"
              :inputHeight="'34px'"
            ></tree-select>
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
            :key="item.id"
            :show-overflow-tooltip="true"
          ></el-table-column>
        </el-table-column>
        <el-table-column prop="houseUseArea" label="使用面积(m²)" align="center"></el-table-column>
        <el-table-column prop="houseBuildAreas" label="建筑面积(m²)" align="center"></el-table-column>
        <el-table-column prop="housetypeName" label="房屋类型" align="center"></el-table-column>
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
import { SpareRoom, getUnitLists } from "./api";
import { getYardTree } from "../home/api";
import { getHouseTypeList } from "@/axios/commonApi";
import { eventBus } from "@/eventBus/eventBus";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import treeSelect from "@/components/treeSelect/treeSelect_2";
import tableComponent from "@/components/table/table2.vue";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import { log } from "util";
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
      housetypeId: "",
      unitIdList: [],
      unitArr: [],
      unitOptions: [],
      treeShow: true,
      form: {
        yardId: "",
        buildId: "",
        floorName: "",
        houseId: ""
      },
      loading: null,
      tableData: [],
      tableHeight: 0,
      currentPage: 1,
      pageSize: 15,
      total: 1,
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
      houseUseAreaSum: 0,
      houseBuildAreaSum: 0
    };
  },
  created() {
    this.getHouseType();
    // this.getList();
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
    //   获取使用单位列表
    getUnit() {
      getUnitLists().then(res => {
        this.unitArr = res;
      });
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
      eventBus.$emit("clearTreeSelectVal");
      this.housetypeId = ""
      this.form.yardId = ''
      this.form.buildId = ''
      this.form.floorName = ''
      this.form.houseId = ''
      this.unitIdList = []
      this.getList();
    },
    //获取使用凡物类型
    getHouseType() {
      getHouseTypeList().then(res => {
        this.unitOptions = res;
      });
    },
    getList() {
      this.loadingStart('.table-box');
      SpareRoom(this.currentPage, this.pageSize, this.form, this.housetypeId, this.unitIdList)
        .then(res => {
          this.loadingEnd();
          this.tableData = []
          this.total = 0
          this.houseBuildAreaSum = res.houseBuildAreaSum ? res.houseBuildAreaSum.toFixed(2) : 0
          this.houseUseAreaSum = res.houseUseAreaSum ? res.houseUseAreaSum.toFixed(2) : 0
          if (res.spareRoomDtoPage.records.length > 0) {
            this.tableData = res.spareRoomDtoPage.records;
            this.total = res.spareRoomDtoPage.total;
            this.tableData.forEach(item => {
              if (item.useBuildArea) {
                if (item.houseUseArea) {
                  item.houseBuildAreas = item.houseUseArea.toFixed(2);
                } else {
                  item.houseBuildAreas = '-'
                }
                item.houseUseArea = '-'
              } else {
                item.houseBuildAreas = (item.houseUseArea / item.buildCoe).toFixed(2);
                item.houseUseArea = item.houseUseArea ? item.houseUseArea.toFixed(2) : ""
              }
            });
          }
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
    //   加载树节点
    loadNodes(node, resolve) {
      if (node.level === 0) {
        getYardTree(null, 0, null)
          .then(res => {
            res.forEach(item => {
              if (!item.expand || item.expand === undefined) {
                item.leaf = true;
              }
            });
            return resolve(res);
          })
          .catch(err => {
            const msg = err.result ? err.result : '树获取失败';
            this.$message({
              message: msg,
              type: "error"
            });
          });
        setTimeout(() => {
          if (
            this.form.yardId &&
            !this.form.buildId &&
            !this.form.floorName &&
            !this.form.houseId
          ) {
            let tempObj = {};
            tempObj.level = 1;
            tempObj.id_yard = this.form.yardId;
            eventBus.$emit("setTreeDefaultVal", tempObj);
          } else if (
            this.form.yardId &&
            this.form.buildId &&
            !this.form.floorName &&
            !this.form.houseId
          ) {
            let tempObj = {};
            tempObj.level = 2;
            tempObj.id_yard = this.form.yardId;
            tempObj.id_build = `${this.form.yardId}:${this.form.buildId}`;
            eventBus.$emit("setTreeDefaultVal", tempObj);
          } else if (
            this.form.yardId &&
            this.form.buildId &&
            this.form.floorName &&
            !this.form.houseId
          ) {
            let tempObj = {};
            tempObj.level = 3;
            tempObj.id_yard = this.form.yardId;
            tempObj.id_build = `${this.form.yardId}:${this.form.buildId}`;
            tempObj.buildId = this.form.buildId;
            tempObj.id_floor = `${this.form.yardId}:${this.form.buildId}:${
              this.form.floorName
              }`;
            eventBus.$emit("setTreeDefaultVal", tempObj);
          } else if (
            this.form.yardId &&
            this.form.buildId &&
            this.form.floorName &&
            this.form.houseId
          ) {
            let tempObj = {};
            tempObj.level = 4;
            tempObj.id_yard = this.form.yardId;
            tempObj.id_build = `${this.form.yardId}:${this.form.buildId}`;
            tempObj.id_floor = `${this.form.yardId}:${this.form.buildId}:${
              this.form.floorName
              }`;
            tempObj.floorName = this.form.floorName;
            tempObj.id_house = this.form.houseId;
            eventBus.$emit("setTreeDefaultVal", tempObj);
          }
        }, 1000);
      } else if (node.level === 3) {
        const repairRemind_buildId =
          sessionStorage.getItem("repairRemind_buildId") - 0;
        getYardTree(node.data.value, 3, null, repairRemind_buildId)
          .then(res => {
            res.forEach(item => {
              if (!item.expand || item.expand === undefined) {
                item.leaf = true;
              }
            });
            return resolve(res);
          })
          .catch(err => {
            const msg = err.result ? err.result : '树获取失败';
            this.$message({
              message: msg,
              type: "error"
            });
          });
      } else {
        getYardTree(node.data.value, node.data.type, null)
          .then(res => {
            res.forEach(item => {
              if (!item.expand || item.expand === undefined) {
                item.leaf = true;
              }
            });
            return resolve(res);
          })
          .catch(err => {
            const msg = err.result ? err.result : '树获取失败';
            this.$message({
              message: msg,
              type: "error"
            });
          });
      }
    },
    // 选中树节点
    handleCheckChange(data) {
      this.form.yardId = ''
      this.form.buildId = ''
      this.form.floorName = ''
      this.form.houseId = ''
      this.currentLevel = data.type;
      if (data.type === 1) {
        this.form.yardId = data.value;
      } else if (data.type === 2) {
        this.form.buildId = data.value;
      } else if (data.type === 3) {
        this.form.floorName = data.value;
      } else {
        this.form.houseId = data.value;
      }
    },
    // 展开树节点
    handleNodeExpand(data, node) {
      if (node.level === 1) {
        this.form.yardId = data.value;
      } else if (node.level === 2) {
        this.form.buildId = data.value;
        sessionStorage.setItem("repairRemind_buildId", data.value);
      } else if (node.level === 3) {
        this.form.floorName = data.value;
      }
    }
  },
  mounted() {
    setTimeout(() => {
      this.getUnit()
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - queryBoxH - operateBoxH;
      this.getList()
    }, 1);
  }
};
</script>
<style>
#spareRoom .query-box {
  position: relative;
  margin-bottom: 20px;
}
#spareRoom .query-item {
  font-size: 16px;
  /* height: 32px; */
  line-height: 32px;
  float: left;
  width: 20%;
}
#spareRoom .query-item > div {
  float: left;
}
#spareRoom .query-item > .query-input {
  width: calc(100% - 80px);
}
#spareRoom .query-item .el-select {
  width: 100%;
}
#spareRoom .query-item .el-date-editor.el-input,
#spareRoom .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#spareRoom .query-btn {
  float: right;
}
#spareRoom .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#spareRoom .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
  height: 32px;
  line-height: 32px;
}
#spareRoom .operate-btn span {
  display: inline-block;
  margin-right: 40px;
}
#spareRoom .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
/* #spareRoom .query-box .el-select>.el-input{
  width: 200px;
} */
</style>
