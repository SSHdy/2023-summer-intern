import axios from 'axios';
import { BASE_URL, url_name_1, url_name_2 } from "@/axios/global"
/**
 * 获取路由信息
 */
export function getRouterList() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/menu/list`, null).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
 * 获取单位类型列表
 */
export function getUnitTypeList() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/unitType/list`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取房屋类型列表
 */
export function getHouseTypeList() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/houseType/list`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取所有院落信息列表
 * @param {Number} id 1:院落 2:楼栋 空:全部 
 */
export function getAllYardList(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/yard/allList`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取所有单位信息列表
 */
export function getAllUnitList() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/unit/all`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 用户权限下的单位列表
 */
export function getUserUnitList() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/unit/getUserUnit`, null).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询一个院落下的楼栋信息列表
 * @param {Number} id 院落id
 */
export function getSpecificbuildList(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/yard/buildList`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取所有职称信息列表
 * @param {Number} professionalLevel 职务级别(3/4/5)
 * @param {Number} unitGrade 单位级别(0/1)
 */
export function getAllProfessionList(professionalLevel, unitGrade) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/professional/all`, {
            professionalLevel: professionalLevel,
            unitGrade: unitGrade
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取在职情况列表
 */
export function workingConditions() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/statue/statue/list`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取区域列表
 */
export function getRegionList() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/region/all`, null).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 判断某一楼栋下是否关联配置
 * @param {Number} buildId 楼座id
 * @param {Array} floorNameList 楼层id数组
 */
export function isUnionConfig(buildId, floorNameList) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/isUnionConfig`, {
            buildId: buildId,
            floorNameList: floorNameList
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 显示相关联的配置申请通过楼层
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Number} buildId 楼座id
 * @param {Array} floorNameList 楼层id数组
 */
export function configureListByFloor(currentPage, pageSize, buildId, floorNameList) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/configureListByFloor`, {
            currentPage: currentPage,
            pageSize: pageSize,
            floorHouseListVo: {
                buildId: buildId,
                floorNameList: floorNameList
            }
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 显示相关联的配置申请通过房间编号
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Array} houseIdList 房间id数组
 */
export function configureListByHouseId(currentPage, pageSize, houseIdList) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/configureListByHouseId`, {
            currentPage: currentPage,
            pageSize: pageSize,
            houseIdList: houseIdList
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 显示相关联的配置申请通过房间编号
 * @param {Number} buildId 楼座id
 * @param {Array} floorNameList 楼层id数组
 * @param {Array} houseIdList 房间id数组
 */
export function getConfigureIdList(houseIdList) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/configureIdList`, {
            houseIdList: houseIdList
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 用户权限下的楼栋位置信息查询
 */
export function getBuildPositon() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/build/position`, {}).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
 * 上传文件
 */
export const uploadUrl = `${BASE_URL}${url_name_1}/uploads/file`;
