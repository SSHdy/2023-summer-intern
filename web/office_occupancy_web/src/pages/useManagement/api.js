import axios from '@/axios/index'
import {
  url_name_1
} from "@/axios/global"
import {
  format
} from 'util';

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
 * 单元列表下拉框数据
 */
export function getUnitLists() {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/unit/all`).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 *房屋超标报表查询
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Number} unitId 单位id
 * @param {String} yardPosition 院落地址
 * @param {Number} buildId 楼座
 */
export function getHouseProof(currentPage, pageSize, unitIdList, yardPosition, buildId) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/report/houseProof`, {
      currentPage,
      pageSize,
      unitIdList,
      yardPosition,
      buildId
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 *房屋类型报表查询
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Number} id 每页显示个数
 */
export function selectHouseType(buildId, unitIdList) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/report/selectHouseType`, {
      buildId, unitIdList
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 *闲置用房查询
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Number} yardId 院落id
 * @param {Number} buildId 建筑id
 * @param {Number} floorName 房间号
 * @param {String} houseId 房间id
 * @param {Number} housetypeId 房间类型id
 * @param {Array} unitIdList 单位名称集合
 */
export function SpareRoom(currentPage, pageSize, form, housetypeId, unitIdList) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/report/spareRoom`, {
      currentPage: currentPage,
      pageSize: pageSize,
      yardId: form.yardId,
      buildId: form.buildId,
      floorName: form.floorName,
      houseId: form.houseId,
      housetypeId: housetypeId,
      unitIdList: unitIdList
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 *租赁房屋报表查询
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Array} yardIds 院落id
 * @param {Array} buildIds 建筑id
 * @param {Array} floorNames 房间号
 * @param {Array} houseIds 房间id
 * @param {Number} houseTypeId 房屋类型
 * @param {Number} unitId 房屋类型
 */
export function getRentRoomReport(currentPage, pageSize, yardIds, buildIds, floorNames, houseIds, houseTypeId, unitIdList) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/report/rentRoomReport`, {
      currentPage: currentPage,
      pageSize: pageSize,
      yardIds: yardIds,
      buildIds: buildIds,
      floorNames: floorNames,
      houseIds: houseIds,
      houseTypeId: houseTypeId,
      unitIdList: unitIdList
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

// /**
//  * 分页查询单位超标信息
//  * @param {Number} currentPage 当前页码
//  * @param {Number} pageSize 每页显示个数
//  * @param {Number} id 单位id
//  */
// export function getUnitExcessInfo(currentPage, pageSize, id) {
//   return new Promise((resolve, reject) => {
//     axios.post(`${url_name_1}/unit/excessInfo`, {
//       currentPage: currentPage,
//       pageSize: pageSize,
//       id: id,
//     }).then(response => {
//       resolve(response);
//     }, err => {
//       reject(err)
//     })
//   })
// }

/**
 * 分页查询单位超标信息
 * @param {Array} unitIdList 单位名称集合
 */
export function getUnitExcessInfo(unitIdList) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/report/selectExcessUnit`, {
      ids: unitIdList
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
 * 用户权限下的楼栋位置信息查询
 * @param {Array} unitIds 单位id
 * @param {Number} yardId 院落id
 */
export function getBuildPositon(unitIds, yardId) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/build/position`, {
      unitIds: unitIds,
      yardId: yardId
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 查询房间明细汇总
 */
export function selectHouseDetailByUnit() {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/report/selectHouseDetailByUnit`).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 房间明细信息导出
 * @param {Array} arr 房间明细信息数组
 */
export function exportHouseDetailExcel(arr) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'post',
      url: `${url_name_1}/report/exportHouseDetailExcel`,
      responseType: 'arraybuffer',
      data: arr
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}