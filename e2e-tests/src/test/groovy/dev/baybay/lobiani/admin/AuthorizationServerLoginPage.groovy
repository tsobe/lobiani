package dev.baybay.lobiani.admin

import geb.Page

class AuthorizationServerLoginPage extends Page {

    static at = { !username.empty && !password.empty && !submit.empty }

    static content = {
        username { $("#username") }
        password { $("#password") }
        submit { $("[name='action']") }
    }

    def login() {
        username.value System.getProperty("test.admin.user")
        password.value System.getProperty("test.admin.password")
        submit.click()
    }
}
