import axios from 'axios'
import store from '@/store/index';
import router from '@/router/index'
import { BASE_URL, url_name_2 } from "@/axios/global"

function GetRequest(url, key) {
  var theRequest = new Object();
  if (url.indexOf("?") != -1) {
    var str = url.split("?")[1];
    var strs = str.split("&");
    for (var i = 0; i < strs.length; i++) {
      theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
    }
  }
  if (theRequest[key] !== undefined) {
    return true;
  } else {
    return false;
  }
}

axios.interceptors.request.use(config => {
  const grant_type_flag = GetRequest(config.url, 'grant_type');
  if (!grant_type_flag) {
    const token = sessionStorage.getItem('TOKEN');
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, err => {
  return Promise.reject(err);
});

axios.interceptors.response.use(data => {
  if (data.headers.file) {
    sessionStorage.setItem('CADdownloadFileName', data.headers.file);
  }
  return data.data
}, error => {
  switch (error.response.status) {
    case 401:
      const token = sessionStorage.getItem('REFRESH_TOKEN');
      store.dispatch('Logout', token);
      router.push({ path: '/login' });
      // if (token) {
      //   const url = `${url_name_2}/oauth/token?grant_type=refresh_token&refresh_token=${token}&client_id=app&client_secret=123456`
      //   axios.post(url, null, {
      //     headers: {
      //       'Content-Type': 'application/x-www-form-urlencoded',
      //       'Authorization': ''
      //     }
      //   }).then(response => {
      //     sessionStorage.setItem('TOKEN', response.access_token);
      //     sessionStorage.setItem('REFRESH_TOKEN', response.refresh_token);
      //   }).catch(err => {
      //     store.dispatch('Logout', token);
      //     router.push({ path: '/login' });
      //   });
      // };
      break;
    default:
      return Promise.reject(error.response.data)
  }
})
console.log(BASE_URL)
axios.defaults.baseURL = BASE_URL;
export default axios;