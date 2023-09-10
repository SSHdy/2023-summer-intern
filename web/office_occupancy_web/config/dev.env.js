'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')
const codeEnv = require('./code.env')
console.log(codeEnv)
module.exports = merge(
  prodEnv,
  {
    NODE_ENV: '"development"'
  },
  codeEnv
)