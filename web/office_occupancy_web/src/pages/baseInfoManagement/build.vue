<template>
  <div id="baseInfoBuild">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">院落名称：</div>
          <div class="query-input">
            <el-select filterable v-model="yard_id" placeholder="请选择院落名称" style="width:100%">
              <el-option
                v-for="item in yardOptions"
                :key="item.yardId"
                :label="item.yardName"
                :value="item.yardId"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">楼座名称：</div>
          <div class="query-input">
            <el-input v-model.trim="build_name" placeholder="请输入楼座名称"></el-input>
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
          prop="buildArea"
          label="建筑面积(m²)"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="structureType"
          label="结构类型"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="overgroundFloor"
          label="地上楼层"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="undergroundFloor"
          label="地下楼层"
          align="center"
        ></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="buildDate" label="建成时间" align="center"></el-table-column>
        <el-table-column label="操作" width="140" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDetail="false"
              @deleteClick="singleDetle(scope.row)"
              @updateClick="singleUpdate(scope.row)"
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
      <el-form :inline="true" :model="Form" :rules="rules" ref="Form" class="build-info-Form">
        <el-form-item label="院落名称" label-width="130px" prop="yardId">
          <el-select
            filterable
            v-model="Form.yardId"
            placeholder="请选择"
            style="width:200px"
            size="small"
            @change="selectYardHandle"
            :disabled="operateType==='edit'"
          >
            <el-option
              v-for="item in yardOptionsModal"
              :key="item.yardId"
              :label="item.yardName"
              :value="item.yardId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="楼座名称" label-width="130px" prop="buildName">
          <el-input v-model.trim="Form.buildName" size="small" placeholder="请输入楼座名称"></el-input>
        </el-form-item>
        <el-form-item label="建筑面积(m²)" label-width="130px" prop="buildArea">
          <el-input
            v-model.trim="Form.buildArea"
            size="small"
            @keyup.native="limitFloat('buildArea')"
            @blur="limitFloat('buildArea', true)"
            placeholder="请输入建筑面积"
          ></el-input>
        </el-form-item>
        <el-form-item label="建成时间" label-width="130px" prop="buildDate">
          <el-date-picker
            v-model="Form.buildDate"
            type="date"
            placeholder="选择日期"
            size="small"
            style="width:200px;"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="施工单位" label-width="130px" prop="builder">
          <el-input v-model.trim="Form.builder" size="small" placeholder="请输入施工单位"></el-input>
        </el-form-item>
        <el-form-item label="地上楼层" label-width="130px" prop="overgroundFloor">
          <el-input
            v-model.trim="Form.overgroundFloor"
            size="small"
            @keyup.native="limitInt('overgroundFloor')"
            @blur="limitInt('overgroundFloor')"
            placeholder="请输入地上楼层"
            :disabled="floorNotUpdate"
          ></el-input>
        </el-form-item>
        <el-form-item label="地下楼层" label-width="130px" prop="undergroundFloor">
          <el-input
            v-model.trim="Form.undergroundFloor"
            size="small"
            @keyup.native="limitInt('undergroundFloor')"
            @blur="limitInt('undergroundFloor')"
            placeholder="请输入地下楼层"
            :disabled="floorNotUpdate"
          ></el-input>
        </el-form-item>
        <el-form-item label="取得方式" label-width="130px">
          <el-select
            v-model="Form.obtainWay"
            placeholder="请选择取得方式"
            style="width:200px"
            size="small"
          >
            <el-option
              v-for="(item, index) in obtainWayOpts"
              :key="index"
              :label="item.name"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="房产证号" label-width="130px" prop="housePropertyNumber">
          <el-input v-model.trim="Form.housePropertyNumber" size="small" placeholder="请输入房产证号"></el-input>
        </el-form-item>
        <el-form-item label="土地证号" label-width="130px">
          <el-input v-model.trim="Form.buildCertificate" size="small" placeholder="请输入土地证号"></el-input>
        </el-form-item>
        <el-form-item label="占地面积(m²)" label-width="130px" prop="coveredArea">
          <el-input
            v-model.trim="Form.coveredArea"
            size="small"
            @keyup.native="limitFloat('coveredArea')"
            placeholder="请输入占地面积"
            @blur="limitFloat('coveredArea', true)"
          ></el-input>
        </el-form-item>
        <el-form-item label="完损等级" label-width="130px">
          <el-input v-model.trim="Form.lossGrade" size="small" placeholder="请输入完损等级"></el-input>
        </el-form-item>
        <el-form-item label="结构类型" label-width="130px" prop="structureType">
          <el-select
            filterable
            v-model="Form.structureType"
            placeholder="请选择结构类型"
            style="width:200px"
            size="small"
          >
            <el-option
              v-for="(item, index) in structureType"
              :key="index"
              :label="item"
              :value="item"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="权证编号" label-width="130px">
          <el-input v-model.trim="Form.warrantNum" size="small" placeholder="请输入权证编号"></el-input>
        </el-form-item>
        <el-form-item label="所有权人" label-width="130px">
          <el-input v-model.trim="Form.warrantPerson" size="small" placeholder="请输入所有权人"></el-input>
        </el-form-item>
        <el-form-item label="权属说明" label-width="130px">
          <el-select
            filterable
            v-model="Form.warrantRemark"
            placeholder="请选择权属说明"
            style="width:200px"
            size="small"
          >
            <el-option
              v-for="(item, index) in warrantRemarkOpt"
              :key="index"
              :label="item"
              :value="item"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资产原值(万)" label-width="130px">
          <el-input
            v-model.trim="Form.assetsValue"
            size="small"
            @keyup.native="limitFloat('assetsValue')"
            @blur="limitFloat('assetsValue', true)"
            placeholder="请输入资产原值(万)"
          ></el-input>
        </el-form-item>
        <el-form-item label="人防面积(m²)" label-width="130px">
          <el-input
            v-model.trim="Form.civilDefenceArea"
            size="small"
            @keyup.native="limitFloat('civilDefenceArea')"
            @blur="limitFloat('civilDefenceArea', true)"
            placeholder="请输入人防面积"
          ></el-input>
        </el-form-item>
        <el-form-item label="产权所属单位" label-width="130px" prop="propertyUnitId">
          <el-select
            filterable
            v-model="Form.propertyUnitId"
            placeholder="请选择"
            style="width:200px"
            size="small"
          >
            <el-option
              v-for="item in unitOptions"
              :key="item.unitId"
              :label="item.unitName"
              :value="item.unitId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="电梯数量" label-width="130px" prop="elevatorNum" class="form-item-radio">
          <el-input
            v-model.trim="Form.elevatorNum"
            size="small"
            @keyup.native="limitInt('elevatorNum')"
            placeholder="请输入电梯数量"
          ></el-input>
        </el-form-item>
        <el-form-item label="是否租赁" label-width="130px" prop="isRent" class="form-item-radio">
          <el-radio v-model="Form.outRent.isRent" :label="1" @change="rentStatusChange">租赁</el-radio>
          <el-radio v-model="Form.outRent.isRent" :label="0" @change="rentStatusChange">不租赁</el-radio>
        </el-form-item>
        <el-form-item
          label="租用单位"
          label-width="130px"
          prop="outRent.rentUnit"
          v-if="Form.outRent.isRent === 1"
        >
          <el-select
            v-model="Form.outRent.rentUnit"
            filterable
            placeholder="请选择租用单位"
            v-if="operateType !=='detail'"
            style="width:200px;"
            size="small"
          >
            <el-option
              v-for="item in unitAuthorityOptions"
              :key="item.unitId"
              :label="item.unitName"
              :value="item.unitId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="租赁开始时间"
          label-width="130px"
          prop="startTime"
          v-if="Form.outRent.isRent === 1"
        >
          <el-date-picker
            v-model="Form.outRent.startTime"
            type="date"
            placeholder="选择租赁开始时间"
            size="small"
            style="width:200px;"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item
          label="租赁截止时间"
          label-width="130px"
          prop="endTime"
          v-if="Form.outRent.isRent === 1"
        >
          <el-date-picker
            v-model="Form.outRent.endTime"
            type="date"
            placeholder="选择租赁截止时间"
            size="small"
            style="width:200px;"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item
          label="租金标准(元)"
          label-width="130px"
          prop="outRent.rentMoney"
          v-if="Form.outRent.isRent === 1"
        >
          <el-input
            v-model.trim="Form.outRent.rentMoney"
            size="small"
            @keyup.native="limitFloat('rentMoney', null, 'outRent')"
            placeholder="请输入租金标准"
            @blur="limitFloat('rentMoney', true, 'outRent')"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="财政备案"
          label-width="130px"
          prop="outRent.financeRemark"
          v-if="Form.outRent.isRent === 1"
        >
          <el-input v-model.trim="Form.outRent.financeRemark" size="small" placeholder="请输入财政备案"></el-input>
        </el-form-item>
        <el-form-item label="楼座备注" label-width="130px" style="display: block;" prop="buildRemark">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入楼座备注"
            v-model.trim="Form.buildRemark"
            style="width:888px;"
          ></el-input>
        </el-form-item>
        <el-form-item label="上传图片" label-width="130px" style="display: block;">
          <div class="upload_box_img">
            <el-upload
              ref="uploadImg"
              :headers="postHeaders"
              :action="uploadImgUrl"
              list-type="picture-card"
              :file-list="fileListImg"
              :on-remove="handleImgRemove"
              :auto-upload="false"
              :on-success="handleImgSuccess"
              :on-change="handleImgChange"
              :on-error="handleError"
              :accept="acceptImgType"
              :limit="limitImgNum"
              :on-exceed="handleImgExceed"
              :before-upload="beforeAvatarUpload"
              multiple
            >
              <i class="el-icon-plus"></i>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="上传附件" label-width="130px" style="display: block;">
          <div class="upload_box_file">
            <ul class="accessory-checkbox" v-if="operateType==='edit'">
              <li v-for="(item, index) in downloadList" :key="index">
                <el-checkbox @change="checkboxChange($event, item.url)"></el-checkbox>
              </li>
            </ul>
            <el-upload
              ref="upload"
              :headers="postHeaders"
              :action="uploadImgUrl"
              :file-list="fileList"
              :on-remove="handleRemove"
              :auto-upload="false"
              :on-success="handleSuccess"
              :on-change="handleChange"
              :on-error="handleError"
              :accept="acceptType"
              :limit="limitNum"
              :on-exceed="handleExceed"
              :before-upload="beforeAvatarUpload"
              multiple
            >
              <el-button size="small" type="primary" slot="trigger">点击上传</el-button>
              <el-button
                size="small"
                type="primary"
                @click="downloadCAD"
                style="margin-left:10px"
                slot="tip"
                v-if="downloadList.length > 0"
              >勾选下载</el-button>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="经度(°)" label-width="130px" prop="buildLongitude">
          <el-input
            v-model.trim="Form.buildLongitude"
            size="small"
            @blur="mapBindCoord('buildLongitude')"
            @keyup.native="limitFloat('buildLongitude')"
            placeholder="请输入经度"
          ></el-input>
        </el-form-item>
        <el-form-item label="纬度(°)" label-width="130px" prop="buildLatitude">
          <el-input
            v-model.trim="Form.buildLatitude"
            size="small"
            @blur="mapBindCoord('buildLatitude')"
            @keyup.native="limitFloat('buildLatitude')"
            placeholder="请输入纬度"
          ></el-input>
        </el-form-item>
      </el-form>
      <div class="my-map">
        <div class="map-tips">提示：可手动输入经纬度,也可以点击下方地图自动生成经纬度。</div>
        <my-map
          ref="map"
          @mapClick="setPostion"
          :labelContent="'所选位置'"
          :position="position"
          v-if="showMap"
        ></my-map>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitFormBefore" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { BASE_URL, url_name_1 } from "@/axios/global"
import {
  getBuildInfoList,
  getBuildDetailInfo,
  deleteBuild,
  addBuild,
  updateBuild,
  getYardCoordinate,
  deleteProfession,
  noHouseCharNull
} from "./api";
import { downloadCAD } from "@/pages/houseManagement/api";
import { uploadUrl, getAllYardList, getAllUnitList, getUserUnitList } from "@/axios/commonApi";
import global_ from "@/axios/global";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import myMap from "@/components/map/map_2";
import { debug, debuglog } from 'util';
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
    myMap
  },
  data() {
    let validcodeBegin = (rule, value, callback) => {
      const rentBegin = new Date(this.Form.outRent.startTime).getTime();
      const rentEnd = new Date(this.Form.outRent.endTime).getTime();
      if (!rentBegin) {
        callback(new Error('租赁开始时间不能为空'));
      }
      if (rentBegin && rentEnd && (rentBegin > rentEnd)) {
        callback(new Error('租赁开始时间不能大于租赁截止时间'));
      }
      callback()
    };
    let validcodeEnd = (rule, value, callback) => {
      const rentBegin = new Date(this.Form.outRent.startTime).getTime();
      const rentEnd = new Date(this.Form.outRent.endTime).getTime();
      if (!rentEnd) {
        callback(new Error('租赁截止时间不能为空'));
      }
      if (rentBegin && rentEnd && (rentBegin > rentEnd)) {
        callback(new Error('租赁截止时间不能小于租赁开始时间'));
      }
      callback()
    };
    return {
      build_name: "",
      yard_id: "",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      selectData: [],
      tableData: [],
      dialogTitle: "",
      operateType: "add",
      mapOperateType: "add",
      dialogVisible: false,
      obtainWayOpts: [
        {
          value: 1,
          name: '调剂'
        },
        {
          value: 2,
          name: '租用'
        },
        {
          value: 3,
          name: '置换'
        },
        {
          value: 4,
          name: '自建'
        },
        {
          value: 5,
          name: '购置'
        }
      ],
      Form: {
        yardId: "",
        buildId: "",
        buildName: "",
        buildArea: "",
        buildDate: "",
        builder: "",
        overgroundFloor: "",
        undergroundFloor: "",
        obtainWay: "",
        buildCertificate: "",
        coveredArea: "",
        lossGrade: "",
        structureType: "",
        warrantNum: "",
        warrantPerson: "",
        warrantRemark: "",
        assetsValue: "",
        civilDefenceArea: "",
        buildRemark: "",
        buildLongitude: '',
        buildLatitude: '',
        propertyUnitId: '',
        elevatorNum: '',
        housePropertyNumber: '',
        outRent: {
          outRentId: '',
          buildId: '',
          coveredArea: '',
          rentUnit: '',
          startTime: '',
          endTime: '',
          rentMoney: '',
          financeRemark: '',
          isRent: 0
        },
        newFloorImgUrlList: [], // 楼座新增照片url数组
        floorDrawingList: [], // 楼座新增附件
        oldFloorImgIdList: [], // 楼座删除照片id数组
        oldCadIdList: [], // 删除新增附件
        configureIds: [] // 关联配置的id数组
      },
      elevatorsOpts: [
        {
          elevatorId: 0,
          elevatorName: '0台'
        },
        {
          elevatorId: 1,
          elevatorName: '1台'
        },
        {
          elevatorId: 2,
          elevatorName: '2台'
        },
        {
          elevatorId: 3,
          elevatorName: '3台'
        },
        {
          elevatorId: 4,
          elevatorName: '4台'
        },
        {
          elevatorId: 5,
          elevatorName: '5台'
        },
        {
          elevatorId: 6,
          elevatorName: '6台'
        }
      ],
      structureType: [
        "砖木结构",
        "砖混结构",
        "钢筋混泥土结构",
        "钢结构",
        "型钢混泥土结构"
      ],
      warrantRemarkOpt: [
        '已统一登记到机关事务管理部门名下',
        '登记在各部门或其他单位名下、已移交权属资料',
        '登记在各部门或其他单位名下、未移交权属资料',
        '未登记、已移交权属资料',
        '未登记、未移交权属资料',
        '其他情况'
      ],
      yardOptions: [],
      yardOptionsModal: [],
      uploadImgUrl: `${uploadUrl}?uploadType=build`,
      acceptType: "image/jpeg,image/gif,image/png,image/bmp,.doc,.docx,.xlsx,.ppt,.pdf,.rar,.zip",
      acceptImgType: "image/jpeg,image/gif,image/png,image/bmp",
      limitImgNum: 1,
      limitNum: 5,
      fileList: [],
      fileListImg: [],
      uploadNum: 0,
      uploadImgNum: 0,
      uploadedNum: 0,
      uploadedImgNum: 0,
      uploadSuccessNum: 0,
      uploadSuccessImgNum: 0,
      submitFlag: false,
      submitImgFlag: false,
      uploadChangeFlag: false,
      uploadImgChangeFlag: false,
      token: '',
      postHeaders: {
        Authorization: ''
      },
      showMap: false,
      position: {
        lng: 0,
        lat: 0
      },
      yardPosition: {
        yardLatitude: '',
        yardLongitude: ''
      },
      buildPosition: [],
      unitOptions: [],
      unitAuthorityOptions: [],
      CADDownload: [],
      downloadList: [],
      oldUndergroundFloor: null,
      oldOvergroundFloor: null,
      deleteFloorName: [],
      activeBuildId: null,
      floorNotUpdate: false,
      rules: {
        yardId: [
          { required: true, message: "院落名称不能为空", trigger: "change" }
        ],
        buildName: [
          { required: true, message: "楼座名称不能为空", trigger: "blur" }
        ],
        buildArea: [
          { required: true, message: "建筑面积不能为空", trigger: "blur" }
        ],
        structureType: [
          { required: true, message: "结构类型不能为空", trigger: "change" }
        ],
        overgroundFloor: [
          { required: true, message: "地上楼层不能为空", trigger: "blur" }
        ],
        undergroundFloor: [
          { required: true, message: "地下楼层不能为空", trigger: "blur" }
        ],
        buildDate: [
          { required: true, message: "建成时间不能为空", trigger: "blur" }
        ],
        housePropertyNumber: [
          { required: true, message: "房产证号不能为空", trigger: "blur" }
        ],
        buildLongitude: [
          { required: true, message: "经度不能为空", trigger: "blur" }
        ],
        buildLatitude: [
          { required: true, message: "纬度不能为空", trigger: "blur" }
        ],
        propertyUnitId: [
          { required: true, message: "请选择产权所属单位", trigger: "change" }
        ],
        startTime: [
          { required: true, validator: validcodeBegin, trigger: "blur" }
        ],
        endTime: [
          { required: true, validator: validcodeEnd, trigger: "blur" },
        ],
        elevatorNum: [
          { required: true, message: "请选择电梯数量", trigger: "change" }
        ],
        'outRent.rentUnit': [
          { required: true, message: "租用单位不能为空", trigger: "change" }
        ],
        'outRent.rentMoney': [
          { required: true, message: "租金标准不能为空", trigger: "blur" }
        ],
        'outRent.financeRemark': [
          { required: true, message: "财政备案不能为空", trigger: "blur" }
        ]
      }
    };
  },
  watch: {
    fileListImg(val) {
      const uploadBtn = $('.upload_box_img').find('.el-upload');
      if (val.length === this.limitImgNum) {
        uploadBtn.css('display', 'none');
      } else {
        uploadBtn.css('display', 'inline-block');
      }
    }
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
    // 获取楼座信息列表
    getList() {
      this.loadingStart('.table-box');
      getBuildInfoList(
        this.currentPage,
        this.pageSize,
        this.yard_id,
        this.build_name
      )
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          // this.tableData.forEach((item, index) => {
          //   if (item.buildDate !== undefined) {
          //     item.buildDate = moment(item.buildDate).format('YYYY-MM-DD 00:00:00')
          //   }
          // });
          this.loadingEnd();
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '楼座信息列表获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取院落字典项
    getYardDic() {
      getAllYardList(1)
        .then(res => {
          this.yardOptionsModal = res;
          this.yardOptions = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : '院落名称下拉菜单获取失败';
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
          const msg = err.result ? err.result : '单位名称下拉菜单获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 用户权限下的单位字典项
    getUserUnitDic() {
      getUserUnitList().then(res => {
        this.unitAuthorityOptions = res;
      }).catch(err => {
        const msg = err.result ? err.result : '单位下拉选项获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查询楼座信息
    search() {
      this.currentPage = 1;
      this.getList();
    },
    // 重置查询
    reset() {
      this.build_name = "";
      this.yard_id = "";
      this.currentPage = 1;
      this.getList();
    },
    // 新增
    add() {
      this.dialogVisible = true;
      this.showMap = true;
      this.dialogTitle = "新增楼座信息";
      this.operateType = "add";
    },
    // 单个删除
    singleDetle(row) {
      this.$confirm(`此操作将永久删除楼座${row.buildName}, 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        // 判断是否关联配置信息
        this.operateType = "delete";
        this.activeBuildId = row.buildId;
        this.deleteFloorName = [];
        const undergroundFloor = row.undergroundFloor - row.undergroundFloor * 2;
        for (let i = undergroundFloor; i < 0; ++i) {
          this.deleteFloorName.push(i);
        }
        for (let i = 1; i <= row.overgroundFloor; i++) {
          this.deleteFloorName.push(i);
        }
        noHouseCharNull(row.buildId, this.deleteFloorName).then(res => {
          if (res) {
            this.deleteBuild();
          }
        }).catch(err => {
          const msg = '该楼座包含的楼层里含有房间信息，如要删除楼座，请先删除楼座楼层包含的房间信息！'
          this.$message({
            message: msg,
            type: "error"
          });
        })
      }).catch(() => {
        this.$message({
          type: "info",
          message: "已取消删除"
        });
      });
    },
    // 删除楼座
    deleteBuild() {
      deleteBuild(this.activeBuildId).then(res => {
        this.$message({
          message: "删除楼座信息成功!",
          type: "success"
        });
        this.currentPage = 1;
        this.getList();
      }).catch(err => {
        const msg = err.result ? err.result : '删除楼座信息失败';
        this.$message({
          message: msg,
          type: "error"
        });
      });
    },
    // 单个修改
    singleUpdate(row) {
      this.dialogVisible = true;
      this.dialogTitle = "修改楼座信息";
      this.operateType = "edit";
      this.showMap = true;
      this.activeBuildId = row.buildId;
      getBuildDetailInfo(row.buildId).then(res => {
        for ([key, value] of Object.entries(res)) {
          if (this.Form[key] !== undefined) this.Form[key] = value;
        }
        for ([key, value] of Object.entries(res)) {
          if (this.Form.outRent[key] !== undefined) this.Form.outRent[key] = value;
        }
        if (res.floorImg.length > 0) {
          this.uploadedImgNum = res.floorImg.length;
          res.floorImg.forEach((item, index) => {
            const obj = {
              name: item.floorimgId,
              url: `${BASE_URL}${url_name_1}${item.floorVirtualUrl}?access_token=${this.token}`
            };
            this.fileListImg.push(obj);
          });
        }
        if (res.floorDrawingDtoList.length > 0) {
          this.uploadedNum = res.floorDrawingDtoList.length;
          res.floorDrawingDtoList.forEach((item, index) => {
            const obj = {
              name: item.floordrawingFileName,
              url: item.floordrawingId
            };
            this.fileList.push(obj);
            this.downloadList.push(obj);
          });
        }
        this.selectYardHandle(res.yardId);
        this.$refs.map.setBuildPosition(res.buildLongitude, res.buildLatitude, res.buildName);
        this.oldUndergroundFloor = this.Form.undergroundFloor - this.Form.undergroundFloor * 2;
        this.oldOvergroundFloor = this.Form.overgroundFloor;
      });
    },
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
    },
    // 是否租赁
    rentStatusChange(val) {
      if (val === 0) {
        this.Form.outRent.isRent = 0;
      }
    },
    // 提交表单之前
    submitFormBefore() {
      const newUndergroundFloor = this.Form.undergroundFloor - 2 * this.Form.undergroundFloor;
      const newOvergroundFloor = this.Form.overgroundFloor - 0;
      this.deleteFloorName = [];
      if (newUndergroundFloor > this.oldUndergroundFloor) {
        for (let i = this.oldUndergroundFloor; i < newUndergroundFloor; i++) {
          this.deleteFloorName.push(i)
        }
      };
      if (newOvergroundFloor < this.oldOvergroundFloor) {
        for (let i = newOvergroundFloor + 1; i <= this.oldOvergroundFloor; i++) {
          this.deleteFloorName.push(i)
        }
      };
      if (this.deleteFloorName.length > 0) {
        noHouseCharNull(this.activeBuildId, this.deleteFloorName).then(res => {
          if (res) {
            this.submitForm();
          }
        }).catch(err => {
          const msg = '删除的楼层里有房间，如要删除楼层，请先删除楼层里的房间！'
          this.$message({
            message: msg,
            type: "error"
          });
        })
      } else {
        this.submitForm();
      }
    },
    // 提交表单
    submitForm() {
      this.$refs.Form.validate(valid => {
        if (valid) {
          (this.uploadNum === 0 && this.uploadImgNum === 0) ? this.saveForm() : this.submitUpload();
        } else {
          return false;
        }
      });
    },
    // 保存新增楼座信息
    saveForm() {
      if (this.operateType === "add") {
        this.Form.outRent.coveredArea = this.Form.buildArea;
        if (this.Form.outRent.endTime) this.Form.outRent.endTime = moment(this.Form.outRent.endTime).format('YYYY-MM-DD 23:59:59')
        addBuild(this.Form)
          .then(res => {
            this.$message({
              message: "新增楼座信息成功",
              type: "success"
            });
            this.dialogVisible = false;
            this.getList();
          })
          .catch(err => {
            const msg = err.result ? err.result : '新增楼座信息失败';
            this.$message({
              message: msg,
              type: "error"
            });
          });
      } else if (this.operateType === "edit") {
        this.Form.outRent.coveredArea = this.Form.buildArea;
        if (this.Form.outRent.endTime) this.Form.outRent.endTime = moment(this.Form.outRent.endTime).format('YYYY-MM-DD 23:59:59')
        updateBuild(this.Form)
          .then(res => {
            this.$message({
              message: "修改楼座信息成功",
              type: "success"
            });
            this.dialogVisible = false;
            this.getList();
          })
          .catch(err => {
            const msg = err.result ? err.result : '修改楼座信息失败';
            this.$message({
              message: msg,
              type: "error"
            });
          });
      }
    },
    // 开始上传文件
    submitUpload() {
      this.$refs.upload.submit();
      this.$refs.uploadImg.submit();
    },
    // 文件上传之前
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 200;
      if (!isLt2M) {
        this.$message.error('上传文件大小不能超过200MB!');
      }
      return isLt2M;
    },
    // 删除上传文件时钩子
    handleRemove(file, fileList) {
      if (file.status === 'success') {
        this.uploadedNum--;
        this.Form.oldCadIdList.push(file.url);
        this.downloadList = [];
        fileList.forEach(item => {
          if (item.status === 'success') {
            this.downloadList.push(item);
          }
        })
      }
      this.uploadNum = fileList.length - this.uploadedNum;
    },
    handleImgRemove(file, fileList) {
      if (file.status === 'success') {
        this.uploadedImgNum--;
        this.Form.oldFloorImgIdList.push(file.name);
      }
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
      this.fileListImg = fileList;
    },
    // 上传文件成功
    handleSuccess(response, file, fileList) {
      this.uploadSuccessNum++;
      this.Form.floorDrawingList.push({
        floordrawingUrl: response.imgList[0].imgPath,
        floordrawingVirtualUrl: response.imgList[0].imgUrl,
        floordrawingFileName: file.name,
        buildId: this.Form.buildId
      });
      if (this.uploadSuccessNum === this.uploadNum) {
        this.submitFlag = true;
      }
      if (((this.submitFlag && this.uploadChangeFlag) && (this.submitImgFlag && this.uploadImgChangeFlag)) ||
        ((this.submitFlag && this.uploadChangeFlag) && !this.uploadImgChangeFlag)) {
        this.saveForm();
      }
    },
    handleImgSuccess(response, file, fileList) {
      this.uploadSuccessImgNum++;
      this.Form.newFloorImgUrlList.push({
        floorimgUrl: response.imgList[0].imgPath,
        floorVirtualUrl: response.imgList[0].imgUrl
      });
      if (this.uploadSuccessImgNum === this.uploadImgNum) {
        this.submitImgFlag = true;
      }
      if (((this.submitFlag && this.uploadChangeFlag) && (this.submitImgFlag && this.uploadImgChangeFlag)) ||
        (!this.uploadChangeFlag && (this.submitImgFlag && this.uploadImgChangeFlag))) {
        this.saveForm();
      }
    },
    // 上传文件改变时钩子
    handleChange(file, fileList) {
      this.uploadChangeFlag = true;
      this.uploadNum = fileList.length - this.uploadedNum;
    },
    handleImgChange(file, fileList) {
      this.uploadImgChangeFlag = true;
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
      this.fileListImg = fileList;
    },
    // 上传文件超出限制个数
    handleExceed(file, fileList) {
      this.$message.warning(`最多只能上传五个文件！`);
    },
    handleImgExceed(file, fileList) {
      this.$message.warning(`最多只能上传一张图片！`);
    },
    // 上传文件失败
    handleError(err, file, fileList) {
      if (err.status === 401) {
        this.$store.dispatch('Logout', this.token);
        this.$router.push({ path: '/login' });
      }
      const msg = err.result ? err.result : '上传失败';
      this.$message({
        message: msg,
        type: "error"
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
      tempObj.buildId = this.Form.buildId;
      tempObj.floorDrawingIdList = this.CADDownload;
      if (this.CADDownload.length > 0) {
        downloadCAD(tempObj).then(res => {
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
      } else {
        this.$message({
          message: "请先选中一个CAD文件",
          type: "warning"
        });
      }
    },
    // 选择年份
    selectYear(name, val) {
      this.Form[name] = val;
    },
    // 设置经纬度
    setPostion(lng, lat) {
      this.Form.buildLongitude = lng;
      this.Form.buildLatitude = lat;
    },
    // 地图绑定坐标位置
    mapBindCoord(key) {
      this.limitFloat(key);
      if (this.Form.buildLongitude !== null && this.Form.buildLatitude !== null && this.Form.buildLongitude !== '' && this.Form.buildLatitude !== '') {
        eventBus.$emit("mapBindCoord", this.Form.buildLongitude, this.Form.buildLatitude);
      }
    },
    // 查询院落及其楼座的坐标
    selectYardHandle(yardId) {
      getYardCoordinate(yardId).then(res => {
        this.$refs.map.setYardPosition(res.yardLongitude, res.yardLatitude, res.yardName);
      }).catch(err => {
        const msg = err.result ? err.result : '坐标信息获取失败';
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
    // dialog打开
    dialogOpen() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
      })
    },
    // dialog关闭的回调
    dialogClosed() {
      this.fileList = [];
      this.fileListImg = [];
      this.uploadNum = 0;
      this.uploadImgNum = 0;
      this.uploadedNum = 0;
      this.uploadedImgNum = 0;
      this.uploadSuccessNum = 0;
      this.uploadSuccessImgNum = 0;
      this.submitFlag = false;
      this.submitImgFlag = false;
      this.uploadChangeFlag = false;
      this.uploadImgChangeFlag = false;
      this.showMap = false;
      this.clearForm(this.Form);
      this.Form.outRent.isRent = 0;
      this.Form.elevatorNum = '';
      this.CADDownload = [];
      this.downloadList = [];
      this.oldUndergroundFloor = null;
      this.oldOvergroundFloor = null;
      this.activeBuildId = null;
      this.floorNotUpdate = false;
    },
    // 只能输入正浮点数
    limitFloat(key, flag, type) {
      if (type === undefined) {
        this.Form[key] = this.Form[key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '')
      } else {
        this.Form[type][key] = this.Form[type][key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '')
      }
      if (flag && type === undefined && this.Form[key] !== '') {
        this.Form[key] = (this.Form[key] - 0).toFixed(2);
      }
      if (flag && type && this.Form[type][key] !== '') {
        this.Form[type][key] = (this.Form[type][key] - 0).toFixed(2);
      }
    },
    // 只能输入正整数
    limitInt(key) {
      this.Form[key] = this.Form[key].replace(/[^0-9]/g, "");
    },
    // 获取数据类型
    getDataType(obj) {
      var typeArr = ["String", "Object", "Number", "Array", "Date"];
      for (let name of typeArr) {
        if (Object.prototype.toString.call(obj) === `[object ${name}]`) {
          return name;
        }
      }
    }
  },
  mounted() {
    this.getList();
    this.getYardDic();
    this.getUnitDic();
    this.getUserUnitDic();
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
#baseInfoBuild .query-box {
  margin-bottom: 20px;
}
#baseInfoBuild .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#baseInfoBuild .query-item > div {
  float: left;
}
#baseInfoBuild .query-item > .query-input {
  width: calc(100% - 80px);
}
#baseInfoBuild .query-item .el-date-editor.el-input,
#baseInfoBuild .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#baseInfoBuild .query-btn {
  float: left;
  margin-left: 50px;
}
#baseInfoBuild .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#baseInfoBuild .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#baseInfoBuild .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#baseInfoBuild .upload_box_img .el-upload--picture-card {
  width: 100px;
  height: 100px;
  line-height: 100px;
}
#baseInfoBuild
  .upload_box_img
  .el-upload-list--picture-card
  .el-upload-list__item {
  width: 100px;
  height: 100px;
}
#baseInfoBuild .el-upload--picture-card {
  margin-bottom: 8px;
}
#baseInfoBuild .el-upload-list > li {
  margin-bottom: 0;
}
#baseInfoBuild .el-dialog__body {
  height: 500px;
  overflow-y: auto;
}

#baseInfoBuild .config-info-dialog .el-dialog__body {
  height: auto;
}

#baseInfoBuild .my-map {
  width: 100%;
  height: 300px;
  position: relative;
  margin-top: 20px;
}
#baseInfoBuild .build-info-Form {
  position: relative;
}
#baseInfoBuild .map-tips {
  color: #ff0000;
  position: absolute;
  left: 0px;
  width: 100%;
  top: -22px;
}
#baseInfoBuild .rent-info-Form {
  margin-top: 15px;
  margin-bottom: 15px;
}
#baseInfoBuild .form-item-radio .el-form-item__content {
  width: 200px;
}
#baseInfoBuild .upload_box_file {
  position: relative;
}
#baseInfoBuild .upload_box_file .accessory-checkbox {
  position: absolute;
  left: 0px;
  top: 49px;
  line-height: 25.2px;
}
#baseInfoBuild .upload_box_file .accessory-checkbox li + li {
  margin-top: 5px;
}
#baseInfoBuild .upload_box_file .el-upload-list {
  margin-left: 18px;
}
#baseInfoBuild .config-table-pagination-box {
  position: relative;
  height: 30px;
  border-left: 1px solid #c8c8c8;
  border-right: 1px solid #c8c8c8;
  border-bottom: 1px solid #c8c8c8;
}
#baseInfoBuild .config-table-pagination-box > .config-table-pagination {
  position: absolute;
  right: 10px;
  top: 3px;
}
#baseInfoBuild .config-info-dialog .dialog-content {
  padding: 20px;
}
#baseInfoBuild .save-before-room-tips {
  margin-bottom: 5px;
}
</style>


