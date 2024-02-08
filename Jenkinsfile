pipeline {
    agent any

    environment {
        // Consider using environment variables for sensitive information like region
        AWS_DEFAULT_REGION = 'us-east-1'
        ROLE_ARN = 'arn:${AWS::Partition}:iam::aws:policy/AWSCloudFormationFullAccess' // Consider a more restrictive policy based on your needs
        NETWORK_STACK_NAME = 'Dev-network-stack'
        NETWORK_TEMPLATE_FILE = 'network.yaml'
        SSM_STACK_NAME = 'Dev-ssm-stack'
        SSM_TEMPLATE_FILE = 'ssm.yaml'
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

        stage('Deploy SSM') {
            steps {
                script {
                    // Use environment variables if available
                    def region = env.AWS_DEFAULT_REGION ?: 'us-east-1' // Set a default if not defined

                    withIamCredentials(roleArn: ROLE_ARN) {
                        sh """
                            aws cloudformation deploy \
                                --template-file ${SSM_TEMPLATE_FILE} \
                                --stack-name ${SSM_STACK_NAME} \
                                --region ${region} \
                                --capabilities CAPABILITY_IAM
                        """
                    }
                }
            }
        }
    }
}