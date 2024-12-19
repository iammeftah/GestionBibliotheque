pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'Maven'
        JAVA_HOME = tool 'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean compile"
            }
        }

        stage('Test') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn test"
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java'
                    )
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh "${MAVEN_HOME}/bin/mvn sonar:sonar"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Package') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn package -DskipTests"
            }
        }

        stage('Deploy') {
            steps {
                echo 'Simulation du déploiement...'
                sh 'mkdir -p target/deploy'
                sh 'cp target/*.jar target/deploy/'
            }
        }
    }

    post {
        success {
            emailext (
                subject: "Build Réussi: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
                body: "Le build a réussi. \n\nVoir: ${env.BUILD_URL}",
                recipientProviders: [[$class: 'DevelopersRecipientProvider']]
            )
        }
        failure {
            emailext (
                subject: "Build Échoué: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
                body: "Le build a échoué. \n\nVoir: ${env.BUILD_URL}",
                recipientProviders: [[$class: 'DevelopersRecipientProvider']]
            )
        }
        always {
            cleanWs()
        }
    }
}