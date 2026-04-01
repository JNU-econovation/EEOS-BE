#!/bin/sh

set -ex

git fetch origin develop
git reset --hard origin/develop

./gradlew build -x test

sudo docker-compose -f docker-compose-dev.yml --env-file .env down

sudo docker-compose -f docker-compose-dev.yml --env-file .env up --build -d

