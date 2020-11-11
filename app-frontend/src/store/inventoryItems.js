import axios from 'axios'

export default {
  createStore() {
    return {
      state: {
        items: []
      },
      mutations: {
        setItems(state, items) {
          state.items = items
        },
        addItem(state, item) {
          state.items.push(item)
        },
        increaseStockLevel(state, payload) {
          const targetItem = state.items.find(i => i.id === payload.item.id)
          targetItem.stockLevel += payload.amount
        },
        deleteItem(state, item) {
          state.items = state.items.filter(i => i.id !== item.id)
        }
      },
      actions: {
        async fetchItems(context) {
          const response = await axios.get('/inventory-items')
          context.commit('setItems', response.data)
        },
        async defineItem(context, slug) {
          const response = await axios.post('/inventory-items', {slug})
          const item = response.data
          context.commit('addItem', item)
          return item
        },
        async addToStock(context, payload) {
          await axios.post(`/inventory-items/${payload.item.id}/stock`, {amount: payload.amount})
          context.commit('increaseStockLevel', payload)
        },
        async deleteItem(context, item) {
          await axios.delete(`/inventory-items/${item.id}`)
          context.commit('deleteItem', item)
        }
      }
    }
  }
}
