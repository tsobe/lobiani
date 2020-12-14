import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import vuetify from './plugins/vuetify'
import Notifier from './plugins/notifier'

axios.defaults.baseURL = '/api'

Vue.use(Notifier, {store})
Vue.config.productionTip = false
Vue.config.errorHandler = (err, vm) => {
  const responseData = err.response.data
  vm.$notifier.showMessage({
    text: responseData.message || responseData,
    type: 'error'
  })
}

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
