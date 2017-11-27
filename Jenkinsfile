pipeline {
    agent { docker 'maven:3.3.3' }
    stages {

        stage('build') {
            steps {
		sh 'Maven executing clean build'
                sh 'mvn clean install -DskipTests'
            }
        }

	stage('Deploy') {
            steps {
		timeout(time: 3, unit: 'MINUTES') {
                    retry(3) {
                        sh 'date'
			sh 'Starting Deployment'
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Jenkins is Done'
        }
        success {
            echo 'Completed Successfuly'
        }
        failure {
            echo 'Pipeline has FAILED'
        }
        unstable {
            echo 'Run was marked as unstable'
        }
        changed {
            echo 'This will run only if the state of the Pipeline has changed'
        }
    }

}
