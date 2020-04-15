pipeline {
    agent none
    stages {
        stage('Build') {
	        agent {
                docker {
                    image 'maven:3-jdk-11'
		            args '-v $HOME/.m2:/root/.m2'
	            }
	        }
            steps {
                checkout scm
                sh 'mvn clean compile test'
            }
        }
    }
}
