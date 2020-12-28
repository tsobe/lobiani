export default {
  install(Vue, {store}) {
    Vue.prototype.$notifier = {
      showMessage(message) {
        if (typeof message === 'string') {
          message = {text: message, type: 'info'}
        }
        store.commit('notifier/setMessage', message)
      }
    }
  }
}
