def compile() {
    if(app_language == 'nodejs'){
        sh 'npm install'
    }
    if(app_language == 'maven'){
        sh 'mvn package'

    }
}

def testcases() {

    if(app_language == 'nodejs'){
        sh 'echo test'
    }
    if(app_language == 'maven'){
        sh 'echo test'

    }

}