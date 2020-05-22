<template>
  <div class="app-container">

    <!--三级下拉列表-->
    <CatalogSelector v-show="!showSkuForm" @listenOnSelect="getSpuList" />

    <!--spu列表-->
    <div v-show="!showSpuForm && !showSkuForm">
      <div style="margin-bottom:10px;">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="addSpu()">添加SPU</el-button>
      </div>

      <el-table
        v-loading="spuListLoading"
        :data="spuList"
        element-loading-text="数据正在加载......"
        border
        fit
        highlight-current-row>
        <el-table-column align="center" label="序号" width="100">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="商品id" width="100">
          <template slot-scope="scope">
            {{ scope.row.id }}
          </template>
        </el-table-column>
        <el-table-column label="商品名称">
          <template slot-scope="scope">
            <span>{{ scope.row.spuName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="商品描述">
          <template slot-scope="scope">
            <span>{{ scope.row.description }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-plus" @click="addSku(scope.row.id, scope.row.spuName)">添加SKU</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!--spu表单-->
    <SpuForm
      v-show="showSpuForm"
      ref="spuForm"
      :catalog-id="catalogId"
      @listenOnSave="onSpuSave()"
      @listenOnClose="onSpuClose()"/>

    <!--sku表单-->
    <SkuForm
      v-show="showSkuForm"
      ref="skuForm"
      :catalog-id="catalogId"
      :spu-id="selectedSpu.spuId"
      :spu-name="selectedSpu.spuName"
      @listenOnSave="onSkuSave()"
      @listenOnClose="onSkuClose()"/>

  </div>
</template>

<script>
import spu from '@/api/product/spu'
import CatalogSelector from '@/views/components/CatalogSelector'
import SpuForm from '@/views/product/components/SpuForm'
import SkuForm from '@/views/product/components/SkuForm'

export default {
  components: { CatalogSelector, SpuForm, SkuForm },

  data() {
    return {

      // spu所属分类
      catalogId: null,

      // spu列表数据
      spuList: null,
      spuListLoading: false,

      // spu表单显示
      showSpuForm: false,

      // sku表单显示
      showSkuForm: false,

      // 选中的spu
      selectedSpu: {
        spuId: null,
        spuName: null
      }
    }
  },

  methods: {

    // 获取spu列表
    getSpuList(catalogId) {
      this.catalogId = catalogId
      // 查询数据
      this.spuListLoading = true
      spu.getSpuList(this.catalogId).then(response => {
        this.spuList = response.data
        this.spuListLoading = false
      })
    },

    // 选择三级分类确认
    confirmSelect() {
      if (!this.catalogId) {
        this.$alert('请选择三级分类', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        })
        return false
      }
      return true
    },

    // 添加spu
    addSpu() {
      if (!this.confirmSelect()) {
        return
      }

      // 初始化表单
      this.$refs.spuForm.init()

      // 显示表单
      this.showSpuForm = true
    },

    // 保存spu后刷新列表
    onSpuSave() {
      // 刷新Spu列表
      this.getSpuList(this.catalogId)

      // 隐藏表单
      this.showSpuForm = false
    },

    // 关闭spu表单
    onSpuClose() {
      // 隐藏表单
      this.showSpuForm = false
    },

    // 添加sku
    addSku(spuId, spuName) {
      this.selectedSpu.spuId = spuId
      this.selectedSpu.spuName = spuName

      // 初始化表单
      this.$refs.skuForm.init(spuId)

      // 显示表单
      this.showSkuForm = true
    },

    // 保存sku
    onSkuSave() {
      // 隐藏表单
      this.showSkuForm = false
    },

    // 关闭sku表单
    onSkuClose() {
      // 隐藏表单
      this.showSkuForm = false
    }

  }
}
</script>
