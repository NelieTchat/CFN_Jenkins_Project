pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'us-east-1'
        DOCKER_CREDENTIALS_ID = 'Marie'
        K8S_NAMESPACE = 'test'
        DOCKER_IMAGE_TAG = 'tana'
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
                    docker build -f /Users/mbp/Documents/CFN_Jenkins_Project/webapp/Dockerfile -t tchanela/polished:tana .

                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry("${DOCKER_IMAGE_REGISTRY}", "${DOCKER_CREDENTIALS_ID}") {
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
