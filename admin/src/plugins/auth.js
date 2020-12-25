import createAuth0Client from '@auth0/auth0-spa-js'
import createAuthGuard from '@/plugins/authGuard'

function isRedirectCallback() {
  return window.location.search.includes('code=') &&
    window.location.search.includes('state=')
}

export default {
  install(Vue, options) {
    const auth = new Vue({
      data() {
        return {
          authenticated: false,
          auth0Client: null,
          loading: true
        }
      },
      methods: {
        async login(opts) {
          await this.auth0Client.loginWithRedirect(opts)
        },
        async logout(opts) {
          await this.auth0Client.logout(opts)
        }
      },
      async created() {
        this.auth0Client = await createAuth0Client({
          client_id: options.clientId,
          domain: options.domain,
          redirect_uri: options.redirectUri
        })
        try {
          if (isRedirectCallback()) {
            const {appState} = await this.auth0Client.handleRedirectCallback()
            options.onRedirectCallback(appState)
          }
        } finally {
          this.authenticated = await this.auth0Client.isAuthenticated()
        }
        this.loading = false
      }
    })

    Vue.prototype.$auth = auth
    options.router.beforeResolve(createAuthGuard({auth}))
  }
}
