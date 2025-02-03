package de.fabianschmauder.incode.technologies.exercise

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
@OpenAPIDefinition
class IncodeTechnologiesExerciseApplication

fun main(args: Array<String>) {
    runApplication<IncodeTechnologiesExerciseApplication>(*args)
}
