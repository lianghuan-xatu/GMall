import request from '@/utils/request'

export default {

  // 查找一级分类
  getCatalog1() {
    return request({
      url: 'getCatalog1',
      method: 'post'
    })
  },

  // 查找二级分类
  getCatalog2(catalog1Id) {
    return request({
      url: 'getCatalog2?catalog1Id=' + catalog1Id,
      method: 'post'
    })
  },

  // 查找三级分类
  getCatalog3(catalog2Id) {
    return request({
      url: 'getCatalog3?catalog2Id=' + catalog2Id,
      method: 'post'
    })
  }

}
