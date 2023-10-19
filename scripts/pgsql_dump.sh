#!/bin/sh

#change on your path to PostgreSQL
pathA=/c/Program\ Files/PostgreSQL/13/bin
export PATH=$PATH:$pathA

#write your password
PGPASSWORD=
export PGPASSWORD

#change to the path where the dump will be saved
pathB=../backup
#write your user
dbUser=
#write your database
database=
#write your host
host=
#write your port
port=

pg_dump --no-owner -U $dbUser -h $host -p $port -d $database > $pathB/pgsql_$(date "+%Y-%m-%d-%H-%M").dump

unset PGPASSWORD


pg_dump --no-owner -U postgres -d schedule > 2023-09-01.dump
