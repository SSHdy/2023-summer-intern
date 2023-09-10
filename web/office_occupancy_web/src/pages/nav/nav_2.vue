<template>
  <section id="navPage">
    <div class="main">
      <div
        v-for="(item, index) in list"
        :key="index"
        :class="[item.className_1, item.className_2]"
        @click="routerLink(item.children)"
      >
        <div class="iconfont" :class="item.icon"></div>
        <div class="circle-name">{{item.name ? item.name : ''}}</div>
      </div>
      <div class="stamp stamp-1"></div>
      <div class="stamp stamp-2"></div>
      <div class="stamp stamp-3"></div>
      <div class="stamp stamp-4"></div>
      <div class="stamp stamp-5"></div>
      <span class="iconfont">&#xe629;</span>
      <span class="iconfont">&#xe64e;</span>
      <span class="iconfont">&#xe617;</span>
    </div>
  </section>
</template>

<script>
import { getRouterList } from "@/axios/commonApi";
import { BASE_URL, url_name_1, PRO_TYPE } from "@/axios/global"
import { setTimeout } from 'timers';
export default {
  data() {
    return {
      list: [],
      userId: '',
      PRO_TYPE: PRO_TYPE,
      footerTitleUrl: ''
    }
  },
  methods: {
    routerLink(children) {
      const url = children[0].path;
      this.$store.commit('changeNavList', children);
      if (url) {
        this.$router.push({ path: url });
      }
    }
  },
  mounted() {
  },
  created() {
    // 获取路由信息列表
    getRouterList().then(res => {
      const circleNum = 12;
      for (let i = 0; i < 12; i++) {
        let obj = {};
        if (res[i]) {
          switch (res[i].key) {
            case 'houseManagement':
              obj.icon = 'icon-fangwu'
              break;
            case 'configManagement':
              obj.icon = 'icon-peizhiguanli'
              break;
            case 'assetManagement':
              obj.icon = 'icon-zichanguanli'
              break;
            case 'useManagement':
              obj.icon = 'icon-shiyongwendang'
              break;
            case 'repairManagement':
              obj.icon = 'icon-weixiu'
              break;
            case 'disposeManagement':
              obj.icon = 'icon-chuzhijilu'
              break;
            case 'recordManagement':
              obj.icon = 'icon-danganheji'
              break;
            case 'workStatistics':
              obj.icon = 'icon-tongjixiangmu-copy'
              break;
            case 'baseInfoManagement':
              obj.icon = 'icon-jichuxinxi'
              break;
            case 'dataReport':
              obj.icon = 'icon-fuwufangquerenyushangbao'
              break;
            case 'systemManagement':
              obj.icon = 'icon-xitongguanli-xiugai'
              break;
            case 'special':
              obj.icon = 'icon-zhuanxiangjiancha'
              break;
          }
          obj.name = res[i].name;
          obj.children = res[i].children;
          obj.key = res[i].key;
          obj.className_1 = 'circle-style-1';
        } else {
          obj.className_1 = 'circle-style-2';
          obj.icon = 'icon-quanxianguanli';
        }
        obj.className_2 = `circle_${i + 1}`;
        this.list.push(obj);
      }
      for (let i = 0; i < this.list.length; i++) {
        if (this.list[i].key === 'baseInfoManagement') {
          const className = this.list[i].className_2;
          if (className !== 'circle_7') {
            for (let j = 0; j < this.list.length; j++) {
              if (this.list[j].className_2 === 'circle_7') {
                this.list[j].className_2 = className;
                this.list[i].className_2 = 'circle_7';
                break;
              }
            }
          }
          break;
        }
      }
    }).catch(err => {
      const msg = err.result ? err.result : '路由信息获取失败！';
      this.$message({
        message: msg,
        type: "error"
      });
    });
  }
}
</script>

<style scoped>
#navPage {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url("../../assets/imgs/nav-bg-1.png") no-repeat center center /
    100% 100%;
  font-size: 16px;
}
.main {
  width: 12.51em;
  height: 6.8em;
  background: url("../../assets/imgs/nav-bg-2.png") no-repeat center center /
      100% 100%,
    url("../../assets/imgs/circle-bg-2.png") no-repeat 4.41em 1.65em / 4em 4em;
  position: absolute;
  top: 53%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 625%;
}
.main > .iconfont {
  position: absolute;
  color: #12a1e0;
  font-size: 0.48em;
}
.main > .iconfont:nth-of-type(1) {
  bottom: 2.8em;
  right: 0.33em;
  font-size: 0.36em;
}
.main > .iconfont:nth-of-type(2) {
  bottom: 5.42em;
  right: -1.38em;
}
.main > .iconfont:nth-of-type(3) {
  top: 6.75em;
  left: -0.56em;
}
.circle-style-1 {
  border-radius: 50%;
  border: 6px solid #2effff;
  background: #167dbc;
  position: absolute;
  box-sizing: border-box;
  box-shadow: 0 0 0.3em rgba(47, 255, 255, 0.5);
  text-align: center;
  cursor: pointer;
  z-index: 10;
}
.circle-style-2 {
  border-radius: 50%;
  border: 6px solid #1dc3f3;
  background: #167dbc;
  position: absolute;
  box-sizing: border-box;
  z-index: 10;
  text-align: center;
}
.circle-style-1:hover {
  border-color: #ffffff;
  box-shadow: 0 0 0.5em rgba(47, 255, 255);
}
.circle_1 {
  width: 2.3em;
  height: 2.3em;
  padding-top: 0.2em;
}
.circle_2,
.circle_3,
.circle_4,
.circle_5 {
  width: 1.5em;
  height: 1.5em;
  padding-top: 0.1em;
}
.circle_6,
.circle_7 {
  width: 1.8em;
  height: 1.8em;
  padding-top: 0.14em;
}
.circle_8,
.circle_9,
.circle_10,
.circle_11,
.circle_12 {
  width: 1.3em;
  height: 1.3em;
  padding-top: 0.08em;
}
.circle_1 {
  top: 2.5em;
  left: 5.26em;
}
.circle_2 {
  top: 4em;
  left: 3.16em;
}
.circle_3 {
  top: 2.76em;
  left: 1.14em;
}
.circle_4 {
  top: 1.04em;
  left: 4.55em;
}
.circle_5 {
  bottom: 1.7em;
  right: 0.46em;
}
.circle_6 {
  top: 0.62em;
  right: 4.25em;
}
.circle_7 {
  top: 0.88em;
  right: 1.32em;
}
.circle_8 {
  top: 0.82em;
  left: -1em;
}
.circle_9 {
  left: 1.73em;
  top: 0.6em;
}
.circle_10 {
  left: 3.27em;
  top: -0.44em;
}
.circle_11 {
  right: 3.24em;
  top: -0.44em;
}
.circle_12 {
  right: -0.85em;
  top: 0.42em;
}
.circle-name {
  color: #ffffff;
  width: 100%;
  text-align: center;
}
.circle_1 > .circle-name,
.circle_6 > .circle-name {
  font-size: 0.26em;
}
.circle_2 > .circle-name,
.circle_3 > .circle-name,
.circle_4 > .circle-name,
.circle_5 > .circle-name,
.circle_7 > .circle-name,
.circle_8 > .circle-name,
.circle_9 > .circle-name,
.circle_10 > .circle-name,
.circle_11 > .circle-name,
.circle_12 > .circle-name {
  font-size: 0.22em;
}
.circle_1.circle-style-2 {
  line-height: 2em;
  padding: 0;
  margin: 0;
}
.circle_2.circle-style-2,
.circle_3.circle-style-2,
.circle_4.circle-style-2,
.circle_5.circle-style-2 {
  line-height: 1.3em;
  padding: 0;
  margin: 0;
}
.circle_6.circle-style-2 {
  line-height: 1.5em;
  padding: 0;
  margin: 0;
}
.circle_7.circle-style-2 {
  line-height: 1.5em;
  padding: 0;
  margin: 0;
}
.circle_8.circle-style-2,
.circle_9.circle-style-2,
.circle_10.circle-style-2,
.circle_11.circle-style-2,
.circle_12.circle-style-2 {
  line-height: 1.1em;
  padding: 0;
  margin: 0;
}

.circle_1 .iconfont {
  font-size: 1em;
  color: #fff;
  margin-bottom: 0.05em;
}
.circle_2 .iconfont,
.circle_3 .iconfont,
.circle_4 .iconfont,
.circle_5 .iconfont {
  font-size: 0.62em;
  color: #fff;
  margin-bottom: 0.03em;
}
.circle_6 .iconfont {
  font-size: 0.82em;
  color: #fff;
  margin-bottom: 0.03em;
}
.circle_7 .iconfont {
  font-size: 0.76em;
  color: #fff;
  margin-bottom: 0.03em;
}
.circle_8 .iconfont,
.circle_9 .iconfont,
.circle_10 .iconfont,
.circle_11 .iconfont,
.circle_12 .iconfont {
  font-size: 0.52em;
  color: #fff;
  margin-bottom: 0.03em;
}

.stamp {
  position: absolute;
  width: 1.48em;
  height: 1.48em;
  background: url("../../assets/imgs/stamp-icon.png") no-repeat center center /
    100% 100%;
}
.stamp-1 {
  bottom: 1em;
  left: -1.2em;
}
.stamp-2 {
  bottom: 0;
  left: 1.5em;
}
.stamp-3 {
  left: 4.7em;
  bottom: 0.3em;
}
.stamp-4 {
  bottom: 1.45em;
  right: 3.3em;
}
.stamp-5 {
  bottom: 0.9em;
  right: -2em;
}
@media screen and (max-width: 1920px) {
  .main {
    font-size: 625% !important;
  }
}
@media screen and (max-width: 1680px) {
  .main {
    font-size: 547% !important;
  }
}
@media screen and (max-width: 1440px) {
  .main {
    font-size: 469% !important;
  }
}
@media screen and (max-width: 1366px) {
  .main {
    font-size: 400% !important;
  }
}
</style>