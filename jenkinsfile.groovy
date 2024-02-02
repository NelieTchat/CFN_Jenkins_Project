pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Update 'YOUR-CREDENTIALS-ID' with the actual ID of your Jenkins credentials
                    checkout([$class: 'GitSCM', branches: [[name: '*/master']], 
                               userRemoteConfigs: [[url: 'https://github.com/NelieTchat/CloudNet.CFN.git', 
                               credentialsId: '12345678']]])
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
