import InventoryItems from '@/views/InventoryItems'
import NewInventoryItem from '@/views/NewInventoryItem'

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

export default routes
