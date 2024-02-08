pipeline {
    agent any

    environment {
        NETWORK_STACK_NAME = 'Dev-network-stack'
        NETWORK_TEMPLATE_FILE = 'network.yaml'
        SSM_STACK_NAME = 'Dev-ssm-stack'
        SSM_TEMPLATE_FILE = 'ssm.yaml'
        AWS_DEFAULT_REGION = 'us-east-1'
        ROLE_ARN = 'arn:aws:iam::aws:policy/AWSCloudFormationFullAccess'
    }

    stages {
        stage('Deploy Network') {
            steps {
                script {
                    withCredentials([
                        [
                            $class: 'AmazonWebServicesCredentialsBinding',
                            accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                            secretKeyVariable: 'AWS_SECRET_ACCESS_KEY',
                            credentialsId: 'AdminNel'
                        ]
                    ]) {
                        sh """
                            aws cloudformation deploy \\
                            --template-file ${NETWORK_TEMPLATE_FILE} \\
                            --stack-name ${NETWORK_STACK_NAME} \\
                            --region ${AWS_DEFAULT_REGION} \\
                            --role-arn arn:aws:iam::aws:policy/AWSCloudFormationFullAccess
                        """
                    }
                }
            }
        }
    }

    // Additional stages for your specific deployment process (e.g., testing, approvals)
}
