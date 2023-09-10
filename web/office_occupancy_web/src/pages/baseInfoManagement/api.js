import axios from '@/axios/index'
import { url_name_1 } from "@/axios/global"
/**
 * 获取单位信息列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} name 单位名称
 * @param {String} type 单位类型
 */
export function getUnitInfoList(currentPage, pageSize) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/unit/list`, {
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
 * 获取单位信息列表（带查询）
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} name 单位名称
 * @param {String} type 单位类型
 */
export function getUnitInfoList2(currentPage, pageSize, name, type) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/unit/unitAll`, {
            currentPage: currentPage,
            pageSize: pageSize,
            unitName: name,
            unitType: type
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 删除单个单位信息
 * @param {Number} unitId 单位id
 */
export function deleteUnit(unitId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/unit/delete`, { 'unitId': unitId }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 新增或修改单位信息
 * @param {Object} unit 单位信息对象
 */
export function saveOrUpdateUnit(unit) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/unit/saveOrUpdate`, unit).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取某个单位详情
 * @param {Number} unitId 单位id
 */
export function findOneUnitInfo(unitId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/unit/findById`, { id: unitId }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取房屋信息列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} yardName 院落名称
 * @param {String} yardPosition 所属区域
 */
export function getHouseInfoList(currentPage, pageSize, yardName, yardPosition) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/report/houseInfoList`, {
            currentPage: currentPage,
            pageSize: pageSize,
            yardName: yardName,
            yardPosition: yardPosition
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 删除单个房屋信息
 * @param {Number} houseId 房屋id
 */
export function deleteHouse(houseId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/house/deleteHouseByid`, { 'houseId': houseId }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 批量删除房屋信息
 * @param {Array} houseIds 房屋id数组
 */
export function batchDeleteHouse(houseIds) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/house/deleteHouseByIds`, houseIds).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 新增或修改房屋信息
 * @param {Object}  house 房屋信息对象
 */
export function saveOrUpdateHouse(house) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/house/addHouse`, house).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取院落信息列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} yardId 院落id
 * @param {Number} type 类型
 */
export function getYardInfoList(currentPage, pageSize, yardId, type) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/yard/list`, {
            currentPage: currentPage,
            pageSize: pageSize,
            yardId: yardId,
            type: type
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 删除单个院落信息
 * @param {Number} yardId 院落id
 */
export function deleteYard(yardId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/yard/deleteByid`, { 'id': yardId }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 添加院落信息
 * @param {Object} yard 院落信息对象
 */
export function addYard(yard) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/yard/addYard`, yard).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 修改院落信息
 * @param {Object} yard 院落信息对象
 */
export function updateYard(yard) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/yard/updateYard`, yard).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取院落下的楼座经纬度
 * @param {Number} yardId 院落id
 */
export function getYardCoordinate(yardId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/yard/yardBuilds`, { 'id': yardId }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取楼座列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} yardId 院落id
 * @param {String} buildName 楼座名称
 */
export function getBuildInfoList(currentPage, pageSize, yardId, buildName) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/build/list`, {
            currentPage: currentPage,
            pageSize: pageSize,
            buildName: buildName,
            yardId: yardId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}


/**
 * 获取楼座详细信息
 */
export function getBuildDetailInfo(buildId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/build/detail`, {
            'id': buildId,
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 删除单个楼座信息
 * @param {Number} buildId 楼座id
 */
export function deleteBuild(buildId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/build/delete`, { 'buildId': buildId }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 新增楼座信息
 * @param {Object} build 楼座信息对象
 */
export function addBuild(build) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/build/add`, build).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}


/**
 * 修改楼座信息
 * @param {Object} build 楼座信息对象
 */
export function updateBuild(build) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/build/update`, build).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取人员职称关系管理列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} ProfessionalName 职称
 */
export function getProfessionalInfoList(currentPage, pageSize, name) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/professional/list`, {
            currentPage: currentPage,
            pageSize: pageSize,
            professionalName: name
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 删除单个人员职称关系信息
 * @param {Number} professionalId 职称id
 */
export function deleteProfession(professionalId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/professional/delete`, { 'professionalId': professionalId }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 批量删除人员职称关系信息
 * @param {Array} professionalIds 职称id数组
 */
export function batchDeleteProfession(professionalIds) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/professional/batchDelete`, professionalIds).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 新增或修改人员职称关系
 * @param {Object} Professional( 职称信息对象
 */
export function saveOrUpdateProfession(Professional) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/professional/saveOrUpdate`, Professional).then(response => {
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
* 查询物业名称列表
*/
export function getEstateNameList() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/housemanager/estate/getList`, null).then(response => {
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

/**
* 判断院落名称是否重复
* @param {Number} yardName 院落名称
* @param {Number} yardId 院落id
*/
export function yardNameUnique(yardName, yardId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/yard/isRepetition`, {
            yardName: yardName,
            yardId: yardId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 判断院落/土地下是否有出租出借
 * @param {Number} yardId 院落id
 */
export function isHaveRent(yardId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/yard/isHaveRent`, {
            yardId: yardId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 判断院落下是否有楼栋
 * @param {Number} yardId 院落id
 */
export function isHaveBuild(yardId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/yard/isHaveBuild`, {
            yardId: yardId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 判断楼层集合中是否存在房间，如果有，就不能减少楼栋的楼层
 * @param {Number} buildId 楼座id
 * @param {Array} floorName 楼层id数组
 */
export function noHouseCharNull(buildId, floorName) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/build/noHouseCharNull`, {
            buildId: buildId,
            floorName: floorName
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}