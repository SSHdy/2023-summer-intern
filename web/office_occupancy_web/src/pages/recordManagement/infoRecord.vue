<template>
  <div id="infoRecord">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">位置：</div>
          <div class="query-input">
            <search-tree-select
              :showSelectTree="showSelectTree"
              :inputHeight="'32px'"
              :inputWidth="'100%'"
              :placeholderText="`请选择位置`"
              :isMultipleSelect="true"
              :checkStrictly="false"
              treeName="recordSearchTree"
              @select-tree-checked="searchTreeChecked"
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
        <el-table-column type="selection" width="50" align="center"></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="yardName" label="院落名称" align="center"></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="buildName" label="楼座名称" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="yardAdministrativeRegion"
          label="行政区划"
          align="center"
        ></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="unitName" label="所属产权" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="structureType"
          label="结构类型"
          align="center"
        ></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="buildDate" label="建成时间" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="buildArea"
          label="建筑面积(m²)"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowAlter="false"
              :isShowDelete="false"
              :isRecord="true"
              @recordViewClick="recordView(scope.row)"
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
    <el-dialog
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{dialogTitle}}</span>
      </div>
      <div class="dialog-content">
        <div class="content-left">
          <div class="content-title">
            <span>信息清单</span>
            <span class="line"></span>
          </div>
          <div class="tree-box">
            <el-tree
              :data="treeData"
              :props="defaultProps"
              :current-node-key="currentNodeKey"
              ref="tree"
              node-key="id"
              highlight-current
              default-expand-all
              @node-click="handleNodeClick"
              v-if="showYardTree"
            ></el-tree>
          </div>
        </div>
        <div class="content-right">
          <div class="base-info-box">
            <div class="content-title">
              <span>基础信息</span>
              <span class="line"></span>
            </div>
            <div class="yard-base-info" v-if="showInfoType === 0">
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="grid-content">
                    <span>院落名称：</span>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="yardForm.yardName"
                      placement="top-start"
                    >
                      <span>{{yardForm.yardName}}</span>
                    </el-tooltip>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content">
                    <span>行政区划：</span>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="yardForm.yardAdministrativeRegion"
                      placement="top-start"
                    >
                      <span>{{yardForm.yardAdministrativeRegion}}</span>
                    </el-tooltip>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content">
                    <span>院落地址：</span>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="yardForm.yardPosition"
                      placement="top-start"
                    >
                      <span>{{yardForm.yardPosition}}</span>
                    </el-tooltip>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="grid-content">
                    <span>经度(°)：</span>
                    <span>{{yardForm.yardLongitude}}</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content">
                    <span>纬度(°)：</span>
                    <span>{{yardForm.yardLatitude}}</span>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div class="build-base-info" v-if="showInfoType === 1">
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="grid-content">
                    <span>院落名称：</span>
                    <span>{{buildForm.yardName}}</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content">
                    <span>楼座名称：</span>
                    <span>{{buildForm.buildName}}</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content">
                    <span>建筑面积：</span>
                    <span>{{buildForm.buildArea}}m²</span>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="grid-content">
                    <span>建成时间：</span>
                    <span>{{buildForm.buildDate}}</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content">
                    <span>地上楼层：</span>
                    <span>{{buildForm.overgroundFloor}}层</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content">
                    <span>地下楼层：</span>
                    <span>{{buildForm.undergroundFloor}}层</span>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="grid-content">
                    <span>结构类型：</span>
                    <span>{{buildForm.structureType}}</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content">
                    <span>电梯数量：</span>
                    <span>{{buildForm.elevatorNum}}台</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content">
                    <span>产权所属单位：</span>
                    <span>{{buildForm.propertyUnitName}}</span>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div class="floor-base-info" v-if="showInfoType === 2">
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="grid-content">
                    <span>房间数量：</span>
                    <span>{{floorForm.houseNum}}</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content">
                    <span>房间总面积：</span>
                    <span>{{floorForm.sumHouseArea}}m²</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content">
                    <span>使用人数：</span>
                    <span>{{floorForm.useNum}}人</span>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="grid-content">
                    <span>办公用房数量：</span>
                    <span>{{floorForm.officeNum}}</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content">
                    <span>办公用房面积：</span>
                    <span>{{floorForm.officeArea}}m²</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content">
                    <span>领导人数：</span>
                    <span>{{floorForm.leaderNum}}人</span>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="file-downLoad-box" v-if="showInfoType === 1 || showInfoType === 2">
            <div class="content-title">
              <span>文件下载</span>
              <span class="line"></span>
            </div>
            <div class="file-tips" v-if="allDownLoadFileList.length === 0">该{{tipsType}}暂无信息档案</div>
            <div class="file-downLoad" v-if="allDownLoadFileList.length > 0">
              <el-checkbox-group v-model="fileDownLoadCheckedList">
                <div
                  v-for="(file, index) in allDownLoadFileList"
                  :key="index"
                  class="file-download-item"
                >
                  <el-checkbox :label="file.floordrawingId">{{file.floordrawingFileName}}</el-checkbox>
                </div>
              </el-checkbox-group>
              <el-button size="small" type="primary" @click="fileDownLoad">下载</el-button>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getMessageFilesList,
  getMessageFloorTree,
  getFloorData
} from "./api";
import {
  getAllYardList,
  getAllUnitList
} from "@/axios/commonApi";
import {
  getYardDetail,
  getBuildDetail,
  seachCAD,
  downloadCAD
} from "@/pages/houseManagement/api";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import searchTreeSelect from "@/components/treeSelect/treeSelect_6"
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
      buildIdList: [],
      yardIdList: [],
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      selectData: [],
      tableData: [],
      dialogTitle: "档案信息",
      operateType: "add",
      dialogVisible: false,
      options: [],
      yardForm: {
        yardAdministrativeRegion: '',
        yardLatitude: null,
        yardLongitude: null,
        yardName: '',
        yardPosition: ''
      },
      buildForm: {
        yardId: '',
        yardName: '',
        buildName: '',
        buildArea: '',
        buildDate: '',
        builder: '',
        overgroundFloor: '',
        undergroundFloor: '',
        buildNature: '',
        coveredArea: '',
        registerUse: '',
        structureType: '',
        elevatorNum: '',
        propertyUnitId: '',
        propertyUnitName: ''
      },
      floorForm: {
        houseNum: '',
        sumHouseArea: '',
        useNum: '',
        officeNum: '',
        officeArea: '',
        leaderNum: ''
      },
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      showInfoType: null,
      allDownLoadFileList: [],
      fileDownLoadCheckedList: [],
      yardOptions: [],
      unitOptions: [],
      treeData: [],
      showYardTree: false,
      firstLoad: true,
      currentBuildId: null,
      currentYardId: null,
      currentNodeKey: null,
      showSelectTree: true,
      tipsType: ''
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
      getMessageFilesList(this.currentPage, this.pageSize, this.buildIdList, this.yardIdList).then(res => {
        this.tableData = res.records;
        this.tableData.forEach(item => {
          item.yardName = item.yardName ? item.yardName : '-';
          item.buildName = item.buildName ? item.buildName : '-';
          item.yardAdministrativeRegion = item.yardAdministrativeRegion ? item.yardAdministrativeRegion : '-';
          item.unitName = item.unitName ? item.unitName : '-';
          item.structureType = item.structureType ? item.structureType : '-';
          item.buildArea = item.buildArea ? item.buildArea : '-';
          item.buildDate = item.buildDate ? this.formatDate(item.buildDate) : '-';
        })
        this.total = res.total;
        this.loadingEnd();
      }).catch(err => {
        this.loadingEnd();
        const msg = err.result ? err.result : '信息档案信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      });
    },
    // 查询信息
    search() {
      this.currentPage = 1;
      this.getList();
    },
    // 重置
    reset() {
      this.buildIdList = [];
      this.yardIdList = [];
      this.currentPage = 1;
      this.showSelectTree = false;
      eventBus.$emit('clearRecordPosition')
      setTimeout(() => {
        this.showSelectTree = true;
      }, 1000);
      this.getList();
    },
    // 获取院落字典项
    getYardDic() {
      getAllYardList(1)
        .then(res => {
          this.yardOptions = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : '院落获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取单位字典项
    getUnitDic() {
      getAllUnitList()
        .then(res => {
          this.unitOptions = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : '单位获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
    },
    // 查看维修档案
    recordView(row) {
      this.dialogVisible = true;
      this.showYardTree = true;
      this.currentBuildId = row.buildId;
      this.currentYardId = row.yardId;
      this.currentNodeKey = `${row.yardId}:${row.buildId}`;
      getMessageFloorTree(row.yardId, row.buildId).then(res => {
        this.treeData = res;
        this.$nextTick(() => {
          const currentNode = this.$refs.tree.getNode(this.currentNodeKey);
          this.showInfoType = currentNode.data.type;
          if (this.showInfoType === 1) {
            this.tipsType = "楼座";
          } else if (this.showInfoType === 2) {
            this.tipsType = "楼层";
          }
          getBuildDetail(currentNode.data.value).then(res => {
            if (res) {
              for (let key in this.buildForm) {
                this.buildForm[key] = res[key];
              }
              this.buildForm.buildDate = this.formatDate(this.buildForm.buildDate);
              for (let i = 0; i < this.yardOptions.length; i++) {
                if (this.yardOptions[i].yardId === this.buildForm.yardId) {
                  this.buildForm.yardName = this.yardOptions[i].yardName;
                  break;
                }
              }
              for (let i = 0; i < this.unitOptions.length; i++) {
                if (this.unitOptions[i].unitId === this.buildForm.propertyUnitId) {
                  this.buildForm.propertyUnitName = this.unitOptions[i].unitName;
                  break;
                }
              }
            }
          }).catch(err => {
            const msg = err.result ? err.result : '楼座详情获取失败';
            this.$message({
              message: msg,
              type: "error"
            });
          })
          seachCAD(currentNode.data.value).then(res => {
            if (res && res.length > 0) {
              this.allDownLoadFileList = res;
            } else {
              this.allDownLoadFileList = [];
            }
          }).catch(err => {
            this.allDownLoadFileList = [];
            const msg = err.result ? err.result : 'CAD获取失败';
            this.$message({
              message: msg,
              type: "error"
            });
          })
        })
      }).catch(err => {
        const msg = err.result ? err.result : '树获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 搜索树选中
    searchTreeChecked(checked) {
      this.yardIdList = [];
      this.buildIdList = [];
      if (checked && checked.length > 0) {
        checked.forEach(item => {
          if (item.type === 1) {
            this.yardIdList.push(item.value);
          } else if (item.type === 2) {
            this.buildIdList.push(item.value);
          }
        })
      }
    },
    renderContent(h, { node, data, store }) {
      if (data.type === 1) {
        return (
          <span class="custom-tree-node">
            <span class="iconfont">&#xe7c8;</span>
            <span class="show-ellipsis">{data.text}</span>
          </span>
        );
      } else if (data.type === 2) {
        return (
          <span class="custom-tree-node">
            <span class="iconfont">&#xe888;</span>
            <span class="show-ellipsis">{data.text}</span>
          </span>
        );
      } else {
        return (
          <span class="custom-tree-node">
            <span class="show-ellipsis">{data.text}</span>
          </span>
        );
      }
    },
    handleNodeClick(data, node) {
      this.showInfoType = data.type;
      this.fileDownLoadCheckedList = [];
      if (data.type === 0) {
        this.clearForm(this.yardForm);
        getYardDetail(data.value).then(res => {
          if (res) {
            for (let key in this.yardForm) {
              this.yardForm[key] = res[key];
            }
          }
        }).catch(err => {
          const msg = err.result ? err.result : '楼座详情获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        })
      } else if (data.type === 1) {
        this.tipsType = "楼座";
        this.clearForm(this.buildForm);
        getBuildDetail(data.value).then(res => {
          if (res) {
            for (let key in this.buildForm) {
              this.buildForm[key] = res[key];
            }
            this.buildForm.buildDate = this.formatDate(this.buildForm.buildDate);
            for (let i = 0; i < this.yardOptions.length; i++) {
              if (this.yardOptions[i].yardId === this.buildForm.yardId) {
                this.buildForm.yardName = this.yardOptions[i].yardName;
                break;
              }
            }
            for (let i = 0; i < this.unitOptions.length; i++) {
              if (this.unitOptions[i].unitId === this.buildForm.propertyUnitId) {
                this.buildForm.propertyUnitName = this.unitOptions[i].unitName;
                break;
              }
            }
          }
        }).catch(err => {
          const msg = err.result ? err.result : '楼座详情获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        })
        seachCAD(data.value).then(res => {
          if (res && res.length > 0) {
            this.allDownLoadFileList = res;
          } else {
            this.allDownLoadFileList = [];
          }
        }).catch(err => {
          this.allDownLoadFileList = [];
          const msg = err.result ? err.result : 'CAD获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        })
      } else if (data.type === 2) {
        this.tipsType = "楼层";
        this.clearForm(this.floorForm);
        getFloorData(this.currentBuildId, data.value).then(res => {
          if (res) {
            for (let key in this.floorForm) {
              if (!res[key]) {
                this.floorForm[key] = 0;
              } else {
                this.floorForm[key] = res[key];
              }
            }
          }
        }).catch(err => {
          const msg = err.result ? err.result : '楼座详情获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        })
        seachCAD(this.currentBuildId, data.value).then(res => {
          if (res && res.length > 0) {
            this.allDownLoadFileList = res;
          } else {
            this.allDownLoadFileList = [];
          }
        }).catch(err => {
          this.allDownLoadFileList = [];
          const msg = err.result ? err.result : 'CAD获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        })
      }
    },
    // 文件下载
    fileDownLoad() {
      if (this.fileDownLoadCheckedList.length === 0) {
        this.$message({
          message: "请先选中一个文件",
          type: "warning"
        });
        return false;
      }
      let temp = {};
      temp.floorDrawingIdList = this.fileDownLoadCheckedList;
      temp.buildId = this.currentBuildId;
      downloadCAD(temp).then(res => {
        let fileDownload = require('js-file-download');
        let name = decodeURIComponent(sessionStorage.getItem('CADdownloadFileName'));
        fileDownload(res, name);
      }).catch(err => {
        const msg = err.result ? err.result : 'CAD下载失败';
        this.$message({
          message: msg,
          type: "error"
        });
      });
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
    // dialog关闭的回调
    dialogClosed() {
      this.showYardTree = false;
      this.firstLoad = true;
      this.showInfoType = null;
      this.currentBuildId = null;
      this.allDownLoadFileList = [];
      this.fileDownLoadCheckedList = [];
      this.treeData = null;
      this.currentNodeKey = null;
      this.clearForm(this.buildForm);
      this.clearForm(this.floorForm);
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
    // 只能输入正浮点数
    limitFloat(key, flag) {
      this.Form[key] = this.Form[key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '')
      if (flag && this.Form[key] !== '') {
        this.Form[key] = (this.Form[key] - 0).toFixed(2);
      }
    },
    // 格式化时间
    formatDate(time, format) {
      var date = new Date(time);
      var fmt = format ? format : "yyyy-MM-dd";
      var o = {
        "M+": date.getMonth() + 1, //月份
        "d+": date.getDate(), //日
        "h+": date.getHours(), //小时
        "m+": date.getMinutes(), //分
        "s+": date.getSeconds(), //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        S: date.getMilliseconds() //毫秒
      };
      if (/(y+)/.test(fmt))
        fmt = fmt.replace(
          RegExp.$1,
          (date.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length == 1
              ? o[k]
              : ("00" + o[k]).substr(("" + o[k]).length)
          );
      return fmt;
    }
  },
  mounted() {
    this.getList();
    this.getYardDic();
    this.getUnitDic();
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
#infoRecord .query-box {
  margin-bottom: 20px;
}
#infoRecord .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#infoRecord .query-item > div {
  float: left;
}
#infoRecord .query-item > .query-input {
  width: calc(100% - 80px);
}
#infoRecord .query-item .el-date-editor.el-input,
#infoRecord .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#infoRecord .query-btn {
  float: left;
  margin-left: 50px;
}
#infoRecord .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#infoRecord .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#infoRecord .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#infoRecord .el-dialog__body {
  background-color: #efefef;
  padding: 15px;
}
#infoRecord .dialog-content {
  height: 500px;
}
#infoRecord .content-title {
  line-height: 30px;
  padding-left: 20px;
  position: relative;
}
#infoRecord .content-left {
  width: 230px;
  height: 100%;
  background-color: #ffffff;
  float: left;
}
#infoRecord .content-title > .line {
  position: absolute;
  bottom: -1px;
  left: 18px;
  width: 60px;
  height: 2px;
  background-color: #4490f1;
}
#infoRecord .tree-box {
  width: 100%;
  height: calc(100% - 40px);
  overflow-x: hidden;
  overflow-y: auto;
  margin-top: 10px;
}
#infoRecord .content-right {
  width: calc(100% - 245px);
  height: 100%;
  background-color: #ffffff;
  float: right;
  overflow-y: auto;
  overflow-x: hidden;
}
#infoRecord .content-right .content-title {
  border-bottom: 1px solid #d8d8d8;
  margin-left: 20px;
  margin-right: 5px;
  padding-left: 0px;
}
#infoRecord .content-right .content-title > .line {
  left: -2px;
}
#infoRecord .yard-base-info,
#infoRecord .build-base-info,
#infoRecord .floor-base-info {
  padding-left: 20px;
  padding-right: 5px;
  margin-top: 20px;
}
#infoRecord .grid-content {
  line-height: 40px;
  overflow: hidden;
}
#infoRecord .grid-content > span {
  float: left;
}
#infoRecord .file-downLoad-box {
  margin-top: 30px;
}
#infoRecord .file-downLoad {
  margin-left: 20px;
}
#infoRecord .build-base-info .grid-content > span:first-child,
#infoRecord .floor-base-info .grid-content > span:first-child,
#infoRecord .yard-base-info .grid-content > span:first-child {
  width: 98px;
  text-align: right;
}
#infoRecord .build-base-info .grid-content > span:last-child,
#infoRecord .floor-base-info .grid-content > span:last-child,
#infoRecord .yard-base-info .grid-content > span:last-child {
  width: calc(100% - 98px);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
#infoRecord .file-download-item {
  line-height: 40px;
}
#infoRecord .file-tips {
  margin-top: 15px;
  margin-left: 20px;
}
</style>