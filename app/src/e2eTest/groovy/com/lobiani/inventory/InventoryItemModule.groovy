package com.lobiani.inventory

class InventoryItemModule extends geb.Module {

    static content = {
        slug { $("span").text() }
        deleteAction { $(".delete") }
    }

    def delete() {
        deleteAction.click()
    }
}
