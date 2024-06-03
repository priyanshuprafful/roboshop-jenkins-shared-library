def call() {
    if (!env.sonar_extra_opts) {
        env.sonar_extra_opts = ""
    }
    node('workstation') {


        try {

            stage('Check Out Code') {
                sh 'ls -l'
                cleanWs()
                sh 'ls -l'
                git branch: 'main' , url: 'https://github.com/priyanshuprafful/cart'
                sh 'ls -l'

            }
            stage('Compile/Build') {
                sh 'env'
                common.compile()
            }

            stage('Test Cases') {
                common.testcases()
            }

            stage('Code Quality') {
                common.codequality()

            }
        } catch (e) {
            mail bcc: '', body: "${component} - Pipeline Failed \n ${BUILD_URL} ", cc: '', from: 'priyanshugupta0803@gmail.com', replyTo: '', subject: "${component} - Pipeline Failed", to: 'priyanshugupta0803@gmail.com', mimeType: 'text/html'

        }
    }
}