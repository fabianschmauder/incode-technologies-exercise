package de.fabianschmauder.incode.technologies.exercise.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Transformation API")
                    .description(
                        "Solves Task for Incode Technologies :\n" +
                                "1. Build a web app using Spring or any other web framework.\n" +
                                "2. Application should have an endpoint that accepts a collection of elements.\n" +
                                "3. Each element should represent a string value and list of transformers that will be\n" +
                                "applied to the value.\n" +
                                "4. For simplicity let’s make only one transformer is available in the system:\n" +
                                "   - should accept regex as parameter, find matches in element value and remove it from original value.\n" +
                                "5. Transformation results should be stored in the database\n" +
                                "6. Using any IaC tool of your choice deploy this demo app to EC2/ECS in AWS VPC and\n" +
                                "make it accessible from the internet."
                    )
                    .version("1.0")
                    .contact(
                        Contact()
                            .name("Fabian Schmauder")
                            .email("fabian.schmauder@gmail.com")
                    )
            )
    }
}
