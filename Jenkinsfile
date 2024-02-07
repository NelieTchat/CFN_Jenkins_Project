pipeline {
    agent any

    environment {
        // Stack names section
        NETWORK_STACK_NAME = 'Dev-network-stack'
        SSM_STACK_NAME = 'Dev-ssm-role'
        WEBAPP_STACK_NAME = 'Dev-webapp-stack'
        DATABASE_STACK_NAME = 'Dev-database-stack'

        // Template paths section
        NETWORK_TEMPLATE_FILE = 'CloudNet-Project-CFN/network.yaml'
        SSM_TEMPLATE_FILE = 'CloudNet-Project-CFN/ssm.yaml'
        WEBAPP_TEMPLATE_FILE = 'CloudNet-Project-CFN/webapp.yaml'
        DATABASE_TEMPLATE_FILE = 'CloudNet-Project-CFN/DB.yaml'

        // AWS default region
        AWS_DEFAULT_REGION = 'us-east-1'
    }

    stages {
        stage('Deploy') {
            steps {
                withCredentials([usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY']) {
                    // Deploy Network Stack
                    sh """
                        aws cloudformation deploy --template-file ${NETWORK_TEMPLATE_FILE} \
                        --stack-name ${NETWORK_STACK_NAME} --region ${AWS_DEFAULT_REGION}
                    """

                    // Add similar commands for other stacks, uncommenting and adding `--depends-on` as needed
                    // sh "aws cloudformation deploy --template-file ${SSM_TEMPLATE_FILE} --stack-name ${SSM_STACK_NAME} --region ${AWS_DEFAULT_REGION}"
                    // sh "aws cloudformation deploy --template-file ${WEBAPP_TEMPLATE_FILE} --stack-name ${WEBAPP_STACK_NAME} --region ${AWS_DEFAULT_REGION}"
                    // sh "aws cloudformation deploy --template-file ${DATABASE_TEMPLATE_FILE} --stack-name ${DATABASE_STACK_NAME} --region ${AWS_DEFAULT_REGION}"
                }
            }
        }
    }
}
