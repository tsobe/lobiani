import {createLocalVue, mount} from '@vue/test-utils'
import Auth from '@/plugins/auth'
import createAuth0Client from '@auth0/auth0-spa-js'
import flushPromises from 'flush-promises'
import Profile from '@/components/Profile'
import Vuetify from 'vuetify'
import createAuthGuard from '@/plugins/authGuard'

jest.mock('@auth0/auth0-spa-js')
jest.mock('@/plugins/authGuard')

describe('profile', () => {
  beforeEach(setupAuth0ClientMock)

  afterEach(() => {
    jest.resetAllMocks()
    clearRedirect()
  })

  it('should redirect to authorization server when login button is clicked', async () => {
    await mountComponent()

    await loginButtonWrapper.trigger('click')

    expect(mockAuth0Client.loginWithRedirect).toHaveBeenCalled()
  })

  it('should hide login button and show profile when authenticated', async () => {
    mockAuth0Client.isAuthenticated.mockResolvedValue(true)

    await mountComponent()

    expect(loginButtonWrapper.exists()).toBe(false)
    expect(profileWrapper.exists()).toBe(true)
  })

  it('should show login button and hide profile when not authenticated', async () => {
    mockAuth0Client.isAuthenticated.mockResolvedValue(false)

    await mountComponent()

    expect(loginButtonWrapper.exists()).toBe(true)
    expect(profileWrapper.exists()).toBe(false)
  })

  it('should handle redirect callback when it is an authorization redirect', async () => {
    setupRedirect()
    mockAuth0Client.handleRedirectCallback.mockResolvedValue({appState: 'foo'})

    await mountComponent()

    expect(mockAuth0Client.handleRedirectCallback).toHaveBeenCalled()
    expect(authOptions.onRedirectCallback).toHaveBeenCalledWith('foo')
  })

  it('should not handle redirect callback when it is not an authorization redirect', async () => {
    await mountComponent()

    expect(mockAuth0Client.handleRedirectCallback).not.toHaveBeenCalled()
  })

  it('should logout and redirect to home page when logout is clicked', async () => {
    mockAuth0Client.isAuthenticated.mockResolvedValue(true)
    mockAuth0Client.logout.mockResolvedValue({})
    await mountComponent()

    await logoutButtonWrapper.trigger('click')
    await flushPromises()

    expect(mockAuth0Client.logout).toHaveBeenCalledWith({returnTo: window.location.origin})
  })

  it('should add authGuard to router when installing plugin', async () => {
    const authGuard = {}
    createAuthGuard.mockImplementation(() => {
      return authGuard
    })

    installAuthPlugin()

    expect(authOptions.router.beforeResolve).toHaveBeenCalledWith(authGuard)
  })

  let wrapper, loginButtonWrapper, logoutButtonWrapper, profileWrapper

  const mockAuth0Client = {
    loginWithRedirect: jest.fn(),
    handleRedirectCallback: jest.fn(),
    isAuthenticated: jest.fn(),
    logout: jest.fn()
  }

  const authOptions = {
    clientId: 'client_id',
    domain: 'example.eu.auth0.com',
    redirectUri: 'http://example.com',
    onRedirectCallback: jest.fn(),
    router: {
      beforeResolve: jest.fn()
    }
  }

  function installAuthPlugin() {
    const localVue = createLocalVue()
    localVue.use(Auth, authOptions)
    return localVue
  }

  async function mountComponent() {
    wrapper = mount(Profile, {
      localVue: installAuthPlugin(),
      vuetify: new Vuetify()
    })
    await flushPromises()
    loginButtonWrapper = wrapper.find('button[data-login]')
    logoutButtonWrapper = wrapper.find('button[data-logout]')
    profileWrapper = wrapper.find('[data-profile]')
  }

  function setupAuth0ClientMock() {
    createAuth0Client.mockImplementation(options => {
      return isExpectedOptions(options)
        ? Promise.resolve(mockAuth0Client)
        : Promise.reject(new Error(`createAuth0Client called with unexpected options: ${options}`))
    })
  }

  function setupRedirect() {
    delete window.location
    window.location = new URL('http://example.com?code=7tk6S5_uAu0iKtS5&state=fmdHMmE3')
  }

  function clearRedirect() {
    delete window.location
    window.location = new URL('http://example.com')
  }

  function isExpectedOptions(options) {
    return options.client_id === authOptions.clientId &&
      options.domain === authOptions.domain &&
      options.redirect_uri === authOptions.redirectUri
  }
})
