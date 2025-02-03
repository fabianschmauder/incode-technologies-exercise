package de.fabianschmauder.incode.technologies.exercise.service.operator


fun String.replaceRegex(regex: String): String {
    return this.replace(Regex(regex), "")
}
