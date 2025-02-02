package de.fabianschmauder.incode.technologies.exercise.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "dynamodb")
data class DynamoDbProperties(
    var region: String,
    var transformedTableName: String
)
