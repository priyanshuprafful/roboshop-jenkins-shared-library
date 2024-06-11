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

                    sh 'aws ec2 describe-instances --filters "Name=tag:Name,Values=${component-${environment}}" --query "Reservations[*].Instances[*].PrivateIpAddress" --output text >/tmp/servers'

                    sh 'ansible-playbook -i /tmp/servers roboshop.yml -e role_name=${component} -e env=${environment}'

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