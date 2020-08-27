import {createLocalVue, mount} from '@vue/test-utils'
import axios from 'axios'
import flushPromises from 'flush-promises'
import DefineInventoryItem from '@/views/DefineInventoryItem'
import InventoryItem from '@/components/InventoryItem'
import InventoryItems from '@/views/InventoryItems'
import VueRouter from 'vue-router'
import App from '@/App'

jest.mock('axios')

function setupFailingPOSTCall() {
  axios.post.mockRejectedValue({
    response: {
      status: 500,
      data: 'Boom'
    }
  })
}

function setupSuccessfulPOSTCall(data = {}) {
  axios.post.mockResolvedValue({data})
}

describe('DefineInventoryItem', () => {
  describe('item can be defined when API call succeeds', () => {
    beforeAll(async () => {
      setupSuccessfulPOSTCall({
        id: itemId,
        slug: slug
      }
      )

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
        slug: slug
      }])
    })

    it('should reset the slug input', () => {
      expect(slugWrapper.element.value).toBeFalsy()
    })
  })

  describe('item can not be defined when API call fails', () => {
    beforeAll(async () => {
      setupFailingPOSTCall()

      mountComponent()

      await defineItem()
    })

    it('should call the API', () => {
      expectAPIToHaveBeenCalled()
    })

    it('should not emit the "itemDefined" event', () => {
      expect(wrapper.emitted().itemDefined).toBeFalsy()
    })

    it('should not reset the slug input', () => {
      expect(slugWrapper.element.value).toBe(slug)
    })
  })

  let wrapper
  let slugWrapper
  const itemId = 'foo'
  const slug = 'the-matrix-trilogy'

  function mountComponent() {
    wrapper = mount(DefineInventoryItem)
    slugWrapper = wrapper.find('.slug')
  }

  async function defineItem() {
    slugWrapper.setValue(slug)
    await wrapper.find('.add').trigger('click')
    await flushPromises()
  }

  function expectAPIToHaveBeenCalled() {
    expect(axios.post).toHaveBeenCalledWith('/inventory-items', {slug: slug})
  }
})

describe('InventoryItem', () => {
  describe('mount', () => {
    beforeAll(mountComponent)

    it('should display slug', () => {
      expect(wrapper.find('.slug').text()).toBe(slug)
    })

    it('should display initial stock level', () => {
      expect(stockLevelWrapper.text()).toBe(initialStockLevel.toString())
    })
  })

  describe('items can be added to stock when API call succeeds', () => {
    beforeAll(async () => {
      mountComponent()

      setupSuccessfulPOSTCall()

      await addToStock(count)
    })

    it('should call the API', () => {
      expectAPIToHaveBeenCalled()
    })

    it('should increase the item stock level', () => {
      expect(stockLevelWrapper.text()).toBe(count.toString())
    })

    it('should reset the count', () => {
      expect(countWrapper.element.value).toBeFalsy()
    })
  })

  describe('items can not be added to stock when API call fails', () => {
    beforeAll(async () => {
      mountComponent()

      setupFailingPOSTCall()

      await addToStock(count)
    })

    it('should call the API', () => {
      expectAPIToHaveBeenCalled()
    })

    it('should not change the item stock level', () => {
      expect(stockLevelWrapper.text()).toBe(initialStockLevel.toString())
    })

    it('should not reset the count', () => {
      expect(countWrapper.element.value).toBe(count.toString())
    })
  })

  let wrapper
  let countWrapper
  let stockLevelWrapper
  const itemId = 'foo'
  const slug = 'the-matrix-trilogy'
  const initialStockLevel = 0
  const count = 10

  function mountComponent() {
    wrapper = mount(InventoryItem, {
      propsData: {
        item: createItem()
      }
    })
    countWrapper = wrapper.find('input[name="count"]')
    stockLevelWrapper = wrapper.find('.stock-level')
  }

  function createItem() {
    return {
      id: itemId,
      slug: slug,
      stockLevel: initialStockLevel
    }
  }

  async function addToStock(count) {
    countWrapper.setValue(count)
    await wrapper.find('.add-to-stock').trigger('click')
    await flushPromises()
  }

  function expectAPIToHaveBeenCalled() {
    expect(axios.post).toHaveBeenCalledWith(`/inventory-items/${itemId}/stock`, {count: count})
  }
})

describe('Inventory', () => {
  describe('mount', () => {
    beforeAll(async () => {
      setupSuccessfulGETCall()

      await mountComponent()
    })

    it('should call the API', () => {
      expect(axios.get).toHaveBeenCalledWith('/inventory-items')
    })

    it('should display all items', async () => {
      expect(wrapper.findAllComponents(InventoryItem)).toHaveLength(2)
    })
  })

  describe('delete', () => {
    describe('item can be deleted when API call succeeds', () => {
      beforeAll(async () => {
        setupSuccessfulGETCall()

        await mountComponent()

        await deleteItem('memento')
      })

      it('should call tha API', () => {
        expect(axios.delete).toHaveBeenCalledWith('/inventory-items/bar')
      })

      it('should remove the item from the list', () => {
        expect(findItemWrapper(slugToDelete).exists()).toBe(false)
      })
    })

    describe('item can not be deleted when API call fails', () => {
      beforeAll(async () => {
        setupSuccessfulGETCall()
        axios.delete.mockRejectedValue({
          response: {
            status: 500
          }
        })

        await mountComponent()

        await deleteItem('memento')
      })

      it('should call tha API', () => {
        expect(axios.delete).toHaveBeenCalledWith('/inventory-items/bar')
      })

      it('should not remove the item from the list', () => {
        expect(findItemWrapper(slugToDelete).exists()).toBe(true)
      })
    })

    const slugToDelete = 'memento'

    async function deleteItem(slug) {
      await findItemWrapper(slug).find('.delete').trigger('click')
      await flushPromises()
    }

    function findItemWrapper(slug) {
      return wrapper.find('[data-slug="' + slug + '"]')
    }
  })

  it('should navigate to list of items after new item is defined', async () => {
    setupSuccessfulGETCall()
    const localVue = createLocalVue()
    localVue.use(VueRouter)

    const routes = [
      {
        path: '/',
        name: 'InventoryItems',
        component: InventoryItems
      },
      {
        path: '/new',
        name: 'DefineInventoryItem',
        component: DefineInventoryItem
      }
    ]

    const router = new VueRouter({
      routes
    })

    const wrapper = mount(App, {
      localVue,
      router
    })

    await wrapper.vm.$router.push('/new')

    const defineItemWrapper = wrapper.findComponent(DefineInventoryItem)
    defineItemWrapper.vm.$emit('itemDefined', {
      id: 'baz',
      slug: 'the-simpsons',
      stockLevel: 0
    })

    await flushPromises()

    expect(wrapper.vm.$route.path).toBe('/items')
  })

  let wrapper
  const items = [
    {
      id: 'foo',
      slug: 'the-matrix-trilogy',
      stockLevel: 10
    },
    {
      id: 'bar',
      slug: 'memento',
      stockLevel: 17
    }
  ]

  async function mountComponent() {
    wrapper = mount(InventoryItems)
    await flushPromises()
  }

  function setupSuccessfulGETCall() {
    axios.get.mockResolvedValue({data: [...items]})
  }
})
