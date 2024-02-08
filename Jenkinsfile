pipeline {
    agent any

    environment {
        NETWORK_STACK_NAME = 'Dev-network-stack'
        NETWORK_TEMPLATE_FILE = 'network.yaml'
        SSM_STACK_NAME = 'Dev-ssm-stack'
        SSM_TEMPLATE_FILE = 'ssm.yaml'
        AWS_DEFAULT_REGION = 'us-east-1'
    }

    stages {
    //     stage('Deploy Network') {
    //         steps {
    //             script {
    //                 withCredentials([
    //                     [
    //                         $class: 'AmazonWebServicesCredentialsBinding',
    //                         accessKeyVariable: 'AWS_ACCESS_KEY_ID',
    //                         secretKeyVariable: 'AWS_SECRET_ACCESS_KEY',
    //                         credentialsId: 'AdminNel'
    //                     ]
    //                 ]) {
    //                     sh """
    //                         aws cloudformation deploy \\
    //                         --template-file ${NETWORK_TEMPLATE_FILE} \\
    //                         --stack-name ${NETWORK_STACK_NAME} \\
    //                         --region ${AWS_DEFAULT_REGION} \\
    //                     """
    //                 }
    //             }
    //         }
    //     }
    // }

    // Additional stages for your specific deployment process (e.g., testing, approvals)

        stage('Deploy SSM') {
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
                            --template-file ssm.yaml \\
                            --stack-name Dev-ssm-stack \\
                            --region ${AWS_DEFAULT_REGION} \\
                            --capabilities CAPABILITY_IAM
                        """
                    }
                }
            }
        }
    }
}