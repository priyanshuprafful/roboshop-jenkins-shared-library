def call() {
    pipeline {
        agent any

        options {
            ansiColor('xterm')
        }

        parameters {
            string(name: 'app_version', defaultValue: '' , description: 'App_Version')
            string(name: 'component', defaultValue: '' , description: 'Component')
            string(name: 'environment', defaultValue: '' , description: 'Environment')


        }

        stages {

            stage('Clone Application') {
                steps {
                    dir('APP') {
                        git branch: 'main', url: "https://github.com/priyanshuprafful/${component}"
                    }


                }
            }



            stage('Deploy Helm Charts') {
                steps {
                    script {
                        sh 'helm install ${component} . -f APP/helm/${environment}.yaml --set appversion=${app_version}'



                        }

                    }



                }
            }

        }

        post {
            always {
                cleanWs()
            }
        }
    }
