const state = {
    repaireArray: []
  }
  
  const getters = {
    getreapireList(state) {
      return state.repaireArray
    }
  };
  
  const mutations = {
    getRepaireList(state, list) {
      state.repaireArray = list;
    }
  }
  
  export default {
    state,
    getters,
    mutations
  }