def compile() {
    if(app_language == 'nodejs'){
        sh 'npm install'
    }
    if(app_language == 'maven'){
        sh 'mvn package ; mv target/${component}-1.0.jar {component}.jar'

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

    //    sh 'sonar-scanner -Dsonar.host.url=http://172.31.38.198:9000 -Dsonar.login=${SONARQUBE_USER} -Dsonar.password=${SONARQUBE_PASS} -Dsonar.projectKey=${component} ${sonar_extra_opts} -Dsonar.qualitygate.wait=true'
    sh 'echo Okay'
    }
}

def prepareArtifacts() {
    sh 'echo ${TAG_NAME} >VERSION'

   //if (app_language == "node_js" || app_language == "angular") {
         // since we are giving * version will get automatically added
//    }

    if (app_language == "maven") {
        sh 'zip -r ${component}-${TAG_NAME}.zip ${component}.jar schema VERSION'
    } else {
        sh 'zip -r ${component}-${TAG_NAME}.zip * -x Jenkinsfile'
    }


}
def artifactUpload () {
    NEXUS_USER = sh ( script: "aws ssm get-parameter --name prod.nexus.user --with-decryption | jq .Parameter.Value | xargs" , returnStdout: true ).trim()
 //   if (app_language == "node_js" || app_language == "angular") {
        sh 'curl -v -u ${NEXUS_USER}:admin123 --upload-file ${component}-${TAG_NAME}.zip http://172.31.42.150:8081/repository/${component}/${component}-${TAG_NAME}.zip'
 // }
}