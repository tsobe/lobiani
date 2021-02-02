import {createLocalVue, mount} from '@vue/test-utils'
import axios from 'axios'
import flushPromises from 'flush-promises'
import NewInventoryItem from '@/views/NewInventoryItem'
import Vuex from 'vuex'
import Vuetify from 'vuetify'
import _ from 'lodash'
import {when} from 'jest-when'
import {createStore} from './storeTestHelper'

const vueWithVuex = createLocalVue()
vueWithVuex.use(Vuex)

jest.mock('axios')
jest.mock('lodash')

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

  expect(saveWrapper.attributes().disabled).toBeTruthy()
})

it('should not allow saving when item with given slug is already defined', async () => {
  mountComponent()

  await enterSlug(alreadyDefinedItemSlug)

  expect(saveWrapper.attributes().disabled).toBeTruthy()
  expect(validationMsgWrapper.text()).toBe('This slug is already in use')
})

it('should not allow saving when validating slug for uniqueness fails', async () => {
  setupFailingFindItemsBySlugAPICall()
  mountComponent()

  await enterSlug(alreadyDefinedItemSlug)

  expect(saveWrapper.attributes().disabled).toBeTruthy()
  expect(validationMsgWrapper.text()).toBe('Failed to check slug availability')
})

it.each([' ', 'Upperacase', 'space cowboy', 'meh#', 'blah?'])('should not allow saving when slug (%s) has invalid format',
  async (invalidSlug) => {
    mountComponent()

    await enterSlug(invalidSlug)

    expect(saveWrapper.attributes().disabled).toBeTruthy()
    expect(validationMsgWrapper.text()).toBe('Slug must consist of lowercase alpha-numeric and dash(\'-\') characters')
  })

it('should cancel slug uniqueness validation when invalid character is appended', async () => {
  mountComponent()

  await enterSlug('valid-slug')
  await enterSlug('valid-slug#')

  expect(debounceCancel).toHaveBeenCalled()
})

it('should allow saving when unique slug is given eventually', async () => {
  mountComponent()

  await enterSlug(alreadyDefinedItemSlug)
  await enterSlug()

  expect(saveWrapper.attributes().disabled).toBeFalsy()
  expect(validationMsgWrapper.text()).toBeFalsy()
})

it('should navigate to list of items when item is successfully defined', async () => {
  setupSuccessfulDefineNewItemAPICall()
  mountComponent()

  await defineItem()

  expect(router.push).toHaveBeenCalledWith('/items')
})

it('should add item to store when item is successfully defined', async () => {
  setupSuccessfulDefineNewItemAPICall()
  mountComponent()

  await defineItem()

  expect(store.state.inventory.items).toHaveLength(1)
  expect(store.state.inventory.items[0]).toEqual({
    id: itemId,
    slug: slug,
    stockLevel: 0
  })
})

it('should not navigate to list of items when item can not be defined', async () => {
  setupFailingDefineNewItemAPICall()
  mountComponent()

  await defineItem()

  expect(router.push).not.toHaveBeenCalledWith('/items')
})

it('should not add item to store when item can not be defined', async () => {
  setupFailingDefineNewItemAPICall()
  mountComponent()

  await defineItem()

  expect(store.state.inventory.items).toHaveLength(0)
})

it('should navigate back when cancel is clicked', async () => {
  mountComponent()

  await cancelWrapper.trigger('click')

  expect(router.go).toHaveBeenCalledWith(-1)
})

let wrapper
let slugWrapper
let saveWrapper
let cancelWrapper
let validationMsgWrapper
let store
let debounceCancel
let router
const itemId = 'foo'
const slug = 'the-matrix-trilogy'
const alreadyDefinedItemSlug = 'already-defined-item-slug'

function mountComponent() {
  router = {
    go: jest.fn(),
    push: jest.fn()
  }
  store = createStore()
  wrapper = mount(NewInventoryItem, {
    localVue: vueWithVuex,
    vuetify: new Vuetify(),
    store,
    mocks: {
      $router: router
    }
  })
  slugWrapper = wrapper.find('[data-slug]')
  saveWrapper = wrapper.find('[data-save]')
  cancelWrapper = wrapper.find('[data-cancel]')
  validationMsgWrapper = wrapper.find('.v-messages__wrapper')
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

function setupFailingFindItemsBySlugAPICall() {
  when(axios.get)
    .calledWith(`/inventory-items?slug=${alreadyDefinedItemSlug}`)
    .mockRejectedValue({
      response: {
        status: 500
      }
    })
}

function setupSuccessfulDefineNewItemAPICall() {
  when(axios.post)
    .calledWith('/inventory-items', {slug})
    .mockResolvedValue({
      data: {
        id: itemId,
        slug: slug,
        stockLevel: 0
      }
    })
}

function setupFailingDefineNewItemAPICall() {
  when(axios.post)
    .calledWith('/inventory-items', {slug})
    .mockRejectedValue({
      response: {
        status: 500,
        data: 'Boom'
      }
    })
}

function setupImmediateDebounce() {
  debounceCancel = jest.fn()
  _.debounce = jest.fn((fn) => {
    fn.cancel = debounceCancel
    return fn
  })
}
