
    pipeline {
        agent any
        environment {
        DOCKER_BUILDKIT = '1'
        DOCKER_CREDENTIALS_ID = 'dockqad'
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

        stages {
            stage('Clone Code') {
                steps {
                    echo 'Cloning the code'

                    git url: "https://github.com/NelieTchat/CFN_Jenkins_Project.git", branch: "staging"
                }
            }
            stage('Build Docker Image') {
                steps {
                    echo 'This is Build Stage'  //this is the build stage
                    export DOCKER_BUILDKIT=1
                    sh "docker build -f containerization-deployment/Dockerfile -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ." //this will build the docker image
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
        
            stage('Deployement') {
                steps {
                    echo 'Deploying container'
                    sh "docker-compose -f containerization-deployment/docker-compose.yml down && docker-compose up -d"
                }
            }
        }
    }