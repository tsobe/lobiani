import Login from '@/views/Login'
import {createLocalVue, mount} from '@vue/test-utils'
import VueRouter from 'vue-router'

beforeEach(mountComponent)
afterEach(jest.resetAllMocks)

it('should delegate to $auth with targetUrl in appState when logged in with targetUrl', async () => {
  await router.push('/login?targetUrl=%2Fprotected%3Fqp1%3Done%26qp2%3Dtwo')
  await login()

  expect(authMock.login).toHaveBeenCalledWith({
    appState: {
      targetUrl: '/protected?qp1=one&qp2=two'
    }
  })
})

it('should delegate to $auth with empty options when logged in without targetUrl', async () => {
  await router.push('/login')
  await login()

  expect(authMock.login).toHaveBeenCalledWith({})
})

let router
let wrapper
const authMock = {
  login: jest.fn()
}

function mountComponent() {
  const localVue = createLocalVue()
  localVue.use(VueRouter)
  router = new VueRouter({
    routes: [
      {
        path: '/login',
        component: Login
      }
    ]
  })
  wrapper = mount(Login, {
    localVue,
    router,
    mocks: {
      $auth: authMock
    }
  })
}

async function login() {
  await wrapper.find('[data-login]').trigger('click')
}
