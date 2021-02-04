import axios from 'axios'

export function createStore() {
  return {
    namespaced: true,
    state: {
      products: []
    },
    getters: {
      hasProducts: state => {
        return state.products.length > 0
      }
    },
    mutations: {
      set(state, products) {
        state.products = products
      },
      delete(state, id) {
        state.products = state.products.filter(p => p.id !== id)
      },
      add(state, product) {
        state.products.push(product)
      }
    },
    actions: {
      async fetch(context) {
        const response = await axios.get('/products')
        context.commit('set', response.data)
      },
      async define(context, product) {
        const response = await axios.post('/products', product)
        context.commit('add', response.data)
      },
      async delete(context, id) {
        await axios.delete(`/products/${id}`)
        context.commit('delete', id)
      }
    }
  }
}
