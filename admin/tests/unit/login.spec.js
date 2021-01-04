import Login from '@/views/Login'
import {mount} from '@vue/test-utils'

it('should delegate to $auth when login button is clicked', async () => {
  const authMock = {
    login: jest.fn()
  }
  const wrapper = mount(Login, {
    mocks: {
      $auth: authMock
    }
  })

  await wrapper.find('[data-login]').trigger('click')

  expect(authMock.login).toHaveBeenCalledWith({redirect_uri: window.location.origin})
})
