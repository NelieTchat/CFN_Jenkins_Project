pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'us-east-1'
        SSH_PUBLIC_KEY = 'DevOps_key_Pair' // Consider using Jenkins Secret Text credential
        DOCKER_SECRET_TEXT_ID = 'Aimee'
        DOCKER_REGISTRY = 'hub.docker.com' // Update for your Docker registry URL
        APP_NAME = 'elora'
        K8S_NAMESPACE = 'prod'
        // DOCKER_HUB_CREDENTIALS = 'Shammel'
        DOCKER_IMAGE = 'tchanela/elora:light'
        EKS_CLUSTER_NAME = 'eksctl-dev-cluster'
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Pull and Build Docker Image') {
            steps {
                script {
                    // Pull the Docker image
                    sh "docker pull ${DOCKER_REGISTRY}/${APP_NAME}:light"
                    
                    // Build the Docker image (if necessary)
                    sh "docker build -f Dockerfile -t ${DOCKER_REGISTRY}/${APP_NAME}:gracious ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Authenticate with Docker registry
                    withCredentials([usernamePassword(credentialsId: DOCKER_SECRET_TEXT_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD} ${DOCKER_REGISTRY}"
                    }
                    
                    // Push the Docker image
                    sh "docker push ${DOCKER_REGISTRY}/${APP_NAME}:gracious"
                }
            }
        }

        stage('Deploy to EKS') {
            steps {
                script {
                    // Authenticate with AWS CLI using Jenkins credentials
                    withCredentials([awsSimpleCredentials(credentialsId: 'your_aws_credentials_id', region: AWS_DEFAULT_REGION)]) {
                        // Use AWS CLI to configure authentication for EKS
                        sh "aws eks --region ${AWS_DEFAULT_REGION} update-kubeconfig --name ${EKS_CLUSTER_NAME}"
                        
                        // Check if authentication is successful
                        sh "kubectl get svc" // Example command to verify authentication, replace with your deployment steps

                        // Apply Kubernetes deployment
                        sh "kubectl apply -f k8s/deployment.yaml -n ${K8S_NAMESPACE}"

                        // Apply Kubernetes service
                        sh "kubectl apply -f k8s/service.yaml -n ${K8S_NAMESPACE}"
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs() // Clean up workspace after each build
        }
        success {
            echo "Pipeline succeeded!"
            // Additional steps for successful build
        }
        failure {
            echo "Pipeline failed!"
            // Additional steps for failed build
        }
    }
}
