pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'us-east-1'
        DOCKER_CREDENTIALS_ID = 'Elora'
        K8S_NAMESPACE = 'test'
        DOCKER_IMAGE_TAG = 'Eliel'
        EKS_CLUSTER_NAME = 'dev'
        DOCKER_IMAGE_NAME = 'tchanela/polished'
        DOCKER_IMAGE_REGISTRY = 'https://index.docker.io/v1/'
        DEPLOYMENT_YAML_PATH = 'centos-deployment.yaml'
        SERVICE_YAML_PATH = 'centos-svc.yaml'
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
                    def awsRegion = 'us-east-1' // Specify your AWS region here
                    def ecrRepositoryUri = '767397897837.dkr.ecr.us-east-1.amazonaws.com/myk8s-webapp' // Replace with your ECR repository URI
                    
                    def dockerLoginCmd = "aws ecr get-login-password --region ${awsRegion}"
                    def dockerPassword = sh(script: dockerLoginCmd, returnStdout: true).trim()
                    
                    // Use the correct credential ID for Docker Hub here
                    withDockerRegistry([credentialsId: 'Elora', url: "https://${ecrRepositoryUri}"]) {
                        docker.image("${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }

        stage('Deploy to EKS') {
            steps {
                script {
                    withEnv(['PATH+EXTRA=/usr/local/bin']) {
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
                    sh "kubectl get pods -n ${K8S_NAMESPACE}"
                    // Add additional verification steps as needed
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
