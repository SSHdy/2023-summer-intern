<template>
  <div id="baseInfoYard">
    <div class="query-box" id="queryBox">
      <div class="clearfix">
        <div class="query-item">
          <div class="query-title">院落名称：</div>
          <div class="query-input">
            <el-select filterable v-model="yard_id" placeholder="请选择" style="width:100%">
              <el-option
                v-for="item in yardOptions"
                :key="item.yardId"
                :label="item.yardName"
                :value="item.yardId"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="query-item" style="margin-left:50px;">
          <div class="query-title">类型：</div>
          <div class="query-input">
            <el-select v-model="type" placeholder="请选择" style="width:100%">
              <el-option
                v-for="item in typeOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
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
      <table-component :tableData="tableData" :tableHeight="tableHeight">
        <el-table-column
          :show-overflow-tooltip="true"
          prop="serialNum"
          label="序号"
          width="50"
          align="center"
        ></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="yardName" label="院落名称" align="center"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="regionName"
          label="行政区划"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="yardPosition"
          label="院落地址"
          :show-overflow-tooltip="true"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="yardLongitude"
          label="经度(°)"
          align="center"
        ></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="yardLatitude"
          label="纬度(°)"
          align="center"
        ></el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="typeName" label="类型" align="center"></el-table-column>
        <el-table-column label="操作" align="center" width="140">
          <template slot-scope="scope">
            <single-btn-group
              :isShowDetail="false"
              @deleteClick="singleDetle(scope.row)"
              @updateClick="singleUpdate(scope.row)"
            ></single-btn-group>
          </template>
        </el-table-column>
      </table-component>
      <div class="table-operate-box" id="operateBox">
        <div class="operate-btn">
          <table-btn-group :batchDelBtnShow="false" @addClick="add"></table-btn-group>
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
        <el-form-item label="院落名称" label-width="120px" prop="yard.yardName">
          <el-input v-model.trim="Form.yard.yardName" size="small" placeholder="请输入院落名称"></el-input>
        </el-form-item>
        <el-form-item label="行政区划" label-width="120px" prop="yard.regionId">
          <el-select
            filterable
            v-model="Form.yard.regionId"
            placeholder="请选择"
            size="small"
            style="width:200px"
          >
            <el-option
              v-for="item in administrativeRegion"
              :key="item.regionId"
              :label="item.regionName"
              :value="item.regionId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="院落地址" label-width="120px" prop="yard.yardPosition">
          <el-input
            v-model.trim="Form.yard.yardPosition"
            size="small"
            placeholder="请输入院落地址"
          ></el-input>
        </el-form-item>
        <el-form-item label="经度(°)" label-width="120px" prop="yard.yardLongitude">
          <el-input
            v-model.trim="Form.yard.yardLongitude"
            size="small"
            @blur="mapBindCoord('yardLongitude', 'yard', true)"
            @keyup.native="limitFloat('yardLongitude', 'yard')"
            placeholder="请输入经度"
          ></el-input>
        </el-form-item>
        <el-form-item label="纬度(°)" label-width="120px" prop="yard.yardLatitude">
          <el-input
            v-model.trim="Form.yard.yardLatitude"
            size="small"
            @blur="mapBindCoord('yardLatitude', 'yard', true)"
            @keyup.native="limitFloat('yardLatitude', 'yard')"
            placeholder="请输入纬度"
          ></el-input>
        </el-form-item>
        <el-form-item label="类型" label-width="120px" prop="yard.type">
          <el-radio v-model="Form.yard.type" :label="1" @change="changeType">院落</el-radio>
          <el-radio v-model="Form.yard.type" :label="2" @change="changeType">土地</el-radio>
        </el-form-item>
        <el-form-item
          label="添加物业"
          label-width="120px"
          class="estate-add-btn-box"
          prop="yardAndestateVoList"
          v-if="Form.yard.type===1"
        >
          <el-button @click.stop="addEstate" icon="el-icon-plus" class="estate-add-btn">添加</el-button>
        </el-form-item>
      </el-form>
      <div class="estate-table-box" v-if="Form.yard.type===1">
        <div
          class="tips-box"
          v-if="!estateNameFlag || !serviceContentFlag || !startTimeFlag || !startTimeLimitFlag || !endTimeFlag || !endTimeLimitFlag || !contractAmountFlag"
        >提示：{{submitTipsArray.join(',')}}</div>
        <table-component
          :tableData="Form.yardAndestateVoList"
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
                @change="changeEstateName(`${scope.row.estateId},${scope.$index}`)"
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
                value-format="yyyy-MM-dd HH:mm:ss"
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
              <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </table-component>
      </div>
      <div class="map-tips">提示：可手动输入经纬度,也可以点击下方地图自动生成经纬度。</div>
      <div class="my-map">
        <my-map
          @mapClick="setPostion"
          :labelContent="'所选位置'"
          :position="position"
          v-if="showMap"
          :operateType="mapOperateType"
        ></my-map>
      </div>
      <span slot="footer" class="dialog-footer">
        <cancel-btn @clickBtn="dialogVisible = false" :fontSize="14"></cancel-btn>
        <confirm-btn @clickBtn="submitForm('Form')" :fontSize="14"></confirm-btn>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { eventBus } from "@/eventBus/eventBus";
import moment from "moment";
import {
  getYardInfoList,
  deleteYard,
  batchDeleteYard,
  addYard,
  updateYard,
  getEstateNameList,
  getSelectEstateData,
  yardNameUnique,
  isHaveRent,
  isHaveBuild
} from "./api";
import { getAllYardList, getRegionList } from "@/axios/commonApi";
import tableComponent from "@/components/table/table2";
import tablePagination from "@/components/tableGroup/tablePagination";
import singleBtnGroup from "@/components/tableGroup/singleBtnGroup";
import tableBtnGroup from "@/components/tableGroup/tableBtnGroup";
import cancelBtn from "@/components/button/cancelBtn";
import confirmBtn from "@/components/button/confirmBtn";
import searchBtn from "@/components/button/searchBtn";
import resetBtn from "@/components/button/resetBtn";
import myMap from "@/components/map/map";
export default {
  components: {
    tableComponent,
    tablePagination,
    singleBtnGroup,
    tableBtnGroup,
    cancelBtn,
    confirmBtn,
    searchBtn,
    resetBtn,
    myMap
  },
  data() {
    const validator_yardName = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error('院落名称不能为空'));
      } else {
        yardNameUnique(this.Form.yard.yardName, this.Form.yard.yardId).then(res => {
          callback();
        }).catch(err => {
          callback(new Error('院落名称不能重复'));
        })
      }
    };
    return {
      currentPage: 1,
      pageSize: 15,
      total: 0,
      loading: null,
      tableHeight: 0,
      tableData: [],
      estateTableData: [],
      dialogTitle: "",
      dialogVisible: false,
      operateType: "add",
      mapOperateType: "add",
      yard_id: "",
      type: "",
      Form: {
        yard: {
          regionId: '',
          yardAdministrativeRegion: '',
          yardId: null,
          yardLatitude: null,
          yardLongitude: null,
          yardName: '',
          yardPosition: '',
          type: 1
        },
        yardAndestateVoList: [],
        delYardEstateIdList: []
      },
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
      typeOptions: [
        {
          id: 1,
          name: '院落'
        },
        {
          id: 2,
          name: '土地'
        }
      ],
      estateNameOpts: [],
      showMap: false,
      position: {
        lng: 0,
        lat: 0
      },
      yardOptions: [],
      administrativeRegion: [],
      originalType: null,
      rules: {
        'yard.yardName': [
          { required: true, validator: validator_yardName, trigger: "blur" }
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
        ],
        'yard.type': [
          { required: true, message: "请先选择类型", trigger: "blur" }
        ]
      }
    };
  },
  watch: {
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
    // 物业名称重复提示
    changeEstateName(info) {
      const id = info.split(',')[0] - 0
      const index = info.split(',')[1] - 0
      for (let i = 0; i < this.Form.yardAndestateVoList.length; i++) {
        if (id === this.Form.yardAndestateVoList[i].estateId && i !== index) {
          this.$message({
            message: '该物业已存在！',
            type: "warning"
          })
          this.Form.yardAndestateVoList[index].estateId = ''
          return;
        }
      }
    },
    changeType(type) {
      if (this.operateType === 'edit') {
        if (type === 2 && this.originalType === 1) {
          isHaveBuild(this.Form.yard.yardId).then(res => {
          }).catch(err => {
            this.Form.yard.type = this.originalType;
            const msg = err.result ? err.result : '该院落下存在楼座信息，无法修改类型！';
            this.$message({
              message: msg,
              type: "error"
            });
            return;
          })
        }
        if (type !== this.originalType) {
          isHaveRent(this.Form.yard.yardId).then(res => {
          }).catch(err => {
            this.Form.yard.type = this.originalType;
            let tips = '';
            if (this.originalType === 1) {
              tips = '该院落下关联出租出借，无法修改类型！';
            } else {
              tips = '该土地下关联出租出借，无法修改类型！';
            }
            const msg = err.result ? err.result : tips;
            this.$message({
              message: msg,
              type: "error"
            });
          })
        }
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
    // 获取信息列表
    getList() {
      this.loadingStart('.table-box');
      getYardInfoList(this.currentPage, this.pageSize, this.yard_id, this.type)
        .then(res => {
          this.tableData = res.records;
          this.total = res.total;
          this.tableData.forEach((item, index) => {
            item.serialNum = index + 1;
            switch (item.type) {
              case 1:
                item.typeName = '院落';
                break;
              case 2:
                item.typeName = '土地';
                break;
            }
          })
          this.loadingEnd();
        })
        .catch(err => {
          this.loadingEnd();
          const msg = err.result ? err.result : '院落信息获取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
    },
    // 查询列表信息
    search() {
      this.currentPage = 1;
      this.getList();
    },
    // 重置查询
    reset() {
      this.type = '';
      this.yard_id = '';
      this.currentPage = 1;
      this.getList();
    },
    // 获取院落字典项
    getYardDic() {
      getAllYardList(null)
        .then(res => {
          this.yardOptions = res;
        })
        .catch(err => {
          const msg = err.result ? err.result : '树获院落名称下拉菜单获取失败取失败';
          this.$message({
            message: msg,
            type: "error"
          });
        });
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
    // 获取行政区划列表
    getRegionList() {
      getRegionList().then(res => {
        this.administrativeRegion = res;
      }).catch(err => {
        const msg = err.result ? err.result : '行政区划列表获取失败';
        this.$message({
          message: msg,
          type: "error"
        });
      })
    },
    // 新增
    add() {
      this.position.lng = 0;
      this.position.lat = 0;
      this.dialogTitle = "新增院落或土地信息";
      this.operateType = "add";
      this.mapOperateType = "add";
      this.showMap = true; // 重新渲染地图
      this.dialogVisible = true;
    },
    // 单个删除
    singleDetle(row) {
      let typeName = '';
      if (row.type === 1) {
        typeName = '院落';
      } else {
        typeName = '土地';
      }
      this.$confirm(`此操作将永久删除${typeName}${row.yardName}，是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteYard(parseInt(row.yardId))
            .then(res => {
              this.$message({
                message: `${typeName}信息删除成功`,
                type: "success"
              });
              this.currentPage = 1;
              this.getList();
              this.getYardDic();
            })
            .catch(err => {
              const msg = err.result ? err.result : `${typeName}信息删除失败`;
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
      if (row.type === 1) {
        typeName = '院落';
      } else {
        typeName = '土地';
      }
      this.originalType = row.type;
      this.dialogTitle = `修改${typeName}信息`;
      this.operateType = "edit";
      this.mapOperateType = "edit";
      this.showMap = true; // 重新渲染地图
      this.position.lng = row.yardLongitude;
      this.position.lat = row.yardLatitude;
      this.dialogVisible = true;
      let typeName = '';
      for ([key, value] of Object.entries(row)) {
        this.Form.yard[key] = value;
      }
      getSelectEstateData(row.yardId).then(res => {
        if (res && res.yardAndestateVoList && res.yardAndestateVoList.length > 0) {
          this.Form.yardAndestateVoList = res.yardAndestateVoList
        }
      }).catch(err => {
        const msg = err.result ? err.result : '物业信息获取失败';
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
          // 验证判断
          let typeName = '';
          if (this.Form.yard.type === 1) {
            this.estateNameSelectChange();
            this.serviceContentSelectChange();
            this.startTimeBlur();
            this.endTimeBlur();
            this.contractAmountBlur();
            // 验证
            if (!this.estateNameFlag || !this.serviceContentFlag || !this.startTimeFlag || !this.startTimeLimitFlag || !this.endTimeFlag || !this.endTimeLimitFlag || !this.contractAmountFlag) {
              return false;
            };
            this.Form.yardAndestateVoList.forEach(item => {
              item.endTime = moment(item.endTime).format('YYYY-MM-DD 23:59:59')
              this.estateNameOpts.forEach(opt => {
                if (opt.estateId === item.estateId) {
                  item.estateName = opt.estateName;
                }
              })
            })
            typeName = '院落';
          } else {
            typeName = '土地';
          }
          for (let i = 0; i < this.administrativeRegion.length; i++) {
            if (this.administrativeRegion[i].regionId === this.Form.yard.regionId) {
              this.Form.yard.yardAdministrativeRegion = this.administrativeRegion[i].regionName;
              break;
            }
          }
          let msg = this.operateType === "add" ? "新增" : "修改";
          this.Form.yard.yardLongitude = this.Form.yard.yardLongitude - 0;
          this.Form.yard.yardLatitude = this.Form.yard.yardLatitude - 0;
          if (this.operateType === "add") {
            addYard(this.Form).then(res => {
              this.$message({
                message: `${typeName}新增成功`,
                type: "success"
              });
              this.dialogVisible = false;
              this.getList();
              this.getYardDic();
            }).catch(err => {
              const msg = err.result ? err.result : `${typeName}新增失败`;
              this.$message({
                message: msg,
                type: "error"
              });
            })
          } else if (this.operateType === "edit") {
            updateYard(this.Form).then(res => {
              this.$message({
                message: `${typeName}修改成功`,
                type: "success"
              });
              this.dialogVisible = false;
              this.getList();
              this.getYardDic();
            }).catch(err => {
              const msg = err.result ? err.result : `${typeName}修改失败`;
              this.$message({
                message: msg,
                type: "error"
              });
            })
          }
        } else {
          return false;
        }
      });
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
      this.Form.yardAndestateVoList.push(obj);
    },
    // 删除一条物业信息
    handleDelete(index, row) {
      this.Form.yardAndestateVoList.splice(index, 1);
      this.Form.delYardEstateIdList.push(row.yardEstateId);
    },
    // dialog打开
    dialogOpen() {
      this.$nextTick(() => {
        this.$refs["Form"].clearValidate();
      })
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
    // dialog关闭的回调
    dialogClosed() {
      this.showMap = false;
      this.Form.yardAndestateVoList = [];
      this.clearForm(this.Form);
      this.estateNameFlag = true;
      this.serviceContentFlag = true;
      this.startTimeFlag = true;
      this.startTimeLimitFlag = true;
      this.endTimeFlag = true;
      this.endTimeLimitFlag = true;
      this.contractAmountFlag = true;
      this.submitTipsArray = [];
      this.Form.yard.type = 1;
      this.originalType = null;
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
    // 只能输入正浮点数
    limitFloat(key, type, flag) {
      if (this.Form[type][key]) {
        this.Form[type][key] = this.Form[type][key].replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^\./g, '');
      }
      if (flag && this.Form[type][key]) {
        this.Form[type][key] = (this.Form[type][key] - 0).toFixed(6);
      }
    },
    // 设置经纬度
    setPostion(lng, lat) {
      this.Form.yard.yardLongitude = lng;
      this.Form.yard.yardLatitude = lat;
    },
    // 地图绑定坐标位置
    mapBindCoord(key, type, flag) {
      this.limitFloat(key, type, flag);
      const yardLongitude = this.Form.yard.yardLongitude;
      const yardLatitude = this.Form.yard.yardLatitude;
      if (yardLongitude !== '' && yardLongitude !== null && yardLatitude !== '' && yardLatitude !== null) {
        eventBus.$emit("mapBindCoord", yardLongitude, yardLatitude);
      }
    },
    // 验证判断
    estateNameSelectChange() {
      const estateNum = this.Form.yardAndestateVoList.length;
      if (estateNum > 0) {
        let num = 0;
        this.estateNameFlag = false;
        for (let i = 0; i < estateNum; i++) {
          const item = this.Form.yardAndestateVoList[i];
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
      const estateNum = this.Form.yardAndestateVoList.length;
      if (estateNum > 0) {
        let num = 0;
        this.serviceContentFlag = false;
        for (let i = 0; i < estateNum; i++) {
          const item = this.Form.yardAndestateVoList[i];
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
      const estateNum = this.Form.yardAndestateVoList.length;
      if (estateNum > 0) {
        let num = 0;
        let timeNum = 0;
        this.startTimeFlag = false;
        this.startTimeLimitFlag = false;
        for (let i = 0; i < estateNum; i++) {
          const item = this.Form.yardAndestateVoList[i];
          if (!item.startTime) {
            break;
          } else {
            num++
          }
        };
        for (let i = 0; i < estateNum; i++) {
          const item = this.Form.yardAndestateVoList[i];
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
      const estateNum = this.Form.yardAndestateVoList.length;
      if (estateNum) {
        let num = 0;
        let timeNum = 0;
        this.endTimeFlag = false;
        this.endTimeLimitFlag = false;
        for (let i = 0; i < estateNum; i++) {
          const item = this.Form.yardAndestateVoList[i];
          if (!item.endTime) {
            break;
          } else {
            num++
          }
        };
        for (let i = 0; i < estateNum; i++) {
          const item = this.Form.yardAndestateVoList[i];
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
      const estateNum = this.Form.yardAndestateVoList.length;
      if (estateNum > 0) {
        let num = 0;
        this.contractAmountFlag = false;
        for (let i = 0; i < estateNum; i++) {
          const item = this.Form.yardAndestateVoList[i];
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
    // 限制使用金额小数点后两位
    contractAmountLimitFloat(index, flag) {
      this.Form.yardAndestateVoList[index].contractAmount = this.Form.yardAndestateVoList[index].contractAmount.replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').replace(/^\./g, '');
      if (flag && this.Form.yardAndestateVoList[index].contractAmount !== "") {
        this.Form.yardAndestateVoList[index].contractAmount = (this.Form.yardAndestateVoList[index].contractAmount - 0).toFixed(2);
      }
    }
  },
  mounted() {
    this.getList();
    this.getYardDic();
    this.getRegionList();
    this.getEstateNameList();
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
#baseInfoYard .query-box {
  position: relative;
  margin-bottom: 20px;
}
#baseInfoYard .query-item {
  font-size: 16px;
  height: 32px;
  line-height: 32px;
  float: left;
  width: 20%;
}
#baseInfoYard .query-item > div {
  float: left;
}
#baseInfoYard .query-item > .query-input {
  width: calc(100% - 80px);
}
#baseInfoYard .query-item .el-date-editor.el-input,
#baseInfoYard .query-item .el-input__inner {
  height: 32px;
  line-height: 32px;
  width: 100%;
}
#baseInfoYard .query-btn {
  float: left;
  margin-left: 50px;
}
#baseInfoYard .table-operate-box {
  width: 100%;
  height: 66px;
  background: #d9eef6;
  position: relative;
}
#baseInfoYard .operate-btn {
  position: absolute;
  left: 19px;
  top: 17px;
}
#baseInfoYard .operate-pager {
  position: absolute;
  right: 19px;
  top: 17px;
}
#baseInfoYard .my-map {
  width: 100%;
  height: 300px;
}
#baseInfoYard .map-tips {
  color: #ff0000;
  margin-bottom: 10px;
}
#baseInfoYard .estate-add-btn-box {
  display: block;
}
#baseInfoYard .estate-table-box {
  margin-bottom: 25px;
}
#baseInfoYard .estate-table-box .el-input__inner {
  border: 0;
}
#baseInfoYard .estate-table-box .el-table .cell {
  padding: 0;
}
#baseInfoYard .estate-table-box .tab_2 .el-table__body tr.hover-row > td {
  background-color: #fff;
}
#baseInfoYard
  .estate-table-box
  .tab_2
  > .el-table--enable-row-hover
  .el-table__body
  tr:hover
  > td {
  background-color: #fff;
}
#baseInfoYard .estate-add-btn {
  padding: 8px 20px;
  width: 868px;
  border-color: #c0c4cc;
}
#baseInfoYard .tips-box {
  color: red;
}
</style>