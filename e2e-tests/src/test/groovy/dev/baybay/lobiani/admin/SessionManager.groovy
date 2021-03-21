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
            waitForLogin()
            loggedIn = true
        }
    }

    def stop() {
        if (loggedIn) {
            log"Stopping session"
            def protectedPage = spec.page as AdminProtectedPageBase
            protectedPage.logout()
            loggedIn = false
        }
    }

    private log(msg) {
        println "${new Date()} [${spec.class.simpleName}] - $msg"
    }

    private waitForLogin() {
        // this hack is needed cause sometimes application does not immediately get updated
        // with the login status from the authorization server
        sleep 1000
    }
}
