<template>
  <div id="houseGraph">
    <!-- 平面图 -->

    <div class="plane" v-show="activeBtnIndex === 0">
      <div class="tools-box">
        <ul class="tools">
          <el-tooltip
            class="item"
            content="移动"
            effect="dark"
            placement="left-start"
          >
            <li
              class="icon-move-black"
              :class="{ 'acitive-icon': activeIconIndex === 0 }"
              @click="tool('move', 0)"
            >
              <div class="tooltip-box"></div>
            </li>
          </el-tooltip>
          <el-tooltip
            class="item"
            content="实线"
            effect="dark"
            placement="left-start"
          >
            <li
              class="icon-line-black"
              :class="{ 'acitive-icon': activeIconIndex === 1 }"
              @click="tool('line', 1)"
            >
              <div class="tooltip-box"></div>
            </li>
          </el-tooltip>
          <el-tooltip
            class="item"
            content="虚线"
            effect="dark"
            placement="left-start"
          >
            <li
              class="icon-dottedline-black"
              :class="{ 'acitive-icon': activeIconIndex === 2 }"
              @click="tool('dottedline', 2)"
            >
              <div class="tooltip-box"></div>
            </li>
          </el-tooltip>
          <el-tooltip
            class="item"
            content="房间"
            effect="dark"
            placement="left-start"
          >
            <li
              class="icon-rectangle-black"
              :class="{ 'acitive-icon': activeIconIndex === 3 }"
              @click="tool('rectangle', 3)"
            >
              <div class="tooltip-box"></div>
            </li>
          </el-tooltip>
          <el-tooltip
            class="item"
            content="文字"
            effect="dark"
            placement="left-start"
          >
            <li
              class="icon-text-black"
              :class="{ 'acitive-icon': activeIconIndex === 4 }"
              @click="tool('text', 4)"
            >
              <div class="tooltip-box"></div>
            </li>
          </el-tooltip>
          <el-tooltip
            class="item"
            content="门"
            effect="dark"
            placement="left-start"
          >
            <li
              class="icon-door-black"
              :class="{ 'acitive-icon': activeIconIndex === 6 }"
              @click="tool('door', 6)"
            >
              <el-popover
                placement="right"
                width="108"
                trigger="click"
                v-model="popoverDoorIcon"
                popper-class="door-icon-popper"
              >
                <div class="door-icon-box">
                  <div class="door-icon-item" @click="addDoor('door-top')">
                    <span>单开门(上)</span>
                  </div>
                  <div class="door-icon-item" @click="addDoor('door-bottom')">
                    <span>单开门(下)</span>
                  </div>
                  <div class="door-icon-item" @click="addDoor('door-left')">
                    <span>单开门(左)</span>
                  </div>
                  <div class="door-icon-item" @click="addDoor('door-right')">
                    <span>单开门(右)</span>
                  </div>
                </div>
                <div class="tooltip-box" slot="reference">
                  <i class="subscript"></i>
                </div>
              </el-popover>
            </li>
          </el-tooltip>
          <el-tooltip
            class="item"
            content="楼梯"
            effect="dark"
            placement="left-start"
          >
            <li @click="addStairs" class="icon-stairs-black" data-type="text">
              <div class="tooltip-box"></div>
            </li>
          </el-tooltip>
          <el-tooltip
            class="item"
            content="电梯"
            effect="dark"
            placement="left-start"
          >
            <li
              @click="addElevator"
              class="icon-elevator-black"
              data-type="text"
            >
              <div class="tooltip-box"></div>
            </li>
          </el-tooltip>
          <el-tooltip
            class="item"
            content="复制"
            effect="dark"
            placement="left-start"
          >
            <li @click="copyRoom" class="icon-copy-black" data-type="text">
              <div class="tooltip-box"></div>
            </li>
          </el-tooltip>
          <el-tooltip
            class="item"
            content="粘贴"
            effect="dark"
            placement="left-start"
          >
            <li @click="pasteRoom" class="icon-paste-black" data-type="text">
              <div class="tooltip-box"></div>
            </li>
          </el-tooltip>
          <el-tooltip
            class="item"
            content="附件"
            effect="dark"
            placement="left-start"
          >
            <li
              @click="accessoryHandle(5)"
              :class="{ 'acitive-icon': activeIconIndex === 5 }"
              class="icon-accessory-black"
              data-type="text"
            >
              <div class="tooltip-box"></div>
            </li>
          </el-tooltip>
          <el-tooltip
            class="item"
            content="合并"
            effect="dark"
            placement="left-start"
          >
            <li @click="merge" class="icon-merge-black" data-type="text">
              <div class="tooltip-box"></div>
            </li>
          </el-tooltip>
        </ul>
      </div>
      <div @contextmenu.prevent="showContextMenu($event)" class="canvasDiv">
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
      <save-btn
        class="graph-save-btn"
        @clickBtn="allSave"
        v-show="activeBtnIndex === 0"
      ></save-btn>
    </div>
    <!-- 房间信息模态框 -->
    <el-dialog
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="1450px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>基本信息</span>
      </div>
      <div class="basic-info">
        <div class="base-info-content clearfix">
          <div class="base-info-line">
            <div class="content-item houseNumber clearfix">
              <label>房间号</label>
              <el-input
                size="small"
                style="width: 160px"
                v-model="Form.houseNumber"
                placeholder="请输入房间号"
                v-enter-trim
              ></el-input>
            </div>
            <div class="content-item housetypeId clearfix">
              <label>房屋类型</label>
              <el-select
                placeholder="请选择"
                size="small"
                style="width: 160px"
                v-model="Form.housetypeId"
                @change="houseTypeChange"
              >
                <el-option
                  :key="item.housetypeId"
                  :label="item.housetypeName"
                  :value="item.housetypeId"
                  v-for="item in houseType"
                ></el-option>
              </el-select>
            </div>
            <div class="content-item houseUseArea clearfix">
              <label>{{
                Form.housetypeId === 4 ? "建筑面积" : "使用面积"
              }}</label>
              <el-input
                size="small"
                style="width: 160px"
                v-model="Form.houseUseArea"
                @keyup.native="limitFloat('houseUseArea')"
                @blur="limitFloat('houseUseArea', true)"
                :placeholder="
                  Form.housetypeId === 4
                    ? '请输入建筑面积(㎡)'
                    : '请输入使用面积(㎡)'
                "
              ></el-input>
            </div>
            <div class="content-item useType clearfix">
              <label>使用类型</label>
              <el-input
                size="small"
                style="width: 160px"
                v-model="Form.useType"
                placeholder="请输入使用类型"
                v-enter-trim
              ></el-input>
            </div>
            <div class="content-item clearfix">
              <label>是否使用</label>
              <div class="isUser-radio-box">
                <el-radio :label="0" style="width: 50px" v-model="Form.isIdle"
                  >使用</el-radio
                >
                <el-radio :label="1" style="width: 50px" v-model="Form.isIdle"
                  >闲置</el-radio
                >
              </div>
            </div>
          </div>
          <div class="base-info-line">
            <div class="content-item clearfix" v-if="Form.isIdle === 0">
              <label>是否自用</label>
              <div class="isUser-radio-box">
                <el-radio :label="1" style="width: 50px" v-model="Form.isUse"
                  >自用</el-radio
                >
                <el-radio :label="0" style="width: 50px" v-model="Form.isUse"
                  >非自用</el-radio
                >
              </div>
            </div>
            <div
              class="content-item unitIds clearfix"
              v-if="
                Form.isIdle === 0 &&
                Form.isUse === 1 &&
                Form.housetypeId &&
                Form.housetypeId !== 1
              "
            >
              <label>所属单位</label>
              <el-select
                placeholder="请选择"
                size="small"
                style="width: 160px"
                v-model="unitIds"
              >
                <el-option
                  :key="item.unitId"
                  :label="item.unitName"
                  :value="item.unitId"
                  v-for="item in unitOptions"
                  :disabled="item.disabled"
                ></el-option>
              </el-select>
            </div>
          </div>
        </div>
      </div>
      <div class="rent-info" v-if="Form.rent && Form.isUse === 0">
        <div class="line"></div>
        <div class="rent-tips">
          该房间为{{ Form.rent.type === 1 ? "出租" : "出借" }}的房间，{{
            Form.rent.type === 1 ? "出租" : "出借"
          }}相关信息如下：
        </div>
        <div class="rent-info-line clearfix">
          <div class="rent-item">
            <span>{{
              Form.rent.type === 1 ? "承租用途：" : "承借用途："
            }}</span>
            <span>{{
              Form.rent.lesseeUse ? Form.rent.lesseeUse : "暂无"
            }}</span>
          </div>
          <div class="rent-item">
            <span>{{ Form.rent.type === 1 ? "承租方：" : "借用单位：" }}</span>
            <span>{{ Form.rent.renter }}</span>
          </div>
          <div class="rent-item">
            <span>开始时间：</span>
            <span>{{ Form.rent.rentBegin }}</span>
          </div>
          <div class="rent-item">
            <span>结束时间：</span>
            <span>{{ Form.rent.rentEnd }}</span>
          </div>
        </div>
        <div class="rent-info-line clearfix">
          <div class="rent-item" v-if="Form.rent.type === 1">
            <span>租金：</span>
            <span>{{ Form.rent.rentMoney }}</span>
          </div>
          <div class="rent-item" v-if="Form.rent.type === 1">
            <span>押金：</span>
            <span>{{ Form.rent.deposit }}</span>
          </div>
          <div class="rent-item">
            <span>联系人：</span>
            <span>{{ Form.rent.contacts ? Form.rent.contacts : "暂无" }}</span>
          </div>
          <div class="rent-item">
            <span>联系方式：</span>
            <span>{{
              Form.rent.contactPhone ? Form.rent.contactPhone : "暂无"
            }}</span>
          </div>
        </div>
      </div>
      <div
        class="use-info"
        v-if="Form.isIdle === 0 && Form.isUse === 1 && Form.housetypeId === 1"
      >
        <div class="line"></div>
        <div class="content-item clearfix">
          <label style="width: 140px">是否为领导办公室</label>
          <el-radio
            label="1"
            style="width: 50px"
            v-model="isLeader"
            name="isLeader"
            >是</el-radio
          >
          <el-radio
            label="2"
            style="width: 50px"
            v-model="isLeader"
            name="isLeader"
            >否</el-radio
          >
        </div>
        <div class="leader-info" v-show="isLeader === '1'">
          <div class="unit-info-content unit-info-main">
            <div class="content-item-unitName clearfix">
              <label>所属单位</label>
              <el-select
                placeholder="请选择"
                size="small"
                style="width: 160px"
                v-model="Form.leadingOfficeVo.unitId"
              >
                <el-option
                  :key="item.unitId"
                  :label="item.unitName"
                  :value="item.unitId"
                  v-for="item in unitOptions"
                  :disabled="item.disabled"
                ></el-option>
              </el-select>
            </div>
            <div
              :key="i"
              class="content-item-group clearfix"
              v-for="(members, i) in Form.leadingOfficeVo.leadingOfficeList"
            >
              <div class="group-item clearfix">
                <label>等级</label>
                <el-select
                  v-model="members.professionalLevel"
                  filterable
                  placeholder="请选择等级"
                  size="small"
                  style="width: 160px"
                  @change="professionalLevelChange($event, i)"
                >
                  <el-option
                    v-for="item in professionalLevelOpts"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </div>
              <div class="group-item clearfix">
                <label>职级</label>
                <el-select
                  placeholder="请选择职级"
                  no-data-text="请先选择等级"
                  size="small"
                  style="width: 160px"
                  v-model="members.professionalId"
                >
                  <el-option
                    v-for="item in members.professionOpts"
                    :key="item.professionalId"
                    :label="item.professionalName"
                    :value="item.professionalId"
                  ></el-option>
                </el-select>
              </div>
              <div class="group-item clearfix">
                <label>职务</label>
                <el-input
                  size="small"
                  style="width: 160px"
                  v-model="members.duty"
                  placeholder="请输入职务"
                  v-enter-trim
                ></el-input>
              </div>
              <div class="group-item clearfix">
                <label>使用人姓名</label>
                <el-input
                  size="small"
                  style="width: 160px"
                  v-model.number="members.leadingofficeName"
                  placeholder="请输入使用人姓名"
                  v-enter-trim
                ></el-input>
              </div>
              <div class="group-item clearfix">
                <label>在职情况</label>
                <el-select
                  placeholder="请选择在职情况"
                  size="small"
                  style="width: 160px"
                  v-model="members.leadingofficeWorkStatueId"
                >
                  <el-option
                    :key="item.workstatusId"
                    :label="item.workstatusName"
                    :value="item.workstatusId"
                    v-for="item in workStatue"
                  ></el-option>
                </el-select>
              </div>
              <div class="group-item clearfix">
                <a
                  @click="addProfessionItem()"
                  class="add-item-btn"
                  href="javascript:;"
                  v-show="i === 0"
                >
                  <i class="el-icon-plus"></i> 添加
                </a>
                <a
                  @click="deleteProfessionItem(i)"
                  class="delete-item-btn"
                  href="javascript:;"
                  v-show="i !== 0"
                >
                  <i class="el-icon-close"></i> 删除
                </a>
              </div>
            </div>
          </div>
        </div>
        <div class="unit-info" v-show="isLeader === '2'">
          <div class="unit-info-content clearfix">
            <div class="content-item unitId">
              <label>所属单位</label>
              <el-select
                placeholder="请选择"
                size="small"
                style="width: 160px"
                v-model="Form.housingUseVoList[0].unitId"
              >
                <el-option
                  :key="item.unitId"
                  :label="item.unitName"
                  :value="item.unitId"
                  v-for="item in unitOptions"
                  :disabled="item.disabled"
                ></el-option>
              </el-select>
            </div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn
          @clickBtn="dialogVisible = false"
          :fontSize="14"
        ></cancel-btn>
        <confirm-btn
          @clickBtn="submitForm('Form')"
          :fontSize="14"
        ></confirm-btn>
      </span>
    </el-dialog>
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
        <ul class="accessory-checkbox">
          <li v-for="(item, index) in fileList" :key="index">
            <el-checkbox
              @change="checkboxChange($event, item.id)"
            ></el-checkbox>
          </li>
        </ul>
        <el-upload
          class="upload-demo"
          :headers="postHeaders"
          :action="uploadCADUrl"
          :on-remove="handleRemove"
          :on-success="handleSuccess"
          :on-error="handleError"
          :file-list="fileList"
          :before-upload="beforeAvatarUpload"
        >
          <el-button size="small" type="primary" slot="trigger"
            >上传CAD</el-button
          >
          <el-button
            size="small"
            type="primary"
            @click="downloadCAD"
            style="margin-left: 10px"
            slot="tip"
            v-if="fileList.length > 0"
            >下载CAD</el-button
          >
        </el-upload>
      </div>
    </el-dialog>
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
    <div
      class="drawing-size-scale"
      v-if="this.drawing_width_scale > 0 && this.drawing_height_scale > 0"
    >
      <span>{{ this.drawing_width_scale }}</span>
      <span>x</span>
      <span>{{ this.drawing_height_scale }}</span>
    </div>

    <!-- 跳转到示意图总览 -->
    <div class="start-view-box" v-if="activeBtnIndex === 0 && showViewBtn">
      <el-tooltip
        class="item"
        effect="dark"
        content="浏览房间数据"
        placement="right"
      >
        <div class="view-icon" @click="startView">
          <span class="iconfont">&#xe60e;</span>
        </div>
      </el-tooltip>
    </div>

    <!-- 房间关联配置信息 -->
    <el-dialog
      :visible.sync="configDialogVisible"
      :close-on-click-modal="false"
      width="780px"
      @closed="configDialogClosed"
      :center="true"
      class="config-info-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>提示</span>
      </div>
      <div class="dialog-content">
        <p class="save-before-room-tips">
          刚刚修改的房间，关联以下配置信息，点击确定按钮，将撤销以下全部配置信息并保存刚刚修改的房间信息，如若继续请点击确定按钮，否则请点击取消按钮取消操作。
        </p>
        <p>
          <table-component :tableData="configTableData" :tableHeight="200">
            <el-table-column
              type="index"
              width="50"
              label="序号"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="configureNo"
              label="配置编号"
              align="center"
              :show-overflow-tooltip="true"
            ></el-table-column>
            <el-table-column
              prop="unitName"
              label="申请单位"
              align="center"
              :show-overflow-tooltip="true"
            ></el-table-column>
            <el-table-column
              prop="configureApplicant"
              label="申请人"
              align="center"
              :show-overflow-tooltip="true"
            ></el-table-column>
            <el-table-column
              prop="configureDate"
              label="申请时间"
              align="center"
              :show-overflow-tooltip="true"
            ></el-table-column>
          </table-component>
        </p>
        <div class="config-table-pagination-box">
          <div class="config-table-pagination">
            <el-pagination
              small
              layout="prev, pager, next"
              :current-page="configCurrentPage"
              :page-size="configPageSize"
              :total="configTableTotal"
              @current-change="configCurrentChange"
            ></el-pagination>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn
          @clickBtn="configDialogVisible = false"
          :fontSize="14"
        ></cancel-btn>
        <confirm-btn @clickBtn="saveAllInfo" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { PRO_TYPE } from "@/axios/global";
import {
  getHouseTypeList,
  getAllProfessionList,
  workingConditions,
  uploadUrl,
  getUserUnitList,
  getRouterList,
  isUnionConfig,
  configureListByHouseId,
  getConfigureIdList,
} from "@/axios/commonApi";
import {
  houseAdd,
  getHouseGraphJson,
  seachSingleHouseInfo,
  getAllHouseInfo,
  seachCAD,
  saveCAD,
  deleteCAD,
  downloadCAD,
  deletePropertyRight,
} from "./api";
import { eventBus } from "@/eventBus/eventBus";
import { mouseEvent } from "../../../static/js/common";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import saveBtn from "@/components/button/saveBtn";
import threeD from "@/components/threeD/three.vue";
import tableComponent from "@/components/table/table2.vue";
import Detector from "@/assets/js/webglDetector.js";
const $ = require("jquery");
const { fabric } = require("fabric");
const uuidv1 = require("uuid/v1");
export default {
  components: {
    cancelBtn,
    confirmBtn,
    saveBtn,
    threeD,
    tableComponent,
  },
  data() {
    return {
      PRO_TYPE: PRO_TYPE,
      canvas: null, // canvas对象
      mouseFrom: {}, // 起点坐标
      mouseTo: {}, // 终点坐标
      drawType: null, // 绘制类型
      textbox: null, // 文本框
      drawWidth: 1, // 笔触宽度
      color: "#333333", // 画笔颜色
      drawingObject: null, // 当前绘制对象
      moveCount: 1, // 绘制移动计数器
      doDrawing: false, // 绘制状态
      cell_width: 20, // 单元格的距离
      activeIconIndex: null,
      dialogTitle: "",
      dialogVisible: false,
      accessoryDialogVisible: false,
      isLeader: "1",
      oldIsLeader: "",
      unitIds: "",
      Form: {
        houseId: "",
        housinguseId: null,
        houseNumber: "",
        houseUseArea: null,
        housetypeId: null,
        houseTypeFlag: null,
        useType: "",
        isUse: 1,
        isIdle: 0,
        leadingOfficeVo: {
          useunitId: null,
          unitId: null,
          useRelationshipList: [],
          deleteLeadingOfficeIds: [],
          leadingOfficeList: [
            {
              duty: "",
              leadingofficeId: null,
              leadingofficeName: "",
              leadingofficeWorkstatue: null,
              leadingofficeWorkStatueId: null,
              professionalId: null,
              professionalLevel: null,
              professionOpts: [],
            },
          ],
        },
        housingUseVoList: [
          {
            useunitId: null,
            unitId: null,
            housinguseId: null,
            useRelationshipList: [],
            deleteLeadingOfficeIds: [],
            leadingOfficeList: [],
            useForm: null,
            distributionTime: null,
            departmentName: null,
          },
        ],
        deleteUseUnitIds: [],
      },
      oldFormData: null,
      contextMenuItems: {
        delete: {
          name: "删除",
          icon: "delete",
        },
      },
      contextMenuItems_2: {
        edit: {
          name: "编辑",
          icon: "edit",
        },
        delete: {
          name: "删除",
          icon: "delete",
        },
      },
      isRoom: false,
      isOldUse: null,
      AddHouseId: [], // 新增房屋id数组
      deleteHouseId: [], // 删除房屋id数组
      editHouseInfo: [], // 修改房屋信息数组
      existHouseId: [], // 已存在的房屋uuid
      existHouseInfo: [], // 已经存在的房屋信息
      originalHouseInfo: [], // 原始的房屋信息
      houseTypeColor: [
        {
          id: 1,
          color: "#21b124",
        },
        {
          id: 2,
          color: "#f4e83b",
        },
        {
          id: 3,
          color: "#008aff",
        },
        {
          id: 4,
          color: "#12fff1",
        },
        {
          id: 5,
          color: "#ff01fd",
        },
        {
          id: 6,
          color: "#fe8200",
        },
      ],
      houseType: [],
      useType: ["租赁", "调配"],
      unitOptions: [],
      profession: [],
      workStatue: [],
      baseSubmitFlag: true,
      unitSubmitFlag: true,
      professSubmitFlag: true,
      userNumSubmitFlag: true,
      workStatueNumSubmitFlag: true,
      nonLeaderSumbitFlag: true,
      dutyNumSubmitFlag: true,
      uploadCADUrl: `${uploadUrl}?uploadType=floor`,
      fileList: [],
      CADDownload: [],
      activeBtnIndex: 0,
      postHeaders: {
        Authorization: "",
      },
      token: "",
      text_H: 20,
      canvasHeight: 0,
      canvasWidth: 0,
      popoverDoorIcon: false,
      activeObject: null,
      oldHouseType: null,
      drawing_width_scale: 0,
      drawing_height_scale: 0,
      allNavList: null,
      copiedGraphData: null,
      activeList: [],
      startEditObj: null,
      textHeightSpace: 5,
      textWidthSpace: 16,
      isConfigFloor: null,
      configDialogVisible: false,
      configTableData: [],
      configCurrentPage: 1,
      configPageSize: 5,
      configTableTotal: 0,
      updateIdList: [],
      houseVoList: [],
      addHouseIdList: [],
      delHouseIdList: [],
      configureIdList: [],
      rentHouseIds: [],
      wall_coordinate: [], // 3d墙坐标
      configPX: {
        height: 1080,
        width: 1920,
      },
      panning: false,
      movementX: 0,
      movementY: 0,
      threeSacle: 2,
      showViewBtn: false,
      professionalLevelOpts: [
        {
          id: 3,
          name: "市级机关",
        },
        {
          id: 4,
          name: "县级机关",
        },
        {
          id: 5,
          name: "乡级机关",
        },
      ],
      loading: null,
      webglFlag: true,
      moveFlag: false,
      oldActiveIconIndex: null,
    };
  },
  methods: {
    professionalLevelChange($event, i) {
      this.Form.leadingOfficeVo.leadingOfficeList[i].professionalId = "";
      this.Form.leadingOfficeVo.leadingOfficeList[i].professionalNum = "";
      this.getAllProfessionList($event, i);
    },
    // 初始化
    init() {
      // 初始化canvas画布
      this.canvas = new fabric.Canvas("c", {
        skipTargetFind: true,
        selectable: false,
        selection: false,
      });
      fabric.ActiveSelection.prototype.hasControls =
        fabric.ActiveSelection.prototype.hasBorders = false;
      fabric.Object.prototype.originX = "left";
      fabric.Object.prototype.originY = "top";
      fabric.Object.prototype.transparentCorners = false;
      fabric.Object.prototype.borderColor = "red";
      fabric.Object.prototype.cornerColor = "green";
      fabric.Object.prototype.cornerSize = 10;
      fabric.Object.prototype.borderScaleFactor = 2;
      fabric.Object.prototype.hasRotatingPoint = false;
      window.canvas = this.canvas;
      // 设置画布比例
      this.setZoom();
      window.onresize = () => {
        this.setZoom();
      };
      // 设置画布公共事件
      this.setCommonEvent();
      // 初始化右键菜单
      $.contextMenu({
        selector: ".upper-canvas",
        trigger: "none",
        build: function (e, $trigger) {
          return $trigger.data("runCallbackThingie")();
        },
      });
      // 开始编辑
      if (this.startEditObj) {
        this.getHouseGraphJson(
          this.startEditObj.buildId,
          this.startEditObj.floorName,
          true
        );
        this.getAllHouseInfo(
          this.startEditObj.buildId,
          this.startEditObj.floorName
        );
        const buildId = sessionStorage.getItem("buildId");
        const floorName = sessionStorage.getItem("floorName");
        isUnionConfig(buildId, [floorName])
          .then((res) => {
            if (res) {
              this.isConfigFloor = true;
              this.$confirm(
                "该楼层房间关联了配置，如修改或删除房间信息，可能会还原分配方案。",
                "提示",
                {
                  confirmButtonText: "确定",
                  showCancelButton: false,
                  type: "warning",
                  center: true,
                }
              ).then(() => {});
            } else {
              this.isConfigFloor = false;
            }
          })
          .catch((err) => {
            const msg = err.result ? err.result : "是否关联配置获取失败";
            this.$message({
              message: msg,
              type: "error",
            });
          });
      }
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
      this.canvas.requestRenderAll();
    },
    // 设置画布公共事件
    setCommonEvent() {
      const self = this;
      this.canvas.on("mouse:down", function (options) {
        let textFlag = true;
        const objects = self.canvas.getObjects("textbox");
        if (objects && objects.length > 0) {
          for (let i = 0; i < objects.length; i++) {
            const object = objects[i];
            if (self.canvas.containsPoint(options.e, object)) {
              textFlag = false;
              break;
            }
          }
        }
        self.mouseFrom.x = options.e.offsetX;
        self.mouseFrom.y = options.e.offsetY;
        if (self.textbox && textFlag) {
          self.textbox.exitEditing();
          self.textbox = null;
        } else if (!self.textbox && textFlag) {
          if (self.drawType === "text") {
            self.textbox = new fabric.Textbox("", {
              left: self.mouseFrom.x,
              top: self.mouseFrom.y,
              width: 150,
              fontSize: 18,
              fill: self.color,
              moveTo: 10,
            });
            self.canvas.add(self.textbox);
            self.textbox.enterEditing();
          }
        }
        if (
          self.drawType === "line" ||
          self.drawType === "dottedline" ||
          self.drawType === "rectangle"
        ) {
          self.doDrawing = true;
          self.moveCount = 1;
          if (self.mouseFrom.x % self.cell_width >= self.cell_width / 2) {
            self.mouseFrom.x =
              Math.ceil(self.mouseFrom.x / self.cell_width) * self.cell_width -
              1;
          } else {
            self.mouseFrom.x =
              Math.floor(self.mouseFrom.x / self.cell_width) * self.cell_width -
              1;
          }
          if (self.mouseFrom.y % self.cell_width >= self.cell_width / 2) {
            self.mouseFrom.y =
              Math.ceil(self.mouseFrom.y / self.cell_width) * self.cell_width -
              1;
          } else {
            self.mouseFrom.y =
              Math.floor(self.mouseFrom.y / self.cell_width) * self.cell_width -
              1;
          }
        }
        if (!self.doDrawing && options.e.altKey) {
          self.panning = true;
          self.canvas.selection = false;
        }
      });

      this.canvas.on("mouse:move", function (options) {
        if (self.panning && options && options.e && !self.doDrawing) {
          self.movementX += options.e.movementX;
          self.movementY += options.e.movementY;
          const delta = new fabric.Point(
            options.e.movementX,
            options.e.movementY
          );
          self.canvas.relativePan(delta);
          return;
        }
        if (self.moveCount % 2 && !self.doDrawing) {
          return;
        }
        if (self.doDrawing) {
          self.mouseTo.x = options.e.offsetX;
          self.mouseTo.y = options.e.offsetY;
          if (self.drawType === "rectangle") {
            let distance_x = self.mouseTo.x - self.mouseFrom.x;
            let distance_y = self.mouseTo.y - self.mouseFrom.y;
            if (distance_x > 0 && distance_y > 0) {
              let multiple_w = Math.floor(distance_x / self.cell_width);
              let multiple_h = Math.floor(distance_y / self.cell_width);
              if (multiple_w < 1) {
                multiple_w = 1;
              }
              if (multiple_h < 1) {
                multiple_h = 1;
              }
              if (distance_x % self.cell_width >= self.cell_width / 2) {
                distance_x = (multiple_w + 1) * self.cell_width;
                self.drawing_width_scale = multiple_w + 1;
              } else {
                distance_x = multiple_w * self.cell_width;
                self.drawing_width_scale = multiple_w;
              }
              if (distance_y % self.cell_width >= self.cell_width / 2) {
                distance_y = (multiple_h + 1) * self.cell_width;
                self.drawing_height_scale = multiple_h + 1;
              } else {
                distance_y = multiple_h * self.cell_width;
                self.drawing_height_scale = multiple_h;
              }
              self.mouseTo.x = self.mouseFrom.x + distance_x;
              self.mouseTo.y = self.mouseFrom.y + distance_y;
              self.moveCount++;
              self.drawing();
            }
          } else {
            self.moveCount++;
            self.drawing();
          }
        }
      });

      this.canvas.on("mouse:up", function (options) {
        if (!self.doDrawing && self.panning) {
          self.panning = false;
          self.canvas.selection = false;
          let movementX = 0;
          let movementY = 0;
          if (self.movementX > 0) {
            movementX = self.cell_width - (self.movementX % self.cell_width);
          } else if (self.movementX < 0) {
            movementX = -(
              self.cell_width -
              (-self.movementX % self.cell_width)
            );
          }
          if (self.movementY > 0) {
            movementY = self.cell_width - (self.movementY % self.cell_width);
          } else {
            movementY = -(
              self.cell_width -
              (-self.movementY % self.cell_width)
            );
          }
          const delta = new fabric.Point(movementX, movementY);
          self.canvas.relativePan(delta);
          self.movementX = 0;
          self.movementY = 0;
        }
        // 移动图形时mouseup事件
        // 移动自动对齐格子
        let activeObj = self.canvas.getActiveObject();
        if (
          activeObj &&
          !self.doDrawing &&
          self.drawType === "move" &&
          self.moveFlag
        ) {
          const type = activeObj.type;
          if (type !== "textbox") {
            let left, top;
            let activeObjLeft = activeObj.left;
            let activeObjTop = activeObj.top;
            if (activeObjLeft >= 0 && activeObjTop >= 0) {
              if (activeObjLeft % self.cell_width >= self.cell_width / 2) {
                left =
                  Math.ceil(activeObjLeft / self.cell_width) * self.cell_width -
                  1;
              } else {
                left =
                  Math.floor(activeObjLeft / self.cell_width) *
                    self.cell_width -
                  1;
              }
              if (activeObjTop % self.cell_width >= self.cell_width / 2) {
                top =
                  Math.ceil(activeObjTop / self.cell_width) * self.cell_width -
                  1;
              } else {
                top =
                  Math.floor(activeObjTop / self.cell_width) * self.cell_width -
                  1;
              }
              if (type === "group" && activeObj.roomType === "path") {
                const oldLeft = activeObj.pathLeft;
                const oldTop = activeObj.pathTop;
                const uuid = activeObj.uuid;
                const xChange = left - oldLeft;
                const yChange = top - oldTop;
                let points = activeObj.pathPoint;
                self.canvas.remove(activeObj);
                for (let i = 0; i < points.length; i++) {
                  const point = points[i];
                  point[0] = point[0] + xChange;
                  point[1] = point[1] + yChange;
                }
                let path = "";
                for (let i = 0; i < points.length; i++) {
                  if (i === 0) {
                    path += "M " + points[i][0] + " " + points[i][1] + " ";
                  } else {
                    path += "L " + points[i][0] + " " + points[i][1] + " ";
                  }
                }
                path += "z";
                self.setPathGraph(activeObj, points, path);
              } else if (type === "line") {
                if (
                  activeObj.oldLeft &&
                  activeObj.oldTop &&
                  activeObj.oldCoord
                ) {
                  const oldLeft = activeObj.oldLeft;
                  const oldTop = activeObj.oldTop;
                  const xChange = left - oldLeft;
                  const yChange = top - oldTop;
                  activeObj.oldCoord.x1 = activeObj.oldCoord.x1 + xChange;
                  activeObj.oldCoord.x2 = activeObj.oldCoord.x2 + xChange;
                  activeObj.oldCoord.y1 = activeObj.oldCoord.y1 + yChange;
                  activeObj.oldCoord.y2 = activeObj.oldCoord.y2 + yChange;
                  activeObj.oldLeft = left;
                  activeObj.oldTop = top;
                }
                activeObj.set({
                  left: left,
                  top: top,
                });
                activeObj.setCoords();
              } else {
                activeObj.set({
                  left: left,
                  top: top,
                });
                activeObj.setCoords();
                self.canvas.requestRenderAll();
                if (type === "activeSelection") {
                  activeObj.forEachObject((obj) => {
                    if (obj.type === "group" && obj.roomType === "path") {
                      let m = activeObj.calcTransformMatrix();
                      let point = new fabric.Point(obj.left, obj.top);
                      let canvasPoint = fabric.util.transformPoint(point, m);
                      const objLeft = canvasPoint.x;
                      const objTop = canvasPoint.y;
                      if (objLeft >= 0 && objTop >= 0) {
                        const oldLeft = obj.pathLeft;
                        const oldTop = obj.pathTop;
                        const uuid = obj.uuid;
                        const xChange = objLeft - oldLeft;
                        const yChange = objTop - oldTop;
                        let points = obj.pathPoint;
                        self.canvas.remove(obj);
                        for (let i = 0; i < points.length; i++) {
                          const point = points[i];
                          point[0] = point[0] + xChange;
                          point[1] = point[1] + yChange;
                        }
                        let path = "";
                        for (let i = 0; i < points.length; i++) {
                          if (i === 0) {
                            path +=
                              "M " + points[i][0] + " " + points[i][1] + " ";
                          } else {
                            path +=
                              "L " + points[i][0] + " " + points[i][1] + " ";
                          }
                        }
                        path += "z";
                        self.setPathGraph(obj, points, path, activeObj);
                      }
                    } else if (obj.type === "line") {
                      let m = activeObj.calcTransformMatrix();
                      let point = new fabric.Point(obj.left, obj.top);
                      let canvasPoint = fabric.util.transformPoint(point, m);
                      const objLeft = canvasPoint.x;
                      const objTop = canvasPoint.y;
                      if (
                        objLeft >= 0 &&
                        objTop >= 0 &&
                        obj.oldLeft &&
                        obj.oldTop &&
                        obj.oldCoord
                      ) {
                        const oldLeft = obj.oldLeft;
                        const oldTop = obj.oldTop;
                        const xChange = objLeft - oldLeft;
                        const yChange = objTop - oldTop;
                        obj.oldCoord.x1 = obj.oldCoord.x1 + xChange;
                        obj.oldCoord.x2 = obj.oldCoord.x2 + xChange;
                        obj.oldCoord.y1 = obj.oldCoord.y1 + yChange;
                        obj.oldCoord.y2 = obj.oldCoord.y2 + yChange;
                        obj.oldLeft = objLeft;
                        obj.oldTop = objTop;
                      }
                    }
                  });
                }
              }
            }
          }
          self.moveFlag = false;
        }
        // 绘制图形时mouseup事件
        if (self.doDrawing) {
          self.canvas.discardActiveObject();
          self.moveCount = 1;
          self.doDrawing = false;
          self.drawing_width_scale = 0;
          self.drawing_height_scale = 0;
          self.mouseTo.x = options.e.offsetX;
          self.mouseTo.y = options.e.offsetY;
          if (self.drawingObject && self.drawingObject.type === "rect") {
            const w = self.drawingObject.width;
            const h = self.drawingObject.height;
            self.canvas.remove(self.drawingObject);
            let rect = new fabric.Rect({
              left: self.mouseFrom.x,
              top: self.mouseFrom.y,
              stroke: self.color,
              strokeWidth: self.drawWidth,
              fill: "rgba(204, 204, 204, 1)",
              width: w,
              height: h,
            });
            let group = new fabric.Group([rect]);
            group.toObject = (function (toObject) {
              return function () {
                return fabric.util.object.extend(toObject.call(this), {
                  uuid: this.uuid,
                  isUse: this.isUse,
                  typeName: this.typeName,
                });
              };
            })(group.toObject);
            self.canvas.add(group);
            const uuid = uuidv1();
            group.uuid = uuid;
            group.typeName = "room";
            self.canvas.requestRenderAll();
            self.AddHouseId.push(uuid);
          }
          if (
            self.drawingObject &&
            (self.drawingObject.type === "line" ||
              self.drawingObject.type === "dottedline")
          ) {
            let x1 = self.mouseFrom.x;
            let x2 = self.mouseTo.x;
            let y1 = self.mouseFrom.y;
            let y2 = self.mouseTo.y;
            let multiple_w = Math.floor(x2 / self.cell_width);
            let multiple_h = Math.floor(y2 / self.cell_width);
            if (x2 % self.cell_width >= self.cell_width / 2) {
              x2 = (multiple_w + 1) * self.cell_width - 1;
            } else {
              x2 = multiple_w * self.cell_width - 1;
            }
            if (y2 % self.cell_width >= self.cell_width / 2) {
              y2 = (multiple_h + 1) * self.cell_width - 1;
            } else {
              y2 = multiple_h * self.cell_width - 1;
            }
            self.drawingObject.set({
              x2: x2,
              y2: y2,
            });
            self.drawingObject.setCoords();
            self.drawingObject.toObject = (function (toObject) {
              return function () {
                return fabric.util.object.extend(toObject.call(this), {
                  oldLeft: this.oldLeft,
                  oldTop: this.oldTop,
                  oldCoord: this.oldCoord,
                });
              };
            })(self.drawingObject.toObject);
            const oldCoord = {
              x1: x1,
              y1: y1,
              x2: x2,
              y2: y2,
            };
            self.drawingObject.oldLeft = self.drawingObject.left;
            self.drawingObject.oldTop = self.drawingObject.top;
            self.drawingObject.oldCoord = oldCoord;
            self.canvas.requestRenderAll();
          }
          self.drawingObject = null;
        }
      });

      this.canvas.on("mouse:dblclick", function (options) {
        if (
          self.drawType !== "line" &&
          self.drawType !== "dottedline" &&
          self.drawType !== "rectangle"
        ) {
          const objects = self.canvas.getObjects("textbox");
          if (objects && objects.length > 0) {
            for (let i = 0; i < objects.length; i++) {
              const object = objects[i];
              if (self.canvas.containsPoint(options.e, object)) {
                self.textbox = object;
                object.enterEditing();
              }
            }
          }
        }
      });

      this.canvas.on("object:moving", function (e) {
        if (e.target.angle === 0) {
          e.target.opacity = 0.7;
          if (e.target.left + e.target.width >= self.canvasWidth) {
            e.target.left = self.canvasWidth - e.target.width;
          }
          if (e.target.top + e.target.height >= self.canvasHeight) {
            e.target.top = self.canvasHeight - e.target.height;
          }
          if (e.target.left <= 0) {
            e.target.left = 0;
          }
          if (e.target.top <= 0) {
            e.target.top = 0;
          }
        }
      });

      this.canvas.on("object:moved", function (e) {
        e.target.opacity = 0.7;
        self.moveFlag = true;
      });

      this.canvas.on("object:modified", function (e) {
        e.target.opacity = 1;
      });

      this.canvas.on("object:scaling", function (e) {
        if (
          e.target.type === "group" &&
          e.target.typeName === "room" &&
          e.target.angle === 0
        ) {
          const target = e.target;
          let scale_w = target.width * target.scaleX;
          let scale_h = target.height * target.scaleY;
          let multiple_w = Math.floor(scale_w / self.cell_width);
          let multiple_h = Math.floor(scale_h / self.cell_width);
          if (scale_w % self.cell_width >= self.cell_width / 2) {
            self.drawing_width_scale = multiple_w + 1;
          } else {
            self.drawing_width_scale = multiple_w;
          }
          if (scale_h % self.cell_width >= self.cell_width / 2) {
            self.drawing_height_scale = multiple_h + 1;
          } else {
            self.drawing_height_scale = multiple_h;
          }
          self.canvas.requestRenderAll();
        }
      });

      this.canvas.on("object:scaled", function (e) {
        if (
          e.target.type === "group" &&
          e.target.typeName === "room" &&
          e.target.angle === 0
        ) {
          const target = e.target;
          const original_w = target.width;
          const original_h = target.height;
          let scale_w = target.width * target.scaleX;
          let scale_h = target.height * target.scaleY;
          let multiple_w = Math.floor(scale_w / self.cell_width);
          let multiple_h = Math.floor(scale_h / self.cell_width);
          if (scale_w % self.cell_width >= self.cell_width / 2) {
            scale_w = (multiple_w + 1) * self.cell_width + 1;
          } else {
            scale_w = multiple_w * self.cell_width + 1;
          }
          if (scale_h % self.cell_width >= self.cell_width / 2) {
            scale_h = (multiple_h + 1) * self.cell_width + 1;
          } else {
            scale_h = multiple_h * self.cell_width + 1;
          }
          const newScaleW = scale_w / original_w;
          const newScaleH = scale_h / original_h;
          target.set({
            scaleX: newScaleW,
            scaleY: newScaleH,
          });
          target.setCoords();
          self.canvas.requestRenderAll();
        }
        self.drawing_width_scale = 0;
        self.drawing_height_scale = 0;
      });
    },
    // 生成path不规则图形
    setPathGraph(activeObj, points, path, activeSelection) {
      let houseUseAreaVal = "";
      let houseNumberVal = "";
      let housetypeId = "";
      const uuid = activeObj.uuid;
      this.existHouseInfo.forEach((element) => {
        if (element.houseId === uuid) {
          houseUseAreaVal = element.houseUseArea || "";
          houseNumberVal = element.houseNumber || "";
          housetypeId = element.housetypeId || null;
        }
      });
      let pathObj = new fabric.Path(path, {
        stroke: this.color,
        strokeWidth: this.drawWidth,
        fill: "rgba(204, 204, 204, 1)",
      });
      if (housetypeId) {
        // 房间类型颜色
        this.houseTypeColor.forEach((element) => {
          if (element.id === housetypeId) {
            pathObj.set("fill", element.color);
          }
        });
      }
      let group = new fabric.Group([pathObj], {
        hasControls: false,
      });
      group.toObject = (function (toObject) {
        return function () {
          return fabric.util.object.extend(toObject.call(this), {
            uuid: this.uuid,
            isUse: this.isUse,
            typeName: this.typeName,
            pathLeft: this.pathLeft,
            pathTop: this.pathTop,
            pathPoint: this.pathPoint,
            roomType: this.roomType,
          });
        };
      })(group.toObject);
      this.canvas.add(group);
      group.uuid = uuid;
      group.typeName = "room";
      group.pathLeft = group.left;
      group.pathTop = group.top;
      group.pathPoint = points;
      group.roomType = "path";
      if (houseUseAreaVal && houseNumberVal) {
        // 房间信息标注
        // 得到编辑时的数据
        // 房间面积
        let group_w = group.width;
        let group_H = group.height;
        const houseUseArea = new fabric.Text(`${houseUseAreaVal}m²`, {
          strokeWidth: 1,
          originX: "center",
          originY: "center",
          fontSize: 20,
        });
        let area_text_w = houseUseArea.width;
        let area_text_H = houseUseArea.height;
        let area_scale_w = 1;
        let area_scale_y = 1;
        if (area_text_w + this.textWidthSpace * 2 > group_w) {
          const w = group_w - this.textWidthSpace * 2;
          houseUseArea.scaleToWidth(w);
          area_scale_w = houseUseArea.scaleX;
        }
        if (area_text_H * 3 + this.textHeightSpace * 2 > group_H) {
          const h = (group_H - this.textHeightSpace * 2) / 3;
          houseUseArea.scaleToHeight(h);
          area_scale_y = houseUseArea.scaleY;
        }
        // 防止字体变形
        if (area_scale_w > area_scale_y) {
          houseUseArea.set({
            scaleX: area_scale_y,
            scaleY: area_scale_y,
          });
        } else if (area_scale_w < area_scale_y) {
          houseUseArea.set({
            scaleX: area_scale_w,
            scaleY: area_scale_w,
          });
        }
        houseUseArea.toObject = (function (toObject) {
          return function () {
            return fabric.util.object.extend(toObject.call(this), {
              typeName: this.typeName,
            });
          };
        })(houseUseArea.toObject);
        group.add(houseUseArea);
        houseUseArea.typeName = "houseUseArea";

        // 房间号
        const houseNumber = new fabric.Text(`${houseNumberVal}`, {
          strokeWidth: 1,
          originX: "center",
          originY: "center",
          fontSize: 20,
        });
        let num_text_w = houseNumber.width;
        let num_text_H = houseNumber.height;
        let num_scale_w = 1;
        let num_scale_y = 1;
        if (num_text_w + this.textWidthSpace * 2 > group_w) {
          const w = group_w - this.textWidthSpace * 2;
          houseNumber.scaleToWidth(w);
          num_scale_w = houseNumber.scaleX;
        }
        if (num_text_H * 3 + this.textHeightSpace * 2 > group_H) {
          const h = (group_H - this.textHeightSpace * 2) / 3;
          houseNumber.scaleToHeight(h);
          num_scale_y = houseNumber.scaleY;
        }

        // 字体设置成相同的比例
        let minScale;
        let minHeight;
        if (num_scale_w > area_scale_w) {
          minScale = area_scale_w;
          minHeight = houseUseArea.height;
        } else {
          minScale = num_scale_w;
          minHeight = houseNumber.height;
          houseUseArea.set({
            scaleX: minScale,
            scaleY: minScale,
          });
        }
        let top = Math.round(
          -(minHeight * minScale) - this.textHeightSpace / 2
        );
        const _houseNumber = new fabric.Text(`${houseNumberVal}`, {
          strokeWidth: 1,
          originX: "center",
          originY: "center",
          fontSize: 20,
          scaleX: minScale,
          scaleY: minScale,
          top: top,
        });
        _houseNumber.toObject = (function (toObject) {
          return function () {
            return fabric.util.object.extend(toObject.call(this), {
              typeName: this.typeName,
            });
          };
        })(_houseNumber.toObject);
        group.add(_houseNumber);
        _houseNumber.typeName = "houseNumber";
      }
      if (activeSelection) {
        activeSelection.addWithUpdate(group);
      } else {
        this.canvas.setActiveObject(group);
      }
      this.canvas.requestRenderAll();
    },
    // 切换工具
    tool(type, num) {
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      if (!buildId || !floorName) {
        this.popoverDoorIcon = false;
        this.$message({
          message: "请先选择楼层",
          type: "warning",
        });
        return false;
      }
      this.activeIconIndex = num;
      this.drawType = type;
      this.canvas.selectable = false;
      this.canvas.selection = false;
      this.canvas.skipTargetFind = true;
      this.doDrawing = false;
      if (this.textbox) {
        //退出文本编辑状态
        this.textbox.exitEditing();
        this.textbox = null;
      }
      if (this.drawType === "move") {
        this.canvas.selectable = true;
        this.canvas.selection = true;
        this.canvas.skipTargetFind = false;
      }
      this.canvas.discardActiveObject();
    },
    // 绘制图形
    drawing() {
      if (this.drawingObject) {
        this.canvas.remove(this.drawingObject);
      }
      let canvasObject = null;
      switch (this.drawType) {
        case "line": // 直线
          canvasObject = new fabric.Line(
            [
              this.mouseFrom.x,
              this.mouseFrom.y,
              this.mouseTo.x,
              this.mouseTo.y,
            ],
            {
              stroke: this.color,
              strokeWidth: this.drawWidth,
              hasControls: false,
            }
          );
          break;
        case "dottedline": // 虚线
          canvasObject = new fabric.Line(
            [
              this.mouseFrom.x,
              this.mouseFrom.y,
              this.mouseTo.x,
              this.mouseTo.y,
            ],
            {
              strokeDashArray: [3, 1],
              stroke: this.color,
              strokeWidth: this.drawWidth,
              hasControls: false,
            }
          );
          break;
        case "rectangle": // 方形
          const left = this.mouseFrom.x;
          const top = this.mouseFrom.y;
          const width = this.mouseTo.x - this.mouseFrom.x;
          const height = this.mouseTo.y - this.mouseFrom.y;
          canvasObject = new fabric.Rect({
            left: left,
            top: top,
            stroke: "#ff0000",
            strokeWidth: this.drawWidth,
            fill: "rgba(5, 127, 231, 0.2)",
            width: width,
            height: height,
          });
          break;
      }
      if (canvasObject) {
        this.canvas.add(canvasObject);
        this.drawingObject = canvasObject;
        this.canvas.requestRenderAll();
      }
    },
    // 添加门
    addDoor(type) {
      const self = this;
      this.popoverDoorIcon = false;
      fabric.loadSVGFromURL(
        require(`../../assets/imgs/${type}-canvas.svg`),
        (objects, options) => {
          let obj = fabric.util.groupSVGElements(objects, options);
          obj.set({
            left: 59,
            top: 240,
            fill: "rgb(255,255,255)",
          });
          obj.toObject = (function (toObject) {
            return function () {
              return fabric.util.object.extend(toObject.call(this), {
                typeName: this.typeName,
              });
            };
          })(obj.toObject);
          obj.typeName = type;
          this.canvas.add(obj);
          this.canvas.requestRenderAll();
          this.tool("move", 0);
        }
      );
    },
    // 获取下拉选项
    getOptionsList() {
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
      getUserUnitList()
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
    getAllProfessionList(professionType, i) {
      getAllProfessionList(professionType, 0)
        .then((res) => {
          this.$set(
            this.Form.leadingOfficeVo.leadingOfficeList[i],
            "professionOpts",
            res
          );
        })
        .catch((err) => {
          const msg = err.result ? err.result : "职称获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    // 显示右键菜单
    showContextMenu($event) {
      if ($event.which === 3) {
        // 右键
        const $this = $(".upper-canvas");
        this.activeObject = null;
        this.activeList = this.canvas.getActiveObjects();
        this.isRoom = false;
        if (this.activeList.length > 1) {
          $this.data("runCallbackThingie", this.createSomeMenu);
          const position = {
            x: $event.clientX,
            y: $event.clientY,
          };
          $this.contextMenu(position);
        } else if (this.activeList.length === 1) {
          if (this.canvas.containsPoint($event, this.activeList[0])) {
            if (
              this.activeList[0].type === "group" &&
              this.activeList[0].typeName === "room"
            ) {
              this.isRoom = true;
              this.activeObject = this.activeList[0];
            } else {
              this.isRoom = false;
            }
          }
          $this.data("runCallbackThingie", this.createSomeMenu);
          const position = {
            x: $event.clientX,
            y: $event.clientY,
          };
          $this.contextMenu(position);
        } else if (this.activeList.length === 0) {
          const canvasObjects = this.canvas.getObjects();
          if (canvasObjects && canvasObjects.length > 0) {
            for (let i = 0; i < canvasObjects.length; i++) {
              if (this.canvas.containsPoint($event, canvasObjects[i])) {
                this.activeObject = canvasObjects[i];
                if (
                  this.activeObject.type === "group" &&
                  this.activeObject.typeName === "room"
                ) {
                  this.isRoom = true;
                } else {
                  this.isRoom = false;
                }
                $this.data("runCallbackThingie", this.createSomeMenu);
                const position = {
                  x: $event.clientX,
                  y: $event.clientY,
                };
                $this.contextMenu(position);
                return false;
              }
            }
          }
        }
      }
    },
    // 创建菜单项
    createSomeMenu() {
      let contextMenu = null;
      if (this.isRoom) {
        contextMenu = this.contextMenuItems_2;
      } else {
        contextMenu = this.contextMenuItems;
      }
      return {
        callback: this.contextMenuClick,
        items: contextMenu,
      };
    },
    // 点击右键菜单
    contextMenuClick(options, key) {
      if (key === "delete") {
        if (this.activeList.length > 0) {
          let deleteFlag = true;
          this.activeList.forEach((item) => {
            if (item.type === "group" && item.typeName === "room") {
              for (let i = 0; i < this.originalHouseInfo.length; i++) {
                const info = this.originalHouseInfo[i];
                if (info.houseId === item.uuid) {
                  item.actualDeleteflag = true;
                  if (info.rent !== undefined) {
                    this.$message({
                      message: "已经租借的房间不能删除",
                      type: "warning",
                    });
                    deleteFlag = false;
                    return false;
                  }
                  if (info.repairModuleId !== undefined) {
                    this.$message({
                      message: "关联维修的房间不能再次删除",
                      type: "warning",
                    });
                    deleteFlag = false;
                    return false;
                  }
                  if (
                    info.configurePlanDataId !== undefined ||
                    info.configurePlanDataId
                  ) {
                    this.$message({
                      message: "关联配置的房间不能删除",
                      type: "warning",
                    });
                    deleteFlag = false;
                    return false;
                  }
                }
              }
            }
          });
          if (deleteFlag) {
            this.activeList.forEach((item) => {
              if (item.actualDeleteflag) {
                this.deleteHouseId.push(item.uuid);
              }
              for (let i = 0; i < this.AddHouseId.length; i++) {
                if (this.AddHouseId[i] === item.uuid) {
                  this.AddHouseId.splice(i, 1);
                  break;
                }
              }
              for (let i = 0; i < this.editHouseInfo.length; i++) {
                if (this.editHouseInfo[i].houseId === item.uuid) {
                  this.editHouseInfo.splice(i, 1);
                  break;
                }
              }
              this.canvas.remove(item);
            });
          }
          this.canvas.discardActiveObject();
          this.canvas.requestRenderAll();
          this.activeList = [];
        } else {
          let deleteFlag = true;
          let actualDeleteflag = false;
          for (let i = 0; i < this.originalHouseInfo.length; i++) {
            const info = this.originalHouseInfo[i];
            if (info.houseId === this.activeObject.uuid) {
              actualDeleteflag = true;
              if (info.rent !== undefined) {
                this.$message({
                  message: "已经租借的房间不能删除",
                  type: "warning",
                });
                deleteFlag = false;
                return false;
              }
              if (info.repairModuleId !== undefined) {
                this.$message({
                  message: "关联维修的房间不能再次删除",
                  type: "warning",
                });
                deleteFlag = false;
                return false;
              }
              if (
                info.configurePlanDataId !== undefined ||
                info.configurePlanDataId
              ) {
                this.$message({
                  message: "关联配置的房间不能删除",
                  type: "warning",
                });
                deleteFlag = false;
                return false;
              }
            }
          }
          if (deleteFlag) {
            if (actualDeleteflag) {
              this.deleteHouseId.push(this.activeObject.uuid);
            }
            for (let i = 0; i < this.AddHouseId.length; i++) {
              if (this.AddHouseId[i] === this.activeObject.uuid) {
                this.AddHouseId.splice(i, 1);
                break;
              }
            }
            for (let i = 0; i < this.editHouseInfo.length; i++) {
              if (this.editHouseInfo[i].houseId === this.activeObject.uuid) {
                this.editHouseInfo.splice(i, 1);
                break;
              }
            }
            this.canvas.remove(this.activeObject);
            this.canvas.discardActiveObject();
            this.canvas.requestRenderAll();
          }
        }
      } else if (key === "edit") {
        let roomOldDataflag = false; // 判断该房间是否存在原始数据
        this.originalHouseInfo.forEach((element) => {
          if (element.houseId === this.activeObject.uuid) {
            roomOldDataflag = true;
            if (element.housetypeId === 1 && element.isUse === 1) {
              if (
                element.housingUseVoList &&
                element.housingUseVoList.length > 0 &&
                element.housingUseVoList[0].unitId !== null &&
                element.housingUseVoList[0].unitId !== ""
              ) {
                this.oldIsLeader = "2";
              } else if (
                element.leadingOfficeVo &&
                element.leadingOfficeVo.unitId !== null &&
                element.leadingOfficeVo.unitId !== ""
              ) {
                this.oldIsLeader = "1";
              }
            } else {
              this.oldIsLeader = null;
            }
            if (element.isUse === 1) {
              this.isOldUse = 1;
            } else {
              this.isOldUse = 0;
            }
            this.oldHouseType = element.houseTypeFlag;
            this.oldFormData = JSON.parse(JSON.stringify(element));
          }
        });
        if (!roomOldDataflag) {
          this.oldHouseType = null;
          this.oldIsLeader = null;
          this.isOldUse = null;
        }
        // 得到编辑时的数据
        this.existHouseInfo.forEach((element) => {
          if (element.houseId === this.activeObject.uuid) {
            if (element.housetypeId === 1 && element.isUse === 1) {
              if (
                element.housingUseVoList &&
                element.housingUseVoList.length > 0 &&
                element.housingUseVoList[0].unitId !== null &&
                element.housingUseVoList[0].unitId !== ""
              ) {
                this.isLeader = "2";
              } else if (
                element.leadingOfficeVo &&
                element.leadingOfficeVo.unitId !== null &&
                element.leadingOfficeVo.unitId !== ""
              ) {
                this.isLeader = "1";
              }
            } else if (element.housetypeId !== 1 && element.isUse === 1) {
              this.unitIds = element.unitIds[0];
            }

            const cloneObj = JSON.parse(JSON.stringify(element));
            console.log(cloneObj);
            $.extend(this.Form, cloneObj);
          }
        });
        this.Form.houseId = this.activeObject.uuid;
        if (this.Form.rent) {
          this.Form.rent.rentBegin = this.formatDate(this.Form.rent.rentBegin);
          this.Form.rent.rentEnd = this.formatDate(this.Form.rent.rentEnd);
        }
        if (
          this.Form.housetypeId === 1 &&
          this.Form.isUse === 1 &&
          this.Form.leadingOfficeVo &&
          this.Form.leadingOfficeVo.unitId !== null &&
          this.Form.leadingOfficeVo.unitId !== ""
        ) {
          console.log(this.Form);
          this.Form.leadingOfficeVo.leadingOfficeList.forEach((item) => {
            getAllProfessionList(item.professionalLevel, 0)
              .then((res) => {
                this.$set(item, "professionOpts", res);
              })
              .catch((err) => {
                const msg = err.result ? err.result : "职称获取失败";
                this.$message({
                  message: msg,
                  type: "error",
                });
              });
          });
        }
        this.dialogVisible = true;
      }
    },
    // 提交房间信息
    submitForm() {
      if (this.Form.isIdle === 1) {
        this.Form.isUse = 0;
      }
      // 验证判断
      // 房间号，房间类型，使用面积不能为空
      let nullArr = null;
      if (this.Form.houseTypeFlag !== 1 && this.Form.isUse === 1) {
        nullArr = ["houseNumber", "houseUseArea", "housetypeId", "unitIds"];
      } else {
        nullArr = ["houseNumber", "houseUseArea", "housetypeId"];
      }
      this.verificationNull(nullArr);
      if (this.Form.isUse === 1 && this.isLeader === "1") {
        let useRelationshipNum = 0;
        let dutyNum = 0;
        let professNum = 0;
        let userNum = 0;
        let workStatueNum = 0;
        if (
          this.Form.leadingOfficeVo.unitId === null ||
          this.Form.leadingOfficeVo.unitId === ""
        ) {
          const unitNameBox = $(".leader-info").find(".content-item-unitName");
          const verifyBox = $(unitNameBox).find(".el-form-item__error");
          if (verifyBox.length === 0) {
            const labelText = $(unitNameBox).find("label").text();
            const tips = $(
              `<div class="el-form-item__error">${labelText}不能为空</div>`
            );
            $(unitNameBox).append(tips);
          }
          this.unitSubmitFlag = false;
        } else {
          const unitNameBox = $(".leader-info").find(".content-item-unitName");
          const verifyBox = $(unitNameBox).find(".el-form-item__error");
          if (verifyBox) {
            verifyBox.remove();
          }
          this.unitSubmitFlag = true;
        }
        this.Form.leadingOfficeVo.leadingOfficeList.forEach((item, index) => {
          useRelationshipNum++;
          const unitMainBox = $(".leader-info").find(".unit-info-main");
          const group = unitMainBox.find(".content-item-group")[index];
          const dutyBox = $(group).find(".group-item")[1];
          const professBox = $(group).find(".group-item")[2];
          const userBox = $(group).find(".group-item")[3];
          const workStatueBox = $(group).find(".group-item")[4];
          if (item.professionalId === "" || item.professionalId === null) {
            const verifyBox = $(professBox).find(".el-form-item__error");
            if (verifyBox.length === 0) {
              const labelText = $(professBox).find("label").text();
              const tips = $(
                `<div class="el-form-item__error">${labelText}不能为空</div>`
              );
              $(professBox).append(tips);
            }
            this.professSubmitFlag = false;
          } else {
            professNum++;
            const verifyBox = $(professBox).find(".el-form-item__error");
            if (verifyBox) {
              verifyBox.remove();
            }
          }
          if (
            item.leadingofficeName === "" ||
            item.leadingofficeName === null
          ) {
            const verifyBox = $(userBox).find(".el-form-item__error");
            if (verifyBox.length === 0) {
              const labelText = $(userBox).find("label").text();
              const tips = $(
                `<div class="el-form-item__error">${labelText}不能为空</div>`
              );
              $(userBox).append(tips);
            }
            this.userNumSubmitFlag = false;
          } else {
            userNum++;
            const verifyBox = $(userBox).find(".el-form-item__error");
            if (verifyBox) {
              verifyBox.remove();
            }
          }
          if (
            item.leadingofficeWorkStatueId === "" ||
            item.leadingofficeWorkStatueId === null
          ) {
            const verifyBox = $(workStatueBox).find(".el-form-item__error");
            if (verifyBox.length === 0) {
              const labelText = $(workStatueBox).find("label").text();
              const tips = $(
                `<div class="el-form-item__error">${labelText}不能为空</div>`
              );
              $(workStatueBox).append(tips);
            }
            this.workStatueNumSubmitFlag = false;
          } else {
            workStatueNum++;
            const verifyBox = $(workStatueBox).find(".el-form-item__error");
            if (verifyBox) {
              verifyBox.remove();
            }
          }
          if (item.duty === "" || item.duty === null) {
            const verifyBox = $(dutyBox).find(".el-form-item__error");
            if (verifyBox.length === 0) {
              const labelText = $(dutyBox).find("label").text();
              const tips = $(
                `<div class="el-form-item__error">${labelText}不能为空</div>`
              );
              $(dutyBox).append(tips);
            }
            this.dutyNumSubmitFlag = false;
          } else {
            dutyNum++;
            const verifyBox = $(dutyBox).find(".el-form-item__error");
            if (verifyBox) {
              verifyBox.remove();
            }
          }
        });
        if (professNum === useRelationshipNum) {
          this.professSubmitFlag = true;
        }
        if (userNum === useRelationshipNum) {
          this.userNumSubmitFlag = true;
        }
        if (workStatueNum === useRelationshipNum) {
          this.workStatueNumSubmitFlag = true;
        }
        if (dutyNum === useRelationshipNum) {
          this.dutyNumSubmitFlag = true;
        }
      } else if (this.Form.isUse === 1 && this.isLeader === "2") {
        if (
          this.Form.housingUseVoList[0].unitId === "" ||
          this.Form.housingUseVoList[0].unitId === null
        ) {
          const outerBox = $(".unit-info").find(".unitId");
          const verifyBox = $(outerBox).find(".el-form-item__error");
          if (verifyBox.length === 0) {
            const labelText = $(outerBox).find("label").text();
            const tips = $(
              `<div class="el-form-item__error">${labelText}不能为空</div>`
            );
            $(outerBox).append(tips);
          }
          this.nonLeaderSumbitFlag = false;
        } else {
          this.nonLeaderSumbitFlag = true;
        }
      }

      // 保存数据到本地
      if (
        this.Form.isUse === 1 &&
        this.isLeader === "1" &&
        this.baseSubmitFlag &&
        this.professSubmitFlag &&
        this.userNumSubmitFlag &&
        this.workStatueNumSubmitFlag &&
        this.unitSubmitFlag &&
        this.Form.houseTypeFlag === 1
      ) {
        if (this.Form.unitIds && this.Form.unitIds.length > 0) {
          delete this.Form.unitIds;
        }
        // 保存数据到本地
        const obj = JSON.parse(JSON.stringify(this.Form));
        let flagInfo = true;
        if (this.existHouseInfo.length > 0) {
          this.existHouseInfo.forEach((element, index) => {
            if (element.houseId === obj.houseId) {
              this.existHouseInfo.splice(index, 1, obj);
              flagInfo = false;
            }
          });
        }
        if (flagInfo) this.existHouseInfo.push(obj);

        // 保存数据到服务器
        const obj_2 = JSON.parse(JSON.stringify(this.Form));
        // 如果是租借的房间，要做退租处理
        if (obj_2.rent) {
          if (this.rentHouseIds.indexOf(obj_2.houseId) === -1) {
            this.rentHouseIds.push(obj_2.houseId);
          }
        }
        delete obj_2.rent;
        // 字符串转化为数字
        obj_2.houseUseArea = obj_2.houseUseArea - 0;
        // 获取在职情况
        for (
          let i = 0;
          i < obj_2.leadingOfficeVo.leadingOfficeList.length;
          i++
        ) {
          const item = obj_2.leadingOfficeVo.leadingOfficeList[i];
          this.workStatue.forEach((element) => {
            if (item.leadingofficeWorkStatueId === element.key) {
              item.leadingofficeWorkstatue = element.value;
            }
          });
        }
        // 填充useRelationshipList的内容
        let map = {};
        let oldUseRelationshipList = [];
        let newUseRelationshipList = [];
        let oldUseRelationshipListId = [];
        let newUseRelationshipListId = [];
        let currentUseRelationshipList = [];
        for (
          let j = 0;
          j < obj_2.leadingOfficeVo.leadingOfficeList.length;
          j++
        ) {
          const item = obj_2.leadingOfficeVo.leadingOfficeList[j];
          if (!map[item.professionalId]) {
            let tempArr = [];
            tempArr.push(item);
            map[item.professionalId] = tempArr;
          } else {
            map[item.professionalId].push(item);
          }
        }
        for (let key in map) {
          const tempObj = {
            jobId: null,
            professionalId: parseInt(key),
            roomNum: map[key].length,
          };
          newUseRelationshipList.push(tempObj);
          newUseRelationshipListId.push(parseInt(key));
        }
        if (
          obj_2.leadingOfficeVo.useRelationshipList !== undefined &&
          obj_2.leadingOfficeVo.useRelationshipList.length > 0
        ) {
          oldUseRelationshipList = JSON.parse(
            JSON.stringify(obj_2.leadingOfficeVo.useRelationshipList)
          );
          oldUseRelationshipList.forEach((oldEle) => {
            oldUseRelationshipListId.push(oldEle.professionalId);
            if (
              newUseRelationshipListId.indexOf(oldEle.professionalId) === -1
            ) {
              oldEle.roomNum = 0;
              currentUseRelationshipList.push(oldEle);
            } else {
              const index = newUseRelationshipListId.indexOf(
                oldEle.professionalId
              );
              const obj = newUseRelationshipList[index];
              obj.jobId = oldEle.jobId;
              currentUseRelationshipList.push(obj);
            }
          });
          newUseRelationshipList.forEach((newEle) => {
            if (
              oldUseRelationshipListId.indexOf(newEle.professionalId) === -1
            ) {
              currentUseRelationshipList.push(newEle);
            }
          });
          obj_2.leadingOfficeVo.useRelationshipList =
            currentUseRelationshipList;
        } else {
          obj_2.leadingOfficeVo.useRelationshipList = newUseRelationshipList;
        }
        // 非领导和领导之间转换，解除关联关系。
        if (
          this.isOldUse === 1 &&
          this.oldHouseType === 1 &&
          this.oldIsLeader !== this.isLeader
        ) {
          if (obj_2.deleteUseUnitIds === undefined) {
            obj_2.deleteUseUnitIds = [];
          }
          obj_2.deleteUseUnitIds.push(obj_2.housingUseVoList[0].useunitId);
        } else if (this.isOldUse === 1 && this.oldHouseType !== 1) {
          if (obj_2.deleteUseUnitIds === undefined) {
            obj_2.deleteUseUnitIds = [];
          }
          obj_2.housingUseUnitList.forEach((item) => {
            obj_2.deleteUseUnitIds.push(item.useunitId);
          });
          let deleteObj = {};
          deleteObj.useRelationshipList = [];
          obj_2.housingUseVoListClone.forEach((item) => {
            const useRelationshipObj = {
              jobId: item.useRelationshipList[0].jobId,
              roomNum: 0,
            };
            deleteObj.useRelationshipList.push(useRelationshipObj);
          });
          obj_2.housingUseVoList.push(deleteObj);
        }
        if (obj_2.housingUseVoList !== undefined) delete obj_2.housingUseVoList;
        if (obj_2.housingUseUnitList !== undefined)
          delete obj_2.housingUseUnitList;
        if (obj_2.housingUseVoListClone !== undefined)
          delete obj_2.housingUseVoListClone;

        // 判断该房间数据是否已经编辑过
        let flag = true;
        if (this.editHouseInfo.length > 0) {
          this.editHouseInfo.forEach((element, index) => {
            if (element.houseId === obj_2.houseId) {
              this.editHouseInfo.splice(index, 1, obj_2);
              flag = false;
            }
          });
        }
        // 未使用的房间加上标识符
        this.activeObject.isUse = true;
        // 房间类型颜色
        this.houseTypeColor.forEach((element) => {
          if (element.id === this.Form.housetypeId) {
            const rect = this.activeObject.getObjects("rect");
            if (rect.length > 0) {
              rect[0].set("fill", element.color);
            } else {
              const path = this.activeObject.getObjects("path");
              path[0].set("fill", element.color);
            }
            this.canvas.requestRenderAll();
          }
        });
        // 房间信息标注
        const existedText = this.activeObject.getObjects("text");
        let houseUseAreaAddFlag = true;
        let houseNumberAddFlag = true;
        if (existedText.length > 0) {
          existedText.forEach((text) => {
            // 房间使用面积
            if (text.typeName === "houseUseArea") {
              houseUseAreaAddFlag = false;
              text.set({
                text: `${this.Form.houseUseArea}m²`,
                scaleX: 1,
                scaleY: 1,
              });
              let text_w = text.width;
              let text_H = text.height;
              let group_w = this.activeObject.width;
              let group_H = this.activeObject.height;
              let scale_w = 1;
              let scale_y = 1;
              if (text_w + this.textWidthSpace * 2 > group_w) {
                const w = group_w - this.textWidthSpace * 2;
                text.scaleToWidth(w);
                scale_w = text.scaleX;
              }
              if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
                const h = (group_H - this.textHeightSpace * 2) / 3;
                text.scaleToHeight(h);
                scale_y = text.scaleY;
              }
              if (scale_w > scale_y) {
                text.set({
                  scaleX: scale_y,
                  scaleY: scale_y,
                });
              } else if (scale_w < scale_y) {
                text.set({
                  scaleX: scale_w,
                  scaleY: scale_w,
                });
              }
            }
            // 房间号
            if (text.typeName === "houseNumber") {
              houseNumberAddFlag = false;
              text.set({
                text: `${this.Form.houseNumber}`,
                scaleX: 1,
                scaleY: 1,
              });
              let text_w = text.width;
              let text_H = text.height;
              let group_w = this.activeObject.width;
              let group_H = this.activeObject.height;
              let scale_w = 1;
              let scale_y = 1;
              if (text_w + this.textWidthSpace * 2 > group_w) {
                const w = group_w - this.textWidthSpace * 2;
                text.scaleToWidth(w);
                scale_w = text.scaleX;
              }
              if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
                const h = (group_H - this.textHeightSpace * 2) / 3;
                text.scaleToHeight(h);
                scale_y = text.scaleY;
              }
              if (scale_w > scale_y) {
                text.set({
                  scaleX: scale_y,
                  scaleY: scale_y,
                });
              } else if (scale_w < scale_y) {
                text.set({
                  scaleX: scale_w,
                  scaleY: scale_w,
                });
              }
            }
          });
        }
        if (houseUseAreaAddFlag) {
          const houseUseArea = new fabric.Text(`${this.Form.houseUseArea}m²`, {
            strokeWidth: 1,
            originX: "center",
            originY: "center",
            fontSize: 20,
          });
          let text_w = houseUseArea.width;
          let text_H = houseUseArea.height;
          let group_w = this.activeObject.width;
          let group_H = this.activeObject.height;
          let scale_w = 1;
          let scale_y = 1;
          if (text_w + this.textWidthSpace * 2 > group_w) {
            const w = group_w - this.textWidthSpace * 2;
            houseUseArea.scaleToWidth(w);
            scale_w = houseUseArea.scaleX;
          }
          if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
            const h = (group_H - this.textHeightSpace * 2) / 3;
            houseUseArea.scaleToHeight(h);
            scale_y = houseUseArea.scaleY;
          }
          if (scale_w > scale_y) {
            houseUseArea.set({
              scaleX: scale_y,
              scaleY: scale_y,
            });
          } else if (scale_w < scale_y) {
            houseUseArea.set({
              scaleX: scale_w,
              scaleY: scale_w,
            });
          }
          houseUseArea.toObject = (function (toObject) {
            return function () {
              return fabric.util.object.extend(toObject.call(this), {
                typeName: this.typeName,
              });
            };
          })(houseUseArea.toObject);
          this.activeObject.add(houseUseArea);
          houseUseArea.typeName = "houseUseArea";
        }
        if (houseNumberAddFlag) {
          const houseNumber = new fabric.Text(`${this.Form.houseNumber}`, {
            strokeWidth: 1,
            originX: "center",
            originY: "center",
            fontSize: 20,
          });
          let text_w = houseNumber.width;
          let text_H = houseNumber.height;
          let group_w = this.activeObject.width;
          let group_H = this.activeObject.height;
          let scale_w = 1;
          let scale_y = 1;
          if (text_w + this.textWidthSpace * 2 > group_w) {
            const w = group_w - this.textWidthSpace * 2;
            houseNumber.scaleToWidth(w);
            scale_w = houseNumber.scaleX;
          }
          if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
            const h = (group_H - this.textHeightSpace * 2) / 3;
            houseNumber.scaleToHeight(h);
            scale_y = houseNumber.scaleY;
          }
          if (scale_w > scale_y) {
            houseNumber.set({
              scaleX: scale_y,
              scaleY: scale_y,
            });
          } else if (scale_w < scale_y) {
            houseNumber.set({
              scaleX: scale_w,
              scaleY: scale_w,
            });
          }
          houseNumber.toObject = (function (toObject) {
            return function () {
              return fabric.util.object.extend(toObject.call(this), {
                typeName: this.typeName,
              });
            };
          })(houseNumber.toObject);
          this.activeObject.add(houseNumber);
          houseNumber.typeName = "houseNumber";
        }
        // 字体设置成相同的比例
        let updateText = this.activeObject.getObjects("text");
        if (updateText.length > 0) {
          updateText.sort(function (a, b) {
            var value1 = a["scaleX"];
            var value2 = b["scaleX"];
            return value1 - value2;
          });
          const minScale = updateText[0].scaleX;
          const minHeight = updateText[0].height;
          if (minScale < 1) {
            updateText.forEach((text) => {
              text.set({
                scaleX: minScale,
                scaleY: minScale,
              });
            });
          }
          let top = Math.round(
            -(minHeight * minScale) - this.textHeightSpace / 2
          );
          updateText.forEach((text) => {
            if (text.typeName === "houseNumber") {
              text.set({
                top: top,
              });
            }
          });
        }
        this.canvas.requestRenderAll();
        if (flag) this.editHouseInfo.push(obj_2);
        this.dialogVisible = false;
      } else if (
        this.Form.isUse === 1 &&
        this.isLeader === "2" &&
        this.baseSubmitFlag &&
        this.nonLeaderSumbitFlag &&
        this.Form.houseTypeFlag === 1
      ) {
        if (this.Form.unitIds && this.Form.unitIds.length > 0) {
          delete this.Form.unitIds;
        }
        const obj = JSON.parse(JSON.stringify(this.Form));
        let flagInfo = true;
        if (this.existHouseInfo.length > 0) {
          this.existHouseInfo.forEach((element, index) => {
            if (element.houseId === obj.houseId) {
              this.existHouseInfo.splice(index, 1, obj);
              flagInfo = false;
            }
          });
        }
        if (flagInfo) this.existHouseInfo.push(obj);

        // 保存编辑过的数据
        const obj_2 = JSON.parse(JSON.stringify(this.Form));
        // 如果是租借的房间，要做退租处理
        if (obj_2.rent) {
          if (this.rentHouseIds.indexOf(obj_2.houseId) === -1) {
            this.rentHouseIds.push(obj_2.houseId);
          }
        }
        delete obj_2.rent;
        // 非领导和领导之间转换, 办公用房和非办公用房之间转换，解除关联关系。
        if (
          this.isOldUse === 1 &&
          this.oldHouseType === 1 &&
          this.oldIsLeader !== this.isLeader
        ) {
          const deleteHousingUseVoList = [];
          if (obj_2.deleteUseUnitIds === undefined) {
            obj_2.deleteUseUnitIds = [];
          }
          obj_2.deleteUseUnitIds.push(obj_2.leadingOfficeVo.useunitId);
          let housingUseVoTemp = {};
          housingUseVoTemp.deleteLeadingOfficeIds = [];
          housingUseVoTemp.useRelationshipList = [];
          obj_2.leadingOfficeVo.leadingOfficeList.forEach((leadingOffice) => {
            housingUseVoTemp.deleteLeadingOfficeIds.push(
              leadingOffice.leadingofficeId
            );
          });
          obj_2.leadingOfficeVo.useRelationshipList.forEach(
            (useRelationship) => {
              useRelationship.roomNum = 0;
            }
          );
          housingUseVoTemp.useRelationshipList =
            obj_2.leadingOfficeVo.useRelationshipList;
          housingUseVoTemp.unitId = obj_2.housingUseVoList[0].unitId;
          deleteHousingUseVoList.push(housingUseVoTemp);
          obj_2.housingUseVoList = deleteHousingUseVoList;
        } else if (this.isOldUse === 1 && this.oldHouseType !== 1) {
          if (obj_2.deleteUseUnitIds === undefined) {
            obj_2.deleteUseUnitIds = [];
          }
          obj_2.housingUseUnitList.forEach((item) => {
            obj_2.deleteUseUnitIds.push(item.useunitId);
          });
          let deleteObj = {};
          deleteObj.useRelationshipList = [];
          obj_2.housingUseVoListClone.forEach((item) => {
            const useRelationshipObj = {
              jobId: item.useRelationshipList[0].jobId,
              roomNum: 0,
            };
            deleteObj.useRelationshipList.push(useRelationshipObj);
          });
          obj_2.housingUseVoList.push(deleteObj);
        }
        if (obj_2.leadingOfficeVo !== undefined) delete obj_2.leadingOfficeVo;
        if (obj_2.housingUseUnitList !== undefined)
          delete obj_2.housingUseUnitList;
        if (obj_2.housingUseVoListClone !== undefined)
          delete obj_2.housingUseVoListClone;
        // 字符串转化为数字
        obj_2.houseUseArea = obj_2.houseUseArea - 0;
        // 判断该房间数据是否已经编辑过
        let flag = true;
        if (this.editHouseInfo.length > 0) {
          this.editHouseInfo.forEach((element, index) => {
            if (element.houseId === obj_2.houseId) {
              this.editHouseInfo.splice(index, 1, obj_2);
              flag = false;
            }
          });
        }
        // 未使用的房间加上标识符
        this.activeObject.isUse = true;
        // 房间类型颜色
        this.houseTypeColor.forEach((element) => {
          if (element.id === this.Form.housetypeId) {
            const rect = this.activeObject.getObjects("rect");
            if (rect.length > 0) {
              rect[0].set("fill", element.color);
            } else {
              const path = this.activeObject.getObjects("path");
              path[0].set("fill", element.color);
            }
            this.canvas.requestRenderAll();
          }
        });
        // 房间信息标注
        const existedText = this.activeObject.getObjects("text");
        let houseUseAreaAddFlag = true;
        let houseNumberAddFlag = true;
        if (existedText.length > 0) {
          existedText.forEach((text) => {
            // 房间使用面积
            if (text.typeName === "houseUseArea") {
              houseUseAreaAddFlag = false;
              text.set({
                text: `${this.Form.houseUseArea}m²`,
                scaleX: 1,
                scaleY: 1,
              });
              let text_w = text.width;
              let text_H = text.height;
              let group_w = this.activeObject.width;
              let group_H = this.activeObject.height;
              let scale_w = 1;
              let scale_y = 1;
              if (text_w + this.textWidthSpace * 2 > group_w) {
                const w = group_w - this.textWidthSpace * 2;
                text.scaleToWidth(w);
                scale_w = text.scaleX;
              }
              if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
                const h = (group_H - this.textHeightSpace * 2) / 3;
                text.scaleToHeight(h);
                scale_y = text.scaleY;
              }
              if (scale_w > scale_y) {
                text.set({
                  scaleX: scale_y,
                  scaleY: scale_y,
                });
              } else if (scale_w < scale_y) {
                text.set({
                  scaleX: scale_w,
                  scaleY: scale_w,
                });
              }
            }
            // 房间号
            if (text.typeName === "houseNumber") {
              houseNumberAddFlag = false;
              text.set({
                text: `${this.Form.houseNumber}`,
                scaleX: 1,
                scaleY: 1,
              });
              let text_w = text.width;
              let text_H = text.height;
              let group_w = this.activeObject.width;
              let group_H = this.activeObject.height;
              let scale_w = 1;
              let scale_y = 1;
              if (text_w + this.textWidthSpace * 2 > group_w) {
                const w = group_w - this.textWidthSpace * 2;
                text.scaleToWidth(w);
                scale_w = text.scaleX;
              }
              if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
                const h = (group_H - this.textHeightSpace * 2) / 3;
                text.scaleToHeight(h);
                scale_y = text.scaleY;
              }
              if (scale_w > scale_y) {
                text.set({
                  scaleX: scale_y,
                  scaleY: scale_y,
                });
              } else if (scale_w < scale_y) {
                text.set({
                  scaleX: scale_w,
                  scaleY: scale_w,
                });
              }
            }
          });
        }
        if (houseUseAreaAddFlag) {
          const houseUseArea = new fabric.Text(`${this.Form.houseUseArea}m²`, {
            strokeWidth: 1,
            originX: "center",
            originY: "center",
            fontSize: 20,
          });
          let text_w = houseUseArea.width;
          let text_H = houseUseArea.height;
          let group_w = this.activeObject.width;
          let group_H = this.activeObject.height;
          let scale_w = 1;
          let scale_y = 1;
          if (text_w + this.textWidthSpace * 2 > group_w) {
            const w = group_w - this.textWidthSpace * 2;
            houseUseArea.scaleToWidth(w);
            scale_w = houseUseArea.scaleX;
          }
          if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
            const h = (group_H - this.textHeightSpace * 2) / 3;
            houseUseArea.scaleToHeight(h);
            scale_y = houseUseArea.scaleY;
          }
          if (scale_w > scale_y) {
            houseUseArea.set({
              scaleX: scale_y,
              scaleY: scale_y,
            });
          } else if (scale_w < scale_y) {
            houseUseArea.set({
              scaleX: scale_w,
              scaleY: scale_w,
            });
          }
          houseUseArea.toObject = (function (toObject) {
            return function () {
              return fabric.util.object.extend(toObject.call(this), {
                typeName: this.typeName,
              });
            };
          })(houseUseArea.toObject);
          this.activeObject.add(houseUseArea);
          houseUseArea.typeName = "houseUseArea";
        }
        if (houseNumberAddFlag) {
          const houseNumber = new fabric.Text(`${this.Form.houseNumber}`, {
            strokeWidth: 1,
            originX: "center",
            originY: "center",
            fontSize: 20,
          });
          let text_w = houseNumber.width;
          let text_H = houseNumber.height;
          let group_w = this.activeObject.width;
          let group_H = this.activeObject.height;
          let scale_w = 1;
          let scale_y = 1;
          if (text_w + this.textWidthSpace * 2 > group_w) {
            const w = group_w - this.textWidthSpace * 2;
            houseNumber.scaleToWidth(w);
            scale_w = houseNumber.scaleX;
          }
          if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
            const h = (group_H - this.textHeightSpace * 2) / 3;
            houseNumber.scaleToHeight(h);
            scale_y = houseNumber.scaleY;
          }
          if (scale_w > scale_y) {
            houseNumber.set({
              scaleX: scale_y,
              scaleY: scale_y,
            });
          } else if (scale_w < scale_y) {
            houseNumber.set({
              scaleX: scale_w,
              scaleY: scale_w,
            });
          }
          houseNumber.toObject = (function (toObject) {
            return function () {
              return fabric.util.object.extend(toObject.call(this), {
                typeName: this.typeName,
              });
            };
          })(houseNumber.toObject);
          this.activeObject.add(houseNumber);
          houseNumber.typeName = "houseNumber";
        }
        // 字体设置成相同的比例
        let updateText = this.activeObject.getObjects("text");
        if (updateText.length > 0) {
          updateText.sort(function (a, b) {
            var value1 = a["scaleX"];
            var value2 = b["scaleX"];
            return value1 - value2;
          });
          const minScale = updateText[0].scaleX;
          const minHeight = updateText[0].height;
          if (minScale < 1) {
            updateText.forEach((text) => {
              text.set({
                scaleX: minScale,
                scaleY: minScale,
              });
            });
          }
          updateText.forEach((text) => {
            if (text.typeName === "houseNumber") {
              text.set({
                top: -minHeight * minScale - this.textHeightSpace / 2,
              });
            }
          });
        }
        this.canvas.requestRenderAll();
        if (flag) this.editHouseInfo.push(obj_2);
        this.dialogVisible = false;
      } else if (
        this.Form.isUse === 1 &&
        this.baseSubmitFlag &&
        this.Form.houseTypeFlag !== 1
      ) {
        // 保存数据到本地
        const obj = JSON.parse(JSON.stringify(this.Form));
        // 非领导清空领导数据
        this.clearForm(obj.leadingOfficeVo);
        let flagInfo = true;
        if (this.existHouseInfo.length > 0) {
          this.existHouseInfo.forEach((element, index) => {
            if (element.houseId === obj.houseId) {
              this.existHouseInfo.splice(index, 1, obj);
              flagInfo = false;
            }
          });
        }
        // 单位集合数据
        obj.unitIds = [];
        obj.unitIds.push(this.unitIds);
        if (flagInfo) this.existHouseInfo.push(obj);

        const obj_2 = JSON.parse(JSON.stringify(this.Form));
        // 如果是租借的房间，要做退租处理
        if (obj_2.rent) {
          if (this.rentHouseIds.indexOf(obj_2.houseId) === -1) {
            this.rentHouseIds.push(obj_2.houseId);
          }
        }
        delete obj_2.rent;
        // 字符串转化为数字
        obj_2.houseUseArea = obj_2.houseUseArea - 0;
        // 单位集合数据
        obj_2.unitIds = [];
        obj_2.unitIds.push(this.unitIds);
        // 之前使用过的办公用房，解除关联关系
        if (this.isOldUse === 1 && this.oldHouseType === 1) {
          if (this.oldIsLeader === "1") {
            const deleteHousingUseVoList = [];
            if (obj_2.deleteUseUnitIds === undefined) {
              obj_2.deleteUseUnitIds = [];
            }
            obj_2.deleteUseUnitIds.push(
              this.oldFormData.leadingOfficeVo.useunitId
            );
            let housingUseVoTemp = {};
            housingUseVoTemp.deleteLeadingOfficeIds = [];
            housingUseVoTemp.useRelationshipList = [];
            this.oldFormData.leadingOfficeVo.leadingOfficeList.forEach(
              (leadingOffice) => {
                housingUseVoTemp.deleteLeadingOfficeIds.push(
                  leadingOffice.leadingofficeId
                );
              }
            );
            this.oldFormData.leadingOfficeVo.useRelationshipList.forEach(
              (useRelationship) => {
                useRelationship.roomNum = 0;
              }
            );
            housingUseVoTemp.useRelationshipList =
              this.oldFormData.leadingOfficeVo.useRelationshipList;
            deleteHousingUseVoList.push(housingUseVoTemp);
            obj_2.housingUseVoList = deleteHousingUseVoList;
            obj_2.housingUseUnitList = [];
            obj_2.unitIds.forEach((id) => {
              let obj = {};
              obj.unitId = id;
              obj_2.housingUseUnitList.push(obj);
            });
            delete obj_2.leadingOfficeVo;
          } else {
            if (obj_2.deleteUseUnitIds === undefined) {
              obj_2.deleteUseUnitIds = [];
            }
            obj_2.deleteUseUnitIds.push(obj_2.housingUseVoList[0].useunitId);
            const deleteObj = {};
            const useRelationshipObj = {
              jobId: obj_2.housingUseVoList[0].jobId,
              professionalId: obj_2.housingUseVoList[0].professionalId,
              roomNum: 0,
            };
            deleteObj.useRelationshipList = [useRelationshipObj];
            deleteObj.deleteLeadingOfficeIds = [
              obj_2.housingUseVoList[0].leadingofficeId,
            ];
            obj_2.housingUseVoList = [deleteObj];
            obj_2.housingUseUnitList = [];
            obj_2.unitIds.forEach((id) => {
              let obj = {};
              obj.unitId = id;
              obj_2.housingUseUnitList.push(obj);
            });
            delete obj_2.leadingOfficeVo;
          }
        } else if (this.isOldUse === 1 && this.oldHouseType !== 1) {
          if (obj_2.deleteUseUnitIds === undefined) {
            obj_2.deleteUseUnitIds = [];
          }
          for (let i = obj_2.housingUseUnitList.length - 1; i > -1; i--) {
            if (
              obj_2.unitIds.indexOf(obj_2.housingUseUnitList[i].unitId) === -1
            ) {
              obj_2.deleteUseUnitIds.push(
                obj_2.housingUseUnitList[i].useunitId
              );
              obj_2.housingUseUnitList.splice(i, 1);
            }
          }
          let tempIdList = [];
          obj_2.housingUseUnitList.forEach((item) => {
            tempIdList.push(item.unitId);
          });
          obj_2.unitIds.forEach((unitId) => {
            if (tempIdList.indexOf(unitId) === -1) {
              let obj = {};
              obj.unitId = unitId;
              obj_2.housingUseUnitList.push(obj);
            }
          });
          delete obj_2.leadingOfficeVo;
          delete obj_2.housingUseVoList;
        } else {
          obj_2.housingUseUnitList = [];
          obj_2.unitIds.forEach((id) => {
            let obj = {};
            obj.unitId = id;
            obj_2.housingUseUnitList.push(obj);
          });
          delete obj_2.leadingOfficeVo;
          delete obj_2.housingUseVoList;
        }
        // 判断该房间数据是否已经编辑过
        let flag = true;
        if (this.editHouseInfo.length > 0) {
          this.editHouseInfo.forEach((element, index) => {
            if (element.houseId === obj_2.houseId) {
              this.editHouseInfo.splice(index, 1, obj_2);
              flag = false;
            }
          });
        }

        // 未使用的房间加上标识符
        this.activeObject.isUse = true;
        // 房间类型颜色
        this.houseTypeColor.forEach((element) => {
          if (element.id === this.Form.housetypeId) {
            const rect = this.activeObject.getObjects("rect");
            if (rect.length > 0) {
              rect[0].set("fill", element.color);
            } else {
              const path = this.activeObject.getObjects("path");
              path[0].set("fill", element.color);
            }
            this.canvas.requestRenderAll();
          }
        });
        this.dialogVisible = false;
        if (flag) this.editHouseInfo.push(obj_2);
        // 房间信息标注
        const existedText = this.activeObject.getObjects("text");
        let houseUseAreaAddFlag = true;
        let houseNumberAddFlag = true;
        if (existedText.length > 0) {
          existedText.forEach((text) => {
            // 房间使用面积
            if (text.typeName === "houseUseArea") {
              houseUseAreaAddFlag = false;
              text.set({
                text: `${this.Form.houseUseArea}m²`,
                scaleX: 1,
                scaleY: 1,
              });
              let text_w = text.width;
              let text_H = text.height;
              let group_w = this.activeObject.width;
              let group_H = this.activeObject.height;
              let scale_w = 1;
              let scale_y = 1;
              if (text_w + this.textWidthSpace * 2 > group_w) {
                const w = group_w - this.textWidthSpace * 2;
                text.scaleToWidth(w);
                scale_w = text.scaleX;
              }
              if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
                const h = (group_H - this.textHeightSpace * 2) / 3;
                text.scaleToHeight(h);
                scale_y = text.scaleY;
              }
              if (scale_w > scale_y) {
                text.set({
                  scaleX: scale_y,
                  scaleY: scale_y,
                });
              } else if (scale_w < scale_y) {
                text.set({
                  scaleX: scale_w,
                  scaleY: scale_w,
                });
              }
            }
            // 房间号
            if (text.typeName === "houseNumber") {
              houseNumberAddFlag = false;
              text.set({
                text: `${this.Form.houseNumber}`,
                scaleX: 1,
                scaleY: 1,
              });
              let text_w = text.width;
              let text_H = text.height;
              let group_w = this.activeObject.width;
              let group_H = this.activeObject.height;
              let scale_w = 1;
              let scale_y = 1;
              if (text_w + this.textWidthSpace * 2 > group_w) {
                const w = group_w - this.textWidthSpace * 2;
                text.scaleToWidth(w);
                scale_w = text.scaleX;
              }
              if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
                const h = (group_H - this.textHeightSpace * 2) / 3;
                text.scaleToHeight(h);
                scale_y = text.scaleY;
              }
              if (scale_w > scale_y) {
                text.set({
                  scaleX: scale_y,
                  scaleY: scale_y,
                });
              } else if (scale_w < scale_y) {
                text.set({
                  scaleX: scale_w,
                  scaleY: scale_w,
                });
              }
            }
          });
        }
        if (houseUseAreaAddFlag) {
          const houseUseArea = new fabric.Text(`${this.Form.houseUseArea}m²`, {
            strokeWidth: 1,
            originX: "center",
            originY: "center",
            fontSize: 20,
          });
          let text_w = houseUseArea.width;
          let text_H = houseUseArea.height;
          let group_w = this.activeObject.width;
          let group_H = this.activeObject.height;
          let scale_w = 1;
          let scale_y = 1;
          if (text_w + this.textWidthSpace * 2 > group_w) {
            const w = group_w - this.textWidthSpace * 2;
            houseUseArea.scaleToWidth(w);
            scale_w = houseUseArea.scaleX;
          }
          if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
            const h = (group_H - this.textHeightSpace * 2) / 3;
            houseUseArea.scaleToHeight(h);
            scale_y = houseUseArea.scaleY;
          }
          if (scale_w > scale_y) {
            houseUseArea.set({
              scaleX: scale_y,
              scaleY: scale_y,
            });
          } else if (scale_w < scale_y) {
            houseUseArea.set({
              scaleX: scale_w,
              scaleY: scale_w,
            });
          }
          houseUseArea.toObject = (function (toObject) {
            return function () {
              return fabric.util.object.extend(toObject.call(this), {
                typeName: this.typeName,
              });
            };
          })(houseUseArea.toObject);
          this.activeObject.add(houseUseArea);
          houseUseArea.typeName = "houseUseArea";
        }
        if (houseNumberAddFlag) {
          const houseNumber = new fabric.Text(`${this.Form.houseNumber}`, {
            strokeWidth: 1,
            originX: "center",
            originY: "center",
            fontSize: 20,
          });
          let text_w = houseNumber.width;
          let text_H = houseNumber.height;
          let group_w = this.activeObject.width;
          let group_H = this.activeObject.height;
          let scale_w = 1;
          let scale_y = 1;
          if (text_w + this.textWidthSpace * 2 > group_w) {
            const w = group_w - this.textWidthSpace * 2;
            houseNumber.scaleToWidth(w);
            scale_w = houseNumber.scaleX;
          }
          if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
            const h = (group_H - this.textHeightSpace * 2) / 3;
            houseNumber.scaleToHeight(h);
            scale_y = houseNumber.scaleY;
          }
          if (scale_w > scale_y) {
            houseNumber.set({
              scaleX: scale_y,
              scaleY: scale_y,
            });
          } else if (scale_w < scale_y) {
            houseNumber.set({
              scaleX: scale_w,
              scaleY: scale_w,
            });
          }
          houseNumber.toObject = (function (toObject) {
            return function () {
              return fabric.util.object.extend(toObject.call(this), {
                typeName: this.typeName,
              });
            };
          })(houseNumber.toObject);
          this.activeObject.add(houseNumber);
          houseNumber.typeName = "houseNumber";
        }
        // 字体设置成相同的比例
        let updateText = this.activeObject.getObjects("text");
        if (updateText.length > 0) {
          updateText.sort(function (a, b) {
            var value1 = a["scaleX"];
            var value2 = b["scaleX"];
            return value1 - value2;
          });
          const minScale = updateText[0].scaleX;
          const minHeight = updateText[0].height;
          if (minScale < 1) {
            updateText.forEach((text) => {
              text.set({
                scaleX: minScale,
                scaleY: minScale,
              });
            });
          }
          updateText.forEach((text) => {
            if (text.typeName === "houseNumber") {
              text.set({
                top: -minHeight * minScale - this.textHeightSpace / 2,
              });
            }
          });
        }
        this.canvas.requestRenderAll();
      } else if (this.Form.isUse === 0 && this.baseSubmitFlag) {
        if (this.Form.unitIds && this.Form.unitIds.length > 0) {
          delete this.Form.unitIds;
        }
        const obj = JSON.parse(JSON.stringify(this.Form));
        delete obj.housingUseVoList;
        delete obj.leadingOfficeVo;
        let flagInfo = true;
        if (this.existHouseInfo.length > 0) {
          this.existHouseInfo.forEach((element, index) => {
            if (element.houseId === obj.houseId) {
              this.existHouseInfo.splice(index, 1, obj);
              flagInfo = false;
            }
          });
        }
        if (flagInfo) this.existHouseInfo.push(obj);

        const obj_2 = JSON.parse(JSON.stringify(this.Form));

        // 字符串转化为数字
        obj_2.houseUseArea = obj_2.houseUseArea - 0;

        // 之前使用过的办公用房，解除关联关系
        if (this.isOldUse === 1 && this.oldHouseType === 1) {
          if (this.oldIsLeader === "1") {
            const deleteHousingUseVoList = [];
            if (obj_2.deleteUseUnitIds === undefined) {
              obj_2.deleteUseUnitIds = [];
            }
            obj_2.deleteUseUnitIds.push(
              this.oldFormData.leadingOfficeVo.useunitId
            );
            let housingUseVoTemp = {};
            housingUseVoTemp.deleteLeadingOfficeIds = [];
            housingUseVoTemp.useRelationshipList = [];
            this.oldFormData.leadingOfficeVo.leadingOfficeList.forEach(
              (leadingOffice) => {
                housingUseVoTemp.deleteLeadingOfficeIds.push(
                  leadingOffice.leadingofficeId
                );
              }
            );
            this.oldFormData.leadingOfficeVo.useRelationshipList.forEach(
              (useRelationship) => {
                useRelationship.roomNum = 0;
              }
            );
            housingUseVoTemp.useRelationshipList =
              this.oldFormData.leadingOfficeVo.useRelationshipList;
            deleteHousingUseVoList.push(housingUseVoTemp);
            obj_2.housingUseVoList = deleteHousingUseVoList;
            obj_2.housingUseUnitList = [];
            obj_2.unitIds.forEach((id) => {
              let obj = {};
              obj.unitId = id;
              obj_2.housingUseUnitList.push(obj);
            });
            delete obj_2.leadingOfficeVo;
          } else {
            if (obj_2.deleteUseUnitIds === undefined) {
              obj_2.deleteUseUnitIds = [];
            }
            obj_2.deleteUseUnitIds.push(obj_2.housingUseVoList[0].useunitId);
            const deleteObj = {};
            const useRelationshipObj = {
              jobId: obj_2.housingUseVoList[0].jobId,
              professionalId: obj_2.housingUseVoList[0].professionalId,
              roomNum: 0,
            };
            deleteObj.useRelationshipList = [useRelationshipObj];
            deleteObj.deleteLeadingOfficeIds = [
              obj_2.housingUseVoList[0].leadingofficeId,
            ];
            obj_2.housingUseVoList = [deleteObj];
            obj_2.housingUseUnitList = [];
            obj_2.unitIds.forEach((id) => {
              let obj = {};
              obj.unitId = id;
              obj_2.housingUseUnitList.push(obj);
            });
            delete obj_2.leadingOfficeVo;
          }
        } else if (this.isOldUse === 1 && this.oldHouseType !== 1) {
          if (obj_2.deleteUseUnitIds === undefined) {
            obj_2.deleteUseUnitIds = [];
          }
          for (let i = obj_2.housingUseUnitList.length - 1; i > -1; i--) {
            obj_2.deleteUseUnitIds.push(obj_2.housingUseUnitList[i].useunitId);
          }
          delete obj_2.leadingOfficeVo;
          delete obj_2.housingUseVoList;
          delete obj_2.housingUseUnitList;
        } else {
          delete obj_2.housingUseVoList;
          delete obj_2.leadingOfficeVo;
          delete obj_2.housingUseUnitList;
        }
        delete obj_2.housingUseVoListClone;

        // 判断该房间数据是否已经编辑过
        let flag = true;
        if (this.editHouseInfo.length > 0) {
          this.editHouseInfo.forEach((element, index) => {
            if (element.houseId === obj_2.houseId) {
              this.editHouseInfo.splice(index, 1, obj_2);
              flag = false;
            }
          });
        }
        if (flag) this.editHouseInfo.push(obj_2);
        this.dialogVisible = false;

        // 未使用的房间加上标识符
        this.activeObject.isUse = false;

        // 房间类型颜色
        this.houseTypeColor.forEach((element) => {
          if (element.id === this.Form.housetypeId) {
            const rect = this.activeObject.getObjects("rect");
            if (rect.length > 0) {
              rect[0].set("fill", element.color);
            } else {
              const path = this.activeObject.getObjects("path");
              path[0].set("fill", element.color);
            }
          }
        });
        // 房间信息标注
        const existedText = this.activeObject.getObjects("text");
        let houseUseAreaAddFlag = true;
        let houseNumberAddFlag = true;
        if (existedText.length > 0) {
          existedText.forEach((text) => {
            // 房间使用面积
            if (text.typeName === "houseUseArea") {
              houseUseAreaAddFlag = false;
              text.set({
                text: `${this.Form.houseUseArea}m²`,
                scaleX: 1,
                scaleY: 1,
              });
              let text_w = text.width;
              let text_H = text.height;
              let group_w = this.activeObject.width;
              let group_H = this.activeObject.height;
              let scale_w = 1;
              let scale_y = 1;
              if (text_w + this.textWidthSpace * 2 > group_w) {
                const w = group_w - this.textWidthSpace * 2;
                text.scaleToWidth(w);
                scale_w = text.scaleX;
              }
              if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
                const h = (group_H - this.textHeightSpace * 2) / 3;
                text.scaleToHeight(h);
                scale_y = text.scaleY;
              }
              if (scale_w > scale_y) {
                text.set({
                  scaleX: scale_y,
                  scaleY: scale_y,
                });
              } else if (scale_w < scale_y) {
                text.set({
                  scaleX: scale_w,
                  scaleY: scale_w,
                });
              }
            }
            // 房间号
            if (text.typeName === "houseNumber") {
              houseNumberAddFlag = false;
              text.set({
                text: `${this.Form.houseNumber}`,
                scaleX: 1,
                scaleY: 1,
              });
              let text_w = text.width;
              let text_H = text.height;
              let group_w = this.activeObject.width;
              let group_H = this.activeObject.height;
              let scale_w = 1;
              let scale_y = 1;
              if (text_w + this.textWidthSpace * 2 > group_w) {
                const w = group_w - this.textWidthSpace * 2;
                text.scaleToWidth(w);
                scale_w = text.scaleX;
              }
              if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
                const h = (group_H - this.textHeightSpace * 2) / 3;
                text.scaleToHeight(h);
                scale_y = text.scaleY;
              }
              if (scale_w > scale_y) {
                text.set({
                  scaleX: scale_y,
                  scaleY: scale_y,
                });
              } else if (scale_w < scale_y) {
                text.set({
                  scaleX: scale_w,
                  scaleY: scale_w,
                });
              }
            }
          });
        }
        if (houseUseAreaAddFlag) {
          const houseUseArea = new fabric.Text(`${this.Form.houseUseArea}m²`, {
            strokeWidth: 1,
            originX: "center",
            originY: "center",
            fontSize: 20,
          });
          let text_w = houseUseArea.width;
          let text_H = houseUseArea.height;
          let group_w = this.activeObject.width;
          let group_H = this.activeObject.height;
          let scale_w = 1;
          let scale_y = 1;
          if (text_w + this.textWidthSpace * 2 > group_w) {
            const w = group_w - this.textWidthSpace * 2;
            houseUseArea.scaleToWidth(w);
            scale_w = houseUseArea.scaleX;
          }
          if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
            const h = (group_H - this.textHeightSpace * 2) / 3;
            houseUseArea.scaleToHeight(h);
            scale_y = houseUseArea.scaleY;
          }
          if (scale_w > scale_y) {
            houseUseArea.set({
              scaleX: scale_y,
              scaleY: scale_y,
            });
          } else if (scale_w < scale_y) {
            houseUseArea.set({
              scaleX: scale_w,
              scaleY: scale_w,
            });
          }
          houseUseArea.toObject = (function (toObject) {
            return function () {
              return fabric.util.object.extend(toObject.call(this), {
                typeName: this.typeName,
              });
            };
          })(houseUseArea.toObject);
          this.activeObject.add(houseUseArea);
          houseUseArea.typeName = "houseUseArea";
        }
        if (houseNumberAddFlag) {
          const houseNumber = new fabric.Text(`${this.Form.houseNumber}`, {
            strokeWidth: 1,
            originX: "center",
            originY: "center",
            fontSize: 20,
          });
          let text_w = houseNumber.width;
          let text_H = houseNumber.height;
          let group_w = this.activeObject.width;
          let group_H = this.activeObject.height;
          let scale_w = 1;
          let scale_y = 1;
          if (text_w + this.textWidthSpace * 2 > group_w) {
            const w = group_w - this.textWidthSpace * 2;
            houseNumber.scaleToWidth(w);
            scale_w = houseNumber.scaleX;
          }
          if (text_H * 3 + this.textHeightSpace * 2 > group_H) {
            const h = (group_H - this.textHeightSpace * 2) / 3;
            houseNumber.scaleToHeight(h);
            scale_y = houseNumber.scaleY;
          }
          if (scale_w > scale_y) {
            houseNumber.set({
              scaleX: scale_y,
              scaleY: scale_y,
            });
          } else if (scale_w < scale_y) {
            houseNumber.set({
              scaleX: scale_w,
              scaleY: scale_w,
            });
          }
          houseNumber.toObject = (function (toObject) {
            return function () {
              return fabric.util.object.extend(toObject.call(this), {
                typeName: this.typeName,
              });
            };
          })(houseNumber.toObject);
          this.activeObject.add(houseNumber);
          houseNumber.typeName = "houseNumber";
        }
        // 字体设置成相同的比例
        let updateText = this.activeObject.getObjects("text");
        if (updateText.length > 0) {
          updateText.sort(function (a, b) {
            var value1 = a["scaleX"];
            var value2 = b["scaleX"];
            return value1 - value2;
          });
          const minScale = updateText[0].scaleX;
          const minHeight = updateText[0].height;
          if (minScale < 1) {
            updateText.forEach((text) => {
              text.set({
                scaleX: minScale,
                scaleY: minScale,
              });
            });
          }
          updateText.forEach((text) => {
            if (text.typeName === "houseNumber") {
              text.set({
                top: -minHeight * minScale - this.textHeightSpace / 2,
              });
            }
          });
        }
        // 如果是租借的房间，要做退租处理
        if (obj_2.rent) {
          if (this.rentHouseIds.indexOf(obj_2.houseId) > -1) {
            const index = this.rentHouseIds.indexOf(obj_2.houseId);
            this.rentHouseIds.splice(index, 1);
          }
          let rect = this.activeObject.getObjects("rect");
          if (rect.length > 0) {
            rect[0].set("fill", "rgb(254, 130, 0)");
          } else {
            const path = this.activeObject.getObjects("path");
            path[0].set("fill", "rgb(254, 130, 0)");
          }
        }
        delete obj_2.rent;
        this.canvas.requestRenderAll();
      }
    },
    // 保存所有数据
    saveAllInfo() {
      const yardId = sessionStorage.getItem("yardId");
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      const recordHolder = sessionStorage.getItem("name");
      const recordHolderId = sessionStorage.getItem("userId");
      const canvasJson = this.canvas.toJSON([
        "uuid",
        "isExcess",
        "isRecorded",
        "isUse",
        "typeName",
        "pathLeft",
        "pathTop",
        "pathPoint",
        "roomType",
        "oldLeft",
        "oldTop",
        "oldCoord",
      ]);
      const jsonPlanVo =
        canvasJson.objects && canvasJson.objects.length > 0 ? canvasJson : null;
      let obj = new Object();
      this.houseVoList.forEach((houseVoItem) => {
        if (
          houseVoItem.isUse === 1 &&
          houseVoItem.leadingOfficeVo &&
          houseVoItem.leadingOfficeVo.unitId &&
          houseVoItem.leadingOfficeVo.leadingOfficeList &&
          houseVoItem.leadingOfficeVo.leadingOfficeList.length > 0
        ) {
          const leadingOfficeList =
            houseVoItem.leadingOfficeVo.leadingOfficeList;
          leadingOfficeList.forEach((leadingOfficeItem) => {
            delete leadingOfficeItem.professionOpts;
            delete leadingOfficeItem.professionalLevel;
          });
        }
      });
      let commitFlag = false;
      let commitRoomNum = 0;
      let roomDataNum = this.houseVoList.length;
      if (jsonPlanVo) {
        jsonPlanVo.objects.forEach((item) => {
          if (item.type === "group" && item.typeName === "room") {
            commitRoomNum += 1;
          }
        });
      }
      if (commitRoomNum >= roomDataNum) {
        commitFlag = true;
      }
      if (commitFlag) {
        obj.houseVoList = this.houseVoList;
        obj.addHouseIdList = this.addHouseIdList;
        obj.delHouseIdList = this.delHouseIdList;
        obj.recordHolder = recordHolder;
        obj.recordHolderId = recordHolderId;
        obj.configureIds = this.configureIdList;
        obj.rentHouseIds = this.rentHouseIds;
        obj.jsonPlanVo = {
          jsonPlan: jsonPlanVo,
          yardId: yardId,
          buildId: buildId,
          floorName: floorName,
        };
        obj.yardId = yardId;
        obj.buildId = buildId;
        obj.floorName = floorName;
        houseAdd(obj)
          .then((res) => {
            this.$message({
              message: "保存成功",
              type: "success",
            });
            this.editHouseInfo = [];
            this.AddHouseId = [];
            this.deleteHouseId = [];
            this.houseVoList = [];
            this.addHouseIdList = [];
            this.delHouseIdList = [];
            this.rentHouseIds = [];
            this.configDialogVisible = false;
            this.getAllHouseInfo(buildId, floorName);
            this.getHouseGraphJson(buildId, floorName, false);
          })
          .catch((err) => {
            const msg = err.result ? err.result : "保存失败";
            this.$message({
              message: msg,
              type: "error",
            });
            this.editHouseInfo = [];
            this.AddHouseId = [];
            this.deleteHouseId = [];
            this.houseVoList = [];
            this.addHouseIdList = [];
            this.delHouseIdList = [];
            this.rentHouseIds = [];
            this.getAllHouseInfo(buildId, floorName);
            this.getHouseGraphJson(buildId, floorName, false);
          });
      } else {
        this.$message({
          message: "保存失败",
          type: "error",
        });
      }
    },
    // 保存全部
    allSave() {
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      if (!buildId || !floorName) {
        this.$message({
          message: "请先选择楼层",
          type: "warning",
        });
        return false;
      }
      this.houseVoList = [];
      this.addHouseIdList = [];
      this.delHouseIdList = [];
      const allObj = this.canvas.getObjects("group");
      allObj.forEach((element) => {
        this.editHouseInfo.forEach((info) => {
          if (element.uuid === info.houseId) {
            this.houseVoList.push(info);
          }
        });
        this.AddHouseId.forEach((addId) => {
          if (element.uuid === addId) {
            this.addHouseIdList.push(addId);
          }
        });
      });
      this.delHouseIdList = this.deleteHouseId;
      // 判断是否含有配置信息
      if (this.isConfigFloor) {
        this.updateIdList = [];
        this.houseVoList.forEach((item) => {
          this.updateIdList.push(item.houseId);
        });
        this.delHouseIdList.forEach((item) => {
          this.updateIdList.push(item);
        });
        if (this.updateIdList.length > 0) {
          this.configureListByHouseId(this.updateIdList);
          this.getConfigureIdList(this.updateIdList);
        } else {
          this.configureIdList = [];
          this.saveAllInfo();
        }
      } else {
        this.configureIdList = [];
        this.saveAllInfo();
      }
    },
    // 获取配置id
    getConfigureIdList(updateIdList) {
      getConfigureIdList(updateIdList)
        .then((res) => {
          if (res && res.length > 0) {
            this.configureIdList = res;
            this.configDialogVisible = true;
          } else {
            this.configureIdList = [];
            this.saveAllInfo();
          }
        })
        .catch((err) => {
          const msg = err.result ? err.result : "房间关联的配置ID获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    // 验证是否为空
    verificationNull(arr) {
      let i = 0;
      arr.forEach((element) => {
        let val = "";
        if (element !== "unitIds") {
          val = this.Form[element];
        } else {
          val = this.unitIds;
        }
        const outerBox = $("." + element);
        if (val === "" || val === null) {
          const labelText = outerBox.find("label").text();
          const verifyBox = $("." + element).find(".el-form-item__error");
          if (verifyBox.length === 0) {
            const tips = $(
              `<div class="el-form-item__error">${labelText}不能为空</div>`
            );
            outerBox.append(tips);
          }
          this.baseSubmitFlag = false;
        } else {
          const verifyBox = $("." + element).find(".el-form-item__error");
          if (verifyBox) {
            verifyBox.remove();
          }
          i++;
        }
      });
      if (i === arr.length) {
        this.baseSubmitFlag = true;
      }
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
    // 关闭模态框
    dialogClosed() {
      this.isLeader = "1";
      this.unitIds = "";
      this.oldIsLeader = null;
      this.oldHouseType = null;
      this.isOldUse = null;
      this.oldFormData = null;
      this.deleteHousingUseVoList = [];
      $(".el-form-item__error").remove();
      this.baseSubmitFlag = true;
      this.unitSubmitFlag = true;
      this.professSubmitFlag = true;
      this.userNumSubmitFlag = true;
      this.workStatueNumSubmitFlag = true;
      this.nonLeaderSumbitFlag = true;
      delete this.Form.rent;
      this.Form.housingUseVoList = [];
      this.Form.leadingOfficeVo = {};
      this.clearForm(this.Form);
      this.Form.isUse = 1;
      this.Form.isIdle = 0;
      this.Form.housingUseVoList = [
        {
          useunitId: null,
          unitId: null,
          housinguseId: null,
          useRelationshipList: [],
          deleteLeadingOfficeIds: [],
          leadingOfficeList: [],
          useForm: null,
          distributionTime: null,
          departmentName: null,
        },
      ];
      this.Form.leadingOfficeVo = {
        useunitId: null,
        unitId: null,
        useRelationshipList: [],
        deleteLeadingOfficeIds: [],
        leadingOfficeList: [
          {
            duty: "",
            leadingofficeId: null,
            leadingofficeName: "",
            leadingofficeWorkstatue: null,
            leadingofficeWorkStatueId: null,
            professionalId: null,
            professionalLevel: null,
            professionOpts: [],
          },
        ],
      };
    },
    // 添加一条职务信息项
    addProfessionItem() {
      const obj = {
        duty: "",
        leadingofficeId: null,
        leadingofficeName: "",
        leadingofficeWorkstatue: "",
        leadingofficeWorkStatueId: null,
        professionalId: null,
        professionalLevel: null,
        professionOpts: [],
      };
      this.Form.leadingOfficeVo.leadingOfficeList.push(obj);
    },
    // 删除一条职务信息项
    deleteProfessionItem(i) {
      const leadingofficeId =
        this.Form.leadingOfficeVo.leadingOfficeList[i].leadingofficeId;
      this.Form.leadingOfficeVo.leadingOfficeList.splice(i, 1);
      if (leadingofficeId) {
        if (this.Form.leadingOfficeVo.deleteLeadingOfficeIds === undefined) {
          this.Form.leadingOfficeVo.deleteLeadingOfficeIds = [];
        }
        this.Form.leadingOfficeVo.deleteLeadingOfficeIds.push(leadingofficeId);
      }
    },
    // 获取整层楼图形信息
    getHouseGraphJson(buildId, floorName, flag) {
      // flag === false 保存操作; flag === true 点击左侧树读取数据操作
      if (flag && !this.loading) {
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
      if (this.textbox) {
        this.textbox.exitEditing();
        this.textbox = null;
      }
      getHouseGraphJson(buildId, floorName)
        .then((res) => {
          if (res === "" || res === null) {
            if (flag && this.loading) {
              this.loading.close();
              this.loading = null;
              this.$message({
                message: "该楼层暂无示意图",
                type: "warning",
              });
            }
            this.canvas.clear();
            this.$refs.threeD.clear();
            this.tool("move", 0);
            return false;
          }
          this.canvas.loadFromJSON(res, () => {
            const lines = this.canvas.getObjects("line");
            const existHouseObj = this.canvas.getObjects("group");
            if (lines.length > 0) {
              lines.forEach((item, index) => {
                item.hasControls = false;
              });
            }
            if (existHouseObj.length > 0) {
              existHouseObj.forEach((item, index) => {
                if (item.typeName === "room") {
                  this.existHouseId.push(item.uuid);
                }
                // 不规则图形不可以放大缩小
                if (item.typeName === "room" && item.roomType === "path") {
                  item.hasControls = false;
                }
              });
            }
            if (flag && this.loading) {
              this.loading.close();
              this.loading = null;
            }
            this.tool("move", 0);
          });
        })
        .catch((err) => {
          if (flag && this.loading) {
            this.loading.close();
            this.loading = null;
          }
          this.$message({
            message: err.result ? err.result : "示意图加载失败！",
            type: "error",
          });
          this.canvas.clear();
          this.$refs.threeD.clear();
          this.tool("move", 0);
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
      let showViewBtnFlag = false;
      getAllHouseInfo(buildId, floorName)
        .then((res) => {
          console.log(res)
          this.originalHouseInfo = JSON.parse(JSON.stringify(res));
          this.existHouseInfo = JSON.parse(JSON.stringify(res));
          this.originalHouseInfo.forEach((info) => {
            if (info.rent) {
              info.isUse = 0;
              info.isIdle = 0;
            }
            if (showViewBtnFlag === false) {
              if (info.rent || info.isUse === 1) {
                showViewBtnFlag = true;
              }
            }
          });
          if (showViewBtnFlag === false) {
            this.showViewBtn = false;
          } else {
            this.showViewBtn = true;
          }
          this.existHouseInfo.forEach((info) => {
            info.houseUseArea = info.houseUseArea
              ? info.houseUseArea.toFixed(2)
              : "";
            if (info.rent) {
              info.isUse = 0;
              info.isIdle = 0;
            } else {
              info.unitIds = [];
              if (
                info.housetypeId !== undefined &&
                info.housetypeId !== 1 &&
                info.housingUseUnitList &&
                info.housingUseUnitList.length > 0
              ) {
                info.unitIds.push(info.housingUseUnitList[0].unitId);
                info.housingUseVoListClone = JSON.parse(
                  JSON.stringify(info.housingUseVoList)
                );
                delete info.housingUseVoList;
              }
            }
          });
        })
        .catch((err) => {
          const msg = err.result ? err.result : "楼层房间信息获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    // 判断是否已经备案
    isRecorded(houseId) {
      return new Promise((resolve) => {
        const buildId = sessionStorage.getItem("buildId");
        const floorName = sessionStorage.getItem("floorName");
        getAllHouseInfo(buildId, floorName).then((res) => {
          if (res && res.length > 0) {
            for (let i = 0; i < res.length; i++) {
              if (res[i].houseId === houseId) {
                const isRecord = res[i].isRecord;
                if (isRecord && isRecord === 1) {
                  resolve(1);
                } else {
                  resolve(null);
                }
              }
            }
          } else {
            resolve(null);
          }
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
      this.oldActiveIconIndex = this.activeIconIndex;
      this.activeIconIndex = num;
      this.accessoryDialogVisible = true;
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
          }
        })
        .catch((err) => {
          const msg = err.result ? err.result : "CAD获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    // 关闭附件模态框
    accessoryDialogClosed() {
      this.accessoryDialogVisible = false;
      this.fileList = [];
      this.CADDownload = [];
      this.activeIconIndex = this.oldActiveIconIndex;
      this.oldActiveIconIndex = null;
    },
    // 文件上传之前
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 200;
      if (!isLt2M) {
        this.$message.error("上传CAD大小不能超过200MB!");
      }
      return isLt2M;
    },
    // 删除附件
    handleRemove(file, fileList) {
      let deleteArr = [];
      deleteArr.push(file.id);
      deleteCAD(deleteArr)
        .then((res) => {
          this.$message({
            message: "CAD删除成功",
            type: "success",
          });
          this.fileList.forEach((element, i) => {
            if (element.id === file.id) {
              this.fileList.splice(i, 1);
            }
          });
        })
        .catch((err) => {
          const msg = err.result ? err.result : "CAD删除失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    // 上传附件成功
    handleSuccess(response, file, fileList) {
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      if (buildId && floorName) {
        let saveObj = {};
        saveObj.buildId = buildId;
        saveObj.floor = floorName;
        saveObj.floordrawingFileName =
          response.imgList[0].originalName.originalFile;
        saveObj.floordrawingUrl = response.imgList[0].imgPath;
        // 保存新增cad
        saveCAD(saveObj)
          .then((res) => {
            this.fileList = fileList;
            this.fileList[this.fileList.length - 1].id = res.floordrawingId;
            this.$message({
              message: "CAD上传成功",
              type: "success",
            });
          })
          .catch((err) => {
            fileList.splice(fileList.length - 1, 1);
            this.fileList = fileList;
            const msg = err.result ? err.result : "CAD上传失败";
            this.$message({
              message: msg,
              type: "error",
            });
          });
      }
    },
    // 上传附件失败
    handleError(err, file, fileList) {
      if (err.status === 401) {
        this.$store.dispatch("Logout", this.token);
        this.$router.push({ path: "/login" });
      }
      let msg = err.message ? err.message : "附件上传失败";
      this.$message({
        message: msg,
        type: "error",
      });
    },
    // 勾选cad文件
    checkboxChange($event, id) {
      if ($event === true) {
        if (this.CADDownload.indexOf(id) === -1) {
          this.CADDownload.push(id);
        }
      } else {
        const index = this.CADDownload.indexOf(id);
        if (index > -1) {
          this.CADDownload.splice(index, 1);
        }
      }
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
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      if (!buildId || !floorName) {
        this.$message({
          message: "请先选择楼层",
          type: "warning",
        });
        return false;
      }
      this.activeBtnIndex = 0;
    },
    // 3d展示
    threeDisplay() {
      if (this.webglFlag === false) {
        this.$message({
          message: "该浏览器不支持3D显示功能！",
          type: "warning",
        });
        return false;
      }
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      if (!buildId || !floorName) {
        this.$message({
          message: "请先选择楼层",
          type: "warning",
        });
        return false;
      }
      this.activeBtnIndex = 1;
      this.initWebgl();
    },
    // 只能输入正浮点数
    limitFloat(key, flag) {
      this.Form[key] = this.Form[key]
        .replace(/[^\d.]/g, "")
        .replace(/\.{2,}/g, ".")
        .replace(".", "$#$")
        .replace(/\./g, "")
        .replace("$#$", ".")
        .replace(/^(\-)*(\d+)\.(\d\d).*$/, "$1$2.$3")
        .replace(/^\./g, "");
      if (flag && this.Form[key] !== "") {
        this.Form[key] = (this.Form[key] - 0).toFixed(2);
      }
    },
    // 房间类型改变
    houseTypeChange(val) {
      for (let i = 0; i < this.houseType.length; i++) {
        if (this.houseType[i].housetypeId === val) {
          this.Form.houseTypeFlag = this.houseType[i].houseTypeFlag;
        }
      }
    },
    // 清空canvas
    clearCanvas() {
      if (this.canvas && this.canvas.clear) {
        this.canvas.clear();
      }
      sessionStorage.removeItem("yardId");
      sessionStorage.removeItem("buildId");
      sessionStorage.removeItem("floorName");
      this.drawType = null;
      this.popoverDoorIcon = false;
    },
    // 获取路由信息
    getRouterList() {
      getRouterList()
        .then((res) => {
          this.allNavList = res;
        })
        .catch((err) => {
          const msg = err.result ? err.result : "路由信息获取失败！";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    // 复制房间
    copyRoom() {
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      if (!buildId || !floorName) {
        this.$message({
          message: "请先选择楼层",
          type: "warning",
        });
        return false;
      }
      let aObject = this.canvas.getActiveObject();
      if (!aObject) {
        this.$message({
          message: "请先选择要复制的图形",
          type: "warning",
        });
        return;
      }
      if (aObject.type === "activeSelection") {
        aObject.clone(
          (cloned) => {
            this.copiedGraphData = cloned;
            this.copiedGraphData.forEachObject((clonedItem) => {
              if (
                clonedItem.type === "group" &&
                clonedItem.typeName === "room"
              ) {
                clonedItem.forEachObject((groupItem) => {
                  if (groupItem.type !== "rect" && groupItem.type !== "path") {
                    clonedItem.remove(groupItem);
                  } else {
                    groupItem.set({
                      fill: "rgba(204, 204, 204, 1)",
                    });
                  }
                });
                clonedItem.isExcess = false;
                clonedItem.isRecorded = false;
                clonedItem.isUse = false;
                clonedItem.uuid = "";
              }
            });
          },
          [
            "typeName",
            "pathLeft",
            "pathTop",
            "pathPoint",
            "roomType",
            "oldLeft",
            "oldTop",
            "oldCoord",
          ]
        );
      } else if (aObject.type === "group" && aObject.typeName === "room") {
        aObject.clone(
          (cloneObj) => {
            for (let j = cloneObj._objects.length - 1; j >= 0; j--) {
              let groupItem = cloneObj._objects[j];
              if (groupItem.type !== "rect" && groupItem.type !== "path") {
                cloneObj.remove(groupItem);
              } else {
                groupItem.set({
                  fill: "rgba(204, 204, 204, 1)",
                });
              }
            }
            cloneObj.isExcess = false;
            cloneObj.isRecorded = false;
            cloneObj.isUse = false;
            cloneObj.uuid = "";
            this.copiedGraphData = cloneObj;
          },
          [
            "typeName",
            "pathLeft",
            "pathTop",
            "pathPoint",
            "roomType",
            "oldLeft",
            "oldTop",
            "oldCoord",
          ]
        );
      } else {
        aObject.clone(
          (cloneObj) => {
            this.copiedGraphData = cloneObj;
          },
          ["typeName", "oldLeft", "oldTop", "oldCoord"]
        );
      }
      this.canvas.requestRenderAll();
      this.$message({
        message: "已复制",
        type: "success",
      });
    },
    // 粘贴房间
    pasteRoom() {
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      if (!buildId || !floorName) {
        this.$message({
          message: "请先选择楼层",
          type: "warning",
        });
        return false;
      }
      if (!this.copiedGraphData) {
        this.$message({
          message: "请先复制",
          type: "warning",
        });
        return false;
      }
      if (this.copiedGraphData.type === "activeSelection") {
        this.copiedGraphData.clone(
          (cloneObj) => {
            this.canvas.discardActiveObject();
            cloneObj.canvas = this.canvas;
            cloneObj.forEachObject((obj) => {
              if (obj.type === "group" && obj.typeName === "room") {
                const uuid = uuidv1();
                obj.uuid = uuid;
                this.AddHouseId.push(uuid);
              }
              this.canvas.add(obj);
            });
            cloneObj.setCoords();
            canvas.setActiveObject(cloneObj);
            canvas.requestRenderAll();
          },
          [
            "typeName",
            "pathLeft",
            "pathTop",
            "pathPoint",
            "roomType",
            "oldLeft",
            "oldTop",
            "oldCoord",
          ]
        );
      } else if (
        this.copiedGraphData.type === "group" &&
        this.copiedGraphData.typeName === "room"
      ) {
        this.copiedGraphData.clone(
          (cloneObj) => {
            this.canvas.discardActiveObject();
            const uuid = uuidv1();
            cloneObj.uuid = uuid;
            this.AddHouseId.push(uuid);
            this.canvas.add(cloneObj);
            this.canvas.setActiveObject(cloneObj);
            this.canvas.requestRenderAll();
          },
          [
            "typeName",
            "pathLeft",
            "pathTop",
            "pathPoint",
            "roomType",
            "oldLeft",
            "oldTop",
            "oldCoord",
          ]
        );
      } else {
        this.copiedGraphData.clone(
          (cloneObj) => {
            this.canvas.discardActiveObject();
            this.canvas.add(cloneObj);
            this.canvas.setActiveObject(cloneObj);
            this.canvas.requestRenderAll();
          },
          ["typeName", "oldLeft", "oldTop", "oldCoord"]
        );
      }
      this.$message({
        message: "已粘贴",
        type: "success",
      });
    },
    // 添加楼梯
    addStairs() {
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      if (!buildId || !floorName) {
        this.$message({
          message: "请先选择楼层",
          type: "warning",
        });
        return false;
      }
      fabric.loadSVGFromURL(
        require("../../assets/svg/stairs.svg"),
        (objects, options) => {
          let obj = fabric.util.groupSVGElements(objects, options);
          obj.set({
            left: 59,
            top: 260,
          });
          obj.toObject = (function (toObject) {
            return function () {
              return fabric.util.object.extend(toObject.call(this), {
                typeName: this.typeName,
              });
            };
          })(obj.toObject);
          obj.typeName = "stairs";
          this.canvas.add(obj);
          this.canvas.requestRenderAll();
          this.tool("move", 0);
        }
      );
    },
    // 添加电梯
    addElevator() {
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      if (!buildId || !floorName) {
        this.$message({
          message: "请先选择楼层",
          type: "warning",
        });
        return false;
      }
      fabric.loadSVGFromURL(
        require("../../assets/svg/elevator.svg"),
        (objects, options) => {
          let obj = fabric.util.groupSVGElements(objects, options);
          obj.set({
            left: 59,
            top: 300,
          });
          obj.toObject = (function (toObject) {
            return function () {
              return fabric.util.object.extend(toObject.call(this), {
                typeName: this.typeName,
              });
            };
          })(obj.toObject);
          obj.typeName = "elevator";
          this.canvas.add(obj);
          this.canvas.requestRenderAll();
          this.tool("move", 0);
        }
      );
    },
    // 按下delete键删除
    keyToDelete(event) {
      if (event.keyCode === 46) {
        let deleteFlag = true;
        this.activeList = this.canvas.getActiveObjects();
        if (this.activeList.length > 0) {
          this.activeList.forEach((item) => {
            if (item.type === "group" && item.typeName === "room") {
              if (this.originalHouseInfo.length > 0) {
                for (let i = 0; i < this.originalHouseInfo.length; i++) {
                  const info = this.originalHouseInfo[i];
                  if (info.houseId === item.uuid) {
                    item.actualDeleteflag = true;
                    if (info.rent !== undefined) {
                      this.$message({
                        message: "已经租借的房间不能删除",
                        type: "warning",
                      });
                      deleteFlag = false;
                      return false;
                    }
                    if (info.repairModuleId !== undefined) {
                      this.$message({
                        message: "关联维修的房间不能再次删除",
                        type: "warning",
                      });
                      deleteFlag = false;
                      return false;
                    }
                    // if (info.configurePlanDataId !== undefined || info.configurePlanDataId) {
                    //   this.$message({
                    //     message: "关联配置的房间不能删除",
                    //     type: "warning"
                    //   });
                    //   deleteFlag = false;
                    //   return false;
                    // }
                  }
                }
              }
            }
          });
          if (deleteFlag) {
            this.activeList.forEach((item) => {
              if (item.actualDeleteflag) {
                this.deleteHouseId.push(item.uuid);
              }
              for (let i = 0; i < this.AddHouseId.length; i++) {
                if (this.AddHouseId[i] === item.uuid) {
                  this.AddHouseId.splice(i, 1);
                  break;
                }
              }
              for (let i = 0; i < this.editHouseInfo.length; i++) {
                if (this.editHouseInfo[i].houseId === item.uuid) {
                  this.editHouseInfo.splice(i, 1);
                  break;
                }
              }
              this.canvas.remove(item);
            });
          }
          this.canvas.discardActiveObject();
          this.canvas.requestRenderAll();
          this.activeList = [];
        }
      }
    },
    // 浏览房间数据
    startView() {
      const yardId = sessionStorage.getItem("yardId");
      const buildId = sessionStorage.getItem("buildId");
      const floorName = sessionStorage.getItem("floorName");
      const unitId = sessionStorage.getItem("unitId") || null;
      if (
        this.editHouseInfo.length > 0 ||
        this.AddHouseId.length > 0 ||
        this.deleteHouseId.length > 0
      ) {
        this.$confirm(
          "当前楼层数据还未保存，可点击右下角保存按钮保存，如若切换楼层将清空刚才所编辑的楼层数据，是否继续此操作？",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(() => {
            if (!buildId || !floorName) {
              this.$message({
                message: "请先选择楼层",
                type: "warning",
              });
              return false;
            }
            this.$router.push({
              name: "houseGraphView",
              params: {
                yardId: yardId,
                buildId: buildId,
                floorName: floorName,
                unitId: unitId,
                flag: true,
              },
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消切换",
            });
          });
      } else {
        if (!buildId || !floorName) {
          this.$message({
            message: "请先选择楼层",
            type: "warning",
          });
          return false;
        }
        this.$router.push({
          name: "houseGraphView",
          params: {
            yardId: yardId,
            buildId: buildId,
            floorName: floorName,
            unitId: unitId,
            flag: true,
          },
        });
      }
    },
    // 关闭配置信息模态框
    configDialogClosed() {
      this.configTableData = [];
      this.configCurrentPage = 1;
      this.configTableTotal = 0;
    },
    // 配置表格切换页码
    configCurrentChange(num) {
      this.configCurrentPage = num;
      this.configureListByHouseId(this.updateIdList);
    },
    // 获取房间关联配置信息
    configureListByHouseId(houseIdList) {
      configureListByHouseId(
        this.configCurrentPage,
        this.configPageSize,
        houseIdList
      )
        .then((res) => {
          this.configTableData = res.records;
          this.configTableTotal = res.total;
          this.configTableData.forEach((item) => {
            item.configureDate = this.formatDate(item.configureDate);
          });
        })
        .catch((err) => {
          const msg = err.result ? err.result : "房间关联的配置获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
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
    // 合并
    merge() {
      let aObject = this.canvas.getActiveObject();
      if (!aObject) {
        this.$message({
          message: "请先选择要合并的线段",
          type: "warning",
        });
        return;
      }
      if (aObject.type === "activeSelection") {
        let points = [];
        let flag = true;
        aObject.forEachObject((obj) => {
          if (obj.type !== "line") {
            if (flag) {
              this.$message({
                message: "只有线段才能做合并操作",
                type: "warning",
              });
              flag = false;
            }
            return;
          }
          const coord = obj.oldCoord;
          points.push([coord.x1, coord.y1]);
          points.push([coord.x2, coord.y2]);
          this.canvas.remove(obj);
        });
        if (!flag) return false;
        let path = "";
        for (let i = 0; i < points.length; i++) {
          if (i === 0) {
            path += "M " + points[i][0] + " " + points[i][1] + " ";
          } else {
            path += "L " + points[i][0] + " " + points[i][1] + " ";
          }
        }
        path += "z";
        let pathObj = new fabric.Path(path, {
          stroke: this.color,
          strokeWidth: this.drawWidth,
          fill: "rgba(204, 204, 204, 1)",
        });
        let group = new fabric.Group([pathObj], {
          hasControls: false,
        });
        group.toObject = (function (toObject) {
          return function () {
            return fabric.util.object.extend(toObject.call(this), {
              uuid: this.uuid,
              isUse: this.isUse,
              typeName: this.typeName,
              pathLeft: this.pathLeft,
              pathTop: this.pathTop,
              pathPoint: this.pathPoint,
              roomType: this.roomType,
            });
          };
        })(group.toObject);
        this.canvas.add(group);
        const uuid = uuidv1();
        group.uuid = uuid;
        group.typeName = "room";
        group.pathLeft = group.left;
        group.pathTop = group.top;
        group.pathPoint = points;
        group.roomType = "path";
        this.canvas.requestRenderAll();
        this.AddHouseId.push(uuid);
      }
    },
  },
  created() {
    if (
      this.$route.params.yardId &&
      this.$route.params.buildId &&
      this.$route.params.floorName
    ) {
      sessionStorage.setItem("yardId", this.$route.params.yardId);
      sessionStorage.setItem("buildId", this.$route.params.buildId);
      sessionStorage.setItem("floorName", this.$route.params.floorName);
      if (this.$route.params.unitId)
        sessionStorage.setItem("unitId", this.$route.params.unitId);
      let tempObj = {};
      tempObj.yardId = this.$route.params.yardId;
      tempObj.buildId = this.$route.params.buildId;
      tempObj.floorName = this.$route.params.floorName;
      this.startEditObj = tempObj;
      this.$emit("startEdit", tempObj);
    }
  },
  mounted() {
    this.showWebgl();
    this.$nextTick(() => {
      this.init();
    });
    this.token = sessionStorage.getItem("TOKEN");
    if (this.token) {
      this.postHeaders.Authorization = `Bearer ${this.token}`;
    }
    window.addEventListener("keyup", this.keyToDelete, false);
    this.getOptionsList();
    this.getRouterList();

    eventBus.$off("getGraphJson");
    eventBus.$off("clearCanvas");
    eventBus.$off("showSaveTips");
    eventBus.$off("collapse");
    eventBus.$on("getGraphJson", (obj) => {
      isUnionConfig(obj.buildId, [obj.floorName])
        .then((res) => {
          if (res) {
            this.isConfigFloor = true;
            this.$confirm(
              "该楼层房间关联了配置，如修改或删除房间信息，可能会还原分配方案。",
              "提示",
              {
                confirmButtonText: "确定",
                showCancelButton: false,
                type: "warning",
                center: true,
              }
            ).then(() => {});
          } else {
            this.isConfigFloor = false;
          }
        })
        .catch((err) => {
          const msg = err.result ? err.result : "是否关联配置获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
      if (
        this.editHouseInfo.length > 0 ||
        this.AddHouseId.length > 0 ||
        this.deleteHouseId.length > 0
      ) {
        this.$confirm(
          "当前楼层数据还未保存，可点击右下角保存按钮保存，如若切换楼层将清空刚才所编辑的楼层数据，是否继续此操作？",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(() => {
            this.editHouseInfo = [];
            this.AddHouseId = [];
            this.deleteHouseId = [];
            this.houseVoList = [];
            this.addHouseIdList = [];
            this.delHouseIdList = [];
            this.rentHouseIds = [];
            this.clearCanvas();
            sessionStorage.setItem("yardId", obj.yardId);
            sessionStorage.setItem("buildId", obj.buildId);
            sessionStorage.setItem("floorName", obj.floorName);
            this.getHouseGraphJson(obj.buildId, obj.floorName, true);
            this.getAllHouseInfo(obj.buildId, obj.floorName);
          })
          .catch(() => {
            eventBus.$emit("activeTree");
            this.$message({
              type: "info",
              message: "已取消切换",
            });
          });
      } else {
        sessionStorage.setItem("yardId", obj.yardId);
        sessionStorage.setItem("buildId", obj.buildId);
        sessionStorage.setItem("floorName", obj.floorName);
        this.getHouseGraphJson(obj.buildId, obj.floorName, true);
        this.getAllHouseInfo(obj.buildId, obj.floorName);
      }
    });
    eventBus.$on("clearCanvas", () => {
      this.clearCanvas();
    });
    eventBus.$on("showSaveTips", (path, list) => {
      if (
        this.editHouseInfo.length > 0 ||
        this.AddHouseId.length > 0 ||
        this.deleteHouseId.length > 0
      ) {
        this.$confirm(
          "当前楼层数据还未保存，可点击右下角保存按钮保存，如若切换楼层将清空刚才所编辑的楼层数据，是否继续此操作？",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(() => {
            if (path) {
              if (path === "/login") {
                const token = sessionStorage.getItem("TOKEN");
                this.$router.replace({ path: path });
                this.$store.dispatch("Logout", token);
              } else {
                if (list) this.$store.commit("changeNavList", list);
                this.$router.push(path);
              }
            } else {
              this.editHouseInfo = [];
              this.AddHouseId = [];
              this.deleteHouseId = [];
              this.houseVoList = [];
              this.addHouseIdList = [];
              this.delHouseIdList = [];
              this.rentHouseIds = [];
              this.clearCanvas();
            }
          })
          .catch(() => {
            eventBus.$emit("activeTree");
            this.$message({
              type: "info",
              message: "已取消切换",
            });
          });
      } else {
        if (path) {
          if (path === "/login") {
            const token = sessionStorage.getItem("TOKEN");
            this.$router.replace({ path: path });
            this.$store.dispatch("Logout", token);
          } else {
            if (list) this.$store.commit("changeNavList", list);
            this.$router.push(path);
          }
        } else {
          this.editHouseInfo = [];
          this.AddHouseId = [];
          this.deleteHouseId = [];
          this.houseVoList = [];
          this.addHouseIdList = [];
          this.delHouseIdList = [];
          this.rentHouseIds = [];
          this.clearCanvas();
        }
      }
    });
    eventBus.$on("collapse", () => {
      setTimeout(() => {
        this.setZoom();
      }, 1000);
    });
  },
};
</script>

<style>
#houseGraph {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  overflow: hidden;
}

#houseGraph .tools-box {
  position: absolute;
  left: 5px;
  top: 70px;
  z-index: 100;
}

#houseGraph .tools {
  list-style: none;
  margin: 0;
  padding: 0;
}

#houseGraph .tools > li {
  width: 40px;
  height: 40px;
  border-bottom: 1px solid #c1c1c1;
  cursor: pointer;
  position: relative;
  box-sizing: border-box;
}

#houseGraph .canvasDiv {
  width: 100%;
  height: 100%;
  background: url("../../assets/imgs/canvas_bg.png") repeat;
}
#houseGraph .icon-move-black {
  background: #eaeef2 url("../../assets/imgs/move.png") no-repeat center center;
}
#houseGraph .acitive-icon.icon-move-black {
  background: #0b92df url("../../assets/imgs/move-active.png") no-repeat center
    center;
}
#houseGraph .icon-move-black:hover {
  background-color: #dbdee2;
}
#houseGraph .acitive-icon.icon-move-black:hover {
  background-color: #0b92df;
}
#houseGraph .icon-line-black {
  background: #eaeef2 url("../../assets/imgs/line.png") no-repeat center center;
}
#houseGraph .acitive-icon.icon-line-black {
  background: #0b92df url("../../assets/imgs/line-active.png") no-repeat center
    center;
}
#houseGraph .icon-line-black:hover {
  background-color: #dbdee2;
}
#houseGraph .acitive-icon.icon-line-black:hover {
  background-color: #0b92df;
}
#houseGraph .icon-dottedline-black {
  background: #eaeef2 url("../../assets/imgs/dottedline.png") no-repeat center
    center;
}
#houseGraph .acitive-icon.icon-dottedline-black {
  background: #0b92df url("../../assets/imgs/dottedline-active.png") no-repeat
    center center;
}
#houseGraph .icon-dottedline-black:hover {
  background-color: #dbdee2;
}
#houseGraph .acitive-icon.icon-dottedline-black:hover {
  background-color: #0b92df;
}
#houseGraph .icon-rectangle-black {
  background: #eaeef2 url("../../assets/imgs/rect.png") no-repeat center center;
}
#houseGraph .acitive-icon.icon-rectangle-black {
  background: #0b92df url("../../assets/imgs/rect-active.png") no-repeat center
    center;
}
#houseGraph .icon-rectangle-black:hover {
  background-color: #dbdee2;
}
#houseGraph .acitive-icon.icon-rectangle-black:hover {
  background-color: #0b92df;
}
#houseGraph .icon-text-black {
  background: #eaeef2 url("../../assets/imgs/text.png") no-repeat center center;
}
#houseGraph .acitive-icon.icon-text-black {
  background: #0b92df url("../../assets/imgs/text-active.png") no-repeat center
    center;
}
#houseGraph .icon-text-black:hover {
  background-color: #dbdee2;
}
#houseGraph .acitive-icon.icon-text-black:hover {
  background-color: #0b92df;
}
#houseGraph .icon-accessory-black {
  background: #eaeef2 url("../../assets/imgs/accessory.png") no-repeat center
    center;
}
#houseGraph .acitive-icon.icon-accessory-black {
  background: #0b92df url("../../assets/imgs/accessory-active.png") no-repeat
    center center;
}
#houseGraph .icon-accessory-black:hover {
  background-color: #dbdee2;
}
#houseGraph .acitive-icon.icon-accessory-black:hover {
  background-color: #0b92df;
}
#houseGraph .icon-door-black {
  background: #eaeef2 url("../../assets/imgs/door.png") no-repeat center center;
}
#houseGraph .acitive-icon.icon-door-black {
  background: #0b92df url("../../assets/imgs/door-active.png") no-repeat center
    center;
}
#houseGraph .icon-door-black:hover {
  background-color: #dbdee2;
}
#houseGraph .acitive-icon.icon-door-black:hover {
  background-color: #0b92df;
}
#houseGraph .icon-copy-black {
  background: #eaeef2 url("../../assets/imgs/copyRoom.png") no-repeat center
    center;
}
#houseGraph .icon-copy-black:hover {
  background-color: #dbdee2;
}
#houseGraph .icon-paste-black {
  background: #eaeef2 url("../../assets/imgs/pasteRoom.png") no-repeat center
    center;
}
#houseGraph .icon-paste-black:hover {
  background-color: #dbdee2;
}
#houseGraph .icon-stairs-black {
  background: #eaeef2 url("../../assets/imgs/stairs.png") no-repeat center
    center;
}
#houseGraph .icon-stairs-black:hover {
  background-color: #dbdee2;
}
#houseGraph .icon-elevator-black {
  background: #eaeef2 url("../../assets/imgs/elevator.png") no-repeat center
    center;
}
#houseGraph .icon-elevator-black:hover {
  background-color: #dbdee2;
}
#houseGraph .icon-merge-black {
  background: #eaeef2 url("../../assets/imgs/merge.png") no-repeat center center;
}
#houseGraph .icon-merge-black:hover {
  background-color: #dbdee2;
}
#houseGraph .subscript {
  position: absolute;
  right: 0;
  bottom: 0;
  width: 5px;
  height: 5px;
  background: url("../../assets/imgs/subscript.png") no-repeat center center;
}
#houseGraph .acitive-icon i {
  background: url("../../assets/imgs/subscript-active.png") no-repeat center
    center;
}

#houseGraph .tooltip-box {
  width: 100%;
  height: 100%;
}

#houseGraph .leader-info-content,
#houseGraph .base-info-content,
#houseGraph .rent-info {
  padding-top: 20px;
}
#houseGraph .unit-info {
  min-height: 50px;
  position: relative;
}
#houseGraph .unit-info > .el-button {
  width: calc(100% - 40px);
  margin-left: 20px;
  margin-bottom: 20px;
}
#houseGraph .unit-info-content:nth-of-type(1) {
  margin-bottom: 20px;
}

#houseGraph .unit-info-content:nth-of-type(2) {
  padding-top: 20px;
  border-top: 1px solid #dcdfe6;
}

#houseGraph .base-info-content > div {
  overflow: hidden;
  padding-bottom: 20px;
}

#houseGraph .base-info-content .content-item,
#houseGraph .leader-info-content .content-item,
#houseGraph .unit-info-content .content-item,
#houseGraph .unit-info-content .content-item-group > .group-item {
  line-height: 32px;
  margin-bottom: 20px;
  margin-right: 10px;
  float: left;
  position: relative;
}
#houseGraph .base-info-content .content-item {
  margin-bottom: 0;
}

#houseGraph .base-info-content .content-item > div,
#houseGraph .base-info-content .content-item > label,
#houseGraph .leader-info-content > .content-item > div,
#houseGraph .leader-info-content > .content-item > label,
#houseGraph .unit-info-content > .content-item > div,
#houseGraph .unit-info-content > .content-item > label,
#houseGraph .unit-info-content > .content-item-group > .group-item > div,
#houseGraph .unit-info-content > .content-item-group > .group-item > label {
  float: left;
}

#houseGraph .base-info-content .content-item > label,
#houseGraph .leader-info-content > .content-item > label,
#houseGraph .unit-info-content > .content-item > label,
#houseGraph .unit-info-content > .content-item-group > .group-item > label {
  width: 82px;
  height: 32;
  line-height: 32px;
  height: 32px;
  padding-right: 12px;
  text-align: right;
  box-sizing: border-box;
  text-align: justify;
  text-align-last: justify;
  margin-left: 20px;
}

#houseGraph .base-info-content .content-item > label::after,
#houseGraph .leader-info-content > .content-item > label::after,
#houseGraph .unit-info-content > .content-item > label::after,
#houseGraph
  .unit-info-content
  > .content-item-group
  > .group-item
  > label::after {
  display: inline-block;
  width: 100%;
  height: 0;
  content: "\0020";
  overflow: hidden;
}

#houseGraph .el-collapse-item__content {
  padding: 20px;
}

#houseGraph .delete-unit-btn {
  width: 68px;
  height: 26px;
  line-height: 26px;
  text-decoration: none;
  color: #fff;
  background-color: #f65c5f;
  text-align: center;
  border-radius: 4px;
  position: absolute;
  right: 50px;
  top: 3px;
}

#houseGraph .delete-unit-btn:hover {
  background-color: #d82a2d;
}

#houseGraph .add-item-btn {
  text-decoration: none;
  color: #3891e0;
  margin-left: 10px;
}

#houseGraph .delete-item-btn {
  text-decoration: none;
  color: #f65c5f;
  margin-left: 10px;
}

#houseGraph .add-item-btn:hover,
#houseGraph .add-item-btn:hover > i {
  color: #035cac;
}

#houseGraph .delete-item-btn:hover > i,
#houseGraph .delete-item-btn:hover {
  color: #d82a2d;
}

#houseGraph .el-form-item__content {
  width: 160px;
}

#houseGraph .el-dialog__body,
#houseGraph .el-dialog--center .el-dialog__body {
  padding: 0;
}

#houseGraph .el-collapse-item__header {
  height: 30px;
  line-height: 30px;
  padding-left: 20px;
  background-color: #bee2f3 !important;
  font-size: 14px;
  color: #333;
  position: relative;
}

#houseGraph .el-collapse-item__header > .el-icon-edit {
  font-size: 14px;
  color: #037ad7;
}

#houseGraph .el-collapse-item__header > .el-collapse-item__arrow {
  margin-right: 20px;
}

#houseGraph .content-item-unitName {
  margin-bottom: 20px;
  position: relative;
}

#houseGraph .content-item-unitName > label,
#houseGraph .content-item-unitName > div {
  float: left;
}

#houseGraph .content-item-unitName > label {
  width: 82px;
  height: 32;
  line-height: 32px;
  height: 32px;
  padding-right: 12px;
  text-align: right;
  box-sizing: border-box;
  margin-left: 20px;
  text-align: justify;
  text-align-last: justify;
}
#houseGraph .content-item-unitName > label::after {
  display: inline-block;
  width: 100%;
  height: 0;
  content: "\0020";
  overflow: hidden;
}
#houseGraph .el-dialog__footer {
  padding-top: 20px;
}
#houseGraph .threeD {
  background: #4682b4;
}
#houseGraph .btn-box > button {
  position: absolute;
  width: 110px;
  height: 40px;
  background: #3891e0;
  color: #fff;
  border-radius: 5px;
  font-size: 16px;
  font-weight: 600;
}
#houseGraph .btn-box > button:hover {
  background: #2378c4;
}
#houseGraph .btn-box > button.plane-display-btn {
  right: 20px;
  bottom: 130px;
}
#houseGraph .btn-box > button.threeD-display-btn {
  right: 20px;
  bottom: 80px;
}
#houseGraph .btn-box > button.graph-save-btn {
  right: 20px;
  bottom: 30px;
}
#houseGraph .btn-box > .active {
  background: #2378c4;
}
#houseGraph .el-form-item__error {
  left: 102px;
}
#houseGraph .upload-cad-icon {
  background: url("../../assets/imgs/cad-upload.png") no-repeat center center;
  display: inline-block;
  width: 15px;
  height: 15px;
  vertical-align: bottom;
}
#houseGraph .download-cad-icon {
  background: url("../../assets/imgs/cad-download.png") no-repeat center center;
  display: inline-block;
  width: 15px;
  height: 15px;
  vertical-align: bottom;
}
#houseGraph .accessory-main {
  width: 100%;
  border-bottom: 1px solid #e5e5e5;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  padding: 20px 20px 30px 20px;
  position: relative;
}
#houseGraph .el-upload-list {
  width: 90%;
  margin-left: 20px;
}
#houseGraph .el-dialog__footer {
  text-align: center;
}
#houseGraph .accessory-checkbox {
  position: absolute;
  top: 64px;
}
#houseGraph .accessory-checkbox > li {
  margin-bottom: 11px;
  line-height: 19px;
}
#houseGraph .plane,
#houseGraph .threeD {
  width: 100%;
  height: 100%;
}
#houseGraph .house-type-box {
  position: absolute;
  right: 20px;
  bottom: 176px;
}
#houseGraph .house-type-box > ul > li {
  line-height: 25px;
}
#houseGraph .house-type-box > ul > li > span {
  display: inline-block;
  width: 20px;
  height: 15px;
  position: relative;
  top: 2px;
}
#houseGraph .line {
  width: calc(100% - 40px);
  height: 1px;
  background: #999;
  margin-left: 20px;
}
#houseGraph .use-info > .content-item {
  padding: 20px;
}
#houseGraph .use-info .el-collapse {
  border: 0;
}
.door-icon-popper {
  min-width: 20px;
  padding: 0;
  line-height: inherit;
}
.door-icon-popper .door-icon-item {
  padding-left: 15px;
  border-bottom: 1px solid #c1c1c1;
  cursor: pointer;
  box-sizing: border-box;
  width: 108px;
  height: 25px;
  line-height: 25px;
  color: #333;
  background: url("../../assets/imgs/door-smaller-icon.png") no-repeat 5px 6px;
}
.door-icon-popper .door-icon-item:hover {
  background-color: #cfd6e2;
}
.door-icon-popper .door-icon-item:last-child {
  border-bottom: 0;
}
#houseGraph .isUser-radio-box {
  width: 160px;
}
#houseGraph .drawing-size-scale {
  position: relative;
  bottom: 24px;
  left: 24px;
}
#houseGraph .tools > li,
#houseGraph .tools > li.acitive-icon {
  background-size: 30px;
}
#houseGraph .start-view-box {
  position: absolute;
  top: 20px;
  left: 5px;
  z-index: 10;
}
#houseGraph .start-view-box .view-icon {
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
#houseGraph .start-view-box .view-icon > span {
  font-size: 26px;
}
#houseGraph .config-table-pagination-box {
  position: relative;
  height: 30px;
  border-left: 1px solid #c8c8c8;
  border-right: 1px solid #c8c8c8;
  border-bottom: 1px solid #c8c8c8;
}
#houseGraph .config-table-pagination-box > .config-table-pagination {
  position: absolute;
  right: 10px;
  top: 3px;
}
#houseGraph .config-info-dialog .dialog-content {
  padding: 20px;
}
#houseGraph .save-before-room-tips {
  margin-bottom: 5px;
}
#houseGraph .isExcess-icon-tips {
  background: url("../../assets/imgs/isExcess-icon.png") no-repeat right top;
}
#houseGraph .isRecord-icon-tips {
  background: url("../../assets/imgs/isRecord-icon.png") no-repeat right top;
}
#houseGraph .rent-info {
  padding-top: 0px;
}
#houseGraph .rent-tips {
  color: #ff0000;
  padding-bottom: 10px;
  padding-left: 24px;
  padding-top: 10px;
}
#houseGraph .rent-info-line {
  padding-bottom: 20px;
}
#houseGraph .rent-item {
  float: left;
  overflow: hidden;
  margin-right: 10px;
}
#houseGraph .rent-item > span {
  float: left;
}
#houseGraph .rent-item > span:first-child {
  width: 93px;
  text-align: right;
}
#houseGraph .rent-item > span:last-child {
  width: 160px;
}
#houseGraph .btn-box > button.threeDisabled {
  cursor: not-allowed;
  color: #fff;
  background-color: #c8c9cc;
  border-color: #c8c9cc;
}
@media only screen and (max-width: 1366px) {
  #houseGraph .tools > li {
    width: 30px;
    height: 30px;
  }
  #houseGraph .tools > li,
  #houseGraph .tools > li.acitive-icon {
    background-size: 24px;
  }
  #houseGraph .start-view-box .view-icon {
    height: 30px;
    width: 30px;
    line-height: 30px;
  }
  #houseGraph .start-view-box .view-icon > span {
    font-size: 24px;
  }
}
</style>
