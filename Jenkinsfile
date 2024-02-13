pipeline {
  agent any
  environment {
    AWS_REGION = 'us-east-1'
  }
  stages {
    stage('Deploy to CloudFormation') {
      steps {
        script {
          withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'Jenkins-user', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
            // sh "aws cloudformation deploy --stack-name ssm --template-file ssm-role.yml --capabilities CAPABILITY_IAM --region \"${AWS_REGION}\""
            // aws cloudformation deploy --stack-name ssm --template-file ssm-role.yml --region us-east-1 --capabilities CAPABILITY_IAM
            sh "aws cloudformation deploy --stack-name newstack1 --template-file Networking.yml --region \"${AWS_REGION}\""
            sh "aws cloudformation deploy --stack-name App --template-file 'load balancer and Autoscaling.yml' --region \"${AWS_REGION}\""
            sh "aws cloudformation deploy --stack-name rds --template-file rds-mysql.yml --region \"${AWS_REGION}\""
            sh "aws cloudformation deploy --stack-name ssm --template-file ssm-role.yml --capabilities CAPABILITY_IAM --region \"${AWS_REGION}\""
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
  }
  }
}
















