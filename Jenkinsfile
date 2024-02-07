pipeline {
    agent any

    environment {
        AWS_ACCESS_KEY_ID     = AKIATNTT4OJUN4BPKS45
        AWS_SECRET_ACCESS_KEY = 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
        // Stack names section
        NETWORK_STACK_NAME = 'Dev-network-stack'
        SSM_STACK_NAME = 'Dev-ssm-role'
        WEBAPP_STACK_NAME = 'Dev-webapp-stack'
        DATABASE_STACK_NAME = 'Dev-database-stack'

        // Template paths section
        NETWORK_TEMPLATE_FILE = 'network.yaml'
        SSM_TEMPLATE_FILE = 'ssm.yaml'
        WEBAPP_TEMPLATE_FILE = 'webapp.yaml'
        DATABASE_TEMPLATE_FILE = 'DB.yaml'

        // AWS default region
        AWS_DEFAULT_REGION = 'us-east-1'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    git credentialsId: 'AdminNel', url: 'https://github.com/NelieTchat/CFN_Jenkins_Project.git'
                }
            }
        }
        stage('Deploy') {
            steps {

                    sh """
                        aws cloudformation deploy --template-file ${NETWORK_TEMPLATE_FILE} \
                        --stack-name ${NETWORK_STACK_NAME} --region ${AWS_DEFAULT_REGION}
                    """

                    
                    // sh "aws cloudformation deploy --template-file ${SSM_TEMPLATE_FILE} --stack-name ${SSM_STACK_NAME} --region ${AWS_DEFAULT_REGION} --capabilities CAPABILITY_IAM"
                    // sh "aws cloudformation deploy --template-file ${WEBAPP_TEMPLATE_FILE} --stack-name ${WEBAPP_STACK_NAME} --region ${AWS_DEFAULT_REGION}"
                    // sh "aws cloudformation deploy --template-file ${DATABASE_TEMPLATE_FILE} --stack-name ${DATABASE_STACK_NAME} --region ${AWS_DEFAULT_REGION}"
                }
            }
        }
    }