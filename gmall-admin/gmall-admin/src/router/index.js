import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/
export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },

  // 首页
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: 'Dashboard',
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '谷粒商城后台首页', icon: 'dashboard' }
    }]
  },

  // 基本信息管理
  {
    path: '/baseinfo',
    component: Layout,
    redirect: '/baseinfo/prop/list',
    name: 'BasesInfo',
    meta: { title: '基本信息管理', icon: 'table' },
    alwaysShow: true,
    children: [
      {
        path: 'prop/list',
        name: 'BasesInfoPropList',
        component: () => import('@/views/baseinfo/prop/list'),
        meta: { title: '平台属性列表' }
      }
    ]
  },

  // 商品信息管理
  {
    path: '/product',
    component: Layout,
    redirect: '/product/spu/list',
    name: 'Product',
    meta: { title: '商品信息管理', icon: 'shopping' },
    alwaysShow: true,
    children: [
      {
        path: 'spu/list',
        name: 'ProductSpuList',
        component: () => import('@/views/product/spu/list'),
        meta: { title: '商品属性SPU管理' }
      }
    ]
  },

  { path: '*', redirect: '/404', hidden: true }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})
