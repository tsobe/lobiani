package dev.baybay.lobiani.inventory

import dev.baybay.lobiani.admin.AdminProtectedPageBase
import dev.baybay.lobiani.admin.LoadingButtonModule

class NewInventoryItemPage extends AdminProtectedPageBase {

    static at = { waitFor { !saveBtn.empty } }

    static content = {
        slugInput { $("[data-slug]") }
        saveBtn { $("[data-save]").module LoadingButtonModule }
    }

    def enterSlug(slug) {
        slugInput.value slug
    }

    def save() {
        saveBtn.click()
    }
}
