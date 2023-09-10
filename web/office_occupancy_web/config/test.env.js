'use strict'
const merge = require('webpack-merge')
const devEnv = require('./dev.env')
console.log(codeEnv)
module.exports = merge(devEnv, {
  NODE_ENV: '"testing"'
})
