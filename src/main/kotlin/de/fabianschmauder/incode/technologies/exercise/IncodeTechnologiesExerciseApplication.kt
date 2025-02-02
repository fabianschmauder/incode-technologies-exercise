package de.fabianschmauder.incode.technologies.exercise

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class IncodeTechnologiesExerciseApplication

fun main(args: Array<String>) {
    runApplication<IncodeTechnologiesExerciseApplication>(*args)
}
