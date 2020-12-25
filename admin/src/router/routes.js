import InventoryItems from '@/views/InventoryItems'
import NewInventoryItem from '@/views/NewInventoryItem'
import Login from '@/views/Login'

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
    path: '*',
    redirect: '/'
  }
]

export default routes
