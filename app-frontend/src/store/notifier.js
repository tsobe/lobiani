export default function() {
  return {
    namespaced: true,
    state: {
      type: '',
      text: ''
    },
    mutations: {
      setMessage(state, {text, type}) {
        state.text = text
        state.type = type
      }
    }
  }
}
