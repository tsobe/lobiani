import {createLocalVue, mount} from '@vue/test-utils'
import NewProduct from '@/views/NewProduct'
import flushPromises from 'flush-promises'
import Vuetify from 'vuetify'
import {when} from 'jest-when'
import axios from 'axios'
import Slug from '@/components/Slug'
import Vuex from 'vuex'
import {createStore} from './storeTestHelper'

const vueWithVuex = createLocalVue()
vueWithVuex.use(Vuex)

jest.mock('axios')

beforeEach(mountComponent)

it('should disable save button initially', () => {
  expect(saveWrapper.attributes().disabled).toBeTruthy()
})

it('should enable save button when valid info is provided', async () => {
  slugWrapper.setValue('the-matrix-trilogy')
  titleWrapper.setValue('The Matrix Trilogy')
  descriptionWrapper.setValue('This is Matrix')

  await flushPromises()

  expect(saveWrapper.attributes().disabled).toBeFalsy()
})

it.each([' ',
  'Upperacse',
  '#meh',
  'space cowboy',
  'blah?'
])('should not allow saving when slug (%s) has invalid format',
  async (slug) => {
    slugWrapper.setValue(slug)
    titleWrapper.setValue('The Matrix Trilogy')
    descriptionWrapper.setValue('This is Matrix')

    await flushPromises()

    expect(saveWrapper.attributes().disabled).toBeTruthy()
  })

it.each([
  ['slug', '', ''],
  ['slug', ' ', ' '],
  [' ', ' ', ' '],
  ['slug', '', 'Description'],
  ['slug', 'Title', ''],
  ['Uppercase', 'Title', 'Description']])('should disable save button when invalid info (%s, %s, %s) is provided',
  async (slug, title, description) => {
    slugWrapper.setValue(slug)
    titleWrapper.setValue(title)
    descriptionWrapper.setValue(description)

    await flushPromises()

    expect(saveWrapper.attributes().disabled).toBeTruthy()
  })

it('should set resource property on slug', () => {
  const slugComponentWrapper = wrapper.findComponent(Slug)

  expect(slugComponentWrapper.vm.$props.resource).toBe('products')
})

it('should navigate to list of products when product is successfully defined', async () => {
  const product = newProduct()
  when(axios.post).calledWith('/products', product).mockResolvedValue({
    data: {
      id: 'prod-1',
      ...newProduct()
    }
  })

  await defineProduct(product)

  expect(router.push).toHaveBeenCalledWith('/products')
})

it('should add product to store when product is successfully defined', async () => {
  const product = newProduct()
  const definedProduct = {
    id: 'prod-1',
    ...newProduct()
  }
  when(axios.post).calledWith('/products', product).mockResolvedValue({
    data: definedProduct
  })

  await defineProduct(product)

  expect(store.state.product.products).toHaveLength(1)
  expect(store.state.product.products[0]).toEqual(definedProduct)
})

it('should not navigate to list of products when product can not be defined', async () => {
  const product = newProduct()
  when(axios.post).calledWith('/products', product).mockRejectedValue({
    response: {
      status: 500,
      data: 'Boom'
    }
  })

  await defineProduct(product)

  expect(router.push).not.toHaveBeenCalled()
})

it('should not add product to store when product can not be defined', async () => {
  const product = newProduct()
  when(axios.post).calledWith('/products', product).mockRejectedValue({
    response: {
      status: 500,
      data: 'Boom'
    }
  })

  await defineProduct(product)

  expect(store.state.product.products).toHaveLength(0)
})

it('should navigate back when cancel is clicked', async () => {
  await cancelWrapper.trigger('click')

  expect(router.go).toHaveBeenCalledWith(-1)
})

let wrapper
let saveWrapper
let cancelWrapper
let slugWrapper
let titleWrapper
let descriptionWrapper
let router
let store

function mountComponent() {
  router = {
    push: jest.fn(),
    go: jest.fn()
  }
  store = createStore()
  wrapper = mount(NewProduct, {
    localVue: vueWithVuex,
    store,
    vuetify: new Vuetify(),
    mocks: {
      $router: router
    }
  })
  saveWrapper = wrapper.find('[data-save]')
  cancelWrapper = wrapper.find('[data-cancel]')
  slugWrapper = wrapper.find('[data-slug]')
  titleWrapper = wrapper.find('[data-title]')
  descriptionWrapper = wrapper.find('[data-description]')
}

function newProduct() {
  return {
    slug: 'the-matrix-trilogy',
    title: 'The Matrix Trilogy',
    description: 'This is Matrix'
  }
}

async function defineProduct(product) {
  slugWrapper.setValue(product.slug)
  titleWrapper.setValue(product.title)
  descriptionWrapper.setValue(product.description)
  await flushPromises()
  await saveWrapper.trigger('click')
}
