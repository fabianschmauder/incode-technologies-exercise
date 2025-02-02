package de.fabianschmauder.incode.technologies.exercise.data.transform

import software.amazon.awssdk.services.dynamodb.model.AttributeValue

data class Transform(
    val operator: TransformOperator,
    val regex: String
)

enum class TransformOperator{
    REMOVE_REGEX
}

data class TransformDataEntity(
    val requestId: String,
    val value: String,
    val transforms: List<Transform>,
    val result: String
) {
    fun toEntity(): Map<String, AttributeValue> = mapOf(
        "requestId" to AttributeValue.builder().s(this.requestId).build(),
        "value" to AttributeValue.builder().s(this.value).build(),
        "transforms" to AttributeValue.builder().l(
            this.transforms.map { transform ->
                AttributeValue.builder().m(
                    mapOf(
                        "operator" to AttributeValue.builder().s(transform.operator.name).build(),
                        "regex" to AttributeValue.builder().s(transform.regex).build()
                    )
                ).build()
            }
        ).build(),
        "result" to AttributeValue.builder().s(this.result).build(),
    )
}
