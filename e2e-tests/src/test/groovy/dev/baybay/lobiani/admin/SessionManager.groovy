package dev.baybay.lobiani.admin

import geb.spock.GebSpec

class SessionManager {

    private GebSpec spec
    private boolean loggedIn

    SessionManager(GebSpec spec) {
        this.spec = spec
    }

    def start() {
        if (!loggedIn) {
            log"Starting session"
            def adminLoginPage = spec.to AdminLoginPage
            adminLoginPage.login()
            def authServerLoginPage = spec.at AuthorizationServerLoginPage
            authServerLoginPage.login()
            loggedIn = true
        }
    }

    def stop() {
        if (loggedIn) {
            log"Stopping session"
            def protectedPage = spec.page as AdminProtectedPageBase
            protectedPage.openNavigationDrawer()
            protectedPage.logout()
            loggedIn = false
        }
    }

    private log(msg) {
        println "${new Date()} [${spec.class.simpleName}] - $msg"
    }
}
