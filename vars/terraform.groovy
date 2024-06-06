def call () {
    pipeline {
        agent any

        parameters {
            string(name: 'ENV', defaultValue: '' , description: 'dev or prod , specify')
            string(name: 'ACTION', defaultValue: '' , description: 'apply or destroy , specify')
        }

        options {
            ansiColor('xterm')
        }

        stages {
            stage('Init') {
                steps {
                    sh 'terraform init -backend-config=env-${ENV}/state.tfvars'
                }
            }

            stage ('Apply') {
                steps {
                    sh ''
                    sh 'terraform ${ACTION} -auto-approve -var-file=env-${ENV}/main.tfvars'
                    // sh 'echo OKAY'
                }
            }
        }
        post {
            always {
                cleanWs()
            }

        }
    }
}