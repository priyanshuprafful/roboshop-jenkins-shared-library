def call() {
    pipeline {
        agent any

        stages {

            stage('Compile/Build') {
                steps {
                    script {
                        if(app_language == 'nodejs'){
                            sh 'npm install'
                        }
                        if(app_language == 'maven'){
                            sh 'mvn package'

                    }
                }


                }

            }
            stage('Test Cases'){
                steps {
                    echo "Test Cases"
                }
            }

        }
    }
}