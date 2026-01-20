# java-maven-app

A simple Java application built with **Apache Maven**, with a **Jenkins Pipeline** (`Jenkinsfile`) included for CI/CD automation. Actual project was deployed on AWS platform with docker. Install docker on EC2 instance. 

Install Jenkins on the instance with the following command:
```bash
docker run -d -v jenkins_home:/var/jenkins_home -p 8080:8080 -p 50000:50000 --restart=on-failure jenkins/jenkins:lts-jdk21
#instructions to run docker commands from jenkins container is further down below
```

Configure Maven (as a tool via Jenkins UI).

Also, install Nodejs (inside the jenkins container - as root user) with the following command:
docker exec -it -u 0 #JenkinsContainerID /bin/bash
```
curl -sL https://deb.nodesource.com/setup_20.x -o nodesource_setup.sh

sh nodesource_setup.sh
```

This repo contains a standard Maven project layout (`src/main/...`) plus build configuration (`pom.xml`) and a Jenkins pipeline definition (`Jenkinsfile`). 

---

## Project Structure

Typical layout:

- `pom.xml` — Maven build + dependencies  
- `Jenkinsfile` — Jenkins pipeline for building/testing the app  
- `src/main/...` — application source code 

> Note: The default branch shown in the repo UI is `feature/payment`. 

---

## Prerequisites

- Java (JDK) installed (commonly 11/17/21 depending on your `pom.xml`)
- Maven (`mvn`) installed  
- (Optional) Jenkins if you want to run the pipeline

---

## Build and Test (Local)

From the repo root:

```bash
mvn clean test

mvn clean package
# Maven uses this command to generate artifacts in the target/. folder.
```

---

## Making Docker installation from AWS machine available in Jenkins container for jobs
From the AWS machine run the follwing command:
```
docker run -p 8080:8080 -p 50000:50000 -d \
-v jenkins_home:/var/jenkins_home \
-v /var/run/docker.sock:/var/run/docker.sock jenkins/jenkins:lts-jdk21
```

From inside the jenkins container (as root user - "-u 0", run the following commands:
```
groupadd docker
usermod -aG docker jenkins
apt-get update && apt-get install -y docker.io #to install docker cli, so you can talk to docker daemon
exit
```

If you restart the jenkins container and run into the error below:
# Jenkins Docker Socket Permission Error – Quick Fix Guide

## Error

```
permission denied while trying to connect to the Docker daemon socket
```

when running Docker commands from Jenkins (running inside a container).

---

## Cause (1 sentence)

The `docker` group inside the Jenkins container has a **different numeric GID** than the `docker` group on the host that owns `/var/run/docker.sock`.

Linux checks **numbers, not group names**.

---

## Verify

### On host

```bash
getent group docker
ls -l /var/run/docker.sock
```

### Inside container

```bash
getent group docker
ls -l /var/run/docker.sock
groups jenkins
```

If the GIDs differ → this is the problem.

---

## Fix (production‑safe)

### 1. Get host docker GID

```bash
DOCKER_GID=$(getent group docker | cut -d: -f3)
echo $DOCKER_GID
#Please note the above value as you will need it in the next step
```

### 2. Fix inside Jenkins container (as root)

```bash
groupdel docker
groupadd -g $DOCKER_GID docker #replace $DOCKER_GID with the value obtained above
usermod -aG docker jenkins
```

### 3. Restart container

```bash
docker restart <jenkins_container>
```

### 4. Test

```bash
docker exec -it <jenkins_container> bash
su - jenkins
docker ps
```

---

## Rule to remember

> When mounting `/var/run/docker.sock` into a container, always match the container’s `docker` group GID to the host’s `docker` group GID.

Never hardcode the number.

---

## Notes

* Docker CLI must be installed in the Jenkins container.
* Socket access gives Jenkins **root‑level control** of the host.

---

End of guide.


Now go back to host and restart jenkins container.

## Add step in Jenkins UI to build image and push to dockerhub. I am using private registry (change devjumpstart to your dockerhub account name).
```
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
docker build -t devjumpstart/demo-app:1.0 .
docker push devjumpstart/demo-app
```
For the above to work, you need to specify the username/password as env (you have to create credentials to be passed into the env).
