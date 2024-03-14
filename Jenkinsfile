pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'us-east-1'
        SSH_PUBLIC_KEY = 'DevOps_key_Pair'
        DOCKER_USERNAME = 'your_docker_username'
        DOCKER_PASSWORD = 'your_docker_password'
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
                    // Update Docker login command
                    withCredentials([usernamePassword(credentialsId: 'Marie', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        sh """
                        echo \${PASSWORD} | docker login -u \${USERNAME} --password-stdin ${DOCKER_REGISTRY}
                        docker push ${DOCKER_REGISTRY}/${APP_NAME}:${DOCKER_IMAGE_TAG}
                        """
                    }
                }
            }
        }

        stage('Deploy to EKS') {
            steps {
                script {
                    withCredentials([awsSimpleCredentials(credentialsId: 'your_aws_credentials_id', region: AWS_DEFAULT_REGION)]) {
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
