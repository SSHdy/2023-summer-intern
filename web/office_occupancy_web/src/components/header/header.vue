<template>
  <el-header id="header-container">
    <div class="banner">
      <div class="login-msg">
        <img src="../../assets/imgs/avatar.png" class="avatar-img" />
        <span class="msg-font">
          欢迎您
          <i>{{ username }}</i>
          今天是:{{ dateMsg }}
        </span>
      </div>
      <div class="login-btn-group">
        <div class="left-group-banner"></div>
        <div class="center-group-banner">
          <div
            class="banner-btn"
            v-if="
              showRepairBacklogBtn ||
              showDisposeBacklogBtn ||
              showConfigBacklogBtn
            "
          >
            <el-dropdown trigger="click">
              <span class="el-dropdown-link">
                <span class="iconfont banner-btn-img">&#xe630;</span>
                <span class="banner-btn-text">待办事项</span>
                <el-badge
                  class="mark header-btn-mark"
                  :value="allBacklogNum"
                  v-show="allBacklogNum !== 0"
                />
                <i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown" class="header-backlog-dropdown">
                <el-dropdown-item v-if="showRepairBacklogBtn">
                  <div
                    @click="
                      routerLink(
                        '/home/repairManagement/waittingThing',
                        true,
                        'repair'
                      )
                    "
                  >
                    <span>维修待办</span>
                    <el-badge
                      class="mark header-little-mark"
                      :value="repairBacklogNum"
                      v-show="repairBacklogNum !== 0"
                    />
                  </div>
                </el-dropdown-item>
                <el-dropdown-item v-if="showDisposeBacklogBtn">
                  <div
                    @click="
                      routerLink(
                        '/home/disposeManagement/disposeBacklog',
                        true,
                        'dispose'
                      )
                    "
                  >
                    <span>处置待办</span>
                    <el-badge
                      class="mark header-little-mark"
                      :value="disposeBacklogNum"
                      v-show="disposeBacklogNum !== 0"
                    />
                  </div>
                </el-dropdown-item>
                <el-dropdown-item v-if="showConfigBacklogBtn">
                  <div
                    @click="
                      routerLink(
                        '/home/configManagement/configBacklog',
                        true,
                        'config'
                      )
                    "
                  >
                    <span>配置待办</span>
                    <el-badge
                      class="mark header-little-mark"
                      :value="configBacklogNum"
                      v-show="configBacklogNum !== 0"
                    />
                  </div>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <div
            class="banner-btn"
            v-if="showRepairRemindBtn || showRentRemindBtn"
          >
            <el-dropdown trigger="click">
              <span class="el-dropdown-link">
                <span class="iconfont banner-btn-img">&#xe60f;</span>
                <span class="banner-btn-text">提醒收纳箱</span>
                <el-badge
                  class="mark header-btn-mark"
                  :value="allRemindNum"
                  v-show="allRemindNum !== 0"
                />
                <i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown" class="header-backlog-dropdown">
                <el-dropdown-item v-if="showRepairRemindBtn">
                  <div
                    @click="
                      routerLink(
                        '/home/repairManagement/repairRemind',
                        true,
                        'repair'
                      )
                    "
                  >
                    <span>维修提醒</span>
                    <el-badge
                      class="mark header-little-mark"
                      :value="repairRemindNum"
                      v-show="repairRemindNum !== 0"
                    />
                  </div>
                </el-dropdown-item>
                <el-dropdown-item v-if="showRentRemindBtn">
                  <div
                    @click="
                      routerLink(
                        '/home/disposeManagement/rentRemind',
                        true,
                        'dispose'
                      )
                    "
                  >
                    <span>租金缴纳提醒</span>
                    <el-badge
                      class="mark header-little-mark"
                      :value="rentRemindNum"
                      v-show="rentRemindNum !== 0"
                    />
                  </div>
                </el-dropdown-item>
                <el-dropdown-item v-if="showRentRemindBtn">
                  <div
                    @click="
                      routerLink(
                        '/home/disposeManagement/expireRemind',
                        true,
                        'dispose'
                      )
                    "
                  >
                    <span>租借到期提醒</span>
                    <el-badge
                      class="mark header-little-mark"
                      :value="rentExpireCount"
                      v-show="rentExpireCount !== 0"
                    />
                  </div>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <!-- <div class="banner-btn" @click="changePassword">
            <span class="iconfont banner-btn-img">&#xe88a;</span>
            <span>修改密码</span>
          </div> -->
          <div
            class="banner-btn"
            @click="routerLink('/home/nav')"
            v-show="showMenu"
          >
            <span class="iconfont banner-btn-img">&#xe615;</span>
            <span>返回首页</span>
          </div>
          <!-- <div class="banner-btn" @click="routerLink('/login')">
            <span class="iconfont banner-btn-img">&#xe607;</span>
            <span>退出系统</span>
          </div> -->
        </div>
        <div class="right-group-banner"></div>
      </div>
      <div class="project-name-img" @click="routerLink('/home/nav')">
        <img :src="headerTitleUrl" alt="项目名称" />
      </div>
      <div class="china-party-box" @click="routerLink('/home/nav')">
        <img src="../../assets/imgs/china-party-icon.png" />
      </div>
    </div>
    <nav class="nav clearfix" v-show="showMenu">
      <div class="leftHeader" id="leftHeader"></div>
      <div class="rightHeader">
        <ul class="menu clearfix">
          <li
            v-for="menu in navlist"
            :key="menu.key"
            @click="toggleSubmenu(menu.key, menu.path)"
            :class="{ 'router-active': path === menu.key }"
          >
            {{ menu.name }}
          </li>
        </ul>
      </div>
    </nav>
    <div class="top-gap" v-show="showMenu"></div>
    <div class="top" id="top" v-show="showMenu">
      <img src="../../assets/imgs/top.png" />
    </div>
    <div class="history-pages">
      <span
        v-for="(item, index) in historyPages"
        :key="index"
        @click="jumpLabelPage(item.key, item.path)"
        :class="{ 'router-label-active': path === item.key }"
      >
        {{ item.name }}
        <i class="el-icon-close" @click.stop="deleteRouterLabel(item.key)"></i>
      </span>
    </div>
  </el-header>
</template>

<script>
import moment from "moment";
import { BASE_URL, url_name_1, PRO_TYPE } from "@/axios/global";
import { eventBus } from "@/eventBus/eventBus";
export default {
  props: {
    navlist: {
      type: Array,
      default: () => [],
    },
    path: {
      type: String,
      default: "",
    },
    username: {
      type: String,
      default: "",
    },
    showMenu: {
      type: Boolean,
      default: true,
    },
    allBacklogNum: {
      type: Number,
      default: 0,
    },
    allRemindNum: {
      type: Number,
      default: 0,
    },
    repairBacklogNum: {
      type: Number,
      default: 0,
    },
    disposeBacklogNum: {
      type: Number,
      default: 0,
    },
    configBacklogNum: {
      type: Number,
      default: 0,
    },
    repairRemindNum: {
      type: Number,
      default: 0,
    },
    rentRemindNum: {
      type: Number,
      default: 0,
    },
    rentExpireCount: {
      type: Number,
      default: 0,
    },
    showRepairBacklogBtn: {
      type: Boolean,
      default: false,
    },
    showDisposeBacklogBtn: {
      type: Boolean,
      default: false,
    },
    showConfigBacklogBtn: {
      type: Boolean,
      default: false,
    },
    showRepairRemindBtn: {
      type: Boolean,
      default: false,
    },
    showRentRemindBtn: {
      type: Boolean,
      default: false,
    },
    showRentExpireBtn: {
      type: Boolean,
      default: false,
    },
    repairBacklogNavList: {
      type: Array,
      default: () => [],
    },
    disposeBacklogNavList: {
      type: Array,
      default: () => [],
    },
    configBacklogNavList: {
      type: Array,
      default: () => [],
    },
    historyPages: {
      type: Array,
      default: () => [],
    },
    activePageKey: {
      type: String,
      default: "",
    },
    roleBanner: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      topWidth: "",
      showGoHomepage: true,
      PRO_TYPE: PRO_TYPE,
      headerTitleUrl: "",
    };
  },
  computed: {
    dateMsg() {
      moment.locale("zh-cn");
      return moment().format("YYYY年MM月DD日") + " " + moment().format("dddd");
    },
  },
  methods: {
    logout() {
      const token = sessionStorage.getItem("TOKEN");
      if (token) {
        this.$router.replace({ path: "/login" });
        this.$store.dispatch("Logout", token);
      }
    },
    routerLink(url, flag, name) {
      let tips = false;
      let list = [];
      const pageName = this.$route.name;
      if (pageName === "houseGraph") tips = true;
      if (flag) {
        switch (name) {
          case "repair":
            if (tips) {
              list = this.repairBacklogNavList;
            } else {
              this.$store.commit("changeNavList", this.repairBacklogNavList);
            }
            break;
          case "dispose":
            if (tips) {
              list = this.disposeBacklogNavList;
            } else {
              this.$store.commit("changeNavList", this.disposeBacklogNavList);
            }
            break;
          case "config":
            if (tips) {
              list = this.configBacklogNavList;
            } else {
              this.$store.commit("changeNavList", this.configBacklogNavList);
            }
            break;
        }
      }
      if (tips) {
        eventBus.$emit("showSaveTips", url, list);
      } else {
        if (url === "/login") {
          const token = sessionStorage.getItem("TOKEN");
          this.$router.replace({ path: url });
          this.$store.dispatch("Logout", token);
        } else {
          this.$router.push(url);
        }
      }
    },
    toggleSubmenu(key, path) {
      const pageName = this.$route.name;
      if (pageName !== "houseGraph" && pageName !== "roomDistributeView") {
        this.routerLink(path);
      } else if (pageName === "houseGraph") {
        eventBus.$emit("showSaveTips", path);
      } else if (pageName === "roomDistributeView") {
        eventBus.$emit("showDistributeTips", path);
      }
    },
    changePassword() {
      this.$emit("change-password");
    },
    deleteRouterLabel(key) {
      this.$emit("deleteRouterLabel", key);
    },
    jumpLabelPage(key, path) {
      const pageName = this.$route.name;
      if (pageName !== "houseGraph" && pageName !== "roomDistributeView") {
        this.$emit("jumpLabelPage", key, path);
      } else if (pageName === "houseGraph") {
        eventBus.$emit("showSaveTips", path, true);
      } else if (pageName === "roomDistributeView") {
        eventBus.$emit("showDistributeTips", path, true);
      }
    },
  },
  created() {
    // if (this.PRO_TYPE === 'yc') {
    //   this.headerTitleUrl = this.roleBanner === 0 ? `${BASE_URL}${url_name_1}/images/inside.png` : `${BASE_URL}${url_name_1}/images/without.png`;
    // } else if (this.PRO_TYPE === 'sy') {
    //   this.headerTitleUrl = require(`../../assets/imgs/project-name-${this.PRO_TYPE}.png`);
    // } else if (this.PRO_TYPE === 'gl') {
    //   this.headerTitleUrl = require(`../../assets/imgs/project-name-${this.PRO_TYPE}.png`);
    // }
    console.log(this.PRO_TYPE);
    this.headerTitleUrl = require("../../assets/imgs/loginry2.png");
  },
  mounted() {
    this.$on("collapse", (w) => {
      $("#leftHeader").animate({ width: w }, 600);
      $("#top").animate({ width: w }, 600);
    });
    eventBus.$off("hideAside");
    eventBus.$off("showAside");
    eventBus.$on("hideAside", () => {
      $("#top").css("width", "0px");
      $("#leftHeader").css("width", "6px");
    });
    eventBus.$on("showAside", () => {
      const w = window.screen.width;
      if (w <= 1366) {
        $("#top").css("width", "200px");
        $("#leftHeader").css("width", "200px");
      } else {
        $("#top").css("width", "273px");
        $("#leftHeader").css("width", "273px");
      }
    });
  },
};
</script>

<style>
#header-container {
  height: auto !important;
}
#header-container .el-header {
  padding: 0px !important;
  position: relative;
}
#header-container .banner {
  height: 140px;
  width: 100%;
  background: url("../../assets/imgs/banner.png") no-repeat;
  position: relative;
  background-size: cover;
}
#header-container .banner-img {
  width: 100%;
}
#header-container .project-name-img {
  position: absolute;
  top: 50%;
  margin-top: -26px;
  left: 100px;
  cursor: pointer;
}
#header-container .china-party-box {
  position: absolute;
  top: 50%;
  margin-top: -41px;
  left: 15px;
  cursor: pointer;
}
#header-container .avatar-img {
  width: 29px;
  height: 42px;
  margin-right: 12px;
}
#header-container .login-msg {
  padding-top: 36px;
  padding-right: 50px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
#header-container .msg-font {
  font-family: "微软雅黑";
  font-size: 17px;
  color: white;
}
#header-container .msg-font > i {
  color: #c3e76d;
  font-style: normal;
  padding-right: 10px;
  padding-left: 2px;
}
#header-container .login-btn-group {
  display: flex;
  position: absolute;
  bottom: -4px;
  right: 0;
}
#header-container .left-group-banner {
  background: url("../../assets/imgs/left-banner.png") no-repeat right;
  height: 50px;
  width: 78px;
  bottom: 2px;
  left: 1px;
  position: relative;
}
#header-container .right-group-banner {
  background: url("../../assets/imgs/right-banner.png") no-repeat;
  height: 50px;
  width: 79px;
}
#header-container .center-group-banner {
  background: url("../../assets/imgs/center-banner.png");
  background-repeat: repeat-x;
  height: 50px;
  width: auto;
  display: flex;
  z-index: 10;
}
#header-container .banner-btn {
  display: flex;
  align-items: center;
  line-height: 53px;
  cursor: pointer;
  font-family: "微软雅黑";
  color: white;
  font-size: 15px;
  margin-right: 25px;
}
#header-container .el-dropdown > .el-dropdown-link {
  display: flex;
  align-items: center;
  line-height: 53px;
  cursor: pointer;
  font-family: "微软雅黑";
  color: white;
  font-size: 15px;
}
#header-container .banner-btn-img {
  font-size: 22px;
  margin-right: 3px;
}
#header-container .logout-btn-img {
  width: 26px;
  height: 29px;
  margin-right: 5px;
}
#header-container .top {
  position: absolute;
  left: 0;
  top: 162px;
  z-index: 10;
  width: 273px;
  overflow: hidden;
}
#header-container .top > img {
  margin-left: 20px;
}
#header-container .leftHeader {
  width: 273px;
  height: 47px;
  float: left;
  background: url("../../assets/imgs/right-header.png");
}
#header-container .rightHeader {
  width: 50%;
  height: 47px;
  float: left;
  background: url("../../assets/imgs/right-header.png");
  position: relative;
}
#header-container .menu {
  position: absolute;
  top: 2px;
}
#header-container .menu li {
  list-style: none;
  height: 43px;
  float: left;
  text-align: left;
  padding: 0px 20px;
  cursor: pointer;
  line-height: 43px;
  font-size: 16px;
}
#header-container .menu li > i {
  transition: all ease-in-out 0.2s;
  transform-origin: center;
}
#header-container .menu .router-active {
  background-color: #0593e7;
  color: white;
}
#header-container .menu .router-active > i {
  transform: rotate(90deg);
}
#header-container .top-gap {
  min-height: 8px;
  background-color: rgb(0, 79, 122);
}
#header-container .nav {
  width: 200%;
}
#header-container .header-btn-mark .el-badge__content {
  position: absolute;
  top: -28px;
  left: -10px;
}
#header-container .header-backlog-dropdown .el-badge__content {
  position: relative;
  top: 2.2px;
}
#header-container .header-backlog-dropdown .el-dropdown-menu__item {
  cursor: pointer;
}
#header-container .history-pages {
  position: absolute;
  top: 110px;
}
#header-container .history-pages > span {
  display: inline-block;
  width: 140px;
  background-color: #ffffff;
  height: 22px;
  line-height: 22px;
  padding-left: 5px;
  box-sizing: border-box;
  cursor: pointer;
  margin-left: 1px;
}
#header-container .history-pages > span.router-label-active {
  background-color: #0593e7;
  color: white;
}
#header-container .el-icon-close {
  float: right;
  position: relative;
  top: 4px;
  right: 3px;
}
.header-little-mark .el-badge__content {
  position: relative;
  top: 2px;
}
@media only screen and (max-width: 1366px) {
  #header-container .banner {
    height: 82px;
  }
  #header-container .china-party-box > img {
    height: 100%;
  }
  #header-container .project-name-img > img {
    height: 100%;
  }
  #header-container .project-name-img {
    position: absolute;
    top: 50%;
    margin-top: -22px;
    left: 75px;
    cursor: pointer;
    height: 40px;
  }
  #header-container .china-party-box {
    position: absolute;
    top: 50%;
    margin-top: -32px;
    left: 15px;
    cursor: pointer;
    height: 50px;
  }
  #header-container .avatar-img {
    height: 30px;
    width: auto;
  }
  #header-container .msg-font {
    font-size: 15px;
  }
  #header-container .login-msg {
    padding-top: 5px;
  }
  #header-container .left-group-banner,
  #header-container .center-group-banner,
  #header-container .right-group-banner {
    height: 36px;
  }
  #header-container .left-group-banner {
    background-size: contain;
    bottom: 0px;
  }
  #header-container .banner-btn {
    line-height: 36px;
    font-size: 14px;
  }
  #header-container .el-dropdown > .el-dropdown-link {
    line-height: 36px;
    font-size: 14px;
  }
  #header-container .banner-btn-img {
    font-size: 18px;
  }
  #header-container .leftHeader {
    height: 38px;
    background-size: cover;
    width: 200px;
  }
  #header-container .menu li {
    height: 34px;
    line-height: 34px;
    font-size: 14px;
  }
  #header-container .rightHeader {
    background-size: contain;
    height: 38px;
  }
  #header-container .history-pages {
    top: 58px;
  }
  #header-container .top {
    top: 97px;
    left: 13px;
    width: 170px;
  }
  #header-container .top > img {
    width: 85%;
    margin-left: 0;
  }
  #header-container .login-btn-group {
    bottom: 1px;
  }
  #header-container .history-pages > span {
    width: 105px;
  }
}
</style>

