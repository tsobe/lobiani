import Login from '@/views/Login'
import {createLocalVue, mount} from '@vue/test-utils'
import VueRouter from 'vue-router'

beforeEach(mountComponent)
afterEach(jest.resetAllMocks)

it('should delegate to $auth with next in appState when logged in with next', async () => {
  await router.push('/login?next=%2Fprotected%3Fqp1%3Done%26qp2%3Dtwo')
  await login()

  expect(authMock.login).toHaveBeenCalledWith({
    appState: {
      next: '/protected?qp1=one&qp2=two'
    }
  })
})

it('should delegate to $auth with empty options when logged in without next', async () => {
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
