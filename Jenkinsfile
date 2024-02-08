pipeline {
    agent any

    environment {
        NETWORK_STACK_NAME = 'Dev-network-stack'
        NETWORK_TEMPLATE_FILE = 'network.yaml'
        SSM_STACK_NAME = 'Dev-ssm-stack'
        SSM_TEMPLATE_FILE = 'ssm.yaml'
        AWS_DEFAULT_REGION = 'us-east-1'
        ROLE_ARN = 'arn:${AWS::Partition}:iam::aws:policy/AWSCloudFormationFullAccess'
        AWS_ACCESS_KEY_ID = credentials('admin-access-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('admin-secret-access-key')
    }

    stages {
        // stage('Deploy Network') {
        //     steps {
        //         script {
        //             withCredentials([[
        //                 $class: 'AmazonWebServicesCredentialsBinding',
        //                 accessKeyVariable: 'AWS_ACCESS_KEY_ID',
        //                 secretKeyVariable: 'AWS_SECRET_ACCESS_KEY',
        //                 credentialsId: 'admin'
        //             ]]) {
        //                 sh """
        //                     export AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
        //                     export AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}
        //                     aws cloudformation deploy \
        //                     --template-file ${NETWORK_TEMPLATE_FILE} \
        //                     --stack-name ${NETWORK_STACK_NAME} \
        //                     --region ${AWS_DEFAULT_REGION}
        //                 """
        //                 // Additional steps if needed
        //             }
        //         }
        //     }
        // }

    stages {
        stage('Deploy SSM') {
            steps {
                script {
                    sh """
                        aws configure set aws_access_key_id ${AWS_ACCESS_KEY_ID}
                        aws configure set aws_secret_access_key ${AWS_SECRET_ACCESS_KEY}
                        aws cloudformation deploy --template-file ${SSM_TEMPLATE_FILE} --stack-name ${SSM_STACK_NAME} --region ${AWS_DEFAULT_REGION} --capabilities CAPABILITY_IAM
                    """
                    // Additional steps if needed
                }
            }
        }
    }
}