package com.lobiani.inventory

import geb.Page

class InventoryItemDefinitionsPage extends Page {

    static url = "inventory/item-definitions"

    static at = { title == "Inventory item definitions" }

    static content = {
        newDefinition { $("#new-definition") }
        definitions { $(".definitions .definition").moduleList ItemDefinitionModule }
    }

    def openNewItemDefinitionPage() {
        newDefinition.click()
    }

    def hasDefinition(slug) {
        definitions.any { it.slug == slug }
    }

    def deleteDefinition(slug) {
        definitions.find { it.slug == slug }.delete()
    }
}
