# Class schedule
## General info
This repository contains the Class Schedule Project made by Blue Team.

## Diagram 

<img src="./java_web_app.png" alt= “” width="476px" height="574px">

## Application Stack

- ReactJS
- Java (11)
- Gradle (7)
- Tomcat (9)
- PostgreSQL (15)
- MongoDB:latest
- Redis:latest
- Docker:latest

# Install Vagrant on your system:

```wget https://releases.hashicorp.com/vagrant/2.2.19/vagrant_2.2.19_x86_64.deb```

```sudo apt install ./vagrant_2.2.19_x86_64.deb```

Verify Vagrant Installation and Install Vagrant Plugin

Check if Vagrant installed succesfully

`vagrant --version`

Additionally, install the vagrant-docker-compose plugin:

`vagrant plugin install vagrant-docker-compose`

# Create .env.stage and .env.prod
## .env.stage
```
#Postgres configuration
POSTGRES_URL=jdbc:postgresql://postgres:5432/YOUR_POSTGRES_DATABASE
POSTGRES_DATABASE=YOUR_POSTGRES_DATABASE

POSTGRES_TEST_URL=jdbc:postgresql://postgres:5432/YOUR_POSTGRES_TEST_DATABASE
POSTGRES_TEST_DATABASE=YOUR_POSTGRES_TEST_DATABASE

POSTGRES_ADMIN_USERNAME=YOUR_POSTGRES_ADMIN_USERNAME
POSTGRES_ADMIN_PASSWORD=YOUR_POSTGRES_ADMIN_PASSWORD

POSTGRES_USER=YOUR_USER
POSTGRES_PASSWORD=YOUR_POSTGRES_PASSWORD

# Redis configuration
REDIS_URL=redis://redis:6379
REDIS_PORT=6379
REDIS_DATABASES=16

# Mongo configuration
MONGO_DATABASE=YOUR_MONGO_DATABASE
MONGO_URL=mongo
MONGO_INITDB_ROOT_USERNAME=YOUR_MONGO_INITDB_ROOT_USERNAME
MONGO_INITDB_ROOT_PASSWORD=YOUR_MONGO_INITDB_ROOT_PASSWORD

# JWT configuration
JWT_TOKEN=YOUR_JWT_TOKEN
JWT_EXPIRED=YOUR_JWT_EXPIRED
```
## .env.prod
```
# Postgres configuration
POSTGRES_URL=jdbc:postgresql://postgres:5432/YOUR_POSTGRES_DB
POSTGRES_USER=YOUR_POSTGRES_USER
POSTGRES_PASSWORD=YOUR_POSTGRES_PASSWORD
POSTGRES_DB=YOUR_POSTGRES_DB

# Redis configuration
REDIS_URL=redis://redis:6379
REDIS_PORT=6379
REDIS_DATABASES=16

# Mongo configuration
MONGO_DATABASE=YOUR_MONGO_DATABASE
MONGO_URL=mongo
MONGO_INITDB_ROOT_USERNAME=YOUR_MONGO_INITDB_ROOT_USERNAME
MONGO_INITDB_ROOT_PASSWORD=YOUR_MONGO_INITDB_ROOT_PASSWORD

# JWT configuration
JWT_TOKEN=YOUR_JWT_TOKEN
JWT_EXPIRED=YOUR_JWT_EXPIRED

```
## Dump for DB

You need initial_data.dump for DB in environments folder!!!

# Starting the App
To start the application in either a staging (stage) or production (prod) environment, follow these steps:

Launch the application using Vagrant:

## For the staging environment:

`vagrant up stage-compose`
## For the production environment:

`vagrant up prod`
