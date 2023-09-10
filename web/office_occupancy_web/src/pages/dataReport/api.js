import axios from '@/axios/index'
import { url_name_1 } from "@/axios/global"
/**
 * 办公用房列表
 */
export function getYardUnitReportList() {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/yard/yardUnitReport`, null).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 * 业务用房列表
 */
export function getBusinessHouseList() {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/reportUnit/businessHouse`, null).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 * 建设项目列表
 */
export function getConstructHouseList() {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/reportUnit/constructHouseList`, null).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 * 查询单位上报列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Integer} reportYear 年度
 * @param {String} unitName 单位名称
 * @param {Integer} state 上报状态(0未上报  1已上报)
 */
export function getReportUnitList(currentPage, pageSize, reportYear, unitName, state) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/reportUnit/reportUnitList`, {
      currentPage: currentPage,
      pageSize: pageSize,
      reportYear: reportYear,
      unitName: unitName,
      state: state
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 * 单位上报
 * @param {Object} obj 单位上报信息对象
 */
export function reportUnit(obj) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/reportUnit/reportUnit`, obj).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 * 下载模板列表
 */
export function getDownloadTemplateList() {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/downloadTemplate/downloadTemplateList`).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 * 查询行政区划信息
 */
export function selectDistInfo() {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/distInformation/selectDistInfo`).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 * 副省级城市数据上报信息导出
 * @param {Object} obj 上报信息对象
 */
export function exportDataExcel(obj) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'post',
      url: `${url_name_1}/reportUnit/exportDataExcel`,
      responseType: 'arraybuffer',
      data: obj
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 查询所有单位集合
 */
export function getAllReportUnitList() {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/reportUnit/unitList`, {}).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}

/**
 * 单位上报文件下载
 * @param {Array} addressList 下载文件集合
 */
export function downLoadReport(addressList) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'post',
      url: `${url_name_1}/reportUnit/downLoad`,
      responseType: 'arraybuffer',
      data: {
        addressList: addressList
      }
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
 * 查询所有单位上报文件按年份
 * @param {String} reportYear 上报年份
 */
export function getAllReportEnclosure(reportYear) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/reportUnit/allReportEnclosure`, {
      reportYear: reportYear
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err)
    })
  })
}



