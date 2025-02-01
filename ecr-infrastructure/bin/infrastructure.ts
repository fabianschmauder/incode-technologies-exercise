#!/usr/bin/env node
import * as cdk from 'aws-cdk-lib';
import { IncodeExerciseECRStack } from '../lib/infrastructure-stack';

const app = new cdk.App();

new IncodeExerciseECRStack(app, 'IncodeExerciseECRStack', {});
