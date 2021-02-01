import axios from 'axios'

export function createStore() {
  return {
    namespaced: true,
    state: {
      products: []
    },
    mutations: {
      setProducts(state, products) {
        state.products = products
      }
    },
    actions: {
      async fetchProducts(context) {
        const response = await axios.get('/products')
        context.commit('setProducts', response.data)
      }
    }
  }
}
