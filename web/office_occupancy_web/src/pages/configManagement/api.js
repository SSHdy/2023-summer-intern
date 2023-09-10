import axios from '@/axios/index'
import { url_name_1 } from "@/axios/global"

/**
 * 配置申请列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} configureNo 配置编号
 * @param {String} startTime 开始时间
 * @param {Number} endTime 结束时间
 * @param {Number} userId 用户id
 * @param {Number} unitId 申请单位id
 */
export function getConfigureList(currentPage, pageSize, configureNo, startTime, endTime, userId, unitId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/configureList`, {
            currentPage: currentPage,
            pageSize: pageSize,
            configureNo: configureNo,
            startTime: startTime,
            endTime: endTime,
            userId: userId,
            unitId: unitId ? unitId : ''
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 新增配置申请
 * @param {Number} configObj 配置申请信息对象
 */
export function addConfigure(configObj) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/addConfigure`, configObj).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 修改配置申请
 * @param {Number} configObj 配置申请信息对象
 */
export function updateConfigure(configObj) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/updateConfigure`, configObj).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询配置申请基础数据
 * @param {Number} id 配置申请id
 */
export function getBasicConfigure(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/basicConfigure`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 配置申请详情
 * @param {Number} id 配置申请id
 */
export function getConfigureDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/configureDetail`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 处置审批列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} startTime 开始时间
 * @param {String} configureNo 配置编号
 * @param {Number} endTime 结束时间
 * @param {Number} userId 用户id
 * @param {Number} unitId 申请单位
 */
export function getConfigureApprovalList(currentPage, pageSize, configureNo, startTime, endTime, userId, unitId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/configureApprovalList`, {
            currentPage: currentPage,
            pageSize: pageSize,
            configureNo: configureNo,
            startTime: startTime,
            endTime: endTime,
            userId: userId,
            unitId: unitId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 配置申请详情
 * @param {Object} obj 审批信息对象
 */
export function saveSubmitConfigure(obj) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/saveSubmitConfigure`, obj).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询参与统筹调剂单位下拉列表
 */
export function getReapportionUnit() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/unit/reapportionUnit`, null).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询自动分房的筛选条件列表
 * @param {Number} id 配置申请id
 */
export function getAutoConditionList(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/autoCondition`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 查询自动分房符合条件的房间
* @param {Number} currentPage 当前页码
* @param {Number} pageSize 每页显示个数
* @param {Number} elevatorsNumber 电梯台数
* @param {Array} regionId 区域id数组
* @param {Number} roomAreaMax 房间面积范围最大值
* @param {Number} roomAreaMin 房间面积范围最小值
* @param {Number} selfUnitId 办理人所属单位
* @param {Number} otherUnitId 参与统筹调剂单位id
*/
export function getAutoAllocationRoomList(currentPage, pageSize, elevatorsNumber, regionId, roomAreaMax, roomAreaMin, selfUnitId, otherUnitId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/autoAllocationRoom`, {
            currentPage: currentPage,
            pageSize: pageSize,
            elevatorsNumber: elevatorsNumber,
            regionId: regionId,
            roomAreaMax: roomAreaMax,
            roomAreaMin: roomAreaMin,
            selfUnitId: selfUnitId,
            otherUnitId: otherUnitId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询审批流程
 * @param {Number} id 流程id
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
* 领导查看配置流程
* @param {Number} id 配置申请id
* @param {Number} step 当前步骤
*/
export function leaderReviewDetail(id, step) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/leaderReviewDetail`, {
            repairApplyId: id,
            step: step
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 查询配置步骤集合
* @param {Number} id 配置申请id
*/
export function selectConfigureStep(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/selectConfigureStep`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 查看分配方案数据
* @param {Number} currentPage 当前页码
* @param {Number} pageSize 每页显示个数
* @param {Number} configureId 配置申请id
*/
export function getPlanDataDetail(currentPage, pageSize, configureId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/planDataDetail`, {
            currentPage: currentPage,
            pageSize: pageSize,
            configureId: configureId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 新增配置方案
* @param {Number} configureId 新增配置方案
* @param {Number} planName 方案名称
*/
export function addConfigurePlan(configureId, planName) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configurePlan/addConfigurePlan`, {
            configureId: configureId,
            planName: planName
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 查询配置方案相关联的配置方案数据
* @param {Number} id 方案id
*/
export function getConfigurePlanDataCount(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configurePlan/configurePlanDataCount`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 配置方案列表
* @param {Number} configureId 新增配置方案
*/
export function getConfigurePlanList(configureId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configurePlan/configurePlanList`, {
            configureId: configureId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 删除配置方案
* @param {Number} id 方案id
*/
export function deleteConfigurePlan(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configurePlan/deleteConfigurePlan`, {
            id: id,
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 修改配置方案
* @param {Number} configureId 新增配置方案
* @param {Number} planId 方案id
* @param {Number} planName 方案名称
*/
export function updateConfigurePlan(configureId, planId, planName) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configurePlan/updateConfigurePlan`, {
            configureId: configureId,
            planId: planId,
            planName: planName
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取房间图形json
 * @param {Number} buildId 
 * @param {Number} floorName
 */
export function getHouseGraphJson(buildId, floorName) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configurePlan/jsonPlan`, {
            'buildId': buildId,
            'floorName': floorName
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
 * 查询分配单位信息
 * @param {Number} houseId 房间id
 * @param {Number} planId 方案id
 */
export function getAllocationUnit(houseId, planId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/allocationUnit`, {
            houseId: houseId,
            planId: planId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
 * 新增或修改方案配数据
 * @param {Number} obj 房间配置方案数据
 */
export function saveConfigurePlanData(obj) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configurePlan/aueConfigurePlanData`, obj).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}


/**
 * 查询人工分房，符合条件的房间
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Number} planId 方案id
 */
export function getArtificialAllocation(currentPage, pageSize, planId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/artificialAllocation`, {
            currentPage: currentPage,
            pageSize: pageSize,
            planId: planId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
 * 查看分配方案名称(人工)
 * @param {Number} id 方案id
 */
export function getSchemeName(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/planData`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
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
* 按照楼层查询人工分房的房间信息
* @param {Number} buildId 楼栋id
* @param {Number} floorName 楼层id
* @param {Number} planId 方案id
*/
export function getallocationFloorInfo(buildId, floorName, planId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/allocationFloor`, {
            buildId: buildId,
            floorName: floorName,
            planId: planId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
* 查询单位房间面积变化
* @param {Number} id 方案id
*/
export function getChangeData(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/change`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
* 判断领导审批是否被打回
* @param {Number} id 配置申请id
*/
export function getLeaderApprovalAdvice(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/configure/isLeaderApproval`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}
