package de.fabianschmauder.incode.technologies.exercise.controller.dto

import de.fabianschmauder.incode.technologies.exercise.data.transform.Transform

data class TransformDataDto(
    val value: String? = null,
    val transformer: List<Transform>? = listOf()
)
