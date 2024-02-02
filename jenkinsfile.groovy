pipeline {
    agent any

    stages {
        // Stage 1: Linting CloudFormation templates
        stage('Linting') {
            steps {
                script {
                    sh 'cfn-lint --include-nested CloudNet-Project-CFN/DB.yaml CloudNet-Project-CFN/network.yaml CloudNet-Project-CFN/webapp.yaml CloudNet-Project-CFN/ssm.yaml'
                }
            }
        }

        // Stage 2: Packaging CloudFormation template
        stage('Building Stack') {
            steps {
                script {
                    def s3BucketName = 'nel-read-buck' // Replace with your actual S3 bucket name

                    // Clean existing workspace
                    deleteDir()

                    // Package CloudFormation template
                    sh "aws cloudformation package --template-file CloudNet-Project-CFN/cloudformation.yaml --output-template-file packaged-template.yaml --nel-read-buck"
                }
            }
        }

        // Stage 3: Deploying CloudFormation stack
        stage('Deploying Stack') {
            steps {
                script {
                    // Add your deployment steps using AWS CLI or SDK
                    // Example: sh "aws cloudformation deploy --template-file packaged-template.yaml --stack-name my-stack-name"
                }
            }
        }

        // Stage 4: Cleanup (optional)
        stage('Cleanup') {
            steps {
                script {
                    // Add your cleanup steps here if needed
                }
            }
        }
    }

    // Post-pipeline actions
    post {
        always {
            echo 'This will always execute after the pipeline completes.'
        }
        failure {
            echo 'Oh no! An error occurred during deployment. Please check the logs for details.'
        }
    }
}
