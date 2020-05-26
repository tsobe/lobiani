package com.lobiani.app.inventory

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/inventory/item-definitions")
class InventoryItemDefinitionController {

    private val definitions = mutableListOf<String>()

    @GetMapping
    fun definitions(model: ModelMap): String {
        model.addAttribute("definitions", definitions)
        return "inventory/definitions"
    }

    @GetMapping("/new")
    fun newDefinition(): String {
        return "inventory/new-definition"
    }

    @PostMapping("/new")
    fun addNewDefinition(@RequestParam slug: String): String {
        definitions.add(slug)
        return "redirect:/inventory/item-definitions"
    }

    @GetMapping("/delete")
    fun deleteDefinition(@RequestParam slug: String): String {
        definitions.removeIf { slug == it }
        return "redirect:/inventory/item-definitions"
    }
}
