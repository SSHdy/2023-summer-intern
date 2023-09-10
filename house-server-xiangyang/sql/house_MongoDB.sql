/*
 Navicat Premium Data Transfer

 Source Server         : 130mongodb
 Source Server Type    : MongoDB
 Source Server Version : 40004
 Source Host           : 192.168.2.130:27017
 Source Schema         : house_shiyan

 Target Server Type    : MongoDB
 Target Server Version : 40004
 File Encoding         : 65001

 Date: 09/05/2023 11:10:34
*/


// ----------------------------
// Collection structure for configure
// ----------------------------
db.getCollection("configure").drop();
db.createCollection("configure");

// ----------------------------
// Collection structure for handle
// ----------------------------
db.getCollection("handle").drop();
db.createCollection("handle");

// ----------------------------
// Collection structure for jsonPlanDB
// ----------------------------
db.getCollection("jsonPlanDB").drop();
db.createCollection("jsonPlanDB");
db.getCollection("jsonPlanDB").createIndex({
    buildId: NumberInt("1"),
    floorName: NumberInt("1")
}, {
    name: "build_floor_index",
    unique: true
});

// ----------------------------
// Collection structure for operationlog
// ----------------------------
db.getCollection("operationlog").drop();
db.createCollection("operationlog");
db.getCollection("operationlog").createIndex({
    userName: NumberInt("1")
}, {
    name: "userName"
});
db.getCollection("operationlog").createIndex({
    operationTime: NumberInt("1")
}, {
    name: "operationTime"
});

// ----------------------------
// Collection structure for propertyRight
// ----------------------------
db.getCollection("propertyRight").drop();
db.createCollection("propertyRight");
