#!/user/bin/env groovy

library identifier: 'jenkins-shared-library@master', retriever: modernSCM([
    $class: 'GitSCMSource',
    remote: 'https://gitlab.com/SnrMartins/jenkins-shared-library.git',
    credentialsId: 'gitlab-credentials'
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
                script{
                    def version = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
                    def imageTag = "snrmartins/java-maven-app:${version}-alpine"
                    echo "Building Docker image with tag: ${imageTag}"
                    buildImage(version)
                    dockerLogin()
                    dockerPush(version)
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