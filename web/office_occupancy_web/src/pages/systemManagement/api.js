import axios from '@/axios/index'
import { url_name_1, url_name_2 } from '@/axios/global';
/**
 * 获取菜单信息列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 */
export function getMenuInfoList(currentPage, pageSize) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/menu/menuList`, {
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
 * 获取菜单信息列表(根据查询条件)
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} menuTitle 菜单名称
 */
export function getMenuInfoList2(currentPage, pageSize, menuTitle) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/menu/menuTitleList`, {
            currentPage: currentPage,
            pageSize: pageSize,
            menuTitle: menuTitle
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取菜单信息列表(无分页)
 */
export function getMenuInfoList3() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/menu/menus`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 新增菜单
 * @param {object} menu 菜单对象
 */
export function addMenu(menu) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/menu/add`, menu).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 修改菜单
 * @param {object} menu 菜单对象
 */
export function updateMenu(menu) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/menu/update`, menu).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 用于判断菜单名是否重复
 * @param {String} menuTitle 菜单名称
 * @param {Number} menuId 菜单id
 */
export function menuNameUnique(menuTitle, menuId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/menu/unique`, {
            menuTitle: menuTitle,
            menuId: menuId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询菜单详情
 * @param {Number} menuId 菜单id
 */
export function findOneMenuInfo(menuId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/menu/selectMenu`, {
            menuId: menuId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 删除单个菜单
 * @param {Number} menuId 菜单id
 */
export function deleteMenu(menuId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/menu/delete`, {
            id: menuId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 批量删除菜单
 * @param {Array} menuIds 菜单id数组
 */
export function batchDeleteMenu(menuIds) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/menu/deleteIds`, {
            menuIdList: menuIds
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取角色信息列表(有分页)
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} roleName 角色名称
 */
export function getRoleInfoList(currentPage, pageSize, roleName) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/role/roleList`, {
            currentPage: currentPage,
            pageSize: pageSize,
            roleName: roleName
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取角色信息列表（无分页）
 */
export function getRoleInfoList_2() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/role/roles`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 新增角色
 * @param {object} role 角色对象
 */
export function addRole(role) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/role/addRole`, role).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 新增或者修改角色
 * @param {object} role 角色对象
 */
export function updateRole(role) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/role/updateRole`, role).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 用于判断角色名是否重复
 * @param {String} roleName 角色名称
 * @param {Number} roleId 角色id
 */
export function roleNameUnique(roleName, roleId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/role/unique`, {
            roleName: roleName,
            roleId: roleId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 删除单个角色
 * @param {Number} roleId 角色id
 */
export function deleteRole(roleId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/role/delete`, { 'roleId': roleId }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 批量删除角色
 * @param {Array} roleIds 角色id数组
 */
export function batchDeleteRole(roleIds) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/role/deleteIds`, {
            roleIdList: roleIds
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询角色详情
 * @param {Number} roleId 角色id
 */
export function findOneRoleInfo(roleId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/role/roleDetail`, {
            id: roleId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询角色详情(无一级菜单)
 * @param {Number} roleId 角色id
 */
export function findOneRoleInfo_2(roleId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/role/roleUpdate`, {
            id: roleId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获取用户信息列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} name 用户姓名
 * @param {String} username 登录名
 */
export function getUserInfoList(currentPage, pageSize, name, username) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/user/list`, {
            currentPage: currentPage,
            pageSize: pageSize,
            name: name,
            username: username
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 新增用户
 * @param {object} user 用户对象
 */
export function addUser(user) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/user/add`, user).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 修改用户
 * @param {object} user 用户对象
 */
export function updateUser(user) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/user/update`, user).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 用于判断用户名是否重复
 * @param {String} username 登录名
 * @param {Number} userId 用户id
 */
export function userNameUnique(username, userId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/user/unique`, {
            username: username,
            userId: userId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 删除单个用户
 * @param {Number} userId 用户id
 */
export function deleteUser(userId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/user/delete`, { 'userId': userId }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 批量删除用户
 * @param {Array} userIds 用户id数组
 */
export function batchDeleteUser(userIds) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/user/deleteIds`, {
            userIdList: userIds
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询用户详情
 * @param {Number} userId 角色id
 */
export function findOneUserInfo(userId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/user/selectUserAndRole`, {
            userId: userId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 所有单位树形菜单（无权限）
 * @param {Number} id 树节点id
 */
export function getAllUnitTree(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/unit/all/tree`, {
            id: id
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 获得单位无穷树
 */
export function getInfinityUnitTree() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/tree/unitTree`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 维修流程结点查询
 */
export function getRepairList() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/statue/repair/list`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 查询系统日志
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} userName 操作人
 * @param {String} beginTime 操作开始时间
 * @param {String} endTime 操作结束时间
 */
export function getSystemLog(currentPage, pageSize, userName, beginTime, endTime) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/operationLog/list`, {
            currentPage: currentPage,
            pageSize: pageSize,
            userName: userName,
            beginTime: beginTime,
            endTime: endTime
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 处置管理流程列表
 */
export function gethandleList() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/statue/handle/list`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 配置管理结点查询
 */
export function getConfigList() {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/statue/config/list`).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

// 客户端管理
/**
 * 查询授权列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 */
export function selectClientList(currentPage, pageSize) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/client/selectClientList`, { currentPage, pageSize }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 删除授权
 * @param {Number} id 授权列表的id
 */
export function deleteClient(id) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/client/delete`, { id }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 新增授权
 * @param {Object} Form 表单对象
 */
export function addClient(Form) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/client/add`, {
            authorizedGrantTypes: Form.authorizedGrantType,
            autoapprove: Form.autoapprove,
            clientId: Form.clientId,
            clientSecret: Form.clientSecret,
            companyName: Form.companyName,
            resourceIds: Form.resourceIds,
            scope: Form.scope,
            webServerRedirectUri: Form.webServerRedirectUri,
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 修改授权
 * @param {Object} Form 表单对象
 */
export function updateClient(Form) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/client/update`, {
            authorizedGrantTypes: Form.authorizedGrantTypes,
            autoapprove: Form.autoapprove,
            clientId: Form.clientId,
            clientSecret: Form.clientSecret,
            companyName: Form.companyName,
            resourceIds: Form.resourceIds,
            oauthDetailId: Form.oauthDetailId,
            scope: Form.scope,
            webServerRedirectUri: Form.webServerRedirectUri,
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 用于客户端id名是否重复
 * @param {String} clientId 客户端id
 * @param {Number} oauthDetailId 列表id
 */
export function clientUnique(clientId, oauthDetailId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/client/unique`, {
            clientId: clientId,
            oauthDetailId: oauthDetailId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 维修字典列表
 * @param {Number} currentPage 当前页码
 * @param {Number} pageSize 每页显示个数
 * @param {String} repairDicKey 维修字典键
 */
export function getRepairDicList(currentPage, pageSize, repairDicKey) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairDic/list`, {
            currentPage: currentPage,
            pageSize: pageSize,
            repairDicKey: repairDicKey
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 维修字典列表
 * @param {Object} Form 字典表表单对象
 */
export function saveOrUpdateDic(Form) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_1}/repairDic/saveOrUpdate`, Form).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}

/**
 * 用于判断手机号是否重复
 * @param {String} phoneNum 手机号
 * @param {Number} userId 用户id
 */
export function uniquePhoneName(phoneNum, userId) {
    return new Promise((resolve, reject) => {
        axios.post(`${url_name_2}/user/uniquePhoneName`, {
            phoneNum: phoneNum,
            userId: userId
        }).then(response => {
            resolve(response);
        }, err => {
            reject(err)
        })
    })
}