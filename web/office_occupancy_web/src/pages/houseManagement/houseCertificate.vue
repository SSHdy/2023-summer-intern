<template>
  <div id="houseCertificate">
    <div class="table-box">
      <table-component
        :tableData="tableData"
        :tableHeight="tableHeight"
        @select="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center"></el-table-column>
        <el-table-column
          prop="housePropertyCardNumber"
          label="房产证号"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column prop="location" label="坐落位置" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="address" label="楼栋/幢号" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="owner" label="所有权人" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column
          prop="houseCharacter"
          label="房屋性质"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="planningPurpose"
          label="规划用途"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="totalLayerNum"
          label="楼层总数"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="buildArea"
          label="建筑面积(m²)"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="certificationTime"
          label="发证日期"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDetail="true"
              @deleteClick="singleDetle(scope.row)"
              @updateClick="singleUpdate(scope.row)"
              @detailClick="singleDetail(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group @batchDeleteClick="batchDelete" @addClick="addUnit"></table-btn-group>
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
      width="1102px"
      @closed="dialogClosed"
      @open="dialogOpen"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{dialogTitle}}</span>
      </div>
      <el-form :inline="true" :model="Form" :rules="rules" ref="Form">
        <el-form-item
          :label="operateType==='detail'? '房产证号：' : '房产证号'"
          label-width="136px"
          prop="housePropertyCardNumber"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.housePropertyCardNumber"
            size="small"
            :readonly="operateType==='detail'"
            :placeholder="operateType!=='detail'?'请输入房产证号':''"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '坐落位置：' : '坐落位置'"
          label-width="136px"
          prop="location"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.location"
            size="small"
            :readonly="operateType==='detail'"
            :placeholder="operateType!=='detail'?'请输入坐落位置':''"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '所有权人：' : '所有权人'"
          label-width="136px"
          prop="owner"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.owner"
            size="small"
            :readonly="operateType==='detail'"
            :placeholder="operateType!=='detail'?'请输入所有权人':''"
            v-enter-trim
          ></el-input>
        </el-form-item>

        <el-form-item
          :label="operateType==='detail'? '房屋性质：' : '房屋性质'"
          label-width="136px"
          prop="houseCharacter"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.houseCharacter"
            size="small"
            :readonly="operateType==='detail'"
            :placeholder="operateType!=='detail'?'请输入房屋性质':''"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '规划用途：' : '规划用途'"
          label-width="136px"
          prop="planningPurpose"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.planningPurpose"
            :readonly="operateType==='detail'"
            size="small"
            :placeholder="operateType!=='detail'?'请输入规划用途':''"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '发证日期：' : '发证日期'"
          label-width="136px"
          prop="certificationTime"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-date-picker
            v-model="Form.certificationTime"
            type="date"
            placeholder="请选择发证日期"
            size="small"
            style="width:200px;"
            v-if="operateType!=='detail'"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
          <el-input
            v-model="Form.certificationTime"
            size="small"
            :readonly="operateType==='detail'"
            v-if="operateType==='detail'"
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '发证机关：' : '发证机关'"
          label-width="136px"
          prop="sendOffice"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.sendOffice"
            size="small"
            :readonly="operateType==='detail'"
            :placeholder="operateType!=='detail'?'请输入发证机关':''"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '建筑面积(m²)：' : '建筑面积(m²)'"
          label-width="136px"
          prop="buildArea"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.buildArea"
            size="small"
            :placeholder="operateType!=='detail'?'请输入建筑面积':''"
            @keyup.native="limitFloat('buildArea')"
            @blur="limitFloat('buildArea',true)"
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '楼栋/幢号：' : '楼栋/幢号'"
          label-width="136px"
          prop="address"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <tree-select
            v-show="operateType !=='detail'"
            @handleCheckChange="handleCheckChange"
            :treeData="treeData"
            :treeShow="treeShow"
            :inputWidth="'100%'"
            :inputHeight="'34px'"
            :treeName="treeName"
          ></tree-select>
          <el-tooltip class="item" effect="dark" :content="Address" placement="top">
            <span v-show="operateType =='detail'" class="detail-text">{{Address}}</span>
          </el-tooltip>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '楼层总数：' : '楼层总数'"
          label-width="136px"
          prop="totalLayerNum"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.totalLayerNum"
            size="small"
            :readonly="operateType==='detail'"
            :placeholder="operateType!=='detail'?'请输入楼层总数':''"
            @keyup.native="limitInt('totalLayerNum')"
            @blur="limitInt('totalLayerNum')"
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '共有情况：' : '共有情况'"
          label-width="136px"
          prop="common"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.common"
            size="small"
            :readonly="operateType==='detail'"
            :placeholder="operateType!=='detail'?'请输入共有情况':''"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '附记：' : '附记'"
          label-width="136px"
          prop="attachment"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            v-model="Form.attachment"
            :readonly="operateType==='detail'"
            size="small"
            :placeholder="operateType!=='detail'?'请输入附记':''"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '套内建筑面积(m²)：' : '套内建筑面积(m²)'"
          label-width="136px"
          prop="innerBuildArea"
          :class="{'detail-form-item' : operateType==='detail'}"
        >
          <el-input
            :readonly="operateType==='detail'"
            v-model="Form.innerBuildArea"
            size="small"
            :placeholder="operateType!=='detail'?'请输入套内建筑面积':''"
            @keyup.native="limitFloat('innerBuildArea' )"
            @blur="limitFloat('innerBuildArea',true)"
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '备注信息：' : '备注信息'"
          label-width="136px"
          prop="remark"
          :class="{'detail-form-item' : operateType==='detail'}"
          style="display: block;"
        >
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            :placeholder="operateType!=='detail'?'请输入备注信息':''"
            v-model="Form.remark"
            :readonly="operateType==='detail'"
            style="width:900px;margin-top:4px"
            v-enter-trim
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="operateType==='detail'? '证件下载：' : '上传证件图片'"
          label-width="136px"
          prop="addImageFlag"
          style="display: block;"
        >
          <div class="upload_box" v-show="operateType!=='detail'">
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
              :before-upload="beforeAvatarUpload"
              multiple
            >
              <i class="el-icon-plus"></i>
            </el-upload>
          </div>
          <div class="img-preview-box clearfix" v-show="operateType==='detail'">
            <div
              class="img-preview-item"
              v-for="(item, index) in detailFileList"
              :key="index"
              @click="preview(item.enclosureVirtualUrl)"
            >
              <img :src="getImgUrl(item.enclosureVirtualUrl)" alt="已上传证件" />
              <div class="bigger-view-warpper">
                <i class="bigger-view-icon"></i>
              </div>
              <el-checkbox @change="imgCheckboxChange($event, item.enclosurePhysicsUrl)"></el-checkbox>
            </div>
          </div>
          <el-button
            type="primary"
            icon="el-icon-download"
            class="download-btn"
            @click="downloadImg"
            v-show="detailFileList.length"
          >下载证件</el-button>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" v-show="operateType!=='detail'">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { BASE_URL, url_name_1 } from "@/axios/global";
import {
  getPropertyRightList,
  addPropertyRight,
  updatePropertyRight,
  getPropertyRightDetail,
  deletePropertyRight,
  batchDeletePropertyRight,
  download,
  getRightBuildTree
} from "./api";
import { getYardTree } from "../home/api.js";
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
import treeSelect from "@/components/treeSelect/treeSelect_3";
import moment from "moment";
import { loadavg } from "os";
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
    let validUpload = (rule, value, callback) => {
      if (!this.Form.addImageFlag) {
        callback(new Error("请先上传证件"));
      } else {
        callback();
      }
    };
    let validAddress = (rule, value, callback) => {
      if (this.operateType !== "detail") {
        if (!this.Form.address) {
          callback(new Error("楼栋/幢号不能为空"));
        } else {
          callback();
        }
      }
    };
    return {
      treeName: "houseCertificate",
      detailFileList: [],
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
      treeShow: true,
      treeData: [],
      Form: {
        id: "",
        propertyRightId: "",
        propertyType: 1,
        location: "",
        attachment: "",
        buildArea: "",
        certificationTime: "",
        common: "",
        houseCharacter: "",
        innerBuildArea: "",
        owner: "",
        planningPurpose: "",
        remark: "",
        sendOffice: "",
        totalLayerNum: "",
        housePropertyCardNumber: "",
        addImageFlag: "",
        addressList: [],
        address: "",
        handleId: null,
        handleModuleList: [],
        buildNum: []
      },
      deleteEnclosureIdList: [],
      downloadList: [],
      uploadImgNum: 0,
      uploadedImgNum: 0,
      uploadSuccessImgNum: 0,
      acceptImgType: "image/jpeg,image/gif,image/png,image/bmp",
      uploadImgUrl: `${uploadUrl}?uploadType=propertyRights`,
      token: "",
      postHeaders: {
        Authorization: ""
      },
      fileList: [],
      oldFileList: [],
      limitImgNum: 5,
      Address: "",
      rules: {
        housePropertyCardNumber: [
          { required: true, message: "房产证号不能为空", trigger: "blur" }
        ],
        location: [
          { required: true, message: "坐落位置不能为空", trigger: "blur" }
        ],
        owner: [
          { required: true, message: "所有权人不能为空", trigger: "blur" }
        ],
        houseCharacter: [
          { required: true, message: "房屋性质不能为空", trigger: "blur" }
        ],
        planningPurpose: [
          { required: true, message: "规划用途不能为空", trigger: "blur" }
        ],
        certificationTime: [
          { required: true, message: "发证日期不能为空", trigger: "blur" }
        ],
        sendOffice: [
          { required: true, message: "发证机关不能为空", trigger: "blur" }
        ],
        totalLayerNum: [
          { required: true, message: "楼层总数不能为空", trigger: "blur" }
        ],
        buildArea: [
          { required: true, message: "建筑面积不能为空", trigger: "blur" }
        ],
        address: [{ required: true, validator: validAddress, trigger: "blur" }],
        addImageFlag: [
          { required: true, validator: validUpload, trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getRightBuildTree();
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
          customClass: "loading-box"
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
      this.currentPage = 1;
      this.pageSize = size;
      this.getList();
    },
    // 改变页面页数
    changePage(page) {
      this.currentPage = page;
      this.getList();
    },
    // 选择树
    handleCheckChange(checkedNodes, halfCheckedNodes) {
      this.Form.handleModuleList = [];
      this.Form.buildNum = [];
      if (checkedNodes.length > 0) {
        checkedNodes.forEach(node => {
          if (node.type === 1) {
            const yardId = parseInt(node.id.split(":")[0]);
            const buildId = node.value;
            const buildName = node.name;
            const temp = {
              handleId: this.Form.handleId,
              yardId: yardId,
              buildId: buildId,
              buildName: buildName
            };
            this.Form.handleModuleList.push(temp);
            this.Form.buildNum.push(buildId);
          }
        });
      }
      if (this.Form.handleModuleList.length > 0) {
        this.Form.address = "true";
      } else {
        this.Form.address = "";
      }
    },
    // 获取新增树的数据
    getRightBuildTree() {
      getRightBuildTree()
        .then(res => {
          this.treeData = res;
          this.treeData.forEach(item => {
            if (item.type == 0) {
              item["disabled"] = true;
            }
            if (item.children !== undefined && item.children.length > 0) {
              item.children.forEach(obj => {
                delete obj.children;
              });
            }
          });
        })
        .catch(err => {
          const msg = err.result ? err.result : "产权楼栋树获取失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取信息列表
    getList() {
      this.loadingStart(".table-box");
      getPropertyRightList(this.currentPage, this.pageSize, 1)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach(item => {
            item.certificationTime = moment(item.certificationTime).format("YYYY-MM-DD");
            let nameList = [];
            item.handleModuleList.forEach(obj => {
              nameList.push(obj.buildName);
            })
            item.address = nameList.join(",");
          });
          this.loadingEnd();
        })
        .catch(err => {
          const msg = err.result ? err.result : "房产证信息获取失败";
          this.$message({
            message: msg,
            type: "error"
          });
          this.loadingEnd();
        });
    },
    // 查询信息
    search() {
      this.getList();
    },
    reset() {
      this.currentPage = 1;
      this.name = "";
      this.search();
    },
    // 新增
    addUnit() {
      this.dialogVisible = true;
      this.dialogTitle = "新增房产证信息";
      this.operateType = "add";
    },
    // 单个删除
    singleDetle(row) {
      this.$confirm(`此操作将永久删除房产证号为${row.housePropertyCardNumber}的房产证信息, 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deletePropertyRight(row._id)
            .then(res => {
              this.$message({
                message: "删除房产证信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : "删除房产证信息失败";
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
    // 批量删除
    batchDelete() {
      let deleteArray = [];
      this.selectData.forEach((item, index) => {
        deleteArray.push(item._id);
      });
      this.$confirm("此操作将永久删除这些房产证信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          if (deleteArray.length === 0) {
            this.$message({
              message: "请先勾选删除项！",
              type: "warning"
            });
            return false;
          }
          batchDeletePropertyRight(deleteArray)
            .then(res => {
              this.$message({
                message: "批量删除房产证信息成功!",
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
            })
            .catch(err => {
              const msg = err.result ? err.result : "批量删除房产证信息失败";
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
      this.Form._id = row._id;
      this.dialogVisible = true;
      this.dialogTitle = "修改房产证信息";
      this.operateType = "edit";
      getPropertyRightDetail(row._id)
        .then(res => {
          let nameList = [];
          let checkedIdList = [];
          let expandIdList = [];
          res.handleModuleList.forEach(item => {
            nameList.push(item.buildName);
            checkedIdList.push(`${item.yardId}:${item.buildId}`);
            if (expandIdList.indexOf(item.yardId) === -1) {
              expandIdList.push(item.yardId);
            }
          })
          eventBus.$emit(`${this.treeName}_select`, nameList, checkedIdList, expandIdList);
          this.Form.address = "true";
          for ([key, value] of Object.entries(res)) {
            if (this.Form[key] !== undefined) this.Form[key] = value;
          }
          if (res.addressList && res.addressList.length > 0) {
            let imgNum = 0;
            res.addressList.forEach((item, index) => {
              imgNum++;
              item["name"] = item.enclosurePhysicsUrl
                .split(".")[0]
                .substr(
                  item.enclosurePhysicsUrl.split(".")[0].lastIndexOf("/") + 1
                );
              const ext = item.enclosurePhysicsUrl.split(".")[1];
              if (ext === "rar" || ext === "zip") {
                item.enclosureVirtualUrl = "/images/compress.png";
              }
              const obj = {
                name: item["name"],
                url:
                  BASE_URL +
                  url_name_1 +
                  item.enclosureVirtualUrl +
                  "?access_token=" +
                  this.token,
                physics: item.enclosurePhysicsUrl
              };
              this.fileList.push(obj);
              this.oldFileList.push(obj);
            });
            this.uploadedImgNum = imgNum;
          }
          this.uploadedImgNum = res.addressList.length;
          if (this.uploadedImgNum > 0) {
            this.Form.addImageFlag = "true";
          }
          this.Form.addressList = [];
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取房产证信息失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 单个详情
    singleDetail(row) {
      this.dialogTitle = "房产证信息详情";
      this.operateType = "detail";
      getPropertyRightDetail(row._id)
        .then(res => {
          this.dialogVisible = true;
          res.certificationTime = moment(res.certificationTime).format("YYYY-MM-DD");
          for ([key, value] of Object.entries(res)) {
            if (this.Form[key] !== undefined) {
              this.Form[key] = value;
            }
          }
          let nameList = [];
          res.handleModuleList.forEach(item => {
            nameList.push(item.buildName);
          })
          this.Address = nameList.join(",");
          if (res.addressList.length > 0) {
            res.addressList.forEach((item, index) => {
              item["name"] = item.enclosurePhysicsUrl
                .split(".")[0]
                .substr(
                  item.enclosurePhysicsUrl.split(".")[0].lastIndexOf("/") + 1
                );
              const ext = item.enclosurePhysicsUrl.split(".")[1];
              if (ext === "rar" || ext === "zip") {
                item.enclosureVirtualUrl = "/images/compress.png";
              }
              this.detailFileList.push(item);
            });
          }
          this.Form.id = row._id;
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取房产证信息详情失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.uploadImgNum === 0 ? this.saveForm() : this.submitUpload();
        } else {
          return false;
        }
      });
    },
    // 保存表单
    saveForm() {
      if (this.operateType === "add") {
        if (this.Form._id !== undefined) delete this.Form._id;
        addPropertyRight(this.Form)
          .then(res => {
            this.getList();
            this.$message({
              message: "房产证信息新增成功!",
              type: "success"
            });
            this.dialogVisible = false;
          })
          .catch(err => {
            const msg = err.result ? err.result : "房产证信息新增失败";
            this.$message({
              message: msg,
              type: "error"
            });
            this.fileList = this.oldFileList;
            this.uploadImgNum = 0;
            this.uploadSuccessImgNum = 0;
          });
      } else if (this.operateType === "edit") {
        updatePropertyRight(this.Form, this.deleteEnclosureIdList)
          .then(res => {
            this.getList();
            this.$message({
              message: "房产证信息修改成功!",
              type: "success"
            });
            this.dialogVisible = false;
          })
          .catch(err => {
            const msg = err.result ? err.result : "房产证信息修改失败";
            this.$message({
              message: msg,
              type: "error"
            });
            this.fileList = this.oldFileList;
            this.uploadImgNum = 0;
            this.uploadSuccessImgNum = 0;
          });
      }
    },
    // 文件上传之前
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 200;
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 200MB!");
      }
      return isLt2M;
    },
    // 开始上传文件
    submitUpload() {
      this.$refs.upload.submit();
    },
    // 删除上传文件时钩子
    handleRemove(file, fileList) {
      if (file.status === "success") {
        this.deleteEnclosureIdList.push(file.physics);
        this.uploadedImgNum--;
      }
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
      if (fileList.length === 0) {
        this.Form.addImageFlag = "";
      }
    },
    // 上传文件成功时钩子
    handleSuccess(response, file, fileList) {
      this.uploadSuccessImgNum++;
      this.Form.addressList.push({
        enclosureType: 1, // 1图片 2文件
        enclosurePhysicsUrl: response.imgList[0].imgPath,
        enclosureVirtualUrl: response.imgList[0].imgUrl
      });
      if (this.uploadSuccessImgNum === this.uploadImgNum) {
        this.saveForm();
      }
    },
    // 上传文件改变时钩子
    handleChange(file, fileList) {
      const fileType = file.raw.type.split("/")[0];
      if (fileType !== "image") {
        file.url = `${BASE_URL}${url_name_1}/images/compress.png`;
      }
      if (fileList.length > 0) {
        this.Form.addImageFlag = "true";
      }
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
    },
    // 上传文件超出限制个数
    handleExceed(file, fileList) {
      this.$message.warning(`最多只能上传五张图片！`);
    },
    // 上传文件失败
    handleError(err, file, fileList) {
      if (err.status === 401) {
        this.$store.dispatch("Logout", this.token);
        this.$router.push({ path: "/login" });
      }
      this.$message({
        message: err.message,
        type: "error"
      });
    },
    // dialog打开的回调
    dialogOpen() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
      });
    },
    // dialog关闭的回调
    dialogClosed() {
      eventBus.$emit(`${this.treeName}_clearInput`);
      eventBus.$emit(`${this.treeName}_clear`);
      this.clearForm(this.Form);
      this.Form.propertyType = 1;
      this.deleteEnclosureIdList = [];
      this.fileList = [];
      this.oldFileList = [];
      this.detailFileList = [];
      this.downloadList = [];
      this.uploadImgNum = 0;
      this.uploadedImgNum = 0;
      this.uploadSuccessImgNum = 0;
      this.Address = "";
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
    // 获取数据类型
    getDataType(obj) {
      var typeArr = ["String", "Object", "Number", "Array", "Function"];
      for (let name of typeArr) {
        if (Object.prototype.toString.call(obj) === `[object ${name}]`) {
          return name;
        }
      }
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
    // 只能输入正整数
    limitInt(key) {
      this.Form[key] = this.Form[key].replace(/[^0-9]/g, "");
    },
    // 下载图片
    downloadImg() {
      if (this.downloadList.length > 0) {
        download(this.downloadList, 1, this.Form.id)
          .then(res => {
            let fileDownload = require("js-file-download");
            let name = decodeURIComponent(
              sessionStorage.getItem("CADdownloadFileName")
            );
            fileDownload(res, name);
          })
          .catch(err => {
            const msg = err.result ? err.result : "证件下载失败";
            this.$message({
              message: msg,
              type: "error"
            });
          });
      } else {
        this.$message({
          message: "请先选中一个证件",
          type: "warning"
        });
      }
    },
    // 获取图片链接
    getImgUrl(url) {
      return `${BASE_URL}${url_name_1}${url}?access_token=${this.token}`;
    },
    // 图片多选框选择
    imgCheckboxChange($event, id) {
      if ($event) {
        if (this.downloadList.indexOf(id) === -1) {
          this.downloadList.push(id);
        }
      } else {
        const index = this.downloadList.indexOf(id);
        this.downloadList.splice(index, 1);
      }
    },
    // 预览
    preview(url) {
      $(".img-preview-bigger").css("display", "block");
      const img = $("#biggerImg");
      img.attr(
        "src",
        `${BASE_URL}${url_name_1}${url}?access_token=${this.token}`
      );
    },
    // 关闭预览
    closePreview() {
      $(".img-preview-bigger").css("display", "none");
      const img = $("#biggerImg");
      img.css("width", "auto");
    }
  },
  mounted() {
    this.getList();
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - operateBoxH;
    }, 1);
    this.token = sessionStorage.getItem("TOKEN");
    if (this.token) {
      this.postHeaders.Authorization = `Bearer ${this.token}`;
    }
  }
};
</script>

<style>
#houseCertificate .query-box {
  margin-bottom: 20px;
}
#houseCertificate .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#houseCertificate .query-item > div {
  float: left;
}
#houseCertificate .query-item > .query-input {
  width: calc(100% - 80px);
}
#houseCertificate .query-item .el-date-editor.el-input,
#houseCertificate .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#houseCertificate .query-btn {
  float: left;
  margin-left: 50px;
}
#houseCertificate .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#houseCertificate .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#houseCertificate .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#houseCertificate .detail-form-item .el-input__inner {
  border: 0;
  padding-left: 0;
  width: 210px;
}
#houseCertificate .detail-form-item .el-textarea__inner {
  border: 0;
  padding-left: 0;
  width: 550px;
}
#houseCertificate .detail-form-item {
  margin-right: 0;
}
#houseCertificate .img-preview-item {
  float: left;
  margin-right: 20px;
  margin-bottom: 15px;
  width: 100px;
  height: 100px;
  border: 1px dashed #c0ccda;
  position: relative;
}
#houseCertificate .img-preview-item > img {
  width: 100%;
  height: 100%;
}
#houseCertificate .img-preview-item > .bigger-view-warpper {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}
#houseCertificate
  .img-preview-item
  > .bigger-view-warpper
  > .bigger-view-warpper-inner {
  width: 100%;
  height: 100%;
  background: #000;
  opacity: 0.5;
  display: none;
}
#houseCertificate
  .img-preview-item
  > .bigger-view-warpper:hover
  > .bigger-view-warpper-inner {
  display: block;
}
#houseCertificate .img-preview-item > .bigger-view-warpper > i {
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
#houseCertificate .img-preview-item > .bigger-view-warpper > i:hover {
  color: #3891e0;
}
#houseCertificate .img-preview-item > .bigger-view-warpper:hover > i {
  display: block;
}
#houseCertificate .img-preview-item > .el-checkbox {
  position: absolute;
  bottom: -13px;
  right: 0;
}
#houseCertificate .download-btn {
  padding: 7px 22px;
}
#houseCertificate .el-upload-list--picture-card .el-upload-list__item,
#houseCertificate .el-upload--picture-card {
  width: 100px;
  height: 100px;
  line-height: 100px;
}
#houseCertificate .detail-text {
  display: inline-block;
  width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #606266;
}
</style>


