def call() {
    if (!env.sonar_extra_opts) {
        env.sonar_extra_opts=""
    }
    pipeline {
        agent any

        stages {

            stage('Compile/Build') {
                steps {
                  //  sh 'env' // will give all the env details maybe
                    //sh 'exit 1'
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

        post {
            failure {
                mail bcc: '', body: "${component} - Pipeline Failed \n ${BUILD_URL} ", cc: '', from: 'priyanshugupta0803@gmail.com', replyTo: '', subject: "${component} - Pipeline Failed", to: 'priyanshugupta0803@gmail.com' , mimeType: 'text/html'
            }
        }
    }
}