import axios from '@/axios/index'
import { url_name_1, url_name_2 } from "@/axios/global"

/**
 * 获取所有院落树（无权限）
 * @param {Number} id 树节点id
 * @param {Number} type 树的类型
 * @param {Number} buildId 楼座id
 */
export function getAllYardTree(id, type, buildId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/tree/yard/all`, {
            id: id,
            type: type,
            buildId: buildId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}


/**
 * 查询指定类型下的类目
 * @param {Number} categoryPcode 类目编号
 * @param {Number} type 资产类别
 */
export function getCategoryTree(categoryPcode, type) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/category/tree/category`, {
            categoryPcode: categoryPcode,
            type: type
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取院落树
 * @param {Number} id 树节点id
 * @param {Number} type 树的类型
 * @param {Number} searchYardId 查询院落的编号
 * @param {Number} buildId 楼座id(获取某层楼下房间时要传楼座的id)
 */
export function getYardTree(id, type, searchYardId, buildId) {
    const build_id = buildId ? buildId : null;
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/tree/yard/list`, {
            id: id,
            type: type,
            searchYardId: searchYardId,
            buildId: build_id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}
