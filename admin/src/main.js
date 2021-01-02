import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import {Notifier} from './notifier'
import {Auth} from './auth'
import Http from './plugins/http'
import {clientId, domain} from '../auth_config.json'
import globalErrorHandler from './utils/globalErrorHandler'

Vue.use(Notifier, {store})
Vue.use(Auth, {
  router,
  clientId: clientId,
  domain: domain,
  redirectUri: window.location.origin,
  onRedirectCallback: () => {
    router.push('/')
  }
})
Vue.use(Http)
Vue.config.productionTip = false
Vue.config.errorHandler = globalErrorHandler.handle

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
