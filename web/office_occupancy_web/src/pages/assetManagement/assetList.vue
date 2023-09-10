<template>
  <div id="assetRegister">
    <div class="query-box" id="queryBox">
      <div class="clearfix" style="margin-bottom:30px;">
        <div class="query-item">
          <div class="query-title">采购项目名称：</div>
          <div class="query-input">
            <el-input v-model.trim="purchasingPname"></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">状态：</div>
          <div class="query-input">
            <el-select v-model="status" placeholder="请选择状态" style="width:100%">
              <el-option
                v-for="item in statusOpts"
                :key="item.value"
                :label="item.label"
                :value="item.value"
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
      <table-component :tableData="tableData" :tableHeight="tableHeight">
        <el-table-column type="index" label="序号" width="50" align="center"></el-table-column>
        <el-table-column prop="purchasingPname" label="采购项目名称" align="center"></el-table-column>
        <el-table-column prop="applicant" label="申请人" align="center"></el-table-column>
        <el-table-column prop="operator" label="经办人" align="center"></el-table-column>
        <el-table-column prop="handleDepartment" label="经办科室" align="center"></el-table-column>
        <el-table-column prop="procurementType" label="采购分类" align="center"></el-table-column>
        <el-table-column prop="procurementMode" label="采购方式" align="center"></el-table-column>
        <el-table-column prop="agency" label="代理机构" align="center"></el-table-column>
        <el-table-column prop="organizationForm" label="组织形式" align="center"></el-table-column>
        <el-table-column prop="statusText" label="状态" align="center"></el-table-column>
        <el-table-column label="操作" width="90" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowStorage="scope.row.status === 0"
              :isShowAlter="false"
              :isShowDelete="false"
              @storageClick="storageAgin(scope.row.purchasingId)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group @addClick="add" :batchDelBtnShow="false"></table-btn-group>
        </div>
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
    <!-- 资产入库模态框 -->
    <el-dialog
      :visible.sync="enterDialogVisible"
      :close-on-click-modal="false"
      width="85%"
      @closed="enterHandleClose"
      class="enter-dialog"
    >
      <div class="enter-section-1">
        <div class="dialog-header">
          <span>采购项目名称</span>
        </div>
        <div class="dialog-body">
          <div class="introduce-box">
            <div class="baseInfo-box">
              <div class="baseInfo-title">
                <i></i> 基础信息
              </div>
              <div class="baseInfo-body">
                <el-form
                  :inline="true"
                  :model="assetBaseInfoForm"
                  :rules="baseInfoRules"
                  ref="assetBaseInfoForm"
                >
                  <el-form-item label="采购项目名称" label-width="120px" prop="purchasingPname">
                    <el-input
                      v-model.trim="assetBaseInfoForm.purchasingPname"
                      :readonly="operateType === 'edit' || baseInfoSaved"
                      size="small"
                      placeholder="请输入采购项目名称"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="申请人" label-width="120px" prop="applicant">
                    <el-input
                      v-model.trim="assetBaseInfoForm.applicant"
                      :readonly="operateType === 'edit' || baseInfoSaved"
                      size="small"
                      placeholder="请输入申请人"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="经办人" label-width="120px" prop="operator">
                    <el-input
                      v-model.trim="assetBaseInfoForm.operator"
                      :readonly="operateType === 'edit' || baseInfoSaved"
                      size="small"
                      placeholder="请输入经办人"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="经办科室" label-width="120px" prop="handleDepartment">
                    <el-input
                      v-model.trim="assetBaseInfoForm.handleDepartment"
                      :readonly="operateType === 'edit' || baseInfoSaved"
                      size="small"
                      placeholder="请输入经办科室"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="采购分类" label-width="120px" prop="procurementType">
                    <el-input
                      v-model.trim="assetBaseInfoForm.procurementType"
                      :readonly="operateType === 'edit' || baseInfoSaved"
                      size="small"
                      placeholder="请输入采购分类"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="采购方式" label-width="120px" prop="procurementMode">
                    <el-input
                      v-model.trim="assetBaseInfoForm.procurementMode"
                      :readonly="operateType === 'edit' || baseInfoSaved"
                      size="small"
                      placeholder="请输入采购方式"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="代理机构" label-width="120px" prop="agency">
                    <el-input
                      v-model.trim="assetBaseInfoForm.agency"
                      :readonly="operateType === 'edit' || baseInfoSaved"
                      size="small"
                      placeholder="请输入代理机构"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="组织形式" label-width="120px" prop="organizationForm">
                    <el-input
                      v-model.trim="assetBaseInfoForm.organizationForm"
                      :readonly="operateType === 'edit' || baseInfoSaved"
                      size="small"
                      placeholder="请输入组织形式"
                    ></el-input>
                  </el-form-item>
                </el-form>
              </div>
            </div>
            <div class="assetInfo-box">
              <div class="assetInfo-title">
                <i></i> 资产信息
                <div
                  class="assetInfo-add-btn"
                  @click="addAssetInfo"
                  v-if="operateType === 'add' && !baseInfoSaved"
                >
                  <i class="el-icon-plus"></i>添加
                </div>
              </div>
              <div class="assetInfo-body">
                <el-table
                  :data="assetTableData"
                  border
                  style="width: 100%"
                  :height="250"
                  :header-cell-style="tableHeaderCellStyle"
                  :cell-style="tableCellStyle"
                  empty-text="点击右上角+按钮，填写一条资产信息。"
                  class="asset-table"
                >
                  <el-table-column prop="brandName" label="账面资产品牌" width="180" align="left">
                    <template slot-scope="scope">
                      <el-input
                        v-model.trim="scope.row.brandName"
                        :readonly="operateType === 'edit' || baseInfoSaved"
                        size="small"
                        placeholder="请输入账面资产品牌"
                      ></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="brandMode" label="账面资产型号" width="180" align="left">
                    <template slot-scope="scope">
                      <el-input
                        v-model.trim="scope.row.brandMode"
                        :readonly="operateType === 'edit' || baseInfoSaved"
                        size="small"
                        placeholder="请输入账面资产型号"
                      ></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="procurementRecord" label="采购备案" align="left">
                    <template slot-scope="scope">
                      <el-input
                        v-model.trim="scope.row.procurementRecord"
                        :readonly="operateType === 'edit' || baseInfoSaved"
                        size="small"
                        placeholder="请输入采购备案"
                      ></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="unitPrice" label="单价" align="left">
                    <template slot-scope="scope">
                      <el-input
                        v-model.trim="scope.row.unitPrice"
                        :readonly="operateType === 'edit' || baseInfoSaved"
                        size="small"
                        placeholder="请输入单价"
                        @blur="asseLimitFloat(scope.$index, 'unitPrice', true)"
                        @keyup.native="asseLimitFloat(scope.$index, 'unitPrice')"
                      ></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="number" label="总数量" align="left">
                    <template slot-scope="scope">
                      <el-input
                        v-model.trim="scope.row.number"
                        :readonly="operateType === 'edit' || baseInfoSaved"
                        size="small"
                        placeholder="请输入总数量"
                        @blur="asseLimitInt(scope.$index, 'number')"
                        @keyup.native="asseLimitInt(scope.$index, 'number')"
                      ></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="storagedNum" label="已入库数量" align="left">
                    <template slot-scope="scope">
                      <el-input v-model.trim="scope.row.storagedNum" :readonly="true" size="small"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="unit" label="单位" align="left">
                    <template slot-scope="scope">
                      <el-input
                        v-model.trim="scope.row.unit"
                        :readonly="operateType === 'edit' || baseInfoSaved"
                        size="small"
                        placeholder="请输入单位"
                      ></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="category" label="所属类目" align="left">
                    <template slot-scope="scope">
                      <el-input
                        v-model.trim="scope.row.category"
                        :readonly="operateType === 'edit' || baseInfoSaved"
                        size="small"
                        placeholder="请输入所属类目"
                      ></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="categoryCode" label="所属类目编号" align="left">
                    <template slot-scope="scope">
                      <el-input
                        v-model.trim="scope.row.categoryCode"
                        :readonly="operateType === 'edit' || baseInfoSaved"
                        size="small"
                        placeholder="请输入类目编号"
                      ></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="purchaseAmount" label="采购金额" align="left">
                    <template slot-scope="scope">
                      <el-input
                        v-model.trim="scope.row.purchaseAmount"
                        :readonly="operateType === 'edit' || baseInfoSaved"
                        size="small"
                        placeholder="请输入采购金额"
                        @blur="asseLimitFloat(scope.$index, 'purchaseAmount', true)"
                        @keyup.native="asseLimitFloat(scope.$index, 'purchaseAmount')"
                      ></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column label="账面资产类别" width="250" align="left">
                    <template slot-scope="scope">
                      <el-select
                        v-model.trim="scope.row.selectAssetType"
                        v-if="operateType === 'add' && !baseInfoSaved"
                        filterable
                        placeholder="请选择账面资产类别"
                        size="small"
                        @change="assetTypeChangeHandle"
                      >
                        <el-option
                          v-for="item in assetsTypeOpts"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        ></el-option>
                      </el-select>
                      <el-input
                        v-model.trim="scope.row.selectAssetTypeName"
                        v-if="operateType === 'edit' || baseInfoSaved"
                        :readonly="operateType === 'edit' || baseInfoSaved"
                        size="small"
                      ></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="删除"
                    width="70"
                    align="center"
                    v-if="operateType === 'add' && !baseInfoSaved"
                  >
                    <template slot-scope="scope">
                      <single-btn-group
                        :isShowAlter="false"
                        @deleteClick="deleteAssetInfo(scope.$index)"
                      ></single-btn-group>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </div>
          <div class="verification-tips" v-show="verificationTips !== ''">
            <span>{{verificationTips}}</span>
          </div>
          <div class="next-step">
            <el-button type="primary" @click="nextStep">下一步</el-button>
          </div>
        </div>
      </div>
      <div class="enter-section-2">
        <div class="dialog-left">
          <div class="asset-tree-box">
            <el-tree
              class="tree"
              ref="tree"
              node-key="id"
              :data="assetTreeData"
              @node-click="assetTreeNodeClick"
              :render-content="renderContent"
              accordion
              v-if="treeShow"
            ></el-tree>
          </div>
          <div class="prev-step">
            <div style="margin-bottom:15px;">
              <el-button type="primary" @click="prevStep" style="width: 100px">上一步</el-button>
            </div>
          </div>
        </div>
        <div class="dialog-right">
          <div class="dialog-header">
            <span>商品信息填写</span>
          </div>
          <div class="dialog-body" v-if="showAssetForm">
            <div class="dialog-body-form">
              <div v-if="isDetailPage">
                <asset-detail
                  :data="assetDetailData"
                  :detailAssetForm="detailAssetForm"
                  :renderType="typeRender[assetDetailType]"
                  :assetDetailType="assetDetailType"
                ></asset-detail>
              </div>
              <div v-if="!isDetailPage">
                <div class="dialog-body-title">
                  <i></i>
                  {{privateTitle}}
                </div>
                <el-form
                  :inline="true"
                  :model="currentAssetForm"
                  :rules="currentAssetRules"
                  :ref="activeAssetFormName"
                  :validate-on-rule-change="false"
                  class="asset-form"
                >
                  <el-form-item
                    v-for="item in typeRender[assetCommodityType]"
                    :key="item.key"
                    :label="item.text"
                    label-width="135px"
                    :prop="item.key"
                    v-show="item.show"
                  >
                    <!-- 文本框 (带事件)-->
                    <el-input
                      v-model.trim="currentAssetForm[item.key]"
                      :placeholder="`请输入${item.text}`"
                      v-if="item.form === 'input' && item.blurEvent !== undefined"
                      :disabled="item.readonly"
                      size="small"
                      @blur="item.blurEvent"
                    ></el-input>
                    <!-- 文本框 (不带事件)-->
                    <el-input
                      v-model.trim="currentAssetForm[item.key]"
                      :placeholder="`请输入${item.text}`"
                      v-if="item.form === 'input' && item.blurEvent === undefined"
                      :disabled="item.readonly"
                      size="small"
                    ></el-input>
                    <!-- 下拉选择（带事件） -->
                    <el-select
                      v-model="currentAssetForm[item.key]"
                      :placeholder="`请选择${item.text}`"
                      v-if="item.form === 'select' && item.changeEvent !== undefined"
                      size="small"
                      style="width:200px"
                      :disabled="item.readonly"
                      @change="item.changeEvent"
                    >
                      <el-option
                        v-for="o in item.options"
                        :key="o.value"
                        :label="o.label"
                        :value="o.value"
                      ></el-option>
                    </el-select>
                    <!-- 下拉选择（不带事件） -->
                    <el-select
                      v-model="currentAssetForm[item.key]"
                      :placeholder="`请选择${item.text}`"
                      v-if="item.form === 'select' && item.changeEvent === undefined"
                      size="small"
                      style="width:200px"
                      :disabled="item.readonly"
                    >
                      <el-option
                        v-for="o in item.options"
                        :key="o.value"
                        :label="o.label"
                        :value="o.value"
                      ></el-option>
                    </el-select>
                    <!-- 日期 -->
                    <el-date-picker
                      v-model="currentAssetForm[item.key]"
                      type="date"
                      placeholder="选择日期"
                      v-if="item.form === 'Date'"
                      size="small"
                      style="width:200px"
                      :disabled="item.readonly"
                      value-format="yyyy-MM-dd HH:mm:ss"
                    ></el-date-picker>
                    <!-- 下拉选择树 -->
                    <tree-Select
                      v-if="item.form === 'selectTree'"
                      :ref="item.key"
                      :inputHeight="'32px'"
                      :inputWidth="'200px'"
                      :placeholderText="`请选择${item.text}`"
                      :treeName="item.key"
                      :assetsType="activeAssetForm"
                      :showSelectTree="showSelectTree"
                      @select-tree-checked="seletTreeChecked"
                    ></tree-Select>
                  </el-form-item>
                </el-form>
                <div class="dialog-body-title" v-show="false">
                  <i class="photo"></i>
                  上传图片
                </div>
                <div class="dialog-upload-box" v-show="false">
                  <label class="el-form-item__label" style="width: 110px;">资产图片</label>
                  <el-upload
                    ref="upload"
                    :headers="postHeaders"
                    :action="uploadImgUrl"
                    list-type="picture-card"
                    :file-list="fileList"
                    :on-remove="handleRemove"
                    :auto-upload="false"
                    :on-success="handleSuccess"
                    :on-change="handleChange"
                    :on-error="handleError"
                    :accept="acceptImgType"
                    :limit="limitImgNum"
                    :on-exceed="handleExceed"
                    multiple
                  >
                    <i class="el-icon-plus"></i>
                  </el-upload>
                </div>
              </div>
              <div class="dialog-btn-box" v-if="!isDetailPage">
                <cancel-btn @clickBtn="cancelForm" :fontSize="14" text="重置"></cancel-btn>
                <confirm-btn @clickBtn="submitForm" :fontSize="14"></confirm-btn>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getPurchasingList,
  addPurchasing,
  addAssets,
  updateAssert,
  getPurchasingDetail,
  getAssetDetailInfo
} from './api';
import { uploadUrl } from "@/axios/commonApi";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import yearSelect from "@/components/yearPicker/yearSelect";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import assetDetail from "@/components/assets/assetsDetail";
import treeSelect from "@/components/treeSelect/treeSelect_6.vue";
export default {
  components: {
    singleBtnGroup,
    tableBtnGroup,
    tablePagination,
    tableComponent,
    yearSelect,
    cancelBtn,
    confirmBtn,
    searchBtn,
    resetBtn,
    assetDetail,
    treeSelect
  },
  data() {
    return {
      storagedTreeArray: [], // 已入库的id
      showSelectTree: true,
      postHeaders: {
        Authorization: ''
      },
      uploadImgUrl: `${uploadUrl}?uploadType=assets`,
      acceptImgType: "image/jpeg,image/gif,image/png,image/bmp",
      limitImgNum: 3,
      uploadSuccessImgNum: 0,
      uploadImgNum: 0,
      assetsImgList: [],
      fileList: [],
      purchasingPname: '', // 采购项目名称
      status: '', // 状态
      assetsType: '',
      statusOpts: [
        {
          value: '',
          label: '全部'
        },
        {
          value: 0,
          label: '未完成'
        },
        {
          value: 1,
          label: '已完成'
        }
      ],
      assetsTypeOpts: [
        {
          value: 1,
          label: '车辆资产卡片'
        },
        {
          value: 2,
          label: '房屋资产卡片'
        },
        {
          value: 3,
          label: '构筑物资产卡片'
        },
        {
          value: 4,
          label: '家具/用具/装具资产卡片'
        },
        {
          value: 5,
          label: '通用设备资产卡片'
        },
        {
          value: 6,
          label: '专用设备资产卡片'
        },
        {
          value: 7,
          label: '土地使用权资产卡片'
        }
      ],
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      tableData: [],
      operateType: "add",
      enterDialogVisible: false,
      detailDialogVisible: false,
      selectAsset: '',
      currentPurchasingPname: '',
      assetBaseInfoForm: {
        purchasingPname: '',
        applicant: '',
        operator: '',
        handleDepartment: '',
        procurementType: '',
        procurementMode: '',
        agency: '',
        organizationForm: ''
      },
      assetCommodityType: '',
      currentAssetForm: {},
      currentAssetRules: {},
      typeRender: {
        VehicleCard: [
          {
            type: 'String',
            key: 'assetNameComn',
            text: '资产名称',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'categoryName',
            text: '资产分类名称',
            form: 'selectTree',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'categoryCodeComn',
            text: '资产分类代码',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetUnit',
            text: '单位',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'procurementModeComn',
            text: '采购组织形式',
            form: 'select',
            options: [
              {
                label: '政府集中采购',
                value: '政府集中采购'
              },
              {
                label: '部门集中采购',
                value: '部门集中采购'
              },
              {
                label: '分散采购',
                value: '分散采购'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'acquireWayComn',
            text: '取得方式',
            form: 'select',
            options: [
              {
                label: '新购',
                value: '新购'
              },
              {
                label: '调拨',
                value: '调拨'
              },
              {
                label: '接受捐赠',
                value: '接受捐赠'
              },
              {
                label: '自建',
                value: '自建'
              },
              {
                label: '置换',
                value: '置换'
              },
              {
                label: '盘盈',
                value: '盘盈'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Date',
            key: 'acquireDateComn',
            text: '取得日期',
            form: 'Date',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'vehicleProducingArea',
            text: '车辆产地',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'specificationType',
            text: '规格型号',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetBrand',
            text: '品牌',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'vehicleIdentificationNumber',
            text: '车辆识别号',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'licensePlateNumber',
            text: '车牌号',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'dischargeVolume',
            text: '汽车排气量',
            form: 'select',
            options: [
              {
                label: '1.6（含）升以下',
                value: '1.6（含）升以下'
              },
              {
                label: '1.6-1.8（含）升',
                value: '1.6-1.8（含）升'
              },
              {
                label: '2.0（含）-2.5升',
                value: '2.0（含）-2.5升'
              },
              {
                label: '2.5（含）升以上',
                value: '2.5（含）升以上'
              },
              {
                label: '新能源',
                value: '新能源'
              },
              {
                label: '柴油车',
                value: '柴油车'
              }
            ],
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'engineNumber',
            text: '发动机号',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'vehicleDrivingPermit',
            text: '车辆行驶证',
            form: 'select',
            options: [
              {
                label: '有',
                value: '有'
              },
              {
                label: '无',
                value: '无'
              }
            ],
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'assetSeller',
            text: '销售商',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'contractNumber',
            text: '合同编号',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'invoiceNumber',
            text: '发票号',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'Date',
            key: 'warrantyDeadline',
            text: '保修截止日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'compilationSituation',
            text: '编制情况',
            form: 'select',
            options: [
              {
                label: '在编',
                value: '在编'
              },
              {
                label: '不在编',
                value: '不在编'
              },
              {
                label: '未核定车编',
                value: '未核定车编'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'financialAppropriationComn',
            text: '财政拨款（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'nonFinancialAppropriationComn',
            text: '非财政拨款（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'financialEntryStatusComn',
            text: '财务入账状态',
            form: 'select',
            options: [
              {
                label: '已入账',
                value: '已入账'
              },
              {
                label: '未入账',
                value: '未入账'
              }
            ],
            show: true,
            readonly: false,
            required: true,
            changeEvent: this.EntryStatusChange
          },
          {
            type: 'Date',
            key: 'financialEntryDateComn',
            text: '财务入账日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'valueTypeComn',
            text: '价值类型',
            form: 'select',
            options: [
              {
                label: '原值',
                value: '原值'
              },
              {
                label: '暂估值',
                value: '暂估值'
              },
              {
                label: '评估值',
                value: '评估值'
              },
              {
                label: '重置值',
                value: '重置值'
              },
              {
                label: '名义金额',
                value: '名义金额'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            key: 'assetNumberComn',
            text: '数量',
            show: false,
            default: 1
          },
          {
            type: 'Float',
            key: 'assetWorthComn',
            text: '价值（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'accountEntryComn',
            text: '入账科目',
            form: 'select',
            options: [
              {
                label: '固定资产',
                value: '固定资产'
              }
            ],
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'usageStatusComn',
            text: '使用状况',
            form: 'select',
            options: [
              {
                label: '自用',
                value: '自用'
              },
              {
                label: '闲置',
                value: '闲置'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Date',
            key: 'commissioningDateComn',
            text: '投入使用日期',
            form: 'Date',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'useManagementComn',
            text: '使用/管理部门',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetUser',
            text: '使用人',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Integer',
            key: 'accumulatedMileage',
            text: '累计行驶里程',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'vehicleUse',
            text: '车辆用途',
            form: 'select',
            options: [
              {
                label: '副部（省）级及以上领导用车',
                value: '副部（省）级及以上领导用车'
              },
              {
                label: '主要领导干部用车',
                value: '主要领导干部用车'
              },
              {
                label: '机要通信用车',
                value: '机要通信用车'
              },
              {
                label: '应急保障用车',
                value: '应急保障用车'
              },
              {
                label: '执法执勤用车',
                value: '执法执勤用车'
              },
              {
                label: '特种专业技术用车',
                value: '特种专业技术用车'
              },
              {
                label: '离退休干部用车',
                value: '离退休干部用车'
              },
              {
                label: '其他用车',
                value: '其他用车'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'depreciationStatusComn',
            text: '折旧状态',
            form: 'select',
            options: [
              {
                label: '提折旧/摊销',
                value: '提折旧/摊销'
              },
              {
                label: '不提折旧/摊销',
                value: '不提折旧/摊销'
              },
              {
                label: '已完成折旧/摊销',
                value: '已完成折旧/摊销'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetRemarksComn',
            text: '备注',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'assetHolder',
            text: '持证人',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'Date',
            key: 'registrationDate',
            text: '登记日期',
            form: 'Date',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Integer',
            key: 'seatNumber',
            text: '座位数',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'isCIP',
            text: '是否在建工程转入',
            form: 'select',
            options: [
              {
                label: '是',
                value: '是'
              },
              {
                label: '否',
                value: '否'
              }
            ],
            show: true,
            readonly: false,
            required: true
          }
        ],
        HouseCard: [
          {
            type: 'String',
            key: 'assetNameComn',
            text: '资产名称',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'categoryName',
            text: '资产分类名称',
            form: 'selectTree',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'categoryCodeComn',
            text: '资产分类代码',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetUnit',
            text: '单位',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'seatingPosition',
            text: '坐落位置',
            form: 'input',
            show: true,
            readonly: false,
            required: true,
            minLimit: 12
          },
          {
            type: 'Date',
            key: 'acquireDateComn',
            text: '取得日期',
            form: 'Date',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'acquireWayComn',
            text: '取得方式',
            form: 'select',
            options: [
              {
                label: '新购',
                value: '新购'
              },
              {
                label: '调拨',
                value: '调拨'
              },
              {
                label: '接受捐赠',
                value: '接受捐赠'
              },
              {
                label: '自建',
                value: '自建'
              },
              {
                label: '置换',
                value: '置换'
              },
              {
                label: '盘盈',
                value: '盘盈'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'propertyRight',
            text: '产权形式',
            form: 'select',
            options: [
              {
                label: '有产权',
                value: '有产权'
              },
              {
                label: '无产权',
                value: '无产权'
              },
              {
                label: '产权待界定',
                value: '产权待界定'
              }
            ],
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'ownershipNature',
            text: '权属性质',
            form: 'select',
            options: [
              {
                label: '国有',
                value: '国有'
              },
              {
                label: '私有',
                value: '私有'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'ownershipCertificate',
            text: '房屋权属证明',
            form: 'input',
            show: true,
            readonly: false,
          },
          {
            type: 'String',
            key: 'ownershipAge',
            text: '权属年限',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'ownershipId',
            text: '权属证号',
            form: 'input',
            show: true,
            readonly: false,
          },
          {
            type: 'String',
            key: 'propertyOwner',
            text: '权属人',
            form: 'input',
            show: true,
            readonly: false,
          },
          {
            type: 'String',
            key: 'designPurpose',
            text: '设计用途',
            form: 'select',
            options: [
              {
                label: '办公',
                value: '办公'
              },
              {
                label: '住宅',
                value: '住宅'
              },
              {
                label: '商业',
                value: '商业'
              },
              {
                label: '自用',
                value: '自用'
              }
            ],
            show: true,
            readonly: false,
          },
          {
            type: 'String',
            key: 'buildingStructure',
            text: '建筑结构',
            form: 'select',
            options: [
              {
                label: '钢结构',
                value: '钢结构'
              },
              {
                label: '钢筋混凝土结构',
                value: '钢筋混凝土结构'
              },
              {
                label: '砖混结构',
                value: '砖混结构'
              },
              {
                label: '砖木结构',
                value: '砖木结构'
              },
              {
                label: '其他',
                value: '其他'
              },
              {
                label: '简易房',
                value: '简易房'
              }
            ],
            show: true,
            readonly: false,
          },
          {
            type: 'Float',
            key: 'floorArea',
            text: '建筑面积',
            form: 'input',
            show: true,
            readonly: false,
          },
          {
            type: 'Float',
            key: 'heatingArea',
            text: '供暖面积',
            form: 'input',
            show: true,
            readonly: false,
          },
          {
            type: 'Date',
            key: 'completionDate',
            text: '竣工日期',
            form: 'Date',
            show: true,
            readonly: false,
          },
          {
            type: 'Date',
            key: 'issueDate',
            text: '发证日期',
            form: 'Date',
            show: true,
            readonly: false,
          },
          {
            type: 'Float',
            key: 'financialAppropriationComn',
            text: '财政拨款（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'nonFinancialAppropriationComn',
            text: '非财政拨款（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'financialEntryStatusComn',
            text: '财务入账状态',
            form: 'select',
            options: [
              {
                label: '已入账',
                value: '已入账'
              },
              {
                label: '未入账',
                value: '未入账'
              }
            ],
            show: true,
            readonly: false,
            required: true,
            changeEvent: this.EntryStatusChange
          },
          {
            type: 'Date',
            key: 'financialEntryDateComn',
            text: '财务入账日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'valueTypeComn',
            text: '价值类型',
            form: 'select',
            options: [
              {
                label: '原值',
                value: '原值'
              },
              {
                label: '暂估值',
                value: '暂估值'
              },
              {
                label: '评估值',
                value: '评估值'
              },
              {
                label: '重置值',
                value: '重置值'
              },
              {
                label: '名义金额',
                value: '名义金额'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            key: 'assetNumberComn',
            text: '数量',
            show: false,
            default: 1
          },
          {
            type: 'Float',
            key: 'assetWorthComn',
            text: '价值（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'averagePrice',
            text: '均价',
            form: 'input',
            show: true,
            readonly: false,
          },
          {
            type: 'String',
            key: 'accountEntryComn',
            text: '入账科目',
            form: 'select',
            options: [
              {
                label: '固定资产',
                value: '固定资产'
              }
            ],
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'usageStatusComn',
            text: '使用状况',
            form: 'select',
            options: [
              {
                label: '自用',
                value: '自用'
              },
              {
                label: '闲置',
                value: '闲置'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Date',
            key: 'commissioningDateComn',
            text: '投入使用日期',
            form: 'Date',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'useManagementComn',
            text: '使用/管理部门',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'depreciationStatusComn',
            text: '折旧状态',
            form: 'select',
            options: [
              {
                label: '提折旧/摊销',
                value: '提折旧/摊销'
              },
              {
                label: '不提折旧/摊销',
                value: '不提折旧/摊销'
              },
              {
                label: '已完成折旧/摊销',
                value: '已完成折旧/摊销'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'dangerousArea',
            text: '危房面积',
            form: 'input',
            show: true,
            readonly: false,
          },
          {
            type: 'Float',
            key: 'selfUseWorth',
            text: '自用价值',
            form: 'input',
            show: true,
            readonly: false,
          },
          {
            type: 'Float',
            key: 'rentalValue',
            text: '出租价值',
            form: 'input',
            show: true,
            readonly: false,
          },
          {
            type: 'Float',
            key: 'lendingValue',
            text: '出借价值',
            form: 'input',
            show: true,
            readonly: false,
          },
          {
            type: 'Float',
            key: 'idleValue',
            text: '闲置价值',
            form: 'input',
            show: true,
            readonly: false,
          },
          {
            type: 'Float',
            key: 'otherValue',
            text: '其他价值',
            form: 'input',
            show: true,
            readonly: false,
          },
          {
            type: 'String',
            key: 'assetRemarksComn',
            text: '备注',
            form: 'input',
            show: true,
            readonly: false
          }
        ],
        ConstructionCard: [
          {
            key: 'assetNumberComn',
            text: '数量',
            show: false,
            default: 1
          },
          {
            type: 'String',
            key: 'assetNameComn',
            text: '资产名称',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'categoryName',
            text: '资产分类名称',
            form: 'selectTree',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'categoryCodeComn',
            text: '资产分类代码',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetUnit',
            text: '单位',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'seatingPosition',
            text: '坐落位置',
            form: 'input',
            show: true,
            readonly: false,
            required: true,
            minLimit: 12
          },
          {
            type: 'Date',
            key: 'acquireDateComn',
            text: '取得日期',
            form: 'Date',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'acquireWayComn',
            text: '取得方式',
            form: 'select',
            options: [
              {
                label: '新购',
                value: '新购'
              },
              {
                label: '调拨',
                value: '调拨'
              },
              {
                label: '接受捐赠',
                value: '接受捐赠'
              },
              {
                label: '自建',
                value: '自建'
              },
              {
                label: '置换',
                value: '置换'
              },
              {
                label: '盘盈',
                value: '盘盈'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'ownershipNature',
            text: '权属性质',
            form: 'select',
            options: [
              {
                label: '国有',
                value: '国有'
              },
              {
                label: '私有',
                value: '私有'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'buildingStructure',
            text: '建筑结构',
            form: 'select',
            options: [
              {
                label: '钢结构',
                value: '钢结构'
              },
              {
                label: '钢筋混凝土结构',
                value: '钢筋混凝土结构'
              },
              {
                label: '砖混结构',
                value: '砖混结构'
              },
              {
                label: '砖木结构',
                value: '砖木结构'
              },
              {
                label: '其他',
                value: '其他'
              },
              {
                label: '简易房',
                value: '简易房'
              }
            ],
            show: true,
            readonly: false,
          },
          {
            type: 'Date',
            key: 'completionDate',
            text: '竣工日期',
            form: 'Date',
            show: true,
            readonly: false,
          },
          {
            type: 'Float',
            key: 'financialAppropriationComn',
            text: '财政拨款（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'nonFinancialAppropriationComn',
            text: '非财政拨款（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'financialEntryStatusComn',
            text: '财务入账状态',
            form: 'select',
            options: [
              {
                label: '已入账',
                value: '已入账'
              },
              {
                label: '未入账',
                value: '未入账'
              }
            ],
            show: true,
            readonly: false,
            required: true,
            changeEvent: this.EntryStatusChange
          },
          {
            type: 'Date',
            key: 'financialEntryDateComn',
            text: '财务入账日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'valueTypeComn',
            text: '价值类型',
            form: 'select',
            options: [
              {
                label: '原值',
                value: '原值'
              },
              {
                label: '暂估值',
                value: '暂估值'
              },
              {
                label: '评估值',
                value: '评估值'
              },
              {
                label: '重置值',
                value: '重置值'
              },
              {
                label: '名义金额',
                value: '名义金额'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'assetWorthComn',
            text: '价值（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'accountEntryComn',
            text: '入账科目',
            form: 'select',
            options: [
              {
                label: '固定资产',
                value: '固定资产'
              }
            ],
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'usageStatusComn',
            text: '使用状况',
            form: 'select',
            options: [
              {
                label: '自用',
                value: '自用'
              },
              {
                label: '闲置',
                value: '闲置'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'depreciationStatusComn',
            text: '折旧状态',
            form: 'select',
            options: [
              {
                label: '提折旧/摊销',
                value: '提折旧/摊销'
              },
              {
                label: '不提折旧/摊销',
                value: '不提折旧/摊销'
              },
              {
                label: '已完成折旧/摊销',
                value: '已完成折旧/摊销'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Date',
            key: 'commissioningDateComn',
            text: '投入使用日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'assetUnit',
            text: '计量单位',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'useManagementComn',
            text: '使用/管理部门',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetRemarksComn',
            text: '备注',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'isCIP',
            text: '是否在建工程转入',
            form: 'select',
            options: [
              {
                label: '是',
                value: '是'
              },
              {
                label: '否',
                value: '否'
              }
            ],
            show: true,
            readonly: false,
            required: true
          }
        ],
        FurnitureCard: [
          {
            type: 'String',
            key: 'assetNameComn',
            text: '资产名称',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'categoryName',
            text: '资产分类名称',
            form: 'selectTree',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'categoryCodeComn',
            text: '资产分类代码',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetUnit',
            text: '单位',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'storageLocation',
            text: '存放地点',
            form: 'selectTree',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'procurementModeComn',
            text: '采购组织形式',
            form: 'select',
            options: [
              {
                label: '政府集中采购',
                value: '政府集中采购'
              },
              {
                label: '部门集中采购',
                value: '部门集中采购'
              },
              {
                label: '分散采购',
                value: '分散采购'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'acquireWayComn',
            text: '取得方式',
            form: 'select',
            options: [
              {
                label: '新购',
                value: '新购'
              },
              {
                label: '调拨',
                value: '调拨'
              },
              {
                label: '接受捐赠',
                value: '接受捐赠'
              },
              {
                label: '自建',
                value: '自建'
              },
              {
                label: '置换',
                value: '置换'
              },
              {
                label: '盘盈',
                value: '盘盈'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Date',
            key: 'acquireDateComn',
            text: '取得日期',
            form: 'Date',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetBrand',
            text: '品牌',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetSeller',
            text: '销售商',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'Date',
            key: 'warrantyDeadline',
            text: '保修截止日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            key: 'assetNumberComn',
            text: '数量',
            show: false,
            default: 1
          },
          {
            type: 'Float',
            key: 'financialAppropriationComn',
            text: '财政拨款（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'nonFinancialAppropriationComn',
            text: '非财政拨款（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'financialEntryStatusComn',
            text: '财务入账状态',
            form: 'select',
            options: [
              {
                label: '已入账',
                value: '已入账'
              },
              {
                label: '未入账',
                value: '未入账'
              }
            ],
            show: true,
            readonly: false,
            required: true,
            changeEvent: this.EntryStatusChange
          },
          {
            type: 'Date',
            key: 'financialEntryDateComn',
            text: '财务入账日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'valueTypeComn',
            text: '价值类型',
            form: 'select',
            options: [
              {
                label: '原值',
                value: '原值'
              },
              {
                label: '暂估值',
                value: '暂估值'
              },
              {
                label: '评估值',
                value: '评估值'
              },
              {
                label: '重置值',
                value: '重置值'
              },
              {
                label: '名义金额',
                value: '名义金额'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'assetWorthComn',
            text: '价值（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'unitPrice',
            text: '单价',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'accountEntryComn',
            text: '入账科目',
            form: 'select',
            options: [
              {
                label: '固定资产',
                value: '固定资产'
              }
            ],
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'usageStatusComn',
            text: '使用状况',
            form: 'select',
            options: [
              {
                label: '自用',
                value: '自用'
              },
              {
                label: '闲置',
                value: '闲置'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'useManagementComn',
            text: '使用/管理部门',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },

          {
            type: 'String',
            key: 'assetUser',
            text: '使用人',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Date',
            key: 'commissioningDateComn',
            text: '投入使用日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'specificationType',
            text: '规格型号',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'depreciationStatusComn',
            text: '折旧状态',
            form: 'select',
            options: [
              {
                label: '提折旧/摊销',
                value: '提折旧/摊销'
              },
              {
                label: '不提折旧/摊销',
                value: '不提折旧/摊销'
              },
              {
                label: '已完成折旧/摊销',
                value: '已完成折旧/摊销'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetRemarksComn',
            text: '备注',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'isCIP',
            text: '是否在建工程转入',
            form: 'select',
            options: [
              {
                label: '是',
                value: '是'
              },
              {
                label: '否',
                value: '否'
              }
            ],
            show: true,
            readonly: false,
            required: true
          }
        ],
        FlexibleUnitCard: [
          {
            type: 'String',
            key: 'assetNameComn',
            text: '资产名称',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'categoryName',
            text: '资产分类名称',
            form: 'selectTree',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'categoryCodeComn',
            text: '资产分类代码',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetUnit',
            text: '单位',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'storageLocation',
            text: '存放地点',
            form: 'selectTree',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'procurementModeComn',
            text: '采购组织形式',
            form: 'select',
            options: [
              {
                label: '政府集中采购',
                value: '政府集中采购'
              },
              {
                label: '部门集中采购',
                value: '部门集中采购'
              },
              {
                label: '分散采购',
                value: '分散采购'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'acquireWayComn',
            text: '取得方式',
            form: 'select',
            options: [
              {
                label: '新购',
                value: '新购'
              },
              {
                label: '调拨',
                value: '调拨'
              },
              {
                label: '接受捐赠',
                value: '接受捐赠'
              },
              {
                label: '自建',
                value: '自建'
              },
              {
                label: '置换',
                value: '置换'
              },
              {
                label: '盘盈',
                value: '盘盈'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'equipmentUse',
            text: '设备用途',
            form: 'select',
            options: [
              {
                label: '办公',
                value: '办公'
              },
              {
                label: '教学',
                value: '教学'
              },
              {
                label: '培训',
                value: '培训'
              },
              {
                label: '执法',
                value: '执法'
              },
              {
                label: '窗口',
                value: '窗口'
              },
              {
                label: '专用',
                value: '专用'
              },
              {
                label: '其他',
                value: '其他'
              },
              {
                label: '医疗',
                value: '医疗'
              },
              {
                label: '科研',
                value: '科研'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Date',
            key: 'acquireDateComn',
            text: '取得日期',
            form: 'Date',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetBrand',
            text: '品牌',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetManufacturer',
            text: '生产厂家',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'specificationType',
            text: '规格型号',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetSeller',
            text: '销售商',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'contractNumber',
            text: '合同编号',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'invoiceNumber',
            text: '发票号',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'Date',
            key: 'warrantyDeadline',
            text: '保修截止日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'Float',
            key: 'financialAppropriationComn',
            text: '财政拨款（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'nonFinancialAppropriationComn',
            text: '非财政拨款（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'financialEntryStatusComn',
            text: '财务入账状态',
            form: 'select',
            options: [
              {
                label: '已入账',
                value: '已入账'
              },
              {
                label: '未入账',
                value: '未入账'
              }
            ],
            show: true,
            readonly: false,
            required: true,
            changeEvent: this.EntryStatusChange
          },
          {
            type: 'Date',
            key: 'financialEntryDateComn',
            text: '财务入账日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'valueTypeComn',
            text: '价值类型',
            form: 'select',
            options: [
              {
                label: '原值',
                value: '原值'
              },
              {
                label: '暂估值',
                value: '暂估值'
              },
              {
                label: '评估值',
                value: '评估值'
              },
              {
                label: '重置值',
                value: '重置值'
              },
              {
                label: '名义金额',
                value: '名义金额'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            key: 'assetNumberComn',
            text: '数量',
            show: false,
            default: 1
          },
          {
            type: 'Float',
            key: 'assetWorthComn',
            text: '价值（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'accountEntryComn',
            text: '入账科目',
            form: 'select',
            options: [
              {
                label: '固定资产',
                value: '固定资产'
              }
            ],
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'usageStatusComn',
            text: '使用状况',
            form: 'select',
            options: [
              {
                label: '自用',
                value: '自用'
              },
              {
                label: '闲置',
                value: '闲置'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Date',
            key: 'commissioningDateComn',
            text: '投入使用日期',
            form: 'Date',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'depreciationStatusComn',
            text: '折旧状态',
            form: 'select',
            options: [
              {
                label: '提折旧/摊销',
                value: '提折旧/摊销'
              },
              {
                label: '不提折旧/摊销',
                value: '不提折旧/摊销'
              },
              {
                label: '已完成折旧/摊销',
                value: '已完成折旧/摊销'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'useManagementComn',
            text: '使用/管理部门',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },

          {
            type: 'String',
            key: 'assetUser',
            text: '使用人',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetRemarksComn',
            text: '备注',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'fuselageSerialNumber',
            text: '机身序列号',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'isCIP',
            text: '是否在建工程转入',
            form: 'select',
            options: [
              {
                label: '是',
                value: '是'
              },
              {
                label: '否',
                value: '否'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'configStandardName',
            text: '配置分类标准名称',
            form: 'select',
            options: [
              {
                label: '台式机',
                value: '台式机'
              },
              {
                label: '台式机，便携式计算机',
                value: '台式机，便携式计算机'
              },
              {
                label: '便携式计算机',
                value: '便携式计算机'
              },
              {
                label: '打印设备',
                value: '打印设备'
              },
              {
                label: '扫描仪',
                value: '扫描仪'
              },
              {
                label: '复印机',
                value: '复印机'
              },
              {
                label: '投影仪',
                value: '投影仪'
              },
              {
                label: '多功能一体机',
                value: '多功能一体机'
              },
              {
                label: '照相机及器材',
                value: '照相机及器材'
              },
              {
                label: '碎纸机',
                value: '碎纸机'
              },
              {
                label: '空气调节器，制冷空调设备，制冷电器，冷库制冷设备',
                value: '空气调节器，制冷空调设备，制冷电器，冷库制冷设备'
              },
              {
                label: '传真通信设备',
                value: '传真通信设备'
              },
              {
                label: '摄像机',
                value: '摄像机'
              }
            ],
            show: true,
            readonly: false
          }
        ],
        DedicatedDevicesCard: [
          {
            type: 'String',
            key: 'assetNameComn',
            text: '资产名称',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'categoryName',
            text: '资产分类名称',
            form: 'selectTree',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'categoryCodeComn',
            text: '资产分类代码',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetUnit',
            text: '单位',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'storageLocation',
            text: '存放地点',
            form: 'selectTree',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'procurementModeComn',
            text: '采购组织形式',
            form: 'select',
            options: [
              {
                label: '政府集中采购',
                value: '政府集中采购'
              },
              {
                label: '部门集中采购',
                value: '部门集中采购'
              },
              {
                label: '分散采购',
                value: '分散采购'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'acquireWayComn',
            text: '取得方式',
            form: 'select',
            options: [
              {
                label: '新购',
                value: '新购'
              },
              {
                label: '调拨',
                value: '调拨'
              },
              {
                label: '接受捐赠',
                value: '接受捐赠'
              },
              {
                label: '自建',
                value: '自建'
              },
              {
                label: '置换',
                value: '置换'
              },
              {
                label: '盘盈',
                value: '盘盈'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Date',
            key: 'acquireDateComn',
            text: '取得日期',
            form: 'Date',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetBrand',
            text: '品牌',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetManufacturer',
            text: '生产厂家',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'specificationType',
            text: '规格型号',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetSeller',
            text: '销售商',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'contractNumber',
            text: '合同编号',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'invoiceNumber',
            text: '发票号',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'Date',
            key: 'warrantyDeadline',
            text: '保修截止日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'Float',
            key: 'financialAppropriationComn',
            text: '财政拨款（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'nonFinancialAppropriationComn',
            text: '非财政拨款（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'financialEntryStatusComn',
            text: '财务入账状态',
            form: 'select',
            options: [
              {
                label: '已入账',
                value: '已入账'
              },
              {
                label: '未入账',
                value: '未入账'
              }
            ],
            show: true,
            readonly: false,
            required: true,
            changeEvent: this.EntryStatusChange
          },
          {
            type: 'Date',
            key: 'financialEntryDateComn',
            text: '财务入账日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'valueTypeComn',
            text: '价值类型',
            form: 'select',
            options: [
              {
                label: '原值',
                value: '原值'
              },
              {
                label: '暂估值',
                value: '暂估值'
              },
              {
                label: '评估值',
                value: '评估值'
              },
              {
                label: '重置值',
                value: '重置值'
              },
              {
                label: '名义金额',
                value: '名义金额'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            key: 'assetNumberComn',
            text: '数量',
            show: false,
            default: 1
          },
          {
            type: 'Float',
            key: 'assetWorthComn',
            text: '价值（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'accountEntryComn',
            text: '入账科目',
            form: 'select',
            options: [
              {
                label: '固定资产',
                value: '固定资产'
              }
            ],
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'usageStatusComn',
            text: '使用状况',
            form: 'select',
            options: [
              {
                label: '自用',
                value: '自用'
              },
              {
                label: '闲置',
                value: '闲置'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Date',
            key: 'commissioningDateComn',
            text: '投入使用日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'useManagementComn',
            text: '使用/管理部门',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetUser',
            text: '使用人',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'equipmentUse',
            text: '设备用途',
            form: 'select',
            options: [
              {
                label: '办公',
                value: '办公'
              },
              {
                label: '教学',
                value: '教学'
              },
              {
                label: '培训',
                value: '培训'
              },
              {
                label: '执法',
                value: '执法'
              },
              {
                label: '窗口',
                value: '窗口'
              },
              {
                label: '专用',
                value: '专用'
              },
              {
                label: '其他',
                value: '其他'
              },
              {
                label: '医疗',
                value: '医疗'
              },
              {
                label: '科研',
                value: '科研'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'depreciationStatusComn',
            text: '折旧状态',
            form: 'select',
            options: [
              {
                label: '提折旧/摊销',
                value: '提折旧/摊销'
              },
              {
                label: '不提折旧/摊销',
                value: '不提折旧/摊销'
              },
              {
                label: '已完成折旧/摊销',
                value: '已完成折旧/摊销'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetRemarksComn',
            text: '备注',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'fuselageSerialNumber',
            text: '机身序列号',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'maintainCostTotal',
            text: '维修费用合计',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'isCIP',
            text: '是否在建工程转入',
            form: 'select',
            options: [
              {
                label: '是',
                value: '是'
              },
              {
                label: '否',
                value: '否'
              }
            ],
            show: true,
            readonly: false,
            required: true
          }
        ],
        soliCard: [
          {
            type: 'String',
            key: 'assetNameComn',
            text: '资产名称',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'categoryName',
            text: '资产分类名称',
            form: 'selectTree',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'categoryCodeComn',
            text: '资产分类代码',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'assetUnit',
            text: '单位',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'seatingPosition',
            text: '坐落位置',
            form: 'input',
            show: true,
            readonly: false,
            required: true,
            minLimit: 12
          },
          {
            type: 'Date',
            key: 'acquireDateComn',
            text: '取得日期',
            form: 'Date',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'acquireWayComn',
            text: '取得方式',
            form: 'select',
            options: [
              {
                label: '新购',
                value: '新购'
              },
              {
                label: '调拨',
                value: '调拨'
              },
              {
                label: '接受捐赠',
                value: '接受捐赠'
              },
              {
                label: '自建',
                value: '自建'
              },
              {
                label: '置换',
                value: '置换'
              },
              {
                label: '盘盈',
                value: '盘盈'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'propertyRight',
            text: '土地产权形式',
            form: 'select',
            options: [
              {
                label: '有产权',
                value: '有产权'
              },
              {
                label: '无产权',
                value: '无产权'
              },
              {
                label: '产权待界定',
                value: '产权待界定'
              }
            ],
            show: true,
            readonly: false,
            required: true,
            changeEvent: this.propertyChange
          },
          {
            type: 'String',
            key: 'ownershipNature',
            text: '权属性质',
            form: 'select',
            options: [
              {
                label: '国有',
                value: '国有'
              },
              {
                label: '私有',
                value: '私有'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'ownershipCertificate',
            text: '土地权属证明',
            form: 'select',
            options: [
              {
                label: '有土地证',
                value: '有土地证'
              },
              {
                label: '有土地权属相关证明文件',
                value: '私有土地权属相关证明文件有'
              },
              {
                label: '无',
                value: '无'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'ownershipAge',
            text: '权属年限',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'ownershipId',
            text: '土地证号',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'categoryUse',
            text: '地类(用途)',
            form: 'select',
            options: [
              {
                label: '机关团体用地',
                value: '机关团体用地'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'typeUse',
            text: '使用权类型',
            form: 'select',
            options: [
              {
                label: '出让',
                value: '出让'
              },
              {
                label: '划拨',
                value: '划拨'
              },
              {
                label: '租赁',
                value: '租赁'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false
          },
          {
            type: 'Float',
            key: 'areaUse',
            text: '使用权面积',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'soleArea',
            text: '独用面积',
            form: 'input',
            show: true,
            readonly: false,
            blurEvent: this.areaUseChange
          },
          {
            type: 'Float',
            key: 'apportionmentArea',
            text: '分摊面积',
            form: 'input',
            show: true,
            readonly: false,
            blurEvent: this.areaUseChange
          },
          {
            type: 'Date',
            key: 'issueDate',
            text: '发证日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'Float',
            key: 'financialAppropriationComn',
            text: '财政拨款（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'nonFinancialAppropriationComn',
            text: '非财政拨款（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'financialEntryStatusComn',
            text: '财务入账状态',
            form: 'select',
            options: [
              {
                label: '已入账',
                value: '已入账'
              },
              {
                label: '未入账',
                value: '未入账'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Date',
            key: 'financialEntryDateComn',
            text: '财务入账日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'valueTypeComn',
            text: '价值类型',
            form: 'select',
            options: [
              {
                label: '原值',
                value: '原值'
              },
              {
                label: '暂估值',
                value: '暂估值'
              },
              {
                label: '评估值',
                value: '评估值'
              },
              {
                label: '重置值',
                value: '重置值'
              },
              {
                label: '名义金额',
                value: '名义金额'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'assetWorthComn',
            text: '价值（元）',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Float',
            key: 'averagePrice',
            text: '均价',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'landGradation',
            text: '土地级次',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'usageStatusComn',
            text: '使用状况',
            form: 'select',
            options: [
              {
                label: '自用',
                value: '自用'
              },
              {
                label: '闲置',
                value: '闲置'
              },
              {
                label: '其他',
                value: '其他'
              }
            ],
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'Date',
            key: 'commissioningDateComn',
            text: '投入使用日期',
            form: 'Date',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'useManagementComn',
            text: '使用/管理部门',
            form: 'input',
            show: true,
            readonly: false,
            required: true
          },
          {
            type: 'String',
            key: 'depreciationStatusComn',
            text: '折旧状态',
            form: 'select',
            options: [
              {
                label: '提折旧/摊销',
                value: '提折旧/摊销'
              },
              {
                label: '不提折旧/摊销',
                value: '不提折旧/摊销'
              },
              {
                label: '已完成折旧/摊销',
                value: '已完成折旧/摊销'
              }
            ],
            show: true,
            readonly: false,
            required: true,
            default: '提折旧/摊销'
          },
          {
            type: 'String',
            key: 'structureHolder',
            text: '持证人',
            form: 'select',
            options: [
              {
                label: '本单位',
                value: '本单位'
              },
              {
                label: '非本单位',
                value: '非本单位'
              }
            ],
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'assetRemarksComn',
            text: '备注',
            form: 'input',
            show: true,
            readonly: false
          },
          {
            type: 'String',
            key: 'isCIP',
            text: '是否在建工程转入',
            form: 'select',
            options: [
              {
                label: '是',
                value: '是'
              },
              {
                label: '否',
                value: '否'
              }
            ],
            show: true,
            readonly: false,
            required: true
          }
        ]
      },
      VehicleCardForm: {
        assetId: '',
        assetNameComn: '',
        assetNumberComn: '',
        assetWorthComn: '',
        assetsType: '',
        assetUnit: '',
        assetApplicant: '',
        assetOperator: '',
        managementOffice: '',
        categoryName: '',
        categoryCodeComn: '',
        procurementModeComn: '',
        acquireWayComn: '',
        acquireDateComn: '',
        vehicleProducingArea: '',
        specificationType: '',
        assetBrand: '',
        vehicleIdentificationNumber: '',
        licensePlateNumber: '',
        dischargeVolume: '',
        engineNumber: '',
        vehicleDrivingPermit: '',
        assetSeller: '',
        contractNumber: '',
        invoiceNumber: '',
        warrantyDeadline: '',
        compilationSituation: '',
        financialAppropriationComn: '',
        nonFinancialAppropriationComn: '',
        financialEntryStatusComn: '',
        financialEntryDateComn: '',
        valueTypeComn: '',
        accountEntryComn: '',
        usageStatusComn: '',
        commissioningDateComn: '',
        useManagementComn: '',
        assetUser: '',
        accumulatedMileage: '',
        vehicleUse: '',
        depreciationStatusComn: '',
        assetRemarksComn: '',
        assetHolder: '',
        registrationDate: '',
        seatNumber: '',
        isCIP: ''
      },
      HouseForm: {
        assetId: '',
        assetNameComn: '',
        assetNumberComn: '',
        assetWorthComn: '',
        assetsType: '',
        assetUnit: '',
        assetApplicant: '',
        assetOperator: '',
        managementOffice: '',
        categoryName: '',
        categoryCodeComn: '',
        seatingPosition: '',
        acquireDateComn: '',
        acquireWayComn: '',
        propertyRight: '',
        ownershipNature: '',
        ownershipCertificate: '',
        ownershipAge: '',
        ownershipId: '',
        propertyOwner: '',
        designPurpose: '',
        buildingStructure: '',
        floorArea: '',
        heatingArea: '',
        completionDate: '',
        issueDate: '',
        financialAppropriationComn: '',
        nonFinancialAppropriationComn: '',
        financialEntryStatusComn: '',
        financialEntryDateComn: '',
        valueTypeComn: '',
        averagePrice: '',
        accountEntryComn: '',
        usageStatusComn: '',
        commissioningDateComn: '',
        useManagementComn: '',
        depreciationStatusComn: '',
        dangerousArea: '',
        selfUseWorth: '',
        rentalValue: '',
        lendingValue: '',
        idleValue: '',
        otherValue: '',
        assetRemarksComn: ''
      },
      ConstructionForm: {
        assetId: '',
        assetsType: '',
        assetApplicant: '',
        managementOffice: '',
        assetOperator: '',
        assetNumberComn: '',
        assetNameComn: '',
        categoryName: '',
        categoryCodeComn: '',
        seatingPosition: '',
        acquireDateComn: '',
        acquireWayComn: '',
        ownershipNature: '',
        buildingStructure: '',
        completionDate: '',
        financialAppropriationComn: '',
        nonFinancialAppropriationComn: '',
        financialEntryStatusComn: '',
        financialEntryDateComn: '',
        valueTypeComn: '',
        assetWorthComn: '',
        accountEntryComn: '',
        usageStatusComn: '',
        depreciationStatusComn: '',
        commissioningDateComn: '',
        assetUnit: '',
        useManagementComn: '',
        assetRemarksComn: '',
        isCIP: ''
      },
      FurnitureForm: {
        assetId: '',
        assetUnit: '',
        assetApplicant: '',
        assetOperator: '',
        managementOffice: '',
        assetsType: '',
        assetNameComn: '',
        categoryName: '',
        categoryCodeComn: '',
        storageLocation: '',
        procurementModeComn: '',
        acquireWayComn: '',
        acquireDateComn: '',
        assetBrand: '',
        assetSeller: '',
        warrantyDeadline: '',
        assetNumberComn: '',
        financialAppropriationComn: '',
        nonFinancialAppropriationComn: '',
        financialEntryStatusComn: '',
        financialEntryDateComn: '',
        valueTypeComn: '',
        assetWorthComn: '',
        accountEntryComn: '',
        usageStatusComn: '',
        useManagementComn: '',
        assetUser: '',
        commissioningDateComn: '',
        specificationType: '',
        depreciationStatusComn: '',
        assetRemarksComn: '',
        isCIP: '',
        unitPrice: ''
      },
      FlexibleUnitForm: {
        assetId: '',
        assetUnit: '',
        assetApplicant: '',
        assetOperator: '',
        managementOffice: '',
        assetsType: '',
        assetNameComn: '',
        categoryName: '',
        categoryCodeComn: '',
        storageLocation: '',
        procurementModeComn: '',
        acquireWayComn: '',
        equipmentUse: '',
        acquireDateComn: null,
        assetBrand: '',
        assetManufacturer: '',
        specificationType: '',
        assetSeller: '',
        contractNumber: '',
        invoiceNumber: '',
        warrantyDeadline: null,
        financialAppropriationComn: null,
        nonFinancialAppropriationComn: null,
        financialEntryStatusComn: '',
        valueTypeComn: '',
        assetNumberComn: null,
        assetWorthComn: null,
        financialEntryDateComn: null,
        accountEntryComn: '',
        usageStatusComn: '',
        commissioningDateComn: '',
        depreciationStatusComn: '',
        useManagementComn: '',
        assetUser: '',
        assetRemarksComn: '',
        fuselageSerialNumber: '',
        isCIP: '',
        configStandardName: ''
      },
      DedicatedDevicesForm: {
        assetId: '',
        assetUnit: '',
        assetApplicant: '',
        assetOperator: '',
        managementOffice: '',
        assetsType: '',
        assetNameComn: '',
        categoryName: '',
        categoryCodeComn: '',
        storageLocation: '',
        procurementModeComn: '',
        acquireDateComn: '',
        assetBrand: '',
        assetManufacturer: '',
        specificationType: '',
        assetSeller: '',
        contractNumber: '',
        invoiceNumber: '',
        warrantyDeadline: '',
        financialAppropriationComn: '',
        nonFinancialAppropriationComn: '',
        financialEntryStatusComn: '',
        valueTypeComn: '',
        assetNumberComn: '',
        assetWorthComn: '',
        financialEntryDateComn: '',
        accountEntryComn: '',
        usageStatusComn: '',
        commissioningDateComn: '',
        useManagementComn: '',
        assetUser: '',
        equipmentUse: '',
        depreciationStatusComn: '',
        assetRemarksComn: '',
        fuselageSerialNumber: '',
        maintainCostTotal: '',
        isCIP: ''
      },
      soliForm: {
        assetId: '',
        assetUnit: '',
        assetApplicant: '',
        managementOffice: '',
        assetOperator: '',
        assetsType: '',
        assetNameComn: '',
        assetNumberComn: '',
        categoryName: '',
        categoryCodeComn: '',
        seatingPosition: '',
        acquireDateComn: '',
        acquireWayComn: '',
        propertyRight: '',
        ownershipNature: '',
        ownershipCertificate: '',
        ownershipAge: '',
        ownershipId: '',
        categoryUse: '',
        typeUse: '',
        areaUse: '',
        soleArea: '',
        apportionmentArea: '',
        issueDate: '',
        financialAppropriationComn: '',
        nonFinancialAppropriationComn: '',
        financialEntryStatusComn: '',
        financialEntryDateComn: '',
        valueTypeComn: '',
        assetWorthComn: '',
        averagePrice: '',
        landGradation: '',
        usageStatusComn: '',
        commissioningDateComn: '',
        useManagementComn: '',
        depreciationStatusComn: '',
        structureHolder: '',
        assetRemarksComn: '',
        isCIP: ''
      },
      VehicleCardRules: {
      },
      HouseRules: {},
      ConstructionRules: {},
      FurnitureRules: {},
      FlexibleUnitRules: {},
      DedicatedDevicesRules: {},
      soliRules: {},
      activeTabName: '1',
      assetTableData: [],
      selectAssetType: '',
      assetAllType: [
        {
          id: 1,
          label: '车辆资产卡片',
          level: 1,
          children: []
        },
        {
          id: 2,
          label: '房屋资产卡片',
          level: 1,
          children: []
        },
        {
          id: 3,
          label: '构筑物资产卡片',
          level: 1,
          children: []
        },
        {
          id: 4,
          label: '家具/用具/装具资产卡片',
          level: 1,
          children: []
        },
        {
          id: 5,
          label: '通用设备资产卡片',
          level: 1,
          children: []
        },
        {
          id: 6,
          label: '专用设备资产卡片',
          level: 1,
          children: []
        },
        {
          id: 7,
          label: '土地使用权资产卡片',
          level: 1,
          children: []
        }
      ],
      assetTreeData: [],
      showAssetForm: false,
      activeAssetForm: null,
      activeAssetFormName: '',
      currentCommonData: {},
      detailAssetForm: {},
      assetDetailData: {},
      assetDetailType: '',
      privateTitle: '',
      isDetailPage: false,
      treeShow: false,
      detailShow: false,
      tableHeaderCellStyle: {
        "background-color": "#bee2f3",
        "border-color": "#c8c8c8",
        "padding-top": "4px",
        "padding-bottom": "4px",
        "padding-left": "15px",
        "color": "#333333"
      },
      tableCellStyle: {
        "border-color": "#c8c8c8",
        padding: "0"
      },
      certificateForm: {
        assetId: '',
        accountingVoucherNumberComn: '',
        cardIdComn: ''
      },
      certificateDialogVisible: false,
      certificateDialogTitle: '',
      certificateRules: {},
      assetsOtherInfo: null,
      formNumberArr: [],
      formDefaultArr: [],
      token: '',
      verificationTips: '',
      baseInfoRules: {
        purchasingPname: [
          { required: true, message: "采购项目名称不能为空", trigger: "blur" }
        ],
        applicant: [
          { required: true, message: "申请人不能为空", trigger: "blur" }
        ],
        operator: [
          { required: true, message: "经办人不能为空", trigger: "blur" }
        ],
        handleDepartment: [
          { required: true, message: "经办科室不能为空", trigger: "blur" }
        ],
        procurementType: [
          { required: true, message: "采购分类不能为空", trigger: "blur" }
        ],
        procurementMode: [
          { required: true, message: "采购方式不能为空", trigger: "blur" }
        ],
        agency: [
          { required: true, message: "代理机构不能为空", trigger: "blur" }
        ],
        organizationForm: [
          { required: true, message: "组织形式不能为空", trigger: "blur" }
        ]
      },
      baseInfoSaved: false,
      storagedList: {}
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
    // 获取资产信息列表
    getList() {
      this.loadingStart('.table-box');
      getPurchasingList(this.purchasingPname, this.status, this.currentPage, this.pageSize)
        .then(res => {
          this.loadingEnd();
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach(item => {
            if (item.status === 0) {
              item.statusText = '未完成';
            } else {
              item.statusText = '已完成';
            }
          })
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '资产信息获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 查询单位信息
    search() {
      this.currentPage = 1;
      this.getList();
    },
    // 重置查询
    reset() {
      this.purchasingPname = '';
      this.status = '';
      this.currentPage = 1;
      this.getList();
    },
    // 资产入库
    add() {
      this.enterDialogVisible = true;
      this.dialogTitle = "新增资产信息";
      this.operateType = "add";
      this.treeShow = true;
    },
    // 再次入库
    storageAgin(purchasingId) {
      this.enterDialogVisible = true;
      this.dialogTitle = "编辑资产信息";
      this.operateType = "edit";
      this.treeShow = true;
      getPurchasingDetail(purchasingId).then(res => {
        this.assetBaseInfoForm = res.baseInfo;
        this.assetTableData = res.assetInfoList;
        this.assetTableData.forEach(item => {
          item.selectAssetTypeName = this.getAssetTypeName(item.selectAssetType);
          item.storagedNum = 0;
        })
        this.assetTypeChangeHandle();
      }).catch(err => {
        const msg = err.result ? err.result : '采购项目信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // dialog关闭的回调
    enterHandleClose() {
      this.assetTableData.forEach((item, index) => {
        const id = `${item.selectAssetType}:${index + 1}`;
        this.$refs.tree.remove(id);
      })
      this.activeAssetForm = null;
      this.activeAssetFormName = '';
      this.assetsOtherInfo = null;
      this.showAssetForm = false;
      this.selectAsset = '';
      this.assetTableData = [];
      this.activeTabName = '1';
      this.treeShow = false;
      this.isDetailPage = false;
      this.uploadSuccessImgNum = 0;
      this.uploadImgNum = 0;
      this.assetsImgList = [];
      this.fileList = [];
      this.storagedTreeArray = [];
      this.verificationTips = '';
      this.baseInfoSaved = false;
      this.storagedList = {};
      this.clearForm(this.activeAssetFormName);
      this.clearForm('assetBaseInfoForm');
      $('.enter-section-1').css({ 'left': 0 });
      $('.enter-section-2').css({ 'left': '100%' });
      $('.el-tree-node__content').find('i').css('display', 'none');
    },
    // 添加一条账面资产信息
    addAssetInfo() {
      const obj = {
        brandName: '',
        brandMode: '',
        procurementRecord: '',
        unitPrice: '',
        number: '',
        storagedNum: 0,
        unit: '',
        category: '',
        categoryCode: '',
        purchaseAmount: '',
        selectAssetType: ''
      }
      this.assetTableData.push(obj);
    },
    // 删除一条账面资产信息
    deleteAssetInfo(index) {
      this.assetTableData.splice(index, 1);
    },
    // 下一步
    nextStep() {
      if (this.operateType === 'add' && !this.baseInfoSaved) {
        this.$refs['assetBaseInfoForm'].validate(valid => {
          if (valid) {
            // 验证
            if (this.assetTableData.length === 0) {
              this.verificationTips = '请至少添加一条资产信息，并填写完整！'
              return false;
            } else {
              for (let i = 0; i < this.assetTableData.length; i++) {
                const data = this.assetTableData[i];
                for (let key in data) {
                  if (data[key] === '' || data[key] === null) {
                    this.verificationTips = '请将资产的每一项信息都填写完整！'
                    return false;
                  }
                }
              }
              this.verificationTips = '';
            }
            // 调用保存基础信息和资产信息的接口
            this.assetTableData.forEach(item => {
              delete item.storagedNum;
            });
            addPurchasing(this.assetBaseInfoForm, this.assetTableData).then(res => {
              this.baseInfoSaved = true;
              this.assetBaseInfoForm = res.baseInfo;
              this.assetTableData = res.assetInfoList;
              this.assetTableData.forEach(item => {
                item.selectAssetTypeName = this.getAssetTypeName(item.selectAssetType);
              })
              this.nextStepHandle();
            }).catch(err => {
              const msg = err.result ? err.result : '采购项目信息添加失败';
              this.$message({
                message: msg,
                type: "error"
              });
            })
          } else {
            return false;
          }
        });
      } else {
        this.nextStepHandle();
      }
    },
    // 下一步操作
    nextStepHandle() {
      let defaultExpandNode_1 = '';
      let defaultExpandNode_2 = '';
      let defaultFormType = '';
      let defaultFormData = {};
      this.assetTableData.forEach((item, index) => {
        let tempArr = [];
        let tempArr_2 = [];
        let tempObj = {};
        tempObj.label = `${item.brandName}${item.brandMode}`;
        tempObj.id = `${item.selectAssetType}:${index + 1}`;
        tempObj.level = 2;
        tempArr.push(tempObj);
        if (index === 0) {
          defaultExpandNode_1 = item.selectAssetType;
          defaultExpandNode_2 = tempObj.id;
          defaultFormType = item.selectAssetType;
        }
        let node = this.$refs.tree.getNode(item.selectAssetType);
        if (node && node.childNodes && node.childNodes.length > 0) {
          const beforeNode = node.childNodes[node.childNodes.length - 1];
          this.$refs.tree.insertAfter(tempArr[0], beforeNode);
        } else {
          this.$refs.tree.updateKeyChildren(item.selectAssetType, tempArr);
        }
        item.number = item.number - 0;
        for (let i = 1; i <= item.number; i++) {
          let tempObj_2 = {};
          tempObj_2.label = i + '';
          tempObj_2.id = `${item.selectAssetType}:${index + 1}:${i}`;
          tempObj_2.assetId = item.assetId;
          tempObj_2.assetNameComn = item.brandName;
          tempObj_2.assetNumberComn = item.number;
          tempObj_2.assetWorthComn = item.unitPrice;
          tempObj_2.assetsType = item.selectAssetType;
          tempObj_2.assetUnit = item.unit;
          tempObj_2.assetApplicant = this.assetBaseInfoForm.applicant;
          tempObj_2.assetOperator = this.assetBaseInfoForm.operator
          tempObj_2.managementOffice = this.assetBaseInfoForm.handleDepartment
          tempObj_2.level = 3;
          tempObj_2.state = 1; // 状态默认为入库
          let storagedFlag = false;
          for (let i = 0; i < this.storagedTreeArray.length; i++) {
            if (this.storagedTreeArray[i].id === tempObj_2.id) {
              storagedFlag = true;
              tempObj_2.saved = true;
              tempObj_2.guid = this.storagedTreeArray[i].guid;
            }
          }
          if (!storagedFlag) {
            tempObj_2.saved = false;
          }
          tempArr_2.push(tempObj_2);
          if (i === 1 && index === 0) {
            defaultFormData = tempObj_2;
          }
        }
        this.$refs.tree.updateKeyChildren(tempObj.id, tempArr_2);
      })
      // 页面滑动到下一页
      $('.enter-section-1').animate({ 'left': '-100%' }, 1000);
      $('.enter-section-2').animate({ 'left': 0 }, 1000);
      // 显示默认页面
      if (defaultFormData.saved) {
        this.isDetailPage = true;
        getAssetDetailInfo(defaultFormData.guid).then(res => {
          this.assetDetailData = res;
          switch (defaultFormData.assetsType) {
            case 1:
              this.detailAssetForm = this.VehicleCardForm;
              this.assetDetailType = 'VehicleCard';
              break;
            case 2:
              this.detailAssetForm = this.HouseForm;
              this.assetDetailType = 'HouseCard';
              break;
            case 3:
              this.detailAssetForm = this.ConstructionForm;
              this.assetDetailType = 'ConstructionCard';
              break;
            case 4:
              this.detailAssetForm = this.FurnitureForm;
              this.assetDetailType = 'FurnitureCard';
              break;
            case 5:
              this.detailAssetForm = this.FlexibleUnitForm;
              this.assetDetailType = 'FlexibleUnitCard';
              break;
            case 6:
              this.detailAssetForm = this.DedicatedDevicesForm;
              this.assetDetailType = 'DedicatedDevicesCard';
              break;
            case 7:
              this.detailAssetForm = this.soliForm;
              this.assetDetailType = 'soliCard';
              break;
          }
        })
      }
      this.showAssetForm = true;
      this.currentCommonData = defaultFormData;
      this.activeAssetForm = defaultFormType;
      this.activeAssetFormName = this.getActiveFormName(defaultFormType);
      this.toggleForm(defaultFormType);
      this.bindBaseData(defaultFormData);
      // 选中树
      setTimeout(() => {
        this.$refs.tree.store.nodesMap[defaultExpandNode_1].expanded = true;
        this.$refs.tree.store.nodesMap[defaultExpandNode_2].expanded = true;
        this.$refs.tree.setCurrentKey(defaultFormData.id);
      }, 100);
      setTimeout(() => {
        for (let i = 0; i < this.storagedTreeArray.length; i++) {
          const classname = this.storagedTreeArray[i].id.split(':').join('_');
          $(`.${classname}:hidden`).css('display', 'inline-block');
        }
      }, 1000);
    },
    // 上一步
    prevStep() {
      $('.enter-section-1').animate({ 'left': 0 }, 1000, () => {
        this.clearForm(this.activeAssetFormName);
        this.activeAssetForm = null;
        this.activeAssetFormName = '';
        this.assetsOtherInfo = null;
        this.showAssetForm = false;
        this.uploadSuccessImgNum = 0;
        this.uploadImgNum = 0;
        this.assetsImgList = [];
        this.fileList = [];
        this.isDetailPage = false;
        this.assetTableData.forEach((item, index) => {
          const id = `${item.selectAssetType}:${index + 1}`;
          this.$refs.tree.remove(id);
          let flag = true;
          for (let key in this.storagedList) {
            if ((key - 0) === item.assetId) {
              flag = false;
              this.$set(item, 'storagedNum', this.storagedList[key]);
            }
          }
          if (flag) {
            this.$set(item, 'storagedNum', 0);
          }
          this.$set(this.assetTableData, index, item);
        });
      });
      $('.enter-section-2').animate({ 'left': '100%' }, 1000);
    },
    // 选择账面资产类型
    assetTypeChangeHandle() {
      let assetTreeFlag;
      let treeData = [];
      this.assetTableData.forEach((item, index) => {
        assetTreeFlag = true;
        this.assetAllType.forEach(ele => {
          if (item.selectAssetType === ele.id) {
            if (treeData.length > 0) {
              treeData.forEach(data => {
                if (data.id === ele.id) {
                  assetTreeFlag = false;
                }
              })
            }
            if (assetTreeFlag) {
              treeData.push(ele);
            }
          }
        })
      })
      this.assetTreeData = treeData;
    },
    // 点击资产树节点
    assetTreeNodeClick(data, node) {
      if (data.level === 3) {
        this.showSelectTree = false;
        setTimeout(() => {
          this.showSelectTree = true;
        }, 1000)
        eventBus.$emit('closeInput');
        eventBus.$emit('clearRecordPosition');
        const selectAssetType = data.assetsType;
        this.showAssetForm = true;
        this.currentCommonData = data;
        this.uploadSuccessImgNum = 0;
        this.uploadImgNum = 0;
        this.assetsImgList = [];
        this.fileList = [];
        this.assetsOtherInfo = null;
        if (data.saved) {
          this.isDetailPage = true;
          getAssetDetailInfo(data.guid).then(res => {
            this.assetDetailData = res;
            switch (selectAssetType) {
              case 1:
                this.detailAssetForm = this.VehicleCardForm;
                this.assetDetailType = 'VehicleCard';
                break;
              case 2:
                this.detailAssetForm = this.HouseForm;
                this.assetDetailType = 'HouseCard';
                break;
              case 3:
                this.detailAssetForm = this.ConstructionForm;
                this.assetDetailType = 'ConstructionCard';
                break;
              case 4:
                this.detailAssetForm = this.FurnitureForm;
                this.assetDetailType = 'FurnitureCard';
                break;
              case 5:
                this.detailAssetForm = this.FlexibleUnitForm;
                this.assetDetailType = 'FlexibleUnitCard';
                break;
              case 6:
                this.detailAssetForm = this.DedicatedDevicesForm;
                this.assetDetailType = 'DedicatedDevicesCard';
                break;
              case 7:
                this.detailAssetForm = this.soliForm;
                this.assetDetailType = 'soliCard';
                break;
            }
          })
        } else {
          // 清空上一个表单
          this.clearForm(this.activeAssetFormName);
          this.isDetailPage = false;
          this.activeAssetForm = selectAssetType;
          this.activeAssetFormName = this.getActiveFormName(selectAssetType);
          this.toggleForm(selectAssetType);
          this.bindBaseData(data);
        }
      }
    },
    // 切换form
    toggleForm(selectAssetType) {
      this.currentAssetRules = [];
      switch (selectAssetType) {
        case 1:
          this.currentAssetForm = this.VehicleCardForm;
          this.assetCommodityType = 'VehicleCard';
          this.currentAssetRules = this.VehicleCardRules;
          this.privateTitle = '车辆资产卡片填写';
          break;
        case 2:
          this.currentAssetForm = this.HouseForm;
          this.assetCommodityType = 'HouseCard';
          this.currentAssetRules = this.HouseRules;
          this.privateTitle = '房屋资产卡片填写';
          break;
        case 3:
          this.currentAssetForm = this.ConstructionForm;
          this.assetCommodityType = 'ConstructionCard';
          this.currentAssetRules = this.ConstructionRules;
          this.privateTitle = '构筑物资产卡片填写';
          break;
        case 4:
          this.currentAssetForm = this.FurnitureForm;
          this.assetCommodityType = 'FurnitureCard';
          this.currentAssetRules = this.FurnitureRules;
          this.privateTitle = '家具/用具/装具资产卡片填写';
          break;
        case 5:
          this.currentAssetForm = this.FlexibleUnitForm;
          this.assetCommodityType = 'FlexibleUnitCard';
          this.currentAssetRules = this.FlexibleUnitRules;
          this.privateTitle = '通用设备资产卡片填写';
          break;
        case 6:
          this.currentAssetForm = this.DedicatedDevicesForm;
          this.assetCommodityType = 'DedicatedDevicesCard';
          this.currentAssetRules = this.DedicatedDevicesRules;
          this.privateTitle = '专用设备资产卡片填写';
          break;
        case 7:
          this.currentAssetForm = this.soliForm;
          this.assetCommodityType = 'soliCard';
          this.currentAssetRules = this.soliRules;
          this.privateTitle = '土地使用权资产卡片填写';
          break;
      }
      // 绑定验证规则
      this.formNumberArr = [];
      this.typeRender[this.assetCommodityType].forEach(item => {
        this.currentAssetRules[item.key] = [];
        if (item.required === true) {
          const RequiredValid = {
            required: true,
            message: item.form === 'input' ? `请输入${item.text}` : `请选择${item.text}`,
            trigger: 'blur'
          }
          this.currentAssetRules[item.key].push(RequiredValid);
        }
        if (item.type === 'Integer') {
          const IntegerValid = {
            validator: this.validInteger,
            trigger: 'blur'
          }
          this.currentAssetRules[item.key].push(IntegerValid);
        } else if (item.type === 'Float') {
          const FloatValid = {
            validator: this.validFloat,
            trigger: 'blur'
          }
          this.currentAssetRules[item.key].push(FloatValid);
        }
        if (item.key === 'acquireDateComn') {
          const AcquireDateValid = {
            validator: this.validAcquireDate,
            trigger: 'blur'
          }
          this.currentAssetRules[item.key].push(AcquireDateValid);
        }
        // 判单最小字符
        if (item.minLimit !== undefined) {
          const minLimitValid = {
            validator: this.validMinLimit,
            trigger: 'blur'
          }
          this.currentAssetRules[item.key].push(minLimitValid);
        }
        // 绑定默认值
        if (item.default !== undefined) {
          this.currentAssetForm[item.key] = item.default;
        }
        // 获取数字类型
        if (item.type === 'Integer' || item.type === 'Float') {
          this.formNumberArr.push(item.key);
        }
      })
    },
    // 绑定基础数据
    bindBaseData(data) {
      for (let key in data) {
        if (this.currentAssetForm[key] !== undefined) {
          if (data[key]) {
            this.currentAssetForm[key] = data[key];
          }
        }
      }
    },
    // 开始上传文件
    submitUpload() {
      this.$refs.upload.submit();
    },
    // 删除上传文件时钩子
    handleRemove(file, fileList) {
      this.uploadImgNum = fileList.length;
    },
    // 上传文件成功时钩子
    handleSuccess(response, file, fileList) {
      this.uploadSuccessImgNum++;
      this.assetsImgList.push({
        imgPath: response.imgList[0].imgPath,
        imgUrl: response.imgList[0].imgUrl
      });
      if (this.uploadSuccessImgNum === this.uploadImgNum) {
        this.saveForm();
      }
    },
    // 上传文件改变时钩子
    handleChange(file, fileList) {
      this.uploadImgNum = fileList.length;
    },
    // 上传文件超出限制个数
    handleExceed(file, fileList) {
      this.$message.warning(`最多只能上传三张图片！`);
    },
    // 上传文件失败
    handleError(err, file, fileList) {
      if (err.status === 401) {
        this.$store.dispatch('Logout', this.token);
        this.$router.push({ path: '/login' });
      }
      this.$message({
        message: err.message,
        type: "error"
      });
    },
    // 取消
    cancelForm() {
      this.clearForm(this.activeAssetFormName);
      this.bindBaseData(this.currentCommonData);
    },
    // 提交账面资产信息
    submitForm() {
      if (this.uploadImgNum > 0) {
        this.submitUpload();
      } else {
        this.saveForm();
      }
    },
    // 保存账面资产信息
    saveForm() {
      this.$refs[this.activeAssetFormName].validate(valid => {
        if (valid) {
          let formObj = {};
          formObj.purchasingId = this.assetBaseInfoForm.purchasingId;
          formObj.purchasingPname = this.assetBaseInfoForm.purchasingPname;
          formObj.operator = sessionStorage.getItem('name');
          formObj.assetsList = [];
          // 数字类型转化为数字
          for (let key in this[this.activeAssetFormName]) {
            if (this.formNumberArr.indexOf(key) > 0) {
              this[this.activeAssetFormName][key] = this[this.activeAssetFormName][key] - 0;
            }
          }
          let tempObj = this[this.activeAssetFormName];
          tempObj.assetsImgList = this.assetsImgList;
          formObj.assetsList.push(tempObj);
          addAssets(formObj).then(res => {
            this.$message({
              message: `资产入库成功`,
              type: "success"
            });
            this.getList();
            const id = this.currentCommonData.id;
            const classname = id.split(':').join('_');
            $(`.${classname}:hidden`).css('display', 'inline-block');
            let node = this.$refs.tree.getNode(id);
            node.data.saved = true;
            node.data.guid = res[0];
            const storagedItem = {
              id: id,
              guid: res[0]
            }
            this.storagedTreeArray.push(storagedItem);
            // 已入库记录
            const assetId = node.data.assetId;
            if (this.storagedList[assetId] === undefined) {
              this.storagedList[assetId] = 1;
            } else {
              this.storagedList[assetId] = this.storagedList[assetId] + 1;
            }
            getAssetDetailInfo(res[0]).then(resp => {
              this.assetDetailData = resp;
              this.isDetailPage = true;
              switch (this.activeAssetForm) {
                case 1:
                  this.detailAssetForm = this.VehicleCardForm;
                  this.assetDetailType = 'VehicleCard';
                  break;
                case 2:
                  this.detailAssetForm = this.HouseForm;
                  this.assetDetailType = 'HouseCard';
                  break;
                case 3:
                  this.detailAssetForm = this.ConstructionForm;
                  this.assetDetailType = 'ConstructionCard';
                  break;
                case 4:
                  this.detailAssetForm = this.FurnitureForm;
                  this.assetDetailType = 'FurnitureCard';
                  break;
                case 5:
                  this.detailAssetForm = this.FlexibleUnitForm;
                  this.assetDetailType = 'FlexibleUnitCard';
                  break;
                case 6:
                  this.detailAssetForm = this.DedicatedDevicesForm;
                  this.assetDetailType = 'DedicatedDevicesCard';
                  break;
                case 7:
                  this.detailAssetForm = this.soliForm;
                  this.assetDetailType = 'soliCard';
                  break;
              }
            }).catch(err => {
              const msg = err.result ? err.result : '资产详情获取失败！';
              this.$message({
                message: msg,
                type: "error"
              });
            })
          })
        } else {
          return false;
        }
      });
    },
    // 树样式渲染
    renderContent(h, { node, data, store }) {
      if (data.level === 3) {
        const classname = data.id.split(':').join('_');
        return (<span class="custom-tree-node">
          <i class={classname}></i>
          <span style="margin-left:5px;">{data.label}</span>
        </span>)
      } else {
        return (<span class="custom-tree-node">
          <span style="margin-left:5px;">{data.label}</span>
        </span>)
      }
    },
    // 清除表单数据
    clearForm(formType) {
      if (formType) {
        for (let key in this[formType]) {
          var type = this.getDataType(this[formType][key]);
          if (type === "String") {
            this[formType][key] = "";
          } else if (type === "Array") {
            this[formType][key] = [];
          } else if (type === "Object") {
            const keys = Object.keys(this[formType][key]);
            if (keys.length > 0) {
              this.clearForm(this[formType][key]);
            } else {
              this[formType][key] = {};
            }
          } else {
            this[formType][key] = null;
          }
        }
      }
    },
    // 获取当前卡片名称
    getActiveFormName(type) {
      let form = '';
      switch (type) {
        case 1:
          form = 'VehicleCardForm';
          break;
        case 2:
          form = 'HouseForm';
          break;
        case 3:
          form = 'ConstructionForm';
          break;
        case 4:
          form = 'FurnitureForm';
          break;
        case 5:
          form = 'FlexibleUnitForm';
          break;
        case 6:
          form = 'DedicatedDevicesForm';
          break;
        case 7:
          form = 'soliForm';
          break;
      }
      return form;
    },
    // 获取数据类型
    getDataType(obj) {
      var typeArr = ["String", "Object", "Number", "Array", "Date"];
      for (let name of typeArr) {
        if (Object.prototype.toString.call(obj) === `[object ${name}]`) {
          return name;
        }
      }
    },
    // 选择下拉树
    seletTreeChecked(obj) {
      if (obj.treeName === 'storageLocation') {
        this.assetsOtherInfo = obj;
        const form = this.getActiveFormName(this.activeAssetForm);
        this[form].storageLocation = obj.label;
      } else if (obj.treeName === 'categoryName') {
        const form = this.getActiveFormName(this.activeAssetForm);
        this[form].categoryName = obj.text;
        this[form].categoryCodeComn = obj.id;
        this[form].assetUnit = obj.unit;
      }
    },
    // 获取资产卡片名称
    getAssetTypeName(type) {
      let name;
      switch (type) {
        case 1:
          name = '车辆资产卡片';
          break;
        case 2:
          name = '房屋资产卡片';
          break;
        case 3:
          name = '构筑物资产卡片';
          break;
        case 4:
          name = '家具/用具/装具资产卡片';
          break;
        case 5:
          name = '通用设备资产卡片';
          break;
        case 6:
          name = '专用设备资产卡片';
          break;
        case 7:
          name = '土地使用权资产卡片';
          break;
      };
      return name;
    },
    // 只能输入正数
    validFloat(rule, value, callback) {
      value = value + '';
      let reg_1 = /[^\d.]/g;
      let reg_2 = /\.{2,}/g;
      let flag = true;
      // 第一个字符是.
      if (value != "" && value.substr(0, 1) == ".") {
        callback(new Error('只能输入正数'));
        flag = false;
      }
      // “数字”和“.”以外的字符
      if (reg_1.test(value)) {
        callback(new Error('只能输入正数'));
        flag = false;
      }
      // 只保留第一个. 清除多余的
      if (reg_2.test(value)) {
        callback(new Error('只能输入正数'));
        flag = false;
      }
      if (flag) {
        callback()
      }
    },
    // 只能输入正整数
    validInteger(rule, value, callback) {
      let reg = /[^0-9]/g;
      if (reg.test(value)) {
        callback(new Error('只能输入正整数'))
      } else {
        callback()
      }
    },
    // 取得日期必须小于当前日期
    validAcquireDate(rule, value, callback) {
      const currentDate = new Date().getTime();
      const acquireDate = new Date(value).getTime();
      if (acquireDate > currentDate) {
        callback(new Error('取得日期必须小于当前日期'));
      } else {
        callback();
      }
    },
    // 已入账时，财务入账日期为必填
    EntryStatusChange(val) {
      if (val === '已入账') {
        const RequiredValid = {
          required: true,
          message: '请选择财务入账日期',
          trigger: 'blur'
        }
        this.currentAssetRules['financialEntryDateComn'].push(RequiredValid);
        $('label[for="financialEntryDateComn"]').attr('required-icon', '*');
      } else {
        this.currentAssetRules['financialEntryDateComn'] = [];
        this.$refs[this.activeAssetFormName].$children.forEach(item => {
          if (item.prop === 'financialEntryDateComn') {
            item.resetField();
          }
        })
        $('label[for="financialEntryDateComn"]').attr('required-icon', '');
      }
    },
    // 使用权面积 = （独立使用面积 + 分摊面积）
    areaUseChange() {
      this[this.activeAssetFormName]['areaUse'] = (this[this.activeAssetFormName]['soleArea'] - 0) + (this[this.activeAssetFormName]['apportionmentArea'] - 0);
    },
    // 判断最小字符且为汉字
    validMinLimit(rule, val, callback) {
      const limitNum = this.typeRender[this.assetCommodityType].minLimit;
      var len = 0;
      for (var i = 0; i < val.length; i++) {
        var length = val.charCodeAt(i);
        if (length >= 0 && length <= 128) {
          len += 1;
        }
        else {
          len += 2;
        }
      }
      if (len < limitNum) {
        callback(new Error('输入的地址长度不够'));
      } else {
        callback();
      }
    },
    // 无产权或者产权待界定时，土地权属证明不能选择“有土地”
    propertyChange(val) {
      if (val === '无产权' || val === '产权待界定') {
        let form = this.typeRender[this.assetCommodityType]
        for (let i = 0; i < form.length; i++) {
          if (form[i].key === 'ownershipCertificate') {
            form[i].options.forEach((item, index) => {
              if (item.value === '有土地证') {
                form[i].options.splice(index, 1);
                this[this.activeAssetFormName]['ownershipCertificate'] = '';
              }
            });
          }
        }
      } else {
        let form = this.typeRender[this.assetCommodityType]
        for (let i = 0; i < form.length; i++) {
          if (form[i].key === 'ownershipCertificate') {
            let flag = true
            let opts = form[i].options;
            opts.forEach((item, index) => {
              if (item.value === '有土地证') {
                flag = false
              }
            });
            if (flag) {
              opts.unshift({
                label: '有土地证',
                value: '有土地证'
              })
            }
          }
        }
      }
    },
    // 只能输入整数
    asseLimitInt(index, key) {
      this.assetTableData[index][key] = this.assetTableData[index][key].replace(/[^0-9]/g, "");
    },
    // 只能输入浮点数
    asseLimitFloat(index, key, flag) {
      this.assetTableData[index][key] = this.assetTableData[index][key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '')
      if (flag && this.assetTableData[index][key] !== '') {
        this.assetTableData[index][key] = (this.assetTableData[index][key] - 0).toFixed(2);
      }
    }
  },
  mounted() {
    this.getList();
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - queryBoxH - operateBoxH;
    }, 100);
    this.token = sessionStorage.getItem('TOKEN');
    if (this.token) {
      this.postHeaders.Authorization = `Bearer ${this.token}`;
    }
  }
};
</script>

<style>
#assetRegister .query-box {
  position: relative;
  margin-bottom: 20px;
}
#assetRegister .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#assetRegister .query-item > div {
  float: left;
}
#assetRegister .query-item > .query-input {
  width: calc(100% - 112px);
}
#assetRegister .query-item .el-date-editor.el-input,
#assetRegister .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#assetRegister .query-item .el-select {
  width: 100%;
}
#assetRegister .query-btn {
  float: left;
  margin-left: 50px;
}
#assetRegister .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#assetRegister .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#assetRegister .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#assetRegister .enter-dialog .el-dialog__body,
#assetRegister .enter-dialog .el-dialog__header,
#assetRegister .detail-dialog .el-dialog__body {
  padding: 0;
}
#assetRegister .enter-dialog .el-dialog__body {
  width: 100%;
  height: 650px;
  position: relative;
  overflow: hidden;
}
#assetRegister .enter-dialog .el-dialog__body > div {
  width: 100%;
  height: 650px;
  position: absolute;
}
#assetRegister .enter-dialog .el-dialog__body > .enter-section-1 {
  top: 0;
  left: 0;
}
#assetRegister .enter-dialog .el-dialog__body > .enter-section-2 {
  top: 0;
  left: 100%;
}
#assetRegister .dialog-header {
  height: 50px;
  line-height: 50px;
  font-size: 18px;
  border-bottom: 1px solid #d8d8d8;
  padding-left: 20px;
}
#assetRegister .dialog-header > span {
  border-left: 2px solid #4690f0;
  padding-left: 10px;
}
#assetRegister .el-dialog__headerbtn {
  top: 16px;
  z-index: 100;
}
#assetRegister .el-dialog__headerbtn .el-dialog__close {
  font-size: 18px;
}
#assetRegister .dialog-body {
  height: 600px;
  overflow-y: auto;
  overflow-x: hidden;
}
#assetRegister .introduce-box {
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
}
#assetRegister .next-step {
  position: absolute;
  right: 20px;
  bottom: 20px;
}
#assetRegister .verification-tips {
  position: absolute;
  left: 20px;
  bottom: 60px;
  color: #ff0000;
}
#assetRegister .dialog-left {
  width: 200px;
  height: 100%;
  background: #bee2f3;
  float: left;
}
#assetRegister .dialog-right {
  width: calc(100% - 200px);
  float: right;
  height: 100%;
}
#assetRegister .assetInfo-body {
  height: 250px;
  padding-top: 10px;
}
#assetRegister .asset-tree-box {
  width: 100%;
  height: 500px;
  padding: 20px 5px;
  box-sizing: border-box;
  overflow: auto;
}
#assetRegister .prev-step {
  text-align: center;
  margin-top: 30px;
}
#assetRegister .el-tree {
  background: inherit;
  color: #333;
}
#assetRegister .is-current > .el-tree-node__content,
#assetRegister .el-tree-node__content:hover {
  background: #d2edf7;
  color: #409eff;
}
#assetRegister .dialog-btn-box {
  text-align: center;
  margin-top: 30px;
  margin-bottom: 30px;
}
#assetRegister .el-tree-node__content {
  color: #333;
}
#assetRegister .dialog-body-title {
  color: #4690f0;
  font-weight: 600;
  padding: 10px 20px;
}
#assetRegister .dialog-body-title > i {
  background: url("../../assets/imgs/edit-icon.png") no-repeat center center;
  display: inline-block;
  width: 20px;
  height: 20px;
  position: relative;
  top: 4px;
}
#assetRegister .dialog-body-title > i.photo {
  background: url("../../assets/imgs/photo-icon.png") no-repeat center center;
}
#assetRegister .custom-tree-node > i {
  display: none;
  width: 16px;
  height: 16px;
  background: url("../../assets/imgs/complete-icon.png") no-repeat center center;
  position: absolute;
  right: 0;
  background-size: contain;
}
#assetRegister .el-tree-node__content {
  position: relative;
}
#assetRegister .baseInfo-box,
#assetRegister .assetInfo-box {
  margin-top: 20px;
  margin-bottom: 20px;
  width: 100%;
  border: 1px solid #dddddd;
  border-top-color: #0494e6;
}
#assetRegister .baseInfo-title,
#assetRegister .assetInfo-title {
  color: #333;
  font-size: 16px;
  height: 32px;
  line-height: 33px;
  border-bottom: 1px solid #dddddd;
  padding-left: 15px;
  position: relative;
}
#assetRegister .baseInfo-title > i {
  background: url("../../assets/imgs/base-info-icon.png") no-repeat center
    center;
  width: 20px;
  height: 20px;
  display: inline-block;
  position: relative;
  top: 3px;
}
#assetRegister .assetInfo-title > i {
  background: url("../../assets/imgs/asset-info-icon.png") no-repeat center
    center;
  width: 20px;
  height: 20px;
  display: inline-block;
  position: relative;
  top: 4px;
}
#assetRegister label[for="financialEntryDateComn"]::before {
  content: attr(required-icon);
  color: #f56c6c;
  margin-right: 4px;
}
#assetRegister .baseInfo-body {
  padding-top: 20px;
}
#assetRegister .baseInfo-body .el-dialog .el-form-item .el-input__inner {
  width: 240px;
}
#assetRegister .enter-section-2 .dialog-body-form {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
}
#assetRegister .enter-section-2 .dialog-body {
  position: relative;
  width: 100%;
  height: 600px;
  overflow-y: auto;
  overflow-x: hidden;
}
#assetRegister .assetInfo-add-btn {
  position: absolute;
  right: 15px;
  top: 0;
  cursor: pointer;
  color: #0494e6;
}
#assetRegister .assetInfo-add-btn > i {
  color: #0494e6;
  font-size: 18px;
  position: relative;
  top: 1px;
}
#assetRegister .assetInfo-add-btn:hover {
  color: #0069bd;
}
#assetRegister .assetInfo-body .el-table .cell,
#assetRegister .assetInfo-body .el-table th div,
#assetRegister .assetInfo-body .el-table--border td:first-child .cell,
#assetRegister .assetInfo-body .el-table--border th:first-child .cell {
  padding: 0;
}
#assetRegister .assetInfo-body .el-input--small .el-input__inner {
  border: 0;
}
#assetRegister .el-select {
  width: 100%;
}
#assetRegister
  .enter-dialog
  .el-table__header-wrapper
  .has-gutter
  > tr
  > .is-leaf:last-of-type {
  padding-left: 0 !important;
}
</style>
