def call() {
    if (!env.sonar_extra_opts) {
        env.sonar_extra_opts = ""
    }
    node('workstation') {


        try {

            stage('Check Out Code') {

                cleanWs()

                git branch: 'main' , url: 'https://github.com/priyanshuprafful/cart'


            }
            sh 'env'

            if (env.BRANCH_NAME != "main") {
                stage('Compile/Build') {
                    common.compile()

                }

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