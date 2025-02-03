package de.fabianschmauder.incode.technologies.exercise.controller

import de.fabianschmauder.incode.technologies.exercise.IntegrationTest
import de.fabianschmauder.incode.technologies.exercise.controller.dto.TransformDataDto
import de.fabianschmauder.incode.technologies.exercise.data.transform.Transformation
import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformDataEntity
import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformOperator
import kotlinx.coroutines.future.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.http.HttpStatus


class TransformationControllerIntegrationTest : IntegrationTest() {

    @Test
    fun `transform by remove all numbers from string`() = runTest {
        // GIVEN
        given(idUtils.generateId()).willReturn("some-id")

        val transformationData = TransformDataDto(
            value = "some -42 20 data",
            transformer = listOf(
                Transformation(
                    operator = TransformOperator.REMOVE_REGEX,
                    regex = "-?\\d+"
                ),
                Transformation(
                    operator = TransformOperator.REMOVE_REGEX,
                    regex = "\\s\\s"
                ),
            )
        )

        // WHEN
        val response = restTemplate.postForEntity(
            "/transform", transformationData, TransformDataEntity::class.java
        )

        //THEN
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        val resultDataDto = TransformDataEntity(
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
        assertThat(
            response.body, equalTo(
                resultDataDto
            )
        )

        waitForTransformedDbItemCount(1)
        val items = listTransformedItems().await()
        assertThat(items.count(), equalTo(1))
        assertThat(
            items.items()[0], equalTo(
                resultDataDto.toEntity()
            )
        )
    }


    @Test
    fun `invalid regex returns bad request`() = runTest {
        // GIVEN
        given(idUtils.generateId()).willReturn("some-id")

        val transformationData = TransformDataDto(
            value = "some aa data",
            transformer = listOf(
                Transformation(
                    operator = TransformOperator.REMOVE_REGEX,
                    regex = "a**"
                )
            )
        )

        // WHEN
        val response = restTemplate.postForEntity(
            "/transform", transformationData, String::class.java
        )

        //THEN
        assertThat(response.statusCode, equalTo(HttpStatus.BAD_REQUEST))

        val items = listTransformedItems().await()

        assertThat(items.count(), equalTo(0))
    }


    @Test
    fun `empty request returns blank string`() = runTest {
        // GIVEN
        given(idUtils.generateId()).willReturn("some-id")

        val transformationData = TransformDataDto()

        // WHEN
        val response = restTemplate.postForEntity(
            "/transform", transformationData, TransformDataEntity::class.java
        )

        //THEN
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        val resultDataDto = TransformDataEntity(
            requestId = "some-id",
            value = "",
            result = "",
            transformations = listOf(
            )
        )
        assertThat(
            response.body, equalTo(
                resultDataDto
            )
        )

        waitForTransformedDbItemCount(1)

        val items = listTransformedItems().await()
        assertThat(items.count(), equalTo(1))
        assertThat(
            items.items()[0], equalTo(
                resultDataDto.toEntity()
            )
        )
    }
}
