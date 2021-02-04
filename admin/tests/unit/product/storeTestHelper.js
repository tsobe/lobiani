import Vuex from 'vuex'
import {createStore as createProductStore} from '@/store/products'

export function createStore() {
  return new Vuex.Store({
    modules: {
      product: createProductStore()
    }
  })
}

export function createStoreWithProducts(products) {
  const store = createStore()
  store.commit('product/set', products)
  return store
}
