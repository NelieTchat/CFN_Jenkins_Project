pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'us-east-1'
        SSH_PUBLIC_KEY = 'DevOps_key_Pair' // Consider using Jenkins Secret Text credential

        // # Use Jenkins Secret Credential ID for Docker Hub credentials (recommended)
        DOCKER_SECRET_TEXT_ID = 'Aimee'
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

                    // Use Docker Hub credentials if applicable (consider Secret Text)
                    sh "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD} ${DOCKER_REGISTRY}"

                    // Push the Docker image to the registry
                    sh "docker push ${DOCKER_REGISTRY}/${APP_NAME}:latest"
                }
            }
        }

        stage('Deploy to EKS') {
            steps {
                script {
                    // Define waitForDeploymentReady method
                    def waitForDeploymentReady() {
                        timeout(time: 5, unit: 'MINUTES') {
                            // Adjust conditions based on your deployment
                            waitUntil {
                                return sh(script: "kubectl get deployments -n $K8S_NAMESPACE | grep ${APP_NAME} | grep -v Rolling | awk '{print \$1}' | xargs -I {} kubectl rollout status deployment {} -n $K8S_NAMESPACE | grep readyReplicas | awk '{print \$2}'", returnStatus: true) == 0
                            }
                        }
                    }

                    // Assuming a pre-existing EKS cluster (replace with your deployment steps)
                    echo "Deploying to existing EKS cluster..."

                    // Apply Kubernetes deployment
                    waitForDeploymentReady()
                    sh "kubectl apply -f k8s/deployment.yaml -n $K8S_NAMESPACE"

                    // Other steps (optional)
                    echo "Deployment complete!"
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
}
