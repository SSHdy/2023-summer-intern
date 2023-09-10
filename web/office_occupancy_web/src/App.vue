<template>
  <div>
    <router-view />
  </div>
</template>

<script>
import $ from "jquery";
import * as THREE from 'three';
window.THREE = THREE;
import './assets/fonts/iconfont.css';

export default {
  name: "App"
};
</script>

<style>
/* css的初始化 start*/
* {
  margin: 0;
  padding: 0;
}
body {
  overflow: hidden;
  min-width: 1300px;
}
ul,
dl,
dd,
h1,
h2,
h3,
h4,
h5,
h6,
p,
section,
header,
footer,
article,
aside,
table {
  margin: 0;
  padding: 0;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  font-size: 14px;
  color: #333;
}
ul {
  list-style: none;
}
input {
  border: 0 none;
  outline-style: none;
  margin: 0;
  padding: 0;
}
button {
  border: 0;
  outline: none;
  cursor: pointer;
}
.clearfix:after {
  content: "";
  height: 0;
  line-height: 0;
  clear: both;
  display: block;
  visibility: hidden;
}
.clearfix {
  *zoom: 1;
}
/* css的初始化 end*/

/* 公共样式 start*/
.el-input__inner {
  border-color: rgb(200, 200, 200);
}
.el-select .el-input .el-select__caret {
  color: rgb(200, 200, 200);
}
.el-textarea__inner {
  border-color: rgb(200, 200, 200);
  font-family: "Microsoft YaHei";
  resize: none;
}
.el-form-item__label {
  color: #333;
}
.el-button--primary:focus,
.el-button--primary {
  background-color: #3891e0 !important;
  border-color: #3891e0 !important;
}
.el-button--primary:focus,
.el-button--primary:hover {
  background-color: #2378c4 !important;
  border-color: #2378c4 !important;
}
.el-dialog .el-form-item .el-input__inner {
  width: 200px;
}
.el-dialog__header {
  padding: 15px 20px;
  border-bottom: 1px solid #d8d8d8;
}
.el-dialog__headerbtn {
  top: 17px;
}
#queryBox .el-input__icon {
  line-height: 32px;
}
.dialog-title {
  text-align: left;
  height: 20px;
  line-height: 20px;
  font-size: 18px;
  padding-left: 10px;
  border-left: 2px solid #4490f1;
}
.el-dialog__body {
  color: #333;
}
.loading-box .el-loading-spinner i {
  font-size: 80px;
}
.loading-box .el-loading-spinner .el-loading-text {
  font-size: 20px;
}
.el-dialog__footer .dialog-footer {
  font-size: 0;
}
.el-dialog__footer .dialog-footer .el-button > span {
  font-size: inherit;
}
.show-ellipsis {
  width: calc(100% - 30px);
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  display: inline-block;
  position: relative;
  top: 4px;
  margin-left: 5px;
}
.custom-tree-node {
  width: calc(100% - 24px);
}
/* 公共样式 end*/
/* 字体图标 start*/
@font-face {
  font-family: "iconfont";
  src: url("./assets/fonts/iconfont.eot");
  src: url("./assets/fonts/iconfont.eot?#iefix") format("embedded-opentype"),
    url("./assets/fonts/iconfont.woff2") format("woff2"),
    url("./assets/fonts/iconfont.woff") format("woff"),
    url("./assets/fonts/iconfont.ttf") format("truetype"),
    url("./assets/fonts/iconfont.svg#iconfont") format("svg");
}
.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
/* 字体图标 end*/
@media only screen and (max-width: 1366px) {
  ul,
  dl,
  dd,
  h1,
  h2,
  h3,
  h4,
  h5,
  h6,
  p,
  section,
  header,
  footer,
  article,
  aside,
  table {
    font-size: 12px;
  }
  .query-item {
    font-size: 14px !important;
  }
  .reset-btn {
    font-size: 14px !important;
  }
  .search-btn {
    font-size: 14px !important;
  }
  .dialog-title {
    font-size: 16px !important;
  }
}
</style>


