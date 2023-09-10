'use strict'
const merge = require('webpack-merge')
const codeEnv = require('./code.env')
console.log(codeEnv)
module.exports = merge(
  {
    NODE_ENV: '"production"'
  },
  codeEnv
)