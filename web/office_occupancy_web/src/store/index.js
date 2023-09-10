import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import nav from './modules/nav'
import repaire from './modules/repaire'

Vue.use(Vuex);

export default new Vuex.Store({
  strict: process.env.NODE_ENV !== 'production', // 在非生产环境下，使用严格模式
  modules: {
     user,
     nav,
     repaire
  }
});