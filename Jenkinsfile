pipeline {
    agent any

    tools {
        nodejs "node"   // Make sure this matches your NodeJS tool name in Jenkins
    }

    environment {
        DOCKER_IMAGE = "snrmartins/java-maven-app"
    }

    stages {
        stage("Increment version") {
            steps {
                script {
                    dir("app") {
                        // Bump minor version without git tag
                        sh "npm version minor --no-git-tag-version"

                        // Read package.json to get the version
                        def packagejson = readJSON file: 'package.json'
                        def version = packagejson.version
                        env.IMAGE_NAME = "${version}-${BUILD_NUMBER}"
                        echo "New image tag: ${env.IMAGE_NAME}"
                    }
                }
            }
        }

        stage("Install dependencies & Run tests") {
            steps {
                script {
                    dir("app") {
                        sh "npm install"
                        sh "npm test"
                    }
                }
            }
        }

        stage("Build & Push Docker image") {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-token', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                        // Build Docker image using root-level Dockerfile
                        sh "docker build -t ${env.DOCKER_IMAGE}:${env.IMAGE_NAME} ."
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh "docker push ${env.DOCKER_IMAGE}:${env.IMAGE_NAME}"
                    }
                }
            }
        }

        stage("Commit version update") {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'gitlab-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                        sh 'git config --global user.email "jenkins@example.com"'
                        sh 'git config --global user.name "jenkins"'

                        // Update remote with credentials
                        sh 'git remote set-url origin https://$USER:$PASS@gitlab.com/SnrMartins/java-maven-app.git'

                        dir("app") {
                            sh 'git add package.json'
                            sh 'git commit -m "ci: version bump" || echo "No changes to commit"'
                            sh 'git push origin HEAD:patch-6'
                        }
                    }
                }
            }
        }
    }
}
