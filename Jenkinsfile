pipeline {
    agent any

    environment {
        NETWORK_STACK_NAME = 'Dev-network-stack'
        NETWORK_TEMPLATE_FILE = 'network.yaml'
        SSM_STACK_NAME = 'Dev-ssm-stack'
        SSM_TEMPLATE_FILE = 'ssm.yaml'
        AWS_DEFAULT_REGION = 'us-east-1'
        ROLE_ARN = arn:${AWS::Partition}:iam::aws:policy/AWSCloudFormationFullAccess

                    
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
                    withCredentials([usernamePassword(credentialsId: 'admin', passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID')]) {
                        withIamCredentials(roleArn: "${ROLE_ARN}") {
                            sh "aws cloudformation deploy --template-file ${SSM_TEMPLATE_FILE} --stack-name ${SSM_STACK_NAME} --region ${AWS_DEFAULT_REGION} --capabilities CAPABILITY_IAM"

                            // Additional steps if needed
                        }
                    }
                }
            }
        }
    }
}