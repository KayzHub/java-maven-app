pipeline {   
    agent any
    stages {
        stage("test") {
            steps {
                script {
                    echo "Testing the application...."
                }
            }
        }
        
        stage("build") {
            steps {
                script {
                    echo "Building the application...."
                }
            }
        }

        stage("deploy") {
            steps {
                script {
                    dockerCommand = 'docker run -d -p 3080:3080 --name demo-app devjumpstart/demo-app:2.0'
                    sshagent(['ec2_temp_machine']) {
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@98.80.144.201 ${dockerCommand}"
                    }
                }
            }
        }               
    }
} 
