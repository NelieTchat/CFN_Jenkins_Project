pipeline {
  agent any

  environment {
    AWS_DEFAULT_REGION = 'us-east-1'
    K8S_NAMESPACE = 'slick'
    DOCKER_IMAGE_TAG = 'ViaDora'
    AWS_ACCOUNT_ID = '767397897837'
    EKS_CLUSTER_NAME = 'dev'  
    IMAGE_REPO_NAME = 'slam'  
    REPOSITORY_URI = '${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com'
    DEPLOYMENT_YAML_PATH = 'centos-deployment.yaml'
    SERVICE_YAML_PATH = 'centos-svc.yaml'
  }

  stages {
    stage('Checkout Code') {
      steps {
        checkout scm
      }
    }

    stage('Build Docker Image') {
      steps {
        script {
          sh "docker build -t ${IMAGE_REPO_NAME}:${DOCKER_IMAGE_TAG} ."
        }
      }
    }

    stage('Logging into AWS ECR') {
      steps {
        script {
          sh """
            aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${REPOSITORY_URI}
          """
        }
      }
    }

    stage('Pushing to ECR') {
      steps {
        script {
          sh """
            docker tag ${IMAGE_REPO_NAME}:${DOCKER_IMAGE_TAG} ${REPOSITORY_URI}/${IMAGE_REPO_NAME}:${DOCKER_IMAGE_TAG}
          """
          sh """
            docker push ${REPOSITORY_URI}/${IMAGE_REPO_NAME}:${DOCKER_IMAGE_TAG}
          """
        }
      }
    }

    stage('Deploy to EKS') {
      steps {
        script {
        //   withEnv(['PATH+EXTRA=/usr/local/bin']) {
            // Update kubeconfig for the EKS cluster (assuming AWS CLI is configured)
            sh "aws eks --region ${AWS_DEFAULT_REGION} update-kubeconfig --name ${EKS_CLUSTER_NAME}"

            // Create the namespace if it doesn't exist
            sh "kubectl create namespace ${K8S_NAMESPACE} --dry-run=client -o yaml | kubectl apply -f -"

            // Apply deployment YAML
            sh "kubectl apply -f ${DEPLOYMENT_YAML_PATH} -n ${K8S_NAMESPACE}"
            sh "kubectl apply -f ${SERVICE_YAML_PATH} -n ${K8S_NAMESPACE}"
          }
        }
      }
    // }

    stage('Verify Deployment') {
      steps {
        script {
          sh "kubectl get nodes"
          sh "kubectl get pods -n ${K8S_NAMESPACE}"
          sh "kubectl get deployments -n ${K8S_NAMESPACE}"
          sh "kubectl get services -n ${K8S_NAMESPACE}"
          // Additional verification steps can be added here
        }
      }
    }
  }

  post {
    always {
      cleanWs()
    }
    success {
      echo "Pipeline succeeded!"
    }
    failure {
      echo "Pipeline failed!"
    }
  }
}
