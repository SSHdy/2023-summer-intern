webpackJsonp([65],{NXtP:function(e,t){},Qy83:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=a("OMcc"),i=(a("yzMb"),a("+JHs")),r=a("fYBn"),l=a("02tb"),o=a("5kkW"),s=a("3a1n"),c={components:{tableComponent:i.a,cancelBtn:r.a,confirmBtn:l.a,searchBtn:o.a,resetBtn:s.a},data:function(){return{year:"",loading:null,tableData:[],Form:{handleType:"",auditCount:"",auditCountTb:"",auditCountHb:"",handleArea:"",handleAreaTb:"",handleAreaHb:"",handleMoney:"",handleMoneyTb:"",handleMoneyHb:""},pickerOptions:{},datePickerDisabled:!1,currentTime:""}},created:function(){this.getStatisticsDateRange()},mounted:function(){this.year=this.formatDate((new Date).getTime(),"yyyy"),this.currentTime=this.year,this.getList(this.year)},methods:{loadingStart:function(e){this.loading||(this.loading=this.$loading({lock:!0,text:"正在加载...",spinner:"el-icon-loading",target:e,customClass:"loading-box"}))},loadingEnd:function(){this.loading&&(this.loading.close(),this.loading=null)},changeSize:function(e){this.pageSize=e,this.getList()},changePage:function(e){this.currentPage=e,this.getList()},getList:function(e){var t=this;this.loadingStart(".table-box"),Object(n.d)(e).then(function(e){e.forEach(function(e){for(var a in e.handleType=t.getReviewAuditState(e.handleType),t.Form)e[a]||(e[a]="-")}),t.tableData=e,t.loadingEnd()}).catch(function(e){t.loadingEnd();var a=e.result?e.result:"处置工作统计信息获取失败";t.$message({message:a,type:"error"})})},getReviewAuditState:function(e){switch(e){case 1:return"转换用途";case 2:return"出租";case 3:return"拍卖";case 4:return"拆除";case 5:return"出借";case 6:return"征收";case 7:return"合计"}},formatDate:function(e,t){var a=new Date(e),n={"M+":a.getMonth()+1,"d+":a.getDate(),"h+":a.getHours(),"m+":a.getMinutes(),"s+":a.getSeconds(),"q+":Math.floor((a.getMonth()+3)/3),S:a.getMilliseconds()};for(var i in/(y+)/.test(t)&&(t=t.replace(RegExp.$1,(a.getFullYear()+"").substr(4-RegExp.$1.length))),n)new RegExp("("+i+")").test(t)&&(t=t.replace(RegExp.$1,1==RegExp.$1.length?n[i]:("00"+n[i]).substr((""+n[i]).length)));return t},dateChange:function(e){var t=this;if(null===e)return this.$message({message:"年份不能为空！",type:"warning"}),void setTimeout(function(){t.year=t.currentTime},1);this.currentTime=e,this.getList(e)},getStatisticsDateRange:function(){var e=this;Object(n.g)(2).then(function(t){if(t){var a=t.maxYear+"-12-31 23:59:59",n=t.minYear+"-01-01 00:00:00";e.pickerOptions.disabledDate=function(e){return e.getTime()>new Date(a).getTime()||e.getTime()<new Date(n).getTime()}}else e.datePickerDisabled=!0}).catch(function(t){var a=t.result?t.result:"工作统计日期范围获取失败";e.$message({message:a,type:"error"})})}}},u={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{attrs:{id:"handle_year"}},[a("div",{staticClass:"query-box",attrs:{id:"queryBox"}},[a("div",{staticClass:"clearfix"},[a("div",{staticClass:"query-item"},[a("div",{staticClass:"query-title"},[e._v("选择年份：")]),e._v(" "),a("div",{staticClass:"query-input"},[a("el-date-picker",{attrs:{type:"year",placeholder:"选择年份","picker-options":e.pickerOptions,format:"yyyy 年","value-format":"yyyy",disabled:e.datePickerDisabled},on:{change:e.dateChange},model:{value:e.year,callback:function(t){e.year=t},expression:"year"}})],1)])])]),e._v(" "),a("div",{staticClass:"table-box"},[a("table-component",{attrs:{tableData:e.tableData}},[a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"handleType",label:"处置类型",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"auditCount",label:"审批数量",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"auditCountHb",label:"审批数量环比",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"handleArea",label:"处置面积(m²)",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"handleAreaHb",label:"处置面积环比(m²)",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"handleMoney",label:"处置金额(元)",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"handleMoneyHb",label:"处置金额环比(元)",align:"center"}})],1)],1)])},staticRenderFns:[]};var d=a("VU/8")(c,u,!1,function(e){a("NXtP")},null,null);t.default=d.exports}});
//# sourceMappingURL=65.47272199d231fc53cf65.js.map