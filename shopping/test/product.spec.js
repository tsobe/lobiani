import { mount } from '@vue/test-utils'
import Product from '@/components/Product.vue'

it('should display product information when mounted', () => {
  const title = 'The Matrix'
  const description = 'This is Matrix'
  const price = '17'
  const currency = 'EUR'
  const stockLevel = '20'
  const wrapper = mount(Product, {
    propsData: {
      product: {
        title: title,
        description: description,
        price: {
          value: price,
          currency: currency
        },
        stockLevel: stockLevel
      }
    }
  })

  expect(wrapper.find('[data-title]').text()).toBe(title)
  expect(wrapper.find('[data-description]').text()).toBe(description)
  expect(wrapper.find('[data-price]').text()).toBe(`${price} ${currency}`)
  expect(wrapper.find('[data-stock-level]').text()).toBe(stockLevel)
})
