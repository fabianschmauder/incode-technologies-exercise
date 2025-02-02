package de.fabianschmauder.incode.technologies.exercise.service

import de.fabianschmauder.incode.technologies.exercise.data.transform.Transform
import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformDataEntity
import de.fabianschmauder.incode.technologies.exercise.data.transform.TransformDataRepository
import org.springframework.stereotype.Service

@Service
class TransformService(private val transformDataRepository: TransformDataRepository) {
    suspend fun transform(requestId: String, value: String, transforms: List<Transform>) = transformDataRepository.putItem(
        TransformDataEntity(
            requestId = requestId,
            value = value,
            transforms = transforms,
            result = value.applyTransformations(transforms)
        )
    )
}
