import axios from '@/axios/index'
import { url_name_2 } from "@/axios/global"
/**
 * 获取路由信息
 */
export function getRouterList() {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_2}/menu/list`, null).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}