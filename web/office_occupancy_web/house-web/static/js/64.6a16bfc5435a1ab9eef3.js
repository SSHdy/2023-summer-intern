webpackJsonp([64],{"WTi+":function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=a("OMcc"),o=(a("yzMb"),a("+JHs")),i=a("fYBn"),r=a("02tb"),l=a("5kkW"),s=a("3a1n"),c={components:{tableComponent:o.a,cancelBtn:i.a,confirmBtn:r.a,searchBtn:l.a,resetBtn:s.a},data:function(){return{month:"",loading:null,tableData:[],Form:{repairType:"",auditCount:"",auditCountTb:"",auditCountHb:"",repairModuleArea:"",repairModuleAreaTb:"",repairModuleAreaHb:"",repairPaymentMoney:"",repairPaymentMoneyTb:"",repairPaymentMoneyHb:""},pickerOptions:{},datePickerDisabled:!1,currentTime:""}},created:function(){this.getStatisticsDateRange()},mounted:function(){this.month=this.formatDate((new Date).getTime(),"yyyy-MM"),this.currentTime=this.month,this.getList(this.month)},methods:{loadingStart:function(e){this.loading||(this.loading=this.$loading({lock:!0,text:"正在加载...",spinner:"el-icon-loading",target:e,customClass:"loading-box"}))},loadingEnd:function(){this.loading&&(this.loading.close(),this.loading=null)},changeSize:function(e){this.pageSize=e,this.getList()},changePage:function(e){this.currentPage=e,this.getList()},getList:function(e){var t=this;this.loadingStart(".table-box"),Object(n.e)(e).then(function(e){e.forEach(function(e){for(var a in e.repairType=t.getReviewAuditState(e.repairType),t.Form)e[a]||(e[a]="-")}),t.tableData=e,t.loadingEnd()}).catch(function(e){t.loadingEnd();var a=e.result?e.result:"维修工作统计信息获取失败";t.$message({message:a,type:"error"})})},getReviewAuditState:function(e){switch(e){case 1:return"零星维修";case 2:return"申请单位部门维修";case 3:return"机关管理局费用维修";case 4:return"合计"}},formatDate:function(e,t){var a=new Date(e),n={"M+":a.getMonth()+1,"d+":a.getDate(),"h+":a.getHours(),"m+":a.getMinutes(),"s+":a.getSeconds(),"q+":Math.floor((a.getMonth()+3)/3),S:a.getMilliseconds()};for(var o in/(y+)/.test(t)&&(t=t.replace(RegExp.$1,(a.getFullYear()+"").substr(4-RegExp.$1.length))),n)new RegExp("("+o+")").test(t)&&(t=t.replace(RegExp.$1,1==RegExp.$1.length?n[o]:("00"+n[o]).substr((""+n[o]).length)));return t},dateChange:function(e){var t=this;if(null===e)return this.$message({message:"月份不能为空！",type:"warning"}),void setTimeout(function(){t.month=t.currentTime},1);this.currentTime=e,this.getList(e)},getStatisticsDateRange:function(){var e=this;Object(n.g)(1).then(function(t){if(t){var a=t.maxMonth+"-31 23:59:59",n=t.minMonth+"-01 00:00:00";e.pickerOptions.disabledDate=function(e){return e.getTime()>new Date(a).getTime()||e.getTime()<new Date(n).getTime()}}else e.datePickerDisabled=!0}).catch(function(t){var a=t.result?t.result:"工作统计日期范围获取失败";e.$message({message:a,type:"error"})})}}},u={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{attrs:{id:"repair_month"}},[a("div",{staticClass:"query-box",attrs:{id:"queryBox"}},[a("div",{staticClass:"clearfix"},[a("div",{staticClass:"query-item"},[a("div",{staticClass:"query-title"},[e._v("选择月份：")]),e._v(" "),a("div",{staticClass:"query-input"},[a("el-date-picker",{attrs:{type:"month",placeholder:"选择月份","picker-options":e.pickerOptions,format:"yyyy 年 MM 月","value-format":"yyyy-MM",disabled:e.datePickerDisabled},on:{change:e.dateChange},model:{value:e.month,callback:function(t){e.month=t},expression:"month"}})],1)])])]),e._v(" "),a("div",{staticClass:"table-box"},[a("table-component",{attrs:{tableData:e.tableData}},[a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"repairType",label:"维修类型",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"auditCount",label:"审批数量",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"auditCountTb",label:"审批数量同比",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"auditCountHb",label:"审批数量环比",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"repairModuleArea",label:"维修面积(m²)",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"repairModuleAreaTb",label:"维修面积同比(m²)",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"repairModuleAreaHb",label:"维修面积环比(m²)",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"repairPaymentMoney",label:"付款金额(元)",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"repairPaymentMoneyTb",label:"付款金额同比(元)",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,prop:"repairPaymentMoneyHb",label:"付款金额环比(元)",align:"center"}})],1)],1)])},staticRenderFns:[]};var p=a("VU/8")(c,u,!1,function(e){a("ZGPZ")},null,null);t.default=p.exports},ZGPZ:function(e,t){}});
//# sourceMappingURL=64.6a16bfc5435a1ab9eef3.js.map