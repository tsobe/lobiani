import {mount} from '@vue/test-utils'
import NewProduct from '@/views/NewProduct'
import flushPromises from 'flush-promises'
import Vuetify from 'vuetify'
import {when} from 'jest-when'
import axios from 'axios'
import Slug from '@/components/Slug'

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
  const slugWrapper = wrapper.findComponent(Slug)

  expect(slugWrapper.vm.$props.resource).toBe('products')
})

it('should navigate to list of products when product defined', async () => {
  when(axios.post)
    .calledWith('/products', {
      slug: 'the-matrix-trilogy',
      title: 'The Matrix Trilogy',
      description: 'This is Matrix'
    })
    .mockResolvedValue({
      data: {}
    })

  slugWrapper.setValue('the-matrix-trilogy')
  titleWrapper.setValue('The Matrix Trilogy')
  descriptionWrapper.setValue('This is Matrix')
  await flushPromises()

  await saveWrapper.trigger('click')

  expect(router.push).toHaveBeenCalledWith('/products')
})

it('should not navigate to list of products when product can not be defined', async () => {
  when(axios.post)
    .calledWith('/products', {
      slug: 'the-matrix-trilogy',
      title: 'The Matrix Trilogy',
      description: 'This is Matrix'
    })
    .mockRejectedValue({
      response: {
        status: 500,
        data: 'Boom'
      }
    })

  slugWrapper.setValue('the-matrix-trilogy')
  titleWrapper.setValue('The Matrix Trilogy')
  descriptionWrapper.setValue('This is Matrix')
  await flushPromises()

  await saveWrapper.trigger('click')

  expect(router.push).not.toHaveBeenCalled()
})

it('should navigate to list of products when cancel is clicked', async () => {
  await cancelWrapper.trigger('click')

  expect(router.push).toHaveBeenCalledWith('/products')
})

let wrapper
let saveWrapper
let cancelWrapper
let slugWrapper
let titleWrapper
let descriptionWrapper
let router

function mountComponent() {
  router = {
    push: jest.fn()
  }
  wrapper = mount(NewProduct, {
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
