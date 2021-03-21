package dev.baybay.lobiani.admin

import geb.Page

abstract class AdminProtectedPageBase extends Page {

    static content = {
        logoutBtn { $("[data-logout]") }
        navigationDrawerToggle { $(".v-toolbar__content .v-app-bar__nav-icon") }
    }


    def logout() {
        if (!logoutBtn.displayed) {
            navigationDrawerToggle.click()
        }
        logoutBtn.click()
    }
}
