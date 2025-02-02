package de.fabianschmauder.incode.technologies.exercise.service

import de.fabianschmauder.incode.technologies.exercise.data.transform.Transform
import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformOperator
import de.fabianschmauder.incode.technologies.exercise.service.operator.replaceRegex

fun String.applyTransformations(transforms: List<Transform>): String {
    var result = this
    transforms.forEach { transform ->
        result = applyTransform(transform, result)
    }
    return result
}

private fun applyTransform(transform: Transform, result: String) = when (transform.operator) {
    TransformOperator.REMOVE_REGEX -> result.replaceRegex(transform.regex)
}
