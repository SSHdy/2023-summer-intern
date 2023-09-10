import axios from '@/axios/index'
import { url_name_1 } from "@/axios/global"
import { format } from 'util';
/**
 * 分页查询维修提醒信息列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 */
export function getRepairRemindInfoList(currentPage, pageSize) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairRemind/list`, {
            currentPage: currentPage,
            pageSize: pageSize
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 新增或修改维修提醒信息
* @param {Number} RepairRemindObj 维修提醒对象
*/
export function saveOrUpdateRepairRemind(RepairRemindObj) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairRemind/saveOrUpdate`, RepairRemindObj).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

// /**
// * 删除维修提醒信息
// * @param {Number} repairRemind 维修提醒id
// */
// export function deleteRepairRemind(repairRemind) {
//     return new Promise((resolve, reject) => {
//         axios.post(`${url_name_1}/repairRemind/delete`, {
//             repairRemind: repairRemind
//         }).then(response => {
//             resolve(response);
//         }, err => {
//             reject(err)
//         })
//     })
// }

/**
* 维修提醒状态修改
* @param {Number} repairRemind 维修提醒id
* @param {Number} remaindHandle 维修提醒状态
* @param {Date} remaindHandle 下次提醒时间
*/
export function updateRepairRemindState(repairRemind, remaindHandle, remaindTime) {
    let nextRemaindTime = remaindTime ? remaindTime : null;
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairRemind/updateState`, {
            repairRemind: repairRemind,
            remaindHandle: remaindHandle,
            remaindTime: nextRemaindTime
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 新增或修改维修部位信息
* @param {Object} repairPart 维修部位信息对象 
*/
export function addOrUpdateRepairParts(repairPart) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairParts/addOrUpdate`, repairPart).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 分页查询维修部位列表
* @param {Number} currentPage 当前页码
* @param {Number} pageSize 每页显示个数
* @param {Number} repairPartsId 维修部位id
*/
export function getRepairPartInfoList(currentPage, pageSize, repairPartsId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairParts/list`, {
            currentPage: currentPage,
            pageSize: pageSize,
            repairPartsId: repairPartsId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 查询维修部位信息列表（无分页）
*/
export function getRepairPartAllList() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairParts/allList`, null).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 删除维修部位信息
* @param {Number} repairPartsId 维修部位id
*/
export function deletetRepairPart(repairPartsId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairParts/delete`, {
            id: repairPartsId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 分页查询维修部位列表
* @param {Array} repairPartsIds 批量删除维修部位信息
*/
export function batchDeleteRepairPart(repairPartsIds) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairParts/batchDelete`, repairPartsIds).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 分页查询维修申请列表
* @param {Number} currentPage 当前页码
* @param {Number} pageSize 每页显示个数
* @param {Number} userId 用户id
* @param {number} applyStatus 用户申请状态
* @param {string} endApplicationTime 用户维修结束时间
* @param {string} startApplicationTime 用户维修开始时间
* @param {string} repairApplyNo 申请编号
*/
export function getRepairApplyList(currentPage, endApplicationTime, pageSize, repairApplyNo, startApplicationTime, userId, applyStatus) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairApply/repairApplyList`, {
            currentPage,
            pageSize,
            userId,
            applyStatus,
            endApplicationTime,
            startApplicationTime,
            repairApplyNo
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 维修申请详情
* @param {number} id 查看详情的id
*/
export function getRepairApplyDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairApply/repairApplyDetail`, { id: id }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 单元列表下拉框数据
*/
export function getUnitList() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/unit/all`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 维修部位下拉框数据
*/
export function getRepairPartsList() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairParts/allList`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}


/**
 * 闲置用房树形菜单查询
 */
export function getSpareRoomTree(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/tree/spareRoomTree`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 添加维修申请
* @param {array} removeUserIds 移除的维修用户id
* @param {string} repairApplicant 维修用户
* @param {number} repairApplicantId 维修用户id
* @param {array} repairModuleList 维修模块
* @param {array} unitIdList 用户id列表
* @param {array} userIds 审核权限的id
* @param {string} repairProject 维修内容
*/
export function addRepair(removeUserIds,
    repairApplicant,
    repairApplicantId,
    repairModuleList,
    unitIdList,
    userIds,
    repairProject) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairApply/addRepair`, {
            removeUserIds,
            repairApplicant,
            repairApplicantId,
            repairModuleList,
            unitIdList,
            userIds,
            repairProject
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 修改维修申请
* @param {array} repairModuleList 维修模块
* @param {number} repairApplyId 维修模块
* @param {array} unitIdList 单位列表
*/
export function updateRepair(repairModuleList,repairApplyId,unitIdList) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairApply/updateRepairApply`, {
            repairModuleList,repairApplyId,unitIdList
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 维修申请的查看
* @param {string} id 维修模块的流程id
*/
export function selectProcessHistory(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/process/selectProcessHistory`, {
            id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 下载证件信息
 * @param {array} addressList 选中证件信息的列表
 */
export function download(addressList,downLoadType,repairApplyId) {
    return new Promise((resolve, reject) => {
        axios({
            method: 'post',
            url: `${url_name_1}/propertyRight/download`,
            responseType: 'arraybuffer',
            data: { addressList: addressList,downLoadType: downLoadType,repairApplyId: repairApplyId }
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

//   维修工程管理

/**
 * 工程名查询
 */
export function getEngine() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairProject/all`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 分页查询维修项目列表
 * @param {number} currentPage 当前页码
 * @param {number} pageSize 每页展示的数据条数
 * @param {string} beginDate 开始时间
 * @param {string} endDate 结束时间
 * @param {number} repairApplyId 维修功工程名
 * @param {number} yardId 院落id
 * @param {Number} userId 当前用户的id
 */
export function getRepairProject(currentPage, pageSize, beginDate, endDate, repairApplyId, yardId,userId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairProject/list`, { currentPage, pageSize, beginDate, endDate, repairApplyId, yardId,userId}).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取维修项目详情
 * @param {number} id 维修申请id
 */
export function getProjectDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairApply/projectDetail`, { id }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取维修信息详情集合
 * @param {number} id 维修申请id
 */
export function getRepairApprovalDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairApply/repairApprovalDetail`, { id }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取维修信息详情
 * @param {number} id 维修模块id
 */
export function getRepairMessageDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairApply/repairMessageDetail`, { id }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 维修项目信息添加
 * @param {Number} id 维修申请id
 */
export function completedProjectDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairApply/completedProject`, { id }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 添加竣工验收信息
 * @param {object} constructObject 施工对象
 * @param {number} repairApplyId 维修模块id
 */
export function addConstructManage(constructObject, repairApplyId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairApply/constructManage`, { constructObject, repairApplyId }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询施工日志信息
 * @param {number} id 维修模块id
 */
export function getRepairManage(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/housemanager/repair-project-parts/repairLog`, { id }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 新增施工日志信息
 * @param {Array} partsIdAndImgVoList 维修模块和图片对应的集合
 * @param {Number} repairApplyId 维修申请id
 * @param {Array} repairItems 维修日志增减项
 * @param {Object} repairItems 维修项目内容
 */
export function addRepairManage(partsIdAndImgVoList,repairApplyId,repairItems,repairProject) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/housemanager/repair-project-parts/addRepairManage`, { partsIdAndImgVoList,repairApplyId,repairItems,repairProject }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 修改施工日志信息
 * @param {Array} deleteRepairPmImgIdList 要删除的维修模块和图片对应的集合
 * @param {Array} partsIdAndImgVoList 维修模块和图片对应的集合
 * @param {Number} repairApplyId 维修id
 * @param {Object} repairProject 维修项目内容
 */
export function updateRepairManage(deleteRepairPmImgIdList, partsIdAndImgVoList,repairApplyId,repairProject) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/housemanager/repair-project-parts/repairManageUpdate`, {deleteRepairPmImgIdList, partsIdAndImgVoList,repairApplyId,repairProject}).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询维修项目转移人员列表
 * @param {number} id 维修模块id
 */
export function getUser(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairPm/userList`, { id }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询维修项目转移人员列表
 * @param {number} repairApplyId 维修申请id
 *  @param {number} repairNewUsername 新项目负责人编号
 * @param {number} repairNewUserid 新项目负责人编号
 * @param {string} repairChangeReason 变更原因
 */
export function changeManage(repairApplyId, repairNewUsername, repairNewUserid, repairChangeReason) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairPm/changeManage`, { repairApplyId, repairNewUsername, repairNewUserid, repairChangeReason }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询付款详情
 * @param {number} id 维修申请id
 */
export function getRepairPayment(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairPayment/detail`, { id }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 付款状态修改
 * @param {Array} repairPaymentId 维修流程节点id
 * @param {number} repairApplyId 维修申请id
 */
export function updateState(repairPaymentId, repairApplyId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairPayment/payMent`, { repairPaymentId, repairApplyId }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

//质保期管理
/**
 * 查询维修模块位置列表(维修地点)
 */
export function getRepairModule() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairModule/list`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 维修部位信息列表（无分页）
 */
export function getRepairParts() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairParts/allList`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 维修模块信息管理列表
* @param {number} currentPage 当前页
* @param {number} pageSize 每页展示的数据条数
* @param {number} repairModuleId 维修地点id
* @param {number} repairPartsId 维修部位id
*/
export function getRepairModulePageList(currentPage, pageSize, repairModuleId, repairPartsId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairModule/pageList`, { currentPage, pageSize, repairModuleId, repairPartsId }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

// 维修提醒
/**
* 获取维修提醒列表
* @param {Number} currentPage 当前页
* @param {Number} pageSize 每页展示的数据条数
* @param {Number} buildId 维修地点id
* @param {String} floorName 维修部位id
* @param {Number} houseId 维修部位id
* @param {Array} unitId 维修部位id
* @param {Number} yardId 维修部位id
* @param {Number} remindState 维修提醒状态
*/
export function getRepairRemindList(currentPage, pageSize , form , unitId , remindState) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairRemind/repairRemindList`, { currentPage, pageSize,buildId:form.buildId,floorName:form.floorName,houseId:form.houseId,yardId:form.yardId ,unitId:unitId , remindState:remindState}).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 添加维修提醒
* @param {object} unitList Form集合
* @param {string} unitList 使用单位id集合
*/
export function addRepairRemind(repairRemind, unitList) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairRemind/addRepairRemind`, {
            repairRemind, unitList
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/** 批量删除维修提醒
 * @param {Array} ids 移除的维修提醒的id列表
 */
export function batchDeleteRepairRemind(ids) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairRemind/batchDeleteRepairRemind`,{
            ids
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 维修提醒信息基本数据
* @param {number} id 维修提醒id
*/
export function selectRepairRemindDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairRemind/selectRepairRemindDetail`, {
            id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 暂不提醒
* @param {Number} id 维修提醒id
*/
export function notRemind(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairRemind/notRemind`, {
            id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 查询相关维修记录
* @param {number} buildId 建筑id
* @param {number} floorName 楼层id
* @param {string} houseId 房屋id
* @param {number} yardId 院落id
* @param {number} repairPart 维修部位id
*/
export function selectRepairRemind(row) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairRemind/selectRepairRemind`, {
            buildId: row.buildId,
            floorName: row.floorName,
            houseId: row.houseId,
            yardId: row.yardId,
            repairPart: row.repairPart,
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

 /**
* 修改维修提醒时间
* @param {number} repairRemind 维修提醒id
* @param {string} remaindTime 维修提醒时间
*/
export function updateRepairRemind(remaindTime,repairRemind) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairRemind/updateRepairRemind`,{
            remaindTime,repairRemind
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

 /**
* 删除维修提醒
* @param {number} id 维修提醒id
*/
export function deleteRepairRemind(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairRemind/deleteRepairRemind`,{
            id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 下载租赁附件
 * @param {Array} downloadList 下载列表
 */
export function downloadRentAccessory(downloadList) {
    return new Promise((resolve, reject) => {
        axios({
            method: 'post',
            url: `${url_name_1}/rent/downLoad`,
            responseType: 'arraybuffer',
            data: {
                enclosureIdList: downloadList
            }
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}