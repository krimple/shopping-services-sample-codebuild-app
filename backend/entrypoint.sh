#!/bin/bash

ENV_FILE="./.env"

# Build env vars if an '.env' is present (for local development)
# when this file is not present, we are building in AWS so we
# are getting our environment from the container management system (ECS)
# via the ECS task
if [ -f $ENV_FILE ]; then
  tmpfile=$(mktemp)
  grep '=' ./.env | sed  '/#/d' | sed '/^$/d' | sed -e 's/^/export &/' > $tmpfile
  source $tmpfile
  rm $tmpfile
fi

java \
  -Xms48m \
  -Xmx256m \
  -jar ./shopping-cart.jar


