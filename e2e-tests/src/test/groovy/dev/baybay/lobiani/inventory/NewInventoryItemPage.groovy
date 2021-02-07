package dev.baybay.lobiani.inventory

import dev.baybay.lobiani.admin.AdminProtectedPageBase
import geb.module.FormElement

class NewInventoryItemPage extends AdminProtectedPageBase {

    static at = { !saveBtn.empty }

    static content = {
        slugInput { $("[data-slug]") }
        saveBtn { $("[data-save]") }
    }

    def enterSlug(slug) {
        slugInput.value(slug)
    }

    def save() {
        waitFor { saveBtn.module(FormElement).enabled }
        saveBtn.click()
    }
}
