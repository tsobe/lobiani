export default {
  handle: (err, vm) => {
    console.error(err)
    let errorMessage
    if (err.response) {
      const responseData = err.response.data
      errorMessage = responseData.message || responseData
    } else if (err.request) {
      errorMessage = 'error connecting backend'
    } else {
      errorMessage = err.message
    }
    vm.$notifier.showMessage({
      text: errorMessage,
      type: 'error'
    })
  }
}
