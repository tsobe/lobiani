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

  expect(saveWrapper.attributes().disabled).toBeTruthy()
})

it('should not allow saving when item with given slug is already defined', async () => {
  mountComponent()

  await enterSlug(alreadyDefinedItemSlug)

  expect(saveWrapper.attributes().disabled).toBeTruthy()
  expect(validationMsgWrapper.isVisible()).toBe(true)
  expect(validationMsgWrapper.text()).toBe('An item with this slug is already defined')
})

it('should not allow saving when validating slug for uniqueness fails', async () => {
  setupFailingFindItemsBySlugAPICall()
  mountComponent()

  await enterSlug(alreadyDefinedItemSlug)

  expect(saveWrapper.attributes().disabled).toBeTruthy()
  expect(validationMsgWrapper.isVisible()).toBe(true)
  expect(validationMsgWrapper.text()).toBe('Failed to check slug availability')
})

it.each([' ', 'Upperacase', 'space cowboy', 'meh#', 'blah?'])('should not allow saving when slug (%s) has invalid format',
  async (invalidSlug) => {
    mountComponent()

    await enterSlug(invalidSlug)

    expect(saveWrapper.attributes().disabled).toBeTruthy()
    expect(validationMsgWrapper.isVisible()).toBe(true)
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
  expect(validationMsgWrapper.isVisible()).toBe(false)
  expect(validationMsgWrapper.text()).toBeFalsy()
})

describe('item can be defined when API call succeeds', () => {
  beforeAll(async () => {
    setupImmediateDebounce()
    setupSuccessfulFindItemsBySlugAPICall()
    setupSuccessfulDefineNewItemAPICall()

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
    expect(store.state.inventory.items).toHaveLength(1)
    expect(store.state.inventory.items[0]).toEqual({
      id: itemId,
      slug: slug,
      stockLevel: 0
    })
  })

  it('should reset the slug input', () => {
    expect(slugWrapper.element.value).toBeFalsy()
  })

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
})

describe('item can not be defined when API call fails', () => {
  beforeAll(async () => {
    setupImmediateDebounce()
    setupSuccessfulFindItemsBySlugAPICall()
    setupFailingDefineNewItemAPICall()

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
    expect(store.state.inventory.items).toHaveLength(0)
  })

  it('should not reset the slug input', () => {
    expect(slugWrapper.element.value).toBe(slug)
  })

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
})

let wrapper
let slugWrapper
let saveWrapper
let validationMsgWrapper
let store
let debounceCancel
const itemId = 'foo'
const slug = 'the-matrix-trilogy'
const alreadyDefinedItemSlug = 'already-defined-item-slug'

function mountComponent() {
  store = createStore()
  wrapper = mount(NewInventoryItem, {
    localVue: vueWithVuex,
    vuetify: new Vuetify(),
    store
  })
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

function setupFailingFindItemsBySlugAPICall() {
  when(axios.get)
    .calledWith(`/inventory-items?slug=${alreadyDefinedItemSlug}`)
    .mockRejectedValue({
      response: {
        status: 500
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
