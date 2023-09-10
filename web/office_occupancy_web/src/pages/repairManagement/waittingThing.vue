<template>
  <div id="waittingThing">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">申请时间：</div>
          <div class="query-input">
            <el-date-picker
              v-model="startApplicationTime"
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
              v-model="endApplicationTime"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd 23:59:59"
              :picker-options="pickerOptions_end"
            ></el-date-picker>
          </div>
        </div>
        <div class="query-item" style="margin-left: 50px;">
          <div class="query-title">申请编号：</div>
          <div class="query-input">
            <el-input v-model="repairApplyNo" placeholder="请输入申请编号"></el-input>
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
        <el-table-column
          prop="serialNum"
          label="序号"
          width="50"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="repairApplyNo"
          label="申请编号"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="repairApplicant"
          label="申请人"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="repairApplyDate"
          label="申请时间"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="repairModuleArea"
          label="申请面积(m²)"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <div class="table-operate" @click="openDialog(scope.row)">
              <span class="iconfont">&#xe695;</span>
              <span>{{scope.row.stepText}}</span>
            </div>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group :batchDelBtnShow="false" :addBtnShow="false"></table-btn-group>
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

    <!-- 科室审批 -->
    <el-dialog
      :visible.sync="dialogVisible.officeApprove"
      @open="dialogOpen"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
      class="officeApprove"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <repair-info
        :showRepairModuleInfo="showRepairModuleInfo"
        :editing="editing"
        :showEditBtn="true"
        :showDeleteBtn="true"
        :showMoreBtn="true"
        :activeInfo="activeInfo"
        :baseInfo="baseInfo"
        :repairInfoList="repairInfoList"
        :repairInfoDetail="repairInfoDetail"
        @editRepairModuleArea="editRepairModuleArea"
        @showInfoDetail="showInfoDetail"
        @deleteRepairModule="deleteRepairModule"
      ></repair-info>
      <div class="repair-records-box" v-if="showRepairRecord">
        <div class="info-detail-title clearfix">
          <span>维修记录：</span>
          <div class="info-detail-btn">
            <el-popover
              v-model="popoverRepairRecords"
              placement="bottom-end"
              width="300"
              trigger="click"
              popper-class="more-info"
              :visible-arrow="false"
            >
              <ul class="repair-info-more">
                <li
                  v-for="(item, index) in repairRecordList"
                  :key="index"
                  :class="{'info-active': activeRecord === index}"
                  @click="showRecordDetail(index)"
                >{{item.positionStr}}</li>
              </ul>
              <span class="more-btn" slot="reference">
                更多
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-popover>
          </div>
        </div>
        <div class="info-detail-content">
          <div v-show="repairRecordDetail.positionStr">
            <span>维修地点</span>：
            <span>{{repairRecordDetail.positionStr}}</span>
          </div>
          <div v-show="repairRecordDetail.repairPartName">
            <span>维修部位</span>：
            <span>{{repairRecordDetail.repairPartName}}</span>
          </div>
          <div v-show="repairRecordDetail.repairModulePosition">
            <span>维修位置</span>：
            <span>{{repairRecordDetail.repairModulePosition}}</span>
          </div>
          <div v-show="repairRecordDetail.completionTime">
            <span>维修时间</span>：
            <span>{{repairRecordDetail.completionTime}}</span>
          </div>
          <div v-show="repairRecordDetail.repairUnitNameList">
            <span>维修单位</span>：
            <span>{{repairRecordDetail.repairUnitNameList}}</span>
          </div>
          <div v-show="repairRecordDetail.contactPhone">
            <span>联系电话</span>：
            <span>{{repairRecordDetail.contactPhone}}</span>
          </div>
          <div v-show="repairRecordDetail.completionQuality">
            <span>保质期</span>：
            <span>{{repairRecordDetail.completionQuality}}</span>
          </div>
        </div>
      </div>
      <div class="repair-approve-box">
        <el-form :model="Form_officeApprove" :rules="rules_officeApprove" ref="Form_officeApprove">
          <el-form-item label="审批：" label-width="115px" prop="auditState">
            <el-radio
              v-model="Form_officeApprove.auditState"
              :label="1"
              v-show="showRepairModuleInfo"
              @change="auditStateRadioChange"
              name="approve"
            >实施专项维修</el-radio>
            <el-radio
              v-model="Form_officeApprove.auditState"
              :label="3"
              v-show="showRepairModuleInfo"
              v-if="false"
              @change="auditStateRadioChange"
              name="approve"
            >实施零星维修</el-radio>
            <el-radio
              v-model="Form_officeApprove.auditState"
              :label="2"
              :style="showRepairModuleInfo? 'margin-left :20px' : 'margin-left :0px'"
              @change="auditStateRadioChange"
              name="approve"
            >不同意维修</el-radio>
          </el-form-item>
          <el-form-item
            label="责任人："
            label-width="115px"
            prop="responsiblePerson"
            v-if="Form_officeApprove.auditState === 3"
          >
            <el-input
              size="small"
              placeholder="请输入责任人"
              v-model="Form_officeApprove.responsiblePerson"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="施工单位："
            label-width="115px"
            prop="repairUnitName"
            v-if="Form_officeApprove.auditState === 3"
          >
            <el-input
              size="small"
              placeholder="请输入施工单位"
              v-model="Form_officeApprove.repairUnitName"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="采购名称："
            label-width="115px"
            prop="repairHttpResultVo.common_name"
            v-if="Form_officeApprove.auditState === 3"
          >
            <el-input
              size="small"
              placeholder="请输入采购名称"
              v-model="Form_officeApprove.repairHttpResultVo.common_name"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="采购单价："
            label-width="115px"
            prop="repairHttpResultVo.common_price"
            v-if="Form_officeApprove.auditState === 3"
          >
            <el-input
              size="small"
              placeholder="请输入采购单价(每平米的价格)"
              v-model="Form_officeApprove.repairHttpResultVo.common_price"
              @keyup.native="limitFloat_2('common_price')"
              @blur="limitFloat_2('common_price', true)"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="采购数量："
            label-width="115px"
            prop="repairHttpResultVo.common_quantity"
            v-if="Form_officeApprove.auditState === 3"
          >
            <el-input
              size="small"
              placeholder="请输入采购数量"
              v-model="Form_officeApprove.repairHttpResultVo.common_quantity"
              @keyup.native="limitInt('common_quantity', 'repairHttpResultVo')"
              @blur="limitInt('common_quantity', 'repairHttpResultVo')"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="预算事项："
            label-width="115px"
            prop="repairHttpResultVo.detailid"
            v-if="Form_officeApprove.auditState === 3"
          >
            <custom-select
              name="officeApprove"
              inputWidth="100%"
              placeholderText="请选择预算事项"
              :inputText="inputText"
              :common_quantity="Form_officeApprove.repairHttpResultVo.common_quantity"
              :common_price="Form_officeApprove.repairHttpResultVo.common_price"
              @getSectionList="getSectionList"
            >
              <el-table
                ref="singleTable"
                :data="officeTableData"
                style="width: 100%"
                :height="200"
                :header-cell-style="tableHeaderCellStyle"
                :cell-style="tableCellStyle"
                @row-click="tableRowClick"
              >
                <el-table-column width="50">
                  <template slot-scope="scope">
                    <el-radio
                      v-model="Form_officeApprove.repairHttpResultVo.detailid"
                      :label="scope.row.id"
                    >&thinsp;</el-radio>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="title"
                  label="预算项目名称"
                  align="center"
                  :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                  prop="deptid"
                  label="预算科室名称"
                  align="center"
                  :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                  prop="approval_amount"
                  label="预算批复金额"
                  align="center"
                  :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                  prop="used_proportion"
                  label="执行比例"
                  align="center"
                  :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                  prop="balance_proportion"
                  label="余额占比"
                  align="center"
                  :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                  prop="adjust_amount_in"
                  label="调整金额(调入)"
                  align="center"
                  :width="120"
                  :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                  prop="adjust_amount_out"
                  label="调整金额(调出)"
                  align="center"
                  :width="120"
                  :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                  prop="balance_amount"
                  label="可用余额"
                  align="center"
                  :show-overflow-tooltip="true"
                ></el-table-column>
              </el-table>
            </custom-select>
          </el-form-item>
          <el-form-item
            label="事项说明："
            label-width="115px"
            prop="repairHttpResultVo.description"
            v-if="Form_officeApprove.auditState === 3"
          >
            <el-input
              v-model="Form_officeApprove.repairHttpResultVo.description"
              type="textarea"
              :rows="4"
              placeholder="请输入事项说明(例如：维修地点，维修部位，维修内容或项目简介)"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="简述："
            label-width="115px"
            prop="repairHttpResultVo.title"
            v-if="Form_officeApprove.auditState === 3"
          >
            <el-input
              v-model="Form_officeApprove.repairHttpResultVo.title"
              type="textarea"
              :rows="4"
              placeholder="请输入简述(维修内容)"
              class="approve-advices-textarea"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="审批意见："
            label-width="115px"
            prop="comment"
            v-if="Form_officeApprove.auditState !== 3"
          >
            <el-input
              v-model="Form_officeApprove.comment"
              type="textarea"
              :rows="4"
              placeholder="请输入审批意见"
              class="approve-advices-textarea"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.officeApprove = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm_noUpload(`Form_officeApprove`)" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /科室审批 -->
    <!-- 审批意见 -->
    <el-dialog
      :visible.sync="dialogVisible.partyOrganizationApprove"
      @open="dialogOpen"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <repair-info
        :showRepairModuleInfo="showRepairModuleInfo"
        :editing="editing"
        :showEditBtn="false"
        :showDeleteBtn="false"
        :showMoreBtn="true"
        :activeInfo="activeInfo"
        :baseInfo="baseInfo"
        :repairInfoList="repairInfoList"
        :repairInfoDetail="repairInfoDetail"
        @editRepairModuleArea="editRepairModuleArea"
        @showInfoDetail="showInfoDetail"
        @deleteRepairModule="deleteRepairModule"
      ></repair-info>
      <div class="repair-approve-box">
        <el-form
          :model="Form_partyOrganizationApprove"
          :rules="rules_partyOrganizationApprove"
          ref="Form_partyOrganizationApprove"
        >
          <el-form-item label="审批：" label-width="105px" prop="auditState">
            <el-radio
              v-model="Form_partyOrganizationApprove.auditState"
              :label="1"
              v-show="showRepairModuleInfo"
              @change="auditStateRadioChange"
              name="approve"
            >申请单位自费修理</el-radio>
            <el-radio
              v-model="Form_partyOrganizationApprove.auditState"
              :label="3"
              v-show="showRepairModuleInfo"
              v-if="false"
              @change="auditStateRadioChange"
              name="approve"
            >机关事务服务中心经费维修</el-radio>
            <el-radio
              v-model="Form_partyOrganizationApprove.auditState"
              :label="2"
              :style="showRepairModuleInfo? 'margin-left :20px' : 'margin-left :0px'"
              @change="auditStateRadioChange"
              name="approve"
            >不同意维修</el-radio>
          </el-form-item>
          <el-form-item label="审批意见：" label-width="105px" prop="comment">
            <el-input
              v-model="Form_partyOrganizationApprove.comment"
              type="textarea"
              :rows="4"
              placeholder="请输入审批意见"
              class="approve-advices-textarea"
            ></el-input>
          </el-form-item>
          <el-form-item label="审批原件：" label-width="105px" prop="repairApprovalDataList">
            <div class="repaire-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=partyWill`"
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
                <div
                  slot="tip"
                  class="el-upload__tip"
                  v-show="fileList.length > 0"
                >已选中{{fileList.length}}个文件</div>
              </el-upload>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.partyOrganizationApprove = false" :fontSize="14"></cancel-btn>
        <confirm-btn
          @clickBtn="submitForm_hasUpload('Form_partyOrganizationApprove')"
          :fontSize="14"
        ></confirm-btn>
      </span>
    </el-dialog>
    <!-- /审批意见 -->

    <!-- 自费维修上传 -->
    <el-dialog
      :visible.sync="dialogVisible.selfPayingRepairUpload"
      @open="dialogOpen"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <repair-info
        :showRepairModuleInfo="showRepairModuleInfo"
        :editing="editing"
        :showEditBtn="false"
        :showDeleteBtn="false"
        :showMoreBtn="true"
        :activeInfo="activeInfo"
        :baseInfo="baseInfo"
        :repairInfoList="repairInfoList"
        :repairInfoDetail="repairInfoDetail"
        @editRepairModuleArea="editRepairModuleArea"
        @showInfoDetail="showInfoDetail"
        @deleteRepairModule="deleteRepairModule"
      ></repair-info>
      <div class="repair-approve-box">
        <el-form
          :model="Form_selfPayingRepairUpload"
          :rules="rules_selfPayingRepairUpload"
          ref="Form_selfPayingRepairUpload"
        >
          <el-form-item label="领导审批意见：" label-width="125px" v-if="commentInfo">
            <div class="comment-info">{{commentInfo}}</div>
          </el-form-item>
          <el-form-item label="上传文件：" label-width="105px" prop="repairApprovalDataList">
            <div class="repaire-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=repairPlan`"
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
                <div
                  slot="tip"
                  class="el-upload__tip"
                  v-show="fileList.length > 0"
                >已选中{{fileList.length}}个文件</div>
              </el-upload>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.selfPayingRepairUpload = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm_hasUpload('Form_selfPayingRepairUpload')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /部门维修上传 -->

    <!-- 部门维修审批 -->
    <el-dialog
      :visible.sync="dialogVisible.selfPayingRepairApprove"
      @open="dialogOpen"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <repair-info
        :showRepairModuleInfo="showRepairModuleInfo"
        :editing="editing"
        :showEditBtn="false"
        :showDeleteBtn="false"
        :showMoreBtn="true"
        :activeInfo="activeInfo"
        :baseInfo="baseInfo"
        :repairInfoList="repairInfoList"
        :repairInfoDetail="repairInfoDetail"
        @editRepairModuleArea="editRepairModuleArea"
        @showInfoDetail="showInfoDetail"
        @deleteRepairModule="deleteRepairModule"
      ></repair-info>
      <div class="repair-approve-box">
        <el-form
          :model="Form_selfPayingRepairApprove"
          :rules="rules_selfPayingRepairApprove"
          ref="Form_selfPayingRepairApprove"
        >
          <el-form-item label="文件下载：" label-width="105px">
            <div class="repaire-file-download">
              <el-checkbox-group
                v-model="fileDownLoadCheckedList"
                @change="handleCheckedFileChange"
              >
                <div v-for="(file, index) in downLoadList" :key="index">
                  <el-checkbox :label="file.physicalAddress">{{file.repairFileName}}</el-checkbox>
                </div>
              </el-checkbox-group>
              <el-button
                size="small"
                type="primary"
                @click="fileDownLoad(downloadCheckedList, 5)"
              >点击下载文件</el-button>
            </div>
          </el-form-item>
          <el-form-item label="审批：" label-width="105px" prop="auditState">
            <el-radio
              v-model="Form_selfPayingRepairApprove.auditState"
              :label="1"
              v-show="showRepairModuleInfo"
              @change="auditStateRadioChange"
              name="approve"
            >维修方案合格</el-radio>
            <el-radio
              v-model="Form_selfPayingRepairApprove.auditState"
              :label="2"
              :style="showRepairModuleInfo? 'margin-left :20px' : 'margin-left :0px'"
              @change="auditStateRadioChange"
              name="approve"
            >维修方案不合格</el-radio>
          </el-form-item>
          <el-form-item label="审批意见：" label-width="105px" prop="comment">
            <el-input
              v-model="Form_selfPayingRepairApprove.comment"
              type="textarea"
              :rows="4"
              placeholder="请输入审批意见"
              class="approve-advices-textarea"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.selfPayingRepairApprove = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm_noUpload('Form_selfPayingRepairApprove')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /部门维修审批 -->

    <!-- 预算维修上传 -->
    <el-dialog
      :visible.sync="dialogVisible.budgetRepairUpload"
      @open="dialogOpen"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <repair-info
        :showRepairModuleInfo="showRepairModuleInfo"
        :editing="editing"
        :showEditBtn="false"
        :showDeleteBtn="false"
        :showMoreBtn="true"
        :activeInfo="activeInfo"
        :baseInfo="baseInfo"
        :repairInfoList="repairInfoList"
        :repairInfoDetail="repairInfoDetail"
        @editRepairModuleArea="editRepairModuleArea"
        @showInfoDetail="showInfoDetail"
        @deleteRepairModule="deleteRepairModule"
      ></repair-info>
      <div class="repair-approve-box">
        <el-form
          :model="Form_budgetRepairUpload"
          :rules="rules_budgetRepairUpload"
          ref="Form_budgetRepairUpload"
        >
          <el-form-item label="领导审批意见：" label-width="125px" v-if="commentInfo">
            <div class="comment-info">{{commentInfo}}</div>
          </el-form-item>
          <el-form-item label="上传文件：" label-width="105px" prop="repairApprovalDataList">
            <div class="repaire-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=budgetPlan`"
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
                <div
                  slot="tip"
                  class="el-upload__tip"
                  v-show="fileList.length > 0"
                >已选中{{fileList.length}}个文件</div>
              </el-upload>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.budgetRepairUpload = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm_hasUpload('Form_budgetRepairUpload')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /预算维修上传 -->

    <!-- 预算维修审批 -->
    <el-dialog
      :visible.sync="dialogVisible.budgetRepairApprove"
      @open="dialogOpen"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <repair-info
        :showRepairModuleInfo="showRepairModuleInfo"
        :editing="editing"
        :showEditBtn="false"
        :showDeleteBtn="false"
        :showMoreBtn="true"
        :activeInfo="activeInfo"
        :baseInfo="baseInfo"
        :repairInfoList="repairInfoList"
        :repairInfoDetail="repairInfoDetail"
        @editRepairModuleArea="editRepairModuleArea"
        @showInfoDetail="showInfoDetail"
        @deleteRepairModule="deleteRepairModule"
      ></repair-info>
      <div class="repair-approve-box">
        <el-form
          :model="Form_budgetRepairApprove"
          :rules="rules_budgetRepairApprove"
          ref="Form_budgetRepairApprove"
        >
          <el-form-item label="文件下载：" label-width="105px">
            <div class="repaire-file-download">
              <el-checkbox-group
                v-model="fileDownLoadCheckedList"
                @change="handleCheckedFileChange"
              >
                <div v-for="(file, index) in downLoadList" :key="index">
                  <el-checkbox :label="file.physicalAddress">{{file.repairFileName}}</el-checkbox>
                </div>
              </el-checkbox-group>
              <el-button
                size="small"
                type="primary"
                @click="fileDownLoad(downloadCheckedList, 19)"
              >点击下载文件</el-button>
            </div>
          </el-form-item>
          <el-form-item label="审批：" label-width="105px" prop="auditState">
            <el-radio
              v-model="Form_budgetRepairApprove.auditState"
              :label="1"
              v-show="showRepairModuleInfo"
              @change="auditStateRadioChange"
              name="approve"
            >预算方案合格</el-radio>
            <el-radio
              v-model="Form_budgetRepairApprove.auditState"
              :label="2"
              :style="showRepairModuleInfo? 'margin-left :20px' : 'margin-left :0px'"
              @change="auditStateRadioChange"
              name="approve"
            >预算方案不合格</el-radio>
          </el-form-item>
          <el-form-item label="审批意见：" label-width="105px" prop="comment">
            <el-input
              v-model="Form_budgetRepairApprove.comment"
              type="textarea"
              :rows="4"
              placeholder="请输入审批意见"
              class="approve-advices-textarea"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.budgetRepairApprove = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm_noUpload('Form_budgetRepairApprove')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /预算维修审批 -->

    <!-- 可行性研究 -->
    <el-dialog
      :visible.sync="dialogVisible.feasibilityApprove"
      @open="dialogOpen"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <repair-info
        :showRepairModuleInfo="showRepairModuleInfo"
        :editing="editing"
        :showEditBtn="false"
        :showDeleteBtn="false"
        :showMoreBtn="true"
        :activeInfo="activeInfo"
        :baseInfo="baseInfo"
        :repairInfoList="repairInfoList"
        :repairInfoDetail="repairInfoDetail"
        @editRepairModuleArea="editRepairModuleArea"
        @showInfoDetail="showInfoDetail"
        @deleteRepairModule="deleteRepairModule"
      ></repair-info>
      <div class="repair-approve-box">
        <el-form
          :inline="true"
          :model="Form_feasibilityApprove"
          ref="Form_feasibilityApprove"
          :rules="rules_feasibilityApprove"
        >
          <el-form-item label="领导审批意见：" label-width="125px" v-if="commentInfo" style="width:100%">
            <div class="comment-info">{{commentInfo}}</div>
          </el-form-item>
          <el-form-item label="项目名称：" label-width="105px" prop="repairProject" style="width: 33%">
            <el-input
              v-model="Form_feasibilityApprove.repairProject"
              size="small"
              placeholder="请输入项目名称"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="维修概算(元)："
            label-width="130px"
            prop="budgetEstimate"
            style="width: 33%"
          >
            <el-input
              v-model="Form_feasibilityApprove.budgetEstimate"
              size="small"
              placeholder="请输入维修概算"
              @keyup.native="limitFloat('budgetEstimate')"
              @blur="limitFloat('budgetEstimate', true)"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="单位面积价格(元)："
            label-width="151px"
            prop="unitAreaPrice"
            style="width: 30%"
          >
            <span>{{Form_feasibilityApprove.unitAreaPrice}}</span>
          </el-form-item>
          <el-form-item
            label="项目简介："
            label-width="105px"
            prop="projectIntroduction"
            style="width: 100%"
            class="project-introduction"
          >
            <el-input
              v-model="Form_feasibilityApprove.basicDBObject.projectIntroduction"
              type="textarea"
              :rows="4"
              placeholder="请输入项目简介"
              class="approve-advices-textarea"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="可行性报告："
            label-width="105px"
            style="width: 100%"
            prop="repairApprovalDataList"
          >
            <div class="repaire-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=feasibilityStudy`"
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
                <div
                  slot="tip"
                  class="el-upload__tip"
                  v-show="fileList.length > 0"
                >已选中{{fileList.length}}个文件</div>
              </el-upload>
            </div>
          </el-form-item>
          <el-form-item
            label="平面图或效果图："
            label-width="105px"
            style="width: 100%"
            prop="repairApprovalDataList"
          >
            <div class="repaire-img-upload">
              <el-upload
                ref="uploadImg"
                list-type="picture-card"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=feasibilityStudy`"
                :file-list="fileListImg"
                :auto-upload="false"
                :accept="acceptImgType"
                :limit="limit"
                :on-success="handleImgSuccess"
                :on-remove="handleImgRemove"
                :on-change="handleImgChange"
                :on-error="handleError"
                :on-exceed="handleImgExceed"
                :before-upload="beforeAvatarUpload"
                multiple
              >
                <i class="el-icon-plus"></i>
              </el-upload>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.feasibilityApprove = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm_hasUpload('Form_feasibilityApprove')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /可行性研究 -->

    <!-- 领导审批 -->
    <el-dialog
      :visible.sync="dialogVisible.leaderApprove"
      @open="dialogOpen"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <repair-info
        :showRepairModuleInfo="showRepairModuleInfo"
        :editing="editing"
        :showEditBtn="false"
        :showDeleteBtn="false"
        :showMoreBtn="true"
        :activeInfo="activeInfo"
        :baseInfo="baseInfo"
        :repairInfoList="repairInfoList"
        :repairInfoDetail="repairInfoDetail"
        @editRepairModuleArea="editRepairModuleArea"
        @showInfoDetail="showInfoDetail"
        @deleteRepairModule="deleteRepairModule"
      ></repair-info>
      <div class="repair-approve-box">
        <el-form :model="Form_leaderApprove" :rules="rules_leaderApprove" ref="Form_leaderApprove">
          <el-form-item label="审批：" label-width="105px" prop="auditState">
            <el-radio
              v-model="Form_leaderApprove.auditState"
              :label="1"
              v-show="showRepairModuleInfo"
              @change="auditStateRadioChange"
              name="approve"
            >同意</el-radio>
            <el-radio
              v-model="Form_leaderApprove.auditState"
              :label="2"
              v-show="showRepairModuleInfo"
              @change="auditStateRadioChange"
              name="approve"
            >不同意</el-radio>
            <el-radio
              v-model="Form_leaderApprove.auditState"
              :label="3"
              v-show="showRepairModuleInfo"
              @change="auditStateRadioChange"
              name="approve"
            >不同意修改预算方案</el-radio>
          </el-form-item>
          <el-form-item label="审批意见：" label-width="105px" prop="comment">
            <el-input
              v-model="Form_leaderApprove.comment"
              type="textarea"
              :rows="4"
              placeholder="请输入审批意见"
              class="approve-advices-textarea"
            ></el-input>
          </el-form-item>
          <el-form-item label="上传文件：" label-width="105px" prop="repairApprovalDataList">
            <div class="repaire-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=leaderFile`"
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
                <div
                  slot="tip"
                  class="el-upload__tip"
                  v-show="fileList.length > 0"
                >已选中{{fileList.length}}个文件</div>
              </el-upload>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.leaderApprove = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm_hasUpload('Form_leaderApprove')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /领导审批 -->

    <!-- 发改委审批 -->
    <el-dialog
      :visible.sync="dialogVisible.governmentApprove"
      @open="dialogOpen"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <repair-info
        :showRepairModuleInfo="showRepairModuleInfo"
        :editing="editing"
        :showEditBtn="false"
        :showDeleteBtn="false"
        :showMoreBtn="true"
        :activeInfo="activeInfo"
        :baseInfo="baseInfo"
        :repairInfoList="repairInfoList"
        :repairInfoDetail="repairInfoDetail"
        @editRepairModuleArea="editRepairModuleArea"
        @showInfoDetail="showInfoDetail"
        @deleteRepairModule="deleteRepairModule"
      ></repair-info>
      <div class="repair-approve-box">
        <el-form
          :model="Form_governmentApprove"
          :rules="rules_governmentApprove"
          ref="Form_governmentApprove"
        >
          <el-form-item label="审批：" label-width="105px" prop="auditState">
            <el-radio
              v-model="Form_governmentApprove.auditState"
              :label="1"
              v-show="showRepairModuleInfo"
              @change="auditStateRadioChange"
              name="approve"
            >同意维修</el-radio>
            <el-radio
              v-model="Form_governmentApprove.auditState"
              :label="2"
              v-show="showRepairModuleInfo"
              @change="auditStateRadioChange"
              name="approve"
            >不同意维修</el-radio>
          </el-form-item>
          <el-form-item label="审批意见：" label-width="105px" prop="comment">
            <el-input
              v-model="Form_governmentApprove.comment"
              type="textarea"
              :rows="4"
              placeholder="请输入审批意见"
              class="approve-advices-textarea"
            ></el-input>
          </el-form-item>
          <el-form-item label="批示文件：" label-width="105px" prop="repairApprovalDataList">
            <div class="repaire-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=governmentFile`"
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
                <div
                  slot="tip"
                  class="el-upload__tip"
                  v-show="fileList.length > 0"
                >已选中{{fileList.length}}个文件</div>
              </el-upload>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.governmentApprove = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm_hasUpload('Form_governmentApprove')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /发改委审批 -->

    <!-- 项目施工设计 -->
    <el-dialog
      :visible.sync="dialogVisible.projectConstructionDesign"
      @open="dialogOpen"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <repair-info
        :showRepairModuleInfo="showRepairModuleInfo"
        :editing="editing"
        :showEditBtn="false"
        :showDeleteBtn="false"
        :showMoreBtn="true"
        :activeInfo="activeInfo"
        :baseInfo="baseInfo"
        :repairInfoList="repairInfoList"
        :repairInfoDetail="repairInfoDetail"
        @editRepairModuleArea="editRepairModuleArea"
        @showInfoDetail="showInfoDetail"
        @deleteRepairModule="deleteRepairModule"
      ></repair-info>
      <div class="repair-approve-box">
        <el-form
          :inline="true"
          :model="Form_projectConstructionDesign"
          ref="Form_projectConstructionDesign"
          :rules="rules_projectConstructionDesign"
        >
          <el-form-item label="设计单位：" label-width="105px" prop="designUnit" style="width: 33%">
            <el-input
              v-model="Form_projectConstructionDesign.designUnit"
              size="small"
              placeholder="请输入设计单位"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="规划许可证号："
            label-width="125px"
            prop="planningPermit"
            style="width: 33%"
          >
            <el-input
              v-model="Form_projectConstructionDesign.planningPermit"
              size="small"
              placeholder="请输入规划许可证号"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="设计图纸："
            label-width="105px"
            style="width: 100%"
            prop="repairApprovalDataList"
          >
            <div class="repaire-img-upload">
              <el-upload
                ref="uploadImg"
                list-type="picture-card"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=detailDesign`"
                :file-list="fileListImg"
                :auto-upload="false"
                :accept="acceptImgType"
                :limit="limit"
                :on-success="handleImgSuccess"
                :on-remove="handleImgRemove"
                :on-change="handleImgChange"
                :on-error="handleError"
                :on-exceed="handleImgExceed"
                :before-upload="beforeAvatarUpload"
                multiple
              >
                <i class="el-icon-plus"></i>
              </el-upload>
            </div>
          </el-form-item>
          <el-form-item
            label="规划许可："
            label-width="105px"
            style="width: 100%"
            prop="repairApprovalDataList"
          >
            <div class="repaire-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=detailDesign`"
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
                <div
                  slot="tip"
                  class="el-upload__tip"
                  v-show="fileList.length > 0"
                >已选中{{fileList.length}}个文件</div>
              </el-upload>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.projectConstructionDesign = false" :fontSize="14"></cancel-btn>
        <confirm-btn
          @clickBtn="submitForm_hasUpload('Form_projectConstructionDesign')"
          :fontSize="14"
        ></confirm-btn>
      </span>
    </el-dialog>
    <!-- /项目施工设计 -->

    <!-- 维修预算 -->
    <el-dialog
      :visible.sync="dialogVisible.projectBudget"
      @open="dialogOpen"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <repair-info
        :showRepairModuleInfo="showRepairModuleInfo"
        :editing="editing"
        :showEditBtn="false"
        :showDeleteBtn="false"
        :showMoreBtn="true"
        :activeInfo="activeInfo"
        :baseInfo="baseInfo"
        :repairInfoList="repairInfoList"
        :repairInfoDetail="repairInfoDetail"
        @editRepairModuleArea="editRepairModuleArea"
        @showInfoDetail="showInfoDetail"
        @deleteRepairModule="deleteRepairModule"
      ></repair-info>
      <div class="repair-approve-box">
        <el-form
          :inline="true"
          :model="Form_projectBudget"
          ref="Form_projectBudget"
          :rules="rules_projectBudget"
        >
          <el-form-item label="设计单位：" label-width="105px" style="width: 33%">
            <span>{{projectBudgetDetail.designUnit}}</span>
          </el-form-item>
          <el-form-item label="规划许可证号：" label-width="135px" style="width: 33%">
            <span>{{projectBudgetDetail.planningPermit}}</span>
          </el-form-item>
          <el-form-item
            label="设计图纸："
            label-width="105px"
            style="width: 100%"
            v-show="projectBudgetDetail.designFileList.length > 0"
          >
            <div class="clearfix">
              <div
                class="design-img-item"
                v-for="(item, index) in projectBudgetDetail.designFileList"
                :key="index"
              >
                <img :src="getImgUrl(item.virtualAddress)" alt="设计图纸" />
                <div class="bigger-view-warpper">
                  <div class="bigger-view-warpper-inner"></div>
                  <i class="el-icon-search" @click="preview(item.virtualAddress)"></i>
                </div>
              </div>
            </div>
          </el-form-item>
          <el-form-item label="预算单位：" label-width="105px" prop="auditUnit" style="width: 33%">
            <el-input size="small" placeholder="请输入预算单位" v-model="Form_projectBudget.auditUnit"></el-input>
          </el-form-item>
          <el-form-item
            label="预算总价(元)："
            label-width="135px"
            prop="totalBudgetPrice"
            style="width: 33%"
          >
            <el-input
              size="small"
              placeholder="请输入预算总价"
              v-model="Form_projectBudget.totalBudgetPrice"
              @keyup.native="limitFloat('totalBudgetPrice')"
              @blur="limitFloat('totalBudgetPrice', true)"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="预算文件："
            label-width="105px"
            style="width: 100%"
            prop="repairApprovalDataList"
          >
            <div class="repaire-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=projectBudget`"
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
                <div
                  slot="tip"
                  class="el-upload__tip"
                  v-show="fileList.length > 0"
                >已选中{{fileList.length}}个文件</div>
              </el-upload>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.projectBudget = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm_hasUpload('Form_projectBudget')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /维修预算 -->

    <!-- 预算评审 -->
    <el-dialog
      :visible.sync="dialogVisible.projectApprove"
      @open="dialogOpen"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
      class="projectApprove"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <repair-info
        :showRepairModuleInfo="showRepairModuleInfo"
        :editing="editing"
        :showEditBtn="false"
        :showDeleteBtn="false"
        :showMoreBtn="true"
        :activeInfo="activeInfo"
        :baseInfo="baseInfo"
        :repairInfoList="repairInfoList"
        :repairInfoDetail="repairInfoDetail"
        @editRepairModuleArea="editRepairModuleArea"
        @showInfoDetail="showInfoDetail"
        @deleteRepairModule="deleteRepairModule"
      ></repair-info>
      <div class="repair-approve-box">
        <el-form
          :model="Form_projectApprove"
          ref="Form_projectApprove"
          :rules="rules_projectApprove"
        >
          <el-form-item label="设计单位：" label-width="161px">
            <span>{{projectBudgetDetail.designUnit}}</span>
          </el-form-item>
          <el-form-item label="规划许可证号：" label-width="161px">
            <span>{{projectBudgetDetail.planningPermit}}</span>
          </el-form-item>
          <el-form-item
            label="设计图纸："
            label-width="161px"
            v-show="projectBudgetDetail.designFileList.length > 0"
          >
            <div class="clearfix">
              <div
                class="design-img-item"
                v-for="(item, index) in projectBudgetDetail.designFileList"
                :key="index"
              >
                <img :src="getImgUrl(item.virtualAddress)" alt="设计图纸" />
                <div class="bigger-view-warpper">
                  <div class="bigger-view-warpper-inner"></div>
                  <i class="el-icon-search" @click="preview(item.virtualAddress)"></i>
                </div>
              </div>
            </div>
          </el-form-item>
          <el-form-item label="预算总价：" label-width="161px">
            <span>{{projectBudgetDetail.totalBudgetPrice}}元</span>
          </el-form-item>
          <el-form-item label="预算单位：" label-width="161px">
            <span>{{projectBudgetDetail.auditUnit}}</span>
          </el-form-item>
          <el-form-item label="预算文件：" label-width="161px">
            <div class="repaire-file-download">
              <el-checkbox-group
                v-model="fileDownLoadCheckedList"
                @change="handleCheckedFileChange"
              >
                <div v-for="(file, index) in projectBudgetDetail.budgetFileList" :key="index">
                  <el-checkbox :label="file.physicalAddress">{{file.repairFileName}}</el-checkbox>
                </div>
              </el-checkbox-group>
              <el-button
                size="small"
                type="primary"
                @click="fileDownLoad(downloadCheckedList, 13)"
              >点击下载文件</el-button>
            </div>
          </el-form-item>
          <el-form-item label="预算评审单位：" label-width="161px" prop="accreditationUnit">
            <el-input
              size="small"
              placeholder="请输入预算评审单位"
              v-model="Form_projectApprove.accreditationUnit"
            ></el-input>
          </el-form-item>
          <el-form-item label="财政评审金额(元)：" label-width="161px" prop="finalSum">
            <el-input
              size="small"
              placeholder="请输入财政评审金额"
              v-model="Form_projectApprove.finalSum"
              @keyup.native="limitFloat('finalSum')"
              @blur="limitFloat('finalSum', true, decreaseAmount)"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="下降金额(元)："
            label-width="161px"
            v-show="Form_projectApprove.basicDBObject.decreaseAmount"
          >
            <span>{{Form_projectApprove.basicDBObject.decreaseAmount}}</span>
          </el-form-item>
          <el-form-item label="预算评审文件：" label-width="161px" prop="repairApprovalDataList">
            <div class="repaire-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=projectReview`"
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
                <div
                  slot="tip"
                  class="el-upload__tip"
                  v-show="fileList.length > 0"
                >已选中{{fileList.length}}个文件</div>
              </el-upload>
            </div>
          </el-form-item>
          <el-form-item label="采购名称：" label-width="161px" prop="repairHttpResultVo.common_name">
            <el-input
              size="small"
              placeholder="请输入采购名称"
              v-model="Form_projectApprove.repairHttpResultVo.common_name"
            ></el-input>
          </el-form-item>
          <el-form-item label="采购单价：" label-width="161px" prop="repairHttpResultVo.common_price">
            <el-input
              size="small"
              placeholder="请输入采购单价(每平米的价格)"
              v-model="Form_projectApprove.repairHttpResultVo.common_price"
              @keyup.native="limitFloat_2('common_price')"
              @blur="limitFloat_2('common_price', true)"
            ></el-input>
          </el-form-item>
          <el-form-item label="采购数量：" label-width="161px" prop="repairHttpResultVo.common_quantity">
            <el-input
              size="small"
              placeholder="请输入采购数量"
              v-model="Form_projectApprove.repairHttpResultVo.common_quantity"
              @keyup.native="limitInt('common_quantity', 'repairHttpResultVo')"
              @blur="limitInt('common_quantity', 'repairHttpResultVo')"
            ></el-input>
          </el-form-item>
          <el-form-item label="预算事项：" label-width="161px" prop="repairHttpResultVo.detailid">
            <custom-select
              name="projectApprove"
              inputWidth="100%"
              placeholderText="请选择预算事项"
              :inputText="inputText"
              :common_quantity="Form_projectApprove.repairHttpResultVo.common_quantity"
              :common_price="Form_projectApprove.repairHttpResultVo.common_price"
              @getSectionList="getSectionList"
            >
              <el-table
                ref="singleTable"
                :data="officeTableData"
                style="width: 100%"
                :height="200"
                :header-cell-style="tableHeaderCellStyle"
                :cell-style="tableCellStyle"
                @row-click="tableRowClick"
              >
                <el-table-column width="50">
                  <template slot-scope="scope">
                    <el-radio
                      v-model="Form_projectApprove.repairHttpResultVo.detailid"
                      :label="scope.row.id"
                    >&thinsp;</el-radio>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="title"
                  label="预算项目名称"
                  align="center"
                  :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                  prop="deptid"
                  label="预算科室名称"
                  align="center"
                  :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                  prop="approval_amount"
                  label="预算批复金额"
                  align="center"
                  :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                  prop="used_proportion"
                  label="执行比例"
                  align="center"
                  :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                  prop="balance_proportion"
                  label="余额占比"
                  align="center"
                  :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                  prop="adjust_amount_in"
                  label="调整金额(调入)"
                  align="center"
                  :width="120"
                  :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                  prop="adjust_amount_out"
                  label="调整金额(调出)"
                  align="center"
                  :width="120"
                  :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                  prop="balance_amount"
                  label="可用余额"
                  align="center"
                  :show-overflow-tooltip="true"
                ></el-table-column>
              </el-table>
            </custom-select>
          </el-form-item>
          <el-form-item label="事项说明：" label-width="161px" prop="repairHttpResultVo.description">
            <el-input
              v-model="Form_projectApprove.repairHttpResultVo.description"
              type="textarea"
              :rows="4"
              placeholder="请输入事项说明(例如：维修地点，维修部位，维修内容或项目简介)"
            ></el-input>
          </el-form-item>
          <el-form-item label="简述：" label-width="161px" prop="repairHttpResultVo.title">
            <el-input
              v-model="Form_projectApprove.repairHttpResultVo.title"
              type="textarea"
              :rows="4"
              placeholder="请输入简述(维修内容)"
              class="approve-advices-textarea"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.projectApprove = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm_hasUpload('Form_projectApprove')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /预算评审 -->

    <!-- 指定项目负责人 -->
    <el-dialog
      :visible.sync="dialogVisible.charge"
      @open="dialogOpen"
      :close-on-click-modal="false"
      width="780px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <div class="repair-approve-box">
        <el-form :model="Form_charge" ref="Form_charge" :rules="rules_charge" class="charge-form">
          <el-form-item label="项目负责人：" label-width="135px" prop="projectLeaders">
            <el-select v-model="Form_charge.projectLeaders" multiple placeholder="请选择" size="small">
              <el-option
                v-for="item in chargeOptions"
                :key="item.user_id"
                :label="item.username"
                :value="item.user_id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.charge = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm_noUpload('Form_charge')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>

    <!-- 图片预览 -->
    <div class="img-preview-bigger">
      <div class="img-preview-bigger-mask"></div>
      <img src alt id="biggerImg" @load="loadImage" />
      <i class="el-icon-circle-close" @click="closePreview"></i>
    </div>
    <!-- /图片预览 -->
  </div>
</template>

<script>
import { BASE_URL, url_name_1 } from "@/axios/global";
import { uploadUrl } from "@/axios/commonApi";
import { getRepairApprovalList, getRepairApprovalDetail, getRepairMessageDetail, updateRepairModule, deleteRepairModule, getRepairRecord, saveSubmitRepair, getRepairDetailDb, downloadFile, getProjectFileAddress, getProjectBudgetDetail, getProjectReviewDetail, getUserList, getSectionList } from "./api_2";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import repairInfo from "@/components/repair/repairInfo";
import customSelect from "@/components/customSelect/select";
import { deleteBuild } from '../baseInfoManagement/api';
import { debug } from 'util';
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
    repairInfo,
    customSelect
  },
  data() {
    let validApprovalNull = (rule, value, callback) => {
      if (!this.currentForm.auditState) {
        callback(new Error('请选择审批项'));
      } else {
        callback()
      }
    };
    let validUpload_1 = (rule, value, callback) => {
      if (this.fileList.length === 0) {
        callback(new Error(this.uploadTips));
      } else {
        callback()
      }
    };
    let validUpload_2 = (rule, value, callback) => {
      if (this.fileList.length === 0 && this.fileListImg.length === 0) {
        callback(new Error(this.uploadTips));
      } else {
        callback()
      }
    };
    let validFinalSum = (rule, value, callback) => {
      const finalSum = this.Form_projectApprove.finalSum - 0;
      const totalBudgetPrice = this.projectBudgetDetail.totalBudgetPrice;
      if (finalSum > totalBudgetPrice) {
        callback(new Error('财政评审金额不能超过预算总价'));
      } else {
        callback()
      }
    };
    let validProjectLeaders = (rule, value, callback) => {
      const projectLeadersLen = this.Form_charge.projectLeaders.length;
      if (projectLeadersLen === 0) {
        callback(new Error('请选择项目负责人'));
      } else {
        callback()
      }
    };
    return {
      userId: null,
      username: '',
      token: '',
      postHeaders: {
        Authorization: ''
      },
      startApplicationTime: '',
      endApplicationTime: '',
      repairApplyNo: '',
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      selectData: [],
      tableData: [],
      operateType: "",
      editing: false,
      chargeOptions: [],
      dialogVisible: {
        officeApprove: false, // 3 科室审批
        partyOrganizationApprove: false, // 4 审批意见
        selfPayingRepairUpload: false, // 5 部门维修方案上传
        selfPayingRepairApprove: false, // 6 部门维修审批
        budgetRepairUpload: false, // 19 预算维修方案上传
        budgetRepairApprove: false, // 20 预算维修审批
        feasibilityApprove: false, // 8 可行性研究
        leaderApprove: false, // 9 领导审批
        governmentApprove: false, // 21 发改委审批
        projectConstructionDesign: false, // 22 项目施工设计
        projectBudget: false, // 23 维修预算
        projectApprove: false, // 24 预算评审
        charge: false // 25 指定项目负责人
      },
      Form_officeApprove: {
        processInstanceId: '',
        step: null,
        taskId: '',
        userId: null,
        userName: '',
        name: '',
        auditState: 1,
        comment: '',
        responsiblePerson: '',
        repairUnitName: '',
        phoneNum: '',
        repairHttpResultVo: {
          title: '',
          detailid: '',
          description: '',
          common_name: '',
          common_price: '',
          common_quantity: '',
          common_unit: '',
          common_total: ''
        }
      },
      Form_partyOrganizationApprove: {
        processInstanceId: '',
        step: null,
        taskId: "",
        userId: null,
        userName: '',
        name: '',
        auditState: 1,
        comment: "",
        basicDBObject: {
          repairApprovalDataList: []
        },
        physicalAddressList: [],
        phoneNum: ''
      },
      Form_selfPayingRepairUpload: {
        processInstanceId: '',
        step: null,
        taskId: "",
        userId: null,
        userName: '',
        name: '',
        auditState: 1,
        basicDBObject: {
          repairApprovalDataList: []
        },
        physicalAddressList: [],
        phoneNum: ''
      },
      Form_selfPayingRepairApprove: {
        processInstanceId: '',
        step: null,
        taskId: "",
        userId: null,
        userName: '',
        name: '',
        auditState: 1,
        comment: '',
        phoneNum: ''
      },
      Form_budgetRepairUpload: {
        processInstanceId: '',
        step: null,
        taskId: "",
        userId: null,
        userName: '',
        name: '',
        auditState: 1,
        basicDBObject: {
          repairApprovalDataList: []
        },
        physicalAddressList: [],
        phoneNum: ''
      },
      Form_budgetRepairApprove: {
        processInstanceId: '',
        step: null,
        taskId: "",
        userId: null,
        userName: '',
        name: '',
        auditState: 1,
        comment: '',
        phoneNum: ''
      },
      Form_feasibilityApprove: {
        processInstanceId: '',
        step: null,
        taskId: "",
        userId: null,
        userName: '',
        name: '',
        auditState: 1,
        basicDBObject: {
          repairApprovalDataList: [],
          projectIntroduction: ''
        },
        physicalAddressList: [],
        repairProject: '',
        budgetEstimate: '',
        unitAreaPrice: '',
        phoneNum: ''
      },
      Form_leaderApprove: {
        processInstanceId: '',
        step: null,
        taskId: "",
        userId: null,
        userName: '',
        name: '',
        auditState: 1,
        basicDBObject: {
          repairApprovalDataList: []
        },
        comment: "",
        phoneNum: ''
      },
      Form_governmentApprove: {
        processInstanceId: '',
        step: null,
        taskId: "",
        userId: null,
        userName: '',
        name: '',
        auditState: 1,
        basicDBObject: {
          repairApprovalDataList: []
        },
        comment: "",
        phoneNum: ''
      },
      Form_projectConstructionDesign: {
        processInstanceId: '',
        step: null,
        taskId: "",
        userId: null,
        userName: '',
        name: '',
        auditState: 1,
        basicDBObject: {
          repairApprovalDataList: []
        },
        physicalAddressList: [],
        designUnit: '',
        planningPermit: '',
        phoneNum: ''
      },
      Form_projectBudget: {
        processInstanceId: '',
        step: null,
        taskId: "",
        userId: null,
        userName: '',
        name: '',
        auditState: 1,
        basicDBObject: {
          repairApprovalDataList: []
        },
        physicalAddressList: [],
        auditUnit: '',
        totalBudgetPrice: null,
        phoneNum: ''
      },
      Form_projectApprove: {
        processInstanceId: '',
        step: null,
        taskId: "",
        userId: null,
        userName: '',
        name: '',
        auditState: 1,
        basicDBObject: {
          repairApprovalDataList: [],
          decreaseAmount: null
        },
        physicalAddressList: [],
        accreditationUnit: '',
        finalSum: null,
        phoneNum: '',
        repairHttpResultVo: {
          title: '',
          detailid: '',
          description: '',
          common_name: '',
          common_price: '',
          common_quantity: '',
          common_unit: '',
          common_total: ''
        }
      },
      Form_charge: {
        processInstanceId: '',
        step: null,
        taskId: "",
        userId: null,
        userName: '',
        name: '',
        auditState: 1,
        projectLeaders: [],
        phoneNum: ''
      },
      showRepairModuleInfo: true,
      showRepairRecord: false,
      currentDialogName: '',
      currentForm: null,
      allRepairArea: null,
      unitAreaPrice: null,
      baseInfo: [],
      repairInfoList: [],
      repairRecordList: [],
      activeInfo: 0,
      activeRecord: 0,
      activeRepairModuleId: null,
      repairInfoDetail: {
        repairModuleId: null,
        repairSite: '',
        repairPartName: '',
        repairModuleTarget: '',
        repairModuleArea: '',
        repairImgList: []
      },
      repairRecordDetail: {
        positionStr: '',
        repairPartName: '',
        repairModulePosition: '',
        completionTime: '',
        repairUnitNameList: '',
        contactPhone: '',
        completionQuality: ''
      },
      projectBudgetDetail: {
        designFileList: [],
        budgetFileList: [],
        designUnit: '',
        planningPermit: '',
        totalBudgetPrice: null,
        auditUnit: ''
      },
      popoverRepairRecords: false,
      uploadUrl: uploadUrl,
      acceptType: "image/jpeg,image/gif,image/png,image/bmp,.doc,.docx,.xlsx,.ppt,.pdf,.rar,.zip",
      acceptImgType: "image/jpeg,image/gif,image/png,image/bmp,.rar,.zip",
      limit: 5,
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
      uploadTips: '',
      commentInfo: '',
      downLoadList: [],
      fileDownLoadCheckedList: [],
      downloadCheckedList: [],
      officeTableData: [],
      tableHeaderCellStyle: {
        padding: "6px 0",
      },
      tableCellStyle: {
        padding: "6px 0",
        cursor: 'pointer'
      },
      inputText: '',
      rules_officeApprove: {
        auditState: [
          { required: true, validator: validApprovalNull, trigger: "blur" }
        ],
        responsiblePerson: [
          { required: true, message: "负责人不能为空", trigger: "blur" }
        ],
        repairUnitName: [
          { required: true, message: "施工单位不能为空", trigger: "blur" }
        ],
        'repairHttpResultVo.title': [
          { required: true, message: "简述(维修内容)不能为空", trigger: "blur" }
        ],
        'repairHttpResultVo.detailid': [
          { required: true, message: "预算事项不能为空", trigger: "blur" }
        ],
        'repairHttpResultVo.common_name': [
          { required: true, message: "采购名称不能为空", trigger: "blur" }
        ],
        'repairHttpResultVo.common_price': [
          { required: true, message: "采购单价不能为空", trigger: "blur" }
        ],
        'repairHttpResultVo.common_quantity': [
          { required: true, message: "采购数量不能为空", trigger: "blur" }
        ],
        comment: []
      },
      rules_partyOrganizationApprove: {
        auditState: [
          { required: true, validator: validApprovalNull, trigger: "blur" }
        ],
        comment: [],
        repairApprovalDataList: [
          { required: true, validator: validUpload_1, trigger: "blur" }
        ]
      },
      rules_selfPayingRepairUpload: {
        repairApprovalDataList: [
          { required: true, validator: validUpload_1, trigger: "blur" }
        ]
      },
      rules_selfPayingRepairApprove: {
        auditState: [
          { required: true, validator: validApprovalNull, trigger: "blur" }
        ],
        comment: []
      },
      rules_budgetRepairUpload: {
        repairApprovalDataList: [
          { required: true, validator: validUpload_1, trigger: "blur" }
        ]
      },
      rules_budgetRepairApprove: {
        auditState: [
          { required: true, validator: validApprovalNull, trigger: "blur" }
        ],
        comment: []
      },
      rules_feasibilityApprove: {
        repairProject: [
          { required: true, message: '项目名称不能为空', trigger: 'blur' }
        ],
        budgetEstimate: [
          { required: true, message: '维修概算不能为空', trigger: 'blur' }
        ],
        repairApprovalDataList: [
          { required: true, validator: validUpload_2, trigger: "blur" }
        ]
      },
      rules_leaderApprove: {
        auditState: [
          { required: true, validator: validApprovalNull, trigger: "blur" }
        ],
        comment: [],
        repairApprovalDataList: [
          { required: true, validator: validUpload_1, trigger: "blur" }
        ]
      },
      rules_governmentApprove: {
        auditState: [
          { required: true, validator: validApprovalNull, trigger: "blur" }
        ],
        comment: [],
        repairApprovalDataList: [
          { required: true, validator: validUpload_1, trigger: "blur" }
        ]
      },
      rules_projectConstructionDesign: {
        designUnit: [
          { required: true, message: '设计单位不能为空', trigger: 'blur' }
        ],
        repairApprovalDataList: [
          { required: true, validator: validUpload_2, trigger: "blur" }
        ]
      },
      rules_projectBudget: {
        totalBudgetPrice: [
          { required: true, message: '预算总价不能为空', trigger: 'blur' }
        ],
        auditUnit: [
          { required: true, message: '预算单位不能为空', trigger: 'blur' }
        ],
        repairApprovalDataList: [
          { required: true, validator: validUpload_1, trigger: "blur" }
        ]
      },
      rules_projectApprove: {
        accreditationUnit: [
          { required: true, message: '评审单位不能为空', trigger: 'blur' }
        ],
        finalSum: [
          { required: true, message: '决策金额不能为空', trigger: 'blur' },
          { validator: validFinalSum, trigger: "blur" }
        ],
        repairApprovalDataList: [
          { required: true, validator: validUpload_1, trigger: "blur" }
        ],
        'repairHttpResultVo.title': [
          { required: true, message: "简述(维修内容)不能为空", trigger: "blur" }
        ],
        'repairHttpResultVo.detailid': [
          { required: true, message: "预算事项不能为空", trigger: "blur" }
        ],
        'repairHttpResultVo.common_name': [
          { required: true, message: "采购名称不能为空", trigger: "blur" }
        ],
        'repairHttpResultVo.common_price': [
          { required: true, message: "采购单价不能为空", trigger: "blur" }
        ],
        'repairHttpResultVo.common_quantity': [
          { required: true, message: "采购数量不能为空", trigger: "blur" }
        ],
      },
      rules_charge: {
        projectLeaders: [
          { required: true, validator: validProjectLeaders, trigger: "blur" }
        ]
      },
      pickerOptions_start: {
      },
      pickerOptions_end: {
      }
    };
  },
  watch: {
    'currentForm.budgetEstimate'(newVal) {
      const reg = /^[0-9]+.?[0-9]*$/;
      if (newVal && reg.test(newVal)) {
        this.currentForm.unitAreaPrice = newVal / this.allRepairArea;
        this.currentForm.unitAreaPrice = this.currentForm.unitAreaPrice.toFixed(2);
      } else {
        this.currentForm.unitAreaPrice = null;
      }
    }
  },
  methods: {
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
    // 计算下降金额
    decreaseAmount() {
      const finalSum = this.currentForm.finalSum;
      if (finalSum) {
        this.currentForm.basicDBObject.decreaseAmount = this.projectBudgetDetail.totalBudgetPrice - finalSum;
      } else {
        this.currentForm.basicDBObject.decreaseAmount = null;
      }
      if (this.currentForm.basicDBObject.decreaseAmount < 0) {
        this.currentForm.basicDBObject.decreaseAmount = null;
      } else {
        this.currentForm.basicDBObject.decreaseAmount = this.currentForm.basicDBObject.decreaseAmount.toFixed(2);
      }
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
    // 获取职称信息列表
    getList() {
      this.loadingStart('.table-box');
      this.userId = parseInt(sessionStorage.getItem('userId'));
      getRepairApprovalList(this.currentPage, this.pageSize, this.userId, this.startApplicationTime, this.endApplicationTime, this.repairApplyNo)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach((item, index) => {
            item.serialNum = index + 1;
            switch (item.step) {
              case 3:
                item.stepText = "科室审批";
                break;
              case 4:
                item.stepText = "审批意见";
                break;
              case 5:
                item.stepText = "部门维修上传";
                break;
              case 6:
                item.stepText = "部门维修审批";
                break;
              case 19:
                item.stepText = "预算维修上传";
                break;
              case 20:
                item.stepText = "预算维修审批";
                break;
              case 8:
                item.stepText = "可行性研究";
                break;
              case 9:
                item.stepText = "领导审批";
                break;
              case 21:
                item.stepText = "发改委审批";
                break;
              case 22:
                item.stepText = "项目施工设计";
                break;
              case 23:
                item.stepText = "维修预算";
                break;
              case 24:
                item.stepText = "预算评审";
                break;
              case 25:
                item.stepText = "指定负责人";
                break;
            }
            if (item.repairApplyDate !== undefined) {
              item.repairApplyDate = this.formatDate(item.repairApplyDate, "yyyy-MM-dd hh:mm:ss");
            }
          })
          this.loadingEnd();
        })
        .catch(err => {
          const msg = err.result ? err.result : '维修审批列表获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
          this.loadingEnd();
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
      this.startApplicationTime = '';
      this.endApplicationTime = '';
      this.repairApplyNo = '';
      this.search();
    },
    // 内控预算事项科室预算明细信息
    getSectionList() {
      const name = sessionStorage.getItem('name');
      const telephone = sessionStorage.getItem('telephone');
      if (this.officeTableData.length === 0) {
        getSectionList(name, telephone).then(res => {
          if (res && res.length > 0) {
            this.officeTableData = res;
          } else {
            this.officeTableData = [];
          }
        }).catch(err => {
          this.officeTableData = [];
          const msg = err.result ? err.result : '内控预算事项科室预算明细信息获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        })
      }
    },
    // 审批流程
    openDialog(row) {
      switch (row.step) {
        case 3:
          this.dialogVisible.officeApprove = true;
          this.currentDialogName = 'officeApprove';
          this.currentForm = this.Form_officeApprove;
          this.operateType = "科室审批";
          break;
        case 4:
          this.dialogVisible.partyOrganizationApprove = true;
          this.currentDialogName = 'partyOrganizationApprove';
          this.currentForm = this.Form_partyOrganizationApprove;
          this.operateType = "审批意见";
          this.uploadTips = "请上传审批原件"
          break;
        case 5:
          this.dialogVisible.selfPayingRepairUpload = true;
          this.currentDialogName = 'selfPayingRepairUpload';
          this.currentForm = this.Form_selfPayingRepairUpload;
          this.operateType = "部门维修上传";
          this.uploadTips = "请上传审批文件"
          break;
        case 6:
          this.dialogVisible.selfPayingRepairApprove = true;
          this.currentDialogName = 'selfPayingRepairApprove';
          this.currentForm = this.Form_selfPayingRepairApprove;
          this.operateType = "部门维修审批";
          getProjectFileAddress(row.repairApplyId, 5).then(res => {
            this.downLoadList = res[0].repairPlanDb.repairApprovalDataList;
            this.downLoadList.forEach(item => {
              item.physicalAddress = `${item.physicalAddress},${item.repairFileName}`;
            })
          }).catch(err => {
            const msg = err.result ? err.result : '文件信息获取失败';
            this.$message({
              message: msg,
              type: "error"
            });
          })
          break;
        case 19:
          this.dialogVisible.budgetRepairUpload = true;
          this.currentDialogName = 'budgetRepairUpload';
          this.currentForm = this.Form_budgetRepairUpload;
          this.operateType = "预算维修上传";
          this.uploadTips = "请上传审批文件"
          break;
        case 20:
          this.dialogVisible.budgetRepairApprove = true;
          this.currentDialogName = 'budgetRepairApprove';
          this.currentForm = this.Form_budgetRepairApprove;
          this.operateType = "预算维修审批";
          getProjectFileAddress(row.repairApplyId, 19).then(res => {
            this.downLoadList = res[0].ysRepairPlanDb.repairApprovalDataList;
            this.downLoadList.forEach(item => {
              item.physicalAddress = `${item.physicalAddress},${item.repairFileName}`;
            })
          }).catch(err => {
            const msg = err.result ? err.result : '文件信息获取失败';
            this.$message({
              message: msg,
              type: "error"
            });
          })
          break;
        case 8:
          this.dialogVisible.feasibilityApprove = true;
          this.currentDialogName = 'feasibilityApprove';
          this.currentForm = this.Form_feasibilityApprove;
          this.operateType = "可行性研究";
          this.uploadTips = "请上传相关文件"
          break;
        case 9:
          this.dialogVisible.leaderApprove = true;
          this.currentDialogName = 'leaderApprove';
          this.currentForm = this.Form_leaderApprove;
          this.operateType = "领导审批";
          this.uploadTips = "请上传相关文件"
          break;
        case 21:
          this.dialogVisible.governmentApprove = true;
          this.currentDialogName = 'governmentApprove';
          this.currentForm = this.Form_governmentApprove;
          this.operateType = "发改委审批";
          this.uploadTips = "请上传批示文件"
          break;
        case 22:
          this.dialogVisible.projectConstructionDesign = true;
          this.currentDialogName = 'projectConstructionDesign';
          this.currentForm = this.Form_projectConstructionDesign;
          this.operateType = "项目施工设计";
          this.uploadTips = "请上传相关文件"
          break;
        case 23:
          this.dialogVisible.projectBudget = true;
          this.currentDialogName = 'projectBudget';
          this.currentForm = this.Form_projectBudget;
          this.operateType = "维修预算";
          this.uploadTips = "请上传预算文件"
          break;
        case 24:
          this.dialogVisible.projectApprove = true;
          this.currentDialogName = 'projectApprove';
          this.currentForm = this.Form_projectApprove;
          this.operateType = "预算评审";
          this.uploadTips = "请上传预算评审文件"
          break;
        case 25:
          this.dialogVisible.charge = true;
          this.currentDialogName = 'charge';
          this.currentForm = this.Form_charge;
          this.operateType = "指定项目负责人";
          getUserList().then(res => {
            this.chargeOptions = res;
          }).catch(err => {
            const msg = err.result ? err.result : '项目负责人下拉列表获取失败';
            this.$message({
              message: msg,
              type: "error"
            });
          })
          break
      }
      this.currentForm.processInstanceId = row.processInstanceId;
      this.currentForm.repairId = row.repairApplyId;
      this.currentForm.taskId = row.taskId;
      this.currentForm.step = row.step;
      this.currentForm.userId = this.userId;
      this.currentForm.userName = this.username;
      this.currentForm.name = sessionStorage.getItem('name');
      // 查询维修预算信息
      if (this.currentForm.step === 23) {
        getProjectBudgetDetail(row.repairApplyId).then(res => {
          for ([key, value] of Object.entries(res)) {
            if (key !== 'designFileList' && this.projectBudgetDetail[key] !== undefined) {
              this.projectBudgetDetail[key] = value;
            }
          }
          if (res.designFileList && res.designFileList.length > 0) {
            res.designFileList[0].address.forEach(item => {
              const name = item.name.split('_')[1];
              if (name === 'img') {
                if (item.type === 3) {
                  item.virtualAddress = '/images/compress.png';
                }
                this.projectBudgetDetail.designFileList.push(item);
              }
            })
          }
        }).catch(err => {
          const msg = err.result ? err.result : '维修预算信息获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        })
      }
      // 查询预算评审信息
      if (this.currentForm.step === 24) {
        getProjectReviewDetail(row.repairApplyId).then(res => {
          for ([key, value] of Object.entries(res)) {
            if (key !== 'designFileList' && this.projectBudgetDetail[key] !== undefined) {
              this.projectBudgetDetail[key] = value;
            }
          }
          if (res.designFileList && res.designFileList.length > 0) {
            res.designFileList[0].address.forEach(item => {
              const name = item.name.split('_')[1];
              if (name === 'img') {
                if (item.type === 3) {
                  item.virtualAddress = '/images/compress.png';
                }
                this.projectBudgetDetail.designFileList.push(item);
              }
            })
            this.projectBudgetDetail.budgetFileList = res.designFileList[0].repairFileName;
            this.projectBudgetDetail.budgetFileList.forEach(item => {
              item.physicalAddress = `${item.physicalAddress},${item.repairFileName}`
            })
          }
        }).catch(err => {
          const msg = err.result ? err.result : '预算评审信息获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        })
      }
      // 维修信息
      getRepairApprovalDetail(row.repairApplyId).then(res => {
        this.allRepairArea = res.totalArea;
        if (this.currentForm.step === 21 || this.currentForm.step === 22 || this.currentForm.step === 23 || this.currentForm.step === 24) {
          this.baseInfo[0] = {
            name: '维修项目名称',
            key: 'repairProject',
            value: res.repairProject
          }
          this.baseInfo[1] = {
            name: '维修预算',
            key: 'budgetEstimate',
            value: `${res.budgetEstimate}元`
          }
          this.baseInfo[2] = {
            name: '维修面积',
            key: 'totalArea',
            value: `${res.totalArea}m²`
          }
          this.baseInfo[3] = {
            name: '申请人',
            key: 'repairApplicant',
            value: res.repairApplicant
          }
          this.baseInfo[4] = {
            name: '申请时间',
            key: 'repairApplyDate',
            value: this.formatDate(res.repairApplyDate, "yyyy-MM-dd hh:mm:ss")
          }
          this.baseInfo[5] = {
            name: '使用单位',
            key: 'unitNameList',
            value: res.unitNameList.join(',') || '暂无'
          }
          this.baseInfo[6] = {
            name: '维修单价',
            key: 'unitAreaPrice',
            value: `${res.unitAreaPrice}元`
          }
        } else {
          this.baseInfo[0] = {
            name: '申请人',
            key: 'repairApplicant',
            value: res.repairApplicant
          }
          this.baseInfo[1] = {
            name: '申请时间',
            key: 'repairApplyDate',
            value: this.formatDate(res.repairApplyDate, "yyyy-MM-dd hh:mm:ss")
          }
          this.baseInfo[2] = {
            name: '使用单位',
            key: 'unitNameList',
            value: res.unitNameList.join(',') || '暂无'
          }
        }
        if (res.repairModuleDetailDtoList && res.repairModuleDetailDtoList.length > 0) {
          res.repairModuleDetailDtoList.forEach(item => {
            const _info = item.positionStr;
            const temp = {
              info: _info,
              id: item.repairModuleId
            }
            this.repairInfoList.push(temp);
          })
          this.showRepairModuleInfo = true;
          this.showInfoDetail(res.repairModuleDetailDtoList[0].repairModuleId, 0);
        } else {
          this.showRepairModuleInfo = false;
          this.currentForm.auditState = 2;
        }
      }).catch(err => {
        this.showRepairModuleInfo = false;
        this.currentForm.auditState = 2;
        const msg = err.result ? err.result : '维修审批详情获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
      getRepairDetailDb(row.repairApplyId).then(data => {
        if (data) {
          let uploadedList;
          let disagreeFlag = false;
          switch (this.currentForm.step) {
            case 5:
              if (data.aRepairPlanCommentDb !== undefined) {
                this.commentInfo = data.aRepairPlanCommentDb.aRepairPlanComment
              } else {
                this.commentInfo = '';
              }
              if (data.repairPlanDb) {
                uploadedList = data.repairPlanDb.repairApprovalDataList;
              }
              if (uploadedList && uploadedList.length > 0) {
                this.currentForm.basicDBObject.repairApprovalDataList = uploadedList;
                this.uploadedNum = uploadedList.length;
                uploadedList.forEach(item => {
                  const obj = {
                    name: item.repairFileName,
                    url: BASE_URL + url_name_1 + item.physicalAddress,
                    uid: item.id
                  };
                  this.fileList.push(obj);
                });
              }
              break;
            case 19:
              if (data.ysPlanCommentDb !== undefined) {
                this.commentInfo = data.ysPlanCommentDb.ysPlanComment
              } else {
                this.commentInfo = '';
              }
              if (data.ysRepairPlanDb) {
                uploadedList = data.ysRepairPlanDb.repairApprovalDataList;
              }
              if (uploadedList && uploadedList.length > 0) {
                this.currentForm.basicDBObject.repairApprovalDataList = uploadedList;
                this.uploadedNum = uploadedList.length;
                uploadedList.forEach(item => {
                  const obj = {
                    name: item.repairFileName,
                    url: BASE_URL + url_name_1 + item.physicalAddress,
                    uid: item.id
                  };
                  this.fileList.push(obj);
                });
              }
              break;
            case 8:
              if (data.rhaCommentDb !== undefined) {
                this.commentInfo = data.rhaCommentDb.rhaComment
                disagreeFlag = true;
              } else {
                this.commentInfo = '';
              }
              if (disagreeFlag) {
                getRepairApprovalDetail(row.repairApplyId).then(res => {
                  this.Form_feasibilityApprove.budgetEstimate = res.budgetEstimate;
                  this.Form_feasibilityApprove.repairProject = res.repairProject;
                }).catch(err => {
                  const msg = err.result ? err.result : '维修审批详情获取失败';
                  this.$message({
                    message: msg,
                    type: "error"
                  });
                })
              }
              if (data.researchApprovalDb) {
                this.Form_feasibilityApprove.basicDBObject.projectIntroduction = data.researchApprovalDb.projectIntroduction;
                uploadedList = data.researchApprovalDb.repairApprovalDataList;
              }
              if (uploadedList && uploadedList.length > 0) {
                this.currentForm.basicDBObject.repairApprovalDataList = uploadedList;
                let fileNum = 0;
                let imgNum = 0;
                uploadedList.forEach(item => {
                  const name = item.name.split('_')[1];
                  if (name === 'img') {
                    imgNum++;
                    const obj = {
                      name: BASE_URL + url_name_1 + item.physicalAddress,
                      url: `${BASE_URL}${url_name_1}${item.virtualAddress}?access_token=${this.token}`,
                      uid: item.id
                    };
                    this.fileListImg.push(obj)
                  } else if (name === 'file') {
                    fileNum++;
                    const obj = {
                      name: item.repairFileName,
                      url: BASE_URL + url_name_1 + item.physicalAddress,
                      uid: item.id
                    };
                    this.fileList.push(obj)
                  }
                });
                this.uploadedNum = fileNum;
                this.uploadedImgNum = imgNum;
              }
              break;
          }
        }
      }).catch(err => {
        const msg = err.result ? err.result : '维修申请相关信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查询维修记录
    getRepairRecord(repairModuleId) {
      getRepairRecord(repairModuleId).then(res => {
        this.repairRecordList = res;
        if (this.repairRecordList && this.repairRecordList.length > 0) {
          this.showRepairRecord = true;
          this.showRecordDetail(0);
        } else {
          this.showRepairRecord = false;
        }
      }).catch(err => {
        this.showRepairRecord = false;
        const msg = err.result ? err.result : '维修记录获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 提交表单(无文件上传)
    submitForm_noUpload(formName) {
      this.$refs[formName].validate(valid => {
        this.currentForm.phoneNum = sessionStorage.getItem('telephone');
        // 科室审批零星维修
        if (this.currentDialogName === 'officeApprove') {
          this.currentForm.repairHttpResultVo.common_unit = '平方米';
          this.currentForm.repairHttpResultVo.common_total = (this.currentForm.repairHttpResultVo.common_price * this.currentForm.repairHttpResultVo.common_quantity).toFixed(2);
        }
        if (valid) {
          saveSubmitRepair(this.currentForm)
            .then(res => {
              this.$message({
                message: `审核申请提交成功`,
                type: "success"
              });
              switch (this.currentForm.step) {
                case 3:
                  this.dialogVisible.officeApprove = false;
                  break;
                case 4:
                  this.dialogVisible.partyOrganizationApprove = false;
                  break;
                case 5:
                  this.dialogVisible.selfPayingRepairUpload = false;
                  break;
                case 6:
                  this.dialogVisible.selfPayingRepairApprove = false;
                  break;
                case 19:
                  this.dialogVisible.budgetRepairUpload = false;
                  break;
                case 20:
                  this.dialogVisible.budgetRepairApprove = false;
                  break;
                case 8:
                  this.dialogVisible.feasibilityApprove = false;
                  break;
                case 9:
                  this.dialogVisible.feasibilityApprove = false;
                  break;
                case 21:
                  this.dialogVisible.leaderApprove = false;
                  break;
                case 22:
                  this.dialogVisible.projectConstructionDesign = false;
                  break;
                case 23:
                  this.dialogVisible.projectBudget = false;
                  break;
                case 24:
                  this.dialogVisible.projectApprove = false;
                  break;
                case 25:
                  this.dialogVisible.charge = false;
                  break;
              }
              this.getList();
              eventBus.$emit('updateBacklogTips');
            })
            .catch(err => {
              const msg = err.result ? err.result : '审核申请提交失败';
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
    // 提交表单（有文件上传）
    submitForm_hasUpload(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.loading = this.$loading({
            lock: true,
            text: '正在加载...',
            spinner: 'el-icon-loading',
            target: '.el-dialog',
            customClass: 'loading-box'
          });
          if (this.currentForm.step === 8 || this.currentForm.step === 22) {
            (this.uploadNum === 0 && this.uploadImgNum === 0) ? this.saveForm() : this.submitUpload();
          } else {
            this.uploadNum === 0 ? this.saveForm() : this.submitUpload();
          }
        } else {
          return false;
        }
      });
    },
    // 保存表单（有文件上传）
    saveForm() {
      this.currentForm.phoneNum = sessionStorage.getItem('telephone');
      // 预算评审
      if (this.currentDialogName === 'projectApprove') {
        this.currentForm.repairHttpResultVo.common_unit = '平方米';
        this.currentForm.repairHttpResultVo.common_total = (this.currentForm.repairHttpResultVo.common_price * this.currentForm.repairHttpResultVo.common_quantity).toFixed(2);
      }
      saveSubmitRepair(this.currentForm)
        .then(res => {
          this.$message({
            message: `审核申请提交成功`,
            type: "success"
          });
          switch (this.currentForm.step) {
            case 3:
              this.dialogVisible.officeApprove = false;
              break;
            case 4:
              this.dialogVisible.partyOrganizationApprove = false;
              break;
            case 5:
              this.dialogVisible.selfPayingRepairUpload = false;
              break;
            case 6:
              this.dialogVisible.selfPayingRepairApprove = false;
              break;
            case 19:
              this.dialogVisible.budgetRepairUpload = false;
              break;
            case 20:
              this.dialogVisible.budgetRepairApprove = false;
              break;
            case 8:
              this.dialogVisible.feasibilityApprove = false;
              break;
            case 9:
              this.dialogVisible.leaderApprove = false;
              break;
            case 21:
              this.dialogVisible.governmentApprove = false;
              break;
            case 22:
              this.dialogVisible.projectConstructionDesign = false;
              break;
            case 23:
              this.dialogVisible.projectBudget = false;
              break;
            case 24:
              this.dialogVisible.projectApprove = false;
              break;
            case 25:
              this.dialogVisible.charge = false;
              break;
          }
          this.getList();
          eventBus.$emit('updateBacklogTips');
        })
        .catch(err => {
          const msg = err.result ? err.result : '审核申请提交失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 更改维修面积
    editRepairModuleArea(id) {
      const _this = this;
      this.editing = true;
      const areaDom = $('.repair-module-area-input');
      const oldValue = this.repairInfoDetail.repairModuleArea;
      this.repairInfoDetail.repairModuleArea = '';
      const newObj = document.createElement("input");
      newObj.className = "area-input";
      newObj.innerHTML = oldValue;
      newObj.onkeyup = function () {
        if (this.value != "" && this.value.substr(0, 1) == ".") {
          this.value = "";
        }
        this.value = this.value.replace(/[^\d.]/g, ""); //清除“数字”和“.”以外的字符
        this.value = this.value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
      };
      areaDom.append(newObj);
      newObj.focus();
      newObj.onblur = function () {
        if (this.value !== oldValue && this.value !== '' && this.value !== null) {
          this.value = (this.value - 0).toFixed(2);
          updateRepairModule(id, this.value).then(res => {
            _this.$message({
              message: "维修面积修改成功!",
              type: "success"
            });
            _this.repairInfoDetail.repairModuleArea = this.value ? this.value : oldValue;
            this.remove();
            _this.editing = false;
          }).catch(err => {
            const msg = err.result ? err.result : '维修面积修改失败';
            _this.$message({
              message: msg,
              type: "error"
            });
            _this.repairInfoDetail.repairModuleArea = oldValue;
            this.remove();
            _this.editing = false;
          })
        } else if (this.value === '' || this.value === null) {
          _this.$message({
            message: "维修面积不能为空!",
            type: "warning"
          });
          _this.repairInfoDetail.repairModuleArea = oldValue;
          this.remove();
          _this.editing = false;
        } else {
          _this.repairInfoDetail.repairModuleArea = oldValue;
          this.remove();
          _this.editing = false;
        }
      }
    },
    // 删除维修模块
    deleteRepairModule() {
      if (this.activeRepairModuleId !== "" && this.activeRepairModuleId !== null) {
        this.$confirm("此操作将永久删除此维修模块, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            deleteRepairModule(this.activeRepairModuleId).then(res => {
              this.$message({
                message: "维修模块删除成功!",
                type: "success"
              });
              this.repairInfoList.splice(this.activeInfo, 1);
              if (this.repairInfoList.length > 0) {
                this.showInfoDetail(this.repairInfoList[0].id, 0);
              } else {
                this.showRepairModuleInfo = false;
                this.currentForm.auditState = 2;
                const ruleName = `rules_${this.currentDialogName}`;
                if (this[ruleName].comment.length === 0) {
                  this[ruleName].comment.push({ required: true, message: "审批意见不能为空", trigger: "blur" });
                }
              }
            }).catch(err => {
              const msg = err.result ? err.result : '维修模块删除失败';
              this.$message({
                message: msg,
                type: "error"
              });
            })
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          });
      }
    },
    // 审批单选改变
    auditStateRadioChange(val) {
      const formName = `Form_${this.currentDialogName}`;
      const ruleName = `rules_${this.currentDialogName}`;
      this.$refs[formName].clearValidate(['auditState']);
      if (this.currentDialogName === 'leaderApprove') {
        if (val === 2 || val === 3) {
          if (this[ruleName].comment.length === 0) {
            this[ruleName].comment.push({ required: true, message: "审批意见不能为空", trigger: "blur" });
          }
        } else {
          this[ruleName].comment = [];
          this.$refs[formName].clearValidate(['comment']);
        }
      } else {
        if (val === 2) {
          if (this[ruleName].comment.length === 0) {
            this[ruleName].comment.push({ required: true, message: "审批意见不能为空", trigger: "blur" });
          }
        } else {
          this[ruleName].comment = [];
          this.$refs[formName].clearValidate(['comment']);
        }
      }
    },
    // 维修信息详情
    showInfoDetail(id, index) {
      this.clearForm(this.repairInfoDetail);
      this.activeInfo = index;
      this.activeRepairModuleId = id;
      getRepairMessageDetail(id).then(res => {
        for ([key, value] of Object.entries(res)) {
          if (this.repairInfoDetail[key] !== undefined) {
            this.repairInfoDetail[key] = value;
          }
        }
        this.repairInfoDetail.repairSite = res.positionStr;
        this.repairInfoDetail.repairModuleId = id;
      }).catch(err => {
        const msg = err.result ? err.result : '维修信息详情获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
      this.getRepairRecord(id);
    },
    // 维修记录详情
    showRecordDetail(index) {
      this.clearForm(this.repairRecordDetail);
      this.activeRecord = index;
      this.popoverRepairRecords = false;
      for ([key, value] of Object.entries(this.repairRecordList[index])) {
        if (this.repairRecordDetail[key] !== undefined) {
          if (key === 'repairUnitNameList') {
            this.repairRecordDetail[key] = value.join(',');
          } else if (key === 'completionTime' || key === 'completionQuality') {
            this.repairRecordDetail[key] = this.formatDate(value);
          } else {
            this.repairRecordDetail[key] = value;
          }
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
    // 开始上传文件
    submitUpload() {
      if (this.currentForm.step === 8 || this.currentForm.step === 22) {
        this.$refs.upload.submit();
        this.$refs.uploadImg.submit();
      } else {
        this.$refs.upload.submit();
      }
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
        this.currentForm.physicalAddressList.push(file.url);
        this.currentForm.basicDBObject.repairApprovalDataList.forEach((item, index) => {
          if (item.id === file.uid) {
            this.currentForm.basicDBObject.repairApprovalDataList.splice(index, 1);
          }
        });
      }
      this.uploadNum = fileList.length - this.uploadedNum;
      this.fileList = fileList;
    },
    handleImgRemove(file, fileList) {
      if (file.status === 'success') {
        this.uploadedImgNum--;
        this.currentForm.physicalAddressList.push(file.name);
        this.currentForm.basicDBObject.repairApprovalDataList.forEach((item, index) => {
          if (item.id === file.uid) {
            this.currentForm.basicDBObject.repairApprovalDataList.splice(index, 1);
          }
        })
      }
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
      this.fileListImg = fileList;
    },
    // 上传文件成功
    handleSuccess(response, file, fileList) {
      this.uploadSuccessNum++;
      const fileType = file.raw.type.split('/')[0];
      if (fileType === 'image') {
        this.currentForm.basicDBObject.repairApprovalDataList.push({
          type: 1,
          id: response.imgList[0].id,
          physicalAddress: response.imgList[0].imgPath,
          virtualAddress: response.imgList[0].imgUrl,
          repairFileName: file.name,
          name: `${this.currentDialogName}_file`
        });
      } else {
        this.currentForm.basicDBObject.repairApprovalDataList.push({
          type: 2,
          id: response.imgList[0].id,
          physicalAddress: response.imgList[0].imgPath,
          virtualAddress: response.imgList[0].imgUrl,
          repairFileName: file.name,
          name: `${this.currentDialogName}_file`
        });
      }
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
      let type;
      const fileType = file.raw.type.split('/')[0];
      type = fileType !== 'image' ? 3 : 1;
      this.currentForm.basicDBObject.repairApprovalDataList.push({
        type: type, // 1图片 2文件 3压缩文件
        id: response.imgList[0].id,
        physicalAddress: response.imgList[0].imgPath,
        virtualAddress: response.imgList[0].imgUrl,
        repairFileName: file.name,
        name: `${this.currentDialogName}_img`
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
      this.fileList = fileList;
      // 清除上传验证
      const formName = `Form_${this.currentDialogName}`
      switch (this.currentForm.step) {
        case 4:
          if (this.fileList.length !== 0) {
            this.$refs[formName].clearValidate(['repairApprovalDataList']);
          }
          break;
        case 5:
          if (this.fileList.length !== 0) {
            this.$refs[formName].clearValidate(['repairApprovalDataList']);
          }
          break;
        case 19:
          if (this.fileList.length !== 0) {
            this.$refs[formName].clearValidate(['repairApprovalDataList']);
          }
          break;
        case 8:
          if (this.fileList.length !== 0 || this.fileListImg.length !== 0) {
            this.$refs[formName].clearValidate(['repairApprovalDataList']);
          }
          break;
        case 21:
          if (this.fileList.length !== 0 || this.fileListImg.length !== 0) {
            this.$refs[formName].clearValidate(['repairApprovalDataList']);
          }
          break;
        case 22:
          if (this.fileList.length !== 0 || this.fileListImg.length !== 0) {
            this.$refs[formName].clearValidate(['repairApprovalDataList']);
          }
          break;
        case 23:
          if (this.fileList.length !== 0 || this.fileListImg.length !== 0) {
            this.$refs[formName].clearValidate(['repairApprovalDataList']);
          }
          break;
        case 24:
          if (this.fileList.length !== 0 || this.fileListImg.length !== 0) {
            this.$refs[formName].clearValidate(['repairApprovalDataList']);
          }
          break;
      }
    },
    handleImgChange(file, fileList) {
      const fileType = file.raw.type.split('/')[0];
      if (fileType !== 'image') {
        file.url = `${BASE_URL}${url_name_1}/images/compress.png`;
      }
      this.uploadImgChangeFlag = true;
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
      this.fileListImg = fileList;
      // 清除上传验证
      const formName = `Form_${this.currentDialogName}`
      switch (this.currentForm.step) {
        case 8:
          if (this.fileList.length !== 0 || this.fileListImg.length !== 0) {
            this.$refs[formName].clearValidate(['repairApprovalDataList']);
          }
          break;
        case 22:
          if (this.fileList.length !== 0 || this.fileListImg.length !== 0) {
            this.$refs[formName].clearValidate(['repairApprovalDataList']);
          }
          break;
      }
    },
    // 上传文件超出限制个数
    handleExceed(file, fileList) {
      this.$message.warning(`最多只能上传五个文件！`);
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
      const msg = err.result ? err.result : '上传失败';
      this.$message({
        message: msg,
        type: "error"
      });
    },
    // 文件下载
    fileDownLoad(list, type) {
      if (!list || list.length === 0) {
        this.$message({
          message: "请先选择一个文件",
          type: "warning"
        });
        return;
      }
      downloadFile(list, type, this.currentForm.repairId).then(res => {
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
    // 表格选择
    tableRowClick(row) {
      const total = (this.currentForm.repairHttpResultVo.common_quantity - 0) * (this.currentForm.repairHttpResultVo.common_price - 0);
      if (total <= (row.balance_amount - 0)) {
        this.currentForm.repairHttpResultVo.detailid = row.id;
        this.inputText = row.title;
      } else {
        this.$message({
          message: '采购单价和采购数量的乘积不能超过可用余额！',
          type: "warning"
        });
        this.currentForm.repairHttpResultVo.detailid = '';
        this.inputText = '';
      }
    },
    // dialog打开的回调
    dialogOpen() {
      this.$nextTick(() => {
        const formName = `Form_${this.currentDialogName}`
        if (this.$refs[formName]) {
          this.$refs[formName].clearValidate();
        }
      })
    },
    // dialog关闭的回调
    dialogClosed() {
      const name = `rules_${this.currentDialogName}`;
      if (this[name].comment !== undefined) {
        this[name].comment = [];
      }
      this.currentDialogName = '';
      this.operateType = '';
      this.repairInfoList = [];
      this.repairRecordList = [];
      this.baseInfo = [];
      this.activeInfo = 0;
      this.activeRecord = 0;
      this.showRepairModuleInfo = true;
      this.showRepairRecord = false;
      this.allRepairArea = null;
      this.unitAreaPrice = null;
      this.downLoadList = [];
      this.fileDownLoadCheckedList = [];
      this.downloadCheckedList = [];
      this.officeTableData = [];
      // 清空上传
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
      this.uploadTips = '';
      this.commentInfo = '';
      // 清空表单
      this.clearForm(this.repairInfoDetail);
      this.clearForm(this.repairRecordDetail);
      this.clearForm(this.projectBudgetDetail);
      this.clearForm(this.currentForm);
      this.currentForm.auditState = 0;
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
    limitFloat(key, flag, callback) {
      this.currentForm[key] = this.currentForm[key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '')
      if (flag && this.currentForm[key] !== '') {
        this.currentForm[key] = (this.currentForm[key] - 0).toFixed(2);
      }
      if (callback) {
        callback();
      }
    },
    // 只能输入正浮点数(零星维修)
    limitFloat_2(key, flag) {
      this.currentForm.repairHttpResultVo[key] = this.currentForm.repairHttpResultVo[key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '')
      if (flag && this.currentForm.repairHttpResultVo[key] !== '') {
        this.currentForm.repairHttpResultVo[key] = (this.currentForm.repairHttpResultVo[key] - 0).toFixed(2);
        this.currentForm.repairHttpResultVo.detailid = '';
        this.inputText = '';
      }
    },
    // 只能输入正整数
    limitInt(key1, key2) {
      if (key2) {
        this.currentForm[key2][key1] = this.currentForm[key2][key1].replace(/[^0-9]/g, "");
      } else {
        this.currentForm[key1] = this.currentForm[key1].replace(/[^0-9]/g, "");
      }
      if (this.currentForm.repairHttpResultVo.detailid || this.inputText) {
        this.currentForm.repairHttpResultVo.detailid = '';
        this.inputText = '';
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
    },
    // 获取图片链接
    getImgUrl(url) {
      return `${BASE_URL}${url_name_1}${url}?access_token=${this.token}`;
    }
  },
  mounted() {
    this.getList();
    this.token = sessionStorage.getItem('TOKEN');
    this.username = sessionStorage.getItem('username');
    if (this.token) {
      this.postHeaders.Authorization = `Bearer ${this.token}`;
    }
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - queryBoxH - operateBoxH;
    }, 1);

    this.pickerOptions_start.disabledDate = (time) => {
      if (this.endApplicationTime) {
        return new Date(this.endApplicationTime).getTime() < time.getTime();
      }
    }
    this.pickerOptions_end.disabledDate = (time) => {
      if (this.startApplicationTime) {
        return new Date(this.startApplicationTime).getTime() > time.getTime();
      }
    }
  }
};
</script>

<style>
#waittingThing .query-box {
  margin-bottom: 20px;
}
#waittingThing .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 18%;
}
#waittingThing .query-item > div {
  float: left;
}
#waittingThing .query-item > .query-input {
  width: calc(100% - 80px);
}
#waittingThing .query-item .el-select {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#waittingThing .query-item .el-date-editor.el-input,
#waittingThing .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#waittingThing .query-btn {
  float: right;
}
#waittingThing .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#waittingThing .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#waittingThing .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#waittingThing .table-operate {
  color: #3891e0;
  cursor: pointer;
}
#waittingThing .table-operate:hover {
  color: #035cac;
}
#waittingThing .info-detail-title {
  border-bottom: 1px solid #d8d8d8;
}
#waittingThing .info-detail-title > span {
  font-size: 16px;
  line-height: 36px;
  float: left;
  border-bottom: 1px solid #498fea;
  position: relative;
  top: 1px;
}
#waittingThing .info-detail-title > .info-detail-btn {
  float: right;
}
#waittingThing .info-detail-btn > span {
  display: inline-block;
  line-height: 36px;
  font-size: 16px;
}
#waittingThing .info-detail-btn > span + span {
  margin-left: 20px;
}
#waittingThing .more-btn {
  color: #3891e0;
  cursor: pointer;
}
#waittingThing .more-btn:hover {
  color: #035cac;
}
#waittingThing .el-dialog--center .el-dialog__body {
  padding: 0;
}
#waittingThing .repair-records-box,
#waittingThing .repair-approve-box {
  padding: 25px 25px 30px;
}
#waittingThing .info-detail-content {
  padding-left: 30px;
}
#waittingThing .info-detail-content > div {
  line-height: 40px;
}
#waittingThing .info-detail-content > div > span:first-of-type {
  width: 56px;
  display: inline-block;
  text-align: justify;
  text-align-last: justify;
}
#waittingThing .charge-form .el-select {
  width: calc(100% - 135px);
}
#waittingThing .charge-form .el-select .el-input__inner {
  width: 100%;
}
.more-info {
  background: #f5f5f5;
  padding: 0;
}
.repair-info-more > li {
  line-height: 30px;
  padding: 0 10px;
  cursor: pointer;
}
.repair-info-more > li:hover {
  background: #498fea;
  color: #fff;
}
.repair-info-more > .info-active {
  background: #498fea;
  color: #fff;
}
#waittingThing .el-form-item__label {
  font-size: 16px;
}
#waittingThing .approve-advices-title {
  position: relative;
  top: -78px;
}
#waittingThing .img-preview-bigger {
  width: 100%;
  height: 100%;
  position: fixed;
  z-index: 100000;
  top: 0;
  left: 0;
  display: none;
}
#waittingThing .img-preview-bigger > .img-preview-bigger-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
}
#waittingThing .img-preview-bigger > img {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
}
#waittingThing .img-preview-bigger > i {
  position: absolute;
  font-size: 50px;
  top: 30px;
  right: 30px;
  color: #ffffff;
  cursor: pointer;
}
#waittingThing .img-preview-bigger > i:hover {
  color: #3891e0;
}
#waittingThing .repaire-file-upload .el-upload {
  margin-top: 40px;
}
#waittingThing .repaire-file-upload {
  position: relative;
  min-width: 110px;
}
#waittingThing .upload-icon {
  font-size: 60px;
  position: absolute;
  top: 0;
  left: 20px;
  color: #dfdfdf;
}
#waittingThing .download-icon {
  font-size: 60px;
  color: #dfdfdf;
  display: block;
  padding-left: 20px;
  padding-bottom: 5px;
}
#waittingThing .el-upload__tip {
  position: absolute;
  width: 150px;
  left: 116px;
  top: 34px;
}
#waittingThing .comment-info {
  color: #ff0000;
}
#waittingThing .project-introduction > .el-form-item__content {
  width: calc(100% - 105px);
}
#waittingThing .el-form-item__content > span {
  font-size: 16px;
}
#waittingThing .el-form--inline .el-form-item {
  margin-right: 0px;
}
#waittingThing .design-img-item {
  width: 100px;
  height: 100px;
  float: left;
  margin-right: 15px;
  position: relative;
}
#waittingThing .design-img-item > img {
  width: 100%;
  height: 100%;
  position: relative;
  top: 12px;
}
#waittingThing .bigger-view-warpper {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 12px;
  left: 0;
}
#waittingThing .bigger-view-warpper > .bigger-view-warpper-inner {
  width: 100%;
  height: 100%;
  background: #000;
  opacity: 0.5;
  display: none;
}
#waittingThing .bigger-view-warpper:hover > .bigger-view-warpper-inner {
  display: block;
}
#waittingThing .bigger-view-warpper > i {
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
#waittingThing .bigger-view-warpper > i:hover {
  color: #3891e0;
}
#waittingThing .bigger-view-warpper:hover > i {
  display: block;
}
#waittingThing .officeApprove .el-form-item .el-input__inner,
#waittingThing .projectApprove .el-form-item .el-input__inner {
  width: 210px;
}
</style>


