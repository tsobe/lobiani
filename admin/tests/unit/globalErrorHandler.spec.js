import globalErrorHandler from '@/utils/globalErrorHandler'

afterEach(jest.resetAllMocks)

it('should show response.data.message as message', () => {
  handle({
    response: {
      data: {
        message: 'API contract error'
      }
    }
  })

  expect(notifier.showMessage).toHaveBeenCalledWith({
    text: 'API contract error',
    type: 'error'
  })
})

it('should show response.data as message', () => {
  handle({
    response: {
      data: 'server responded with error'
    }
  })

  expect(notifier.showMessage).toHaveBeenCalledWith({
    text: 'server responded with error',
    type: 'error'
  })
})

it('should show "error connecting backend" when no response is received', () => {
  handle({
    request: 'error'
  })

  expect(notifier.showMessage).toHaveBeenCalledWith({
    text: 'error connecting backend',
    type: 'error'
  })
})

it('should show error.message as message', () => {
  handle({
    message: 'error message'
  })

  expect(notifier.showMessage).toHaveBeenCalledWith({
    text: 'error message',
    type: 'error'
  })
})

function handle(err) {
  globalErrorHandler.handle(err, {$notifier: notifier})
}

const notifier = {
  showMessage: jest.fn()
}
