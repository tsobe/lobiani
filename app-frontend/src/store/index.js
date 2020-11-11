import Vue from 'vue'
import Vuex from 'vuex'
import {createStore} from './inventoryItems'

Vue.use(Vuex)

const store = new Vuex.Store(createStore())

export default store
