# SIE496-Cloud-native-Mircoservices E-commerce System
This repo is for SIE496 Cloud-native Mircoservices project

## Setup

1. install docker application (execute once)

    Follow the [link](https://docs.docker.com/desktop/install/mac-install/) and install docker

2. install docker-compose (execute once)

    If you install docker, you can execute "docker-compose".
    
    Detail [link](https://docs.docker.com/compose/install/)

3. install Java Development Kit

    Follow the [link](https://www.oracle.com/java/technologies/downloads/) and install JDK

4. install Maven 

    ``` shell
    brew install maven
    ```

## Start Local Testing

1. Run setup.sh file to automate maven and docker image build process
    ```shell
    ./setup.sh
    ```

2. cd to docker folder
    ```shell
    cd docker
    ```

3. execute docker compose up
    ```shell
    docker-compose up --build
    ```


The application should run on your local environment now