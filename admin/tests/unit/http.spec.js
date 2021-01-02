import Http from '@/plugins/http'
import axios from 'axios'
import {createLocalVue} from '@vue/test-utils'
import {getInstance} from '@/auth'
import nock from 'nock'
import httpAdapter from 'axios/lib/adapters/http'

axios.defaults.adapter = httpAdapter

jest.mock('@/auth')

beforeEach(installPlugin)
afterEach(jest.resetAllMocks)

it('should inject axios as a global $http variable when installing a plugin', () => {
  expect(http).toBe(axios)
})

it('should set baseURL to /api', () => {
  expect(http.defaults.baseURL).toBe('/api')
})

it('should add authorization header when auth info is available', async () => {
  const auth = {
    idToken: 'id_token'
  }
  getInstance.mockImplementation(() => auth)
  const scope = nock('https://example.com')
    .matchHeader('authorization', 'Bearer id_token')
    .get('/api/hello')
    .reply(200)

  await http.get('https://example.com/api/hello')

  expect(scope.isDone()).toBe(true)
})

it('should not add authorization header when auth info is not available', async () => {
  const scope = nock('https://example.com')
    .get('/api/hello')
    .reply(200)

  await http.get('https://example.com/api/hello')

  expect(scope.isDone()).toBe(true)
})

let http

function installPlugin() {
  const LocalVue = createLocalVue()
  LocalVue.use(Http)
  http = new LocalVue().$http
}
