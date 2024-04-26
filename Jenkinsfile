pipeline {
    agent any
    environment {
        DOCKER_BUILDKIT = '1'
        DOCKER_CREDENTIALS_ID = 'dockqada'
        DOCKER_IMAGE_TAG = 'latest'
        DOCKER_IMAGE_NAME = 'notes-app'
        DOCKER_IMAGE_REGISTRY = 'https://index.docker.io/v1/'
    }

    stages {
        stage('Check Path') {
            steps {
                script {
                    echo "Current working directory: ${pwd()}"
                }
            }
        }

        stage('Clone Code') {
            steps {
                echo 'Cloning the code'
                git url: "https://github.com/NelieTchat/CFN_Jenkins_Project.git", branch: "staging"
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'This is Build Stage'
                sh "docker build -f Dockerfile -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ."
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

        stage('Deployment') {
            steps {
                echo 'Deploying container'
                sh "docker-compose -f docker-compose.yml down && docker-compose up -d"
            }
        }
    }
}
