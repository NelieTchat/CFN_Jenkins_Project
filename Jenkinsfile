pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'us-east-1'
        SSH_PUBLIC_KEY = 'DevOps_key_Pair'
        DOCKER_CREDENTIALS_ID = 'Marie' // Replace with your Docker credentials ID
        DOCKER_REGISTRY = 'https://hub.docker.com/repository/docker/tchanela/elora/general'
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
                    sh "docker pull tchanela/elora:light"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -f Dockerfile -t tchanela/elora:gracious ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Update Docker push command
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        docker.image("tchanela/elora:gracious").push("${DOCKER_IMAGE_TAG}")
                    }
                }
            }
        }

        stage('Deploy to EKS') {
            steps {
                script {
                        sh "aws eks --region ${AWS_DEFAULT_REGION} update-kubeconfig --name ${EKS_CLUSTER_NAME}"
                        sh "kubectl get svc"
                        sh "kubectl apply -f k8s/deployment.yaml -n ${K8S_NAMESPACE}"
                        sh "kubectl apply -f k8s/service.yaml -n ${K8S_NAMESPACE}"
                    }
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
