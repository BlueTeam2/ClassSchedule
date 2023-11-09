#!/bin/bash
echo "Bash script is working"
if [ -n "$POSTGRES_TEST_DATABASE" ]; then

psql -v ON_ERROR_STOP=1 -U "$POSTGRES_USER" << EOSQL
    CREATE USER $POSTGRES_ADMIN WITH PASSWORD '$POSTGRES_ADMIN_PASSWORD';
    CREATE DATABASE $POSTGRES_DATABASE OWNER $POSTGRES_ADMIN;
    CREATE DATABASE $POSTGRES_TEST_DATABASE OWNER $POSTGRES_ADMIN;
EOSQL

psql --set ON_ERROR_STOP=off -U "$POSTGRES_ADMIN" -d "$POSTGRES_DATABASE" -f "/docker-entrypoint-initdb.d/initial_data.dump"

else
psql -v ON_ERROR_STOP=1 -U "$POSTGRES_USER" << EOSQL
    CREATE USER $POSTGRES_ADMIN WITH PASSWORD '$POSTGRES_ADMIN_PASSWORD';
    CREATE DATABASE $POSTGRES_DATABASE OWNER $POSTGRES_ADMIN;
EOSQL
fi
