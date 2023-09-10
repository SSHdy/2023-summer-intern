<template>
  <div id="assetRegister">
    <div class="query-box" id="queryBox">
      <div class="clearfix" style="margin-bottom:30px;">
        <div class="query-item">
          <div class="query-title">资产名称：</div>
          <div class="query-input">
            <el-input v-model.trim="assetNameComn"></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-left:18px;">
          <div class="query-title">资产分类代码：</div>
          <div class="query-input">
            <el-input v-model.trim="categoryCodeComn"></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">资产类别：</div>
          <div class="query-input">
            <el-select v-model="assetsType" placeholder="请选择采购项目">
              <el-option
                v-for="item in assetsTypeOpts"
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
      <table-component
        :tableData="tableData"
        :tableHeight="tableHeight"
        @select="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center"></el-table-column>
        <!-- <el-table-column prop="cardId" label="卡片编号" align="center" width="200"></el-table-column> -->
        <el-table-column prop="assetNameComn" label="资产名称" align="center"></el-table-column>
        <el-table-column prop="assetsType" label="资产类别" align="center"></el-table-column>
        <el-table-column prop="categoryCodeComn" label="资产分类代码" align="center"></el-table-column>
        <el-table-column prop="categoryName" label="资产分类名称" align="center"></el-table-column>
        <el-table-column prop="procurementModeComn" label="采购组织形式" align="center"></el-table-column>
        <!-- <el-table-column prop="budgetItemNumberComn" label="预算项目编号" align="center"></el-table-column> -->
        <el-table-column prop="valueTypeComn" label="价值类型" align="center"></el-table-column>
        <el-table-column prop="assetNumberComn" label="数量" align="center"></el-table-column>
        <el-table-column prop="assetWorthComn" label="价值" align="center"></el-table-column>
        <!-- <el-table-column prop="assetRemarksComn" label="备注" align="center"></el-table-column> -->
        <el-table-column label="操作" width="90" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowLogout="false"
              :isShowLogouted="false"
              :isShowDetail="true"
              :isShowAlter="false"
              :isShowDelete="false"
              :isShowCertificate="false"
              :logoutStatus="!!parseInt(scope.row.isCancleComn)"
              @detailClick="detailView(scope.row)"
              @certificateClick="inputCertificate(scope.row)"
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
          <div class="search-box clearfix">
            <div class="search-icon"></div>
            <el-select
              v-model="selectAsset"
              filterable
              placeholder="请选择采购项目名称"
              style="width: calc(100% - 40px);"
              @change="assetChangeHandle"
            >
              <el-option
                v-for="item in assetOptions"
                :key="item.purchasingId"
                :label="item.purchasingPname"
                :value="item.purchasingId"
              ></el-option>
            </el-select>
          </div>
          <div class="introduce-box">
            <div class="baseInfo-box">
              <div class="baseInfo-title">
                <i></i> 基础信息
              </div>
              <div class="baseInfo-body">
                <el-table
                  :data="assetBaseInfoData"
                  border
                  style="width: 100%"
                  :header-cell-style="tableHeaderCellStyle"
                  :cell-style="tableCellStyle"
                  class="asset-table"
                >
                  <el-table-column prop="purchasingPname" label="采购项目名称"></el-table-column>
                  <el-table-column prop="applicant" label="申请人"></el-table-column>
                  <el-table-column prop="applicationTime" label="申请时间"></el-table-column>
                  <el-table-column prop="operator" label="经办人"></el-table-column>
                  <el-table-column prop="handleDepartment" label="经办科室"></el-table-column>
                  <el-table-column prop="procurementType" label="采购分类"></el-table-column>
                  <el-table-column prop="procurementMode" label="采购方式"></el-table-column>
                  <el-table-column prop="agency" label="代理机构"></el-table-column>
                  <el-table-column prop="organizationForm" label="组织形式"></el-table-column>
                </el-table>
              </div>
            </div>
            <div class="assetInfo-box">
              <div class="assetInfo-title">
                <i></i> 资产信息
              </div>
              <div class="assetInfo-body">
                <el-table
                  :data="assetTableData"
                  height="200"
                  border
                  style="width: 100%"
                  :header-cell-style="tableHeaderCellStyle"
                  :cell-style="tableCellStyle"
                  class="asset-table"
                >
                  <el-table-column prop="brandName" label="账面资产品牌" width="180"></el-table-column>
                  <el-table-column prop="brandMode" label="账面资产型号" width="180"></el-table-column>
                  <el-table-column prop="procurementRecord" label="采购备案"></el-table-column>
                  <el-table-column prop="unitPrice" label="单价"></el-table-column>
                  <el-table-column prop="number" label="数量"></el-table-column>
                  <el-table-column prop="unit" label="单位"></el-table-column>
                  <el-table-column prop="category" label="所属类目"></el-table-column>
                  <el-table-column prop="categoryCode" label="所属类目编号"></el-table-column>
                  <el-table-column prop="purchaseAmount" label="采购金额"></el-table-column>
                  <el-table-column label="账面资产类别" width="250" align="center">
                    <template slot-scope="scope">
                      <el-select
                        v-model="scope.row.selectAssetType"
                        filterable
                        placeholder="请选择账面资产类别"
                        style="width:200px;"
                        size="small"
                        @change="assetTypeChangeHandle(scope.row)"
                      >
                        <el-option
                          v-for="item in assetsTypeOpts"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        ></el-option>
                      </el-select>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </div>
          <div class="next-step">
            <next-btn @clickBtn="nextStep"></next-btn>
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
              <prev-btn @clickBtn="prevStep"></prev-btn>
            </div>
            <!-- <div>
              <el-button
                type="primary"
                @click="clone"
                style="width: 100px"
                :disabled="cloneBtnDisabledFlag"
              >克隆</el-button>
            </div> -->
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
            <div class="dialog-body-clone">
              <table-component
                ref="cloneTable"
                :tableData="cloneTableData"
                @select="cloneHandleSelectionChange"
                :loading="cloneLoading"
                :tableHeight="500"
              >
                <el-table-column type="selection" width="50" align="center"></el-table-column>
                <el-table-column prop="accountEntryComn" label="入账科目" align="center"></el-table-column>
                <el-table-column prop="acquireDateComn_" label="取得日期" align="center"></el-table-column>
                <el-table-column prop="acquireWayComn" label="取得方式" align="center"></el-table-column>
                <el-table-column prop="assetBrand" label="品牌" align="center"></el-table-column>
                <el-table-column prop="useManagementComn" label="使用/管理部门" align="center"></el-table-column>
                <el-table-column prop="assetUser" label="使用人" align="center"></el-table-column>
                <el-table-column prop="specificationType" label="规格型号" align="center"></el-table-column>
                <el-table-column prop="depreciationStatusComn" label="折旧状态" align="center"></el-table-column>
              </table-component>
              <div class="table-operate-box" id="operateBox">
                <div class="operate-btn">
                  <cancel-btn @clickBtn="cancelClone" :fontSize="14"></cancel-btn>
                  <confirm-btn @clickBtn="confirmClone" :fontSize="14"></confirm-btn>
                </div>
                <div class="operate-pager">
                  <table-pagination
                    @size-change="cloneChangeSize"
                    @page-change="cloneChangePage"
                    :currentPage="cloneCurrentPage"
                    :pageSize="clonePageSize"
                    :total="cloneTotal"
                  ></table-pagination>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
    <!-- 详情模态框 -->
    <el-dialog
      :visible.sync="detailDialogVisible"
      :close-on-click-modal="false"
      width="85%"
      @closed="detailHandleClose"
      class="detail-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>资产详情</span>
      </div>
      <div class="dialog-body">
        <asset-detail
          :data="assetDetailData"
          :detailAssetForm="detailAssetForm"
          :renderType="typeRender[assetDetailType]"
          :assetDetailType="assetDetailType"
          v-if="detailShow"
        ></asset-detail>
      </div>
    </el-dialog>
    <!-- 凭证模态框 -->
    <el-dialog
      :visible.sync="certificateDialogVisible"
      :close-on-click-modal="false"
      width="780px"
      @closed="certificateDialogClosed"
      :center="true"
      class="certificateDialog"
    >
      <div slot="title" class="dialog-title">
        <span>{{certificateDialogTitle}}</span>
      </div>

      <el-form :model="certificateForm" :rules="certificateRules" ref="certificateForm">
        <el-form-item label="会计凭证号" label-width="120px" prop="accountingVoucherNumberComn">
          <el-input
            v-model.trim="certificateForm.accountingVoucherNumberComn"
            size="small"
            placeholder="请输入会计凭证号"
          ></el-input>
        </el-form-item>
        <el-form-item label="卡片编号" label-width="120px" prop="cardIdComn">
          <el-input v-model.trim="certificateForm.cardIdComn" size="small" placeholder="请输入卡片编号"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="certificateDialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitCertificateForm('certificateForm')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getAssetInfoList,
  getAssetDetailInfo,
  getSelectAssetsList,
  getSelectAssetsDetailInfo,
  addAssets,
  updateAssert,
  getcloneInfoList
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
import nextBtn from "@/components/button/nextBtn";
import prevBtn from "@/components/button/prevBtn";
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
    treeSelect,
    nextBtn,
    prevBtn
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
      assetNameComn: '',
      categoryCodeComn: '',
      assetsType: '',
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
      selectData: [],
      cloneCurrentPage: 1,
      clonePageSize: 15,
      cloneTotal: 0,
      cloneLoading: true,
      cloneTableData: [],
      cloneSelectData: [],
      cloneBtnDisabledFlag: false,
      operateType: "add",
      enterDialogVisible: false,
      detailDialogVisible: false,
      assetOptions: [],
      selectAsset: '',
      currentPurchasingPname: '',
      assetBaseInfoData: [],
      assetBaseInfoForm: {
        purchasingId: '',
        purchasingPname: '',
        applicant: '',
        applicationTime: '',
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
            key: 'budgetItemNumberComn',
            text: '预算项目编号',
            form: 'input',
            show: true,
            readonly: false
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
            type: 'String',
            key: 'budgetItemNumberComn',
            text: '预算项目编号',
            form: 'input',
            show: true,
            readonly: false
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
            type: 'String',
            key: 'budgetItemNumberComn',
            text: '预算项目编号',
            form: 'input',
            show: true,
            readonly: false
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
            key: 'budgetItemNumberComn',
            text: '预算项目编号',
            form: 'input',
            show: true,
            readonly: false
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
            key: 'budgetItemNumberComn',
            text: '预算项目编号',
            form: 'input',
            show: true,
            readonly: false
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
            key: 'budgetItemNumberComn',
            text: '预算项目编号',
            form: 'input',
            show: true,
            readonly: false
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
            type: 'String',
            key: 'budgetItemNumberComn',
            text: '预算项目编号',
            form: 'input',
            show: true,
            readonly: false
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
        assetsIdComn: '',
        assetNameComn: '',
        assetNumberComn: '',
        assetWorthComn: '',
        assetsType: '',
        assetUnit: '',
        assetApplicant: '',
        applicationTime: '',
        assetOperator: '',
        managementOffice: '',
        categoryName: '',
        categoryCodeComn: '',
        procurementModeComn: '',
        budgetItemNumberComn: '',
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
        assetsIdComn: '',
        assetNameComn: '',
        assetNumberComn: '',
        assetWorthComn: '',
        assetsType: '',
        assetUnit: '',
        assetApplicant: '',
        applicationTime: '',
        assetOperator: '',
        managementOffice: '',
        categoryName: '',
        categoryCodeComn: '',
        seatingPosition: '',
        budgetItemNumberComn: '',
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
        assetsIdComn: '',
        assetsType: '',
        assetApplicant: '',
        applicationTime: '',
        managementOffice: '',
        assetOperator: '',
        assetNumberComn: '',
        assetNameComn: '',
        categoryName: '',
        categoryCodeComn: '',
        seatingPosition: '',
        budgetItemNumberComn: '',
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
        assetsIdComn: '',
        assetUnit: '',
        assetApplicant: '',
        applicationTime: '',
        assetOperator: '',
        managementOffice: '',
        assetsType: '',
        assetNameComn: '',
        categoryName: '',
        categoryCodeComn: '',
        storageLocation: '',
        procurementModeComn: '',
        budgetItemNumberComn: '',
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
        assetsIdComn: '',
        assetUnit: '',
        assetApplicant: '',
        applicationTime: '',
        assetOperator: '',
        managementOffice: '',
        assetsType: '',
        assetNameComn: '',
        categoryName: '',
        categoryCodeComn: '',
        storageLocation: '',
        procurementModeComn: '',
        budgetItemNumberComn: '',
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
        assetsIdComn: '',
        assetUnit: '',
        assetApplicant: '',
        applicationTime: '',
        assetOperator: '',
        managementOffice: '',
        assetsType: '',
        assetNameComn: '',
        categoryName: '',
        categoryCodeComn: '',
        storageLocation: '',
        procurementModeComn: '',
        budgetItemNumberComn: '',
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
        assetsIdComn: '',
        assetUnit: '',
        assetApplicant: '',
        applicationTime: '',
        managementOffice: '',
        assetOperator: '',
        assetsType: '',
        assetNameComn: '',
        assetNumberComn: '',
        categoryName: '',
        categoryCodeComn: '',
        seatingPosition: '',
        budgetItemNumberComn: '',
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
      selectedAssetDate: [],
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
        padding: "10px 0",
        color: "#333333"
      },
      tableCellStyle: {
        "border-color": "#c8c8c8",
        padding: "6px 0"
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
      token: ''
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
      getAssetInfoList(this.assetNameComn, this.assetsType, this.categoryCodeComn, this.currentPage, this.pageSize)
        .then(res => {
          this.loadingEnd();
          res.records.forEach(item => {
            if (item.assetsType !== undefined) {
              for (let i = 0; i < this.assetsTypeOpts.length; i++) {
                if (item.assetsType === this.assetsTypeOpts[i].value) {
                  item.assetsType = this.assetsTypeOpts[i].label;
                  break;
                }
              }
            }
          })
          this.tableData = res.records;
          this.total = res.total;
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
      this.assetNameComn = '';
      this.categoryCodeComn = '';
      this.assetsType = '';
      this.currentPage = 1;
      this.getList();
    },
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
    },
    // 资产入库
    add() {
      this.enterDialogVisible = true;
      this.dialogTitle = "新增资产信息";
      this.operateType = "add";
      this.treeShow = true;
      getSelectAssetsList().then(res => {
        this.assetOptions = res;
      }).catch(err => {
        const msg = err.result ? err.result : '账面资产列表获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 获取账面资产详情
    assetChangeHandle(id) {
      getSelectAssetsDetailInfo(id).then(res => {
        this.assetBaseInfoData = [];
        this.selectedAssetDate = [];
        res.assetsList.forEach(item => {
          item.selectAssetType = '';
        })
        this.assetTableData = res.assetsList
        this.currentPurchasingPname = res.purchasingPname;
        for (let key in this.assetBaseInfoForm) {
          if (key === 'applicationTime') {
            res[key] = this.formatDate(res[key]);
          }
          this.assetBaseInfoForm[key] = res[key];
        }
        this.assetBaseInfoData.push(this.assetBaseInfoForm);
      }).catch(err => {
        const msg = err.result ? err.result : '账面资产详情获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 详情
    detailView(row) {
      getAssetDetailInfo(row.id).then(res => {
        this.assetDetailData = res;
        this.detailDialogVisible = true;
        this.detailShow = true;
        switch (res.assetsType) {
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
        const msg = err.result ? err.result : '资产详情获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // dialog关闭的回调
    enterHandleClose() {
      this.selectedAssetDate.forEach((item, index) => {
        const id = `${item.selectAssetType}:${index + 1}`;
        this.$refs.tree.remove(id);
      })
      this.activeAssetForm = null;
      this.activeAssetFormName = '';
      this.assetsOtherInfo = null;
      this.showAssetForm = false;
      this.selectAsset = '';
      this.assetTableData = [];
      this.selectedAssetDate = [];
      this.activeTabName = '1';
      this.treeShow = false;
      this.isDetailPage = false;
      this.uploadSuccessImgNum = 0;
      this.uploadImgNum = 0;
      this.assetsImgList = [];
      this.fileList = [];
      this.assetBaseInfoData = [];
      this.storagedTreeArray = [];
      this.clearForm(this.activeAssetFormName);
      $('.enter-section-1').css({ 'left': 0 });
      $('.enter-section-2').css({ 'left': '100%' });
      $('.el-tree-node__content').find('i').css('display', 'none');
      for (let key in this.assetBaseInfoForm) {
        var type = this.getDataType(this.assetBaseInfoForm[key]);
        if (type === "String") {
          this.assetBaseInfoForm[key] = "";
        } else if (type === "Array") {
          this.assetBaseInfoForm[key] = [];
        } else if (type === "Object") {
          this.assetBaseInfoForm[key] = {};
        } else {
          this.assetBaseInfoForm[key] = null;
        }
      }
      // 还原克隆数据
      this.cloneCurrentPage = 1;
      this.clonePageSize = 15;
      this.cloneTotal = 0;
      this.cloneTableData = [];
      this.cloneSelectData = [];
      this.cloneBtnDisabledFlag = false;
    },
    detailHandleClose() {
      this.detailShow = false;
    },
    // 下一步
    nextStep() {
      if (this.selectedAssetDate.length === 0) {
        this.$message({
          message: "请先选择至少一个账面资产类别！",
          type: "warning"
        });
        return;
      }
      let defaultExpandNode_1 = '';
      let defaultExpandNode_2 = '';
      let defaultFormType = '';
      let defaultFormData = {};
      this.selectedAssetDate.forEach((item, index) => {
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
        if (node.childNodes && node.childNodes.length > 0) {
          const beforeNode = node.childNodes[node.childNodes.length - 1];
          this.$refs.tree.insertAfter(tempArr[0], beforeNode);
        } else {
          this.$refs.tree.updateKeyChildren(item.selectAssetType, tempArr);
        }
        for (let i = 1; i <= item.number; i++) {
          let tempObj_2 = {};
          tempObj_2.label = i + '';
          tempObj_2.id = `${item.selectAssetType}:${index + 1}:${i}`;
          tempObj_2.assetsIdComn = item.assetId;
          tempObj_2.assetNameComn = item.brandName;
          tempObj_2.assetNumberComn = item.number;
          tempObj_2.assetWorthComn = item.unitPrice;
          tempObj_2.assetsType = item.selectAssetType;
          tempObj_2.assetUnit = item.unit;
          tempObj_2.assetApplicant = this.assetBaseInfoForm.applicant;
          tempObj_2.applicationTime = this.assetBaseInfoForm.applicationTime
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
              const classname = tempObj_2.id.split(':').join('_');
              $(`.${classname}:hidden`).css('display', 'inline-block');
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
      }, 10);
      setTimeout(() => {
        for (let i = 0; i < this.storagedTreeArray.length; i++) {
          const classname = this.storagedTreeArray[i].id.split(':').join('_');
          const icon = $(`.${classname}:hidden`);
          if (icon) {
            $(`.${classname}:hidden`).css('display', 'inline-block');
          }
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
      });
      $('.enter-section-2').animate({ 'left': '100%' }, 1000);
      this.selectedAssetDate.forEach((item, index) => {
        const id = `${item.selectAssetType}:${index + 1}`;
        this.$refs.tree.remove(id);
      })
    },
    // 选择账面资产类型
    assetTypeChangeHandle(row) {
      let flag = true;
      if (this.selectedAssetDate.length > 0) {
        this.selectedAssetDate.forEach((item, index) => {
          if (row.assetId === item.assetId) {
            this.selectedAssetDate.splice(index, 1, row);
            flag = false;
          }
        })
      }
      if (flag) this.selectedAssetDate.push(row);
      let assetTreeFlag = true;
      let treeData = [];
      this.selectedAssetDate.forEach((item, index) => {
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
        this.cloneBtnDisabledFlag = true;
        this.currentCommonData = data;
        this.uploadSuccessImgNum = 0;
        this.uploadImgNum = 0;
        this.assetsImgList = [];
        this.fileList = [];
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
          // 判断是否可以克隆
          getcloneInfoList(data.assetsIdComn, this.cloneCurrentPage, this.clonePageSize)
            .then(res => {
              if (res.total > 0) {
                this.cloneBtnDisabledFlag = false;
              } else {
                this.cloneBtnDisabledFlag = true;
              }
            })
            .catch(err => {
              const msg = err.result ? err.result : '克隆信息获取失败';
              this.$message({
                message: msg,
                type: "error"
              });
            });
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
          if (this.assetsOtherInfo) {
            if (this.assetsOtherInfo.level === 1) {
              tempObj.yardId = this.assetsOtherInfo.yardId;
            } else if (this.assetsOtherInfo.level === 2) {
              tempObj.yardId = this.assetsOtherInfo.yardId;
              tempObj.buildId = this.assetsOtherInfo.buildId;
            } else if (this.assetsOtherInfo.level === 3) {
              tempObj.yardId = this.assetsOtherInfo.yardId;
              tempObj.buildId = this.assetsOtherInfo.buildId;
              tempObj.floorName = this.assetsOtherInfo.floorName;
            } else if (this.assetsOtherInfo.level === 4) {
              tempObj.yardId = this.assetsOtherInfo.yardId;
              tempObj.buildId = this.assetsOtherInfo.buildId;
              tempObj.floorName = this.assetsOtherInfo.floorName;
              tempObj.houseId = this.assetsOtherInfo.houseId;
            }
          }
          const assetsIdComn = tempObj.assetsIdComn;
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
            // 判断是否可以克隆
            getcloneInfoList(assetsIdComn, this.cloneCurrentPage, this.clonePageSize)
              .then(res => {
                if (res.total > 0) {
                  this.cloneBtnDisabledFlag = false;
                } else {
                  this.cloneBtnDisabledFlag = true;
                }
              })
              .catch(err => {
                const msg = err.result ? err.result : '克隆信息获取失败';
                this.$message({
                  message: msg,
                  type: "error"
                });
              });
          }).catch(err => {
            const msg = err.result ? err.result : '资产入库失败';
            this.$message({
              message: msg,
              type: "error"
            });
          })
        } else {
          return false;
        }
      });
    },
    // 克隆
    clone() {
      $('.dialog-body-form').animate({ 'left': '-100%' }, 1000);
      $('.dialog-body-clone').animate({ 'left': 0 }, 1000);
      this.getCloneList();
    },
    // 改变克隆页面条数
    cloneChangeSize(size) {
      this.clonePageSize = size;
      this.getCloneList();
    },
    // 改变克隆页数
    cloneChangePage(page) {
      this.cloneCurrentPage = page;
      this.getCloneList();
    },
    // 获取克隆列表
    getCloneList() {
      this.cloneLoading = true;
      const assetsId = this.currentAssetForm.assetsIdComn;
      getcloneInfoList(assetsId, this.cloneCurrentPage, this.clonePageSize)
        .then(res => {
          this.cloneLoading = false;
          this.cloneTableData = res.records;
          this.cloneTotal = res.total;
          this.cloneTableData.forEach(item => {
            if (item.acquireDateComn) {
              item.acquireDateComn_ = this.dateFormate(item.acquireDateComn, 'yyyy-MM-dd');
            }
          })
        })
        .catch(err => {
          this.cloneLoading = false;
          const msg = err.result ? err.result : '克隆信息获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 克隆table获取选中的row
    cloneHandleSelectionChange(data) {
      if (data.length > 1) {
        const cloneItem = data.pop();
        this.$refs.cloneTable.$children[0].clearSelection();
        this.$refs.cloneTable.$children[0].toggleRowSelection(cloneItem);
        this.cloneSelectData = cloneItem;
      } else {
        this.cloneSelectData = data[0];
      }
    },
    // 确定克隆
    confirmClone() {
      $('.dialog-body-form').animate({ 'left': 0 }, 1000);
      $('.dialog-body-clone').animate({ 'left': '100%' }, 1000);
      for (let key in this.currentAssetForm) {
        this.currentAssetForm[key] = this.cloneSelectData[key];
      }
      // 还原克隆数据
      this.cloneCurrentPage = 1;
      this.clonePageSize = 15;
      this.cloneTotal = 0;
      this.cloneTableData = [];
    },
    // 取消克隆
    cancelClone() {
      $('.dialog-body-form').animate({ 'left': 0 }, 1000);
      $('.dialog-body-clone').animate({ 'left': '100%' }, 1000);
      // 还原克隆数据
      this.cloneCurrentPage = 1;
      this.clonePageSize = 15;
      this.cloneTotal = 0;
      this.cloneTableData = [];
      this.cloneSelectData = [];
      this.cloneBtnDisabledFlag = false;
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
    // 输入凭证信息
    inputCertificate(row) {
      this.certificateDialogVisible = true;
      this.certificateDialogTitle = "填写凭证信息"
      this.certificateForm.assetId = row.id;
    },
    // 提交凭证信息表单
    submitCertificateForm(Form) {
      this.$refs[Form].validate(valid => {
        if (valid) {
          updateAssert(this[Form].assetId, this[Form].accountingVoucherNumberComn, this[Form].cardIdComn)
            .then(res => {
              this.$message({
                message: `凭证信息添加成功`,
                type: "success"
              });
              this.certificateDialogVisible = false;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : '凭证信息添加失败';
              this.$message({
                message: msg,
                type: "error"
              });
            });
        } else {
          return false;
        }
      });
    },
    // 关闭凭证模态框
    certificateDialogClosed() {
      this.certificateForm.accountingVoucherNumberComn = '';
      this.certificateForm.cardIdComn = '';
    },
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
    // 时间类型转换
    dateFormate(date, format) {
      var o = {
        "M+": new Date(date).getMonth() + 1,
        "d+": new Date(date).getDate(),
        "h+": new Date(date).getHours(),
        "m+": new Date(date).getMinutes(),
        "s+": new Date(date).getSeconds(),
        "q+": Math.floor((new Date(date).getMonth() + 3) / 3),
        "S": new Date(date).getMilliseconds()
      }
      if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
        (new Date(date).getFullYear() + "").substr(4 - RegExp.$1.length));
      for (var k in o) if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
          RegExp.$1.length == 1 ? o[k] :
            ("00" + o[k]).substr(("" + o[k]).length));
      return format;
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
}
#assetRegister .search-box {
  width: 50%;
  box-sizing: border-box;
  padding: 20px;
}
#assetRegister .search-box > div {
  float: left;
}
#assetRegister .search-icon {
  width: 40px;
  height: 40px;
  background: #409eff url("../../assets/imgs/proj-select-icon.png") no-repeat
    center center;
}
#assetRegister .search-box .el-input__inner {
  border-radius: 0;
}
#assetRegister .introduce-box {
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
}
#assetRegister .next-step {
  margin-top: 30px;
  text-align: right;
  margin-right: 20px;
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
#assetRegister .asset-table .el-table {
  border-color: #c8c8c8;
  box-sizing: border-box;
}
#assetRegister .asset-table .el-table::before {
  background-color: #c8c8c8;
}
#assetRegister .asset-table .el-table--border::after,
#assetRegister .asset-table .el-table--group::after {
  background-color: #c8c8c8;
}
#assetRegister .asset-table .el-table__fixed-right::before,
#assetRegister .asset-table .el-table__fixed::before {
  background-color: #c8c8c8;
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
#assetRegister .baseInfo-box {
  margin-bottom: 20px;
}
#assetRegister .baseInfo-title,
#assetRegister .assetInfo-title {
  color: #333;
  font-size: 16px;
  height: 30px;
  line-height: 30px;
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
.enter-section-2 .dialog-body-form,
.enter-section-2 .dialog-body-clone {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
}
.enter-section-2 .dialog-body-clone {
  top: 0;
  left: 100%;
  height: 100%;
  padding: 20px;
  box-sizing: border-box;
}
.enter-section-2 .dialog-body {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
}
.enter-section-2 .clone-icon {
  background: url("../../assets/imgs/clone-icon.png") no-repeat center center;
  width: 20px;
  height: 20px;
  display: inline-block;
  position: relative;
  top: 3px;
}
</style>
