pipeline {
    agent any

    tools {
        maven 'maven-3.9'
    }

    stages {

        stage('build jar') {
            steps {
                script {
                    echo "building the application..."
                    sh 'mvn package'
                }
            }
        }

        stage('build image') {
            steps {
                script {
                    echo "building the docker image..."
                    withCredentials([usernamePassword(
                        credentialsId: 'docker-hub-repo',
                        usernameVariable: 'USER',
                        passwordVariable: 'PASS'
                    )]) {
                        // tag with build number (simple + reliable)
                        sh "docker build -t valenciadev/demo-app:${env.BUILD_NUMBER} ."
                        sh 'echo $PASS | docker login -u $USER --password-stdin'
                        sh "docker push valenciadev/demo-app:${env.BUILD_NUMBER}"
                    }
                }
            }
        }

        stage('deploy') {
            steps {
                script {
                    echo "deploying the application..."
                }
            }
        }
    }
}
