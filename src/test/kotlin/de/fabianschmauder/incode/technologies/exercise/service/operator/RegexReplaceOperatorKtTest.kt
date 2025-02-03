package de.fabianschmauder.incode.technologies.exercise.service.operator

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test

class RegexReplaceOperatorKtTest {

    @Test
    fun shouldRemoveMatchingStrings() {
        val result = "Some 8 numbers 12 that -8 should be replaced".replaceRegex("-?\\d+")
        assertThat(result, equalTo("Some  numbers  that  should be replaced"))
    }

    @Test
    fun shouldKeepStringWhenNotMatching() {
        val result = "Some data".replaceRegex("-?\\d+")
        assertThat(result, equalTo("Some data"))
    }
}
