package de.fabianschmauder.incode.technologies.exercise.controller

import de.fabianschmauder.incode.technologies.exercise.controller.dto.TransformDataDto
import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformDataEntity
import de.fabianschmauder.incode.technologies.exercise.service.TransformService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/transform")
class TransformController(private val transformDataService: TransformService) {

    @PostMapping
    suspend fun transform(@RequestBody data: TransformDataDto): TransformDataEntity {
        val requestId = UUID.randomUUID().toString()
        return transformDataService.transform(requestId, data.value.orEmpty(), data.transformer.orEmpty())
    }
}
