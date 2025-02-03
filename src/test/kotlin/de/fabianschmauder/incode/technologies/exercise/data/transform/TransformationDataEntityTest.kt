package de.fabianschmauder.incode.technologies.exercise.data.transform

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

class TransformationDataEntityTest {

    @Test
    fun `to entity should map data to dynamo data`() {
        // GIVEN
        val data = TransformDataEntity(
            requestId = "some-id",
            value = "some -42 20 data",
            result = "some data",
            transformations = listOf(
                Transformation(
                    operator = TransformOperator.REMOVE_REGEX,
                    regex = "-?\\d+"
                ),
                Transformation(
                    operator = TransformOperator.REMOVE_REGEX,
                    regex = "\\s\\s"
                )
            )
        )
        // WHEN
        val entity = data.toEntity()

        // THEN
        assertThat(
            entity, equalTo(
                mapOf(
                    "requestId" to AttributeValue.builder().s("some-id").build(),
                    "value" to AttributeValue.builder().s("some -42 20 data").build(),
                    "transforms" to AttributeValue.builder().l(
                        listOf(
                            AttributeValue.builder().m(
                                mapOf(
                                    "operator" to AttributeValue.builder().s("REMOVE_REGEX").build(),
                                    "regex" to AttributeValue.builder().s("-?\\d+").build()
                                )
                            ).build(),
                            AttributeValue.builder().m(
                                mapOf(
                                    "operator" to AttributeValue.builder().s("REMOVE_REGEX").build(),
                                    "regex" to AttributeValue.builder().s("\\s\\s").build()
                                )
                            ).build(),
                        )
                    ).build(),
                    "result" to AttributeValue.builder().s("some data").build(),
                )
            )
        )
    }
}
