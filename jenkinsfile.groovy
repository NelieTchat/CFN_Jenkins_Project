pipeline {
    agent any

        stage('Building Stack') {
            steps {
                script {
                    git url: 'https://github.com/NelieTchat/CloudNet.CFN.git', credentialsId: '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'


                    // Package CloudFormation template
                    sh "aws cloudformation package --template-file CloudNet.CFN/cloudformation.yaml --output-template-file packaged-template.yaml --s3-bucket ${s3BucketName}"
                }
            }
        }

        stage('Deploying Stack') {
            steps {
                script {
                    // Add your deployment steps here
                }
            }
        }

        stage('Cleanup') {
            steps {
                script {
                    // Add cleanup steps here if needed
                }
            }
        }
    }

    post {
        always {
            echo 'This will always execute after the pipeline completes.'
        }
        failure {
            echo 'Oh no! An error occurred during deployment. Please check the logs for details.'
        }
    }
}
