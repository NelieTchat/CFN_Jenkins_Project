pipeline {
    agent any

    environment {
        AWS_REGION = 'us-east-1'
        NETWORK_STACK_NAME = 'Dev-network-stack'
        SSM_STACK_NAME = 'Dev-ssm-stack'
        WEBAPP_STACK_NAME = 'Dev-webapp-stack'
        DATABASE_STACK_NAME = 'Dev-DB-stack'
        NETWORK_TEMPLATE_FILE = 'network.yaml'
        SSM_TEMPLATE_FILE = 'SsmRole.yaml'
        WEBAPP_STACK_FILE = 'webapp.yaml'
        DATABASE_TEMPLATE_FILE = 'Dev-DB.yaml'
        JENKINS_USERNAME_ROLE_ARN = 'arn:aws:iam::235392496232:role/Jenkins-Username-Role'
        JENKINS_PASSWORD_ROLE_ARN = 'arn:aws:iam::235392496232:role/Jenkins-Password-Role'
        CLOUDFORMATION_ROLE_ARN = 'arn:aws:iam::235392496232:role/Jenkins-CloudFormation-Role'
        // PARAMETER_STORE_PREFIX = ''
    }

    stages {
        stage('Deploy CloudFormation Stacks') {
            steps {
                script {
                    // Assume the CloudFormation IAM role and get temporary credentials
                    def assumeRoleCommand = """aws sts assume-role --role-arn ${JENKINS_CLOUDFORMATION_ROLE_ARN} --role-session-name JenkinsAssumeRoleSession --region ${AWS_REGION} --output json"""
                    def stsOutput = sh(script: assumeRoleCommand, returnStdout: true).trim()
                    def credentials = readJSON text: stsOutput

                    env.AWS_ACCESS_KEY_ID = credentials.Credentials.AccessKeyId
                    env.AWS_SECRET_ACCESS_KEY = credentials.Credentials.SecretAccessKey
                    env.AWS_SESSION_TOKEN = credentials.Credentials.SessionToken

                    echo "AWS_ACCESS_KEY_ID: ${env.AWS_ACCESS_KEY_ID}"
                    echo "AWS_SECRET_ACCESS_KEY: ${env.AWS_SECRET_ACCESS_KEY}"
                    echo "AWS_SESSION_TOKEN: ${env.AWS_SESSION_TOKEN}"

                    // Loop through stack names and deploy with temporary credentials
                    STACK_NAMES.eachWithIndex { name, index ->
                        def accessKeyId = env.AWS_ACCESS_KEY_ID
                        def secretAccessKey = env.AWS_SECRET_ACCESS_KEY
                        def sessionToken = env.AWS_SESSION_TOKEN
                        def templateFile = TEMPLATE_FILES[index]

                        sh "aws cloudformation deploy --stack-name ${name} --template-file ${templateFile} --region ${AWS_REGION} --access-key-id ${accessKeyId} --secret-access-key ${secretAccessKey} --session-token ${sessionToken}"
                    }
                }
            }
        }
    }
}
  post {
    success {
      echo 'Danielle, your Deployment succeeded!'
    }
    failure {
      echo 'Mme F, your Deployment failed!'
    }
  }


//                  def credentials = aws(credentials: 'your-aws-credentials-id', region: AWS_REGION)
//                     def username = sh(script: "aws ssm get-parameter --name ${PARAMETER_STORE_PREFIX}/MasterUsername --region ${AWS_REGION} --query Parameter.Value --output text", returnStdout: true).trim()
//                     def password = sh(script: "aws ssm get-parameter --name ${PARAMETER_STORE_PREFIX}/MasterUserPassword --region ${AWS_REGION} --with-decryption --query Parameter.Value --output text", returnStdout: true).trim()
//                     def operatorEmail = sh(script: "aws ssm get-parameter --name ${PARAMETER_STORE_PREFIX}/OperatorEmail --region ${AWS_REGION} --query Parameter.Value --output text", returnStdout: true).trim()

//                     echo "Retrieved username: ${username}"
//                     echo "Retrieved password: ${password}"
//                     echo "Retrieved operatorEmail: ${peratorEmail}"

//                     // Set environment variables for subsequent stages
//                     env.JENKINS_USERNAME = username
//                     env.JENKINS_PASSWORD = password
//                     env.OPERATOR_EMAIL = operatorEmail
//                 }
//             }
//         }

//         stage('Assume IAM Role') {
//             steps {
//                 script {
//                     // Assume the Jenkins IAM role and get temporary credentials
//                     def assumeRoleCommand = """aws sts assume-role --role-arn ${JENKINS_USERNAME_ROLE_ARN} --role-session-name JenkinsAssumeRoleSession --region ${AWS_REGION} --output json"""
//                     def stsOutput = sh(script: assumeRoleCommand, returnStdout: true).trim()
//                     def credentials = readJSON text: stsOutput

//                     env.AWS_ACCESS_KEY_ID = credentials.Credentials.AccessKeyId
//                     env.AWS_SECRET_ACCESS_KEY = credentials.Credentials.SecretAccessKey
//                     env.AWS_SESSION_TOKEN = credentials.Credentials.SessionToken

//                     echo "AWS_ACCESS_KEY_ID: ${env.AWS_ACCESS_KEY_ID}"
//                     echo "AWS_SECRET_ACCESS_KEY: ${env.AWS_SECRET_ACCESS_KEY}"
//                     echo "AWS_SESSION_TOKEN: ${env.AWS_SESSION_TOKEN}"
//                 }
//             }
//         }

//         stage('Assume Password Role') {
//             steps {
//                 script {
//                     // Assume the Jenkins password IAM role and get temporary credentials
//                     def assumeRoleCommand = """aws sts assume-role --role-arn ${JENKINS_PASSWORD_ROLE_ARN} --role-session-name JenkinsAssumeRoleSession --region ${AWS_REGION} --output json"""
//                     def stsOutput = sh(script: assumeRoleCommand, returnStdout: true).trim()
//                     def credentials = readJSON text: stsOutput

//                     env.AWS_ACCESS_KEY_ID = credentials.Credentials.AccessKeyId
//                     env.AWS_SECRET_ACCESS_KEY = credentials.Credentials.SecretAccessKey
//                     env.AWS_SESSION_TOKEN = credentials.Credentials.SessionToken

//                     echo "AWS_ACCESS_KEY_ID: ${env.AWS_ACCESS_KEY_ID}"
//                     echo "AWS_SECRET_ACCESS_KEY: ${env.AWS_SECRET_ACCESS_KEY}"
//                     echo "AWS_SESSION_TOKEN: ${env.AWS_SESSION_TOKEN}"
//                 }
//             }
//         }

//         stage('Assume CloudFormation Role') {
//             steps {
//                 script {
//                     // Assume the CloudFormation IAM role and get temporary credentials
//                     def assumeRoleCommand = """aws sts assume-role --role-arn ${CLOUDFORMATION_ROLE_ARN} --role-session-name JenkinsAssumeRoleSession --region ${AWS_REGION} --output json"""
//                     def stsOutput = sh(script: assumeRoleCommand, returnStdout: true).trim()
//                     def credentials = readJSON text: stsOutput

//                     env.AWS_ACCESS_KEY_ID = credentials.Credentials.AccessKeyId
//                     env.AWS_SECRET_ACCESS_KEY = credentials.Credentials.SecretAccessKey
//                     env.AWS_SESSION_TOKEN = credentials.Credentials.SessionToken

//                     echo "AWS_ACCESS_KEY_ID: ${env.AWS_ACCESS_KEY_ID}"
//                     echo "AWS_SECRET_ACCESS_KEY: ${env.AWS_SECRET_ACCESS_KEY}"
//                     echo "AWS_SESSION_TOKEN: ${env.AWS_SESSION_TOKEN}"
//                 }
//             }
//         }

//         stage('Deploy CloudFormation Stacks') {
//             steps {
//                 script {
//                     // Use the assumed IAM role credentials for deployment
//                     def awsAccessKeyId = env.AWS_ACCESS_KEY_ID
//                     def awsSecretAccessKey = env.AWS_SECRET_ACCESS_KEY
//                     def awsSessionToken = env.AWS_SESSION_TOKEN

//                     sh "aws cloudformation deploy --stack-name ${NETWORK_STACK_NAME} --template-file ${NETWORK_TEMPLATE_FILE} --region ${AWS_REGION} --access-key-id $awsAccessKeyId --secret-access-key $awsSecretAccessKey --session-token $awsSessionToken"
//                     sh "aws cloudformation deploy --stack-name ${WEBAPP_STACK_NAME} --template-file ${WEBAPP_STACK_FILE} --region ${AWS_REGION} --access-key-id $awsAccessKeyId --secret-access-key $awsSecretAccessKey --session-token $awsSessionToken"
//                     sh "aws cloudformation deploy --stack-name ${DATABASE_STACK_NAME} --template-file ${DATABASE_TEMPLATE_FILE} --region ${AWS_REGION} --access-key-id $awsAccessKeyId --secret-access-key $awsSecretAccessKey --session-token $awsSessionToken"
//                     sh "aws cloudformation deploy --stack-name ${SSM_STACK_NAME} --template-file ${SSM_TEMPLATE_FILE} --capabilities CAPABILITY_IAM --region ${AWS_REGION} --access-key-id $awsAccessKeyId --secret-access-key $awsSecretAccessKey --session-token $awsSessionToken"
//                 }
//             }
//         }
//     }
// }
