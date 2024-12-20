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
                        [$class: 'CloneOption', depth: 0, noTags: false, shallow: false, timeout: 30]
                    ],
                    changelog: true
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
                withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.login=${SONAR_TOKEN} \
                        -Dsonar.projectKey=GestionBibliotheque
                    """
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
                    subject: "✅ Build Réussi: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
                    body: """
                        Le build du projet Gestion Bibliothèque a réussi!

                        Détails:
                        - Numéro du build: ${env.BUILD_NUMBER}
                        - Nom du job: ${env.JOB_NAME}
                        - Commit: ${env.GIT_COMMIT}
                        - Branche: ${env.GIT_BRANCH}

                        Rapports:
                        - Tests: ${env.BUILD_URL}testReport/
                        - Couverture: ${env.BUILD_URL}coverage/
                        - SonarQube: http://localhost:9000/dashboard?id=${SONAR_PROJECT_KEY}

                        Voir les détails complets: ${env.BUILD_URL}
                    """,
                    to: '${EMAIL_RECIPIENTS}',
                    recipientProviders: [
                        [$class: 'DevelopersRecipientProvider'],
                        [$class: 'RequesterRecipientProvider']
                    ],
                    attachLog: true
                )
            }
            failure {
                emailext (
                    subject: "❌ Build Échoué: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
                    body: """
                        Le build du projet Gestion Bibliothèque a échoué!

                        Détails:
                        - Numéro du build: ${env.BUILD_NUMBER}
                        - Nom du job: ${env.JOB_NAME}
                        - Commit: ${env.GIT_COMMIT}
                        - Branche: ${env.GIT_BRANCH}

                        Erreur: ${currentBuild.description ?: 'Voir les logs pour plus de détails'}

                        Voir les logs complets: ${env.BUILD_URL}console
                    """,
                    to: '${EMAIL_RECIPIENTS}',
                    recipientProviders: [
                        [$class: 'DevelopersRecipientProvider'],
                        [$class: 'RequesterRecipientProvider']
                    ],
                    attachLog: true
                )
            }
            always {
                cleanWs()
            }
        }
}