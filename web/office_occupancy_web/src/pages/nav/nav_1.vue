<template>
  <section id="navPage">
    <div class="nav-outer-box">
      <div class="nav-btn-box">
        <div class="nav-title">我的工作台</div>
        <div class="nav-inner-box clearfix">
          <div class="nav-item-box" v-for="(item, index) in list" :key="index">
            <img :src="getImgUrl(item.menuImgVirtualUrl)" @click="routerLink(item.children)" />
            <p>
              <span @click="routerLink(item.children)">{{item.name}}</span>
            </p>
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
      list: [],
      userId: '',
      token: '',
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
    },
    // 获取图片链接
    getImgUrl(url) {
      return `${BASE_URL}${url_name_1}${url}?access_token=${this.token}`;
    }
  },
  mounted() {
  },
  created() {
    this.token = sessionStorage.getItem('TOKEN');
    this.roleBanner = sessionStorage.getItem('roleBanner');
    if (this.PRO_TYPE === 'yc') {
      this.footerTitleUrl = this.roleBanner === '0' ? require(`../../assets/imgs/footer-title-style-1-${this.PRO_TYPE}-2.png`) : require(`../../assets/imgs/footer-title-style-1-${this.PRO_TYPE}-1.png`);
    } else if (this.PRO_TYPE === 'sy') {
      this.footerTitleUrl = require(`../../assets/imgs/footer-title-style-1-${this.PRO_TYPE}.png`)
    } else if (this.PRO_TYPE === 'gl') {
      this.footerTitleUrl = require(`../../assets/imgs/footer-title-style-1-${this.PRO_TYPE}.png`)
    }
    // 获取路由信息列表
    getRouterList()
      .then(res => {
        this.list = res;
      })
      .catch(err => {
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
}
.nav-item-box {
  box-sizing: border-box;
  width: 20%;
  background-color: #ffffff;
  float: left;
  text-align: center;
}
.nav-inner-box > .nav-item-box {
  margin-top: 50px;
}
.nav-inner-box > .nav-item-box > img {
  width: 140px;
  height: 140px;
  cursor: pointer;
}
.nav-inner-box > .nav-item-box > p {
  padding-top: 20px;
  font-size: 22px;
  font-weight: 500;
  color: #333;
}
.nav-inner-box > .nav-item-box > p > span {
  cursor: pointer;
}
.nav-outer-box {
  margin: 32px 32px 0px 32px;
  position: relative;
  box-sizing: border-box;
}
.nav-btn-box {
  width: 100%;
  box-sizing: border-box;
  padding: 20px 20px 60px 20px;
  box-shadow: 0 0 30px #b6b6b6;
  border: 1px solid #eaedf2;
}
.nav-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}
.nav-item-box > img:hover {
  transform: scale(1.1);
}
.nav-desc-box {
  width: 38%;
  box-sizing: border-box;
  padding: 20px;
  box-shadow: 0 0 30px #b6b6b6;
  border: 1px solid #eaedf2;
  height: 100%;
  float: right;
}
.desc-item-box {
  box-sizing: border-box;
  width: 100%;
  background-color: #ffffff;
  text-align: left;
  font-size: 18px;
  color: #666;
  padding: 20px;
}
.desc-item-content {
  padding-top: 5px;
  line-height: 20px;
}
.desc-item-content > span:first-child {
  font-weight: 600;
}
.desc-item-content > span:last-child {
  font-family: "宋体";
  font-size: 14px;
}
.proj-name-box {
  height: 80px;
  position: relative;
  margin-top: 30px;
  margin-bottom: 30px;
}
.proj-name-box > img {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
@media only screen and (max-width: 1366px) {
  .nav-title,
  .nav-inner-box > .nav-item-box > p {
    font-size: 18px;
  }
  .nav-inner-box > .nav-item-box > img {
    width: 120px;
    height: 120px;
  }
  .desc-item-box {
    font-size: 16px;
  }
  .desc-item-content > span:last-child {
    font-size: 12px;
  }
  .proj-name-box > img {
    width: 50%;
  }
}
</style>