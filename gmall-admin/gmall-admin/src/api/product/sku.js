import request from '@/utils/request'

export default {

  // 保存Sku
  saveSkuInfo(skuForm) {
    return request({
      url: 'saveSkuInfo',
      method: 'post',
      data: skuForm
    })
  }
}
