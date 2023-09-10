import axios from '@/axios/index'
import { url_name_1 } from "@/axios/global"
/**
 * 获取房屋租赁信息列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} renter 承租方
 * @param {Number} rentState 租赁状态
 * @param {Number} type 1出租 2出借
 */
export function getRentInfoList(currentPage, pageSize, renter, rentState, type) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/rent/list`, {
            currentPage: currentPage,
            pageSize: pageSize,
            renter: renter,
            rentState: rentState,
            type: type
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 删除租赁信息
* @param {Number} id 租赁id
*/
export function deleteRent(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/rent/deleteRent`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 新增租赁信息
* @param {Object} rent 租赁对象
*/
export function addRent(rent) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/rent/addRent`, rent).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 修改租赁信息
* @param {Object} rent 租赁对象
*/
export function updateRent(rent) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/rent/updateRent`, rent).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 获取租赁详情
* @param {Object} id 租赁id
*/
export function getRentDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/rent/rentDetail`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 闲置用房树形菜单查询
* @param {Number} id 出租/出借id
* @param {Number} handleWay 处置方式（2出租 3出借）
* @param {Array} rentRelationshipIdList 出租关系
*/
export function getSpareRoomTree(id, handleWay, rentRelationshipIdList) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/tree/spareRoomTree`, {
            id: id,
            handleWay: handleWay,
            rentRelationshipIdList: rentRelationshipIdList
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 下载租赁附件
* @param {Array} enclosureIdList 下载列表
* @param {Number} downLoadType 类型
* @param {Array} rentId 出租id
*/
export function downloadRentAccessory(enclosureIdList, downLoadType, rentId) {
    return new Promise((resolve, reject) => {
        axios({
            method: 'post',
            url: `${url_name_1}/rent/downLoad`,
            responseType: 'arraybuffer',
            data: {
                enclosureIdList: enclosureIdList,
                downLoadType: downLoadType,
                rentId: rentId
            }
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
 * 退租
 * @param {Number} id 租赁id
 */
export function rentWithdrawal(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/rent/rentWithdrawal`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取处置申请列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Array} houseIdList 房间id集合
 * @param {Array} yardIdList 院落id集合 
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @param {Number} unitId 单位名称
 * @param {Number} userId 用户id
 */
export function getHandleList(currentPage, pageSize, houseIdList, yardIdList, startTime, endTime, unitId, userId) {
    const houseIdArray = houseIdList.length > 0 ? houseIdList : null;
    const yardIdArray = yardIdList.length > 0 ? yardIdList : null;
    const _unitId = unitId ? unitId : null;
    const _userId = userId ? userId : null;
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/handle/handleList`, {
            currentPage: currentPage,
            pageSize: pageSize,
            houseIdList: houseIdArray,
            yardIdList: yardIdArray,
            startTime: startTime,
            endTime: endTime,
            unitId: _unitId,
            userId: _userId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询处置列表（不包含出租出借）
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Array} houseIdList 房间id集合
 * @param {Array} yardIdList 院落id集合 
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @param {Number} unitId 单位名称
 * @param {Number} userId 用户id
 * @param {Number} handleWay 处置方式
 */
export function getHandleLs(currentPage, pageSize, houseIdList, yardIdList, startTime, endTime, unitId, userId, handleWay) {
    const houseIdArray = houseIdList.length > 0 ? houseIdList : null;
    const yardIdArray = yardIdList.length > 0 ? yardIdList : null;
    const _unitId = unitId ? unitId : null;
    const _userId = userId ? userId : null;
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/handle/handleLs`, {
            currentPage: currentPage,
            pageSize: pageSize,
            houseIdList: houseIdArray,
            yardIdList: yardIdArray,
            startTime: startTime,
            endTime: endTime,
            unitId: _unitId,
            userId: _userId,
            handleWay: handleWay
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 新增处置申请
* @param {Object} disposeApplyObj 处置申请信息对象
*/
export function addHandle(disposeApplyObj) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/handle/addHandle`, disposeApplyObj).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 修改处置申请
* @param {Object} disposeApplyObj 处置申请信息对象
*/
export function updateHandle(disposeApplyObj) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/handle/updateHandle`, disposeApplyObj).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 处置申请详情
* @param {Number} id 处置申请id
*/
export function getHandleDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/handle/handleDetail`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 查询可以处置的位置
*/
export function selectHandlePo() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/handle/selectHandlePo`, null).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 修改时查询可以处置的位置
* @param {Number} id 处置申请id
*/
export function selectHandlePoUp(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/handle/selectHandlePoUp`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 查询所有院落楼栋树
*/
export function selectAllHandlePo() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/handle/selectAllHandlePo`, null).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 查询审批流程
* @param {Number} id 审批流程id
*/
export function selectProcessHistory(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/process/selectProcessHistory`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取处置审批列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Array} houseIdList 房间id集合
 * @param {Array} yardIdList 楼栋id集合
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @param {Number} userId 用户id
 */
export function getHandleApprovalList(currentPage, pageSize, houseIdList, yardIdList, startTime, endTime, userId) {
    const houseIdArray = houseIdList.length > 0 ? houseIdList : null;
    const yardIdArray = yardIdList.length > 0 ? yardIdList : null;
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/handle/handleApprovalList`, {
            currentPage: currentPage,
            pageSize: pageSize,
            houseIdList: houseIdArray,
            yardIdList: yardIdArray,
            startTime: startTime,
            endTime: endTime,
            userId: userId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 审核申请审批
* @param {Object} obj 审批信息对象
*/
export function saveSubmitHandle(obj) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/handle/saveSubmitHandle`, obj).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 领导查看处置流程
* @param {Number} repairApplyId 处置申请id
* @param {Number} step 处置申请id
*/
export function leaderReviewDetail(repairApplyId, step) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/handle/leaderReviewDetail`, {
            repairApplyId: repairApplyId,
            step: step
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 领导查看
* @param {Number} id 处置申请id
*/
export function leaderDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/handle/leaderDetail`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 文件下载
 * @param {Array} downloadList  文件物理路径集合
 */
export function downloadFile(addressList) {
    return new Promise((resolve, reject) => {
        axios({
            method: 'post',
            url: `${url_name_1}/repairApply/downLoad`,
            responseType: 'arraybuffer',
            data: addressList
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
* 租金缴纳提醒列表
* @param {Number} currentPage 当前页码
* @param {Number} pageSize 每页显示个数
* @param {String} renter 承租方
*/
export function getRentRmindList(currentPage, pageSize, renter) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/rent/rentRmindList`, {
            currentPage: currentPage,
            pageSize: pageSize,
            renter: renter
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 修改租金缴纳提醒状态
* @param {Array} rentIdList 租金缴纳提醒id数组
* @param {Number} type 类型（1出租，2出借）
*/
export function updateIsRead(rentIdList, type) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/rent/updateIsRead`, {
            rentIdList: rentIdList,
            type: type
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 出租到期提醒列表
* @param {Number} currentPage 当前页码
* @param {Number} pageSize 每页显示个数
* @param {String} renter 承租方
* @param {String} type 类型(1出租， 2出借)
*/
export function getRentExpireList(currentPage, pageSize, renter, type) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/rent/rentExpireList`, {
            currentPage: currentPage,
            pageSize: pageSize,
            renter: renter,
            type: type
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}