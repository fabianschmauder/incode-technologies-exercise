import * as cdk from 'aws-cdk-lib';
import {Construct} from 'constructs';
import * as ecr from 'aws-cdk-lib/aws-ecr';

export class IncodeExerciseECRStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {
        super(scope, id, props);
        new ecr.Repository(this, 'Docker registry', {
            repositoryName: 'incode-exercise',
            removalPolicy: cdk.RemovalPolicy.DESTROY,
            lifecycleRules: [
                { maxImageCount: 10 }
            ]
        });
    }
}
