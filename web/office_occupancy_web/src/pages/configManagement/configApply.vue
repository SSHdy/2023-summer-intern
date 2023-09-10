<template>
  <div id="configApply">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">配置编号：</div>
          <div class="query-input">
            <el-input v-model.trim="configureNo" placeholder="请输入配置编号"></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">申请时间：</div>
          <div class="query-input">
            <el-date-picker
              v-model="startTime"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd HH:mm:ss"
              :picker-options="pickerOptions_start"
            ></el-date-picker>
          </div>
        </div>
        <div class="query-item">
          <div class="query-title" style="margin:0 5px">至</div>
          <div class="query-input">
            <el-date-picker
              v-model="endTime"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd 23:59:59"
              :picker-options="pickerOptions_end"
            ></el-date-picker>
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
        <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="configureNo"
          label="配置编号"
          align="center"
        ></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="unitName" label="申请单位" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="configureApplicant"
          label="申请人"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="unitComplement"
          label="编制人数"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="actualNumber"
          label="实有人数"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="totalArea"
          label="现有面积(m²)"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="configureDate"
          label="申请时间"
          align="center"
        ></el-table-column>
        <el-table-column label="申请状态" width="140" align="center">
          <template slot-scope="scope">
            <span
              :class="{'status-unreviewed' : scope.row.applyStatus===0,'status-pass' : scope.row.applyStatus===1,'status-notPass' : scope.row.applyStatus===2,'status-reviewimg' : scope.row.applyStatus===3}"
            >{{scope.row.applyStatusText}}</span>
          </template>
        </el-table-column>
        <el-table-column label="审批" width="140" align="center">
          <template slot-scope="scope">
            <div class="view-box" @click="stepView(scope.row)">
              <span class="iconfont view-icon">&#xe62b;</span>
              <span class="view-text">查看</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDelete="false"
              :isShowDetail="true"
              :isShowAlter="scope.row.applyStatus === 0"
              @updateClick="singleUpdate(scope.row)"
              @detailClick="singleDetail(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group :batchDelBtnShow="false" @addClick="addUnit"></table-btn-group>
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
    <!-- 新增修改 -->
    <el-dialog
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="780px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{dialogTitle}}</span>
      </div>
      <el-form :model="Form" :rules="rules" ref="Form" inline>
        <el-form-item label="申请单位：" label-width="120px">
          <span class="info-item">{{baseInfoForm.unitName}}</span>
        </el-form-item>
        <el-form-item label="单位负责人：" label-width="120px">
          <span class="info-item">{{baseInfoForm.unitCharger}}</span>
        </el-form-item>
        <el-form-item label="编制人数：" label-width="120px">
          <span class="info-item">{{baseInfoForm.unitComplement}}人</span>
        </el-form-item>
        <el-form-item label="实有人数：" label-width="120px">
          <span class="info-item">{{baseInfoForm.actualNumber}}人</span>
        </el-form-item>
        <el-form-item label="现有面积：" label-width="120px">
          <span class="info-item">{{baseInfoForm.totalArea ? baseInfoForm.totalArea : '暂无'}}</span>
        </el-form-item>
        <el-form-item label="人均面积：" label-width="120px">
          <span class="info-item">{{baseInfoForm.PerCapitaArea ? baseInfoForm.PerCapitaArea : '暂无'}}</span>
        </el-form-item>
        <el-form-item label="联系电话：" label-width="120px" prop="contactPhone">
          <el-input
            v-model.trim="Form.contactPhone"
            placeholder="请输入联系电话"
            size="small"
            @keyup.native="limitInt('contactPhone')"
            @blur="limitInt('contactPhone')"
            v-if="operateType !== 'detail'"
          ></el-input>
          <span v-if="operateType === 'detail'" class="info-item">{{Form.contactPhone}}</span>
        </el-form-item>
        <el-form-item label="联系人：" label-width="120px" prop="contacts">
          <el-input
            v-model.trim="Form.contacts"
            placeholder="请输入联系人"
            size="small"
            v-if="operateType !== 'detail'"
          ></el-input>
          <span v-if="operateType === 'detail'" class="info-item">{{Form.contacts}}</span>
        </el-form-item>
        <el-form-item label="单位性质：" label-width="120px">
          <el-input
            v-model.trim="Form.unitProperty"
            placeholder="请输入单位性质"
            size="small"
            v-if="operateType !== 'detail'"
          ></el-input>
          <span v-if="operateType === 'detail'" class="info-item">{{Form.unitProperty}}</span>
        </el-form-item>
        <el-form-item label="单位地址：" label-width="120px">
          <el-input
            v-model.trim="Form.unitAddress"
            placeholder="请输入单位地址"
            size="small"
            v-if="operateType !== 'detail'"
          ></el-input>
          <span v-if="operateType === 'detail'" class="info-item">{{Form.unitAddress}}</span>
        </el-form-item>
        <el-form-item label="申请原因：" label-width="120px" prop="applyReason" style="width:100%">
          <el-input
            v-model.trim="Form.applyReason"
            type="textarea"
            :rows="5"
            placeholder="请输入申请原因"
            size="small"
            style="width:534px"
            v-if="operateType !== 'detail'"
          ></el-input>
          <span v-if="operateType === 'detail'" class="info-item">{{Form.applyReason}}</span>
        </el-form-item>
        <el-form-item
          :label="operateType!=='detail' ? '上传附件：': '下载附件：'"
          label-width="120px"
          prop="enclosureList"
        >
          <div class="file-upload" v-if="operateType!=='detail'">
            <el-upload
              ref="upload"
              :headers="postHeaders"
              :action="`${uploadUrl}?uploadType=configure`"
              :file-list="fileList"
              :auto-upload="false"
              :accept="acceptType"
              :limit="limit"
              :on-success="handleSuccess"
              :on-remove="handleRemove"
              :on-change="handleChange"
              :on-error="handleError"
              :on-exceed="handleExceed"
              :before-upload="beforeAvatarUpload"
              multiple
            >
              <i class="iconfont upload-icon">&#xe661;</i>
              <el-button slot="trigger" size="small" type="primary">点击选择文件</el-button>
            </el-upload>
          </div>
          <div class="file-download" v-if="operateType==='detail' && enclosureList.length > 0">
            <el-checkbox-group v-model="fileDownLoadCheckedList" @change="handleCheckedFileChange">
              <div v-for="(file, index) in enclosureList" :key="index" class="file-download-item">
                <el-checkbox :label="file.detail">{{file.enclosureName}}</el-checkbox>
              </div>
            </el-checkbox-group>
            <el-button size="small" type="primary" @click="fileDownLoad(downloadCheckedList)">点击下载</el-button>
          </div>
          <span v-if="operateType==='detail' && enclosureList.length === 0">暂无附件</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" v-if="operateType !== 'detail'">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /新增修改 -->
    <!-- 审批进度流程 -->
    <el-dialog
      :visible.sync="stepDialogVisible"
      :close-on-click-modal="false"
      width="780px"
      @closed="stepDialogClosed"
      :center="true"
      class="step-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>申请审批进度</span>
      </div>
      <div class="step-box">
        <el-steps
          :active="applyStepActive"
          align-center
          finish-status="success"
          :space="100"
          direction="vertical"
        >
          <el-step v-for="(item,index) in applyProcess" :key="index" :title="item.stepName">
            <div slot="description" v-show="index < applyStepActive">
              <div>
                <span>操作人：</span>
                {{item.transactors}}
              </div>
              <div>
                <span>操作时间：</span>
                {{item.processingTime}}
              </div>
            </div>
          </el-step>
        </el-steps>
      </div>
    </el-dialog>
    <!-- /审批进度流程 -->
  </div>
</template>

<script>
import {
  getConfigureList,
  addConfigure,
  updateConfigure,
  getBasicConfigure,
  getConfigureDetail,
  selectProcessHistory,
  downloadFile
} from "./api";
import { BASE_URL, url_name_1 } from "@/axios/global";
import { uploadUrl } from "@/axios/commonApi";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
export default {
  components: {
    singleBtnGroup,
    tableBtnGroup,
    tablePagination,
    tableComponent,
    cancelBtn,
    confirmBtn,
    searchBtn,
    resetBtn
  },
  data() {
    return {
      token: '',
      postHeaders: {
        Authorization: ''
      },
      configureNo: '',
      userId: '',
      startTime: "",
      endTime: "",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      selectData: [],
      tableData: [],
      dialogTitle: "",
      operateType: "add",
      uploadUrl: uploadUrl,
      acceptType: "image/jpeg,image/gif,image/png,image/bmp,.doc,.docx,.xlsx,.ppt,.pdf,.rar,.zip",
      limit: 5,
      fileList: [],
      uploadNum: 0,
      uploadedNum: 0,
      uploadSuccessNum: 0,
      dialogVisible: false,
      stepDialogVisible: false,
      baseInfoForm: {
        actualNumber: '',
        totalArea: null,
        PerCapitaArea: null,
        unitCharger: '',
        unitComplement: null,
        unitName: ''
      },
      Form: {
        applyReason: '',
        configureApplicant: '',
        configureApplicantId: null,
        configureId: null,
        contactPhone: '',
        contacts: '',
        unitAddress: '',
        unitProperty: '',
        enclosureList: [],
        deleteEnclosure: []
      },
      applyStepActive: 0,
      applyProcess: [],
      downloadCheckedList: [],
      fileDownLoadCheckedList: [],
      activeConfigureId: null,
      enclosureList: [],
      rules: {
        contactPhone: [
          { required: true, message: "联系电话不能为空", trigger: "blur" }
        ],
        contacts: [
          { required: true, message: "联系人不能为空", trigger: "blur" }
        ],
        applyReason: [
          { required: true, message: "申请原因不能为空", trigger: "blur" }
        ]
      },
      pickerOptions_start: {
      },
      pickerOptions_end: {
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
    // 获取列表
    getList() {
      this.loadingStart('.table-box');
      getConfigureList(this.currentPage, this.pageSize, this.configureNo, this.startTime, this.endTime, this.userId)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach(item => {
            item.applyStatusText = this.getApplyStatus(item.applyStatus);
            if (!item.totalArea) {
              item.totalArea = '无';
            }
            if (!item.actualNumber) {
              item.actualNumber = '无';
            }
          })
          this.loadingEnd();
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '配置申请列表获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取申请状态
    getApplyStatus(status) {
      switch (status) {
        case 0:
          return '未审核';
          break;
        case 1:
          return '审核通过';
          break;
        case 2:
          return '审核未通过';
          break;
        case 3:
          return '审核中';
          break;
      }
    },
    // 查询信息
    search() {
      this.currentPage = 1;
      this.getList();
    },
    // 重置
    reset() {
      this.currentPage = 1;
      this.configureNo = '';
      this.startTime = "";
      this.endTime = "";
      this.getList();
    },
    // 新增
    addUnit() {
      this.dialogVisible = true;
      this.dialogTitle = "新增配置申请";
      this.operateType = "add";
      this.getBasicConfigure();
    },
    // 单个修改
    singleUpdate(row) {
      this.dialogVisible = true;
      this.dialogTitle = "修改配置申请";
      this.operateType = "edit";
      this.Form.configureId = row.configureId;
      this.getBasicConfigure(row.configureId);
      getConfigureDetail(row.configureId).then(res => {
        for ([key, value] of Object.entries(res.configure)) {
          if (this.Form[key] !== undefined) {
            this.Form[key] = value;
          }
        }
        if (res.configureEnclosureList && res.configureEnclosureList.length > 0) {
          this.enclosureList = res.configureEnclosureList;
          this.uploadedNum = res.configureEnclosureList.length;
          res.configureEnclosureList.forEach(item => {
            const obj = {
              name: item.enclosureName,
              url: BASE_URL + url_name_1 + item.physicalAddress,
              uid: item.configureEnclosureId
            };
            this.fileList.push(obj);
          });
        }
      }).catch(err => {
        const msg = err.result ? err.result : '配置申请信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 单个详情
    singleDetail(row) {
      this.dialogVisible = true;
      this.dialogTitle = "配置申请详情";
      this.operateType = "detail";
      this.activeConfigureId = row.configureId;
      this.getBasicConfigure(row.configureId);
      getConfigureDetail(row.configureId).then(res => {
        for ([key, value] of Object.entries(res.configure)) {
          if (this.Form[key] !== undefined) {
            if (value) {
              this.Form[key] = value;
            } else {
              this.Form[key] = '暂无';
            }
          }
        }
        if (res.configureEnclosureList && res.configureEnclosureList.length > 0) {
          res.configureEnclosureList.forEach(item => {
            item.detail = `${item.enclosurePhysicsUrl},${item.enclosureName}`;
          });
          this.enclosureList = res.configureEnclosureList;
        }
      }).catch(err => {
        const msg = err.result ? err.result : '配置申请信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查询配置申请基础数据
    getBasicConfigure(id) {
      const configureId = id ? id : null;
      getBasicConfigure(configureId).then(res => {
        for ([key, value] of Object.entries(res)) {
          if (this.baseInfoForm[key] !== undefined) {
            this.baseInfoForm[key] = value;
          }
        }
        if (this.baseInfoForm.totalArea) {
          if (this.baseInfoForm.actualNumber !== 0) {
            this.baseInfoForm.PerCapitaArea = (this.baseInfoForm.totalArea / this.baseInfoForm.actualNumber).toFixed(2);
            this.baseInfoForm.PerCapitaArea = `${this.baseInfoForm.PerCapitaArea}m²`;
          }
          this.baseInfoForm.totalArea = this.baseInfoForm.totalArea.toFixed(2)
          this.baseInfoForm.totalArea = `${this.baseInfoForm.totalArea}m²`;
        }
      }).catch(err => {
        const msg = err.result ? err.result : '配置申请基础数据获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 审批流程
    stepView(row) {
      this.stepDialogVisible = true;
      selectProcessHistory(row.processInstanceId).then(res => {
        this.applyProcess = res;
        let endFlag = false;
        for (let i = this.applyProcess.length - 1; i >= 0; i--) {
          if (this.applyProcess[i].stepName === 'EndEvent') {
            endFlag = true;
          }
          if (this.applyProcess[i].stepName === 'StartEvent' || this.applyProcess[i].stepName === 'ExclusiveGateway' || this.applyProcess[i].stepName === 'EndEvent') {
            this.applyProcess.splice(i, 1);
          }
        }
        if (endFlag) {
          this.applyStepActive = this.applyProcess.length;
        } else {
          this.applyStepActive = this.applyProcess.length - 1;
        }
      }).catch(err => {
        const msg = err.result ? err.result : `审批进度获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.uploadNum === 0 ? this.saveForm() : this.submitUpload();
        } else {
          return false;
        }
      });
    },
    // 开始上传文件
    submitUpload() {
      this.$refs.upload.submit();
    },
    // 保存表单
    saveForm() {
      this.Form.configureApplicant = sessionStorage.getItem('name');
      this.Form.configureApplicantId = sessionStorage.getItem('userId');
      if (this.operateType === 'add') {
        addConfigure(this.Form).then(res => {
          this.$message({
            message: '配置申请新增成功',
            type: "success"
          });
          this.getList();
          this.dialogVisible = false;
          eventBus.$emit('updateBacklogTips');
        }).catch(err => {
          const msg = err.result ? err.result : `配置申请新增失败`;
          this.$message({
            message: msg,
            type: "error"
          });
        })
      } else if (this.operateType === 'edit') {
        updateConfigure(this.Form).then(res => {
          this.$message({
            message: '配置申请修改成功',
            type: "success"
          });
          this.getList();
          this.dialogVisible = false;
        }).catch(err => {
          const msg = err.result ? err.result : `配置申请修改失败`;
          this.$message({
            message: msg,
            type: "error"
          });
        })
      }
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
      this.fileList = [];
      this.uploadNum = 0;
      this.uploadedNum = 0;
      this.uploadSuccessNum = 0;
      this.downloadCheckedList = [];
      this.fileDownLoadCheckedList = [];
      this.activeConfigureId = null;
      this.enclosureList = [];
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
        this.Form.deleteEnclosure.push(file.uid);
      }
      this.uploadNum = fileList.length - this.uploadedNum;
      this.fileList = fileList;
    },
    // 上传文件成功
    handleSuccess(response, file, fileList) {
      this.uploadSuccessNum++;
      const fileType = file.raw.type.split('/')[0];
      if (fileType === 'image') {
        this.Form.enclosureList.push({
          configureId: this.Form.configureId,
          enclosureType: 1,
          enclosurePhysicsUrl: response.imgList[0].imgPath,
          enclosureVirtualUrl: response.imgList[0].imgUrl,
          enclosureName: file.name,
        });
      } else {
        this.Form.enclosureList.push({
          configureId: this.Form.configureId,
          enclosureType: 2,
          enclosurePhysicsUrl: response.imgList[0].imgPath,
          enclosureVirtualUrl: response.imgList[0].imgUrl,
          enclosureName: file.name,
        });
      }
      if (this.uploadSuccessNum === this.uploadNum) {
        this.saveForm();
      }
    },
    // 上传文件改变时钩子
    handleChange(file, fileList) {
      this.uploadChangeFlag = true;
      this.uploadNum = fileList.length - this.uploadedNum;
      this.fileList = fileList;
    },
    // 上传文件超出限制个数
    handleExceed(file, fileList) {
      this.$message.warning(`最多只能上传五个文件！`);
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
    // 文件下载
    fileDownLoad(list) {
      if (!list || list.length === 0) {
        this.$message({
          message: "请先选择一个文件",
          type: "warning"
        });
        return;
      }
      const downloadObj = {};
      downloadObj.addressList = list;
      downloadObj.configureId = this.activeConfigureId;
      downloadObj.downLoadType = 1;
      downloadFile(downloadObj).then(res => {
        let fileDownload = require('js-file-download');
        let name = decodeURIComponent(sessionStorage.getItem('CADdownloadFileName'));
        fileDownload(res, name);
      }).catch(err => {
        const msg = err.result ? err.result : '文件下载失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 改变checkbox
    handleCheckedFileChange(val) {
      this.downloadCheckedList = [];
      this.fileDownLoadCheckedList.forEach(item => {
        const arr = item.split(',');
        const obj = {
          address: arr[0],
          fileName: arr[1]
        }
        this.downloadCheckedList.push(obj);
      })
    },
    // 查看审批进度模态框关闭
    stepDialogClosed() {
      this.applyStepActive = 0;
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
    }
  },
  mounted() {
    this.userId = sessionStorage.getItem('userId');
    this.token = sessionStorage.getItem('TOKEN');
    this.username = sessionStorage.getItem('username');
    if (this.token) {
      this.postHeaders.Authorization = `Bearer ${this.token}`;
    }
    this.getList();
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - queryBoxH - operateBoxH;
    }, 1);

    this.pickerOptions_start.disabledDate = (time) => {
      if (this.endTime) {
        return new Date(this.endTime).getTime() < time.getTime();
      }
    }

    this.pickerOptions_end.disabledDate = (time) => {
      if (this.startTime) {
        return new Date(this.startTime).getTime() > time.getTime();
      }
    }
  }
};
</script>

<style>
#configApply .query-box {
  margin-bottom: 20px;
}
#configApply .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 18%;
}
#configApply .query-item > div {
  float: left;
}
#configApply .query-item > .query-input {
  width: calc(100% - 80px);
}
#configApply .query-item .el-select {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#configApply .query-item .el-date-editor.el-input,
#configApply .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#configApply .query-btn {
  float: right;
}
#configApply .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#configApply .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#configApply .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#configApply .status-unreviewed {
  color: #ff9f02;
}
#configApply .status-pass {
  color: #3891e0;
}
#configApply .view-box {
  cursor: pointer;
  color: #3891e0;
  font-size: 0;
  height: 28px;
  line-height: 28px;
}
#configApply .view-box:hover > .view-icon,
#configApply .view-box:hover > .view-text {
  color: #035cac;
}
#configApply .view-icon {
  font-size: 14px;
  position: relative;
  top: 1px;
}
#configApply .view-text {
  font-size: 14px;
}
#configApply .status-notPass {
  color: #f9463f;
}
#configApply .status-reviewimg {
  color: #1aa97b;
}
#configApply .detail-item {
  margin-bottom: 25px;
}
#configApply .detail-item > span {
  float: left;
}
#configApply .el-form-item {
  width: 320px;
}
#configApply .el-form-item__label {
  padding: 0;
}
#configApply .el-form-item__content {
  width: calc(100% - 120px);
}
#configApply .info-item {
  display: block;
}
#configApply .step-dialog .el-dialog__body {
  max-height: 500px;
  overflow: auto;
}
#configApply .file-upload .el-upload {
  margin-top: 40px;
}
#configApply .file-upload {
  position: relative;
  min-width: 500px;
}
#configApply .upload-icon {
  font-size: 60px;
  position: absolute;
  top: 0;
  left: 20px;
  color: #dfdfdf;
}
</style>


