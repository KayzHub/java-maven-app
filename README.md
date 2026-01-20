# java-maven-app

A simple Java application built with **Apache Maven**, with a **Jenkins Pipeline** (`Jenkinsfile`) included for CI/CD automation.

This repo contains a standard Maven project layout (`src/main/...`) plus build configuration (`pom.xml`) and a Jenkins pipeline definition (`Jenkinsfile`).  [oai_citation:0‡GitHub](https://github.com/KayzHub/java-maven-app.git)

---

## Project Structure

Typical layout:

- `pom.xml` — Maven build + dependencies  [oai_citation:1‡GitHub](https://github.com/KayzHub/java-maven-app.git)  
- `Jenkinsfile` — Jenkins pipeline for building/testing the app  [oai_citation:2‡GitHub](https://github.com/KayzHub/java-maven-app.git)  
- `src/main/...` — application source code  [oai_citation:3‡GitHub](https://github.com/KayzHub/java-maven-app.git)  

> Note: The default branch shown in the repo UI is `feature/payment`.  [oai_citation:4‡GitHub](https://github.com/KayzHub/java-maven-app.git)

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
