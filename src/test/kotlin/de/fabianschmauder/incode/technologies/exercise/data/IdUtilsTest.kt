package de.fabianschmauder.incode.technologies.exercise.data

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test

class IdUtilsTest {

    val idUtils: IdUtils = IdUtils()

    @Test
    fun generateARandomUUID() {
        val firstId = idUtils.generateId()
        val secondId = idUtils.generateId()
        assertThat(firstId, not(equalTo(secondId)))
    }
}
