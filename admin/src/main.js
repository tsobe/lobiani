import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import vuetify from './plugins/vuetify'
import Notifier from './plugins/notifier'
import Auth from './plugins/auth'
import {clientId, domain} from '../auth_config.json'

axios.defaults.baseURL = '/api'

Vue.use(Notifier, {store})
Vue.use(Auth, {
  store,
  options: {
    clientId: clientId,
    domain: domain,
    redirectUri: window.location.origin,
    onRedirectCallback: () => {
      router.push('/')
    }
  }
})
Vue.config.productionTip = false
Vue.config.errorHandler = (err, vm) => {
  console.error(err)
  const response = err.response || {}
  const responseData = response.data || {}
  const errorMessage = responseData.message || responseData || err
  vm.$notifier.showMessage({
    text: errorMessage,
    type: 'error'
  })
}

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
