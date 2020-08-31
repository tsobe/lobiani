import Vue from 'vue'
import VueRouter from 'vue-router'
import InventoryItems from '@/views/InventoryItems'
import NewInventoryItem from '@/views/NewInventoryItem'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/items'
  },
  {
    path: '/items',
    name: 'InventoryItems',
    component: InventoryItems
  },
  {
    path: '/new',
    name: 'NewInventoryItem',
    component: NewInventoryItem
  },
  {
    path: '*',
    redirect: '/'
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
