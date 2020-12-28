import {createLocalVue, mount} from '@vue/test-utils'
import Vuex from 'vuex'
import VueRouter from 'vue-router'
import App from '@/App'
import routes from '@/router/routes'
import inventoryItems from '@/store/inventoryItems'
import Vuetify from 'vuetify'
import flushPromises from 'flush-promises'
import NewInventoryItem from '@/views/NewInventoryItem'
import axios from 'axios'

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

it('should navigate to list of items after new item is defined', async () => {
  await mountComponent()
  await wrapper.vm.$router.push('/new')

  await defineItem()

  expect(wrapper.vm.$route.path).toBe('/items')
})

it('should navigate to list of items when cancel is clicked', async () => {
  await mountComponent()
  await wrapper.vm.$router.push('/new')

  await wrapper.find('[data-cancel]').trigger('click')

  expect(wrapper.vm.$route.path).toBe('/items')
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
    store: new Vuex.Store(inventoryItems.createStore()),
    vuetify: new Vuetify(),
    mocks: {
      $auth: authMock
    }
  })
  await flushPromises()
}

async function defineItem() {
  const newItemWrapper = wrapper.findComponent(NewInventoryItem)
  newItemWrapper.vm.$emit('itemDefined', {
    id: 'baz',
    slug: 'the-simpsons',
    stockLevel: 0
  })
  await flushPromises()
}

function setupSuccessfulGETCallWithItems() {
  axios.get.mockResolvedValue({data: [...items]})
}
