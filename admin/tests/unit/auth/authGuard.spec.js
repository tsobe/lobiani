import VueRouter from 'vue-router'
import {createLocalVue} from '@vue/test-utils'
import {createAuthGuard} from '@/auth'
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

beforeEach(createRouter)

it('should redirect to login route when not authenticated', async () => {
  auth.authenticated = false

  await expect(router.push('/')).rejects.toBeRedirectFailure()

  expect(router.currentRoute.fullPath).toBe('/login')
})

it('should redirect to login route and include encoded original URL as next when not authenticated', async () => {
  auth.authenticated = false

  await expect(router.push('/protected?qp1=one&qp2=two')).rejects.toBeRedirectFailure()

  expect(router.currentRoute.fullPath).toBe('/login?next=%2Fprotected%3Fqp1%3Done%26qp2%3Dtwo')
})

it('should allow access on public route when not authenticated', async () => {
  auth.authenticated = false

  router.push('/public')

  expect(router.currentRoute.path).toBe('/public')
})

it('should allow access on protected route when authenticated', async () => {
  auth.authenticated = true

  router.push('/protected')

  expect(router.currentRoute.path).toBe('/protected')
})

it('should allow access on protected route after auth loading completes with authenticated status', async () => {
  auth.authenticated = false
  auth.loading = true

  router.push('/protected')
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
