pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {
                cleanWs()
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/iammeftah/GestionBibliotheque'
                    ]],
                    extensions: [
                        [$class: 'CleanBeforeCheckout'],
                        [$class: 'CloneOption', depth: 1, noTags: true, shallow: true, timeout: 30]
                    ]
                ])
            }
        }

        stage('Build') {
            steps {
                sh "mvn clean compile"
            }
        }

        stage('Test') {
            steps {
                script {
                    try {
                        sh "mvn -version"
                        sh "java -version"
                        sh """
                            mvn test \
                            org.jacoco:jacoco-maven-plugin:prepare-agent \
                            -Dmaven.test.failure.ignore=false \
                            -Dsurefire.useFile=false \
                            -Dmaven.test.redirectTestOutputToFile=false
                        """
                    } catch (Exception e) {
                        echo "Test stage failed with error: ${e.getMessage()}"
                        echo "Printing surefire reports if they exist:"
                        sh 'find . -name "surefire-reports" -type d -exec ls -la {} \\;'
                        throw e
                    }
                }
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('Generate Coverage Report') {
            steps {
                sh "mvn org.jacoco:jacoco-maven-plugin:report"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh "mvn sonar:sonar"
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
                sh "mvn package -DskipTests"
            }
        }

        stage('Deploy') {
            steps {
                echo 'Simulation du déploiement...'
                sh '''
                    mkdir -p target/deploy
                    cp target/*.jar target/deploy/
                '''
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