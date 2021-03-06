import {createStore, Notifier} from '@/notifier'
import NotificationHolder from '@/notifier/NotificationHolder'
import {createLocalVue, mount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vuetify from 'vuetify'

it('should not show notification initially', () => {
  mountComponent('Hello!')

  expect(getNotificationContentWrapper().isVisible()).toBe(false)
})

it('should show notification when triggered with message text', async () => {
  mountComponent('Hello!')

  await wrapper.find('button').trigger('click')

  expect(getNotificationContentWrapper().text()).toBe('Hello!')
})

it('should show notification when triggered with message object', async () => {
  mountComponent({
    text: 'You!'
  })

  await wrapper.find('button').trigger('click')

  expect(getNotificationContentWrapper().text()).toBe('You!')
})

it('should hide notification when dismissed', async () => {
  mountComponent('Hello!')

  await wrapper.find('button').trigger('click')
  await wrapper.find('[data-dismiss]').trigger('click')

  expect(getNotificationContentWrapper().isVisible()).toBe(false)
})

const HelperComponent = {
  template: '<div><button @click="triggerShowMessage"></button><notification-holder></notification-holder></div>',
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

function getNotificationContentWrapper() {
  return wrapper.findComponent(NotificationHolder).find('[data-notification] .v-snack__content')
}
