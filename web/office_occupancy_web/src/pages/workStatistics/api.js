import axios from '@/axios/index'
import { url_name_1 } from "@/axios/global"
/**
* 配置工作量统计(按月)
* @param {String} applyDateMonth 月份
*/
export function getConfigureStatisticsByMonth(applyDateMonth) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/workStatistics/configureStatisticsByMonth`, {
      applyDateMonth: applyDateMonth
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
* 配置工作量统计(按年)
* @param {String} applyDateYear 年份
*/
export function getConfigureStatisticsByYear(applyDateYear) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/workStatistics/configureStatisticsByYear`, {
      applyDateYear: applyDateYear
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
* 查询处置工作统计(按月)
* @param {String} applyDateMonth 月份
*/
export function getSelectHandleMothCount(applyDateMonth) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/workStatistics/selectHandleMothCount`, {
      applyDateMonth: applyDateMonth
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
* 查询处置工作统计(按年)
* @param {String} applyDateYear 年份
*/
export function getSelectHandleYearCount(applyDateYear) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/workStatistics/selectHandleYearCount`, {
      applyDateYear: applyDateYear
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
* 维修工作统计列表(按月)
* @param {String} applyDateMonth 月份
*/
export function getSelectRepairMothCount(applyDateMonth) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/workStatistics/selectRepairMothCount`, {
      applyDateMonth: applyDateMonth
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
* 维修工作统计列表(按年)
* @param {String} applyDateYear 年份
*/
export function getSelectRepairYearCount(applyDateYear) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/workStatistics/selectRepairYearCount`, {
      applyDateYear: applyDateYear
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}

/**
* 查询工作统计日期范围
* @param {Number} id 类型 （1维修，2处置，3配置）
*/
export function getStatisticsDateRange(id) {
  return new Promise((resolve, reject) => {
    axios.post(`${url_name_1}/workStatistics/statisticsDateRange`, {
      id: id
    }).then(response => {
      resolve(response);
    }, err => {
      reject(err);
    })
  })
}