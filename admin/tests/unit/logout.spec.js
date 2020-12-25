import Logout from '@/components/Logout'
import {mount} from '@vue/test-utils'

it('should delegate to $auth when logout button is clicked', async () => {
  const authMock = {
    logout: jest.fn()
  }
  const wrapper = mount(Logout, {
    mocks: {
      $auth: authMock
    }
  })

  await wrapper.find('[data-logout]').trigger('click')

  expect(authMock.logout).toHaveBeenCalled()
})
