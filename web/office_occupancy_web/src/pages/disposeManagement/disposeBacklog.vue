<template>
  <div id="disposeBacklog">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">处置位置：</div>
          <div class="query-input">
            <search-tree-select
              @handleCheckChange="handleSearchCheckChange"
              :treeData="allTreeData"
              :treeShow="true"
              :inputWidth="'100%'"
              :inputHeight="'32px'"
              :treeName="this.searchTreeName"
            ></search-tree-select>
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
          prop="yardPosition"
          label="处置院落"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="buildPosition"
          label="处置楼座"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="handleApplicant"
          label="申请人"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="handleDate"
          label="申请时间"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="handleArea"
          label="处置面积(m²)"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <div class="table-operate" @click="openDialog(scope.row)">
              <span class="iconfont">&#xe695;</span>
              <span>{{scope.row.applyStatusText}}</span>
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
    <!-- 申请初审 -->
    <el-dialog
      :visible.sync="dialogVisible.firstTrial"
      :close-on-click-modal="false"
      width="780px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
      class="firstTrial-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <div class="dialog-content">
        <div class="content-item">
          <span>处置院落：</span>
          <span>{{commonInfo.yardPosition}}</span>
        </div>
        <div class="content-item">
          <span>处置位置：</span>
          <span>{{commonInfo.handlePosition}}</span>
        </div>
        <div class="content-item">
          <span>处置面积：</span>
          <span>{{commonInfo.handleArea}}m²</span>
        </div>
        <div class="content-item">
          <span>当前状态：</span>
          <span>{{commonInfo.applyStatusText}}</span>
        </div>
        <div class="content-item">
          <span>申请时间：</span>
          <span>{{commonInfo.handleDate}}</span>
        </div>
        <div class="content-item">
          <span>申请单位：</span>
          <span>{{commonInfo.unitName}}</span>
        </div>
        <div class="content-item">
          <span>申请人：</span>
          <span>{{commonInfo.handleApplicant}}</span>
        </div>
        <div class="content-item">
          <span>申请原因：</span>
          <span>{{commonInfo.applyReason}}</span>
        </div>
        <el-form :model="Form_firstTrial" :rules="rules_firstTrial" ref="Form_firstTrial">
          <el-form-item label="处置方式：" label-width="80px" prop="auditState" class="handleWay">
            <el-radio
              v-model="Form_firstTrial.auditState"
              label="1-1"
              @change="firstTrialRadioChange"
              :name="currentDialogName"
            >转换用途</el-radio>
            <el-radio
              v-model="Form_firstTrial.auditState"
              label="1-2"
              @change="firstTrialRadioChange"
              :name="currentDialogName"
            >出租</el-radio>
            <el-radio
              v-model="Form_firstTrial.auditState"
              label="1-6"
              @change="firstTrialRadioChange"
              :name="currentDialogName"
            >出借</el-radio>
            <el-radio
              v-model="Form_firstTrial.auditState"
              label="1-7"
              @change="firstTrialRadioChange"
              :name="currentDialogName"
              v-if="commonInfo.isHouse !== 1"
            >征收</el-radio>
            <el-radio
              v-model="Form_firstTrial.auditState"
              label="1-3"
              @change="firstTrialRadioChange"
              :name="currentDialogName"
              v-if="commonInfo.isHouse !== 1"
            >拍卖</el-radio>
            <el-radio
              v-model="Form_firstTrial.auditState"
              label="1-4"
              @change="firstTrialRadioChange"
              :name="currentDialogName"
              v-if="commonInfo.isHouse !== 1"
            >拆除</el-radio>
            <el-radio
              v-model="Form_firstTrial.auditState"
              label="2-5"
              @change="firstTrialRadioChange"
              :name="currentDialogName"
            >不处置</el-radio>
          </el-form-item>
          <el-form-item label="处置意见：" label-width="80px" prop="comment" class="comment">
            <el-input
              v-model="Form_firstTrial.comment"
              type="textarea"
              :rows="4"
              placeholder="请输入处置意见"
              class="approve-advices-textarea"
              v-enter-trim
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.firstTrial= false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form_firstTrial')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /申请初审 -->

    <!-- 财政或人民政府审批 -->
    <el-dialog
      :visible.sync="dialogVisible.finance"
      :close-on-click-modal="false"
      width="780px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
      class="inline-dialog finance-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <div class="dialog-content">
        <div class="clearfix">
          <div class="content-item">
            <span>处置院落：</span>
            <span>{{commonInfo.yardPosition}}</span>
          </div>
          <div class="content-item">
            <span>处置面积：</span>
            <span>{{commonInfo.handleArea}}m²</span>
          </div>
          <div class="content-item">
            <span>当前状态：</span>
            <span>{{commonInfo.applyStatusText}}</span>
          </div>
          <div class="content-item">
            <span>申请时间：</span>
            <span>{{commonInfo.handleDate}}</span>
          </div>
          <div class="content-item">
            <span>申请单位：</span>
            <span>{{commonInfo.unitName}}</span>
          </div>
          <div class="content-item">
            <span>申请人：</span>
            <span>{{commonInfo.handleApplicant}}</span>
          </div>
          <div class="content-item block-item">
            <span>申请原因：</span>
            <span>{{commonInfo.applyReason}}</span>
          </div>
          <div class="content-item block-item">
            <span>处置位置：</span>
            <span>{{commonInfo.handlePosition}}</span>
          </div>
        </div>
        <div class="clearfix" style="margin-top:22px;">
          <div class="content-item">
            <span>处置方式：</span>
            <span>{{initApplyDb.handleWayName}}</span>
          </div>
          <div class="content-item">
            <span>初审人：</span>
            <span>{{initApplyDb.initApplyUser}}</span>
          </div>
          <div class="content-item block-item">
            <span>处置意见：</span>
            <span>{{initApplyDb.initApplyComment ? initApplyDb.initApplyComment : '暂无'}}</span>
          </div>
        </div>
        <el-form :model="Form_finance" :rules="rules_finance" ref="Form_finance">
          <el-form-item label="处置方式：" label-width="80px" prop="auditState">
            <el-radio
              v-model="Form_finance.auditState"
              :label="1"
              @change="financeRadioChange"
              :name="currentDialogName"
            >同意处置</el-radio>
            <el-radio
              v-model="Form_finance.auditState"
              :label="2"
              @change="financeRadioChange"
              :name="currentDialogName"
            >不同意处置</el-radio>
          </el-form-item>
          <el-form-item label="审批意见：" label-width="80px" prop="comment">
            <el-input
              v-model="Form_finance.comment"
              type="textarea"
              :rows="4"
              placeholder="请输入审批意见"
              class="approve-advices-textarea"
              v-enter-trim
            ></el-input>
          </el-form-item>
          <el-form-item label="审批资料：" label-width="80px" prop="basicDBObject" style="width:100%">
            <div class="repaire-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=financeApproval`"
                :file-list="fileList"
                :auto-upload="false"
                :limit="limit"
                :accept="acceptType"
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
        <cancel-btn @clickBtn="dialogVisible.finance = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form_finance')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /财政或人民政府审批 -->

    <!-- 转换用途 -->
    <el-dialog
      :visible.sync="dialogVisible.purposes"
      :close-on-click-modal="false"
      width="780px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
      class="inline-dialog purposes-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <div class="dialog-content">
        <div class="clearfix">
          <div class="content-item">
            <span>处置院落：</span>
            <span>{{commonInfo.yardPosition}}</span>
          </div>
          <div class="content-item">
            <span>处置面积：</span>
            <span>{{commonInfo.handleArea}}m²</span>
          </div>
          <div class="content-item">
            <span>申请时间：</span>
            <span>{{commonInfo.handleDate}}</span>
          </div>
          <div class="content-item">
            <span>处置方式：</span>
            <span>转换用途</span>
          </div>
          <div class="content-item block-item">
            <span>处置位置：</span>
            <span>{{commonInfo.handlePosition}}</span>
          </div>
        </div>
        <el-form :model="Form_purposes" :rules="rules_purposes" ref="Form_purposes">
          <el-form-item label="转换后用途：" label-width="94px" prop="ponUse">
            <el-input v-model="Form_purposes.ponUse" placeholder="请输入转换后用途" size="small"></el-input>
          </el-form-item>
          <el-form-item label="备注：" label-width="94px" prop="comment">
            <el-input
              v-model="Form_purposes.comment"
              type="textarea"
              :rows="4"
              placeholder="请输入备注"
              class="approve-advices-textarea"
              v-enter-trim
            ></el-input>
          </el-form-item>
          <el-form-item label="资料登记：" label-width="94px" prop="basicDBObject" style="width:100%">
            <div class="repaire-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=conversionPurposes`"
                :file-list="fileList"
                :auto-upload="false"
                :limit="limit"
                :accept="acceptType"
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
        <cancel-btn @clickBtn="dialogVisible.purposes = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form_purposes')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /转换用途 -->

    <!-- 拍卖 -->
    <el-dialog
      :visible.sync="dialogVisible.auction"
      :close-on-click-modal="false"
      width="780px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
      class="inline-dialog auction-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <div class="dialog-content">
        <div class="clearfix">
          <div class="content-item">
            <span>处置院落：</span>
            <span>{{commonInfo.yardPosition}}</span>
          </div>
          <div class="content-item">
            <span>处置面积：</span>
            <span>{{commonInfo.handleArea}}m²</span>
          </div>
          <div class="content-item">
            <span>申请时间：</span>
            <span>{{commonInfo.handleDate}}</span>
          </div>
          <div class="content-item">
            <span>处置方式：</span>
            <span>拍卖</span>
          </div>
          <div class="content-item block-item">
            <span>处置位置：</span>
            <span>{{commonInfo.handlePosition}}</span>
          </div>
        </div>
        <el-form :model="Form_auction" :rules="rules_auction" ref="Form_auction" inline>
          <el-form-item label="中标人：" label-width="108px" prop="winningBidder">
            <el-input v-model="Form_auction.winningBidder" placeholder="请输入中标人" size="small"></el-input>
          </el-form-item>
          <el-form-item label="申请人：" label-width="108px" prop="contacts">
            <el-input v-model="Form_auction.contacts" placeholder="请输入申请人" size="small"></el-input>
          </el-form-item>
          <el-form-item label="联系电话：" label-width="108px" prop="contactNumber">
            <el-input
              v-model="Form_auction.contactNumber"
              placeholder="请输入联系电话"
              size="small"
              @keyup.native="limitInt('contactNumber')"
              @blur="limitInt('contactNumber')"
            ></el-input>
          </el-form-item>
          <el-form-item label="中标金额(元)：" label-width="108px" prop="winningAmount">
            <el-input
              v-model="Form_auction.winningAmount"
              placeholder="请输入中标金额"
              size="small"
              @keyup.native="limitFloat('winningAmount')"
              @blur="limitFloat('winningAmount', true)"
            ></el-input>
          </el-form-item>
          <el-form-item label="代理机构：" label-width="108px" prop="agency">
            <el-input v-model="Form_auction.agency" placeholder="请输入代理机构" size="small" v-enter-trim></el-input>
          </el-form-item>
          <el-form-item label="代理联系方式：" label-width="108px" prop="agentContact">
            <el-input
              v-model="Form_auction.agentContact"
              placeholder="请输入代理联系方式"
              size="small"
              v-enter-trim
            ></el-input>
          </el-form-item>
          <el-form-item label="拍卖资料：" label-width="108px" prop="basicDBObject" style="width:100%">
            <div class="repaire-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=auction`"
                :file-list="fileList"
                :auto-upload="false"
                :limit="limit"
                :accept="acceptType"
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
        <cancel-btn @clickBtn="dialogVisible.auction = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form_auction')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /拍卖 -->

    <!-- 拆除 -->
    <el-dialog
      :visible.sync="dialogVisible.dismantle"
      :close-on-click-modal="false"
      width="780px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
      class="inline-dialog dismantle-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <div class="dialog-content">
        <div class="clearfix">
          <div class="content-item">
            <span>处置院落：</span>
            <span>{{commonInfo.yardPosition}}</span>
          </div>
          <div class="content-item">
            <span>处置面积：</span>
            <span>{{commonInfo.handleArea}}m²</span>
          </div>
          <div class="content-item">
            <span>申请时间：</span>
            <span>{{commonInfo.handleDate}}</span>
          </div>
          <div class="content-item">
            <span>处置方式：</span>
            <span>拆除</span>
          </div>
          <div class="content-item block-item">
            <span>处置位置：</span>
            <span>{{commonInfo.handlePosition}}</span>
          </div>
        </div>
        <el-form :model="Form_dismantle" :rules="rules_dismantle" ref="Form_dismantle" inline>
          <el-form-item label="实施单位：" label-width="80px" prop="implementationUnit">
            <el-input
              v-model="Form_dismantle.implementationUnit"
              placeholder="请输入实施单位"
              size="small"
              v-enter-trim
            ></el-input>
          </el-form-item>
          <el-form-item label="拆除时间：" label-width="80px" prop="demolitionTime">
            <el-date-picker
              v-model="Form_dismantle.demolitionTime"
              type="date"
              placeholder="选择日期"
              size="small"
              style="width:200px;"
              value-format="yyyy-MM-dd HH:mm:ss"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="备注：" label-width="80px" prop="comment" style="width:100%;">
            <el-input
              v-model="Form_dismantle.comment"
              type="textarea"
              :rows="4"
              placeholder="请输入备注"
              class="approve-advices-textarea"
              style="width:565px;"
              v-enter-trim
            ></el-input>
          </el-form-item>
          <el-form-item label="拆除文件：" label-width="80px" prop="basicDBObject" style="width:100%">
            <div class="repaire-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=dismantle`"
                :file-list="fileList"
                :auto-upload="false"
                :limit="limit"
                :accept="acceptType"
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
        <cancel-btn @clickBtn="dialogVisible.dismantle = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form_dismantle')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /拆除 -->

    <!-- 征收 -->
    <el-dialog
      :visible.sync="dialogVisible.levy"
      :close-on-click-modal="false"
      width="780px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
      class="inline-dialog levy-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <div class="dialog-content">
        <div class="clearfix">
          <div class="content-item">
            <span>处置院落：</span>
            <span>{{commonInfo.yardPosition}}</span>
          </div>
          <div class="content-item">
            <span>处置面积：</span>
            <span>{{commonInfo.handleArea}}m²</span>
          </div>
          <div class="content-item">
            <span>申请时间：</span>
            <span>{{commonInfo.handleDate}}</span>
          </div>
          <div class="content-item">
            <span>处置方式：</span>
            <span>征收</span>
          </div>
          <div class="content-item block-item">
            <span>处置位置：</span>
            <span>{{commonInfo.handlePosition}}</span>
          </div>
        </div>
        <el-form :model="Form_levy" :rules="rules_levy" ref="Form_levy" inline>
          <el-form-item label="房屋征收单位：" label-width="108px" prop="houseLevyUnit">
            <el-input
              v-model="Form_levy.houseLevyUnit"
              placeholder="请输入房屋征收单位"
              size="small"
              v-enter-trim
            ></el-input>
          </el-form-item>
          <el-form-item label="征收补偿款：" label-width="108px" prop="levyCompensation">
            <el-input
              v-model="Form_levy.levyCompensation"
              placeholder="请输入征收补偿款"
              size="small"
              @keyup.native="limitFloat('levyCompensation')"
              @blur="limitFloat('levyCompensation', true)"
            ></el-input>
          </el-form-item>
          <el-form-item label="征收资料：" label-width="108px" prop="basicDBObject" style="width:100%">
            <div class="repaire-file-upload">
              <el-upload
                ref="upload"
                :headers="postHeaders"
                :action="`${uploadUrl}?uploadType=dismantle`"
                :file-list="fileList"
                :auto-upload="false"
                :limit="limit"
                :accept="acceptType"
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
        <cancel-btn @clickBtn="dialogVisible.levy = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form_levy')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /征收 -->
  </div>
</template>

<script>
import {
  getHandleApprovalList,
  selectAllHandlePo,
  saveSubmitHandle,
  leaderReviewDetail,
  getHandleDetail
} from "./api";
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
import searchTreeSelect from "@/components/treeSelect/treeSelect_3"
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
    searchTreeSelect
  },
  data() {
    return {
      searchTreeName: 'disposeSearchTree',
      postHeaders: {
        Authorization: ''
      },
      token: '',
      username: "",
      userId: "",
      houseIdList: [],
      yardIdList: [],
      startTime: "",
      endTime: "",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      selectData: [],
      tableData: [],
      operateType: "",
      uploadUrl: uploadUrl,
      acceptType: "image/jpeg,image/gif,image/png,image/bmp,.doc,.docx,.xlsx,.ppt,.pdf,.rar,.zip",
      treeData: [],
      allTreeData: [],
      dialogVisible: {
        firstTrial: false,
        finance: false,
        purposes: false,
        auction: false,
        dismantle: false,
        levy: false
      },
      currentDialogName: '',
      currentForm: null,
      tipsText: '',
      fileList: [],
      limit: 5,
      uploadNum: 0,
      uploadSuccessNum: 0,
      commonInfo: {
        yardPosition: '',
        applyStatusText: '',
        handleDate: '',
        applyReason: '',
        handleArea: '',
        unitName: '',
        handleApplicant: '',
        handlePosition: '',
        isHouse: null
      },
      initApplyDb: {
        handleWay: null,
        handleWayName: '',
        initApplyAuditState: null,
        initApplyComment: '',
        initApplyTime: "",
        initApplyUser: ""
      },
      Form_firstTrial: {
        auditState: '1-1',
        comment: '',
        handleId: null,
        handleWay: null,
        processInstanceId: null,
        step: null,
        taskId: null,
        userId: null,
        userName: ''
      },
      Form_finance: {
        auditState: 1,
        basicDBObject: [],
        comment: '',
        handleId: null,
        handleWay: null,
        processInstanceId: null,
        step: null,
        taskId: null,
        userId: null,
        userName: ''
      },
      Form_purposes: {
        auditState: 0,
        basicDBObject: [],
        comment: '',
        handleId: null,
        processInstanceId: null,
        step: null,
        taskId: null,
        userId: null,
        userName: '',
        ponUse: ''
      },
      Form_auction: {
        auditState: 0,
        basicDBObject: [],
        handleId: null,
        processInstanceId: null,
        step: null,
        taskId: null,
        userId: null,
        userName: '',
        winningBidder: '',
        contacts: '',
        contactNumber: null,
        winningAmount: null,
        agency: '',
        agentContact: null
      },
      Form_dismantle: {
        auditState: 0,
        basicDBObject: [],
        handleId: null,
        processInstanceId: null,
        step: null,
        taskId: null,
        userId: null,
        userName: '',
        implementationUnit: '',
        demolitionTime: '',
        comment: ''
      },
      Form_levy: {
        auditState: 0,
        basicDBObject: [],
        handleId: null,
        processInstanceId: null,
        step: null,
        taskId: null,
        userId: null,
        userName: '',
        houseLevyUnit: '',
        levyCompensation: null
      },
      rules_firstTrial: {
        auditState: [
          { required: true, message: '请选择处置方式', trigger: "blur" }
        ],
        comment: []
      },
      rules_finance: {
        auditState: [
          { required: true, message: '请选择审批', trigger: "blur" }
        ],
        comment: []
      },
      rules_purposes: {
        ponUse: [
          { required: true, message: '转换后用途不能为空', trigger: "blur" }
        ]
      },
      rules_auction: {
        winningBidder: [
          { required: true, message: '中标人不能为空', trigger: "blur" }
        ],
        contacts: [
          { required: true, message: '申请人不能为空', trigger: "blur" }
        ],
        contactNumber: [
          { required: true, message: '联系电话不能为空', trigger: "blur" }
        ],
        winningAmount: [
          { required: true, message: '中标金额不能为空', trigger: "blur" }
        ],
        agency: [
          { required: true, message: '代理机构不能为空', trigger: "blur" }
        ],
        agentContact: [
          { required: true, message: '代理联系方式不能为空', trigger: "blur" }
        ]
      },
      rules_dismantle: {
        implementationUnit: [
          { required: true, message: '实施单位不能为空', trigger: "blur" }
        ],
        demolitionTime: [
          { required: true, message: '拆除时间不能为空', trigger: "change" }
        ]
      },
      rules_levy: {
        houseLevyUnit: [
          { required: true, message: '房屋征收单位不能为空', trigger: "blur" }
        ],
        levyCompensation: [
          { required: true, message: '征收补偿款不能为空', trigger: "change" }
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
      this.userId = sessionStorage.getItem('userId');
      getHandleApprovalList(this.currentPage, this.pageSize, this.houseIdList, this.yardIdList, this.startTime, this.endTime, this.userId)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach(item => {
            item.buildPosition = '';
            item.yardPosition = '';
            item.buildPositionDtoList.forEach((position, index) => {
              if (index === 0) {
                item.yardPosition = position.yardName;
              }
              if (item.buildPositionDtoList.length === 1) {
                item.buildPosition = position.buildName;
              } else if (item.buildPositionDtoList.length === (index + 1)) {
                item.buildPosition += position.buildName;
              } else {
                item.buildPosition += `${position.buildName}、`;
              }
            });
            item.applyStatusText = this.getApplyStatus(item.step);
            item.handleArea = item.handleArea ? item.handleArea : 0;
          })
          this.loadingEnd();
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '处置申请列表获取失败';
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
      this.houseIdList = [];
      this.yardIdList = [];
      this.startTime = "";
      this.endTime = "";
      eventBus.$emit(`${this.searchTreeName}_clear`);
      this.getList();
    },
    // 获取申请状态 
    getApplyStatus(status) {
      switch (status) {
        case 2:
          return '申请初审';
          break;
        case 3:
          return '财政或人民政府审批';
          break;
        case 4:
          return '转换用途登记';
          break;
        case 5:
          return '拍卖处置登记';
          break;
        case 6:
          return '拆除登记';
          break;
        case 7:
          return '征收登记';
          break;
      }
    },
    // 选择处置楼座位置
    handleSearchCheckChange(checkedNodes, halfCheckedNodes) {
      this.houseIdList = [];
      this.yardIdList = [];
      const nodes = checkedNodes.concat(halfCheckedNodes);
      if (nodes.length > 0) {
        nodes.forEach(node => {
          if (node.type === 3) {
            this.houseIdList.push(node.value);
          } else if (node.type === 0) {
            this.yardIdList.push(node.value);
          }
        })
      }
    },
    // 获取搜索树的数据
    selectAllHandlePo() {
      selectAllHandlePo().then(res => {
        this.allTreeData = res;
      }).catch(err => {
        const msg = err.result ? err.result : '处置位置获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 处置流程
    openDialog(row) {
      for ([key, value] of Object.entries(row)) {
        if (this.commonInfo[key] !== undefined) {
          this.commonInfo[key] = value;
        }
      }
      getHandleDetail(row.handleId).then(res => {
        let positionArr = [];
        this.commonInfo.isHouse = res.isHouse;
        if (res.isHouse === 0) {
          res.buildPositionDtoList.forEach(build => {
            const position = `${build.yardName}-${build.buildName}`
            positionArr.push(position);
          })
        } else {
          const yardName = res.buildPositionDtoList[0].yardName;
          res.buildPositionDtoList.forEach(build => {
            const buildName = build.buildName;
            const floorNameDtoList = res.buildPositionDtoList[0].floorNameDtoList;
            floorNameDtoList.forEach(floor => {
              const floorName = floor.floorName;
              floor.houseNumber.forEach((house => {
                const houseNumber = house.houseNumber;
                const position = `${yardName}-${buildName}-${floorName}层-${houseNumber}`
                positionArr.push(position);
              }))
            });
          })
        }
        this.commonInfo.handlePosition = positionArr.join('，');
      }).catch(err => {
        const msg = err.result ? err.result : `处置相关信息获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
      switch (row.step) {
        case 2:
          this.dialogVisible.firstTrial = true;
          this.currentDialogName = 'firstTrial';
          this.currentForm = this.Form_firstTrial;
          this.operateType = "申请初审";
          this.tipsText = '初审'
          break;
        case 3:
          this.dialogVisible.finance = true;
          this.currentDialogName = 'finance';
          this.currentForm = this.Form_finance;
          this.operateType = "财政或人民政府审批";
          this.tipsText = '审批';
          leaderReviewDetail(row.handleId, 2).then(res => {
            const obj = res[0].initApplyDb;
            switch (obj.handleWay) {
              case 1:
                obj.handleWayName = '转换用途';
                break;
              case 2:
                obj.handleWayName = '出租';
                break;
              case 3:
                obj.handleWayName = '拍卖';
                break;
              case 4:
                obj.handleWayName = '拆除';
                break;
              case 6:
                obj.handleWayName = '出借';
                break;
              case 7:
                obj.handleWayName = '征收';
                break;
            }
            for ([key, value] of Object.entries(obj)) {
              if (this.initApplyDb[key] !== undefined) {
                this.initApplyDb[key] = value;
              }
            }
            this.currentForm.handleWay = this.initApplyDb.handleWay;
          }).catch(err => {
            const msg = err.result ? err.result : `申请信息获取失败`;
            this.$message({
              message: msg,
              type: "error"
            });
          })
          break;
        case 4:
          this.dialogVisible.purposes = true;
          this.currentDialogName = 'purposes';
          this.currentForm = this.Form_purposes;
          this.operateType = "转换用途登记";
          this.tipsText = '登记'
          break;
        case 5:
          this.dialogVisible.auction = true;
          this.currentDialogName = 'auction';
          this.currentForm = this.Form_auction;
          this.operateType = "拍卖处置登记";
          this.tipsText = '登记'
          break;
        case 6:
          this.dialogVisible.dismantle = true;
          this.currentDialogName = 'dismantle';
          this.currentForm = this.Form_dismantle;
          this.operateType = "拆除登记";
          this.tipsText = '登记'
          break;
        case 7:
          this.dialogVisible.levy = true;
          this.currentDialogName = 'levy';
          this.currentForm = this.Form_levy;
          this.operateType = "征收登记";
          this.tipsText = '登记'
          break;
      }
      this.currentForm.handleId = row.handleId;
      this.currentForm.processInstanceId = row.processInstanceId;
      this.currentForm.taskId = row.taskId;
      this.currentForm.step = row.step;
      this.currentForm.userId = this.userId;
      this.currentForm.userName = sessionStorage.getItem('name');
    },
    // 初审单选框改变
    firstTrialRadioChange(val) {
      const formName = `Form_${this.currentDialogName}`;
      const ruleName = `rules_${this.currentDialogName}`;
      if (val.split('-')[0] === '2') {
        if (this[ruleName].comment.length === 0) {
          this[ruleName].comment.push({ required: true, message: "处置意见不能为空", trigger: "blur" });
        }
      } else {
        this[ruleName].comment = [];
        this.$refs[formName].clearValidate(['comment']);
      }
    },
    // 政府审批单选框改变
    financeRadioChange(val) {
      const formName = `Form_${this.currentDialogName}`;
      const ruleName = `rules_${this.currentDialogName}`;
      if (val === 2) {
        if (this[ruleName].comment.length === 0) {
          this[ruleName].comment.push({ required: true, message: "审批意见不能为空", trigger: "blur" });
        }
      } else {
        this[ruleName].comment = [];
        this.$refs[formName].clearValidate(['comment']);
      }
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
    // 保存表单
    saveForm() {
      if (this.currentDialogName === 'firstTrial') {
        const tempArr = this.currentForm.auditState.split('-');
        this.currentForm.auditState = parseInt(tempArr[0]);
        this.currentForm.handleWay = parseInt(tempArr[1]);
      }
      saveSubmitHandle(this.currentForm).then(res => {
        this.$message({
          message: `${this.tipsText}成功`,
          type: "success"
        });
        this.getList();
        this.dialogVisible[this.currentDialogName] = false;
        eventBus.$emit('updateBacklogTips');
      }).catch(err => {
        const msg = err.result ? err.result : `${this.tipsText}失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 开始上传文件
    submitUpload() {
      this.$refs.upload.submit();
    },
    // 上传文件成功
    handleSuccess(response, file, fileList) {
      this.uploadSuccessNum++;
      const fileType = file.raw.type.split('/')[0];
      if (fileType === 'image') {
        this.currentForm.basicDBObject.push({
          type: 1,
          id: response.imgList[0].id,
          physicalAddress: response.imgList[0].imgPath,
          virtualAddress: response.imgList[0].imgUrl,
          repairFileName: file.name
        });
      } else {
        this.currentForm.basicDBObject.push({
          type: 2,
          id: response.imgList[0].id,
          physicalAddress: response.imgList[0].imgPath,
          virtualAddress: response.imgList[0].imgUrl,
          repairFileName: file.name
        });
      }
      if (this.uploadSuccessNum === this.uploadNum) {
        this.saveForm();
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
      this.uploadNum = fileList.length;
    },
    // 上传文件改变时钩子
    handleChange(file, fileList) {
      this.uploadNum = fileList.length
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
    // 上传文件超出限制个数
    handleExceed(file, fileList) {
      this.$message.warning(`最多只能上传五个文件！`);
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
        const formName = `Form_${this.currentDialogName}`
        if (this.$refs[formName]) {
          this.$refs[formName].clearValidate();
        }
      })
    },
    // dialog关闭的回调
    dialogClosed() {
      this.clearForm(this.currentForm);
      this.clearForm(this.commonInfo);
      switch (this.currentDialogName) {
        case 'firstTrial':
          this.currentForm.auditState = '1-1';
          break;
        case 'finance':
          this.currentForm.auditState = 1;
          this.clearForm(this.initApplyDb);
          break;
        case 'purposes':
          this.currentForm.auditState = 0;
          break;
        case 'auction':
          this.currentForm.auditState = 0;
          break;
        case 'dismantle':
          this.currentForm.auditState = 0;
          break;
      }
      this.currentDialogName = '';
      this.currentForm = null;
      this.operateType = '';
      this.fileList = [];
      this.uploadNum = 0;
      this.uploadSuccessNum = 0;
      this.tipsText = '';
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
      this.currentForm[key] = this.currentForm[key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '')
      if (flag && this.currentForm[key] !== '') {
        this.currentForm[key] = (this.currentForm[key] - 0).toFixed(2);
      }
    },
    // 只能输入正整数
    limitInt(key) {
      this.currentForm[key] = this.currentForm[key].replace(/[^0-9]/g, "");
    }
  },
  mounted() {
    this.getList();
    this.selectAllHandlePo();
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
#disposeBacklog .query-box {
  margin-bottom: 20px;
}
#disposeBacklog .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 18%;
}
#disposeBacklog .query-item > div {
  float: left;
}
#disposeBacklog .query-item > .query-input {
  width: calc(100% - 80px);
}
#disposeBacklog .query-item .el-select {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#disposeBacklog .query-item .el-date-editor.el-input,
#disposeBacklog .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#disposeBacklog .query-btn {
  float: right;
}
#disposeBacklog .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#disposeBacklog .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#disposeBacklog .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#disposeBacklog .status-unreviewed {
  color: #ff9f02;
}
#disposeBacklog .status-pass {
  color: #3891e0;
}
#disposeBacklog .view-box {
  cursor: pointer;
  color: #3891e0;
  font-size: 0;
  height: 28px;
  line-height: 28px;
}
#disposeBacklog .view-box:hover > .view-icon,
#disposeBacklog .view-box:hover > .view-text {
  color: #035cac;
}
#disposeBacklog .view-icon {
  font-size: 14px;
  position: relative;
  top: 1px;
}
#disposeBacklog .view-text {
  font-size: 14px;
}
#disposeBacklog .status-notPass {
  color: #f9463f;
}
#disposeBacklog .status-reviewimg {
  color: #1aa97b;
}
#disposeBacklog .detail-item {
  margin-bottom: 25px;
}
#disposeBacklog .detail-item > span {
  float: left;
}
#disposeBacklog .table-operate {
  color: #3891e0;
  cursor: pointer;
}
#disposeBacklog .table-operate > span:first-child {
  position: relative;
  top: 1px;
}
#disposeBacklog .table-operate:hover {
  color: #035cac;
}
#disposeBacklog .content-item {
  margin-bottom: 22px;
  overflow: hidden;
}
#disposeBacklog .content-item > span {
  display: block;
  line-height: 20px;
  float: left;
}
#disposeBacklog .content-item > span:first-child {
  text-align: right;
}
#disposeBacklog .firstTrial-dialog .content-item > span:first-child,
#disposeBacklog .finance-dialog .content-item > span:first-child,
#disposeBacklog .dismantle-dialog .content-item > span:first-child {
  width: 80px;
}
#disposeBacklog .purposes-dialog .content-item > span:first-child {
  width: 94px;
}
#disposeBacklog .auction-dialog .content-item > span:first-child,
#disposeBacklog .levy-dialog .content-item > span:first-child {
  width: 108px;
}
#disposeBacklog .firstTrial-dialog .content-item > span:last-child,
#disposeBacklog .finance-dialog .content-item > span:last-child,
#disposeBacklog .dismantle-dialog .content-item > span:last-child {
  width: calc(100% - 80px);
}
#disposeBacklog .purposes-dialog .content-item > span:last-child {
  width: calc(100% - 94px);
}
#disposeBacklog .auction-dialog .content-item > span:last-child,
#disposeBacklog .levy-dialog .content-item > span:last-child {
  width: calc(100% - 108px);
}
#disposeBacklog .el-form--inline .el-form-item {
  margin-right: 0px;
}
#disposeBacklog .levy-dialog .el-form-item__label {
  padding-right: 0;
}
#disposeBacklog .levy-dialog .el-form-item {
  width: 50%;
  float: left;
}
#disposeBacklog .approve-advices-textarea {
  line-height: 40px;
}
#disposeBacklog .inline-dialog .content-item {
  width: 50%;
  float: left;
}
#disposeBacklog .block-item {
  width: 100% !important;
}
#disposeBacklog .el-textarea__inner {
  line-height: 28px;
}
#disposeBacklog .repaire-file-upload .el-upload {
  margin-top: 40px;
}
#disposeBacklog .repaire-file-upload {
  position: relative;
  min-width: 500px;
}
#disposeBacklog .upload-icon {
  font-size: 60px;
  position: absolute;
  top: 0;
  left: 20px;
  color: #dfdfdf;
}
#disposeBacklog .el-upload__tip {
  position: absolute;
  width: 150px;
  left: 116px;
  top: 34px;
}
#disposeBacklog .auction-dialog .el-form--inline .el-form-item,
#disposeBacklog .dismantle-dialog .el-form--inline .el-form-item {
  float: left;
  width: 50%;
  margin-right: 0;
}
#disposeBacklog .auction-dialog .el-form,
#disposeBacklog .dismantle-dialog .el-form,
#disposeBacklog .levy-dialog .el-form {
  overflow: hidden;
}
#disposeBacklog .el-form-item__label {
  padding: 0;
}
</style>


