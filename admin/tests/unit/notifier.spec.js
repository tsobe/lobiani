import createNotifier from '@/store/notifier'
import NotificationHolder from '@/components/NotificationHolder'
import Notifier from '@/plugins/notifier'
import {createLocalVue, mount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vuetify from 'vuetify'

describe('notifier', () => {
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
        notifier: createNotifier()
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
})
