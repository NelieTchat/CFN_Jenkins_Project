pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'us-east-1'
        SSH_PUBLIC_KEY = 'DevOps_key_Pair'
        DOCKER_USERNAME = 'your_docker_username'
        DOCKER_PASSWORD = 'your_docker_password'
        DOCKER_CREDENTIALS_ID = 'Marie'
        DOCKER_REGISTRY = 'https://hub.docker.com/repository/docker/tchanela/elora/general' // Update for your Docker registry URL
        APP_NAME = 'elora'
        K8S_NAMESPACE = 'prod'
        DOCKER_IMAGE_TAG = 'gracious'
        EKS_CLUSTER_NAME = 'eksctl-dev-cluster'
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Pull Docker Image') {
            steps {
                script {
                    // Pull the Docker image
                    sh "docker pull tchanela/elora:light"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image (if necessary)
                    sh "docker build -f Dockerfile -t tchanela/elora:gracious ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Securely retrieve credentials from Jenkins using withCredentials block
                    withCredentials([usernamePassword(credentialsId: 'Marie', usernameVariable: 'tchanela', passwordVariable: 'PASSWORD')]) {
                        sh """
                        # Authenticate to Docker registry using retrieved credentials
                        docker login -u ${USERNAME} -p ${PASSWORD} ${DOCKER_REGISTRY}

                        # Push the built image to the registry
                        docker push ${DOCKER_REGISTRY}/${APP_NAME}:${DOCKER_IMAGE_TAG}
                        """
                    }
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
