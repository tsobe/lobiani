import axios from 'axios'
import InventoryItem from '@/components/InventoryItem'
import flushPromises from 'flush-promises'
import {createLocalVue, mount} from '@vue/test-utils'
import InventoryItems from '@/views/InventoryItems'
import Vuex from 'vuex'
import inventoryItems from '@/store/inventoryItems'
import Vuetify from 'vuetify'

jest.mock('axios')

const vueWithVuex = createLocalVue()
vueWithVuex.use(Vuex)

describe('InventoryItems', () => {
  beforeEach(setupSuccessfulGETCallWithItems)
  afterEach(jest.resetAllMocks)

  it('should display all items when mounted', async () => {
    await mountComponent()

    expect(wrapper.findAllComponents(InventoryItem)).toHaveLength(2)
  })

  it('should disappear the item when delete API call succeeds', async () => {
    await mountComponent()

    await deleteItem(slugToDelete)

    expect(findItemWrapper(slugToDelete).exists()).toBe(false)
    expect(axios.delete).toHaveBeenCalledWith('/inventory-items/bar')
  })

  it('should not disappear the item when delete API call succeeds', async () => {
    axios.delete.mockRejectedValue({
      response: {
        status: 500
      }
    })
    await mountComponent()

    await deleteItem(slugToDelete)

    expect(findItemWrapper(slugToDelete).exists()).toBe(true)
    expect(axios.delete).toHaveBeenCalledWith('/inventory-items/bar')
  })

  it('should not fetch items from the API when mounted and store already has data', async () => {
    const store = createStoreWithItems()

    await mountComponent(store)

    expect(axios.get).not.toHaveBeenCalledWith('/inventory-items')
  })

  let wrapper
  const slugToDelete = 'memento'
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

  async function mountComponent(store = new Vuex.Store(inventoryItems.createStore())) {
    wrapper = mount(InventoryItems, {
      localVue: vueWithVuex,
      vuetify: new Vuetify(),
      store
    })
    await flushPromises()
  }

  function createStoreWithItems() {
    const store = new Vuex.Store(inventoryItems.createStore())
    store.commit('setItems', items)
    return store
  }

  function setupSuccessfulGETCallWithItems() {
    axios.get.mockResolvedValue({data: [...items]})
  }

  async function deleteItem(slug) {
    await findItemWrapper(slug).find('[data-delete]').trigger('click')
  }

  function findItemWrapper(slug) {
    return wrapper.find('[data-item="' + slug + '"]')
  }
})
