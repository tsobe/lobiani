package com.lobiani.app

import org.springframework.web.bind.annotation.GetMapping
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
}
