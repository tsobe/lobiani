import axios from 'axios'

export default {
  createStore() {
    return {
      state: {
        items: []
      },
      mutations: {
        addItem(state, item) {
          state.items.push(item)
        }
      },
      actions: {
        async defineItem(context, slug) {
          const response = await axios.post('/inventory-items', {slug})
          const item = {
            id: response.data.id,
            slug
          }
          context.commit('addItem', item)
          return item
        }
      }
    }
  }
}
