package de.fabianschmauder.incode.technologies.exercise.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.regions.Region

@Configuration
class DynamoDbConfig(private val dbProperties: DynamoDbProperties) {
    @Bean
    fun dynamoDbAsyncClient(): DynamoDbAsyncClient {
        return DynamoDbAsyncClient.builder()
            .region(Region.of(dbProperties.region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .httpClientBuilder(NettyNioAsyncHttpClient.builder())
            .build()
    }

}
