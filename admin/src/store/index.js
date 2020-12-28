import Vue from 'vue'
import Vuex from 'vuex'
import inventoryItems from './inventoryItems'
import createNotifier from '@/notifier/store'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    notifier: createNotifier()
  },
  ...inventoryItems.createStore()
})

export default store
