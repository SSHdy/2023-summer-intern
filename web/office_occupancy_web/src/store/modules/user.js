import axios from '@/axios/index';
import { url_name_2 } from "@/axios/global"
const state = {
}

const mutations = {

}

const actions = {
  Logout(context, token) {
    if (token) {
      const url = `${url_name_2}/oauth/logout?access_token=${token}`;
      axios.post(url).then(response => {
        sessionStorage.clear();
      }).catch(err => {
        console.log(err);
      });
    }
  }
}

export default {
  state,
  actions,
  mutations
}