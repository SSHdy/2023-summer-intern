webpackJsonp([10],{"0xzQ":function(e,t,r){"use strict";function n(){var e="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";this.encode=function(r){var n,o,i,s,a,c,l,u="",d=0;for(r=t(r);d<r.length;)s=(n=r.charCodeAt(d++))>>2,a=(3&n)<<4|(o=r.charCodeAt(d++))>>4,c=(15&o)<<2|(i=r.charCodeAt(d++))>>6,l=63&i,isNaN(o)?c=l=64:isNaN(i)&&(l=64),u=u+e.charAt(s)+e.charAt(a)+e.charAt(c)+e.charAt(l);return u},this.decode=function(t){var n,o,i,s,a,c,l="",u=0;for(t=t.replace(/[^A-Za-z0-9\+\/\=]/g,"");u<t.length;)n=e.indexOf(t.charAt(u++))<<2|(s=e.indexOf(t.charAt(u++)))>>4,o=(15&s)<<4|(a=e.indexOf(t.charAt(u++)))>>2,i=(3&a)<<6|(c=e.indexOf(t.charAt(u++))),l+=String.fromCharCode(n),64!=a&&(l+=String.fromCharCode(o)),64!=c&&(l+=String.fromCharCode(i));return l=r(l)};var t=function(e){e=e.replace(/\r\n/g,"\n");for(var t="",r=0;r<e.length;r++){var n=e.charCodeAt(r);n<128?t+=String.fromCharCode(n):n>127&&n<2048?(t+=String.fromCharCode(n>>6|192),t+=String.fromCharCode(63&n|128)):(t+=String.fromCharCode(n>>12|224),t+=String.fromCharCode(n>>6&63|128),t+=String.fromCharCode(63&n|128))}return t},r=function(e){for(var t="",r=0,n=0,o=0,i=0;r<e.length;)(n=e.charCodeAt(r))<128?(t+=String.fromCharCode(n),r++):n>191&&n<224?(o=e.charCodeAt(r+1),t+=String.fromCharCode((31&n)<<6|63&o),r+=2):(o=e.charCodeAt(r+1),i=e.charCodeAt(r+2),t+=String.fromCharCode((15&n)<<12|(63&o)<<6|63&i),r+=3);return t}}r.d(t,"a",function(){return n})},"8BHQ":function(e,t,r){e.exports=r.p+"static/img/login-national-emblem.461f719.png"},DW41:function(e,t){},"J+bv":function(e,t,r){e.exports=r.p+"static/img/login-bg-2.efc638f.png"},"NXB/":function(e,t,r){e.exports=r.p+"static/img/loginry1.6fe8fcf.png"},QlWu:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=r("eOoE"),o=r("u3Mf"),i=r("0xzQ"),s=r("SE2n"),a={name:"Login",data:function(){return{clientHeight:"",PRO_TYPE:o.e,loginTitleUrl:"",bgUrl:"",user:{name:"",password:"",remeberPwd:!1},rules:{name:[{required:!0,message:"用户名不能为空",trigger:"blur"}],password:[{required:!0,message:"密码不能为空",trigger:"blur"}]}}},mounted:function(){var e=this;this.loginTitleUrl=r("NXB/"),this.bgUrl=r("mlLq"),console.log(this.bgUrl),this.clientHeight=""+document.documentElement.clientHeight,window.onresize=function(){e.clientHeight=""+document.documentElement.clientHeight},this.getCookie()},watch:{clientHeight:function(){this.changeFixed(this.clientHeight)}},methods:{changeFixed:function(e){this.$refs.login.style.height=e+"px"},login:function(){var e=this;this.$refs.loginForm.validate(function(t){if(!t)return!1;var r=(new i.a).encode("app:123456"),a=encodeURIComponent(e.user.name),c=encodeURIComponent(e.user.password),l=o.g+"/oauth/token?client_id=app&client_secret=123456&grant_type=password&username="+a+"&password="+c;n.a.post(l,null,{headers:{"Content-Type":"application/x-www-form-urlencoded",Authorization:"Basic "+r}}).then(function(t){sessionStorage.setItem("TOKEN",t.access_token),sessionStorage.setItem("REFRESH_TOKEN",t.refresh_token),e.user.remeberPwd?document.cookie.length||e.setCookie(e.user.name,e.user.password,7):e.clearCookie(),Object(s.a)(t.access_token).then(function(t){sessionStorage.setItem("name",t.name),sessionStorage.setItem("username",t.username),sessionStorage.setItem("userId",t.userId),sessionStorage.setItem("roleBanner",t.roleBanner),sessionStorage.setItem("telephone",t.telephone),e.$router.push({path:"/home/nav"})}).catch(function(t){var r=t.result?t.result:"用户信息获取失败！";e.$message({message:r,type:"error"})})}).catch(function(t){var r=void 0;t.error_description?(t.error_description=decodeURIComponent(t.error_description),r=t.error_description):r="token获取失败",e.$message({message:r,type:"error"})})})},setCookie:function(e,t,r){var n=new Date;n.setTime(n.getTime()+864e5*r);var o=(new i.a).encode(""+t);window.document.cookie="userName="+e+";path=/;expires="+n.toGMTString(),window.document.cookie="userPwd="+o+";path=/;expires="+n.toGMTString()},getCookie:function(){if(document.cookie.length>0){for(var e=document.cookie.split("; "),t=0;t<e.length;t++){var r=e[t].split("=");if("userName"==r[0])this.user.name=r[1];else if("userPwd"==r[0]){var n=(new i.a).decode(""+r[1]);this.user.password=n}}this.user.remeberPwd=!0}},clearCookie:function(){this.setCookie("","",-1)}}},c={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{ref:"login",staticClass:"login",attrs:{id:"login"}},[r("div",{staticClass:"login-bg-1"},[r("img",{attrs:{src:e.bgUrl}})]),e._v(" "),e._m(0),e._v(" "),r("div",{staticClass:"login-bg-3"},[r("img",{attrs:{src:e.loginTitleUrl}})]),e._v(" "),e._m(1),e._v(" "),r("el-form",{ref:"loginForm",staticClass:"msg",attrs:{model:e.user,"label-width":"25%",rules:e.rules,"status-icon":""},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key,"Enter"))return null;e.login()}}},[r("el-form-item",{attrs:{label:"用户名:",prop:"name"}},[r("el-input",{directives:[{name:"enter-trim",rawName:"v-enter-trim"}],model:{value:e.user.name,callback:function(t){e.$set(e.user,"name",t)},expression:"user.name"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"密 码:",prop:"password"}},[r("el-input",{directives:[{name:"enter-trim",rawName:"v-enter-trim"}],attrs:{type:"password"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key,"Enter"))return null;e.login()}},model:{value:e.user.password,callback:function(t){e.$set(e.user,"password",t)},expression:"user.password"}})],1),e._v(" "),r("el-form-item",[r("el-checkbox",{attrs:{label:"记住密码"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key,"Enter"))return null;e.login()}},model:{value:e.user.remeberPwd,callback:function(t){e.$set(e.user,"remeberPwd",t)},expression:"user.remeberPwd"}})],1),e._v(" "),r("el-form-item",[r("el-button",{staticClass:"loginbtn",attrs:{type:"warning",size:"medium"},on:{click:function(t){e.login()}}},[e._v("登录")])],1)],1)],1)},staticRenderFns:[function(){var e=this.$createElement,t=this._self._c||e;return t("div",{staticClass:"login-bg-2"},[t("img",{attrs:{src:r("J+bv")}})])},function(){var e=this.$createElement,t=this._self._c||e;return t("div",{staticClass:"login-bg-4"},[t("img",{attrs:{src:r("8BHQ")}})])}]};var l=r("VU/8")(a,c,!1,function(e){r("DW41")},null,null);t.default=l.exports},SE2n:function(e,t,r){"use strict";t.a=function(e){return new o.a(function(t,r){i.a.post(s.g+"/user/detail",null,{headers:{Authorization:"Bearer "+e}}).then(function(e){t(e)},function(e){r(e)})})};var n=r("//Fk"),o=r.n(n),i=r("eOoE"),s=r("u3Mf")},mlLq:function(e,t,r){e.exports=r.p+"static/img/login-bg-1-sy.d972f4e.png"}});
//# sourceMappingURL=10.2708cf6b40ffbd8d70a2.js.map