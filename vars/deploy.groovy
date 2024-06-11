def call() {
    pipeline {
        agent any

        parameters {
            string(name: 'app_version', defaultValue: '' , description: 'App_Version')
            string(name: 'component', defaultValue: '' , description: 'Component')
            string(name: 'environment', defaultValue: '' , description: 'Environment')


        }

        stages {

            stage('Update Parameter Store') {
                steps {

                    sh 'aws ssm put-parameter --name ${environment}.${component}.app_version --type "String" --value "${app_version}"  --overwrite'

                }
            }



            stage('Deploy Servers') {
                steps {
                    script {
                        env.NEXUS_USER = sh ( script: "aws ssm get-parameter --name prod.nexus.user --with-decryption | jq .Parameter.Value | xargs" , returnStdout: true ).trim()
                        env.NEXUS_PASS = sh ( script: "aws ssm get-parameter --name prod.nexus.pass --with-decryption | jq .Parameter.Value | xargs" , returnStdout: true ).trim()
                        wrap([$class: 'MaskPasswordsBuildWrapper',
                              varPasswordPairs: [[password: NEXUS_PASS] , [password:NEXUS_USER]]]) {

                            //   if (app_language == "node_js" || app_language == "angular") {
                            sh 'curl -v -u ${NEXUS_USER}:${NEXUS_PASS} --upload-file ${component}-${TAG_NAME}.zip http://172.31.42.150:8081/repository/${component}/${component}-${TAG_NAME}.zip'
                            // }

                        }
                        //   if (app_language == "node_js" || app_language == "angular") {
                        sh 'curl -v -u ${NEXUS_USER}:${NEXUS_PASS} --upload-file ${component}-${TAG_NAME}.zip http://172.31.42.150:8081/repository/${component}/${component}-${TAG_NAME}.zip'
                        // }
                    }

                    sh 'aws ec2 describe-instances --filters "Name=tag:Name,Values=${component}-${environment}" --query "Reservations[*].Instances[*].PrivateIpAddress" --output text >/tmp/servers'

                    sh 'ansible-playbook -i /tmp/servers roboshop.yml -e role_name=${component} -e env=${environment} -e ansible_user=centos -e ansible_password=${SSH_PASSWORD}'

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