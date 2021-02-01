import {mount} from '@vue/test-utils'
import Product from '@/components/Product'

it('should display all attributes when mounted', () => {
  const wrapper = mount(Product, {
    propsData: {
      product: {
        id: 'product-1',
        slug: 'the-matrix-trilogy',
        title: 'The Matrix Trilogy',
        description: 'This is Matrix'
      }
    }
  })

  expect(wrapper.find('[data-slug]').text()).toBe('the-matrix-trilogy')
  expect(wrapper.find('[data-title]').text()).toBe('The Matrix Trilogy')
  expect(wrapper.find('[data-description]').text()).toBe('This is Matrix')
})
