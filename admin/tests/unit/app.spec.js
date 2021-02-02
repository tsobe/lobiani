import {createLocalVue, mount} from '@vue/test-utils'
import Vuex from 'vuex'
import VueRouter from 'vue-router'
import App from '@/App'
import routes from '@/router/routes'
import Vuetify from 'vuetify'
import flushPromises from 'flush-promises'
import axios from 'axios'
import {createStore} from './inventory/storeTestHelper'

jest.mock('axios')

beforeEach(setupSuccessfulGETCallWithItems)
afterEach(jest.resetAllMocks)

it('should render navigation drawer and app bar when authenticated', async () => {
  authMock.authenticated = true

  await mountComponent()

  expect(wrapper.find('nav').exists()).toBe(true)
  expect(wrapper.find('header').exists()).toBe(true)
})

it('should not render navigation drawer and app bar when not authenticated', async () => {
  authMock.authenticated = false

  await mountComponent()

  expect(wrapper.find('nav').exists()).toBe(false)
  expect(wrapper.find('header').exists()).toBe(false)
})

let wrapper
const items = [
  {
    id: 'foo',
    slug: 'the-matrix-trilogy',
    stockLevel: 10
  },
  {
    id: 'bar',
    slug: 'memento',
    stockLevel: 17
  }
]
const authMock = {
  authenticated: true,
  user: {}
}

async function mountComponent() {
  const localVue = createLocalVue()
  localVue.use(Vuex)
  localVue.use(VueRouter)
  wrapper = mount(App, {
    localVue: localVue,
    router: new VueRouter({routes}),
    store: createStore(),
    vuetify: new Vuetify(),
    mocks: {
      $auth: authMock
    }
  })
  await flushPromises()
}

function setupSuccessfulGETCallWithItems() {
  axios.get.mockResolvedValue({data: [...items]})
}
