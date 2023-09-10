import axios from '@/axios/index'
import { url_name_1, url_name_2 } from "@/axios/global"
/**
 * 添加房间
 * @param {Object} house 房间信息集合
 */
export function houseAdd(house) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/house/addUpdateDeleteHouseAndJsonPlan`, house).then(response => {
            resolve(response);
        }, err => {
            reject(err);
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
        axios.post(`${url_name_1}/house/jsonPlan`, {
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
 * 获取房间图形json
 * @param {String} houseId 房间的id 
 */
export function seachSingleHouseInfo(houseId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/house/info`, houseId).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
 * 楼层所有房屋信息列表
 * @param {Number} buildId 楼座id
 * @param {Number} floorName 楼层id 
 */
export function getAllHouseInfo(buildId, floorName) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/house/allInfo`, {
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
* 查询CAD
* @param {Number} buildId 楼座Id
* @param {Number} floor 楼层Id
*/
export function seachCAD(buildId, floor) {
    const floorId = floor ? floor : null;
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/floorDrawing/select`, {
            buildId: buildId,
            floor: floorId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
* 保存CAD
* @param {Array} CADArray 
*/
export function saveCAD(CADArray) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/floorDrawing/add`, CADArray).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
* 删除CAD
* @param {Array} CADArray 
*/
export function deleteCAD(CADArray) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/floorDrawing/delete`, CADArray).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
* 下载CAD
* @param {Array} CADArray 
*/
export function downloadCAD(CADArray) {
    return new Promise((resolve, reject) => {
        axios({
            method: 'post',
            url: `${url_name_1}/floorDrawing/downLoad`,
            responseType: 'arraybuffer',
            data: CADArray
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
 * 获取配置管理列表
 * @param {*} currentPage 
 * @param {*} pageSize 
 * @param {*} name 
 * @param {*} type 
 */
export function getConfigInfoList(currentPage, pageSize, unit, time) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/Config/list`, {
            currentPage: currentPage,
            pageSize: pageSize,
            unit: unit,
            time: time
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 配置管理列表单个删除
 * @param {*} id 
 */
export function deleteConfig(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/Config/delete`, { 'id': id }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 配置管理列表批量删除
 * @param {*} ids 
 */
export function batchDeleteConfig(ids) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/Config/batchDelete`, ids).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 配置管理列表新增修改
 * @param {*} config 
 */
export function saveOrUpdateConfig(config) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/Config/saveOrUpdate`, config).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取维修信息列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 */
export function getServiceViewInfoList(currentPage, pageSize) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/ServiceView/list`, {
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
 * 查询院落经纬度
 * @param {Array} unitIds 单位id
 */
export function getYardPositon(unitIds) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/yard/position`, {
            unitIds: unitIds
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
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
 * 楼栋信息详情
 * @param {Number} id 楼栋id
 */
export function getBuildDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/build/detail`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
 * 查询单个院落信息
 * @param {Number} id 院落id
 */
export function getYardDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/yard/detail`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**
 * 查询某个楼栋下的单位信息和人员职称信息
 * @param {Number} id 楼座id
 */
export function getUnitPersonInfo(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/build/unitPersonInfo`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取重大设备信息列表
 * @param {Number} yardId 院落id
 * @param {Number} buildId  楼栋id
 */
export function getDeviceInfoList(yardId, buildId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/build/selectBuildAndEquipment`, {
            yardId: yardId,
            buildId: buildId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询某个楼栋下的最后几条设备信息列表
 * @param {Number} id  楼栋id
 */
export function getDeviceLastInfoList(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/equipment/equipmentLast`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询某楼栋相关的维修模块记录
 * @param {Number} buildId 楼座id
 */
export function getRepairBuildRecord(buildId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairApply/repairBuildRecord`, {
            id: buildId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取单位信息列表（带查询）
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Number} propertyType 产权类型
 */
export function getPropertyRightList(currentPage, pageSize, propertyType) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/propertyRight/propertyRightList`, {
            currentPage: currentPage,
            pageSize: pageSize,
            propertyType: propertyType
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}



/**
 * 添加房产证信息
 * @param {Object} Form 房产权信息对象
 * @param {number} PropertyType 房产权信息对象
 
 */
export function addPropertyRight(Form) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/propertyRight/addPropertyRight`, Form).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 添加不动产信息
 * @param {Object} Form 不动产权信息对象
 * @param {number} PropertyType 不动产权信息对象
 */
export function addPropertyRights(Form) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/propertyRight/addPropertyRight`, Form).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 添加土地证信息
 * @param {Object} Form 土地证信息对象
 * @param {number} PropertyType 土地证信息对象
 */
export function addPropertyRightS(Form) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/propertyRight/addPropertyRight`, Form).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 修改房产证信息
 * @param {Object} basicDBObject 产权信息对象
 * @param {Array} physicalAddressList 产权信息删除的图片的虚拟地址
 */
export function updatePropertyRight(basicDBObject, physicalAddressList) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/propertyRight/updatePropertyRight`, {
            basicDBObject, physicalAddressList
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 修改不动产信息
 * @param {Object} basicDBObject 产权信息对象
 * @param {Array} physicalAddressList 产权信息删除的图片的虚拟地址
 */
export function updatePropertyRights(basicDBObject, physicalAddressList) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/propertyRight/updatePropertyRight`, {
            basicDBObject, physicalAddressList
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 修改土地证信息
 * @param {Object} Form 产权信息对象
 * @param {number} PropertyType 产权信息对象
 */
export function updatePropertyRightS(Form, PropertyType) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/propertyRight/updatePropertyRight`, {
            AloneArea: Form.AloneArea,
            excursus: Form.excursus,
            landType: Form.landType,
            releaseDate: Form.releaseDate,
            LandCardNumber: Form.LandCardNumber,
            LandNumber: Form.LandNumber,
            figureNumber: Form.figureNumber,
            userArea: Form.userArea,
            residentialArea: Form.residentialArea,
            sharingArea: Form.sharingArea,
            rightUser: Form.rightUser,
            buildNum: Form.buildNum,
            UseRightType: Form.UseRightType,
            rightUseStartDate: Form.rightUseStartDate,
            rightUseEndDate: Form.rightUseEndDate,
            BusinessArea: Form.BusinessArea,
            getPrice: Form.getPrice,
            certificateOrgan: Form.certificateOrgan,
            addressList: Form.addressList,
            propertyType: PropertyType,
            _id: Form.id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询产权信息详情
 * @param {Number} id 产权信息id
 */
export function getPropertyRightDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/propertyRight/detail`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 删除产权证信息
 * @param {Number} id 产权信息id
 */
export function deletePropertyRight(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/propertyRight/deletePropertyRight`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 批量删除产权证信息
 * @param {array} ids 选中产权信息的id列表
 */
export function batchDeletePropertyRight(ids) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/propertyRight/batchDeletePropertyRight`, {
            ids: ids
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 批量删除产权证信息
 */
export function getRightBuildTree() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/tree/rightBuildTree`,
            null
        ).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 下载证件信息
 * @param {Array} addressList 选中证件信息的列表
 * @param {Number} downLoadType 下载类型
 * @param {String} propertyRightId 产权类型
 */
export function download(addressList, downLoadType, propertyRightId) {
    return new Promise((resolve, reject) => {
        axios({
            method: 'post',
            url: `${url_name_1}/propertyRight/download`,
            responseType: 'arraybuffer',
            data: { addressList: addressList, downLoadType: downLoadType, propertyRightId: propertyRightId }
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err);
        })
    })
}

/**************************************************物业管理strat************************************************/
/**
 * 查询物业信息列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Number} estateType 物业类型
 * @param {String} manager 管理人
 */
export function getEstateList(currentPage, pageSize, estateType, manager) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/housemanager/estate/selectEstateList`, {
            currentPage: currentPage,
            pageSize: pageSize,
            estateType: estateType,
            manager: manager
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 添加物业信息
 * @param {Object} estate 物业信息
 */
export function addEstate(estate) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/housemanager/estate/addEstate`, estate).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 修改物业信息
 * @param {Object} estate 物业信息
 */
export function updateEstate(estate) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/housemanager/estate/updateEstate`, estate).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 删除物业信息
 * @param {Number} id 物业id
 */
export function deleteEstate(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/housemanager/estate/deleteEstate`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 批量删除物业信息
 * @param {array} ids 物业id列表
 */
export function batchDeleteEstate(ids) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/housemanager/estate/batchDeleteEstate`, {
            ids: ids
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 根据院落编号查询物业关系
* @param {Number} id 院落id
*/
export function getSelectEstateData(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/yard/selectEstate`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}
/**************************************************物业管理end************************************************/

/**
* 楼栋处置信息
* @param {Number} id 楼座id
*/
export function getBuildHandleMsg(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/build/buildHandleMsg`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 查询单位树房间信息
* @param {Number} id 单位id
*/
export function getUnitTreeDetail(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/unit/unitTreeDetail`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 房间明细查询
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {Number} unitId 单位id
 * @param {Number} isLeading 是否为领导
 * @param {String} usePerson 房间使用人员
 */
export function getRoomDetailList(currentPage, pageSize, unitId, isLeading, usePerson, unitGrade, unitType, yardId, buildId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/house/roomDetail`, {
            currentPage: currentPage,
            pageSize: pageSize,
            unitId: unitId,
            isLeading: isLeading,
            usePerson: usePerson,
            unitGrade: unitGrade,
            unitType: unitType,
            yardId: yardId,
            buildId: buildId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
* 查询楼栋产权信息
* @param {Number} buildId 楼座id
* @param {Number} type 类型
*/
export function getBuildRightDetail(buildId, type) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/build/buildRight`, {
            buildId: buildId,
            type: type
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}
