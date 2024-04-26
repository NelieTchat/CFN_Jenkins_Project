pipeline {
    agent any
    environment {
        DOCKER_CREDENTIALS_ID = 'dockery'
        DOCKER_IMAGE_TAG = 'latest'
        DOCKER_IMAGE_NAME_REPO = 'tchanela/notes-app'
        DOCKER_IMAGE_REGISTRY = 'https://index.docker.io/v1/'
    }

    stages {
        stage('Check Path') {
            steps {
                script {
                    echo "Current working directory: ${pwd()}"
                    sh "ls"
                }
            }
        }

        stage('Clone Code') {
            steps {
                echo 'Cloning the code'
                git url: "https://github.com/NelieTchat/CFN_Jenkins_Project.git", branch: "main"
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'This is Build Stage'
                sh "docker build -t tchanela/notes-app:latest ."
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withDockerRegistry([credentialsId: 'dockery', url: "${DOCKER_IMAGE_REGISTRY}"]) {
                        docker.image("${DOCKER_IMAGE_NAME_REPO}:${DOCKER_IMAGE_TAG}").push()
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
