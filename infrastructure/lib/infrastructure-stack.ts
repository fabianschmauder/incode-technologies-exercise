import * as cdk from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {Vpc} from "aws-cdk-lib/aws-ec2";
import * as ecr from 'aws-cdk-lib/aws-ecr';
import * as ec2 from 'aws-cdk-lib/aws-ec2';
import * as ecs from 'aws-cdk-lib/aws-ecs';
import * as ecsPatterns from 'aws-cdk-lib/aws-ecs-patterns';

export class InfrastructureStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {
        super(scope, id, props);
        const vpc = new Vpc(this, "IncodeVpc", {maxAzs: 3});

        const cluster = new ecs.Cluster(this, 'IncodeCluster', {
            vpc: vpc,
        });

        cluster.addCapacity('BaseAutoScalingGroup', {
            instanceType: new ec2.InstanceType('t2.micro'),
            minCapacity: 1,
            maxCapacity: 3,
        });


        const repository = ecr.Repository.fromRepositoryName(this, 'IncodeExerciseRepo', 'incode-exercise');

        let service = new ecsPatterns.ApplicationLoadBalancedEc2Service(this, 'IncodeExerciseService', {
            cluster,
            memoryLimitMiB: 512,
            desiredCount: 1,
            taskImageOptions: {
                image: ecs.ContainerImage.fromEcrRepository(repository, 'latest'),
                containerPort: 8080,
            },
            publicLoadBalancer: true,
        });
        service.targetGroup.healthCheck = {
            path: '/actuator/health'
        }
    }
}
