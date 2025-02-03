package de.fabianschmauder.incode.technologies.exercise.service

import de.fabianschmauder.incode.technologies.exercise.data.transform.Transformation
import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformOperator
import de.fabianschmauder.incode.technologies.exercise.service.operator.replaceRegex

fun String.applyTransformations(transformations: List<Transformation>): String {
    var result = this
    transformations.forEach { transform ->
        result = applyTransform(transform, result)
    }
    return result
}

private fun applyTransform(transformation: Transformation, result: String) = when (transformation.operator) {
    TransformOperator.REMOVE_REGEX -> result.replaceRegex(transformation.regex)
}
