export default ({auth, publicPaths = ['/login'], redirectTo = '/login'}) => {
  function buildRedirectURL(path) {
    return redirectTo + (path === '/' ? '' : `?targetUrl=${encodeURIComponent(path)}`)
  }

  function isPublicPath(path) {
    return publicPaths.includes(path)
  }

  function onLoadingComplete(cb) {
    auth.$watch('loading', loading => {
      if (!loading) {
        cb()
      }
    })
  }

  return (to, from, next) => {
    function check() {
      if (!auth.authenticated && !isPublicPath(to.path)) {
        next({
          path: buildRedirectURL(to.fullPath),
          replace: true
        })
      } else {
        next()
      }
    }

    if (!auth.loading) {
      check()
    } else {
      onLoadingComplete(check)
    }
  }
}
