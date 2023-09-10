<template>
  <div id="houseDetail">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">单位名称：</div>
          <div class="query-input">
            <el-select filterable v-model="unitName" placeholder="请选择单位名称" style="width:100%">
              <el-option
                v-for="item in unitOptions"
                :key="item.unitId"
                :label="item.unitName"
                :value="item.unitId"
              ></el-option>
            </el-select>
          </div>
        </div>
        <!-- <div class="query-item" style="margin-left:20px">
          <div class="query-title">用房人员级别：</div>
          <div class="query-input">
            <el-select v-model="isLeading" placeholder="请选择用房人员级别" style="width:100%">
              <el-option
                v-for="item in isLeadingOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
        </div> -->
        <div class="query-item" style="margin-left:50px">
          <div class="query-title">使用人员名称：</div>
          <div class="query-input">
            <el-input v-model="usePerson" placeholder="请输入使用人员名称" v-enter-trim></el-input>
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
        <el-table-column type="index" width="50" label="序号" align="center"></el-table-column>
        <el-table-column prop="yardName" label="院落名称" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="buildName" label="楼座名称" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="floorName" label="楼层" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column
          prop="houseNumber"
          label="房间号"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="houseUseArea"
          label="使用面积(m²)"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="unitName"
          label="单位名称"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="usePerson"
          label="使用人员名称"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
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
import { getRoomDetailList } from "./api";
import { getUserUnitList } from "@/axios/commonApi";
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
    tableComponent
  },
  data() {
    return {
      unitName: "",
      usePerson: "",
      isLeading: "",
      unitOptions: [],
      loading: null,
      tableData: [],
      tableHeight: 0,
      currentPage: 1,
      pageSize: 15,
      total: 0,
      // isLeadingOptions: [
      //   {
      //     id: '',
      //     name: '全部'
      //   },
      //   {
      //     id: 0,
      //     name: '领导'
      //   },
      //   {
      //     id: 1,
      //     name: '一般人员'
      //   }
      // ]
    };
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
    search() {
      this.currentPage = 1;
      this.getList();
    },
    reset() {
      this.currentPage = 1;
      this.unitName = "";
      this.isLeading = "";
      this.usePerson = "";
      this.getList();
    },
    // 用户权限下的单位字典项
    getUserUnitDic() {
      getUserUnitList().then(res => {
        this.unitOptions = res;
        const obj = {
          unitId: '',
          unitName: '全部'
        }
        this.unitOptions.unshift(obj);
      }).catch(err => {
        const msg = err.result ? err.result : '单位下拉选项获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    getList() {
      this.loadingStart('.table-box');
      getRoomDetailList(this.currentPage, this.pageSize, this.unitName, this.isLeading, this.usePerson, null, null, null, null).then(res => {
        this.tableData = res.records;
        this.total = res.total;
        this.tableData.forEach(item => {
          if (item.isLeading === 0) {
            item.isLeadingTxt = '领导';
          } else {
            item.isLeadingTxt = '一般人员';
          }
        });
        this.loadingEnd();
      }).catch(err => {
        this.loadingEnd();
        const msg = err.result ? err.result : '超标单位列表获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      });
    }
  },
  mounted() {
    this.getUserUnitDic();
    this.getList();
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
#houseDetail .query-box {
  margin-bottom: 20px;
}
#houseDetail .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#houseDetail .query-item > div {
  float: left;
}
#houseDetail .query-item > .query-input {
  width: calc(100% - 112px);
}
#houseDetail .query-item .el-date-editor.el-input,
#houseDetail .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#houseDetail .query-btn {
  float: right;
}
#houseDetail .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#houseDetail .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#houseDetail .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#houseDetail .el-table .cell {
  line-height: inherit;
}
#houseDetail .el-table th div {
  line-height: inherit;
}
</style>
