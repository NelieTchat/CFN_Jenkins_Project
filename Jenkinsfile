pipeline {
    agent any

    parameters {
        string(name: 'NETWORK_STACK_NAME', defaultValue: 'Dev-network-stack', description: 'Name of the network stack')
        string(name: 'SSM_STACK_NAME', defaultValue: 'Dev-ssm-stack', description: 'Name of the ssmrole stack')
        string(name: 'DATABASE_STACK_NAME', defaultValue: 'Dev-DB-stack', description: 'Name of the database stack')
        string(name: 'WEBAPP_STACK_NAME', defaultValue: 'Dev-web-app-stack', description: 'Name of the webapp stack')
    }

    environment {
        AWS_REGION = 'us-east-1'  // Replace with your actual region

        JENKINS_USERNAME_ROLE_ARN = 'arn:aws:ssm:us-east-1:235392496232:parameter/MasterUsername'  
        JENKINS_PASSWORD_ROLE_ARN = 'arn:aws:ssm:us-east-1:235392496232:parameter/MasterUserPassword'  
        JENKINS_OPERATOREMAIL_ROLE_ARN = 'arn:aws:ssm:us-east-1:235392496232:parameter/OperatorEmail'  
        CLOUDFORMATION_ROLE_ARN = 'arn:aws:iam::aws:policy/AWSCloudFormationFullAccess'

        NETWORK_STACK_NAME = "${params.NETWORK_STACK_NAME}"
        SSM_STACK_NAME = "${params.SSM_STACK_NAME}"
        DATABASE_STACK_NAME = "${params.DATABASE_STACK_NAME}"
        WEBAPP_STACK_NAME = "${params.WEBAPP_STACK_NAME}"
    }

    // Define the `getSSMParameters` function outside the stages
    def getSSMParameters(String parameterName, String roleArnEnvVar, String outputVar) {
        String roleArn = env[roleArnEnvVar]

        withCredentials([
            [$class: 'AmazonWebServicesCredentialsBinding',
             region: AWS_REGION,
             roleArn: roleArn]
            ]
        ) {
            script {
                sh """
                    ${outputVar}=\$(aws ssm get-parameter --name /my-app/${parameterName} --query Parameter.Value --output text)
                    export ${outputVar}
                """
            }
        }
    }

    def deployStack(String templateFile, String stackName, String roleArn) {
        withCredentials([
            [
                $class: 'AmazonWebServicesCredentialsBinding',
                region: AWS_REGION,
                roleArn: roleArn
            ]
        ]) {
            sh """
                aws cloudformation deploy --template-file ${templateFile} --stack-name ${stackName} --region ${AWS_REGION}
            """
        }
    }

    stages {
        stage('Deploy') {
            steps {
                script {
                    // Access secrets using retrieved parameters (no need for nested script block)
                    getSSMParameters('MasterUsername', 'JENKINS_USERNAME_ROLE_ARN', 'DATABASE_USERNAME')
                    getSSMParameters('MasterUserPassword', 'JENKINS_PASSWORD_ROLE_ARN', 'DATABASE_PASSWORD')
                    getSSMParameters('OperatorEmail', 'JENKINS_OPERATOREMAIL_ROLE_ARN', 'OPERATOR_EMAIL')

                    // Validate CloudFormation templates (optional)
                    // sh '...'

                    // Deploy Network stack
                    deployStack('network.yaml', NETWORK_STACK_NAME, 'CLOUDFORMATION_ROLE')

                    // Deploy SSM stack
                    deployStack('JenkinsSSMAccessRole.yaml', SSM_STACK_NAME, 'CLOUDFORMATION_ROLE')

                    // Deploy WebApp stack
                    deployStack('webapp.yaml', WEBAPP_STACK_NAME, 'CLOUDFORMATION_ROLE')

                    // Deploy Database stack
                    deployStack('DB.yaml', DATABASE_STACK_NAME, 'CLOUDFORMATION_ROLE')
                }
            }

            post {
                always {
                    echo "All stack deployments completed!"
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully!"
        }
        failure {
            echo "Pipeline failed"
        }
    }
}
