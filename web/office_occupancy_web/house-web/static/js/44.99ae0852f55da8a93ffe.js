webpackJsonp([44],{"3F/z":function(e,t){},EyH1:function(e,t,a){"use strict";(function(e){var i=a("fZjL"),n=a.n(i),s=a("d7EF"),l=a.n(s),r=a("W3Iv"),o=a.n(r),c=a("BO1k"),u=a.n(c),g=a("os9D"),m=(a("yzMb"),a("uSev")),d=a("dO4M"),p=a("FAvH"),h=a("GR1E"),f=a("fYBn"),v=a("02tb"),b=a("5kkW"),y=a("3a1n");t.a={components:{singleBtnGroup:d.a,tableBtnGroup:p.a,tablePagination:h.a,tableComponent:m.a,cancelBtn:f.a,confirmBtn:v.a,searchBtn:b.a,resetBtn:y.a},data:function(){return{estateType:"",manager:"",currentPage:1,pageSize:15,total:0,loading:null,tableHeight:0,selectData:[],tableData:[],dialogTitle:"",operateType:"add",dialogVisible:!1,estateTypeOpts:[{id:1,text:"自管"},{id:2,text:"第三方物业"}],Form:{estateId:null,estateType:"",manager:"",estatePhone:"",estateName:""},rules:{estateName:[{required:!0,message:"物业名称不能为空",trigger:"change"}],estateType:[{required:!0,message:"物业类型不能为空",trigger:"change"}],manager:[{required:!0,message:"管理人不能为空",trigger:"blur"}],estatePhone:[{required:!0,message:"物业电话不能为空",trigger:"blur"}]}}},methods:{loadingStart:function(e){this.loading||(this.loading=this.$loading({lock:!0,text:"正在加载...",spinner:"el-icon-loading",target:e,customClass:"loading-box"}))},loadingEnd:function(){this.loading&&(this.loading.close(),this.loading=null)},changeSize:function(e){this.pageSize=e,this.getList()},changePage:function(e){this.currentPage=e,this.getList()},getList:function(){var e=this;this.loadingStart(".table-box"),Object(g.q)(this.currentPage,this.pageSize,this.estateType,this.manager).then(function(t){e.tableData=t.records,e.total=t.total,e.tableData.forEach(function(e){1===e.estateType?e.estateTypeName="自管":e.estateTypeName="第三方物业"}),e.loadingEnd()}).catch(function(t){var a=t.result?t.result:"获取物业信息失败";e.$message({message:a,type:"error"}),e.loadingEnd()})},search:function(){this.currentPage=1,this.getList()},reset:function(){this.currentPage=1,this.estateType="",this.manager="",this.search()},add:function(){this.dialogVisible=!0,this.dialogTitle="新增物业信息",this.operateType="add"},singleDetle:function(e){var t=this;this.$confirm("此操作将永久删除物业"+e.estateName+", 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){Object(g.h)(parseInt(e.estateId)).then(function(e){t.$message({message:"删除物业信息成功!",type:"success"}),t.currentPage=1,t.getList()}).catch(function(e){var a=e.result?e.result:"删除物业信息失败";t.$message({message:a,type:"error"})})}).catch(function(){t.$message({type:"info",message:"已取消删除"})})},batchDelete:function(){var e=this,t=[];this.selectData.forEach(function(e,a){t.push(e.estateId)}),this.$confirm("此操作将永久删除这些物业信息, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){if(0===t.length)return e.$message({message:"请先勾选删除项！",type:"warning"}),!1;Object(g.e)(t).then(function(t){e.$message({message:"批量删除物业信息成功!",type:"success"}),e.currentPage=1,e.getList()}).catch(function(t){var a=t.result?t.result:"批量删除物业信息失败";e.$message({message:a,type:"error"})})}).catch(function(){e.$message({type:"info",message:"已取消删除"})})},singleUpdate:function(e){this.dialogVisible=!0,this.dialogTitle="修改物业信息",this.operateType="edit";var t=!0,a=!1,i=void 0;try{for(var n,s=u()(o()(e));!(t=(n=s.next()).done);t=!0){var r=n.value,c=l()(r,2),g=c[0],m=c[1];void 0!==this.Form[g]&&(this.Form[g]=m)}}catch(e){a=!0,i=e}finally{try{!t&&s.return&&s.return()}finally{if(a)throw i}}},handleSelectionChange:function(e){this.selectData=e},submitForm:function(e){var t=this;this.$refs[e].validate(function(e){if(!e)return!1;"add"===t.operateType?Object(g.a)(t.Form).then(function(e){t.$message({message:"新增物业信息成功",type:"success"}),t.dialogVisible=!1,t.getList()}).catch(function(e){var a=e.result?e.result:"新增物业信息失败";t.$message({message:a,type:"error"})}):Object(g.F)(t.Form).then(function(e){t.$message({message:"修改物业信息成功",type:"success"}),t.dialogVisible=!1,t.getList()}).catch(function(e){var a=e.result?e.result:"修改物业信息失败";t.$message({message:a,type:"error"})})})},dialogOpen:function(){var e=this;this.$nextTick(function(){e.$refs.Form.clearValidate()})},clearForm:function(e){for(var t in e){var a=this.getDataType(e[t]);if("String"===a)e[t]="";else if("Array"===a)e[t]=[];else if("Object"===a){n()(e[t]).length>0?this.clearForm(e[t]):e[t]={}}else e[t]=null}},dialogClosed:function(){this.clearForm(this.Form)},getDataType:function(e){var t=!0,a=!1,i=void 0;try{for(var n,s=u()(["String","Object","Number","Array","Function"]);!(t=(n=s.next()).done);t=!0){var l=n.value;if(Object.prototype.toString.call(e)==="[object "+l+"]")return l}}catch(e){a=!0,i=e}finally{try{!t&&s.return&&s.return()}finally{if(a)throw i}}},limitInt:function(e){this.Form[e]=this.Form[e].replace(/[^0-9]/g,"")}},mounted:function(){var t=this;this.getList(),setTimeout(function(){var a=e("#mainDiv").height(),i=e("#queryBox").outerHeight(!0),n=e("#operateBox").height();t.tableHeight=a-i-n},1)}}}).call(t,a("7t+N"))},KcxC:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=a("EyH1"),n={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{attrs:{id:"estate"}},[a("div",{staticClass:"query-box",attrs:{id:"queryBox"}},[a("div",{staticClass:"clearfix"},[a("div",{staticClass:"query-item"},[a("div",{staticClass:"query-title"},[e._v("物业类型：")]),e._v(" "),a("div",{staticClass:"query-input"},[a("el-select",{attrs:{placeholder:"请选择物业类型"},model:{value:e.estateType,callback:function(t){e.estateType=t},expression:"estateType"}},e._l(e.estateTypeOpts,function(e){return a("el-option",{key:e.id,attrs:{label:e.text,value:e.id}})}))],1)]),e._v(" "),a("div",{staticClass:"query-item",staticStyle:{"margin-left":"50px"}},[a("div",{staticClass:"query-title"},[e._v("管理人：")]),e._v(" "),a("div",{staticClass:"query-input"},[a("el-input",{directives:[{name:"enter-trim",rawName:"v-enter-trim"}],attrs:{placeholder:"请输入管理人",size:"small"},model:{value:e.manager,callback:function(t){e.manager=t},expression:"manager"}})],1)]),e._v(" "),a("div",{staticClass:"query-btn"},[a("search-btn",{on:{clickBtn:e.search}}),e._v(" "),a("reset-btn",{on:{clickBtn:e.reset}})],1)])]),e._v(" "),a("div",{staticClass:"table-box"},[a("table-component",{attrs:{tableData:e.tableData,tableHeight:e.tableHeight},on:{select:e.handleSelectionChange}},[a("el-table-column",{attrs:{type:"selection",width:"50",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{prop:"estateName",label:"物业名称",align:"center","show-overflow-tooltip":!0}}),e._v(" "),a("el-table-column",{attrs:{prop:"estateTypeName",label:"物业类型",align:"center","show-overflow-tooltip":!0}}),e._v(" "),a("el-table-column",{attrs:{prop:"manager",label:"管理人",align:"center","show-overflow-tooltip":!0}}),e._v(" "),a("el-table-column",{attrs:{prop:"estatePhone",label:"物业电话",align:"center","show-overflow-tooltip":!0}}),e._v(" "),a("el-table-column",{attrs:{label:"操作",width:"130",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("single-btn-group",{attrs:{isShowDetail:!1},on:{deleteClick:function(a){e.singleDetle(t.row)},updateClick:function(a){e.singleUpdate(t.row)}}})]}}])})],1),e._v(" "),a("div",{staticClass:"table-operate-box",attrs:{id:"operateBox"}},[a("div",{staticClass:"operate-btn"},[a("table-btn-group",{on:{batchDeleteClick:e.batchDelete,addClick:e.add}})],1),e._v(" "),a("div",{staticClass:"operate-pager"},[a("table-pagination",{attrs:{currentPage:e.currentPage,pageSize:e.pageSize,total:e.total},on:{"size-change":e.changeSize,"page-change":e.changePage}})],1)])],1),e._v(" "),a("el-dialog",{attrs:{visible:e.dialogVisible,"close-on-click-modal":!1,width:"780px",center:!0},on:{"update:visible":function(t){e.dialogVisible=t},closed:e.dialogClosed,open:e.dialogOpen}},[a("div",{staticClass:"dialog-title",attrs:{slot:"title"},slot:"title"},[a("span",[e._v(e._s(e.dialogTitle))])]),e._v(" "),a("el-form",{ref:"Form",attrs:{inline:!0,model:e.Form,rules:e.rules}},[a("el-form-item",{attrs:{label:"物业名称","label-width":"120px",prop:"estateName"}},[a("el-input",{directives:[{name:"enter-trim",rawName:"v-enter-trim"}],attrs:{placeholder:"请输入物业名称",size:"small"},model:{value:e.Form.estateName,callback:function(t){e.$set(e.Form,"estateName",t)},expression:"Form.estateName"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"物业类型","label-width":"120px",prop:"estateType"}},[a("el-select",{staticStyle:{width:"200px"},attrs:{placeholder:"请选择物业类型",size:"small"},model:{value:e.Form.estateType,callback:function(t){e.$set(e.Form,"estateType",t)},expression:"Form.estateType"}},e._l(e.estateTypeOpts,function(e){return a("el-option",{key:e.id,attrs:{label:e.text,value:e.id}})}))],1),e._v(" "),a("el-form-item",{attrs:{label:"管理人","label-width":"120px",prop:"manager"}},[a("el-input",{directives:[{name:"enter-trim",rawName:"v-enter-trim"}],attrs:{placeholder:"请输入管理人",size:"small"},model:{value:e.Form.manager,callback:function(t){e.$set(e.Form,"manager",t)},expression:"Form.manager"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"物业电话","label-width":"120px",prop:"estatePhone"}},[a("el-input",{attrs:{placeholder:"请输入物业电话",size:"small"},on:{blur:function(t){e.limitInt("estatePhone")}},nativeOn:{keyup:function(t){e.limitInt("estatePhone")}},model:{value:e.Form.estatePhone,callback:function(t){e.$set(e.Form,"estatePhone",t)},expression:"Form.estatePhone"}})],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("cancel-btn",{attrs:{fontSize:14},on:{clickBtn:function(t){e.dialogVisible=!1}}}),e._v(" "),a("confirm-btn",{attrs:{fontSize:14},on:{clickBtn:function(t){e.submitForm("Form")}}})],1)],1)],1)},staticRenderFns:[]};var s=function(e){a("3F/z")},l=a("VU/8")(i.a,n,!1,s,null,null);t.default=l.exports}});
//# sourceMappingURL=44.99ae0852f55da8a93ffe.js.map