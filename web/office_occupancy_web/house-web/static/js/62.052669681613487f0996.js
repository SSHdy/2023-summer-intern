webpackJsonp([62],{fPDR:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=n("OMcc"),o=(n("yzMb"),n("+JHs")),l=n("fYBn"),i=n("02tb"),r=n("5kkW"),s=n("3a1n"),c={components:{tableComponent:o.a,cancelBtn:l.a,confirmBtn:i.a,searchBtn:r.a,resetBtn:s.a},data:function(){return{month:"",loading:null,tableData:[],Form:{handleType:"",auditCount:"",auditCountTb:"",auditCountHb:"",handleArea:"",handleAreaTb:"",handleAreaHb:"",handleMoney:"",handleMoneyTb:"",handleMoneyHb:""},pickerOptions:{},datePickerDisabled:!1,currentTime:""}},created:function(){this.getStatisticsDateRange()},mounted:function(){this.month=this.formatDate((new Date).getTime(),"yyyy-MM"),this.currentTime=this.month,this.getList(this.month)},methods:{loadingStart:function(t){this.loading||(this.loading=this.$loading({lock:!0,text:"正在加载...",spinner:"el-icon-loading",target:t,customClass:"loading-box"}))},loadingEnd:function(){this.loading&&(this.loading.close(),this.loading=null)},changeSize:function(t){this.pageSize=t,this.getList()},changePage:function(t){this.currentPage=t,this.getList()},getList:function(t){var e=this;this.loadingStart(".table-box"),Object(a.c)(t).then(function(t){t.forEach(function(t){for(var n in t.handleType=e.getReviewAuditState(t.handleType),e.Form)t[n]||(t[n]="-")}),e.tableData=t,e.loadingEnd()}).catch(function(t){e.loadingEnd();var n=t.result?t.result:"处置工作统计信息获取失败";e.$message({message:n,type:"error"})})},getReviewAuditState:function(t){switch(t){case 1:return"转换用途";case 2:return"出租";case 3:return"拍卖";case 4:return"拆除";case 5:return"出借";case 6:return"征收";case 7:return"合计"}},formatDate:function(t,e){var n=new Date(t),a={"M+":n.getMonth()+1,"d+":n.getDate(),"h+":n.getHours(),"m+":n.getMinutes(),"s+":n.getSeconds(),"q+":Math.floor((n.getMonth()+3)/3),S:n.getMilliseconds()};for(var o in/(y+)/.test(e)&&(e=e.replace(RegExp.$1,(n.getFullYear()+"").substr(4-RegExp.$1.length))),a)new RegExp("("+o+")").test(e)&&(e=e.replace(RegExp.$1,1==RegExp.$1.length?a[o]:("00"+a[o]).substr((""+a[o]).length)));return e},dateChange:function(t){var e=this;if(null===t)return this.$message({message:"月份不能为空！",type:"warning"}),void setTimeout(function(){e.month=e.currentTime},1);this.currentTime=t,this.getList(t)},getStatisticsDateRange:function(){var t=this;Object(a.g)(2).then(function(e){if(e){var n=e.maxMonth+"-31 23:59:59",a=e.minMonth+"-01 00:00:00";t.pickerOptions.disabledDate=function(t){return t.getTime()>new Date(n).getTime()||t.getTime()<new Date(a).getTime()}}else t.datePickerDisabled=!0}).catch(function(e){var n=e.result?e.result:"工作统计日期范围获取失败";t.$message({message:n,type:"error"})})}}},u={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"handle_month"}},[n("div",{staticClass:"query-box",attrs:{id:"queryBox"}},[n("div",{staticClass:"clearfix"},[n("div",{staticClass:"query-item"},[n("div",{staticClass:"query-title"},[t._v("选择月份：")]),t._v(" "),n("div",{staticClass:"query-input"},[n("el-date-picker",{attrs:{type:"month",placeholder:"选择月份","picker-options":t.pickerOptions,format:"yyyy 年 MM 月","value-format":"yyyy-MM",disabled:t.datePickerDisabled},on:{change:t.dateChange},model:{value:t.month,callback:function(e){t.month=e},expression:"month"}})],1)])])]),t._v(" "),n("div",{staticClass:"table-box"},[n("table-component",{attrs:{tableData:t.tableData}},[n("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"handleType",label:"处置类型",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"auditCount",label:"审批数量",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"auditCountTb",label:"审批数量同比",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"auditCountHb",label:"审批数量环比",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"handleArea",label:"处置面积(m²)",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"handleAreaTb",label:"处置面积同比(m²)",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"handleAreaHb",label:"处置面积环比(m²)",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"handleMoney",label:"处置金额(元)",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"handleMoneyTb",label:"处置金额同比(元)",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"handleMoneyHb",label:"处置金额环比(元)",align:"center"}})],1)],1)])},staticRenderFns:[]};var h=n("VU/8")(c,u,!1,function(t){n("uOqh")},null,null);e.default=h.exports},uOqh:function(t,e){}});
//# sourceMappingURL=62.052669681613487f0996.js.map