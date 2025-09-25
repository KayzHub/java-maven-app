def gv

pipeline {
    agent any
    parameters {
        choice(name: 'VERSION' , choices: ['1.1.0', '1.2.0', '1.3.0'], description: '')
        booleanParam(name: 'executeTests', defaultValue: true, description:'')
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }      
        stage("build") {
            steps {
                script{
                    gv.buildAPP()
                }
            }
        }
        stage("test") {
            when {
                expression {
                    params.executeTests
                }
            }
            steps {
                script{
                    gv.testAPP()
                } 
            }
        }
        stage("deploy") {
            steps {
                script{
                    env.ENV = input message: "select the environment to deploy to", ok: "Done", parameters: [choice(name: 'ONE', choices: ['dev', 'staging', 'prod'], description: '')]

                    gv.deployAPP()
                    echo "Depoying to ${ENV}"
                }
            }
        }
    }
}