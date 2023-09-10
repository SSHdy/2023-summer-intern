<template>
  <div id="baseInfoUnit">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">单位名称：</div>
          <div class="query-input">
            <el-input v-model.trim="name" placeholder="请输入单位名称"></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">单位类型：</div>
          <div class="query-input">
            <el-select v-model="type" filterable placeholder="请选择">
              <el-option
                v-for="item in unitTypeOptions"
                :key="item.unittypeId"
                :label="item.unittypeName"
                :value="item.unittypeId"
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
      <table-component :tableData="tableData" :tableHeight="tableHeight" rowKey="unitId">
        <el-table-column :show-overflow-tooltip="true" prop="unitName" label="单位名称" align="left"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="unittypeName"
          label="单位类型"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="unitSocialCode"
          label="统一社会信用代码"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="unitComplement"
          label="总编制人数"
          align="center"
        ></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="unitLevel" label="单位分类" align="center"></el-table-column>
        <el-table-column label="操作" width="190" align="center">
          <template slot-scope="scope">
            <single-btn-group
              type="unit"
              :isShowDetail="false"
              :unitClassify="scope.row.unitClassify"
              @addClick="tableAdd(scope.row)"
              @updateClick="tableEdit(scope.row)"
              @deleteClick="tableDelete(scope.row.unitId, scope.row.unitName)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group :batchDelBtnShow="false" @addClick="addUnit" :addText="'添加'"></table-btn-group>
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
        <el-form-item label="是否为单位" label-width="134px" class="radio-box">
          <el-radio-group v-model="unitClassify" @change="unitClassifyChange">
            <el-radio label="yes" name="unitClassify">是</el-radio>
            <el-radio :label="0" name="unitClassify">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="类别名称"
          label-width="134px"
          prop="unit.unitName"
          v-if="unitClassify === 0"
        >
          <el-input v-model.trim="Form.unit.unitName" size="small" placeholder="请输入类别名称"></el-input>
        </el-form-item>
        <div class="line" v-if="unitClassify === 'yes'"></div>
        <el-form-item
          label="是否临时单位"
          label-width="134px"
          class="radio-box"
          v-if="unitClassify === 'yes'"
        >
          <el-radio-group v-model="Form.unit.isTempUnit">
            <el-radio :label="1" name="isTempUnit">是</el-radio>
            <el-radio :label="0" name="isTempUnit">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="单位名称"
          label-width="134px"
          prop="unit.unitName"
          v-if="unitClassify === 'yes'"
        >
          <el-input v-model.trim="Form.unit.unitName" size="small" placeholder="请输入单位名称"></el-input>
        </el-form-item>
        <el-form-item
          label="办公人数"
          label-width="134px"
          prop="unit.workerNumber"
          v-if="unitClassify === 'yes' && Form.unit.isTempUnit === 1"
        >
          <el-input
            v-model.trim="Form.unit.workerNumber"
            size="small"
            placeholder="请输入办公人数"
            @keyup.native="limitInt('workerNumber')"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="备注"
          label-width="134px"
          prop="unit.remark"
          v-if="unitClassify === 'yes' && Form.unit.isTempUnit === 1"
        >
          <el-input
            v-model.trim="Form.unit.remark"
            size="small"
            type="textarea"
            style="width:878px"
            placeholder="请输入备注"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="单位类型"
          label-width="134px"
          prop="unit.unitType"
          v-if="unitClassify === 'yes' && Form.unit.isTempUnit === 0"
        >
          <el-select
            v-model="Form.unit.unitType"
            filterable
            placeholder="请选择单位类型"
            size="small"
            style="width:200px;"
          >
            <el-option
              v-for="item in unitTypeOptions"
              :key="item.unittypeId"
              :label="item.unittypeName"
              :value="item.unittypeId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="单位类别"
          label-width="134px"
          prop="unit.unitCategory"
          v-if="unitClassify === 'yes' && Form.unit.isTempUnit === 0"
        >
          <el-select
            v-model="Form.unit.unitCategory "
            filterable
            placeholder="请选择单位类别"
            size="small"
            style="width:200px;"
          >
            <el-option
              v-for="(item, index) in unitCategoryOpts"
              :key="index"
              :label="item"
              :value="item"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="单位级别"
          label-width="134px"
          prop="unit.unitGrade"
          v-if="unitClassify === 'yes' && Form.unit.isTempUnit === 0"
        >
          <el-select
            placeholder="请选择单位级别"
            size="small"
            style="width:200px;"
            v-model="Form.unit.unitGrade"
            @change="unitGradeChange"
          >
            <el-option
              v-for="(item, index) in unitGradeOpts"
              :key="index"
              :label="item"
              :value="item"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="机关级别"
          label-width="134px"
          prop="unit.officeLevel"
          v-if="unitClassify === 'yes' && Form.unit.isTempUnit === 0"
        >
          <el-select
            v-model="Form.unit.officeLevel"
            filterable
            placeholder="请选择机关级别"
            size="small"
            style="width:200px;"
          >
            <el-option
              v-for="(item, index) in officeLevelOpts"
              :key="index"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item
          label="组织机构代码"
          label-width="134px"
          prop="unit.unitOrganizationCode"
          v-if="unitClassify === 'yes' && Form.unit.isTempUnit === 0"
        >
          <el-input v-model.trim="Form.unit.unitOrganizationCode" size="small" placeholder="请输入组织机构代码"></el-input>
        </el-form-item>-->
        <el-form-item
          label="统一社会信用代码"
          label-width="134px"
          prop="unit.unitSocialCode"
          v-if="unitClassify === 'yes' && Form.unit.isTempUnit === 0"
        >
          <el-input v-model.trim="Form.unit.unitSocialCode" size="small" placeholder="请输入统一社会信用代码"></el-input>
        </el-form-item>
        <!-- <el-form-item
          label="单位负责人"
          label-width="134px"
          prop="unit.unitCharger"
          v-if="unitClassify === 'yes' && Form.unit.isTempUnit === 0"
        >
          <el-input.trim v-model="Form.unit.unitCharger" size="small" placeholder="请输入单位负责人"></el-input>
        </el-form-item>-->
        <el-form-item
          label="工勤编制数"
          label-width="134px"
          prop="unit.unitWork"
          v-if="unitClassify === 'yes' && Form.unit.isTempUnit === 0"
        >
          <el-input
            v-model.trim="Form.unit.unitWork"
            size="small"
            @keyup.native="limitInt('unitWork')"
            placeholder="请输入工勤编制数"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="单位副职数"
          label-width="134px"
          prop="unit.unitDeputy"
          v-if="unitClassify === 'yes' && Form.unit.unitGrade === '副县'"
        >
          <el-input
            v-model.trim="Form.unit.unitDeputy"
            size="small"
            @keyup.native="limitInt('unitDeputy')"
            placeholder="请输入单位副职数"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="单位编制信息"
          label-width="134px"
          prop="unitProfessionalList"
          v-if="unitClassify === 'yes' && Form.unit.isTempUnit === 0"
          style="display: block;"
          class="unit-professional-list"
        >
          <div
            class="unit-level-box"
            v-for="(item, index) in Form.unitProfessionalList"
            :key="index"
          >
            <div class="unit-level-item">
              <el-select
                v-model="item.professionalLevel"
                filterable
                no-data-text="请先选择单位级别"
                placeholder="请选择等级"
                size="small"
                style="width:200px;"
                @change="professionalLevelChange($event, index)"
              >
                <el-option
                  v-for="item in professionalLevelOpts"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
              <span>-</span>
              <el-select
                v-model="item.professionalId"
                no-data-text="请先选择等级"
                filterable
                placeholder="请选择职级"
                size="small"
                style="width:200px;"
              >
                <el-option
                  v-for="item in item.professionOpts"
                  :key="item.professionalId"
                  :label="item.professionalName"
                  :value="item.professionalId"
                ></el-option>
              </el-select>
              <span>-</span>
              <el-input
                v-model.trim="item.professionalNum"
                size="small"
                style="width:200px;"
                placeholder="请输入人数"
                @keyup.native="unitProfessionalLimitInt('professionalNum',index)"
              ></el-input>
              <span
                class="delete-item-btn"
                @click="deleteUnitProfessional(index)"
                v-if="index !== 0"
              >
                <i class="el-icon-close"></i> 删除
              </span>
              <span class="add-item-btn" @click="addUnitProfessional" v-if="index === 0">
                <i class="el-icon-plus"></i> 添加
              </span>
            </div>
          </div>
        </el-form-item>
        <el-form-item
          label="当前总编制人数"
          label-width="134px"
          v-if="unitClassify === 'yes' && Form.unit.unitComplement > 0 && Form.unit.isTempUnit === 0"
        >
          <span>{{Form.unit.unitComplement}}</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getUnitInfoList,
  deleteUnit,
  saveOrUpdateUnit,
  findOneUnitInfo,
  getUnitInfoList2
} from "./api";
import { getUnitTypeList, getAllProfessionList } from "@/axios/commonApi";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup_2";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import { defaultCoreCipherList, defaultCipherList } from "constants";
export default {
  components: {
    singleBtnGroup,
    tableBtnGroup,
    tableComponent,
    tablePagination,
    cancelBtn,
    confirmBtn,
    searchBtn,
    resetBtn
  },
  data() {
    let validUnitProfessional = (rule, value, callback) => {
      if (this.Form.unitProfessionalList.length === 0) {
        callback(new Error("请至少添加一条单位编制信息"));
      } else {
        this.Form.unitProfessionalList.forEach(item => {
          if (item.professionalId === '' || item.professionalNum === '') {
            callback(new Error("请将单位编制信息填写完整"));
          }
        })
        callback();
      }
    }
    return {
      name: "",
      type: "",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      tableHeight: 0,
      tableData: [],
      loading: null,
      unitTypeOptions: [],
      dialogTitle: "",
      operateType: "add",
      operateLevel: 0,
      dialogVisible: false,
      unitClassify: "yes",
      oldUnitClassify: null,
      Form: {
        unit: {
          // unitCharger: "",
          unitComplement: 0,
          unitId: null,
          unitName: "",
          // unitOrganizationCode: "",
          unitPid: 0,
          unitSocialCode: "",
          unitGrade: "",
          unitType: null,
          unitClassify: null,
          unitCategory: null,
          officeLevel: null,
          unitWork: null,
          unitDeputy: null,
          isTempUnit: 0,
          workerNumber: null,
          remark: ''
        },
        unitProfessionalList: []
      },
      unitCategoryOpts: ['机关本级', '垂直管理机构', '派出机构', '参照公务员法管理的事业单位', '其他'],
      unitGradeOpts: ['正县', '副县', '正科', '副科'],
      officeLevelOpts: [
        {
          name: '市级机关',
          id: 1
        },
        {
          name: '县级机关',
          id: 2
        },
        {
          name: '乡级机关',
          id: 3
        }
      ],
      msg: '',
      unitGradeOriginalVal: '',
      rules: {
        "unit.unitName": [
          { required: true, message: "单位名称不能为空", trigger: "blur" }
        ],
        "unit.unitType": [
          { required: true, message: "请选择单位类型", trigger: "change" }
        ],
        // "unit.unitOrganizationCode": [
        //   { required: true, message: "组织机构代码不能为空", trigger: "blur" }
        // ],
        "unit.unitSocialCode": [
          { required: true, message: "统一社会信用代码不能为空", trigger: "blur" }
        ],
        // "unit.unitCharger": [
        //   { required: true, message: "单位负责人不能为空", trigger: "blur" }
        // ],
        "unit.unitCategory": [
          { required: true, message: "请选择单位类别", trigger: "change" }
        ],
        "unit.unitGrade": [
          { required: true, message: "请选择单位级别", trigger: "change" }
        ],
        "unit.officeLevel": [
          { required: true, message: "请选择机关级别", trigger: "change" }
        ],
        "unit.unitWork": [
          { required: true, message: "工勤编制数不能为空", trigger: "blur" }
        ],
        "unit.unitDeputy": [
          { required: true, message: "单位副职数不能为空", trigger: "blur" }
        ],
        unitProfessionalList: [
          { required: true, validator: validUnitProfessional, trigger: "blur" }
        ]
      }
    };
  },
  watch: {
    "Form.unit.unitWork"(newValue, oldValue) {
      const unitWorkNum = (this.Form.unit.unitWork !== '' && this.Form.unit.unitWork !== null) ? parseInt(this.Form.unit.unitWork) : 0;
      const unitDeputyNum = (this.Form.unit.unitDeputy !== '' && this.Form.unit.unitDeputy !== null) ? parseInt(this.Form.unit.unitDeputy) : 0;
      let unitProfessionalListNum = 0;
      if (this.Form.unitProfessionalList.length > 0) {
        this.Form.unitProfessionalList.forEach(item => {
          const professionalNum = (item.professionalNum !== '' && item.professionalNum !== null) ? parseInt(item.professionalNum) : 0;
          unitProfessionalListNum += professionalNum;
        })
      }
      this.Form.unit.unitComplement = unitWorkNum + unitDeputyNum + unitProfessionalListNum;
    },
    "Form.unit.unitDeputy"(newValue, oldValue) {
      const unitWorkNum = (this.Form.unit.unitWork !== '' && this.Form.unit.unitWork !== null) ? parseInt(this.Form.unit.unitWork) : 0;
      const unitDeputyNum = (this.Form.unit.unitDeputy !== '' && this.Form.unit.unitDeputy !== null) ? parseInt(this.Form.unit.unitDeputy) : 0;
      let unitProfessionalListNum = 0;
      if (this.Form.unitProfessionalList.length > 0) {
        this.Form.unitProfessionalList.forEach(item => {
          const professionalNum = (item.professionalNum !== '' && item.professionalNum !== null) ? parseInt(item.professionalNum) : 0;
          unitProfessionalListNum += professionalNum;
        })
      }
      this.Form.unit.unitComplement = unitWorkNum + unitDeputyNum + unitProfessionalListNum;
    },
    "Form.unitProfessionalList": {
      handler(newValue, oldValue) {
        const unitWorkNum = (this.Form.unit.unitWork !== '' && this.Form.unit.unitWork !== null) ? parseInt(this.Form.unit.unitWork) : 0;
        const unitDeputyNum = (this.Form.unit.unitDeputy !== '' && this.Form.unit.unitDeputy !== null) ? parseInt(this.Form.unit.unitDeputy) : 0;
        let unitProfessionalListNum = 0;
        if (this.Form.unitProfessionalList.length > 0) {
          this.Form.unitProfessionalList.forEach(item => {
            const professionalNum = (item.professionalNum !== '' && item.professionalNum !== null) ? parseInt(item.professionalNum) : 0;
            unitProfessionalListNum += professionalNum;
          })
        }
        this.Form.unit.unitComplement = unitWorkNum + unitDeputyNum + unitProfessionalListNum;
      },
      deep: true
    }
  },
  methods: {
    unitGradeChange(val) {
      if (this.unitGradeOriginalVal === '' || this.unitGradeOriginalVal === null) {
        this.professionalLevelOpts = [
          {
            id: 3,
            name: '市级机关'
          },
          {
            id: 4,
            name: '县级机关'
          },
          {
            id: 5,
            name: '乡级机关'
          }
        ]
        this.unitGradeOriginalVal = val;
      } else {
        this.$confirm('单位级别改变会清空单位编制信息内容, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.Form.unitProfessionalList = [];
          const obj = {
            professionalId: '',
            professionalNum: '',
            professionalLevel: '',
            professionOpts: []
          }
          this.Form.unitProfessionalList.push(obj);
          if (val !== '副县') {
            this.Form.unit.unitDeputy = null;
          }
          this.unitGradeOriginalVal = val;
        }).catch(() => {
          this.Form.unit.unitGrade = this.unitGradeOriginalVal;
        })
      }
    },
    unitClassifyChange(val) {
      this.$refs["Form"].clearValidate();
      this.rules["unit.unitName"] = [];
      this.Form.unit.unitName = '';
      if (val === 0) {
        this.rules["unit.unitName"].push({ required: true, message: "类别名称不能为空", trigger: "blur" });
      } else {
        this.rules["unit.unitName"].push({ required: true, message: "单位名称不能为空", trigger: "blur" });
        if (this.oldUnitClassify === 0) {
          this.Form.unitProfessionalList = [];
          const obj = {
            professionalId: '',
            professionalNum: '',
            professionalLevel: '',
            professionOpts: []
          }
          this.Form.unitProfessionalList.push(obj);
        }
      }
    },
    addUnitProfessional() {
      if (this.operateType === 'add_level1' || this.operateType === 'add_level2') {
        const obj = {
          professionalId: '',
          professionalNum: '',
          professionalLevel: '',
          professionOpts: []
        }
        this.Form.unitProfessionalList.push(obj);
      } else {
        const obj = {
          professionalId: '',
          professionalNum: '',
          professionalLevel: '',
          unitId: this.Form.unit.unitId,
          professionOpts: []
        }
        this.Form.unitProfessionalList.push(obj);
      }
    },
    deleteUnitProfessional(index) {
      this.Form.unitProfessionalList.splice(index, 1);
    },
    professionalLevelChange($event, index) {
      this.Form.unitProfessionalList[index].professionalId = '';
      this.Form.unitProfessionalList[index].professionalNum = '';
      this.getAllProfessionList($event, index)
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
    // 获取单位信息列表
    getList() {
      this.loadingStart('.table-box');
      if (this.name || this.type) {
        getUnitInfoList2(this.currentPage, this.pageSize, this.name, this.type)
          .then(res => {
            this.tableData = res.records;
            this.total = res.total;
            this.recursionList(this.tableData);
            this.loadingEnd();
          })
          .catch(err => {
            this.loadingEnd();
            const msg = err.result ? err.result : '获取单位信息失败';
            this.$message({
              message: msg,
              type: "error"
            });
          });
      } else if (this.name === "" && this.type === "") {
        getUnitInfoList(this.currentPage, this.pageSize)
          .then(res => {
            this.total = res.total;
            this.tableData = res.records;
            this.recursionList(this.tableData);
            this.loadingEnd();
          })
          .catch(err => {
            const msg = err.result ? err.result : '获取单位信息失败';
            this.$message({
              message: msg,
              type: "error"
            });
            this.loadingEnd();
          });
      }
    },
    // 递归遍历list
    recursionList(list) {
      list.forEach(item => {
        if (item.unitClassify !== undefined) {
          switch (item.unitClassify) {
            case 0:
              item.unitLevel = "-";
              break;
            case 1:
              item.unitLevel = "一级单位";
              break;
            case 2:
              item.unitLevel = "二级单位";
              break;
          }
        }
        if (item.unitComplement !== undefined && item.unitComplement === 0) {
          item.unitComplement = "-";
        }
        // if (!item.unitOrganizationCode && item.unitOrganizationCode !== 0) {
        //   item.unitOrganizationCode = "-";
        // }
        if (!item.unittypeName) {
          item.unittypeName = "-";
        }
        if (item.children && item.children.length > 0) {
          this.recursionList(item.children);
        }
      });
    },
    // 获取单位类型列表
    getUnitTypeList() {
      getUnitTypeList()
        .then(res => {
          this.unitTypeOptions = res;
          this.getList();
        })
        .catch(err => {
          const msg = err.result ? err.result : '单位类型获取失败';
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
    // 重置
    reset() {
      this.currentPage = 1;
      this.name = "";
      this.type = "";
      this.search();
    },
    // 新增一级单位
    addUnit() {
      this.dialogVisible = true;
      this.dialogTitle = "新增单位";
      this.operateType = "add_level1";
      this.Form.unitProfessionalList = [];
      this.professionalLevelOpts = [];
      const obj = {
        professionalId: '',
        professionalNum: '',
        professionalLevel: '',
        professionOpts: []
      }
      this.Form.unitProfessionalList.push(obj);
    },
    // 新增二级单位
    tableAdd(row) {
      this.operateLevel = row.unitClassify;
      this.dialogVisible = true;
      this.dialogTitle = "新增单位";
      this.operateType = "add_level2";
      this.Form.unit.unitPid = parseInt(row.unitId);
      this.Form.unitProfessionalList = [];
      this.professionalLevelOpts = [];
      const obj = {
        professionalId: '',
        professionalNum: '',
        professionalLevel: '',
        professionOpts: []
      }
      this.Form.unitProfessionalList.push(obj);
    },
    // 单个删除
    tableDelete(id, name) {
      this.$confirm(`此操作将永久删除单位${name}, 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteUnit(parseInt(id)).then(res => {
          this.$message({
            message: "删除单位信息成功!",
            type: "success"
          })
          this.currentPage = 1
          this.getList()
        }).catch(err => {
          const msg = err.result ? err.result : '删除单位信息失败!';
          this.$message({
            message: msg,
            type: "error"
          })
        })
      }).catch(() => {
        this.$message({
          type: "info",
          message: "已取消删除"
        })
      })
    },
    // 单个修改
    tableEdit(row) {
      this.dialogVisible = true;
      this.dialogTitle = "修改单位";
      this.operateType = "edit";
      this.operateLevel = row.unitClassify;
      this.professionalLevelOpts = [
        {
          id: 3,
          name: '市级机关'
        },
        {
          id: 4,
          name: '县级机关'
        },
        {
          id: 5,
          name: '乡级机关'
        }
      ];
      findOneUnitInfo(row.unitId).then(res => {
        Object.keys(res).map(key => {
          if (this.Form.unit[key] !== undefined) {
            if (key === "unitClassify") {
              this.rules["unit.unitName"] = [];
              if (res[key] === 0) {
                this.rules["unit.unitName"].push({ required: true, message: "类别名称不能为空", trigger: "blur" });
              } else {
                this.rules["unit.unitName"].push({ required: true, message: "单位名称不能为空", trigger: "blur" });
              }
              this.unitClassify = res[key] === 0 ? 0 : "yes";
              this.oldUnitClassify = res[key] === 0 ? 0 : "yes";
            } else {
              this.Form.unit[key] = res[key];
            }
          }
        });
        this.Form.unit.unitWork = this.Form.unit.unitWork === 0 ? '' : this.Form.unit.unitWork;
        this.Form.unit.unitDeputy = this.Form.unit.unitDeputy === 0 ? '' : this.Form.unit.unitDeputy;
        this.Form.unit.workerNumber = this.Form.unit.workerNumber === 0 ? '' : this.Form.unit.workerNumber;
        if (res.unitClassify !== 0) {
          this.Form.unitProfessionalList = res.unitProfessionalList ? res.unitProfessionalList : [];
          this.unitGradeOriginalVal = this.Form.unit.unitGrade;
          if (this.Form.unitProfessionalList.length > 0) {
            this.Form.unitProfessionalList.forEach(item => {
              let unitGrade = 1;
              if (item.professionalLevel === 5 || this.Form.unit.unitGrade === '正县' || this.Form.unit.unitGrade === '副县') {
                unitGrade = 0; // 乡级机关，正县，副县
              }
              if ((this.Form.unit.unitGrade === '正科' || this.Form.unit.unitGrade === '副科') && item.isMunicipal === 1) {
                item.professionalLevel = 3
              }
              getAllProfessionList(item.professionalLevel, unitGrade)
                .then(res => {
                  this.$set(item, 'professionOpts', res);
                })
                .catch(err => {
                  const msg = err.result ? err.result : '职称获取失败';
                  this.$message({
                    message: msg,
                    type: "error"
                  });
                });
            })
          } else {
            const obj = {
              professionalId: '',
              professionalNum: '',
              professionalLevel: '',
              professionOpts: []
            }
            this.Form.unitProfessionalList.push(obj);
          }
        }
      }).catch(err => {
        const msg = err.result ? err.result : '单位信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      });
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.unitClassify === 0) {
            if (this.operateType === "edit") {
              this.msg = "修改分类";
            } else {
              this.msg = "新增分类";
            }
          } else {
            if (this.operateType === "edit") {
              this.msg = "修改单位";
            } else {
              this.msg = "新增单位";
            }
          }
          this.Form.unit.unitWork = parseInt(this.Form.unit.unitWork);
          this.Form.unit.unitDeputy = this.Form.unit.unitDeputy ? parseInt(this.Form.unit.unitDeputy) : 0;
          if (this.Form.unit.isTempUnit === 0) {
            this.Form.unit.workerNumber = 0;
            this.Form.unit.remark = '';
          } else {
            this.Form.unit.unitComplement = 0;
            this.Form.unit.unitWork = 0;
            this.Form.unit.unitDeputy = 0;
            // this.Form.unit.unitOrganizationCode = "";
            this.Form.unit.unitSocialCode = "";
            this.Form.unit.unitGrade = "";
            this.Form.unit.unitType = null;
            this.Form.unitProfessionalList = [];
            this.Form.unit.unitCategory = "";
            this.Form.unit.officeLevel = "";
          }
          if (this.operateType === "add_level1" && this.unitClassify === "yes") {
            this.Form.unit.unitClassify = 1
            this.saveOrUpdateUnit();
          } else if (this.operateType === "add_level2" && this.unitClassify === "yes") {
            if (this.operateLevel === 0) {
              this.Form.unit.unitClassify = 1;
            } else if (this.operateLevel === 1) {
              this.Form.unit.unitClassify = 2;
            }
            this.saveOrUpdateUnit();
          } else if (this.operateType === "edit" && this.unitClassify === "yes") {
            if (this.operateLevel === 1) {
              this.Form.unit.unitClassify = 1;
              this.saveOrUpdateUnit();
            } else if (this.operateLevel === 2) {
              this.Form.unit.unitClassify = 2;
              this.saveOrUpdateUnit();
            } else if (this.operateLevel === 0) {
              if (this.Form.unit.unitPid === 0) {
                this.Form.unit.unitClassify = 1;
                this.saveOrUpdateUnit();
              } else {
                findOneUnitInfo(this.Form.unit.unitPid).then(res => {
                  if (res.unitClassify === 1) {
                    this.Form.unit.unitClassify = 2;
                  } else if (res.unitClassify === 0) {
                    this.Form.unit.unitClassify = 1;
                  }
                  this.saveOrUpdateUnit();
                }).catch(err => {
                  const msg = err.result ? err.result : '单位信息获取失败';
                  this.$message({
                    message: msg,
                    type: "error"
                  });
                })
              }
            }
          } else if (this.unitClassify === 0) {
            this.Form.unit.unitClassify = 0;
            // this.Form.unit.unitCharger = "";
            this.Form.unit.unitComplement = 0; // 后台限制，人数不能为空
            this.Form.unit.unitWork = 0;
            this.Form.unit.unitDeputy = 0;
            // this.Form.unit.unitOrganizationCode = "";
            this.Form.unit.unitSocialCode = "";
            this.Form.unit.unitGrade = "";
            this.Form.unit.unitType = null;
            this.Form.unitProfessionalList = [];
            this.Form.unit.unitCategory = "";
            this.Form.unit.officeLevel = "";
            this.saveOrUpdateUnit();
          }
        } else {
          return false;
        }
      });
    },
    // 保存或者更新单位
    saveOrUpdateUnit() {
      // 新增单位时，单位级别选择正科或者副科时，并且单位编制信息的单位级别是市级机关时, isMunicipal（是否是正科或副科下的市级机关  1是 2否）
      if (this.Form.unitProfessionalList.length > 0 && (this.Form.unit.unitGrade === '正科' || this.Form.unit.unitGrade === '副科')) {
        this.Form.unitProfessionalList.forEach(item => {
          if (item.professionalLevel === 3) {
            item.isMunicipal = 1
          } else {
            item.isMunicipal = 2
          }
        })
      } else if (this.Form.unitProfessionalList.length > 0 && (this.Form.unit.unitGrade !== '正科' && this.Form.unit.unitGrade !== '副科')) {
        this.Form.unitProfessionalList.forEach(item => {
          item.isMunicipal = 2
        })
      }
      saveOrUpdateUnit(this.Form).then(res => {
        this.$message({
          message: `${this.msg}成功`,
          type: "success"
        });
        this.dialogVisible = false;
        this.getList();
      }).catch(err => {
        const msg = err.result ? err.result : `${this.msg}失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      });
    },
    // 模态框打开的回调
    dialogOpen() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
      })
    },
    // dialog关闭的回调
    dialogClosed() {
      for (let key in this.Form.unit) {
        var type = this.getDataType(this.Form.unit[key]);
        if (type === "String") {
          this.Form.unit[key] = "";
        } else if (type === "Array") {
          this.Form.unit[key] = [];
        } else if (type === "Object") {
          this.Form.unit[key] = {};
        } else {
          this.Form.unit[key] = null;
        }
      }
      this.Form.unit.unitPid = 0;
      this.Form.unit.unitComplement = 0;
      this.Form.unit.isTempUnit = 0;
      this.Form.unitProfessionalList = [];
      this.unitClassify = "yes";
      this.unitGradeOriginalVal = '';
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
    // 只能输入正整数
    limitInt(key) {
      this.Form.unit[key] = this.Form.unit[key].replace(/[^0-9]/g, "");
    },
    unitProfessionalLimitInt(key, index) {
      this.Form.unitProfessionalList[index][key] = this.Form.unitProfessionalList[index][key].replace(/[^0-9]/g, "");
    },
    getAllProfessionList(professionalLevel, index) {
      let unitGrade = 1;
      if (professionalLevel === 5 || this.Form.unit.unitGrade === '正县' || this.Form.unit.unitGrade === '副县') {
        unitGrade = 0; // 乡级机关，正县，副县
      }
      getAllProfessionList(professionalLevel, unitGrade)
        .then(res => {
          this.$set(this.Form.unitProfessionalList[index], 'professionOpts', res);
        })
        .catch(err => {
          const msg = err.result ? err.result : '职称获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    }
  },
  mounted() {
    this.getUnitTypeList();
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
#baseInfoUnit .query-box {
  margin-bottom: 20px;
}
#baseInfoUnit .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#baseInfoUnit .query-item > div {
  float: left;
}
#baseInfoUnit .query-item > .query-input {
  width: calc(100% - 80px);
}
#baseInfoUnit .query-item .el-date-editor.el-input,
#baseInfoUnit .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#baseInfoUnit .query-item .el-select {
  width: 100%;
}
#baseInfoUnit .query-btn {
  float: left;
  margin-left: 50px;
}
#baseInfoUnit .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#baseInfoUnit .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#baseInfoUnit .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#baseInfoUnit .radio-box .el-form-item__content {
  width: 200px;
}
#baseInfoUnit .add-item-btn {
  color: #3891e0;
  margin-left: 10px;
  cursor: pointer;
}
#baseInfoUnit .add-item-btn:hover {
  color: #035cac;
}
#baseInfoUnit .delete-item-btn {
  color: #f65c5f;
  margin-left: 10px;
  cursor: pointer;
}
#baseInfoUnit .delete-item-btn:hover {
  color: #d82a2d;
}
#baseInfoUnit .unit-level-box {
  display: inline-block;
}
#baseInfoUnit .unit-professional-list > .el-form-item__content {
  width: 800px;
}
#baseInfoUnit .line {
  width: 100%;
  height: 1px;
  background: #999;
  margin-bottom: 30px;
}
</style>


