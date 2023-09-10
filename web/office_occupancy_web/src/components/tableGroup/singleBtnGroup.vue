<template>
  <div class="singleBtn clearfix">
    <div class="update" @click="update" v-if="isShowAlter">
      <a class="el-icon-edit">修改</a>
    </div>
    <div class="detail" @click="detail" v-if="isShowDetail">
      <a class="el-icon-view">详情</a>
    </div>
     <div class="rectify" @click="rectify" v-if="isShowrectify">
      <a class="iconfont">&#xe662;整改</a>
    </div>
    <div class="delete" @click="del" v-if="isShowDelete">
      <a class="el-icon-delete">删除</a>
    </div>
    <div class="look" @click="look" v-if="isShowLook">
      <a class="el-icon-tickets">查看</a>
    </div>
    <div class="certificate" @click="certificate" v-if="isShowCertificate">
      <i></i>
      <span>凭证</span>
    </div>
    <div class="logout" @click="logout" v-if="isShowLogout && !logoutStatus">
      <i></i>
      <span>注销</span>
    </div>
    <div class="logouted" @click="logouted" v-if="isShowLogouted && logoutStatus">
      <span>已注销</span>
    </div>
    <div class="ignore" @click="ignore" v-if="isShowIgnore">
      <i></i>
      <span>忽略</span>
    </div>
    <div class="acceptance" @click="acceptance" v-if="isShowAcceptance">
      <i></i>
      <span>受理</span>
    </div>
    <div class="construction" @click="construction" v-if="isShowConstruction">
      <a class="iconfont">&#xe614;施工管理</a>
    </div>
    <div class="completed" @click="completed" v-if="isShowCompleted">
      <a class="iconfont">&#xe643;竣工验收</a>
    </div>
    <div class="process" @click="process" v-if="isShowProcess">
      <a class="iconfont">&#xe69f;付款流程</a>
    </div>
    <div class="move" @click="move" v-if="isShowMove">
      <a class="iconfont">&#xe7e4;转移</a>
    </div>
    <div class="surrender" @click="surrender" v-if="isShowSurrender">
      <i></i>
      <span>{{this.surrenderText}}</span>
    </div>
    <div class="remind" @click="remind" v-if="isShowRemind">
      <a class="iconfont">&#xe6f1;维修提醒</a>
    </div>
    <div class="haveRead" @click="haveRead" v-if="isShowHaveRead">
      <a class="iconfont">&#xe61e;关闭</a>
    </div>
    <div class="recordView" @click="recordView" v-if="isRecord">
      <a class="iconfont">&#xe62b;{{this.recordText}}</a>
    </div>
    <div class="storage" @click="storage" v-if="isShowStorage">
      <a class="iconfont">&#xe645;入库</a>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    isShowAlter: {
      type: Boolean,
      default: true
    },
    isShowDelete: {
      type: Boolean,
      default: true
    },
    isShowDetail: {
      type: Boolean,
      default: false
    },
    isShowLogout: {
      type: Boolean,
      default: false
    },
    isShowLogouted: {
      type: Boolean,
      default: false
    },
    isShowCertificate: {
      type: Boolean,
      default: false
    },
    logoutStatus: {
      type: Boolean,
      default: false
    },
    isShowIgnore: {
      type: Boolean,
      default: false
    },
    isShowAcceptance: {
      type: Boolean,
      default: false
    },
    isShowLook: {
      type: Boolean,
      default: false
    },
    isShowConstruction: {
      type: Boolean,
      default: false
    },
    isShowCompleted: {
      type: Boolean,
      default: false
    },
    isShowMove: {
      type: Boolean,
      default: false
    },
    isShowProcess: {
      type: Boolean,
      default: false
    },
    isShowSurrender: {
      type: Boolean,
      default: false
    },
    isShowRemind: {
      type: Boolean,
      default: false
    },
    isShowHaveRead: {
      type: Boolean,
      default: false
    },
    isRecord: {
      type: Boolean,
      default: false
    },
    isShowrectify: {
      type: Boolean,
      default: false
    },
    isShowStorage: {
      type: Boolean,
      default: false
    },
    surrenderText: {
      type: String,
      default: '退租'
    },
    recordText: {
      type: String,
      default: '信息档案'
    }
  },
  methods: {
    detail() {
      this.$emit("detailClick");
    },
    update() {
      this.$emit("updateClick");
    },
    del() {
      this.$emit("deleteClick");
    },
    logout() {
      this.$emit("logoutClick");
    },
    logouted() {
      this.$emit("logoutedClick");
    },
    certificate() {
      this.$emit("certificateClick");
    },
    ignore() {
      this.$emit("ignoreClick");
    },
    acceptance() {
      this.$emit("acceptanceClick");
    },
    look() {
      this.$emit("lookClick");
    },
    construction() {
      this.$emit("constructionClick");
    },
    completed() {
      this.$emit("completedClick");
    },
    move() {
      this.$emit("moveClick");
    },
    process() {
      this.$emit("processClick");
    },
    surrender() {
      this.$emit("surrenderClick");
    },
    remind() {
      this.$emit("remindClick")
    },
    haveRead() {
      this.$emit("haveReadClick")
    },
    rectify() {
      this.$emit("rectifyClick")
    },
    recordView() {
      this.$emit("recordViewClick")
    },
    storage() {
      this.$emit("storageClick")
    }
  }
};
</script>


<style scoped>
.singleBtn > div {
  float: left;
  cursor: pointer;
  margin-left: 10px;
}
.singleBtn > .detail {
  color: #ff9e00;
}
.singleBtn > .detail:hover {
  color: #ff5400;
}
.singleBtn > .update {
  color: #3891e0;
}
.singleBtn > .update:hover {
  color: #035cac;
}
.singleBtn > .delete {
  color: #f65c5f;
}
.singleBtn > .delete:hover {
  color: #d82a2d;
}
.singleBtn > .logout {
  color: #f65c5f;
}
.singleBtn > .logout:hover {
  color: #e34a4d;
}
.singleBtn > .certificate {
  color: #1aa97b;
}
.singleBtn > .certificate:hover {
  color: #148f67;
}
.singleBtn > .logout > i {
  width: 12px;
  height: 12px;
  display: inline-block;
  background: url("../../assets/imgs/logout-icon.png") no-repeat center center;
  background-size: contain;
  position: relative;
  top: 1px;
}
.singleBtn > .logout:hover > i {
  background: url("../../assets/imgs/logout-hover-icon.png") no-repeat center
    center;
  background-size: contain;
}
.singleBtn > .certificate > i {
  width: 12px;
  height: 12px;
  display: inline-block;
  background: url("../../assets/imgs/certificate-icon.png") no-repeat center
    center;
  background-size: contain;
  position: relative;
  top: 1px;
}
.singleBtn > .certificate:hover > i {
  background: url("../../assets/imgs/certificate-hover-icon.png") no-repeat
    center center;
  background-size: contain;
}
.singleBtn > .logouted {
  color: #3891e0;
}
.singleBtn > .logouted:hover {
  color: #2b83d2;
}
.singleBtn > .ignore {
  color: #3891e0;
}
.singleBtn > .ignore:hover {
  color: #2b83d2;
}
.singleBtn > .ignore > i {
  width: 12px;
  height: 12px;
  display: inline-block;
  background: url("../../assets/imgs/ignore-icon.png") no-repeat center center;
  background-size: contain;
  position: relative;
  top: 1px;
}
.singleBtn > .ignore:hover > i {
  background: url("../../assets/imgs/ignore-hover-icon.png") no-repeat center
    center;
  background-size: contain;
}
.singleBtn > .acceptance {
  color: #1aa97b;
}
.singleBtn > .acceptance:hover {
  color: #169f73;
}
.singleBtn > .acceptance > i {
  width: 12px;
  height: 12px;
  display: inline-block;
  background: url("../../assets/imgs/acceptance-icon.png") no-repeat center
    center;
  background-size: contain;
  position: relative;
  top: 1px;
}
.singleBtn > .acceptance:hover > i {
  background: url("../../assets/imgs/acceptance-hover-icon.png") no-repeat
    center center;
  background-size: contain;
}
.singleBtn > .look {
  color: #1aa97b;
}
.singleBtn > .look:hover {
  color: #169f73;
}
.singleBtn > .construction .iconfont {
  font-size: 14px;
  color: #1aa97b;
}
.singleBtn > .construction:hover {
  font-size: 14px;
  color: #169f73;
}
.singleBtn > .completed .iconfont {
  font-size: 14px;
  color: #4687f1;
}
.singleBtn > .completed:hover {
  font-size: 14px;
  color: #169f73;
}
.singleBtn > .move .iconfont {
  font-size: 14px;
  color: #3891e0;
}
.singleBtn > .move:hover {
  font-size: 14px;
  color: #169f73;
}
.singleBtn > .process .iconfont {
  font-size: 14px;
  color: #ffa107;
}
.singleBtn > .process:hover {
  font-size: 14px;
  color: #169f73;
}
.singleBtn > .surrender > i {
  width: 12px;
  height: 12px;
  display: inline-block;
  background: url("../../assets/imgs/surrender-icon.png") no-repeat center
    center;
  background-size: contain;
  position: relative;
  top: 1px;
}
.singleBtn > .surrender:hover > i {
  background: url("../../assets/imgs/surrender-hover-icon.png") no-repeat center
    center;
  background-size: contain;
}
.singleBtn > .surrender {
  color: #1aa97b;
}
.singleBtn > .surrender:hover {
  color: #0b9669;
}
.singleBtn > .remind .iconfont {
  font-size: 14px;
  color: #3891e0;
}
.singleBtn > .remind:hover {
  color: #2b83d2;
}
.singleBtn > .haveRead .iconfont {
  font-size: 14px;
  color: #171cfc;
}
.singleBtn > .haveRead:hover {
  color: #0509bb;
}
.singleBtn > .rectify .iconfont {
  font-size: 14px;
  color: #37b48b;
}
.singleBtn > .rectify:hover {
  color: #11b880;
}
.singleBtn > .recordView .iconfont {
  font-size: 14px;
  color: #3891e0;
}
.singleBtn > .recordView:hover {
  color: #2b83d2;
}
.singleBtn > .storage .iconfont {
  font-size: 14px;
  color: #3891e0;
}
.singleBtn > .storage:hover {
  color: #2b83d2;
}
</style>