<template>
  <div id="unitOverproof">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">使用单位：</div>
          <div class="query-input">
            <el-select filterable multiple v-model="unitIdList" placeholder="请选择使用单位">
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
          <div class="query-title">坐落地址：</div>
          <div class="query-input">
            <el-input v-model="yardPosition" placeholder="请输入坐落地址" v-enter-trim></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px">
          <div class="query-title">楼座：</div>
          <div class="query-input">
            <el-select filterable v-model="buildId" placeholder="请选择楼座">
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
        <el-table-column prop="specifiedArea" label="标准面积(m²)" align="center"></el-table-column>
        <el-table-column prop="overproofArea" label="超标面积(m²)" align="center"></el-table-column>
        <el-table-column prop="yardPosition" label="坐落地址" align="center"></el-table-column>
        <el-table-column prop="housetypeName" label="房屋类型" align="center"></el-table-column>
        <el-table-column prop="units" label="单位名称" align="center"></el-table-column>
        <el-table-column prop="leadingOfficeName" label="使用者姓名" align="center"></el-table-column>
        <el-table-column prop="professionalName" label="级别" align="center"></el-table-column>
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
import { getUnitList, getHouseProof } from "./api";
import { getYardTree } from "../home/api";
import { getHouseTypeList, getBuildPositon } from "@/axios/commonApi";
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
      buildId: '',
      buildName: '',
      housetypeId: "",
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
      yardPosition: '',
      unitIdList: [],
      buildOpts: []
    };
  },
  created() {
    this.getUnit();
    this.getList();
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
      this.unitIdList = [];
      this.yardPosition = "";
      this.buildId = "";
      this.getList();
    },
    // 获取使用单位列表
    getUnit() {
      getUnitList().then(res => {
        this.unitOptions = res;
      });
    },
    getList() {
      this.loadingStart('.table-box');
      getHouseProof(this.currentPage, this.pageSize, this.unitIdList, this.yardPosition, this.buildId)
        .then(res => {
          this.loadingEnd();
          if (res.records.length > 0) {
            this.tableData = res.records;
            this.total = res.total;
            res.records.forEach(item => {
              item['unit'] = [];
              item['units'] = '';
              item.unitNames.forEach(items => {
                item['unit'].push(items.unitName)
              });
              item['units'] = item['unit'].join(',');
              item.houseUseArea = item.houseUseArea.toFixed(2);
              item.specifiedArea = item.specifiedArea.toFixed(2);
              item.overproofArea = (item.houseUseArea - item.specifiedArea).toFixed(2);
            })
          } else {
            this.tableData = []
            this.total = 0
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
    // 加载树节点
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
    },
    // 获取楼座字典项
    getBuildPositon() {
      getBuildPositon()
        .then(res => {
          this.buildOpts = [];
          res.forEach(element => {
            if (element.type === 1) {
              this.buildOpts.push(element);
            }
          });
          // const obj = {
          //   buildId: '',
          //   buildName: '全部'
          // }
          // this.buildOpts.unshift(obj);
        })
        .catch(err => {
          const msg = err.result ? err.result : '楼座名称获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    }
  },
  mounted() {
    this.getBuildPositon();
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
#unitOverproof .query-box {
  position: relative;
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
#unitOverproof .query-item .el-select {
  width: 100%;
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
</style>
