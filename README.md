# java-maven-app

A simple Java application built with **Apache Maven**, with a **Jenkins Pipeline** (`Jenkinsfile`) included for CI/CD automation.

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
