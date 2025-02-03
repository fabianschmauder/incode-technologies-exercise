package de.fabianschmauder.incode.technologies.exercise.data.transform

import de.fabianschmauder.incode.technologies.exercise.config.DynamoDbProperties
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse
import java.util.concurrent.CompletableFuture

@Repository
class TransformDataRepository(
    private val dbProperties: DynamoDbProperties,
    private val client: DynamoDbAsyncClient
) {

    suspend fun putItem(item: TransformDataEntity): CompletableFuture<PutItemResponse> = client.putItem(
        PutItemRequest.builder()
            .tableName(dbProperties.transformedTableName)
            .item(item.toEntity())
            .build()
    )
}
