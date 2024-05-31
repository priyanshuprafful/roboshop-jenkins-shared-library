def compile() {
    if(app_language == 'nodejs'){
        sh 'npm install'
    }
    if(app_language == 'maven'){
        sh 'mvn package'

    }
}

def testcases() {

    // npm test
    // mvn test
    // python -m unittests
    // go test
    sh 'echo Okay'

}

def codequality() {
    withAWSParameterStore(credentialsId: 'PARAMETER1' , naming: 'absolute', path: '/sonarqube', recursive: true, regionName: 'us-east-1')
    {

        sh 'sonar-scanner -Dsonar.host.url=http://172.31.38.198:9000 -Dsonar.login=${SONARQUBE_USER} -Dsonar.password=${SONARQUBE_PASS} -Dsonar.projectKey=${component} ${sonar_extra_opts} -Dsonar.qualitygate.wait=true'
    }
}