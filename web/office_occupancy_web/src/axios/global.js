// let BASE_URL = 'http://192.168.2.130:8091/';
// let BASE_URL = 'http://192.168.2.93:8091/';
// let BASE_URL = 'http://www.pcitiot.com:8091/'
console.log(process.env);
// let BASE_URL = process.env.BASE_URL;
let BASE_URL = "http://192.168.2.93:7191/"
// let BASE_URL ="http://10.180.178.115:9110/";
let LNG = process.env.LNG || "112.1613";
let LAT = process.env.LAT || "32.029004";
let PRO_TYPE = process.env.PRO_TYPE;
let PRO_NAME = process.env.PRO_NAME;
let PRO_TITLE = process.env.PRO_TITLE;
const url_name_1 = 'houseapi';
const url_name_2 = 'authorityapi';

export {
  BASE_URL,
  LNG,
  LAT,
  PRO_TYPE,
  PRO_NAME,
  PRO_TITLE,
  url_name_1,
  url_name_2
}
