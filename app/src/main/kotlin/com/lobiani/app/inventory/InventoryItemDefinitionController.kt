package com.lobiani.app.inventory

import com.lobiani.app.inventory.api.DefineNewInventoryItem
import com.lobiani.app.inventory.api.DeleteInventoryItemDefinition
import com.lobiani.app.inventory.api.QueryAllInventoryItemDefinitions
import com.lobiani.app.inventory.query.InventoryItemDefinition
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
@RequestMapping("/inventory/item-definitions")
class InventoryItemDefinitionController(
        private val commandGateway: CommandGateway,
        private val queryGateway: QueryGateway) {

    @GetMapping
    fun definitions(model: ModelMap): String {
        model.addAttribute("definitions", getAllDefinitions())
        return "inventory/definitions"
    }

    @GetMapping("/new")
    fun newDefinition(): String {
        return "inventory/new-definition"
    }

    @PostMapping("/new")
    fun addNewDefinition(@RequestParam slug: String): String {
        commandGateway.sendAndWait<String>(DefineNewInventoryItem(UUID.randomUUID(), slug))
        return "redirect:/inventory/item-definitions"
    }

    @GetMapping("/delete")
    fun deleteDefinition(@RequestParam id: UUID): String {
        commandGateway.sendAndWait<Void>(DeleteInventoryItemDefinition(id))
        return "redirect:/inventory/item-definitions"
    }

    private fun getAllDefinitions(): MutableList<InventoryItemDefinition>? {
        return queryGateway.query(QueryAllInventoryItemDefinitions(),
                ResponseTypes.multipleInstancesOf(InventoryItemDefinition::class.java)).get()
    }
}
