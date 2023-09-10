import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store/index';
import { PRO_TYPE } from "@/axios/global"

// 登陆
const login = () =>
  import('@/pages/login/login');
// 大系统登陆
const loginRy = () =>
  import('@/pages/login/loginRy');
// 主页
const home = () =>
  import('@/pages/home/index');

// 导航页
let nav = null;
if (PRO_TYPE === 'yc') {
  nav = () =>
    import('@/pages/nav/nav_3');
} else {
  nav = () =>
    import('@/pages/nav/nav_2');
}

// 办公用房管理
const houseView = () =>
  import('@/pages/houseManagement/view');
const houseGraph = () =>
  import('@/pages/houseManagement/graph');
const houseGraphView = () =>
  import('@/pages/houseManagement/graphView');
const houseCertificate = () =>
  import('@/pages/houseManagement/houseCertificate');
const landCertificate = () =>
  import('@/pages/houseManagement/landCertificate');
const immovables = () =>
  import('@/pages/houseManagement/immovables');
const estate = () =>
  import('@/pages/houseManagement/estate');
const houseDetail = () =>
import('@/pages/houseManagement/houseDetail');

// 处置管理
const rentManagement = () =>
  import('@/pages/disposeManagement/rentManagement')
const rentRemind = () =>
  import('@/pages/disposeManagement/rentRemind')
const lentManagement = () =>
  import('@/pages/disposeManagement/lentManagement')
const disposeApply = () =>
  import('@/pages/disposeManagement/disposeApply')
const disposeBacklog = () =>
  import('@/pages/disposeManagement/disposeBacklog')
const disposeQuery = () =>
  import('@/pages/disposeManagement/disposeQuery')
const disposeList = () =>
  import('@/pages/disposeManagement/disposeList')
const expireRemind = () =>
  import('@/pages/disposeManagement/expireRemind')

// 配置管理
const configApply = () =>
  import('@/pages/configManagement/configApply')
const configBacklog = () =>
  import('@/pages/configManagement/configBacklog')
const configQuery = () =>
  import('@/pages/configManagement/configQuery')
const roomDistributeView = () =>
  import('@/pages/configManagement/graphRoomDistribute')

// 维修管理
const repairRemind = () =>
  import('@/pages/repairManagement/repairRemind')
const qualityAssuranceDate = () =>
  import("@/pages/repairManagement/qualityAssuranceDate")
const repairApply = () =>
  import("@/pages/repairManagement/repairApply")
const repairProjectManage = () =>
  import("@/pages/repairManagement/repairProjectManage")
const waittingThing = () =>
  import("@/pages/repairManagement/waittingThing")
const repairView = () =>
  import("@/pages/repairManagement/repairView")

// 资产管理
const assetDevice = () =>
  import('@/pages/assetManagement/device')
const assetRegister = () =>
  import('@/pages/assetManagement/assetRegister')
const assetSituation = () =>
  import('@/pages/assetManagement/assetSituation')
const assetStorage = () =>
  import('@/pages/assetManagement/assetStorage')
const assetList = () =>
  import('@/pages/assetManagement/assetList')
const assetDetail = () =>
  import('@/pages/assetManagement/assetDetail')

// 使用管理
const houseProof = () =>
  import('@/pages/useManagement/houseProof') // 超标房间统计
const houseType = () =>
  import('@/pages/useManagement/houseType') // 房屋类型统计
const spareRoom = () =>
  import('@/pages/useManagement/spareRoom') // 闲置用房统计
const rentRoomReport = () =>
  import('@/pages/useManagement/rentRoomReport') // 出租房间统计
const unitOverproof = () =>
  import('@/pages/useManagement/unitOverproof') // 超标单位统计
const houseDetailSum = () =>
  import('@/pages/useManagement/houseDetailSum') // 办公用房明细汇总


// 档案管理
const infoRecord = () =>
  import('@/pages/recordManagement/infoRecord')
const repairRecord = () =>
  import('@/pages/recordManagement/repairRecord')


// 基础信息管理
const baseInfoUnit = () =>
  import('@/pages/baseInfoManagement/unit')
const baseInfoYard = () =>
  import('@/pages/baseInfoManagement/yard')
const baseInfoBuild = () =>
  import('@/pages/baseInfoManagement/build')
const baseInfoProfession = () =>
  import('@/pages/baseInfoManagement/profession')
const repairPart = () =>
  import('@/pages/baseInfoManagement/repairPart')

// 系统管理
const user = () =>
  import('@/pages/systemManagement/user')
const menus = () =>
  import('@/pages/systemManagement/menus')
const role = () =>
  import('@/pages/systemManagement/role')
const log = () =>
  import('@/pages/systemManagement/log')
const client = () =>
  import('@/pages/systemManagement/client')
const dict = () =>
  import('@/pages/systemManagement/dict')

// 工作统计
const handle_year = () =>
  import('@/pages/workStatistics/handle-year')
const handle_month = () =>
  import('@/pages/workStatistics/handle-month')
const repair_year = () =>
  import('@/pages/workStatistics/repair-year')
const repair_month = () =>
  import('@/pages/workStatistics/repair-month')
const config_year = () =>
  import('@/pages/workStatistics/config-year')
const config_month = () =>
  import('@/pages/workStatistics/config-month')

// 数据上报
const dataCollect = () =>
  import('@/pages/dataReport/dataCollect')
const dataReport = () =>
  import('@/pages/dataReport/dataReport')

//专项检查
const specialCheck = () =>
  import('@/pages/special/specialCheck')
Vue.use(Router)

var router = new Router({
  routes: [
    // 重定向
    {
      path: '*',
      redirect: '/login'
    },
    // 登录
    {
      path: '/login',
      name: 'login',
      component: login,
      name: 'login'
    },
        // 大系统登录
        {
          path: '/loginRy',
          name: 'loginRy',
          component: loginRy,
          name: 'loginRy'
        },
    {
      path: '/home',
      name: 'home',
      component: home,
      children: [
        // 导航页面
        {
          path: 'nav',
          component: nav,
          meta: {
            requireAuth: true
          },
          name: 'nav'
        }
      ]
    },
    {
      path: '/home/:id',
      name: 'home',
      component: home,
      children: [
        // 办公用房管理
        {
          path: 'houseView',
          component: houseView,
          meta: {
            requireAuth: true
          },
          name: 'houseView'
        },
        {
          path: 'houseGraph',
          component: houseGraph,
          meta: {
            requireAuth: true
          },
          name: 'houseGraph'
        },
        {
          path: 'houseGraphView',
          component: houseGraphView,
          meta: {
            requireAuth: true
          },
          name: 'houseGraphView'
        },
        {
          path: 'houseCertificate',
          component: houseCertificate,
          meta: {
            requireAuth: true
          },
          name: 'houseCertificate'
        },
        {
          path: 'roomDistributeView',
          component: roomDistributeView,
          meta: {
            requireAuth: true
          },
          name: 'roomDistributeView'
        },
        {
          path: 'landCertificate',
          component: landCertificate,
          meta: {
            requireAuth: true
          },
          name: 'landCertificate'
        },
        {
          path: 'estate',
          component: estate,
          meta: {
            requireAuth: true
          },
          name: 'estate'
        },
        {
          path: 'houseDetail',
          component: houseDetail,
          meta: {
            requireAuth: true
          },
          name: 'houseDetail'
        },
        {
          path: 'immovables',
          component: immovables,
          meta: {
            requireAuth: true
          },
          name: 'immovables'
        },
        // 处置管理
        {
          path: 'rentManagement',
          component: rentManagement,
          meta: {
            requireAuth: true
          },
          name: 'rentManagement'
        },
        {
          path: 'rentRemind',
          component: rentRemind,
          meta: {
            requireAuth: true
          },
          name: 'rentRemind'
        },
        {
          path: 'lentManagement',
          component: lentManagement,
          meta: {
            requireAuth: true
          },
          name: 'lentManagement'
        },
        {
          path: 'disposeApply',
          component: disposeApply,
          meta: {
            requireAuth: true
          },
          name: 'disposeApply'
        },
        {
          path: 'disposeBacklog',
          component: disposeBacklog,
          meta: {
            requireAuth: true
          },
          name: 'disposeBacklog'
        },
        {
          path: 'disposeQuery',
          component: disposeQuery,
          meta: {
            requireAuth: true
          },
          name: 'disposeQuery'
        },
        {
          path: 'disposeList',
          component: disposeList,
          meta: {
            requireAuth: true
          },
          name: 'disposeList'
        },
        {
          path: 'expireRemind',
          component: expireRemind,
          meta: {
            requireAuth: true
          },
          name: 'expireRemind'
        },
        // 配置管理
        {
          path: 'configApply',
          component: configApply,
          meta: {
            requireAuth: true
          },
          name: 'configApply'
        },
        {
          path: 'configBacklog',
          component: configBacklog,
          meta: {
            requireAuth: true
          },
          name: 'configBacklog'
        },
        {
          path: 'configQuery',
          component: configQuery,
          meta: {
            requireAuth: true
          },
          name: 'configQuery'
        },
        // 维修管理
        {
          path: 'repairRemind',
          component: repairRemind,
          name: 'repairRemind',
          meta: {
            requireAuth: true
          },
          name: 'repairRemind'
        },
        {
          path: 'qualityAssuranceDate',
          component: qualityAssuranceDate,
          meta: {
            requireAuth: true
          },
          name: 'qualityAssuranceDate'
        },
        {
          path: 'repairApply',
          component: repairApply,
          meta: {
            requireAuth: true
          },
          name: 'repairApply'
        },
        {
          path: 'repairProjectManage',
          component: repairProjectManage,
          meta: {
            requireAuth: true
          },
          name: 'repairProjectManage'
        },
        {
          path: 'waittingThing',
          component: waittingThing,
          meta: {
            requireAuth: true
          },
          name: 'waittingThing'
        },
        {
          path: 'repairView',
          component: repairView,
          meta: {
            requireAuth: true
          },
          name: 'repairView'
        },
        // 资产管理
        {
          path: 'assetRegister',
          component: assetRegister,
          meta: {
            requireAuth: true
          },
          name: 'assetRegister'
        },
        {
          path: 'assetSituation',
          component: assetSituation,
          meta: {
            requireAuth: true
          },
          name: 'assetSituation'
        },
        {
          path: 'assetDevice',
          component: assetDevice,
          meta: {
            requireAuth: true
          },
          name: 'assetDevice'
        },
        {
          path: 'assetStorage',
          component: assetStorage,
          meta: {
            requireAuth: true
          },
          name: 'assetStorage'
        },
        {
          path: 'assetList',
          component: assetList,
          meta: {
            requireAuth: true
          },
          name: 'assetList'
        },
        {
          path: 'assetDetail',
          component: assetDetail,
          meta: {
            requireAuth: true
          },
          name: 'assetDetail'
        },
        // 统计报表
        {
          path: 'houseProof',
          component: houseProof,
          meta: {
            requireAuth: true
          },
          name: 'houseProof'
        },
        {
          path: 'houseType',
          component: houseType,
          meta: {
            requireAuth: true
          },
          name: 'houseType'
        },
        {
          path: 'spareRoom',
          component: spareRoom,
          meta: {
            requireAuth: true
          },
          name: 'spareRoom'
        },
        {
          path: 'rentRoomReport',
          component: rentRoomReport,
          meta: {
            requireAuth: true
          },
          name: 'rentRoomReport'
        },
        {
          path: 'unitOverproof',
          component: unitOverproof,
          meta: {
            requireAuth: true
          },
          name: 'unitOverproof'
        },
        {
          path: 'houseDetailSum',
          component: houseDetailSum,
          meta: {
            requireAuth: true
          },
          name: 'houseDetailSum'
        },
        // 档案管理
        {
          path: 'infoRecord',
          component: infoRecord,
          meta: {
            requireAuth: true
          },
          name: 'infoRecord'
        },
        {
          path: 'repairRecord',
          component: repairRecord,
          meta: {
            requireAuth: true
          },
          name: 'repairRecord'
        },
        // 基础信息管理
        {
          path: 'baseInfoUnit',
          component: baseInfoUnit,
          meta: {
            requireAuth: true
          },
          name: 'baseInfoUnit'
        },
        {
          path: 'baseInfoYard',
          component: baseInfoYard,
          meta: {
            requireAuth: true
          },
          name: 'baseInfoYard'
        },
        {
          path: 'baseInfoBuild',
          component: baseInfoBuild,
          meta: {
            requireAuth: true
          },
          name: 'baseInfoBuild'
        },
        {
          path: 'baseInfoProfession',
          component: baseInfoProfession,
          meta: {
            requireAuth: true
          },
          name: 'baseInfoProfession'
        },
        {
          path: 'repairPart',
          component: repairPart,
          meta: {
            requireAuth: true
          },
          name: 'repairPart'
        },
        // 系统管理
        {
          path: 'user',
          component: user,
          name: 'user',
          meta: {
            requireAuth: true
          },
          name: 'user'
        },
        {
          path: 'role',
          component: role,
          meta: {
            requireAuth: true
          },
          name: 'role'
        },
        {
          path: 'menus',
          component: menus,
          meta: {
            requireAuth: true
          },
          name: 'menus'
        },
        {
          path: 'log',
          component: log,
          meta: {
            requireAuth: true
          },
          name: 'log'
        },
        {
          path: 'client',
          component: client,
          meta: {
            requireAuth: true
          },
          name: 'client'
        },
        {
          path: 'dict',
          component: dict,
          meta: {
            requireAuth: true
          },
          name: 'dict'
        },
        // 工作统计
        {
          path: 'handle-year',
          component: handle_year,
          meta: {
            requireAuth: true
          },
          name: 'handle-year'
        },
        {
          path: 'handle-month',
          component: handle_month,
          meta: {
            requireAuth: true
          },
          name: 'handle-month'
        },
        {
          path: 'repair-year',
          component: repair_year,
          meta: {
            requireAuth: true
          },
          name: 'repair-year'
        },
        {
          path: 'repair-month',
          component: repair_month,
          meta: {
            requireAuth: true
          },
          name: 'repair-month'
        },
        {
          path: 'config-year',
          component: config_year,
          meta: {
            requireAuth: true
          },
          name: 'config-year'
        },
        {
          path: 'config-month',
          component: config_month,
          meta: {
            requireAuth: true
          },
          name: 'config-month'
        },
        // 数据上报
        {
          path: 'dataReport',
          component: dataReport,
          meta: {
            requireAuth: true
          },
          name: 'dataReport'
        },
        {
          path: 'dataCollect',
          component: dataCollect,
          meta: {
            requireAuth: true
          },
          name: 'dataCollect'
        },
        {
          path: 'specialCheck',
          component: specialCheck,
          meta: {
            requireAuth: true
          },
          name: 'specialCheck'
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  console.log(to.fullPath.split('?'))
  console.log(to)
    if (to.meta.requireAuth) {
      console.log(1)
      const token = sessionStorage.getItem('TOKEN');
      if (token) {
        next();
      } else {
        next({ path: '/login' });
        store.dispatch('Logout', token);
      }
    } else {
      console.log(2)
      next();
    }
  
});
router.onError((error) => {
  const pattern = /Loading chunk (\d)+ failed/g;
  const isChunkLoadFailed = error.message.match(pattern);
  const targetPath = router.history.pending.fullPath;
  if (isChunkLoadFailed) {
    router.replace(targetPath);
  }
});
export default router;