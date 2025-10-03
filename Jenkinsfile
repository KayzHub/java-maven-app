#!/user/bin/env groovy

@Library('jenkins-shared-library') _
def gv

pipeline {
    agent any
    tools {
        maven 'maven-3.9'
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
                script{
                    def version = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
                    def imageTag = "snrmartins/java-maven-app:${version}-alpine"
                    echo "Building Docker image with tag: ${imageTag}"
                    buildImage(imageTag)
                    dockerLogin()
                    dockerPush(imageTag)
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