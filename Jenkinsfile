pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'Maven'
        JAVA_HOME = tool 'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {
                // Clean workspace before checkout
                cleanWs()

                // Git checkout for public repository
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']], // Specify your branch here
                    userRemoteConfigs: [[
                        url: 'https://github.com/iammeftah/GestionBibliotheque'  // Replace with your public repo URL
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
                sh "${MAVEN_HOME}/bin/mvn clean compile"
            }
        }

        stage('Test') {
            steps {
                script {
                    try {
                        // Print Maven and Java version for debugging
                        sh "${MAVEN_HOME}/bin/mvn -version"
                        sh "java -version"

                        // Run tests with verbose output
                        sh """
                            ${MAVEN_HOME}/bin/mvn test \
                            -Dmaven.test.failure.ignore=false \
                            -Dsurefire.useFile=false \
                            -Dmaven.test.redirectTestOutputToFile=false \
                            -X
                        """
                    } catch (Exception e) {
                        // Print detailed error information
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
                    script {
                        try {
                            jacoco(
                                execPattern: '**/target/jacoco.exec',
                                classPattern: '**/target/classes',
                                sourcePattern: '**/src/main/java'
                            )
                        } catch (Exception e) {
                            echo "JaCoCo report generation failed: ${e.getMessage()}"
                            echo "Continuing pipeline execution..."
                        }
                    }
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