<template>
  <div id="repairView">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">维修内容：</div>
          <div class="query-input">
            <el-input v-model="repairTarget" placeholder="请输入维修内容"></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-left:25px;">
          <div class="query-title">申请编号：</div>
          <div class="query-input">
            <el-input v-model="repairApplyNo" placeholder="请输入申请编号"></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-left:25px;">
          <div class="query-title">申请状态：</div>
          <div class="query-input">
            <el-select v-model="applyStatus" placeholder="请选择申请状态" size="small">
              <el-option
                v-for="item in applyStatusOpts"
                :key="item.value"
                :label="item.text"
                :value="item.value"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="query-item" style="margin-left:25px;">
          <div class="query-title">维修时间：</div>
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
        <div class="query-btn">
          <search-btn @clickBtn="search"></search-btn>
          <reset-btn @clickBtn="reset"></reset-btn>
        </div>
      </div>
    </div>
    <div class="table-box">
      <table-component :tableData="tableData" :tableHeight="tableHeight">
        <el-table-column prop="serialNum" label="序号" width="50" align="center"></el-table-column>
        <el-table-column prop="repairApplyNo" label="申请编号" width="180" align="center"></el-table-column>
        <el-table-column
          prop="repairTargetList"
          label="维修内容"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column prop="repairApplyDate" label="申请时间" width="180" align="center"></el-table-column>
        <el-table-column
          prop="repairApplicant"
          label="申请人"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column prop="applyStatus" label="申请状态" width="120" align="center">
          <template slot-scope="scope">
            <div
              :class="{'notAudit' : scope.row.applyStatus==='未审核','passAudit' : scope.row.applyStatus==='审核通过','failedAudit' : scope.row.applyStatus==='审核未通过','Auditing' : scope.row.applyStatus==='审核中'}"
            >
              <span>{{scope.row.applyStatus}}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="90" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowAlter="false"
              :isShowDelete="false"
              :isShowDetail="true"
              @detailClick="singleDetail(scope.row)"
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
    <el-dialog
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="1100px"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>详情</span>
      </div>
      <div class="dialog-content">
        <div class="title-box clearfix" v-if="showInfoFlag">
          <span
            v-for="item in governmentRepairProcess"
            :key="item.stepValue"
            v-show="item.state === 1"
            :class="{'governmentRepairActive': processActiveStep === item.stepValue}"
            @click="changeGovernmentProcess(item.step, item.stepValue, item.stepName)"
          >{{item.stepName}}</span>
        </div>
        <div class="content-box">
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
            @showInfoDetail="showInfoDetail"
          ></repair-info>
          <div class="government-repair-info" v-if="showInfoFlag">
            <!-- 科室审批 -->
            <div v-if="governmentActiveStep === 3" class="info-item-inline clearfix">
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.busApprovalAuditState"
              >
                <span>审批</span>：
                <span>{{governmentActiveObj.busApprovalAuditState}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.busApprovalUser"
              >
                <span>审批人</span>：
                <span>{{governmentActiveObj.busApprovalUser}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.busApprovalTime"
              >
                <span>审批时间</span>：
                <span>{{governmentActiveObj.busApprovalTime}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.busApprovalComment"
                style="width:100%"
              >
                <span>审批意见</span>：
                <span>{{governmentActiveObj.busApprovalComment}}</span>
              </div>
            </div>
            <!-- /科室审批 -->
            <!-- 审批意见 -->
            <div v-if="governmentActiveStep === 4" class="info-item-inline clearfix">
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.dzhApprovalAuditState"
              >
                <span>审批</span>：
                <span>{{governmentActiveObj.dzhApprovalAuditState}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.dzhApprovalUser"
              >
                <span>审批人</span>：
                <span>{{governmentActiveObj.dzhApprovalUser}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.dzhApprovalTime"
              >
                <span>审批时间</span>：
                <span>{{governmentActiveObj.dzhApprovalTime}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.dzhApprovalComment"
                style="width:100%"
              >
                <span>审批意见</span>：
                <span>{{governmentActiveObj.dzhApprovalComment}}</span>
              </div>
              <div
                class="repaire-file-download-box clearfix"
                v-if="governmentFileActiveObj.repairApprovalDataList && governmentFileActiveObj.repairApprovalDataList.length > 0"
              >
                <div class="repaire-file-download-title">审批原件</div>
                <div class="repaire-file-download-colons">：</div>
                <div class="repaire-file-download">
                  <el-checkbox-group
                    v-model="fileDownLoadCheckedList"
                    @change="handleCheckedFileChange"
                  >
                    <div
                      v-for="(file, index) in governmentFileActiveObj.repairApprovalDataList"
                      :key="index"
                      class="repaire-file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                  <el-button
                    size="small"
                    type="primary"
                    @click="fileDownLoad(downloadCheckedList, 4)"
                  >点击下载</el-button>
                </div>
              </div>
            </div>
            <!-- /审批意见 -->
            <!-- 部门维修上传 -->
            <div v-if="governmentActiveStep === 5" class="clearfix">
              <div
                class="repaire-file-download-box clearfix"
                v-if="governmentFileActiveObj.repairApprovalDataList && governmentFileActiveObj.repairApprovalDataList.length > 0"
              >
                <div class="repaire-file-download-title">审批文件</div>
                <div class="repaire-file-download-colons">：</div>
                <div class="repaire-file-download">
                  <el-checkbox-group
                    v-model="fileDownLoadCheckedList"
                    @change="handleCheckedFileChange"
                  >
                    <div
                      v-for="(file, index) in governmentFileActiveObj.repairApprovalDataList"
                      :key="index"
                      class="repaire-file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                  <el-button
                    size="small"
                    type="primary"
                    @click="fileDownLoad(downloadCheckedList, 5)"
                  >点击下载</el-button>
                </div>
              </div>
            </div>
            <!-- /部门维修上传 -->
            <!-- 部门维修审批 -->
            <div v-if="governmentActiveStep === 6">
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.aRepairPlanAuditState"
              >
                <span>审批</span>：
                <span>{{governmentActiveObj.aRepairPlanAuditState}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.aRepairPlanUser"
              >
                <span>审批人</span>：
                <span>{{governmentActiveObj.aRepairPlanUser}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.aRepairPlanTime"
              >
                <span>审批时间</span>：
                <span>{{governmentActiveObj.aRepairPlanTime}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.aRepairPlanComment"
                style="width:100%"
              >
                <span>审批意见</span>：
                <span>{{governmentActiveObj.aRepairPlanComment}}</span>
              </div>
            </div>
            <!-- /部门维修审批 -->
            <!-- 预算维修上传 -->
            <div v-if="governmentActiveStep === 19" class="clearfix">
              <div
                class="repaire-file-download-box clearfix"
                v-if="governmentFileActiveObj.repairApprovalDataList && governmentFileActiveObj.repairApprovalDataList.length > 0"
              >
                <div class="repaire-file-download-title">审批文件</div>
                <div class="repaire-file-download-colons">：</div>
                <div class="repaire-file-download">
                  <el-checkbox-group
                    v-model="fileDownLoadCheckedList"
                    @change="handleCheckedFileChange"
                  >
                    <div
                      v-for="(file, index) in governmentFileActiveObj.repairApprovalDataList"
                      :key="index"
                      class="repaire-file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                  <el-button
                    size="small"
                    type="primary"
                    @click="fileDownLoad(downloadCheckedList, 19)"
                  >点击下载</el-button>
                </div>
              </div>
            </div>
            <!-- /预算维修上传 -->
            <!-- 预算维修审批 -->
            <div v-if="governmentActiveStep === 20">
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.ysPlanAuditState"
              >
                <span>审批</span>：
                <span>{{governmentActiveObj.ysPlanAuditState}}</span>
              </div>
              <div class="info-item" v-if="governmentActiveObj && governmentActiveObj.ysPlanUser">
                <span>审批人</span>：
                <span>{{governmentActiveObj.ysPlanUser}}</span>
              </div>
              <div class="info-item" v-if="governmentActiveObj && governmentActiveObj.ysPlanTime">
                <span>审批时间</span>：
                <span>{{governmentActiveObj.ysPlanTime}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.ysPlanComment"
                style="width:100%"
              >
                <span>审批意见</span>：
                <span>{{governmentActiveObj.ysPlanComment}}</span>
              </div>
            </div>
            <!-- /预算维修审批 -->
            <!-- 可行性研究 -->
            <div v-if="governmentActiveStep === 8" class="info-item-inline clearfix">
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.repairProject"
                style="width:33%"
              >
                <span>项目名称</span>：
                <span>{{governmentActiveObj.repairProject}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.budgetEstimate"
                style="width:33%"
              >
                <span>维修概算</span>：
                <span>{{governmentActiveObj.budgetEstimate}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.unitAreaPrice"
                style="width:33%"
              >
                <span style="width:auto;">单位面积价格</span>：
                <span>{{governmentActiveObj.unitAreaPrice}}元</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.projectIntroduction"
                style="width:100%"
              >
                <span>项目简介</span>：
                <span>{{governmentActiveObj.projectIntroduction}}</span>
              </div>
              <div
                class="repaire-file-download-box clearfix"
                v-if="governmentActiveObj.repairApprovalDataList && governmentActiveObj.repairApprovalDataList.file.length > 0"
                style="margin-bottom:20px;"
              >
                <div class="repaire-file-download-title">可行性报告</div>
                <div class="repaire-file-download-colons">：</div>
                <div class="repaire-file-download">
                  <el-checkbox-group
                    v-model="fileDownLoadCheckedList"
                    @change="handleCheckedFileChange"
                  >
                    <div
                      v-for="(file, index) in governmentActiveObj.repairApprovalDataList.file"
                      :key="index"
                      class="repaire-file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                  <el-button
                    size="small"
                    type="primary"
                    @click="fileDownLoad(downloadCheckedList, 8)"
                  >点击下载</el-button>
                </div>
              </div>
              <div
                class="repaire-file-download-box clearfix"
                v-if="governmentActiveObj.repairApprovalDataList && governmentActiveObj.repairApprovalDataList.image.length > 0"
              >
                <div class="repaire-file-download-title">平面图或效果图</div>
                <div class="repaire-file-download-colons">：</div>
                <div class="repaire-file-download clearfix">
                  <el-checkbox-group
                    v-model="imageDownLoadCheckedList"
                    @change="handleCheckedImageChange"
                  >
                    <div
                      class="img-view-item"
                      v-for="(image, index) in governmentActiveObj.repairApprovalDataList.image"
                      :key="index"
                    >
                      <img :src="getImgUrl(image.virtualAddress)" alt="平面图" />
                      <div class="bigger-view-warpper">
                        <div class="bigger-view-warpper-inner"></div>
                        <i class="el-icon-search" @click="preview(image.virtualAddress)"></i>
                      </div>
                      <el-checkbox :label="image.detail">{{null}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                  <div class="img-download-btn">
                    <el-button
                      size="small"
                      type="primary"
                      @click="fileDownLoad(downloadCheckedList_img, 9)"
                    >点击下载</el-button>
                  </div>
                </div>
              </div>
            </div>
            <!-- /可行性研究 -->
            <!-- 发改委审批 -->
            <div v-if="governmentActiveStep === 11" class="info-item-inline clearfix">
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.galAuditState"
              >
                <span>审批</span>：
                <span>{{governmentActiveObj.galAuditState}}</span>
              </div>
              <div class="info-item" v-if="governmentActiveObj && governmentActiveObj.galUser">
                <span>审批人</span>：
                <span>{{governmentActiveObj.galUser}}</span>
              </div>
              <div class="info-item" v-if="governmentActiveObj && governmentActiveObj.galTime">
                <span>审批时间</span>：
                <span>{{governmentActiveObj.galTime}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.galComment"
                style="width:100%"
              >
                <span>审批意见</span>：
                <span>{{governmentActiveObj.galComment}}</span>
              </div>
              <div
                class="repaire-file-download-box clearfix"
                v-if="governmentFileActiveObj.repairApprovalDataList && governmentFileActiveObj.repairApprovalDataList.length > 0"
              >
                <div class="repaire-file-download-title">批示文件</div>
                <div class="repaire-file-download-colons">：</div>
                <div class="repaire-file-download">
                  <el-checkbox-group
                    v-model="fileDownLoadCheckedList"
                    @change="handleCheckedFileChange"
                  >
                    <div
                      v-for="(file, index) in governmentFileActiveObj.repairApprovalDataList"
                      :key="index"
                      class="repaire-file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                  <el-button
                    size="small"
                    type="primary"
                    @click="fileDownLoad(downloadCheckedList, 11)"
                  >点击下载</el-button>
                </div>
              </div>
            </div>
            <!-- /发改委审批 -->
            <!-- 施工设计 -->
            <div v-if="governmentActiveStep === 12" class="info-item-inline clearfix">
              <div class="info-item" v-if="governmentActiveObj && governmentActiveObj.designUnit">
                <span>设计单位</span>：
                <span>{{governmentActiveObj.designUnit}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.planningPermit"
              >
                <span style="width:auto;">规划许可证</span>：
                <span>{{governmentActiveObj.planningPermit}}</span>
              </div>
              <div
                class="repaire-file-download-box clearfix"
                v-if="governmentActiveObj.imgList && governmentActiveObj.imgList.length > 0"
              >
                <div class="repaire-file-download-title">设计图纸</div>
                <div class="repaire-file-download-colons">：</div>
                <div class="repaire-file-download clearfix">
                  <el-checkbox-group
                    v-model="imageDownLoadCheckedList"
                    @change="handleCheckedImageChange"
                  >
                    <div
                      class="img-view-item"
                      v-for="(image, index) in governmentActiveObj.imgList"
                      :key="index"
                    >
                      <img :src="getImgUrl(image.virtualAddress)" alt="设计图纸" />
                      <div class="bigger-view-warpper">
                        <div class="bigger-view-warpper-inner"></div>
                        <i class="el-icon-search" @click="preview(image.virtualAddress)"></i>
                      </div>
                      <el-checkbox :label="image.detail">{{null}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                  <div class="img-download-btn">
                    <el-button
                      size="small"
                      type="primary"
                      @click="fileDownLoad(downloadCheckedList_img, 12)"
                    >点击下载</el-button>
                  </div>
                </div>
              </div>
              <div
                class="repaire-file-download-box clearfix"
                v-if="governmentActiveObj.fileList && governmentActiveObj.fileList.length > 0"
              >
                <div class="repaire-file-download-title">规划许可</div>
                <div class="repaire-file-download-colons">：</div>
                <div class="repaire-file-download">
                  <el-checkbox-group
                    v-model="fileDownLoadCheckedList"
                    @change="handleCheckedFileChange"
                  >
                    <div
                      v-for="(file, index) in governmentActiveObj.fileList"
                      :key="index"
                      class="repaire-file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                  <el-button
                    size="small"
                    type="primary"
                    @click="fileDownLoad(downloadCheckedList, 17)"
                  >点击下载</el-button>
                </div>
              </div>
            </div>
            <!-- /施工设计 -->
            <!-- 维修预算 -->
            <div v-if="governmentActiveStep === 13" class="info-item-inline clearfix">
              <div class="info-item" v-if="governmentActiveObj && governmentActiveObj.auditUnit">
                <span>预算单位</span>：
                <span>{{governmentActiveObj.auditUnit}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.totalBudgetPrice"
              >
                <span style="width:auto;">预算总价(元)</span>：
                <span>{{governmentActiveObj.totalBudgetPrice}}</span>
              </div>
              <div
                class="repaire-file-download-box clearfix"
                v-if="governmentActiveObj.budgetFileList && governmentActiveObj.budgetFileList.length > 0"
              >
                <div class="repaire-file-download-title">预算文件</div>
                <div class="repaire-file-download-colons">：</div>
                <div class="repaire-file-download">
                  <el-checkbox-group
                    v-model="fileDownLoadCheckedList"
                    @change="handleCheckedFileChange"
                  >
                    <div
                      v-for="(file, index) in governmentActiveObj.budgetFileList"
                      :key="index"
                      class="repaire-file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                  <el-button
                    size="small"
                    type="primary"
                    @click="fileDownLoad(downloadCheckedList, 13)"
                  >点击下载</el-button>
                </div>
              </div>
            </div>
            <!-- /维修预算 -->
            <!-- 预算评审 -->
            <div v-if="governmentActiveStep === 14" class="info-item-inline clearfix">
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.accreditationUnit"
              >
                <span style="width:auto;">预算评审单位</span>：
                <span>{{governmentActiveObj.accreditationUnit}}</span>
              </div>
              <div class="info-item" v-if="governmentActiveObj && governmentActiveObj.finalSum">
                <span style="width:auto;">财政评审金额（元）</span>：
                <span>{{governmentActiveObj.finalSum}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.decreaseAmount"
              >
                <span style="width:auto;">下降金额（元）</span>：
                <span>{{governmentActiveObj.decreaseAmount}}</span>
              </div>
              <div
                class="repaire-file-download-box clearfix"
                v-if="governmentActiveObj.approvalFileList && governmentActiveObj.approvalFileList.length > 0"
              >
                <div class="repaire-file-download-title" style="width:auto;">预算评审文件</div>
                <div class="repaire-file-download-colons">：</div>
                <div class="repaire-file-download">
                  <el-checkbox-group
                    v-model="fileDownLoadCheckedList"
                    @change="handleCheckedFileChange"
                  >
                    <div
                      v-for="(file, index) in governmentActiveObj.approvalFileList"
                      :key="index"
                      class="repaire-file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                  <el-button
                    size="small"
                    type="primary"
                    @click="fileDownLoad(downloadCheckedList, 14)"
                  >点击下载</el-button>
                </div>
              </div>
            </div>
            <!-- /预算评审 -->
            <!-- 竣工验收 -->
            <div v-if="governmentActiveStep === 27 || governmentActiveStep === 18">
              <div
                class="repaire-file-download-box clearfix"
                v-if="governmentActiveObj.auditReport && governmentActiveObj.auditReport.length > 0"
              >
                <div class="repaire-file-download-title">竣工报告</div>
                <div class="repaire-file-download-colons">：</div>
                <div class="repaire-file-download">
                  <el-checkbox-group
                    v-model="fileDownLoadCheckedList"
                    @change="handleCheckedFileChange"
                  >
                    <div
                      v-for="(file, index) in governmentActiveObj.auditReport"
                      :key="index"
                      class="repaire-file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                  <el-button
                    size="small"
                    type="primary"
                    @click="fileDownLoad(downloadCheckedList, 15)"
                  >点击下载</el-button>
                </div>
              </div>
              <div
                class="repaire-file-download-box clearfix"
                v-if="governmentActiveObj.buildDrawings && governmentActiveObj.buildDrawings.length > 0"
              >
                <div class="repaire-file-download-title">竣工图纸</div>
                <div class="repaire-file-download-colons">：</div>
                <div class="repaire-file-download clearfix">
                  <el-checkbox-group
                    v-model="imageDownLoadCheckedList"
                    @change="handleCheckedImageChange"
                  >
                    <div
                      class="img-view-item"
                      v-for="(image, index) in governmentActiveObj.buildDrawings"
                      :key="index"
                    >
                      <img :src="getImgUrl(image.virtualAddress)" alt="竣工图纸" />
                      <div class="bigger-view-warpper">
                        <div class="bigger-view-warpper-inner"></div>
                        <i class="el-icon-search" @click="preview(image.virtualAddress)"></i>
                      </div>
                      <el-checkbox :label="image.detail">{{null}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                  <div class="img-download-btn">
                    <el-button
                      size="small"
                      type="primary"
                      @click="fileDownLoad(downloadCheckedList_img, 16)"
                    >点击下载</el-button>
                  </div>
                </div>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.comment"
                style="width:100%"
              >
                <span>备注</span>：
                <span>{{governmentActiveObj.comment}}</span>
              </div>
            </div>
            <!-- /竣工验收 -->
            <!-- 项目日志 -->
            <div
              v-if="governmentActiveStep === 17 && processActiveStep === 14 && showLog"
              class="repair-log-box"
            >
              <div class="info-detail-title clearfix">
                <span>日志信息：</span>
                <div class="info-detail-btn">
                  <el-popover
                    v-model="popoverLogInfo"
                    placement="bottom-end"
                    width="300"
                    trigger="click"
                    popper-class="more-info"
                    :visible-arrow="false"
                  >
                    <ul class="log-info-more">
                      <li
                        v-for="(item, index) in repairLogList"
                        :key="index"
                        :class="{'info-active': logActiveInfo === index}"
                        @click="showLogInfoDetail(item.repairProjectId, index)"
                      >{{item.repairProjectDate}}</li>
                    </ul>
                    <span class="more-btn" slot="reference">
                      {{currentLogTime}}
                      <i class="el-icon-arrow-down"></i>
                    </span>
                  </el-popover>
                </div>
              </div>
              <div class="info-detail-content">
                <div v-if="logInfoDetail.repairProjectPart">
                  <span>施工部位</span>：
                  <span>{{logInfoDetail.repairProjectPart}}</span>
                </div>
                <div v-if="logInfoDetail.repairProjectWeather">
                  <span>天气</span>：
                  <span>{{logInfoDetail.repairProjectWeather}}</span>
                </div>
                <div v-if="logInfoDetail.repairProjectNum">
                  <span>人数</span>：
                  <span>{{logInfoDetail.repairProjectNum}}</span>
                </div>
                <div v-if="logInfoDetail.repairProjectProblems">
                  <span>存在问题</span>：
                  <span>{{logInfoDetail.repairProjectProblems}}</span>
                </div>
                <div v-if="logInfoDetail.repairProjectSecurity">
                  <span>质量技术安全</span>：
                  <span>{{logInfoDetail.repairProjectSecurity}}</span>
                </div>
                <div v-if="logInfoDetail.repairProjectConstruction">
                  <span>文明施工情况</span>：
                  <span>{{logInfoDetail.repairProjectConstruction}}</span>
                </div>
                <div
                  v-show="repairLogImgList.length>0"
                  class="repaire-file-download-box clearfix"
                  v-for="(item, index) in repairLogImgList"
                  :key="index"
                >
                  <div
                    class="repaire-file-download-title"
                    style="width:84px;"
                  >{{item.repairPartName}}</div>
                  <div style="position: relative;top: 40px;">：</div>
                  <div class="repaire-file-download clearfix">
                    <el-checkbox-group
                      v-model="imageDownLoadCheckedList"
                      @change="handleCheckedImageChange"
                    >
                      <div
                        class="img-view-item"
                        v-for="(image, index) in item.repairPmImgList"
                        :key="index"
                      >
                        <img :src="getImgUrl(image.repairPmImgVirtualUrl)" alt="维修图片" />
                        <div class="bigger-view-warpper">
                          <div class="bigger-view-warpper-inner"></div>
                          <i class="el-icon-search" @click="preview(image.repairPmImgVirtualUrl)"></i>
                        </div>
                      </div>
                    </el-checkbox-group>
                  </div>
                </div>
              </div>
            </div>
            <!-- /项目日志 -->
            <!-- 进度付款 -->
            <div
              v-if="governmentActiveStep === 17 && processActiveStep === 13 && showRepairPaymentDetail"
            >
              <div class="info-detail-title clearfix">
                <span>付款进度：</span>
              </div>
              <div class="pay-detail-step">
                <el-steps :active="RepairPaymentActive" align-center finish-status="success">
                  <el-step
                    v-for="(item,index) in RepairPaymentDetail"
                    :key="index"
                    :title="item.stepLabel"
                  >
                    <div slot="description">
                      <div v-show="item.repairPaymentState === 3">
                        <span>付款人：</span>
                        {{item.repairPaymentUsername}}
                      </div>
                      <div v-show="item.repairPaymentMoney">
                        <span>付款金额：</span>
                        {{item.repairPaymentMoney}}元
                      </div>
                      <div v-show="item.repairPaymentState === 3">
                        <span>付款时间：</span>
                        {{item.repairPaymentTime}}
                      </div>
                    </div>
                  </el-step>
                </el-steps>
              </div>
            </div>
            <!-- /进度付款 -->
            <!-- 指定项目负责人 -->
            <div v-if="governmentActiveStep === 16">
              <div class="info-item">
                <span style="width:auto;">项目负责人</span>：
                <span>{{repairName}}</span>
              </div>
            </div>
            <!-- /指定项目负责人 -->
            <!-- 内控采购流程 -->
            <div
              v-if="(governmentActiveStep === 15 && DBStepList.length > 0) || governmentActiveStep === 18"
            >
              <div class="title-box-db clearfix" v-if="governmentActiveStep !== 18">
                <span
                  v-for="item in DBStepList"
                  :key="item.step"
                  :class="{'governmentRepairActive': activeDBStep === item.step}"
                  @click="changeDBProcess(item.step, item.stepTags)"
                >{{item.name}}</span>
              </div>
              <div
                class="info-item-inline clearfix"
                style="margin-bottom:30px;"
                v-for="(item, i) in DBStepDetail"
                :key="i"
              >
                <div class="info-item">
                  <span>经办人</span>：
                  <span>{{item.currentUser}}</span>
                </div>
                <div class="info-item">
                  <span>审批时间</span>：
                  <span>{{item.approvalTime}}</span>
                </div>
                <div class="info-item">
                  <span>审批状态</span>：
                  <span>{{item.approvalState}}</span>
                </div>
                <div class="info-item" v-if="item.comment" style="width:100%">
                  <span>审批意见</span>：
                  <span>{{item.comment}}</span>
                </div>
                <div
                  class="repaire-file-download-box clearfix"
                  v-if="item.approvalFileList && item.approvalFileList.length > 0"
                >
                  <div class="repaire-file-download-title">审批文件</div>
                  <div class="repaire-file-download-colons">：</div>
                  <div class="repaire-file-download">
                    <el-checkbox-group
                      v-model="fileDownLoadCheckedList"
                      @change="handleCheckedFileChange"
                    >
                      <div
                        v-for="(file, index) in item.approvalFileList"
                        :key="index"
                        class="repaire-file-download-item"
                      >
                        <el-checkbox :label="file.detail">{{file.title}}</el-checkbox>
                      </div>
                    </el-checkbox-group>
                    <el-button
                      size="small"
                      type="primary"
                      @click="fileDownLoad(downloadCheckedList)"
                    >点击下载</el-button>
                  </div>
                </div>
              </div>
            </div>
            <!-- /内控采购流程 -->
            <!-- 领导审批 -->
            <div v-if="governmentActiveStep === 9" class="info-item-inline clearfix">
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.ldaAuditState"
              >
                <span>审批</span>：
                <span>{{governmentActiveObj.ldaAuditState}}</span>
              </div>
              <div class="info-item" v-if="governmentActiveObj && governmentActiveObj.ldaUser">
                <span>审批人</span>：
                <span>{{governmentActiveObj.ldaUser}}</span>
              </div>
              <div class="info-item" v-if="governmentActiveObj && governmentActiveObj.ldaTime">
                <span>审批时间</span>：
                <span>{{governmentActiveObj.ldaTime}}</span>
              </div>
              <div
                class="info-item"
                v-if="governmentActiveObj && governmentActiveObj.ldaComment"
                style="width:100%"
              >
                <span>审批意见</span>：
                <span>{{governmentActiveObj.ldaComment}}</span>
              </div>
              <div
                class="repaire-file-download-box clearfix"
                v-if="governmentFileActiveObj.repairApprovalDataList && governmentFileActiveObj.repairApprovalDataList.length > 0"
              >
                <div class="repaire-file-download-title">上传文件</div>
                <div class="repaire-file-download-colons">：</div>
                <div class="repaire-file-download">
                  <el-checkbox-group
                    v-model="fileDownLoadCheckedList"
                    @change="handleCheckedFileChange"
                  >
                    <div
                      v-for="(file, index) in governmentFileActiveObj.repairApprovalDataList"
                      :key="index"
                      class="repaire-file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                  </el-checkbox-group>
                  <el-button
                    size="small"
                    type="primary"
                    @click="fileDownLoad(downloadCheckedList, 10)"
                  >点击下载</el-button>
                </div>
              </div>
            </div>
            <!-- /领导审批 -->
          </div>
        </div>
      </div>
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
import moment from "moment";
import { BASE_URL, url_name_1 } from "@/axios/global";
import {
  getRepairManage,
  getRepairPayment
} from "./api";
import {
  getLeaderReviewList,
  getRepairMessageDetail,
  getRepairApprovalDetail,
  selectRepairType,
  getLeaderReviewDetail,
  downloadFile,
  getProjectReviewDetail,
  getProjectBudgetDetail,
  getDesignReviewDetail,
  getGoverReviewDetail,
  getchargerList,
  getUserList,
  getCompletedProjectInfo,
  getDBStepDetail,
  getDBStepList
} from "./api_2";
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
    repairInfo
  },
  data() {
    return {
      userId: '',
      token: '',
      repairApplyNo: '',
      applyStatus: '',
      repairTarget: '',
      startApplicationTime: '',
      endApplicationTime: '',
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      tableData: [],
      dialogVisible: false,
      showInfoFlag: true,
      applyStatusOpts: [
        { text: "未审核", value: 0 },
        { text: "审核通过", value: 1 },
        { text: "审核未通过", value: 2 },
        { text: "审核中", value: 3 }
      ],
      currentRepairApplyId: '',
      governmentActiveStep: 3,
      processActiveStep: 3,
      governmentActiveName: '',
      governmentFileActiveName: '',
      governmentActiveObj: {},
      governmentFileActiveObj: {},
      governmentRepairProcess: [],
      showRepairModuleInfo: true,
      editing: false,
      activeInfo: 0,
      logActiveInfo: 0,
      baseInfo: [],
      repairInfoList: [],
      repairInfoDetail: {
        repairModuleId: null,
        repairSite: '',
        repairPartName: '',
        repairModuleTarget: '',
        repairModuleArea: '',
        repairImgList: [],
        completionQuality: ''
      },
      governmentRepairInfo: {
        busApprovalCommentDb: {
          busApprovalAuditState: '',
          busApprovalComment: '',
          busApprovalTime: '',
          busApprovalUser: ''
        },
        dzhApprovalCommentDb: {
          dzhApprovalAuditState: '',
          dzhApprovalComment: '',
          dzhApprovalTime: '',
          dzhApprovalUser: ''
        },
        dzhApprovalDb: {
          repairApprovalDataList: []
        },
        researchApprovalDb: {
          projectIntroduction: '',
          repairApprovalDataList: {
            file: [],
            image: []
          },
          repairProject: '',
          budgetEstimate: '',
          unitAreaPrice: ''
        },
        galCommentDb: {
          galAuditState: '',
          galComment: '',
          galTime: '',
          galUser: ''
        },
        governmentApprovalDb: {
          repairApprovalDataList: []
        },
        detailDesignDb: {
          designUnit: '',
          planningPermit: '',
          imgList: [],
          fileList: []
        },
        projectBudgetDb: {
          totalBudgetPrice: '',
          auditUnit: '',
          budgetFileList: []
        },
        projectApproveDb: {
          accreditationUnit: '',
          finalSum: '',
          decreaseAmount: '',
          approvalFileList: []
        },
        repairPlanDb: {
          repairApprovalDataList: []
        },
        aRepairPlanCommentDb: {
          aRepairPlanAuditState: '',
          aRepairPlanComment: '',
          aRepairPlanTime: '',
          aRepairPlanUser: ''
        },
        ysRepairPlanDb: {
          repairApprovalDataList: []
        },
        ysPlanCommentDb: {
          ysPlanAuditState: '',
          ysPlanComment: '',
          ysPlanTime: '',
          ysPlanUser: ''
        },
        constructManageDb: {
          auditReport: [],
          buildDrawings: [],
          comment: ''
        },
        ldaCommentDb: {
          ldaAuditState: "",
          ldaComment: "",
          ldaTime: "",
          ldaUser: "",
        },
        ldaDb: {
          repairApprovalDataList: []
        }
      },
      popoverLogInfo: false,
      repairLogList: [],
      repairLogImgList: [],
      logInfoDetail: {
        repairProjectPart: '',
        repairProjectWeather: '',
        repairProjectNum: '',
        repairProjectProblems: '',
        repairProjectSecurity: '',
        repairProjectConstruction: ''
      },
      currentLogTime: '',
      fileDownLoadCheckedList: [],
      imageDownLoadCheckedList: [],
      downloadCheckedList: [],
      downloadCheckedList_img: [],
      showLog: false,
      showRepairPaymentDetail: false,
      RepairPaymentDetail: [],
      RepairPaymentActive: 0,
      repairName: '',
      chargeOptions: [],
      currentStepName: '',
      pickerOptions_start: {
      },
      pickerOptions_end: {
      },
      DBStepList: [],
      activeDBStep: 1,
      DBStepDetail: [],
      activeDownloadNum: null
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
    // 获取信息列表
    getList() {
      this.loadingStart('.table-box');
      getLeaderReviewList(
        this.currentPage,
        this.pageSize,
        this.repairTarget,
        this.repairApplyNo,
        this.applyStatus,
        this.startApplicationTime,
        this.endApplicationTime
      )
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach((item, index) => {
            item.serialNum = index + 1;
            switch (item.applyStatus) {
              case 0:
                item.applyStatus = "未审核";
                break;
              case 1:
                item.applyStatus = "审核通过";
                break;
              case 2:
                item.applyStatus = "审核未通过";
                break;
              case 3:
                item.applyStatus = "审核中";
                break;
            }
            item.repairTargetList = item.repairTargetList.join(',');
          })
          this.loadingEnd();
        })
        .catch(err => {
          const msg = err.result ? err.result : '维修列表获取失败';
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
      this.currentPage = 1
      this.repairApplyNo = "";
      this.applyStatus = null;
      this.startApplicationTime = "";
      this.endApplicationTime = "";
      this.repairTarget = '';
      this.getList()
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
    // 详情
    singleDetail(row) {
      this.dialogVisible = true;
      this.currentRepairApplyId = row.repairApplyId;
      selectRepairType(row.repairApplyId).then(res => {
        if (res.repairTypeDtoList && res.repairTypeDtoList.length > 0) {
          res.repairTypeDtoList.forEach(item => {
            if (item.step === 11) {
              item.state = res.state;
            } else {
              item.state = 1;
            }
          })
          this.governmentRepairProcess = res.repairTypeDtoList;
          this.showInfoFlag = true;
          this.getLeaderReviewDetail(this.currentRepairApplyId, this.governmentActiveStep);
        } else {
          this.showInfoFlag = false;
        }
      }).catch(err => {
        this.showInfoFlag = false;
        const msg = err.result ? err.result : '维修类型列表获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
      this.getRepairApprovalDetail(this.currentRepairApplyId, true);
    },
    // 内控采购流程步骤
    getDBStepList() {
      getDBStepList(this.currentRepairApplyId).then(res => {
        if (res && res.length > 0) {
          this.DBStepList = res
        } else {
          this.DBStepList = [];
        }
        this.changeDBProcess(res[0].step, res[0].stepTags)
      }).catch(err => {
        this.DBStepList = [];
        const msg = err.result ? err.result : '内控采购流程步骤集合获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 内控流程改变
    changeDBProcess(step, stepTags) {
      this.downloadCheckedList = [];
      this.fileDownLoadCheckedList = [];
      if (step) {
        this.activeDBStep = step;
      }
      switch (step) {
        case 1:
          this.activeDownloadNum = 21;
          break;
        case 2:
          this.activeDownloadNum = 22;
          break;
        case 3:
          this.activeDownloadNum = 23;
          break;
        case 4:
          this.activeDownloadNum = 24;
          break;
        case 5:
          this.activeDownloadNum = 25;
          break;
        case 6:
          this.activeDownloadNum = 26;
          break;
      }
      getDBStepDetail(this.currentRepairApplyId, stepTags).then(res => {
        if (res && res.length > 0) {
          this.DBStepDetail = res
          this.DBStepDetail.forEach(item => {
            item.approvalTime = moment(item.approvalTime * 1000).format("YYYY-MM-DD hh:mm:ss");
            if (item.approvalFileList && item.approvalFileList.length > 0) {
              item.approvalFileList.forEach(file => {
                const arr = file.path.split('/');
                file.title = arr[arr.length - 1];
                file.detail = `${file.path},${file.title}`;
              });
            }
          })
        } else {
          this.DBStepDetail = [];
        }
      }).catch(err => {
        this.DBStepDetail = [];
        const msg = err.result ? err.result : '内控采购流程步骤详情获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 机关事务管理维修流程改变
    changeGovernmentProcess(step, stepValue, stepName) {
      this.fileDownLoadCheckedList = [];
      this.imageDownLoadCheckedList = [];
      this.downloadCheckedList = [];
      this.downloadCheckedList_img = [];
      this.RepairPaymentDetail = [];
      this.DBStepList = [];
      this.activeDBStep = 1;
      this.DBStepDetail = [];
      this.RepairPaymentActive = 0;
      this.governmentActiveStep = step;
      this.processActiveStep = stepValue;
      this.currentStepName = stepName;
      this.getRepairApprovalDetail(this.currentRepairApplyId);
      if (this.governmentActiveStep === 3 || this.governmentActiveStep === 4 || this.governmentActiveStep === 5 || this.governmentActiveStep === 6 || this.governmentActiveStep === 8 || this.governmentActiveStep === 19 || this.governmentActiveStep === 20 || this.governmentActiveStep === 9) {
        this.getLeaderReviewDetail(this.currentRepairApplyId, this.governmentActiveStep);
      } else if (this.governmentActiveStep === 11) {
        this.getGoverReviewDetail(this.currentRepairApplyId);
      } else if (this.governmentActiveStep === 12) {
        this.getDesignReviewDetail(this.currentRepairApplyId);
      } else if (this.governmentActiveStep === 13) {
        this.getProjectBudgetDetail(this.currentRepairApplyId, 2);
      } else if (this.governmentActiveStep === 14) {
        this.getProjectReviewDetail(this.currentRepairApplyId, 2);
      } else if (this.governmentActiveStep === 17 && this.processActiveStep === 14) {
        this.getLogDetail(this.currentRepairApplyId);
      } else if (this.governmentActiveStep === 17 && this.processActiveStep === 13) {
        this.getRepairPayment(this.currentRepairApplyId)
      } else if (this.governmentActiveStep === 16) {
        this.getchargerList(this.currentRepairApplyId);
      } else if (this.governmentActiveStep === 27) {
        this.getCompletedProjectInfo(this.currentRepairApplyId);
      } else if (this.governmentActiveStep === 15) {
        this.getDBStepList();
      } else if (this.governmentActiveStep === 18) {
        this.changeDBProcess(null, 'PREREPORT_APPROVAL');
        this.activeDownloadNum = 21;
      }
    },
    // 获取维修信息
    getRepairApprovalDetail(repairApplyId, flag) {
      getRepairApprovalDetail(repairApplyId).then(res => {
        if (this.governmentActiveStep === 8) {
          this.governmentRepairInfo.researchApprovalDb.repairProject = res.repairProject;
          this.governmentRepairInfo.researchApprovalDb.budgetEstimate = res.budgetEstimate;
          this.governmentRepairInfo.researchApprovalDb.unitAreaPrice = res.unitAreaPrice;
        }
        this.baseInfo = [];
        if (this.governmentActiveStep === 11 || this.governmentActiveStep === 12 || this.governmentActiveStep === 13 || this.governmentActiveStep === 14) {
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
          this.baseInfo[3] = {
            name: '维修预算',
            key: 'budgetEstimate',
            value: `${res.budgetEstimate}元`
          }
          this.baseInfo[4] = {
            name: '维修面积',
            key: 'totalArea',
            value: `${res.totalArea}m²`
          }
          this.baseInfo[5] = {
            name: '维修项目名称',
            key: 'repairProject',
            value: res.repairProject
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
          this.baseInfo[3] = {
            name: '责任人',
            key: 'responsiblePerson',
            value: res.responsiblePerson || ''
          }
          this.baseInfo[4] = {
            name: '施工单位',
            key: 'repairUnitName',
            value: res.repairUnitName || ''
          }
        }
        if (flag) {
          this.repairInfoList = [];
          if (res.repairModuleDetailDtoList && res.repairModuleDetailDtoList.length > 0) {
            res.repairModuleDetailDtoList.forEach(item => {
              const temp = {
                info: item.positionStr,
                id: item.repairModuleId
              }
              this.repairInfoList.push(temp);
            })
            this.showRepairModuleInfo = true;
            this.showInfoDetail(res.repairModuleDetailDtoList[0].repairModuleId, 0);
          } else {
            this.showRepairModuleInfo = false;
          }
        }
      }).catch(err => {
        this.showRepairModuleInfo = false;
        const msg = err.result ? err.result : '维修审批详情获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 获取审批详情
    getLeaderReviewDetail(repairApplyId, step) {
      getLeaderReviewDetail(repairApplyId, step).then(res => {
        if (res) {
          const obj = res[0];
          if (step === 3) {
            this.governmentActiveName = 'busApprovalCommentDb';
            this.governmentActiveObj = this.governmentRepairInfo[this.governmentActiveName];
            for ([key, value] of Object.entries(obj[this.governmentActiveName])) {
              if (this.governmentActiveObj[key] !== undefined) {
                this.governmentActiveObj[key] = value;
              }
            }
          } else if (step === 4) {
            this.governmentActiveName = 'dzhApprovalCommentDb';
            this.governmentFileActiveName = 'dzhApprovalDb';
            this.governmentActiveObj = this.governmentRepairInfo[this.governmentActiveName];
            this.governmentFileActiveObj = this.governmentRepairInfo[this.governmentFileActiveName];
            for ([key, value] of Object.entries(obj[this.governmentActiveName])) {
              if (this.governmentActiveObj[key] !== undefined) {
                this.governmentActiveObj[key] = value;
              }
            }
            for ([key, value] of Object.entries(obj[this.governmentFileActiveName])) {
              if (this.governmentFileActiveObj[key] !== undefined) {
                this.governmentFileActiveObj[key] = value;
              }
            }
            this.governmentFileActiveObj.repairApprovalDataList.forEach(item => {
              item.detail = `${item.physicalAddress},${item.repairFileName}`;
            })
          } else if (step === 8) {
            this.governmentActiveName = 'researchApprovalDb';
            this.governmentActiveObj = this.governmentRepairInfo[this.governmentActiveName];
            this.governmentActiveObj.repairApprovalDataList.file = [];
            this.governmentActiveObj.repairApprovalDataList.image = [];
            this.governmentActiveObj.projectIntroduction = obj[this.governmentActiveName].projectIntroduction
            obj[this.governmentActiveName].repairApprovalDataList.forEach(item => {
              const name = item.name.split('_')[1];
              if (name === 'img') {
                if (item.type === 3) {
                  item.virtualAddress = '/images/compress.png';
                }
                this.governmentActiveObj.repairApprovalDataList.image.push(item);
              } else if (name === 'file') {
                this.governmentActiveObj.repairApprovalDataList.file.push(item);
              }
            })
            this.governmentActiveObj.repairApprovalDataList.image.forEach(img => {
              img.detail = `${img.physicalAddress},${img.repairFileName}`;
            })
            this.governmentActiveObj.repairApprovalDataList.file.forEach(file => {
              file.detail = `${file.physicalAddress},${file.repairFileName}`;
            })
          } else if (step === 5) {
            this.governmentFileActiveName = 'repairPlanDb';
            this.governmentFileActiveObj = this.governmentRepairInfo[this.governmentFileActiveName];
            this.governmentFileActiveObj.repairApprovalDataList = obj[this.governmentFileActiveName].repairApprovalDataList;
            this.governmentFileActiveObj.repairApprovalDataList.forEach(item => {
              item.detail = `${item.physicalAddress},${item.repairFileName}`;
            })
          } else if (step === 6) {
            this.governmentActiveName = 'aRepairPlanCommentDb';
            this.governmentActiveObj = this.governmentRepairInfo[this.governmentActiveName];
            for ([key, value] of Object.entries(obj[this.governmentActiveName])) {
              if (this.governmentActiveObj[key] !== undefined) {
                this.governmentActiveObj[key] = value;
              }
            }
          } else if (step === 19) {
            this.governmentFileActiveName = 'ysRepairPlanDb';
            this.governmentFileActiveObj = this.governmentRepairInfo[this.governmentFileActiveName];
            this.governmentFileActiveObj.repairApprovalDataList = obj[this.governmentFileActiveName].repairApprovalDataList;
            this.governmentFileActiveObj.repairApprovalDataList.forEach(item => {
              item.detail = `${item.physicalAddress},${item.repairFileName}`;
            })
          } else if (step === 20) {
            this.governmentActiveName = 'ysPlanCommentDb';
            this.governmentActiveObj = this.governmentRepairInfo[this.governmentActiveName];
            for ([key, value] of Object.entries(obj[this.governmentActiveName])) {
              if (this.governmentActiveObj[key] !== undefined) {
                this.governmentActiveObj[key] = value;
              }
            }
          } else if (step === 9) {
            this.governmentActiveName = 'ldaCommentDb';
            this.governmentFileActiveName = 'ldaDb';
            this.governmentActiveObj = this.governmentRepairInfo[this.governmentActiveName];
            this.governmentFileActiveObj = this.governmentRepairInfo[this.governmentFileActiveName];
            for ([key, value] of Object.entries(obj[this.governmentActiveName])) {
              if (this.governmentActiveObj[key] !== undefined) {
                this.governmentActiveObj[key] = value;
              }
            }
            for ([key, value] of Object.entries(obj[this.governmentFileActiveName])) {
              if (this.governmentFileActiveObj[key] !== undefined) {
                this.governmentFileActiveObj[key] = value;
              }
            }
            this.governmentFileActiveObj.repairApprovalDataList.forEach(item => {
              item.detail = `${item.physicalAddress},${item.repairFileName}`;
            })
            switch (this.governmentActiveObj.ldaAuditState) {
              case 1:
                this.governmentActiveObj.ldaAuditState = '同意';
                break;
              case 2:
                this.governmentActiveObj.ldaAuditState = '不同意';
                break;
              case 3:
                this.governmentActiveObj.ldaAuditState = '不同意修改预算方案';
                break;
            }
          }
          if (this.governmentActiveName === 'busApprovalCommentDb') {
            switch (this.governmentActiveObj.busApprovalAuditState) {
              case 1:
                this.governmentActiveObj.busApprovalAuditState = '实施专项维修';
                break;
              case 2:
                this.governmentActiveObj.busApprovalAuditState = '不同意维修';
                break;
              case 3:
                this.governmentActiveObj.busApprovalAuditState = '实施零星维修';
                break;
            }
          }
          if (this.governmentActiveName === 'dzhApprovalCommentDb') {
            switch (this.governmentActiveObj.dzhApprovalAuditState) {
              case 1:
                this.governmentActiveObj.dzhApprovalAuditState = '申请单位自费修理';
                break;
              case 2:
                this.governmentActiveObj.dzhApprovalAuditState = '不同意维修';
                break;
              case 3:
                this.governmentActiveObj.dzhApprovalAuditState = '机关事务服务中心经费维修';
                break;
            }
          }
          if (this.governmentActiveName === 'aRepairPlanCommentDb') {
            switch (this.governmentActiveObj.aRepairPlanAuditState) {
              case 1:
                this.governmentActiveObj.aRepairPlanAuditState = '维修方案合格';
                break;
              case 2:
                this.governmentActiveObj.aRepairPlanAuditState = '维修方案不合格';
                break;
            }
          }
          if (this.governmentActiveName === 'ysPlanCommentDb') {
            switch (this.governmentActiveObj.ysPlanAuditState) {
              case 1:
                this.governmentActiveObj.ysPlanAuditState = '预算方案合格';
                break;
              case 2:
                this.governmentActiveObj.ysPlanAuditState = '预算方案不合格';
                break;
            }
          }
        }
      }).catch(err => {
        const msg = err.result ? err.result : `${this.currentStepName}详情获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查询政府或发改委审批
    getGoverReviewDetail(repairApplyId) {
      getGoverReviewDetail(repairApplyId).then(res => {
        if (res) {
          const obj = res[0]
          this.governmentActiveName = 'galCommentDb';
          this.governmentFileActiveName = 'governmentApprovalDb';
          this.governmentActiveObj = this.governmentRepairInfo[this.governmentActiveName];
          this.governmentFileActiveObj = this.governmentRepairInfo[this.governmentFileActiveName];
          for ([key, value] of Object.entries(obj[this.governmentActiveName])) {
            if (this.governmentActiveObj[key] !== undefined) {
              this.governmentActiveObj[key] = value;
            }
          }
          for ([key, value] of Object.entries(obj[this.governmentFileActiveName])) {
            if (this.governmentFileActiveObj[key] !== undefined) {
              this.governmentFileActiveObj[key] = value;
            }
          }
          switch (this.governmentActiveObj.galAuditState) {
            case 1:
              this.governmentActiveObj.galAuditState = '同意维修';
              break;
            case 2:
              this.governmentActiveObj.galAuditState = '不同意维修';
              break;
          }
          this.governmentFileActiveObj.repairApprovalDataList.forEach(item => {
            item.detail = `${item.physicalAddress},${item.repairFileName}`;
          })
        }
      }).catch(err => {
        const msg = err.result ? err.result : `${this.currentStepName}详情获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查询项目施工设计
    getDesignReviewDetail(repairApplyId) {
      getDesignReviewDetail(repairApplyId).then(res => {
        if (res) {
          this.governmentActiveName = 'detailDesignDb';
          this.governmentActiveObj = this.governmentRepairInfo[this.governmentActiveName];
          this.governmentActiveObj.fileList = [];
          this.governmentActiveObj.imgList = [];
          res.documentList[0].detailDesignDb.repairApprovalDataList.forEach(item => {
            const name = item.name.split('_')[1];
            if (name === 'img') {
              if (item.type === 3) {
                item.virtualAddress = '/images/compress.png';
              }
              this.governmentActiveObj.imgList.push(item);
            } else if (name === 'file') {
              this.governmentActiveObj.fileList.push(item);
            }
          })
          this.governmentActiveObj.fileList.forEach(file => {
            file.detail = `${file.physicalAddress},${file.repairFileName}`;
          })
          this.governmentActiveObj.imgList.forEach(img => {
            img.detail = `${img.physicalAddress},${img.repairFileName}`;
          })
          this.governmentActiveObj.designUnit = res.designUnit;
          this.governmentActiveObj.planningPermit = res.planningPermit;
        }
      }).catch(err => {
        const msg = err.result ? err.result : `${this.currentStepName}详情获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查询维修预算信息
    getProjectBudgetDetail(repairApplyId, state) {
      getProjectBudgetDetail(repairApplyId, state).then(res => {
        if (res) {
          this.governmentActiveName = 'projectBudgetDb';
          this.governmentActiveObj = this.governmentRepairInfo[this.governmentActiveName];
          this.governmentActiveObj.totalBudgetPrice = res.totalBudgetPrice;
          this.governmentActiveObj.auditUnit = res.auditUnit;
          this.governmentActiveObj.budgetFileList = res.designFileList[0].budget;
          this.governmentActiveObj.budgetFileList.forEach(file => {
            file.detail = `${file.physicalAddress},${file.repairFileName}`;
          })
        }
      }).catch(err => {
        const msg = err.result ? err.result : `${this.currentStepName}详情获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查询预算评审信息
    getProjectReviewDetail(repairApplyId, state) {
      getProjectReviewDetail(repairApplyId, state).then(res => {
        if (res) {
          this.governmentActiveName = 'projectApproveDb';
          this.governmentActiveObj = this.governmentRepairInfo[this.governmentActiveName];
          this.governmentActiveObj.accreditationUnit = res.accreditationUnit;
          this.governmentActiveObj.finalSum = res.finalSum;
          this.governmentActiveObj.decreaseAmount = res.designFileList[0].decAmount[0];
          this.governmentActiveObj.approvalFileList = res.designFileList[0].approval;
          this.governmentActiveObj.approvalFileList.forEach(file => {
            file.detail = `${file.physicalAddress},${file.repairFileName}`;
          })
        }
      }).catch(err => {
        const msg = err.result ? err.result : `${this.currentStepName}详情获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查询施工日志信息
    getLogDetail(repairApplyId) {
      getRepairManage(repairApplyId).then(res => {
        if (res) {
          this.showLog = true;
          this.repairLogList = res.repairLogDtoList;
          this.currentLogTime = this.repairLogList[0].repairProjectDate;
          for ([key, value] of Object.entries(this.repairLogList[0])) {
            if (this.logInfoDetail[key] !== undefined) {
              this.logInfoDetail[key] = value;
            }
          }
          let tempArr = []
          if (this.repairLogList[0].partsIdAndImgVoList && this.repairLogList[0].partsIdAndImgVoList.length > 0) {
            this.repairLogList[0].partsIdAndImgVoList.forEach(item => {
              tempArr.push(item.repairPartName);
            })
          }
          this.logInfoDetail.repairProjectPart = tempArr.join(',');
          this.repairLogImgList = JSON.parse(JSON.stringify(this.repairLogList[0].partsIdAndImgVoList));
          this.repairLogImgList.forEach(item => {
            item.repairPartName = `维修图片（${item.repairPartName}）`
          })
          for (let i = this.repairLogImgList.length - 1; i > -1; i--) {
            if (this.repairLogImgList[i].repairPmImgList.length === 0) {
              this.repairLogImgList.splice(i, 1);
            }
          }
        } else {
          this.showLog = false;
        }
      }).catch(err => {
        const msg = err.result ? err.result : `${this.currentStepName}详情获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 切换施工日志详情
    showLogInfoDetail(id, index) {
      let repairObj = null;
      this.popoverLogInfo = false;
      this.logActiveInfo = index;
      for (let i = 0; i < this.repairLogList.length; i++) {
        if (this.repairLogList[i].repairProjectId === id) {
          repairObj = this.repairLogList[i];
          break;
        }
      }
      this.currentLogTime = repairObj.repairProjectDate;
      for ([key, value] of Object.entries(repairObj)) {
        if (this.logInfoDetail[key] !== undefined) {
          this.logInfoDetail[key] = value;
        }
      };
      let tempArr = []
      if (repairObj.partsIdAndImgVoList && repairObj.partsIdAndImgVoList.length > 0) {
        repairObj.partsIdAndImgVoList.forEach(item => {
          tempArr.push(item.repairPartName);
        })
      }
      this.logInfoDetail.repairProjectPart = tempArr.join(',');
      this.repairLogImgList = JSON.parse(JSON.stringify(repairObj.partsIdAndImgVoList));
      this.repairLogImgList.forEach(item => {
        item.repairPartName = `维修图片（${item.repairPartName}）`
      })
      for (let i = this.repairLogImgList.length - 1; i > -1; i--) {
        if (this.repairLogImgList[i].repairPmImgList.length === 0) {
          this.repairLogImgList.splice(i, 1);
        }
      }
    },
    // 查询付款进度
    getRepairPayment(repairApplyId) {
      getRepairPayment(repairApplyId).then(res => {
        if (res.length > 0) {
          this.showRepairPaymentDetail = true;
          this.RepairPaymentDetail = res;
          this.RepairPaymentDetail.forEach((item, index) => {
            if (item.repairPaymentState == 3) {
              this.RepairPaymentActive = index + 1
            }
          })
        } else {
          this.showRepairPaymentDetail = false;
        }
      }).catch(err => {
        const msg = err.result ? err.result : `${this.currentStepName}详情获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查询项目负责人
    getchargerList(repairApplyId) {
      getchargerList(repairApplyId).then(res => {
        if (res && res.length > 0) {
          this.repairName = '';
          for (let i = 0; i < res.length; i++) {
            this.chargeOptions.forEach(item => {
              if (item.user_id === res[i].repairPmUserid) {
                if (res.length === 1) {
                  this.repairName = item.username;
                } else {
                  if (i === (res.length - 1)) {
                    this.repairName += item.username;
                  } else {
                    this.repairName += `${item.username}、`
                  }
                }
              }
            })
          }
        }
      }).catch(err => {
        const msg = err.result ? err.result : `${this.currentStepName}详情获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查询竣工验收
    getCompletedProjectInfo(repairApplyId) {
      getCompletedProjectInfo(repairApplyId).then(res => {
        const obj = res.documentList[0];
        this.governmentActiveName = 'constructManageDb';
        this.governmentActiveObj = this.governmentRepairInfo[this.governmentActiveName];
        for ([key, value] of Object.entries(obj[this.governmentActiveName])) {
          if (this.governmentActiveObj[key] !== undefined) {
            this.governmentActiveObj[key] = value;
          }
        }
        if (this.governmentActiveObj.auditReport && this.governmentActiveObj.auditReport.length > 0) {
          this.governmentActiveObj.auditReport.forEach(file => {
            file.detail = `${file.physicalAddress},${file.repairFileName}`;
          })
        }
        if (this.governmentActiveObj.buildDrawings && this.governmentActiveObj.buildDrawings.length > 0) {
          this.governmentActiveObj.buildDrawings.forEach(image => {
            if (image.type === 3) {
              image.virtualAddress = '/images/compress.png';
            }
            image.detail = `${image.physicalAddress},${image.repairFileName}`;
          })
        }
      }).catch(err => {
        const msg = err.result ? err.result : `${this.currentStepName}详情获取失败`;
        this.$message({
          message: msg,
          type: "error"
        });
      })
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
      if (type === undefined || type === null) {
        type = this.activeDownloadNum;
      }
      downloadFile(list, type, this.currentRepairApplyId).then(res => {
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
    handleCheckedImageChange(val) {
      this.downloadCheckedList_img = [];
      this.imageDownLoadCheckedList.forEach(item => {
        const arr = item.split(',');
        const obj = {
          address: arr[0],
          fileName: arr[1]
        }
        this.downloadCheckedList_img.push(obj);
      })
    },
    // dialog关闭的回调
    dialogClosed() {
      this.baseInfo = [];
      this.governmentActiveStep = 3;
      this.fileDownLoadCheckedList = [];
      this.downloadCheckedList = [];
      this.imageDownLoadCheckedList = [];
      this.downloadCheckedList_img = [];
      this.RepairPaymentDetail = [];
      this.RepairPaymentActive = 0;
      this.processActiveStep = 3;
      this.repairName = '';
      this.currentStepName = '';
      this.activeDBStep = null;
      this.DBStepList = [];
      this.DBStepDetail = [];
      this.clearForm(this.governmentRepairInfo);
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
    // 格式化时间
    formatDate(time) {
      var date = new Date(time);
      var fmt = "yyyy-MM-dd hh:mm:ss";
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
    // 获取图片链接
    getImgUrl(url) {
      return `${BASE_URL}${url_name_1}${url}?access_token=${this.token}`;
    }
  },
  mounted() {
    this.userId = sessionStorage.getItem('userId');
    this.token = sessionStorage.getItem('TOKEN');
    this.getList();
    // 指定项目负责人
    getUserList().then(res => {
      this.chargeOptions = res;
    }).catch(err => {
      this.chargeOptions = [];
      const msg = err.result ? err.result : '项目负责人获取失败';
      this.$message({
        message: msg,
        type: "error"
      });
    })
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
#repairView .query-box {
  margin-bottom: 20px;
  position: relative;
}
#repairView .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 16%;
}
#repairView .query-item > div {
  float: left;
}
#repairView .query-item > .query-input {
  width: calc(100% - 80px);
}
#repairView .query-item .el-select {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#repairView .query-item .el-date-editor.el-input,
#repairView .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#repairView .query-btn {
  position: absolute;
  right: 0;
}
#repairView .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#repairView .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#repairView .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#repairView .table-box .notAudit {
  color: #ff9e00;
}
#repairView .table-box .passAudit {
  color: #3891e0;
}
#repairView .table-box .failedAudit {
  color: #f9463f;
}
#repairView .table-box .Auditing {
  color: #1aa97b;
}
#repairView .img-preview-bigger {
  width: 100%;
  height: 100%;
  position: fixed;
  z-index: 100000;
  top: 0;
  left: 0;
  display: none;
}
#repairView .img-preview-bigger > .img-preview-bigger-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
}
#repairView .img-preview-bigger > img {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
}
#repairView .img-preview-bigger > i {
  position: absolute;
  font-size: 50px;
  top: 30px;
  right: 30px;
  color: #ffffff;
  cursor: pointer;
}
#repairView .img-preview-bigger > i:hover {
  color: #3891e0;
}
#repairView .title-box {
  padding: 20px;
}
#repairView .title-box > span,
#repairView .title-box-db > span {
  width: 145px;
  padding: 5px 20px;
  text-align: center;
  border: 1px solid #cbdbef;
  float: left;
  color: #4a8eec;
  margin-left: 20px;
  margin-bottom: 20px;
  cursor: pointer;
}
#repairView .title-box-db > span {
  margin-left: 18px;
}
#repairView .title-box > span.governmentRepairActive,
#repairView .title-box-db > span.governmentRepairActive {
  background: #4a8eec;
  color: #ffffff;
}
#repairView .el-dialog__body {
  padding: 0;
}
#repairView .government-repair-info {
  padding: 10px 25px 30px;
}
#repairView .government-repair-info .info-item {
  line-height: 40px;
  padding-left: 30px;
  box-sizing: border-box;
}
#repairView .government-repair-info .info-item > span:first-child {
  width: 56px;
  display: inline-block;
  text-align: justify;
  text-justify: distribute-all-lines;
  text-align-last: justify;
}
#repairView .el-tabs__item {
  font-size: 16px;
}
#repairView .repair-info-base > div {
  font-size: 14px;
}
#repairView .download-icon {
  font-size: 60px;
  color: #dfdfdf;
  display: block;
  padding-left: 20px;
  padding-bottom: 5px;
}
#repairView .repaire-file-download-box {
  padding-left: 30px;
  float: left;
  width: 100%;
}
#repairView .repaire-file-download-box > div {
  float: left;
}
#repairView .repaire-file-download-title {
  line-height: 40px;
  width: 56px;
  text-align: justify;
  text-justify: distribute-all-lines;
  text-align-last: justify;
}
#repairView .repaire-file-download {
  margin-left: 5px;
  position: relative;
  top: 10px;
}
#repairView .info-item-inline > .info-item {
  float: left;
  width: 33%;
}
#repairView .repaire-file-download .img-view-item {
  width: 100px;
  height: 100px;
  margin-right: 20px;
  float: left;
  position: relative;
  top: 4px;
}
#repairView .repaire-file-download .img-view-item > img {
  width: 100%;
  height: 100%;
}
#repairView .repaire-file-download > .img-download-btn {
  float: left;
  width: 100%;
  margin-top: 22px;
  margin-bottom: 15px;
}
#repairView .bigger-view-warpper {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}
#repairView .bigger-view-warpper > .bigger-view-warpper-inner {
  width: 100%;
  height: 100%;
  background: #000;
  opacity: 0.5;
  display: none;
}
#repairView .bigger-view-warpper:hover > .bigger-view-warpper-inner {
  display: block;
}
#repairView .bigger-view-warpper > i {
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
#repairView .bigger-view-warpper > i:hover {
  color: #3891e0;
}
#repairView .bigger-view-warpper:hover > i {
  display: block;
}
#repairView .info-detail-title {
  border-bottom: 1px solid #d8d8d8;
}
#repairView .info-detail-title > span {
  font-size: 16px;
  line-height: 36px;
  float: left;
  border-bottom: 1px solid #498fea;
  position: relative;
  top: 1px;
}
#repairView .info-detail-title > .info-detail-btn {
  float: right;
}
#repairView .more-btn {
  color: #3891e0;
  cursor: pointer;
  display: inline-block;
  line-height: 36px;
  font-size: 16px;
}
#repairView .more-btn:hover {
  color: #035cac;
}
#repairView .info-detail-content {
  padding-left: 30px;
}
#repairView .info-detail-content > div {
  line-height: 40px;
}
#repairView .repair-log-box .info-detail-content > div > span:first-of-type {
  width: 84px;
  display: inline-block;
  text-align: justify;
  text-justify: distribute-all-lines;
  text-align-last: justify;
}
#repairView .repair-log-box .repaire-file-download-box {
  padding-left: 0;
  float: inherit;
}
#repairView .repaire-file-download-item {
  line-height: 22px;
  margin-bottom: 10px;
}
#repairView .img-view-item > .el-checkbox {
  position: absolute;
  bottom: 0;
  right: -8px;
}
#repairView .pay-detail-step {
  margin-top: 30px;
}
#repairView .dialog-content .content-box {
  height: 500px;
  overflow-y: auto;
  overflow-x: hidden;
}
#repairView .repair-info-box {
  border-bottom: 0;
}
#repairView .repaire-file-download-colons {
  position: relative;
  top: 10px;
}
.more-info {
  background: #f5f5f5;
  padding: 0;
}
.repair-info-more > li,
.log-info-more > li {
  line-height: 30px;
  padding: 0 10px;
  cursor: pointer;
}
.repair-info-more > li:hover,
.log-info-more > li:hover {
  background: #498fea;
  color: #fff;
}
.repair-info-more > .info-active,
.log-info-more > .info-active {
  background: #498fea;
  color: #fff;
}
</style>


