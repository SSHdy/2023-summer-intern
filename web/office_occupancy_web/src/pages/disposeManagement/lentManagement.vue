<template>
  <div id="lentManagement">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">借用单位：</div>
          <div class="query-input">
            <el-input v-model="renter" placeholder="请输入借用单位" v-enter-trim></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">出借状态：</div>
          <div class="query-input">
            <el-select filterable v-model="rentState" placeholder="请选择出借状态" style="width:100%">
              <el-option
                v-for="item in rentStateOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
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
        <el-table-column :show-overflow-tooltip="true" prop="renter" label="借用单位" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="lesseeYears"
          label="承借年限(年)"
          align="center"
        ></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="lesseeUse" label="承借用途" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="rentStateText"
          label="出借状态"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="rentBegin"
          label="借用起始时间"
          align="center"
        ></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="rentEnd" label="借用截止时间" align="center"></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDetail="true"
              :isShowDelete="false"
              :isShowSurrender="scope.row.rentState === 1"
              :isShowAlter="scope.row.rentState === 1"
              surrenderText="归还"
              @updateClick="singleUpdate(scope.row)"
              @detailClick="singleDetail(scope.row)"
              @surrenderClick="surrender(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group @addClick="addUnit" :batchDelBtnShow="false"></table-btn-group>
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
    <el-dialog
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="1100px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{dialogTitle}}</span>
      </div>
      <el-form :inline="true" :model="Form" :rules="rules" ref="Form">
        <el-form-item
          :label="operateType==='detail'? '承借用途：' : '承借用途'"
          label-width="120px"
          prop="lesseeUse"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.lesseeUse"
            size="small"
            placeholder="请输入承借用途"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '承借年限(年)：' : '承借年限(年)'"
          label-width="120px"
          prop="lesseeYears"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.lesseeYears"
            size="small"
            placeholder="请输入承借年限"
            @keyup.native="limitInt('lesseeYears')"
            @blur="limitInt('lesseeYears', true)"
            :readonly="operateType==='detail'"
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '借用单位：' : '借用单位'"
          label-width="120px"
          prop="renter"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.renter"
            size="small"
            placeholder="请输入借用单位"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '借用起始时间：' : '借用起始时间'"
          label-width="120px"
          prop="rentBegin"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-date-picker
            v-model="Form.rentBegin"
            type="date"
            placeholder="选择借用起始时间"
            size="small"
            style="width:200px;"
            v-if="operateType!=='detail'"
            value-format="yyyy-MM-dd hh:mm:ss"
            @change="rentBeginChange"
          ></el-date-picker>
          <el-input
            v-model="Form.rentBegin"
            size="small"
            :readonly="operateType==='detail'"
            v-if="operateType==='detail'"
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '借用截止时间：' : '借用截止时间'"
          label-width="120px"
          prop="rentEnd"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-date-picker
            v-model="Form.rentEnd"
            type="date"
            placeholder="借用截止时间自动生成"
            size="small"
            style="width:200px;"
            v-if="operateType!=='detail'"
            value-format="yyyy-MM-dd hh:mm:ss"
            :disabled="true"
          ></el-date-picker>
          <el-input
            v-model="Form.rentEnd"
            size="small"
            :readonly="operateType==='detail'"
            v-if="operateType==='detail'"
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '联系人：' : '联系人'"
          label-width="120px"
          prop="contacts"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.contacts"
            size="small"
            placeholder="请输入联系人"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '联系方式：' : '联系方式'"
          label-width="120px"
          prop="contactPhone"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.contactPhone"
            size="small"
            placeholder="请输入联系方式"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '建筑面积(m²)：' : '建筑面积(m²)'"
          label-width="120px"
          prop="coveredArea "
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.coveredArea"
            size="small"
            placeholder="请输入建筑面积"
            @keyup.native="limitFloat('coveredArea')"
            @blur="limitFloat('coveredArea', true)"
            :readonly="operateType==='detail'"
          ></el-input>
        </el-form-item>
        <el-form-item :label="operateType==='detail'? '类型：' : '类型'" label-width="120px" prop="type">
          <el-radio-group
            v-model="Form.state"
            @change="typeChangeHandle"
            v-if="operateType!=='detail'"
          >
            <el-radio :label="1">院落</el-radio>
            <el-radio :label="2">土地</el-radio>
          </el-radio-group>
          <span
            v-if="operateType==='detail'"
            class="state-form-item"
          >{{Form.state === 1 ? '院落' : '土地'}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '所属单位：' : '所属单位'"
          label-width="120px"
          prop="affiliatedUnit"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-select
            v-model="Form.affiliatedUnit"
            filterable
            placeholder="请选择所属单位"
            v-if="operateType !=='detail'"
            style="width:200px;"
            size="small"
          >
            <el-option
              v-for="item in affiliatedUnitList"
              :key="item.unitId"
              :label="item.unitName"
              :value="item.unitId"
            ></el-option>
          </el-select>
          <el-input
            v-model="affiliatedUnitName"
            size="small"
            :readonly="operateType ==='detail'"
            v-if="operateType ==='detail'"
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '财政备案：' : '财政备案'"
          label-width="120px"
          prop="financialCode"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.financialCode"
            size="small"
            placeholder="请输入财政备案"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '所属办公楼：' : '所属办公楼'"
          label-width="120px"
          prop="officeBuilding"
          :class="{'detail-form-item' : operateType==='detail'}"
          v-if="!isSoil"
        >
          <el-input
            v-if="operateType!=='detail'"
            v-model="Form.officeBuilding"
            size="small"
            placeholder="选择承借位置自动生成"
            :disabled="true"
          ></el-input>
          <span
            v-if="operateType==='detail'"
            class="officeBuilding-form-item"
          >{{Form.officeBuilding}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '承借位置：' : '承借位置'"
          label-width="120px"
          :class="{'detail-form-item' : operateType==='detail'}"
          prop="hasRentSite"
          style="display:block"
          v-if="!isSoil"
        >
          <tree-select
            @handleCheckChange="handleCheckChange"
            :treeData="treeData"
            :treeShow="treeShow"
            :treeName="treeName"
            :inputWidth="'868px'"
            :inputHeight="'34px'"
            v-show=" operateType!=='detail'"
          ></tree-select>
          <el-input
            v-model="houseNumber"
            size="small"
            :readonly="operateType==='detail'"
            v-if="operateType==='detail'"
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '承借位置：' : '承借位置'"
          label-width="120px"
          class="rent-site"
          :class="{'detail-form-item' : operateType==='detail'}"
          prop="soilList"
          style="display:block"
          v-if="isSoil"
        >
          <el-select
            v-model="Form.soilList"
            filterable
            multiple
            placeholder="请选择承借位置"
            v-if="operateType !=='detail'"
            style="width:868px;"
            size="small"
          >
            <el-option
              v-for="item in soilOpts"
              :key="item.yardId"
              :label="item.yardName"
              :value="item.yardId"
            ></el-option>
          </el-select>
          <el-input
            v-model="soilListName"
            size="small"
            :readonly="operateType==='detail'"
            v-if="operateType==='detail'"
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '备注信息：' : '备注信息'"
          label-width="120px"
          prop="remark"
          :class="{'detail-form-item' : operateType==='detail'}"
          style="display: block;"
        >
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入备注"
            v-model="Form.remark"
            style="width:534px;"
            :readonly="operateType==='detail'"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '合同下载：' : '上传合同'"
          label-width="120px"
          style="display: block;"
          prop="addFileFlag"
          v-if="operateType === 'add' || operateType === 'edit' || (operateType === 'detail' && detailFileListCert.length > 0)"
        >
          <div class="upload_box" v-show="operateType!=='detail'">
            <el-upload
              ref="uploadCert"
              :headers="postHeaders"
              :action="uploadCertUrl"
              :file-list="fileListCert"
              :auto-upload="false"
              :accept="acceptCertType"
              :limit="limitNum"
              :on-success="handleCertSuccess"
              :on-change="handleCertChange"
              :on-remove="handleCertRemove"
              :on-error="handleError"
              :on-exceed="handleCertExceed"
              :before-upload="beforeAvatarUpload"
              multiple
            >
              <el-button size="small" type="primary">点击上传</el-button>
            </el-upload>
          </div>
          <div class="cert-preview-box" v-show="operateType==='detail'">
            <div class="cert-preview-item" v-for="(item, index) in detailFileListCert" :key="index">
              <el-checkbox @change="certCheckboxChange($event, item.enclosureId)"></el-checkbox>
              <span class="cert-preview-name">{{item.enclosureName}}</span>
            </div>
            <el-button
              type="primary"
              icon="el-icon-download"
              class="download-btn"
              @click="downloadImg('cert')"
              v-show="detailFileListCert.length > 0"
            >下载合同</el-button>
          </div>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '证件下载：' : '上传证件'"
          label-width="120px"
          style="display: block;"
          prop="addImageFlag"
          v-if="operateType === 'add' || operateType === 'edit' || (operateType === 'detail' && detailFileListImg.length > 0)"
        >
          <div class="upload_box" v-show="operateType!=='detail'">
            <el-upload
              ref="uploadImg"
              list-type="picture-card"
              :headers="postHeaders"
              :action="uploadImgUrl"
              :file-list="fileListImg"
              :auto-upload="false"
              :accept="acceptImgType"
              :limit="limitNum"
              :on-success="handleImgSuccess"
              :on-change="handleImgChange"
              :on-error="handleError"
              :on-remove="handleImgRemove"
              :on-exceed="handleImgExceed"
              :before-upload="beforeAvatarUpload"
              multiple
            >
              <i class="el-icon-plus"></i>
            </el-upload>
          </div>
          <div class="img-preview-box clearfix" v-show="operateType==='detail'">
            <div class="img-preview-item" v-for="(item, index) in detailFileListImg" :key="index">
              <img :src="getImgUrl(item.enclosureVirtualUrl)" alt="已上传证件" />
              <div class="bigger-view-warpper">
                <div class="bigger-view-warpper-inner"></div>
                <i class="el-icon-search" @click="preview(item.enclosureVirtualUrl)"></i>
              </div>
              <el-checkbox @change="imgCheckboxChange($event, item.enclosureId)"></el-checkbox>
            </div>
            <div>
              <el-button
                type="primary"
                icon="el-icon-download"
                class="download-btn"
                @click="downloadImg('img')"
                v-show="detailFileListImg.length > 0"
              >下载证件</el-button>
            </div>
          </div>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" v-show="operateType!=='detail'">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- 图片预览 -->
    <div class="img-preview-bigger">
      <div class="img-preview-bigger-mask"></div>
      <img src alt id="biggerImg" @load="loadImage" />
      <i class="el-icon-circle-close" @click="closePreview"></i>
    </div>
  </div>
</template>

<script>
import { BASE_URL, url_name_1 } from "@/axios/global";
import {
  getUserUnitList
} from "@/axios/commonApi";
import {
  getRentInfoList,
  deleteRent,
  addRent,
  updateRent,
  getRentDetail,
  getSpareRoomTree,
  downloadRentAccessory,
  rentWithdrawal
} from "./api";
import { uploadUrl, getAllYardList } from "@/axios/commonApi";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import treeSelect from "@/components/treeSelect/treeSelect_3";
import moment from "moment";
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
    treeSelect
  },
  data() {
    let validcodeRentBegin = (rule, value, callback) => {
      const rentBegin = new Date(this.Form.rentBegin).getTime();
      const rentEnd = new Date(this.Form.rentEnd).getTime();
      if (!rentBegin && rentEnd) {
        callback(new Error('借用起始时间不能为空'));
      }
      if (rentBegin && rentEnd && (rentBegin > rentEnd)) {
        callback(new Error('借用起始时间不能大于借用截止时间'));
      }
      callback()
    };
    let validcodeRentEnd = (rule, value, callback) => {
      const rentBegin = new Date(this.Form.rentBegin).getTime();
      const rentEnd = new Date(this.Form.rentEnd).getTime();
      if (rentBegin && !rentEnd) {
        callback(new Error('借用截止时间不能为空'));
      }
      if (rentBegin && rentEnd && (rentBegin > rentEnd)) {
        callback(new Error('借用截止时间不能小于借用起始时间'));
      }
      callback()
    };
    let validUpload_1 = (rule, value, callback) => {
      if (this.fileListCert.length === 0) {
        callback(new Error('请先上传合同'));
      } else {
        callback()
      }
    };
    let validUpload_2 = (rule, value, callback) => {
      if (this.fileListImg.length === 0) {
        callback(new Error('请先上传证件'));
      } else {
        callback()
      }
    };
    return {
      treeName: 'rent',
      renter: "",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      selectData: [],
      tableData: [],
      dialogTitle: "",
      operateType: "add",
      dialogVisible: false,
      Form: {
        type: 2,
        state: 1,
        soilList: [],
        rentId: "",
        lesseeUse: "",
        lesseeYears: "",
        renter: "",
        rentBegin: "",
        rentEnd: "",
        contacts: "",
        contactPhone: "",
        addressList: [],
        remark: '',
        deleteEnclosureIdList: [],
        housingUseList: [],
        hasRentSite: '',
        coveredArea: '',
        officeBuilding: '',
        affiliatedUnit: '',
        financialCode: '',
        addImageFlag: '',
        addFileFlag: '',
        delHouseIdList: []
      },
      yardList: [],
      soilListName: '',
      token: '',
      postHeaders: {
        Authorization: ''
      },
      limitNum: 5,
      uploadImgUrl: `${uploadUrl}?uploadType=rentImg`,
      uploadCertUrl: `${uploadUrl}?uploadType=rentCertificate`,
      fileListCert: [],
      fileListImg: [],
      oldFileListCert: [],
      oldFileListImg: [],
      acceptCertType: ".doc,.docx",
      acceptImgType: "image/jpeg,image/gif,image/png,image/bmp,.doc,.docx,.xlsx,.ppt,.pdf,.rar,.zip",
      uploadCertNum: 0,
      uploadedCertNum: 0,
      uploadSuccessCertNum: 0,
      uploadImgNum: 0,
      uploadedImgNum: 0,
      uploadSuccessImgNum: 0,
      submitCertFlag: false,
      submitImgFlag: false,
      treeShow: false,
      treeData: [],
      checkedNodes: [],
      houseIdList: [],
      houseNameList: [],
      expandIdList: [],
      houseNumber: '',
      detailFileListCert: [],
      detailFileListImg: [],
      downloadImgList: [],
      downloadCertList: [],
      uploadImgChangeFlag: false,
      uploadCertChangeFlag: false,
      rentState: 1,
      rentStateOptions: [
        {
          id: '',
          name: '全部'
        },
        {
          id: 1,
          name: '出借中'
        },
        {
          id: 0,
          name: '已归还'
        }
      ],
      affiliatedUnitList: [],
      affiliatedUnitName: '',
      isSoil: false,
      soilOpts: [],
      updateHouseId: [],
      rules: {
        soilList: [
          { required: true, message: "承借位置不能为空", trigger: "change" }
        ],
        hasRentSite: [
          { required: true, message: "承借位置不能为空", trigger: "blur" }
        ],
        renter: [
          { required: true, message: "借用单位不能为空", trigger: "blur" }
        ],
        rentBegin: [
          { required: true, message: "借用起始时间不能为空", trigger: "blur" },
          { validator: validcodeRentBegin, trigger: "blur" }
        ],
        rentEnd: [
          { required: true, message: "借用截止时间不能为空", trigger: "blur" },
          { validator: validcodeRentEnd, trigger: "blur" }
        ],
        addFileFlag: [
          { required: true, validator: validUpload_1, trigger: "blur" }
        ],
        addImageFlag: [
          { required: true, validator: validUpload_2, trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    // 租用起始时间改变
    rentBeginChange() {
      if (this.Form.rentBegin && this.Form.lesseeYears) {
        const nextYear = parseInt(this.Form.lesseeYears);
        const rentEnd = this.getAfterNYear(this.Form.rentBegin, nextYear);
        this.Form.rentEnd = moment(rentEnd).format('YYYY-MM-DD')
      } else {
        this.Form.rentEnd = "";
      }
    },
    // 计算几年后时间
    getAfterNYear(startdate, nextYear) {
      var expriedYear = parseInt(startdate.substring(0, 4)) + nextYear;
      var expriedMonth = startdate.substring(4, 6);
      var expriedDay = startdate.substring(6);
      //考虑二月份场景，若N年后的二月份日期大于该年的二月份的最后一天，则取该年二月份最后一天
      if (expriedMonth == '02' || expriedMonth == 2) {
        var monthEndDate = new Date(expriedYear, expriedMonth, 0).getDate();
        if (parseInt(expriedDay) > monthEndDate) {//为月底时间
          //取两年后的二月份最后一天
          expriedDay = monthEndDate;
        }
      }
      return expriedYear + expriedMonth + expriedDay;
    },
    // 选择类型
    typeChangeHandle(val) {
      if (val === 1) {
        this.isSoil = false;
        this.$refs["Form"].clearValidate('soilList');
        if (this.Form.state === 1) {
          if (this.yardList && this.yardList.length > 0) {
            this.expandIdList = [];
            this.houseNameList = [];
            this.houseIdList = [];
            let officeBuildingNameArr = [];
            this.yardList.forEach(yard => {
              this.expandIdList.push(yard.yardId);
              yard.buildList.forEach(build => {
                officeBuildingNameArr.push(build.buildName);
                this.expandIdList.push(build.buildId);
                build.floorNameList.forEach(floor => {
                  this.expandIdList.push(`${build.buildId}:${floor.floorName}`);
                  floor.houseNameList.forEach(house => {
                    this.houseIdList.push(`${yard.yardId}:${build.buildId}:${floor.floorName}:${house.houseId}`);
                    this.houseNameList.push(house.houseNumber);
                  })
                })
              })
            })
            if (this.houseNameList.length > 0) {
              this.Form.hasRentSite = 'true'
            } else {
              this.Form.hasRentSite = '';
            }
            this.Form.officeBuilding = officeBuildingNameArr.join(',');
            this.$nextTick(() => {
              eventBus.$emit(`${this.treeName}_select`, this.houseNameList, this.houseIdList, this.expandIdList);
            })
          }
        }
      } else {
        this.isSoil = true;
        this.Form.officeBuilding = '';
        this.$refs["Form"].clearValidate('hasRentSite');
      }
    },
    // 获取土地下拉选项
    getSoilOpts() {
      getAllYardList(2)
        .then(res => {
          this.soilOpts = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : '土地下拉菜单获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
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
    // 获取信息列表
    getList() {
      this.loadingStart('.table-box');
      getRentInfoList(this.currentPage, this.pageSize, this.renter, this.rentState, 2)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach(item => {
            if (item.rentType) {
              switch (item.rentType) {
                case 1:
                  item.rentType = '直接出租'
                  break;
                case 2:
                  item.rentType = '转出租'
                  break;
                case 3:
                  item.rentType = '回租出租'
                  break;
              }
            }
            if (item.rentState !== undefined) {
              this.rentStateOptions.forEach(opt => {
                if (opt.id === item.rentState) {
                  item.rentStateText = opt.name;
                }
              })
            }
            if (item.rentMoney) {
              item.rentMoney = String(item.rentMoney.toFixed(2));
            }
            if (item.deposit) {
              item.deposit = String(item.deposit.toFixed(2));
            }
            if (item.rentBegin) {
              item.rentBegin = moment(item.rentBegin).format('YYYY-MM-DD')
            }
            if (item.rentEnd) {
              item.rentEnd = moment(item.rentEnd).format('YYYY-MM-DD')
            }
            if (item.paymentTime) {
              item.paymentTime = moment(item.paymentTime).format('YYYY-MM-DD')
            }
          })
          this.loadingEnd();
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '出借信息获取失败';
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
      this.currentPage = 1;
      this.renter = '';
      this.rentState = 1;
      this.search();
    },
    // 新增
    addUnit() {
      this.dialogVisible = true;
      this.dialogTitle = "新增出借信息";
      this.operateType = "add";
      this.treeShow = true;
      this.getSpareRoomTree();
    },
    // 单个删除
    singleDetle(row) {
      this.$confirm(`此操作将永久删除借用单位为${row.renter}的出借信息, 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteRent(parseInt(row.rentId))
            .then(res => {
              this.$message({
                message: "删除出借信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : '删除出借信息失败！';
              this.$message({
                message: msg,
                type: "error"
              });
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    // 单个修改
    singleUpdate(row) {
      this.dialogVisible = true;
      this.dialogTitle = "修改出借信息";
      this.operateType = "edit";
      this.treeShow = true;
      getRentDetail(row.rentId).then(res => {
        this.getSpareRoomTree(res.rentId, res.rentRelationshipIdList);
        for ([key, value] of Object.entries(res)) {
          if (this.Form[key] !== undefined && value !== '') {
            this.Form[key] = value
          }
        }
        this.updateHouseId = [];
        this.yardList = res.yardList;
        if (res.state === 1) {
          this.isSoil = false;
          if (res.yardList && res.yardList.length > 0) {
            res.yardList.forEach(yard => {
              this.expandIdList.push(yard.yardId);
              yard.buildList.forEach(build => {
                this.expandIdList.push(build.buildId);
                build.floorNameList.forEach(floor => {
                  this.expandIdList.push(`${build.buildId}:${floor.floorName}`);
                  floor.houseNameList.forEach(house => {
                    this.houseIdList.push(`${yard.yardId}:${build.buildId}:${floor.floorName}:${house.houseId}`);
                    this.updateHouseId.push(house.houseId);
                    this.houseNameList.push(house.houseNumber);
                  })
                })
              })
            })
          }
          if (this.houseNameList.length > 0) {
            this.Form.hasRentSite = 'true'
          } else {
            this.Form.hasRentSite = '';
          }
          eventBus.$emit(`${this.treeName}_select`, this.houseNameList, this.houseIdList, this.expandIdList);
        } else {
          this.isSoil = true;
          if (res.yardList && res.yardList.length > 0) {
            this.Form.soilList = [];
            res.yardList.forEach(item => {
              this.Form.soilList.push(item.yardId);
            })
          }
        }
        if (res.enclosureList && res.enclosureList.length > 0) {
          let imgNum = 0;
          let certNum = 0;
          res.enclosureList.forEach((item, index) => {
            if (item.enclosureType === 1) {
              imgNum++;
              const obj = {
                name: item.enclosureId,
                url: BASE_URL + url_name_1 + item.enclosureVirtualUrl + '?access_token=' + this.token
              };
              this.fileListImg.push(obj);
              this.oldFileListImg.push(obj);
            } else if (item.enclosureType === 2) {
              certNum++
              const obj = {
                name: item.enclosureName,
                uid: item.enclosureId,
                url: BASE_URL + url_name_1 + item.enclosureVirtualUrl + '?access_token=' + this.token
              };
              this.fileListCert.push(obj);
              this.oldFileListCert.push(obj);
            } else if (item.enclosureType === 3) {
              imgNum++;
              const obj = {
                name: item.enclosureId,
                url: BASE_URL + url_name_1 + '/images/compress.png' + '?access_token=' + this.token
              };
              this.fileListImg.push(obj);
              this.oldFileListImg.push(obj);
            }
          });
          this.uploadedCertNum = certNum;
          this.uploadedImgNum = imgNum;
        }
      }).catch(err => {
        const msg = err.result ? err.result : '出借详情获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 单个详情
    singleDetail(row) {
      this.dialogVisible = true;
      this.dialogTitle = "出借信息详情";
      this.operateType = "detail";
      getRentDetail(row.rentId).then(res => {
        for (let key in this.Form) {
          if (res[key] !== undefined && res[key] !== '') {
            const val = res[key];
            if (key === 'rentType') {
              switch (val) {
                case 1:
                  this.Form[key] = '直接出租'
                  break;
                case 2:
                  this.Form[key] = '转出租'
                  break;
                case 3:
                  this.Form[key] = '回租出租'
                  break;
              }
            } else if (key === 'rentMode') {
              switch (val) {
                case 1:
                  this.Form[key] = '承租'
                  break;
                case 2:
                  this.Form[key] = '预租'
                  break;
                case 3:
                  this.Form[key] = '转租'
                  break;
                case 4:
                  this.Form[key] = '委托出租'
                  break;
                case 5:
                  this.Form[key] = '代理出租'
                  break;
                case 6:
                  this.Form[key] = '承租权转让'
                  break;
                case 7:
                  this.Form[key] = '承租权交换'
                  break;
              }
            } else if (key === 'rentBegin' || key === 'rentEnd') {
              this.Form[key] = moment(val).format('YYYY-MM-DD')
            } else if (key === 'affiliatedUnit') {
              this.affiliatedUnitList.forEach(item => {
                if (item.unitId === res.affiliatedUnit) {
                  this.affiliatedUnitName = item.unitName;
                }
              })
            } else {
              this.Form[key] = val;
            }
          } else {
            this.Form[key] = '暂无';
            if (key === 'affiliatedUnit') {
              this.affiliatedUnitName = '暂无';
            }
          }
        }
        if (res.state === 1) {
          this.isSoil = false;
          if (res.yardList && res.yardList.length > 0) {
            res.yardList.forEach(yard => {
              yard.buildList.forEach(build => {
                build.floorNameList.forEach(floor => {
                  floor.houseNameList.forEach(house => {
                    this.houseNameList.push(house.houseNumber);
                  })
                })
              })
            })
            this.houseNumber = this.houseNameList.join(',');
          } else {
            this.houseNumber = '暂无'
          }
        } else {
          this.isSoil = true;
          let soilListNameArr = [];
          if (res.yardList && res.yardList.length > 0) {
            res.yardList.forEach(item => {
              soilListNameArr.push(item.yardName);
            })
            this.soilListName = soilListNameArr.join(',')
          }
        }
        if (res.enclosureList.length > 0) {
          res.enclosureList.forEach((item, index) => {
            if (item.enclosureType === 1) {
              this.detailFileListImg.push(item);
            } else if (item.enclosureType === 2) {
              this.detailFileListCert.push(item);
            } else if (item.enclosureType === 3) {
              item.enclosureVirtualUrl = '/images/compress.png';
              this.detailFileListImg.push(item);
            }
          });
        }
      }).catch(err => {
        const msg = err.result ? err.result : '出借详情获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 获取闲置用房树
    getSpareRoomTree(id, rentRelationshipIdList) {
      const _id = id ? id : null;
      const _rentRelationshipIdList = rentRelationshipIdList ? rentRelationshipIdList : null;
      getSpareRoomTree(_id, 6, _rentRelationshipIdList).then(res => {
        this.treeData = res;
      }).catch(err => {
        const msg = err.result ? err.result : '获取闲置用房树形菜单失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          (this.uploadImgNum === 0 && this.uploadCertNum === 0) ? this.saveForm() : this.submitUpload();
        } else {
          return false;
        }
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
    // dialog打开
    dialogOpen() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
      })
    },
    // dialog关闭的回调
    dialogClosed() {
      this.clearForm(this.Form);
      this.Form.type = 2;
      this.Form.state = 1;
      this.Form.addressList = [];
      this.Form.deleteEnclosureIdList = [];
      this.fileListCert = [];
      this.fileListImg = [];
      this.oldFileListCert = [];
      this.oldFileListImg = [];
      this.uploadCertNum = 0;
      this.uploadedCertNum = 0;
      this.uploadSuccessCertNum = 0;
      this.uploadImgNum = 0;
      this.uploadedImgNum = 0;
      this.uploadSuccessImgNum = 0;
      this.submitCertFlag = false;
      this.submitImgFlag = false;
      this.treeShow = false;
      this.checkedNodes = [];
      this.houseIdList = [];
      this.houseNameList = [];
      this.expandIdList = [];
      this.houseNumber = '';
      this.detailFileListCert = [];
      this.detailFileListImg = [];
      this.downloadImgList = [];
      this.downloadCertList = [];
      this.uploadCertChangeFlag = false;
      this.uploadImgChangeFlag = false;
      this.affiliatedUnitName = "";
      this.isSoil = false;
      this.soilListName = '';
      this.yardList = [];
      this.updateHouseId = [];
      eventBus.$emit(`${this.treeName}_clear`);
    },
    // 开始上传文件
    submitUpload() {
      this.$refs.uploadCert.submit();
      this.$refs.uploadImg.submit();
    },
    // 文件上传之前
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 200;
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过200MB!');
      }
      return isLt2M;
    },
    // 删除上传文件时钩子
    handleCertRemove(file, fileList) {
      if (file.status === 'success') {
        this.Form.deleteEnclosureIdList.push(file.uid);
        this.uploadedCertNum--;
      }
      this.uploadCertNum = fileList.length - this.uploadedCertNum;
      this.fileList = fileList;
      this.fileListCert = fileList;
      if (fileList.length === 0) {
        this.Form.addFileFlag = '';
      }
    },
    handleImgRemove(file, fileList) {
      if (file.status === 'success') {
        this.Form.deleteEnclosureIdList.push(file.name);
        this.uploadedImgNum--;
      }
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
      this.fileListImg = fileList;
      if (fileList.length === 0) {
        this.Form.addImageFlag = '';
      }
    },
    // 上传文件成功时钩子
    handleCertSuccess(response, file, fileList) {
      this.uploadSuccessCertNum++;
      this.Form.addressList.push({
        enclosureType: 2, // 1图片 2文件
        enclosurePhysicsUrl: response.imgList[0].imgPath,
        enclosureVirtualUrl: response.imgList[0].imgUrl,
        enclosureName: file.name
      });
      if ((this.uploadSuccessCertNum === this.uploadCertNum)) {
        this.submitCertFlag = true;
      }
      if (((this.submitCertFlag && this.uploadCertChangeFlag) && (this.submitImgFlag && this.uploadImgChangeFlag)) ||
        ((this.submitCertFlag && this.uploadCertChangeFlag) && !this.uploadImgChangeFlag)) {
        this.saveForm();
      }
    },
    handleImgSuccess(response, file, fileList) {
      this.uploadSuccessImgNum++;
      let type;
      const fileType = file.raw.type.split('/')[0];
      type = fileType !== 'image' ? 3 : 1;
      this.Form.addressList.push({
        enclosureType: type, // 1图片 2文件
        enclosurePhysicsUrl: response.imgList[0].imgPath,
        enclosureVirtualUrl: response.imgList[0].imgUrl,
        enclosureName: file.name
      });
      if ((this.uploadSuccessImgNum === this.uploadImgNum)) {
        this.submitImgFlag = true;
      }
      if (((this.submitCertFlag && this.uploadCertChangeFlag) && (this.submitImgFlag && this.uploadImgChangeFlag)) ||
        (!this.uploadCertChangeFlag && (this.submitImgFlag && this.uploadImgChangeFlag))) {
        this.saveForm();
      }
    },
    // 上传文件改变时钩子
    handleCertChange(file, fileList) {
      this.uploadCertChangeFlag = true;
      this.uploadCertNum = fileList.length - this.uploadedCertNum;
      this.fileListCert = fileList;
      if (fileList.length > 0) {
        this.Form.addFileFlag = 'true';
      }
      this.$refs['Form'].clearValidate(['addFileFlag']);
    },
    handleImgChange(file, fileList) {
      const fileType = file.raw.type.split('/')[0];
      if (fileType !== 'image') {
        file.url = `${BASE_URL}${url_name_1}/images/compress.png`;
      }
      this.uploadImgChangeFlag = true;
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
      this.fileListImg = fileList;
      if (fileList.length > 0) {
        this.Form.addImageFlag = 'true';
      }
      this.$refs['Form'].clearValidate(['addImageFlag']);
    },
    // 上传文件超出限制个数
    handleCertExceed(file, fileList) {
      this.$message.warning(`最多只能上传五份文件！`);
    },
    handleImgExceed(file, fileList) {
      this.$message.warning(`最多只能上传五张图片！`);
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
    // 选择树
    handleCheckChange(checkedNodes, halfCheckedNodes) {
      this.checkedNodes = checkedNodes;
      if (checkedNodes.length > 0) {
        this.Form.hasRentSite = 'true'
        let tempArr = [];
        const nodes = checkedNodes.concat(halfCheckedNodes);
        nodes.forEach(item => {
          if (item.level === 1) {
            tempArr.push(item.name);
          }
        });
        this.Form.officeBuilding = tempArr.join(',');
      } else {
        this.Form.hasRentSite = ''
        this.Form.officeBuilding = ''
      }
    },
    // 保存表单
    saveForm() {
      this.Form.housingUseList = [];
      this.Form.delHouseIdList = [];
      this.Form.rentEnd = moment(this.Form.rentEnd).format('YYYY-MM-DD 23:59:59')
      if (this.Form.state === 2 && this.updateHouseId.length > 0) {
        this.Form.delHouseIdList = this.updateHouseId;
      }
      if (this.Form.state === 1 && this.checkedNodes.length > 0) {
        let newHouseId = [];
        this.checkedNodes.forEach(node => {
          if (node.level === 3) {
            const obj = {};
            const arr = node.id.split(':');
            obj.yardId = arr[0];
            obj.buildId = arr[1];
            obj.floorName = arr[2];
            obj.houseId = arr[3];
            newHouseId.push(arr[3]);
            this.Form.housingUseList.push(obj);
          }
        });
        if (this.updateHouseId.length > 0) {
          this.updateHouseId.forEach(item => {
            if (newHouseId.indexOf(item) === -1) {
              this.Form.delHouseIdList.push(item);
            }
          })
        }
        this.soilList = []
      }
      if (this.operateType === 'add') {
        addRent(this.Form).then(res => {
          this.dialogVisible = false;
          this.getList();
          this.$message({
            message: "出借信息新增成功!",
            type: "success"
          });
        }).catch(err => {
          const msg = err.result ? err.result : '出借信息新增失败';
          this.$message({
            message: msg,
            type: "error"
          });
          this.fileListCert = this.oldFileListCert;
          this.fileListImg = this.oldFileListImg;
          this.uploadCertNum = 0;
          this.uploadSuccessCertNum = 0;
          this.uploadImgNum = 0;
          this.uploadSuccessImgNum = 0;
          this.submitCertFlag = false;
          this.submitImgFlag = false;
          this.uploadCertChangeFlag = false;
          this.uploadImgChangeFlag = false;
          this.Form.addressList = [];
        })
      } else if (this.operateType === 'edit') {
        updateRent(this.Form).then(res => {
          this.dialogVisible = false;
          this.getList();
          this.$message({
            message: "出借信息修改成功!",
            type: "success"
          });
        }).catch(err => {
          const msg = err.result ? err.result : '出借信息修改失败';
          this.$message({
            message: msg,
            type: "error"
          });
          this.fileListCert = this.oldFileListCert;
          this.fileListImg = this.oldFileListImg;
          this.uploadCertNum = 0;
          this.uploadSuccessCertNum = 0;
          this.uploadImgNum = 0;
          this.uploadSuccessImgNum = 0;
          this.submitCertFlag = false;
          this.submitImgFlag = false;
          this.uploadCertChangeFlag = false;
          this.uploadImgChangeFlag = false;
          this.Form.addressList = [];
        })
      }
    },
    // 文件多选框选择
    certCheckboxChange($event, id) {
      if ($event) {
        if (this.downloadCertList.indexOf(id) === -1) {
          this.downloadCertList.push(id);
        }
      } else {
        const index = this.downloadCertList.indexOf(id);
        this.downloadCertList.splice(index, 1);
      }
    },
    // 图片多选框选择
    imgCheckboxChange($event, id) {
      if ($event) {
        if (this.downloadImgList.indexOf(id) === -1) {
          this.downloadImgList.push(id);
        }
      } else {
        const index = this.downloadImgList.indexOf(id);
        this.downloadImgList.splice(index, 1);
      }
    },
    // 下载图片
    downloadImg(type) {
      if (type === 'cert') {
        if (this.downloadCertList.length > 0) {
          downloadRentAccessory(this.downloadCertList, 3, this.Form.rentId).then(res => {
            let fileDownload = require('js-file-download');
            let name = decodeURIComponent(sessionStorage.getItem('CADdownloadFileName'));
            fileDownload(res, name);
          }).catch(err => {
            const msg = err.result ? err.result : '合同下载失败';
            this.$message({
              message: msg,
              type: "error"
            });
          })
        } else {
          this.$message({
            message: "请先选中一个合同",
            type: "warning"
          });
        }
      } else {
        if (this.downloadImgList.length > 0) {
          downloadRentAccessory(this.downloadImgList, 4, this.Form.rentId).then(res => {
            let fileDownload = require('js-file-download');
            let name = decodeURIComponent(sessionStorage.getItem('CADdownloadFileName'));
            fileDownload(res, name);
          }).catch(err => {
            const msg = err.result ? err.result : '证件下载失败';
            this.$message({
              message: msg,
              type: "error"
            });
          })
        } else {
          this.$message({
            message: "请先选中一个证件",
            type: "warning"
          });
        }
      }

    },
    // 获取图片链接
    getImgUrl(url) {
      return `${BASE_URL}${url_name_1}${url}?access_token=${this.token}`
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
      this.Form[key] = this.Form[key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '');
      if (flag && this.Form[key] !== '') {
        this.Form[key] = (this.Form[key] - 0).toFixed(2);
      }
    },
    // 只能输入正整数
    limitInt(key, flag) {
      this.Form[key] = this.Form[key].replace(/[^0-9]/g, "");
      if (flag && key === 'lesseeYears') {
        if (this.Form.rentBegin && this.Form.lesseeYears) {
          const nextYear = parseInt(this.Form.lesseeYears);
          const rentEnd = this.getAfterNYear(this.Form.rentBegin, nextYear);
          this.Form.rentEnd = moment(rentEnd).format('YYYY-MM-DD')
        } else {
          this.Form.rentEnd = '';
        }
      }
    },
    // 预览
    preview(url) {
      $('.img-preview-bigger').css('display', 'block');
      const img = $('#biggerImg');
      img.attr('src', BASE_URL + url_name_1 + url + '?access_token=' + this.token);
    },
    // 关闭预览
    closePreview() {
      $('.img-preview-bigger').css('display', 'none');
      const img = $('#biggerImg');
      img.css('width', 'auto');
    },
    // 图片加载完成
    loadImage() {
      const window_width = $(window).width();
      const img = $('#biggerImg');
      const img_width = img.width();
      if (img_width >= window_width) {
        img.css('width', '80%');
      }
    },
    // 归还
    surrender(row) {
      this.$confirm("归还, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          rentWithdrawal(parseInt(row.rentId))
            .then(res => {
              this.$message({
                message: "归还成功!",
                type: "success"
              });
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : '归还失败';
              this.$message({
                message: msg,
                type: "error"
              });
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消归还"
          });
        });
    },
    // 用户权限下的单位列表
    getUserUnitList() {
      getUserUnitList().then(res => {
        this.affiliatedUnitList = res;
      }).catch(err => {
        const msg = err.result ? err.result : '单位获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    }
  },
  mounted() {
    this.getList();
    this.getUserUnitList();
    this.getSoilOpts();
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - queryBoxH - operateBoxH;
    }, 1);
    this.token = sessionStorage.getItem('TOKEN');
    if (this.token) {
      this.postHeaders.Authorization = `Bearer ${this.token}`;
    }
  }
};
</script>

<style>
#lentManagement .query-box {
  margin-bottom: 20px;
}
#lentManagement .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#lentManagement .query-item > div {
  float: left;
}
#lentManagement .query-item > .query-input {
  width: calc(100% - 80px);
}
#lentManagement .query-item .el-date-editor.el-input,
#lentManagement .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#lentManagement .query-btn {
  float: left;
  margin-left: 50px;
}
#lentManagement .el-textarea__inner {
  width: 868px;
  line-height: 40px;
  padding-top: 0;
}
#lentManagement .el-textarea {
  vertical-align: middle !important;
}
#lentManagement .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#lentManagement .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#lentManagement .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#lentManagement .upload_box .el-upload--picture-card {
  width: 100px;
  height: 100px;
  line-height: 100px;
}
#lentManagement
  .upload_box
  .el-upload-list--picture-card
  .el-upload-list__item {
  width: 100px;
  height: 100px;
}
#lentManagement .el-upload--picture-card {
  margin-bottom: 8px;
}
#lentManagement .el-upload-list > li {
  margin-bottom: 0;
}
#lentManagement .detail-form-item .el-input__inner {
  border: 0;
  padding-left: 0;
  width: 215px;
}
#lentManagement .detail-form-item .el-textarea__inner {
  border: 0;
  padding-left: 0;
  width: 550px;
}
#lentManagement .img-preview-item {
  float: left;
  margin-right: 20px;
  margin-bottom: 15px;
  width: 100px;
  height: 100px;
  border: 1px solid #c0ccda;
  position: relative;
}
#lentManagement .img-preview-item > img {
  width: 100%;
  height: 100%;
}
#lentManagement .img-preview-item > .bigger-view-warpper {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}
#lentManagement
  .img-preview-item
  > .bigger-view-warpper
  > .bigger-view-warpper-inner {
  width: 100%;
  height: 100%;
  background: #000;
  opacity: 0.5;
  display: none;
}
#lentManagement
  .img-preview-item
  > .bigger-view-warpper:hover
  > .bigger-view-warpper-inner {
  display: block;
}
#lentManagement .img-preview-item > .bigger-view-warpper > i {
  position: absolute;
  font-size: 26px;
  color: #ffffff;
  cursor: pointer;
  display: none;
  top: 50%;
  left: 50%;
  margin-left: -13px;
  margin-top: -13px;
}
#lentManagement .img-preview-item > .bigger-view-warpper > i:hover {
  color: #3891e0;
}
#lentManagement .img-preview-item > .bigger-view-warpper:hover > i {
  display: block;
}
#lentManagement .img-preview-item > .el-checkbox {
  position: absolute;
  bottom: -13px;
  right: 0;
}
#lentManagement .download-btn {
  padding: 7px 22px;
}
#lentManagement .img-preview-bigger {
  width: 100%;
  height: 100%;
  position: fixed;
  z-index: 100000;
  top: 0;
  left: 0;
  display: none;
}
#lentManagement .img-preview-bigger > .img-preview-bigger-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
}
#lentManagement .img-preview-bigger > img {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
}
#lentManagement .img-preview-bigger > i {
  position: absolute;
  font-size: 50px;
  top: 30px;
  right: 30px;
  color: #ffffff;
  cursor: pointer;
}
#lentManagement .img-preview-bigger > i:hover {
  color: #3891e0;
}
#lentManagement .rent-site .el-input__inner {
  width: 868px;
}
#lentManagement .cert-preview-item .el-checkbox {
  margin-right: 5px;
  color: #606266;
}
#lentManagement .officeBuilding-form-item,
#lentManagement .state-form-item {
  width: 215px;
  display: inline-block;
  padding-right: 15px;
  box-sizing: border-box;
  color: #606266;
}
</style>


