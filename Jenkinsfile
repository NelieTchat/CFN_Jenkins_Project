pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'us-east-1'
        DOCKER_CREDENTIALS_ID = 'Marie'
        K8S_NAMESPACE = 'prod'
        DOCKER_IMAGE_TAG = 'gracious'
        EKS_CLUSTER_NAME = 'dev'
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
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        docker.image("tchanela/elora:gracious").push("${DOCKER_IMAGE_TAG}")
                    }
                }
            }
        }

        stage('Deploy to EKS') {
            steps {
                script {
                    withEnv(['PATH+EXTRA=/usr/local/bin']) {
                        sh "aws eks --region us-east-1 update-kubeconfig --name dev"
                        sh "/usr/local/bin/kubectl get svc"
                        sh "/usr/local/bin/kubectl apply -f k8s/deployment.yaml -n ${K8S_NAMESPACE}"
                        sh "/usr/local/bin/kubectl apply -f k8s/service.yaml -n ${K8S_NAMESPACE}"
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
