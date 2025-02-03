import * as cdk from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {Vpc} from "aws-cdk-lib/aws-ec2";
import * as ecr from 'aws-cdk-lib/aws-ecr';
import * as ec2 from 'aws-cdk-lib/aws-ec2';
import * as ecs from 'aws-cdk-lib/aws-ecs';
import * as ecsPatterns from 'aws-cdk-lib/aws-ecs-patterns';
import * as logs from 'aws-cdk-lib/aws-logs';
import * as dynamodb from 'aws-cdk-lib/aws-dynamodb';
import * as iam from 'aws-cdk-lib/aws-iam';

export class InfrastructureStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {
        super(scope, id, props);

        const table = new dynamodb.Table(this, 'TransformationTable', {
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
            minCapacity: 2,
            maxCapacity: 3,
        });


        const repository = ecr.Repository.fromRepositoryName(this, 'IncodeExerciseRepo', 'incode-exercise');

        const serviceRole = new iam.Role(this, 'IncodeExerciseServiceRole', {
            assumedBy: new iam.ServicePrincipal('ecs-tasks.amazonaws.com'),
        });

        serviceRole.addToPolicy(new iam.PolicyStatement({
            actions: ['dynamodb:PutItem'],
            resources: [table.tableArn],
            effect: iam.Effect.ALLOW,
        }));

        const imageTag = this.node.tryGetContext("IMAGE_TAG") || "latest";
        const service = new ecsPatterns.ApplicationLoadBalancedEc2Service(this, 'IncodeExerciseService', {
            cluster,
            memoryLimitMiB: 512,
            cpu: 256,
            desiredCount: 2,
            taskImageOptions: {
                image: ecs.ContainerImage.fromEcrRepository(repository, imageTag),
                containerPort: 8080,
                taskRole: serviceRole,
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

        new cdk.CfnOutput(this, 'LoadBalancerURL', {
            value: `http://${service.loadBalancer.loadBalancerDnsName}`,
        });
    }
}
