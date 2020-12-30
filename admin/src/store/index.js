import Vue from 'vue'
import Vuex from 'vuex'
import inventoryItems from './inventoryItems'
import {createStore as createNotifierStore} from '@/notifier'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    notifier: createNotifierStore(),
    inventory: inventoryItems.createStore()
  }
})

export default store
