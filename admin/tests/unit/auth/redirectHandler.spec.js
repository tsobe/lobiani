import {createRedirectHandler} from '@/auth'
import VueRouter from 'vue-router'
import {createLocalVue} from '@vue/test-utils'

beforeEach(createRouterAndHandler)

it('should redirect to next when appState contains it', () => {
  redirectHandler.handle({
    next: '/protected'
  })

  expect(router.currentRoute.path).toBe('/protected')
})

it.each([undefined, {}])('should redirect to root path when appState is %s', appState => {
  redirectHandler.handle(appState)

  expect(router.currentRoute.path).toBe('/')
})

let router
let redirectHandler

function createRouterAndHandler() {
  createRouter()
  redirectHandler = createRedirectHandler(router)
}

function createRouter() {
  const localVue = createLocalVue()
  localVue.use(VueRouter)
  router = new VueRouter({
    routes: [
      {
        path: '/protected'
      }
    ]
  })
}
