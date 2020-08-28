import Vue from 'vue'
import VueRouter from 'vue-router'
import InventoryItems from '@/views/InventoryItems'
import DefineInventoryItem from '@/views/DefineInventoryItem'

Vue.use(VueRouter)

const routes = [
  {
    path: '/items',
    name: 'InventoryItems',
    component: InventoryItems
  },
  {
    path: '/new',
    name: 'DefineInventoryItem',
    component: DefineInventoryItem
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
