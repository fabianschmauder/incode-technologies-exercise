package de.fabianschmauder.incode.technologies.exercise.data

import org.springframework.stereotype.Service
import java.util.*

@Service
class IdUtils {

    fun generateId(): String = UUID.randomUUID().toString()
}
