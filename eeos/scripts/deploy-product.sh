#!/bin/sh

set -ex

git fetch origin release
git reset --hard origin/release

./gradlew build -x test

sudo docker-compose -f docker-compose-prod.yml --env-file .env down

sudo docker-compose -f docker-compose-prod.yml --env-file .env up --build -d

