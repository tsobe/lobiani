import {createLocalVue, mount} from '@vue/test-utils'
import axios from 'axios'
import flushPromises from 'flush-promises'
import NewInventoryItem from '@/views/NewInventoryItem'
import InventoryItem from '@/components/InventoryItem'
import InventoryItems from '@/views/InventoryItems'
import Vuex from 'vuex'
import inventoryItems from '@/store/inventoryItems'
import VueRouter from 'vue-router'
import App from '@/App'
import routes from '@/router/routes'
import Vuetify from 'vuetify'
import _ from 'lodash'

const vueWithVuex = createLocalVue()
vueWithVuex.use(Vuex)

jest.mock('axios')
jest.mock('lodash')

function setupImmediateDebounce() {
  _.debounce = jest.fn((fn) => fn)
}

function setupFailingPOSTCall() {
  axios.post.mockRejectedValue({
    response: {
      status: 500,
      data: 'Boom'
    }
  })
}

function setupSuccessfulPOSTCall(data = {}) {
  axios.post.mockResolvedValue({data})
}

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

function setupSuccessfulGETCallWithItems() {
  axios.get.mockResolvedValue({data: [...items]})
}

function mountWithStore(component) {
  const store = inventoryItems.createStore()
  const wrapper = mount(component, {
    localVue: vueWithVuex,
    vuetify: new Vuetify(),
    store: new Vuex.Store(store)
  })
  return {
    wrapper,
    store
  }
}

describe('NewInventoryItem', () => {
  beforeEach(() => {
    setupImmediateDebounce()
    setupSuccessfulFindItemsBySlugAPICall()
  })

  afterEach(() => {
    jest.resetAllMocks()
  })

  it('should disable save button initially', () => {
    mountComponent()

    expect(saveWrapper.attributes().disabled).toBeTruthy()
  })

  it('should not show validation message initially', () => {
    mountComponent()

    expect(validationMsgWrapper.isVisible()).toBe(false)
    expect(validationMsgWrapper.text()).toBeFalsy()
  })

  it('should enable save button when unique slug is given', async () => {
    mountComponent()

    await enterSlug()

    expect(saveWrapper.attributes().disabled).toBeFalsy()
  })

  it('should disable save button again when slug is reset', async () => {
    mountComponent()

    await enterSlug()
    await resetSlug()

    expect(saveWrapper.attributes().disabled).toBe('disabled')
  })

  it('should not allow saving when item with given slug is already defined', async () => {
    mountComponent()

    await enterSlug(alreadyDefinedItemSlug)

    expect(saveWrapper.attributes().disabled).toBe('disabled')
    expect(validationMsgWrapper.isVisible()).toBe(true)
    expect(validationMsgWrapper.text()).toBe('An item with this slug is already defined')
  })

  it('should not allow saving when validating slug for uniqueness fails', async () => {
    setupFailingFindItemsBySlugAPIResponse()
    mountComponent()

    await enterSlug(alreadyDefinedItemSlug)

    expect(saveWrapper.attributes().disabled).toBe('disabled')
    expect(validationMsgWrapper.isVisible()).toBe(true)
    expect(validationMsgWrapper.text()).toBe('Failed to check slug availability')
  })

  it('should allow saving when unique slug is given eventually', async () => {
    mountComponent()
    jest.useFakeTimers()

    await enterSlug(alreadyDefinedItemSlug)
    await enterSlug()

    jest.runOnlyPendingTimers()

    expect(saveWrapper.attributes().disabled).toBeFalsy()
    expect(validationMsgWrapper.isVisible()).toBe(false)
    expect(validationMsgWrapper.text()).toBeFalsy()
  })

  describe('item can be defined when API call succeeds', () => {
    beforeAll(async () => {
      setupImmediateDebounce()
      setupSuccessfulFindItemsBySlugAPICall()
      setupSuccessfulPOSTCall({
        id: itemId,
        slug: slug,
        stockLevel: 0
      })

      mountComponent()

      await defineItem()
    })

    it('should call the API', () => {
      expectAPIToHaveBeenCalled()
    })

    it('should emit the "itemDefined" event', () => {
      expect(wrapper.emitted().itemDefined).toBeTruthy()
      expect(wrapper.emitted().itemDefined).toHaveLength(1)
      expect(wrapper.emitted().itemDefined[0]).toEqual([{
        id: itemId,
        slug: slug,
        stockLevel: 0
      }])
    })

    it('should add item to store', () => {
      expect(store.state.items).toHaveLength(1)
      expect(store.state.items[0]).toEqual({
        id: itemId,
        slug: slug,
        stockLevel: 0
      })
    })

    it('should reset the slug input', () => {
      expect(slugWrapper.element.value).toBeFalsy()
    })
  })

  describe('item can not be defined when API call fails', () => {
    beforeAll(async () => {
      setupImmediateDebounce()
      setupSuccessfulFindItemsBySlugAPICall()
      setupFailingPOSTCall()

      mountComponent()

      await defineItem()
    })

    it('should call the API', () => {
      expectAPIToHaveBeenCalled()
    })

    it('should not emit the "itemDefined" event', () => {
      expect(wrapper.emitted().itemDefined).toBeFalsy()
    })

    it('should not add item to store', () => {
      expect(store.state.items).toHaveLength(0)
    })

    it('should not reset the slug input', () => {
      expect(slugWrapper.element.value).toBe(slug)
    })
  })

  let wrapper
  let slugWrapper
  let saveWrapper
  let validationMsgWrapper
  let store
  const itemId = 'foo'
  const slug = 'the-matrix-trilogy'
  const alreadyDefinedItemSlug = 'already-defined-item-slug'

  function mountComponent() {
    const mounted = mountWithStore(NewInventoryItem)
    wrapper = mounted.wrapper
    store = mounted.store
    slugWrapper = wrapper.find('[data-slug]')
    saveWrapper = wrapper.find('[data-save]')
    validationMsgWrapper = wrapper.find('[data-validation-message]')
  }

  async function defineItem() {
    await enterSlug()
    await saveWrapper.trigger('click')
    await flushPromises()
  }

  async function enterSlug(input = slug) {
    slugWrapper.setValue(input)
    await flushPromises()
  }

  async function resetSlug() {
    slugWrapper.setValue('')
    await flushPromises()
  }

  function expectAPIToHaveBeenCalled() {
    expect(axios.post).toHaveBeenCalledWith('/inventory-items', {slug: slug})
  }

  function setupSuccessfulFindItemsBySlugAPICall() {
    axios.get.mockImplementation(uri => {
      const itemsBySlug = uri === `/inventory-items?slug=${alreadyDefinedItemSlug}`
        ? [{
          id: 'foo',
          slug,
          stockLevel: 10
        }]
        : []
      return {data: itemsBySlug}
    })
  }

  function setupFailingFindItemsBySlugAPIResponse() {
    axios.get.mockRejectedValue({
      response: {
        status: 500
      }
    })
  }
})

describe('InventoryItem', () => {
  afterEach(jest.resetAllMocks)

  describe('mount', () => {
    beforeAll(mountComponent)

    it('should display slug', () => {
      expect(wrapper.find('[data-slug]').text()).toBe(slug)
    })

    it('should display initial stock level', () => {
      expect(stockLevelWrapper.text()).toBe(initialStockLevel.toString())
    })

    it('should disable add-to-stock button initially', () => {
      expect(addToStockWrapper.attributes().disabled).toBeTruthy()
    })
  })

  it('should enable add-to-stock button when valid amount is entered', async () => {
    mountComponent()

    await enterAmount(1)

    expect(addToStockWrapper.attributes().disabled).toBeFalsy()
  })

  it.each([-1, 0, 'foo'])('should disable add-to-stock button when invalid amount (%d) is entered',
    async (amount) => {
      mountComponent()

      await enterAmount(amount)

      expect(addToStockWrapper.attributes().disabled).toBeTruthy()
    })

  describe('items can be added to stock when API call succeeds', () => {
    beforeAll(async () => {
      mountComponent()

      setupSuccessfulPOSTCall()

      await addToStock(amount)
    })

    it('should call the API', () => {
      expectAPIToHaveBeenCalled()
    })

    it('should increase the item stock level', () => {
      expect(stockLevelWrapper.text()).toBe(amount.toString())
    })

    it('should reset the amount', () => {
      expect(amountWrapper.element.value).toBeFalsy()
    })
  })

  describe('items can not be added to stock when API call fails', () => {
    beforeAll(async () => {
      mountComponent()

      setupFailingPOSTCall()

      await addToStock(amount)
    })

    it('should call the API', () => {
      expectAPIToHaveBeenCalled()
    })

    it('should not change the item stock level', () => {
      expect(stockLevelWrapper.text()).toBe(initialStockLevel.toString())
    })

    it('should not reset the amount', () => {
      expect(amountWrapper.element.value).toBe(amount.toString())
    })
  })

  it('should not accept NaN as amount', async () => {
    mountComponent()

    await enterAmount('foo')

    expect(amountWrapper.element.value).toBeFalsy()
  })

  let wrapper
  let amountWrapper
  let stockLevelWrapper
  let addToStockWrapper
  const itemId = 'foo'
  const slug = 'the-matrix-trilogy'
  const initialStockLevel = 0
  const amount = 10

  function mountComponent() {
    const item = createItem()
    const store = new Vuex.Store(inventoryItems.createStore())
    store.commit('setItems', [item])

    wrapper = mount(InventoryItem, {
      propsData: {
        item: item
      },
      localVue: vueWithVuex,
      store
    })
    amountWrapper = wrapper.find('[data-amount]')
    stockLevelWrapper = wrapper.find('[data-stock-level]')
    addToStockWrapper = wrapper.find('[data-add-to-stock]')
  }

  function createItem() {
    return {
      id: itemId,
      slug: slug,
      stockLevel: initialStockLevel
    }
  }

  async function addToStock(amount) {
    await enterAmount(amount)
    await addToStockWrapper.trigger('click')
    await flushPromises()
  }

  async function enterAmount(amount) {
    amountWrapper.setValue(amount)
    await flushPromises()
  }

  function expectAPIToHaveBeenCalled() {
    expect(axios.post).toHaveBeenCalledWith(`/inventory-items/${itemId}/stock`, {amount: amount})
  }
})

describe('InventoryItems', () => {
  afterEach(jest.resetAllMocks)

  describe('mount', () => {
    beforeAll(async () => {
      setupSuccessfulGETCallWithItems()

      await mountComponent()
    })

    it('should call the API', () => {
      expect(axios.get).toHaveBeenCalledWith('/inventory-items')
    })

    it('should display all items', async () => {
      expect(wrapper.findAllComponents(InventoryItem)).toHaveLength(2)
    })
  })

  describe('delete', () => {
    describe('item can be deleted when API call succeeds', () => {
      beforeAll(async () => {
        setupSuccessfulGETCallWithItems()

        await mountComponent()

        await deleteItem(slugToDelete)
      })

      it('should call tha API', () => {
        expect(axios.delete).toHaveBeenCalledWith('/inventory-items/bar')
      })

      it('should remove the item from the list', () => {
        expect(findItemWrapper(slugToDelete).exists()).toBe(false)
      })
    })

    describe('item can not be deleted when API call fails', () => {
      beforeAll(async () => {
        setupSuccessfulGETCallWithItems()
        axios.delete.mockRejectedValue({
          response: {
            status: 500
          }
        })

        await mountComponent()

        await deleteItem(slugToDelete)
      })

      it('should call tha API', () => {
        expect(axios.delete).toHaveBeenCalledWith('/inventory-items/bar')
      })

      it('should not remove the item from the list', () => {
        expect(findItemWrapper(slugToDelete).exists()).toBe(true)
      })
    })

    const slugToDelete = 'memento'

    async function deleteItem(slug) {
      await findItemWrapper(slug).find('[data-delete]').trigger('click')
      await flushPromises()
    }

    function findItemWrapper(slug) {
      return wrapper.find('[data-item="' + slug + '"]')
    }
  })

  it('should not fetch items from the API when mounted and store already has data', async () => {
    const store = createStoreWithItems()

    mount(InventoryItems, {
      localVue: vueWithVuex,
      store
    })
    await flushPromises()

    expect(axios.get).not.toHaveBeenCalledWith('/inventory-items')
  })

  let wrapper

  async function mountComponent() {
    wrapper = mountWithStore(InventoryItems).wrapper
    await flushPromises()
  }

  function createStoreWithItems() {
    const store = new Vuex.Store(inventoryItems.createStore())
    store.commit('setItems', items)
    return store
  }
})

describe('App navigation', () => {
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

  async function mountComponent() {
    const localVue = createLocalVue()
    localVue.use(Vuex)
    localVue.use(VueRouter)

    wrapper = mount(App, {
      localVue: localVue,
      router: new VueRouter({routes}),
      store: new Vuex.Store(inventoryItems.createStore()),
      vuetify: new Vuetify()
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
})
