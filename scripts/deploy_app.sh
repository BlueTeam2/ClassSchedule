#!/bin/bash

set -e


POSTGRES_COMMAND="docker run -d
  --name postgres
  --hostname postgres
  --restart always
  --env-file $ENV_FILE
  -v $POSTGRES_ENTRYPOINT_DIR:/docker-entrypoint-initdb.d
  -v postgres:/var/lib/postgresql/data
  --network backend
  --expose 5432
  postgres:15.1-alpine"

MONGO_COMMAND="docker run -d
  --name mongo
  --restart always
  --hostname mongo
  --env-file $ENV_FILE
  --network backend
  --expose 27017
  -v mongo:/data/db
  mongo:jammy"

REDIS_COMMAND="docker run -d
  --name redis
  --restart always
  --hostname redis
  --expose 6379
  -v redis:/root/redis
  --env-file $ENV_FILE
  --network backend
  redis:alpine"

SCHEDULE_APP_COMMAND="docker run -d
  --name schedule-app
  --restart always
  --env-file $ENV_FILE
  --network backend
  -p $APP_PORT:8080
  $APP_IMAGE"

SCHEDULE_APP_TEST_COMMAND="docker run -d
  --name schedule-app-test
  --restart always
  --env-file $ENV_FILE
  --network backend
  $APP_TEST_IMAGE"


create_network() {
    network_name="$1"
    
    if docker network ls | awk '{print $2}' | grep -q "$network_name"; then
        echo "Network '$network_name' already exists"
    else
        echo "Network '$network_name' will be created..."
        docker network create "$network_name" > /dev/null
    fi
}

run_container() {
    container_name="$1"
    run_command="$2"

    if [ -n "$(docker ps -q -f name="$container_name")" ]; then
        echo "Container '$container_name' is already running"
        return 0
    fi

    if [ -n "$(docker ps -a -q -f name="$container_name")" ]; then
        echo "Container '$container_name' already exists. Restarting it..."
        docker restart "$container_name" > /dev/null
    else
        echo "Container '$container_name' will be created..."
        $run_command > /dev/null
    fi
}

prune() {
    containers="$1"
    networks="$2"
    volumes="$3"

    docker rm -f $containers &> /dev/null || true
    docker network remove -f $networks &> /dev/null || true
    docker volume remove -f $volumes &> /dev/null || true

    echo 'All Schedule containers, networks and volumes were successfully deleted'
}


OPERATION="$1"

if [ "$OPERATION" == 'run' ]; then
  create_network 'backend'
  run_container 'postgres' "$POSTGRES_COMMAND"
  run_container 'mongo' "$MONGO_COMMAND"
  run_container 'redis' "$REDIS_COMMAND"
  run_container 'schedule-app' "$SCHEDULE_APP_COMMAND"
  run_container 'schedule-app-test' "$SCHEDULE_APP_TEST_COMMAND"

elif [ "$OPERATION" == 'prune' ]; then
  prune 'schedule-app schedule-app-test redis mongo postgres' 'backend' 'redis mongo postgres' 
  
else
    echo 'Specify one of the next options:
    run: create/restart all application resources
    prune: remove all related application resources'
fi
