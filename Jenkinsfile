pipeline {
    agent any
    tools {
        nodejs "node"
    }
    stages {
        stage("increment version") {
            steps {
                script {
                    sh "npm version minor --no-git-tag-version"
                    def packagejson = readJSON file: 'package.json'
                    def version = packagejson.version
                    env.IMAGE_NAME = "${version}-${BUILD_NUMBER}"
                }
            }
        }
        stage('Run tests') {
            steps {
                script {
                    sh "npm install"
                    sh "npm test"
                }
            }
        }
        stage('Build and Push docker image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-token', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                        sh "docker build -t snrmartins/java-maven-app:${env.IMAGE_NAME} ."
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh "docker push snrmartins/java-maven-app:${env.IMAGE_NAME}"
                    }
                }
            }
        }
        stage('commit version update') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'gitlab-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                        sh 'git config --global user.email "jenkins@example.com"'
                        sh 'git config --global user.name "jenkins"'
                        sh 'git remote set-url origin https://$USER:$PASS@gitlab.com/twn-devops-bootcamp/latest/08-jenkins/jenkins-exercises.git'
                        sh 'git add package.json'
                        sh 'git commit -m "ci: version bump" || echo "No changes to commit"'
                        sh 'git push origin HEAD:jenkins-jobs'
                    }
                }
            }
        }
    }
}