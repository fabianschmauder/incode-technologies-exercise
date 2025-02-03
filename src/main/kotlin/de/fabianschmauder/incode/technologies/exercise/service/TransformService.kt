package de.fabianschmauder.incode.technologies.exercise.service

import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformDataEntity
import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformDataRepository
import de.fabianschmauder.incode.technologies.exercise.data.transform.Transformation
import org.springframework.stereotype.Service

@Service
class TransformService(private val transformDataRepository: TransformDataRepository) {
    suspend fun transform(requestId: String, value: String, transformations: List<Transformation>): TransformDataEntity {
        val entity = TransformDataEntity(
            requestId = requestId,
            value = value,
            transformations = transformations,
            result = value.applyTransformations(transformations)
        )
        return transformDataRepository.putItem(
            entity
        ).let { entity }
    }
}
