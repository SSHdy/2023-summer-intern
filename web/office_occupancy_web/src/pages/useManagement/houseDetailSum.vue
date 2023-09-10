<template>
  <div id="houseDetailSum">
    <div class="table-box">
      <export-btn @clickBtn="openExportDialog" class="export-btn" v-show="showExportBtn"></export-btn>
      <table-component :tableData="tableData" :tableHeight="tableHeight">
        <el-table-column width="50" label="序号" align="center" type="index"></el-table-column>
        <el-table-column prop="unitName" label="单位名称" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column
          prop="unitComplement"
          label="核定编制"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="unitTypeName"
          label="单位性质"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column prop="unitGrade" label="单位级别" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column
          label="办公地点"
          align="center"
          :show-overflow-tooltip="true"
          class-name="table-cell-special"
          width="320"
        >
          <template slot-scope="scope">
            <div
              v-for="(item, index) in scope.row.areaDetailDtoList"
              :key="index"
              class="table-column-item"
            >{{item.officeLocation}}</div>
          </template>
        </el-table-column>
        <el-table-column label="面积(㎡)" align="center">
          <el-table-column
            label="建筑面积"
            align="center"
            :show-overflow-tooltip="true"
            class-name="table-cell-special"
          >
            <template slot-scope="scope">
              <div
                v-for="(item, index) in scope.row.areaDetailDtoList"
                :key="index"
                class="table-column-item"
              >{{item.buildArea}}</div>
            </template>
          </el-table-column>
          <el-table-column
            label="基本办公用房"
            align="center"
            :show-overflow-tooltip="true"
            class-name="table-cell-special"
          >
            <template slot-scope="scope">
              <div
                v-for="(item, index) in scope.row.areaDetailDtoList"
                :key="index"
                class="table-column-item"
              >{{item.basicOfficeArea}}</div>
            </template>
          </el-table-column>
          <el-table-column
            label="附属用房"
            align="center"
            :show-overflow-tooltip="true"
            class-name="table-cell-special"
          >
            <template slot-scope="scope">
              <div
                v-for="(item, index) in scope.row.areaDetailDtoList"
                :key="index"
                class="table-column-item"
              >{{item.subsidiaryArea}}</div>
            </template>
          </el-table-column>
          <el-table-column
            label="业务用房"
            align="center"
            :show-overflow-tooltip="true"
            class-name="table-cell-special"
          >
            <template slot-scope="scope">
              <div
                v-for="(item, index) in scope.row.areaDetailDtoList"
                :key="index"
                class="table-column-item"
              >{{item.businessArea}}</div>
            </template>
          </el-table-column>
          <el-table-column
            label="出租"
            align="center"
            :show-overflow-tooltip="true"
            class-name="table-cell-special"
          >
            <template slot-scope="scope">
              <div
                v-for="(item, index) in scope.row.areaDetailDtoList"
                :key="index"
                class="table-column-item"
              >{{item.rentArea}}</div>
            </template>
          </el-table-column>
          <el-table-column
            label="空置"
            align="center"
            :show-overflow-tooltip="true"
            class-name="table-cell-special"
          >
            <template slot-scope="scope">
              <div
                v-for="(item, index) in scope.row.areaDetailDtoList"
                :key="index"
                class="table-column-item"
              >{{item.spareArea}}</div>
            </template>
          </el-table-column>
          <el-table-column
            label="出借"
            align="center"
            :show-overflow-tooltip="true"
            class-name="table-cell-special"
          >
            <template slot-scope="scope">
              <div
                v-for="(item, index) in scope.row.areaDetailDtoList"
                :key="index"
                class="table-column-item"
              >{{item.lendArea}}</div>
            </template>
          </el-table-column>
        </el-table-column>
      </table-component>
    </div>
  </div>
</template>
<script>
import { selectHouseDetailByUnit, exportHouseDetailExcel } from "./api";
import exportBtn from "@/components/button/exportBtn";
import tableComponent from "@/components/table/table2.vue";
import { log } from "util";
export default {
  components: {
    exportBtn,
    tableComponent
  },
  data() {
    return {
      loading: null,
      tableData: [],
      tableHeight: 0,
      showExportBtn: false
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
          customClass: "loading-box"
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
    getList() {
      this.loadingStart(".table-box");
      selectHouseDetailByUnit()
        .then(res => {
          if (res.length > 0) {
            this.showExportBtn = true;
          }
          this.tableData = res;
          this.loadingEnd();
        }).catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : "房间明细汇总信息获取失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 导出
    openExportDialog() {
      exportHouseDetailExcel(this.tableData).then(res => {
        let fileDownload = require('js-file-download');
        let name = decodeURIComponent(sessionStorage.getItem('CADdownloadFileName'));
        fileDownload(res, name);
        this.$message({
          message: '导出成功',
          type: "success"
        });
        this.dialogVisible = false;
      }).catch(err => {
        const msg = err.result ? err.result : '导出数据失败';
        this.$message({
          message: msg,
          type: "error"
        });
      });
    }
  },
  mounted() {
    this.getList();
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let btnH = $("#houseDetailSum").find('.export-btn').outerHeight(true);
      this.tableHeight = mainDivH - btnH;
    }, 1);
  }
};
</script>
<style>
#houseDetailSum .export-btn {
  margin-bottom: 20px;
  margin-right: 0;
  float: right;
}
#houseDetailSum .query-box {
  margin-bottom: 20px;
}
#houseDetailSum .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#houseDetailSum .query-item > div {
  float: left;
}
#houseDetailSum .query-item > .query-input {
  width: calc(100% - 80px);
}
#houseDetailSum .query-item .el-date-editor.el-input,
#houseDetailSum .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#houseDetailSum .query-btn {
  float: right;
}
#houseDetailSum .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#houseDetailSum .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#houseDetailSum .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#houseDetailSum .el-table .cell {
  line-height: inherit;
  padding-left: 5px;
  padding-right: 5px;
}
#houseDetailSum .el-table th div {
  line-height: inherit;
}
#houseDetailSum .table-column-item {
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  border-bottom: 1px solid #c8c8c8;
  line-height: 30px;
}
#houseDetailSum .table-column-item:last-child {
  border-bottom: none;
}
#houseDetailSum .table-cell-special {
  padding-top: 0 !important;
  padding-bottom: 0 !important;
}
#houseDetailSum .table-cell-special .cell {
  padding-left: 0;
  padding-right: 0;
}
</style>
