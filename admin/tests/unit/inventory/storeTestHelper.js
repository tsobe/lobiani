import Vuex from 'vuex'
import inventoryItems from '@/store/inventoryItems'

export function createStore() {
  return new Vuex.Store({
    modules: {
      inventory: inventoryItems.createStore()
    }
  })
}

export function createStoreWithItems(items) {
  const store = createStore()
  store.commit('inventory/setItems', items)
  return store
}
