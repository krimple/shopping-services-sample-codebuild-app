#!/bin/bash

# This script deploys the Angular app to an S3 bucket for hosting

aws cloudformation deploy --stack-name ${1} --capabilities CAPABILITY_NAMED_IAM \
  --template-file ./backend/cloudformation.yml