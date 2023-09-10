<template>
  <div class="assets-detail-container">
    <div class="private-info">
      <div class="title">
        <i></i>
        {{privateTitle}}
      </div>
      <div class="content clearfix">
        <div class="info-item" v-for="item in renderType" :key="item.key">
          <span class="info-item-title">{{item.text}}：</span>
          <span class="info-item-content">{{Form[item.key]}}</span>
        </div>
      </div>
    </div>
    <div class="photo-info" v-if="Form.assetsImgList && Form.assetsImgList.length > 0">
      <div class="title">
        <i></i>
        图片信息
      </div>
      <div class="content clearfix">
        <div class="info-item" v-for="(photo, index) in Form.assetsImgList" :key="index">
          <img :src="getImageUrl(photo.imgUrl)" alt="资产图片" style="width:100%; height:100%;">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { BASE_URL, url_name_1 } from "@/axios/global"
import { eventBus } from "@/eventBus/eventBus";
export default {
  props: {
    data: {
      type: Object,
      default: () => {
        return {};
      }
    },
    detailAssetForm: {
      type: Object,
      default: () => {
        return {};
      }
    },
    renderType: {
      type: Array,
      default: () => {
        return [];
      }
    },
    assetDetailType: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      Form: {},
      privateTitle: '',
      token: ''
    };
  },
  methods: {
    dateFormate(date, format) {
      var o = {
        "M+": new Date(date).getMonth() + 1,
        "d+": new Date(date).getDate(),
        "h+": new Date(date).getHours(),
        "m+": new Date(date).getMinutes(),
        "s+": new Date(date).getSeconds(),
        "q+": Math.floor((new Date(date).getMonth() + 3) / 3),
        "S": new Date(date).getMilliseconds()
      }
      if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
        (new Date(date).getFullYear() + "").substr(4 - RegExp.$1.length));
      for (var k in o) if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
          RegExp.$1.length == 1 ? o[k] :
            ("00" + o[k]).substr(("" + o[k]).length));
      return format;
    },
    getImageUrl(url) {
      return `${BASE_URL}${url_name_1}${url}?access_token=${this.token}`;
    }
  },
  mounted() {
    this.token = sessionStorage.getItem('TOKEN');
    this.Form = JSON.parse(JSON.stringify(this.detailAssetForm));
    for (let key in this.Form) {
      this.Form[key] = this.data[key];
    }
    this.Form.assetsImgList = this.data.assetsImgList;
    for (const key in this.Form) {
      if (key === 'acquireDateComn' || key === 'financialEntryDateComn' || key === 'commissioningDateComn' || key === 'warrantyDeadline' || key === 'registrationDate' || key === 'completionDate' || key === 'issueDate') {
        if (this.Form[key] !== undefined && this.Form[key] !== '') {
          this.Form[key] = this.dateFormate(this.Form[key], 'yyyy-MM-dd');
        }
      }
    }
    switch (this.assetDetailType) {
      case 'VehicleCard':
        this.privateTitle = '车辆卡片资料信息';
        break;
      case 'HouseCard':
        this.privateTitle = '房屋卡片资料信息';
        break;
      case 'ConstructionCard':
        this.privateTitle = '构筑物卡片资料信息';
        break;
      case 'FurnitureCard':
        this.privateTitle = '家具/用具/装具卡片资料信息';
        break;
      case 'FlexibleUnitCard':
        this.privateTitle = '通用设备卡片资料信息';
        break;
      case 'DedicatedDevicesCard':
        this.privateTitle = '专用设备卡片资料信息';
        break;
      case 'soliCard':
        this.privateTitle = '土地使用权卡片资料信息';
        break;
    }
    // console.log('data', this.data);
    // console.log('detailAssetForm', this.detailAssetForm);
    // console.log('renderType', this.renderType);
    // console.log('assetDetailType', this.assetDetailType);
  },
}
</script>

<style scoped>
.base-info > .title,
.private-info > .title,
.photo-info > .title {
  color: #4690f0;
  font-weight: 600;
  padding: 10px 20px;
}
.content {
  color: #333;
  padding: 0 20px;
}
.base-info i,
.private-info i,
.photo-info i {
  background: url("../../assets/imgs/info-icon.png") no-repeat center center;
  display: inline-block;
  width: 20px;
  height: 20px;
  position: relative;
  top: 4px;
}
.photo-info i {
  background: url("../../assets/imgs/photo-icon.png") no-repeat center center;
}
.info-item-title {
  width: 135px;
  text-align: right;
  display: inline-block;
}
.info-item-content {
  display: inline-block;
  width: 258px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
.info-item {
  float: left;
  margin-top: 10px;
  margin-bottom: 10px;
  overflow: hidden;
}
.info-item > span {
  float: left;
}
.photo-info .info-item {
  float: left;
  margin-right: 15px;
  width: 100px;
  height: 100px;
}
</style>
