const state = {
  nav: [],
  allNav: []
}

const getters = {
  getNavList(state) {
    return state.nav
  },
  getAllNavList() {
    return state.allNav
  }
};

const mutations = {
  changeNavList(state, list) {
    state.nav = list;
  },
  changeAllNavList(state, list) {
    state.allNav = list;
  }
}

export default {
  state,
  getters,
  mutations
}