stage("Increment version") {
    steps {
        script {
            dir("app") {
                sh "npm version minor --no-git-tag-version"
                def packagejson = readJSON file: 'package.json'
                def version = packagejson.version
                env.IMAGE_NAME = "${version}-${BUILD_NUMBER}"
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
                dir("app") {
                    sh "docker build -t snrmartins/my-node-app:${env.IMAGE_NAME} ."
                    sh "echo $PASS | docker login -u $USER --password-stdin"
                    sh "docker push snrmartins/my-node-app:${env.IMAGE_NAME}"
                }
            }
        }
    }
}

stage("Commit version update") {
    steps {
        script {
            withCredentials([usernamePassword(credentialsId: 'gitlab-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                dir("app") {
                    sh 'git config --global user.email "jenkins@example.com"'
                    sh 'git config --global user.name "jenkins"'
                    sh 'git add package.json'
                    sh 'git commit -m "ci: version bump" || echo "No changes to commit"'
                    sh 'git push https://$USER:$PASS@gitlab.com/SnrMartins/your-nodejs-repo.git HEAD:main'
                }
            }
        }
    }
}
