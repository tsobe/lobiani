import {createLocalVue, mount} from '@vue/test-utils'
import Products from '@/views/Products'
import Product from '@/components/Product'
import {when} from 'jest-when'
import axios from 'axios'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import Vuetify from 'vuetify'
import {createStore as createProductStore} from '@/store/products'

const vueWithVuex = createLocalVue()
vueWithVuex.use(Vuex)

jest.mock('axios')

afterEach(jest.resetAllMocks)

it('should display all products when mounted', async () => {
  when(axios.get).calledWith('/products').mockResolvedValue({
    data: [
      newProduct('prod-1', 'the-matrix-trilogy'),
      newProduct('prod-2', 'memento')
    ]
  })
  await mountComponent()

  expect(wrapper.findAllComponents(Product)).toHaveLength(2)
})

it('should delete product when API call succeeds', async () => {
  const id = 'prod-1'
  const slug = 'the-matrix-trilogy'
  when(axios.get).calledWith('/products').mockResolvedValue({
    data: [
      newProduct(id, slug),
      newProduct('prod-2', 'memento')
    ]
  })
  when(axios.delete).calledWith(`/products/${id}`).mockResolvedValue({})
  await mountComponent()

  await deleteProduct(slug)

  expect(findProductWrapper(slug).exists()).toBe(false)
})

it('should not delete product when API call fails', async () => {
  const id = 'prod-1'
  const slug = 'the-matrix-trilogy'
  when(axios.get).calledWith('/products').mockResolvedValue({
    data: [
      newProduct(id, slug),
      newProduct('prod-2', 'memento')
    ]
  })
  when(axios.delete).calledWith(`/products/${id}`).mockRejectedValue({
    response: {
      status: 500
    }
  })
  await mountComponent()

  await deleteProduct('the-matrix-trilogy')

  expect(findProductWrapper('the-matrix-trilogy').exists()).toBe(true)
})

it('should not fetch products from the API when mounted and store already has data', async () => {
  const store = createStore()
  store.commit('product/set', [newProduct('prod-1', 'the-matrix-trilogy')])

  await mountComponent(store)

  expect(axios.get).not.toHaveBeenCalledWith('/products')
})

let wrapper

async function mountComponent(store = createStore()) {
  wrapper = mount(Products, {
    localVue: vueWithVuex,
    vuetify: new Vuetify(),
    store
  })
  await flushPromises()
  return wrapper
}

function newProduct(id, slug) {
  return {
    id: id,
    slug: slug,
    title: `${id}-title`,
    description: `${id}-description`
  }
}

function createStore() {
  return new Vuex.Store({
    modules: {
      product: createProductStore()
    }
  })
}

async function deleteProduct(slug) {
  await findProductWrapper(slug).find('[data-delete]').trigger('click')
}

function findProductWrapper(slug) {
  return wrapper.find(`[data-product="${slug}"]`)
}
