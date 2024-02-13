pipeline {
    agent any

    parameters {
        string(name: 'NETWORK_STACK_NAME', defaultValue: 'Dev-network-stack', description: 'Name of the network stack')
        string(name: 'SSM_STACK_NAME', defaultValue: 'Dev-ssm-stack', description: 'Name of the ssmrole stack')
        string(name: 'DATABASE_STACK_NAME', defaultValue: 'Dev-DB-stack', description: 'Name of the database stack')
        string(name: 'WEBAPP_STACK_NAME', defaultValue: 'Dev-DB-stackk', description: 'Name of the webapp stack')
    }

    environment {
        AWS_REGION = 'us-east-1'

        // IAM roles with ARNs (replace placeholders with actual roles)
        JENKINS_USERNAME_ROLE_ARN = 'arn:aws:ssm:us-east-1:235392496232:role/YourUsernameRole'
        JENKINS_PASSWORD_ROLE_ARN = 'arn:aws:ssm:us-east-1:235392496232:role/YourPasswordRole'
        JENKINS_OPERATOR_EMAIL_ROLE_ARN = 'arn:aws:ssm:us-east-1:235392496232:role/YourOperatorEmailRole'
        CLOUDFORMATION_DEPLOYER_ROLE_ARN = 'arn:aws:iam::aws:policy/AWSCloudFormationFullAccess'

        // Access stack names from parameters:
        NETWORK_STACK_NAME = "${params.NETWORK_STACK_NAME}"
        SSM_STACK_NAME = "${params.SSM_STACK_NAME}"
        DATABASE_STACK_NAME = "${params.DATABASE_STACK_NAME}"
        WEBAPP_STACK_NAME = "${params.WEBAPP_STACK_NAME}"
    }

    // Functions with role-based access
    def getSSMParameters(String parameterName, String roleArnEnvVar, String outputVar) {
        String roleArn = env[roleArnEnvVar] // Access role ARN from environment variable

        withCredentials([
            [
                $class: 'AmazonWebServicesCredentialsBinding',
                region: AWS_REGION,
                roleArn: roleArn
            ]
        ]) {
            script {
                // Retrieve secrets and set environment variables
                sh """
                    ${outputVar}=\$(aws ssm get-parameter --name /my-app/${parameterName} --query Parameter.Value --output text)
                    export ${outputVar}
                """
            }
        }
    }

    // Improved `deployStack` function:
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
        // Validate CloudFormation templates stage
        stage('Validate CloudFormation Templates') {
            steps {
                parallel {
                    // Validate network template
                    stage('Validate Network Template') {
                        steps {
                            sh 'aws cloudformation validate-template --template-body file://network.yaml'
                        }
                    }
                    // Validate SSM role template
                    stage('Validate ssmRole Template') {
                        steps {
                            sh 'aws cloudformation validate-template --template-body file://ssm.yaml'
                        }
                    }
                    // Validate webapp template
                    stage('Validate webapp Template') {
                        steps {
                            sh 'aws cloudformation validate-template --template-body file://webapp.yaml'
                        }
                    }
                    // Validate database template
                    stage('Validate DB Template') {
                        steps {
                            sh 'aws cloudformation validate-template --template-body file://DB.yaml'
                        }
                    }
                }
            }
        }

        // Deployment stages (replace with your specific needs)
        stage('Deploy Network') {
            steps {
                script {
                    // Wait for previous stages (if applicable)
                    build job: '...', wait: true // Replace with any dependencies

                    // Retrieve secrets using appropriate IAM role
                    getSSMParameters('database-username', 'JENKINS_USERNAME_ROLE_ARN', 'DATABASE_USERNAME')
                    getSSMParameters('database-password', 'JENKINS_PASSWORD_ROLE_ARN', 'DATABASE_PASSWORD')
                    getSSMParameters('operator1-email', 'JENKINS_OPERATOR_EMAIL_ROLE_ARN', 'OPERATOR_EMAIL')
                }
            }
            // Deploy stack using appropriate IAM role
            deployStack('network.yaml', NETWORK_STACK_NAME, 'CLOUDFORMATION_ROLEN')
            post {
                always {
                    echo "Network stack deployment completed!"
                }
            }

            // Deploy SSM stack using appropriate IAM role
            deployStack('JenkinsSSMAccessRole.yaml', SSM_STACK_NAME, 'CLOUDFORMATION_ROLE')
            post {
                always {
                    echo "SSM stack deployment completed!"
                }
            }

            // Deploy webapp stack using appropriate IAM role
            deployStack('webapp.yaml', WEBAPP_STACK_NAME, 'CLOUDFORMATION_ROLE')
            post {
                always {
                    echo "WebApp stack deployment completed!"
                }
            }

            // Deploy database stack using appropriate IAM role
            deployStack('DB.yaml', DATABASE_STACK_NAME, 'CLOUDFORMATION_ROLE')
            post {
                always {
                    echo "Database stack deployment completed!"
                }
            }
        }
    }

    // Post-build actions
    post {
        success {
            echo "Pipeline completed successfully!"
        }
    }
}
