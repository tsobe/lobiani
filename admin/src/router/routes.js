import InventoryItems from '@/views/InventoryItems'
import NewInventoryItem from '@/views/NewInventoryItem'
import Login from '@/views/Login'
import NewProduct from '@/views/NewProduct'
import Products from '@/views/Products'

const routes = [
  {
    path: '/',
    redirect: '/items'
  },
  {
    path: '/login',
    component: Login
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
    path: '/products',
    component: Products
  },
  {
    path: '/products/new',
    component: NewProduct
  },
  {
    path: '*',
    redirect: '/'
  }
]

export default routes
