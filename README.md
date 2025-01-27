# BrainQuest

[![License](https://img.shields.io/badge/License-GPL--v3.0-lightgrey)](https://github.com/rumpli/BrainQuest/blob/main/LICENSE)
![Code](https://img.shields.io/badge/Language-Java-brown)
![Code](https://img.shields.io/badge/Language-TypeScript-blue)
![Code](https://img.shields.io/badge/Language-JavaScript-yellow)
![Code](https://img.shields.io/badge/Language-Python-darkblue)
-----

Table of Contents
=================
- [BrainQuest](#brainquest)
  - [](#)
- [Table of Contents](#table-of-contents)
- [Intro](#intro)
  - [What is BrainQuest?](#what-is-brainquest)
  - [Available](#available)
  - [Contributors](#contributors)
- [Development environment setup](#development-environment-setup)
  - [Requirements](#requirements)
  - [Database](#database)
    - [Reset Database](#reset-database)
  - [Backend](#backend)
  - [Frontend](#frontend)
- [Docker Compose setup](#docker-compose-setup)
  - [Requirements](#requirements-1)
  - [Start Project](#start-project)
- [Build container image](#build-container-image)
-----

# Intro
Welcome to BrainQuest!
This is the further development of BrainQuest, based on the [FFHS-PA5 project](https://github.com/rumpli/FFHS-PA5).

## What is BrainQuest?
Lorem Ipsum

## Available
BrainQuest is available online in your webbrowser:
https://brainquest.netcreed.ch/

Or soon in the Google Play Store!

## Contributors

<!-- readme: collaborators,contributors,alexblaeuer,rumpli/- -start -->
<table>
	<tbody>
		<tr>
            <td align="center">
                <a href="https://github.com/alexblaeuer">
                    <img src="https://avatars.githubusercontent.com/u/11502742?v=4" width="100;" alt="alexblaeuer"/>
                    <br />
                    <sub><b>Alex Bläuer</b></sub>
                </a>
            </td>
		</tr>
	<tbody>
</table>
<!-- readme: collaborators,contributors,alexblaeuer,rumpli/- -end -->

**API Documentation**

https://api.brainquest.netcreed.ch/swagger-ui/index.html#/

# Development environment setup

## Requirements

- IntelliJ IDEA
- OpenJDK 23.0.1
- Gradle
- Docker
- Docker Compose
- Git

## Database

```bash
cd docker
docker compose up -d
```

### Reset Database

```bash
# stop backend
cd docker
docker compose down
# delete folder postgres in path ./data
docker compose up -d
# start backend
```

## Backend

1. Open project in IntelliJ
2. Load all Gradle projects
3. Start Backend by running/debugging `BrainQuestApplication` class
4. Create User/Change Password in Spring Shell

```bash
# create user
create-user

# change password
change-password
```

**API Documentation**

Swagger UI:

http://localhost:8080/swagger-ui/index.html#/

## Frontend

> [README Frontend](./frontend/README.md)

# Docker Compose setup

Host the application locally with Docker Compose.

**Play game**

http://localhost:5000/

**API Documentation**

http://localhost:8080/swagger-ui/index.html#/

## Requirements

- Docker
- Docker Compose

## Start Project

1. Create folder `brainquest` under desired path
2. Create file `docker-compose.yaml`

```yaml
services:
  db:
    image: postgres:16
    environment:
      POSTGRES_DB: brainquest
      POSTGRES_USER: brainquest
      POSTGRES_PASSWORD: brainquest
    restart: unless-stopped
    networks:
      - brainquest
    volumes:
      - ./data/postgres:/var/lib/postgresql/data

  backend:
    image: alexblaeuer/brainquest:1.0.0-amd64
    #image: alexblaeuer/brainquest:1.0.0-arm64
    depends_on:
      - db
    environment:
      APP_URL: http://localhost:8080
      FRONTEND_URL: http://localhost:5000
      DB_URL: db
      DB_PORT: 5432
      DB_NAME: brainquest
      DB_USERNAME: brainquest
      DB_PASSWORD: brainquest
    restart: unless-stopped
    networks:
      - brainquest
    ports:
      #- "80:8080"
      - "8080:8080"

  frontend:
    image: rumpli/brainquest-frontend:v1.0.2
    environment:
      API_URL: http://localhost:8080/api
    restart: unless-stopped
    networks:
      - brainquest
    ports:
      - "5000:5000"

networks:
  brainquest:
    driver: bridge
```
3. Start project with `docker compose up -d`


4. Create User/Change Password

```bash
# start spring shell
docker compose exec backend /bin/bash
# change port since default is already in use
java -DPORT=8081 -jar brainquest.jar

# create user
create-user

# change password
change-password
```

# Build container image

```bash
# amd64
docker build --platform linux/amd64 -t alexblaeuer/brainquest:<image_tag>-amd64 .
docker push alexblaeuer/brainquest:<image_tag>-amd64

# arm64
docker build --platform linux/arm64 -t alexblaeuer/brainquest:<image_tag>-arm64 .
docker push alexblaeuer/brainquest:<image_tag>-arm64
```
