package com.lobiani.inventory

import geb.Page

class NewInventoryItemDefinitionPage extends Page {

    static at = { !form.empty }

    static content = {
        form { $(".new-definition-form") }
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
