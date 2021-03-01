import {createLocalVue, mount} from '@vue/test-utils'
import Product from '@/components/Product'
import {when} from 'jest-when'
import axios from 'axios'
import {createStoreWithProducts} from './storeTestHelper'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'

jest.mock('axios')

const vueWithVuex = createLocalVue()
vueWithVuex.use(Vuex)

it('should display all attributes when mounted', () => {
  const product = newProduct()

  mountComponent(product)

  expect(slugWrapper.text()).toBe(product.slug)
  expect(titleWrapper.text()).toBe(product.title)
  expect(descriptionWrapper.text()).toBe(product.description)
  expect(currentPriceWrapper.text()).toBe(`${product.price.value} ${product.price.currency}`)
})

it('should not display price when not available', () => {
  const product = newProduct()
  product.price = null

  mountComponent(product)

  expect(currentPriceWrapper.text()).toBe('N/A')
})

it('should disable assign-price button when price is not entered initially', () => {
  mountComponent()

  expect(assignPriceWrapper.attributes().disabled).toBeTruthy()
})

it.each([null, '', 0, -5])('should disable assign-price button when invalid price is entered',
  async (invalidPrice) => {
    mountComponent()

    await priceToAssignWrapper.setValue(invalidPrice)

    expect(assignPriceWrapper.attributes().disabled).toBeTruthy()
  })

it('should disable assign-price button when price entered is same as the current price', async () => {
  const product = newProduct()
  mountComponent(product)

  await priceToAssignWrapper.setValue(product.price.value)

  expect(assignPriceWrapper.attributes().disabled).toBeTruthy()
})

it('should enable assign-price button when valid new price is entered', async () => {
  const currentPrice = 17
  const newPrice = 18
  const product = newProduct(currentPrice)
  mountComponent(product)

  await priceToAssignWrapper.setValue(newPrice)

  expect(assignPriceWrapper.attributes().disabled).toBeFalsy()
})

it('should enable assign-price button when valid price is entered and price was not available before', async () => {
  const product = newProduct()
  product.price = null
  mountComponent(product)

  await priceToAssignWrapper.setValue(17)

  expect(assignPriceWrapper.attributes().disabled).toBeFalsy()
})

it('should update current price when API call succeeds', async () => {
  const currentPrice = 11
  const newPrice = 17
  const product = newProduct(currentPrice)
  mountComponent(product)
  when(axios.put)
    .calledWith(`/products/${product.id}/price`, {
      value: newPrice,
      currency: 'EUR'
    })
    .mockResolvedValue({data: {}})

  await assignPrice(newPrice)

  expect(currentPriceWrapper.text()).toBe(`${newPrice} EUR`)
  expect(priceToAssignWrapper.element.value).toBeFalsy()
})

it('should not update current price when API call fails', async () => {
  const currentPrice = 11
  const newPrice = 17
  const product = newProduct(currentPrice)
  mountComponent(product)
  when(axios.put)
    .calledWith(`/products/${product.id}/price`, {
      value: newPrice,
      currency: 'EUR'
    })
    .mockRejectedValue({
      response: {
        status: 500,
        data: 'Boom'
      }
    })

  await assignPrice(newPrice)

  expect(currentPriceWrapper.text()).toBe(`${currentPrice} EUR`)
  expect(priceToAssignWrapper.element.value).toBe(`${newPrice}`)
})

let wrapper
let slugWrapper
let titleWrapper
let descriptionWrapper
let currentPriceWrapper
let assignPriceWrapper
let priceToAssignWrapper

function mountComponent(product = newProduct()) {
  const store = createStoreWithProducts([product])
  wrapper = mount(Product, {
    propsData: {
      product
    },
    store,
    localVue: vueWithVuex
  })
  slugWrapper = wrapper.find('[data-slug]')
  titleWrapper = wrapper.find('[data-title]')
  descriptionWrapper = wrapper.find('[data-description]')
  currentPriceWrapper = wrapper.find('[data-current-price]')
  assignPriceWrapper = wrapper.find('[data-assign-price]')
  priceToAssignWrapper = wrapper.find('[data-price-to-assign]')
}

function newProduct(price = 17) {
  return {
    id: 'product-1',
    slug: 'the-matrix-trilogy',
    title: 'The Matrix Trilogy',
    description: 'This is Matrix',
    price: {
      value: price,
      currency: 'EUR'
    }
  }
}

async function assignPrice(price) {
  await priceToAssignWrapper.setValue(price)
  await assignPriceWrapper.trigger('click')
  await flushPromises()
}
