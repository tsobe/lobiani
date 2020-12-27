export default ({auth, publicPaths = ['/login'], redirectTo = '/login'}) => {
  return (to, from, next) => {
    function check() {
      if (!auth.authenticated && !publicPaths.includes(to.path)) {
        next({
          path: redirectTo,
          replace: true
        })
      } else {
        next()
      }
    }

    function onLoadingComplete(cb) {
      auth.$watch('loading', loading => {
        if (!loading) {
          cb()
        }
      })
    }

    if (!auth.loading) {
      check()
    } else {
      onLoadingComplete(check)
    }
  }
}
