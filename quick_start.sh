#!/usr/bin/env sh
docker rm --force mongo-dev;
docker run --rm --name mongo-dev -d \
  -e MONGO_INITDB_ROOT_USERNAME=user \
  -e MONGO_INITDB_ROOT_PASSWORD=pass \
  -e MONGO_INITDB_DATABASE=dev \
  -p 27017:27017 \
  -v "${PWD}/docker/mongo/volumes/init:/docker-entrypoint-initdb.d" \
  mongo:4.2 \
  && spring_profiles_active=dev ./gradlew bootRun