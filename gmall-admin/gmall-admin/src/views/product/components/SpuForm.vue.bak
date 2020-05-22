<template>
  <!--表单-->
  <el-form :model="spuForm" label-width="70px">

    <el-form-item label="spu名称">
      <el-input v-model="spuForm.spuName"/>
    </el-form-item>

    <el-form-item label="spu描述">
      <el-input v-model="spuForm.description" :rows="5" type="textarea"/>
    </el-form-item>

    <el-form-item label="图片上传" >
      <el-upload
        :on-success="onUploadSuccess"
        :before-upload="beforeUpload"
        :on-preview="onUploadPreview"
        :on-remove="onUploadRemove"
        :multiple="true"
        :action="BASE_API+'/fileUpload'"
        class="upload-demo"
        list-type="picture-card">
        <i class="el-icon-plus"/>
        <div slot="tip" class="el-upload__tip">只能上传jpg/png/gif文件，且不超过2MB</div>
      </el-upload>
    </el-form-item>

    <div style="margin-bottom:10px;">
      <el-button type="primary" icon="el-icon-plus" size="mini" @click="addSaleAttr()">添加销售属性</el-button>
    </div>
    <div>
      <el-table
        v-loading="spuSaleAttrListLoading"
        ref="spuSaleAttrTable"
        :data="spuSaleAttrListTemp"
        element-loading-text="数据正在加载......"
        border
        fit
        highlight-current-row>
        <el-table-column align="center" label="序号" width="100">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="销售属性id" width="100">
          <template slot-scope="scope">
            {{ scope.row.saleAttrId }}
          </template>
        </el-table-column>
        <el-table-column label="销售属性名称" width="200">
          <template slot-scope="scope">
            <el-select
              ref="saleAttr"
              v-model="scope.row.saleAttr"
              placeholder="请选择">
              <el-option
                v-for="baseSaleAttr in baseSaleAttrList"
                :key="baseSaleAttr.id"
                :label="baseSaleAttr.name"
                :value="baseSaleAttr.id+'|'+baseSaleAttr.name"/>
            </el-select>
          </template>
        </el-table-column>

        <el-table-column label="销售属性值">
          <template slot-scope="scope">
            <el-tag
              v-for="tag in scope.row.spuSaleAttrValueList"
              :key="tag"
              :disable-transitions="false"
              closable
              @close="handleTagClose(tag, scope.row)">
              {{ tag }}
            </el-tag>

            <el-input
              v-if="scope.row.edit"
              v-model="scope.row.saleAttrValue"
              class="input-new-tag"
              size="small"
              @keyup.enter.native="saveAttrValue(scope.row)"
              @blur="saveAttrValue(scope.row)" />
            <el-button v-else class="button-new-tag" size="small" @click="editAttrValue(scope.row)">+ 添加</el-button>

        </template></el-table-column>

        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" icon="el-icon-edit" @click="deleteSaleAttr(scope.row.saleAttr)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!--按钮-->
    <div style="margin-top:22px;">
      <el-button type="primary" size="mini" @click="saveSpuInfo()">保存</el-button>
      <el-button type="default" size="mini" @click="backToSpuList()">返回</el-button>
    </div>

    <!--图片预览-->
    <el-dialog :visible.sync="dialogImageVisible">
      <img :src="dialogImageUrl" width="100%" alt="">
    </el-dialog>
  </el-form>

</template>

<script>
import spu from '@/api/product/spu'
export default {

  props: {
    catalogId: {
      type: String,
      default: null
    }
  },

  data() {
    return {

      // 接口API地址
      BASE_API: process.env.BASE_API,

      // 基本销售属性列表
      baseSaleAttrList: [],

      // Spu表单数据
      spuForm: {
        id: null,
        spuName: null,
        description: null,
        catalog3Id: null,
        // 上传文件列表
        spuImageList: [],
        // 销售属性
        spuSaleAttrList: []
      },
      spuSaleAttrListTemp: [], // 临时数据：格式：saleAttrId|saleAttrName

      // 销售属性列表loading
      spuSaleAttrListLoading: false,

      // 图片预览区域url
      dialogImageUrl: '',
      // 图片预览对话框
      dialogImageVisible: false

    }
  },

  methods: {

    init() {
      // 初始化值
      // Spu表单数据
      this.spuForm = {
        id: null,
        spuName: null,
        description: null,
        catalog3Id: null,
        // 上传文件列表
        spuImageList: [],
        // 销售属性
        spuSaleAttrList: []
      }

      this.getBaseSaleAttrList()
    },

    // 获取基本销售属性列表
    getBaseSaleAttrList() {
      spu.getBaseSaleAttrList().then(response => {
        this.baseSaleAttrList = response.data
      })
    },

    // 保存Spu
    saveSpuInfo() {
      this.spuForm.catalog3Id = this.catalogId
      this.spuForm.spuSaleAttrList = []
      this.spuSaleAttrListTemp.forEach(spuSaleAttr => {
        // 处理销售属性
        const arr = spuSaleAttr.saleAttr.split('|')
        spuSaleAttr.saleAttrId = arr[0]
        spuSaleAttr.saleAttrName = arr[1]
        const saleAttr = {
          saleAttrId: arr[0],
          saleAttrName: arr[1],
          spuSaleAttrValueList: []
        }

        // 处理销售属性值
        spuSaleAttr.spuSaleAttrValueList.forEach(spuSaleAttrValue => {
          const saleAttrValue = {
            saleAttrId: arr[0],
            saleAttrValueName: spuSaleAttrValue
          }
          saleAttr.spuSaleAttrValueList.push(saleAttrValue)
        })

        this.spuForm.spuSaleAttrList.push(saleAttr)
      })

      // console.log(this.spuForm)

      spu.saveSpuInfo(this.spuForm).then(response => {
        // 调用父组件监听函数
        this.$emit('listenOnSave')
      })
    },

    // 返回Spu列表页面
    backToSpuList() {
      this.$emit('listenOnClose')
    },

    // 文件上传限制条件
    beforeUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isPNG = file.type === 'image/png'
      const isGIF = file.type === 'image/gif'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG && !isPNG && !isGIF) {
        this.$message.error('上传头像图片只能是 JPG、PNG 或 GIF 格式!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
        return false
      }
      return true
    },

    // 上传图片成功的回调
    onUploadSuccess(res, file) {
      // 填充上传文件列表
      this.spuForm.spuImageList.push({
        imgName: file.name,
        imgUrl: file.response
      })
    },

    // 上传的文件预览
    onUploadPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogImageVisible = true
    },

    // 删除上传的文件
    onUploadRemove(file, fileList) {
      this.spuForm.spuImageList = []
      fileList.forEach(file => {
        this.spuForm.spuImageList.push({
          imgName: file.name,
          imgUrl: file.response
        })
      })
    },

    // 添加销售属性
    addSaleAttr() {
      const saleAttr = {
        saleAttr: null, // id|name 字符串
        saleAttrValue: null, // current input tag
        edit: false,
        spuSaleAttrValueList: [] // [tag1, tag2, tag3]
      }
      this.spuSaleAttrListTemp.push(saleAttr)
    },

    // 根据name删除销售属性
    deleteSaleAttr(saleAttr) {
      const tempList = []
      this.spuForm.spuSaleAttrList.forEach(item => {
        if (item.saleAttr !== saleAttr) {
          tempList.push(item)
        }
      })
      this.spuForm.spuSaleAttrList = tempList
    },

    // 添加销售属性值
    editAttrValue(row) {
      row.edit = true
    },

    // 保存销售属性值
    saveAttrValue(row) {
      if (!row.spuSaleAttrValueList) {
        row.spuSaleAttrValueList = []
      }

      const saleAttrValue = row.saleAttrValue
      if (saleAttrValue) {
        row.spuSaleAttrValueList.push(saleAttrValue)
      }

      row.saleAttrValue = ''
      row.edit = false
    },

    // 删除销售属性值
    handleTagClose(tag, row) {
      const index = row.spuSaleAttrValueList.indexOf(tag)
      row.spuSaleAttrValueList.splice(index, 1)
    }
  }
}
</script>

<style scoped>
.el-tag + .el-tag {
    margin-left: 10px;
  }
  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
</style>
