<template>
  <div id="dataCollect">
    <div class="tab-container">
      <export-btn @clickBtn="openExportDialog"></export-btn>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="办公用房" name="office">
          <div class="table-box">
            <table-component :tableData="tableData_office" :tableHeight="tableHeight">
              <el-table-column type="index" label="序号" width="50" align="center" fixed></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="position"
                label="地址坐落"
                align="center"
                width="200"
                fixed
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="siteArea"
                label="用地面积(m²)"
                align="center"
                width="120"
                fixed
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="totalCoveredArea"
                label="总建筑面积(m²)"
                align="center"
                width="120"
                fixed
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="officeCoveredArea"
                label="办公用房建筑面积(m²)"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="technologyCoveredArea"
                label="技术业务用房建筑面积(m²)"
                align="center"
              ></el-table-column>
              <el-table-column align="center" label="使用单位（多个单位填多条）及批复编制情况(人)">
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="unitName"
                  label="单位名称"
                  align="center"
                  class-name="table-cell-special"
                  width="150"
                >
                  <template slot-scope="scope">
                    <div
                      v-for="(item, index) in scope.row.unitName"
                      :key="index"
                      class="table-column-item"
                    >{{item}}</div>
                  </template>
                </el-table-column>
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="unitType"
                  label="类别"
                  align="center"
                  class-name="table-cell-special"
                >
                  <template slot-scope="scope">
                    <div
                      v-for="(item, index) in scope.row.unitType"
                      :key="index"
                      class="table-column-item"
                    >{{item}}</div>
                  </template>
                </el-table-column>
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="cityOfficialStaffNum"
                  label="市级正职"
                  align="center"
                  class-name="table-cell-special"
                >
                  <template slot-scope="scope">
                    <div
                      v-for="(item, index) in scope.row.cityOfficialStaffNum"
                      :key="index"
                      class="table-column-item"
                    >{{item}}</div>
                  </template>
                </el-table-column>
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="cityDeputyStaffNum"
                  label="市级副职"
                  align="center"
                  class-name="table-cell-special"
                >
                  <template slot-scope="scope">
                    <div
                      v-for="(item, index) in scope.row.cityDeputyStaffNum"
                      :key="index"
                      class="table-column-item"
                    >{{item}}</div>
                  </template>
                </el-table-column>
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="bureauOfficialStaffNum"
                  label="正局（处）级"
                  align="center"
                  class-name="table-cell-special"
                >
                  <template slot-scope="scope">
                    <div
                      v-for="(item, index) in scope.row.bureauOfficialStaffNum"
                      :key="index"
                      class="table-column-item"
                    >{{item}}</div>
                  </template>
                </el-table-column>
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="bureauDeputyStaffNum"
                  label="副局（处）级"
                  align="center"
                  class-name="table-cell-special"
                >
                  <template slot-scope="scope">
                    <div
                      v-for="(item, index) in scope.row.bureauDeputyStaffNum"
                      :key="index"
                      class="table-column-item"
                    >{{item}}</div>
                  </template>
                </el-table-column>
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="underDirectorNum"
                  label="局（处）级以下"
                  align="center"
                  class-name="table-cell-special"
                >
                  <template slot-scope="scope">
                    <div
                      v-for="(item, index) in scope.row.underDirectorNum"
                      :key="index"
                      class="table-column-item"
                    >{{item}}</div>
                  </template>
                </el-table-column>
              </el-table-column>
              <el-table-column align="center" label="基本办公用房使用面积(m²)">
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="officeUseArea"
                  label="办公室"
                  align="center"
                ></el-table-column>
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="serviceUseArea"
                  label="服务用房"
                  align="center"
                ></el-table-column>
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="equipmentUseArea"
                  label="设备用房"
                  align="center"
                ></el-table-column>
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="totalUseArea"
                  label="合计"
                  align="center"
                ></el-table-column>
              </el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="affiliatedCoveredArea"
                label="附属用房建筑面积"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="registration"
                label="权属登记情况"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="buildingAge"
                label="建设年代"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="isRent"
                label="是否为租（借）用"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="remark"
                label="备注"
                align="center"
              ></el-table-column>
            </table-component>
          </div>
        </el-tab-pane>
        <el-tab-pane label="技术业务用房" name="technology">
          <div class="table-box">
            <table-component :tableData="tableData_technology" :tableHeight="tableHeight">
              <el-table-column type="index" label="序号" width="50" align="center"></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="yardPosition"
                label="地址坐落"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="coveredArea"
                label="用地面积(m²)"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="buildArea"
                label="总建筑面积(m²)"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="unitNameList"
                label="主要使用单位"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="warrantRemark"
                label="权属登记情况"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="buildDate"
                label="建设年代"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="remark"
                label="备注"
                align="center"
              ></el-table-column>
            </table-component>
          </div>
        </el-tab-pane>
        <el-tab-pane label="办公用房建设项目" name="construction">
          <div class="table-box">
            <table-component :tableData="tableData_construction" :tableHeight="tableHeight">
              <el-table-column type="index" label="序号" width="50" align="center"></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="projectName"
                label="项目名称"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="approvalDepartment"
                label="审批部门"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="unitName"
                label="建设单位"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="unitList"
                label="主要使用单位"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="yardPosition"
                label="地址坐落"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="buildArea"
                label="建设规模(m²)"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="estimateInvestment"
                label="估算投资(万元)"
                align="center"
              ></el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="remark"
                label="备注"
                align="center"
              ></el-table-column>
            </table-component>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <el-dialog
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="780px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>导出数据</span>
      </div>
      <el-form :inline="true" :model="Form" ref="Form" :rules="rules">
        <el-form-item label="行政区划名称" label-width="120px">
          <div style="width:200px;">{{Form.distName}}</div>
        </el-form-item>
        <el-form-item label="行政区划级别" label-width="120px" prop="distLevel">
          <el-select
            v-model="Form.distLevel"
            placeholder="请选择行政区划级别"
            style="width:200px;"
            size="small"
          >
            <el-option
              v-for="(item, index) in distLevelOpt"
              :key="index"
              :label="item"
              :value="item"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="行政区划代码" label-width="120px" prop="distCode">
          <el-input v-model.trim="Form.distCode" size="small" placeholder="请输入行政区划代码"></el-input>
        </el-form-item>
        <el-form-item label="填表人单位" label-width="120px" prop="fillerUnit">
          <el-input v-model.trim="Form.fillerUnit" size="small" placeholder="请输入填表人单位"></el-input>
        </el-form-item>
        <el-form-item label="填表人姓名" label-width="120px" prop="fillerName">
          <el-input v-model.trim="Form.fillerName" size="small" placeholder="请输入填表人姓名"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" label-width="120px" prop="contactNumber">
          <el-input
            v-model.trim="Form.contactNumber"
            size="small"
            placeholder="请输入联系电话"
            @keyup.native="limitInt('contactNumber')"
            @blur="limitInt('contactNumber')"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="exportData" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { PRO_NAME } from "@/axios/global"
import {
  getBusinessHouseList,
  getConstructHouseList,
  getYardUnitReportList,
  selectDistInfo,
  exportDataExcel,
  getYardUnitReport
} from "./api";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import exportBtn from "@/components/button/exportBtn";
export default {
  components: {
    singleBtnGroup,
    tableBtnGroup,
    tablePagination,
    tableComponent,
    cancelBtn,
    confirmBtn,
    exportBtn
  },
  data() {
    return {
      loading: null,
      tableHeight: 0,
      officeKeyArr_1: ['unitName', 'unitType', 'cityOfficialStaffNum', 'cityDeputyStaffNum', 'bureauOfficialStaffNum', 'bureauDeputyStaffNum', 'underDirectorNum'],
      officeKeyArr_2: ['position', 'siteArea', 'totalCoveredArea', 'officeCoveredArea', 'technologyCoveredArea', 'officeUseArea', 'serviceUseArea', 'equipmentUseArea', 'totalUseArea', 'affiliatedCoveredArea', 'registration', 'buildingAge', 'isRent', 'remark'],
      officeKeyArr_3: ['siteArea', 'totalCoveredArea', 'officeCoveredArea', 'technologyCoveredArea', 'officeUseArea', 'serviceUseArea', 'equipmentUseArea', 'totalUseArea', 'affiliatedCoveredArea'],
      tableData_office: [
        // {
        //   siteArea: '4000.00', // 用地面积,
        //   totalCoveredArea: '6000.00', // 总建筑面积
        //   officeCoveredArea: '5000.00', // 办公用房建筑面积
        //   technologyCoveredArea: '1000.00', // 技术业务用房建筑面积
        //   unitName: ['宜昌市财政局', '市国库收付局'], //单位名称
        //   unitType: ['机关本级', '机关本级'], // 类别
        //   cityOfficialStaffNum: [0, 1], //市级正职（副省级）
        //   cityDeputyStaffNum: [0, 1], // 市级副职（副市级）
        //   bureauOfficialStaffNum: [1, 1],// 市直部内正职（正局级）
        //   bureauDeputyStaffNum: [1, 2], // 市直部内副职（副局级）
        //   underDirectorNum: [109, 77], // 处级以下
        //   officeUseArea: '600.00', // 办公室使用面积
        //   serviceUseArea: '200.00', // 服务用房使用面积
        //   equipmentUseArea: '150.00', // 设备用房使用面积
        //   totalUseArea: '950.00', // 总使用面积
        //   affiliatedCoveredArea: '300.00', // 附属用房建筑面积
        //   registration: '已登记', // 权属登记情况
        //   buildingAge: '2010年', // 建设年代
        //   isRent: '否', // 是否为租（借）用
        //   remark: '' // 备注
        // }
      ],
      tableData_technology: [],
      tableData_construction: [],
      dialogVisible: false,
      Form: {
        distInformationId: "",
        distName: PRO_NAME,
        distLevel: "",
        distCode: "",
        fillerUnit: "",
        fillerName: "",
        contactNumber: ""
      },
      activeName: 'office',
      distLevelOpt: ['市（州、盟）', '县（区、旗）', '乡（镇）'],
      businessHouseDtos: null,
      yardUnitProfessionalDtos: null,
      constructHouseDtos: null,
      rules: {
        distLevel: [
          { required: true, message: "请选择行政区划级别", trigger: "change" }
        ],
        distCode: [
          { required: true, message: "行政区划代码不能为空", trigger: "blur" }
        ],
        fillerUnit: [
          { required: true, message: "填表人单位不能为空", trigger: "blur" }
        ],
        fillerName: [
          { required: true, message: "填表人姓名不能为空", trigger: "blur" }
        ],
        contactNumber: [
          { required: true, message: "联系电话不能为空", trigger: "blur" }
        ]
      }
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
    // 获取信息列表
    getList(name, flag) {
      this.loadingStart('.table-box');
      if (name === 'office') {
        this.loadingEnd();
        getYardUnitReportList().then(res => {
          if (flag) {
            this.yardUnitProfessionalDtos = JSON.parse(JSON.stringify(res));
          }
          res.forEach(item => {
            for (let i = 0; i < this.officeKeyArr_1.length; i++) {
              const key = this.officeKeyArr_1[i];
              item[key] = [];
            }
            for (let i = 0; i < this.officeKeyArr_2.length; i++) {
              const key = this.officeKeyArr_2[i];
              if (item[key] === undefined || item[key] === '' || item[key] === null) {
                item[key] = '-'
              } else {
                if (this.officeKeyArr_3.indexOf(key) !== -1) {
                  item[key] = item[key].toFixed(2);
                }
              }
            }
            const useUnitAndSituationList = item.useUnitAndSituation;
            if (useUnitAndSituationList && useUnitAndSituationList.length > 0) {
              useUnitAndSituationList.forEach(info => {
                for (let i = 0; i < this.officeKeyArr_1.length; i++) {
                  const key = this.officeKeyArr_1[i];
                  if (info[key]) {
                    item[key].push(info[key])
                  } else if (info[key] === 0) {
                    item[key].push(0);
                  } else {
                    item[key].push('-');
                  }
                }
              })
            } else {
              for (let i = 0; i < this.officeKeyArr_1.length; i++) {
                const key = this.officeKeyArr_1[i];
                item[key].push('-');
              }
            }
          })
          this.tableData_office = res;
          this.loadingEnd();
        }).catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '办公用房列表获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
      } else if (name === 'technology') {
        getBusinessHouseList().then(res => {
          if (res && res.length > 0) {
            res.forEach(item => {
              item.buildDate = item.buildDate ? this.formatDate(item.buildDate) : '-';
              item.unitNameList = item.unitNameList ? item.unitNameList.join('，') : '-';
              item.coveredArea = item.coveredArea ? item.coveredArea.toFixed(2) : '-';
              item.buildArea = item.buildArea ? item.buildArea.toFixed(2) : '-';
              item.remark = item.remark ? item.remark : '-';
              item.warrantRemark = item.warrantRemark ? item.warrantRemark : '-';
              item.yardPosition = item.yardPosition ? item.yardPosition : '-';
            })
            this.tableData_technology = res;
          }
          this.loadingEnd();
        }).catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '技术业务用房列表获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
      } else if (name === 'construction') {
        getConstructHouseList().then(res => {
          res.forEach(item => {
            item.buildArea = item.buildArea ? item.buildArea.toFixed(2) : '-';
            item.estimateInvestment = item.estimateInvestment ? item.estimateInvestment.toFixed(2) : '-';
            item.remark = item.remark ? item.remark : '-';
            item.projectName = item.projectName ? item.projectName : '-';
            item.approvalDepartment = item.approvalDepartment ? item.approvalDepartment : '-';
            item.unitName = item.unitName ? item.unitName : '-';
            item.unitList = item.unitList ? item.unitList.join('，') : '-';
            item.yardPosition = item.yardPosition ? item.yardPosition : '-';
          })
          this.tableData_construction = res;
          this.loadingEnd();
        }).catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '办公用房建设项目列表获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
      }
    },
    // 获取所有的数据
    getAllInfo() {
      getBusinessHouseList().then(res => {
        this.businessHouseDtos = res;
      }).catch(err => {
        this.loadingEnd();
        const msg = err.result ? err.result : '技术业务用房列表获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      });
      getConstructHouseList().then(res => {
        this.constructHouseDtos = res;
      }).catch(err => {
        this.loadingEnd();
        const msg = err.result ? err.result : '办公用房建设项目列表获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      });
    },
    // 切换tab
    handleClick(tab) {
      this.getList(tab.name);
    },
    // 查询行政区划信息
    selectDistInfo() {
      selectDistInfo().then(res => {
        for ([key, value] of Object.entries(res)) {
          if (this.Form[key] !== undefined) {
            this.Form[key] = value;
          }
        }
      }).catch(err => {
        const msg = err.result ? err.result : `行政区划信息获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    openExportDialog() {
      this.dialogVisible = true;
      this.selectDistInfo();
    },
    // 导出
    exportData() {
      this.$refs["Form"].validate(valid => {
        if (valid) {
          let Form = {};
          Form.distInformation = this.Form;
          Form.businessHouseDtos = this.businessHouseDtos;
          Form.yardUnitProfessionalDtos = this.yardUnitProfessionalDtos;
          Form.constructHouseDtos = this.constructHouseDtos;
          exportDataExcel(Form).then(res => {
            let fileDownload = require('js-file-download');
            let name = decodeURIComponent(sessionStorage.getItem('CADdownloadFileName'));
            fileDownload(res, name);
            this.$message({
              message: '导出成功',
              type: "success"
            });
            this.dialogVisible = false;
          }).catch(err => {
            const msg = err.result ? err.result : '导出数据失败';
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
    // dialog打开的回调
    dialogOpen() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
      })
    },
    // dialog关闭的回调
    dialogClosed() {
      this.clearForm(this.Form);
      this.Form.distName = PRO_NAME;
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
    // 只能输入正整数
    limitInt(key) {
      this.Form[key] = this.Form[key].replace(/[^0-9]/g, "");
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
    this.getList('office', true);
    this.getAllInfo();
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $(".queryBox").outerHeight(true) || 0;
      let tabHeaderH = $(".el-tabs__header").outerHeight(true);
      this.tableHeight = mainDivH - queryBoxH - tabHeaderH;
    }, 1);
  }
};
</script>

<style>
#dataCollect .query-box {
  margin-bottom: 20px;
}
#dataCollect .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#dataCollect .query-item > div {
  float: left;
}
#dataCollect .query-item > .query-input {
  width: calc(100% - 80px);
}
#dataCollect .query-item .el-date-editor.el-input,
#dataCollect .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#dataCollect .query-btn {
  float: left;
  margin-left: 50px;
}
#dataCollect .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#dataCollect .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#dataCollect .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#dataCollect .table-column-item {
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  border-bottom: 1px solid #c8c8c8;
  line-height: 30px;
}
#dataCollect .table-column-item:last-child {
  border-bottom: none;
}
#dataCollect .table-cell-special {
  padding-top: 0 !important;
  padding-bottom: 0 !important;
}
#dataCollect .table-cell-special .cell {
  padding-left: 0;
  padding-right: 0;
}
#dataCollect .export-btn {
  position: absolute;
  right: 30px;
  z-index: 100;
}
</style>


