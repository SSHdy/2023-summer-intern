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
      >
        <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
        <el-table-column prop="assetNameComn" label="资产名称" align="center"></el-table-column>
        <el-table-column prop="assetsType" label="资产类别" align="center"></el-table-column>
        <el-table-column prop="categoryCodeComn" label="资产分类代码" align="center"></el-table-column>
        <el-table-column prop="categoryName" label="资产分类名称" align="center"></el-table-column>
        <el-table-column prop="procurementModeComn" label="采购组织形式" align="center"></el-table-column>
        <el-table-column prop="valueTypeComn" label="价值类型" align="center"></el-table-column>
        <el-table-column prop="assetNumberComn" label="数量" align="center"></el-table-column>
        <el-table-column prop="assetWorthComn" label="价值" align="center"></el-table-column>
        <el-table-column label="操作" width="90" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowLogout="false"
              :isShowLogouted="false"
              :isShowDetail="true"
              :isShowAlter="false"
              :isShowDelete="false"
              :isShowCertificate="false"
              @detailClick="detailView(scope.row)"
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
  </div>
</template>

<script>
import {
  getAssetInfoList,
  getAssetDetailInfo,
  getSelectAssetsList,
  getSelectAssetsDetailInfo
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
      operateType: "add",
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
      detailDialogVisible: false,
      detailAssetForm: {},
      assetDetailData: {},
      assetDetailType: '',
      detailShow: false
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
    detailHandleClose() {
      this.detailShow = false;
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
</style>
