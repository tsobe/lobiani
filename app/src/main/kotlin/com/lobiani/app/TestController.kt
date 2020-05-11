package com.lobiani.app

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class TestController {

    @GetMapping("/error")
    fun error() {
        throw IllegalArgumentException("Boom");
    }

    @GetMapping("/hello")
    fun hello(@RequestParam(defaultValue = "John") name: String): String {
        return "Hello $name"
    }

    @GetMapping("/sum")
    fun sum(@RequestParam a: Int, @RequestParam b: Int): Int {
        return a + b
    }

}
