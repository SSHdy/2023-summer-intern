// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import importDirective from '@/directive'
import Jquery_contextmenu from 'jquery-contextmenu'
import 'jquery-contextmenu/dist/jquery.contextMenu.css'
Vue.use(Jquery_contextmenu)
import ZkTable from 'vue-table-with-tree-grid'
Vue.component(ZkTable.name, ZkTable);

import {
  Input, Checkbox, CheckboxButton, CheckboxGroup, Form, FormItem, Button, Container, Header, Aside, Main, Select, Option, Tree, Pagination, Badge, Dialog, Table,
  TableColumn, Message, MessageBox, Loading, Icon, DatePicker, Upload, tooltip, Collapse, CollapseItem, Radio, RadioGroup, RadioButton, Progress, Tabs, TabPane,
  Carousel, CarouselItem, Steps, Step, Popover, Dropdown, DropdownMenu, DropdownItem, Slider, Col, Row
} from 'element-ui';
Vue.config.productionTip = false
Vue.component(Input.name, Input);
Vue.component(Checkbox.name, Checkbox);
Vue.component(CheckboxButton.name, CheckboxButton);
Vue.component(CheckboxGroup.name, CheckboxGroup);
Vue.component(Form.name, Form);
Vue.component(FormItem.name, FormItem);
Vue.component(Button.name, Button);
Vue.component(Container.name, Container);
Vue.component(Header.name, Header);
Vue.component(Aside.name, Aside);
Vue.component(Main.name, Main);
Vue.component(Select.name, Select);
Vue.component(Option.name, Option);
Vue.component(Tree.name, Tree);
Vue.component(Pagination.name, Pagination);
Vue.component(Badge.name, Badge);
Vue.component(Dialog.name, Dialog);
Vue.component(Table.name, Table);
Vue.component(TableColumn.name, TableColumn);
Vue.component(Message.name, Message);
Vue.component(Icon.name, Icon);
Vue.component(DatePicker.name, DatePicker);
Vue.component(Upload.name, Upload);
Vue.component(tooltip.name, tooltip);
Vue.component(Collapse.name, Collapse);
Vue.component(CollapseItem.name, CollapseItem);
Vue.component(Radio.name, Radio);
Vue.component(RadioGroup.name, RadioGroup);
Vue.component(RadioButton.name, RadioButton);
Vue.component(Progress.name, Progress);
Vue.component(Tabs.name, Tabs);
Vue.component(TabPane.name, TabPane);
Vue.component(Carousel.name, Carousel);
Vue.component(CarouselItem.name, CarouselItem);
Vue.component(Steps.name, Steps);
Vue.component(Step.name, Step);
Vue.component(Popover.name, Popover);
Vue.component(Dropdown.name, Dropdown);
Vue.component(DropdownMenu.name, DropdownMenu);
Vue.component(DropdownItem.name, DropdownItem);
Vue.component(Slider.name, Slider);
Vue.component(Col.name, Col);
Vue.component(Row.name, Row);
Vue.prototype.$loading = Loading.service;
Vue.prototype.$message = Message;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$alert = MessageBox.alert;

Vue.config.devtools = true

/**
 * 注册指令
 */
importDirective(Vue)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})

// Vue.directives('throttle', {
//   inserted: function (el, binding) {
//     let timer;
//     el.addEventListener('click', () => {
//       if (timer) {
//         clearTimeout(timer)
//       }
//       timer = setTimeout(() => {
//         binding.value()
//       }, 1000);
//     })
//   }
// })
