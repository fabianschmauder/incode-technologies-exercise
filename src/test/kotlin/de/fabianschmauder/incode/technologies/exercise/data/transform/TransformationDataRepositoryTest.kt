package de.fabianschmauder.incode.technologies.exercise.data.transform

import de.fabianschmauder.incode.technologies.exercise.IntegrationTest
import kotlinx.coroutines.future.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class TransformationDataRepositoryTest : IntegrationTest() {

    @Autowired
    lateinit var repository: TransformDataRepository

    @Test
    fun saveTransformData() = runTest {
        val resultData = TransformDataEntity(
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
        repository.putItem(
            TransformDataEntity(
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
        ).await()
        // THEN
        val items = listTransformedItems().await()
        assertThat(items.count(), equalTo(1))
        assertThat(
            items.items()[0], equalTo(
                resultData.toEntity()
            )
        )
    }

}
