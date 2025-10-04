#!/usr/bin/env groovy

library(
  identifier: 'jenkins-shared-library@master',
  retriever: modernSCM([
    $class: 'GitSCMSource',
    remote: 'https://gitlab.com/SnrMartins/jenkins-shared-library.git',
    credentialsId: 'gitlab-credentials'
  ])
)

def gv

pipeline {
    agent any
    tools {
        maven 'maven-3.9'
    }
    environment {
        branchName = "${env.BRANCH_NAME}"
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage("build jar") {
            steps {
                script{
                    buildJar()
                }
            }
        }
        stage("build and push image") {
    steps {
        script {
            buildAndPush("snrmartins/java-maven-app")

                }
            }
        }
        stage("deploy") {
            steps {
                script{
                    gv.deployApp()
                }
            }
        }
    }
}