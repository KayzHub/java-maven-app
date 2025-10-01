pipeline {
    agent any
    tools {
        nodejs "node"
    }
    stages {
        stage("increment version") {
            steps {
                script{
                    dir("app") {
                        sh "npm version minor -no-git-tag-version"
                        def packagejson = readJSON file: 'package.json'
                        def version = package.version
                        env.IMAGE_NAME = "$version-$BUILD_NUMBER" 
                    }
                } 
            }
        }
        stage('Run tests') {
            steps {
                script{
                    dir("app") {
                        sh "npm install"
                         sh "npm run test"
                    }
                } 
            }
        }
        stage('Build and Push docker image') {
            steps {
                script{
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                        sh 'docker build -t snrmartins/java-maven-app:20-alpine .'
                        sh 'echo $PASS | docker login -u $USER --password-stdin'
                        sh 'docker push snrmartins/java-maven-app:20-alpine'
                    }
                }
            }
        }
        stage('commit version update') {
            steps {
                script{
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                        sh 'git config --global user'
                }
            }
        }
    }
}

