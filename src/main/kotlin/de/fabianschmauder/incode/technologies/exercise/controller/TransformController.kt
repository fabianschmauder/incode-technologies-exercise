package de.fabianschmauder.incode.technologies.exercise.controller

import de.fabianschmauder.incode.technologies.exercise.controller.dto.TransformDataDto
import de.fabianschmauder.incode.technologies.exercise.data.IdUtils
import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformDataEntity
import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformOperator
import de.fabianschmauder.incode.technologies.exercise.service.TransformService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.regex.PatternSyntaxException


@RestController
@RequestMapping("/transform")
@Tag(name = "Transform")
class TransformController(
    private val idUtils: IdUtils,
    private val transformDataService: TransformService
) {

    fun validateRequestDto(data: TransformDataDto) {
        for (transformation in data.transformations.orEmpty()) {
            when (transformation.operator) {
                TransformOperator.REMOVE_REGEX -> {
                    try {
                        Regex(transformation.regex)
                    } catch (e: PatternSyntaxException) {
                        throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid regex: ${transformation.regex}")
                    }
                }
            }
        }
    }

    @PostMapping
    @Tag(name = "Transform", description = "Accept a value and apply a list of transformations on it")
    suspend fun transform(@RequestBody data: TransformDataDto): TransformDataEntity {
        validateRequestDto(data)

        return transformDataService.transform(
            idUtils.generateId(),
            data.value.orEmpty(),
            data.transformations.orEmpty()
        )
    }
}
