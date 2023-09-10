<template>
  <div id="roomDistributeView">
    <!-- 平面图 -->
    <div class="plane">
      <div @contextmenu.prevent="showContextMenu($event);" class="canvasDiv">
        <canvas id="c">请使用支持HTML5的浏览器</canvas>
      </div>
    </div>
    <!-- 切换按钮 -->
    <div class="btn-box">
      <button
        class="graph-btn batch-distribute-btn"
        @click="batchDistribution"
        v-if="canDistributeOrSave"
      >批量分配</button>
      <button
        class="graph-btn batch-revert-btn"
        @click="batchRevert"
        v-if="canDistributeOrSave"
      >批量撤销</button>
      <button class="graph-btn" @click="allSave">保存</button>
    </div>
    <!-- 房间信息模态框 -->
    <el-dialog
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="1100px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>房间信息</span>
      </div>
      <div class="dialog-content">
        <div class="base-info-box">
          <div class="base-info-title">
            <span class="base-info-icon"></span>
            <span>基本信息</span>
          </div>
          <div class="base-info-content" v-if="!isBatchDistribute">
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="grid-content">
                  <span class="grid-content-title">使用单位：</span>
                  <span>{{Form.originalUnitNameList ? Form.originalUnitNameList : '暂无'}}</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content">
                  <span class="grid-content-title">房间号：</span>
                  <span>{{Form.houseNumber}}</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content">
                  <span class="grid-content-title">使用面积：</span>
                  <span>{{Form.houseUseArea}}m²</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content">
                  <span class="grid-content-title">房间类型：</span>
                  <span>{{Form.housetypeName}}</span>
                </div>
              </el-col>
            </el-row>
          </div>
          <div class="base-info-content" v-if="isBatchDistribute">
            <el-row>
              <el-col :span="24">
                <div class="grid-content batch">
                  <span>选择分配的房间：</span>
                  <div class="room-table">
                    <table-component
                      :tableData="selectedDistributeList"
                      :tableHeight="200"
                      :tableCellPadding="'0'"
                      @select="handleSelectionChange"
                    >
                      <el-table-column prop="houseNumber" label="房间号" :show-overflow-tooltip="true"></el-table-column>
                      <el-table-column
                        prop="originalUnitNameList"
                        label="使用单位"
                        :show-overflow-tooltip="true"
                      ></el-table-column>
                      <el-table-column
                        prop="houseUseArea"
                        label="使用面积(m²)"
                        :show-overflow-tooltip="true"
                      ></el-table-column>
                      <el-table-column
                        prop="housetypeName"
                        label="房间类型"
                        :show-overflow-tooltip="true"
                      ></el-table-column>
                      <el-table-column prop="isIdleText" label="是否调整" :show-overflow-tooltip="true"></el-table-column>
                      <el-table-column
                        prop="newUnitNameList"
                        label="已分配单位"
                        :show-overflow-tooltip="true"
                      ></el-table-column>
                    </table-component>
                  </div>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <div class="grid-content batch">
                  <span>可分配的房间总面积：</span>
                  <span>{{this.totalRoomArea}}m²</span>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="grid-content batch">
                  <span>可分配的房间总数量：</span>
                  <span>{{this.totalRoomNum}}</span>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="distribute-unit-box">
          <div class="distribute-unit-title">
            <span class="edit-info-icon"></span>
            <span>使用情况</span>
            <span
              class="distribute-btn"
              v-if="!currentUseState && !distributeFlag"
              @click="distributeStart"
            >
              <span class="iconfont">&#xe6b9;</span>调整
            </span>
            <span
              class="distribute-btn"
              v-if="!currentUseState && distributeFlag"
              @click="distributeEnd"
            >
              <span class="iconfont">&#xe61e;</span>取消调整
            </span>
          </div>
          <div class="distribute-unit-content" v-if="Form.isIdle !== null">
            <el-form :model="Form" :rules="rules" ref="Form" label-width="78px">
              <el-form-item label="使用情况" prop="isIdle">
                <el-radio-group v-model="Form.isIdle" @change="idleRadioChange">
                  <el-radio :label="1">占用</el-radio>
                  <el-radio :label="0">闲置</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item
                label="占用单位"
                prop="selectedUnitIdList"
                v-if="Form.isIdle===1"
                class="unit-select-form-item"
              >
                <el-select v-model="Form.selectedUnitIdList" multiple placeholder="请选择单位">
                  <el-option
                    v-for="item in unitOptions"
                    :key="item.unitId"
                    :label="item.unitName"
                    :value="item.unitId"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>

    <!-- 房屋类型列表 -->
    <div class="house-type-box">
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

    <!-- 单位面积变化 -->
    <div class="change-detail-dialog" v-show="showChangeDetail">
      <div class="top-arrow-btn" v-show="!expandChangeDetail" @click="changeBoxExpand">
        <img src="../../assets/imgs/top-arrow-btn.png" alt="展开按钮" />
      </div>
      <div class="down-arrow-btn" v-show="expandChangeDetail" @click="changeBoxCollapse">
        <img src="../../assets/imgs/down-arrow-btn.png" alt="收缩按钮" />
      </div>
      <div class="change-detail-title">分配统计</div>
      <div class="change-detail-content">
        <el-table
          :data="distributeChangeList"
          :header-cell-style="tableHeaderCellStyle"
          :cell-style="tableCellStyle"
          :height="tableHeight"
          style="width: 100%"
        >
          <el-table-column
            prop="unitNames"
            label="单位名称"
            header-align="center"
            align="center"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            prop="houseNumber"
            label="房间数量"
            header-align="center"
            align="center"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            prop="useArea"
            label="面积(m²)"
            header-align="center"
            align="center"
            :show-overflow-tooltip="true"
          ></el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import { PRO_TYPE } from "@/axios/global"
import { getUserUnitList, getRouterList } from "@/axios/commonApi";
import { getHouseGraphJson, getAllocationUnit, saveConfigurePlanData, getallocationFloorInfo, getChangeData } from "./api";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import saveBtn from "@/components/button/saveBtn";
const $ = require("jquery");
const { fabric } = require('fabric');
const uuidv1 = require('uuid/v1');
export default {
  components: {
    cancelBtn,
    confirmBtn,
    saveBtn,
    tableComponent
  },
  data() {
    return {
      PRO_TYPE: PRO_TYPE,
      canvas: {}, // canvas对象
      canvasHeight: 0,
      canvasWidth: 0,
      houseTypeColor: [
        {
          id: 1,
          color: '#21b124'
        },
        {
          id: 2,
          color: '#f4e83b'
        },
        {
          id: 3,
          color: '#008aff'
        },
        {
          id: 4,
          color: '#12fff1'
        },
        {
          id: 5,
          color: '#ff01fd'
        },
        {
          id: 6,
          color: '#fe8200'
        }
      ],
      dialogTitle: "",
      dialogVisible: false,
      contextMenuItems: {
        distribute: {
          name: "模拟分配",
          icon: "edit"
        },
        revert: {
          name: "撤销"
        }
      },
      contextMenuItems_2: {
        distribute: {
          name: "模拟分配",
          icon: "edit"
        }
      },
      Form: {
        configurePlanDataId: null,
        houseId: null,
        houseNumber: null,
        originalUnitNameList: '',
        newUnitNameList: '',
        houseUseArea: '',
        housetypeName: '',
        isIdle: null,
        originalUnitList: [],
        oldConfUnitList: [],
        newConfUnitList: [],
        selectedUnitIdList: [],
        isUse: null,
        applyStatus: null,
        handleWay: null,
        isIdleText: ''
      },
      originalHouseList: [], // 原始的房屋信息数组
      editHouseInfo: [], // 修改房屋信息数组
      saveFormList: [], // 保存的房屋信息数组
      distributableList: [], // 可批量分配的房间
      selectedDistributeList: [], // 选择的批量分配的房间
      distributeChangeList: [], // 记录单位面积改变
      oldDistributeChangeList: [],
      distributeChangeObj: {}, // 记录单位面积改变
      unitOptions: [],
      revertSaveList: [], // 撤销
      rules: {
        selectedUnitIdList: [
          { required: true, message: "分配单位不能为空", trigger: "change" }
        ]
      },
      totalRoomArea: null,
      totalRoomNum: null,
      configureId: null,
      planId: null,
      isHandle: null,
      currentUseState: false,
      oldIdle: null,
      distributeFlag: false,
      isBatchDistribute: false,
      expandChangeDetail: false,
      showChangeDetail: false,
      tableHeight: 0,
      tableHeaderCellStyle: {
        padding: "6px 0",
        color: "#333333"
      },
      tableCellStyle: {
        padding: "6px 0",
        'background-color': '#f3f3f3'
      },
      canDistributeOrSave: true,
      notDistributeReason: '',
      allNavList: null
    };
  },
  methods: {
    changeBoxCollapse() {
      $('.change-detail-dialog').animate({ height: '4px' }, () => {
        this.expandChangeDetail = false;
      })
    },
    changeBoxExpand(id) {
      $('.change-detail-dialog').animate({ height: '40%' }, () => {
        this.expandChangeDetail = true;
        const h = $('.change-detail-content').height();
        this.tableHeight = h;
      })
    },
    idleRadioChange(type) {
      if (this.Form.isUse === 0 && type === 0) {
        this.Form.isIdle = 1;
        this.$message({
          message: '该房间没有原使用单位，使用情况不能更改为闲置！',
          type: "warning"
        });
        return;
      }
    },
    // 开始分配
    distributeStart() {
      this.distributeFlag = true;
      this.Form.isIdle = 1;
    },
    // 取消分配
    distributeEnd() {
      this.distributeFlag = false;
      this.Form.isIdle = null;
      this.selectedUnitIdList = [];
    },
    // 初始化
    init() {
      // 初始化canvas画布
      if (this.canvas && this.canvas.clear) {
        this.canvas.clear();
      }
      this.canvas = new fabric.Canvas("c");
      fabric.ActiveSelection.prototype.hasControls = fabric.ActiveSelection.prototype.hasBorders = false;
      fabric.Object.prototype.originX = 'left';
      fabric.Object.prototype.originY = 'top';
      fabric.Object.prototype.transparentCorners = false;
      fabric.Object.prototype.borderColor = 'red';
      fabric.Object.prototype.cornerColor = 'green';
      fabric.Object.prototype.cornerSize = 6;
      fabric.Object.prototype.borderScaleFactor = 2;
      fabric.Object.prototype.transparentCorners = fabric.Object.prototype.hasControls = fabric.Object.prototype.hasRotatingPoint = false;
      fabric.Object.prototype.lockMovementX = fabric.Object.prototype.lockMovementY = true;
      window.canvas = this.canvas;
      // 设置画布比例
      this.setZoom();
      window.onresize = () => {
        this.setZoom();
      };
      // 初始化右键菜单
      $.contextMenu({
        selector: ".upper-canvas",
        trigger: "none",
        build: function (e, $trigger) {
          return $trigger.data("runCallbackThingie")();
        }
      });
    },
    // 设置画布比例
    setZoom() {
      const canvasDiv = $(this.canvas.wrapperEl).parent();
      this.canvasWidth = canvasDiv.width();
      this.canvasHeight = canvasDiv.height();
      window.zoom = 1;
      this.canvas.setZoom(1);
      this.canvas.setWidth(this.canvasWidth);
      this.canvas.setHeight(this.canvasHeight);
      this.canvas.renderAll();
    },
    // 显示右键菜单
    showContextMenu($event) {
      if ($event.which === 3) {
        // 右键
        const $this = $($event.toElement);
        const canvasObjects = this.canvas.getObjects();
        if (canvasObjects && canvasObjects.length > 0) {
          for (let i = 0; i < canvasObjects.length; i++) {
            const canvasobj = canvasObjects[i];
            if (this.canvas.containsPoint($event, canvasobj)) {
              const type = canvasobj.type;
              if (type !== "group") {
                return false;
              }
              this.canvas.discardActiveObject();
              this.canvas.setActiveObject(canvasobj);
              this.canvas.renderAll();
              let flag = true;
              for (let i = 0; i < this.editHouseInfo.length; i++) {
                if (this.editHouseInfo[i].houseId === canvasobj.uuid) {
                  flag = false;
                  this.currentUseState = (this.editHouseInfo[i].isIdle === 0 || this.editHouseInfo[i].isIdle === 1) ? true : false;
                  $this.data("runCallbackThingie", this.createSomeMenu);
                  const position = {
                    x: $event.clientX,
                    y: $event.clientY
                  };
                  $this.contextMenu(position);
                }
              }
              if (flag) {
                for (let i = 0; i < this.originalHouseList.length; i++) {
                  if (this.originalHouseList[i].houseId === canvasobj.uuid) {
                    this.currentUseState = (this.originalHouseList[i].isIdle === 0 || this.originalHouseList[i].isIdle === 1) ? true : false;
                    $this.data("runCallbackThingie", this.createSomeMenu);
                    const position = {
                      x: $event.clientX,
                      y: $event.clientY
                    };
                    $this.contextMenu(position);
                  }
                }
              }
              return false;
            }
          }
        }
      }
    },
    // 创建菜单项
    createSomeMenu() {
      const contextMenu = this.currentUseState
        ? this.contextMenuItems
        : this.contextMenuItems_2;
      return {
        callback: this.contextMenuClick,
        items: contextMenu
      };
    },
    // 点击右键菜单
    contextMenuClick(options, key) {
      if (key === "distribute") {
        if (!this.canDistributeOrSave) {
          this.$message({
            message: `该楼座已${this.notDistributeReason}, 不能进行分配`,
            type: "warning"
          });
          return false;
        }
        const activeObject = this.canvas.getActiveObject();
        let editFlag = false;
        for (let i = 0; i < this.editHouseInfo.length; i++) {
          if (this.editHouseInfo[i].houseId === activeObject.uuid) {
            this.Form = JSON.parse(JSON.stringify(this.editHouseInfo[i]));
            editFlag = true;
            break;
          }
        }
        let currentHouseInfo = null;
        for (let i = 0; i < this.originalHouseList.length; i++) {
          if (this.originalHouseList[i].houseId === activeObject.uuid) {
            const data = this.originalHouseList[i];
            this.oldIdle = (data.isIdle === 0 || data.isIdle === 1) ? data.isIdle : null;
            currentHouseInfo = JSON.parse(JSON.stringify(data));
          }
        }
        if (!editFlag) {
          this.Form = JSON.parse(JSON.stringify(currentHouseInfo));
        }
        this.dialogVisible = true;
      } else if (key === "revert") {
        this.$confirm("此操作将永久删除房间配置方案数据, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          const activeObject = this.canvas.getActiveObject();
          let currentHouseInfo = null;
          for (let i = 0; i < this.originalHouseList.length; i++) {
            if (this.originalHouseList[i].houseId === activeObject.uuid) {
              currentHouseInfo = JSON.parse(JSON.stringify(this.originalHouseList[i]));
              break;
            }
          }
          for (let i = 0; i < this.saveFormList.length; i++) {
            if (this.saveFormList[i].houseId === activeObject.uuid) {
              this.saveFormList.splice(i, 1);
              break;
            }
          }
          for (let i = 0; i < this.distributableList.length; i++) {
            if (this.distributableList[i].houseId === activeObject.uuid) {
              this.distributableList[i].isIdle = null;
              this.distributableList[i].selectedUnitIdList = [];
              break;
            }
          }
          let flag = true;
          for (let i = 0; i < this.editHouseInfo.length; i++) {
            if (this.editHouseInfo[i].houseId === activeObject.uuid) {
              this.editHouseInfo[i].isIdle = null;
              this.editHouseInfo[i].selectedUnitIdList = [];
              flag = false;
              break;
            }
          }
          if (flag) {
            const obj = JSON.parse(JSON.stringify(currentHouseInfo))
            obj.selectedUnitIdList = [];
            obj.isIdle = null;
            this.editHouseInfo.push(obj);
          }
          if (currentHouseInfo.configurePlanDataId) {
            this.revertSaveList.push(currentHouseInfo.configurePlanDataId);
          }
          // 记录单位面积变化
          if (this.distributeChangeObj[currentHouseInfo.houseId] === undefined) {
            this.distributeChangeObj[currentHouseInfo.houseId] = {};
            this.distributeChangeObj[currentHouseInfo.houseId].remove = [];
            this.distributeChangeObj[currentHouseInfo.houseId].add = [];
          }
          if (currentHouseInfo.isIdle === 0) {
            // - 闲置
            const removeObj = {};
            removeObj.unitNames = '闲置';
            removeObj.useArea = `-${currentHouseInfo.houseUseArea}` - 0;
            removeObj.houseNumber = -1;
            this.distributeChangeObj[currentHouseInfo.houseId].remove = [];
            this.distributeChangeObj[currentHouseInfo.houseId].remove.push(removeObj);
            // + 原单位
            if (currentHouseInfo.rent) {
              const addObj = {};
              addObj.unitNames = '出租';
              addObj.useArea = `${currentHouseInfo.houseUseArea}` - 0;
              addObj.houseNumber = +1;
              this.distributeChangeObj[currentHouseInfo.houseId].add = [];
              this.distributeChangeObj[currentHouseInfo.houseId].add.push(addObj);
            } else {
              let addOriginalName = [];
              currentHouseInfo.originalUnitList.forEach(originalUnit => {
                addOriginalName.push(originalUnit.originalUnitName)
              });
              const addObj = {};
              addObj.unitNames = addOriginalName.join(',');
              addObj.useArea = `${currentHouseInfo.houseUseArea}` - 0;
              addObj.houseNumber = +1;
              this.distributeChangeObj[currentHouseInfo.houseId].add = [];
              this.distributeChangeObj[currentHouseInfo.houseId].add.push(addObj);
            }
          } else if (currentHouseInfo.isIdle === 1) {
            // - 分配的单位（以前）
            let removeOriginalName = [];
            if (currentHouseInfo.newConfUnitList.length > 0) {
              currentHouseInfo.newConfUnitList.forEach(unit => {
                removeOriginalName.push(unit.newUnitName)
              });
            }
            const removeObj = {};
            removeObj.unitNames = removeOriginalName.join(',');
            removeObj.useArea = `-${currentHouseInfo.houseUseArea}` - 0;
            removeObj.houseNumber = -1;
            this.distributeChangeObj[currentHouseInfo.houseId].remove = [];
            this.distributeChangeObj[currentHouseInfo.houseId].remove.push(removeObj);
            // + 原单位
            if (currentHouseInfo.rent) {
              const addObj = {};
              addObj.unitNames = '出租';
              addObj.useArea = `${currentHouseInfo.houseUseArea}` - 0;
              addObj.houseNumber = +1;
              this.distributeChangeObj[currentHouseInfo.houseId].add = [];
              this.distributeChangeObj[currentHouseInfo.houseId].add.push(addObj);
            } else {
              if (currentHouseInfo.originalUnitList.length > 0) {
                let addOriginalName = [];
                currentHouseInfo.originalUnitList.forEach(originalUnit => {
                  addOriginalName.push(originalUnit.originalUnitName)
                });
                const addObj = {};
                addObj.unitNames = addOriginalName.join(',');
                addObj.useArea = `${currentHouseInfo.houseUseArea}` - 0;
                addObj.houseNumber = +1;
                this.distributeChangeObj[currentHouseInfo.houseId].add = [];
                this.distributeChangeObj[currentHouseInfo.houseId].add.push(addObj);
              } else {
                const addObj = {};
                addObj.unitNames = '闲置';
                addObj.useArea = `${currentHouseInfo.houseUseArea}` - 0;
                addObj.houseNumber = +1;
                this.distributeChangeObj[currentHouseInfo.houseId].add = [];
                this.distributeChangeObj[currentHouseInfo.houseId].add.push(addObj);
              }
            }
          } else {
            this.distributeChangeObj[currentHouseInfo.houseId].add = [];
            this.distributeChangeObj[currentHouseInfo.houseId].remove = [];
          }
          this.getChangeData();

          // 去掉已修改图标
          const image = activeObject.getObjects('image');
          if (image.length > 0) {
            image.forEach(img => {
              activeObject.remove(img);
              this.canvas.requestRenderAll();
            })
          }
          this.$message({
            message: "撤销成功",
            type: "success"
          });
          // console.log('distributeChangeObj', this.distributeChangeObj);
        }).catch(() => {
          this.$message({
            type: "info",
            message: "已取消撤销操作"
          });
        });
      }
    },
    // 批量分配
    batchDistribution() {
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      if (!buildId || !floorName) {
        this.$message({
          message: "请先选择楼层",
          type: "warning"
        });
        return false;
      }
      if (!this.canDistributeOrSave) {
        this.$message({
          message: `该楼座已${this.notDistributeReason}, 不能进行分配`,
          type: "warning"
        });
        return false;
      }
      let objs = [];
      const aObject = canvas.getActiveObject();
      if (!aObject) {
        this.$message({
          message: "请先选择要分配的房间！",
          type: "warning"
        });
        return false;
      }
      if (aObject.type === 'group' && aObject.typeName === 'room') {
        objs.push(aObject);
      } else if (aObject.type === 'activeSelection') {
        for (let i = 0; i < aObject._objects.length; i++) {
          const obj = aObject._objects[i];
          if (obj.type === 'group' && obj.typeName === 'room') {
            objs.push(obj);
          }
        }
      }
      this.selectedDistributeList = [];
      for (let i = 0; i < objs.length; i++) {
        for (let j = 0; j < this.distributableList.length; j++) {
          if (this.distributableList[j].houseId === objs[i].uuid) {
            this.selectedDistributeList.push(this.distributableList[j]);
            break;
          }
        }
      }
      this.totalRoomArea = 0;
      this.selectedDistributeList.forEach(item => {
        if (item.houseUseArea) {
          this.totalRoomArea += item.houseUseArea;
        }
        item.isIdleText = (item.isIdle === 1 || item.isIdle === 0) ? '已调整' : '未调整';
        if (item.selectedUnitIdList.length > 0) {
          let temp = [];
          item.selectedUnitIdList = item.selectedUnitIdList.sort((a, b) => {
            return a - b;
          });
          item.selectedUnitIdList.forEach(item => {
            for (let i = 0; i < this.unitOptions.length; i++) {
              if (item === this.unitOptions[i].unitId) {
                temp.push(this.unitOptions[i].unitName);
                break;
              }
            }
          });
          item.newUnitNameList = temp.join(',');
        } else {
          item.newUnitNameList = '暂无';
        }
      })
      this.totalRoomArea = this.totalRoomArea.toFixed(2);
      this.totalRoomNum = this.selectedDistributeList.length;
      this.isBatchDistribute = true;
      this.dialogVisible = true;
    },
    // 批量撤销
    batchRevert() {
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      if (!buildId || !floorName) {
        this.$message({
          message: "请先选择楼层",
          type: "warning"
        });
        return false;
      }
      this.$confirm("此操作将永久批量删除整层楼的房间配置方案数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.distributableList.forEach(room => {
            room.isIdle = null;
            room.selectedUnitIdList = [];
            let currentHouseInfo = null;
            for (let i = 0; i < this.originalHouseList.length; i++) {
              if (this.originalHouseList[i].houseId === room.houseId) {
                currentHouseInfo = JSON.parse(JSON.stringify(this.originalHouseList[i]));
                break;
              }
            }
            for (let i = 0; i < this.saveFormList.length; i++) {
              if (this.saveFormList[i].houseId === room.houseId) {
                this.saveFormList.splice(i, 1);
                break;
              }
            }
            let flag = true;
            for (let i = 0; i < this.editHouseInfo.length; i++) {
              if (this.editHouseInfo[i].houseId === room.houseId) {
                this.editHouseInfo[i].isIdle = null;
                this.editHouseInfo[i].selectedUnitIdList = [];
                flag = false;
                break;
              }
            }
            if (flag) {
              const obj = JSON.parse(JSON.stringify(currentHouseInfo))
              obj.selectedUnitIdList = [];
              obj.isIdle = null;
              this.editHouseInfo.push(obj);
            }
            if (currentHouseInfo.configurePlanDataId) {
              if (this.revertSaveList.indexOf(currentHouseInfo.configurePlanDataId) === -1) {
                this.revertSaveList.push(currentHouseInfo.configurePlanDataId);
              }
            }
            // 记录单位面积变化
            if (this.distributeChangeObj[currentHouseInfo.houseId] === undefined) {
              this.distributeChangeObj[currentHouseInfo.houseId] = {};
              this.distributeChangeObj[currentHouseInfo.houseId].remove = [];
              this.distributeChangeObj[currentHouseInfo.houseId].add = [];
            }
            if (currentHouseInfo.isIdle === 0) {
              // - 闲置
              const removeObj = {};
              removeObj.unitNames = '闲置';
              removeObj.useArea = `-${currentHouseInfo.houseUseArea}` - 0;
              removeObj.houseNumber = -1;
              this.distributeChangeObj[currentHouseInfo.houseId].remove = [];
              this.distributeChangeObj[currentHouseInfo.houseId].remove.push(removeObj);
              // + 原单位
              if (currentHouseInfo.rent) {
                const addObj = {};
                addObj.unitNames = '出租';
                addObj.useArea = `${currentHouseInfo.houseUseArea}` - 0;
                addObj.houseNumber = +1;
                this.distributeChangeObj[currentHouseInfo.houseId].add = [];
                this.distributeChangeObj[currentHouseInfo.houseId].add.push(addObj);
              } else {
                let addOriginalName = [];
                currentHouseInfo.originalUnitList.forEach(originalUnit => {
                  addOriginalName.push(originalUnit.originalUnitName)
                });
                const addObj = {};
                addObj.unitNames = addOriginalName.join(',');
                addObj.useArea = `${currentHouseInfo.houseUseArea}` - 0;
                addObj.houseNumber = +1;
                this.distributeChangeObj[currentHouseInfo.houseId].add = [];
                this.distributeChangeObj[currentHouseInfo.houseId].add.push(addObj);
              }
            } else if (currentHouseInfo.isIdle === 1) {
              // - 分配的单位（以前）
              let removeOriginalName = [];
              if (currentHouseInfo.newConfUnitList.length > 0) {
                currentHouseInfo.newConfUnitList.forEach(unit => {
                  removeOriginalName.push(unit.newUnitName)
                });
              }
              const removeObj = {};
              removeObj.unitNames = removeOriginalName.join(',');
              removeObj.useArea = `-${currentHouseInfo.houseUseArea}` - 0;
              removeObj.houseNumber = -1;
              this.distributeChangeObj[currentHouseInfo.houseId].remove = [];
              this.distributeChangeObj[currentHouseInfo.houseId].remove.push(removeObj);
              // + 原单位
              if (currentHouseInfo.rent) {
                const addObj = {};
                addObj.unitNames = '出租';
                addObj.useArea = `${currentHouseInfo.houseUseArea}` - 0;
                addObj.houseNumber = +1;
                this.distributeChangeObj[currentHouseInfo.houseId].add = [];
                this.distributeChangeObj[currentHouseInfo.houseId].add.push(addObj);
              } else {
                if (currentHouseInfo.originalUnitList.length > 0) {
                  let addOriginalName = [];
                  currentHouseInfo.originalUnitList.forEach(originalUnit => {
                    addOriginalName.push(originalUnit.originalUnitName)
                  });
                  const addObj = {};
                  addObj.unitNames = addOriginalName.join(',');
                  addObj.useArea = `${currentHouseInfo.houseUseArea}` - 0;
                  addObj.houseNumber = +1;
                  this.distributeChangeObj[currentHouseInfo.houseId].add = [];
                  this.distributeChangeObj[currentHouseInfo.houseId].add.push(addObj);
                } else {
                  const addObj = {};
                  addObj.unitNames = '闲置';
                  addObj.useArea = `${currentHouseInfo.houseUseArea}` - 0;
                  addObj.houseNumber = +1;
                  this.distributeChangeObj[currentHouseInfo.houseId].add = [];
                  this.distributeChangeObj[currentHouseInfo.houseId].add.push(addObj);
                }
              }
            } else {
              this.distributeChangeObj[currentHouseInfo.houseId].add = [];
              this.distributeChangeObj[currentHouseInfo.houseId].remove = [];
            }
          })
          this.getChangeData();
          // 去掉已修改图标
          const activeObjects = this.canvas.getObjects('group');
          if (activeObjects.length > 0) {
            activeObjects.forEach(obj => {
              if (obj.typeName === 'room') {
                const image = obj.getObjects('image');
                if (image.length > 0) {
                  image.forEach(img => {
                    obj.remove(img);
                    this.canvas.requestRenderAll();
                  })
                }
              }
            })
          }
          this.$message({
            message: "撤销成功",
            type: "success"
          });
          // console.log('distributeChangeObj', this.distributeChangeObj);
          // console.log('revertSaveList', this.revertSaveList);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消撤销操作"
          });
        });
    },
    // 批量分配数据提交
    batchDistributeDataSubmit() {
      if (this.selectedDistributeList.length > 0) {
        this.selectedDistributeList.forEach(selected => {
          this.Form.houseId = selected.houseId;
          this.Form.configurePlanDataId = selected.configurePlanDataId ? selected.configurePlanDataId : null;
          this.Form.newConfUnitList = selected.newConfUnitList;
          this.Form.oldConfUnitList = selected.oldConfUnitList;
          this.Form.originalUnitList = selected.originalUnitList;

          // 整合单位面积变化数据
          let originalObj = null;
          let originalDistributedUnit = null;
          let originalIdle = null;
          const activeIdle = this.Form.isIdle;
          for (let i = 0; i < this.originalHouseList.length; i++) {
            if (this.Form.houseId === this.originalHouseList[i].houseId) {
              originalObj = this.originalHouseList[i];
              originalDistributedUnit = this.originalHouseList[i].newConfUnitList;
              originalIdle = this.originalHouseList[i].isIdle;
              break;
            }
          }
          if (this.distributeChangeObj[originalObj.houseId] === undefined) {
            this.distributeChangeObj[originalObj.houseId] = {};
            this.distributeChangeObj[originalObj.houseId].remove = [];
            this.distributeChangeObj[originalObj.houseId].add = [];
          }
          if (originalIdle !== 0 && originalIdle !== 1) {
            if (activeIdle === 0) {
              // - 原单位
              if (originalObj.rent) {
                const removeObj = {};
                removeObj.unitNames = '出租';
                removeObj.useArea = `-${originalObj.houseUseArea}` - 0;
                removeObj.houseNumber = -1;
                this.distributeChangeObj[originalObj.houseId].remove = [];
                this.distributeChangeObj[originalObj.houseId].remove.push(removeObj);
              } else {
                let removeOriginalName = [];
                originalObj.originalUnitList.forEach(originalUnit => {
                  removeOriginalName.push(originalUnit.originalUnitName)
                });
                const removeObj = {};
                removeObj.unitNames = removeOriginalName.join(',');
                removeObj.useArea = `-${originalObj.houseUseArea}` - 0;
                removeObj.houseNumber = -1;
                this.distributeChangeObj[originalObj.houseId].remove = [];
                this.distributeChangeObj[originalObj.houseId].remove.push(removeObj);
              }
              // + 闲置
              const addObj = {};
              addObj.unitNames = '闲置';
              addObj.useArea = `${originalObj.houseUseArea}` - 0;
              addObj.houseNumber = +1;
              this.distributeChangeObj[originalObj.houseId].add = [];
              this.distributeChangeObj[originalObj.houseId].add.push(addObj);
            } else if (activeIdle === 1) {
              // - 原单位
              if (originalObj.rent) {
                const removeObj = {};
                removeObj.unitNames = '出租';
                removeObj.useArea = `-${originalObj.houseUseArea}` - 0;
                removeObj.houseNumber = -1;
                this.distributeChangeObj[originalObj.houseId].remove = [];
                this.distributeChangeObj[originalObj.houseId].remove.push(removeObj);
              } else {
                if (originalObj.originalUnitList.length > 0) {
                  let removeOriginalName = [];
                  originalObj.originalUnitList.forEach(originalUnit => {
                    removeOriginalName.push(originalUnit.originalUnitName)
                  });
                  const removeObj = {};
                  removeObj.unitNames = removeOriginalName.join(',');
                  removeObj.useArea = `-${originalObj.houseUseArea}` - 0;
                  removeObj.houseNumber = -1;
                  this.distributeChangeObj[originalObj.houseId].remove = [];
                  this.distributeChangeObj[originalObj.houseId].remove.push(removeObj);
                } else {
                  const removeObj = {};
                  removeObj.unitNames = '闲置';
                  removeObj.useArea = `-${originalObj.houseUseArea}` - 0;
                  removeObj.houseNumber = -1;
                  this.distributeChangeObj[originalObj.houseId].remove = [];
                  this.distributeChangeObj[originalObj.houseId].remove.push(removeObj);
                }
              }
              // + 新分配的单位
              let addNewUnitName = [];
              let selectedUnitIdList = this.Form.selectedUnitIdList;
              selectedUnitIdList = selectedUnitIdList.sort((a, b) => {
                return a - b;
              });
              selectedUnitIdList.forEach(item => {
                for (let i = 0; i < this.unitOptions.length; i++) {
                  if (item === this.unitOptions[i].unitId) {
                    addNewUnitName.push(this.unitOptions[i].unitName);
                    break;
                  }
                }
              });
              const addObj = {};
              addObj.unitNames = addNewUnitName.join(',');
              addObj.useArea = `${originalObj.houseUseArea}` - 0;
              addObj.houseNumber = +1;
              this.distributeChangeObj[originalObj.houseId].add = [];
              this.distributeChangeObj[originalObj.houseId].add.push(addObj);
            }
          } else if (originalIdle === 0) {
            if (activeIdle === 1) {
              // - 闲置
              const removeObj = {};
              removeObj.unitNames = '闲置';
              removeObj.useArea = `-${originalObj.houseUseArea}` - 0;
              removeObj.houseNumber = -1;
              this.distributeChangeObj[originalObj.houseId].remove = [];
              this.distributeChangeObj[originalObj.houseId].remove.push(removeObj);
              // + 新分配的单位
              let addNewUnitName = [];
              let selectedUnitIdList = this.Form.selectedUnitIdList;
              selectedUnitIdList = selectedUnitIdList.sort((a, b) => {
                return a - b;
              });
              selectedUnitIdList.forEach(item => {
                for (let i = 0; i < this.unitOptions.length; i++) {
                  if (item === this.unitOptions[i].unitId) {
                    addNewUnitName.push(this.unitOptions[i].unitName);
                    break;
                  }
                }
              });
              const addObj = {};
              addObj.unitNames = addNewUnitName.join(',');
              addObj.useArea = `${originalObj.houseUseArea}` - 0;
              addObj.houseNumber = +1;
              this.distributeChangeObj[originalObj.houseId].add = [];
              this.distributeChangeObj[originalObj.houseId].add.push(addObj);
            }
          } else if (originalIdle === 1) {
            if (activeIdle === 0) {
              // - 分配的单位（以前）
              let removeOriginalName = [];
              if (originalDistributedUnit.length > 0) {
                originalDistributedUnit.forEach(unit => {
                  removeOriginalName.push(unit.newUnitName)
                });
              }
              const removeObj = {};
              removeObj.unitNames = removeOriginalName.join(',');
              removeObj.useArea = `-${originalObj.houseUseArea}` - 0;
              removeObj.houseNumber = -1;
              this.distributeChangeObj[originalObj.houseId].remove = [];
              this.distributeChangeObj[originalObj.houseId].remove.push(removeObj);
              // + 闲置
              const addObj = {};
              addObj.unitNames = '闲置';
              addObj.useArea = `${originalObj.houseUseArea}` - 0;
              addObj.houseNumber = +1;
              this.distributeChangeObj[originalObj.houseId].add = [];
              this.distributeChangeObj[originalObj.houseId].add.push(addObj);
            } else if (activeIdle === 1) {
              // - 分配的单位（以前）
              let removeOriginalName = [];
              if (originalDistributedUnit.length > 0) {
                originalDistributedUnit.forEach(unit => {
                  removeOriginalName.push(unit.newUnitName)
                });
              }
              const removeObj = {};
              removeObj.unitNames = removeOriginalName.join(',');
              removeObj.useArea = `-${originalObj.houseUseArea}` - 0;
              removeObj.houseNumber = -1;
              this.distributeChangeObj[originalObj.houseId].remove = [];
              this.distributeChangeObj[originalObj.houseId].remove.push(removeObj);
              // + 新分配的单位
              let addNewUnitName = [];
              let selectedUnitIdList = this.Form.selectedUnitIdList;
              selectedUnitIdList = selectedUnitIdList.sort((a, b) => {
                return a - b;
              });
              selectedUnitIdList.forEach(item => {
                for (let i = 0; i < this.unitOptions.length; i++) {
                  if (item === this.unitOptions[i].unitId) {
                    addNewUnitName.push(this.unitOptions[i].unitName);
                    break;
                  }
                }
              });
              const addObj = {};
              addObj.unitNames = addNewUnitName.join(',');
              addObj.useArea = `${originalObj.houseUseArea}` - 0;
              addObj.houseNumber = +1;
              this.distributeChangeObj[originalObj.houseId].add = [];
              this.distributeChangeObj[originalObj.houseId].add.push(addObj);
            }
          }

          // 保存数据到编辑的数组
          const yardId = sessionStorage.getItem("yardId");
          const buildId = sessionStorage.getItem("buildId");
          const planId = sessionStorage.getItem("planId");
          const obj_1 = JSON.parse(JSON.stringify(this.Form));
          if (obj_1.isIdle === 0) {
            obj_1.selectedUnitIdList = [];
          }
          // 保存数据到批量的数组
          for (let i = 0; i < this.distributableList.length; i++) {
            if (this.distributableList[i].houseId === obj_1.houseId) {
              let info = this.distributableList[i];
              obj_1.houseNumber = info.houseNumber;
              obj_1.houseUseArea = info.houseUseArea;
              obj_1.housetypeName = info.housetypeName;
              obj_1.originalUnitNameList = info.originalUnitNameList;
              obj_1.rent = info.rent
              info.isIdle = obj_1.isIdle;
              info.selectedUnitIdList = obj_1.selectedUnitIdList;
            }
          }

          // 保存数据到编辑的数组
          let editAddFlag = true;
          for (let i = 0; i < this.editHouseInfo.length; i++) {
            if (this.editHouseInfo[i].houseId === obj_1.houseId) {
              editAddFlag = false
              this.editHouseInfo.splice(i, 1, obj_1);
            }
          }
          if (editAddFlag) {
            this.editHouseInfo.push(obj_1)
          }

          // 保存数据到提交的数组
          const obj_2 = JSON.parse(JSON.stringify(this.Form));
          for (let i = 0; i < this.originalHouseList.length; i++) {
            if (this.originalHouseList[i].houseId === obj_2.houseId) {
              const data = this.originalHouseList[i];
              this.oldIdle = (data.isIdle === 0 || data.isIdle === 1) ? data.isIdle : null;
            }
          }

          if (obj_2.isIdle === 0) {
            obj_2.selectedUnitIdList = [];
          }
          let temp = {};
          temp.buildId = buildId;
          temp.yardId = yardId;
          temp.planId = planId;
          temp.houseId = obj_2.houseId;
          temp.isIdle = obj_2.isIdle;
          temp.configurePlanDataId = obj_2.configurePlanDataId;
          temp.configurePlanOldUnits = [];
          temp.configurePlanNewUnits = [];
          temp.deleteCPNUnitIds = [];
          temp.deleteCPOUnitIds = [];
          let oldConfUnitList = [];
          if (obj_2.selectedUnitIdList.length > 0) {
            obj_2.selectedUnitIdList = obj_2.selectedUnitIdList.sort((a, b) => {
              return a - b;
            })
          }
          if (obj_2.newConfUnitList && obj_2.newConfUnitList.length > 0) {
            obj_2.newConfUnitList.forEach(item => {
              oldConfUnitList.push(item.newUnitId);
            });
            obj_2.newConfUnitList.forEach(item => {
              if (obj_2.selectedUnitIdList.indexOf(item.newUnitId) === -1) {
                temp.deleteCPNUnitIds.push(item.configurePlanNewUnitId);
              }
            });
          }
          if (obj_2.selectedUnitIdList.length > 0) {
            obj_2.selectedUnitIdList.forEach(item => {
              if (oldConfUnitList.indexOf(item) === -1) {
                temp.configurePlanNewUnits.push({
                  unitId: item
                })
              }
            })
          }
          if (obj_2.oldConfUnitList && obj_2.oldConfUnitList.length > 0) {
            obj_2.oldConfUnitList.forEach(item => {
              temp.deleteCPOUnitIds.push(item.configurePlanOldUnitId);
            })
          }
          if (obj_2.originalUnitList && obj_2.originalUnitList.length > 0) {
            obj_2.originalUnitList.forEach(item => {
              temp.configurePlanOldUnits.push({
                unitId: item.originalUnitId
              })
            });
          }
          if (obj_2.isIdle === 0) {
            temp.configurePlanNewUnits.push({
              unitId: 0
            })
          };
          if (obj_2.isUse === 0) {
            temp.configurePlanOldUnits.push({
              unitId: 0
            })
          }
          for (let i = 0; i < this.saveFormList.length; i++) {
            if (this.saveFormList[i].houseId === temp.houseId) {
              this.saveFormList.splice(i, 1);
            }
          }
          if (temp.deleteCPNUnitIds.length > 0 || temp.configurePlanNewUnits.length > 0 || this.oldIdle !== temp.isIdle) {
            if (this.revertSaveList.indexOf(temp.configurePlanDataId) > -1) {
              const index = this.revertSaveList.indexOf(temp.configurePlanDataId);
              this.revertSaveList.splice(index, 1);
            };
            this.saveFormList.push(temp);
          }
          // 给图形添加修改的图标
          let activeObject = this.canvas.getObjects('group');
          for (let i = 0; i < activeObject.length; i++) {
            const obj = activeObject[i];
            if (obj.type === 'group' && obj.typeName === 'room' && obj.uuid === selected.houseId) {
              const imgs = obj.getObjects('image');
              const imgLen = imgs.length;
              if (imgLen === 0) {
                fabric.Image.fromURL(require('../../assets/imgs/modified-icon.png'), (img) => {
                  const obj_w = obj.width;
                  const obj_h = obj.height;
                  const img_w = img.width;
                  const img_h = img.height;
                  img.set({
                    originX: 'center',
                    originY: 'center',
                    top: - (obj_h / 2) + (img_h / 2) + 1,
                    left: obj_w / 2 - (img_w / 2) - 1
                  });
                  obj.add(img);
                  this.canvas.requestRenderAll();
                })
              }
            }
          }
        })
        this.getChangeData();
      }
      this.dialogVisible = false;
    },
    // 提交房间信息
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.Form.isIdle === 0) {
            this.Form.selectedUnitIdList = [];
          }
          if (this.isBatchDistribute && this.Form.isIdle !== null) {
            // 原使用单位和现存单位对比
            if (this.Form.isIdle === 1 && this.Form.selectedUnitIdList.length > 0) {
              let unitSameArray = [];
              let unitSameFlag = false;
              let selectedUnitIdList = this.Form.selectedUnitIdList.sort((a, b) => {
                return a - b;
              });
              let selectedDistributeCloneList = JSON.parse(JSON.stringify(this.selectedDistributeList));
              for (let i = this.selectedDistributeList.length - 1; i >= 0; i--) {
                let selected = this.selectedDistributeList[i];
                let quondamUnitIdList = [];
                selected.originalUnitList.forEach(item => {
                  quondamUnitIdList.push(item.originalUnitId);
                  quondamUnitIdList.sort((a, b) => {
                    return a - b;
                  });;
                });
                if (JSON.stringify(selectedUnitIdList) == JSON.stringify(quondamUnitIdList)) {
                  unitSameFlag = true;
                  unitSameArray.push(selected.houseNumber);
                  selectedDistributeCloneList.splice(i, 1)
                }
              }
              if (unitSameFlag) {
                const unitSameText = unitSameArray.join('、');
                this.$confirm(`${unitSameText}，原使用单位和分配单位完全相同，若点击确定按钮，房间号为${unitSameText}的房间将不进行分配，其余的房间正常分配。若点击取消，则重新选择分配单位。`, '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning',
                  center: true
                }).then(() => {
                  this.selectedDistributeList = selectedDistributeCloneList;
                  this.batchDistributeDataSubmit();
                }).catch(() => {
                });
              } else {
                this.batchDistributeDataSubmit();
              }
            } else if (this.Form.isIdle === 0) {
              // 已经闲置的房间不能再次分配为闲置
              let unusedRoomArray = [];
              let unusedFlag = false;
              let selectedDistributeCloneList = JSON.parse(JSON.stringify(this.selectedDistributeList));
              for (let i = this.selectedDistributeList.length - 1; i >= 0; i--) {
                let selected = this.selectedDistributeList[i];
                if (selected.isUse === 0 && !selected.rent) {
                  unusedFlag = true;
                  unusedRoomArray.unshift(selected.houseNumber);
                  selectedDistributeCloneList.splice(i, 1)
                }
              }
              if (unusedFlag) {
                const unusedRoomText = unusedRoomArray.join('、');
                this.$confirm(`${unusedRoomText}没有原使用单位，若点击确定按钮，该房间的使用状态不会改变，其余的房间变为不使用。若点击取消，则重新选择。`, '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning',
                  center: true
                }).then(() => {
                  this.selectedDistributeList = selectedDistributeCloneList;
                  this.batchDistributeDataSubmit();
                }).catch(() => {
                });
              } else {
                this.batchDistributeDataSubmit();
              }
            }
          } else if (!this.isBatchDistribute && this.Form.isIdle !== null) {
            // 原使用单位和现存单位对比
            if (this.Form.isIdle === 1) {
              let selectedUnitIdList = this.Form.selectedUnitIdList.sort((a, b) => {
                return a - b;
              });
              let quondamUnitIdList = [];
              this.Form.originalUnitList.forEach(item => {
                quondamUnitIdList.push(item.originalUnitId);
                quondamUnitIdList.sort((a, b) => {
                  return a - b;
                });
              });
              if (JSON.stringify(selectedUnitIdList) == JSON.stringify(quondamUnitIdList)) {
                this.$message({
                  message: "分配单位不能和原使用单位完全相同！",
                  type: "warning"
                });
                return
              }
            }

            // 整合单位面积变化数据
            let originalDistributedUnit = null;
            let originalIdle = null;
            const changeInfo = JSON.parse(JSON.stringify(this.Form));
            const activeIdle = changeInfo.isIdle;
            for (let i = 0; i < this.originalHouseList.length; i++) {
              if (changeInfo.houseId === this.originalHouseList[i].houseId) {
                originalDistributedUnit = this.originalHouseList[i].newConfUnitList;
                originalIdle = this.originalHouseList[i].isIdle;
                break;
              }
            }
            if (this.distributeChangeObj[changeInfo.houseId] === undefined) {
              this.distributeChangeObj[changeInfo.houseId] = {};
              this.distributeChangeObj[changeInfo.houseId].remove = [];
              this.distributeChangeObj[changeInfo.houseId].add = [];
            }
            if (originalIdle !== 0 && originalIdle !== 1) {
              if (activeIdle === 0) {
                // - 原单位
                if (changeInfo.rent) {
                  const removeObj = {};
                  removeObj.unitNames = '出租';
                  removeObj.useArea = `-${changeInfo.houseUseArea}` - 0;
                  removeObj.houseNumber = -1;
                  this.distributeChangeObj[changeInfo.houseId].remove = [];
                  this.distributeChangeObj[changeInfo.houseId].remove.push(removeObj);
                } else {
                  let removeOriginalName = [];
                  changeInfo.originalUnitList.forEach(originalUnit => {
                    removeOriginalName.push(originalUnit.originalUnitName)
                  });
                  const removeObj = {};
                  removeObj.unitNames = removeOriginalName.join(',');
                  removeObj.useArea = `-${changeInfo.houseUseArea}` - 0;
                  removeObj.houseNumber = -1;
                  this.distributeChangeObj[changeInfo.houseId].remove = [];
                  this.distributeChangeObj[changeInfo.houseId].remove.push(removeObj);
                }
                // + 闲置
                const addObj = {};
                addObj.unitNames = '闲置';
                addObj.useArea = `${changeInfo.houseUseArea}` - 0;
                addObj.houseNumber = +1;
                this.distributeChangeObj[changeInfo.houseId].add = [];
                this.distributeChangeObj[changeInfo.houseId].add.push(addObj);
              } else if (activeIdle === 1) {
                // - 原单位
                if (changeInfo.rent) {
                  const removeObj = {};
                  removeObj.unitNames = '出租';
                  removeObj.useArea = `-${changeInfo.houseUseArea}` - 0;
                  removeObj.houseNumber = -1;
                  this.distributeChangeObj[changeInfo.houseId].remove = [];
                  this.distributeChangeObj[changeInfo.houseId].remove.push(removeObj);
                } else {
                  if (changeInfo.originalUnitList.length > 0) {
                    let removeOriginalName = [];
                    changeInfo.originalUnitList.forEach(originalUnit => {
                      removeOriginalName.push(originalUnit.originalUnitName)
                    });
                    const removeObj = {};
                    removeObj.unitNames = removeOriginalName.join(',');
                    removeObj.useArea = `-${changeInfo.houseUseArea}` - 0;
                    removeObj.houseNumber = -1;
                    this.distributeChangeObj[changeInfo.houseId].remove = [];
                    this.distributeChangeObj[changeInfo.houseId].remove.push(removeObj);
                  } else {
                    const removeObj = {};
                    removeObj.unitNames = '闲置';
                    removeObj.useArea = `-${changeInfo.houseUseArea}` - 0;
                    removeObj.houseNumber = -1;
                    this.distributeChangeObj[changeInfo.houseId].remove = [];
                    this.distributeChangeObj[changeInfo.houseId].remove.push(removeObj);
                  }
                }
                // + 新分配的单位
                let addNewUnitName = [];
                let selectedUnitIdList = changeInfo.selectedUnitIdList;
                selectedUnitIdList = selectedUnitIdList.sort((a, b) => {
                  return a - b;
                });
                selectedUnitIdList.forEach(item => {
                  for (let i = 0; i < this.unitOptions.length; i++) {
                    if (item === this.unitOptions[i].unitId) {
                      addNewUnitName.push(this.unitOptions[i].unitName);
                      break;
                    }
                  }
                });
                const addObj = {};
                addObj.unitNames = addNewUnitName.join(',');
                addObj.useArea = `${changeInfo.houseUseArea}` - 0;
                addObj.houseNumber = +1;
                this.distributeChangeObj[changeInfo.houseId].add = [];
                this.distributeChangeObj[changeInfo.houseId].add.push(addObj);
              }
            } else if (originalIdle === 0) {
              if (activeIdle === 1) {
                // - 闲置
                const removeObj = {};
                removeObj.unitNames = '闲置';
                removeObj.useArea = `-${changeInfo.houseUseArea}` - 0;
                removeObj.houseNumber = -1;
                this.distributeChangeObj[changeInfo.houseId].remove = [];
                this.distributeChangeObj[changeInfo.houseId].remove.push(removeObj);
                // + 新分配的单位
                let addNewUnitName = [];
                let selectedUnitIdList = changeInfo.selectedUnitIdList;
                selectedUnitIdList = selectedUnitIdList.sort((a, b) => {
                  return a - b;
                });
                selectedUnitIdList.forEach(item => {
                  for (let i = 0; i < this.unitOptions.length; i++) {
                    if (item === this.unitOptions[i].unitId) {
                      addNewUnitName.push(this.unitOptions[i].unitName);
                      break;
                    }
                  }
                });
                const addObj = {};
                addObj.unitNames = addNewUnitName.join(',');
                addObj.useArea = `${changeInfo.houseUseArea}` - 0;
                addObj.houseNumber = +1;
                this.distributeChangeObj[changeInfo.houseId].add = [];
                this.distributeChangeObj[changeInfo.houseId].add.push(addObj);
              }
            } else if (originalIdle === 1) {
              if (activeIdle === 0) {
                // - 分配的单位（以前）
                let removeOriginalName = [];
                if (originalDistributedUnit.length > 0) {
                  originalDistributedUnit.forEach(unit => {
                    removeOriginalName.push(unit.newUnitName)
                  });
                }
                const removeObj = {};
                removeObj.unitNames = removeOriginalName.join(',');
                removeObj.useArea = `-${changeInfo.houseUseArea}` - 0;
                removeObj.houseNumber = -1;
                this.distributeChangeObj[changeInfo.houseId].remove = [];
                this.distributeChangeObj[changeInfo.houseId].remove.push(removeObj);
                // + 闲置
                const addObj = {};
                addObj.unitNames = '闲置';
                addObj.useArea = `${changeInfo.houseUseArea}` - 0;
                addObj.houseNumber = +1;
                this.distributeChangeObj[changeInfo.houseId].add = [];
                this.distributeChangeObj[changeInfo.houseId].add.push(addObj);
              } else if (activeIdle === 1) {
                // - 分配的单位（以前）
                let removeOriginalName = [];
                if (originalDistributedUnit.length > 0) {
                  originalDistributedUnit.forEach(unit => {
                    removeOriginalName.push(unit.newUnitName)
                  });
                }
                const removeObj = {};
                removeObj.unitNames = removeOriginalName.join(',');
                removeObj.useArea = `-${changeInfo.houseUseArea}` - 0;
                removeObj.houseNumber = -1;
                this.distributeChangeObj[changeInfo.houseId].remove = [];
                this.distributeChangeObj[changeInfo.houseId].remove.push(removeObj);
                // + 新分配的单位
                let addNewUnitName = [];
                let selectedUnitIdList = this.Form.selectedUnitIdList;
                selectedUnitIdList = selectedUnitIdList.sort((a, b) => {
                  return a - b;
                });
                selectedUnitIdList.forEach(item => {
                  for (let i = 0; i < this.unitOptions.length; i++) {
                    if (item === this.unitOptions[i].unitId) {
                      addNewUnitName.push(this.unitOptions[i].unitName);
                      break;
                    }
                  }
                });
                const addObj = {};
                addObj.unitNames = addNewUnitName.join(',');
                addObj.useArea = `${changeInfo.houseUseArea}` - 0;
                addObj.houseNumber = +1;
                this.distributeChangeObj[changeInfo.houseId].add = [];
                this.distributeChangeObj[changeInfo.houseId].add.push(addObj);
              }
            }
            this.getChangeData();

            // 给图形添加修改的图标
            const activeObject = this.canvas.getActiveObject();
            const imgs = activeObject.getObjects('image');
            const imgLen = imgs.length;
            if (imgLen === 0) {
              fabric.Image.fromURL(require('../../assets/imgs/modified-icon.png'), (img) => {
                const obj_w = activeObject.width;
                const obj_h = activeObject.height;
                const img_w = img.width;
                const img_h = img.height;
                img.set({
                  originX: 'center',
                  originY: 'center',
                  top: - (obj_h / 2) + (img_h / 2) + 1,
                  left: obj_w / 2 - (img_w / 2) - 1
                });
                activeObject.add(img);
                this.canvas.requestRenderAll();
              })
            }

            // 保存数据到编辑的数组
            const yardId = sessionStorage.getItem("yardId");
            const buildId = sessionStorage.getItem("buildId");
            const planId = sessionStorage.getItem("planId");
            const obj_1 = JSON.parse(JSON.stringify(this.Form));
            if (obj_1.isIdle === 0) {
              obj_1.selectedUnitIdList = [];
            }
            for (let i = 0; i < this.editHouseInfo.length; i++) {
              if (this.editHouseInfo[i].houseId === obj_1.houseId) {
                this.editHouseInfo.splice(i, 1);
              }
            }
            this.editHouseInfo.push(obj_1);

            // 保存数据到批量的数组
            for (let i = 0; i < this.distributableList.length; i++) {
              if (this.distributableList[i].houseId === obj_1.houseId) {
                this.distributableList.splice(i, 1);
              }
            }
            this.distributableList.push(obj_1);

            // 保存数据到提交的数组
            const obj_2 = JSON.parse(JSON.stringify(this.Form));
            if (obj_2.isIdle === 0) {
              obj_2.selectedUnitIdList = [];
            }
            let temp = {};
            temp.buildId = buildId;
            temp.yardId = yardId;
            temp.planId = planId;
            temp.houseId = obj_2.houseId;
            temp.isIdle = obj_2.isIdle;
            temp.configurePlanDataId = obj_2.configurePlanDataId || null;
            temp.configurePlanOldUnits = [];
            temp.configurePlanNewUnits = [];
            temp.deleteCPNUnitIds = [];
            temp.deleteCPOUnitIds = [];
            let oldConfUnitList = [];
            if (obj_2.selectedUnitIdList.length > 0) {
              obj_2.selectedUnitIdList = obj_2.selectedUnitIdList.sort((a, b) => {
                return a - b;
              })
            }
            if (obj_2.newConfUnitList && obj_2.newConfUnitList.length > 0) {
              obj_2.newConfUnitList.forEach(item => {
                oldConfUnitList.push(item.newUnitId);
              });
              obj_2.newConfUnitList.forEach(item => {
                if (obj_2.selectedUnitIdList.indexOf(item.newUnitId) === -1) {
                  temp.deleteCPNUnitIds.push(item.configurePlanNewUnitId);
                }
              });
            }
            if (obj_2.selectedUnitIdList.length > 0) {
              obj_2.selectedUnitIdList.forEach(item => {
                if (oldConfUnitList.indexOf(item) === -1) {
                  temp.configurePlanNewUnits.push({
                    unitId: item
                  })
                }
              })
            }
            if (obj_2.oldConfUnitList && obj_2.oldConfUnitList.length > 0) {
              obj_2.oldConfUnitList.forEach(item => {
                temp.deleteCPOUnitIds.push(item.configurePlanOldUnitId);
              })
            }
            if (obj_2.originalUnitList && obj_2.originalUnitList.length > 0) {
              obj_2.originalUnitList.forEach(item => {
                temp.configurePlanOldUnits.push({
                  unitId: item.originalUnitId
                })
              });
            }
            if (obj_2.isIdle === 0) {
              temp.configurePlanNewUnits.push({
                unitId: 0
              })
            };
            if (obj_2.isUse === 0) {
              temp.configurePlanOldUnits.push({
                unitId: 0
              })
            }
            for (let i = 0; i < this.saveFormList.length; i++) {
              if (this.saveFormList[i].houseId === temp.houseId) {
                this.saveFormList.splice(i, 1);
              }
            }
            if (temp.deleteCPNUnitIds.length > 0 || temp.configurePlanNewUnits.length > 0 || this.oldIdle !== temp.isIdle) {
              if (this.revertSaveList.indexOf(temp.configurePlanDataId) > -1) {
                const index = this.revertSaveList.indexOf(temp.configurePlanDataId);
                this.revertSaveList.splice(index, 1);
              };
              this.saveFormList.push(temp);
            }
            this.dialogVisible = false;
          }
        } else {
          return false;
        }
      });
    },
    // 保存全部
    allSave() {
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      const configureId = sessionStorage.getItem("configureId");
      const planId = sessionStorage.getItem("planId");
      if (!buildId || !floorName) {
        this.$message({
          message: "请先选择楼层",
          type: "warning"
        });
        return false;
      }
      let saveObj = {};
      saveObj.configureId = configureId;
      saveObj.planId = planId;
      saveObj.configurePlanDataList = this.saveFormList;
      saveObj.configurePlanChangeDataList = this.distributeChangeList;
      if (this.revertSaveList.length > 0) {
        saveObj.deleteCPDIds = this.revertSaveList;
      }
      saveConfigurePlanData(saveObj).then(res => {
        this.$message({
          message: '保存成功',
          type: "success"
        });
        this.saveFormList = [];
        this.editHouseInfo = [];
        this.revertSaveList = [];
        this.originalHouseList = [];
        this.distributableList = [];
        this.distributableIdList = [];
        this.selectedDistributeList = [];
        this.distributeChangeList = [];
        this.distributeChangeObj = {};
        const buildId = sessionStorage.getItem('buildId') - 0;
        const floorName = sessionStorage.getItem('floorName') - 0;
        this.getallocationFloorInfo(buildId, floorName);
        this.getOldChangeData();
      }).catch(err => {
        const msg = err.result ? err.result : '保存失败';
        this.$message({
          message: msg,
          type: "error"
        });
        this.saveFormList = [];
        this.editHouseInfo = [];
        this.revertSaveList = [];
        this.originalHouseList = [];
        this.distributableList = [];
        this.distributableIdList = [];
        this.selectedDistributeList = [];
        this.distributeChangeList = [];
        this.distributeChangeObj = {};
      })
    },
    // 去掉图标
    removeIcon() {
      const roomObj = this.canvas.getObjects('group');
      roomObj.forEach(room => {
        if (room.typeName === 'room') {
          const image = room.getObjects('image');
          if (image.length > 0) {
            image.forEach(img => {
              if (img.typeName === 'overproof' || img.typeName === 'isRecord') {
                room.remove(img);
                this.canvas.renderAll();
              }
            })
          }
        }
      })
      const buildId = sessionStorage.getItem('buildId');
      const floorName = sessionStorage.getItem('floorName');
      this.getallocationFloorInfo(buildId, floorName);
    },
    // 获取整层楼图形信息
    getHouseGraphJson(buildId, floorName) {
      getHouseGraphJson(buildId, floorName)
        .then(res => {
          this.canvas.loadFromJSON(res, this.removeIcon);
          if (!this.showChangeDetail) {
            this.showChangeDetail = true;
          }
        })
        .catch(err => {
          const msg = err.result ? err.result : '暂无楼层图形';
          this.$message({
            message: msg,
            type: "warning"
          });
          this.canvas.clear();
        });
    },
    // 获取整层楼的房间信息
    getallocationFloorInfo(buildId, floorName) {
      this.originalHouseList = [];
      this.distributableList = [];
      this.distributableIdList = [];
      this.selectedDistributeList = [];
      const planId = sessionStorage.getItem('planId');
      getallocationFloorInfo(buildId, floorName, planId).then(res => {
        res.forEach(item => {
          let obj = {
            configurePlanDataId: null,
            houseId: null,
            houseNumber: null,
            originalUnitNameList: '',
            newUnitNameList: '',
            houseUseArea: '',
            housetypeName: '',
            isIdle: null,
            originalUnitList: [],
            oldConfUnitList: [],
            newConfUnitList: [],
            selectedUnitIdList: [],
            isUse: null,
            applyStatus: null,
            handleWay: null,
            isIdleText: '',
            rent: ''
          }
          for ([key, value] of Object.entries(item.allocationUnit)) {
            if (obj[key] !== undefined) {
              obj[key] = value;
            }
          }
          if (item.allocationConf) {
            for ([key, value] of Object.entries(item.allocationConf)) {
              if (obj[key] !== undefined) {
                obj[key] = value;
              }
            }
          }

          obj.newConfUnitList = item.newConfUnitList;
          obj.oldConfUnitList = item.oldConfUnitList;
          obj.isIdleText = (obj.isIdle === 0 || obj.isIdle === 1) ? '已调整' : '未调整';
          let originalUnitName = [];
          let newUnitName = [];

          if (obj.rent) {
            obj.originalUnitNameList = '出租'
          } else {
            if (obj.originalUnitList && obj.originalUnitList.length > 0) {
              obj.originalUnitList.forEach(unit => {
                originalUnitName.push(unit.originalUnitName);
              });
              obj.originalUnitNameList = originalUnitName.join(',');
            } else {
              obj.originalUnitNameList = '暂无'
            }
          }

          if (obj.configurePlanDataId && obj.newConfUnitList.length > 0) {
            obj.newConfUnitList.forEach(unit => {
              obj.selectedUnitIdList.push(unit.newUnitId);
              newUnitName.push(unit.newUnitName);
            })
            obj.newUnitNameList = originalUnitName.join(',');
          } else {
            obj.newUnitNameList = '暂无'
          }

          this.originalHouseList.push(obj);
          const _obj = JSON.parse(JSON.stringify(obj));
          this.distributableList.push(_obj)
          this.distributableIdList.push(_obj.houseId);
        })
        // 已经修改过的添加图标
        const roomObj = this.canvas.getObjects('group');
        for (let i = 0; i < this.originalHouseList.length; i++) {
          if (this.originalHouseList[i].isIdle === 1 || this.originalHouseList[i].isIdle === 0) {
            for (let j = 0; j < roomObj.length; j++) {
              const room = roomObj[j];
              if (room.typeName === 'room') {
                if (room.uuid === this.originalHouseList[i].houseId) {
                  fabric.Image.fromURL(require('../../assets/imgs/modified-icon.png'), (img) => {
                    const obj_w = room.width;
                    const obj_h = room.height;
                    const img_w = img.width;
                    const img_h = img.height;
                    img.set({
                      originX: 'center',
                      originY: 'center',
                      top: - (obj_h / 2) + (img_h / 2) + 1,
                      left: obj_w / 2 - (img_w / 2) - 1
                    });
                    room.add(img);
                    this.canvas.requestRenderAll();
                  });
                  break;
                }
              }
            }
          }
        }
        // console.log('originalHouseList', this.originalHouseList);
        // console.log('distributableList', this.distributableList);
      }).catch(err => {
        const msg = err.result ? err.result : '楼层房间数据获取失败';
        this.$message({
          message: msg,
          type: "warning"
        });
      })
    },
    // 整合改变的数据
    getChangeData() {
      const obj = {};
      let data = [];
      let changeList = [];
      this.distributeChangeList = JSON.parse(JSON.stringify(this.oldDistributeChangeList));
      for (let key in this.distributeChangeObj) {
        data.push.apply(data, this.distributeChangeObj[key].add);
        data.push.apply(data, this.distributeChangeObj[key].remove);
      }
      if (data.length > 0) {
        data.forEach(item => {
          if (obj[item.unitNames] !== undefined) {
            obj[item.unitNames].push(item);
          } else {
            obj[item.unitNames] = [];
            obj[item.unitNames].push(item);
          }
        });
        for (let key in obj) {
          let itemObj = {};
          let area = 0;
          let num = 0;
          let flag = true;
          itemObj.unitNames = key;
          obj[key].forEach(item => {
            area += item.useArea;
            num += item.houseNumber;
          })
          itemObj.useArea = area;
          itemObj.houseNumber = num;
          if (this.distributeChangeList.length > 0) {
            for (let i = this.distributeChangeList.length - 1; i >= 0; i--) {
              if (itemObj.unitNames === this.distributeChangeList[i].unitNames) {
                flag = false;
                this.distributeChangeList[i].useArea = itemObj.useArea + this.distributeChangeList[i].useArea;
                this.distributeChangeList[i].houseNumber = itemObj.houseNumber + this.distributeChangeList[i].houseNumber;
                if (this.distributeChangeList[i].useArea === 0 && this.distributeChangeList[i].houseNumber === 0) {
                  this.distributeChangeList.splice(i, 1);
                }
              }
            }
          }
          if (flag) {
            changeList.push(itemObj)
          }
        }
      }
      this.distributeChangeList.push.apply(this.distributeChangeList, changeList);
      this.distributeChangeList.forEach(item => {
        item.useArea = item.useArea.toFixed(2);
      })
      // console.log('distributeChangeList', this.distributeChangeList);
    },
    // dialog打开的回调
    dialogOpen() {
      this.$nextTick(() => {
        if (this.$refs["Form"]) {
          this.$refs["Form"].clearValidate();
        }
      })
    },
    // 关闭模态框
    dialogClosed() {
      this.clearForm(this.Form);
      this.currentUseState = false;
      this.oldIdle = null;
      this.distributeFlag = false;
      this.isBatchDistribute = false;
      this.selectedDistributeList = [];
    },
    // 获取数据类型
    getDataType(obj) {
      let typeArr = ["String", "Object", "Number", "Array", "Date"];
      for (let name of typeArr) {
        if (Object.prototype.toString.call(obj) === `[object ${name}]`) {
          return name;
        }
      }
    },
    // 清空表单
    clearForm(form) {
      for (let key in form) {
        let type = this.getDataType(form[key]);
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
    // 只能输入正浮点数
    limitFloat(key, flag) {
      this.Form[key] = this.Form[key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '')
      if (flag && this.Form[key] !== '') {
        this.Form[key] = (this.Form[key] - 0).toFixed(2);
      }
    },
    // 选择需要分配的房间
    handleSelectionChange(rows) {
      this.selectedDistributeList = rows;
    },
    // 获取改变的数据
    getOldChangeData() {
      const plan = sessionStorage.getItem('planId') - 0;
      getChangeData(plan).then(res => {
        if (res) {
          this.oldDistributeChangeList = res;
          this.distributeChangeList = res;
        }
      }).catch(err => {
        const msg = err.result ? err.result : '分配统计数据获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    getRouterList() {
      getRouterList()
        .then(res => {
          this.allNavList = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : '路由信息获取失败！';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.init();
    });
    eventBus.$off("getGraphJson");
    eventBus.$off("clearCanvas");
    eventBus.$off("showDistributeTips");
    eventBus.$off("collapse");
    eventBus.$on("getGraphJson", obj => {
      this.getHouseGraphJson(obj.buildId, obj.floorName);
      if (obj.canDistributeOrSave === false) {
        this.canDistributeOrSave = false;
      } else {
        this.canDistributeOrSave = true;
      }
      if (obj.notDistributeReason) {
        this.notDistributeReason = obj.notDistributeReason;
      }
    });
    eventBus.$on("clearCanvas", () => {
      if (this.canvas && this.canvas.clear) {
        this.canvas.clear();
      }
      sessionStorage.removeItem("yardId");
      sessionStorage.removeItem("buildId");
      sessionStorage.removeItem("floorName");
    });
    eventBus.$on("showDistributeTips", (path, flag) => {
      if (this.revertSaveList.length > 0 || this.saveFormList.length > 0) {
        this.$confirm('当前楼层数据还未保存，可点击右下角保存按钮保存，如若切换楼层将清空刚才所编辑的数据，是否继续此操作？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          sessionStorage.removeItem("yardId");
          sessionStorage.removeItem("buildId");
          sessionStorage.removeItem("floorName");
          if (path) {
            this.$router.push(path);
          }
          if (flag) {
            this.menuKey = this.$route.params.id;
            for (let i = 0; i < this.allNavList.length; i++) {
              if (this.allNavList[i].key === this.menuKey) {
                this.navlist = this.allNavList[i].children;
                this.$store.commit('changeNavList', this.navlist);
              }
            };
          }
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消切换'
          });
        });
      } else {
        sessionStorage.removeItem("yardId");
        sessionStorage.removeItem("buildId");
        sessionStorage.removeItem("floorName");
        if (path) {
          this.$router.push(path);
        }
        if (flag) {
          this.menuKey = this.$route.params.id;
          for (let i = 0; i < this.allNavList.length; i++) {
            if (this.allNavList[i].key === this.menuKey) {
              this.navlist = this.allNavList[i].children;
              this.$store.commit('changeNavList', this.navlist);
            }
          };
        }
      }
    });
    eventBus.$on('collapse', () => {
      setTimeout(() => {
        this.setZoom();
      }, 1000)
    });
    getUserUnitList().then(res => {
      this.unitOptions = res;
    }).catch(err => {
      const msg = err.result ? err.result : '单位下拉选项获取失败';
      this.$message({
        message: msg,
        type: "error"
      });
    })
    this.getOldChangeData();
    this.getRouterList();
  }
};
</script>

<style>
#roomDistributeView {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  overflow: hidden;
}
#roomDistributeView .canvasDiv {
  width: 100%;
  height: 100%;
  background: url("../../assets/imgs/canvas_bg.png") repeat;
}
#roomDistributeView .house-type-box {
  position: absolute;
  right: 20px;
  bottom: 200px;
}
#roomDistributeView .house-type-box > ul > li {
  line-height: 25px;
}
#roomDistributeView .house-type-box > ul > li > span {
  display: inline-block;
  width: 20px;
  height: 15px;
  position: relative;
  top: 2px;
}
#roomDistributeView .btn-box > button {
  position: absolute;
  width: 110px;
  height: 40px;
  background: #3891e0;
  color: #fff;
  border-radius: 5px;
  font-size: 16px;
  font-weight: 600;
}
#roomDistributeView .btn-box > button:hover {
  background: #2378c4;
}
#roomDistributeView .btn-box > button.graph-btn {
  right: 20px;
  bottom: 30px;
}
#roomDistributeView .btn-box > .active {
  background: #2378c4;
}
#roomDistributeView .plane {
  width: 100%;
  height: 100%;
}
#roomDistributeView .base-info-title,
#roomDistributeView .distribute-unit-title {
  height: 32px;
  line-height: 32px;
  background-color: #bee2f3;
}
#roomDistributeView .el-dialog__body {
  padding: 0;
}
#roomDistributeView .base-info-icon {
  width: 16px;
  height: 18px;
  display: inline-block;
  background: url("../../assets/imgs/base-info-icon.png") no-repeat;
  margin-left: 20px;
  position: relative;
  top: 3px;
  background-size: contain;
}
#roomDistributeView .base-info-content {
  padding: 20px;
  overflow: hidden;
}
#roomDistributeView .grid-content {
  overflow: hidden;
}
#roomDistributeView .grid-content > span.grid-content-title {
  float: left;
  display: block;
  height: 20px;
  line-height: 20px;
  width: 78px;
  text-align: right;
  text-align: justify;
  text-justify: distribute-all-lines;
  text-align-last: justify;
}
#roomDistributeView .batch {
  margin-bottom: 20px;
}
#roomDistributeView .batch > span {
  width: 140px;
}
#roomDistributeView .grid-content > .room-table {
  width: 100%;
  box-sizing: border-box;
  float: left;
}
#roomDistributeView .edit-info-icon {
  width: 16px;
  height: 16px;
  display: inline-block;
  background: url("../../assets/imgs/edit-icon.png") no-repeat;
  margin-left: 20px;
  position: relative;
  top: 3px;
  background-size: contain;
}
#roomDistributeView .distribute-unit-content {
  padding: 20px;
}
#roomDistributeView .unit-select-form-item .el-input__inner {
  width: 300px;
}
#roomDistributeView .revocation-btn {
  color: #409eff;
  margin-left: 20px;
  cursor: pointer;
}
#roomDistributeView .distribute-btn {
  float: right;
  margin-right: 20px;
  cursor: pointer;
  color: #1582d8;
}
#roomDistributeView .batch-distribute-btn {
  bottom: 140px !important;
}
#roomDistributeView .batch-revert-btn {
  bottom: 85px !important;
}
#roomDistributeView .change-detail-dialog {
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
#roomDistributeView .change-detail-content {
  width: calc(100% - 30px);
  height: calc(100% - 75px);
  background: #fff;
  margin: 0px 15px 15px 15px;
  position: relative;
}
#roomDistributeView .change-detail-title {
  width: calc(100% - 30px);
  height: 35px;
  line-height: 35px;
  margin: 25px 15px 0px 15px;
  color: rgb(4, 148, 230);
  border-bottom: 1px solid rgb(4, 148, 230);
  background: #fff;
  padding-left: 20px;
  box-sizing: border-box;
  font-size: 16px;
}
#roomDistributeView .down-arrow-btn {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 50%;
  margin-left: -48px;
}
#roomDistributeView .top-arrow-btn {
  position: absolute;
  cursor: pointer;
  top: -22px;
  left: 50%;
  margin-left: -48px;
}
</style>
