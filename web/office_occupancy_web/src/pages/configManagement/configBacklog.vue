<template>
  <div id="configBacklog">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">配置编号：</div>
          <div class="query-input">
            <el-input v-model.trim="configureNo" placeholder="请输入配置编号"></el-input>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">申请单位：</div>
          <div class="query-input">
            <el-select filterable v-model="unitId" placeholder="请选择" style="width:100%">
              <el-option
                v-for="item in unitAuthorityOptions"
                :key="item.unitId"
                :label="item.unitName"
                :value="item.unitId"
              ></el-option>
            </el-select>
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
          prop="perArea"
          label="人均面积(m²)"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="configureDate"
          label="申请时间"
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
    <!-- 单位审查 -->
    <el-dialog
      :visible.sync="dialogVisible.unit"
      :close-on-click-modal="false"
      width="1100px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
      class="unit-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <div class="dialog-content">
        <div class="clearfix">
          <div class="content-item">
            <span>申请单位：</span>
            <span>{{baseInfoForm.unitName}}</span>
          </div>
          <div class="content-item">
            <span>单位负责人：</span>
            <span>{{baseInfoForm.unitCharger}}</span>
          </div>
          <div class="content-item">
            <span>编制人数：</span>
            <span>{{baseInfoForm.unitComplement}}人</span>
          </div>
          <div class="content-item">
            <span>实有人数：</span>
            <span>{{baseInfoForm.actualNumber}}人</span>
          </div>
          <div class="content-item">
            <span>现有面积：</span>
            <span>{{baseInfoForm.totalArea ? baseInfoForm.totalArea : '暂无'}}</span>
          </div>
          <div class="content-item">
            <span>人均面积：</span>
            <span>{{baseInfoForm.PerCapitaArea ? baseInfoForm.PerCapitaArea : '暂无'}}</span>
          </div>
          <div class="content-item">
            <span>联系电话：</span>
            <span>{{baseInfoForm.contactPhone}}</span>
          </div>
          <div class="content-item">
            <span>联系人：</span>
            <span>{{baseInfoForm.contacts}}</span>
          </div>
          <div class="content-item">
            <span>单位性质：</span>
            <span>{{baseInfoForm.unitProperty}}</span>
          </div>
          <div class="content-item">
            <span>单位地址：</span>
            <span>{{baseInfoForm.unitAddress}}</span>
          </div>
          <div class="content-item" style="width:100%">
            <span>申请原因：</span>
            <span>{{baseInfoForm.applyReason}}</span>
          </div>
          <div class="content-item" style="width:100%" v-if="baseInfoForm.enclosureList.length > 0">
            <span>下载附件：</span>
            <div class="file-download">
              <el-checkbox-group
                v-model="fileDownLoadCheckedList"
                @change="handleCheckedFileChange"
              >
                <div
                  v-for="(file, index) in baseInfoForm.enclosureList"
                  :key="index"
                  class="file-download-item"
                >
                  <el-checkbox :label="file.detail">{{file.enclosureName}}</el-checkbox>
                </div>
              </el-checkbox-group>
              <el-button size="small" type="primary" @click="fileDownLoad(downloadCheckedList)">点击下载</el-button>
            </div>
          </div>
        </div>
        <el-form :model="Form_unit" :rules="rules_unit" ref="Form_unit" inline>
          <el-form-item label="审查结果：" label-width="84px" prop="auditState" style="width:100%">
            <el-radio
              v-model="Form_unit.auditState"
              :label="1"
              @change="auditStateRadioChange"
              :name="currentDialogName"
            >同意申请</el-radio>
            <el-radio
              v-model="Form_unit.auditState"
              :label="2"
              @change="auditStateRadioChange"
              :name="currentDialogName"
            >不同意申请</el-radio>
          </el-form-item>
          <el-form-item
            label="审查意见："
            label-width="84px"
            prop="comment"
            class="common-item"
            style="width:100%"
          >
            <el-input
              v-model.trim="Form_unit.comment"
              type="textarea"
              :rows="4"
              placeholder="请输入审批意见"
              :width="851"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="区域："
            label-width="84px"
            prop="regionIds"
            class="region-item"
            v-if="Form_unit.auditState===1"
            style="width:100%"
          >
            <el-select
              filterable
              multiple
              v-model="Form_unit.regionIds"
              placeholder="请选择区域"
              style="width:100%"
            >
              <el-option
                v-for="item in regionOpts"
                :key="item.regionId"
                :label="item.regionName"
                :value="item.regionId"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            label="电梯台数："
            label-width="84px"
            prop="elevatorsNumber"
            v-if="Form_unit.auditState===1"
            class="elevators-number"
          >
            <el-select
              v-model="Form_unit.elevatorsNumber"
              placeholder="请选择电梯台数"
              style="width:200px"
              size="small"
            >
              <el-option
                v-for="item in elevatorsOpts"
                :key="item.elevatorId"
                :label="item.elevatorName"
                :value="item.elevatorId"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            label="参与统筹调剂单位："
            label-width="140px"
            prop="redistributionUnitId"
            v-if="Form_unit.auditState===1"
          >
            <el-select
              filterable
              v-model="Form_unit.redistributionUnitId"
              placeholder="请选择参与统筹调剂单位"
              style="width:200px"
              size="small"
            >
              <el-option
                v-for="item in unitOpts"
                :key="item.unitId"
                :label="item.unitName"
                :value="item.unitId"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            label="房间信息："
            label-width="84px"
            style="width:100%"
            class="house-info-table-item"
            v-if="Form_unit.auditState===1"
          >
            <el-button @click.stop="addHouseInfo" icon="el-icon-plus" class="house-info-add-btn">添加</el-button>
            <div class="house-table-box">
              <table-component :tableData="Form_unit.configureRooms" :tableHeight="200">
                <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
                <el-table-column label="房间数量" align="center">
                  <template slot-scope="scope">
                    <el-input
                      v-model.trim="scope.row.roomNumber"
                      placeholder="请输入房间数量"
                      size="small"
                      @blur="roomNumberLimitInt(scope.$index, true)"
                      @keyup.native="roomNumberLimitInt(scope.$index)"
                      class="roomNumber-item"
                    />
                  </template>
                </el-table-column>
                <el-table-column label="房间面积范围(m²)" align="center" :width="196">
                  <template slot-scope="scope">
                    <el-input
                      v-model.trim="scope.row.roomAreaMin"
                      size="small"
                      @blur="roomAreaMinLimitFloat(scope.$index, true, true)"
                      @keyup.native="roomAreaMinLimitFloat(scope.$index)"
                      class="roomArea-item"
                    />
                    <span class="roomArea-line">-</span>
                    <el-input
                      v-model.trim="scope.row.roomAreaMax"
                      size="small"
                      @blur="roomAreaMaxLimitFloat(scope.$index, true, true)"
                      @keyup.native="roomAreaMaxLimitFloat(scope.$index)"
                      class="roomArea-item"
                    />
                  </template>
                </el-table-column>
                <el-table-column label="操作" align="center" :width="80">
                  <template slot-scope="scope">
                    <el-button size="mini" type="danger" @click="deleteHouseInfo(scope.$index)">删除</el-button>
                  </template>
                </el-table-column>
              </table-component>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.unit = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form_unit')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /单位审查 -->

    <!-- 分配方案 -->
    <el-dialog
      :visible.sync="dialogVisible.project"
      :close-on-click-modal="false"
      width="1100px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
      class="project-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <div class="dialog-content">
        <div class="prev-page" v-show="!isSureDistributePage">
          <div class="clearfix">
            <div class="content-item">
              <span>申请单位：</span>
              <span>{{baseInfoForm.unitName}}</span>
            </div>
            <div class="content-item">
              <span>单位负责人：</span>
              <span>{{baseInfoForm.unitCharger}}</span>
            </div>
            <div class="content-item">
              <span>编制人数：</span>
              <span>{{baseInfoForm.unitComplement}}</span>
            </div>
            <div class="content-item">
              <span>实有人数：</span>
              <span>{{baseInfoForm.actualNumber}}</span>
            </div>
            <div class="content-item">
              <span>现有面积：</span>
              <span>{{baseInfoForm.totalArea ? baseInfoForm.totalArea : '暂无'}}</span>
            </div>
            <div class="content-item">
              <span>人均面积：</span>
              <span>{{baseInfoForm.PerCapitaArea ? baseInfoForm.PerCapitaArea : '暂无'}}</span>
            </div>
            <div class="content-item">
              <span>联系电话：</span>
              <span>{{baseInfoForm.contactPhone}}</span>
            </div>
            <div class="content-item">
              <span>联系人：</span>
              <span>{{baseInfoForm.contacts}}</span>
            </div>
            <div class="content-item">
              <span>单位性质：</span>
              <span>{{baseInfoForm.unitProperty}}</span>
            </div>
            <div class="content-item">
              <span>单位地址：</span>
              <span>{{baseInfoForm.unitAddress}}</span>
            </div>
            <div class="content-item" style="width:100%">
              <span>申请原因：</span>
              <span>{{baseInfoForm.applyReason}}</span>
            </div>
            <div
              class="content-item"
              style="width:100%"
              v-if="baseInfoForm.enclosureList.length > 0"
            >
              <span>下载附件：</span>
              <div class="file-download">
                <el-checkbox-group
                  v-model="fileDownLoadCheckedList"
                  @change="handleCheckedFileChange"
                >
                  <div
                    v-for="(file, index) in baseInfoForm.enclosureList"
                    :key="index"
                    class="file-download-item"
                  >
                    <el-checkbox :label="file.detail">{{file.enclosureName}}</el-checkbox>
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
          <div style="margin-top:20px;" class="clearfix">
            <div class="content-item">
              <span>电梯台数：</span>
              <span>{{baseInfoForm.elevatorsNumber}}台</span>
            </div>
            <div class="content-item newUnitName" v-show="baseInfoForm.newUnitName">
              <span>参与统筹调剂单位</span>：
              <span>{{baseInfoForm.newUnitName}}</span>
            </div>
            <div class="content-item" style="width:100%">
              <span>区域：</span>
              <span>{{baseInfoForm.regionName}}</span>
            </div>
            <div class="content-item" style="width:100%" v-if="baseInfoForm.leaderAdvice">
              <span style="color:#ff0000">审批意见：</span>
              <span>{{baseInfoForm.leaderAdvice}}</span>
            </div>
          </div>
          <el-form :model="Form_project" :rules="rules_project" ref="Form_project">
            <el-form-item label="分配方案：" label-width="84px" prop="allocationPlan">
              <el-radio-group v-model="Form_project.allocationPlan" @change="allocationPlanChange">
                <el-radio :label="1">调剂</el-radio>
                <el-radio :label="2">置换</el-radio>
                <el-radio :label="3">租用</el-radio>
                <el-radio :label="4">建设</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="选择方案：" label-width="84px" v-if="Form_project.allocationPlan===1">
              <div class="plan-btn-box">
                <span
                  :class="{'active' : Form_project.selectionPlan === 1}"
                  @click="selectionPlanChange(1)"
                >智能分配</span>
                <span
                  :class="{'active' : Form_project.selectionPlan === 2}"
                  @click="selectionPlanChange(2)"
                >人工分配</span>
              </div>
            </el-form-item>
          </el-form>
          <div class="distribute-box" v-if="Form_project.allocationPlan===1">
            <div class="intelligent-distribution" v-if="Form_project.selectionPlan === 1">
              <el-tabs
                v-model="distributionActiveName"
                type="card"
                @tab-click="distributionTabClick"
              >
                <el-tab-pane
                  v-for="(item, index) in autoConditionList"
                  :key="index"
                  :label="item.name"
                  :name="item.tableName"
                >
                  <div class="distribute-room-table">
                    <el-table
                      :ref="item.tableName"
                      :data="item.tableData"
                      :height="200"
                      border
                      fit
                      :header-cell-style="tableHeaderCellStyle"
                      :cell-style="tableCellStyle"
                      @select="handleSelectionChange"
                      @select-all="handleSelectionAllChange"
                    >
                      <el-table-column type="selection" width="50" align="center"></el-table-column>
                      <el-table-column label="房间位置" prop="position" align="center"></el-table-column>
                      <el-table-column label="房间面积(m²)" prop="houseUseArea" align="center"></el-table-column>
                      <el-table-column label="原使用单位" prop="unitName" align="center"></el-table-column>
                    </el-table>
                  </div>
                  <div class="distribution-table-pagination-box">
                    <div class="distribution-table-tips">
                      您还能选择
                      <span>{{distributeNum}}</span>个智能分配
                    </div>
                    <div class="distribution-table-pagination">
                      <el-pagination
                        small
                        layout="prev, pager, next"
                        :current-page="item.currentPage"
                        :page-size="item.pageSize"
                        :total="item.total"
                        @current-change="distributeCurrentChange"
                      ></el-pagination>
                    </div>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
            <div class="labour-distribution" v-if="Form_project.selectionPlan === 2">
              <div class="scheme-btn-box clearfix">
                <span>添加方案：</span>
                <el-button @click.stop="addScheme" icon="el-icon-plus" class="scheme-add-btn">添加</el-button>
              </div>
              <div class="scheme-table-box">
                <div class="scheme-table">
                  <el-table
                    ref="scheme"
                    :data="schemeTableData"
                    :height="200"
                    border
                    fit
                    :header-cell-style="tableHeaderCellStyle"
                    :cell-style="tableCellStyle"
                  >
                    <el-table-column width="50" align="center">
                      <template slot-scope="scope">
                        <el-checkbox
                          v-model="scope.row.checked"
                          @change="schemeHandleSelectionChange(scope.row)"
                        ></el-checkbox>
                      </template>
                    </el-table-column>
                    <el-table-column label="方案名称" align="center" class-name="plan-name-column">
                      <template slot-scope="scope">
                        <el-input
                          placeholder="请输入方案名称"
                          v-model.trim="scope.row.planName"
                          @blur="savePlanName(scope.row.planName, scope.row.planId, scope.$index)"
                          @focus="planNameInputFocus(scope.row.planName)"
                          :readonly="scope.row.readonly"
                          size="small"
                        ></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="操作" width="300" align="center">
                      <template slot-scope="scope">
                        <div class="scheme-operate-box">
                          <div
                            class="update"
                            @click="updateName($event, scope.$index, scope.row.planName)"
                          >
                            <a class="el-icon-edit">编辑方案名称</a>
                          </div>
                          <div
                            class="update"
                            @click="updateScheme(scope.row.planId)"
                            :class="{'disable-btn' : !scope.row.planId}"
                          >
                            <a class="el-icon-edit">编辑方案</a>
                          </div>
                          <div
                            class="delete"
                            @click="deleteScheme(scope.row.planId, scope.$index, scope.row.planName)"
                          >
                            <a class="el-icon-delete">删除</a>
                          </div>
                        </div>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
                <div class="distribution-table-pagination-box">
                  <div class="distribution-table-pagination">
                    <el-pagination
                      small
                      layout="prev, pager, next"
                      :current-page="schemeCurrentPage"
                      :page-size="schemePageSize"
                      :total="schemeTotal"
                      @current-change="schemeCurrentChange"
                    ></el-pagination>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="next-page" v-show="isSureDistributePage">
          <div class="next-page-intelligent" v-if="Form_project.selectionPlan === 1">
            <el-tabs
              v-model="sureDistributionActiveName"
              type="card"
              @tab-click="sureDistributionTabClick"
            >
              <el-tab-pane
                v-for="(item, index) in selectDataArray"
                :key="index"
                :label="item.name"
                :name="item.name"
              >
                <table-component :tableData="item.tableData" :tableHeight="200">
                  <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
                  <el-table-column label="房间位置" prop="position" align="center"></el-table-column>
                  <el-table-column label="房间面积(m²)" prop="houseUseArea" align="center"></el-table-column>
                  <el-table-column label="原使用单位" prop="unitName" align="center"></el-table-column>
                </table-component>
                <div class="distribution-table-pagination-box">
                  <div class="distribution-table-pagination">
                    <el-pagination
                      small
                      layout="prev, pager, next"
                      :current-page="item.currentPage"
                      :page-size="item.pageSize"
                      :total="item.total"
                      @current-change="sureDistributeCurrentChange"
                    ></el-pagination>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
          <div class="next-page-labour" v-if="Form_project.selectionPlan === 2">
            <div class="allocation-plan">
              <div>分配方案：</div>
              <table-component :tableData="planTableData" :tableHeight="200">
                <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
                <el-table-column label="房间位置" prop="position" align="center"></el-table-column>
                <el-table-column label="房间面积(m²)" prop="houseUseArea" align="center"></el-table-column>
                <el-table-column label="原使用单位" prop="oldUnitName" align="center"></el-table-column>
                <el-table-column label="新使用单位" prop="newUnitName" align="center"></el-table-column>
              </table-component>
              <div class="distribution-table-pagination-box">
                <div class="distribution-table-pagination">
                  <el-pagination
                    small
                    layout="prev, pager, next"
                    :current-page="planCurrentPage"
                    :page-size="planPageSize"
                    :total="planTotal"
                    @current-change="planeCurrentChange"
                  ></el-pagination>
                </div>
              </div>
            </div>
            <div class="allocation-statistics">
              <div>分配统计：</div>
              <table-component :tableData="distributeChangeList" :tableHeight="200">
                <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
                <el-table-column label="单位名称" prop="unitNames" align="center"></el-table-column>
                <el-table-column label="房间数量" prop="houseNumber" align="center"></el-table-column>
                <el-table-column label="面积(m²)" prop="useArea" align="center"></el-table-column>
              </table-component>
            </div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <prev-btn
          @clickBtn="prev"
          :fontSize="14"
          v-if="isSureDistributePage && Form_project.allocationPlan === 1"
        ></prev-btn>
        <next-btn
          @clickBtn="next"
          :fontSize="14"
          v-if="!isSureDistributePage && Form_project.allocationPlan === 1"
        ></next-btn>
        <confirm-btn
          @clickBtn="submitForm('Form_project')"
          :fontSize="14"
          :marginLeft="Form_project.allocationPlan === 1 ? 180 : 0"
          v-if="isSureDistributePage || Form_project.allocationPlan !== 1"
        ></confirm-btn>
      </span>
    </el-dialog>
    <!-- /分配方案 -->

    <!-- 领导审批 -->
    <el-dialog
      :visible.sync="dialogVisible.leader"
      :close-on-click-modal="false"
      width="1100px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
      class="leader-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <div class="dialog-content">
        <div class="clearfix">
          <div class="content-item">
            <span>申请单位：</span>
            <span>{{baseInfoForm.unitName}}</span>
          </div>
          <div class="content-item">
            <span>单位负责人：</span>
            <span>{{baseInfoForm.unitCharger}}</span>
          </div>
          <div class="content-item">
            <span>编制人数：</span>
            <span>{{baseInfoForm.unitComplement}}人</span>
          </div>
          <div class="content-item">
            <span>实有人数：</span>
            <span>{{baseInfoForm.actualNumber}}人</span>
          </div>
          <div class="content-item">
            <span>现有面积：</span>
            <span>{{baseInfoForm.totalArea ? baseInfoForm.totalArea : '暂无'}}</span>
          </div>
          <div class="content-item">
            <span>人均面积：</span>
            <span>{{baseInfoForm.PerCapitaArea ? baseInfoForm.PerCapitaArea : '暂无'}}</span>
          </div>
          <div class="content-item">
            <span>联系电话：</span>
            <span>{{baseInfoForm.contactPhone}}</span>
          </div>
          <div class="content-item">
            <span>联系人：</span>
            <span>{{baseInfoForm.contacts}}</span>
          </div>
          <div class="content-item">
            <span>单位性质：</span>
            <span>{{baseInfoForm.unitProperty}}</span>
          </div>
          <div class="content-item">
            <span>单位地址：</span>
            <span>{{baseInfoForm.unitAddress}}</span>
          </div>
          <div class="content-item" style="width:100%">
            <span>申请原因：</span>
            <span>{{baseInfoForm.applyReason}}</span>
          </div>
          <div class="content-item" style="width:100%" v-if="baseInfoForm.enclosureList.length > 0">
            <span>下载附件：</span>
            <div class="file-download">
              <el-checkbox-group
                v-model="fileDownLoadCheckedList"
                @change="handleCheckedFileChange"
              >
                <div
                  v-for="(file, index) in baseInfoForm.enclosureList"
                  :key="index"
                  class="file-download-item"
                >
                  <el-checkbox :label="file.detail">{{file.enclosureName}}</el-checkbox>
                </div>
              </el-checkbox-group>
              <el-button size="small" type="primary" @click="fileDownLoad(downloadCheckedList)">点击下载</el-button>
            </div>
          </div>
        </div>
        <div style="margin-top:20px;" class="clearfix">
          <div class="content-item">
            <span>分配方案：</span>
            <span>{{baseInfoForm.allocationPlanName}}</span>
          </div>
          <div class="content-item" v-if="baseInfoForm.allocationPlan === 1">
            <span>选择方案：</span>
            <span>{{baseInfoForm.selectionPlanName}}</span>
          </div>
          <div
            class="content-item"
            v-if="baseInfoForm.selectionPlan === 2 && baseInfoForm.allocationPlan === 1"
          >
            <span>方案名称：</span>
            <span>{{baseInfoForm.planName}}</span>
          </div>
        </div>
        <div class="room-distribute-table" v-if="baseInfoForm.allocationPlan === 1">
          <div class="allocation-plan">
            <div>分配方案：</div>
            <table-component :tableData="distributeTableData" :tableHeight="200">
              <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
              <el-table-column label="房间位置" prop="position" align="center"></el-table-column>
              <el-table-column label="房间面积(m²)" prop="houseUseArea" align="center"></el-table-column>
              <el-table-column label="原使用单位" prop="oldUnitName" align="center"></el-table-column>
              <el-table-column
                label="新使用单位"
                prop="newUnitName"
                align="center"
                v-if="baseInfoForm.selectionPlan === 2"
              ></el-table-column>
            </table-component>
            <div class="distribution-table-pagination-box">
              <div class="distribution-table-pagination">
                <el-pagination
                  small
                  layout="prev, pager, next"
                  :current-page="distributeCurrentPage"
                  :page-size="distributePageSize"
                  :total="distributeTotal"
                  @current-change="leaderDistributeCurrentChange"
                ></el-pagination>
              </div>
            </div>
          </div>
          <div class="allocation-statistics" v-if="baseInfoForm.selectionPlan === 2">
            <div>分配统计：</div>
            <table-component :tableData="distributeChangeList" :tableHeight="200">
              <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
              <el-table-column label="单位名称" prop="unitNames" align="center"></el-table-column>
              <el-table-column label="房间数量" prop="houseNumber" align="center"></el-table-column>
              <el-table-column label="面积(m²)" prop="useArea" align="center"></el-table-column>
            </table-component>
          </div>
        </div>
        <el-form :model="Form_leader" :rules="rules_leader" ref="Form_leader" inline>
          <el-form-item label="审批结果：" label-width="84px" prop="auditState" style="width:100%">
            <el-radio
              v-model="Form_leader.auditState"
              :label="1"
              :name="currentDialogName"
              @change="auditStateRadioChange"
            >同意</el-radio>
            <el-radio
              v-model="Form_leader.auditState"
              :label="2"
              :name="currentDialogName"
              @change="auditStateRadioChange"
            >不同意</el-radio>
          </el-form-item>
          <el-form-item
            label="审批意见："
            label-width="84px"
            prop="comment"
            class="common-item"
            style="width:100%"
          >
            <el-input
              v-model.trim="Form_leader.comment"
              type="textarea"
              :rows="4"
              placeholder="请输入审批意见"
              :width="851"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible.leader = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form_leader')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <!-- /领导审批 -->

    <!-- 完善资料 -->
    <el-dialog
      :visible.sync="dialogVisible.addInfo"
      :close-on-click-modal="false"
      width="1100px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
      class="addInfo-dialog"
    >
      <div slot="title" class="dialog-title">
        <span>{{operateType}}</span>
      </div>
      <div class="dialog-content">
        <div class="step-item">
          <div class="step-item-title">
            <span>添加基础信息</span>
          </div>
          <div class="step-item-content">
            <el-form
              :model=" Form_replacement"
              :rules="rules_replacement"
              ref="Form_replacement"
              label-width="100px"
              v-if="activeAuditState===2"
            >
              <el-form-item label="类型">
                <div>{{currentAddInfoType}}</div>
              </el-form-item>
              <el-form-item label="模式">
                <div>
                  <el-radio v-model="replaceMode" :label="1">补钱</el-radio>
                  <el-radio v-model="replaceMode" :label="2">收益</el-radio>
                </div>
              </el-form-item>
              <el-form-item label="补钱(元)" prop="makeMoney" v-if="replaceMode === 1">
                <el-input
                  v-model.trim="Form_replacement.makeMoney"
                  placeholder="请输入补钱"
                  @keyup.native="baseLimitFloat('makeMoney')"
                  @blur="baseLimitFloat('makeMoney', true)"
                ></el-input>
              </el-form-item>
              <el-form-item label="收益(元)" prop="profit" v-if="replaceMode === 2">
                <el-input
                  v-model.trim="Form_replacement.profit"
                  placeholder="请输入收益"
                  @keyup.native="baseLimitFloat('profit')"
                  @blur="baseLimitFloat('profit', true)"
                ></el-input>
              </el-form-item>
              <el-form-item label="面积(㎡)" prop="area">
                <el-input
                  v-model.trim="Form_replacement.area"
                  placeholder="请输入面积"
                  @keyup.native="baseLimitFloat('area')"
                  @blur="baseLimitFloat('area', true)"
                ></el-input>
              </el-form-item>
              <el-form-item label="上传文件：" label-width="100px" prop="improvingDataList">
                <div class="improving-file-upload">
                  <el-upload
                    ref="improvingUpload"
                    :headers="postHeaders"
                    :action="uploadImprovingUrl"
                    :file-list="fileListImproving"
                    :auto-upload="false"
                    :accept="acceptType"
                    :limit="limitNum"
                    :on-success="improvingHandleSuccess"
                    :on-remove="improvingHandleRemove"
                    :on-change="improvingHandleChange"
                    :on-error="handleError"
                    :on-exceed="handleExceed"
                    :before-upload="beforeAvatarUpload"
                    multiple
                  >
                    <i class="iconfont upload-icon">&#xe661;</i>
                    <el-button slot="trigger" size="small" type="primary">点击选择文件</el-button>
                  </el-upload>
                </div>
              </el-form-item>
            </el-form>
            <el-form
              :model="Form_rent"
              :rules="rules_rent"
              ref="Form_rent"
              label-width="100px"
              v-if="activeAuditState===3"
            >
              <el-form-item label="类型">
                <div>{{currentAddInfoType}}</div>
              </el-form-item>
              <el-form-item label="付款周期" prop="rentalMode">
                <el-select v-model="Form_rent.rentalMode" placeholder="请选择付款周期" size="small">
                  <el-option
                    v-for="(item, index) in rentalModeOpts"
                    :key="index"
                    :label="item"
                    :value="item"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="租金(元)" prop="rent">
                <el-input
                  v-model.trim="Form_rent.rent"
                  placeholder="请输入租金"
                  @keyup.native="baseLimitFloat('rent')"
                  @blur="baseLimitFloat('rent', true)"
                ></el-input>
              </el-form-item>
              <el-form-item label="期限(年)" prop="term">
                <el-input v-model.trim="Form_rent.term" placeholder="请输入期限"></el-input>
              </el-form-item>
              <el-form-item label="面积(㎡)" prop="area">
                <el-input
                  v-model.trim="Form_rent.area"
                  placeholder="请输入面积"
                  @keyup.native="baseLimitFloat('area')"
                  @blur="baseLimitFloat('area', true)"
                ></el-input>
              </el-form-item>
              <el-form-item label="上传文件：" label-width="100px" prop="improvingDataList">
                <div class="improving-file-upload">
                  <el-upload
                    ref="improvingUpload"
                    :headers="postHeaders"
                    :action="uploadImprovingUrl"
                    :file-list="fileListImproving"
                    :auto-upload="false"
                    :accept="acceptType"
                    :limit="limitNum"
                    :on-success="improvingHandleSuccess"
                    :on-remove="improvingHandleRemove"
                    :on-change="improvingHandleChange"
                    :on-error="handleError"
                    :on-exceed="handleExceed"
                    :before-upload="beforeAvatarUpload"
                    multiple
                  >
                    <i class="iconfont upload-icon">&#xe661;</i>
                    <el-button slot="trigger" size="small" type="primary">点击选择文件</el-button>
                  </el-upload>
                </div>
              </el-form-item>
            </el-form>
            <el-form
              :model=" Form_construction"
              :rules="rules_construction"
              ref="Form_construction"
              label-width="100px"
              v-if="activeAuditState===4"
            >
              <el-form-item label="类型">
                <div>{{currentAddInfoType}}</div>
              </el-form-item>
              <el-form-item label="财政支出" prop="expenditure">
                <el-input
                  v-model.trim="Form_construction.expenditure"
                  placeholder="请输入财政支出"
                  @keyup.native="baseLimitFloat('expenditure')"
                  @blur="baseLimitFloat('expenditure', true)"
                ></el-input>
              </el-form-item>
              <el-form-item label="面积(㎡)" prop="area">
                <el-input
                  v-model.trim="Form_construction.area"
                  placeholder="请输入面积"
                  @keyup.native="baseLimitFloat('area')"
                  @blur="baseLimitFloat('area', true)"
                ></el-input>
              </el-form-item>
              <el-form-item label="项目名称" prop="projectName">
                <el-input v-model.trim="Form_construction.projectName" placeholder="请输入项目名称"></el-input>
              </el-form-item>
              <el-form-item label="上传文件：" label-width="100px" prop="improvingDataList">
                <div class="improving-file-upload">
                  <el-upload
                    ref="improvingUpload"
                    :headers="postHeaders"
                    :action="uploadImprovingUrl"
                    :file-list="fileListImproving"
                    :auto-upload="false"
                    :accept="acceptType"
                    :limit="limitNum"
                    :on-success="improvingHandleSuccess"
                    :on-remove="improvingHandleRemove"
                    :on-change="improvingHandleChange"
                    :on-error="handleError"
                    :on-exceed="handleExceed"
                    :before-upload="beforeAvatarUpload"
                    multiple
                  >
                    <i class="iconfont upload-icon">&#xe661;</i>
                    <el-button slot="trigger" size="small" type="primary">点击选择文件</el-button>
                  </el-upload>
                </div>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <div class="step-item">
          <div class="step-item-title">
            <span>添加院落信息</span>
          </div>
          <div class="step-item-content">
            <el-form :inline="true" :model="Form_yard" :rules="rules_yard" ref="Form_yard">
              <el-form-item label="院落名称" label-width="120px" prop="yard.yardName">
                <el-input v-model.trim="Form_yard.yard.yardName" size="small" placeholder="请输入院落名称"></el-input>
              </el-form-item>
              <el-form-item label="行政区划" label-width="120px" prop="yard.regionId">
                <el-select
                  filterable
                  v-model="Form_yard.yard.regionId"
                  placeholder="请选择"
                  size="small"
                  style="width:200px"
                  :popper-append-to-body="false"
                >
                  <el-option
                    v-for="item in regionOpts"
                    :key="item.regionId"
                    :label="item.regionName"
                    :value="item.regionId"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="院落地址" label-width="120px" prop="yard.yardPosition">
                <el-input
                  v-model.trim="Form_yard.yard.yardPosition"
                  size="small"
                  placeholder="请输入院落地址"
                ></el-input>
              </el-form-item>
              <el-form-item label="经度(°)" label-width="120px" prop="yard.yardLongitude">
                <el-input
                  v-model.trim="Form_yard.yard.yardLongitude"
                  size="small"
                  @blur="yardMapBindCoord('yardLongitude', 'yard', true)"
                  @keyup.native="yardLimitFloat('yardLongitude', 'yard')"
                  placeholder="请输入经度"
                ></el-input>
              </el-form-item>
              <el-form-item label="纬度(°)" label-width="120px" prop="yard.yardLatitude">
                <el-input
                  v-model.trim="Form_yard.yard.yardLatitude"
                  size="small"
                  @blur="yardMapBindCoord('yardLatitude', 'yard', true)"
                  @keyup.native="yardLimitFloat('yardLatitude', 'yard')"
                  placeholder="请输入纬度"
                ></el-input>
              </el-form-item>
              <el-form-item label="类型" label-width="120px" prop="yard.type">
                <el-radio v-model="Form_yard.yard.type" :label="1">院落</el-radio>
                <el-radio v-model="Form_yard.yard.type" :label="2">土地</el-radio>
              </el-form-item>
              <el-form-item
                label="添加物业"
                label-width="120px"
                class="estate-add-btn-box"
                prop="yardAndestateVoList"
                v-if="Form_yard.yard.type === 1"
              >
                <el-button @click.stop="addEstate" icon="el-icon-plus" class="estate-add-btn">添加</el-button>
              </el-form-item>
            </el-form>
            <div class="estate-table-box" v-if="Form_yard.yard.type === 1">
              <div
                class="tips-box"
                v-if="!estateNameFlag || !serviceContentFlag || !startTimeFlag || !startTimeLimitFlag || !endTimeFlag || !endTimeLimitFlag || !contractAmountFlag"
              >提示：{{submitTipsArray.join(',')}}</div>
              <table-component
                :tableData="Form_yard.yardAndestateVoList"
                :tableHeight="200"
                :tableCellPadding="'0'"
              >
                <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
                <el-table-column label="物业名称" align="center">
                  <template slot-scope="scope">
                    <el-select
                      v-model="scope.row.estateId"
                      placeholder="请选择物业名称"
                      size="small"
                      style="width:100%"
                    >
                      <el-option
                        v-for="item in estateNameOpts"
                        :key="item.estateId"
                        :label="item.estateName"
                        :value="item.estateId"
                      ></el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column label="服务内容" align="center">
                  <template slot-scope="scope">
                    <el-select
                      v-model="scope.row.serviceContentList"
                      placeholder="请选择服务内容"
                      size="small"
                      style="width:100%"
                      multiple
                    >
                      <el-option
                        v-for="item in serviceContentOpts"
                        :key="item.id"
                        :label="item.text"
                        :value="item.id"
                      ></el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column label="开始使用时间" align="center" :width="150">
                  <template slot-scope="scope">
                    <el-date-picker
                      v-model="scope.row.startTime"
                      type="date"
                      placeholder="开始使用时间"
                      value-format="yyyy-MM-dd HH:mm:ss"
                      size="small"
                      style="width:100%"
                    ></el-date-picker>
                  </template>
                </el-table-column>
                <el-table-column label="结束使用时间" align="center" :width="150">
                  <template slot-scope="scope">
                    <el-date-picker
                      v-model="scope.row.endTime"
                      type="date"
                      placeholder="结束使用时间"
                      value-format="yyyy-MM-dd 23:59:59"
                      size="small"
                      style="width:100%"
                    ></el-date-picker>
                  </template>
                </el-table-column>
                <el-table-column label="使用金额" align="center" :width="130">
                  <template slot-scope="scope">
                    <el-input
                      v-model.trim="scope.row.contractAmount"
                      placeholder="请输入使用金额"
                      size="small"
                      style="width:100%"
                      @blur="contractAmountLimitFloat(scope.$index, true)"
                      @keyup.native="contractAmountLimitFloat(scope.$index)"
                    />
                  </template>
                </el-table-column>
                <el-table-column label="操作" align="center" :width="65">
                  <template slot-scope="scope">
                    <el-button
                      size="mini"
                      type="danger"
                      @click="handleDelete(scope.$index, scope.row)"
                    >删除</el-button>
                  </template>
                </el-table-column>
              </table-component>
            </div>
            <div class="map-tips">提示：可手动输入经纬度,也可以点击下方地图自动生成经纬度。</div>
            <div class="my-map">
              <yard-map @mapClick="setPostion" :labelContent="'所选位置'" :position="position"></yard-map>
            </div>
          </div>
        </div>
        <div class="step-item">
          <div class="step-item-title">
            <span>添加楼座信息</span>
            <div class="btn-box">
              <span @click="deleteBuild">
                <i class="iconfont">&#xe767;</i> 重置
              </span>
              <span @click="addBuild(false)">
                <i class="el-icon-circle-plus-outline"></i> 新增
              </span>
              <el-popover
                popper-class="more-info"
                v-model="popoverRepairInfo"
                placement="bottom-end"
                width="300"
                trigger="click"
                :visible-arrow="false"
              >
                <ul class="repair-info-more">
                  <li
                    v-for="(item, index) in localSaveBuildList"
                    :key="index"
                    :class="{'info-active': activeInfo === index}"
                    @click="showInfoDetail(index)"
                  >{{item.buildName}}</li>
                </ul>
                <span class="more-btn" slot="reference" v-show="localSaveBuildList.length > 0">
                  更多
                  <i class="el-icon-arrow-down"></i>
                </span>
              </el-popover>
            </div>
          </div>
          <div class="step-item-content">
            <el-form
              :inline="true"
              :model="Form_build"
              :rules="rules_build"
              ref="Form_build"
              class="build-info-Form"
            >
              <el-form-item label="院落名称" label-width="130px" prop="yardId">
                <el-select
                  filterable
                  v-model="Form_build.yardId"
                  placeholder="请选择"
                  style="width:200px"
                  size="small"
                  @change="selectYardHandle"
                  :popper-append-to-body="false"
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
                <el-input v-model.trim="Form_build.buildName" size="small" placeholder="请输入楼座名称"></el-input>
              </el-form-item>
              <el-form-item label="建筑面积(m²)" label-width="130px" prop="buildArea">
                <el-input
                  v-model.trim="Form_build.buildArea"
                  size="small"
                  @keyup.native="buildLimitFloat('buildArea')"
                  @blur="buildLimitFloat('buildArea', true)"
                  placeholder="请输入建筑面积"
                ></el-input>
              </el-form-item>
              <el-form-item label="建成时间" label-width="130px" prop="buildDate">
                <el-date-picker
                  v-model="Form_build.buildDate"
                  type="date"
                  placeholder="选择日期"
                  size="small"
                  style="width:200px;"
                  value-format="yyyy-MM-dd HH:mm:ss"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="施工单位" label-width="130px" prop="builder">
                <el-input v-model.trim="Form_build.builder" size="small" placeholder="请输入施工单位"></el-input>
              </el-form-item>
              <el-form-item label="地上楼层" label-width="130px" prop="overgroundFloor">
                <el-input
                  v-model.trim="Form_build.overgroundFloor"
                  size="small"
                  @keyup.native="buildLimitInt('overgroundFloor')"
                  @blur="buildLimitInt('overgroundFloor')"
                  placeholder="请输入地上楼层"
                ></el-input>
              </el-form-item>
              <el-form-item label="地下楼层" label-width="130px" prop="undergroundFloor">
                <el-input
                  v-model.trim="Form_build.undergroundFloor"
                  size="small"
                  @keyup.native="buildLimitInt('undergroundFloor')"
                  @blur="buildLimitInt('undergroundFloor')"
                  placeholder="请输入地下楼层"
                ></el-input>
              </el-form-item>
              <el-form-item label="取得方式" label-width="130px" prop="obtainWay">
                <el-select
                  v-model="Form_build.obtainWay"
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
                <el-input
                  v-model.trim="Form_build.housePropertyNumber"
                  size="small"
                  placeholder="请输入房产证号"
                ></el-input>
              </el-form-item>
              <el-form-item label="土地证号" label-width="130px" prop="buildCertificate">
                <el-input
                  v-model.trim="Form_build.buildCertificate"
                  size="small"
                  placeholder="请输入土地证号"
                ></el-input>
              </el-form-item>
              <el-form-item label="占地面积(m²)" label-width="130px" prop="coveredArea">
                <el-input
                  v-model.trim="Form_build.coveredArea"
                  size="small"
                  @keyup.native="buildLimitFloat('coveredArea')"
                  placeholder="请输入占地面积"
                  @blur="buildLimitFloat('coveredArea', true)"
                ></el-input>
              </el-form-item>
              <el-form-item label="完损等级" label-width="130px" prop="lossGrade">
                <el-input v-model.trim="Form_build.lossGrade" size="small" placeholder="请输入完损等级"></el-input>
              </el-form-item>
              <el-form-item label="结构类型" label-width="130px" prop="structureType">
                <el-select
                  filterable
                  v-model="Form_build.structureType"
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
              <el-form-item label="权证编号" label-width="130px" prop="warrantNum">
                <el-input v-model.trim="Form_build.warrantNum" size="small" placeholder="请输入权证编号"></el-input>
              </el-form-item>
              <el-form-item label="所有权人" label-width="130px" prop="warrantPerson">
                <el-input
                  v-model.trim="Form_build.warrantPerson"
                  size="small"
                  placeholder="请输入所有权人"
                ></el-input>
              </el-form-item>
              <el-form-item label="权属说明" label-width="130px" prop="warrantRemark">
                <el-select
                  filterable
                  v-model="Form_build.warrantRemark"
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
              <el-form-item label="资产原值(万)" label-width="130px" prop="assetsValue">
                <el-input
                  v-model.trim="Form_build.assetsValue"
                  size="small"
                  @keyup.native="buildLimitFloat('assetsValue')"
                  @blur="buildLimitFloat('assetsValue', true)"
                  placeholder="请输入资产原值(万)"
                ></el-input>
              </el-form-item>
              <el-form-item label="人防面积(m²)" label-width="130px" prop="civilDefenceArea">
                <el-input
                  v-model.trim="Form_build.civilDefenceArea"
                  size="small"
                  @keyup.native="buildLimitFloat('civilDefenceArea')"
                  @blur="buildLimitFloat('civilDefenceArea', true)"
                  placeholder="请输入人防面积"
                ></el-input>
              </el-form-item>
              <el-form-item label="产权所属单位" label-width="130px" prop="propertyUnitId">
                <el-select
                  filterable
                  v-model="Form_build.propertyUnitId"
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
              <el-form-item
                label="电梯数量"
                label-width="130px"
                prop="elevatorNum"
                class="form-item-radio"
              >
                <el-input
                  v-model.trim="Form_build.elevatorNum"
                  size="small"
                  @keyup.native="buildLimitInt('elevatorNum')"
                  placeholder="请输入电梯数量"
                ></el-input>
              </el-form-item>
              <el-form-item label="是否租赁" label-width="130px" prop="isRent" class="form-item-radio">
                <el-radio
                  v-model="Form_build.outRent.isRent"
                  :label="1"
                  @change="rentStatusChange"
                >租赁</el-radio>
                <el-radio
                  v-model="Form_build.outRent.isRent"
                  :label="0"
                  @change="rentStatusChange"
                >不租赁</el-radio>
              </el-form-item>
              <el-form-item
                label="租用单位"
                label-width="130px"
                prop="outRent.rentUnit"
                v-if="Form_build.outRent.isRent === 1"
              >
                <el-select
                  v-model="Form_build.outRent.rentUnit"
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
                v-if="Form_build.outRent.isRent === 1"
              >
                <el-date-picker
                  v-model="Form_build.outRent.startTime"
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
                v-if="Form_build.outRent.isRent === 1"
              >
                <el-date-picker
                  v-model="Form_build.outRent.endTime"
                  type="date"
                  placeholder="选择租赁截止时间"
                  size="small"
                  style="width:200px;"
                  value-format="yyyy-MM-dd 23:59:59"
                ></el-date-picker>
              </el-form-item>
              <el-form-item
                label="租金标准(元)"
                label-width="130px"
                prop="outRent.rentMoney"
                v-if="Form_build.outRent.isRent === 1"
              >
                <el-input
                  v-model.trim="Form_build.outRent.rentMoney"
                  size="small"
                  @keyup.native="buildLimitFloat('rentMoney', null, 'outRent')"
                  placeholder="请输入租金标准"
                  @blur="buildLimitFloat('rentMoney', true, 'outRent')"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="财政备案"
                label-width="130px"
                prop="outRent.financeRemark"
                v-if="Form_build.outRent.isRent === 1"
              >
                <el-input
                  v-model.trim="Form_build.outRent.financeRemark"
                  size="small"
                  placeholder="请输入财政备案"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="楼座备注"
                label-width="130px"
                style="display: block;"
                prop="buildRemark"
              >
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 2, maxRows: 4}"
                  placeholder="请输入楼座备注"
                  v-model.trim="Form_build.buildRemark"
                  style="width:888px;"
                ></el-input>
              </el-form-item>
              <el-form-item label="上传图片" label-width="130px" style="display: block;">
                <div class="upload_box_img">
                  <el-upload
                    ref="uploadImg"
                    :headers="postHeaders"
                    :action="uploadBuildUrl"
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
                  <el-upload
                    ref="upload"
                    :headers="postHeaders"
                    :action="uploadBuildUrl"
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
                  </el-upload>
                </div>
              </el-form-item>
              <el-form-item label="经度(°)" label-width="130px" prop="buildLongitude">
                <el-input
                  v-model.trim="Form_build.buildLongitude"
                  size="small"
                  @blur="buildMapBindCoord('buildLongitude')"
                  @keyup.native="buildLimitFloat('buildLongitude')"
                  placeholder="请输入经度"
                ></el-input>
              </el-form-item>
              <el-form-item label="纬度(°)" label-width="130px" prop="buildLatitude">
                <el-input
                  v-model.trim="Form_build.buildLatitude"
                  size="small"
                  @blur="buildMapBindCoord('buildLatitude')"
                  @keyup.native="buildLimitFloat('buildLatitude')"
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
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <prev-btn
          :fontSize="14"
          :marginLeft="0"
          v-show="addInfoProcess === 1 || addInfoProcess === 2"
          @clickBtn="prevStep"
        ></prev-btn>
        <next-btn :fontSize="14" :marginLeft="0" v-show="addInfoProcess === 0" @clickBtn="nextStep"></next-btn>
        <next-btn
          :fontSize="14"
          :marginLeft="100"
          v-show="addInfoProcess === 1"
          @clickBtn="nextStep"
        ></next-btn>
        <confirm-btn
          :fontSize="14"
          :marginLeft="100"
          text="跳过"
          v-show="addInfoProcess === 1"
          @clickBtn="jumpToNext"
        ></confirm-btn>
        <confirm-btn
          :fontSize="14"
          :marginLeft="100"
          text="保存"
          v-show="addInfoProcess === 2"
          @clickBtn="addBuild(true)"
        ></confirm-btn>
      </span>
    </el-dialog>
    <!-- /完善资料 -->
  </div>
</template>

<script>
import {
  getConfigureApprovalList,
  saveSubmitConfigure,
  getBasicConfigure,
  getConfigureDetail,
  getReapportionUnit,
  getAutoConditionList,
  getAutoAllocationRoomList,
  leaderReviewDetail,
  addConfigurePlan,
  getConfigurePlanDataCount,
  getConfigurePlanList,
  deleteConfigurePlan,
  updateConfigurePlan,
  getArtificialAllocation,
  downloadFile,
  getSchemeName,
  getPlanDataDetail,
  getLeaderApprovalAdvice,
  getChangeData
} from "./api";
import { getUserUnitList, getRegionList, uploadUrl, getAllYardList, getAllUnitList } from "@/axios/commonApi";
import { BASE_URL, url_name_1 } from "@/axios/global"
import {
  getYardCoordinate,
  getEstateNameList
} from "@/pages/baseInfoManagement/api";
import { eventBus } from "@/eventBus/eventBus";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import prevBtn from "@/components/button/prevBtn";
import nextBtn from "@/components/button/nextBtn";
import yearSelect from "@/components/yearPicker/yearSelect";
import myMap from "@/components/map/map_2";
import yardMap from "@/components/map/map";
import { debug, debuglog } from 'util';
import { fail } from 'assert';
const uuidv1 = require('uuid/v1');
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
    prevBtn,
    nextBtn,
    yearSelect,
    myMap,
    yardMap
  },
  data() {
    let validcodeBegin = (rule, value, callback) => {
      const rentBegin = new Date(this.Form_build.outRent.startTime).getTime();
      const rentEnd = new Date(this.Form_build.outRent.endTime).getTime();
      if (!rentBegin) {
        callback(new Error('租赁开始时间不能为空'));
      }
      if (rentBegin && rentEnd && (rentBegin > rentEnd)) {
        callback(new Error('租赁开始时间不能大于租赁截止时间'));
      }
      callback()
    };
    let validcodeEnd = (rule, value, callback) => {
      const rentBegin = new Date(this.Form_build.outRent.startTime).getTime();
      const rentEnd = new Date(this.Form_build.outRent.endTime).getTime();
      if (!rentEnd) {
        callback(new Error('租赁截止时间不能为空'));
      }
      if (rentBegin && rentEnd && (rentBegin > rentEnd)) {
        callback(new Error('租赁截止时间不能小于租赁开始时间'));
      }
      callback()
    };
    return {
      activeName: 'yard',
      tableHeaderCellStyle: {
        "background-color": "#bee2f3",
        "border-color": "#c8c8c8",
        padding: "6px 0",
        color: "#333333"
      },
      tableCellStyle: {
        "border-color": "#c8c8c8",
        padding: '6px 0px'
      },
      unitId: '',
      username: "",
      userId: "",
      configureNo: "",
      buildIdList: [],
      startTime: "",
      endTime: "",
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      selectRow: [],
      selectData: {},
      selectDataArray: [],
      tableData: [],
      distributeChangeList: [],
      operateType: "",
      dialogVisible: {
        unit: false,
        project: false,
        leader: false,
        addInfo: false
      },
      unitOpts: [],
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
      regionOpts: [],
      regionIdList: [],
      autoConditionList: [],
      currentDialogName: '',
      currentForm: null,
      tipsText: '',
      distributionActiveName: 'table1',
      sureDistributionActiveName: '',
      currentAutoCondition: null,
      sureCurrentAutoCondition: null,
      isSureDistributePage: false,
      distributeTableData: [],
      replaceMode: 1,
      baseInfoForm: {
        unitName: '',
        unitCharger: '',
        unitComplement: null,
        actualNumber: null,
        totalArea: null,
        PerCapitaArea: null,
        contactPhone: null,
        contacts: '',
        unitProperty: '',
        unitAddress: '',
        applyReason: '',
        unitId: null,
        elevatorsNumber: null,
        regionName: '',
        newUnitName: '',
        enclosureList: [],
        allocationPlan: null,
        allocationPlanName: '',
        selectionPlan: null,
        selectionPlanName: '',
        planName: '',
        leaderAdvice: ''
      },
      Form_unit: {
        auditState: 1,
        configureId: null,
        configureRooms: [
          {
            roomAreaMax: '',
            roomAreaMin: '',
            roomNumber: ''
          }
        ],
        elevatorsNumber: 0,
        processInstanceId: null,
        regionIds: [],
        step: null,
        taskId: null,
        userId: null,
        userName: '',
        redistributionUnitId: '',
        comment: ''
      },
      Form_project: {
        allocationPlan: 1,
        auditState: 0,
        configureId: null,
        configurePlanDataList: [],
        processInstanceId: null,
        selectionPlan: 1,
        step: null,
        taskId: null,
        userId: null,
        userName: ''
      },
      Form_leader: {
        allocationPlan: null,
        auditState: 1,
        comment: '',
        configureId: null,
        processInstanceId: null,
        step: null,
        taskId: null,
        userId: null,
        userName: ''
      },
      Form_addInfo: {
        configureId: null,
        processInstanceId: null,
        step: null,
        auditState: 0,
        taskId: null,
        userId: null,
        userName: ''
      },
      Form_yard: {
        yard: {
          type: 1,
          regionId: '',
          yardId: null,
          yardLatitude: null,
          yardLongitude: null,
          yardName: '',
          yardPosition: ''
        },
        yardAndestateVoList: []
      },
      Form_build: {
        uuid: "",
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
        elevatorNum: 0,
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
        floorDrawingList: [], //  楼座新增附件
        fileList: [],
        fileListImg: []
      },
      Form_replacement: {
        makeMoney: '',
        profit: '',
        area: '',
        improvingDataList: []
      },
      Form_rent: {
        rentalMode: '',
        rent: '',
        term: '',
        area: '',
        improvingDataList: []
      },
      Form_construction: {
        expenditure: '',
        area: '',
        projectName: '',
        improvingDataList: []
      },
      localSaveBuildInfo: [],
      localSaveBuildList: [],
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
      rentalModeOpts: ['年', '季'],
      yardOptionsModal: [],
      uploadBuildUrl: `${uploadUrl}?uploadType=build`,
      uploadImprovingUrl: `${uploadUrl}?uploadType=improvingData`,
      acceptType: "image/jpeg,image/gif,image/png,image/bmp,.doc,.docx,.xlsx,.ppt,.pdf,.rar,.zip",
      acceptImgType: "image/jpeg,image/gif,image/png,image/bmp",
      limitImgNum: 1,
      limitNum: 5,
      fileList: [],
      fileListImg: [],
      fileListImproving: [],
      uploadNum: 0,
      uploadImgNum: 0,
      uploadImprovingNum: 0,
      uploadedNum: 0,
      uploadedImgNum: 0,
      uploadedImprovingNum: 0,
      uploadSuccessNum: 0,
      uploadSuccessImgNum: 0,
      uploadSuccessImprovingNum: 0,
      submitFlag: false,
      submitImgFlag: false,
      submitImprovingFlag: false,
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
      unitOptions: [],
      unitAuthorityOptions: [],
      AllSchemeTableData: [],
      schemeTableData: [],
      schemeCurrentPage: 1,
      schemePageSize: 5,
      schemeTotal: 0,
      planTableData: [],
      planCurrentPage: 1,
      planPageSize: 5,
      planTotal: 0,
      oldPlanName: '',
      selectedSchemeId: '',
      downloadCheckedList: [],
      fileDownLoadCheckedList: [],
      distributeCurrentPage: 1,
      distributePageSize: 5,
      distributeTotal: 0,
      currentPlanId: null,
      addInfoType: 'yard',
      estateNameOpts: [],
      estateNameFlag: true,
      serviceContentFlag: true,
      startTimeFlag: true,
      startTimeLimitFlag: true,
      endTimeFlag: true,
      endTimeLimitFlag: true,
      contractAmountFlag: true,
      submitTipsArray: [],
      serviceContentOpts: [
        {
          id: 1,
          text: '安保'
        },
        {
          id: 2,
          text: '保洁'
        },
        {
          id: 3,
          text: '绿化'
        },
        {
          id: 4,
          text: '零星维修'
        },
        {
          id: 5,
          text: '收发'
        }
      ],
      addYardName: '',
      addYardLongitude: '',
      addYardLatitude: '',
      roomNumberSubmitFlag: false,
      roomAreaNotNullFlag: false,
      roomAreaRangeRepeatFlag: false,
      roomAreaRangeErrorFlag: false,
      addInfoProcess: 0,
      activeAuditState: null,
      activeAuditStateFormName: null,
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
      rules_unit: {
        auditState: [
          { required: true, message: '请选择审查结果', trigger: "blur" }
        ],
        regionIds: [
          { required: true, message: '请选择区域', trigger: "change" }
        ],
        elevatorsNumber: [
          { required: true, message: '请选择电梯台数', trigger: "change" }
        ],
        comment: []
      },
      rules_project: {},
      rules_leader: {
        auditState: [
          { required: true, message: '请选择审批结果', trigger: "blur" }
        ],
        comment: []
      },
      rules_build: {
        yardId: [
          { required: true, message: "院落名称不能为空", trigger: "blur" }
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
        housePropertyNumber: [
          { required: true, message: "房产证号不能为空", trigger: "blur" }
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
      },
      rules_yard: {
        'yard.yardName': [
          { required: true, message: "院落名称不能为空", trigger: "blur" }
        ],
        'yard.yardPosition': [
          { required: true, message: "院落地址不能为空", trigger: "blur" }
        ],
        'yard.regionId': [
          { required: true, message: "行政区划不能为空", trigger: "change" }
        ],
        'yard.yardLongitude': [
          { required: true, message: "经度不能为空", trigger: "blur" }
        ],
        'yard.yardLatitude': [
          { required: true, message: "纬度不能为空", trigger: "blur" }
        ]
      },
      rules_replacement: {
        makeMoney: [
          { required: true, message: "补钱不能为空", trigger: "blur" }
        ],
        profit: [
          { required: true, message: "收益不能为空", trigger: "blur" }
        ],
        area: [
          { required: true, message: "面积不能为空", trigger: "blur" }
        ]
      },
      rules_rent: {
        rentalMode: [
          { required: true, message: "付款周期不能为空", trigger: "blur" }
        ],
        rent: [
          { required: true, message: "租金不能为空", trigger: "blur" }
        ],
        term: [
          { required: true, message: "期限不能为空", trigger: "blur" }
        ],
        area: [
          { required: true, message: "面积不能为空", trigger: "blur" }
        ]
      },
      rules_construction: {
        expenditure: [
          { required: true, message: "财政支出不能为空", trigger: "blur" }
        ],
        area: [
          { required: true, message: "面积不能为空", trigger: "blur" }
        ],
        projectName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" }
        ]
      },
      activeInfo: null,
      popoverRepairInfo: false,
      submitAllFlag: false,
      distributeNum: 0,
      pickerOptions_start: {
      },
      pickerOptions_end: {
      },
      currentAddInfoType: ''
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
    },
    estateNameFlag: {
      handler(val, oldVal) {
        if (!val) {
          const tips = '物业名称不能为空';
          if (this.submitTipsArray.indexOf(tips) === -1) {
            this.submitTipsArray.push(tips)
          }
        } else {
          const tips = '物业名称不能为空';
          if (this.submitTipsArray.indexOf(tips) > -1) {
            const i = this.submitTipsArray.indexOf(tips);
            this.submitTipsArray.splice(i, 1);
          }
        }
      },
      immediate: false
    },
    serviceContentFlag: {
      handler(val, oldVal) {
        if (!val) {
          const tips = '服务内容不能为空';
          if (this.submitTipsArray.indexOf(tips) === -1) {
            this.submitTipsArray.push(tips)
          }
        } else {
          const tips = '服务内容不能为空';
          if (this.submitTipsArray.indexOf(tips) > -1) {
            const i = this.submitTipsArray.indexOf(tips);
            this.submitTipsArray.splice(i, 1);
          }
        }
      },
      immediate: false
    },
    startTimeFlag: {
      handler(val, oldVal) {
        if (!val) {
          const tips = '开始使用时间不能为空';
          if (this.submitTipsArray.indexOf(tips) === -1) {
            this.submitTipsArray.push(tips)
          }
        } else {
          const tips = '开始使用时间不能为空';
          if (this.submitTipsArray.indexOf(tips) > -1) {
            const i = this.submitTipsArray.indexOf(tips);
            this.submitTipsArray.splice(i, 1);
          }
        }
      },
      immediate: false
    },
    startTimeLimitFlag: {
      handler(val, oldVal) {
        if (!val) {
          const tips = '开始使用时间不能大于结束使用时间';
          if (this.submitTipsArray.indexOf(tips) === -1) {
            this.submitTipsArray.push(tips)
          }
        } else {
          const tips = '开始使用时间不能大于结束使用时间';
          if (this.submitTipsArray.indexOf(tips) > -1) {
            const i = this.submitTipsArray.indexOf(tips);
            this.submitTipsArray.splice(i, 1);
          }
        }
      },
      immediate: false
    },
    endTimeFlag: {
      handler(val, oldVal) {
        if (!val) {
          const tips = '结束使用时间不能为空';
          if (this.submitTipsArray.indexOf(tips) === -1) {
            this.submitTipsArray.push(tips)
          }
        } else {
          const tips = '结束使用时间不能为空';
          if (this.submitTipsArray.indexOf(tips) > -1) {
            const i = this.submitTipsArray.indexOf(tips);
            this.submitTipsArray.splice(i, 1);
          }
        }
      },
      immediate: false
    },
    endTimeLimitFlag: {
      handler(val, oldVal) {
        if (!val) {
          const tips = '结束使用时间不能小于开始使用时间';
          if (this.submitTipsArray.indexOf(tips) === -1) {
            this.submitTipsArray.push(tips)
          }
        } else {
          const tips = '结束使用时间不能小于开始使用时间';
          if (this.submitTipsArray.indexOf(tips) > -1) {
            const i = this.submitTipsArray.indexOf(tips);
            this.submitTipsArray.splice(i, 1);
          }
        }
      },
      immediate: false
    },
    contractAmountFlag: {
      handler(val, oldVal) {
        if (!val) {
          const tips = '使用金额不能为空';
          if (this.submitTipsArray.indexOf(tips) === -1) {
            this.submitTipsArray.push(tips)
          }
        } else {
          const tips = '使用金额不能为空';
          if (this.submitTipsArray.indexOf(tips) > -1) {
            const i = this.submitTipsArray.indexOf(tips);
            this.submitTipsArray.splice(i, 1);
          }
        }
      },
      immediate: false
    }
  },
  methods: {
    // 改变分配方案
    allocationPlanChange(val) {
      if (val === 1) {
        const name = this.currentAutoCondition.name;
        const tableName = this.currentAutoCondition.tableName;
        const currentPage = this.currentAutoCondition.currentPage;
        if (this.selectData[name] && this.selectData[name][currentPage]) {
          const rows = this.selectData[name][currentPage];
          this.$nextTick(() => {
            rows.forEach(row => {
              this.$refs[tableName][0].toggleRowSelection(this.currentAutoCondition.tableData[row.index], true);
            })
          })
        }
      }
    },
    // 楼座详情
    showInfoDetail(index) {
      this.$refs['Form_build'].clearValidate();
      this.activeInfo = index;
      this.popoverRepairInfo = false;
      const currentObj = JSON.parse(JSON.stringify(this.localSaveBuildInfo[index]));
      this.Form_build = currentObj;
      this.fileList = currentObj.fileList;
      this.fileListImg = currentObj.fileListImg;
      this.uploadedNum = currentObj.fileList.length;
      this.uploadedImgNum = currentObj.fileListImg.length;
      this.selectYardHandle(currentObj.yardId);
      this.$refs.map.setBuildPosition(currentObj.buildLongitude, currentObj.buildLatitude, currentObj.buildName, 'build');

    },
    // 基本信息到院落
    baseInfoToYard() {
      this.addInfoProcess++
      this.addInfoType = 'yard';
      // 高度适应
      const contentBox = $('.addInfo-dialog').find('.dialog-content');
      const stepBox = $('.addInfo-dialog').find('.step-item')[1];
      const h = $(stepBox).outerHeight();
      contentBox.css('height', h);
      // 移动
      const distance = 1100 * this.addInfoProcess;
      const box = $('.addInfo-dialog').find('.dialog-content');
      box.animate({ left: `-${distance}px` });
      // 补签收益保留一项的值
      if (this.activeAuditState === 2) {
        if (this.replaceMode === 1) {
          this.Form_replacement.profit = '0';
        } else {
          this.Form_replacement.makeMoney = '0';
        }
      }
      // 清空上传文件的信息
      this.uploadImprovingNum = 0;
      this.uploadedImprovingNum = 0;
      // this.uploadSuccessImprovingNum = 0;
      this.submitImprovingFlag = false;
    },
    // 完善资料下一步
    nextStep() {
      const body = $('.addInfo-dialog').find('.el-dialog__body');
      if (this.addInfoProcess === 0) {
        this.$refs[this.activeAuditStateFormName].validate(valid => {
          if (valid) {
            if (this.uploadImprovingNum === 0) {
              this.baseInfoToYard();
            } else {
              this.$refs.improvingUpload.submit();
            }
          } else {
            return false;
          }
        });
      } else if (this.addInfoProcess === 1) {
        this.$refs['Form_yard'].validate(valid => {
          if (valid) {
            // 验证判断
            if (this.Form_yard.yard.type === 1) {
              this.estateNameSelectChange();
              this.serviceContentSelectChange();
              this.startTimeBlur();
              this.endTimeBlur();
              this.contractAmountBlur();
              if (this.estateNameFlag && this.serviceContentFlag && this.startTimeFlag && this.startTimeLimitFlag && this.endTimeFlag && this.endTimeLimitFlag && this.contractAmountFlag) {
                this.addYardName = this.Form_yard.yard.yardName
                this.addYardLongitude = this.Form_yard.yard.yardLongitude
                this.addYardLatitude = this.Form_yard.yard.yardLatitude
                this.getYardDic();
                this.getUnitDic();
                this.estateNameFlag = true;
                this.serviceContentFlag = true;
                this.startTimeFlag = true;
                this.startTimeLimitFlag = true;
                this.endTimeFlag = true;
                this.endTimeLimitFlag = true;
                this.contractAmountFlag = true;
                this.submitTipsArray = [];
                this.addInfoProcess++
                this.addInfoType = 'build';
                const distance = 1100 * this.addInfoProcess;
                // 高度适应
                const contentBox = $('.addInfo-dialog').find('.dialog-content');
                const stepBox = $('.addInfo-dialog').find('.step-item')[2];
                const h = $(stepBox).outerHeight();
                contentBox.css('height', h);
                // 移动
                const box = $('.addInfo-dialog').find('.dialog-content');
                box.animate({ left: `-${distance}px` });
              }
            } else {
              this.getYardDic();
              this.getUnitDic();
              this.estateNameFlag = true;
              this.serviceContentFlag = true;
              this.startTimeFlag = true;
              this.startTimeLimitFlag = true;
              this.endTimeFlag = true;
              this.endTimeLimitFlag = true;
              this.contractAmountFlag = true;
              this.submitTipsArray = [];
              this.addInfoProcess++
              this.addInfoType = 'build';
              const distance = 1100 * this.addInfoProcess;
              // 高度适应
              const contentBox = $('.addInfo-dialog').find('.dialog-content');
              const stepBox = $('.addInfo-dialog').find('.step-item')[2];
              const h = $(stepBox).outerHeight();
              contentBox.css('height', h);
              // 移动
              const box = $('.addInfo-dialog').find('.dialog-content');
              box.animate({ left: `-${distance}px` });
            }
          } else {
            return false;
          }
        });
      }
      body.scrollTop(0)
    },
    // 完善资料上一步
    prevStep() {
      this.addInfoProcess--;
      const body = $('.addInfo-dialog').find('.el-dialog__body');
      if (this.addInfoProcess === 0) {
        const contentBox = $('.addInfo-dialog').find('.dialog-content');
        const stepBox = $('.addInfo-dialog').find('.step-item')[0];
        const h = $(stepBox).outerHeight();
        contentBox.css('height', h);
        this.uploadedImprovingNum = this.fileListImproving.length;
      } else if (this.addInfoProcess === 1) {
        // 高度适应
        this.addInfoType = 'yard';
        const contentBox = $('.addInfo-dialog').find('.dialog-content');
        const stepBox = $('.addInfo-dialog').find('.step-item')[1];
        const h = $(stepBox).outerHeight();
        contentBox.css('height', h);
      }
      // 移动
      body.scrollTop(0)
      const distance = 1100 * this.addInfoProcess;
      const box = $('.addInfo-dialog').find('.dialog-content');
      box.animate({ left: `-${distance}px` });
    },
    // 完善资料跳过
    jumpToNext() {
      this.getYardDic();
      this.getUnitDic();
      this.addInfoProcess++
      this.addInfoType = 'build';
      this.clearForm(this.Form_yard);
      this.Form_yard.yard.type = 1;
      this.$refs['Form_yard'].clearValidate();
      // 高度适应
      const contentBox = $('.addInfo-dialog').find('.dialog-content');
      const stepBox = $('.addInfo-dialog').find('.step-item')[2];
      const h = $(stepBox).outerHeight();
      contentBox.css('height', h);
      // 移动
      const body = $('.addInfo-dialog').find('.el-dialog__body');
      body.scrollTop(0)
      const distance = 1100 * this.addInfoProcess;
      const box = $('.addInfo-dialog').find('.dialog-content');
      box.animate({ left: `-${distance}px` });
    },
    // 完善资料新增楼座
    addBuild(flag) {
      this.$refs['Form_build'].validate(valid => {
        if (valid) {
          if (flag) {
            this.submitAllFlag = true;
          } else {
            this.submitAllFlag = false;
          }
          (this.uploadNum === 0 && this.uploadImgNum === 0) ? this.saveBuildForm() : this.submitUpload();
        } else {
          return false;
        }
      });
    },
    // 完善资料删除楼座信息
    deleteBuild() {
      if (this.localSaveBuildInfo.length > 0) {
        for (let i = this.localSaveBuildInfo.length - 1; i >= 0; i--) {
          if (this.localSaveBuildInfo[i].uuid === this.Form_build.uuid) {
            this.localSaveBuildInfo.splice(i, 1);
            this.localSaveBuildList.splice(i, 1);
            break;
          }
        }
      }
      this.$refs['Form_build'].resetFields();
      this.clearForm(this.Form_build);
      this.Form_build.outRent.isRent = 0;
      this.Form_build.elevatorNum = 0;
      this.showMap = false;
      this.uploadChangeFlag = false;
      this.uploadImgChangeFlag = false;
      this.submitImgFlag = false;
      this.submitFlag = false;
      setTimeout(() => {
        this.showMap = true;
        this.uploadNum = 0;
        this.uploadImgNum = 0;
        this.uploadedNum = 0;
        this.uploadedImgNum = 0;
        this.uploadSuccessNum = 0;
        this.uploadSuccessImgNum = 0;
        this.fileList = [];
        this.fileListImg = [];
      }, 1000);
    },
    // 完善资料本地保存楼座信息
    saveBuildForm() {
      const Form = JSON.parse(JSON.stringify(this.Form_build));
      if (Form.uuid) {
        for (let i = this.localSaveBuildInfo.length - 1; i >= 0; i--) {
          if (this.localSaveBuildInfo[i].uuid === Form.uuid) {
            this.localSaveBuildInfo.splice(i, 1, Form);
            break;
          }
        }
      } else {
        Form.uuid = uuidv1();
        this.localSaveBuildInfo.push(Form);
        this.localSaveBuildList.push({
          buildName: Form.buildName,
          id: Form.uuid
        })
      }
      if (this.submitAllFlag) {
        this.submitForm();
      } else {
        this.$refs['Form_build'].resetFields();
        this.clearForm(this.Form_build);
        this.Form_build.outRent.isRent = 0;
        this.Form_build.elevatorNum = 0;
        this.showMap = false;
        this.uploadChangeFlag = false;
        this.uploadImgChangeFlag = false;
        this.submitImgFlag = false;
        this.submitFlag = false;
        setTimeout(() => {
          this.showMap = true;
          this.uploadNum = 0;
          this.uploadImgNum = 0;
          this.uploadedNum = 0;
          this.uploadedImgNum = 0;
          this.uploadSuccessNum = 0;
          this.uploadSuccessImgNum = 0;
          this.fileList = [];
          this.fileListImg = [];
        }, 1000)
      }
      this.activeInfo = null;
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
    // 获取列表
    getList() {
      this.loadingStart('.table-box');
      getConfigureApprovalList(this.currentPage, this.pageSize, this.configureNo, this.startTime, this.endTime, this.userId, this.unitId)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach(item => {
            item.applyStatusText = this.getApplyStatus(item.step);
            if (!item.totalArea) {
              item.totalArea = '无';
            }
            if (!item.actualNumber) {
              item.actualNumber = '无';
            }
            if (!item.perArea) {
              item.perArea = '无';
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
      this.unitId = "";
      this.getList();
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
    // 获取单位列表
    getReapportionUnit() {
      getReapportionUnit().then(res => {
        this.unitOpts = res;
        this.unitOpts.unshift({
          unitId: '',
          unitName: '暂无单位'
        })
      }).catch(err => {
        const msg = err.result ? err.result : '重新分配的单位列表获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 获取行政区划列表
    getRegionList() {
      getRegionList().then(res => {
        this.regionOpts = res;
        this.regionOpts.forEach(region => {
          this.regionIdList.push(region.regionId);
        })
      }).catch(err => {
        const msg = err.result ? err.result : '行政区划列表获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 获取申请状态 
    getApplyStatus(status) {
      switch (status) {
        case 2:
          return '单位审查';
          break;
        case 3:
          return '分配方案';
          break;
        case 4:
          return '领导审批';
          break;
        case 5:
          return '完善资料';
          break;
      }
    },
    // 获取审核状态
    getReviewAuditState(status) {
      switch (status) {
        case 1:
          return '调剂';
          break;
        case 2:
          return '置换';
          break;
        case 3:
          return '租用';
          break;
        case 4:
          return '建设';
          break;
      }
    },
    // 配置流程
    openDialog(row) {
      getConfigureDetail(row.configureId).then(res => {
        for ([key, value] of Object.entries(res.configure)) {
          if (this.baseInfoForm[key] !== undefined) {
            if (value) {
              this.baseInfoForm[key] = value;
            } else {
              this.baseInfoForm[key] = '暂无';
            }
          }
        }
        if (res.configureEnclosureList && res.configureEnclosureList.length > 0) {
          res.configureEnclosureList.forEach(item => {
            item.detail = `${item.enclosurePhysicsUrl},${item.enclosureName}`;
          });
          this.baseInfoForm.enclosureList = res.configureEnclosureList;
        }
      }).catch(err => {
        const msg = err.result ? err.result : '配置申请信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
      this.getBasicConfigure(row.configureId);
      switch (row.step) {
        case 2:
          this.dialogVisible.unit = true;
          this.currentDialogName = 'unit';
          this.currentForm = this.Form_unit;
          this.operateType = "单位审查";
          this.tipsText = '审查'
          this.currentForm.regionIds = this.regionIdList;
          break;
        case 3:
          this.dialogVisible.project = true;
          this.currentDialogName = 'project';
          this.currentForm = this.Form_project;
          this.operateType = "拟定分配方案";
          this.tipsText = '拟定分配方案'
          getAutoConditionList(row.configureId).then(res => {
            res.forEach((item, index) => {
              item.name = `${item.roomAreaMin}(m²)-${item.roomAreaMax}(m²)`;
              item.tableName = `table${index + 1}`;
              item.tableData = [];
              item.currentPage = 1;
              item.pageSize = 5;
              item.total = 0;
              item.gotTheData = false;
            });
            this.autoConditionList = res;
            this.currentAutoCondition = this.autoConditionList[0];
            this.distributionActiveName = `table1`;
            this.distributeNum = this.currentAutoCondition.roomNumber;
            this.getAutoAllocationRoomList();
          }).catch(err => {
            const msg = err.result ? err.result : '自动分房的筛选条件列表获取失败';
            this.$message({
              message: msg,
              type: "error"
            });
          })
          leaderReviewDetail(row.configureId, 2).then(res => {
            this.baseInfoForm.elevatorsNumber = res.elevatorsNumber;
            this.baseInfoForm.regionName = res.regionList.join(',');
            this.baseInfoForm.newUnitName = res.newUnitName || '';
          }).catch(err => {
            const msg = err.result ? err.result : '单位审查相关信息获取失败';
            this.$message({
              message: msg,
              type: "error"
            });
          })
          getLeaderApprovalAdvice(row.configureId).then(res => {
            if (res) {
              this.baseInfoForm.leaderAdvice = res
            }
          }).catch(err => {
            const msg = err.result ? err.result : '领导审批意见获取失败';
            this.$message({
              message: msg,
              type: "error"
            });
          })
          break;
        case 4:
          this.dialogVisible.leader = true;
          this.currentDialogName = 'leader';
          this.currentForm = this.Form_leader;
          this.operateType = "领导审批";
          this.tipsText = '领导审批'
          leaderReviewDetail(row.configureId, 3).then(res => {
            this.currentPlanId = res.planId;
            this.currentForm.configureId = row.configureId;
            this.currentForm.allocationPlan = res.allocationPlan;
            this.baseInfoForm.allocationPlan = res.allocationPlan;
            this.baseInfoForm.allocationPlanName = this.getReviewAuditState(res.allocationPlan);
            this.baseInfoForm.selectionPlan = res.selectionPlan;
            this.baseInfoForm.selectionPlanName = res.selectionPlan === 1 ? '智能分配' : '人工分配';
            if (res.allocationPlan === 1) {
              if (res.selectionPlan === 1) {
                this.getPlanDataDetail();
              } else {
                this.getSchemeDataDetail();
                this.getSchemeName();
                this.getChangeData(this.currentPlanId);
              }
            }
          }).catch(err => {
            const msg = err.result ? err.result : '分配方案相关信息获取失败';
            this.$message({
              message: msg,
              type: "error"
            });
          })
          break;
        case 5:
          this.dialogVisible.addInfo = true;
          this.currentDialogName = 'addInfo';
          this.currentForm = this.Form_addInfo;
          this.activeAuditState = row.allocationPlan;
          this.addInfoProcess = 0;
          this.operateType = "完善资料";
          this.tipsText = '资料添加'
          this.showMap = true;
          this.getEstateNameList();
          let formName = '';
          switch (this.activeAuditState) {
            case 2:
              this.activeAuditStateFormName = 'Form_replacement';
              formName = 'Form_replacement';
              this.currentAddInfoType = '置换';
              break;
            case 3:
              this.activeAuditStateFormName = 'Form_rent';
              formName = 'Form_rent';
              this.currentAddInfoType = '租用';
              break;
            case 4:
              this.activeAuditStateFormName = 'Form_construction';
              formName = 'Form_construction';
              this.currentAddInfoType = '建设';
              break;
          }
          this.$nextTick(() => {
            this.$refs[formName].clearValidate();
            const contentBox = $('.addInfo-dialog').find('.dialog-content');
            const stepBox = $('.addInfo-dialog').find('.step-item')[0];
            const h = $(stepBox).outerHeight();
            contentBox.css('height', h);
          })
          break;
      }
      this.currentForm.configureId = row.configureId;
      this.currentForm.processInstanceId = row.processInstanceId;
      this.currentForm.taskId = row.taskId;
      this.currentForm.step = row.step;
      this.currentForm.userId = this.userId;
      this.currentForm.userName = sessionStorage.getItem('name');
    },
    // 获取改变的数据
    getChangeData(id) {
      getChangeData(id).then(res => {
        if (res) {
          this.distributeChangeList = res;
        }
      }).catch(err => {
        const msg = err.result ? err.result : '分配统计数据获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 获取方案名称
    getSchemeName() {
      getSchemeName(this.currentForm.configureId).then(res => {
        this.baseInfoForm.planName = res;
      }).catch(err => {
        const msg = err.result ? err.result : '方案名称获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 改变房间分配方案表格的页码
    leaderDistributeCurrentChange(pageNum) {
      this.distributeCurrentPage = pageNum;
      if (this.currentForm.selectionPlan === 1) {
        this.getPlanDataDetail();
      } else {
        this.getSchemeDataDetail();
      }
    },
    // 查看智能分配方案数据
    getPlanDataDetail() {
      getPlanDataDetail(this.distributeCurrentPage, this.distributePageSize, this.currentForm.configureId).then(res => {
        this.distributeTotal = res.total;
        this.distributeTableData = res.records[0].planDataDtoList;
        this.distributeTableData.forEach(item => {
          item.position = `${item.yardName}-${item.buildName}-${item.houseNumber}`;
          if (item.oldUnitNameList && item.oldUnitNameList.length > 0) {
            item.oldUnitName = item.oldUnitNameList.join(',');
          } else {
            item.oldUnitName = '无';
          }
        })
      }).catch(err => {
        const msg = err.result ? err.result : '房间分配方案数据获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查看人工分配方案数据
    getSchemeDataDetail() {
      getArtificialAllocation(this.distributeCurrentPage, this.distributePageSize, this.currentPlanId).then(res => {
        this.distributeTotal = res.total;
        this.distributeTableData = res.records;
        this.distributeTableData.forEach(item => {
          let oldUnitArr = [];
          let newUnitArr = [];
          item.position = `${item.yardName}-${item.buildName}-${item.houseNumber}`;
          item.newUnitList.forEach(newUnit => {
            if (newUnit.newUnitId !== 0) {
              newUnitArr.push(newUnit.newUnitName);
            }
          });
          item.oldUnitList.forEach(oldUnit => {
            if (oldUnit.oldUnitId !== 0) {
              oldUnitArr.push(oldUnit.oldUnitName);
            }
          });
          item.oldUnitName = oldUnitArr.join(',');
          item.newUnitName = newUnitArr.join(',');
        })
      }).catch(err => {
        const msg = err.result ? err.result : '该方案房间信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查询自动分房符合条件的房间
    getAutoAllocationRoomList() {
      getAutoAllocationRoomList(this.currentAutoCondition.currentPage, this.currentAutoCondition.pageSize, this.currentAutoCondition.elevatorsNumber, this.currentAutoCondition.regionId, this.currentAutoCondition.roomAreaMax, this.currentAutoCondition.roomAreaMin, this.baseInfoForm.unitId, this.currentAutoCondition.redistributionUnitId).then(res => {
        if (res && res.records.length > 0) {
          res.records.forEach(item => {
            item.position = `${item.yardName}-${item.buildName}-${item.houseNumber}`
            if (item.unitList && item.unitList.length > 0) {
              let unitNameArr = [];
              item.unitList.forEach(list => {
                unitNameArr.push(list.unitName);
              })
              item.unitName = unitNameArr.join(',')
            } else {
              item.unitName = '';
            }
          });
          res.records.forEach((item, index) => {
            item.index = index;
          })
          this.currentAutoCondition.tableData = res.records;
          this.currentAutoCondition.total = res.total;
          this.currentAutoCondition.gotTheData = true;
          const name = this.currentAutoCondition.name;
          const tableName = this.currentAutoCondition.tableName;
          const currentPage = this.currentAutoCondition.currentPage;
          if (this.selectData[name] && this.selectData[name][currentPage]) {
            const rows = this.selectData[name][currentPage];
            this.$nextTick(() => {
              rows.forEach(row => {
                this.$refs[tableName][0].toggleRowSelection(this.currentAutoCondition.tableData[row.index], true);
              })
            })
          }
        }
      }).catch(err => {
        const msg = err.result ? err.result : '房间信息列表获取失败';
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
    // 添加一条房间信息
    addHouseInfo() {
      const obj = {
        roomAreaMax: null,
        roomAreaMin: null,
        roomNumber: null
      };
      this.Form_unit.configureRooms.push(obj);
    },
    // 删除一条房间信息
    deleteHouseInfo(index) {
      if (this.Form_unit.configureRooms.length === 1) {
        this.$message({
          message: '至少要有一条房间信息',
          type: "warning"
        });
        return false;
      }
      this.Form_unit.configureRooms.splice(index, 1);
    },
    // 审批单选改变
    auditStateRadioChange(val) {
      const formName = `Form_${this.currentDialogName}`;
      const ruleName = `rules_${this.currentDialogName}`;
      this.$refs[formName].clearValidate(['auditState']);
      if (val === 2) {
        if (this[ruleName].comment.length === 0) {
          this[ruleName].comment.push({ required: true, message: "审批意见不能为空", trigger: "blur" });
        }
      } else {
        this[ruleName].comment = [];
        this.$refs[formName].clearValidate(['comment']);
      }
    },
    // 改变方案房间信息表格的页码
    planeCurrentChange(currentPage) {
      this.planCurrentPage = currentPage;
      this.getConfigurePlanDataCount();
    },
    // 修改方案
    updateScheme(planId) {
      if (planId) {
        sessionStorage.setItem('planId', planId);
        sessionStorage.setItem('configureId', this.currentForm.configureId);
        this.$router.push({
          name: 'roomDistributeView'
        })
      } else {
        this.$message({
          message: '请先编辑方案名称',
          type: "warning"
        });
      }
    },
    // 编辑方案名称
    updateName($event, index, name) {
      this.schemeTableData[index].readonly = false;
      this.$nextTick(() => {
        const inputs = $('.plan-name-column').find('input');
        $(inputs[index]).focus();
        this.oldPlanName = name;
      })
    },
    // 删除方案
    deleteScheme(planId, index, name) {
      if (planId) {
        this.$confirm(`此操作将永久删除方案${name}, 是否继续?`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            deleteConfigurePlan(planId).then(res => {
              this.$message({
                message: '方案删除成功',
                type: "success"
              });
              this.getSchemeList();
            }).catch(err => {
              const msg = err.result ? err.result : '方案删除失败';
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
      } else {
        const _index = (this.schemeCurrentPage - 1) * this.schemePageSize + index;
        this.allSchemeTableData.splice(_index, 1);
        this.schemeTotal = this.allSchemeTableData.length;
        const remainder = this.schemeTotal % this.schemePageSize;
        if (remainder > 0) {
          if (this.schemeTotal > this.schemePageSize) {
            this.schemeCurrentPage = Math.ceil(this.schemeTotal / this.schemePageSize);
            this.schemeTableData = this.allSchemeTableData.slice((this.schemeCurrentPage - 1) * this.schemePageSize);
          } else {
            this.schemeTableData = this.allSchemeTableData
          }
        } else {
          if (this.schemeTotal > this.schemePageSize) {
            this.schemeCurrentPage = this.schemeTotal / this.schemePageSize;
            this.schemeTableData = this.allSchemeTableData.slice((this.schemeCurrentPage - 1) * this.schemePageSize);
          } else {
            this.schemeTableData = this.allSchemeTableData;
          }
        }
      }
    },
    // 改变人工分配表格的页码
    schemeCurrentChange(currentPage) {
      this.schemeCurrentPage = currentPage;
      const startIndex = (this.schemeCurrentPage - 1) * this.schemePageSize;
      const endIndex = this.schemeCurrentPage * this.schemePageSize;
      this.schemeTableData = this.allSchemeTableData.slice(startIndex, endIndex);
    },
    // 添加方案
    addScheme() {
      const obj = {
        planName: '',
        readonly: true,
        planId: null,
        checked: false
      }
      this.allSchemeTableData.push(obj);
      this.schemeTotal = this.allSchemeTableData.length;
      const remainder = this.schemeTotal % this.schemePageSize;
      if (remainder > 0) {
        if (this.schemeTotal > this.schemePageSize) {
          this.schemeCurrentPage = Math.ceil(this.schemeTotal / this.schemePageSize);
          this.schemeTableData = this.allSchemeTableData.slice((this.schemeCurrentPage - 1) * this.schemePageSize);
        } else {
          this.schemeTableData = this.allSchemeTableData
        }
      } else {
        if (this.schemeTotal > this.schemePageSize) {
          this.schemeCurrentPage = this.schemeTotal / this.schemePageSize;
          this.schemeTableData = this.allSchemeTableData.slice((this.schemeCurrentPage - 1) * this.schemePageSize);
        } else {
          this.schemeTableData = this.allSchemeTableData;
        }
      }
    },
    // 方案名称input得到焦点
    planNameInputFocus(name) {
      this.oldPlanName = name;
    },
    // 保存方案名
    savePlanName(name, planId, index) {
      name = name.trim()
      if (name && name !== this.oldPlanName) {
        this.schemeTableData[index].readonly = true;
        if (!planId) {
          addConfigurePlan(this.currentForm.configureId, name).then(res => {
            this.getSchemeList();
            this.$message({
              message: '方案名称新增成功',
              type: "success"
            });
          }).catch(err => {
            const msg = err.result ? err.result : '方案名称新增失败';
            this.$message({
              message: msg,
              type: "error"
            });
          });
        } else {
          updateConfigurePlan(this.currentForm.configureId, planId, name).then(res => {
            this.getSchemeList();
            this.$message({
              message: '方案名称修改成功',
              type: "success"
            });
          }).catch(err => {
            const msg = err.result ? err.result : '方案名称修改失败';
            this.$message({
              message: msg,
              type: "error"
            });
          });
        }
      }
    },
    // 获取方案列表
    getSchemeList() {
      getConfigurePlanList(this.currentForm.configureId).then(res => {
        res.forEach(item => {
          item.readonly = true;
          item.checked = false;
        })
        this.allSchemeTableData = res;
        this.schemeTotal = res.length;
        const floorNum = Math.ceil(this.schemeTotal / this.schemePageSize);
        if (this.schemeTotal > this.schemePageSize) {
          if (floorNum < this.schemeCurrentPage) {
            this.schemeCurrentPage = floorNum;
          }
        } else {
          this.schemeCurrentPage = 1;
        }
        const startIndex = (this.schemeCurrentPage - 1) * this.schemePageSize;
        const endIndex = this.schemeCurrentPage * this.schemePageSize;
        this.schemeTableData = this.allSchemeTableData.slice(startIndex, endIndex);
        if (this.selectedSchemeId) {
          for (let i = 0; i < this.schemeTableData.length; i++) {
            if (this.schemeTableData[i].planId === this.selectedSchemeId) {
              this.schemeTableData[i].checked = true;
            }
          }
        }
      }).catch(err => {
        const msg = err.result ? err.result : '配置方案列表获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 上一步
    prev() {
      this.isSureDistributePage = false;
      this.operateType = '拟定分配方案';
      this.distributeChangeList = [];
      if (this.currentForm.selectionPlan === 1) {
        this.getAutoAllocationRoomList();
      } else {
        this.getSchemeList();
      }
    },
    // 下一步
    next() {
      this.operateType = '确认分配';
      if (this.currentForm.selectionPlan === 1) {
        const allKeys = Object.keys(this.selectData);
        if (allKeys.length === 0) {
          this.$message({
            message: '请选择至少一个房间',
            type: "warning"
          });
          return false;
        }
        // 判断个数是否匹配
        for (let i = 0; i < this.autoConditionList.length; i++) {
          let flag = true;
          const item = this.autoConditionList[i];
          if (item.roomNumber !== 0 && item.gotTheData === false) {
            this.$message({
              message: '所选的房间个数不匹配！',
              type: "warning"
            });
            return false;
          }
          if (item.total >= item.roomNumber) {
            for (let key in this.selectData) {
              if (key === item.name) {
                flag = false;
                const selectDataItem = this.selectData[key];
                let num = 0;
                for (let _key in selectDataItem) {
                  num += selectDataItem[_key].length;
                }
                if (num !== item.roomNumber) {
                  this.$message({
                    message: '所选的房间个数不匹配！',
                    type: "warning"
                  });
                  return false;
                }
              }
            }
            if (flag) {
              this.$message({
                message: '所选的房间个数不匹配！',
                type: "warning"
              });
              return false;
            }
          }
        }
        // selectData按照正序排序
        const keys = Object.keys(this.selectData).sort();
        let newSelectData = {}
        for (var i = 0; i < keys.length; i++) {
          const index = keys[i];
          newSelectData[index] = this.selectData[index];
        }

        this.isSureDistributePage = true;
        this.selectDataArray = [];
        for ([key_1, value_1] of Object.entries(newSelectData)) {
          let obj = {};
          obj.allTableData = [];
          const keys = Object.keys(value_1);
          keys.sort((a, b) => {
            return a - b;
          })
          keys.forEach(key => {
            const temp = value_1[key].sort((a, b) => {
              return a.index - b.index;
            })
            temp.forEach(item => {
              obj.allTableData.push(item);
            })
          })
          obj.name = key_1;
          obj.currentPage = 1;
          obj.pageSize = 5;
          obj.total = obj.allTableData.length || 0;
          const startIndex = (obj.currentPage - 1) * obj.pageSize;
          const endIndex = obj.currentPage * obj.pageSize;
          obj.tableData = obj.allTableData.slice(startIndex, endIndex) || [];
          this.selectDataArray.push(obj);
        }
        this.sureCurrentAutoCondition = this.selectDataArray[0];
        this.sureDistributionActiveName = this.selectDataArray[0].name;
      } else {
        if (!this.selectedSchemeId) {
          this.$message({
            message: '请选择一个方案',
            type: "warning"
          });
        } else {
          getConfigurePlanDataCount(this.selectedSchemeId).then(res => {
            if (res > 0) {
              this.isSureDistributePage = true;
              this.getConfigurePlanDataCount();
            } else {
              this.$message({
                message: '该方案暂无任何配置信息，请先编辑方案',
                type: "warning"
              });
            }
          }).catch(err => {
            const msg = err.result ? err.result : '方案个数获取失败';
            this.$message({
              message: msg,
              type: "error"
            });
          })
          this.getChangeData(this.selectedSchemeId);
        }
      }
    },
    // 获取方案下的房间信息
    getConfigurePlanDataCount() {
      getArtificialAllocation(this.planCurrentPage, this.planPageSize, this.selectedSchemeId).then(res => {
        res.records.forEach(item => {
          item.position = `${item.yardName}-${item.buildName}-${item.houseNumber}`;
          if (item.newUnitList.length > 0) {
            let newUnitArr = [];
            item.newUnitList.forEach(newUnit => {
              if (newUnit.newUnitId !== 0) {
                newUnitArr.push(newUnit.newUnitName);
              }
            });
            item.newUnitName = newUnitArr.join(',');
          } else {
            item.newUnitName = '暂无';
          }
          if (item.oldUnitList.length > 0) {
            let oldUnitArr = [];
            item.oldUnitList.forEach(oldUnit => {
              if (oldUnit.oldUnitId !== 0) {
                oldUnitArr.push(oldUnit.oldUnitName);
              }
              item.oldUnitName = oldUnitArr.join(',');
            });
          } else {
            item.oldUnitName = '暂无';
          }
        })
        this.planTableData = res.records;
        this.planTotal = res.total;
      }).catch(err => {
        const msg = err.result ? err.result : '该方案房间信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 改变智能分配的表格分页
    distributeCurrentChange(currentPage) {
      this.currentAutoCondition.currentPage = currentPage;
      this.getAutoAllocationRoomList();
    },
    // 改变确认智能分配的表格分页
    sureDistributeCurrentChange(currentPage) {
      this.sureCurrentAutoCondition.currentPage = currentPage;
      const startIndex = (this.sureCurrentAutoCondition.currentPage - 1) * this.sureCurrentAutoCondition.pageSize;
      const endIndex = this.sureCurrentAutoCondition.currentPage * this.sureCurrentAutoCondition.pageSize;
      this.sureCurrentAutoCondition.tableData = this.sureCurrentAutoCondition.allTableData.slice(startIndex, endIndex);
    },
    // 选中智能分配的row
    handleSelectionChange(data) {
      const name = this.currentAutoCondition.name;
      const currentPage = this.currentAutoCondition.currentPage;
      if (data.length > 0) {
        if (this.selectData[name] === undefined) {
          this.selectData[name] = {};
        }
        this.selectData[name][currentPage] = data;
      } else {
        if (this.selectData[name] && this.selectData[name][currentPage]) {
          delete this.selectData[name][currentPage];
        }
        const keys = Object.keys(this.selectData[name]);
        if (keys.length === 0) {
          delete this.selectData[name]
        }
      }
      const selectedObj = this.selectData[this.currentAutoCondition.name];
      let allNum = 0;
      if (selectedObj) {
        for (let key in selectedObj) {
          allNum += selectedObj[key].length;
        }
      }
      const distributeNum = this.currentAutoCondition.roomNumber - allNum;
      if (distributeNum >= 0) {
        this.distributeNum = distributeNum;
      }
    },
    // 全选智能分配row
    handleSelectionAllChange(data) {
      const name = this.currentAutoCondition.name;
      const currentPage = this.currentAutoCondition.currentPage;
      if (data.length > 0) {
        if (this.selectData[name] === undefined) {
          this.selectData[name] = {};
        }
        this.selectData[name][currentPage] = data;
      } else {
        if (this.selectData[name] && this.selectData[name][currentPage]) {
          delete this.selectData[name][currentPage];
        }
        const keys = Object.keys(this.selectData[name]);
        if (keys.length === 0) {
          delete this.selectData[name]
        }
      }
      const selectedObj = this.selectData[this.currentAutoCondition.name];
      let allNum = 0;
      if (selectedObj) {
        for (let key in selectedObj) {
          allNum += selectedObj[key].length;
        }
      }
      const distributeNum = this.currentAutoCondition.roomNumber - allNum;
      if (distributeNum >= 0) {
        this.distributeNum = distributeNum;
      } else {
        this.distributeNum = 0;
      }
    },
    // 选中人工分配的row
    schemeHandleSelectionChange(row) {
      if (row.checked === false) {
        this.selectedSchemeId = '';
        return;
      }
      if (!row.planId) {
        this.$message({
          message: '请先编辑方案名称',
          type: "warning"
        });
        this.schemeTableData.forEach(item => {
          item.checked = false;
        });
        this.selectedSchemeId = '';
      } else {
        this.schemeTableData.forEach(item => {
          if (item.planId !== row.planId) {
            item.checked = false;
          }
        });
        this.selectedSchemeId = row.planId;
      }
    },
    // 切换方案
    selectionPlanChange(num) {
      this.currentForm.selectionPlan = num;
      if (num === 2) {
        this.getSchemeList();
      } else {
        this.getAutoAllocationRoomList();
      }
    },
    // 切换智能分配的tab栏
    distributionTabClick(tab) {
      const name = tab.name;
      for (let i = 0; i < this.autoConditionList.length; i++) {
        if (this.autoConditionList[i].tableName === name) {
          this.currentAutoCondition = this.autoConditionList[i];
          break;
        }
      }
      this.getAutoAllocationRoomList();
      const selectedObj = this.selectData[this.currentAutoCondition.name];
      let allNum = 0;
      if (selectedObj) {
        for (let key in selectedObj) {
          allNum += selectedObj[key].length;
        }
      }
      const distributeNum = this.currentAutoCondition.roomNumber - allNum;
      if (distributeNum >= 0) {
        this.distributeNum = distributeNum;
      }
    },
    // 切换确认智能分配的tab栏
    sureDistributionTabClick(tab) {
      const name = tab.name;
      for (let i = 0; i < this.selectDataArray.length; i++) {
        if (this.selectDataArray[i].name === name) {
          this.sureCurrentAutoCondition = this.selectDataArray[i];
          this.sureDistributionActiveName = this.selectDataArray[i].name;
          break;
        }
      }
    },
    // 新增一条物业信息
    addEstate() {
      const obj = {
        estateId: null,
        estateName: '',
        serviceContentId: null,
        contractAmount: null,
        endTime: '',
        startTime: '',
        serviceContentList: []
      };
      this.Form_yard.yardAndestateVoList.push(obj);
    },
    // 删除一条物业信息
    handleDelete(index, row) {
      this.Form_yard.yardAndestateVoList.splice(index, 1);
    },
    // 获取物业名称列表
    getEstateNameList() {
      getEstateNameList().then((res => {
        this.estateNameOpts = res;
      })).catch(err => {
        const msg = err.result ? err.result : '物业名称下拉选项获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 查询院落及其楼座的坐标
    selectYardHandle(yardId) {
      if (yardId === this.addYardName) {
        this.$refs.map.setYardPosition(this.addYardLongitude, this.addYardLatitude, this.addYardName, 'yard');
        return false;
      }
      getYardCoordinate(yardId).then(res => {
        this.$refs.map.setYardPosition(res.yardLongitude, res.yardLatitude, res.yardName, 'yard');
      }).catch(err => {
        const msg = err.result ? err.result : '坐标信息获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 选择年份
    selectYear(name, val) {
      this.Form_build[name] = val;
    },
    // 获取所有单位字典项
    getUnitDic() {
      getAllUnitList()
        .then(res => {
          this.unitOptions = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : '单位信息获取失败';
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
          if (this.addYardName && this.addYardLongitude && this.addYardLatitude && this.Form_yard.yard.type === 1) {
            let temp = {};
            temp.yardId = this.addYardName;
            temp.yardLatitude = this.addYardLatitude;
            temp.yardLongitude = this.addYardLongitude;
            temp.yardName = this.addYardName;
            this.yardOptionsModal.push(temp);
          }
          let flag = true;
          if (this.Form_build.yardId) {
            for (let i = 0; i < this.yardOptionsModal.length; i++) {
              if (this.yardOptionsModal[i].yardId === this.Form_build.yardId) {
                flag = false;
                break;
              }
            }
            if (flag) {
              this.Form_build.yardId = '';
            }
          }
        })
        .catch(err => {
          const msg = err.result ? err.result : '院落名称下拉菜单获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 是否租赁
    rentStatusChange(val) {
      if (val === 0) {
        this.Form_build.outRent.isRent = 0;
      }
    },
    // 设置经纬度
    setPostion(lng, lat) {
      if (this.addInfoType === 'build') {
        this.Form_build.buildLongitude = lng;
        this.Form_build.buildLatitude = lat;
      } else {
        this.Form_yard.yard.yardLongitude = lng;
        this.Form_yard.yard.yardLatitude = lat;
      }
    },
    // 地图绑定坐标位置（楼座）
    buildMapBindCoord(key, type) {
      this.buildLimitFloat(key);
      if (this.Form_build.buildLongitude !== '' && this.Form_build.buildLatitude !== null && this.Form_build.buildLongitude !== '' && this.Form_build.buildLatitude !== null) {
        eventBus.$emit("mapBindCoord", this.Form_build.buildLongitude, this.Form_build.buildLatitude);
      }
    },
    // 地图绑定坐标位置（院落）
    yardMapBindCoord(key, type, flag) {
      this.yardLimitFloat(key, type, flag);
      const yardLongitude = this.Form_yard.yard.yardLongitude;
      const yardLatitude = this.Form_yard.yard.yardLatitude;
      if (yardLongitude !== '' && yardLongitude !== null && yardLatitude !== '' && yardLatitude !== null) {
        eventBus.$emit("mapBindCoord", yardLongitude, yardLatitude);
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
        for (let i = this.Form_build.floorDrawingList.length - 1; i >= 0; i--) {
          if (this.Form_build.floorDrawingList[i].floordrawingVirtualUrl === file.response.imgList[0].imgUrl) {
            this.Form_build.floorDrawingList.splice(i, 1);
            break;
          }
        }
        for (let i = this.Form_build.fileList.length - 1; i >= 0; i--) {
          if (this.Form_build.fileList[i].uid === file.uid) {
            this.Form_build.fileList.splice(i, 1);
            break;
          }
        }
      }
      this.uploadNum = fileList.length - this.uploadedNum;
      this.fileList = fileList;
      this.$nextTick(() => {
        const contentBox = $('.addInfo-dialog').find('.dialog-content');
        const stepBox = $('.addInfo-dialog').find('.step-item')[2];
        const h = $(stepBox).outerHeight();
        contentBox.css('height', h);
      })
    },
    handleImgRemove(file, fileList) {
      if (file.status === 'success') {
        this.uploadedImgNum--;
        this.Form_build.fileListImg = [];
        this.Form_build.newFloorImgUrlList = [];
      }
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
      this.fileListImg = fileList;
    },
    improvingHandleRemove(file, fileList) {
      if (file.status === 'success') {
        this.uploadedImprovingNum--;
        for (let i = this[this.activeAuditStateFormName].improvingDataList.length - 1; i >= 0; i--) {
          const file = this[this.activeAuditStateFormName].improvingDataList[i];
          if (file.floordrawingVirtualUrl === file.response.imgList[0].imgUrl) {
            this[this.activeAuditStateFormName].improvingDataList.splice(i, 1);
            break;
          }
        }
      }
      this.uploadImprovingNum = fileList.length - this.uploadedImprovingNum;
      this.fileListImproving = fileList;
      this.$nextTick(() => {
        const contentBox = $('.addInfo-dialog').find('.dialog-content');
        const stepBox = $('.addInfo-dialog').find('.step-item')[0];
        const h = $(stepBox).outerHeight();
        contentBox.css('height', h);
      })
    },
    // 上传文件成功
    handleSuccess(response, file, fileList) {
      this.uploadSuccessNum++;
      this.Form_build.floorDrawingList.push({
        floordrawingUrl: response.imgList[0].imgPath,
        floordrawingVirtualUrl: response.imgList[0].imgUrl,
        floordrawingFileName: file.name,
        buildId: this.Form_build.buildId
      });
      this.Form_build.fileList.push(file);
      if (this.uploadSuccessNum === this.uploadNum) {
        this.submitFlag = true;
      }
      if (((this.submitFlag && this.uploadChangeFlag) && (this.submitImgFlag && this.uploadImgChangeFlag)) ||
        ((this.submitFlag && this.uploadChangeFlag) && !this.uploadImgChangeFlag)) {
        this.saveBuildForm();
      }
    },
    handleImgSuccess(response, file, fileList) {
      this.uploadSuccessImgNum++;
      this.Form_build.newFloorImgUrlList.push({
        floorimgUrl: response.imgList[0].imgPath,
        floorVirtualUrl: response.imgList[0].imgUrl
      });
      this.Form_build.fileListImg.push(file);
      if (this.uploadSuccessImgNum === this.uploadImgNum) {
        this.submitImgFlag = true;
      }
      if (((this.submitFlag && this.uploadChangeFlag) && (this.submitImgFlag && this.uploadImgChangeFlag)) ||
        (!this.uploadChangeFlag && (this.submitImgFlag && this.uploadImgChangeFlag))) {
        this.saveBuildForm();
      }
    },
    improvingHandleSuccess(response, file, fileList) {
      this.uploadSuccessImprovingNum++;
      this[this.activeAuditStateFormName].improvingDataList.push({
        improvingDrawingUrl: response.imgList[0].imgPath,
        improvingDrawingVirtualUrl: response.imgList[0].imgUrl,
        improvingDrawingFileName: file.name
      });
      if (this.uploadSuccessImprovingNum === this.uploadImprovingNum) {
        this.submitImprovingFlag = true;
      }
      if (this.submitImprovingFlag) {
        this.baseInfoToYard();
      }
    },
    // 上传文件改变时钩子
    handleChange(file, fileList) {
      this.uploadChangeFlag = true;
      this.uploadNum = fileList.length - this.uploadedNum;
      this.$nextTick(() => {
        const contentBox = $('.addInfo-dialog').find('.dialog-content');
        const stepBox = $('.addInfo-dialog').find('.step-item')[2];
        const h = $(stepBox).outerHeight();
        contentBox.css('height', h);
      })
    },
    handleImgChange(file, fileList) {
      this.uploadImgChangeFlag = true;
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
      this.fileListImg = fileList;
    },
    improvingHandleChange(file, fileList) {
      this.uploadImprovingNum = fileList.length - this.uploadedImprovingNum;
      this.$nextTick(() => {
        const contentBox = $('.addInfo-dialog').find('.dialog-content');
        const stepBox = $('.addInfo-dialog').find('.step-item')[0];
        const h = $(stepBox).outerHeight();
        contentBox.css('height', h);
      })
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
    // 验证判断
    estateNameSelectChange() {
      const estateNum = this.Form_yard.yardAndestateVoList.length;
      if (estateNum > 0) {
        let num = 0;
        this.estateNameFlag = false;
        for (let i = 0; i < estateNum; i++) {
          const item = this.Form_yard.yardAndestateVoList[i];
          if (!item.estateId) {
            break;
          } else {
            num++
          }
        };
        if (num === estateNum) {
          this.estateNameFlag = true;
        }
      } else {
        this.estateNameFlag = true;
      }
    },
    serviceContentSelectChange() {
      const estateNum = this.Form_yard.yardAndestateVoList.length;
      if (estateNum > 0) {
        let num = 0;
        this.serviceContentFlag = false;
        for (let i = 0; i < estateNum; i++) {
          const item = this.Form_yard.yardAndestateVoList[i];
          if (item.serviceContentList.length === 0) {
            break;
          } else {
            num++
          }
        };
        if (num === estateNum) {
          this.serviceContentFlag = true;
        }
      } else {
        this.serviceContentFlag = true;
      }
    },
    startTimeBlur() {
      const estateNum = this.Form_yard.yardAndestateVoList.length;
      if (estateNum > 0) {
        let num = 0;
        let timeNum = 0;
        this.startTimeFlag = false;
        this.startTimeLimitFlag = false;
        for (let i = 0; i < estateNum; i++) {
          const item = this.Form_yard.yardAndestateVoList[i];
          if (!item.startTime) {
            break;
          } else {
            num++
          }
        };
        for (let i = 0; i < estateNum; i++) {
          const item = this.Form_yard.yardAndestateVoList[i];
          if (item.startTime && item.endTime && (item.startTime >= item.endTime)) {
            break;
          } else {
            timeNum++
          }
        };
        if (num === estateNum) {
          this.startTimeFlag = true;
        }
        if (timeNum === estateNum) {
          this.startTimeLimitFlag = true;
        }
      } else {
        this.startTimeFlag = true;
        this.startTimeLimitFlag = true;
      }
    },
    endTimeBlur() {
      const estateNum = this.Form_yard.yardAndestateVoList.length;
      if (estateNum) {
        let num = 0;
        let timeNum = 0;
        this.endTimeFlag = false;
        this.endTimeLimitFlag = false;
        for (let i = 0; i < estateNum; i++) {
          const item = this.Form_yard.yardAndestateVoList[i];
          if (!item.endTime) {
            break;
          } else {
            num++
          }
        };
        for (let i = 0; i < estateNum; i++) {
          const item = this.Form_yard.yardAndestateVoList[i];
          if (item.startTime && item.endTime && (item.startTime >= item.endTime)) {
            break;
          } else {
            timeNum++
          }
        };
        if (num === estateNum) {
          this.endTimeFlag = true;
        }
        if (timeNum === estateNum) {
          this.endTimeLimitFlag = true;
        }
      } else {
        this.endTimeFlag = true;
        this.endTimeLimitFlag = true;
      }
    },
    contractAmountBlur() {
      const estateNum = this.Form_yard.yardAndestateVoList.length;
      if (estateNum > 0) {
        let num = 0;
        this.contractAmountFlag = false;
        for (let i = 0; i < estateNum; i++) {
          const item = this.Form_yard.yardAndestateVoList[i];
          if (!item.contractAmount) {
            break;
          } else {
            num++
          }
        };
        if (num === estateNum) {
          this.contractAmountFlag = true;
        }
      } else {
        this.contractAmountFlag = true;
      }
    },
    // 提交表单
    submitForm(formName) {
      if (this.currentDialogName === 'addInfo') {
        if (this.localSaveBuildInfo.length === 0) {
          this.$message({
            message: '请至少添加一个楼座',
            type: "warning"
          });
          return false;
        }
        this.localSaveBuildInfo.forEach(build => {
          if (build.yardId === this.addYardName) {
            build.yardId = '';
          }
          delete build.fileList;
          delete build.fileListImg;
          delete build.uuid;
        });
        this.currentForm.buildVoList = this.localSaveBuildInfo;
        if (this.Form_yard.yard.yardName) {
          this.currentForm.yardAddVo = this.Form_yard;
        }
        switch (this.activeAuditState) {
          case 2:
            this.currentForm.improvingDataRRC = this.Form_replacement;
            break;
          case 3:
            this.currentForm.improvingDataRRC = this.Form_rent;
            break;
          case 4:
            this.currentForm.improvingDataRRC = this.Form_construction;
            break;
        }
        saveSubmitConfigure(this.currentForm).then(res => {
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
      } else {
        this.$refs[formName].validate(valid => {
          if (valid) {
            let obj = JSON.parse(JSON.stringify(this.currentForm));
            if (this.currentDialogName === 'unit') {
              if (obj.auditState === 1) {
                this.submitVerify();
                if (!this.roomNumberSubmitFlag || !this.roomAreaNotNullFlag || !this.roomAreaRangeRepeatFlag || !this.roomAreaRangeErrorFlag) {
                  return false;
                }
              }
              if (obj.auditState === 2) {
                delete obj['configureRooms'];
                delete obj['elevatorsNumber'];
                delete obj['regionIds'];
                delete obj['useunitId'];
              } else {
                if (obj.useunitId === '暂无单位') {
                  obj.useunitId = '';
                }
                delete obj['comment'];
              }
            } else if (this.currentDialogName === 'project') {
              if (this.currentForm.allocationPlan === 1) {
                if (this.currentForm.selectionPlan === 1) {
                  this.selectDataArray.forEach(ele => {
                    ele.allTableData.forEach(item => {
                      let temp = {};
                      temp.buildId = item.buildId;
                      temp.houseId = item.houseId;
                      temp.yardId = item.yardId;
                      if (item.unitList && item.unitList.length > 0) {
                        temp.configurePlanOldUnits = [];
                        item.unitList.forEach(unit => {
                          temp.configurePlanOldUnits.push({ unitId: unit.unitId });
                        })
                      }
                      obj.configurePlanDataList.push(temp);
                    })
                  })
                } else {
                  obj.planId = this.selectedSchemeId;
                }
              } else {
                delete obj['selectionPlan'];
              }
            }
            saveSubmitConfigure(obj).then(res => {
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
          } else {
            return false;
          }
        });
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
        const formName = `Form_${this.currentDialogName}`
        if (this.$refs[formName]) {
          this.$refs[formName].clearValidate();
        }
      })
    },
    // dialog关闭的回调
    dialogClosed() {
      switch (this.currentDialogName) {
        case 'unit':
          this.currentForm.configureRooms = [];
          this.clearForm(this.currentForm);
          this.clearForm(this.baseInfoForm);
          this.currentForm.auditState = 1;
          const obj = {
            roomAreaMax: null,
            roomAreaMin: null,
            roomNumber: null
          };
          this.currentForm.configureRooms.push(obj);
          this.currentForm.elevatorsNumber = 0;
          this.currentForm.useunitId = '暂无单位';
          break;
        case 'project':
          this.clearForm(this.currentForm);
          this.clearForm(this.baseInfoForm);
          this.autoConditionList = [];
          this.currentAutoCondition = null;
          this.sureCurrentAutoCondition = null;
          this.distributionActiveName = 'table1';
          this.sureDistributionActiveName = '';
          this.currentForm.allocationPlan = 1;
          this.currentForm.auditState = 0;
          this.currentForm.selectionPlan = 1;
          this.selectData = {};
          this.selectDataArray = [];
          this.isSureDistributePage = false;
          this.allSchemeTableData = [];
          this.schemeTableData = [];
          this.schemeCurrentPage = 1;
          this.schemePageSize = 5;
          this.schemeTotal = 0;
          this.planTableData = [];
          this.planCurrentPage = 1;
          this.planPageSize = 5;
          this.planTotal = 0;
          this.oldPlanName = '';
          this.selectedSchemeId = '';
          this.distributeChangeList = [];
          this.distributeNum = 0;
          break;
        case 'leader':
          this.distributeCurrentPage = 1;
          this.distributeTotal = 0;
          this.clearForm(this.currentForm);
          this.clearForm(this.baseInfoForm);
          this.currentForm.auditState = 1;
          this.rules_leader.comment = [];
          this.distributeChangeList = [];
          break;
        case 'addInfo':
          this.clearForm(this.currentForm);
          this.currentForm.auditState = 0;
          this.clearForm(this.baseInfoForm);
          this.Form_build.outRent.isRent = 0;
          this.Form_build.elevatorNum = 0;
          this.clearForm(this.Form_build);
          this.Form_yard.yardAndestateVoList = [];
          this.clearForm(this.Form_yard);
          this.Form_yard.yard.type = 1;
          this.clearForm(this.Form_replacement);
          this.clearForm(this.Form_rent);
          this.clearForm(this.Form_construction);
          this.$refs['Form_build'].clearValidate();
          this.$refs['Form_yard'].clearValidate();
          switch (this.activeAuditState) {
            case 2:
              this.$refs['Form_replacement'].clearValidate();
              break;
            case 3:
              this.$refs['Form_rent'].clearValidate();
              break;
            case 4:
              this.$refs['Form_construction'].clearValidate();
              break;
          }
          this.uploadImgNum = 0;
          this.uploadSuccessImgNum = 0;
          this.fileList = [];
          this.fileListImg = [];
          this.uploadChangeFlag = false;
          this.uploadImgChangeFlag = false;
          this.submitImgFlag = false;
          this.submitFlag = false;
          this.showMap = false;
          this.addInfoType = 'yard';
          this.addYardName = '';
          this.addYardLongitude = '';
          this.addYardLatitude = '';
          this.activeAuditStateFormName = null;
          this.localSaveBuildInfo = [];
          this.localSaveBuildList = [];
          this.activeInfo = null;
          this.submitAllFlag = false;
          this.addInfoProcess = 0;
          this.replaceMode = 1;
          this.currentAddInfoType = '';
          this.uploadImprovingNum = 0;
          this.uploadedImprovingNum = 0;
          this.uploadSuccessImprovingNum = 0;
          this.submitImprovingFlag = false;
          this.fileListImproving = [];
          const body = $('.addInfo-dialog').find('.el-dialog__body');
          body.scrollTop(0)
          const box = $('.addInfo-dialog').find('.dialog-content');
          box.animate({ left: `0px` });
          break;
      }
      this.downloadCheckedList = [];
      this.fileDownLoadCheckedList = [];
      this.currentDialogName = '';
      this.currentForm = null;
      this.operateType = '';
      this.tipsText = '';
      this.roomNumberSubmitFlag = false;
      this.roomAreaNotNullFlag = false;
      this.roomAreaRangeRepeatFlag = false;
      this.roomAreaRangeErrorFlag = false;
      this.currentPlanId = null;
      this.distributeTableData = [];
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
    // 只能输入正浮点数(楼座)
    buildLimitFloat(key, flag, type) {
      if (type === undefined) {
        this.Form_build[key] = this.Form_build[key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '')
      } else {
        this.Form_build[type][key] = this.Form_build[type][key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '')
      }
      if (flag && type === undefined && this.Form_build[key] !== '') {
        this.Form_build[key] = (this.Form_build[key] - 0).toFixed(2);
      }
      if (flag && type && this.Form_build[type][key] !== '') {
        this.Form_build[type][key] = (this.Form_build[type][key] - 0).toFixed(2);
      }
    },
    // 只能输入正整数(楼座)
    buildLimitInt(key) {
      this.Form_build[key] = this.Form_build[key].replace(/[^0-9]/g, "");
    },
    // 只能输入正浮点数(院落)
    yardLimitFloat(key, type, flag) {
      if (this.Form_yard[type][key]) {
        this.Form_yard[type][key] = this.Form_yard[type][key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^\./g, '');
      }
      if (flag && this.Form_yard[type][key]) {
        this.Form_yard[type][key] = (this.Form_yard[type][key] - 0).toFixed(6);
      }
    },
    // 只能输入正浮点数(基本信息)
    baseLimitFloat(key, flag) {
      this[this.activeAuditStateFormName][key] = this[this.activeAuditStateFormName][key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '')
      if (flag && this[this.activeAuditStateFormName][key] !== '') {
        this[this.activeAuditStateFormName][key] = (this[this.activeAuditStateFormName][key] - 0).toFixed(2);
      }
    },
    // 限制使用金额小数点后两位
    contractAmountLimitFloat(index, flag) {
      this.Form_yard.yardAndestateVoList[index].contractAmount = this.Form_yard.yardAndestateVoList[index].contractAmount.replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '');
      if (flag && this.Form_yard.yardAndestateVoList[index].contractAmount !== "") {
        this.Form_yard.yardAndestateVoList[index].contractAmount = (this.Form_yard.yardAndestateVoList[index].contractAmount - 0).toFixed(2);
      }
    },
    // 房间数量只能输入整数
    roomNumberLimitInt(index, flag) {
      if (this.Form_unit.configureRooms[index].roomNumber) {
        this.Form_unit.configureRooms[index].roomNumber = this.Form_unit.configureRooms[index].roomNumber.replace(/[^0-9]/g, "");
      }
      if (flag) {
        let roomNumber = this.Form_unit.configureRooms[index].roomNumber;
        const tips = $('<p class="errorTips roomNumberTips">房间数量不能为空</p>');
        const tipsNum = $('.house-table-box').find('.roomNumberTips').length;
        if (!roomNumber) {
          if (tipsNum === 0) {
            $('.house-table-box').append(tips);
            this.roomNumberSubmitFlag = false;
          }
        } else {
          $('.roomNumberTips').remove();
          this.roomNumberSubmitFlag = true;
        }
      }
    },
    // 房间面积只能输入浮点数
    roomAreaMinLimitFloat(index, flag, submitFlag) {
      if (this.Form_unit.configureRooms[index].roomAreaMin) {
        this.Form_unit.configureRooms[index].roomAreaMin = this.Form_unit.configureRooms[index].roomAreaMin.replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '');
        if (flag && this.Form_unit.configureRooms[index].roomAreaMin !== "") {
          this.Form_unit.configureRooms[index].roomAreaMin = (this.Form_unit.configureRooms[index].roomAreaMin - 0).toFixed(2);
        }
      }

      if (submitFlag) {
        const roomAreaMin = (this.Form_unit.configureRooms[index].roomAreaMin !== '' && this.Form_unit.configureRooms[index].roomAreaMin !== null) ? this.Form_unit.configureRooms[index].roomAreaMin - 0 : '';
        const roomAreaMax = (this.Form_unit.configureRooms[index].roomAreaMax !== '' && this.Form_unit.configureRooms[index].roomAreaMax !== null) ? this.Form_unit.configureRooms[index].roomAreaMax - 0 : '';
        const tip_1 = $('<p class="errorTips roomAreaMinNotNull">房间面积范围不能为空</p>');
        const tip_2 = $('<p class="errorTips roomAreaRangeRepet">房间面积范围不能重复</p>');
        const tip_3 = $('<p class="errorTips roomAreaRangeError">房间面积范围的最大值不能小于等于最小值</p>');
        const tipsNum_AreaNotNull = $('.house-table-box').find('.roomAreaMinNotNull').length;
        const tipsNum_AreaRangeRepeat = $('.house-table-box').find('.roomAreaRangeRepet').length;
        const tipsNum_AreaRangeError = $('.house-table-box').find('.roomAreaRangeError').length;

        // 房间面积范围不能为空
        if (roomAreaMin === '') {
          if (tipsNum_AreaNotNull === 0) {
            $('.house-table-box').append(tip_1);
            this.roomAreaNotNullFlag = false;
          }
        } else {
          $('.roomAreaMinNotNull').remove();
          this.roomAreaNotNullFlag = true;
        }

        // 房间面积范围不能重复
        let flag = false
        for (let i = 0; i < this.Form_unit.configureRooms.length; i++) {
          if (i !== index) {
            const otherRoomAreaMin = (this.Form_unit.configureRooms[i].roomAreaMin !== '' && this.Form_unit.configureRooms[i].roomAreaMin !== null) ? this.Form_unit.configureRooms[i].roomAreaMin - 0 : '';
            const otherRoomAreaMax = (this.Form_unit.configureRooms[i].roomAreaMax !== '' && this.Form_unit.configureRooms[i].roomAreaMax !== null) ? this.Form_unit.configureRooms[i].roomAreaMax - 0 : '';
            if ((otherRoomAreaMin !== '' && otherRoomAreaMax !== '' && roomAreaMin !== '' && roomAreaMax !== '') && (otherRoomAreaMin <= roomAreaMin && roomAreaMin <= otherRoomAreaMax) || (otherRoomAreaMin <= roomAreaMax && roomAreaMax <= otherRoomAreaMax)) {
              flag = true;
              break;
            }
          }
        }
        if (flag) {
          if (tipsNum_AreaRangeRepeat === 0) {
            $('.house-table-box').append(tip_2);
            this.roomAreaRangeRepeatFlag = false;
          }
        } else {
          $('.roomAreaRangeRepet').remove();
          this.roomAreaRangeRepeatFlag = true;
        }

        // 房间面积范围的最小值不能大于等于最大值
        if (roomAreaMin !== '' && roomAreaMax !== '' && roomAreaMin >= roomAreaMax) {
          if (tipsNum_AreaRangeError === 0) {
            $('.house-table-box').append(tip_3);
            this.roomAreaRangeErrorFlag = false;
          }
        } else {
          $('.roomAreaRangeError').remove();
          this.roomAreaRangeErrorFlag = true;
        }
      }
    },
    roomAreaMaxLimitFloat(index, flag, submitFlag) {
      if (this.Form_unit.configureRooms[index].roomAreaMax) {
        this.Form_unit.configureRooms[index].roomAreaMax = this.Form_unit.configureRooms[index].roomAreaMax.replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '');
        if (flag && this.Form_unit.configureRooms[index].roomAreaMax !== "") {
          this.Form_unit.configureRooms[index].roomAreaMax = (this.Form_unit.configureRooms[index].roomAreaMax - 0).toFixed(2);
        }
      }
      if (submitFlag) {
        const roomAreaMin = (this.Form_unit.configureRooms[index].roomAreaMin !== '' && this.Form_unit.configureRooms[index].roomAreaMin !== null) ? this.Form_unit.configureRooms[index].roomAreaMin - 0 : '';
        const roomAreaMax = (this.Form_unit.configureRooms[index].roomAreaMax !== '' && this.Form_unit.configureRooms[index].roomAreaMax !== null) ? this.Form_unit.configureRooms[index].roomAreaMax - 0 : '';
        const tip_1 = $('<p class="errorTips roomAreaMinNotNull">房间面积范围不能为空</p>');
        const tip_2 = $('<p class="errorTips roomAreaRangeRepet">房间面积范围不能重复</p>');
        const tip_3 = $('<p class="errorTips roomAreaRangeError">房间面积范围的最大值不能小于等于最小值</p>');
        const tipsNum_AreaNotNull = $('.house-table-box').find('.roomAreaMinNotNull').length;
        const tipsNum_AreaRangeRepeat = $('.house-table-box').find('.roomAreaRangeRepet').length;
        const tipsNum_AreaRangeError = $('.house-table-box').find('.roomAreaRangeError').length;

        // 房间面积范围不能为空
        if (roomAreaMax === '') {
          if (tipsNum_AreaNotNull === 0) {
            $('.house-table-box').append(tip_1);
            this.roomAreaNotNullFlag = false;
          }
        } else {
          $('.roomAreaMinNotNull').remove();
          this.roomAreaNotNullFlag = true;
        }

        // 房间面积范围不能重复
        let flag = false
        for (let i = 0; i < this.Form_unit.configureRooms.length; i++) {
          if (i !== index) {
            const otherRoomAreaMin = (this.Form_unit.configureRooms[i].roomAreaMin !== '' && this.Form_unit.configureRooms[i].roomAreaMin !== null) ? this.Form_unit.configureRooms[i].roomAreaMin - 0 : '';
            const otherRoomAreaMax = (this.Form_unit.configureRooms[i].roomAreaMax !== '' && this.Form_unit.configureRooms[i].roomAreaMax !== null) ? this.Form_unit.configureRooms[i].roomAreaMax - 0 : '';
            if ((otherRoomAreaMin !== '' && otherRoomAreaMax !== '' && roomAreaMin !== '' && roomAreaMax !== '') && (otherRoomAreaMin <= roomAreaMin && roomAreaMin <= otherRoomAreaMax) || (otherRoomAreaMin <= roomAreaMax && roomAreaMax <= otherRoomAreaMax)) {
              flag = true;
              break;
            }
          }
        }
        if (flag) {
          if (tipsNum_AreaRangeRepeat === 0) {
            $('.house-table-box').append(tip_2);
            this.roomAreaRangeRepeatFlag = false;
          }
        } else {
          $('.roomAreaRangeRepet').remove();
          this.roomAreaRangeRepeatFlag = true;
        }

        // 房间面积范围的最小值不能大于等于最大值
        if (roomAreaMin !== '' && roomAreaMax !== '' && roomAreaMin >= roomAreaMax) {
          if (tipsNum_AreaRangeError === 0) {
            $('.house-table-box').append(tip_3);
            this.roomAreaRangeErrorFlag = false;
          }
        } else {
          $('.roomAreaRangeError').remove();
          this.roomAreaRangeErrorFlag = true;
        }
      }
    },
    submitVerify() {
      this.roomNumberSubmitFlag = false;
      this.roomAreaNotNullFlag = false;
      this.roomAreaRangeRepeatFlag = false;
      this.roomAreaRangeErrorFlag = false;
      // 房间数量不能为空
      const tips = $('<p class="errorTips roomNumberTips">房间数量不能为空</p>');
      const tipsNum = $('.house-table-box').find('.roomNumberTips').length;
      let roomNumberNotNullFlag = true;
      for (let i = 0; i < this.Form_unit.configureRooms.length; i++) {
        const roomNumber = (this.Form_unit.configureRooms[i].roomNumber !== '' && this.Form_unit.configureRooms[i].roomNumber !== null) ? this.Form_unit.configureRooms[i].roomNumber : '';
        if (!roomNumber) {
          if (tipsNum === 0) {
            $('.house-table-box').append(tips);
          }
          this.roomNumberSubmitFlag = false;
          roomNumberNotNullFlag = false;
          break;
        }
      }
      if (roomNumberNotNullFlag) {
        $('.roomNumberTips').remove();
        this.roomNumberSubmitFlag = true;
      }

      // 房间面积范围不能为空
      const tip_1 = $('<p class="errorTips roomAreaMinNotNull">房间面积范围不能为空</p>');
      const tipsNum_AreaNotNull = $('.house-table-box').find('.roomAreaMinNotNull').length;
      let areaNotNullFlag = true;
      for (let i = 0; i < this.Form_unit.configureRooms.length; i++) {
        const roomAreaMin = (this.Form_unit.configureRooms[i].roomAreaMin !== '' && this.Form_unit.configureRooms[i].roomAreaMin !== null) ? this.Form_unit.configureRooms[i].roomAreaMin - 0 : '';
        const roomAreaMax = (this.Form_unit.configureRooms[i].roomAreaMax !== '' && this.Form_unit.configureRooms[i].roomAreaMax !== null) ? this.Form_unit.configureRooms[i].roomAreaMax - 0 : '';
        if (roomAreaMin === '' || roomAreaMin === null || roomAreaMax === '' || roomAreaMax === null) {
          if (tipsNum_AreaNotNull === 0) {
            $('.house-table-box').append(tip_1);
          }
          this.roomAreaNotNullFlag = false;
          areaNotNullFlag = false;
          break;
        }
      }
      if (areaNotNullFlag) {
        $('.roomAreaMinNotNull').remove();
        this.roomAreaNotNullFlag = true;
      }

      // 房间面积范围不能重复
      const tip_2 = $('<p class="errorTips roomAreaRangeRepet">房间面积范围不能重复</p>');
      const tipsNum_AreaRangeRepeat = $('.house-table-box').find('.roomAreaRangeRepet').length;
      let areaRangeRepeatFlag = true;
      for (let i = 0; i < this.Form_unit.configureRooms.length; i++) {
        const roomAreaMin = (this.Form_unit.configureRooms[i].roomAreaMin !== '' && this.Form_unit.configureRooms[i].roomAreaMin !== null) ? this.Form_unit.configureRooms[i].roomAreaMin - 0 : '';
        const roomAreaMax = (this.Form_unit.configureRooms[i].roomAreaMax !== '' && this.Form_unit.configureRooms[i].roomAreaMax !== null) ? this.Form_unit.configureRooms[i].roomAreaMax - 0 : '';
        for (let j = 0; j < this.Form_unit.configureRooms.length; j++) {
          if (i !== j) {
            const otherRoomAreaMin = (this.Form_unit.configureRooms[j].roomAreaMin !== '' && this.Form_unit.configureRooms[j].roomAreaMin !== null) ? this.Form_unit.configureRooms[j].roomAreaMin - 0 : '';
            const otherRoomAreaMax = (this.Form_unit.configureRooms[j].roomAreaMax !== '' && this.Form_unit.configureRooms[j].roomAreaMax !== null) ? this.Form_unit.configureRooms[j].roomAreaMax - 0 : '';
            if ((otherRoomAreaMin !== '' && otherRoomAreaMax !== '') && (otherRoomAreaMin <= roomAreaMin && roomAreaMin <= otherRoomAreaMax) || (otherRoomAreaMin <= roomAreaMax && roomAreaMax <= otherRoomAreaMax)) {
              if (tipsNum_AreaRangeRepeat === 0) {
                $('.house-table-box').append(tip_2);
              }
              this.roomAreaRangeRepeatFlag = false;
              areaRangeRepeatFlag = false;
              break;
            }
          }
        }
      }
      if (areaRangeRepeatFlag) {
        $('.roomAreaRangeError').remove();
        this.roomAreaRangeRepeatFlag = true;
      }

      // 房间面积范围的最大值不能小于等于最小值
      const tip_3 = $('<p class="errorTips roomAreaRangeError">房间面积范围的最大值不能小于等于最小值</p>');
      const tipsNum_AreaRangeError = $('.house-table-box').find('.roomAreaRangeError').length;
      let areaRangeErrorFlag = true;
      for (let i = 0; i < this.Form_unit.configureRooms.length; i++) {
        const roomAreaMin = (this.Form_unit.configureRooms[i].roomAreaMin !== '' && this.Form_unit.configureRooms[i].roomAreaMin !== null) ? this.Form_unit.configureRooms[i].roomAreaMin - 0 : '';
        const roomAreaMax = (this.Form_unit.configureRooms[i].roomAreaMax !== '' && this.Form_unit.configureRooms[i].roomAreaMax !== null) ? this.Form_unit.configureRooms[i].roomAreaMax - 0 : '';
        if (roomAreaMin !== '' && roomAreaMax !== '' && roomAreaMin >= roomAreaMax) {
          if (tipsNum_AreaRangeError === 0) {
            $('.house-table-box').append(tip_3);
          }
          this.roomAreaRangeErrorFlag = false;
          areaRangeErrorFlag = false;
          break;
        }
      }
      if (areaRangeErrorFlag) {
        $('.roomAreaRangeError').remove();
        this.roomAreaRangeErrorFlag = true;
      }
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
      downloadObj.configureId = this.currentForm.configureId;
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
    }
  },
  mounted() {
    this.userId = sessionStorage.getItem('userId');
    this.getUserUnitDic();
    this.getList();
    this.getReapportionUnit();
    this.getRegionList();
    this.username = sessionStorage.getItem('username');
    this.token = sessionStorage.getItem('TOKEN');
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
#configBacklog .query-box {
  margin-bottom: 20px;
}
#configBacklog .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 18%;
}
#configBacklog .query-item > div {
  float: left;
}
#configBacklog .query-item > .query-input {
  width: calc(100% - 80px);
}
#configBacklog .query-item .el-select {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#configBacklog .query-item .el-date-editor.el-input,
#configBacklog .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#configBacklog .query-btn {
  float: right;
}
#configBacklog .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#configBacklog .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#configBacklog .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#configBacklog .table-operate {
  color: #3891e0;
  cursor: pointer;
}
#configBacklog .table-operate > span:first-child {
  position: relative;
  top: 1px;
}
#configBacklog .table-operate:hover {
  color: #035cac;
}
#configBacklog .content-item {
  width: 50%;
  float: left;
  margin-bottom: 22px;
  overflow: hidden;
}
#configBacklog .content-item > span {
  float: left;
  line-height: 20px;
}
#configBacklog .content-item > span:first-child,
#configBacklog .content-item > span:first-child {
  width: 84px;
  text-align: right;
}
#configBacklog .project-dialog .newUnitName > span:first-child {
  width: 71px;
  letter-spacing: 1.1px;
}
#configBacklog .content-item > span:last-child,
#configBacklog .content-item > span:last-child {
  width: calc(100% - 84px);
}
#configBacklog .project-dialog .newUnitName > span:last-child {
  position: relative;
  left: 16px;
  top: -19px;
}
#configBacklog .unit-dialog .elevators-number {
  margin-right: 180px;
}
#configBacklog .house-info-table-item .el-form-item__content,
#configBacklog .common-item .el-form-item__content {
  width: calc(100% - 84px);
}
#configBacklog .house-info-add-btn {
  padding: 8px 20px;
  border-color: #c0c4cc;
  margin-bottom: 20px;
  width: 100%;
}
#configBacklog .scheme-add-btn {
  padding: 8px 20px;
  width: calc(100% - 84px);
  border-color: #c0c4cc;
  margin-bottom: 20px;
}
#configBacklog .roomNumber-item .el-input__inner {
  border: 0;
  width: 100%;
}
#configBacklog .roomArea-line {
  float: left;
  margin-left: 5px;
  margin-right: 5px;
}
#configBacklog .roomArea-item {
  float: left;
  width: 80px;
}
#configBacklog .roomArea-item .el-input__inner {
  width: 100%;
  height: 26px;
}
#configBacklog .house-table-box .tab_2 .el-table__body tr.hover-row > td,
#configBacklog .scheme-table .el-table__body tr.hover-row > td {
  background-color: #fff;
}
#configBacklog
  .house-table-box
  .tab_2
  > .el-table--enable-row-hover
  .el-table__body
  tr:hover
  > td,
#configBacklog
  .scheme-table
  > .el-table--enable-row-hover
  .el-table__body
  tr:hover
  > td {
  background-color: #fff;
}
#configBacklog .house-table-box .tab_2 th,
#configBacklog .scheme-table .el-table th {
  line-height: initial;
}
#configBacklog .region-item .el-form-item__content {
  width: calc(100% - 84px);
}
#configBacklog .region-item .el-input__inner {
  width: 100%;
}
#configBacklog .plan-btn-box > span {
  display: inline-block;
  width: 64px;
  height: 26px;
  line-height: 26px;
  border: 1px solid #3891e0;
  color: #3891e0;
  text-align: center;
  cursor: pointer;
}
#configBacklog .plan-btn-box > span:first-child {
  margin-right: 30px;
}
#configBacklog .plan-btn-box > span.active {
  color: #fff;
  background: #3891e0;
  text-align: center;
}
#configBacklog .distribution-table-pagination-box {
  position: relative;
  height: 30px;
  border-left: 1px solid #c8c8c8;
  border-right: 1px solid #c8c8c8;
  border-bottom: 1px solid #c8c8c8;
}
#configBacklog
  .distribution-table-pagination-box
  > .distribution-table-pagination {
  position: absolute;
  right: 10px;
  top: 3px;
}
#configBacklog .distribution-table-pagination-box > .distribution-table-tips {
  position: absolute;
  left: 10px;
  top: 4px;
}
#configBacklog .distribute-room-table .el-table__body tr.hover-row > td {
  background-color: #fff6e6;
}
#configBacklog
  .distribute-room-table
  > .el-table--enable-row-hover
  .el-table__body
  tr:hover
  > td {
  background-color: #fff6e6;
}
#configBacklog .distribute-room-table > .el-table,
#configBacklog .scheme-table .el-table {
  border-color: #c8c8c8;
  box-sizing: border-box;
}
#configBacklog .distribute-room-table > .el-table::before,
#configBacklog .scheme-table .el-table::before {
  background-color: #c8c8c8;
}
#configBacklog .distribute-room-table > .el-table--border::after,
#configBacklog .distribute-room-table > .el-table--group::after,
#configBacklog .scheme-table .el-table--border::after,
#configBacklog .scheme-table .el-table--group::after {
  background-color: #c8c8c8;
}
#configBacklog .distribute-room-table .el-table__fixed-right::before,
#configBacklog .distribute-room-table .el-table__fixed::before,
#configBacklog .scheme-table .el-table__fixed-right::before,
#configBacklog .scheme-table .el-table__fixed::before {
  background-color: #c8c8c8;
}
#configBacklog .update {
  color: #3891e0;
  cursor: pointer;
  display: inline-block;
}
#configBacklog .update:hover {
  color: #035cac;
}
#configBacklog .delete {
  color: #f65c5f;
  cursor: pointer;
  display: inline-block;
}
#configBacklog .delete:hover {
  color: #d82a2d;
}
#configBacklog .scheme-btn-box > button,
#configBacklog .scheme-btn-box > span {
  float: left;
}
#configBacklog .scheme-btn-box > span {
  width: 84px;
  text-align: right;
  height: 40px;
  line-height: 40px;
}
#configBacklog .scheme-btn-box > button {
  position: relative;
  top: 4px;
}
#configBacklog .scheme-operate-box > div + div {
  margin-left: 15px;
}
#configBacklog td.plan-name-column {
  padding: 0 !important;
}
#configBacklog td.plan-name-column > .cell {
  padding: 0 !important;
}
#configBacklog td.plan-name-column .el-input__inner {
  border: 0;
}
#configBacklog .disable-btn {
  cursor: not-allowed;
}
#configBacklog .errorTips {
  color: #ff0000;
  line-height: 25px;
}
#configBacklog .file-download {
  float: left;
}
#configBacklog .file-download-item {
  line-height: 20px;
  margin-bottom: 10px;
}
#configBacklog .room-distribute-table {
  padding-left: 14px;
  padding-bottom: 20px;
}
#configBacklog .addInfo-dialog .upload_box {
  height: 100px;
}
#configBacklog .addInfo-dialog .upload_box .el-upload--picture-card {
  width: 100px;
  height: 100px;
  line-height: 100px;
}
#configBacklog
  .addInfo-dialog
  .upload_box
  .el-upload-list--picture-card
  .el-upload-list__item {
  width: 100px;
  height: 100px;
}
#configBacklog .addInfo-dialog .el-upload--picture-card {
  margin-bottom: 8px;
}
#configBacklog .addInfo-dialog .el-upload-list > li {
  margin-bottom: 0;
}
#configBacklog .addInfo-dialog .my-map {
  width: 100%;
  height: 300px;
  position: relative;
}
#configBacklog .addInfo-dialog .map-tips {
  color: #ff0000;
  width: 100%;
  margin-bottom: 5px;
}
#configBacklog .addInfo-dialog .rent-info-Form {
  margin-top: 15px;
  margin-bottom: 15px;
}
#configBacklog .addInfo-dialog .form-item-radio .el-form-item__content {
  width: 200px;
}
#configBacklog .addInfo-dialog .info-add-btn {
  position: absolute;
  right: 30px;
  top: 40px;
  color: #3891e0;
  cursor: pointer;
  z-index: 10000;
}
#configBacklog .addInfo-dialog .info-add-btn:hover {
  color: #3891e0;
}

#configBacklog .addInfo-dialog .estate-add-btn-box {
  display: block;
}
#configBacklog .addInfo-dialog .estate-table-box {
  margin-bottom: 25px;
}
#configBacklog .addInfo-dialog .estate-table-box .el-input__inner {
  border: 0;
}
#configBacklog .addInfo-dialog .estate-table-box .el-table .cell {
  padding: 0;
}
#configBacklog
  .addInfo-dialog
  .estate-table-box
  .tab_2
  .el-table__body
  tr.hover-row
  > td {
  background-color: #fff;
}
#configBacklog
  .addInfo-dialog
  .estate-table-box
  .tab_2
  > .el-table--enable-row-hover
  .el-table__body
  tr:hover
  > td {
  background-color: #fff;
}
#configBacklog .addInfo-dialog .estate-add-btn {
  padding: 8px 20px;
  width: 868px;
  border-color: #c0c4cc;
}
#configBacklog .addInfo-dialog .tips-box {
  color: red;
}
#configBacklog .unit-dialog .el-form-item__label,
#configBacklog .project-dialog .el-form-item__label,
#configBacklog .leader-dialog .el-form-item__label {
  padding-right: 0px;
}
#configBacklog .allocation-statistics {
  margin-top: 20px;
}
#configBacklog .addInfo-dialog .el-dialog__body {
  height: 500px;
  padding: 0;
  overflow-y: auto;
  overflow-x: hidden;
  position: relative;
}
#configBacklog .addInfo-dialog .dialog-content {
  width: 100000px;
  position: absolute;
  left: 0;
  top: 0;
  overflow: hidden;
}
#configBacklog .step-item {
  float: left;
  width: 1100px;
  box-sizing: border-box;
  padding-left: 20px;
  padding-right: 30px;
  padding-top: 30px;
  padding-bottom: 20px;
}
#configBacklog .step-item-title {
  line-height: 30px;
  border-bottom: 1px solid #d8d8d8;
}
/* #configBacklog .step-item:first-child .step-item-content {
  padding-left: 300px;
  box-sizing: border-box;
}
#configBacklog .step-item:first-child .step-item-content .el-input__inner {
  width: 300px;
} */
#configBacklog .step-item-content {
  padding-top: 20px;
}
#configBacklog .step-item-title > span {
  display: inline-block;
  height: 100%;
  box-sizing: border-box;
  border-bottom: 2px solid #4490f1;
  position: relative;
  top: 1px;
}
#configBacklog .step-item-title > .btn-box {
  float: right;
}
#configBacklog .btn-box span {
  cursor: pointer;
  display: inline-block;
  padding: 0 10px;
  color: #3891e0;
  font-size: 16px;
}
#configBacklog .btn-box span:hover {
  color: #035cac;
}
#configBacklog .distribution-table-tips > span {
  color: #035cac;
}
#configBacklog .upload_box_img .el-upload--picture-card {
  width: 100px;
  height: 100px;
  line-height: 100px;
}
#configBacklog
  .upload_box_img
  .el-upload-list--picture-card
  .el-upload-list__item {
  width: 100px;
  height: 100px;
}
#configBacklog .el-upload--picture-card {
  margin-bottom: 8px;
}
#configBacklog .el-upload-list > li {
  margin-bottom: 0;
}
#configBacklog .improving-file-upload .el-upload {
  margin-top: 40px;
}
#configBacklog .improving-file-upload {
  position: relative;
  min-width: 110px;
}
#configBacklog .upload-icon {
  font-size: 60px;
  position: absolute;
  top: 0;
  left: 20px;
  color: #dfdfdf;
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
</style>


