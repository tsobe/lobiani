package com.lobiani.app

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException

/**
 * @author Beka Tsotsoria
 */
@RestController
class TestController {

    @GetMapping("/error")
    fun error() {
        throw IllegalArgumentException("Boom");
    }

    @GetMapping
    fun hello(@RequestParam(defaultValue = "John") name: String): String {
        return "Hello $name"
    }
}
