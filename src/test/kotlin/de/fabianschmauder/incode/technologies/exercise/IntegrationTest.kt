package de.fabianschmauder.incode.technologies.exercise

import de.fabianschmauder.incode.technologies.exercise.data.IdUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTest {
    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @MockitoBean
    lateinit var idUtils: IdUtils

    @AfterEach
    fun cleanUp() = runTest {
        listTransformedItems().await().items().forEach { item ->
            dynamoDbClient!!.deleteItem(
                DeleteItemRequest.builder()
                    .tableName(TRANSFORMED_DATA_TABLE_NAME)
                    .key(
                        mapOf(
                            "requestId" to item["requestId"]!!
                        )
                    )
                    .build()
            )
        }
    }

    suspend fun listTransformedItems(): CompletableFuture<ScanResponse> {
        return dynamoDbClient!!.scan(ScanRequest.builder().tableName(TRANSFORMED_DATA_TABLE_NAME).build())
    }

    fun waitForTransformedDbItemCount(count: Int) {
        var itemsCount = 0
        runBlocking {
            var runs = 0
            while (itemsCount != count && runs < 30) {
                delay(100)
                listTransformedItems().thenApply {
                    itemsCount = it.count()
                }
                runs++
            }
        }
        await().atMost(3, TimeUnit.SECONDS).until { itemsCount == count }
    }

    companion object {
        private val LOCALSTACK_CONTAINER: LocalStackContainer = LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
            .withServices(LocalStackContainer.Service.DYNAMODB)

        var dynamoDbClient: DynamoDbAsyncClient? = null
        const val TRANSFORMED_DATA_TABLE_NAME = "transformed-data"

        @BeforeAll
        @JvmStatic
        fun startServer() {
            if (!LOCALSTACK_CONTAINER.isRunning)
                LOCALSTACK_CONTAINER.start()
            val endpoint = LOCALSTACK_CONTAINER.getEndpointOverride(LocalStackContainer.Service.DYNAMODB)

            System.setProperty("aws.accessKeyId", LOCALSTACK_CONTAINER.accessKey)
            System.setProperty("aws.secretAccessKey", LOCALSTACK_CONTAINER.secretKey)
            System.setProperty("dynamodb.region", LOCALSTACK_CONTAINER.region)
            System.setProperty("dynamodb.endpoint", endpoint.toString())

            dynamoDbClient = DynamoDbAsyncClient.builder()
                .region(Region.of(LOCALSTACK_CONTAINER.region))
                .endpointOverride(endpoint)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .httpClientBuilder(NettyNioAsyncHttpClient.builder())
                .build()

            val tables = dynamoDbClient!!.listTables().join()
            if (!tables.tableNames().contains(TRANSFORMED_DATA_TABLE_NAME)) {
                dynamoDbClient!!.createTable(
                    CreateTableRequest.builder()
                        .tableName(TRANSFORMED_DATA_TABLE_NAME)
                        .billingMode(BillingMode.PAY_PER_REQUEST)
                        .keySchema(
                            KeySchemaElement.builder()
                                .attributeName("requestId").keyType(KeyType.HASH)
                                .build()
                        )
                        .attributeDefinitions(
                            AttributeDefinition.builder()
                                .attributeName("requestId")
                                .attributeType(ScalarAttributeType.S)
                                .build()
                        )
                        .build()
                ).join()
            }
        }
    }
}
