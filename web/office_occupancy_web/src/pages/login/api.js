import axios from '@/axios/index'
import { url_name_2 } from "@/axios/global"

/**
 * 获取用户详情
 */
export function getUserDetail(token) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/user/detail`, null, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}
