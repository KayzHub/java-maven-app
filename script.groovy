def buildJar() {
    echo 'building the application...'
    sh 'mvn clean install'
}

def buildImage() {
    echo 'building the application...'
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t snrmartins/java-maven-app:20-alpine .'
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh 'docker push snrmartins/java-maven-app:20-alpine'
    }
}

def deployApp() {
    echo 'deploying the application...'
}

return this