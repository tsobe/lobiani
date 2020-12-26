import {mount} from '@vue/test-utils'
import Profile from '@/components/profile'

describe('Profile', () => {
  it('should show basic profile info when user inf is available', () => {
    const auth = {
      user: {
        picture: 'https://example.com/avatar.jpg',
        name: 'John Smith',
        email: 'jsmith@example.com'
      }
    }
    const wrapper = mount(Profile, {
      mocks: {
        $auth: auth
      }
    })

    expect(wrapper.find('img').element.getAttribute('src')).toBe(auth.user.picture)
    expect(wrapper.find('[data-name]').text()).toBe(auth.user.name)
    expect(wrapper.find('[data-email]').text()).toBe(auth.user.email)
  })
})
