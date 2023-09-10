<template>
  <section class="el-container is-vertical">
    <appHeader
      ref="header"
      :navlist="navlist"
      :path="path"
      :username="user_name"
      :showMenu="showMenu"
      :allBacklogNum="allBacklogNum"
      :allRemindNum="allRemindNum"
      :repairBacklogNum="repairBacklogNum"
      :disposeBacklogNum="disposeBacklogNum"
      :configBacklogNum="configBacklogNum"
      :repairRemindNum="repairRemindNum"
      :rentRemindNum="rentRemindNum"
      :rentExpireCount="rentExpireCount"
      :showRentRemindBtn="showRentRemindBtn"
      :showRepairRemindBtn="showRepairRemindBtn"
      :showRepairBacklogBtn="showRepairBacklogBtn"
      :showDisposeBacklogBtn="showDisposeBacklogBtn"
      :showConfigBacklogBtn="showConfigBacklogBtn"
      :showRentExpireBtn="showRentExpireBtn"
      :repairBacklogNavList="repairBacklogNavList"
      :disposeBacklogNavList="disposeBacklogNavList"
      :configBacklogNavList="configBacklogNavList"
      :historyPages="historyPages"
      :roleBanner="roleBanner"
      @change-password="changePassword"
      @deleteRouterLabel="deleteRouterLabel"
      @jumpLabelPage="jumpLabelPage"
    ></appHeader>
    <main class="el-main">
      <section id="container" class="el-container" :style="mainClass">
        <aside class="el-aside" v-show="asideShow">
          <div class="aside-main">
            <div class="aside-top">
              <img src="../../assets/imgs/aside-top1.png" />
            </div>
            <div class="aside-center">
              <div class="button-box" v-if="!isGraphPage && !isDistributePage">
                <button
                  class="button"
                  @click="activeButton(0)"
                  :class="{ 'active-button': activeButtonIndex === 0 }"
                >
                  按院落
                </button>
                <button
                  class="button"
                  @click="activeButton(1)"
                  :class="{ 'active-button': activeButtonIndex === 1 }"
                >
                  按单位
                </button>
              </div>
              <div class="select-box">
                <el-select
                  size="small"
                  placeholder="请选择"
                  v-model="selectedVal"
                  @change="selectChanged"
                  filterable
                >
                  <el-option
                    v-for="item in options"
                    :key="item.id"
                    :label="item.text"
                    :value="item.id"
                    style="width: 215px"
                  ></el-option>
                </el-select>
              </div>
              <div class="tree-box">
                <el-tree
                  ref="tree"
                  node-key="id"
                  :load="loadNode"
                  lazy
                  accordion
                  highlight-current
                  :default-expanded-keys="defaultExpandedkeys"
                  :props="defaultProps"
                  :render-content="renderContent"
                  @node-expand="handleNodeExpand"
                  @node-collapse="handleNodeCollapse"
                  @node-click="handleNodeClick"
                ></el-tree>
              </div>
            </div>
          </div>
          <div class="aside-collapse-btn">
            <div class="aside-collapse-btn-inner" id="asideCollapseBtn">
              <img :src="asideArrowIcon" style="width: 5px" />
            </div>
          </div>
        </aside>
        <main class="el-main">
          <div class="top-child-menu">
            <div class="child-menu-top" v-show="childrens.length"></div>
            <div class="child-menu-center" v-show="childrens.length">
              <div class="left-child-menu"></div>
              <div class="center-child-menu">
                <li
                  v-for="menu in childrens"
                  :key="menu.key"
                  @click="routerLink(menu.path)"
                  :class="{ 'router-active': path === menu.key }"
                >
                  <div class="li-title">
                    <span class="child-title">{{ menu.name }}</span>
                    <span class="under-border"></span>
                  </div>
                </li>
              </div>
              <div class="right-child-menu"></div>
            </div>
          </div>
          <div class="mainDiv" id="mainDiv">
            <router-view
              @startEdit="startEdit"
              @startView="startView"
              :editGraph="editGraph"
            ></router-view>
          </div>
        </main>
      </section>
    </main>
    <footer class="el-footer"></footer>
    <!-- 修改密码dialog -->
    <el-dialog
      :visible.sync="changePasswordDialogVisible"
      width="60%"
      center
      class="change-password-dialog"
      :close-on-click-modal="false"
      @open="changePasswordDialogOpen"
      @closed="changePasswordDialogClosed"
    >
      <div slot="title" class="dialog-title">
        <span>修改密码</span>
      </div>
      <div class="form-box">
        <el-form
          :model="changePasswordForm"
          status-icon
          :rules="changePasswordRules"
          ref="changePasswordForm"
          label-width="80px"
          class="change-password-form"
        >
          <el-form-item label="当前密码" prop="password">
            <el-input
              type="password"
              v-model="changePasswordForm.password"
              autocomplete="off"
              @keyup.native="validateTrim('password')"
              @blur="validateTrim('password')"
            ></el-input>
          </el-form-item>
          <el-form-item label="新的密码" prop="newPassword">
            <el-input
              type="password"
              v-model="changePasswordForm.newPassword"
              autocomplete="off"
              @keyup.native="validateTrim('newPassword')"
              @blur="validateTrim('newPassword')"
            ></el-input>
            <div class="form-tips">
              登录密码必须是6-20位字母、数字或特殊字符组成
            </div>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkNewPassword">
            <el-input
              type="password"
              v-model="changePasswordForm.checkNewPassword"
              @keyup.native="validateTrim('checkNewPassword')"
              @blur="validateTrim('checkNewPassword')"
            ></el-input>
            <div class="form-tips">必须和上面密码保持一致</div>
          </el-form-item>
        </el-form>
        <div class="btn-box">
          <cancel-btn
            @clickBtn="changePasswordDialogVisible = false"
            :fontSize="14"
            :padding="'10px 35px'"
          ></cancel-btn>
          <confirm-btn
            @clickBtn="changePasswordSubmitForm('changePasswordForm')"
            :fontSize="14"
            :text="'确定'"
            :padding="'10px 35px'"
          ></confirm-btn>
        </div>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import uuidv1 from "uuid/v1";
import { getRouterList, getUserUnitList } from "@/axios/commonApi";
import {
  getYardTree,
  getUnitTree,
  getAllYardTree,
  updatePassWord,
  getTodoListNum,
  getTodoListProcessNum,
  getRemindNum,
  getHandleYardTree,
} from "./api";
import { eventBus } from "@/eventBus/eventBus";
import appHeader from "@/components/header/header";
import confirmBtn from "@/components/button/confirmBtn";
import cancelBtn from "@/components/button/cancelBtn";
export default {
  components: {
    appHeader,
    confirmBtn,
    cancelBtn,
  },
  data() {
    var validatePass = (rule, value, callback) => {
      const len = value.length;
      if (value === "") {
        callback(new Error("请输入新的密码"));
      } else if (len < 6 || len > 20) {
        callback(new Error("密码必须在6-20个字符之间"));
      } else if (
        this.changePasswordForm.checkNewPassword !== "" &&
        value !== this.changePasswordForm.checkNewPassword
      ) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.changePasswordForm.newPassword) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      clientHeight: 0,
      options: [],
      selectedVal: "",
      mainClass: {
        height: "0px",
      },
      headerH: 0,
      treeData: [],
      navlist: [],
      allNavList: [],
      childrens: [],
      menuKey: "",
      path: "", // 主要用于导航的判断
      path_: "",
      activeButtonIndex: 0,
      collapseFlag: true,
      asideArrowIcon: require("../../assets/imgs/aside-arrow-left.png"),
      asideShow: true,
      isGraphPage: false,
      treeYardList: [],
      treeAllYardList: [],
      treeUnitList: [],
      defaultProps: {
        children: "children",
        label: "text",
        isLeaf: "leaf",
      },
      user_name: "", // 当前用户的名字
      userId: "",
      resolve: null,
      firstLoad: true,
      showMenu: true,
      changePasswordDialogVisible: false,
      changePasswordForm: {
        password: "",
        newPassword: "",
        checkNewPassword: "",
      },
      changePasswordRules: {
        password: [
          { required: true, message: "当前密码不能为空", trigger: "blur" },
        ],
        newPassword: [
          { required: true, validator: validatePass, trigger: "blur" },
        ],
        checkNewPassword: [
          { required: true, validator: validatePass2, trigger: "blur" },
        ],
      },
      allBacklogNum: 0,
      allRemindNum: 0,
      repairBacklogNum: 0,
      disposeBacklogNum: 0,
      configBacklogNum: 0,
      repairRemindNum: 0,
      rentRemindNum: 0,
      rentExpireCount: 0,
      repairBacklogNavList: [],
      disposeBacklogNavList: [],
      configBacklogNavList: [],
      showRepairBacklogBtn: false,
      showDisposeBacklogBtn: false,
      showConfigBacklogBtn: false,
      showRepairRemindBtn: false,
      showRentRemindBtn: false,
      showRentExpireBtn: false,
      isDistributePage: false,
      handleYardTreeList: [],
      historyPages: [],
      defaultExpandedkeys: [],
      currentNodeKey: null,
      activeFloorName: null,
      editGraph: false,
    };
  },
  watch: {
    $route: {
      handler(to, from) {
        let fromPath = "";
        if (from) {
          fromPath = from.path.split("/").pop();
        }
        this.setRoute(fromPath, to);
      },
      immediate: true,
    },
    clientHeight() {
      this.changeFixed(this.clientHeight);
    },
    headerH() {
      this.changeFixed(this.clientHeight);
    },
  },
  methods: {
    // 开始编辑房间信息
    startEdit(obj) {
      setTimeout(() => {
        const yardTreeId = parseInt(obj.yardId);
        const buildTreeId = parseInt(obj.buildId);
        this.defaultExpandedkeys = [yardTreeId, buildTreeId];
        this.activeFloorName = `${obj.buildId}:${obj.floorName}`;
      }, 1000);
    },
    // 开始浏览房间信息
    startView(obj) {
      setTimeout(() => {
        if (obj.unitId) {
          const unitId = obj.unitId;
          const yardTreeId = `${obj.unitId}:${obj.yardId}`;
          const buildTreeId = `${obj.unitId}:${obj.yardId}:${obj.buildId}`;
          this.defaultExpandedkeys = [unitId, yardTreeId, buildTreeId];
          this.activeFloorName = `${obj.unitId}:${obj.yardId}:${obj.buildId}:${obj.floorName}`;
        } else {
          const yardTreeId = parseInt(obj.yardId);
          const buildTreeId = `${obj.yardId}:${obj.buildId}`;
          this.defaultExpandedkeys = [yardTreeId, buildTreeId];
          this.activeFloorName = `${obj.yardId}:${obj.buildId}:${obj.floorName}`;
        }
      }, 1000);
    },
    jumpLabelPage(key, path) {
      this.$router.push(path);
      this.$nextTick(() => {
        for (let i = 0; i < this.allNavList.length; i++) {
          if (this.allNavList[i].key === this.menuKey) {
            this.navlist = this.allNavList[i].children;
            this.$store.commit("changeNavList", this.navlist);
          }
        }
      });
    },
    getRemindTipsNum() {
      // 查询维修提醒数量
      getRemindNum()
        .then((res) => {
          this.repairRemindNum = res.repairCount;
          this.rentRemindNum = res.rentCount;
          this.rentExpireCount = res.rentExpireCount;
          if (
            this.showRentRemindBtn &&
            this.showRepairRemindBtn &&
            this.showRentExpireBtn
          ) {
            this.allRemindNum =
              res.rentCount + res.repairCount + res.rentExpireCount;
          } else if (
            this.showRentRemindBtn &&
            this.showRepairRemindBtn &&
            !this.showRentExpireBtn
          ) {
            this.allRemindNum = res.rentCount + res.repairCount;
          } else if (
            this.showRentRemindBtn &&
            !this.showRepairRemindBtn &&
            this.showRentExpireBtn
          ) {
            this.allRemindNum = res.rentCount + res.rentExpireCount;
          } else if (
            !this.showRentRemindBtn &&
            this.showRepairRemindBtn &&
            this.showRentExpireBtn
          ) {
            this.allRemindNum = res.repairCount + res.rentExpireCount;
          } else if (
            this.showRentRemindBtn &&
            !this.showRepairRemindBtn &&
            !this.showRentExpireBtn
          ) {
            this.allRemindNum = res.rentCount;
          } else if (
            !this.showRentRemindBtn &&
            this.showRepairRemindBtn &&
            !this.showRentExpireBtn
          ) {
            this.allRemindNum = res.repairCount;
          } else if (
            !this.showRentRemindBtn &&
            !this.showRepairRemindBtn &&
            this.showRentExpireBtn
          ) {
            this.allRemindNum = res.rentExpireCount;
          }
        })
        .catch((err) => {
          const msg = err.result ? err.result : "维修提醒数量获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    // 查询待办事项总数量
    getThingTipsNum() {
      this.userId = this.userId
        ? this.userId
        : sessionStorage.getItem("userId");
      getTodoListNum(this.userId)
        .then((res) => {
          this.allBacklogNum = res;
        })
        .catch((err) => {
          const msg = err.result ? err.result : "待办事项总数量获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    // 统计每个待办事项数量
    getThingListTipsNum() {
      getTodoListProcessNum(this.userId)
        .then((res) => {
          this.repairBacklogNum = res.repairNum;
          this.disposeBacklogNum = res.handleNum;
          this.configBacklogNum = res.configureNum;
        })
        .catch((err) => {
          const msg = err.result ? err.result : "待办事项数量获取失败";
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    validateTrim(key) {
      this.changePasswordForm[key] = $.trim(this.changePasswordForm[key]);
    },
    changeFixed(clientHeight) {
      const offsetTop = $("#container").offset().top;
      const footerHight = $(".el-footer").height();
      this.mainClass.height = `${clientHeight - offsetTop - footerHight}px`;
    },
    routerLink(url) {
      this.$router.push(url);
    },
    activeButton(index) {
      this.activeButtonIndex = index;
      this.selectedVal = "";
      const treeRootNode = this.$refs.tree.root;
      treeRootNode.childNodes = [];
      eventBus.$emit("hideBuildDetail");
      if (index === 0) {
        this.options = this.treeYardList;
        this.loadNode(treeRootNode, this.resolve);
      } else if (index === 1) {
        this.loadNode(treeRootNode, this.resolve);
        // 获取单位列表
        getUserUnitList().then((res) => {
          this.treeUnitList = JSON.parse(JSON.stringify(res));
          this.options = res;
          this.options.forEach((ele) => {
            ele.id = ele.unitId;
            ele.text = ele.unitName;
          });
          this.options.unshift({
            text: "全部",
            id: "",
          });
        });
      }
      if (this.path_ !== "houseView") {
        this.$router.push({ path: "/home/houseManagement/houseView" });
      }
    },
    setRoute(fromPath, to) {
      this.menuKey = to.params.id;
      if (this.menuKey) {
        this.navlist = this.$store.getters.getNavList;
      }
      this.path_ = to.name;
      this.path = to.name;
      if (this.path === "houseGraphView") {
        this.path = "houseView";
      }
      if (this.navlist.length > 0) {
        for (let j = 0; j < this.navlist.length; j++) {
          if (this.navlist[j].key === this.path) {
            if (this.historyPages.length > 6) {
              this.historyPages.shift();
            }
            let pushFlag = true;
            for (let n = 0; n < this.historyPages.length; n++) {
              if (this.historyPages[n].key === this.path) {
                pushFlag = false;
                break;
              }
            }
            if (pushFlag) {
              this.historyPages.push({
                name: this.navlist[j].name,
                key: this.navlist[j].key,
                path: this.navlist[j].path,
              });
            }
            break;
          }
        }
      }

      // 还原canvas页面数据
      if (!(this.path_ === "houseGraphView" && fromPath === "houseView")) {
        sessionStorage.removeItem("yardId");
        sessionStorage.removeItem("buildId");
        sessionStorage.removeItem("floorName");
        sessionStorage.removeItem("unitId");
      }

      // 判断是否是示意图页面
      if (this.path_ === "houseGraph") {
        this.isGraphPage = true;
        this.isDistributePage = false;
      } else if (this.path_ === "roomDistributeView") {
        this.isGraphPage = false;
        this.isDistributePage = true;
      } else {
        this.isGraphPage = false;
        this.isDistributePage = false;
      }

      // 判断是否显示导航
      if (this.path_ === "nav") {
        this.showMenu = false;
      } else {
        this.showMenu = true;
      }

      // 清空展开树节点
      this.defaultExpandedkeys = [];

      // 获取树的数据
      if (
        this.firstLoad === false &&
        !(this.path_ === "houseView" && fromPath === "houseGraphView") &&
        !(this.path_ === "houseGraphView" && fromPath === "houseView") &&
        (this.path_ === "houseView" ||
          this.path_ === "houseGraph" ||
          this.path_ === "roomDistributeView" ||
          this.path_ === "houseGraphView")
      ) {
        console.log("进入流程");
        if (
          this.$route.name === "houseGraphView" &&
          this.$route.params &&
          this.$route.params.unitId !== "" &&
          this.$route.params.unitId !== null &&
          this.$route.params.unitId !== undefined
        ) {
          this.activeButtonIndex = 1;
        } else {
          this.activeButtonIndex = 0;
        }
        this.selectedVal = "";
        if (this.path_ === "houseView" || this.path_ === "houseGraphView") {
          if (this.activeButtonIndex === 0) {
            getYardTree(null, 0, this.selectedVal)
              .then((res) => {
                this.treeYardList = JSON.parse(JSON.stringify(res));
                this.treeYardList.unshift({
                  text: "全部",
                  id: "",
                });
                this.options = this.treeYardList;
              })
              .catch((err) => {
                const msg = err.result ? err.result : "树获取失败";
                this.$message({
                  message: msg,
                  type: "error",
                });
              });
          } else if (this.activeButtonIndex === 1) {
            getUserUnitList()
              .then((res) => {
                this.treeUnitList = JSON.parse(JSON.stringify(res));
                this.options = res;
                this.options.forEach((ele) => {
                  ele.id = ele.unitId;
                  ele.text = ele.unitName;
                });
                this.options.unshift({
                  text: "全部",
                  id: "",
                });
              })
              .catch((err) => {
                const msg = err.result ? err.result : "单位下拉选项获取失败";
                this.$message({
                  message: msg,
                  type: "error",
                });
              });
          }
        } else if (this.path_ === "houseGraph") {
          getAllYardTree(null, 0, null, this.selectedVal)
            .then((res) => {
              this.treeAllYardList = JSON.parse(JSON.stringify(res));
              this.treeAllYardList.unshift({
                text: "全部",
                id: "",
              });
              this.options = this.treeAllYardList;
            })
            .catch((err) => {
              const msg = err.result ? err.result : "树获取失败";
              this.$message({
                message: msg,
                type: "error",
              });
            });
        } else if (this.path_ === "roomDistributeView") {
          getHandleYardTree(null, 0, null, this.selectedVal)
            .then((res) => {
              res.unshift({
                text: "全部",
                id: "",
              });
              this.options = res;
            })
            .catch((err) => {
              const msg = err.result ? err.result : "树获取失败";
              this.$message({
                message: msg,
                type: "error",
              });
            });
        }
        const treeRootNode = this.$refs.tree.root;
        treeRootNode.childNodes = [];
        this.loadNode(treeRootNode, this.resolve);
      }

      // 隐藏楼栋详情框
      if (
        this.firstLoad === false &&
        !(this.path_ === "houseView" && fromPath === "houseGraphView")
      ) {
        eventBus.$emit("hideBuildDetail");
      }

      this.$nextTick(() => {
        // 判断是否显示左侧树
        if (
          this.path_ === "houseView" ||
          this.path_ === "houseGraph" ||
          this.path_ === "houseGraphView" ||
          this.path_ === "roomDistributeView"
        ) {
          this.asideShow = true;
          eventBus.$emit("showAside");
          const windowScreenWidth = window.screen.width;
          if (windowScreenWidth <= 1366) {
            $(".el-aside").css({ width: 200 });
          } else {
            $(".el-aside").css({ width: 273 });
          }
          this.collapseFlag = true;
          this.asideArrowIcon = this.collapseFlag
            ? require("../../assets/imgs/aside-arrow-left.png")
            : require("../../assets/imgs/aside-arrow-right.png");
        } else {
          this.asideShow = false;
          eventBus.$emit("hideAside");
          $(".el-aside").css({ width: 6 });
          this.collapseFlag = false;
          this.asideArrowIcon = this.collapseFlag
            ? require("../../assets/imgs/aside-arrow-left.png")
            : require("../../assets/imgs/aside-arrow-right.png");
        }

        // 左侧树的高度计算
        if (
          this.path_ === "houseGraph" ||
          this.path_ === "roomDistributeView"
        ) {
          $(".tree-box").height("calc(100% - 72px)");
        } else {
          $(".tree-box").height("calc(100% - 124px)");
        }

        // 获取header的高度
        this.headerH = $(".el-header").height();
      });
    },
    handleNodeCollapse(data, node) {
      if (
        this.activeButtonIndex === 0 &&
        !this.isGraphPage &&
        !this.isDistributePage
      ) {
        if (data.type === 1) {
          if (this.selectedVal === "") {
            eventBus.$emit("allBmMarker");
            eventBus.$emit("hideBuildDetail");
          } else {
            eventBus.$emit("updateBmMarker", data.value);
          }
        } else if (data.type === 2) {
          this.$nextTick(() => {
            eventBus.$emit("onlyShowBuildMarker", data.value);
          });
        }
      } else if (
        this.activeButtonIndex === 1 &&
        !this.isGraphPage &&
        !this.isDistributePage
      ) {
        if (data.type === 0) {
          eventBus.$emit("allBmMarker", data.unitIds);
          eventBus.$emit("hideBuildDetail");
        } else if (data.type === 1 || data.type === 2) {
          eventBus.$emit("allBmMarker", data.unitIds);
          eventBus.$emit("hideBuildDetail");
        } else if (data.type === 3) {
          eventBus.$emit("updateBmMarker", data.value);
        } else if (data.type === 4) {
          eventBus.$emit("onlyShowBuildMarker", data.value);
        }
      }
    },
    handleNodeExpand(data, node) {
      if (
        this.activeButtonIndex === 0 &&
        !this.isGraphPage &&
        !this.isDistributePage
      ) {
        if (data.type === 1) {
          eventBus.$emit("updateBmMarker", data.value);
        } else if (data.type === 2) {
          eventBus.$emit("onlyShowBuildMarker", data.value);
        }
      } else if (
        this.activeButtonIndex === 1 &&
        !this.isGraphPage &&
        !this.isDistributePage
      ) {
        if (data.type === 0) {
          eventBus.$emit("allBmMarker", data.unitIds);
        } else if (data.type === 1 || data.type === 2) {
          eventBus.$emit("getUnitHouseInfo", data.unitIds, data.value);
        } else if (data.type === 3) {
          eventBus.$emit("updateBmMarker", data.value);
        } else if (data.type === 4) {
          eventBus.$emit("onlyShowBuildMarker", data.value);
        }
      }
    },
    handleNodeClick(data, node) {
      console.log(data);
      if (
        this.activeButtonIndex === 0 &&
        !this.isGraphPage &&
        !this.isDistributePage
      ) {
        if (data.type === 3) {
          eventBus.$emit("hideBuildDetail");
          const tempArr = data.id.split(":");
          const yardId = parseInt(tempArr[0]);
          const buildId = parseInt(tempArr[1]);
          const floorName = parseInt(tempArr[2]);
          sessionStorage.setItem("yardId", yardId);
          sessionStorage.setItem("buildId", buildId);
          sessionStorage.setItem("floorName", floorName);
          if (this.path_ !== "houseGraphView") {
            this.$router.push({
              name: "houseGraphView",
              params: {
                yardId: yardId,
                buildId: buildId,
                floorName: floorName,
              },
            });
          } else {
            const obj = {
              buildId: buildId,
              floorName: floorName,
            };
            eventBus.$emit("closeHouseDetailBox");
            eventBus.$emit("getGraphJsonView", obj);
          }
        } else {
          if (this.path_ !== "houseView") {
            this.$router.push({ path: "/home/houseManagement/houseView" });
            sessionStorage.removeItem("yardId");
            sessionStorage.removeItem("buildId");
            sessionStorage.removeItem("floorName");
          }
        }
      } else if (
        this.activeButtonIndex === 1 &&
        !this.isGraphPage &&
        !this.isDistributePage
      ) {
        if (data.type !== 3 && data.type !== 4) {
          eventBus.$emit("hideBuildDetail");
        }
        if (data.type === 5) {
          const unitId = parseInt(data.id.split(":")[0]);
          const yardId = data.yardId;
          const buildId = data.buildId;
          const floorName = data.value;
          sessionStorage.setItem("yardId", yardId);
          sessionStorage.setItem("buildId", buildId);
          sessionStorage.setItem("floorName", floorName);
          sessionStorage.setItem("unitId", unitId);
          if (this.path_ !== "houseGraphView") {
            this.$router.push({
              name: "houseGraphView",
              params: {
                yardId: yardId,
                buildId: buildId,
                floorName: floorName,
              },
            });
          } else {
            const obj = {
              yardId: yardId,
              buildId: buildId,
              floorName: floorName,
            };
            eventBus.$emit("closeHouseDetailBox");
            eventBus.$emit("getGraphJsonView", obj);
          }
        } else {
          if (this.path_ !== "houseView") {
            this.$router.push({ path: "/home/houseManagement/houseView" });
          }
          sessionStorage.removeItem("yardId");
          sessionStorage.removeItem("buildId");
          sessionStorage.removeItem("floorName");
          sessionStorage.removeItem("unitId");
        }
      } else if (this.isGraphPage) {
        if (data.type === 3) {
          const yardId = data.yardId;
          const buildId = data.buildId;
          const floorName = data.value;
          // sessionStorage.setItem("yardId", yardId);
          // sessionStorage.setItem("buildId", buildId);
          // sessionStorage.setItem("floorName", floorName);
          // sessionStorage.setItem("unitId", '');
          const obj = {
            yardId: yardId,
            buildId: buildId,
            floorName: floorName,
          };
          eventBus.$emit("getGraphJson", obj);
        } else {
          eventBus.$emit("showSaveTips");
        }
      } else if (this.isDistributePage) {
        if (data.type === 3) {
          const tempArr = data.id.split(":");
          const yardId = tempArr[0] - 0;
          const buildId = tempArr[1] - 0;
          const floorName = tempArr[2] - 0;
          let canDistributeOrSave = true;
          let notDistributeReason = "";
          if (data.handle === 1) {
            switch (data.handleWay) {
              case 3:
                canDistributeOrSave = false;
                notDistributeReason = "拍卖";
                break;
              case 4:
                canDistributeOrSave = false;
                notDistributeReason = "拆除";
                break;
              case 7:
                canDistributeOrSave = false;
                notDistributeReason = "征收";
                break;
            }
          }
          const obj = {
            buildId: buildId,
            floorName: floorName,
            canDistributeOrSave: canDistributeOrSave,
            notDistributeReason: notDistributeReason,
          };
          eventBus.$emit("getGraphJson", obj);
          sessionStorage.setItem("yardId", yardId);
          sessionStorage.setItem("buildId", buildId);
          sessionStorage.setItem("floorName", floorName);
        }
      }
    },
    loadNode(node, resolve) {
      console.log(node, resolve);
      // 非示意图页面的院落树
      if (
        this.activeButtonIndex === 0 &&
        !this.isGraphPage &&
        !this.isDistributePage
      ) {
        if (node.level === 0) {
          getYardTree(null, 0, this.selectedVal)
            .then((res) => {
              console.log(res);
              console.log(this.firstLoad);
              if (this.firstLoad) {
                this.treeYardList = JSON.parse(JSON.stringify(res));
                this.treeYardList.unshift({
                  text: "全部",
                  id: "",
                });
                this.options = this.treeYardList;
                console.log(this.options);
              }
              console.log(this.options);
              res.forEach((item) => {
                if (!item.expand || item.expand === undefined) {
                  item.leaf = true;
                }
                item.showTooltip = this.getStrLen(item.text);
              });
              if (this.firstLoad) {
                this.resolve = resolve;
                this.firstLoad = false;
              }
              if (this.selectedVal === "") {
                eventBus.$emit("allBmMarker");
              }
              return resolve(res);
            })
            .catch((err) => {
              const msg = err.result ? err.result : "树获取失败";
              this.$message({
                message: msg,
                type: "error",
              });
            });
        } else {
          getYardTree(node.data.value, node.data.type, this.selectedVal)
            .then((res) => {
              console.log(res);
              if (node.data.type === 2) {
                res.forEach((item) => {
                  item.leaf = true;
                  item.showTooltip = this.getStrLen(item.text);
                });
                if (this.firstLoad) {
                  this.resolve = resolve;
                  this.firstLoad = false;
                }
                if (this.activeFloorName) {
                  this.$nextTick(() => {
                    this.$refs.tree.setCurrentKey(this.activeFloorName);
                    this.activeFloorName = null;
                  });
                }
              } else {
                res.forEach((item) => {
                  if (!item.expand || item.expand === undefined) {
                    item.leaf = true;
                  }
                  item.showTooltip = this.getStrLen(item.text);
                });
              }
              return resolve(res);
            })
            .catch((err) => {
              const msg = err.result ? err.result : "树获取失败";
              this.$message({
                message: msg,
                type: "error",
              });
            });
        }
      } else if (
        this.activeButtonIndex === 1 &&
        !this.isGraphPage &&
        !this.isDistributePage
      ) {
        if (node.level === 0) {
          getUnitTree(null, null, 0, null, this.selectedVal)
            .then((res) => {
              console.log(res);
              res.forEach((item) => {
                if (!item.expand || item.expand === undefined) {
                  item.leaf = true;
                }
                item.showTooltip = this.getStrLen(item.text);
              });
              if (this.firstLoad) {
                this.resolve = resolve;
                this.firstLoad = false;
              }
              if (this.selectedVal === "") {
                eventBus.$emit("allBmMarker");
              }
              return resolve(res);
            })
            .catch((err) => {
              const msg = err.result ? err.result : "树获取失败";
              this.$message({
                message: msg,
                type: "error",
              });
            });
        } else {
          if (typeof node.data.unitId !== "undefined") {
            getUnitTree(
              node.data.unitId,
              node.data.value,
              node.data.type,
              node.data.authority || null,
              this.selectedVal
            )
              .then((res) => {
                console.log(res);
                let isFloor = false;
                res.forEach((item) => {
                  if (item.type === 5) {
                    isFloor = true;
                  }
                  if (!item.expand || item.expand === undefined) {
                    item.leaf = true;
                  }
                  item.showTooltip = this.getStrLen(item.text);
                });
                if (isFloor && this.activeFloorName) {
                  this.$nextTick(() => {
                    this.$refs.tree.setCurrentKey(this.activeFloorName);
                    this.activeFloorName = null;
                  });
                }
                return resolve(res);
              })
              .catch((err) => {
                const msg = err.result ? err.result : "树获取失败";
                this.$message({
                  message: msg,
                  type: "error",
                });
              });
          }
        }
      } else if (this.isGraphPage) {
        if (node.level === 0) {
          getAllYardTree(null, 0, null, this.selectedVal)
            .then((res) => {
              console.log(res);
              if (this.firstLoad) {
                this.treeAllYardList = JSON.parse(JSON.stringify(res));
                this.treeAllYardList.unshift({
                  text: "全部",
                  id: "",
                });
                this.options = this.treeAllYardList;
              }
              res.forEach((item) => {
                if (!item.expand || item.expand === undefined) {
                  item.leaf = true;
                }
                item.showTooltip = this.getStrLen(item.text);
              });
              if (this.firstLoad) {
                this.resolve = resolve;
                this.firstLoad = false;
              }
              return resolve(res);
            })
            .catch((err) => {
              const msg = err.result ? err.result : "树获取失败";
              this.$message({
                message: msg,
                type: "error",
              });
            });
        } else {
          getAllYardTree(
            node.data.value,
            node.data.type,
            null,
            this.selectedVal
          )
            .then((res) => {
              console.log(res);
              if (node.data.type === 2) {
                res.forEach((item) => {
                  item.leaf = true;
                  item.showTooltip = this.getStrLen(item.text);
                });
                if (this.activeFloorName) {
                  this.$nextTick(() => {
                    this.$refs.tree.setCurrentKey(this.activeFloorName);
                    this.activeFloorName = null;
                  });
                }
              } else {
                res.forEach((item) => {
                  if (!item.expand || item.expand === undefined) {
                    item.leaf = true;
                  }
                  item.showTooltip = this.getStrLen(item.text);
                });
              }
              return resolve(res);
            })
            .catch((err) => {
              const msg = err.result ? err.result : "树获取失败";
              this.$message({
                message: msg,
                type: "error",
              });
            });
        }
      } else if (this.isDistributePage) {
        if (node.level === 0) {
          getHandleYardTree(null, 0, this.selectedVal)
            .then((res) => {
              console.log(res);
              if (this.firstLoad) {
                this.handleYardTreeList = JSON.parse(JSON.stringify(res));
                this.handleYardTreeList.unshift({
                  text: "全部",
                  id: "",
                });
                this.options = this.handleYardTreeList;
              }
              res.forEach((item) => {
                if (!item.expand || item.expand === undefined) {
                  item.leaf = true;
                }
                item.showTooltip = this.getStrLen(item.text);
              });
              if (this.firstLoad) {
                this.resolve = resolve;
                this.firstLoad = false;
              }
              if (this.selectedVal === "") {
                eventBus.$emit("allBmMarker");
              }
              return resolve(res);
            })
            .catch((err) => {
              const msg = err.result ? err.result : "树获取失败";
              this.$message({
                message: msg,
                type: "error",
              });
            });
        } else {
          getHandleYardTree(node.data.value, node.data.type, this.selectedVal)
            .then((res) => {
              console.log(res);
              if (node.data.type === 2) {
                res.forEach((item) => {
                  item.leaf = true;
                  item.showTooltip = this.getStrLen(item.text);
                });
              } else {
                res.forEach((item) => {
                  if (!item.expand || item.expand === undefined) {
                    item.leaf = true;
                  }
                  item.showTooltip = this.getStrLen(item.text);
                });
              }
              return resolve(res);
            })
            .catch((err) => {
              const msg = err.result ? err.result : "树获取失败";
              this.$message({
                message: msg,
                type: "error",
              });
            });
        }
      }
    },
    selectChanged(val) {
      this.selectedVal = val;
      if (this.path_ === "houseGraphView") {
        this.$router.push({ path: "/home/houseManagement/houseView" });
      }
      if (!this.isGraphPage && !this.isDistributePage && val === "") {
        eventBus.$emit("hideBuildDetail");
        eventBus.$emit("allBmMarker");
        const treeRootNode = this.$refs.tree.root;
        treeRootNode.childNodes = [];
        this.loadNode(treeRootNode, this.resolve);
      } else if (
        this.activeButtonIndex === 0 &&
        !this.isGraphPage &&
        !this.isDistributePage &&
        val !== ""
      ) {
        let tempVal = "";
        for (let i = 0; i < this.treeYardList.length; i++) {
          if (this.treeYardList[i].id === val) {
            tempVal = this.treeYardList[i].value;
            break;
          }
        }
        eventBus.$emit("hideBuildDetail");
        eventBus.$emit("updateBmMarker", tempVal);
        const treeRootNode = this.$refs.tree.root;
        treeRootNode.childNodes = [];
        this.loadNode(treeRootNode, this.resolve);
      } else if (
        this.activeButtonIndex === 1 &&
        !this.isGraphPage &&
        !this.isDistributePage &&
        val !== ""
      ) {
        let tempVal = "";
        for (let i = 0; i < this.treeUnitList.length; i++) {
          if (this.treeUnitList[i].unitId === val) {
            if (this.treeUnitList[i].unitIds) {
              tempVal = this.treeUnitList[i].unitIds;
            }
            break;
          }
        }
        eventBus.$emit("hideBuildDetail");
        eventBus.$emit("allBmMarker", tempVal);
        const treeRootNode = this.$refs.tree.root;
        treeRootNode.childNodes = [];
        this.loadNode(treeRootNode, this.resolve);
      } else if (this.isGraphPage || this.isDistributePage) {
        eventBus.$emit("clearCanvas");
        const treeRootNode = this.$refs.tree.root;
        treeRootNode.childNodes = [];
        this.loadNode(treeRootNode, this.resolve);
      }
    },
    renderContent(h, { node, data, store }) {
      if (
        this.activeButtonIndex === 1 &&
        !this.isGraphPage &&
        !this.isDistributePage
      ) {
        if (data.type === 3) {
          return (
            <span class="custom-tree-node">
              <span class="iconfont">&#xe7c8;</span>
              <el-tooltip
                class="item"
                effect="dark"
                content={data.text}
                placement="right"
                disabled={data.showTooltip}
              >
                <span class="show-ellipsis">{data.text}</span>
              </el-tooltip>
            </span>
          );
        } else if (data.type === 4) {
          return (
            <span class="custom-tree-node">
              <span class="iconfont">&#xe888;</span>
              <el-tooltip
                class="item"
                effect="dark"
                content={data.text}
                placement="right"
                disabled={data.showTooltip}
              >
                <span class="show-ellipsis">{data.text}</span>
              </el-tooltip>
            </span>
          );
        } else {
          return (
            <span class="custom-tree-node">
              <el-tooltip
                class="item"
                effect="dark"
                content={data.text}
                placement="right"
                disabled={data.showTooltip}
              >
                <span class="show-ellipsis">{data.text}</span>
              </el-tooltip>
            </span>
          );
        }
      } else if (this.isDistributePage) {
        if (data.type === 1) {
          return (
            <span class="custom-tree-node distributeTree">
              <span class="iconfont">&#xe7c8;</span>
              <el-tooltip
                class="item"
                effect="dark"
                content={data.text}
                placement="right"
                disabled={data.showTooltip}
              >
                <span class="show-ellipsis">{data.text}</span>
              </el-tooltip>
            </span>
          );
        } else if (data.type === 2) {
          if (
            data.applyStatus === 1 &&
            (data.handleWay === 3 ||
              data.handleWay === 4 ||
              data.handleWay === 7)
          ) {
            return (
              <span class="custom-tree-node distributeTree">
                <span class="iconfont">&#xe888;</span>
                <el-tooltip
                  class="item"
                  effect="dark"
                  content={data.text}
                  placement="right"
                  disabled={data.showTooltip}
                >
                  <span class="show-ellipsis">{data.text}</span>
                </el-tooltip>
                <span class="iconfont">&#xe640;</span>
              </span>
            );
          } else {
            return (
              <span class="custom-tree-node distributeTree">
                <span class="iconfont">&#xe888;</span>
                <el-tooltip
                  class="item"
                  effect="dark"
                  content={data.text}
                  placement="right"
                  disabled={data.showTooltip}
                >
                  <span class="show-ellipsis">{data.text}</span>
                </el-tooltip>
              </span>
            );
          }
        } else {
          return (
            <span class="custom-tree-node distributeTree">
              <span class="show-ellipsis">{data.text}</span>
            </span>
          );
        }
      } else {
        if (data.type === 1) {
          return (
            <span class="custom-tree-node">
              <span class="iconfont">&#xe7c8;</span>
              <el-tooltip
                class="item"
                effect="dark"
                content={data.text}
                placement="right"
                disabled={data.showTooltip}
              >
                <span class="show-ellipsis">{data.text}</span>
              </el-tooltip>
            </span>
          );
        } else if (data.type === 2) {
          return (
            <span class="custom-tree-node">
              <span class="iconfont">&#xe888;</span>
              <el-tooltip
                class="item"
                effect="dark"
                content={data.text}
                placement="right"
                disabled={data.showTooltip}
              >
                <span class="show-ellipsis">{data.text}</span>
              </el-tooltip>
            </span>
          );
        } else {
          return (
            <span class="custom-tree-node">
              <span class="show-ellipsis">{data.text}</span>
            </span>
          );
        }
      }
    },
    changePassword() {
      this.changePasswordDialogVisible = true;
    },
    changePasswordSubmitForm(formName) {
      this.$refs[formName].validate((valid) => {
        const username = sessionStorage.getItem("username");
        if (valid) {
          updatePassWord(
            this[formName].newPassword,
            this[formName].password,
            username
          )
            .then((res) => {
              this.$message({
                message: "修改密码成功!",
                type: "success",
              });
              setTimeout(() => {
                this.changePasswordDialogVisible = false;
              }, 1000);
            })
            .catch((err) => {
              const msg = err.result ? err.result : "修改密码失败";
              this.$message({
                message: msg,
                type: "error",
              });
            });
        } else {
          return false;
        }
      });
    },
    changePasswordDialogOpen() {
      this.$nextTick(() => {
        this.$refs["changePasswordForm"].clearValidate();
      });
    },
    changePasswordDialogClosed() {
      this.changePasswordForm.password = "";
      this.changePasswordForm.newPassword = "";
      this.changePasswordForm.checkNewPassword = "";
    },
    getStrLen(str) {
      var len = 0;
      for (var i = 0; i < str.length; i++) {
        var c = str.charCodeAt(i);
        if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {
          len++;
        } else {
          len += 2;
        }
      }
      return len <= 26;
    },
    deleteRouterLabel(key) {
      for (let i = 0; i < this.historyPages.length; i++) {
        if (this.historyPages[i].key === key) {
          this.historyPages.splice(i, 1);
        }
      }
    },
  },
  created() {
    // 获取用户名
    this.user_name = sessionStorage.getItem("name");
    this.roleBanner = sessionStorage.getItem("roleBanner") - 0;
    getRouterList()
      .then((res) => {
        this.allNavList = res;
        for (let i = 0; i < res.length; i++) {
          const key = res[i].key;
          const children = res[i].children;
          if (key === "repairManagement") {
            this.repairBacklogNavList = children;
            children.forEach((item) => {
              if (item.key === "waittingThing") {
                this.showRepairBacklogBtn = true;
              }
              if (item.key === "repairRemind") {
                this.showRepairRemindBtn = true;
              }
            });
          }
          if (key === "disposeManagement") {
            this.disposeBacklogNavList = children;
            children.forEach((item) => {
              if (item.key === "disposeBacklog") {
                this.showDisposeBacklogBtn = true;
              }
              if (item.key === "rentManagement") {
                this.showRentRemindBtn = true;
              }
              if (item.key === "rentManagement") {
                this.showRentExpireBtn = true;
              }
            });
          }
          if (key === "configManagement") {
            this.configBacklogNavList = children;
            children.forEach((item) => {
              if (item.key === "configBacklog") {
                this.showConfigBacklogBtn = true;
              }
            });
          }
          if (key === "houseManagement") {
            children.forEach((item) => {
              if (item.key === "houseGraph") {
                this.editGraph = true;
              }
            });
          }
          if (res[i].key === this.menuKey) {
            this.navlist = res[i].children;
            this.$store.commit("changeNavList", this.navlist);
            for (let j = 0; j < this.navlist.length; j++) {
              if (this.navlist[j].key === this.path) {
                if (this.historyPages.length > 4) {
                  this.historyPages.shift();
                }
                let pushFlag = true;
                for (let n = 0; n < this.historyPages.length; n++) {
                  if (this.historyPages[n].key === this.path) {
                    pushFlag = false;
                  }
                  break;
                }
                if (pushFlag) {
                  this.historyPages.push({
                    name: this.navlist[j].name,
                    key: this.navlist[j].key,
                    path: this.navlist[j].path,
                  });
                }
                break;
              }
            }
          }
        }
      })
      .catch((err) => {
        const msg = err.result ? err.result : "路由信息获取失败！";
        this.$message({
          message: msg,
          type: "error",
        });
      });
  },
  mounted() {
    // 屏幕高度自适应
    this.clientHeight = $(window).height();
    window.onresize = () => {
      this.clientHeight = $(window).height();
    };
    // aside的显示隐藏
    const self = this;
    $("#asideCollapseBtn").on("click", () => {
      const windowScreenWidth = window.screen.width;
      let collapseW;
      if (windowScreenWidth <= 1366) {
        collapseW = 200;
      } else {
        collapseW = 273;
      }
      let w = self.collapseFlag ? 6 : collapseW;
      eventBus.$emit("collapse", w);
      this.$refs.header.$emit("collapse", w);
      $(".el-aside").animate({ width: w }, 600, () => {
        this.collapseFlag = !this.collapseFlag;
        this.asideArrowIcon = this.collapseFlag
          ? require("../../assets/imgs/aside-arrow-left.png")
          : require("../../assets/imgs/aside-arrow-right.png");
      });
    });

    // 刷新还原页面
    if (this.path_ === "houseGraphView") {
      this.$router.push({ path: "/home/houseManagement/houseView" });
    }
    sessionStorage.removeItem("yardId");
    sessionStorage.removeItem("buildId");
    sessionStorage.removeItem("floorName");
    sessionStorage.removeItem("unitId");
    this.getRemindTipsNum();
    this.getThingTipsNum();
    this.getThingListTipsNum();
    eventBus.$off("updateBacklogTips");
    eventBus.$off("updateRemindTips");
    eventBus.$off("activeTree");
    eventBus.$on("updateBacklogTips", () => {
      this.getThingTipsNum();
      this.getThingListTipsNum();
    });
    eventBus.$on("updateRemindTips", () => {
      this.getRemindTipsNum();
    });
    eventBus.$on("activeTree", () => {
      const yardId = parseInt(sessionStorage.getItem("yardId"));
      const buildId = parseInt(sessionStorage.getItem("buildId"));
      const floorName = sessionStorage.getItem("floorName");
      let defaultExpandedkeysArr = [yardId, buildId];
      this.defaultExpandedkeys = defaultExpandedkeysArr;
      const treeNodeId = `${buildId}:${floorName}`;
      this.$refs.tree.setCurrentKey(treeNodeId);
    });
  },
};
</script>

<style>
.mainDiv {
  padding: 50px 50px 20px 50px;
  height: 100%;
  width: 100%;
  box-sizing: border-box;
  position: absolute;
  left: 0;
  top: 0;
}
.el-header,
.el-main {
  padding: 0px 0px;
  position: relative;
}
.el-aside {
  background-color: rgb(0, 79, 122);
  position: relative;
  overflow: hidden;
}
.el-footer {
  height: 17px;
  background: linear-gradient(0deg, #025e90, #0e84c1); /* 标准的语法 */
}
.top-child-menu {
  position: absolute;
  width: 100%;
  z-index: 10;
}
.li-title:hover {
  color: #3ed2d7;
  cursor: pointer;
}
.center-child-menu .router-active {
  color: #3ed2d7;
}
.center-child-menu .router-active .under-border,
.li-title:hover .under-border {
  border-top: 2px solid #3ed2d7;
}
.li-title:active {
  color: #4af5fb;
}
.li-title:active .under-border {
  border-top: 2px solid #4af5fb;
}
.under-border {
  position: relative;
  top: -4px;
  width: 50px;
  margin-left: calc(50% - 25px);
  max-height: 2px;
  flex: center;
}
.li-title {
  display: flex;
  flex-direction: column;
  font-size: 16px;
  font-family: "Microsoft Yahei";
  font-weight: bold;
}
.aside-main {
  position: absolute;
  width: calc(100% - 6px);
  height: 100%;
}
.aside-top {
  padding-left: 5px;
}
.aside-top > img {
  width: 100%;
}
.aside-center {
  background: #e6f4fa url("../../assets/imgs/asideBg.png") bottom center
    no-repeat;
  height: calc(100% - 57px);
  margin: 0px 0px 0px 5px;
}
.button-float {
  float: left;
  padding: 30px 30px 10px 30px;
  width: 55px;
}
.button-box {
  text-align: center;
  padding-top: 20px;
}
.button-box .button:first-of-type {
  margin-right: 10px;
}
.button-box .button:last-of-type {
  margin-left: 10px;
}
.button-box .button {
  color: white;
  background: url("../../assets/imgs/button-hover.png") center center no-repeat;
  border: none;
  width: 77px;
  height: 32px;
  line-height: 32px;
}
.button-box .button:hover {
  background-image: url("../../assets/imgs/button-active.png");
}
.button-box .active-button {
  background-image: url("../../assets/imgs/button-active.png");
}
.select-box {
  padding-bottom: 20px;
  padding-top: 20px;
  text-align: center;
}
.child-menu-center {
  text-align: center;
  width: 100%;
  margin: 0 auto;
  display: flex;
  justify-content: center;
}
.child-menu-top {
  width: 100%;
  height: 8px;
  background: url("../../assets/imgs/childmenu-top.png") center center;
}
.left-child-menu {
  background: url("../../assets/imgs/left-menu.png") center center no-repeat;
  height: 51px;
  width: 114px;
  float: left;
}
.center-child-menu {
  background: url("../../assets/imgs/center-menu.png") center center no-repeat;
  background-size: cover;
  height: 51px;
  float: left;
  width: auto;
  line-height: 41px;
  color: white;
}
.center-child-menu li {
  display: -webkit-inline-box;
}
.center-child-menu li:not(:last-child):after {
  content: url("../../assets/imgs/separate.png");
  margin: 30px 15px;
  text-decoration: initial;
  position: relative;
  top: 5px;
}
.right-child-menu {
  background: url("../../assets/imgs/right-menu.png") center center no-repeat;
  height: 51px;
  width: 114px;
  float: left;
}
.tree-box {
  width: 100%;
  height: calc(100% - 124px);
  overflow-y: auto;
}
.footer {
  height: 10px;
  position: absolute;
  bottom: 0px;
  background-color: rgb(0, 79, 122);
  width: calc(100% - 273px);
}
.footer-all {
  height: 10px;
  position: absolute;
  bottom: 0px;
  background-color: rgb(0, 79, 122);
  width: 100%;
}
.aside-collapse-btn {
  width: 6px;
  height: 100%;
  background: rgb(0, 79, 122);
  position: absolute;
  top: 0;
  right: 0;
}
.aside-collapse-btn > .aside-collapse-btn-inner {
  position: absolute;
  top: 50%;
  margin-top: -40px;
  left: 0;
  width: 6px;
  height: 80px;
  line-height: 80px;
  background-color: rgb(73, 199, 245);
  color: #ffffff;
  cursor: pointer;
  overflow: hidden;
}
.aside-collapse-btn > .aside-collapse-btn-inner:hover {
  background-color: #23a0d2;
}
.tree-box .el-tree {
  background: inherit;
}
.tree-box .el-tree .el-tree-node__label {
  font-size: 15px;
  color: #333;
}
.tree-box .el-tree .el-tree-node__content:hover {
  background-color: #34b4ff;
}
.tree-box
  .el-tree--highlight-current
  .el-tree-node.is-current
  > .el-tree-node__content {
  background-color: #34b4ff;
  color: #ffffff;
}
.tree-box
  .el-tree--highlight-current
  .el-tree-node.is-current
  > .el-tree-node__content
  > .el-tree-node__label {
  color: #ffffff;
}
.el-tree-node__expand-icon {
  color: #333;
}
.change-password-dialog .step-box {
  padding-bottom: 50px;
  box-sizing: border-box;
  width: 80%;
  position: absolute;
  top: 110px;
  left: 50%;
  transform: translateX(-50%);
  border-bottom: 1px solid #9f9f9f;
}
.change-password-dialog .change-password-form {
  box-sizing: border-box;
  width: 50%;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}
.change-password-dialog .el-dialog {
  height: 400px;
}
.change-password-dialog .change-password-form .el-form-item__content {
  width: 50%;
}
.change-password-dialog
  .change-password-form
  .el-form-item__content
  .el-input__inner {
  width: 100%;
}
.change-password-dialog .change-password-form .form-tips {
  position: absolute;
  left: calc(100% + 10px);
  font-size: 12px;
  color: #666;
  width: 270px;
  line-height: 20px;
  top: 9px;
}
.change-password-dialog .btn-box {
  position: absolute;
  top: 300px;
  left: 0;
  text-align: center;
  width: 100%;
}
.distributeTree .show-ellipsis {
  width: calc(100% - 40px);
}
.el-aside {
  width: 273px;
}
@media only screen and (max-width: 1366px) {
  .el-aside {
    width: 200px;
  }
  .button-box .button {
    width: 70px;
    height: auto;
    line-height: 32px;
    background-size: contain;
  }
  .aside-center {
    height: calc(100% - 40px);
  }
  .select-box {
    padding: 15px;
  }
}
</style>