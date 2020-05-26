package com.lobiani.inventory

class ItemDefinitionModule extends geb.Module {

    static content = {
        slug { $("span").text() }
        deleteAction { $(".delete") }
    }

    def delete() {
        deleteAction.click()
    }
}
