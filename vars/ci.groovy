def call() {
    if (!env.sonar_extra_opts) {
        env.sonar_extra_opts=""
    }
    pipeline {
        agent any

        stages {

            stage('Compile/Build') {
                steps {
                    mail bcc: '', body: 'AWS verified Jenkins  ', cc: '', from: 'priyanshugupta0803@gmail.com', replyTo: '', subject: 'Test Jenkins ', to: 'priyanshugupta0803@gmail.com'
                    script {
                        common.compile()
                }


                }

            }
            stage('Test Cases'){
                steps {
                   script {
                       common.testcases()
                   }
                }
            }

            stage('Code Quality'){
                steps {
                    script{
                        common.codequality()
                    }
                }
            }
        }
    }
}