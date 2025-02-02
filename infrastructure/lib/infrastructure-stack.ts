import * as cdk from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {Vpc} from "aws-cdk-lib/aws-ec2";
import * as ecr from 'aws-cdk-lib/aws-ecr';
import * as ec2 from 'aws-cdk-lib/aws-ec2';
import * as ecs from 'aws-cdk-lib/aws-ecs';
import * as ecsPatterns from 'aws-cdk-lib/aws-ecs-patterns';
import * as logs from 'aws-cdk-lib/aws-logs';
import * as dynamodb from 'aws-cdk-lib/aws-dynamodb';

export class InfrastructureStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {
        super(scope, id, props);

        new dynamodb.Table(this, 'TransformationTable', {
            tableName: 'transformed-data',
            partitionKey: {
                name: 'requestId',
                type: dynamodb.AttributeType.STRING
            },
            billingMode: dynamodb.BillingMode.PAY_PER_REQUEST,
            removalPolicy: cdk.RemovalPolicy.DESTROY,
        });

        const vpc = new Vpc(this, "IncodeVpc", {maxAzs: 3});

        vpc.addGatewayEndpoint('DynamoDbEndpoint', {
            service: ec2.GatewayVpcEndpointAwsService.DYNAMODB,
        });

        const cluster = new ecs.Cluster(this, 'IncodeCluster', {
            vpc: vpc,
        });

        cluster.addCapacity('BaseAutoScalingGroup', {
            instanceType: new ec2.InstanceType('t2.micro'),
            minCapacity: 1,
            maxCapacity: 3,
        });


        const repository = ecr.Repository.fromRepositoryName(this, 'IncodeExerciseRepo', 'incode-exercise');

        const service = new ecsPatterns.ApplicationLoadBalancedEc2Service(this, 'IncodeExerciseService', {
            cluster,
            memoryLimitMiB: 512,
            desiredCount: 1,
            taskImageOptions: {
                image: ecs.ContainerImage.fromEcrRepository(repository, 'latest'),
                containerPort: 8080,
                logDriver: ecs.LogDrivers.awsLogs({
                    streamPrefix: 'IncodeExerciseService',
                    logRetention: logs.RetentionDays.ONE_WEEK,
                }),
            },
            publicLoadBalancer: true,
        });
        service.targetGroup.healthCheck = {
            path: '/actuator/health'
        }
    }
}
