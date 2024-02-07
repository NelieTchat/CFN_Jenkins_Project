pipeline {
    agent any

    environment {
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
        stage('Deploy') {
            steps {
                // withCredentials([usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY']) {
                    // Deploy Network Stack
                    // sh """
                    //     pwd
                    //     ls
                    //     aws cloudformation deploy --template-file ${NETWORK_TEMPLATE_FILE} \
                    //     --stack-name ${NETWORK_STACK_NAME} --region ${AWS_DEFAULT_REGION}
                    // """

                    
                    sh "aws cloudformation deploy --template-file ${SSM_TEMPLATE_FILE} --stack-name ${SSM_STACK_NAME} --region ${AWS_DEFAULT_REGION}"
                    // sh "aws cloudformation deploy --template-file ${WEBAPP_TEMPLATE_FILE} --stack-name ${WEBAPP_STACK_NAME} --region ${AWS_DEFAULT_REGION}"
                    // sh "aws cloudformation deploy --template-file ${DATABASE_TEMPLATE_FILE} --stack-name ${DATABASE_STACK_NAME} --region ${AWS_DEFAULT_REGION}"
                }
            }
        }
    }