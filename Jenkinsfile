@Library('jenkins-shared-library') _

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
                script {
                    buildJar()   // this comes from shared library too
                }
            }
        }

        stage("build and push image") {
            steps {
                script {
                    buildAndPush("snrmartins/java-maven-app")   // ✅ from shared lib
                }
            }
        }

        stage("deploy") {
            steps {
                script {
                    gv.deployApp()  // also from shared library
                }
            }
        }
    }
}
