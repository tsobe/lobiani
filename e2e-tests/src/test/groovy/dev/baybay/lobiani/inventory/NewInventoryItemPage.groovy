package dev.baybay.lobiani.inventory

import geb.Page
import geb.module.FormElement

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
        waitFor { saveBtn.module(FormElement).enabled }
        saveBtn.click()
    }
}
