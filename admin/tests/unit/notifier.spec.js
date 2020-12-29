import {createStore, Notifier} from '@/notifier'
import NotificationHolder from '@/notifier/NotificationHolder'
import {createLocalVue, mount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vuetify from 'vuetify'

it('should not show notification initially', () => {
  mountComponent('Hello!')

  expect(wrapper.findComponent(NotificationHolder).find('[data-notification]').text()).toBeFalsy()
})

it('should show notification when triggered with message text', async () => {
  mountComponent('Hello!')

  await wrapper.find('button').trigger('click')

  expect(wrapper.findComponent(NotificationHolder).find('[data-notification]').text()).toBe('Hello!')
})

it('should show notification when triggered with message object', async () => {
  mountComponent({
    text: 'You!'
  })

  await wrapper.find('button').trigger('click')

  expect(wrapper.findComponent(NotificationHolder).find('[data-notification]').text()).toBe('You!')
})

const HelperComponent = {
  template: '<div><button v-on:click="triggerShowMessage"></button><notification-holder></notification-holder></div>',
  components: {NotificationHolder},
  methods: {
    triggerShowMessage() {
      this.$notifier.showMessage(this.message)
    }
  }
}

let wrapper

function mountComponent(message) {
  const localVue = createLocalVue()
  localVue.use(Vuex)
  const store = new Vuex.Store({
    modules: {
      notifier: createStore()
    }
  })
  localVue.use(Notifier, {store})

  wrapper = mount(HelperComponent, {
    localVue,
    store,
    vuetify: new Vuetify(),
    data: () => ({message})
  })
}
