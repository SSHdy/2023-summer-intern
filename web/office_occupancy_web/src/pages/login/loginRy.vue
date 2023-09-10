<template>
  <div id="login" ref="login" class="login">
    <div class="login-bg-1">
      <img :src="bgUrl" />
    </div>
    <div class="login-bg-2">
      <img src="../../assets/imgs/login-bg-2.png" />
    </div>
    <div class="login-bg-3">
      <img :src="loginTitleUrl" />
    </div>
    <div class="login-bg-4">
      <img src="../../assets/imgs/login-national-emblem.png" />
    </div>
  </div>
</template>

<script>
import axios from "@/axios/index";
import { url_name_2, PRO_TYPE } from "@/axios/global";
import { Base64 } from "../../../static/base64/base64";
import { getUserDetail } from "./api";
export default {
  name: "Login",
  data() {
    return {
      clientHeight: "",
      PRO_TYPE: PRO_TYPE,
      loginTitleUrl: "",
      bgUrl: "",
      user: {
        name: "",
        password: "",
        remeberPwd: false,
      },
      rules: {
        name: [{ required: true, message: "用户名不能为空", trigger: "blur" }],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
        ],
      },
    };
  },
  mounted() {
    // this.loginTitleUrl = require(`../../assets/imgs/login-title-${this.PRO_TYPE}.png`);
    // this.bgUrl = require(`../../assets/imgs/login-bg-1-${this.PRO_TYPE}.png`);
    this.loginTitleUrl = require(`../../assets/imgs/loginry1.png`);
    this.bgUrl = require(`../../assets/imgs/login-bg-1-sy.png`);
    console.log(this.bgUrl);
    this.clientHeight = `${document.documentElement.clientHeight}`;
    window.onresize = () => {
      this.clientHeight = `${document.documentElement.clientHeight}`;
    };
    this.getCookie();
  },
  watch: {
    clientHeight() {
      this.changeFixed(this.clientHeight);
    },
  },
  methods: {
    changeFixed(clientHeight) {
      this.$refs.login.style.height = clientHeight + "px";
    },
    login() {
      // this.username = window.location.split('')
      this.user.name = this.$route.query.username;
      this.user.password = this.$route.query.password;
      const b = new Base64();
      const base64code = b.encode("app:123456");
      const username = encodeURIComponent(this.user.name);
      const password = encodeURIComponent(this.user.password);
      const url = `${url_name_2}/oauth/token?client_id=app&client_secret=123456&grant_type=password&username=${username}&password=${password}`;
      axios
        .post(url, null, {
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            Authorization: `Basic ${base64code}`,
          },
        })
        .then((response) => {
          sessionStorage.setItem("TOKEN", response.access_token);
          sessionStorage.setItem("REFRESH_TOKEN", response.refresh_token);
          if (this.user.remeberPwd) {
            if (!document.cookie.length) {
              this.setCookie(this.user.name, this.user.password, 7);
            }
          } else {
            this.clearCookie();
          }
          // 获取用户信息
          getUserDetail(response.access_token)
            .then((res) => {
              sessionStorage.setItem("name", res.name);
              sessionStorage.setItem("username", res.username);
              sessionStorage.setItem("userId", res.userId);
              sessionStorage.setItem("roleBanner", res.roleBanner);
              sessionStorage.setItem("telephone", res.telephone);
              this.$router.push({
                path: "/home/nav",
              });
            })
            .catch((err) => {
              const msg = err.result ? err.result : "用户信息获取失败！";
              this.$message({
                message: msg,
                type: "error",
              });
            });
        })
        .catch((err) => {
          let msg;
          if (err.error_description) {
            err.error_description = decodeURIComponent(err.error_description);
            msg = err.error_description;
          } else {
            msg = "token获取失败";
          }
          this.$message({
            message: msg,
            type: "error",
          });
        });
    },
    //设置cookie
    setCookie(c_name, c_pwd, exdays) {
      var exdate = new Date();
      exdate.setTime(exdate.getTime() + 24 * 60 * 60 * 1000 * exdays);
      const b = new Base64();
      const pwd = b.encode(`${c_pwd}`);
      window.document.cookie =
        "userName" + "=" + c_name + ";path=/;expires=" + exdate.toGMTString();
      window.document.cookie =
        "userPwd" + "=" + pwd + ";path=/;expires=" + exdate.toGMTString();
    },
    //读取cookie
    getCookie: function () {
      if (document.cookie.length > 0) {
        var arr = document.cookie.split("; ");
        for (var i = 0; i < arr.length; i++) {
          var arr2 = arr[i].split("=");
          if (arr2[0] == "userName") {
            this.user.name = arr2[1];
          } else if (arr2[0] == "userPwd") {
            const b = new Base64();
            const pwd = b.decode(`${arr2[1]}`);
            this.user.password = pwd;
          }
        }
        this.user.remeberPwd = true;
      }
    },
    //清除cookie
    clearCookie: function () {
      this.setCookie("", "", -1);
    },
  },
  created() {
    console.log(this.$route);
    this.login();
  },
};
</script> 

<style>
#login {
  width: 100%;
  position: relative;
}
.login-bg-1 > img,
.login-bg-2 > img,
.login-bg-4 > img {
  width: 100%;
  height: 100%;
}
.login-bg-1 {
  width: 100%;
  height: 50%;
  top: 0;
  left: 0;
  position: absolute;
}
.login-bg-2 {
  width: 100%;
  height: 53%;
  top: 47%;
  left: 0;
  position: absolute;
}
.login-bg-3 {
  position: absolute;
  top: 33%;
  left: 50%;
  transform: translateX(-50%);
}
.login-bg-4 {
  width: 120px;
  top: 16%;
  left: calc(50% - 60px);
  position: absolute;
}
.loginbtn {
  width: 100%;
}
.login .el-form-item__label {
  color: white;
}
.msg {
  position: absolute;
  top: calc(47% + 60px);
  color: white;
}
.loginButton {
  border: 0px;
  height: 36px;
  width: 100%;
}
.login .el-checkbox {
  color: white;
}
.remeberPwd {
  padding-left: 25%;
}
@media only screen and (max-width: 1920px) {
  .msg {
    left: calc(50% - 230px);
    width: 400px;
  }
}
@media only screen and (max-width: 1440px) {
  .msg {
    left: calc(50% - 200px);
    width: 350px;
  }
}
@media only screen and (max-width: 1366px) {
  .msg {
    left: calc(50% - 165px);
    width: 300px;
    top: 50%;
  }
  .login-bg-3 {
    height: 40px;
  }
  .login-bg-3 > img {
    height: 100%;
  }
  .login-bg-4 {
    width: 80px;
  }
  .el-form-item {
    margin-bottom: 10px;
  }
  .el-input__inner {
    height: 32px;
    line-height: 32px;
  }
}
</style> 