package de.fabianschmauder.incode.technologies.exercise.service

import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformOperator
import de.fabianschmauder.incode.technologies.exercise.data.transform.Transformation
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test

class ApplyTransformationUtilsKtTest {

    @Test
    fun `apply two remove regex transformation should be applied to string`() {
        // GIVEN //WHEN
        val result = "Some 8 numbers 12".applyTransformations(
            listOf(
                Transformation(
                    operator = TransformOperator.REMOVE_REGEX,
                    regex = "-?\\d+"
                ),
                Transformation(
                    operator = TransformOperator.REMOVE_REGEX,
                    regex = "\\s"
                ),
            )
        )
        assertThat(result, equalTo("Somenumbers"))
    }
}
