import axios from 'axios'
import {getInstance} from '@/auth'

export default {
  install(Vue) {
    axios.interceptors.request.use(config => {
      const auth = getInstance()
      if (auth) {
        config.headers.Authorization = `Bearer ${auth.idToken}`
      }
      return config
    })
    axios.defaults.baseURL = '/api'
    Vue.prototype.$http = axios
  }
}
