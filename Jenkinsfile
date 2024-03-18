pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'us-east-1'
        DOCKER_CREDENTIALS_ID = 'Elora'
        K8S_NAMESPACE = 'test'
        DOCKER_IMAGE_TAG = 'Samuel'
        DOCKER_IMAGE_NAME = 'tchanela/polished'
         EKS_CLUSTER_NAME = 'dev'
        DOCKER_IMAGE_REGISTRY = 'https://index.docker.io/v1/'
        DEPLOYMENT_YAML_PATH = 'nginx-deployment.yaml'
        SERVICE_YAML_PATH = 'nginx-svc.yaml'
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
                    sh "docker pull ${DOCKER_IMAGE_NAME}:smooth"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -f Dockerfile -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withDockerRegistry([credentialsId: 'Elora', url: "${DOCKER_IMAGE_REGISTRY}"]) {
                        docker.image("${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }

        stage('Deploy to EKS') {
            steps {
                script {
                    withEnv(['PATH+EXTRA=/usr/local/bin']) {
                        // Update kubeconfig for the EKS cluster
                        sh "aws eks --region ${AWS_DEFAULT_REGION} update-kubeconfig --name ${EKS_CLUSTER_NAME}"

                        // Create the namespace if it doesn't exist
                        sh "kubectl create namespace ${K8S_NAMESPACE} --dry-run=client -o yaml | kubectl apply -f -"

                        // Apply deployment YAML
                        sh "kubectl apply -f ${DEPLOYMENT_YAML_PATH} -n ${K8S_NAMESPACE}"
                        sh "kubectl apply -f ${SERVICE_YAML_PATH} -n ${K8S_NAMESPACE}"
                    }
                }
            }
        }
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
