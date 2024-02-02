pipeline {
    agent any

    environment {
        // Define the credentials ID for GitHub access
        GIT_CREDENTIALS = credentials('12345678')
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Set up Git with credentials
                    checkout([$class: 'GitSCM', branches: [[name: '*/master']], 
                               userRemoteConfigs: [[url: 'https://github.com/NelieTchat/CloudNet.CFN.git']],
                               credentialsId: env.GIT_CREDENTIALS])
                }
            }
        }

        stage('Build') {
            steps {
                sh 'echo "Building your project"'
                // Add build steps as needed
            }
        }

        stage('Test') {
            steps {
                sh 'echo "Running tests"'
                // Add test steps as needed
            }
        }

        stage('Deploy') {
            steps {
                sh 'echo "Deploying your project"'
                // Add deployment steps as needed
            }
        }
    }

    post {
        success {
            echo 'Pipeline succeeded! You can add further actions here.'
        }
        failure {
            echo 'Pipeline failed! You can add further actions for failure handling.'
        }
    }
}
