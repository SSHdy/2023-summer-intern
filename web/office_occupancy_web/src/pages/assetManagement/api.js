import axios from '@/axios/index'
import { url_name_1 } from "@/axios/global"
/**
 * 获取重大设备信息列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 */
export function getDeviceInfoList(currentPage, pageSize) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/equipment/equipmentList`, {
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
 * 删除单个重大设备
 * @param {Number} deviceId 设备id
 */
export function deleteDevice(deviceId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/equipment/delete`, {
            id: deviceId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 
 * @param {Array} deviceIds 设备id数组
 */
// 批量删除单位
export function batchDeleteDevice(deviceIds) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/equipment/deleteBatch`, {
            ids: deviceIds
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 
 * @param {Object} device 设备信息
 */
// 新增修改设备信息
export function addDevice(device) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/equipment/add`, device).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 
 * @param {Object} device 设备信息
 */
// 新增修改设备信息
export function updateDevice(device) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/equipment/update`, device).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取设备详情
 * @param {Number} id 设备id
 */
export function getDeviceDetailInfo(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/equipment/detail`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取资产总览信息列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 */
export function getViewInfoList(currentPage, pageSize) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repaire/list`, {
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
 * 获取资产维修信息列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 */
export function getServiceInfoList(currentPage, pageSize) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/service/list`, {
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
 * 获取闲置用房信息列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize  每页显示个数
 */
export function getHouseInfoList(currentPage, pageSize) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/house/getHouseInfoList`, {
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
 * 删除单个闲置用房信息
 * @param {Number} id
 */
export function deleteHouse(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/house/delete`, { 'id': id }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 批量删除单位
 * @param {Array} ids id数组
 */
export function batchDeleteHouse(ids) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/house/batchDelete`, ids).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 新增修改设备信息
 * @param {Object} obj 
 */
export function saveOrUpdateHouse(obj) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/house/saveOrUpdate`, obj).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 分页查询资产信息列表
 * @param {String} assetNameComn 当前页码
 * @param {Number} assetsType 当前页码
 * @param {String} categoryCodeComn 当前页码
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize  每页显示个数
 */
export function getAssetInfoList(assetNameComn, assetsType, categoryCodeComn, currentPage, pageSize) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/assets/list`, {
            assetNameComn: assetNameComn,
            assetsType: assetsType,
            categoryCodeComn: categoryCodeComn,
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
 * 分页查询资产信息列表
 * @param {Number} id 资产id
 */
export function getAssetDetailInfo(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/assets/detail`, {
            id: id,
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取账面资产列表
 */
export function getSelectAssetsList() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/assets/selectAssets`)
            .then(response => {
                resolve(response);
            }, err => {
                reject(err)
            })
    })
}

/**
 * 获取账面资产详情
 * @param {Number} purchasingId 采购项目编号
 */
export function getSelectAssetsDetailInfo(purchasingId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/assets/selectPurchasingById`, {
            purchasingId: purchasingId
        })
            .then(response => {
                resolve(response);
            }, err => {
                reject(err)
            })
    })
}

/**
 * 新增账面资产
 * @param {Object} Assets 账面资产对象
 */
export function addAssets(Assets) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/assets/add`, Assets)
            .then(response => {
                resolve(response);
            }, err => {
                reject(err)
            })
    })
}

/**
 * 获取楼栋信息详情
 * @param {number} buildId
 */
export function buildDetails({ id: buildId }) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/build/detail`, {
            id: buildId
        }
        ).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 根据院落id楼座id查询单位使用人数房间类别
 * @param {Number} yardId 院落id
 * @param {Number} buidId 楼座id
 */
export function selectBuildHouseUnitHouseUse(params) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/build/selectBuildHouseUnitHouseUse`, {
            yardId: params.yardId,
            buidId: params.buidId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 资产凭证修改
 * @param {Number} id 资产id
 * @param {Number} accountingVoucherNumber 会计凭证号
 * @param {Number} cardId 卡片编号
 */
export function updateAssert(id, accountingVoucherNumber, cardId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/assets/updateAssert`, {
            id: id,
            accountingVoucherNumber: accountingVoucherNumber,
            cardId: cardId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询已经入库的资产信息
 * @param {Number} assetsType 资产类型 
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 页码大小
 */
export function storageAsset(assetsType, currentPage, pageSize) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/assets/storageAsset`, {
            assetsType,
            currentPage,
            pageSize
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 资产导出
 * @param {array} assetsIds 所有资产的id列表
 * @param {Number} assetsType 资产类型
 */
export function exportAssetsExcel(assetsIds, assetsType) {
    return new Promise((resolve, reject) => {
        axios({
            method: 'post',
            url: `${url_name_1}/assets/exportAssetsExcel`,
            responseType: 'arraybuffer',
            data: {
                assetsIds: assetsIds,
                assetsType: assetsType
            }
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
 * 资产情况统计
 * @param {string} beginDate 开始时间
 * @param {string} endDate 结束时间
 */
export function assertStatistics(beginDate, endDate) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/assets/assertStatistics`, {
            beginDate,
            endDate
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 资产导出
 * @param {Number} assetsId 资产id
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 */
export function getcloneInfoList(assetsId, currentPage, pageSize) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/assets/findByAssetId`, {
            assetsIdComn: assetsId,
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
 * 采购项目管理列表
 * @param {Number} purchasingPname 采购id
 * @param {Number} status 状态
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 */
export function getPurchasingList(purchasingPname, status, currentPage, pageSize) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/purchasing/list`, {
            purchasingPname: purchasingPname ? purchasingPname : null,
            status: status === 0 || status === 1 ? status : null,
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
 * 采购项目管理列表
 * @param {Array} assetInfoList 资产信息列表
 * @param {Number} baseInfo 采购项目基本信息对象
 */
export function addPurchasing(baseInfo, assetInfoList) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/purchasing/add`, {
            baseInfo: baseInfo,
            assetInfoList: assetInfoList
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 采购信息详情
 * @param {Number} id 采购id
 */
export function getPurchasingDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/purchasing/purchasingDetail`, {
            purchasingId: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}
