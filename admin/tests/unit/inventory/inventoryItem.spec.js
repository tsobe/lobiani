import Vuex from 'vuex'
import inventoryItems from '@/store/inventoryItems'
import {createLocalVue, mount} from '@vue/test-utils'
import InventoryItem from '@/components/InventoryItem'
import flushPromises from 'flush-promises'
import axios from 'axios'
import {when} from 'jest-when'

jest.mock('axios')

const vueWithVuex = createLocalVue()
vueWithVuex.use(Vuex)

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
    setupSuccessfulAPICall()

    await addToStock(validAmount)
  })

  it('should increase the item stock level', () => {
    expect(stockLevelWrapper.text()).toBe(validAmount.toString())
  })

  it('should reset the amount', () => {
    expect(amountWrapper.element.value).toBeFalsy()
  })

  function setupSuccessfulAPICall() {
    when(axios.post)
      .calledWith(`/inventory-items/${itemId}/stock`, {amount: validAmount})
      .mockResolvedValue({data: {}})
  }
})

describe('items can not be added to stock when API call fails', () => {
  beforeAll(async () => {
    mountComponent()
    setupFailedAPICall()

    await addToStock(validAmount)
  })

  it('should not change the item stock level', () => {
    expect(stockLevelWrapper.text()).toBe(initialStockLevel.toString())
  })

  it('should not reset the amount', () => {
    expect(amountWrapper.element.value).toBe(validAmount.toString())
  })

  function setupFailedAPICall() {
    when(axios.post)
      .calledWith(`/inventory-items/${itemId}/stock`, {amount: validAmount})
      .mockRejectedValue({
        response: {
          status: 500,
          data: 'Boom'
        }
      })
  }
})

let wrapper
let amountWrapper
let stockLevelWrapper
let addToStockWrapper
const itemId = 'foo'
const slug = 'the-matrix-trilogy'
const initialStockLevel = 0
const validAmount = 10

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
