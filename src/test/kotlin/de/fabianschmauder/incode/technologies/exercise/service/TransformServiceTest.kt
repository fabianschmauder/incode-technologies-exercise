package de.fabianschmauder.incode.technologies.exercise.service

import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformDataEntity
import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformDataRepository
import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformOperator
import de.fabianschmauder.incode.technologies.exercise.data.transform.Transformation
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

class TransformServiceTest {

    private val mockRepository = Mockito.mock(TransformDataRepository::class.java)
    private val transformService = TransformService(mockRepository)

    @Test
    fun testTransform() = runTest {
        val result = transformService.transform(
            "req-id", "some data 33", listOf(
                Transformation(
                    operator = TransformOperator.REMOVE_REGEX,
                    regex = "-?\\d+"
                )
            )
        )

        val entity = TransformDataEntity(
            requestId = "req-id",
            value = "some data 33",
            transformations = listOf(
                Transformation(
                    operator = TransformOperator.REMOVE_REGEX,
                    regex = "-?\\d+"
                )
            ),
            result = "some data "
        )
        assertThat(
            result, equalTo(
                entity
            )
        )

        verify(mockRepository).putItem(entity)
    }
}
