<template>
  <div id="assetStorage">
    <el-tabs v-model="activeName2" type="card" @tab-click="handleClick">
      <el-tab-pane label="车辆" name="first">
        <export-btn
          style="float:right;margin-bottom:10px;"
          :fontSize="17"
          :showIcon="false"
          padding="12px 30px"
          @clickBtn="exportExcel(1)"
        ></export-btn>
        <div class="query-box" id="queryBox">
          <div class="clearfix" style="margin-bottom:30px;">
            <div class="table-box">
              <table-component
                :tableData="tableData1"
                :tableHeight="tableHeight"
                @select="handleSelectionChange"
              >
                <el-table-column type="selection" width="50" align="center"></el-table-column>
                <el-table-column prop="assetNameComn" label="资产名称" align="center"></el-table-column>
                <el-table-column prop="assetsType" label="资产类别" align="center"></el-table-column>
                <el-table-column prop="categoryCodeComn" label="资产分类代码" align="center"></el-table-column>
                <el-table-column prop="categoryName" label="资产分类名称" align="center"></el-table-column>
                <el-table-column prop="procurementModeComn" label="采购组织形式" align="center"></el-table-column>
                <el-table-column prop="budgetItemNumberComn" label="预算项目编号" align="center"></el-table-column>
                <el-table-column prop="valueTypeComn" label="价值类型" align="center"></el-table-column>
                <el-table-column prop="assetNumberComn" label="数量" align="center"></el-table-column>
                <el-table-column prop="assetWorthComn" label="价值" align="center"></el-table-column>
                <!-- <el-table-column prop="assetRemarksComn" label="备注" align="center"></el-table-column> -->
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
        </div>
      </el-tab-pane>
      <el-tab-pane label="房屋" name="second">
        <export-btn
          style="float:right;margin-bottom:10px;"
          :fontSize="17"
          :showIcon="false"
          padding="12px 30px"
          @clickBtn="exportExcel(2)"
        ></export-btn>
        <div class="query-box" id="queryBox">
          <div class="clearfix" style="margin-bottom:30px;">
            <div class="table-box">
              <table-component
                :tableData="tableData2"
                :tableHeight="tableHeight"
                @select="handleSelectionChange"
              >
                <el-table-column type="selection" width="50" align="center"></el-table-column>
                <el-table-column prop="assetNameComn" label="资产名称" align="center"></el-table-column>
                <el-table-column prop="assetsType" label="资产类别" align="center"></el-table-column>
                <el-table-column prop="categoryCodeComn" label="资产分类代码" align="center"></el-table-column>
                <el-table-column prop="categoryName" label="资产分类名称" align="center"></el-table-column>
                <el-table-column prop="procurementModeComn" label="采购组织形式" align="center"></el-table-column>
                <el-table-column prop="budgetItemNumberComn" label="预算项目编号" align="center"></el-table-column>
                <el-table-column prop="valueTypeComn" label="价值类型" align="center"></el-table-column>
                <el-table-column prop="assetNumberComn" label="数量" align="center"></el-table-column>
                <el-table-column prop="assetWorthComn" label="价值" align="center"></el-table-column>
                <!-- <el-table-column prop="assetRemarksComn" label="备注" align="center"></el-table-column> -->
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
        </div>
      </el-tab-pane>
      <el-tab-pane label="构建物" name="third">
        <export-btn
          style="float:right;margin-bottom:10px;"
          :fontSize="17"
          :showIcon="false"
          padding="12px 30px"
          @clickBtn="exportExcel(3)"
        ></export-btn>
        <div class="query-box" id="queryBox">
          <div class="clearfix" style="margin-bottom:30px;">
            <div class="table-box">
              <table-component
                :tableData="tableData3"
                :tableHeight="tableHeight"
                @select="handleSelectionChange"
              >
                <el-table-column type="selection" width="50" align="center"></el-table-column>
                <el-table-column prop="assetNameComn" label="资产名称" align="center"></el-table-column>
                <el-table-column prop="assetsType" label="资产类别" align="center"></el-table-column>
                <el-table-column prop="categoryCodeComn" label="资产分类代码" align="center"></el-table-column>
                <el-table-column prop="categoryName" label="资产分类名称" align="center"></el-table-column>
                <el-table-column prop="procurementModeComn" label="采购组织形式" align="center"></el-table-column>
                <el-table-column prop="budgetItemNumberComn" label="预算项目编号" align="center"></el-table-column>
                <el-table-column prop="valueTypeComn" label="价值类型" align="center"></el-table-column>
                <el-table-column prop="assetNumberComn" label="数量" align="center"></el-table-column>
                <el-table-column prop="assetWorthComn" label="价值" align="center"></el-table-column>
                <!-- <el-table-column prop="assetRemarksComn" label="备注" align="center"></el-table-column> -->
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
        </div>
      </el-tab-pane>
      <el-tab-pane label="家具、用具、装具" name="fourth">
        <export-btn
          style="float:right;margin-bottom:10px;"
          :fontSize="17"
          :showIcon="false"
          padding="12px 30px"
          @clickBtn="exportExcel(4)"
        ></export-btn>
        <div class="query-box" id="queryBox">
          <div class="clearfix" style="margin-bottom:30px;">
            <div class="table-box">
              <table-component
                :tableData="tableData4"
                :tableHeight="tableHeight"
                @select="handleSelectionChange"
              >
                <el-table-column type="selection" width="50" align="center"></el-table-column>
                <el-table-column prop="assetNameComn" label="资产名称" align="center"></el-table-column>
                <el-table-column prop="assetsType" label="资产类别" align="center"></el-table-column>
                <el-table-column prop="categoryCodeComn" label="资产分类代码" align="center"></el-table-column>
                <el-table-column prop="categoryName" label="资产分类名称" align="center"></el-table-column>
                <el-table-column prop="procurementModeComn" label="采购组织形式" align="center"></el-table-column>
                <el-table-column prop="budgetItemNumberComn" label="预算项目编号" align="center"></el-table-column>
                <el-table-column prop="valueTypeComn" label="价值类型" align="center"></el-table-column>
                <el-table-column prop="assetNumberComn" label="数量" align="center"></el-table-column>
                <el-table-column prop="assetWorthComn" label="价值" align="center"></el-table-column>
                <!-- <el-table-column prop="assetRemarksComn" label="备注" align="center"></el-table-column> -->
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
        </div>
      </el-tab-pane>
      <el-tab-pane label="通用设备" name="fifth">
        <export-btn
          style="float:right;margin-bottom:10px;"
          :fontSize="17"
          :showIcon="false"
          padding="12px 30px"
          @clickBtn="exportExcel(5)"
        ></export-btn>
        <div class="query-box" id="queryBox">
          <div class="clearfix" style="margin-bottom:30px;">
            <div class="table-box">
              <table-component
                :tableData="tableData5"
                :tableHeight="tableHeight"
                @select="handleSelectionChange"
              >
                <el-table-column type="selection" width="50" align="center"></el-table-column>
                <el-table-column prop="assetNameComn" label="资产名称" align="center"></el-table-column>
                <el-table-column prop="assetsType" label="资产类别" align="center"></el-table-column>
                <el-table-column prop="categoryCodeComn" label="资产分类代码" align="center"></el-table-column>
                <el-table-column prop="categoryName" label="资产分类名称" align="center"></el-table-column>
                <el-table-column prop="procurementModeComn" label="采购组织形式" align="center"></el-table-column>
                <el-table-column prop="budgetItemNumberComn" label="预算项目编号" align="center"></el-table-column>
                <el-table-column prop="valueTypeComn" label="价值类型" align="center"></el-table-column>
                <el-table-column prop="assetNumberComn" label="数量" align="center"></el-table-column>
                <el-table-column prop="assetWorthComn" label="价值" align="center"></el-table-column>
                <!-- <el-table-column prop="assetRemarksComn" label="备注" align="center"></el-table-column> -->
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
        </div>
      </el-tab-pane>
      <el-tab-pane label="专用设备" name="sixth">
        <export-btn
          style="float:right;margin-bottom:10px;"
          :fontSize="17"
          :showIcon="false"
          padding="12px 30px"
          @clickBtn="exportExcel(6)"
        ></export-btn>
        <div class="query-box" id="queryBox">
          <div class="clearfix" style="margin-bottom:30px;">
            <div class="table-box">
              <table-component
                :tableData="tableData6"
                :tableHeight="tableHeight"
                @select="handleSelectionChange"
              >
                <el-table-column type="selection" width="50" align="center"></el-table-column>
                <el-table-column prop="assetNameComn" label="资产名称" align="center"></el-table-column>
                <el-table-column prop="assetsType" label="资产类别" align="center"></el-table-column>
                <el-table-column prop="categoryCodeComn" label="资产分类代码" align="center"></el-table-column>
                <el-table-column prop="categoryName" label="资产分类名称" align="center"></el-table-column>
                <el-table-column prop="procurementModeComn" label="采购组织形式" align="center"></el-table-column>
                <el-table-column prop="budgetItemNumberComn" label="预算项目编号" align="center"></el-table-column>
                <el-table-column prop="valueTypeComn" label="价值类型" align="center"></el-table-column>
                <el-table-column prop="assetNumberComn" label="数量" align="center"></el-table-column>
                <el-table-column prop="assetWorthComn" label="价值" align="center"></el-table-column>
                <!-- <el-table-column prop="assetRemarksComn" label="备注" align="center"></el-table-column> -->
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
        </div>
      </el-tab-pane>
      <el-tab-pane label="土地使用权" name="seventh">
        <export-btn
          style="float:right;margin-bottom:10px;"
          :fontSize="17"
          :showIcon="false"
          padding="12px 30px"
          @clickBtn="exportExcel(7)"
        ></export-btn>
        <div class="query-box" id="queryBox">
          <div class="clearfix" style="margin-bottom:30px;">
            <div class="table-box">
              <table-component
                :tableData="tableData7"
                :tableHeight="tableHeight"
                @select="handleSelectionChange"
              >
                <el-table-column type="selection" width="50" align="center"></el-table-column>
                <el-table-column prop="assetNameComn" label="资产名称" align="center"></el-table-column>
                <el-table-column prop="assetsType" label="资产类别" align="center"></el-table-column>
                <el-table-column prop="categoryCodeComn" label="资产分类代码" align="center"></el-table-column>
                <el-table-column prop="categoryName" label="资产分类名称" align="center"></el-table-column>
                <el-table-column prop="procurementModeComn" label="采购组织形式" align="center"></el-table-column>
                <el-table-column prop="budgetItemNumberComn" label="预算项目编号" align="center"></el-table-column>
                <el-table-column prop="valueTypeComn" label="价值类型" align="center"></el-table-column>
                <el-table-column prop="assetNumberComn" label="数量" align="center"></el-table-column>
                <el-table-column prop="assetWorthComn" label="价值" align="center"></el-table-column>
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
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import tableComponent from "@/components/table/table2.vue";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import assetDetail from "@/components/assets/assetsDetail";
import exportBtn from "@/components/button/exportBtn";
import { storageAsset, exportAssetsExcel, getAssetDetailInfo } from "./api";
export default {
  components: {
    tableComponent,
    tableBtnGroup,
    tablePagination,
    singleBtnGroup,
    assetDetail,
    exportBtn
  },
  data() {
    return {
      activeName2: "first",
      loading: true,
      tableData1: [],
      tableData2: [],
      tableData3: [],
      tableData4: [],
      tableData5: [],
      tableData6: [],
      tableData7: [],
      tableHeight: 0,
      currentPage: 1,
      pageSize: 5,
      total: 0,
      assetsType: 1,
      list: []
    };
  },
  created() {
    this.getStorageAssetList();
  },
  methods: {
    // 获取入库列表
    getStorageAssetList() {
      this.loading = true;
      storageAsset(this.assetsType, this.currentPage, this.pageSize).then(
        res => {
          res.records.forEach(item => {
            if (item.assetWorthComn) {
              item.assetWorthComn = parseFloat(item.assetWorthComn.toFixed(2)).toLocaleString()
            }
          })
          if (this.assetsType == 1) {
            this.tableData1 = res.records;
          } else if (this.assetsType == 2) {
            this.tableData2 = res.records;
          } else if (this.assetsType == 3) {
            this.tableData3 = res.records;
          } else if (this.assetsType == 4) {
            this.tableData4 = res.records;
          } else if (this.assetsType == 5) {
            this.tableData5 = res.records;
          } else if (this.assetsType == 6) {
            this.tableData6 = res.records;
          } else if (this.assetsType == 7) {
            this.tableData7 = res.records;
          }
          this.total = res.total;
        }
      );
    },
    handleClick(tab, event) {
      this.assetsType = parseInt(tab.index) + 1;
      this.currentPage = 1
      this.pageSize = 5
      this.getStorageAssetList();
    },
    handleSelectionChange(data) {
      var listist = [];
      data.forEach(i => {
        listist.push(i.id);
      });
      this.list = listist;
    },
    // 改变页面条数
    changeSize(size) {
      this.currentPage = 1
      this.pageSize = size;
      this.getStorageAssetList();
    },
    // 改变页面页数
    changePage(page) {
      this.currentPage = page;
      this.getStorageAssetList();
    },
    exportExcel(assetsType) {
      if (this.list.length) {
        exportAssetsExcel(this.list, assetsType).then(res => {
          let fileDownload = require('js-file-download');
          let name = decodeURIComponent(sessionStorage.getItem('CADdownloadFileName'));
          fileDownload(res, name);
        }).catch(err => {
          const msg = err.result ? err.result : '导出数据失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
      } else {
        this.$message({
          type: 'info',
          message: "请勾选数据进行导出"
        })
      }
    }
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
#assetStorage .query-box {
  margin-bottom: 20px;
}
#assetStorage .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#assetStorage .query-item > div {
  float: left;
}
#assetStorage .query-item > .query-input {
  width: calc(100% - 80px);
}
#assetStorage .query-item .el-date-editor.el-input,
#assetStorage .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#assetStorage .query-btn {
  float: left;
  margin-left: 50px;
}
#assetStorage .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#assetStorage .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#assetStorage .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#assetStorage .el-tabs--card > .el-tabs__header {
  border-bottom: none;
  margin-bottom: 0;
}
#assetStorage .el-tabs--card > .el-tabs__header .el-tabs__nav {
  border: none;
}
#assetStorage .el-tabs--card > .el-tabs__header .el-tabs__item {
  border-left: none;
  border-bottom: none;
  width: 180px;
  background-color: #ecf0f1;
  text-align: center;
  font-size: 17px;
  color: #666;
}
#assetStorage .el-tabs--card > .el-tabs__header .el-tabs__item.is-active {
  background-color: #bee2f4;
  color: #0593e7;
}
#assetStorage
  .el-tabs--card
  > .el-tabs__header
  .el-tabs__item
  + .el-tabs__item {
  margin-left: 4px;
}
/* #assetStorage .dialog-body {
  height: 600px;
  overflow-y: auto;
} */
</style>
