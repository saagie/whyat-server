package io.saagie.whyat.controller

import io.saagie.whyat.domain.Example
import io.saagie.whyat.service.ExampleService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ExampleController(val exampleService: ExampleService) {

    @PostMapping("/example")
    fun appendExample(@RequestBody example: Example): Example {
        return exampleService.appendExample(example)
    }
}