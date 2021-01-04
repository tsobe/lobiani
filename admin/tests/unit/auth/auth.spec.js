import {createLocalVue} from '@vue/test-utils'
import {Auth, createAuthGuard} from '@/auth'
import createAuth0Client from '@auth0/auth0-spa-js'
import flushPromises from 'flush-promises'

jest.mock('@auth0/auth0-spa-js')
jest.mock('@/auth/authGuard')

beforeEach(setupAuth0ClientMock)

afterEach(() => {
  jest.resetAllMocks()
  clearRedirect()
})

it('should redirect to authorization server when login is requested', async () => {
  await initAuth()
  const opts = {}

  await auth.login(opts)

  expect(auth.loading).toBe(true)
  expect(mockAuth0Client.loginWithRedirect).toHaveBeenCalledWith(opts)
})

it('should be authenticated when authorization server indicates so', async () => {
  mockAuth0Client.isAuthenticated.mockResolvedValue(true)

  await initAuth()

  expect(auth.authenticated).toBe(true)
})

it('should not be authenticated when authorization server does not indicate so', async () => {
  mockAuth0Client.isAuthenticated.mockResolvedValue(false)

  await initAuth()

  expect(auth.authenticated).toBe(false)
})

it('should handle redirect callback when it is an authorization redirect', async () => {
  setupRedirect()
  mockAuth0Client.handleRedirectCallback.mockResolvedValue({appState: 'foo'})

  await initAuth()

  expect(authOptions.onRedirectCallback).toHaveBeenCalledWith('foo')
})

it('should not handle redirect callback when it is not an authorization redirect', async () => {
  await initAuth()

  expect(mockAuth0Client.handleRedirectCallback).not.toHaveBeenCalled()
  expect(authOptions.onRedirectCallback).not.toHaveBeenCalled()
})

it('should logout from authorization server when logout is requested', async () => {
  mockAuth0Client.isAuthenticated.mockResolvedValue(true)
  mockAuth0Client.logout.mockResolvedValue({})
  await initAuth()
  const opts = {}

  await auth.logout(opts)

  expect(auth.loading).toBe(true)
  expect(mockAuth0Client.logout).toHaveBeenCalledWith(opts)
})

it('should add authGuard to router when initializing', async () => {
  const authGuard = {}
  createAuthGuard.mockImplementation((opts) => {
    return opts.auth ? authGuard : null
  })

  await initAuth()

  expect(authOptions.router.beforeResolve).toHaveBeenCalledWith(authGuard)
})

it('auth should have user when available', async () => {
  const user = {}
  mockAuth0Client.getUser.mockResolvedValue(user)

  await initAuth()

  expect(auth.user).toBe(user)
})

it('should retrieve user info after redirect callback is handled', async () => {
  setupRedirect()
  mockAuth0Client.handleRedirectCallback.mockResolvedValue({appState: 'foo'})
  mockAuth0Client.getUser.mockResolvedValue({})

  await initAuth()

  expect(mockAuth0Client.getUser).toHaveBeenCalledAfter(mockAuth0Client.handleRedirectCallback)
})

it('should retrieve user info before authentication status is checked', async () => {
  mockAuth0Client.isAuthenticated.mockResolvedValue(true)
  mockAuth0Client.getUser.mockResolvedValue({})

  await initAuth()

  expect(mockAuth0Client.getUser).toHaveBeenCalledBefore(mockAuth0Client.isAuthenticated)
})

it('should retrieve id_token before authentication status is checked', async () => {
  mockAuth0Client.isAuthenticated.mockResolvedValue(true)
  mockAuth0Client.getIdTokenClaims.mockResolvedValue({
    __raw: idToken
  })

  await initAuth()

  expect(mockAuth0Client.getIdTokenClaims).toHaveBeenCalledBefore(mockAuth0Client.isAuthenticated)
})

it('auth should have id_token when available', async () => {
  mockAuth0Client.getIdTokenClaims.mockResolvedValue({
    __raw: idToken
  })

  await initAuth()

  expect(auth.idToken).toBe(idToken)
})

let auth
const idToken = 'xyz'

const mockAuth0Client = {
  loginWithRedirect: jest.fn(),
  handleRedirectCallback: jest.fn(),
  isAuthenticated: jest.fn(),
  getIdTokenClaims: jest.fn(),
  getUser: jest.fn(),
  logout: jest.fn()
}

const authOptions = {
  clientId: 'client_id',
  domain: 'example.eu.auth0.com',
  redirectUri: 'https://example.com',
  onRedirectCallback: jest.fn(),
  router: {
    beforeResolve: jest.fn()
  }
}

async function initAuth() {
  const localVue = createLocalVue()
  localVue.use(Auth, authOptions)
  auth = localVue.prototype.$auth
  await flushPromises()
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
  window.location = new URL('https://example.com?code=7tk6S5_uAu0iKtS5&state=fmdHMmE3')
}

function clearRedirect() {
  delete window.location
  window.location = new URL('https://example.com')
}

function isExpectedOptions(options) {
  return options.client_id === authOptions.clientId &&
    options.domain === authOptions.domain &&
    options.redirect_uri === authOptions.redirectUri
}
