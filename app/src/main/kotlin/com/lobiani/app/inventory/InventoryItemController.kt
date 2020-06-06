package com.lobiani.app.inventory

import com.lobiani.app.inventory.api.DefineInventoryItem
import com.lobiani.app.inventory.api.DeleteInventoryItem
import com.lobiani.app.inventory.api.QueryAllInventoryItems
import com.lobiani.app.inventory.query.InventoryItem
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@Controller
@RequestMapping("/inventory-items")
class InventoryItemController(
        private val commandGateway: CommandGateway,
        private val queryGateway: QueryGateway) {

    @GetMapping
    fun items(model: ModelMap): String {
        model.addAttribute("items", getAllItems())
        return "inventory/items"
    }

    @GetMapping("/new")
    fun newItem(): String {
        return "inventory/new-item"
    }

    @PostMapping("/new")
    fun defineNewItem(@RequestParam slug: String): String {
        commandGateway.sendAndWait<String>(DefineInventoryItem(UUID.randomUUID(), slug))
        return "redirect:/inventory-items"
    }

    @GetMapping("/delete")
    fun deleteItem(@RequestParam id: UUID): String {
        commandGateway.sendAndWait<Void>(DeleteInventoryItem(id))
        return "redirect:/inventory-items"
    }

    private fun getAllItems(): List<InventoryItem>? {
        return queryGateway.query(QueryAllInventoryItems(),
                ResponseTypes.multipleInstancesOf(InventoryItem::class.java)).get()
    }
}
