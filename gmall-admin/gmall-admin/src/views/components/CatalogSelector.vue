<template>
  <!--查询表单-->
  <el-form :inline="true" class="demo-form-inline">

    <!-- 一级分类 -->
    <el-form-item label="一级分类">
      <el-select
        v-model="catalog1Id"
        placeholder="请选择"
        @change="catalog1Changed">
        <el-option
          v-for="catalog in catalogList1"
          :key="catalog.id"
          :label="catalog.name"
          :value="catalog.id"/>
      </el-select>
    </el-form-item>

    <!-- 二级分类 -->
    <el-form-item label="二级分类">
      <el-select
        v-model="catalog2Id"
        placeholder="请选择"
        @change="catalog2Changed">
        <el-option
          v-for="catalog in catalogList2"
          :key="catalog.id"
          :label="catalog.name"
          :value="catalog.id"/>
      </el-select>
    </el-form-item>

    <!-- 三级分类 -->
    <el-form-item label="三级分类">
      <el-select
        v-model="catalog3Id"
        placeholder="请选择"
        @change="catalog3Changed">
        <el-option
          v-for="catalog in catalogList3"
          :key="catalog.id"
          :label="catalog.name"
          :value="catalog.id"/>
      </el-select>
    </el-form-item>

  </el-form>

</template>

<script>
import prop from '@/api/components/CatalogSelector'

export default {

  data() {
    return {
      // 查询表单数据
      catalog1Id: null,
      catalog2Id: null,
      catalog3Id: null,
      catalogList1: [],
      catalogList2: [],
      catalogList3: []
    }
  },

  // 初始化一级类别
  created() {
    prop.getCatalog1().then(response => {
      this.catalogList1 = response.data
    })
  },

  methods: {
    // 切换二级类别
    catalog1Changed() {
      prop.getCatalog2(this.catalog1Id).then(response => {
        this.catalog2Id = null
        this.catalog3Id = null
        this.catalogList2 = response.data
      })

      // 清空属性列表
      this.attrInfoList = null
    },

    // 切换三级类别
    catalog2Changed() {
      prop.getCatalog3(this.catalog2Id).then(response => {
        this.catalog3Id = null
        this.catalogList3 = response.data
      })
    },

    // 显示属性列表
    catalog3Changed() {
      // 子组件向父组件传值
      this.$emit('listenOnSelect', this.catalog3Id)
    }

  }

}
</script>

