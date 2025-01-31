package de.fabianschmauder.incode.technologies.exercise.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transform")
class TransformController {

    @GetMapping
    suspend fun getStatus(): String {
        return "Up"
    }
}
