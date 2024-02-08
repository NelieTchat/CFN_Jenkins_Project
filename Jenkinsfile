pipeline {
    agent any

    environment {
        NETWORK_STACK_NAME = 'Dev-network-stack'
        NETWORK_TEMPLATE_FILE = 'network.yaml'
        SSM_STACK_NAME = 'Dev-ssm-stack'
        SSM_TEMPLATE_FILE = 'ssm.yaml'
        AWS_DEFAULT_REGION = 'us-east-1'
        ROLE_ARN = 'arn:aws:iam::aws:policy/AWSCloudFormationFullAccess'
        // AWS_ACCESS_KEY_ID = credentials('admin-access-key-id')
        // AWS_SECRET_ACCESS_KEY = credentials('admin-secret-access-key')
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
                            credentialsId: 'admin'
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

        // Stage for deploying the SSM stack (optional)
        // Uncomment and modify as needed
        // stage('Deploy SSM') {
        //   steps {
        //     script {
        //       withCredentials([
        //         $class: 'AmazonWebServicesCredentialsBinding',
        //         accessKeyVariable: 'AWS_ACCESS_KEY_ID',
        //         secretKeyVariable: 'AWS_SECRET_ACCESS_KEY',
        //         credentialsId: 'aws-credentials' // Replace with your credential ID
        //       ]) {
        //         sh """
        //           aws configure set aws_access_key_id ${AWS_ACCESS_KEY_ID}
        //           aws configure set aws_secret_access_key ${AWS_SECRET_ACCESS_KEY}
        //           aws cloudformation deploy \
        //             --template-file ssm.yaml \
        //             --stack-name Dev-ssm-stack \
        //             --region ${AWS_DEFAULT_REGION} \
        //             --capabilities CAPABILITY_IAM
        //         """
        //       }
        //     }
        //   }
        // }
    }

    // Additional stages for your specific deployment process (e.g., testing, approvals)
}
