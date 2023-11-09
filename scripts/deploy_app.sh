#!/bin/bash

set -ex


if [ "$#" -eq 2 ]; then
    ENV_FILE="$1"
    OPERATION="$2"
    source "$ENV_FILE" 2>/dev/null || echo '[ERROR] Something wrong with the environment file!'
else
    echo -e "Usage: $0 <PATH_TO_ENV_FILE> <OPERATION>\n"
    echo 'Specify one of the next OPERATION:
        run-stage: create/restart all application resources for Stage environment
        run-dev: create/restart all application containers for Dev environment
        prune-stage: remove all related application resources for Stage envirionment
        prune-dev: remove all related application resources for Dev envirionment'
    exit 1
fi

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
DOCKER_CONTEXT=$(dirname "$SCRIPT_DIR")


## Stage commands

STAGE_POSTGRES_RUN_COMMAND="docker run -d
    --name postgres
    --hostname postgres
    --restart always
    --env-file $ENV_FILE
    -v $POSTGRES_ENTRYPOINT_DIR:/docker-entrypoint-initdb.d
    -v postgres:/var/lib/postgresql/data
    --network backend
    --expose 5432
    postgres:15.1-alpine"

STAGE_MONGO_RUN_COMMAND="docker run -d
    --name mongo
    --restart always
    --hostname mongo
    --env-file $ENV_FILE
    --network backend
    --expose 27017
    -v mongo:/data/db
    mongo:jammy"

STAGE_REDIS_RUN_COMMAND="docker run -d
    --name redis
    --restart always
    --hostname redis
    --expose 6379
    -v redis:/root/redis
    --env-file $ENV_FILE
    --network backend
    redis:alpine"

STAGE_SCHEDULE_RUN_COMMAND="docker run -d
    --name schedule-app
    --restart always
    --env-file $ENV_FILE
    --network backend
    -p $SCHEDULE_APP_PORT:8080
    $SCHEDULE_APP_IMAGE"

STAGE_SCHEDULE_TEST_RUN_COMMAND="docker run -d
    --name schedule-app-test
    --restart no
    --env-file $ENV_FILE
    --network backend
    $SCHEDULE_TEST_APP_IMAGE"


## Dev build commands

DEV_SCHEDULE_TEST_BUILD_COMMAND="docker build
    --target backend-test
    -t schedule-backend-test-dev
    $DOCKER_CONTEXT"

DEV_SCHEDULE_BUILD_COMMAND="docker build
    --target backend-tomcat-dev
    -t schedule-backend-dev
    $DOCKER_CONTEXT"

DEV_FRONTEND_BUILD_COMMAND="docker build
    --target frontend-dev
    -t schedule-frontend-dev
    $DOCKER_CONTEXT"


## Dev run commands

DEV_SCHEDULE_TEST_RUN_COMMAND="docker run -d
    --name schedule-backend-test-dev
    --env-file $ENV_FILE
    schedule-backend-test-dev"

DEV_SCHEDULE_RUN_COMMAND="docker run -d
    --name schedule-backend-dev
    --env-file $ENV_FILE
    -p $APP_PORT:8080
    schedule-backend-dev"

DEV_FRONTEND_RUN_COMMAND="docker run -d
    --name schedule-frontend-dev
    --env-file $ENV_FILE
    -p 3000:3000
    schedule-frontend-dev"


stage_create_network() {
    network_name="$1"

    if docker network ls | awk '{print $2}' | grep -q "^${network_name}$"; then
        echo "Network '$network_name' already exists"
    else
        echo "Network '$network_name' will be created..."
        docker network create "$network_name" >/dev/null
    fi
}

misc_run_container() {
    container_name="$1"
    run_command="$2"

    if [ -n "$(docker ps -q -f name="^${container_name}$")" ]; then
        echo "Container '$container_name' is already running"
        return 0
    fi

    if [ -n "$(docker ps -a -q -f name="^${container_name}$")" ]; then
        echo "Container '$container_name' already exists. Restarting it..."
        docker restart "$container_name" >/dev/null
    else
        echo "Container '$container_name' will be created..."
        $run_command >/dev/null
    fi
}

misc_prune() {
    containers="$1"
    networks="$2"
    volumes="$3"

    docker rm -f $containers &>/dev/null || true
    docker network remove -f $networks &>/dev/null || true
    docker volume remove -f $volumes &>/dev/null || true

    echo 'All Schedule containers, networks and volumes were successfully deleted'
}


case "$OPERATION" in
    run-stage)
        stage_create_network 'backend'
        misc_run_container 'postgres' "$STAGE_POSTGRES_RUN_COMMAND"
        misc_run_container 'mongo' "$STAGE_MONGO_RUN_COMMAND"
        misc_run_container 'redis' "$STAGE_REDIS_RUN_COMMAND"
        misc_run_container 'schedule-app-test' "$STAGE_SCHEDULE_TEST_RUN_COMMAND"
        misc_run_container 'schedule-app' "$STAGE_SCHEDULE_RUN_COMMAND"
        ;;
    run-dev)
        $DEV_SCHEDULE_TEST_BUILD_COMMAND
        $DEV_SCHEDULE_BUILD_COMMAND
        $DEV_FRONTEND_BUILD_COMMAND
        misc_run_container 'schedule-backend-test-dev' "$DEV_SCHEDULE_TEST_RUN_COMMAND"
        misc_run_container 'schedule-backend-dev' "$DEV_SCHEDULE_RUN_COMMAND"
        misc_run_container 'schedule-frontend-dev' "$DEV_FRONTEND_RUN_COMMAND"
        ;;
    prune-stage)
        misc_prune 'schedule-app schedule-app-test redis mongo postgres' \
            'backend' \
            'redis mongo postgres'
        ;;
    prune-dev)
        misc_prune 'schedule-frontend-dev schedule-backend-dev schedule-backend-test-dev'  \
            '' \
            ''
        ;;
    *)
        echo 'Invalid operation!'
        ;;
esac
