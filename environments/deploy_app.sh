#/bin/bash

docker network create backend

docker network create backend

docker run -d \
  --name postgres-15.1 \
  --hostname postgres \
  --restart always \
  --env-file "$ENV_FILE" \
  -v "$POSTGRES_ENTRYPOINT_DIR:/docker-entrypoint-initdb.d" \
  -v postgres:/var/lib/postgresql/data \
  --network backend \
  --expose 5432 \
  postgres:15.1-alpine

docker run -d \
  --name mongo \
  --restart always \
  --hostname mongo \
  --env-file "$ENV_FILE" \
  --network backend \
  --expose 27017 \
  -v mongo:/data/db \
  mongo:jammy

docker run -d \
  --name redis \
  --restart always \
  --hostname redis \
  --expose 6379 \
  -v redis:/root/redis \
  --env-file "$ENV_FILE" \
  --network backend \
  redis:alpine

docker run -d \
  --name schedule-app \
  --restart always \
  --env-file "$ENV_FILE" \
  --network backend \
  -p "$APP_PORT:8080" \
  "$APP_IMAGE"
  