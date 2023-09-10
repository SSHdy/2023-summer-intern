<template>
  <div class="repair-info-box">
    <div class="repair-info-base clearfix" :style="showRepairModuleInfo? 'margin-bottom:20px' : ''">
      <div v-for="item in baseInfo" :key="item.key" v-show="item.value">
        <span
          :style="item.key==='repairProject' ? 'width:auto' : ''"
          class="nav-title"
        >{{item.name}}</span>：
        <span>{{item.value}}</span>
      </div>
    </div>
    <div class="repair-info-detail" v-if="showRepairModuleInfo">
      <div class="info-detail-title clearfix">
        <span>维修信息：</span>
        <div class="info-detail-btn">
          <span class="delete-btn" v-show="showDeleteBtn=== true" @click="deleteRepairModule">
            <i class="el-icon-delete"></i>删除
          </span>
          <el-popover
            v-model="popoverRepairInfo"
            placement="bottom-end"
            width="300"
            trigger="click"
            popper-class="more-info"
            :visible-arrow="false"
          >
            <ul class="repair-info-more">
              <li
                v-for="(item, index) in repairInfoList"
                :key="index"
                :class="{'info-active': activeInfo === index}"
                @click="showInfoDetail(item.id, index)"
              >{{item.info}}</li>
            </ul>
            <span class="more-btn" slot="reference" v-show="showMoreBtn === true">
              更多
              <i class="el-icon-arrow-down"></i>
            </span>
          </el-popover>
        </div>
      </div>
      <div class="info-detail-content">
        <div v-if="repairInfoDetail.repairSite">
          <span>维修地点</span>：
          <span>{{repairInfoDetail.repairSite}}</span>
        </div>
        <div v-if="repairInfoDetail.repairPartName">
          <span>维修部位</span>：
          <span>{{repairInfoDetail.repairPartName}}</span>
        </div>
        <div v-if="repairInfoDetail.repairModuleTarget">
          <span>维修内容</span>：
          <span>{{repairInfoDetail.repairModuleTarget}}</span>
        </div>
        <div>
          <span>维修面积</span>：
          <span class="repair-module-area-input"></span>
          <span class="repair-module-area">{{repairInfoDetail.repairModuleArea}}m²</span>
          <span
            class="edit-btn"
            @click="editRepairModuleArea(repairInfoDetail.repairModuleId)"
            v-show="showEditBtn === true && editing === false"
          >
            <i class="iconfont">&#xe61a;</i>
            <span class="edit-btn-txt">编辑</span>
          </span>
          <span class="save-btn" v-if="editing === true">
            <i class="iconfont">&#xe62f;</i>
            <span class="save-btn-txt">保存</span>
          </span>
        </div>
        <div v-if="repairInfoDetail.completionQuality">
          <span>质保期</span>：
          <span>{{repairInfoDetail.completionQuality}}</span>
        </div>
        <div
          class="info-detail-content-img clearfix"
          v-if="repairInfoDetail.repairImgList.length > 0"
        >
          <span>维修图片</span>
          <span>：</span>
          <div class="repair-img-box clearfix">
            <div
              class="repair-img-item"
              v-for="(item, index) in repairInfoDetail.repairImgList"
              :key="index"
            >
              <img :src="getImgUrl(item)" alt="维修图片" />
              <div class="bigger-view-warpper">
                <div class="bigger-view-warpper-inner"></div>
                <i class="el-icon-search" @click="preview(item)"></i>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { BASE_URL, url_name_1 } from "@/axios/global"
export default {
  props: {
    showRepairModuleInfo: {
      type: Boolean,
      default: true
    },
    editing: {
      type: Boolean,
      default: false
    },
    showEditBtn: {
      type: Boolean,
      default: false
    },
    showDeleteBtn: {
      type: Boolean,
      default: false
    },
    showMoreBtn: {
      type: Boolean,
      default: true
    },
    activeInfo: {
      type: Number,
      default: 0
    },
    baseInfo: {
      type: Array,
      default: () => {
        return [];
      }
    },
    repairInfoList: {
      type: Array,
      default: () => {
        return [];
      }
    },
    repairInfoDetail: {
      type: Object,
      default: () => {
        return {
          repairModuleId: null,
          repairSite: '',
          repairPartName: '',
          repairModuleTarget: '',
          repairModuleArea: '',
          completionQuality: '',
          repairImgList: []
        }
      }
    }
  },
  data() {
    return {
      popoverRepairInfo: false,
      token: ''
    }
  },
  methods: {
    // 获取图片链接
    getImgUrl(item) {
      let url = '';
      if (item.type === 3) {
        url = '/images/compress.png';
      } else {
        url = item.virtualAddress;
      }
      return `${BASE_URL}${url_name_1}${url}?access_token=${this.token}`
    },
    // 预览
    preview(item) {
      $('.img-preview-bigger').css('display', 'block');
      const img = $('#biggerImg');
      let url = '';
      if (item.type === 3) {
        url = '/images/compress.png';
      } else {
        url = item.virtualAddress;
      }
      img.attr('src', BASE_URL + url_name_1 + url + '?access_token=' + this.token);
    },
    showInfoDetail(id, index) {
      this.popoverRepairInfo = false;
      this.$emit('showInfoDetail', id, index);
    },
    editRepairModuleArea(id) {
      this.$emit('editRepairModuleArea', id);
    },
    deleteRepairModule() {
      this.$emit('deleteRepairModule');
    }
  },
  mounted() {
    this.token = sessionStorage.getItem('TOKEN');
  },
};
</script>

<style scoped>
.repair-info-box {
  padding: 25px 25px 30px;
  border-bottom: 1px solid #d8d8d8;
}
.repair-info-base > div {
  float: left;
  width: 33%;
  margin-bottom: 15px;
}
.info-detail-title {
  border-bottom: 1px solid #d8d8d8;
}
.info-detail-title > span {
  font-size: 16px;
  line-height: 36px;
  float: left;
  border-bottom: 1px solid #498fea;
  position: relative;
  top: 1px;
}
.info-detail-title > .info-detail-btn {
  float: right;
}
.info-detail-btn > span {
  display: inline-block;
  line-height: 36px;
  font-size: 16px;
}
.info-detail-btn > span + span {
  margin-left: 20px;
}
.delete-btn {
  color: #f65c5f;
  cursor: pointer;
}
.delete-btn:hover {
  color: #d82a2d;
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
.more-btn {
  color: #3891e0;
  cursor: pointer;
}
.more-btn:hover {
  color: #035cac;
}
.info-detail-content {
  padding-left: 30px;
}
.info-detail-content > div {
  line-height: 40px;
}
.info-detail-content-img > span,
.info-detail-content-img > .repair-img-box {
  float: left;
}
.edit-btn,
.more-btn,
.save-btn {
  color: #3891e0;
  cursor: pointer;
}
.edit-btn {
  margin-left: 30px;
}
.edit-btn:hover,
.more-btn:hover,
.save-btn:hover {
  color: #035cac;
}
.edit-btn > i,
.save-btn > i {
  font-size: 18px;
  position: relative;
  top: 2px;
}
.repair-img-item {
  float: left;
  width: 100px;
  height: 100px;
  position: relative;
  top: 12px;
}
.repair-img-item + .repair-img-item {
  margin-left: 20px;
}
.repair-img-item > img {
  width: 100px;
  height: 100px;
  display: inline-block;
}
.repair-info-base {
  padding: 0px 30px;
}
.bigger-view-warpper {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 12px;
  left: 0;
}
.bigger-view-warpper > .bigger-view-warpper-inner {
  width: 100%;
  height: 100%;
  background: #000;
  opacity: 0.5;
  display: none;
}
.bigger-view-warpper:hover > .bigger-view-warpper-inner {
  display: block;
}
.bigger-view-warpper > i {
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
.bigger-view-warpper > i:hover {
  color: #3891e0;
}
.bigger-view-warpper:hover > i {
  display: block;
}
.info-detail-content > div > span:first-of-type,
.nav-title {
  width: 56px;
  display: inline-block;
  text-align: justify;
  text-justify: distribute-all-lines;
  text-align-last: justify;
}
</style>