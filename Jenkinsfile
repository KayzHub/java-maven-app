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
                    withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
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
                    withCredentials([usernamePassword(credentialsId: 'gitlab-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                        sh 'git config --global user.email "jenkins@example.com"'
                        sh 'git config --global user.name "jenkins"'
                        sh 'git remote set-url original https://$USER:$PASS@gitlab.com/twn-devops-bootcamp/latest/08-jenkins/jenkins-exercises.git'
                        sh 'git add .'
                        sh 'git commit -m "ci: version bump"'
                        sh 'git push origin HEAD:jenkins-jobs'
                    }
                }
            }
        }
    }
}