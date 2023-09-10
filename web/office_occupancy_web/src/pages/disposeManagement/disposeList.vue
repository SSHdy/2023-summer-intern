<template>
  <div id="disposeList">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">处置位置：</div>
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
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">处置方式：</div>
          <div class="query-input">
            <el-select v-model="handleWay" placeholder="请选择处置方式" style="width:100%">
              <el-option
                v-for="item in handleWayOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">申请时间：</div>
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
    <div class="table-box">
      <table-component :tableData="tableData" :tableHeight="tableHeight">
        <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="yardPosition"
          label="处置院落"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="buildPosition"
          label="处置楼座"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="handleWayText"
          label="处置方式"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="handleDate"
          label="申请时间"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="handleArea"
          label="处置面积(m²)"
          align="center"
        ></el-table-column>
        <el-table-column label="申请状态" width="140" align="center">
          <template slot-scope="scope">
            <span
              :class="{'status-unreviewed' : scope.row.applyStatus===0,'status-pass' : scope.row.applyStatus===1,'status-notPass' : scope.row.applyStatus===2,'status-reviewimg' : scope.row.applyStatus===3}"
            >{{scope.row.applyStatusText}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDelete="false"
              :isShowDetail="true"
              :isShowAlter="false"
              @detailClick="singleDetail(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
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
    <!-- 详情 -->
    <el-dialog
      :visible.sync="detailDialogVisible"
      :close-on-click-modal="false"
      width="780px"
      @closed="detailDialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>处置详情</span>
      </div>
      <div class="dialog-content">
        <div class="common-info-box">
          <div class="content-item">
            <span>处置院落：</span>
            <span>{{commonInfo.yardPosition}}</span>
          </div>
          <div class="content-item">
            <span>处置位置：</span>
            <el-tooltip
              class="item"
              effect="dark"
              :content="commonInfo.handlePosition"
              placement="top"
            >
              <span>{{commonInfo.handlePosition}}</span>
            </el-tooltip>
          </div>
          <div class="content-item">
            <span>处置面积：</span>
            <span>{{commonInfo.handleArea}}m²</span>
          </div>
          <div class="content-item">
            <span>申请人：</span>
            <span>{{commonInfo.handleApplicant}}</span>
          </div>
          <div class="content-item">
            <span>申请时间：</span>
            <span>{{commonInfo.handleDate}}</span>
          </div>
          <div class="content-item">
            <span>申请单位：</span>
            <span>{{commonInfo.unitName}}</span>
          </div>
        </div>
        <div class="purposes-info-box" v-if="isShow.conversionPurposesDb">
          <div class="content-item">
            <span>处置方式：</span>
            <span>转换用途</span>
          </div>
          <div class="content-item">
            <span>登记人：</span>
            <span>{{conversionPurposesDb.cpsUser}}</span>
          </div>
          <div class="content-item">
            <span>登记时间：</span>
            <span>{{conversionPurposesDb.cpsTime}}</span>
          </div>
          <div class="content-item">
            <span>转换后用途：</span>
            <span>{{conversionPurposesDb.ponUse}}</span>
          </div>
          <div class="content-item" style="width:100%;">
            <span>备注：</span>
            <span>{{conversionPurposesDb.cpsComment ? conversionPurposesDb.cpsComment : '暂无'}}</span>
          </div>
          <div
            class="content-item"
            style="width:100%;"
            v-if="conversionPurposesDb.conPurposesData && conversionPurposesDb.conPurposesData.length > 0"
          >
            <span>资料登记：</span>
            <div class="file-download">
              <el-checkbox-group
                v-model="purposesDownLoadCheckedList"
                @change="purposesCheckedFileChange"
              >
                <div
                  v-for="(file, index) in conversionPurposesDb.conPurposesData"
                  :key="index"
                  class="file-download-item"
                >
                  <el-checkbox
                    :label="`${file.physicalAddress},${file.repairFileName}`"
                  >{{file.repairFileName}}</el-checkbox>
                </div>
              </el-checkbox-group>
              <el-button
                size="small"
                type="primary"
                @click="fileDownLoad(purposesDownloadList, 2)"
              >点击下载文件</el-button>
            </div>
          </div>
        </div>
        <div class="auction-info-box" v-if="isShow.auctionDb">
          <div class="content-item">
            <span>处置方式：</span>
            <span>拍卖</span>
          </div>
          <div class="content-item">
            <span>登记人：</span>
            <span>{{auctionDb.auctionUser}}</span>
          </div>
          <div class="content-item">
            <span>登记时间：</span>
            <span>{{auctionDb.auctionTime}}</span>
          </div>
          <div class="content-item">
            <span>中标人：</span>
            <span>{{auctionDb.winningBidder}}</span>
          </div>
          <div class="content-item">
            <span>申请人：</span>
            <span>{{auctionDb.contacts}}</span>
          </div>
          <div class="content-item">
            <span>联系电话：</span>
            <span>{{auctionDb.contactNumber}}</span>
          </div>
          <div class="content-item">
            <span>中标金额：</span>
            <span>{{auctionDb.winningAmount}}元</span>
          </div>
          <div class="content-item">
            <span>代理机构：</span>
            <span>{{auctionDb.agency}}</span>
          </div>
          <div class="content-item">
            <span>代理联系方式：</span>
            <span>{{auctionDb.agentContact}}</span>
          </div>
          <div
            class="content-item"
            style="width:100%"
            v-if="auctionDb.auctionData && auctionDb.auctionData.length > 0"
          >
            <span>拍卖资料：</span>
            <div class="file-download">
              <el-checkbox-group
                v-model="auctionDownLoadCheckedList"
                @change="auctionCheckedFileChange"
              >
                <div
                  v-for="(file, index) in auctionDb.auctionData"
                  :key="index"
                  class="file-download-item"
                >
                  <el-checkbox
                    :label="`${file.physicalAddress},${file.repairFileName}`"
                  >{{file.repairFileName}}</el-checkbox>
                </div>
              </el-checkbox-group>
              <el-button
                size="small"
                type="primary"
                @click="fileDownLoad(auctionDownloadList, 3)"
              >点击下载文件</el-button>
            </div>
          </div>
        </div>
        <div class="dismantle-info-box" v-if="isShow.dismantleDb">
          <div class="content-item">
            <span>处置方式：</span>
            <span>拆除</span>
          </div>
          <div class="content-item">
            <span>登记人：</span>
            <span>{{dismantleDb.dismantleUser}}</span>
          </div>
          <div class="content-item">
            <span>登记时间：</span>
            <span>{{dismantleDb.dismantleTime}}</span>
          </div>
          <div class="content-item">
            <span>实施单位：</span>
            <span>{{dismantleDb.implementationUnit}}</span>
          </div>
          <div class="content-item">
            <span>拆除时间：</span>
            <span>{{dismantleDb.demolitionTime}}</span>
          </div>
          <div class="content-item" style="width:100%;">
            <span>备注：</span>
            <span>{{dismantleDb.dismantleComment ? dismantleDb.dismantleComment : '暂无'}}</span>
          </div>
          <div
            class="content-item"
            style="width:100%;"
            v-if="dismantleDb.dismantleData && dismantleDb.dismantleData.length > 0"
          >
            <span>拆除文件：</span>
            <div class="file-download">
              <el-checkbox-group
                v-model="dismantleDownLoadCheckedList"
                @change="dismantleCheckedFileChange"
              >
                <div
                  v-for="(file, index) in dismantleDb.dismantleData"
                  :key="index"
                  class="file-download-item"
                >
                  <el-checkbox
                    :label="`${file.physicalAddress},${file.repairFileName}`"
                  >{{file.repairFileName}}</el-checkbox>
                </div>
              </el-checkbox-group>
              <el-button
                size="small"
                type="primary"
                @click="fileDownLoad(dismantleDownloadList, 4)"
              >点击下载文件</el-button>
            </div>
          </div>
        </div>
        <div class="levy-info-box" v-if="isShow.levyDb">
          <div class="content-item">
            <span>处置方式：</span>
            <span>征收</span>
          </div>
          <div class="content-item">
            <span>登记人：</span>
            <span>{{levyDb.levyUser}}</span>
          </div>
          <div class="content-item">
            <span>登记时间：</span>
            <span>{{levyDb.levyTime}}</span>
          </div>
          <div class="content-item">
            <span>房屋征收单位：</span>
            <span>{{levyDb.houseLevyUnit}}</span>
          </div>
          <div class="content-item">
            <span>征收补偿款：</span>
            <span>{{levyDb.levyCompensation}}元</span>
          </div>
          <div
            class="content-item"
            style="width:100%;"
            v-if="levyDb.levyData && levyDb.levyData.length > 0"
          >
            <span>征收资料：</span>
            <div class="file-download">
              <el-checkbox-group v-model="levyDownLoadCheckedList" @change="levyCheckedFileChange">
                <div
                  v-for="(file, index) in levyDb.levyData"
                  :key="index"
                  class="file-download-item"
                >
                  <el-checkbox
                    :label="`${file.physicalAddress},${file.repairFileName}`"
                  >{{file.repairFileName}}</el-checkbox>
                </div>
              </el-checkbox-group>
              <el-button
                size="small"
                type="primary"
                @click="fileDownLoad(levyDownloadList, 5)"
              >点击下载文件</el-button>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
    <!-- /详情 -->
  </div>
</template>

<script>
import {
  getHandleLs,
  selectAllHandlePo,
  getHandleDetail,
  leaderDetail,
  downloadFile
} from "./api";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import searchTreeSelect from "@/components/treeSelect/treeSelect_3"
export default {
  components: {
    singleBtnGroup,
    tableBtnGroup,
    tablePagination,
    tableComponent,
    cancelBtn,
    confirmBtn,
    searchBtn,
    resetBtn,
    searchTreeSelect
  },
  data() {
    return {
      userId: '',
      searchTreeName: 'disposeSearchTree',
      houseIdList: [],
      yardIdList: [],
      handleWay: "",
      startTime: "",
      endTime: "",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      selectData: [],
      tableData: [],
      dialogTitle: "",
      operateType: "add",
      detailDialogVisible: false,
      allTreeData: [],
      dismantleDownLoadCheckedList: [],
      levyDownLoadCheckedList: [],
      dismantleDownloadList: [],
      levyDownloadList: [],
      purposesDownLoadCheckedList: [],
      purposesDownloadList: [],
      auctionDownLoadCheckedList: [],
      auctionDownloadList: [],
      handleWayOptions: [
        {
          id: '',
          name: '全部'
        },
        {
          id: 1,
          name: '转换用途'
        },
        {
          id: 3,
          name: '拍卖'
        },
        {
          id: 4,
          name: '拆除'
        },
        {
          id: 7,
          name: '征收'
        }
      ],
      isShow: {
        conversionPurposesDb: false, // 转换用途
        auctionDb: false, // 拍卖
        dismantleDb: false, // 拆除
        levyDb: false // 征收
      },
      commonInfo: {
        yardPosition: '',
        handlePosition: '',
        handleDate: '',
        handleArea: '',
        unitName: '',
        handleApplicant: ''
      },
      conversionPurposesDb: {
        conPurposesData: [],
        cpsComment: "",
        cpsTime: "",
        cpsUser: "",
        ponUse: ""
      },
      auctionDb: {
        agency: "",
        agentContact: "",
        auctionData: [],
        auctionTime: "",
        auctionUser: "",
        contactNumber: "",
        contacts: "",
        winningAmount: "",
        winningBidder: "",
      },
      dismantleDb: {
        demolitionTime: '',
        dismantleData: [],
        dismantleTime: '',
        dismantleUser: '',
        implementationUnit: '',
        dismantleComment: ''
      },
      levyDb: {
        levyTime: '',
        levyUser: '',
        houseLevyUnit: '',
        levyCompensation: '',
        levyData: []
      },
      currentHandleId: null,
      pickerOptions_start: {
      },
      pickerOptions_end: {
      }
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
    // 获取列表
    getList() {
      this.loadingStart('.table-box');
      this.userId = sessionStorage.getItem('userId') - 0;
      getHandleLs(this.currentPage, this.pageSize, this.houseIdList, this.yardIdList, this.startTime, this.endTime, null, this.userId, this.handleWay)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach(item => {
            item.buildPosition = '';
            item.yardPosition = '';
            item.buildPositionDtoList.forEach((position, index) => {
              if (index === 0) {
                item.yardPosition = position.yardName;
              }
              if (item.buildPositionDtoList.length === 1) {
                item.buildPosition = position.buildName;
              } else if (item.buildPositionDtoList.length === (index + 1)) {
                item.buildPosition += position.buildName;
              } else {
                item.buildPosition += `${position.buildName}、`;
              }
            });
            item.applyStatusText = this.getApplyStatus(item.applyStatus);
            for (let i = 0; i < this.handleWayOptions.length; i++) {
              if (this.handleWayOptions[i].id === item.handleWay) {
                item.handleWayText = this.handleWayOptions[i].name;
                break;
              }
            }
            item.handleArea = item.handleArea ? item.handleArea : 0;
          })
          this.loadingEnd();
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '处置申请列表获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取申请状态
    getApplyStatus(status) {
      switch (status) {
        case 0:
          return '未审核';
          break;
        case 1:
          return '审核通过';
          break;
        case 2:
          return '审核未通过';
          break;
        case 3:
          return '审核中';
          break;
      }
    },
    // 查询信息
    search() {
      this.currentPage = 1;
      this.getList();
    },
    // 重置
    reset() {
      this.currentPage = 1;
      this.houseIdList = [];
      this.yardIdList = [];
      this.startTime = "";
      this.endTime = "";
      this.handleWay = "";
      eventBus.$emit(`${this.searchTreeName}_clear`);
      this.getList();
    },
    handleSearchCheckChange(checkedNodes, halfCheckedNodes) {
      this.houseIdList = [];
      this.yardIdList = [];
      const nodes = checkedNodes.concat(halfCheckedNodes);
      if (nodes.length > 0) {
        nodes.forEach(node => {
          if (node.type === 3) {
            this.houseIdList.push(node.value);
          } else if (node.type === 0) {
            this.yardIdList.push(node.value);
          }
        })
      }
    },
    // 单个详情
    singleDetail(row) {
      this.detailDialogVisible = true;
      this.currentHandleId = row.handleId;
      for ([key, value] of Object.entries(row)) {
        if (this.commonInfo[key] !== undefined) {
          this.commonInfo[key] = value;
        }
      }
      getHandleDetail(row.handleId).then(res => {
        let positionArr = [];
        if (res.isHouse === 0) {
          res.buildPositionDtoList.forEach(build => {
            const position = `${build.yardName}-${build.buildName}`
            positionArr.push(position);
          })
        } else {
          const yardName = res.buildPositionDtoList[0].yardName;
          res.buildPositionDtoList.forEach(build => {
            const buildName = build.buildName;
            const floorNameDtoList = res.buildPositionDtoList[0].floorNameDtoList;
            floorNameDtoList.forEach(floor => {
              const floorName = floor.floorName;
              floor.houseNumber.forEach((house => {
                const houseNumber = house.houseNumber;
                const position = `${yardName}-${buildName}-${floorName}层-${houseNumber}`
                positionArr.push(position);
              }))
            });
          })
        }
        this.commonInfo.handlePosition = positionArr.join('，');
      }).catch(err => {
        const msg = err.result ? err.result : `处置相关信息获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
      leaderDetail(row.handleId).then(res => {
        // 判断显隐
        let keyArr = [];
        let allInfo = {};
        res.documents.forEach(item => {
          if (item) {
            for (let key in item[0]) {
              keyArr.push(key);
              allInfo[key] = item[0][key];
            }
          }
        });
        for (let key in this.isShow) {
          if (keyArr.indexOf(key) > -1) {
            this.isShow[key] = true;
          } else {
            this.isShow[key] = false;
          }
        }
        // 绑定拆除信息
        if (allInfo.dismantleDb !== undefined) {
          for ([key, value] of Object.entries(allInfo.dismantleDb)) {
            if (this.dismantleDb[key] !== undefined) {
              this.dismantleDb[key] = value;
            }
          }
          this.dismantleDb.demolitionTime = this.dismantleDb.demolitionTime.split(' ')[0];
        }
        // 绑定拍卖信息
        if (allInfo.auctionDb !== undefined) {
          for ([key, value] of Object.entries(allInfo.auctionDb)) {
            if (this.auctionDb[key] !== undefined) {
              this.auctionDb[key] = value;
            }
          }
        }
        // 绑定转换用途信息
        if (allInfo.conversionPurposesDb !== undefined) {
          for ([key, value] of Object.entries(allInfo.conversionPurposesDb)) {
            if (this.conversionPurposesDb[key] !== undefined) {
              this.conversionPurposesDb[key] = value;
            }
          }
        }
        // 绑定征收信息
        if (allInfo.levyDb !== undefined) {
          for ([key, value] of Object.entries(allInfo.levyDb)) {
            if (this.levyDb[key] !== undefined) {
              this.levyDb[key] = value;
            }
          }
          this.levyDb.levyTime = this.levyDb.levyTime.split(' ')[0];
        }
      }).catch(err => {
        const msg = err.result ? err.result : '处置详情获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    dismantleCheckedFileChange(val) {
      this.dismantleDownloadList = [];
      this.dismantleDownLoadCheckedList.forEach(item => {
        const arr = item.split(',');
        const obj = {
          address: arr[0],
          fileName: arr[1]
        }
        this.dismantleDownloadList.push(obj);
      })
    },
    auctionCheckedFileChange(val) {
      this.auctionDownloadList = [];
      this.auctionDownLoadCheckedList.forEach(item => {
        const arr = item.split(',');
        const obj = {
          address: arr[0],
          fileName: arr[1]
        }
        this.auctionDownloadList.push(obj);
      })
    },
    purposesCheckedFileChange(val) {
      this.purposesDownloadList = [];
      this.purposesDownLoadCheckedList.forEach(item => {
        const arr = item.split(',');
        const obj = {
          address: arr[0],
          fileName: arr[1]
        }
        this.purposesDownloadList.push(obj);
      })
    },
    levyCheckedFileChange(val) {
      this.levyDownloadList = [];
      this.levyDownLoadCheckedList.forEach(item => {
        const arr = item.split(',');
        const obj = {
          address: arr[0],
          fileName: arr[1]
        }
        this.levyDownloadList.push(obj);
      })
    },
    // 文件下载
    fileDownLoad(list, type) {
      if (!list || list.length === 0) {
        this.$message({
          message: "请先选择一个文件",
          type: "warning"
        });
        return;
      }
      let downloadFlies = {};
      downloadFlies.addressList = list;
      downloadFlies.downLoadType = type;
      downloadFlies.handleId = this.currentHandleId;
      downloadFile(downloadFlies).then(res => {
        let fileDownload = require('js-file-download');
        let name = decodeURIComponent(sessionStorage.getItem('CADdownloadFileName'));
        fileDownload(res, name);
      }).catch(err => {
        const msg = err.result ? err.result : '文件下载失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 清空表单
    clearForm(form) {
      for (let key in form) {
        var type = this.getDataType(form[key]);
        if (type === "String") {
          form[key] = "";
        } else if (type === "Array") {
          form[key] = [];
        } else if (type === "Object") {
          const keys = Object.keys(form[key]);
          if (keys.length > 0) {
            this.clearForm(form[key]);
          } else {
            form[key] = {};
          }
        } else {
          form[key] = null;
        }
      }
    },
    detailDialogClosed() {
      this.dismantleDownLoadCheckedList = [];
      this.levyDownLoadCheckedList = [];
      this.dismantleDownloadList = [];
      this.levyDownloadList = [];
      this.purposesDownLoadCheckedList = [];
      this.purposesDownloadList = [];
      this.auctionDownLoadCheckedList = [];
      this.auctionDownloadList = [];
      this.currentHandleId = null;
      for (let key in this.isShow) {
        this.isShow[key] = false;
      };
      this.clearForm(this.commonInfo);
      this.clearForm(this.conversionPurposesDb);
      this.clearForm(this.auctionDb);
      this.clearForm(this.dismantleDb);
      this.clearForm(this.levyDb);
    },
    // 获取数据类型
    getDataType(obj) {
      var typeArr = [
        "String",
        "Object",
        "Number",
        "Array",
        "Function"
      ];
      for (let name of typeArr) {
        if (Object.prototype.toString.call(obj) === `[object ${name}]`) {
          return name;
        }
      }
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
    }
  },
  mounted() {
    this.getList();
    this.selectAllHandlePo();
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - queryBoxH - operateBoxH;
    }, 1);

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
  }
};
</script>

<style>
#disposeList .query-box {
  margin-bottom: 20px;
}
#disposeList .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 18%;
}
#disposeList .query-item > div {
  float: left;
}
#disposeList .query-item > .query-input {
  width: calc(100% - 80px);
}
#disposeList .query-item .el-select {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#disposeList .query-item .el-date-editor.el-input,
#disposeList .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#disposeList .query-btn {
  float: right;
}
#disposeList .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#disposeList .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#disposeList .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#disposeList .status-unreviewed {
  color: #ff9f02;
}
#disposeList .status-pass {
  color: #3891e0;
}
#disposeList .view-box {
  cursor: pointer;
  color: #3891e0;
  font-size: 0;
  height: 28px;
  line-height: 28px;
}
#disposeList .view-box:hover > .view-icon,
#disposeList .view-box:hover > .view-text {
  color: #035cac;
}
#disposeList .view-icon {
  font-size: 14px;
  position: relative;
  top: 1px;
}
#disposeList .view-text {
  font-size: 14px;
}
#disposeList .status-notPass {
  color: #f9463f;
}
#disposeList .status-reviewimg {
  color: #1aa97b;
}
#disposeList .content-item {
  margin-bottom: 22px;
  overflow: hidden;
  width: 50%;
  float: left;
}
#disposeList .content-item > span {
  display: block;
  height: 20px;
  line-height: 20px;
  float: left;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
#disposeList .content-item > span:first-child {
  text-align: right;
  width: 108px;
}
#disposeList .content-item > span:last-child {
  width: calc(100% - 108px);
}
#disposeList .dialog-content > div {
  overflow: hidden;
  margin-bottom: 30px;
}
#disposeList .file-download {
  float: left;
}
#disposeList .file-download-item {
  margin-bottom: 22px;
}
</style>


