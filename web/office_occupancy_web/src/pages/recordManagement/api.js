import axios from '@/axios/index'
import { url_name_1 } from '@/axios/global';

/**
 * 查询信息档案列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Array} buildIdList 楼座id
 * @param {Array} yardIdList 院落id
 */
export function getMessageFilesList(currentPage, pageSize, buildIdList, yardIdList) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/floorDrawing/messageFilesList`, {
            currentPage: currentPage,
            pageSize: pageSize,
            buildIdList: buildIdList,
            yardIdList: yardIdList
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询信息档案列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Array} buildIdList 楼座id
 * @param {Array} yardIdList 院落id
 */
export function getRepairFilesList(currentPage, pageSize, buildIdList, yardIdList) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairApply/repairFilesList`, {
            currentPage: currentPage,
            pageSize: pageSize,
            buildIdList: buildIdList,
            yardIdList: yardIdList
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询某栋楼关联的维修申请
 * @param {Number} buildId 楼座id
 * @param {String} startTime 起始时间
 * @param {String} buildId 截止时间
 */
export function getBuildRepairApplyList(buildId, startTime, endTime) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairApply/buildRepairApplyList`, {
            buildId: buildId,
            startTime: startTime,
            endTime: endTime
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
 * 查询某条维修申请的所有审批文件
 * @param {Number} id 楼座id
 */
export function getRepairApplyFiles(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairApply/repairApplyFiles`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
 * 查询某条维修申请的所有审批文件
 * @param {Number} yardId 院落id
 * @param {Number} buildId 楼座id
 */
export function getMessageFloorTree(yardId, buildId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/tree/messageFloorTree`, {
            yardId: yardId,
            buildId: buildId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
 * 查询某一楼层相关数据
 * @param {Number} buildId 楼座id
 * @param {Number} floorName 楼层id
 */
export function getFloorData(buildId, floorName) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/floorDrawing/floorData`, {
            buildId: buildId,
            floorName: floorName
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}


/**
 * 获取内控采购流程所有审批文件
 * @param {Number} id 维修申请id
 */
export function getDBAllFiles(id) {
    return new Promise((resolve, reject) => {
      axios.post(`${url_name_1}/repairApply/dbAllFiles`, {
        id: id,
      }).then(response => {
        resolve(response);
      }, err => {
        reject(err);
      })
    })
  }