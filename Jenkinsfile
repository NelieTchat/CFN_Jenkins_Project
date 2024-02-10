pipeline {
    agent any

    parameters {
        string(name: 'NETWORK_STACK_NAME', defaultValue: 'Dev-network-stack', description: 'Name of the network stack')
        string(name: 'SSM_STACK_NAME', defaultValue: 'Dev-ssm-stack', description: 'Name of the ssmrole stack')
        string(name: 'DATABASE_STACK_NAME', defaultValue: 'Dev-DB-stack', description: 'Name of the database stack')
        string(name: 'WEBAPP_STACK_NAME', defaultValue: 'Dev-DB-stackk', description: 'Name of the webapp stack')
    }

    environment {
        // Access stack names from parameters:
        NETWORK_STACK_NAME = "${params.NETWORK_STACK_NAME}"
        SSM_STACK_NAME = "${params.SSM_STACK_NAME}"
        DATABASE_STACK_NAME = "${params.DATABASE_STACK_NAME}"
        WEBAPP_STACK_NAME = "${params.WEBAPP_STACK_NAME}"

        AWS_DEFAULT_REGION = 'us-east-1'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'master', url: 'https://github.com/NelieTchat/CFN_Jenkins_Project.git'
            }
        }

        stage('Build Application') {
            steps {
                // Replace with your specific build commands (e.g., mvn clean package)
                sh 'mvn clean package'
            }
        }

        stage('Validate CloudFormation Templates') {
            // when {
            //     expression {
            //         anyOf {
            //             fileExists('network.yaml') && hasChanges(file: 'network.yaml')
            //             fileExists('ssm.yaml') && hasChanges(file: 'ssm.yaml')
            //             fileExists('webapp.yaml') && hasChanges(file: 'webapp.yaml')
            //             fileExists('DB.yaml') && hasChanges(file: 'DB.yaml')
            //         }
            //     }
            // }
            steps {
                parallel {
                    stage('Validate Network Template') {
                        steps {
                            sh 'aws cloudformation validate-template --template-body file://network.yaml'
                        }
                    }
                    stage('Validate ssmRole Template') {
                        steps {
                            sh 'aws cloudformation validate-template --template-body file://ssm.yaml'
                        }
                    }
                    stage('Validate webapp Template') {
                        steps {
                            sh 'aws cloudformation validate-template --template-body file://webapp.yaml'
                        }
                    }
                    stage('Validate DB Template') {
                        steps {
                            sh 'aws cloudformation validate-template --template-body file://DB.yaml'
                        }
                    }
                }
            }
        }

        stage('Deploy Infrastructure') {
            
            // when {
            //     expression {
            //         previousStageWasSuccessful('Validate CloudFormation Templates')
            //     }
            // }
            steps {
                parallel {
                    stage('Deploy infrastructure stack') {
                        steps {
                            deployStack("network.yaml", NETWORK_STACK_NAME)
                            deployStack("ssm.yaml", SSM_STACK_NAME)
                            deployStack("webapp.yaml", WEBAPP_STACK_NAME)
                            deployStack("DB.yaml", DATABASE_STACK_NAME)
                            ssh "configure_database.sh $DATABASE_USERNAME $DATABASE_PASSWORD"
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully!"
        }
        failure {
            // Add failure notification or actions (e.g., email, Slack)
        }
    }
}

// Improved `deployStack` function:
def deployStack(String templateFile, String stackName) {
    withCredentials([
    [
        $class: 'AmazonWebServicesCredentialsBinding',
        region: '${AWS_DEFAULT_REGION}',
        roleArn: 'arn:aws:ssm:us-east-1:235392496232:parameter/MasterUsername/*'
        // secretId: '/my-app/database-username'
    ]
    ]) 
    withCredentials([
    [
        $class: 'AmazonWebServicesCredentialsBinding',
        region: '${AWS_DEFAULT_REGION}',
        roleArn: 'arn:aws:ssm:us-east-1:235392496232:parameter/MasterUserPassword/*'
        // secretId: '/my-app/database-password'
    ]
    ]) {
        withCredentials([
            [
                $class: 'AmazonWebServicesCredentialsBinding',
                region: '${AWS_DEFAULT_REGION}',
                roleArn: 'arn:aws:ssm:us-east-1:235392496232:parameter/MasterUserPassword/*'
                // secretId: '/my-app/database-password'
            ]
        ])
        script {
            // Retrieve secrets and set environment variables
            sh """
                DATABASE_USERNAME=\$(aws ssm get-parameter --name /my-app/database-username --query Parameter.Value --output text)
                DATABASE_PASSWORD=\$(aws ssm get-parameter --name /my-app/database-password --query Parameter.Value --output text)
                OPERATOR_EMAIL=\$(aws ssm get-parameter --name /my-app/operator1-email --query Parameter.Value --output text)

                export DATABASE_USERNAME="\$DATABASE_USERNAME"
                export DATABASE_PASSWORD="\$DATABASE_PASSWORD"
                export OPERATOR_EMAIL="\$OPERATOR_EMAIL"

                // Deploy stack...
            """

        }
    }
}