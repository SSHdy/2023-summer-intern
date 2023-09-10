<template>
  <div id="dataReport">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">年份：</div>
          <div class="query-input">
            <el-date-picker
              v-model="reportYear"
              type="year"
              placeholder="选择年份"
              format="yyyy 年"
              value-format="yyyy"
            ></el-date-picker>
          </div>
        </div>
        <div class="query-item" style="margin-left: 50px">
          <div class="query-title">状态：</div>
          <div class="query-input">
            <el-select
              v-model="state"
              placeholder="请选择状态"
              style="width: 100%"
            >
              <el-option
                v-for="item in stateOpts"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="query-item" style="margin-left: 50px">
          <div class="query-title">单位名称：</div>
          <div class="query-input">
            <el-input
              v-model.trim="unitName"
              placeholder="请输入单位名称"
            ></el-input>
          </div>
        </div>
        <div class="query-btn">
          <search-btn @clickBtn="search"></search-btn>
          <reset-btn @clickBtn="reset"></reset-btn>
          <download-btn
            text="模板下载"
            :showIcon="true"
            :marginLeft="10"
            @clickBtn="templateDialogOpen"
          ></download-btn>
        </div>
      </div>
    </div>
    <div class="table-box">
      <table-component
        :tableData="tableData"
        :tableHeight="tableHeight"
        @select="handleSelectionChange"
      >
        <!-- <el-table-column type="selection" width="50" align="center"></el-table-column> -->
        <el-table-column
          :show-overflow-tooltip="true"
          prop="reportUnitName"
          label="单位名称"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="isReport"
          label="上报状态"
          align="center"
        >
          <template slot-scope="scope">
            <div>{{ scope.row.isReport === 1 ? "已上报" : "未上报" }}</div>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <upload-btn
            :fontSize="14"
            :marginLeft="0"
            padding="7px 10px"
            :showIcon="true"
            @clickBtn="uploadOpen"
          ></upload-btn>
          <download-btn
            :fontSize="14"
            padding="7px 10px"
            :showIcon="true"
            :marginLeft="10"
            @clickBtn="downloadOpen"
          ></download-btn>
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
    <!-- 模板下载 -->
    <el-dialog
      :visible.sync="templateDialogVisible"
      :close-on-click-modal="false"
      width="780px"
      @closed="templateDialogClosed"
      :center="true"
      class="template-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>{{ dialogTitle }}</span>
      </div>
      <div class="dialog-content">
        <el-radio-group v-model="templateCheckList">
          <el-radio
            v-for="(item, index) in templateList"
            :key="index"
            :label="item.downloadTemplateVirtualUrl"
            >{{ item.downloadTemplateName }}</el-radio
          >
        </el-radio-group>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn
          @clickBtn="templateDialogVisible = false"
          :fontSize="14"
        ></cancel-btn>
        <confirm-btn @clickBtn="templateDownLoad" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!--/模板下载 -->
    <!-- 上传 -->
    <el-dialog
      :visible.sync="uploadDialogVisible"
      :close-on-click-modal="false"
      width="780px"
      @open="uploadDialogOpen"
      @closed="uploadDialogClosed"
      :center="true"
      class="upload-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>{{ dialogTitle }}</span>
      </div>
      <div class="dialog-content">
        <el-form
          :model="reportUploadForm"
          :rules="reportUploadRules"
          ref="reportUploadForm"
        >
          <el-form-item label="上报单位：" label-width="105px" prop="reportId">
            <el-select
              filterable
              v-model="reportUploadForm.reportId"
              placeholder="请选择上报单位"
              style="width: 200px"
              size="small"
            >
              <el-option
                v-for="item in reportUnitOpts"
                :key="item.reportId"
                :label="item.reportUnitName"
                :value="item.reportId"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="上传文件：" label-width="105px" prop="haveFile">
            <div class="report-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="uploadUrl"
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
              >
                <i class="iconfont upload-icon">&#xe661;</i>
                <el-button
                  slot="trigger"
                  size="small"
                  type="primary"
                  :disabled="fileList.length === 1"
                  >点击选择文件</el-button
                >
              </el-upload>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn
          @clickBtn="uploadDialogVisible = false"
          :fontSize="14"
        ></cancel-btn>
        <confirm-btn @clickBtn="submitUploadData" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /上传 -->
    <!-- 下载 -->
    <el-dialog
      :visible.sync="downloadDialogVisible"
      :close-on-click-modal="false"
      width="780px"
      @closed="downloadDialogClosed"
      :center="true"
      class="download-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>{{ dialogTitle }}</span>
      </div>
      <div class="dialog-content">
        <el-form
          :model="reportDownloadForm"
          :rules="reportDownloadRules"
          ref="reportDownloadForm"
          inline
        >
          <el-form-item
            label="选择年份："
            label-width="105px"
            prop="reportYear"
          >
            <el-date-picker
              v-model="reportDownloadForm.reportYear"
              type="year"
              placeholder="选择年份"
              format="yyyy 年"
              value-format="yyyy"
            ></el-date-picker>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn
          @clickBtn="downloadDialogVisible = false"
          :fontSize="14"
        ></cancel-btn>
        <confirm-btn @clickBtn="download" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /下载 -->
  </div>
</template>

<script>
import {
  getReportUnitList,
  reportUnit,
  getDownloadTemplateList,
  getAllReportUnitList,
  downLoadReport,
  getAllReportEnclosure,
} from "./api";
import { eventBus } from "@/eventBus/eventBus";
import { BASE_URL, url_name_1 } from "@/axios/global";
import tableComponent from "@/components/table/table2.vue";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import downloadBtn from "@/components/button/downloadBtn";
import uploadBtn from "@/components/button/uploadBtn";
export default {
  components: {
    tablePagination,
    tableComponent,
    cancelBtn,
    confirmBtn,
    searchBtn,
    resetBtn,
    downloadBtn,
    uploadBtn,
  },
  data() {
    return {
      reportYear: "",
      unitName: "",
      state: "",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      selectData: [],
      tableData: [],
      dialogTitle: "",
      operateType: "add",
      templateDialogVisible: false,
      uploadDialogVisible: false,
      stateOpts: [
        {
          id: "",
          name: "全部",
        },
        {
          id: 1,
          name: "已上报",
        },
        {
          id: 0,
          name: "未上报",
        },
      ],
      templateCheckList: "",
      templateList: [],
      uploadUrl: `${BASE_URL}${url_name_1}/uploads/unitFile?uploadType=reportUnit`,
      acceptType: ".xlsx,.xls",
      limit: 1,
      fileList: [],
      uploadNum: 0,
      uploadSuccessNum: 0,
      submitFlag: false,
      token: "",
      postHeaders: {
        Authorization: "",
      },
      reportUploadForm: {
        reportId: "",
        reportEnclosureName: "",
        reportEnclosurePhysicsUrl: "",
        reportEnclosureVirtualUrl: "",
        haveFile: "",
      },
      isReported: false,
      reportUnitOpts: [],
      reportUploadRules: {
        reportId: [
          { required: true, message: "请选择上报单位", trigger: "change" },
        ],
        haveFile: [
          { required: true, message: "请先上传文件", trigger: "blur" },
        ],
      },
      addressList: [],
      downloadDialogVisible: false,
      reportDownloadForm: {
        reportYear: "",
      },
      reportDownloadRules: {
        reportYear: [
          { required: true, message: "请先选择年份", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.reportYear = this.formatDate(new Date().getTime(), "yyyy");
  },
  mounted() {
    this.getList();
    this.getDownloadTemplateList();
    this.token = sessionStorage.getItem("TOKEN");
    if (this.token) {
      this.postHeaders.Authorization = `Bearer ${this.token}`;
    }
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - queryBoxH - operateBoxH;
    }, 1);
  },
  methods: {
    // 开始加载
    loadingStart(classname) {
      if (!this.loading) {
        this.loading = this.$loading({
          lock: true,
          text: "正在加载...",
          spinner: "el-icon-loading",
          target: classname,
          customClass: "loading-box",
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
      this.loadingStart(".table-box");
      getReportUnitList(
        this.currentPage,
        this.pageSize,
        this.reportYear,
        this.unitName,
        this.state
      )
        .then((res) => {
          this.tableData = res.records;
          this.total = res.total;
          this.loadingEnd();
        })
        .catch((err) => {
          this.loadingEnd();
          const msg = err.result ? err.result : "单位上报列表获取失败";
          this.$message({
            message: msg,
            type: "error",
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
      this.reportYear = this.formatDate(new Date().getTime(), "yyyy");
      this.unitName = "";
      this.state = "";
      this.search();
    },
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
    },
    // 获取模板链接
    getDownloadTemplateList() {
      getDownloadTemplateList()
        .then((res) => {
          this.templateList = res;
        })
        .catch((err) => {
          const msg = err.result ? err.result : "模板链接获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    // 打开模板下载
    templateDialogOpen() {
      this.dialogTitle = "模板下载";
      this.templateDialogVisible = true;
    },
    // 模板下载
    templateDownLoad() {
      if (this.templateCheckList) {
        console.log(BASE_URL);
        const url = `${BASE_URL}${url_name_1}${this.templateCheckList}`;
        window.open(url, "_self");
        this.$message({
          message: "下载成功！",
          type: "success",
        });
        this.templateDialogVisible = false;
      } else {
        this.$message({
          message: "请先选择一个模板！",
          type: "warning",
        });
      }
    },
    // 打开上传模态框
    uploadOpen() {
      this.dialogTitle = "上传";
      this.uploadDialogVisible = true;
      this.getAllReportUnitList();
    },
    // 获取上报单位列表
    getAllReportUnitList() {
      getAllReportUnitList()
        .then((res) => {
          this.reportUnitOpts = res;
        })
        .catch((err) => {
          const msg = err.result ? err.result : "上报单位获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    // 保存上传文件
    saveUploadData() {
      delete this.reportUploadForm.haveFile;
      reportUnit(this.reportUploadForm)
        .then((res) => {
          this.$message({
            message: "上传成功",
            type: "success",
          });
          this.uploadDialogVisible = false;
          this.getList();
        })
        .catch((err) => {
          const msg = err.result ? err.result : "上传失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    // 提交上传文件
    submitUploadData() {
      this.$refs.reportUploadForm.validate((valid) => {
        if (valid) {
          for (let i = 0; i < this.reportUnitOpts.length; i++) {
            if (
              this.reportUnitOpts[i].reportId === this.reportUploadForm.reportId
            ) {
              if (this.reportUnitOpts[i].isReport === 1) {
                this.$confirm(
                  "该单位已有上报文件, 继续上传将覆盖已有文件，是否继续?",
                  "提示",
                  {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                  }
                )
                  .then(() => {
                    this.$refs.upload.submit();
                  })
                  .catch(() => {
                    this.$message({
                      type: "info",
                      message: "已取消上传",
                    });
                  });
              } else {
                this.$refs.upload.submit();
              }
              return false;
            }
          }
        } else {
          return false;
        }
      });
    },
    // 文件上传之前
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 200;
      if (!isLt2M) {
        this.$message.error("上传文件大小不能超过200MB!");
        return false;
      }
    },
    // 删除上传文件时钩子
    handleRemove(file, fileList) {
      this.uploadNum = fileList.length;
      this.fileList = fileList;
      this.reportUploadForm.haveFile = "";
    },
    // 上传文件改变时钩子
    handleChange(file, fileList) {
      this.uploadNum = fileList.length;
      this.fileList = fileList;
      if (fileList.length === 1) {
        this.reportUploadForm.haveFile = file.name;
      } else {
        this.reportUploadForm.haveFile = "";
      }
    },
    // 上传文件超出限制个数
    handleExceed(file, fileList) {
      this.$message.warning(`最多只能上传一个文件！`);
    },
    // 上传文件失败
    handleError(err, file, fileList) {
      if (err.status === 401) {
        this.$store.dispatch("Logout", this.token);
        this.$router.push({ path: "/login" });
      }
      const msg = err.result ? err.result : "上传失败";
      this.$message({
        message: msg,
        type: "error",
      });
    },
    // 上传文件成功
    handleSuccess(response, file, fileList) {
      this.uploadSuccessNum++;
      this.reportUploadForm.reportEnclosureName = file.name;
      this.reportUploadForm.reportEnclosurePhysicsUrl =
        response.imgList[0].imgPath;
      this.reportUploadForm.reportEnclosureVirtualUrl =
        response.imgList[0].imgUrl;
      if (this.uploadSuccessNum === this.uploadNum) {
        this.submitFlag = true;
      }
      if (this.submitFlag) {
        this.saveUploadData();
      }
    },
    // 打开下载模态框
    downloadOpen() {
      this.dialogTitle = "下载";
      this.downloadDialogVisible = true;
      this.reportDownloadForm.reportYear = this.formatDate(
        new Date().getTime(),
        "yyyy"
      );
    },
    // 下载文件
    download() {
      this.$refs.reportDownloadForm.validate((valid) => {
        if (valid) {
          getAllReportEnclosure(this.reportDownloadForm.reportYear)
            .then((list) => {
              downLoadReport(list)
                .then((res) => {
                  let fileDownload = require("js-file-download");
                  let name = decodeURIComponent(
                    sessionStorage.getItem("CADdownloadFileName")
                  );
                  fileDownload(res, name);
                  this.downloadDialogVisible = false;
                })
                .catch((err) => {
                  const msg = err.result ? err.result : "暂无上报文件！";
                  this.$message({
                    message: msg,
                    type: "error",
                  });
                });
            })
            .catch((err) => {
              const msg = err.result ? err.result : "年度上报文件获取失败";
              this.$message({
                message: msg,
                type: "error",
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
    uploadDialogOpen() {
      this.$nextTick(() => {
        this.$refs.reportUploadForm.clearValidate();
      });
    },
    // dialog关闭的回调
    templateDialogClosed() {
      this.templateCheckList = "";
    },
    uploadDialogClosed() {
      this.clearForm(this.reportUploadForm);
      this.reportUploadForm.haveFile = "";
      this.fileList = [];
      this.uploadNum = 0;
      this.uploadSuccessNum = 0;
      this.submitFlag = false;
      this.isReported = false;
    },
    downloadDialogClosed() {
      this.reportDownloadForm.reportYear = "";
    },
    // 获取数据类型
    getDataType(obj) {
      var typeArr = ["String", "Object", "Number", "Array", "Function"];
      for (let name of typeArr) {
        if (Object.prototype.toString.call(obj) === `[object ${name}]`) {
          return name;
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
  },
};
</script>

<style>
#dataReport .query-box {
  margin-bottom: 20px;
}
#dataReport .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#dataReport .query-item > div {
  float: left;
}
#dataReport .query-item > .query-input {
  width: calc(100% - 80px);
}
#dataReport .query-item .el-date-editor.el-input,
#dataReport .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#dataReport .query-btn {
  float: right;
  margin-left: 50px;
}
#dataReport .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#dataReport .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#dataReport .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#dataReport .template-dialog .dialog-content,
#dataReport .download-dialog .dialog-content {
  text-align: center;
}
#dataReport .template-dialog .el-radio-group {
  text-align: left;
}
#dataReport .template-dialog .el-radio {
  display: block;
  margin-top: 20px;
  margin-bottom: 20px;
}
#dataReport .report-file-upload .el-upload {
  margin-top: 40px;
}
#dataReport .report-file-upload {
  position: relative;
  min-width: 110px;
}
#dataReport .upload-icon {
  font-size: 60px;
  position: absolute;
  top: 0;
  left: 20px;
  color: #dfdfdf;
}
#dataReport .download-dialog .el-form-item .el-input__inner {
  width: auto;
}
</style>


