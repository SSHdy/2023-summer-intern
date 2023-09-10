<template>
  <div id="repairProjectManage">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">申请时间：</div>
          <div class="query-input">
            <el-date-picker
              v-model="beginDate"
              type="date"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择日期"
              :picker-options="pickerOptions_start"
            ></el-date-picker>
          </div>
        </div>
        <div class="query-item">
          <div class="query-title" style="margin:0 5px">至</div>
          <div class="query-input">
            <el-date-picker
              v-model="endDate"
              type="date"
              value-format="yyyy-MM-dd 23:59:59"
              placeholder="选择日期"
              :picker-options="pickerOptions_end"
            ></el-date-picker>
          </div>
        </div>
        <div class="query-item" style="margin-right: 50px">
          <div class="query-title">工程名称：</div>
          <div class="query-input">
            <el-select v-model="repairApplyId" placeholder="请选择工程名称">
              <el-option
                v-for="(item,index) in engineOpts"
                :key="index"
                :label="item.repairProject"
                :value="item.repairApplyId"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="query-item">
          <div class="query-title">院落：</div>
          <div class="query-input">
            <el-select v-model="yardId" placeholder="请选择院落">
              <el-option
                v-for="item in yardOpts"
                :key="item.yardId"
                :label="item.yardName"
                :value="item.yardId"
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
      <table-component
        :tableData="tableData"
        :tableHeight="tableHeight"
        @select="handleSelectionChange"
      >
        <el-table-column type="index" label="序号" align="center" width="50"></el-table-column>
        <el-table-column
          prop="repairProject"
          label="工程名称"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column label="使用单位" align="center" prop="unitName" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="finalSum" label="财政评审金额" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column
          prop="designUnit"
          label="施工单位"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="repairApplyDate"
          width="240"
          label="申请时间"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column label="管理" align="center" width="120" prop="repairProgress">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDetail="false"
              :isShowAlter="false"
              :isShowDelete="false"
              :isShowConstruction="scope.row.repairProgress != 100 && scope.row.repairType == 2"
              :isShowCompleted="scope.row.repairProgress == 100 || scope.row.repairType == 0"
              @constructionClick="constructionClick(scope.row)"
              @completedClick="completedClick(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
        <el-table-column label="付款" width="120" align="center">
          <template slot-scope="scope">
            <single-btn-group
              :isShowAlter="false"
              :isShowDelete="false"
              :isShowProcess="scope.row.repairType == 2"
              @processClick="processClick(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
        <el-table-column label="项目状态" align="center" prop="repairProgress">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.repairProgress"
              :text-inside="true"
              :stroke-width="12"
            ></el-progress>
          </template>
        </el-table-column>
        <el-table-column label="负责人" align="center" width="80">
          <template slot-scope="scope">
            <single-btn-group
              :isShowAlter="false"
              :isShowDelete="false"
              :isShowMove="scope.row.repairType == 2"
              @moveClick="moveClick(scope.row)"
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
      :visible.sync="dialogVisible.ConstructionManagement"
      :close-on-click-modal="false"
      width="1100px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>{{title}}</span>
      </div>
      <div
        class="repair-info-box"
        style="margin-bottom:20px;height:500px;border-bottom:1px solid #d8d8d8"
      >
        <div class="repair-info-base clearfix">
          <div v-for="item in baseInfo" :key="item.id">
            <span v-if="item.text!=''">{{item.name}}：</span>
            <span>{{item.text}}</span>
          </div>
        </div>
        <div class="repair-info-detail" style="margin-top:20px;">
          <div class="info-detail-title clearfix">
            <span>维修信息：</span>
            <div class="info-detail-btn">
              <el-popover
                class="detail"
                placement="bottom-end"
                width="300"
                trigger="click"
                popper-class="More-Info"
                :visible-arrow="false"
              >
                <ul class="repairProject-info-more">
                  <li
                    v-for="(item, index) in repairProjectInfoMore"
                    :key="index"
                    :class="{'info-active': activeInfo === index}"
                    @click="showInfoDetail(item.repairModuleId,index)"
                  >{{item.text}}</li>
                </ul>
                <span class="more-btn" slot="reference" v-show="repairProjectInfoMore.length > 0">
                  更多
                  <i class="el-icon-arrow-down"></i>
                </span>
              </el-popover>
            </div>
          </div>
          <div class="section2-content">
            <div v-if="infoState">
              <span>维修地点：</span>
              <span>{{repaireModuleAddress}}</span>
            </div>
            <div v-if="infoState">
              <span>维修部位：</span>
              <span>{{repairPartName}}</span>
            </div>
            <div v-if="infoState">
              <span>维修内容：</span>
              <span>{{repairModuleTarget}}</span>
            </div>
            <div v-if="infoState">
              <span>维修面积(m²)：</span>
              <span>{{repairModuleArea}}</span>
            </div>
            <div v-if="completionQuality && infoState">
              <span>质保日期：</span>
              <span>{{completionQuality}}</span>
            </div>
            <div v-if="infoState && detailFileListImg.length>0" style="height:70px">
              <span style="display:inline-block">照片：</span>
              <div
                style="display:inline-block;vertical-align: top;padding-top:12px"
                class="img-preview-box clearfix"
              >
                <div
                  class="img-preview-item"
                  v-for="(item, index) in detailFileListImg"
                  :key="index"
                >
                  <img :src="getImgUrl(item.virtualAddress)" alt="已上传图片" />
                  <!-- <div class="bigger-view-warpper">
                    <div class="bigger-view-warpper-inner"></div>
                    <i class="el-icon-search" @click="preview(item.virtualAddress)"></i>
                  </div>-->
                  <!-- <el-checkbox @change="imgCheckboxChange($event, item.enclosureId)"></el-checkbox> -->
                </div>
              </div>
            </div>
            <div v-if="!infoState" class="noInfo">暂无维修记录</div>
          </div>
        </div>
      </div>
      <el-collapse accordion v-show="operateType == 'complete'">
        <el-collapse-item
          :name="index"
          v-for="(item ,index) in logInfo"
          :key="index"
          v-show="logInfo.length !=0"
        >
          <template slot="title">
            <div class="info-detail-title workDaily clearfix">
              <span>工作日志：</span>
              <span>{{item.repairProjectUsername}}</span>
              <span>{{item.repairProjectDate}}</span>
            </div>
          </template>
          <div
            v-show="!state"
            class="edit-btn"
            style="font-size:14px;float:right;line-height: 45px;"
          >
            <el-popover
              class="detail"
              placement="bottom-end"
              width="300"
              trigger="click"
              popper-class="More-Info"
              :visible-arrow="false"
            >
              <ul class="repairProject-info-more">
                <li
                  v-for="(item, index) in item['partsIdAndImgVoList']"
                  :key="index"
                  :class="{'info-active': activeInfo === index}"
                  @click="showPartDetail(index)"
                >{{item.repairPartName}}</li>
              </ul>
              <span
                class="more-btn"
                slot="reference"
                v-show="item['partsIdAndImgVoList'].length > 0"
              >
                更多
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-popover>
          </div>
          <!-- <div
            v-show="index == logInfo.length-1 && !state"
            class="edit-btn"
            style="font-size:14px;float:right;margin-right:10px;line-height: 45px;"
            @click="editBtn"
          >
            <i class="iconfont">&#xe61a;</i>编辑
          </div>-->
          <!-- <div
            v-show="index == logInfo.length-1 && !state && edit"
            class="edit-btn"
            style="font-size:14px;float:right;margin-right:10px;line-height: 45px;"
            @click="saveBtn"
          >
            <i class="iconfont">&#xe61a;</i>保存
          </div>-->
          <div class="section2-content" style="padding-top:10px">
            <el-form :inline="true" :rules="rules" ref="Forms">
              <el-form-item
                label="施工部位："
                prop="repairePart"
                label-width="92px"
                class="detail-form-item"
              >
                <el-input
                  v-model="item['partsIdAndImgVoList'][indexs]['repairPartName']"
                  size="small"
                  class="detail-form-item"
                  readonly="readonly"
                ></el-input>
              </el-form-item>
              <el-form-item
                label-width="92px"
                :label="'照片('+item['partsIdAndImgVoList'][indexs]['repairPartName'] + ')：'"
                style="display: block;"
              >
              <span>{{item['partsIdAndImgVoList'][indexs]['repairPartName']}}</span>
                <div class="img-preview-box clearfix">
                  <div
                    class="img-preview-item"
                    v-for="items in item['partsIdAndImgVoList'][indexs]['repairPmImgList']"
                    :key="items.repairPmImgId"
                    @click="preview(items.repairPmImgVirtualUrl)"
                  >
                    <img :src="getImgUrl(items.repairPmImgVirtualUrl)" alt="已上传证件" />
                    <div class="bigger-view-warpper">
                      <i class="bigger-view-icon"></i>
                    </div>
                  </div>
                </div>
              </el-form-item>
              <el-form-item
                label="施工天气："
                label-width="92px"
                prop="repairProjectWeather"
                class="detail-form-item"
              >
                <el-input v-model="item.repairProjectWeather" size="small" readonly="readonly"></el-input>
              </el-form-item>
              <el-form-item
                label="施工人数："
                label-width="92px"
                style="width:100%"
                prop="repairProjectNum"
                class="detail-form-item"
              >
                <el-input v-model="item.repairProjectNum" size="small" readonly="readonly"></el-input>
              </el-form-item>
              <el-form-item
                label="存在问题："
                label-width="92px"
                prop="repairProjectProblems"
                class="detail-form-item"
              >
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 2, maxRows: 4}"
                  v-model="item.repairProjectProblems"
                  size="small"
                  readonly="readonly"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="质量安全："
                label-width="92px"
                prop="repairProjectSecurity"
                class="detail-form-item"
              >
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 2, maxRows: 4}"
                  v-model="item.repairProjectSecurity"
                  size="small"
                  readonly="readonly"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="文明施工："
                label-width="92px"
                prop="repairProjectConstruction"
                class="detail-form-item"
              >
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 2, maxRows: 4}"
                  v-model="item.repairProjectConstruction"
                  size="small"
                  readonly="readonly"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="项目进度："
                label-width="92px"
                prop="repairProjectProgress"
                class="detail-form-item"
              >
                <el-slider v-model="item.repairProjectProgress" disabled="disabled"></el-slider>
              </el-form-item>
            </el-form>
          </div>
        </el-collapse-item>
        <el-collapse-item :name="logInfo.length" v-show="editLogInfo.repairProjectUsername">
          <template slot="title">
            <div class="info-detail-title workDaily clearfix">
              <span>工作日志：</span>
              <span>{{editLogInfo.repairProjectUsername}}</span>
              <span>{{editLogInfo.repairProjectDate}}</span>
            </div>
          </template>
          <div class="edit-btn" style="font-size:14px;float:right;line-height: 45px;">
            <el-popover
              class="detail"
              placement="bottom-end"
              width="300"
              trigger="click"
              popper-class="More-Info"
              :visible-arrow="false"
            >
              <ul class="repairProject-info-more">
                <li
                  v-for="(item, index) in editLogInfo['partsIdAndImgVoList']"
                  :key="index"
                  :class="{'info-active': activeInfo === index}"
                  @click="showPartDetails(index)"
                >{{item.repairPartName}}</li>
              </ul>
              <span class="more-btn" slot="reference">
                更多
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-popover>
          </div>
          <div
            v-show="!state"
            class="edit-btn"
            style="font-size:14px;float:right;margin-right:10px;line-height: 45px;"
            @click="editBtn"
          >
            <i class="iconfont">&#xe61a;</i>编辑
          </div>
          <div
            v-show="!state && edit"
            class="edit-btn"
            style="font-size:14px;float:right;margin-right:10px;line-height: 45px;"
            @click="saveBtn"
          >
            <i class="iconfont">&#xe61a;</i>保存
          </div>

          <div class="section2-content" style="padding-top:10px">
            <el-form :inline="true" :rules="rules" ref="Forms">
              <el-form-item
                label="施工部位："
                prop="repairePart"
                label-width="92px"
                :class="{'detail-form-item' : !edit}"
              >
                <el-select
                  v-if="edit"
                  v-model="Forms.repairPartName"
                  placeholder="请选择施工部位"
                  style="width:100%;height:32px"
                  size="small"
                >
                  <el-option
                    v-for="its in repairProjectInfoMore"
                    :key="its.repairPartId"
                    :label="its.repairPartName"
                    :value="its.repairPartName"
                  ></el-option>
                </el-select>
                <!-- <el-input
                   v-if="!edit"
                  size="small"
                  v-model="editLogInfo['partsIdAndImgVoList'][indexs]['repairPartName']"
                  class="detail-form-item"
                  readonly='readonly'
                ></el-input>-->
                <span v-if="!edit">{{Forms.repairPartName}}</span>
              </el-form-item>
              <el-form-item
                label-width="92px"
                :label="'照片('+Forms.repairPartName + ')：'"
                style="display: block;"
              >
                <div class="upload_box" v-if="edit">
                  <el-upload
                    ref="uploads"
                    :headers="postHeaders"
                    :action="uploadImgUrl"
                    list-type="picture-card"
                    :file-list="showFileList"
                    :on-remove="handleShowRemove"
                    :auto-upload="false"
                    :on-success="handleShowSuccess"
                    :on-change="handleShowChange"
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
                <div class="img-preview-box clearfix" v-else>
                  <div
                    class="img-preview-item"
                    v-for="items in Forms.repairPmImgList"
                    :key="items.repairPmImgId"
                    @click="preview(items.repairPmImgVirtualUrl)"
                  >
                    <img :src="getImgUrl(items.repairPmImgVirtualUrl)" alt="已上传证件" />
                    <div class="bigger-view-warpper">
                      <i class="bigger-view-icon"></i>
                    </div>
                  </div>
                </div>
              </el-form-item>
              <el-form-item
                label="施工天气："
                label-width="92px"
                prop="repairProjectWeather"
                :class="{'detail-form-item':!edit}"
              >
                <el-input
                  v-model="editLogInfo.repairProjectWeather"
                  size="small"
                  placeholder="请输入施工天气"
                  :readonly="!edit"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="施工人数："
                label-width="92px"
                style="width:100%"
                prop="repairProjectNum"
                :class="{'detail-form-item':!edit}"
              >
                <el-input v-model="editLogInfo.repairProjectNum" size="small" :readonly="!edit"></el-input>
              </el-form-item>
              <el-form-item
                label="存在问题："
                label-width="92px"
                prop="repairProjectProblems"
                :class="{'detail-form-item':!edit}"
              >
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 2, maxRows: 4}"
                  v-model="editLogInfo.repairProjectProblems"
                  size="small"
                  :readonly="!edit"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="质量安全："
                label-width="92px"
                prop="repairProjectSecurity"
                :class="{'detail-form-item':!edit}"
              >
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 2, maxRows: 4}"
                  v-model="editLogInfo.repairProjectSecurity"
                  size="small"
                  :readonly="!edit"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="文明施工："
                label-width="92px"
                prop="repairProjectConstruction"
                :class="{'detail-form-item':!edit}"
              >
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 2, maxRows: 4}"
                  v-model="editLogInfo.repairProjectConstruction"
                  size="small"
                  :readonly="!edit"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="项目进度："
                label-width="92px"
                prop="repairProjectProgress"
                :class="{'detail-form-item':!edit}"
              >
                <el-slider v-model="Forms.repairProjectProgress" :disabled="!edit"></el-slider>
              </el-form-item>
            </el-form>
          </div>
        </el-collapse-item>
      </el-collapse>
      <div style="margin-top:10px" v-show="operateType == 'complete' && state ">
        <div class="constructLog">
          <div>施工日志：</div>
        </div>
        <div class="section2-content add" style="padding: 20px 0 20px 80px;padding-left:80px">
          <div class="addRepairPart">
            <span @click="addrepairPart">
              <i class="el-icon-circle-plus-outline"></i>新增施工部位
            </span>
          </div>
          <el-form :inline="true" :model="forms" :rules="rule" ref="forms">
            <el-form-item label="施工部位" label-width="80px" prop="repairePart" class="repairePart">
              <el-select
                v-model="forms.repairPartName"
                placeholder="请选择施工部位"
                style="width:100%;height:32px"
                popper-class="repairPart"
                size="small"
              >
                <el-option
                  v-for="item in repairProjectInfoMore"
                  :key="item.repairPartId"
                  :label="item.repairPartName"
                  :value="item.repairPartName"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item
              label-width="80px"
              :label="forms.repairPartName?`照片(${forms.repairPartName})`:'照片'"
              style="display: block;"
            >
              <div class="upload_box">
                <el-upload
                  ref="uploadRegImg"
                  :headers="postHeaders"
                  :action="uploadImgUrl"
                  list-type="picture-card"
                  :file-list="fileLists"
                  :on-remove="handleRegRemove"
                  :auto-upload="false"
                  :on-success="handleRegSuccess"
                  :on-change="handleRegChange"
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
                  v-for="im in partsIdImgList"
                  :key="im.id"
                  @click="preview(im.text)"
                >
                  <img :src="getImgUrl(im.text)" alt="已上传证件" />
                  <div class="bigger-view-warpper">
                    <i class="bigger-view-icon"></i>
                  </div>
                  <el-checkbox @change="imgCheckboxChange($event, item.text)"></el-checkbox>
                </div>
                <!-- <el-button
                    type="primary"
                    icon="el-icon-download"
                    @click="downloadImg"
                    v-show="detailFileList.length"
                >下载证件</el-button>-->
              </div>
            </el-form-item>
            <el-form-item label-width="80px" label="天气" prop="repairProjectWeather">
              <el-input v-model="forms.repairProjectWeather" size="small" placeholder="请输入施工天气"></el-input>
            </el-form-item>
            <el-form-item label="施工人数" label-width="80px" prop="repairProjectNum">
              <el-input
                @keyup.native="limitFloat('repairProjectNum')"
                @blur="limitFloat('repairProjectNum', true)"
                v-model="forms.repairProjectNum"
                size="small"
                placeholder="请输入施工人数"
              ></el-input>
            </el-form-item>
            <el-form-item label-width="80px" label="存在问题" prop="repairProjectProblems">
              <el-input
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4}"
                v-model="forms.repairProjectProblems"
                size="small"
                placeholder="请输入存在问题"
              ></el-input>
            </el-form-item>
            <el-form-item label-width="80px" label="质量安全" prop="repairProjectSecurity">
              <el-input
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4}"
                v-model="forms.repairProjectSecurity"
                size="small"
                placeholder="请输入质量安全"
              ></el-input>
            </el-form-item>
            <el-form-item label="文明施工" label-width="80px" prop="repairProjectConstruction">
              <el-input
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4}"
                v-model="forms.repairProjectConstruction"
                size="small"
                placeholder="请输入文明施工"
              ></el-input>
            </el-form-item>
            <el-form-item label-width="80px" label="施工进度" prop="repairProjectProgress">
              <el-slider v-model="forms.repairProjectProgress"></el-slider>
            </el-form-item>

            <el-form-item label-width="80px" label="增项内容" prop="repairItemsAddExplain">
              <el-input
                :autosize="{ minRows: 2, maxRows: 4}"
                v-model="forms.repairItemsAddExplain"
                size="small"
                placeholder="请输入增项"
              ></el-input>
            </el-form-item>
            <el-form-item label="减项内容" label-width="80px" prop="repairItemsReduceExplain">
              <el-input
                :autosize="{ minRows: 2, maxRows: 4}"
                v-model="forms.repairItemsReduceExplain"
                size="small"
                placeholder="请输入减项"
              ></el-input>
            </el-form-item>
            <el-form-item label-width="80px" label="增减文件" prop="uploadFile" style="display: block;">
              <div class="upload_box">
                <el-upload
                  ref="uploadRegCerts"
                  :headers="postHeaders"
                  :action="uploadReguCertUrl"
                  :file-list="fileListRegCert"
                  :auto-upload="false"
                  :accept="acceptCertType"
                  :limit="limitNums"
                  :on-success="handleRegCertSuccess"
                  :on-change="handleRegCertChange"
                  :on-remove="handleRegCertRemove"
                  :on-error="handleRegError"
                  :on-exceed="handleRegCertExceed"
                  :before-upload="beforeAvatarUpload"
                  multiple
                >
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
                <div class="cert-preview-box">
                  <div
                    class="cert-preview-item"
                    v-for="(item, index) in detailFileListRegCert"
                    :key="index"
                  >
                    <el-checkbox @change="certCheckboxChange($event, item.enclosureId)"></el-checkbox>
                    <span class="cert-preview-name">{{item.enclosureName}}</span>
                  </div>
                  <el-button
                    type="primary"
                    icon="el-icon-download"
                    class="download-btn"
                    @click="downloadImg('cert')"
                    v-show="detailFileListRegCert.length > 0"
                  >下载文件</el-button>
                </div>
              </div>
            </el-form-item>
          </el-form>
          <div class="query-box" id="queryBox" v-show="operateType==='complete'"></div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <div v-show="operateType==='constructManage'">
          <div class="Title">
            <div>维修验收：</div>
          </div>
          <el-form class="completeForm" :inline="true" :model="Form" :rules="Rules" ref="Form">
            <el-form-item
              v-show="repairType == 0"
              :label=" status ? '施工单位：':'施工单位'"
              prop="constructionUnit"
              label-width="100px"
              :class="{'detail-form-item' : status}"
            >
              <el-input
                v-model="Form.constructionUnit"
                size="small"
                :placeholder="!status?'请输入施工单位':''"
                :readonly="status"
              ></el-input>
            </el-form-item>
            <el-form-item
              :label=" status ? '联系方式：':'联系方式'"
              v-show="repairType == 0"
              prop="constructionUnit"
              label-width="100px"
              :class="{'detail-form-item' : status}"
            >
              <el-input
                type="phone"
                v-model="Form.phone"
                size="small"
                :placeholder="!status?'请输入联系方式':''"
                :readonly="status"
              ></el-input>
            </el-form-item>
            <el-form-item
              :label=" status ? '负责人：':'负责人'"
              v-show="repairType == 0"
              prop="constructionUnit"
              label-width="100px"
              :class="{'detail-form-item' : status}"
            >
              <el-input
                v-model="Form.applicantUserName"
                size="small"
                :placeholder="!status?'请输入负责人':''"
                :readonly="status"
              ></el-input>
            </el-form-item>
            <el-form-item
              :label=" status ? '开工时间：':'开工时间'"
              v-show="repairType == 0"
              prop="startTime"
              label-width="100px"
              :class="{'detail-form-item' : status}"
            >
              <el-date-picker
                v-if="!status"
                v-model="Form.startTime"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
              ></el-date-picker>
              <span v-else>{{Form.startTime}}</span>
            </el-form-item>
            <el-form-item
              :label=" status ? '竣工报告：':'竣工报告'"
              label-width="100px"
              prop="auditing"
              style="display: block;margin-bottom:5px"
            >
              <div class="upload_box" v-show="!status">
                <el-upload
                  ref="uploadCert"
                  :headers="postHeaders"
                  :action="uploadCertUrl"
                  :file-list="fileListCert"
                  :auto-upload="false"
                  :accept="acceptCertType"
                  :limit="limitNum"
                  :on-success="handleCertSuccess"
                  :on-change="handleCertChange"
                  :on-remove="handleCertRemove"
                  :on-error="handleError"
                  :on-exceed="handleCertExceed"
                  :before-upload="beforeAvatarUpload"
                  multiple
                >
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
              <el-form-item label-width="90px">
                <div class="repaire-file-download clearfix" v-if="cerList && cerList.length > 0">
                  <el-checkbox-group
                    v-model="fileDownLoadCheckedList"
                    @change="handleCheckedFileChange"
                  >
                    <div
                      v-for="(file, index) in cerList"
                      :key="index"
                      class="repaire-file-download-item"
                    >
                      <el-checkbox :label="file.detail">{{file.repairFileName}}</el-checkbox>
                    </div>
                    <el-button
                      size="small"
                      type="primary"
                      @click.native="fileDownLoad(downloadCheckedList)"
                    >点击下载</el-button>
                  </el-checkbox-group>
                </div>
              </el-form-item>
            </el-form-item>
            <el-form-item
              :label=" status ? '竣工图纸：':'竣工图纸'"
              label-width="100px"
              prop="drawing"
              style="display: block;"
            >
              <div class="upload_box" v-show="!status" style="padding-top: 10px;">
                <el-upload
                  ref="upload"
                  list-type="picture-card"
                  :headers="postHeaders"
                  :action="uploadImgUrl"
                  :file-list="fileListImg"
                  :auto-upload="false"
                  :accept="completeAcceptImgType"
                  :limit="limitNum"
                  :on-success="handleSuccess"
                  :on-change="handleChange"
                  :on-error="handleError"
                  :on-remove="handleRemove"
                  :on-exceed="handleExceed"
                  :before-upload="beforeAvatarUpload"
                  multiple
                >
                  <i class="el-icon-plus"></i>
                </el-upload>
              </div>
              <el-form-item label-width="105px">
                <div
                  class="repaire-file-download clearfix"
                  style="padding-top:12px"
                  v-if="imgList && imgList.length > 0"
                >
                  <el-checkbox-group
                    v-model="imageDownLoadCheckedList"
                    @change="handleCheckedImageChange"
                  >
                    <div class="img-view-item" v-for="(image, index) in imgList" :key="index">
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
                      @click="fileDownLoads(downloadCheckedList_img)"
                    >点击下载</el-button>
                  </div>
                </div>
              </el-form-item>
            </el-form-item>
            <el-form-item
              style="margin:10px 0"
              :label=" status ? '备注信息：':'备注信息'"
              label-width="90px"
              prop="comment"
              :class="{'detail-form-item' : status}"
            >
              <el-input
                type="textarea"
                v-model="Form.comment"
                size="small"
                :readonly="status"
                :placeholder="!status?'请输入备注信息':''"
              ></el-input>
            </el-form-item>
          </el-form>
        </div>
        <cancel-btn @clickBtn="dialogVisible.ConstructionManagement = false" :fontSize="14"></cancel-btn>
        <confirm-btn
          @clickBtn="submitForm('Form')"
          :fontSize="14"
          v-show="operateType==='constructManage'"
        ></confirm-btn>
        <confirm-btn
          @clickBtn="submitForms('forms')"
          :fontSize="14"
          v-show="operateType==='complete' && state"
        ></confirm-btn>
        <confirm-btn
          @clickBtn="submitforms('Forms')"
          :fontSize="14"
          v-show="operateType==='complete' && !state"
        ></confirm-btn>
      </span>
    </el-dialog>
    <el-dialog
      class="move"
      :visible.sync="dialogVisible.move"
      :close-on-click-modal="false"
      width="1100px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>项目转移</span>
      </div>
      <el-form :inline="true" :model="form" :rules="rules" ref="form">
        <el-form-item label="选择转移人员：" label-width="130px" prop="movePerson">
          <el-select
            v-model="form.movePerson"
            placeholder="请选择转移人员"
            style="width:200px"
            size="small"
          >
            <el-option
              v-for="item in personOpts"
              :key="item.user_id"
              :label="item.username"
              :value="item.user_id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="项目转移原因：" label-width="130px" prop="moveReason">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入项目转移原因"
            v-model="form.moveReason"
            style="margin-top: 2px;width:534px;"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" v-show="operateType!=='detail'">
        <cancel-btn @clickBtn="dialogVisible.move = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitMove" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
    <el-dialog
      class="payProcess"
      :visible.sync="dialogVisible.payProcess"
      :close-on-click-modal="false"
      width="1100px"
      @open="dialogOpen"
      @closed="dialogClosed"
      :center="true"
    >
      <div slot="title" class="dialog-title">
        <span>付款流程</span>
      </div>
      <el-steps :active="active" align-center finish-status="success">
        <el-step
          v-for="(item,index) in RepairPaymentDetail"
          :key="index"
          :title="item.stepLabel ? item.stepLabel:''"
          @click.native="step(item.repairPaymentId,item.repairPaymentState,index)"
        >
          <div slot="description">
            <div :class="{'hidden':!item.repairPaymentUsername}">
              <span>付款人：</span>
              {{item.repairPaymentUsername}}
            </div>
            <div :class="{'hidden':  typeof(item.repairPaymentMoney) == Number}">
              <span>付款金额(万)：</span>
              {{item.repairPaymentMoney}}
            </div>
            <div :class="{'hidden':!item.repairPaymentTime}">
              <span>付款时间：</span>
              {{item.repairPaymentTime}}
            </div>
          </div>
        </el-step>
      </el-steps>
    </el-dialog>
  </div>
</template>
<script>
import { BASE_URL, url_name_1 } from "@/axios/global";
import { uploadUrl, getAllYardList } from "@/axios/commonApi";
import {
  getEngine,
  getRepairProject,
  download,
  getProjectDetail,
  getRepairApprovalDetail,
  getRepairMessageDetail,
  addConstructManage,
  getRepairManage,
  getUser,
  changeManage,
  getRepairPayment,
  updateState,
  getRepairPartAllList,
  addRepairManage,
  updateRepairManage,
  completedProjectDetail,
  downloadRentAccessory
} from "./api";
import { downloadFile } from "./api_2.js";
import tableComponent from "@/components/table/table2.vue";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import addBtn from "@/components/button/addBtn";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import tablePagination from "@/components/tableGroup/tablePagination";
import { fail } from "assert";
import { log } from "util";
export default {
  components: {
    confirmBtn,
    cancelBtn,
    tableComponent,
    singleBtnGroup,
    tableBtnGroup,
    tablePagination,
    searchBtn,
    resetBtn
  },
  data() {
    let validAuditing = (rule, value, callback) => {
      if (!this.Form.auditing) {
        callback(new Error("请先上传报告"));
      } else {
        callback();
      }
    };
    let validDrawing = (rule, value, callback) => {
      if (!this.Form.drawing) {
        callback(new Error("请先上传图纸"));
      } else {
        callback();
      }
    };
    let validcode = (rule, value, callback) => {
      if (!this.forms.uploadFile) {
        callback(new Error("请先上传文件"));
      } else {
        callback();
      }
    };
    return {
      indexs: 0,
      submitFlag: false,
      ImgList: [],
      activeInfo: 0,
      title: "",
      beginDate: "",
      endDate: "",
      repairApplyId: "",
      yardId: "",
      yardOpts: [],
      engineOpts: [],
      loading: null,
      tableHeight: 0,
      tableData: [],
      currentPage: 1,
      pageSize: 15,
      total: 1,
      dialogVisible: {
        ConstructionManagement: false,
        move: false,
        payProcess: false
      },
      baseInfo: [
        {
          id: 1,
          text: "",
          data: "repairProject",
          name: "维修项目名称"
        },
        {
          id: 2,
          text: "",
          data: "finalSum",
          name: "财政评审金额"
        },
        {
          id: 3,
          text: "",
          data: "repairModuleArea",
          name: "维修面积(m²)"
        },
        {
          id: 4,
          text: "",
          data: "repairApplicant",
          name: "申请人"
        },
        {
          id: 5,
          text: "",
          data: "repairApplyDate",
          name: "申请时间"
        },
        {
          id: 6,
          text: "",
          data: "unitName",
          name: "使用单位"
        },
        {
          id: 7,
          text: "",
          data: "designUnit",
          name: "设计单位"
        },
        {
          id: 8,
          text: "",
          data: "accreditationUnit",
          name: "监理单位"
        },
        {
          id: 9,
          text: "",
          data: "auditUnit",
          name: "审计单位"
        },
        {
          id: 10,
          text: "",
          data: "planningPermit",
          name: "规划许可证"
        }
      ],
      repairInfoMore: [],
      repairProjectInfoMore: [],
      popoverShow: false,
      operateType: "constructManage",
      // operateTypes:'edit',
      edit: false,
      // toggleFlag:true,
      Form: {
        comment: "",
        addressList: [],
        deleteEnclosureIdList: [],
        constructionUnit: "",
        applicantUserName: "",
        phone: "",
        startTime: "",
        auditing: "",
        drawing: ""
      },
      Forms: {
        lesseeUse: "",
        repairPartName: "",
        repairPmImgList: [],
        addressList: [],
        deleteEnclosureIdList: []
      },
      rules: {
        // renter: [
        //   { required: true, message: "承租方不能为空", trigger: "blur" }
        // ],
        // rentMoney: [
        //   { required: true, message: "租金不能为空", trigger: "blur" }
        // ],
        // deposit: [
        //   { required: true, message: "押金不能为空", trigger: "blur" }
        // ],
        // rentBegin: [
        //   { validator: validcodeRentBegin, trigger: "blur" }
        // ],
      },
      rule: {
        uploadFile: [{ required: true, validator: validcode, trigger: "blur" }]
      },
      Rules: {
        auditing: [
          { required: true, validator: validAuditing, trigger: "blur" }
        ],
        drawing: [{ required: true, validator: validDrawing, trigger: "blur" }]
      },
      token: "",
      postHeaders: {
        Authorization: ""
      },
      limitImgNum: 5,
      limitNum: 5,
      limitNums: 1,
      acceptCertType: ".doc,.docx",
      acceptImgType: "image/jpeg,image/gif,image/png,image/bmp",
      completeAcceptImgType: ".dwg,.dwt,.dws,.dxt",
      uploadImgUrl: `${uploadUrl}?uploadType=constructFile`,
      uploadCertUrl: `${uploadUrl}?uploadType=constructFile`,
      uploadReguCertUrl: `${uploadUrl}?uploadType=constructFile`,
      uploadSuccessImgNum: 0,
      uploadSuccessRegImgNum: 0,
      uploadSuccessCertNum: 0,
      uploadSuccessRegCertNum: 0,
      fileListCert: [],
      fileListRegCert: [],
      oldFileListRegCert: [],
      oldFileListCert: [],
      fileList: [],
      showFileList: [],
      oldShowFileList: [],
      fileLists: [],
      oldFileList: [],
      oldFileLists: [],
      fileListImg: [],
      detailFileListImg: [],
      detailFileListImgs: [],
      detailFileListCert: [],
      detailFileListCerts: [],
      detailFileListRegCert: [],
      detailFileList: [],
      pictureOpts: [],
      repaireModuleAddress: "",
      repairModuleArea: "",
      completionQuality: "",
      repairModuleTarget: "",
      repairPartName: "",
      comment: "",
      uploadImgNum: 0,
      uploadRegImgNum: 0,
      uploadedImgNum: 0,
      uploadedRegImgNum: 0,
      uploadCertNum: 0,
      uploadRegCertNum: 0,
      uploadedCertNum: 0,
      uploadedRegCertNum: 0,
      listShow: true,
      logInfo: [],
      editLogInfo: {},
      form: {
        movePerson: "",
        moveReason: ""
      },
      personOpts: [],
      RepairPaymentDetail: [],
      active: 0,
      repairType: "",
      repairePart: "",
      partsIdImgList: [],
      repairPartsOpts: [],
      forms: {
        repairPartsId: "",
        repairPartName: "",
        repairProjectWeather: "",
        repairProjectNum: null,
        repairProjectId: null,
        repairProjectDate: "",
        repairProjectProblems: "",
        repairProjectSecurity: "",
        repairProjectConstruction: "",
        repairItemsAddExplain: "",
        repairItemsReduceExplain: "",
        repairProjectProgress: 0,
        addressList: [],
        deleteEnclosureIdList: [],
        uploadFile: ""
      },
      uploadImgChangeFlag: false,
      uploadRegImgChangeFlag: false,
      uploadCertChangeFlag: false,
      uploadRegCertChangeFlag: false,
      submitCertFlag: false,
      submitRegCertFlag: false,
      submitImgFlag: false,
      submitRegImgFlag: false,
      state: true,
      status: false,
      downloadCertList: [],
      downloadImgList: [],
      downLoadList: [],
      fileDownLoadCheckedList: [],
      downloadCheckedList: [],
      imageDownLoadCheckedList: [],
      downloadCheckedList_img: [],
      imgList: [],
      FileList: [],
      cerList: [],
      infoState: true,
      pickerOptions_start: {
      },
      pickerOptions_end: {
      }
    };
  },
  created() {
    this.getEngineList();
    this.getYardList();
    this.getRepairProjectList();
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
    search() {
      this.currentPage = 1;
      this.getRepairProjectList();
    },
    reset() {
      this.currentPage = 1;
      this.beginDate = "";
      this.endDate = "";
      this.repairApplyId = "";
      this.yardId = "";
      this.getRepairProjectList();
    },
    // 获取选中的row
    handleSelectionChange(data) {
      this.selectData = data;
    },
    // 获取基础数据
    getBaseInfo(id) {
      getProjectDetail(id)
        .then(res => {
          let obj = res[0];
          this.baseInfo.forEach(item => {
            for (let key in obj) {
              if (item.data == key) {
                item.text = obj[key];
                if (key == "unitName") {
                  if (obj[key].length > 1) {
                    item.text = obj[key][0];
                  } else {
                    item.text = obj[key].join(",");
                  }
                }
              }
              if (key == "unitName") {
                obj[key];
              }
            }
          });
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取维修项目详情失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取维修详情集合
    getRepairApprovalDetailList(id) {
      getRepairApprovalDetail(id)
        .then(res => {
          if (res.repairModuleDetailDtoList.length > 0) {
            res.repairModuleDetailDtoList.forEach(item => {
              let obj = {};
              if (item.positionStr) {
                obj["text"] = item.positionStr;
                obj["repairModuleId"] = item.repairModuleId;
                obj["repairPartId"] = item.repairPartId;
                obj["repairPartName"] = item["repairPartName"];
                this.repairProjectInfoMore.push(obj);
              }
            });
            if (this.repairProjectInfoMore.length > 0) {
              this.getRepairMessageDetailList(
                this.repairProjectInfoMore[0].repairModuleId
              );
            }
          } else {
            this.infoState = false;
          }
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取维修详情集合失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 维修信息详情
    getRepairMessageDetailList(id) {
      getRepairMessageDetail(id)
        .then(res => {
          this.repaireModuleAddress = res.positionStr;
          this.repairModuleTarget = res.repairModuleTarget;
          this.repairPartName = res.repairPartName;
          this.repairModuleArea = res.repairModuleArea;
          this.completionQuality = res.completionQuality;
          res.repairImgList.forEach(item => {
            this.detailFileListImg.push(item);
          });
        })
        .catch(err => {
          const msg = err.result ? err.result : "维修信息详情失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取维修部位的信息
    getRepairPartAll() {
      getRepairPartAllList().then(res => {
        this.repairPartsOpts = res;
      });
    },
    // 施工管理
    constructionClick(row) {
      this.repairApplyId = row.repairApplyId;
      this.dialogVisible.ConstructionManagement = true;
      this.title = "施工管理";
      this.operateType = "complete";
      this.getBaseInfo(row.repairApplyId);
      this.getRepairApprovalDetailList(row.repairApplyId);
      this.getRepairPartAll();
      getRepairManage(row.repairApplyId)
        .then(res => {
          if (res) {
            if (res.dateState == 1) {
              this.state = false;
            }
            if (JSON.stringify(res) != "{}") {
              res.repairLogDtoList.forEach(item => {
                item["part"] = [];
                item["part"].push(
                  item["partsIdAndImgVoList"][0]["repairPartName"]
                );
                item["repairePart"] =
                  item.part.length > 0 ? item.part.join(",") : "";
              });
              this.logInfo = res.repairLogDtoList;
              if (this.logInfo.length > 1) {
                this.editLogInfo = this.logInfo[this.logInfo.length - 1];
                this.logInfo.pop();
                this.Forms.repairPartName = this.editLogInfo[
                  "partsIdAndImgVoList"
                ][this.indexs].repairPartName;
                this.Forms.repairPmImgList = this.editLogInfo[
                  "partsIdAndImgVoList"
                ][this.indexs].repairPmImgList;
                // this.Forms.repairProjectWeather = this.editLogInfo.repairProjectWeather
                // this.Forms.repairProjectNum = this.editLogInfo.repairProjectNum
                this.Forms.repairProjectId = this.editLogInfo.repairProjectId;
                this.Forms.repairProjectDate = this.editLogInfo.repairProjectDate;
                // this.Forms.repairProjectConstruction = this.editLogInfo.repairProjectConstruction
                // this.Forms.repairProjectProblems = this.editLogInfo.repairProjectProblems
                this.Forms.repairProjectProgress = this.editLogInfo.repairProjectProgress;
                // this.Forms.repairProjectSecurity = this.editLogInfo.repairProjectSecurity
              } else if (this.logInfo.length == 1) {
                this.editLogInfo = JSON.parse(JSON.stringify(this.logInfo[0]));
                this.logInfo.pop();
                this.Forms.repairPartName = this.editLogInfo[
                  "partsIdAndImgVoList"
                ][this.indexs].repairPartName;
                this.Forms.repairPmImgList = JSON.parse(
                  JSON.stringify(
                    this.editLogInfo["partsIdAndImgVoList"][this.indexs]
                      .repairPmImgList
                  )
                );
                // this.Forms.repairProjectWeather = this.editLogInfo.repairProjectWeather
                // this.Forms.repairProjectNum = this.editLogInfo.repairProjectNum
                // this.Forms.repairProjectConstruction = this.editLogInfo.repairProjectConstruction
                // this.Forms.repairProjectProblems = this.editLogInfo.repairProjectProblems
                this.Forms.repairProjectProgress = this.editLogInfo.repairProjectProgress;
                // this.Forms.repairProjectSecurity = this.editLogInfo.repairProjectSecurity
              }
              // for (let i = 0; i < this.logInfo.length; i++) {
              //   for (let j = 0; j < this.logInfo.length - 1; j++) {
              //     if (
              //       new Date(this.logInfo[i].repairProjectDate) <
              //       new Date(this.logInfo[j].repairProjectDate)
              //     ) {
              //       let temp = this.logInfo[i];
              //       this.logInfo[i] = this.logInfo[j];
              //       this.logInfo[j] = temp;
              //     }
              //   }
              // }
            }
          } else {
            this.logInfo = [];
          }
        })
        .catch(err => {
          const msg = err.result ? err.result : "查询施工管理信息失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 竣工验收
    completedClick(row) {
      this.repairApplyId = row.repairApplyId;
      this.repairType = row.repairType;
      this.dialogVisible.ConstructionManagement = true;
      this.title = "竣工验收";
      sessionStorage.setItem("id", row.repairApplyId);
      this.operateType = "constructManage";
      this.getBaseInfo(row.repairApplyId);
      this.getRepairApprovalDetailList(row.repairApplyId);
      this.getCompletedProject(row.repairApplyId);
    },
    // 查询竣工信息
    getCompletedProject(id) {
      completedProjectDetail(id)
        .then(res => {
          if (
            JSON.stringify(res.documentList[0]) != "{}" &&
            res.documentList.length > 0
          ) {
            this.status = true;
            this.Form.comment = res.documentList[0].constructManageDb.comment;
            this.Form.applicantUserName =
              res.documentList[0].constructManageDb.applicantUserName;
            this.Form.startTime =
              res.documentList[0].constructManageDb.startTime;
            this.Form.constructionUnit =
              res.documentList[0].constructManageDb.constructionUnit;
            this.Form.phone = res.documentList[0].constructManageDb.phone;
            this.cerList = res.documentList[0].constructManageDb.auditReport;
            this.imgList = res.documentList[0].constructManageDb.buildDrawings;
            this.imgList.forEach(item => {
              const ext = item.virtualAddress.split(".")[1];
              if (
                ext === "rar" ||
                ext === "zip" ||
                ext == "dwg" ||
                ext == "dwt" ||
                ext == "dws" ||
                ext == "dxt"
              ) {
                item.virtualAddress = "/images/compress.png";
              }
            });
            this.cerList.forEach(file => {
              file.detail = `${file.physicalAddress},${file.repairFileName}`;
            });
            this.imgList.forEach(image => {
              image.detail = `${image.physicalAddress},${image.repairFileName}`;
            });
          }
        })
        .catch(err => {
          const msg = err.result ? err.result : "查询竣工信息失败";
          this.$message({
            message: msg,
            type: "error"
          });
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
      downloadFile(list, 15, this.repairApplyId)
        .then(res => {
          let fileDownload = require("js-file-download");
          let name = decodeURIComponent(
            sessionStorage.getItem("CADdownloadFileName")
          );
          fileDownload(res, name);
        })
        .catch(err => {
          const msg = err.result ? err.result : "文件下载失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 文件下载
    fileDownLoads(list) {
      if (!list || list.length === 0) {
        this.$message({
          message: "请先选择一个文件",
          type: "warning"
        });
        return;
      }
      downloadFile(list, 16, this.repairApplyId)
        .then(res => {
          let fileDownload = require("js-file-download");
          let name = decodeURIComponent(
            sessionStorage.getItem("CADdownloadFileName")
          );
          fileDownload(res, name);
        })
        .catch(err => {
          const msg = err.result ? err.result : "文件下载失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 改变checkbox
    handleCheckedFileChange(val) {
      this.downloadCheckedList = [];
      this.fileDownLoadCheckedList.forEach(item => {
        const arr = item.split(",");
        const obj = {
          address: arr[0],
          fileName: arr[1]
        };
        this.downloadCheckedList.push(obj);
      });
    },
    handleCheckedImageChange(val) {
      this.downloadCheckedList_img = [];
      this.imageDownLoadCheckedList.forEach(item => {
        const arr = item.split(",");
        const obj = {
          address: arr[0],
          fileName: arr[1]
        };
        this.downloadCheckedList_img.push(obj);
      });
    },
    // 文件多选框选择
    certCheckboxChange($event, id) {
      if ($event) {
        if (this.downloadCertList.indexOf(id) === -1) {
          this.downloadCertList.push(id);
        }
      } else {
        const index = this.downloadCertList.indexOf(id);
        this.downloadCertList.splice(index, 1);
      }
    },
    // 图片多选框选择
    imgCheckboxChange($event, id) {
      if ($event) {
        if (this.downloadImgList.indexOf(id) === -1) {
          this.downloadImgList.push(id);
        }
      } else {
        const index = this.downloadImgList.indexOf(id);
        this.downloadImgList.splice(index, 1);
      }
    },
    // 付款流程
    processClick(row) {
      this.dialogVisible.payProcess = true;
      this.repairApplyId = row.repairApplyId;
      this.getRepairPaymentDetail(row.repairApplyId);
    },
    // 获取付款流程详情
    getRepairPaymentDetail(id) {
      getRepairPayment(id)
        .then(res => {
          this.RepairPaymentDetail = res;
          this.RepairPaymentDetail.forEach((item, index) => {
            if (item.repairPaymentState == 3) {
              this.active = index + 1;
            }
          });
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取付款流程详情失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取转移人员列表
    getUserlist(id) {
      getUser(id)
        .then(res => {
          this.personOpts = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : "查询转移人员列表失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 转移
    moveClick(row) {
      this.dialogVisible.move = true;
      this.getUserlist(row.repairApplyId);
      sessionStorage.setItem("id", row.repairApplyId);
    },
    submitMove() {
      let id = sessionStorage.getItem("id");
      let repairNewUsername = "";
      this.personOpts.forEach(item => {
        if (item.user_id == this.form.movePerson) {
          repairNewUsername = item.username;
        }
      });
      changeManage(
        id,
        repairNewUsername,
        this.form.movePerson,
        this.form.moveReason
      )
        .then(res => {
          sessionStorage.removeItem("id");
          this.dialogVisible.move = false;
          this.getRepairProjectList();
        })
        .catch(err => {
          const msg = err.result ? err.result : "项目转移失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 改变页面条数
    changeSize(size) {
      this.currentPage = 1
      this.pageSize = size;
      this.getRepairProjectList();
    },
    // 改变页面页数
    changePage(page) {
      this.currentPage = page;
      this.getRepairProjectList();
    },
    showInfoDetail(repairModuleId, index) {
      this.activeInfo = index;
      this.repaireModuleAddress = "";
      this.repairModuleArea = "";
      this.completionQuality = "";
      this.repairModuleTarget = "";
      this.detailFileListImg = [];
      this.getRepairMessageDetailList(repairModuleId);
    },
    showPartDetail(index) {
      // if(this.toggleFlag){
      this.activeInfo = index;
      this.indexs = index;
      // }
    },
    showPartDetails(index) {
      this.Forms.addressList = [];
      this.edit = false;
      this.activeInfo = index;
      this.Forms["repairPartName"] = this.editLogInfo.partsIdAndImgVoList[
        index
      ].repairPartName;
      this.Forms.repairPmImgList = JSON.parse(
        JSON.stringify(
          this.editLogInfo.partsIdAndImgVoList[index].repairPmImgList
        )
      );
      this.showFileList = [];
      this.editLogInfo["partsIdAndImgVoList"][this.activeInfo][
        "repairPmImgList"
      ].forEach(item => {
        const ext = item.repairPmImgVirtualUrl.split(".")[1];
        if (ext === "rar" || ext === "zip") {
          item.repairPmImgVirtualUrl = "/images/compress.png";
        }
        const obj = {
          name: item.repairPmImgId,
          url:
            BASE_URL +
            url_name_1 +
            item.repairPmImgVirtualUrl +
            "?access_token=" +
            this.token,
          repairPmImgPhysicsUrl: item.repairPmImgPhysicsUrl
        };
        this.showFileList.push(obj);
      });
      this.uploadedImgNum = this.showFileList.length;
      this.uploadSuccessImgNum = 0;
      // }
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
    // 获取工程名称列表
    getEngineList() {
      getEngine()
        .then(res => {
          // this.engineOpts = res
          res.forEach(item => {
            if (item.repairProject) {
              this.engineOpts.push(item);
            }
          });
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取工程名列表失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取所有院落信息列表
    getYardList() {
      getAllYardList(1)
        .then(res => {
          this.yardOpts = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : "获取院落列表失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 获取项目管理列表数据
    getRepairProjectList() {
      this.loadingStart(".table-box");
      if (
        this.beginDate &&
        this.endDate &&
        new Date(this.endDate) < new Date(this.beginDate)
      ) {
        this.$message({
          message: "开始日期不能大于结束日期!",
          type: "warning"
        });
      } else {
        let userId = sessionStorage.getItem("userId");
        getRepairProject(
          this.currentPage,
          this.pageSize,
          this.beginDate,
          this.endDate,
          this.repairApplyId,
          this.yardId,
          userId
        )
          .then(res => {
            this.total = res.total;
            res.records.forEach(item => {
              for (let key in item) {
                if (key == "repairApplyDate") {
                  item[key] = this.formatDate(item[key]);
                } else if (key == "yardId") {
                  this.yardOpts.forEach(items => {
                    if (items.yardId == item[key]) {
                      item[key] = items.yardName;
                    }
                  });
                }
              }
              if (item["unitName"]) {
                item["unitName"] = item["unitName"].join(",");
              }
            });
            this.tableData = res.records;
            this.loadingEnd();
          })
          .catch(err => {
            const msg = err.result ? err.result : "获取所有项目列表失败";
            this.$message({
              message: msg,
              type: "error"
            });
            this.loadingEnd();
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
    dialogOpen() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
      });
    },
    // dialog关闭的回调
    dialogClosed() {
      this.Forms.deleteEnclosureIdList = [];
      this.editLogInfo = {};
      this.indexs = 0;
      this.submitFlag = false;
      this.repairProjectInfoMore = [];
      this.activeInfo = 0;
      sessionStorage.removeItem("id");
      this.clearForm(this.Form);
      this.clearForm(this.forms);
      this.clearForm(this.Forms);
      this.forms.repairProjectProgress = 0;
      this.Form.addressList = [];
      this.baseInfo.forEach(item => {
        for (let key in item) {
          item.value = "";
        }
      });
      this.fileLists = [];
      this.oldFileLists = [];
      this.showFileList = [];
      this.oldShowFileList = [];
      this.fileListCert = [];
      this.fileListRegCert = [];
      this.fileListImg = [];
      this.oldFileListCert = [];
      this.oldFileListRegCert = [];
      this.oldFileListImg = [];
      this.uploadCertNum = 0;
      this.uploadRegCertNum = 0;
      this.uploadedCertNum = 0;
      this.uploadImgNum = 0;
      this.uploadRegImgNum = 0;
      this.uploadedImgNum = 0;
      this.uploadedRegImgNum = 0;
      this.uploadSuccessCertNum = 0;
      this.uploadSuccessRegCertNum = 0;
      this.uploadSuccessImgNum = 0;
      this.uploadSuccessRegImgNum = 0;
      this.submitCertFlag = false;
      this.submitRegCertFlag = false;
      this.submitImgFlag = false;
      this.submitRegImgFlag = false;
      this.detailFileListCert = [];
      this.detailFileListCerts = [];
      this.detailFileListRegCert = [];
      this.detailFileListImg = [];
      this.detailFileListImgs = [];
      this.downloadImgList = [];
      this.downloadCertList = [];
      this.uploadCertChangeFlag = false;
      this.uploadRegCertChangeFlag = false;
      this.uploadImgChangeFlag = false;
      this.uploadRegImgChangeFlag = false;
      this.detailFileList = [];
      this.repairInfoMore = [];
      this.repaireModuleAddress = "";
      this.repairModuleArea = "";
      this.completionQuality = "";
      this.repairModuleTarget = "";
      this.repairPartName = "";
      this.logInfo = [];
      this.repairApplyId = "";
      this.form = {
        movePerson: "",
        moveReason: ""
      };
      this.deleteEnclosureIdList = [];
      this.active = 0;
      this.repairType = "";
      this.addBtn = false;
      this.state = true;
      this.status = false;
      this.downLoadList = [];
      this.fileDownLoadCheckedList = [];
      this.imageDownLoadCheckedList = [];
      this.downloadCheckedList = [];
      this.cerList = [];
      this.imgList = [];
      this.ImgList = [];
      this.infoState = true;
      this.downloadCheckedList_img = [];
      this.edit = false;
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
    // 下载图片
    downloadImg(type) {
      if (type === "cert") {
        if (this.downloadCertList.length > 0) {
          downloadRentAccessory(this.downloadCertList)
            .then(res => {
              let fileDownload = require("js-file-download");
              let name = decodeURIComponent(
                sessionStorage.getItem("CADdownloadFileName")
              );
              fileDownload(res, name);
            })
            .catch(err => {
              const msg = err.result ? err.result : "合同下载失败";
              this.$message({
                message: msg,
                type: "error"
              });
            });
        } else {
          this.$message({
            message: "请先选中一个合同",
            type: "warning"
          });
        }
      } else {
        if (this.downloadImgList.length > 0) {
          downloadRentAccessory(this.downloadImgList)
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
      }
    },
    // 获取图片链接
    getImgUrl(url) {
      return `${BASE_URL}${url_name_1}${url}?access_token=${this.token}`;
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
    arrow() {
      this.listShow = !this.listShow;
    },
    editBtn() {
      this.edit = true;
      this.showFileList = [];
      this.Forms.repairPmImgList.forEach(item => {
        const ext = item.repairPmImgVirtualUrl.split(".")[1];
        if (ext === "rar" || ext === "zip") {
          item.repairPmImgVirtualUrl = "/images/compress.png";
        }
        const obj = {
          name: item.repairPmImgId,
          url:
            BASE_URL +
            url_name_1 +
            item.repairPmImgVirtualUrl +
            "?access_token=" +
            this.token,
          repairPmImgPhysicsUrl: item.repairPmImgPhysicsUrl
        };
        this.showFileList.push(obj);
      });
      // this.toggleFlag = false;
      // this.uploadSuccessImgNum = this.showFileList.length
      this.uploadedImgNum = this.showFileList.length;
    },
    saveBtn() {
      if (this.uploadImgNum != 0) {
        this.$refs.uploads.submit();
      }
    },
    // 新增施工部位
    addrepairPart() {
      this.repairProjectInfoMore.forEach(item => {
        if (item.repairPartName == this.forms.repairPartName) {
          this.forms.repairPartsId = item.repairPartId;
          this.forms.repairModuleId = item.repairModuleId;
        }
      });
      if (!this.uploadRegImgNum == 0) {
        this.$refs.uploadRegImg.submit();
      }
    },
    saveRepairPart() {
      let obj = {
        repairPmImgList: []
      };
      obj["repairPartsId"] = JSON.parse(
        JSON.stringify(this.forms.repairPartsId)
      );
      obj["repairPartName"] = JSON.parse(
        JSON.stringify(this.forms.repairPartName)
      );
      obj["repairModuleId"] = JSON.parse(
        JSON.stringify(this.forms.repairModuleId)
      );
      this.forms.addressList.forEach(item => {
        let object = {};
        object["enclosureType"] = item.enclosureType;
        object["repairFileName"] = item.repairFileName;
        object["repairPmImgPhysicsUrl"] = item.physicalAddress;
        object["repairPmImgVirtualUrl"] = item.virtualAddress;
        obj["repairPmImgList"].push(object);
      });
      this.ImgList.push(obj);
      this.forms.repairPartName = "";
      this.forms.addressList = [];
      this.fileLists = [];
      this.uploadSuccessRegImgNum = 0;
      this.uploadRegImgNum = 0;
      this.uploadedRegImgNum = 0;
      this.submitRegImgFlag = false;
    },
    saveRepairParts() {
      this.editLogInfo.partsIdAndImgVoList[
        this.activeInfo
      ].repairPartName = this.Forms.repairPartName;
      this.repairProjectInfoMore.forEach(item => {
        if (item.repairPartName == this.Forms.repairPartName) {
          this.editLogInfo.partsIdAndImgVoList[this.activeInfo].repairPartsId =
            item.repairPartId;
          this.editLogInfo.partsIdAndImgVoList[this.activeInfo].repairModuleId =
            item.repairModuleId;
        }
      });
      this.editLogInfo.partsIdAndImgVoList[
        this.activeInfo
      ].repairPmImgList = [];
      this.Forms.addressList.forEach(item => {
        let obj = {};
        obj["enclosureType"] = item.enclosureType;
        obj["repairPmImgPhysicsUrl"] = item.physicalAddress;
        obj["repairPmImgVirtualUrl"] = item.virtualAddress;
        this.editLogInfo.partsIdAndImgVoList[
          this.activeInfo
        ].repairPmImgList.push(obj);
      });
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.uploadImgNum === 0 && this.uploadCertNum === 0
            ? this.saveForm()
            : this.submitUpload();
        } else {
          return false;
        }
      });
    },
    submitForms(formName) {
      this.submitFlag = true;
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.uploadRegCertNum === 0 && this.uploadRegImgNum === 0
            ? this.saveForm()
            : this.submitUploads();
        } else {
          return false;
        }
      });
    },
    submitforms(formName) {
      // this.$refs[formName].validate(valid => {
      //   if (valid) {
      this.submitFlag = true;
      if (this.uploadImgNum === this.uploadSuccessImgNum) {
        this.saveForms();
      } else {
        this.submitUpload();
      }
      //   } else {
      //     return false;
      //   }
      // });
    },
    // 保存表单
    saveForm() {
      if (this.operateType === "constructManage") {
        //竣工验收
        if (this.repairType == 0) {
          var constructObject = {
            comment: "",
            buildDrawings: [],
            auditReport: [],
            constructionUnit: "",
            applicantUserName: "",
            phone: "",
            startTime: ""
          };
          constructObject.constructionUnit = this.Form.constructionUnit;
          constructObject.applicantUserName = this.Form.applicantUserName;
          constructObject.phone = this.Form.phone;
          constructObject.startTime = this.Form.startTime;
        } else {
          var constructObject = {
            comment: "",
            buildDrawings: [],
            auditReport: []
          };
        }
        this.Form.addressList.forEach(item => {
          if (item.enclosureType == 4 || item.enclosureType == 3) {
            constructObject.buildDrawings.push(item);
          }
          if (item.enclosureType == 2) {
            constructObject.auditReport.push(item);
          }
        });
        constructObject.comment = this.Form.comment;
        addConstructManage(constructObject, this.repairApplyId)
          .then(res => {
            this.dialogVisible.ConstructionManagement = false;
            this.repairApplyId = "";
            this.getRepairProjectList();
          })
          .catch(err => {
            const msg = err.result ? err.result : "竣工验收失败";
            this.$message({
              message: msg,
              type: "error"
            });
          });
        sessionStorage.removeItem("id");
      } else if (this.operateType === "complete") {
        //新增施工日志
        let repairProject = {};
        let repairItems = [];
        let partsIdAndImgVoList = [];
        let repairPmImgList = [];
        let obj = {};
        let Obj = {};
        this.repairProjectInfoMore.forEach(item => {
          if (item.repairPartName == this.forms.repairPartName) {
            this.forms.repairPartsId = item.repairPartId;
            this.forms["repairModuleId"] = item.repairModuleId;
          }
        });
        Obj["repairModuleId"] = this.forms.repairModuleId;
        Obj["repairPartName"] = this.forms.repairPartName;
        Obj["repairPartsId"] = this.forms.repairPartsId;
        obj["repairItemsAddExplain"] = this.forms.repairItemsAddExplain;
        obj["repairItemsReduceExplain"] = this.forms.repairItemsReduceExplain;
        this.forms.addressList.forEach(item => {
          if (item.enclosureType == 1 || item.enclosureType == 3) {
            let object = {};
            object["enclosureType"] = item.enclosureType;
            object["repairFileName"] = item.repairFileName;
            object["repairPmImgPhysicsUrl"] = item.physicalAddress;
            object["repairPmImgVirtualUrl"] = item.virtualAddress;
            repairPmImgList.push(object);
          } else if (item.enclosureType == 2) {
            obj["repairItemsPhysicalAddress"] = item.physicalAddress;
            obj["repairItemsVirtualAddress"] = item.virtualAddress;
          }
        });
        Obj["repairPmImgList"] = repairPmImgList;
        // repairPmImgList
        this.ImgList.push(Obj);
        partsIdAndImgVoList = this.ImgList;
        obj["repairApplyId"] = this.repairApplyId;
        repairItems.push(obj);
        repairProject[
          "repairProjectConstruction"
        ] = this.forms.repairProjectConstruction;
        repairProject["repairProjectNum"] = this.forms.repairProjectNum;
        repairProject[
          "repairProjectProblems"
        ] = this.forms.repairProjectProblems;
        repairProject[
          "repairProjectProgress"
        ] = this.forms.repairProjectProgress;
        repairProject[
          "repairProjectSecurity"
        ] = this.forms.repairProjectSecurity;
        repairProject["repairProjectWeather"] = this.forms.repairProjectWeather;
        repairProject["repairProjectUsername"] = sessionStorage.getItem(
          "username"
        );
        repairProject["repairProjectUserid"] = sessionStorage.getItem("userId");
        addRepairManage(
          partsIdAndImgVoList,
          this.repairApplyId,
          repairItems,
          repairProject
        )
          .then(res => {
            this.dialogVisible.ConstructionManagement = false;
            this.repairApplyId = "";
            this.getRepairProjectList();
            this.$message({
              message: "施工日志新增成功!",
              type: "success"
            });
          })
          .catch(err => {
            const msg = err.result ? err.result : "施工日志新增失败";
            this.$message({
              message: msg,
              type: "error"
            });
          });
      }
      this.fileListCert = this.oldFileListCert;
      this.fileListRegCert = this.oldFileListRegCert;
      this.fileListImg = this.oldFileListImg;
      this.uploadCertNum = 0;
      this.uploadRegCertNum = 0;
      this.uploadSuccessCertNum = 0;
      this.uploadSuccessRegCertNum = 0;
      this.uploadImgNum = 0;
      this.uploadRegImgNum = 0;
      this.uploadSuccessImgNum = 0;
      this.uploadSuccessRegImgNum = 0;
      this.submitCertFlag = false;
      this.submitRegCertFlag = false;
      this.submitImgFlag = false;
      this.submitRegImgFlag = false;
      this.uploadCertChangeFlag = false;
      this.uploadRegCertChangeFlag = false;
      this.uploadImgChangeFlag = false;
      this.uploadRegImgChangeFlag = false;
    },
    saveForms() {
      this.editLogInfo.partsIdAndImgVoList.forEach(item => {
        item.repairPmImgList.forEach((items, i) => {
          if (items.repairProjectPartsId) {
            item.repairPmImgList.splice(i, 1);
          }
        });
      });
      let deleteRepairPmImgIdList = this.Forms.deleteEnclosureIdList;
      let repairProject = {};
      repairProject[
        "repairProjectConstruction"
      ] = this.editLogInfo.repairProjectConstruction;
      repairProject["repairProjectDate"] = this.editLogInfo.repairProjectDate;
      repairProject["repairProjectId"] = this.editLogInfo.repairProjectId;
      repairProject["repairProjectNum"] = this.editLogInfo.repairProjectNum;
      repairProject[
        "repairProjectProblems"
      ] = this.editLogInfo.repairProjectProblems;
      repairProject[
        "repairProjectSecurity"
      ] = this.editLogInfo.repairProjectSecurity;
      repairProject[
        "repairProjectUsername"
      ] = this.editLogInfo.repairProjectUsername;
      repairProject[
        "repairProjectUserid"
      ] = this.editLogInfo.repairProjectUserid;
      repairProject[
        "repairProjectWeather"
      ] = this.editLogInfo.repairProjectWeather;
      repairProject["repairProjectProgress"] = this.Forms.repairProjectProgress;
      let partsIdAndImgVoList = [];
      let repairPmImgList = [];
      updateRepairManage(
        deleteRepairPmImgIdList,
        this.editLogInfo.partsIdAndImgVoList,
        this.repairApplyId,
        repairProject
      )
        .then(res => {
          this.dialogVisible.ConstructionManagement = false;
          this.repairApplyId = "";
          this.getRepairProjectList();
          this.$message({
            message: "施工日志修改成功!",
            type: "success"
          });
        })
        .catch(err => {
          const msg = err.result ? err.result : "施工日志修改失败";
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 文件上传之前
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 200;
      if (!isLt2M) {
        this.$message.error("上传照片大小不能超过 200MB!");
      }
      return isLt2M;
    },

    // 开始上传文件
    submitUpload() {
      this.$refs.uploadCert.submit();
      this.$refs.upload.submit();
    },
    submitUploads() {
      this.$refs.uploadRegImg.submit();
      this.$refs.uploadRegCerts.submit();
    },
    // 删除上传文件时钩子
    handleCertRemove(file, fileList) {
      if (file.status === "success") {
        this.Form.deleteEnclosureIdList.push(file.uid);
        this.uploadedCertNum--;
      }
      this.uploadCertNum = fileList.length - this.uploadedCertNum;
      if (fileList.length === 0) {
        this.Form.auditing = "";
      }
    },
    handleRemove(file, fileList) {
      if (file.status === "success") {
        this.Form.deleteEnclosureIdList.push(file.name);
        this.uploadedImgNum--;
      }
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
      if (fileList.length == 0) {
        this.Form.drawing = "";
      }
    },
    handleShowRemove(file, fileList) {
      if (file.status === "success") {
        this.Forms.deleteEnclosureIdList.push(file.name);
        this.uploadedImgNum--;
      }
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
    },
    handleRegRemove(file, fileList) {
      if (file.status === "success") {
        this.forms.deleteEnclosureIdList.push(file.name);
        this.uploadedRegImgNum--;
      }
      this.uploadRegImgNum = fileList.length - this.uploadedRegImgNum;
    },
    handleRegCertRemove(file, fileList) {
      if (file.status === "success") {
        this.forms.deleteEnclosureIdList.push(file.uid);
        this.uploadedRegCertNum--;
      }
      this.uploadRegCertNum = fileList.length - this.uploadedRegCertNum;
      if (fileList.length == 0) {
        this.forms.uploadFile = "";
      }
    },
    // 上传文件成功时钩子
    handleCertSuccess(response, file, fileList) {
      this.uploadCertChangeFlag = true;
      this.uploadSuccessCertNum++;
      this.Form.addressList.push({
        enclosureType: 2, // 1图片 2文件
        physicalAddress: response.imgList[0].imgPath,
        virtualAddress: response.imgList[0].imgUrl,
        repairFileName: file.name
      });
      if (this.uploadSuccessCertNum === this.uploadCertNum) {
        this.submitCertFlag = true;
      }
      // if (
      //   (this.submitCertFlag &&
      //     this.uploadCertChangeFlag &&
      //     (this.submitImgFlag && this.uploadImgChangeFlag)) ||
      //   (this.submitCertFlag &&
      //     this.uploadCertChangeFlag &&
      //     !this.uploadImgChangeFlag)
      // ) {
      //   this.saveForm();
      // }
      if (this.submitCertFlag && this.submitImgFlag) {
        this.saveForm();
      }
      if (fileList.length > 0) {
        this.Form.auditing = "true";
      }
    },
    handleRegCertSuccess(response, file, fileList) {
      this.uploadSuccessRegCertNum++;
      this.forms.addressList.push({
        enclosureType: 2, // 1图片 2文件
        physicalAddress: response.imgList[0].imgPath,
        virtualAddress: response.imgList[0].imgUrl
      });
      if (this.uploadSuccessRegCertNum === this.uploadRegCertNum) {
        this.submitRegCertFlag = true;
      }
      // if (
      //   (this.submitFlag &&
      //     this.submitRegCertFlag &&
      //     this.uploadRegCertChangeFlag &&
      //     (this.submitRegImgFlag && this.uploadRegImgChangeFlag)) ||
      //   (this.submitRegCertFlag &&
      //     this.uploadRegCertChangeFlag &&
      //     !this.uploadRegImgChangeFlag)
      // ) {
      //   this.saveForm();
      // }
      if (this.submitRegImgFlag && this.submitRegCertFlag && this.submitFlag) {
        this.saveForm();
      }
    },
    handleSuccess(response, file, fileList) {
      this.uploadSuccessImgNum++;
      let arr = file.name.split(".");
      if (arr[arr.length - 1] == "rar" || arr[arr.length - 1] == "zip") {
        this.Form.addressList.push({
          enclosureType: 3, // 1图片 2文件 3压缩包
          physicalAddress: response.imgList[0].imgPath,
          virtualAddress: response.imgList[0].imgUrl,
          repairFileName: file.name
        });
      } else {
        this.Form.addressList.push({
          enclosureType: 4, // 1图片 2文件
          physicalAddress: response.imgList[0].imgPath,
          virtualAddress: response.imgList[0].imgUrl,
          repairFileName: file.name
        });
      }

      if (this.uploadSuccessImgNum === this.uploadImgNum) {
        // this.saveForm();
        this.submitImgFlag = true;
      }
      if (this.submitImgFlag && this.submitCertFlag) {
        this.saveForm();
      }
    },
    handleShowSuccess(response, file, fileList) {
      this.uploadSuccessImgNum++;
      let arr = file.name.split(".");
      if (arr[arr.length - 1] == "rar" || arr[arr.length - 1] == "zip") {
        this.Forms.addressList.push({
          enclosureType: 3, // 1图片 2文件 3压缩包
          physicalAddress: response.imgList[0].imgPath,
          virtualAddress: response.imgList[0].imgUrl,
          repairFileName: file.name
        });
      } else {
        this.Forms.addressList.push({
          enclosureType: 1, // 1图片 2文件
          physicalAddress: response.imgList[0].imgPath,
          virtualAddress: response.imgList[0].imgUrl,
          repairFileName: file.name
        });
      }
      // this.Forms.addressList.push({
      //   enclosureType: 1, // 1图片 2文件
      //   physicalAddress: response.imgList[0].imgPath,
      //   virtualAddress: response.imgList[0].imgUrl,
      //   repairFileName: file.name
      // });
      if (this.uploadSuccessImgNum === this.uploadImgNum) {
        this.saveRepairParts();
      }
      if (this.submitFlag) {
        this.saveForms();
      }
    },
    handleRegSuccess(response, file, fileList) {
      this.uploadSuccessRegImgNum++;
      let arr = file.name.split(".");
      if (arr[arr.length - 1] == "rar" || arr[arr.length - 1] == "zip") {
        this.forms.addressList.push({
          enclosureType: 3, // 1图片 2文件 3压缩包
          physicalAddress: response.imgList[0].imgPath,
          virtualAddress: response.imgList[0].imgUrl,
          repairFileName: file.name
        });
      } else {
        this.forms.addressList.push({
          enclosureType: 1, // 1图片 2文件
          physicalAddress: response.imgList[0].imgPath,
          virtualAddress: response.imgList[0].imgUrl,
          repairFileName: file.name
        });
      }
      if (this.uploadSuccessRegImgNum === this.uploadRegImgNum) {
        this.submitRegImgFlag = true;
        if (!this.submitFlag) {
          this.saveRepairPart();
        }
      }
      // if(this.submitFlag){
      //   if (
      //   (
      //     this.submitRegCertFlag &&
      //     this.uploadRegCertChangeFlag &&
      //     (this.submitRegImgFlag && this.uploadRegImgChangeFlag)) ||
      //   (!this.uploadRegCertChangeFlag &&
      //     this.submitRegImgFlag &&
      //     this.uploadRegImgChangeFlag)
      // ) {
      //   this.saveForm();
      // }
      // }
      if (this.submitRegImgFlag && this.submitRegCertFlag && this.submitFlag) {
        this.saveForm();
      }
    },
    // 上传文件改变时钩子
    handleCertChange(file, fileList) {
      const fileType = file.raw.type.split("/")[0];
      if (fileType !== "image") {
        file.url = `${BASE_URL}${url_name_1}/images/compress.png`;
      }
      this.uploadCertNum = fileList.length - this.uploadedCertNum;
      if (fileList.length > 0) {
        this.Form.auditing = "true";
      }
    },
    handleChange(file, fileList) {
      const fileType = file.raw.type.split("/")[0];
      if (fileType !== "image") {
        file.url = `${BASE_URL}${url_name_1}/images/compress.png`;
      }
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
      if (fileList.length > 0) {
        this.Form.drawing = "true";
      }
    },
    handleShowChange(file, fileList) {
      const fileType = file.raw.type.split("/")[0];
      if (fileType !== "image") {
        file.url = `${BASE_URL}${url_name_1}/images/compress.png`;
      }
      this.uploadImgNum = fileList.length - this.uploadedImgNum;
    },
    handleRegChange(file, fileList) {
      const fileType = file.raw.type.split("/")[0];
      if (fileType !== "image") {
        file.url = `${BASE_URL}${url_name_1}/images/compress.png`;
      }
      this.uploadRegImgChangeFlag = true;
      this.uploadRegImgNum = fileList.length - this.uploadedRegImgNum;
    },
    handleRegCertChange(file, fileList) {
      const fileType = file.raw.type.split("/")[0];
      if (fileType !== "image") {
        file.url = `${BASE_URL}${url_name_1}/images/compress.png`;
      }
      this.uploadRegCertChangeFlag = true;
      this.uploadRegCertNum = fileList.length - this.uploadedRegCertNum;
      if (fileList.length > 0) {
        this.forms.uploadFile = "true";
      }
    },
    // 上传文件超出限制个数
    handleCertExceed(file, fileList) {
      this.$message.warning(`最多只能上传五份文件！`);
    },
    handleExceed(file, fileList) {
      this.$message.warning(`最多只能上传五张图片！`);
    },
    handleRegCertExceed(file, fileList) {
      this.$message.warning(`最多只能上传一份文件！`);
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
    handleRegError(err, file, fileList) {
      if (err.status === 401) {
        this.$store.dispatch("Logout", this.token);
        this.$router.push({ path: "/login" });
      }
      this.$message({
        message: err.message,
        type: "error"
      });
    },
    step(repairPaymentId, state, index) {
      let repairPaymentIdList = [];
      if (state == 1) {
        this.RepairPaymentDetail.forEach((item, indexs) => {
          if (item.repairPaymentState == 1 && indexs <= index) {
            repairPaymentIdList.push(item.repairPaymentId);
          }
        });
        this.$confirm("确认付款, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            updateState(repairPaymentIdList, this.repairApplyId)
              .then(res => {
                this.dialogVisible.payProcess = false;
                this.$message({
                  message: "状态修改成功!",
                  type: "success"
                });
              })
              .catch(err => {
                const msg = err.result ? err.result : "发起付款失败";
                this.$message({
                  message: msg,
                  type: "error"
                });
              });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消"
            });
          });
      }
    }
  },
  mounted() {
    setTimeout(() => {
      let mainDivH = $("#mainDiv").height();
      let queryBoxH = $("#queryBox").outerHeight(true);
      let operateBoxH = $("#operateBox").height();
      this.tableHeight = mainDivH - queryBoxH - operateBoxH;
    }, 1);
    this.token = sessionStorage.getItem("TOKEN");
    if (this.token) {
      this.postHeaders.Authorization = `Bearer ${this.token}`;
    }

    this.pickerOptions_start.disabledDate = (time) => {
      if (this.endDate) {
        return new Date(this.endDate).getTime() < time.getTime();
      }
    }

    this.pickerOptions_end.disabledDate = (time) => {
      if (this.beginDate) {
        return new Date(this.beginDate).getTime() > time.getTime();
      }
    }
  }
};
</script>
<style>
#repairProjectManage .query-box {
  margin-bottom: 20px;
}
#repairProjectManage .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#repairProjectManage .query-item > div {
  float: left;
}
#repairProjectManage .query-item > .query-input {
  width: calc(100% - 80px);
}
#repairProjectManage .query-item .el-select {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
/* #repairProjectManage .el-input {
  width: 250%;
} */
#repairProjectManage .query-item .el-date-editor.el-input,
#repairProjectManage .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#repairProjectManage .query-btn {
  float: right;
}
#repairProjectManage .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#repairProjectManage .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#repairProjectManage .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#repairProjectManage .repair-info-box,
#repairProjectManage .repair-records-box {
  padding: 25px 48px 15px;
  padding-right: 25px;
  /* border-bottom: 1px solid #d8d8d8; */
}
#repairProjectManage .repair-info-base > div {
  float: left;
  font-size: 14px;
  line-height: 36px;
  width: 300px;
}
#repairProjectManage .repair-info-detail .info-detail-title {
  width: 100%;
  border-bottom: 1px solid #d8d8d8;
}
#repairProjectManage .info-detail-title > span {
  font-size: 16px;
  line-height: 36px;
  float: left;
  border-bottom: 1px solid #498fea;
  position: relative;
  top: 1px;
  /* width: 100% */
}
#repairProjectManage .workDaily.info-detail-title > span:nth-of-type(2),
#repairProjectManage .workDaily.info-detail-title > span:nth-of-type(3) {
  float: right;
  margin-right: 50px;
  border-bottom: none;
  font-size: 14px;
  color: #ccc;
}
#repairProjectManage .info-detail-title > .info-detail-btn {
  float: right;
}
#repairProjectManage .info-detail-title > .info-detail-btn .el-dropdown {
  line-height: 36px;
  font-size: 16px;
}
#repairProjectManage .info-detail-btn > span {
  display: inline-block;
  line-height: 36px;
  font-size: 16px;
}
#repairProjectManage .info-detail-btn > span + span {
  margin-left: 20px;
}
#repairProjectManage .delete-btn {
  color: #f65c5f;
  cursor: pointer;
}
#repairProjectManage .delete-btn:hover {
  color: #d82a2d;
}
#repairProjectManage .more-btn {
  color: #3891e0;
  cursor: pointer;
  vertical-align: middle;
  font-size: 16px;
}
#repairProjectManage .edit-btn {
  color: #3891e0;
  cursor: pointer;
  vertical-align: middle;
}
#repairProjectManage .edit-btn:hover,
#repairProjectManage .more-btn:hover {
  color: #035cac;
  font-size: 16px;
}
#repairProjectManage .edit-btn > i {
  font-size: 18px;
  position: relative;
  top: 2px;
}
#repairProjectManage .el-dialog--center .el-dialog__body {
  padding: 0;
}
#repairProjectManage .section2-content {
  padding-left: 30px;
}
#repairProjectManage .section2-content > div {
  height: 40px;
  line-height: 40px;
}
#repairProjectManage .section2-content .el-form-item__content {
  width: 70%;
}
#repairProjectManage
  .section2-content.add
  .el-form-item:nth-of-type(3)
  .el-form-item__content,
#repairProjectManage
  .section2-content.add
  .el-form-item:nth-of-type(4)
  .el-form-item__content,
#repairProjectManage
  .section2-content.add
  .el-form-item:nth-of-type(5)
  .el-form-item__content,
#repairProjectManage
  .section2-content.add
  .el-form-item:nth-of-type(6)
  .el-form-item__content,
#repairProjectManage
  .section2-content.add
  .el-form-item:nth-of-type(7)
  .el-form-item__content,
#repairProjectManage
  .section2-content.add
  .el-form-item:nth-of-type(8)
  .el-form-item__content,
#repairProjectManage
  .section2-content.add
  .el-form-item:nth-of-type(9)
  .el-form-item__content,
#repairProjectManage
  .section2-content.add
  .el-form-item:nth-of-type(10)
  .el-form-item__content,
#repairProjectManage
  .section2-content.add
  .el-form-item:nth-of-type(12)
  .el-form-item__content,
#repairProjectManage
  .section2-content.add
  .el-form-item:nth-of-type(13)
  .el-form-item__content,
#repairProjectManage
  .section2-content.add
  .el-form-item:nth-of-type(14)
  .el-form-item__content {
  width: 84%;
}
#repairProjectManage
  .section2-content
  .el-form-item:nth-of-type(2)
  .el-form-item__content {
  width: 90%;
  padding-top: 10px;
}
#repairProjectManage .section2-content .el-textarea,
#repairProjectManage .section2-content .upload_box {
  margin-top: 4px;
}
#repairProjectManage .More-Info {
  background: #f5f5f5;
  padding: 0;
}
#repairProjectManage .repair-info-more > li {
  line-height: 30px;
  padding: 0 10px;
  cursor: pointer;
}
#repairProjectManage .repair-info-more > li:hover {
  background: #498fea;
  color: #fff;
}
#repairProjectManage .arrow .iconfont:active {
  transform: rotate(-90deg);
}
#repairProjectManage .detail-form-item .el-input__inner {
  border: 0;
  padding-left: 0;
  width: 215px;
  margin-top: 6px;
}
#repairProjectManage .detail-form-item .el-textarea__inner {
  border: 0;
  padding-left: 0;
  width: 550px;
  margin-top: 1px;
}
#repairProjectManage .el-form-item {
  width: 100%;
  margin-right: 10px;
  margin-bottom: 20px;
}
#repairProjectManage .add .el-form-item {
  margin-right: 10px;
}
#repairProjectManage .el-form-item:nth-of-type(1) {
  width: 33.33333%;
}
#repairProjectManage .completeForm .el-form-item:nth-of-type(1) {
  width: 100%;
}
#repairProjectManage .completeForm .el-date-editor.el-input {
  width: 200px;
}
#repairProjectManage .completeForm .el-date-editor.el-input .el-input__inner {
  height: 32px;
  line-height: 32px;
}
#repairProjectManage .add .el-form-item:nth-of-type(1) {
  width: 30%;
}
#repairProjectManage .repair-records-box .el-form-item .el-form-item__label {
  font-size: 12px;
}
#repairProjectManage .section2-content.add .el-form-item__label {
  text-align-last: justify;
}
#repairProjectManage .section2-content.add .el-form-item .el-input__inner {
  width: 100%;
}
#repairProjectManage .dialog-footer .el-input {
  width: 150%;
}
#repairProjectManage .dialog-footer #queryBox .el-upload {
  display: inherit;
  text-align: left;
}
#repairProjectManage .upload {
  width: 100%;
  height: inherit;
  margin-top: 10px;
}
#repairProjectManage .img-preview-item {
  float: left;
  margin-right: 20px;
  margin-bottom: 5px;
  width: 100px;
  height: 100px;
  border: 1px dashed #c0ccda;
  position: relative;
}
#repairProjectManage .img-preview-item > img {
  width: 100%;
  height: 100%;
}
#repairProjectManage .img-preview-item > .bigger-view-warpper {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}
#repairProjectManage
  .img-preview-item
  > .bigger-view-warpper
  > .bigger-view-warpper-inner {
  width: 100%;
  height: 100%;
  background: #000;
  opacity: 0.5;
  display: none;
}
#repairProjectManage
  .img-preview-item
  > .bigger-view-warpper:hover
  > .bigger-view-warpper-inner {
  display: block;
}
#repairProjectManage .img-preview-item > .bigger-view-warpper > i {
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
#repairProjectManage .img-preview-item > .bigger-view-warpper > i:hover {
  color: #3891e0;
}
#repairProjectManage .img-preview-item > .bigger-view-warpper:hover > i {
  display: block;
}
#repairProjectManage .img-preview-item > .el-checkbox {
  position: absolute;
  bottom: -13px;
  right: 0;
}
#repairProjectManage .download-btn {
  padding: 7px 22px;
}
#repairProjectManage .el-dialog__footer {
  padding-left: 15px;
  margin-top: -1px;
  padding-right: 0;
  /* border-top: 1px solid #d8d8d8; */
}
#repairProjectManage .el-dialog__footer .el-form-item {
  text-align: left;
}
#repairProjectManage .el-dialog__footer .el-input--small {
  width: 100%;
}
#repairProjectManage .info-detail-btn .Time {
  font-size: 12px;
  color: #ccc;
}
#repairProjectManage .el-collapse {
  margin-top: -1px;
  padding: 25px 48px 30px;
  padding-right: 25px;
  border-top: none;
  border-bottom: none;
}
#repairProjectManage .el-collapse-item > div:first-child {
  border-bottom: 1px solid #d8d8d8;
}
#repairProjectManage .el-collapse .el-collapse-item__header {
  border-bottom: none;
  height: 37px;
  line-break: 36px;
}
#repairProjectManage .el-collapse-item .info-detail-title {
  width: 100%;
}
#repairProjectManage .el-collapse .el-collapse-item .el-collapse-item__wrap {
  border-bottom: none;
  overflow: visible;
}

#repairProjectManage .el-collapse-item__content .el-form-item {
  line-height: 40px;
}
#repairProjectManage .el-collapse-item__content .el-form-item:nth-of-type(1) {
  width: 40%;
}
#repairProjectManage
  .el-collapse-item__content
  .el-form-item:nth-of-type(3)
  .el-form-item__content,
#repairProjectManage
  .el-collapse-item__content
  .el-form-item:nth-of-type(4)
  .el-form-item__content {
  width: 70%;
}
#repairProjectManage
  .el-collapse-item__content
  .el-form-item:nth-of-type(4)
  .el-form-item__content
  .el-input__inner,
#repairProjectManage
  .el-collapse-item__content
  .el-form-item:nth-of-type(3)
  .el-form-item__content
  .el-input__inner,
#repairProjectManage
  .el-collapse-item__content
  .el-form-item:nth-of-type(1)
  .el-form-item__content
  .el-input__inner {
  width: 100%;
}
#repairProjectManage .move .el-dialog__body {
  padding: 50px;
}
#repairProjectManage .payProcess .el-dialog__body {
  padding: 100px 0;
}
#repairProjectManage .move .el-dialog__body .el-form-item {
  width: 100%;
  margin-bottom: 30px;
}
#repairProjectManage .add .el-select .el-input__inner {
  height: 32px !important;
}
#repairProjectManage .add .el-select .el-select__tags {
  margin-top: 4px;
}
#repairProjectManage .add .addRepairPart {
  text-align: right;
  width: 100%;
  /* margin-right: 20px; */
}
#repairProjectManage .add .addRepairPart span {
  display: inline-block;
  margin-right: 20px;
  color: #3891e0;
  cursor: pointer;
  font-size: 16px;
}
/* #repairProjectManage .el-dialog__footer .el-form-item__content{
padding-top: 12px;
} */
#repairProjectManage
  .section2-content
  .el-form-item__content
  .el-input--suffix
  input {
  height: 32px !important;
}
#repairProjectManage .completeForm {
  padding-left: 50px;
  padding-top: 10px;
}
#repairProjectManage .completeForm .el-form-item {
  padding-right: 0px;
  margin-right: 0;
}
#repairProjectManage
  .completeForm
  .el-form-item:last-child
  .el-form-item__content
  .el-input--small {
  width: 200%;
  padding-top: 5px;
}
/* #repairProjectManage .el-form-item:nth-of-type(2) .el-form-item__content {
  padding-top: 10px;
} */
#repairProjectManage .upload_box .el-upload--picture-card {
  width: 100px;
  height: 100px;
  line-height: 100px;
}
#repairProjectManage
  .upload_box
  .el-upload-list--picture-card
  .el-upload-list__item {
  width: 100px;
  height: 100px;
}
#repairProjectManage .el-progress-bar__innerText {
  transform: scale(0.9);
  margin-top: -5px;
}
#repairProjectManage .repaire-file-download-box {
  padding-left: 30px;
  float: left;
  width: 100%;
}
#repairProjectManage .repaire-file-download-box > div {
  float: left;
}
#repairProjectManage .repaire-file-download-title {
  line-height: 40px;
  width: 56px;
  text-align: justify;
  text-justify: distribute-all-lines;
  text-align-last: justify;
}
#repairProjectManage .repaire-file-download {
  margin-left: 5px;
}
#repairProjectManage .info-item-inline > .info-item {
  float: left;
  width: 33%;
}
#repairProjectManage .repaire-file-download .img-view-item {
  width: 100px;
  height: 100px;
  margin-right: 20px;
  float: left;
  position: relative;
  margin-bottom: 15px;
  border: 1px dashed #ccc;
}
#repairProjectManage .repaire-file-download .img-view-item > img {
  width: 100%;
  height: 100%;
  position: relative;
}
#repairProjectManage
  .repaire-file-download
  .img-view-item
  .bigger-view-warpper {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0px;
  left: 0;
}
#repairProjectManage
  .repaire-file-download
  .bigger-view-warpper
  > .bigger-view-warpper-inner {
  width: 100%;
  height: 100%;
  background: #000;
  opacity: 0.5;
  display: none;
}
#repairProjectManage
  .repaire-file-download
  .bigger-view-warpper:hover
  > .bigger-view-warpper-inner {
  display: block;
}
#repairProjectManage .repaire-file-download .bigger-view-warpper > i {
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
#repairProjectManage .repaire-file-download .bigger-view-warpper > i:hover {
  color: #3891e0;
}
#repairProjectManage .repaire-file-download .bigger-view-warpper:hover > i {
  display: block;
}
#repairProjectManage .repaire-file-download > .img-download-btn {
  float: left;
  width: 100%;
}
#repairProjectManage .noInfo {
  text-align: center;
}
#repairProjectManage .repaire-file-download-box {
  padding-left: 0;
  float: inherit;
}
#repairProjectManage .repaire-file-download-item {
  line-height: 40px;
}
#repairProjectManage .img-view-item > .el-checkbox {
  position: absolute;
  bottom: -13px;
  right: -10px;
}
#repairProjectManage .hidden {
  visibility: hidden;
}
#repairProjectManage .repairePart .el-form-item__content > .el-input {
  width: 88%;
  position: absolute;
  top: 1px;
}
#repairProjectManage
  .repairePart
  .el-form-item__content
  > .el-input
  .el-input__inner {
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
  border-right: none;
}
#repairProjectManage .repairePart .el-input .el-input__inner:focus,
#repairProjectManage .repairePart .el-select .el-input__inner:focus {
  border-color: #dcdfe6;
}

#repairProjectManage .Title {
  line-height: 36px;
  height: 36px;
  font-size: 16px;
  text-align: left;
  border-bottom: 1px solid #d8d8d8;
  margin-left: 30px;
}
#repairProjectManage .Title div {
  width: 80px;
  border-bottom: 1px solid #498fea;
}
.el-select-dropdown.el-popper.repairPart {
  width: 217px !important;
  min-width: 0 !important;
}
.el-select-dropdown.el-popper.repairPart .el-select-dropdown__item.is-disabled {
  padding: 0;
}
#repairProjectManage .constructLog {
  border-bottom: 1px solid #d8d8d8;
  height: 37px;
  line-height: 37px;
  font-size: 16px;
  margin-left: 48px;
}
#repairProjectManage .constructLog div {
  width: 80px;
  border-bottom: 1px solid #498fea;
}
.el-popper.More-Info {
  padding: 0 !important;
}
.el-popper.More-Info .repairProject-info-more {
  margin-top: -2px;
}
.el-popper.More-Info .repairProject-info-more li {
  line-height: 30px;
  padding: 0 10px;
  font-size: 13px;
}
.el-popper.More-Info .repairProject-info-more .info-active {
  background-color: #498fea;
  color: #fff;
}
</style>

