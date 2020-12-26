import VueRouter from 'vue-router'
import {createLocalVue} from '@vue/test-utils'
import createAuthGuard from '@/plugins/authGuard'
import flushPromises from 'flush-promises'

const {isNavigationFailure, NavigationFailureType} = VueRouter

expect.extend({
  toBeRedirectFailure(failure) {
    return {
      pass: isNavigationFailure(failure, NavigationFailureType.redirected),
      message: () => `expected redirection, but got ${failure}`
    }
  }
})

describe('authGuard', () => {
  beforeEach(createRouter)

  it('should redirect to login route when not authenticated', async () => {
    auth.authenticated = false

    await expect(router.push('/protected')).rejects.toBeRedirectFailure()

    expect(router.currentRoute.path).toBe('/login')
  })

  it('should allow access on public route when not authenticated', async () => {
    auth.authenticated = false

    await expect(router.push('/public'))

    expect(router.currentRoute.path).toBe('/public')
  })

  it('should allow access on protected route when authenticated', async () => {
    auth.authenticated = true

    await expect(router.push('/protected'))

    expect(router.currentRoute.path).toBe('/protected')
  })

  it('should allow access on protected route after auth loading completes with authenticated status', async () => {
    auth.authenticated = false
    auth.loading = true

    expect(router.push('/protected'))
    completeAuthLoading(true)
    await flushPromises()

    expect(router.currentRoute.path).toBe('/protected')
  })

  it('should not allow access on protected route after auth loading completes with unauthenticated status', async () => {
    auth.authenticated = false
    auth.loading = true

    const routePromise = router.push('/protected')
    completeAuthLoading(false)

    await expect(routePromise).rejects.toBeRedirectFailure()
    expect(router.currentRoute.path).toBe('/login')
  })

  let router
  const auth = new (createLocalVue())({
    data() {
      return {
        authenticated: false,
        loading: false
      }
    }
  })

  function createRouter() {
    const localVue = createLocalVue()
    localVue.use(VueRouter)
    router = new VueRouter({
      routes: [
        {
          path: '/protected'
        },
        {
          path: '/login'
        },
        {
          path: '/public'
        }
      ]
    })
    router.beforeResolve(createAuthGuard({
      auth,
      publicPaths: ['/login', '/public']
    }))
  }

  function completeAuthLoading(authenticated) {
    auth.authenticated = authenticated
    auth.loading = false
  }
})
