import Vue from 'vue'
import Vuex from 'vuex'
import inventoryItems from './inventoryItems'

Vue.use(Vuex)

const store = new Vuex.Store(inventoryItems.createStore())

export default store
