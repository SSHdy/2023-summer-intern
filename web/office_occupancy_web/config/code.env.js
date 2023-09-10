/*
 * @param
 * BASE_URL 接口前缀
 * LNG 经度
 * LAT 纬度
 * PRO_TYPE 项目类型
 * PRO_NAME 项目名称
 * PRO_TITLE 项目标题
 */
function getCodeEnv() {
  const args = process.argv
  var res
  console.log(args)
  for (var i = 0; i < args.length; i++) {
    if (args[i].indexOf('--env.CODE_ENV=') !== -1) {
      res = args[i].split('=')[1] || ''
      break
    }
  }
  console.log(res)
  return res;

}

const CODE_ENV = `"${getCodeEnv()}"`
console.log(CODE_ENV)
const { BASE_URL, LNG, LAT, PRO_TYPE, PRO_NAME, PRO_TITLE } = (() => {
  const def = {
    BASE_URL: '""',
    LNG: '""',
    LAT: '""',
    PRO_TYPE: '""'
  }
  console.log(CODE_ENV)
  switch (CODE_ENV) {
    case '"development_sy"':
      def.BASE_URL = '"http://192.168.2.130:7191/"'
      def.LNG = '"112.1613"'
      def.LAT = '"32.029004"'
      def.PRO_TYPE = '"sy"'
      def.PRO_NAME = '"十堰市"'
      def.PRO_TITLE = '"十堰市党政机关办公用房管理系统"'
      break

    case '"production_sy"':
      def.BASE_URL = '"http://10.180.178.115:9110/"'
      def.LNG = '"112.1613"'
      def.LAT = '"32.029004"'
      def.PRO_TYPE = '"sy"'
      def.PRO_NAME = '"十堰市"'
      def.PRO_TITLE = '"十堰市党政机关办公用房管理系统"'
      break
    case '"development_xy"':
      def.BASE_URL = '"http://192.168.2.130:7191/"'
      def.LNG = '"112.20"'
      def.LAT = '"32.08"'
      def.PRO_TYPE = '"xy"'
      def.PRO_NAME = '"襄阳市"'
      def.PRO_TITLE = '"襄阳市机关事务服务中心办公用房管理系统"'
      break

    case '"production_xy"':
      def.BASE_URL = '"http://10.180.178.115:9110/"'
      def.LNG = '"112.20"'
      def.LAT = '"32.08"'
      def.PRO_TYPE = '"xy"'
      def.PRO_NAME = '"襄阳市"'
      def.PRO_TITLE = '"襄阳市机关事务服务中心办公用房管理系统"'
      break
    case '"production_sy_test130"':
      def.BASE_URL = '"http://192.168.2.130:7191/"'
      def.LNG = '"112.1613"'
      def.LAT = '"32.029004"'
      def.PRO_TYPE = '"sy"'
      def.PRO_NAME = '"十堰市"'
      def.PRO_TITLE = '"十堰市党政机关办公用房管理系统"'
      break
    case '"production_sy_test93"':
      def.BASE_URL = '"http://192.168.2.93:7191/"'
      def.LNG = '"112.1613"'
      def.LAT = '"32.029004"'
      def.PRO_TYPE = '"sy"'
      def.PRO_NAME = '"十堰市"'
      def.PRO_TITLE = '"十堰市党政机关办公用房管理系统"'
      break
  }
  return def
})()

module.exports = {
  BASE_URL,
  LNG,
  LAT,
  PRO_TYPE,
  PRO_NAME,
  PRO_TITLE
}
