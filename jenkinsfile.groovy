pipeline {
    agent any  // This means the pipeline can run on any available agent (node/agent) in the Jenkins environment

    stages {
        stage('Checkout') {
            steps {
                // This step checks out the source code from your version control system (e.g., Git)
                checkout scm
            }
        }

        stage('Build') {
            steps {
                // This is where you can define the build steps for your project
                sh 'echo "Building your project"'
                // You can add more build commands or use tools like Maven, Gradle, etc.
            }
        }

        stage('Test') {
            steps {
                // This is where you can define the test steps for your project
                sh 'echo "Running tests"'
                // You can add commands to run your tests here
            }
        }

        stage('Deploy') {
            steps {
                // This is where you can define the deployment steps for your project
                sh 'echo "Deploying your project"'
                // You can add commands to deploy your application to a server or a cloud platform
            }
        }
    }

    post {
        success {
            // This block will be executed if the pipeline succeeds
            echo 'Pipeline succeeded! You can add further actions here.'
        }
        failure {
            // This block will be executed if the pipeline fails
            echo 'Pipeline failed! You can add further actions for failure handling.'
        }
    }
}
