import axios from '@/axios/index'
import { url_name_1, url_name_2 } from "@/axios/global"
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

/**
 * 获取单位树
 * @param {Number} unitId 单位id
 * @param {Number} id 树节点id
 * @param {Number} type 树的类型
 * @param {Boolean} authority 用户权限
 * @param {Number} searchUnitId 查询单位的编号
 */
export function getUnitTree(unitId, id, type, authority, searchUnitId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/tree/unit/list`, {
            unitId: unitId,
            id: id,
            type: type,
            authority: authority,
            searchUnitId: searchUnitId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}


/**
 * 获取所有院落树（无权限）
 * @param {Number} id 树节点id
 * @param {Number} type 树的类型
 * @param {Number} buildId 楼座id
 * @param {Number} searchYardId 查询院落的编号
 */
export function getAllYardTree(id, type, buildId, searchYardId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/tree/yard/all`, {
            id: id,
            type: type,
            buildId: buildId,
            searchYardId: searchYardId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 修改密码
 * @param {Number} id 用户id
 */
export function updatePassWord(newPassword, password, username) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/user/updatePassWord`, {
            newPassword: newPassword,
            password: password,
            username: username
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 统计维修待办事项数量
 * @param {Number} id 用户id
 */
export function getTodoListNum(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/process/todoListNum`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 统计每个待办事项数量
 * @param {Number} id 用户id
 */
export function getTodoListProcessNum(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/process/todoListProcessNum`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 查询维修提醒数量
*/
export function getRemindNum() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/rent/remindNum`, null).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取院落树(带处置信息)
 * @param {Number} id 树节点id
 * @param {Number} type 树的类型
 * @param {Number} searchYardId 查询院落的编号
 * @param {Number} buildId 楼座id
 */
export function getHandleYardTree(id, type, searchYardId, buildId) {
    const build_id = buildId ? buildId : null;
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/tree/yard/listHandle`, {
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