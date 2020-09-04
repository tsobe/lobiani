package dev.baybay.lobiani.inventory

import geb.Page

class NewInventoryItemPage extends Page {

    static at = { !saveBtn.empty }

    static content = {
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
