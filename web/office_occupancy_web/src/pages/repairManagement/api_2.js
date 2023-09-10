import axios from '@/axios/index'
import { url_name_1, url_name_2 } from "@/axios/global"

/**
* 查看维修审批列表
* @param {Number} currentPage 当前页码
* @param {Number} pageSize 每页显示个数
* @param {Number} userId 用户id
* @param {string} startApplicationTime 申请开始时间
* @param {string} endApplicationTime 申请结束时间
* @param {string} repairApplyNo 申请编号
*/
export function getRepairApprovalList(currentPage, pageSize, userId, startApplicationTime, endApplicationTime, repairApplyNo) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/repairApprovalList`, {
      currentPage: currentPage,
      pageSize: pageSize,
      userId: userId,
      startApplicationTime: startApplicationTime,
      endApplicationTime: endApplicationTime,
      repairApplyNo: repairApplyNo
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
* 维修审批详情
* @param {Number} id 维修申请id
*/
export function getRepairApprovalDetail(id) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/repairApprovalDetail`, {
      id: id,
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
* 维修信息详情
* @param {Number} id 维修模块id
*/
export function getRepairMessageDetail(id) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/repairMessageDetail`, {
      id: id,
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
* 修改维修模块的面积
* @param {Number} repairModuleId 维修模块id
* @param {Number} repairModuleArea 维修模块面积
*/
export function updateRepairModule(repairModuleId, repairModuleArea) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/updateRepairModule`, {
      repairModuleId: repairModuleId,
      repairModuleArea: repairModuleArea
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
* 删除维修模块的一条记录
* @param {Number} id 维修模块id
*/
export function deleteRepairModule(id) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/deleteRepairModule`, {
      id: id,
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}


/**
* 查询相关维修记录
* @param {Number} id 维修申请id
*/
export function getRepairRecord(id) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/repairRecord`, {
      id: id,
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
* 审核申请审批
* @param {Number} Form 审核申请信息对象
*/
export function saveSubmitRepair(Form) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/saveSubmitRepair`, Form).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
* 查询维修申请相关的信息
* @param {Number} id 维修申请id
*/
export function getRepairDetailDb(id) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/repairDetailDb`, {
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
 * @param {Number} downLoadType  下载类型
 * @param {String} repairApplyId  维修申请id
 */
export function downloadFile(addressList, downLoadType, repairApplyId) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'post',
      url: `${url_name_1}/repairApply/downLoad`,
      responseType: 'arraybuffer',
      data: {
        addressList: addressList,
        downLoadType: downLoadType,
        repairApplyId: repairApplyId
      }
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 查询对应流程下的文件路径
 * @param {Number} repairApplyId 维修申请id
 * @param {Number} step 维修流程步骤
 */
export function getProjectFileAddress(repairApplyId, step) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/projectFileAddress`, {
      repairApplyId: repairApplyId,
      step: step
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 查询维修预算信息
 * @param {Number} leaderState 评审状态
 * @param {Number} repairApplyId 维修申请id
 */
export function getProjectBudgetDetail(repairApplyId, leaderState) {
  const state = leaderState ? leaderState : 1;
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/projectBudgetDetail`, {
      leaderState: state,
      repairApplyId: repairApplyId
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 查询预算评审信息
 * @param {Number} leaderState 评审状态
 * @param {Number} repairApplyId 维修申请id
 */
export function getProjectReviewDetail(repairApplyId, leaderState) {
  const state = leaderState ? leaderState : 1;
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/projectReviewDetail`, {
      leaderState: state,
      repairApplyId: repairApplyId
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 查询维修类型列表
 * @param {Number} repairApplyId 维修申请id
 */
export function selectRepairType(repairApplyId) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/selectRepairType`, {
      id: repairApplyId
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 获取领导审批详情
 * @param {Number} repairApplyId 维修申请id
 * @param {Number} step 查询类型
 */
export function getLeaderReviewDetail(repairApplyId, step) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/leaderReviewDetail`, {
      repairApplyId: repairApplyId,
      step: step
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 查询政府或发改委审批
 * @param {Number} id 维修申请id
 */
export function getGoverReviewDetail(id) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/goverReviewDetail`, {
      id: id
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 查询项目施工设计
 * @param {Number} id 维修申请id
 */
export function getDesignReviewDetail(id) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/designReviewDetail`, {
      id: id
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 查询用户列表
 */
export function getUserList() {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_2}/user/userList`, []).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 查询维修查看列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} repairTarget 维修内容
 * @param {Number} repairApplyNo 申请编号
 * @param {Number} applyStatus 申请状态
 * @param {Number} startApplicationTime 维修开始时间
 * @param {Number} endApplicationTime 维修结束时间
 */
export function getLeaderReviewList(currentPage, pageSize, repairTarget, repairApplyNo, applyStatus, startApplicationTime, endApplicationTime) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/leaderReviewList`, {
      currentPage: currentPage,
      pageSize: pageSize,
      repairTarget: repairTarget,
      repairApplyNo: repairApplyNo,
      applyStatus: applyStatus,
      startApplicationTime: startApplicationTime,
      endApplicationTime: endApplicationTime
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 查询项目负责人
 * @param {Number} id 维修申请id
 */
export function getchargerList(id) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairPm/resUser`, {
      id: id
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 查询竣工验收信息
 * @param {Number} id 维修申请id
 */
export function getCompletedProjectInfo(id) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/completedProject`, {
      id: id
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 获取内控预算事项科室预算明细
 * @param {String} name 登录人的姓名
 * @param {String} phoneNum 电话号码
 */
export function getSectionList(name, phoneNum) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/getSectionList`, {
      name: name,
      phoneNum: phoneNum
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 获取内控采购流程步骤详情
 * @param {Number} repairApplyId 维修申请id
 * @param {String} stepTags 步骤标识
 */
export function getDBStepDetail(repairApplyId, stepTags) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/dbStepDetail`, {
      repairApplyId: repairApplyId,
      stepTags: stepTags
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 获取内控采购流程完成步骤集合
 * @param {Number} id 维修申请id
 */
export function getDBStepList(id) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/repairApply/dbStepList`, {
      id: id,
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}