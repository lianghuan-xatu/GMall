'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')
// 后端接口地址
module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  BASE_API: '"http://localhost:8081"'
})
