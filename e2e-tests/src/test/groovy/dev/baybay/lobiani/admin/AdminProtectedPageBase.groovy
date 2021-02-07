package dev.baybay.lobiani.admin

import geb.Page

abstract class AdminProtectedPageBase extends Page {

    static content = {
        logoutBtn { $("[data-logout]") }
        navigationDrawerToggle { $(".v-toolbar__content .v-app-bar__nav-icon") }
    }

    def openNavigationDrawer() {
        navigationDrawerToggle.click()
    }

    def logout() {
        logoutBtn.click()
    }
}
