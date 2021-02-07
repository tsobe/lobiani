package dev.baybay.lobiani.admin

import geb.Page

class AdminLoginPage extends Page {

    static url = "/login"

    static at = { !loginBtn.empty }

    static content = {
        loginBtn { $("[data-login]") }
    }

    def login() {
        loginBtn.click()
    }
}
