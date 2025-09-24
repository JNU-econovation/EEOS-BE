#!/bin/sh

set -ex

git fetch origin release
git reset --hard origin/release

./gradlew build -x test

sudo docker-compose -f docker-compose-prod.yml down

sudo docker-compose -f docker-compose-prod.yml up --build -d

