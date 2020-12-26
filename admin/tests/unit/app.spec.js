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

describe('App', () => {
  afterEach(jest.resetAllMocks)

  it('should navigate to list of items after new item is defined', async () => {
    setupSuccessfulGETCallWithItems()
    await mountComponent()
    await wrapper.vm.$router.push('/new')

    await defineItem()

    expect(wrapper.vm.$route.path).toBe('/items')
  })

  it('should navigate to list of items when cancel is clicked', async () => {
    setupSuccessfulGETCallWithItems()
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
        $auth: {}
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
})
