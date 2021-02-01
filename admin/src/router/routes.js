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
    redirect: '/inventory'
  },
  {
    path: '/inventory',
    component: InventoryItems
  },
  {
    path: '/new',
    redirect: '/inventory/new'
  },
  {
    path: '/inventory/new',
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
