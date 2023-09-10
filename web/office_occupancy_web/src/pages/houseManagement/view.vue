<template>
  <div id="houseView">
    <baidu-map
      :center="center"
      id="baidu_map"
      ak="2IMiGQlksUfRyo9UiGGq6EY6EsZAtFAl"
      :zoom="zoom"
      :scroll-wheel-zoom="true"
      @ready="handler"
    >
      <!-- 点部分 -->
      <bm-marker
        v-for="item in yardList"
        :key="item.vkey"
        :position="{ lng: item.buildLongitude, lat: item.buildLatitude }"
        :icon="{ url: yardImageUrl, size: { width: 50, height: 39 } }"
        animation="BMAP_ANIMATION_BOUNCE"
        @click="yardMakerClick(item.buildId)"
      >
        <bm-label
          :content="item.buildName"
          :labelStyle="{
            color: '#333',
            fontSize: '14px',
            border: '1px solid #ccc',
          }"
          :offset="{ width: -10, height: 30 }"
        />
      </bm-marker>

      <bm-marker
        v-for="item in buildList"
        :key="item.vkey"
        :position="{ lng: item.buildLongitude, lat: item.buildLatitude }"
        :icon="{ url: buildImageUrl, size: { width: 39, height: 39 } }"
        animation="BMAP_ANIMATION_BOUNCE"
        @click="buildMakerClick(item.buildId)"
      >
        <bm-label
          :content="item.buildName"
          :labelStyle="{
            color: '#333',
            fontSize: '14px',
            border: '1px solid #ccc',
          }"
          :offset="{ width: -15, height: 30 }"
        />
      </bm-marker>

      <bm-info-window
        :width="350"
        :show="infoWindow.show"
        :title="infoWindow.buildName"
        :offset="{ width: -15, height: -40 }"
        :position="{ lng: infoWindow.lng, lat: infoWindow.lat }"
        @close="infoWindowClose"
        @open="infoWindowOpen"
      >
        <div class="yard-infoWindow" v-show="isYardWindowShow">
          <ul>
            <li class="clearfix">
              <span class="item-title"></span>
              <span class="item-content"
                >院落名称：{{ infoWindow.yardName }}</span
              >
            </li>
            <li class="clearfix">
              <span class="item-title"></span>
              <span class="item-content"
                >所属区域：{{ infoWindow.yardAdministrativeRegion }}</span
              >
            </li>
            <li class="clearfix">
              <span class="item-title"></span>
              <span class="item-content"
                >具体地址：{{ infoWindow.yardPosition }}</span
              >
            </li>
          </ul>
        </div>
        <div class="build-infoWindow" v-show="!isYardWindowShow">
          <div class="build-infoWindow-content clearfix">
            <div class="build-infoWindow-content-left">
              <img
                :src="getImgUrl(infoWindow.floorImg[0].floorVirtualUrl)"
                alt="楼座图片"
                style="width: 100%; height: 100%"
                v-if="infoWindow.floorImg.length > 0"
              />
              <img
                src="../../assets/imgs/assetBanner.png"
                alt="楼座图片"
                v-if="infoWindow.floorImg.length === 0"
              />
            </div>
            <div class="build-infoWindow-content-right">
              <ul>
                <li class="clearfix">
                  <span class="item-title"></span>
                  <span class="item-content"
                    >楼座名称：{{ infoWindow.buildName }}</span
                  >
                </li>
                <li class="clearfix">
                  <span class="item-title"></span>
                  <span class="item-content"
                    >建筑面积：{{ infoWindow.buildArea }}</span
                  >
                </li>
                <li class="clearfix">
                  <span class="item-title"></span>
                  <span class="item-content"
                    >所属区域：{{ infoWindow.yardAdministrativeRegion }}</span
                  >
                </li>
                <li class="clearfix">
                  <span class="item-title"></span>
                  <span class="item-content"
                    >具体地址：{{ infoWindow.yardPosition }}</span
                  >
                </li>
              </ul>
            </div>
          </div>
        </div>
      </bm-info-window>
    </baidu-map>

    <div class="build-detail-dialog" v-show="showBuildDetail">
      <div
        class="top-arrow-btn"
        v-show="!expandBuildDetail"
        @click="buildExpand"
      >
        <img src="../../assets/imgs/top-arrow-btn.png" alt="展开按钮" />
      </div>
      <div
        class="down-arrow-btn"
        v-show="expandBuildDetail"
        @click="buildCollapse"
      >
        <img src="../../assets/imgs/down-arrow-btn.png" alt="收缩按钮" />
      </div>
      <div class="build-detail-content">
        <el-tabs v-model="activeName" @tab-click="tabHandleClick">
          <el-tab-pane
            label="产权产籍信息"
            name="propert"
            v-if="!isYardDetail && !isUnitDetail"
          >
            <el-table
              :data="propertyTableData"
              :header-cell-style="tableHeaderCellStyle"
              :cell-style="tableCellStyle"
              :height="tableHeight"
              style="width: 100%"
            >
              <el-table-column
                prop="yardPosition"
                label="资产地址"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="warrantPerson"
                label="产权所有"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="buildArea"
                label="建筑面积(m²)"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="buildCertificate"
                label="土地证号"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="buildDate"
                label="建成时间"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="builder"
                label="施工单位"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="coveredArea"
                label="占地面积(m²)"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane
            label="资产情况信息"
            name="situation"
            v-if="!isYardDetail && !isUnitDetail"
          >
            <el-table
              :data="situationTableData"
              :header-cell-style="tableHeaderCellStyle"
              :cell-style="tableCellStyle"
              :height="tableHeight"
              style="width: 100%"
            >
              <el-table-column
                prop="useUnitName"
                label="使用单位名称"
                header-align="center"
                align="center"
                v-if="situationColumn.useUnitName"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="useUnitNum"
                label="使用单位数量"
                header-align="center"
                align="center"
                v-if="situationColumn.useUnitNum"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="enterPeopleNum"
                label="入驻人员数量"
                header-align="center"
                align="center"
                v-if="situationColumn.enterPeopleNum"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="officeNum"
                label="办公室用房数量"
                header-align="center"
                align="center"
                v-if="situationColumn.officeNum"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="rentArea"
                label="出租面积(m²)"
                header-align="center"
                align="center"
                v-if="showRentArea && situationColumn.rentArea"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="selfUseArea"
                label="自用面积(m²)"
                header-align="center"
                align="center"
                v-if="situationColumn.selfUseArea"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="spareArea"
                label="闲置面积(m²)"
                header-align="center"
                align="center"
                v-if="situationColumn.spareArea"
                :show-overflow-tooltip="true"
              ></el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane
            label="重大设备信息"
            name="device"
            v-if="!isYardDetail && !isUnitDetail && showAssetDevice"
          >
            <el-table
              :data="deviceTableData"
              :header-cell-style="tableHeaderCellStyle"
              :cell-style="tableCellStyle"
              :height="tableHeight"
              style="width: 100%"
            >
              <el-table-column
                prop="equipmentName"
                label="类别"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="equipmentNum"
                label="数量"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="equipmentCharge"
                label="负责人"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="equipmentContact"
                label="负责人联系方式"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane
            label="资产维修信息"
            name="maintain"
            v-if="!isYardDetail && !isUnitDetail && showRepair"
          >
            <el-button
              size="small"
              type="primary"
              class="more-btn"
              round
              v-if="repairTableData.length > 5"
              @click="jumpRepairPage"
            >
              更多
              <i class="el-icon-more el-icon--right"></i>
            </el-button>
            <el-table
              :data="repairTableData"
              :header-cell-style="tableHeaderCellStyle"
              :cell-style="tableCellStyle"
              :height="tableHeight"
              style="width: 100%"
            >
              <el-table-column
                prop="positionStr"
                label="维修地点"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="repairPartName"
                label="维修部位"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="repairModuleTarget"
                label="维修内容"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="repairModuleArea"
                label="维修面积(m²)"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane
            label="房产证信息"
            name="houseCertificate"
            v-if="!isYardDetail && !isUnitDetail && showHouseCertificate"
          >
            <el-table
              :data="houseCertificateTableData"
              :header-cell-style="tableHeaderCellStyle"
              :cell-style="tableCellStyle"
              :height="tableHeight"
              style="width: 100%"
            >
              <el-table-column
                prop="housePropertyCardNumber"
                label="房产证号"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="buildName"
                label="楼栋/幢号"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="owner"
                label="所有权人"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="totalLayerNum"
                label="总层数"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="buildArea"
                label="建筑面积(m²)"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane
            label="不动产信息"
            name="immovables"
            v-if="!isYardDetail && !isUnitDetail && showImmovables"
          >
            <el-table
              :data="immovablesTableData"
              :header-cell-style="tableHeaderCellStyle"
              :cell-style="tableCellStyle"
              :height="tableHeight"
              style="width: 100%"
            >
              <el-table-column
                prop="warrantNum"
                label="权证编号"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="obligee"
                label="权利人"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="buildName"
                label="楼栋/幢号"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="unitNum"
                label="单元号"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="application"
                label="用途"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="liveTime"
                label="使用期限(年)"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="certificationDate"
                label="发证日期"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="certificationOffice"
                label="发证机关"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane
            label="物业信息"
            name="estate"
            v-if="isYardDetail && !isUnitDetail"
          >
            <el-table
              :data="estateDataList"
              :header-cell-style="tableHeaderCellStyle"
              :cell-style="tableCellStyle"
              :height="tableHeight"
              style="width: 100%"
            >
              <el-table-column
                prop="serialNum"
                label="序号"
                width="50"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="estateName"
                label="物业名称"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="serviceContentText"
                label="服务内容"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="startTime"
                label="开始使用时间"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="endTime"
                label="结束使用时间"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="contractAmount"
                label="使用金额(元)"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="单位信息" name="unit" v-if="isUnitDetail">
            <el-table
              :data="unitDataList"
              :header-cell-style="tableHeaderCellStyle"
              :cell-style="tableCellStyle"
              :height="tableHeight"
              style="width: 100%"
            >
              <el-table-column
                prop="houseTypeName"
                label="房间类型"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="houseNum"
                label="房间数"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="headcount"
                label="编制人数"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
              <el-table-column
                prop="totalUseArea"
                label="使用面积(m²)"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
              ></el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <div class="accessory-box" v-if="showAccessory && fileList.length > 0">
      <el-tooltip
        class="item"
        effect="dark"
        content="下载附件"
        placement="right"
      >
        <div class="accessory-icon" @click="accessoryHandle">
          <span class="iconfont">&#xe648;</span>
        </div>
      </el-tooltip>
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
  </div>
</template>

<script>
import { BASE_URL, url_name_1, LNG, LAT } from "@/axios/global";
import { getRouterList, uploadUrl } from "@/axios/commonApi";
import {
  getYardPositon,
  getBuildPositon,
  getBuildDetail,
  getYardDetail,
  getUnitPersonInfo,
  getDeviceLastInfoList,
  getRepairBuildRecord,
  getSelectEstateData,
  getUnitTreeDetail,
  getBuildRightDetail,
  seachCAD,
  saveCAD,
  deleteCAD,
  downloadCAD,
} from "./api";
import BaiduMap from "vue-baidu-map/components/map/Map.vue";
import bmMarker from "vue-baidu-map/components/overlays/Marker.vue";
import bmLabel from "vue-baidu-map/components/overlays/Label.vue";
import bmInfoWindow from "vue-baidu-map/components/overlays/InfoWindow.vue";
import cancelBtn from "@/components/button/cancelBtn";
import downloadBtn from "@/components/button/downloadBtn";
import { log, debug } from "util";
import { eventBus } from "@/eventBus/eventBus";
export default {
  components: {
    BaiduMap,
    bmMarker,
    bmLabel,
    bmInfoWindow,
    cancelBtn,
    downloadBtn,
  },
  data() {
    return {
      token: "",
      center: { lng: LNG, lat: LAT },
      zoom: 15,
      yardList: [],
      buildList: [],
      markerPoint: {},
      yardImageUrl: `${BASE_URL}${url_name_1}/images/yard.png`,
      buildImageUrl: `${BASE_URL}${url_name_1}/images/build.png`,
      infoWindow: {
        show: false,
        yardName: "",
        yardPosition: "",
        yardAdministrativeRegion: "",
        buildName: "",
        buildArea: "",
        floorImg: [],
        lng: "",
        lat: "",
      },
      isYardWindowShow: true,
      activeName: "propert",
      tableHeight: 0,
      tableHeaderCellStyle: {
        padding: "6px 0",
        color: "#333333",
      },
      tableCellStyle: {
        padding: "6px 0",
        "background-color": "#f3f3f3",
      },
      propertyTableData: [],
      situationTableData: [],
      deviceTableData: [],
      repairTableData: [],
      unitDataList: [],
      houseCertificateTableData: [],
      immovablesTableData: [],
      expandBuildDetail: false,
      showBuildDetail: false,
      yardId: "",
      buildId: "",
      situationColumn: {
        enterPeopleNum: true,
        useUnitName: true,
        useUnitNum: true,
        officeNum: true,
        rentArea: true,
        selfUseArea: true,
        spareArea: true,
      },
      isYardDetail: true,
      estateDataList: [],
      serviceContentOpts: [
        {
          id: 1,
          text: "安保",
        },
        {
          id: 2,
          text: "保洁",
        },
        {
          id: 3,
          text: "绿化",
        },
        {
          id: 4,
          text: "零星维修",
        },
        {
          id: 5,
          text: "收发",
        },
      ],
      isUnitDetail: false,
      unitId: null,
      accessoryDialogVisible: false,
      fileList: [],
      CADDownload: [],
      showAccessory: false,
      showRentArea: false,
      showHouseCertificate: false,
      showImmovables: false,
      showAssetDevice: false,
      showRepair: false,
    };
  },
  mounted() {
    console.log(LNG, LAT);
    this.token = sessionStorage.getItem("TOKEN");
    this.getYardListPositon(null);
    eventBus.$off("updateBmMarker");
    eventBus.$off("allBmMarker");
    eventBus.$off("onlyShowBuildMarker");
    eventBus.$off("hideBuildDetail");
    eventBus.$off("getUnitHouseInfo");
    eventBus.$on("updateBmMarker", (id) => {
      this.showAccessory = false;
      this.infoWindow.show = false;
      this.isYardDetail = true;
      this.isUnitDetail = false;
      this.yardList = [];
      this.buildList = [];
      getBuildPositon(null, id)
        .then((res) => {
          if (res && res.length > 0) {
            for (let i = 0; i < res.length; i++) {
              res[i].vkey = parseInt(Math.random() * 10 + 1);
              if (res[i].type === 0) {
                this.yardList.push(res[i]);
              } else {
                this.buildList.push(res[i]);
              }
            }
            this.center.lng = this.yardList[0].buildLongitude;
            this.center.lat = this.yardList[0].buildLatitude;
            const callback = (param) => {
              this.activeName = "estate";
              getSelectEstateData(param)
                .then((res) => {
                  if (
                    res &&
                    res.yardAndestateVoList &&
                    res.yardAndestateVoList.length > 0
                  ) {
                    this.estateDataList = res.yardAndestateVoList;
                    this.estateDataList.forEach((item, index) => {
                      item.serialNum = index + 1;
                      item.serviceContentText = "";
                      item.startTime = item.startTime
                        ? item.startTime.split(" ")[0]
                        : "";
                      item.endTime = item.endTime
                        ? item.endTime.split(" ")[0]
                        : "";
                      item.serviceContentList.forEach((serviceId, index) => {
                        for (
                          let i = 0;
                          i < this.serviceContentOpts.length;
                          i++
                        ) {
                          if (this.serviceContentOpts[i].id === serviceId) {
                            if (item.serviceContentList.length === 1) {
                              item.serviceContentText += `${this.serviceContentOpts[i].text}`;
                            } else {
                              if (
                                index ===
                                item.serviceContentList.length - 1
                              ) {
                                item.serviceContentText += `${this.serviceContentOpts[i].text}`;
                              } else {
                                item.serviceContentText += `${this.serviceContentOpts[i].text}，`;
                              }
                            }
                          }
                        }
                      });
                    });
                  } else {
                    this.estateDataList = [];
                  }
                })
                .catch((err) => {
                  this.estateDataList = [];
                  const msg = err.result ? err.result : "物业信息获取失败";
                  this.$message({
                    message: msg,
                    type: "error",
                  });
                });
            };
            this.isYardDetail = true;
            this.buildExpand(id, callback);
          }
        })
        .catch((err) => {
          const msg = err.result ? err.result : "楼座坐标获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    });
    eventBus.$on("allBmMarker", (unitIds) => {
      this.showAccessory = false;
      const ids = unitIds ? unitIds : null;
      this.getYardListPositon(ids);
    });
    eventBus.$on("onlyShowBuildMarker", (id) => {
      this.CADDownload = [];
      this.fileList = [];
      this.showAccessory = true;
      this.buildId = id;
      this.infoWindow.show = false;
      this.isYardDetail = false;
      this.isUnitDetail = false;
      const callback = (param) => {
        getBuildDetail(param)
          .then((res) => {
            this.yardId = res.yardId;
            this.buildId = res.buildId;
            this.yardList = [];
            this.buildList = [];
            this.buildList.push(res);
            this.center.lng = res.buildLongitude;
            this.center.lat = res.buildLatitude;
            const tempObj = {
              yardPosition: "",
              warrantPerson: "",
              buildArea: "",
              buildCertificate: "",
              buildDate: "",
              builder: "",
              coveredArea: "",
              buildNature: "",
              buildConfirmation: "",
              obtainDate: "",
            };
            for (let key in res) {
              if (
                key === "buildConfirmation" ||
                key === "obtainDate" ||
                key === "buildDate"
              ) {
                if (res[key]) {
                  tempObj[key] = this.formatDate(res[key]);
                } else {
                  tempObj[key] = "暂无";
                }
              } else {
                if (tempObj[key] !== undefined) {
                  if (res[key]) {
                    tempObj[key] = res[key];
                  } else {
                    tempObj[key] = "暂无";
                  }
                }
              }
            }
            this.propertyTableData = [];
            this.propertyTableData.push(tempObj);
            this.activeName = "propert";
          })
          .catch((err) => {
            const msg = err.result ? err.result : "楼座信息获取失败";
            this.$message({
              message: msg,
              type: "error",
            });
          });
        seachCAD(param)
          .then((res) => {
            if (res && res.length > 0) {
              res.forEach((item, index) => {
                const obj = {
                  name: item.floordrawingFileName,
                  id: item.floordrawingId,
                };
                this.fileList.push(obj);
              });
            }
          })
          .catch((err) => {
            const msg = err.result ? err.result : "CAD获取失败";
            this.$message({
              message: msg,
              type: "error",
            });
          });
      };
      this.buildExpand(id, callback);
    });
    eventBus.$on("hideBuildDetail", () => {
      this.showAccessory = false;
      if (this.showBuildDetail) {
        this.showBuildDetail = false;
        $("#baidu_map").css({ height: "100%" });
        $(".build-detail-dialog").css({ height: "4px" });
      }
    });
    eventBus.$on("getUnitHouseInfo", (unitIds, id) => {
      this.showAccessory = false;
      this.unitId = id;
      this.infoWindow.show = false;
      this.isYardDetail = false;
      this.isUnitDetail = true;
      this.activeName = "unit";
      const ids = unitIds ? unitIds : null;
      getUnitTreeDetail(id)
        .then((res) => {
          if (res) {
            let total_useArea = 0;
            let total_houseNum = 0;
            res.houseTypeSumDtoList.forEach((item, index) => {
              item.headcount = "-";
              total_useArea += item.totalUseArea - 0;
              total_houseNum += item.houseNum;
            });
            let tempObj = {};
            tempObj.houseTypeName = "合计";
            tempObj.headcount = res.unitComplement;
            tempObj.houseNum = total_houseNum;
            tempObj.totalUseArea = total_useArea.toFixed(2);
            res.houseTypeSumDtoList.push(tempObj);
            this.unitDataList = res.houseTypeSumDtoList;
          } else {
            this.unitDataList = [];
          }
          const callback = (param) => {
            this.getYardListPositon(param);
          };
          this.buildExpand(ids, callback);
        })
        .catch((err) => {
          this.unitDataList = [];
          const msg = err.result ? err.result : "单位信息获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    });
    getRouterList()
      .then((res) => {
        for (let i = 0; i < res.length; i++) {
          const key = res[i].key;
          const children = res[i].children;
          if (key === "disposeManagement") {
            this.showRentArea = true;
          }
          if (key === "houseManagement") {
            children.forEach((item) => {
              // 房产证
              if (item.key === "houseCertificate") {
                this.showHouseCertificate = true;
              }
              // 不动产
              if (item.key === "immovables") {
                this.showImmovables = true;
              }
              // 重大设备
              if (item.key === "assetDevice") {
                this.showAssetDevice = true;
              }
            });
          }
          if (key === "assetManagement") {
            children.forEach((item) => {
              if (item.key === "assetDevice") {
                this.showAssetDevice = true;
              }
            });
          }
          if (key === "repairManagement") {
            this.showRepair = true;
          }
        }
      })
      .catch((err) => {
        const msg = err.result ? err.result : "路由信息获取失败！";
        this.$message({
          message: msg,
          type: "error",
        });
      });
  },
  methods: {
    handler({ BMap, map }) {
      this.center.lng = LNG;
      this.center.lat = LAT;
      this.zoom = this.zoom;
    },
    getYardListPositon(unitIds) {
      getYardPositon(unitIds)
        .then((res) => {
          res.forEach((element) => {
            element.buildLongitude = element.yardLongitude;
            element.buildLatitude = element.yardLatitude;
            element.buildName = element.yardName;
            element.buildId = element.yardId;
            this.buildList = [];
            this.center.lng = LNG;
            this.center.lat = LAT;
          });
          this.yardList = res;
        })
        .catch((err) => {
          const msg = err.result ? err.result : "院落坐标获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    infoWindowClose(e) {
      this.infoWindow.show = false;
    },
    infoWindowOpen(e) {
      this.infoWindow.show = true;
    },
    buildMakerClick(id) {
      getBuildDetail(id)
        .then((res) => {
          this.isYardWindowShow = false;
          this.infoWindow.buildName = res.buildName ? res.buildName : "";
          this.infoWindow.buildArea = res.buildArea ? `${res.buildArea}㎡` : "";
          this.infoWindow.yardAdministrativeRegion =
            res.yardAdministrativeRegion ? res.yardAdministrativeRegion : "";
          this.infoWindow.yardPosition = res.yardPosition
            ? res.yardPosition
            : "";
          this.infoWindow.floorImg = res.floorImg ? res.floorImg : [];
          this.infoWindow.lng = res.buildLongitude ? res.buildLongitude : "";
          this.infoWindow.lat = res.buildLatitude ? res.buildLatitude : "";
          this.infoWindow.show = true;
        })
        .catch((err) => {
          const msg = err.result ? err.result : "楼座详情获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    yardMakerClick(buildId) {
      getYardDetail(buildId)
        .then((res) => {
          this.isYardWindowShow = true;
          this.infoWindow.yardName = res.yardName ? res.yardName : "";
          this.infoWindow.buildName = res.yardName ? res.yardName : "";
          this.infoWindow.yardPosition = res.yardPosition
            ? res.yardPosition
            : "";
          this.infoWindow.yardAdministrativeRegion =
            res.yardAdministrativeRegion ? res.yardAdministrativeRegion : "";
          this.infoWindow.lng = res.yardLongitude ? res.yardLongitude : "";
          this.infoWindow.lat = res.yardLatitude ? res.yardLatitude : "";
          this.infoWindow.show = true;
        })
        .catch((err) => {
          const msg = err.result ? err.result : "院落详情获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    getImgUrl(url) {
      return `${BASE_URL}${url_name_1}${url}?access_token=${this.token}`;
    },
    tabHandleClick(tab, event) {
      this.propertyTableData = [];
      this.situationTableData = [];
      this.deviceTableData = [];
      this.repairTableData = [];
      this.unitDataList = [];
      this.houseCertificateTableData = [];
      this.immovablesTableData = [];
      if (tab.name === "propert" && this.buildId) {
        getBuildDetail(this.buildId)
          .then((res) => {
            if (res) {
              const tempObj = {
                yardPosition: "",
                warrantPerson: "",
                buildArea: "",
                buildCertificate: "",
                buildDate: "",
                builder: "",
                coveredArea: "",
                buildNature: "",
                buildConfirmation: "",
                obtainDate: "",
              };
              for (let key in res) {
                if (
                  key === "buildConfirmation" ||
                  key === "obtainDate" ||
                  key === "buildDate"
                ) {
                  if (res[key]) {
                    tempObj[key] = this.formatDate(res[key]);
                  } else {
                    tempObj[key] = "暂无";
                  }
                } else {
                  if (tempObj[key] !== undefined) {
                    if (res[key]) {
                      tempObj[key] = res[key];
                    } else {
                      tempObj[key] = "暂无";
                    }
                  }
                }
              }
              this.propertyTableData = [];
              this.propertyTableData.push(tempObj);
            } else {
              this.propertyTableData = [];
            }
          })
          .catch((err) => {
            const msg = err.result ? err.result : "楼座信息获取失败";
            this.$message({
              message: msg,
              type: "error",
            });
          });
      } else if (tab.name === "situation" && this.buildId) {
        getUnitPersonInfo(this.buildId)
          .then((res) => {
            if (res) {
              const tempObj = {
                enterPeopleNum: "",
                useUnitName: "",
                useUnitNum: "",
                officeNum: "",
                rentArea: "",
                selfUseArea: "",
                spareArea: "",
              };
              for (let key in tempObj) {
                if (res[key] !== undefined) {
                  tempObj[key] = res[key];
                  this.situationColumn[key] = true;
                } else {
                  this.situationColumn[key] = false;
                }
              }
              this.situationTableData = [];
              this.situationTableData.push(tempObj);
            } else {
              this.situationTableData = [];
            }
          })
          .catch((err) => {
            const msg = err.result ? err.result : "资产情况信息获取失败";
            this.$message({
              message: msg,
              type: "error",
            });
          });
      } else if (tab.name === "device" && this.buildId) {
        getDeviceLastInfoList(this.buildId)
          .then((res) => {
            if (res) {
              this.deviceTableData = res;
            } else {
              this.deviceTableData = [];
            }
          })
          .catch((err) => {
            const msg = err.result ? err.result : "重大设备信息获取失败";
            this.$message({
              message: msg,
              type: "error",
            });
          });
      } else if (tab.name === "maintain" && this.buildId) {
        getRepairBuildRecord(this.buildId)
          .then((res) => {
            if (res) {
              this.repairTableData = res.repairModuleDetailDtoList;
            } else {
              this.repairTableData = [];
            }
          })
          .catch((err) => {
            const msg = err.result ? err.result : "维修模块记录获取失败";
            this.$message({
              message: msg,
              type: "error",
            });
          });
      } else if (tab.name === "unit") {
        getUnitTreeDetail(this.unitId)
          .then((res) => {
            if (res) {
              let total_headcount = 0;
              let total_useArea = 0;
              let total_houseNum = 0;
              res.forEach((item, index) => {
                if (item.houseTypeName !== "办公室用房") {
                  item.headcount = "-";
                }
                if (item.headcount !== "-") {
                  total_headcount += item.headcount;
                }
                total_useArea += item.totalUseArea - 0;
                total_houseNum += item.houseNum;
              });
              let tempObj = {};
              tempObj.houseTypeName = "合计";
              tempObj.headcount = total_headcount;
              tempObj.houseNum = total_houseNum;
              tempObj.totalUseArea = total_useArea.toFixed(2);
              res.push(tempObj);
              this.unitDataList = res;
            } else {
              this.unitDataList = [];
            }
          })
          .catch((err) => {
            this.unitDataList = [];
            const msg = err.result ? err.result : "单位信息获取失败";
            this.$message({
              message: msg,
              type: "error",
            });
          });
      } else if (tab.name === "houseCertificate" && this.buildId) {
        getBuildRightDetail(this.buildId, 1)
          .then((res) => {
            if (res) {
              res.forEach((item) => {
                let temp = [];
                item.handleModuleList.forEach((obj) => {
                  temp.push(obj.buildName);
                });
                item.buildName = temp.join(",");
              });
              this.houseCertificateTableData = res;
            } else {
              this.houseCertificateTableData = [];
            }
          })
          .catch((err) => {
            this.houseCertificateTableData = [];
            const msg = err.result ? err.result : "房产证信息获取失败";
            this.$message({
              message: msg,
              type: "error",
            });
          });
      } else if (tab.name === "immovables" && this.buildId) {
        getBuildRightDetail(this.buildId, 3)
          .then((res) => {
            if (res) {
              res.forEach((item) => {
                item.certificationDate = this.formatDate(
                  item.certificationDate
                );
                let temp = [];
                item.handleModuleList.forEach((obj) => {
                  temp.push(obj.buildName);
                });
                item.buildName = temp.join(",");
              });
              this.immovablesTableData = res;
            } else {
              this.immovablesTableData = [];
            }
          })
          .catch((err) => {
            this.immovablesTableData = [];
            const msg = err.result ? err.result : "不动产信息获取失败";
            this.$message({
              message: msg,
              type: "error",
            });
          });
      }
    },
    buildCollapse() {
      $(".build-detail-dialog").animate({ height: "4px" }, () => {
        this.expandBuildDetail = false;
      });
      $("#baidu_map").animate({ height: "100%" });
    },
    buildExpand(id, callback) {
      if (!this.showBuildDetail) {
        this.showBuildDetail = true;
      }
      $("#baidu_map").animate({ height: "60%" });
      $(".build-detail-dialog").animate({ height: "40%" }, () => {
        this.expandBuildDetail = true;
        const h = $(".el-tabs__content").height();
        this.tableHeight = h;
        if (callback) {
          callback(id);
        }
      });
    },
    jumpRepairPage() {
      this.$router.push("/home/repairManagement/qualityAssuranceDate");
    },
    // 格式化时间
    formatDate(time) {
      var date = new Date(time);
      var fmt = "yyyy-MM-dd";
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
    // 打开附件模态框
    accessoryHandle() {
      this.accessoryDialogVisible = true;
    },
    // 关闭附件模态框
    accessoryDialogClosed() {
      this.accessoryDialogVisible = false;
      this.CADDownload = [];
    },
    // 下载cad文件
    downloadCAD() {
      let tempObj = {};
      tempObj.buildId = this.buildId;
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
  },
};
</script>

<style>
#houseView {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
}
#houseView #baidu_map {
  width: 100%;
  height: 100%;
}
#houseView .yard-infoWindow .item-title,
#houseView .build-infoWindow-content-right .item-title {
  display: inline-block;
  width: 10px;
  height: 8px;
  background: url("../../assets/imgs/green-circle-icon.png") no-repeat 0 0;
  position: relative;
  top: 5px;
}
#houseView .yard-infoWindow .item-content,
#houseView .build-infoWindow-content-right .item-content {
  width: calc(100% - 12px);
}
#houseView .build-infoWindow-content-left,
#houseView .build-infoWindow-content-right {
  width: 50%;
  float: left;
  padding: 10px;
  box-sizing: border-box;
}
#houseView .BMap_bubble_title {
  text-align: center;
  font-weight: 500;
  font-size: 16px;
}
#houseView .build-infoWindow-content-left > img {
  width: 100%;
  height: 100%;
}
#houseView .yard-infoWindow li > span,
#houseView .build-infoWindow-content-right li > span {
  float: left;
}
#houseView .build-infoWindow-content-left .el-carousel {
  width: 100%;
  height: 150px;
}
#houseView .build-detail-dialog {
  width: 100%;
  height: 4px;
  position: absolute;
  left: 0;
  bottom: 0;
  background: #efefef;
  z-index: 1000;
  box-sizing: border-box;
  border-top: 4px solid rgb(0, 79, 122);
}
#houseView .build-detail-content {
  width: calc(100% - 30px);
  height: calc(100% - 40px);
  background: #fff;
  margin: 25px 15px 15px 15px;
  position: relative;
}
#houseView .down-arrow-btn {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 50%;
  margin-left: -48px;
}
#houseView .top-arrow-btn {
  position: absolute;
  cursor: pointer;
  top: -22px;
  left: 50%;
  margin-left: -48px;
}
#houseView .el-tabs {
  height: 100%;
}
#houseView .el-tabs__nav {
  margin-left: 20px;
}
#houseView .el-tabs__content {
  height: calc(100% - 55px);
}
#houseView .build-detail-content .more-btn {
  position: absolute;
  right: 25px;
  top: 0;
  z-index: 10;
}
#houseView .accessory-box {
  position: absolute;
  top: 20px;
  left: 20px;
  z-index: 10;
}
#houseView .accessory-box .accessory-icon {
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
#houseView .accessory-box .accessory-icon > span {
  font-size: 26px;
}
#houseView .accessory-item {
  line-height: 30px;
}
#houseView .accessoryDialog .el-dialog__footer {
  text-align: center;
}
@media only screen and (max-width: 1366px) {
  #houseView .accessory-box .accessory-icon {
    height: 30px;
    width: 30px;
    line-height: 30px;
  }
  #houseView .accessory-box .accessory-icon > span {
    font-size: 24px;
  }
}
</style>

