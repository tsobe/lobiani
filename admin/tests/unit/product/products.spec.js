import {createLocalVue, mount} from '@vue/test-utils'
import Products from '@/views/Products'
import Product from '@/components/Product'
import {when} from 'jest-when'
import axios from 'axios'
import Vuex from 'vuex'
import {createStore} from '@/store/products'
import flushPromises from 'flush-promises'

jest.mock('axios')

it('should display all products when mounted', async () => {
  const products = [
    {
      id: 'prod-1',
      slug: 'the-matrix-trilogy',
      title: 'The Matrix Trilogy',
      description: 'This is Matrix'
    },
    {
      id: 'prod-2',
      slug: 'memento',
      title: 'Memento',
      description: 'This is Memento'
    }
  ]

  when(axios.get)
    .calledWith('/products')
    .mockResolvedValue({
      data: products
    })

  const localVue = createLocalVue()
  localVue.use(Vuex)
  const wrapper = mount(Products, {
    localVue,
    store: new Vuex.Store({
      modules: {
        product: createStore()
      }
    })
  })
  await flushPromises()

  expect(wrapper.findAllComponents(Product)).toHaveLength(2)
})
