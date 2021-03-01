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
      },
      setPrice(state, payload) {
        state.products.find(p => p.id === payload.id).price = payload.price
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
      async assignPrice(context, payload) {
        await axios.put(`/products/${payload.id}/price`, payload.price)
        context.commit('setPrice', payload)
      },
      async delete(context, id) {
        await axios.delete(`/products/${id}`)
        context.commit('delete', id)
      }
    }
  }
}
