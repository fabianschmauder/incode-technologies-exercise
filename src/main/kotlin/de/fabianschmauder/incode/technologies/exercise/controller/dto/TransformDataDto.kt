package de.fabianschmauder.incode.technologies.exercise.controller.dto

import de.fabianschmauder.incode.technologies.exercise.data.transform.Transformation

data class TransformDataDto(
    val value: String? = null,
    val transformations: List<Transformation>? = listOf()
)
