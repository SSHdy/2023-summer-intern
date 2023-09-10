<template>
  <section id="navPage">
    <div class="main">
      <div class="top-left">
        <div
          class="nav-item"
          v-for="(item, index) in list_1"
          :key="index"
          @click="routerLink(item.children)"
          :style="{ 'cursor': item.cursor ? item.cursor : 'not-allowed' }"
        >
          <div class="nav-item-group">
            <div class="iconfont nav-item-icon" :class="item.icon"></div>
            <div class="nav-item-name">{{item.name}}</div>
          </div>
        </div>
      </div>
      <div class="top-right">
        <div
          class="nav-item"
          v-for="(item, index) in list_2"
          :key="index"
          :style="{'width': item.width, 'height': item.height, 'margin-bottom': item.marginBottom, 'margin-left': item.marginLeft, 'background': item.background, 'cursor': item.cursor ? item.cursor : 'not-allowed'}"
          @click="routerLink(item.children)"
        >
          <div class="nav-item-group">
            <div class="iconfont nav-item-icon" :class="item.icon"></div>
            <div class="nav-item-name">{{item.name}}</div>
          </div>
        </div>
      </div>
      <div class="bottom">
        <div
          class="nav-item"
          v-for="(item, index) in list_3"
          :key="index"
          :style="{'width': item.width, 'height': item.height, 'margin-left': item.marginLeft, 'background': item.background, 'cursor': item.cursor ? item.cursor : 'not-allowed'}"
          @click="routerLink(item.children)"
        >
          <div class="nav-item-group">
            <div class="iconfont nav-item-icon" :class="item.icon"></div>
            <div class="nav-item-name">{{item.name}}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="proj-name-box">
      <img :src="footerTitleUrl" />
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
      list_1: [
        {
          name: '办公用房管理',
          key: 'houseManagement',
          icon: 'icon-fangwu'
        }
      ],
      list_2: [
        {
          name: '系统管理',
          key: 'systemManagement',
          icon: 'icon-xitongguanli-xiugai',
          background: '#00b7ff',
          width: 'calc(50% - 14px)',
          height: 'calc(50% - 14px)',
          marginBottom: '14px',
          marginLeft: '14px'
        },
        {
          name: '基础信息管理',
          key: 'baseInfoManagement',
          icon: 'icon-jichuxinxi',
          background: '#f59942',
          width: 'calc(50% - 14px)',
          height: 'calc(50% - 14px)',
          marginBottom: '14px',
          marginLeft: '14px'
        },
        {
          name: '维修管理',
          key: 'repairManagement',
          icon: 'icon-weixiu',
          background: '#fac834',
          width: 'calc(50% - 14px)',
          height: 'calc(50% - 14px)',
          marginBottom: '14px',
          marginLeft: '14px'
        },
        {
          name: '数据上报',
          key: 'dataReport',
          icon: 'icon-fuwufangquerenyushangbao',
          background: '#6bc73e',
          width: 'calc(50% - 14px)',
          height: 'calc(50% - 14px)',
          marginBottom: '14px',
          marginLeft: '14px'
        }
      ],
      list_3: [
        {
          name: '使用管理',
          key: 'useManagement',
          icon: 'icon-shiyongwendang',
          background: '#2f7fc3',
          width: 'calc((56% - 42px) / 4)',
          height: '100%',
          marginLeft: '0'
        },
        {
          name: '处置管理',
          key: 'disposeManagement',
          icon: 'icon-chuzhijilu',
          background: '#e84f9a',
          width: 'calc((56% - 42px) / 4)',
          height: '100%',
          marginLeft: '14px'
        },
        {
          name: '工作统计',
          key: 'workStatistics',
          icon: 'icon-tongjixiangmu-copy',
          background: '#6b6fd2',
          width: 'calc((56% - 42px) / 4)',
          height: '100%',
          marginLeft: '14px'
        },
        {
          name: '配置管理',
          key: 'configManagement',
          icon: 'icon-peizhiguanli',
          background: '#3298e9',
          width: 'calc((56% - 42px) / 4)',
          height: '100%',
          marginLeft: '14px'
        },
        {
          name: '档案管理',
          key: 'recordManagement',
          icon: 'icon-danganheji',
          background: '#2f7fc3',
          width: 'calc(22% - 14px)',
          height: '100%',
          marginLeft: '14px'
        },
        {
          name: '资产管理',
          key: 'assetManagement',
          icon: 'icon-zichanguanli',
          background: '#a54be2',
          width: 'calc(22% - 14px)',
          height: '100%',
          marginLeft: '14px'
        }
      ],
      userId: '',
      PRO_TYPE: PRO_TYPE,
      footerTitleUrl: ''
    }
  },
  methods: {
    routerLink(children) {
      if (children) {
        const url = children[0].path;
        this.$store.commit('changeNavList', children);
        if (url) {
          this.$router.push({ path: url });
        }
      }
    }
  },
  mounted() {
  },
  created() {
    this.roleBanner = sessionStorage.getItem('roleBanner');
    if (this.PRO_TYPE === 'yc') {
      this.footerTitleUrl = this.roleBanner === '0' ? require(`../../assets/imgs/footer-title-style-2-${this.PRO_TYPE}-2.png`) : require(`../../assets/imgs/footer-title-style-2-${this.PRO_TYPE}-1.png`);
    } else if (this.PRO_TYPE === 'sy') {
      this.footerTitleUrl = require(`../../assets/imgs/footer-title-style-2-${this.PRO_TYPE}.png`)
    } else if (this.PRO_TYPE === 'gl') {
      this.footerTitleUrl = require(`../../assets/imgs/footer-title-style-2-${this.PRO_TYPE}.png`)
    }
    // 获取路由信息列表
    getRouterList().then(res => {
      for (let i = 0; i < res.length; i++) {
        for (let j = 0; j < this.list_1.length; j++) {
          if (this.list_1[j].key === res[i].key) {
            this.list_1[j].children = res[i].children;
            this.$set(this.list_1[j], 'cursor', 'pointer')
            break;
          }
        }
        for (let j = 0; j < this.list_2.length; j++) {
          if (this.list_2[j].key === res[i].key) {
            this.list_2[j].children = res[i].children;
            this.$set(this.list_2[j], 'cursor', 'pointer')
            break;
          }
        }
        for (let j = 0; j < this.list_3.length; j++) {
          if (this.list_3[j].key === res[i].key) {
            this.list_3[j].children = res[i].children;
            this.$set(this.list_3[j], 'cursor', 'pointer')
            break;
          }
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
}
.main {
  width: 70%;
  height: calc(100% - 200px);
  position: absolute;
  top: 80px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-wrap: wrap;
}
.main > .top-left,
.main > .top-right {
  width: 50%;
  height: 67%;
}
.main > .top-right {
  display: flex;
  flex-wrap: wrap;
}
.main > .bottom {
  width: 100%;
  height: 32%;
  display: flex;
}
.main > .top-left > .nav-item {
  width: 100%;
  height: calc(100% - 14px);
  background: url("../../assets/imgs/nav-item-bg.png") no-repeat center center /
    100% 100%;
  color: #0593ec !important;
}
.nav-item {
  font-size: 32px;
  text-align: center;
  color: #ffffff;
  box-sizing: border-box;
  position: relative;
}
.nav-item > .nav-item-group {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 100%;
  text-align: center;
}
.nav-item > .nav-item-group > .nav-item-icon {
  font-size: 88px;
  padding-bottom: 15px;
}
.main > .top-left > .nav-item > .nav-item-group > .nav-item-icon {
  font-size: 150px;
  padding-bottom: 30px;
}
.proj-name-box {
  width: 856px;
  position: absolute;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
}
.proj-name-box > img {
  width: 100%;
}
@media screen and (max-width: 1680px) {
  .nav-item {
    font-size: 28px;
  }
  .nav-item > .nav-item-group > .nav-item-icon {
    font-size: 80px;
  }
}
@media screen and (max-width: 1440px) {
  .main {
    top: 30px;
    height: calc(100% - 100px);
  }
  .nav-item {
    font-size: 24px;
  }
  .nav-item > .nav-item-group > .nav-item-icon {
    font-size: 64px;
  }
  .main > .top-left > .nav-item > .nav-item-group > .nav-item-icon {
    font-size: 124px;
  }
  .proj-name-box {
    width: 600px;
    bottom: 16px;
  }
}
@media screen and (max-width: 1366px) {
  .main {
    top: 30px;
    height: calc(100% - 100px);
  }
  .nav-item {
    font-size: 22px;
  }
  .nav-item > .nav-item-group > .nav-item-icon {
    font-size: 60px;
  }
  .main > .top-left > .nav-item > .nav-item-group > .nav-item-icon {
    font-size: 120px;
  }
  .proj-name-box {
    width: 600px;
    bottom: 16px;
  }
}
</style>