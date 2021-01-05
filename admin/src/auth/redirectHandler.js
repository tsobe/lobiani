export function createRedirectHandler(router) {
  return {
    handle: appState => {
      router.push(appState?.next ?? '/')
    }
  }
}
