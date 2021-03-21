import {mount} from '@vue/test-utils'
import Product from '@/components/Product.vue'

it('should display product information when mounted', () => {
  const product = newProduct()

  mountComponent(product)

  expect(wrapper.find('[data-title]').text()).toBe(product.title)
  expect(wrapper.find('[data-description]').text()).toBe(product.description)
  expect(wrapper.find('[data-price]').text()).toBe(`${product.price.value} €`)
})

it('should display stock summary as OUT OF STOCK when stockLevel is 0', () => {
  const stockLevel = 0
  const product = newProduct(stockLevel)

  mountComponent(product)

  expect(stockSummaryWrapper.text()).toBe('out of stock')
  expect(stockSummaryWrapper.classes('text-red-600')).toBe(true)
})

it('should display stock summary as IN STOCK when stockLevel is far greater than 0', () => {
  const stockLevel = 30
  const product = newProduct(stockLevel)

  mountComponent(product)

  expect(stockSummaryWrapper.text()).toBe('in stock')
  expect(stockSummaryWrapper.classes('text-green-600')).toBe(true)
})

it('should display stock summary with warning in plural when stockLevel is approaching 0', () => {
  const stockLevel = 4
  const product = newProduct(stockLevel)

  mountComponent(product)

  expect(stockSummaryWrapper.text()).toBe('only 4 items left')
  expect(stockSummaryWrapper.classes('text-yellow-600')).toBe(true)
})

it('should display stock summary with warning in singular when stockLevel is approaching 0', () => {
  const stockLevel = 1
  const product = newProduct(stockLevel)

  mountComponent(product)

  expect(stockSummaryWrapper.text()).toBe('only 1 item left')
  expect(stockSummaryWrapper.classes('text-yellow-600')).toBe(true)
})

let wrapper
let stockSummaryWrapper

function mountComponent(product) {
  wrapper = mount(Product, {
    propsData: {
      product: product
    }
  })
  stockSummaryWrapper = wrapper.find('[data-stock-summary]')
}

function newProduct(stockLevel = 20) {
  return {
    title: 'The Matrix',
    description: 'This is Matrix',
    price: {
      value: '17',
      currency: 'EUR'
    },
    stockLevel: stockLevel
  }
}
