webpackJsonp([42],{F9Ys:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=a("nA8G"),n={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{attrs:{id:"houseDetail"}},[a("div",{staticClass:"query-box",attrs:{id:"queryBox"}},[a("div",{staticClass:"clearfix"},[a("div",{staticClass:"query-item"},[a("div",{staticClass:"query-title"},[t._v("单位名称：")]),t._v(" "),a("div",{staticClass:"query-input"},[a("el-select",{staticStyle:{width:"100%"},attrs:{filterable:"",placeholder:"请选择单位名称"},model:{value:t.unitName,callback:function(e){t.unitName=e},expression:"unitName"}},t._l(t.unitOptions,function(t){return a("el-option",{key:t.unitId,attrs:{label:t.unitName,value:t.unitId}})}))],1)]),t._v(" "),a("div",{staticClass:"query-item",staticStyle:{"margin-left":"50px"}},[a("div",{staticClass:"query-title"},[t._v("使用人员名称：")]),t._v(" "),a("div",{staticClass:"query-input"},[a("el-input",{directives:[{name:"enter-trim",rawName:"v-enter-trim"}],attrs:{placeholder:"请输入使用人员名称"},model:{value:t.usePerson,callback:function(e){t.usePerson=e},expression:"usePerson"}})],1)]),t._v(" "),a("div",{staticClass:"query-btn"},[a("search-btn",{on:{clickBtn:t.search}}),t._v(" "),a("reset-btn",{on:{clickBtn:t.reset}})],1)])]),t._v(" "),a("div",{staticClass:"table-box"},[a("table-component",{attrs:{tableData:t.tableData,tableHeight:t.tableHeight}},[a("el-table-column",{attrs:{type:"index",width:"50",label:"序号",align:"center"}}),t._v(" "),a("el-table-column",{attrs:{prop:"yardName",label:"院落名称",align:"center","show-overflow-tooltip":!0}}),t._v(" "),a("el-table-column",{attrs:{prop:"buildName",label:"楼座名称",align:"center","show-overflow-tooltip":!0}}),t._v(" "),a("el-table-column",{attrs:{prop:"floorName",label:"楼层",align:"center","show-overflow-tooltip":!0}}),t._v(" "),a("el-table-column",{attrs:{prop:"houseNumber",label:"房间号",align:"center","show-overflow-tooltip":!0}}),t._v(" "),a("el-table-column",{attrs:{prop:"houseUseArea",label:"使用面积(m²)",align:"center","show-overflow-tooltip":!0}}),t._v(" "),a("el-table-column",{attrs:{prop:"unitName",label:"单位名称",align:"center","show-overflow-tooltip":!0}}),t._v(" "),a("el-table-column",{attrs:{prop:"usePerson",label:"使用人员名称",align:"center","show-overflow-tooltip":!0}})],1),t._v(" "),a("div",{staticClass:"table-operate-box",attrs:{id:"operateBox"}},[a("div",{staticClass:"operate-pager"},[a("table-pagination",{attrs:{currentPage:t.currentPage,pageSize:t.pageSize,total:t.total},on:{"size-change":t.changeSize,"page-change":t.changePage}})],1)])],1)])},staticRenderFns:[]};var s=function(t){a("HJIV")},l=a("VU/8")(i.a,n,!1,s,null,null);e.default=l.exports},HJIV:function(t,e){},nA8G:function(t,e,a){"use strict";(function(t){var i=a("os9D"),n=a("Oizb"),s=a("5kkW"),l=a("3a1n"),o=a("uSev"),r=a("GR1E"),u=a("OMJi");a.n(u);e.a={components:{searchBtn:s.a,resetBtn:l.a,tablePagination:r.a,tableComponent:o.a},data:function(){return{unitName:"",usePerson:"",isLeading:"",unitOptions:[],loading:null,tableData:[],tableHeight:0,currentPage:1,pageSize:15,total:0}},methods:{loadingStart:function(t){this.loading||(this.loading=this.$loading({lock:!0,text:"正在加载...",spinner:"el-icon-loading",target:t,customClass:"loading-box"}))},loadingEnd:function(){this.loading&&(this.loading.close(),this.loading=null)},changeSize:function(t){this.pageSize=t,this.getList()},changePage:function(t){this.currentPage=t,this.getList()},search:function(){this.currentPage=1,this.getList()},reset:function(){this.currentPage=1,this.unitName="",this.isLeading="",this.usePerson="",this.getList()},getUserUnitDic:function(){var t=this;Object(n.l)().then(function(e){t.unitOptions=e;t.unitOptions.unshift({unitId:"",unitName:"全部"})}).catch(function(e){var a=e.result?e.result:"单位下拉选项获取失败";t.$message({message:a,type:"error"})})},getList:function(){var t=this;this.loadingStart(".table-box"),Object(i.w)(this.currentPage,this.pageSize,this.unitName,this.isLeading,this.usePerson,null,null,null,null).then(function(e){t.tableData=e.records,t.total=e.total,t.tableData.forEach(function(t){0===t.isLeading?t.isLeadingTxt="领导":t.isLeadingTxt="一般人员"}),t.loadingEnd()}).catch(function(e){t.loadingEnd();var a=e.result?e.result:"超标单位列表获取失败";t.$message({message:a,type:"error"})})}},mounted:function(){var e=this;this.getUserUnitDic(),this.getList(),setTimeout(function(){var a=t("#mainDiv").height(),i=t("#queryBox").outerHeight(!0),n=t("#operateBox").height();e.tableHeight=a-i-n},1)}}}).call(e,a("7t+N"))}});
//# sourceMappingURL=42.f1826ce092470e2a6240.js.map