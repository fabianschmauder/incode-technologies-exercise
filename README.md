# Incode Technologies Exercise

## Description

Solves Task for Incode Technologies :

1. Build a web app using Spring or any other web framework.
2. Application should have an endpoint that accepts a collection of elements.
3. Each element should represent a string value and a list of transformers that will be applied to the value.
4. For simplicity, let’s make only one transformer available in the system:
    - Should accept regex as a parameter, find matches in the element value, and remove it from the original value.
5. Transformation results should be stored in the database.
6. Using any IaC tool of your choice, deploy this demo app to EC2/ECS in AWS VPC and make it accessible from the internet.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Deployment](#deployment)
- [Usage](#usage)
- [License](#license)

## Prerequisites

Before you begin, ensure you have met the following requirements:

- You have npm installed (comes with Node.js).
- You have the Java Development Kit (JDK) installed.
- You Deploy locally have the AWS CDK installed globally (`npm install -g aws-cdk`).

## Deployment

- The Pipeline automatically deploy the environment
  - ECR for docker image
  - Build kotlin application and push the application to the docker registry
  - create DynamoDB Table for data
  - setup ecs and runs service

- To setup pipeline you need
  - configure in the secrets
    - AWS_ACCOUNT_ID
    - AWS_REGION
    - AWS_ROLE_NAME 
  - configure OpenID Connect that GitHub can connect to your AWS account by creating an OIDC provider:
    ```bash
    aws iam create-open-id-connect-provider \
        --url https://token.actions.githubusercontent.com \
        --client-id-list sts.amazonaws.com \
        --thumbprint-list 6938fd4d98bab03faadb97b34396831e3780aea1
    ```

## Usage

You can access the loadbalancer and open the swagger doc under
 ```
$LoadBalancerUrl/swagger-ui/index.html
 ```

## License

This project is licensed under the [MIT License](LICENSE).
