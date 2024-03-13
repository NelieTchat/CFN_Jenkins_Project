pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'us-east-1'
        EKS_CLUSTER_NAME = 'Dev'
        SSH_PUBLIC_KEY = 'DevOps_key_Pair' // Consider using Jenkins Secret Text credential
        
        DOCKER_HUB_CREDENTIALS_ID = 'Henry' // If using Docker Hub
        DOCKER_REGISTRY = 'hub.docker.com' // Update for your Docker registry URL
        APP_NAME = 'lemuel'
        K8S_NAMESPACE = 'prod'
    }

    stages {

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build and Push Docker Image (Multi-arch)') {
            steps {
                script {
                    // Build multi-architecture Docker image
                    sh "docker buildx build --platform linux/arm64,linux/amd64 -t ${DOCKER_REGISTRY}/${APP_NAME}:gracious ."

                    // Use Docker Hub credentials if applicable
                    sh "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD} ${DOCKER_REGISTRY}"

                    // Push the Docker image to the registry
                    sh "docker push ${DOCKER_REGISTRY}/${APP_NAME}:gracious"
                }
            }
        }

        stage('Deploy to EKS') {
            steps {
                script {
                    // // Update the deployment.yaml file with the correct Docker image
                    // sh "sed -i 's#image:.*#image: ${DOCKER_REGISTRY}/${APP_NAME}:latest#' k8s/deployment.yaml"

                    // Apply Kubernetes deployment
                    sh "kubectl apply -f k8s/deployment.yaml -n $K8S_NAMESPACE"

                    // Optional: Wait for deployment to be ready
                    waitForDeploymentReady()
                }
            }
        }
    }

    post {
        always {
            cleanWs() // Clean up workspace after each build
        }
        success {
            // Optional: Send notification on successful builds
        }
        failure {
            // Optional: Send notification on build failures
        }
    }

    // Function to wait for deployment to be ready
    def waitForDeploymentReady() {
        timeout(time: 5, unit: 'MINUTES') {
            // Adjust conditions based on your deployment
            waitUntil {
                return sh(script: "kubectl get deployments -n $K8S_NAMESPACE | grep ${APP_NAME} | grep -v Rolling | awk '{print \$1}' | xargs -I {} kubectl rollout status deployment {} -n $K8S_NAMESPACE | grep readyReplicas | awk '{print \$2}'", returnStatus: true) == 0
            }
        }
    }
}
