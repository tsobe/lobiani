import Vue from 'vue'
import Vuex from 'vuex'
import inventoryItems from './inventoryItems'
import {createStore as createNotifierStore} from '@/notifier'
import {createStore as createProductStore} from './products'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    notifier: createNotifierStore(),
    inventory: inventoryItems.createStore(),
    product: createProductStore()
  }
})

export default store
