import axios from '@/axios/index'
import {
  url_name_1
} from "@/axios/global"

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
 * 单元列表下拉框数据
 */
export function getUnitList() {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/unit/getUnitByUser`).then(response => {
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
 * 专项巡检信息列表
 * @param {Array} buildIds 楼栋id集合
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Number} unitId 单位名称
 * @param {Array} yardIds 院落id集合
 * 
 */
export function getSpecialList(currentPage, pageSize, unitId, yardIds, buildIds) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/specialInspection/getList`, {
      currentPage,
      pageSize,
      unitId,
      yardIds,
      buildIds,
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 * 删除一条专项巡检信息
 * @param {Number} id 需要删除的专项巡检id
 */
export function delSpecialList(id) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/specialInspection/delete`, {
      id
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 * 批量删除专项巡检信息 
 * @param {Array} ids 批量删除专项巡检信息集合
 */
export function batchDelSpecialList(ids) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/specialInspection/batchDelete`, ids).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 * 新增或修改专项巡检信息 
 * @param {Object} Form 表单对象
 */
export function addOrUpdate(Form) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/specialInspection/addOrUpdate
    `, {
      specialInspectionBuildId: Form.specialInspectionBuildId,
      specialInspectionPoints: Form.specialInspectionPoints,
      specialInspectionProblem: Form.specialInspectionProblem,
      specialInspectionRectifyComments: Form.specialInspectionRectifyComments,
      specialInspectionResult: Form.specialInspectionResult,
      specialInspectionRectifyTime: Form.specialInspectionRectifyTime,
      specialInspectionTime: Form.specialInspectionTime,
      specialInspectionUnitId: Form.specialInspectionUnitId,
      specialInspectionUserName: Form.specialInspectionUserName,
      specialInspectionYardId: Form.specialInspectionYardId,
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 * 新增或修改专项巡检信息 
 * @param {Object} Form 表单对象
 */
export function Update(Form) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/specialInspection/addOrUpdate
    `, {
      specialInspectionId: Form.specialInspectionId,
      specialInspectionBuildId: Form.specialInspectionBuildId,
      specialInspectionPoints: Form.specialInspectionPoints,
      specialInspectionProblem: Form.specialInspectionProblem,
      specialInspectionRectifyComments: Form.specialInspectionRectifyComments,
      specialInspectionResult: Form.specialInspectionResult,
      specialInspectionRectifyTime: Form.specialInspectionRectifyTime,
      specialInspectionTime: Form.specialInspectionTime,
      specialInspectionUnitId: Form.specialInspectionUnitId,
      specialInspectionUserName: Form.specialInspectionUserName,
      specialInspectionYardId: Form.specialInspectionYardId,
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 * 专项检查详情
 * @param {Number} id 需要删除的专项巡检id
 */
export function specialDetail(id) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/specialInspection/specialDetail
    `, {
      id
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 * 整改专项巡检信息
 * @param {Object} Form 专项巡检id
 */
export function reform(Form) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/specialInspection/reform
    `, {
      specialInspectionId: Form.specialInspectionId,
      specialInspectionRectifyResult: Form.specialInspectionRectifyResult,
      specialInspectionRectifyState: Form.specialInspectionRectifyState
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}
