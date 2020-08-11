package dev.baybay.lobiani.inventory

import geb.Page

class NewInventoryItemPage extends Page {

    static at = { !form.empty }

    static content = {
        form { $(".new-item-form") }
        slugInput { $(".slug") }
        saveBtn { $(".add") }
    }

    def enterSlug(slug) {
        slugInput.value(slug)
    }

    def save() {
        saveBtn.click()
    }
}
