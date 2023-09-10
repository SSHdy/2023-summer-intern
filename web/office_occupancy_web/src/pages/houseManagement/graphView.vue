<template>
  <div id="houseGraphView">
    <!-- 平面图 -->

    <div class="plane" v-show="activeBtnIndex === 0">
      <div class="canvasDiv">
        <canvas id="c">请使用支持HTML5的浏览器</canvas>
      </div>
    </div>
    <!-- 三维图 -->
    <div class="threeD" v-show="activeBtnIndex === 1">
      <three-d ref="threeD" @finished="initWebglFinished"></three-d>
    </div>
    <!-- 切换按钮 -->
    <div class="btn-box">
      <button
        class="cad-display-btn"
        @click="accessoryHandle"
        v-show="activeBtnIndex === 0 && fileList.length > 0"
      >
        <i class="cad-small-icon"></i>下载CAD
      </button>
      <button
        class="plane-display-btn"
        :class="{ active: activeBtnIndex === 0 }"
        @click="planeDisplay"
      >
        平面显示
      </button>
      <button
        class="threeD-display-btn"
        :class="{
          active: activeBtnIndex === 1,
          threeDisabled: webglFlag === false,
        }"
        @click="threeDisplay"
      >
        3D显示
      </button>
    </div>

    <!-- 附件模态框 -->
    <el-dialog
      :before-close="accessoryDialogClosed"
      :visible.sync="accessoryDialogVisible"
      :close-on-click-modal="false"
      class="accessoryDialog"
      width="780px"
    >
      <div slot="title" class="dialog-title">
        <span>附件</span>
      </div>
      <div class="accessory-main">
        <el-checkbox-group v-model="CADDownload">
          <div
            v-for="(item, index) in fileList"
            :key="index"
            class="accessory-item"
          >
            <el-checkbox :label="item.id">{{ item.name }}</el-checkbox>
          </div>
        </el-checkbox-group>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn
          @clickBtn="accessoryDialogClosed"
          :fontSize="14"
        ></cancel-btn>
        <download-btn
          @clickBtn="downloadCAD"
          :fontSize="14"
          :marginLeft="180"
        ></download-btn>
      </span>
    </el-dialog>

    <!-- 房间详情 -->
    <div class="house-detail-info">
      <div class="house-base-info">
        <div class="house-base-header">房间信息</div>
        <div class="house-base-body">
          <el-table
            :data="houseBaseInfo"
            :header-cell-style="tableHeaderCellStyle"
            :cell-style="tableCellStyle"
            style="width: 100%"
          >
            <el-table-column
              prop="housetypeName"
              label="房屋类型"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="houseNumber"
              label="房间号"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="houseUseArea"
              :label="`${areaTitle}(m²)`"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="useType"
              label="使用类型"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
          </el-table>
        </div>
        <div class="house-base-header" v-if="showHandleDetail">处置信息</div>
        <div class="house-base-body" v-if="showHandleDetail">
          <el-table
            :data="handleTableList"
            :header-cell-style="tableHeaderCellStyle"
            :cell-style="tableCellStyle"
            style="width: 100%"
          >
            <el-table-column
              prop="handleWay"
              label="处置方式"
              header-align="center"
              align="center"
              :show-overflow-tooltip="true"
            ></el-table-column>
            <el-table-column
              prop="cpsUser"
              label="登记人"
              header-align="center"
              align="center"
              :show-overflow-tooltip="true"
            ></el-table-column>
            <el-table-column
              prop="cpsTime"
              label="登记时间"
              header-align="center"
              align="center"
              :show-overflow-tooltip="true"
            ></el-table-column>
            <el-table-column
              prop="ponUse"
              label="转换后用途"
              header-align="center"
              align="center"
              :show-overflow-tooltip="true"
            ></el-table-column>
            <el-table-column
              prop="cpsComment"
              label="备注"
              header-align="center"
              align="center"
              :show-overflow-tooltip="true"
            ></el-table-column>
          </el-table>
        </div>
      </div>
      <div class="house-unit-info">
        <div class="house-unit-header">使用信息</div>
        <div class="house-unit-body">
          <div v-if="houseUseType === 1" class="idle-house-tips">
            此房间为闲置用房
          </div>
          <el-table
            :data="houseLeaderUnitInfo"
            :header-cell-style="tableHeaderCellStyle"
            :cell-style="tableCellStyle"
            style="width: 100%"
            v-if="houseUseType === 2"
          >
            <el-table-column
              prop="unitName"
              label="所属单位"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
          </el-table>
          <el-table
            :data="houseLeaderInfo"
            :header-cell-style="tableHeaderCellStyle"
            :cell-style="tableCellStyle"
            :height="155"
            style="width: 100%"
            v-if="houseUseType === 2"
          >
            <el-table-column
              prop="professionalLevelName"
              label="等级"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="professionalName"
              label="职级"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="duty"
              label="职务"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="leadingofficeName"
              label="使用人姓名"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="leadingofficeWorkStatue"
              label="在职情况"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
          </el-table>

          <el-table
            :data="houseUseInfo"
            :header-cell-style="tableHeaderCellStyle"
            :cell-style="tableCellStyle"
            style="width: 100%"
            v-if="houseUseType === 3"
          >
            <el-table-column
              prop="unitName"
              label="所属单位"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
          </el-table>

          <el-table
            :data="rentUseInfo"
            :header-cell-style="tableHeaderCellStyle"
            :cell-style="tableCellStyle"
            style="width: 100%"
            v-if="houseUseType === 4 && currentRentType === 1"
          >
            <el-table-column
              prop="lesseeUse"
              label="承租用途"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="lesseeYears"
              label="承租年限(年)"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="renter"
              label="承租方"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="rentBegin"
              label="开始时间"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="rentEnd"
              label="结束时间"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="rentMoney"
              label="租金"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
          </el-table>

          <el-table
            :data="rentUseInfo"
            :header-cell-style="tableHeaderCellStyle"
            :cell-style="tableCellStyle"
            style="width: 100%"
            v-if="houseUseType === 4 && currentRentType === 1"
          >
            <el-table-column
              prop="deposit"
              label="押金"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="rentType"
              label="租赁类型"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="rentMode"
              label="租赁方式"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="contacts"
              label="联系人"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="contactPhone"
              label="联系方式"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="remark"
              label="备注信息"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
          </el-table>

          <!-- 出借信息 -->
          <el-table
            :data="rentUseInfo"
            :header-cell-style="tableHeaderCellStyle"
            :cell-style="tableCellStyle"
            style="width: 100%"
            v-if="houseUseType === 4 && currentRentType === 2"
          >
            <el-table-column
              prop="lesseeUse"
              label="承借用途"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="lesseeYears"
              label="承借年限(年)"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="renter"
              label="借用单位"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="contacts"
              label="联系人"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
          </el-table>

          <el-table
            :data="rentUseInfo"
            :header-cell-style="tableHeaderCellStyle"
            :cell-style="tableCellStyle"
            style="width: 100%"
            v-if="houseUseType === 4 && currentRentType === 2"
          >
            <el-table-column
              prop="contactPhone"
              label="联系方式"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="rentBegin"
              label="开始时间"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="rentEnd"
              label="结束时间"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="remark"
              label="备注信息"
              align="center"
              header-align="center"
              show-overflow-tooltip
            ></el-table-column>
          </el-table>
        </div>
      </div>
      <button
        type="button"
        class="el-dialog__headerbtn"
        @click="closeHouseDetailBox"
      >
        <i class="el-dialog__close el-icon el-icon-close"></i>
      </button>
    </div>
    <!-- 房屋类型列表 -->
    <div class="house-type-box" v-show="activeBtnIndex === 0">
      <ul>
        <li>
          <span style="background: #cccccc"></span>
          未填写信息
        </li>
        <li>
          <span style="background: #21b124"></span>
          办公室用房
        </li>
        <li>
          <span style="background: #f4e83b"></span>
          服务用房
        </li>
        <li>
          <span style="background: #008aff"></span>
          设备用房
        </li>
        <li>
          <span style="background: #12fff1"></span>
          附属用房
        </li>
        <li>
          <span style="background: #ff01fd"></span>
          业务用房
        </li>
        <li v-if="PRO_TYPE !== 'gl'">
          <span style="background: #fe8200"></span>
          租借用房
        </li>
      </ul>
    </div>
    <!-- 跳转到示意图 -->
    <div class="start-edit-box" v-if="editGraph">
      <el-tooltip
        class="item"
        effect="dark"
        content="开始编辑"
        placement="right"
      >
        <div class="edit-icon" @click="startEdit">
          <span class="iconfont">&#xe610;</span>
        </div>
      </el-tooltip>
    </div>
  </div>
</template>

<script>
import { PRO_TYPE } from "@/axios/global";
import {
  getHouseTypeList,
  getAllUnitList,
  getAllProfessionList,
  workingConditions,
  uploadUrl,
} from "@/axios/commonApi";
import { leaderDetail } from "@/pages/disposeManagement/api";
import {
  houseAdd,
  getHouseGraphJson,
  seachSingleHouseInfo,
  getAllHouseInfo,
  seachCAD,
  saveCAD,
  deleteCAD,
  downloadCAD,
} from "./api";
import { eventBus } from "@/eventBus/eventBus";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import downloadBtn from "@/components/button/downloadBtn";
import saveBtn from "@/components/button/saveBtn";
import threeD from "@/components/threeD/three.vue";
import Detector from "@/assets/js/webglDetector.js";
import { debug, debuglog } from "util";
import { mouseEvent } from "../../../static/js/common";
const $ = require("jquery");
const { fabric } = require("fabric");
const uuidv1 = require("uuid/v1");
export default {
  components: {
    cancelBtn,
    confirmBtn,
    saveBtn,
    downloadBtn,
    threeD,
  },
  props: {
    editGraph: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      PRO_TYPE: PRO_TYPE,
      canvas: null, // canvas对象
      accessoryDialogVisible: false,
      existHouseInfo: [], // 已经存在的房屋信息
      houseType: [],
      unitOptions: [],
      profession: [],
      workStatue: [],
      fileList: [],
      CADDownload: [],
      activeBtnIndex: 0,
      houseBaseInfo: [],
      houseUseInfo: [],
      houseLeaderInfo: [],
      houseLeaderUnitInfo: [],
      rentUseInfo: [],
      houseUseType: 0,
      showDownloadBtn: true,
      tableHeaderCellStyle: {
        "background-color": "#f8f8f8",
        "border-bottom": "1px solid #d4d4d4",
        padding: "6px 0",
        color: "#333333",
      },
      tableCellStyle: {
        padding: "6px 0",
        "background-color": "#f9f9f9",
        "border-bottom": "1px solid #d4d4d4",
      },
      rentList: [],
      lentList: [],
      transformList: [],
      handleTableList: [],
      showHandleDetail: false,
      currentRentType: null,
      rentForm: {
        lesseeYears: "",
        lesseeUse: "",
        renter: "",
        rentBegin: "",
        rentEnd: "",
        rentMoney: "",
        deposit: "",
        rentType: "",
        rentMode: "",
        contacts: "",
        contactPhone: "",
        remark: "",
      },
      wall_coordinate: [], // 3d墙坐标
      firstInitThreeD: true,
      configPX: {
        height: 1080,
        width: 1920,
      },
      panning: false,
      threeSacle: 2,
      viewFlag: false,
      loading: null,
      webglFlag: true,
      areaTitle: "使用面积",
    };
  },
  methods: {
    // 初始化
    init() {
      // 初始化canvas画布
      this.canvas = new fabric.Canvas("c");
      fabric.Object.prototype.originX = "left";
      fabric.Object.prototype.originY = "top";
      window.canvas = this.canvas;
      // 设置画布比例
      this.setZoom();
      window.onresize = () => {
        this.setZoom();
      };
      this.setCommonEvent();
    },
    // 设置画布比例
    setZoom() {
      const zoom = 1;
      const canvasDiv = $(this.canvas.wrapperEl).parent();
      this.canvasWidth = canvasDiv.width();
      this.canvasHeight = canvasDiv.height();
      const cHeight = this.canvas.height;
      const cWidth = this.canvas.width;
      let height = this.canvasHeight > cHeight ? this.canvasHeight : cHeight;
      let width = this.canvasWidth > cWidth ? this.canvasWidth : cWidth;
      if (width > height) {
        //横版
        width = this.canvasWidth;
        height = this.canvasHeight;
      } else {
        //竖版
        height = ((height * this.canvasHeight) / this.configPX.height) * 0.8;
      }
      window.zoom = zoom;
      this.canvas.setZoom(zoom);
      this.canvas.setWidth(width);
      this.canvas.setHeight(height);
      this.canvas.renderAll();
      const self = this;
      mouseEvent.wheel({
        target: canvasDiv[0],
        callback(e) {
          let zoom = (e.deltaY > 0 ? 0.1 : -0.1) + self.canvas.getZoom();
          zoom = Math.max(0.1, zoom); //最小为原来的1/10
          zoom = Math.min(3, zoom); //最大是原来的3倍
          let zoomPoint = new fabric.Point(e.pageX, e.pageY);
          self.canvas.zoomToPoint(zoomPoint, zoom);
        },
      });
    },
    setCommonEvent() {
      const _this = this;
      this.canvas.on("selection:created", function (e) {
        const target = e.target;
        target.hasControls = false;
        target.lockMovementX = target.lockMovementY = true;
        target.borderColor = "red";
        target.borderScaleFactor = 2;
        if (target.typeName === "room") {
          const h = $(".house-detail-info").height();
          $(".house-detail-info").animate({ bottom: 0 });
          const uuid = e.target.uuid;
          _this.existHouseInfo.forEach((element) => {
            if (uuid === element.houseId) {
              const tempObj = {};
              _this.houseBaseInfo = [];
              _this.houseUseInfo = [];
              tempObj.houseBuildArea = element.houseBuildArea || "暂无";
              tempObj.houseNumber = element.houseNumber || "暂无";
              tempObj.houseUseArea = element.houseUseArea || "暂无";
              tempObj.useType = element.useType || "暂无";
              _this.houseType.forEach((type) => {
                if (element.housetypeId === type.housetypeId) {
                  tempObj.housetypeName = type.housetypeName;
                }
              });
              tempObj.housetypeName = tempObj.housetypeName || "暂无";
              if (element.housetypeId === 4) {
                _this.areaTitle = "建筑面积";
              } else {
                _this.areaTitle = "使用面积";
              }
              _this.houseBaseInfo.push(tempObj);
              if (element.rent) {
                // 出租
                _this.rentUseInfo = [];
                _this.houseUseType = 4; // 出租出借
                if (element.rent.type === 1) {
                  $(".house-unit-header").text("出租信息");
                  _this.currentRentType = 1;
                } else if (element.rent.type === 2) {
                  $(".house-unit-header").text("出借信息");
                  _this.currentRentType = 2;
                }
                for (let key in _this.rentForm) {
                  if (
                    element.rent[key] !== "" &&
                    element.rent[key] !== null &&
                    element.rent[key] !== undefined
                  ) {
                    if (key === "rentBegin" || key === "rentEnd") {
                      _this.rentForm[key] = _this.formatDate(element.rent[key]);
                    } else if (key === "rentType") {
                      switch (element.rent[key]) {
                        case 1:
                          _this.rentForm[key] = "直接租赁";
                          break;
                        case 2:
                          _this.rentForm[key] = "转租赁";
                          break;
                        case 3:
                          _this.rentForm[key] = "回租租赁";
                          break;
                      }
                    } else if (key === "rentMode") {
                      switch (element.rent[key]) {
                        case 1:
                          _this.rentForm[key] = "承租";
                          break;
                        case 2:
                          _this.rentForm[key] = "预租";
                          break;
                        case 3:
                          _this.rentForm[key] = "转租";
                          break;
                        case 4:
                          _this.rentForm[key] = "委托出租";
                          break;
                        case 5:
                          _this.rentForm[key] = "代理出租";
                          break;
                        case 6:
                          _this.rentForm[key] = "承租权转让";
                          break;
                        case 7:
                          _this.rentForm[key] = "承租权交换";
                          break;
                      }
                    } else {
                      _this.rentForm[key] = element.rent[key];
                    }
                  } else {
                    _this.rentForm[key] = "暂无";
                  }
                }
                const rentObj = JSON.parse(JSON.stringify(_this.rentForm));
                _this.rentUseInfo.push(rentObj);
              } else if (element.housetypeId === 1 && element.isUse === 1) {
                // 办公用房
                if (
                  element.housingUseVoList &&
                  element.housingUseVoList.length > 0 &&
                  element.housingUseVoList[0].unitId !== null &&
                  element.housingUseVoList[0].unitId !== ""
                ) {
                  // 非领导
                  $(".house-unit-header").text("使用信息");
                  _this.houseUseType = 3;
                  const unitId = element.housingUseVoList[0].unitId;
                  const tempObj = {};
                  _this.unitOptions.forEach((unitInfo) => {
                    if (unitInfo.unitId === unitId) {
                      tempObj.unitName = unitInfo.unitName;
                    }
                  });
                  _this.houseUseInfo.push(tempObj);
                } else if (
                  element.leadingOfficeVo &&
                  element.leadingOfficeVo.unitId !== null &&
                  element.leadingOfficeVo.unitId !== ""
                ) {
                  // 领导
                  $(".house-unit-header").text("使用信息");
                  _this.houseUseType = 2;
                  _this.houseLeaderUnitInfo = [];
                  _this.houseLeaderInfo = [];
                  const tempObjUnit = {};
                  const unitId = element.leadingOfficeVo.unitId;
                  const leadingOfficeList =
                    element.leadingOfficeVo.leadingOfficeList;
                  _this.unitOptions.forEach((unitInfo) => {
                    if (unitInfo.unitId === unitId) {
                      tempObjUnit.unitName = unitInfo.unitName;
                    }
                  });
                  leadingOfficeList.forEach((item) => {
                    _this.profession.forEach((pro) => {
                      if (pro.professionalId === item.professionalId) {
                        item.professionalName = pro.professionalName;
                      }
                    });
                    _this.workStatue.forEach((work) => {
                      if (
                        work.workstatusId === item.leadingofficeWorkStatueId
                      ) {
                        item.leadingofficeWorkStatue = work.workstatusName;
                      }
                    });
                    item.professionalLevelName = _this.getProfessionalLevel(
                      item.professionalLevel
                    );
                  });
                  _this.houseLeaderUnitInfo.push(tempObjUnit);
                  _this.houseLeaderInfo = leadingOfficeList;
                }
              } else if (element.housetypeId !== 1 && element.isUse === 1) {
                // 非办公用房
                $(".house-unit-header").text("使用信息");
                _this.houseUseType = 3;
                const unitId = element.housingUseUnitList[0].unitId;
                const tempObj = {};
                _this.unitOptions.forEach((unitInfo) => {
                  if (unitInfo.unitId === unitId) {
                    tempObj.unitName = unitInfo.unitName;
                  }
                });
                _this.houseUseInfo.push(tempObj);
              } else if (element.isUse === 0) {
                // 闲置
                $(".house-unit-header").text("使用信息");
                _this.houseUseType = 1; // 闲置
              }
              if (element.handleWay && element.handleWay === 1) {
                _this.showHandleDetail = true;
                _this.handleTableList = [];
                leaderDetail(element.handleId)
                  .then((res) => {
                    const tempObj = {
                      handleWay: "转换用途",
                      cpsComment: "",
                      cpsTime: "",
                      cpsUser: "",
                      ponUse: "",
                    };
                    let keyArr = [];
                    let allInfo = {};
                    res.documents.forEach((item) => {
                      if (item) {
                        for (let key in item[0]) {
                          keyArr.push(key);
                          allInfo[key] = item[0][key];
                        }
                      }
                    });
                    if (allInfo.conversionPurposesDb !== undefined) {
                      for ([key, value] of Object.entries(
                        allInfo.conversionPurposesDb
                      )) {
                        if (tempObj[key] !== undefined) {
                          if (value) {
                            tempObj[key] = value;
                          } else {
                            tempObj[key] = "暂无";
                          }
                        }
                      }
                      _this.handleTableList.push(tempObj);
                    }
                  })
                  .catch((err) => {
                    const msg = err.result ? err.result : "处置详情获取失败";
                    _this.$message({
                      message: msg,
                      type: "error",
                    });
                  });
              } else {
                _this.showHandleDetail = false;
              }
            }
          });
        } else {
          _this.closeHouseDetailBox();
        }
      });
      this.canvas.on("selection:updated", function (e) {
        const target = e.target;
        target.hasControls = false;
        target.lockMovementX = target.lockMovementY = true;
        target.borderColor = "red";
        target.borderScaleFactor = 2;
        if (target.typeName === "room") {
          const h = $(".house-detail-info").height();
          $(".house-detail-info").animate({ bottom: 0 });
          const uuid = e.target.uuid;
          _this.existHouseInfo.forEach((element) => {
            if (uuid === element.houseId) {
              const tempObj = {};
              _this.houseBaseInfo = [];
              _this.houseUseInfo = [];
              tempObj.houseBuildArea = element.houseBuildArea || "暂无";
              tempObj.houseNumber = element.houseNumber || "暂无";
              tempObj.houseUseArea = element.houseUseArea || "暂无";
              tempObj.useType = element.useType || "暂无";
              _this.houseType.forEach((type) => {
                if (element.housetypeId === type.housetypeId) {
                  tempObj.housetypeName = type.housetypeName;
                }
              });
              tempObj.housetypeName = tempObj.housetypeName || "暂无";
              if (element.housetypeId === 4) {
                _this.areaTitle = "建筑面积";
              } else {
                _this.areaTitle = "使用面积";
              }
              _this.houseBaseInfo.push(tempObj);
              if (element.rent) {
                // 出租
                _this.rentUseInfo = [];
                _this.houseUseType = 4; // 出租出借
                if (element.rent.type === 1) {
                  $(".house-unit-header").text("出租信息");
                  _this.currentRentType = 1;
                } else if (element.rent.type === 2) {
                  $(".house-unit-header").text("出借信息");
                  _this.currentRentType = 2;
                }
                for (let key in _this.rentForm) {
                  if (
                    element.rent[key] !== "" &&
                    element.rent[key] !== null &&
                    element.rent[key] !== undefined
                  ) {
                    if (key === "rentBegin" || key === "rentEnd") {
                      _this.rentForm[key] = _this.formatDate(element.rent[key]);
                    } else if (key === "rentType") {
                      switch (element.rent[key]) {
                        case 1:
                          _this.rentForm[key] = "直接租赁";
                          break;
                        case 2:
                          _this.rentForm[key] = "转租赁";
                          break;
                        case 3:
                          _this.rentForm[key] = "回租租赁";
                          break;
                      }
                    } else if (key === "rentMode") {
                      switch (element.rent[key]) {
                        case 1:
                          _this.rentForm[key] = "承租";
                          break;
                        case 2:
                          _this.rentForm[key] = "预租";
                          break;
                        case 3:
                          _this.rentForm[key] = "转租";
                          break;
                        case 4:
                          _this.rentForm[key] = "委托出租";
                          break;
                        case 5:
                          _this.rentForm[key] = "代理出租";
                          break;
                        case 6:
                          _this.rentForm[key] = "承租权转让";
                          break;
                        case 7:
                          _this.rentForm[key] = "承租权交换";
                          break;
                      }
                    } else {
                      _this.rentForm[key] = element.rent[key];
                    }
                  } else {
                    _this.rentForm[key] = "暂无";
                  }
                }
                const rentObj = JSON.parse(JSON.stringify(_this.rentForm));
                _this.rentUseInfo.push(rentObj);
              } else if (element.housetypeId === 1 && element.isUse === 1) {
                // 办公用房
                if (
                  element.housingUseVoList &&
                  element.housingUseVoList.length > 0 &&
                  element.housingUseVoList[0].unitId !== null &&
                  element.housingUseVoList[0].unitId !== ""
                ) {
                  // 非领导
                  $(".house-unit-header").text("使用信息");
                  _this.houseUseType = 3;
                  const unitId = element.housingUseVoList[0].unitId;
                  const tempObj = {};
                  _this.unitOptions.forEach((unitInfo) => {
                    if (unitInfo.unitId === unitId) {
                      tempObj.unitName = unitInfo.unitName;
                    }
                  });
                  _this.houseUseInfo.push(tempObj);
                } else if (
                  element.leadingOfficeVo &&
                  element.leadingOfficeVo.unitId !== null &&
                  element.leadingOfficeVo.unitId !== ""
                ) {
                  // 领导
                  $(".house-unit-header").text("使用信息");
                  _this.houseUseType = 2;
                  _this.houseLeaderUnitInfo = [];
                  _this.houseLeaderInfo = [];
                  const tempObjUnit = {};
                  const unitId = element.leadingOfficeVo.unitId;
                  const leadingOfficeList =
                    element.leadingOfficeVo.leadingOfficeList;
                  _this.unitOptions.forEach((unitInfo) => {
                    if (unitInfo.unitId === unitId) {
                      tempObjUnit.unitName = unitInfo.unitName;
                    }
                  });
                  leadingOfficeList.forEach((item) => {
                    _this.profession.forEach((pro) => {
                      if (pro.professionalId === item.professionalId) {
                        item.professionalName = pro.professionalName;
                      }
                    });
                    _this.workStatue.forEach((work) => {
                      if (
                        work.workstatusId === item.leadingofficeWorkStatueId
                      ) {
                        item.leadingofficeWorkStatue = work.workstatusName;
                      }
                    });
                    item.professionalLevelName = _this.getProfessionalLevel(
                      item.professionalLevel
                    );
                  });
                  _this.houseLeaderUnitInfo.push(tempObjUnit);
                  _this.houseLeaderInfo = leadingOfficeList;
                }
              } else if (element.housetypeId !== 1 && element.isUse === 1) {
                // 非办公用房
                $(".house-unit-header").text("使用信息");
                _this.houseUseType = 3;
                const unitId = element.housingUseUnitList[0].unitId;
                const tempObj = {};
                _this.unitOptions.forEach((unitInfo) => {
                  if (unitInfo.unitId === unitId) {
                    tempObj.unitName = unitInfo.unitName;
                  }
                });
                _this.houseUseInfo.push(tempObj);
              } else if (element.isUse === 0) {
                // 闲置
                $(".house-unit-header").text("使用信息");
                _this.houseUseType = 1; // 闲置
              }
            }
          });
        } else {
          _this.closeHouseDetailBox();
        }
      });
      this.canvas.on("mouse:down", function (options) {
        if (options.e.altKey) {
          _this.panning = true;
        }
      });
      this.canvas.on("mouse:move", function (options) {
        if (_this.panning && options && options.e) {
          var delta = new fabric.Point(
            options.e.movementX,
            options.e.movementY
          );
          _this.canvas.relativePan(delta);
          return;
        }
      });
      this.canvas.on("mouse:up", function (options) {
        _this.panning = false;
        _this.canvas.selection = false;
      });
    },
    // 初始化数据
    initCommonData() {
      getHouseTypeList()
        .then((res) => {
          this.houseType = res;
        })
        .catch((err) => {
          const msg = err.result ? err.result : "房屋形式获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
      getAllUnitList()
        .then((res) => {
          this.unitOptions = res;
        })
        .catch((err) => {
          const msg = err.result ? err.result : "单位名称获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
      getAllProfessionList()
        .then((res) => {
          this.profession = res;
        })
        .catch((err) => {
          const msg = err.result ? err.result : "职称获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
      workingConditions()
        .then((res) => {
          this.workStatue = res;
        })
        .catch((err) => {
          const msg = err.result ? err.result : "在职情况获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    // 取数据类型
    getDataType(obj) {
      var typeArr = ["String", "Object", "Number", "Array", "Date"];
      for (let name of typeArr) {
        if (Object.prototype.toString.call(obj) === `[object ${name}]`) {
          return name;
        }
      }
    },
    // 获取楼层示意图
    getHouseGraphJson(buildId, floorName) {
      if (!this.loading) {
        this.loading = this.$loading({
          target: ".plane",
          lock: true,
          text: "拼命加载中",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
        });
      }
      this.existHouseId = [];
      this.activeBtnIndex = 0;
      getHouseGraphJson(buildId, floorName)
        .then((res) => {
          if (res === "" || res === null) {
            this.$message({
              message: "该楼层暂无示意图",
              type: "warning",
            });
            if (this.loading) {
              this.loading.close();
              this.loading = null;
            }
            if (this.canvas) this.canvas.clear();
            this.$refs.threeD.clear();
            return false;
          }
          this.canvas.loadFromJSON(res, () => {
            const existHouseObj = this.canvas.getObjects("group");
            if (existHouseObj.length > 0) {
              existHouseObj.forEach((item) => {
                if (this.rentList.indexOf(item.uuid) > -1) {
                  const imgobj = item.getObjects("image");
                  const imgLen = imgobj.length;
                  fabric.Image.fromURL(
                    require("../../assets/imgs/rent-icon.png"),
                    (img) => {
                      const obj_w = item.width;
                      const obj_h = item.height;
                      const img_w = img.width;
                      const img_h = img.height;
                      let topVal;
                      if (imgLen === 0) {
                        topVal = -(obj_h / 2) + img_h;
                      } else {
                        topVal = -(obj_h / 2) + (img_h * (imgLen + 1) + 10);
                      }
                      img.set({
                        originX: "center",
                        originY: "center",
                        top: topVal,
                        left: obj_w / 2 - 50,
                      });
                      img.toObject = (function (toObject) {
                        return function () {
                          return fabric.util.object.extend(
                            toObject.call(this),
                            {
                              textType: this.textType,
                            }
                          );
                        };
                      })(img.toObject);
                      item.add(img);
                      img.textType = "rent";
                      this.canvas.renderAll();
                    }
                  );
                } else if (this.lentList.indexOf(item.uuid) > -1) {
                  const imgobj = item.getObjects("image");
                  const imgLen = imgobj.length;
                  fabric.Image.fromURL(
                    require("../../assets/imgs/lent-icon.png"),
                    (img) => {
                      const obj_w = item.width;
                      const obj_h = item.height;
                      const img_w = img.width;
                      const img_h = img.height;
                      let topVal;
                      if (imgLen === 0) {
                        topVal = -(obj_h / 2) + img_h;
                      } else {
                        topVal = -(obj_h / 2) + (img_h * (imgLen + 1) + 10);
                      }
                      img.set({
                        originX: "center",
                        originY: "center",
                        top: topVal,
                        left: obj_w / 2 - 50,
                      });
                      img.toObject = (function (toObject) {
                        return function () {
                          return fabric.util.object.extend(
                            toObject.call(this),
                            {
                              textType: this.textType,
                            }
                          );
                        };
                      })(img.toObject);
                      item.add(img);
                      img.textType = "lent";
                      this.canvas.renderAll();
                    }
                  );
                } else if (this.transformList.indexOf(item.uuid) > -1) {
                  const imgobj = item.getObjects("image");
                  const imgLen = imgobj.length;
                  fabric.Image.fromURL(
                    require("../../assets/imgs/transform-icon.png"),
                    (img) => {
                      const obj_w = item.width;
                      const obj_h = item.height;
                      const img_w = img.width;
                      const img_h = img.height;
                      let topVal;
                      if (imgLen === 0) {
                        topVal = -(obj_h / 2) + img_h;
                      } else {
                        topVal = -(obj_h / 2) + (img_h * (imgLen + 1) + 10);
                      }
                      img.set({
                        originX: "center",
                        originY: "center",
                        top: topVal,
                        left: obj_w / 2 - 50,
                      });
                      img.toObject = (function (toObject) {
                        return function () {
                          return fabric.util.object.extend(
                            toObject.call(this),
                            {
                              textType: this.textType,
                            }
                          );
                        };
                      })(img.toObject);
                      item.add(img);
                      img.textType = "transform";
                      this.canvas.renderAll();
                    }
                  );
                }
                if (item.typeName === "room") {
                  this.existHouseId.push(item.uuid);
                }
              });
            }
            if (this.loading) {
              this.loading.close();
              this.loading = null;
            }
          });
        })
        .catch((err) => {
          this.$message({
            message: err.result ? err.result : "示意图加载失败！",
            type: "error",
          });
          if (this.loading) {
            this.loading.close();
            this.loading = null;
          }
          if (this.canvas) this.canvas.clear();
          this.$refs.threeD.clear();
        });
    },
    // 初始化整层楼三维模型信息
    initWebgl() {
      // loading
      this.wall_coordinate = [];
      const existHouseObj = this.canvas.getObjects("group");
      if (existHouseObj.length > 0) {
        existHouseObj.forEach((item, index) => {
          if (item.typeName === "room") {
            const commonRoomNum = item.getObjects("rect");
            const specialRoomNum = item.getObjects("path");
            if (commonRoomNum.length > 0) {
              this.existHouseId.push(item.uuid);
              let line_1 = [];
              let line_2 = [];
              let line_3 = [];
              let line_4 = [];
              let aCoordsObj = JSON.parse(JSON.stringify(item.aCoords));
              for (let key in aCoordsObj) {
                aCoordsObj[key].x = aCoordsObj[key].x * this.threeSacle;
                aCoordsObj[key].y = aCoordsObj[key].y * this.threeSacle;
              }
              line_1.push(aCoordsObj.tl);
              line_1.push(aCoordsObj.tr);
              line_2.push(aCoordsObj.tr);
              line_2.push(aCoordsObj.br);
              line_3.push(aCoordsObj.tl);
              line_3.push(aCoordsObj.bl);
              line_4.push(aCoordsObj.bl);
              line_4.push(aCoordsObj.br);
              this.wall_coordinate.push(line_1);
              this.wall_coordinate.push(line_2);
              this.wall_coordinate.push(line_3);
              this.wall_coordinate.push(line_4);
            } else if (specialRoomNum.length > 0) {
              const path = specialRoomNum[0].path;
              for (let i = 0; i < path.length; i++) {
                const point = path[i];
                const nextPoint = path[i + 1];
                const line = [];
                if (
                  (point[0] === "M" || point[0] === "L") &&
                  nextPoint &&
                  nextPoint[0] !== "z"
                ) {
                  line.push({
                    x: point[1] * this.threeSacle,
                    y: point[2] * this.threeSacle,
                  });
                  line.push({
                    x: nextPoint[1] * this.threeSacle,
                    y: nextPoint[2] * this.threeSacle,
                  });
                  this.wall_coordinate.push(line);
                }
              }
              const line = [];
              const firstPoint = path[0];
              const lastPoint = path[path.length - 2];
              line.push({
                x: lastPoint[1] * this.threeSacle,
                y: lastPoint[2] * this.threeSacle,
              });
              line.push({
                x: firstPoint[1] * this.threeSacle,
                y: firstPoint[2] * this.threeSacle,
              });
              this.wall_coordinate.push(line);
            }
          }
        });
      }
      const lines = this.canvas.getObjects("line");
      if (lines.length > 0) {
        lines.forEach((item) => {
          const aCoordsObj = JSON.parse(JSON.stringify(item.aCoords));
          if (item.width > 0 && item.height === 0) {
            // 横线
            const line = [];
            line.push({
              x: aCoordsObj.tl.x * this.threeSacle,
              y: aCoordsObj.tl.y * this.threeSacle,
            });
            line.push({
              x: aCoordsObj.tr.x * this.threeSacle,
              y: aCoordsObj.tr.y * this.threeSacle,
            });
            this.wall_coordinate.push(line);
          } else if (item.width === 0 && item.height > 0) {
            // 竖线
            const line = [];
            line.push({
              x: aCoordsObj.tr.x * this.threeSacle,
              y: aCoordsObj.tr.y * this.threeSacle,
            });
            line.push({
              x: aCoordsObj.br.x * this.threeSacle,
              y: aCoordsObj.br.y * this.threeSacle,
            });
            this.wall_coordinate.push(line);
          } else if (item.width > 0 && item.height > 0) {
            // 斜线
            if ((item.x1 > 0 && item.y1 > 0) || (item.x1 < 0 && item.y1 < 0)) {
              const line = [];
              line.push({
                x: aCoordsObj.tl.x * this.threeSacle,
                y: aCoordsObj.tl.y * this.threeSacle,
              });
              line.push({
                x: aCoordsObj.br.x * this.threeSacle,
                y: aCoordsObj.br.y * this.threeSacle,
              });
              this.wall_coordinate.push(line);
            } else if (
              (item.x1 > 0 && item.y1 < 0) ||
              (item.x1 < 0 && item.y1 > 0)
            ) {
              const line = [];
              line.push({
                x: aCoordsObj.tr.x * this.threeSacle,
                y: aCoordsObj.tr.y * this.threeSacle,
              });
              line.push({
                x: aCoordsObj.bl.x * this.threeSacle,
                y: aCoordsObj.bl.y * this.threeSacle,
              });
              this.wall_coordinate.push(line);
            }
          }
        });
      }
      if (existHouseObj.length > 0 || lines.length > 0) {
        this.loading = this.$loading({
          target: ".threeD",
          lock: true,
          text: "拼命加载中",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
        });
        this.$refs.threeD.init(this.wall_coordinate);
      }
    },
    // 三维模型初始化完成
    initWebglFinished() {
      this.loading.close();
    },
    // 获取整层楼房屋信息
    getAllHouseInfo(buildId, floorName) {
      getAllHouseInfo(buildId, floorName)
        .then((res) => {
          this.existHouseInfo = res;
          this.rentList = [];
          this.lentList = [];
          this.transformList = [];
          this.existHouseInfo.forEach((item) => {
            switch (item.handleWay) {
              case 1:
                this.transformList.push(item.houseId);
                break;
              case 2:
                this.rentList.push(item.houseId);
                break;
              case 6:
                this.lentList.push(item.houseId);
                break;
            }
          });
        })
        .catch((err) => {
          const msg = err.result ? err.result : "楼层房间详情获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    // 打开附件模态框
    accessoryHandle(num) {
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      if (!buildId || !floorName) {
        this.$message({
          message: "请先选择楼层",
          type: "warning",
        });
        return false;
      }
      this.activeIconIndex = num;
      this.accessoryDialogVisible = true;
    },
    // 关闭附件模态框
    accessoryDialogClosed() {
      this.accessoryDialogVisible = false;
      this.CADDownload = [];
    },
    // 查询CAD
    seachCAD(buildId, floorName) {
      seachCAD(buildId, floorName)
        .then((res) => {
          if (res && res.length > 0) {
            res.forEach((item, index) => {
              const obj = {
                name: item.floordrawingFileName,
                id: item.floordrawingId,
              };
              this.fileList.push(obj);
            });
          } else {
            this.fileList = [];
          }
        })
        .catch((err) => {
          this.fileList = [];
          const msg = err.result ? err.result : "CAD获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    // 下载cad文件
    downloadCAD() {
      let tempObj = {};
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      tempObj.buildId = buildId;
      tempObj.floorName = floorName;
      tempObj.floorDrawingIdList = this.CADDownload;
      if (this.CADDownload.length > 0) {
        downloadCAD(tempObj)
          .then((res) => {
            let fileDownload = require("js-file-download");
            let name = decodeURIComponent(
              sessionStorage.getItem("CADdownloadFileName")
            );
            fileDownload(res, name);
          })
          .catch((err) => {
            const msg = err.result ? err.result : "CAD下载失败";
            this.$message({
              message: msg,
              type: "error",
            });
          });
      } else {
        this.$message({
          message: "请先选中一个CAD文件",
          type: "warning",
        });
      }
    },
    // 平面展示
    planeDisplay() {
      this.activeBtnIndex = 0;
    },
    // 3D展示
    threeDisplay() {
      if (this.webglFlag === false) {
        this.$message({
          message: "该浏览器不支持3D显示功能！",
          type: "warning",
        });
        return false;
      }
      this.activeBtnIndex = 1;
      this.initWebgl();
    },
    // 关闭房间详情框
    closeHouseDetailBox() {
      const h = $(".house-detail-info").height();
      $(".house-detail-info").animate({ bottom: -h - 10 });
      this.houseBaseInfo = [];
      this.houseUseInfo = [];
      this.houseLeaderInfo = [];
      this.houseLeaderUnitInfo = [];
      this.rentUseInfo = [];
      this.handleTableList = [];
    },
    // 开始编辑
    startEdit() {
      const yardId = sessionStorage.getItem("yardId");
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      const unitId = sessionStorage.getItem("unitId") || null;
      this.$router.push({
        name: "houseGraph",
        params: {
          yardId: yardId,
          buildId: buildId,
          floorName: floorName,
          unitId: unitId,
        },
      });
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
        S: date.getMilliseconds(), //毫秒
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
    },
    // 获取等级
    getProfessionalLevel(level) {
      switch (level) {
        case 1:
          return "中央机关";
          break;
        case 2:
          return "省级机关";
          break;
        case 3:
          return "市级机关";
          break;
        case 4:
          return "县级机关";
          break;
      }
    },
    // 判断是否支持webgl
    showWebgl() {
      if (!Detector.webgl) {
        this.webglFlag = false;
        this.$alert(
          "该浏览器不支持webgl，无法使用3D显示功能，建议使用高版本的谷歌浏览器查看。",
          "关于3D显示的提醒",
          {
            type: "warning",
            confirmButtonText: "确定",
          }
        );
      }
    },
  },
  created() {
    if (
      this.$route.params.yardId &&
      this.$route.params.buildId &&
      this.$route.params.floorName
    ) {
      this.viewFlag = true;
      if (this.$route.params.flag) {
        sessionStorage.setItem("yardId", this.$route.params.yardId);
        sessionStorage.setItem("buildId", this.$route.params.buildId);
        sessionStorage.setItem("floorName", this.$route.params.floorName);
        if (this.$route.params.unitId)
          sessionStorage.setItem("unitId", this.$route.params.unitId);
        let tempObj = {};
        tempObj.yardId = this.$route.params.yardId;
        tempObj.buildId = this.$route.params.buildId;
        tempObj.floorName = this.$route.params.floorName;
        tempObj.unitId = this.$route.params.unitId;
        this.$emit("startView", tempObj);
      }
    }
    this.showWebgl();
    this.initCommonData();
    eventBus.$off("getGraphJsonView");
    eventBus.$off("closeHouseDetailBox");
    eventBus.$on("getGraphJsonView", (obj) => {
      if (this.canvas) this.canvas.clear();
      this.fileList = [];
      this.getHouseGraphJson(obj.buildId, obj.floorName);
      this.getAllHouseInfo(obj.buildId, obj.floorName);
      this.seachCAD(obj.buildId, obj.floorName);
    });
    eventBus.$on("closeHouseDetailBox", (obj) => {
      this.closeHouseDetailBox();
    });
  },
  mounted() {
    if (this.viewFlag) {
      this.$nextTick(() => {
        this.init();
        const buildId = sessionStorage.getItem("buildId");
        const floorName = sessionStorage.getItem("floorName");
        this.getHouseGraphJson(buildId, floorName);
        this.getAllHouseInfo(buildId, floorName);
        this.seachCAD(buildId, floorName);
      });
    }
  },
};
</script>

<style>
#houseGraphView {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  overflow: hidden;
}
#houseGraphView .canvasDiv {
  width: 100%;
  height: 100%;
  background: url("../../assets/imgs/canvas_bg.png") repeat;
}
#houseGraphView .btn-box > button {
  position: absolute;
  width: 110px;
  height: 40px;
  background: #3891e0;
  color: #fff;
  border-radius: 5px;
  font-size: 16px;
  font-weight: 600;
}
#houseGraphView .btn-box > button:hover {
  background: #2378c4;
}
#houseGraphView .btn-box > button.threeD-display-btn {
  right: 20px;
  bottom: 30px;
}
#houseGraphView .btn-box > button.cad-display-btn {
  right: 20px;
  bottom: 130px;
}
#houseGraphView .btn-box > button.plane-display-btn {
  right: 20px;
  bottom: 80px;
}
#houseGraphView .btn-box > .active {
  background: #2378c4;
}
#houseGraphView .plane,
#houseGraphView .threeD {
  width: 100%;
  height: 100%;
}
#houseGraphView .accessory-box {
  position: absolute;
  top: 20px;
  left: 20px;
  z-index: 10;
}
#houseGraphView .accessory-box .accessory-icon {
  width: 40px;
  height: 40px;
  border-radius: 3px;
  border: 1px solid #1c9ee5;
  background: #fff;
  cursor: pointer;
  color: #1c9ee5;
  text-align: center;
  line-height: 40px;
}
#houseGraphView .accessory-box .accessory-icon > span {
  font-size: 26px;
}
#houseGraphView .accessory-item {
  line-height: 30px;
}
#houseGraphView .accessoryDialog .el-dialog__footer {
  text-align: center;
}
#houseGraphView .house-detail-info {
  width: 80%;
  height: 280px;
  background: #f8f8f8;
  border: 1px solid #e2e2e2;
  position: absolute;
  bottom: -280px;
  left: 50%;
  transform: translateX(-50%);
  box-sizing: border-box;
  z-index: 1000;
}
#houseGraphView .house-base-info {
  width: 50%;
  height: 100%;
  padding-right: 15px;
  float: left;
  box-sizing: border-box;
}
#houseGraphView .house-unit-info {
  width: 50%;
  height: 100%;
  padding-left: 15px;
  float: left;
  box-sizing: border-box;
}
#houseGraphView .house-base-header,
#houseGraphView .house-unit-header {
  height: 50px;
  line-height: 50px;
  padding-left: 18px;
  color: #1f8dc8;
  border-bottom: 1px solid #1f8dc8;
  font-size: 18px;
}
#houseGraphView .house-base-body {
  width: 100%;
  box-sizing: border-box;
}
#houseGraphView .el-table--border::after,
#houseGraphView .el-table--group::after,
#houseGraphView .el-table::before {
  background-color: #d4d4d4;
}
#houseGraphView .el-table__empty-block {
  background-color: #f9f9f9;
}
#houseGraphView .house-detail-info .el-dialog__headerbtn {
  top: 16px;
  right: 16px;
}
#houseGraphView .start-edit-box {
  position: absolute;
  top: 20px;
  left: 5px;
  z-index: 10;
}
#houseGraphView .start-edit-box .edit-icon {
  width: 40px;
  height: 40px;
  border-radius: 3px;
  border: 1px solid #1c9ee5;
  background: #fff;
  cursor: pointer;
  color: #1c9ee5;
  text-align: center;
  line-height: 40px;
}
#houseGraphView .start-edit-box .edit-icon > span {
  font-size: 26px;
}
#houseGraphView .threeD {
  background: #4682b4;
}
#houseGraphView .house-type-box {
  position: absolute;
  right: 20px;
  bottom: 176px;
}
#houseGraphView .house-type-box > ul > li {
  line-height: 25px;
}
#houseGraphView .house-type-box > ul > li > span {
  display: inline-block;
  width: 20px;
  height: 15px;
  position: relative;
  top: 2px;
}
#houseGraphView .house-type-box > ul > li > span {
  display: inline-block;
  width: 20px;
  height: 15px;
  position: relative;
  top: 2px;
}
#houseGraphView .isExcess-icon-tips {
  background: url("../../assets/imgs/isExcess-icon.png") no-repeat right top;
}
#houseGraphView .isRecord-icon-tips {
  background: url("../../assets/imgs/isRecord-icon.png") no-repeat right top;
}
#houseGraphView .btn-box > button.threeDisabled {
  cursor: not-allowed;
  color: #fff;
  background-color: #c8c9cc;
  border-color: #c8c9cc;
}
@media only screen and (max-width: 1366px) {
  #houseGraphView .start-edit-box .edit-icon {
    height: 30px;
    width: 30px;
    line-height: 30px;
  }
  #houseGraphView .start-edit-box .edit-icon > span {
    font-size: 24px;
  }
}
</style>
