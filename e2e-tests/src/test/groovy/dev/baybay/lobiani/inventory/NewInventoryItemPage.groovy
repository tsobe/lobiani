package dev.baybay.lobiani.inventory

import geb.Page

class NewInventoryItemPage extends Page {

    static at = { !saveBtn.empty }

    static content = {
        slugInput { $("[data-slug]") }
        saveBtn { $("[data-save]") }
    }

    def enterSlug(slug) {
        slugInput.value(slug)
    }

    def save() {
        saveBtn.click()
    }
}
