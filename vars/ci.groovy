def call() {
    if (!env.sonar_extra_opts) {
        env.sonar_extra_opts = ""
    }

    if (env.TAG_NAME ==~ ".*"){
        env.GTAG = "true"
    }else {
        env.GTAG = "false"
    }
    node('workstation') {


        try {

            stage('Check Out Code') {

                cleanWs()

                git branch: 'main' , url: "https://github.com/priyanshuprafful/${component}"


            }
            sh 'env'

            if (env.BRANCH_NAME != "main") {
                stage('Compile/Build') {
                    common.compile()

                }

            }
            if (env.GTAG != "true" && env.BRANCH_NAME != "main"){
                stage('Test Cases') {
                    common.testcases()
                }

            }

            if (BRANCH_NAME ==~ "PR-.*") {

                stage('Code Quality') {
                    common.codequality()

                }

            }

            if (env.GTAG == "true") {
                stage('Package') {
                    common.prepareArtifacts()

                }
                stage('Artifact Upload') {
                    common.testcases()

                }

            }

        } catch (e) {
            mail bcc: '', body: "${component} - Pipeline Failed \n ${BUILD_URL} ", cc: '', from: 'priyanshugupta0803@gmail.com', replyTo: '', subject: "${component} - Pipeline Failed", to: 'priyanshugupta0803@gmail.com', mimeType: 'text/html'

        }
    }
}