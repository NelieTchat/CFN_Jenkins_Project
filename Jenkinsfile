pipeline {
    agent any
    environment {
        AWS_REGION = 'us-east-1'
        NETWORK_STACK_NAME = 'Dev-network-stack'
        SSM_STACK_NAME = 'Dev-ssm-stack'
        WEBAPP_STACK_NAME = 'Dev-webapp-stack'
        DATABASE_STACK_NAME = 'Dev-DB-stack'

        NETWORK_TEMPLATE_FILE = 'network.yaml'
        SSM_TEMPLATE_FILE = 'SsmRole.yaml'
        WEBAPP_STACK_FILE = 'webapp.yaml'
        DATABASE_TEMPLATE_FILE = 'Dev-DB.yaml'
    }
    stages {
        stage('Deploy Cloudformation stacks') {
            steps {
                script {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'AdminNel', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        sh "aws cloudformation deploy --stack-name ${SSM_STACK_NAME} --template-file ${SSM_TEMPLATE_FILE} --capabilities CAPABILITY_IAM --region \"${AWS_REGION}\""
                        sh "aws cloudformation deploy --stack-name ${NETWORK_STACK_NAME} --template-file ${NETWORK_TEMPLATE_FILE} --region \"${AWS_REGION}\""
                        sh "aws cloudformation deploy --stack-name ${WEBAPP_STACK_NAME} --template-file ${WEBAPP_STACK_FILE} --region \"${AWS_REGION}\""
                        sh "aws cloudformation deploy --stack-name ${DATABASE_STACK_NAME} --template-file ${DATABASE_TEMPLATE_FILE} --region \"${AWS_REGION}\""

                    }
                }
            }
        }
    }
    post {
        success {
            echo 'Nelie, your Deployment succeeded!'
        }
        failure {
            echo 'Sorry, your Deployment failed!'
        }
    }
}
