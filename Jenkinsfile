#!/usr/bin/env groovy

pipeline {
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    agent any
    tools {
        jdk "jdk-17.0.1"
    }
    environment {
        discordWebhook = credentials('discordWebhook')
    }
    stages {
        stage('Clean') {
            steps {
                echo 'Cleaning Project'
                sh 'chmod +x gradlew'
                sh './gradlew clean'
            }
        }
        stage('Build') {
            steps {
                echo 'Building'
                sh './gradlew build'
            }
        }
        stage('Publish') {
            when {
                anyOf {
                    branch 'main'
                }
            }
            stages {
                stage('Deploy Previews') {
                    steps {
                        echo 'Deploying previews to various places'
                        sh './gradlew publish publishToDiscord'
                    }
                }
                stage('Deploy releases') {
                    steps {
                        echo 'Maybe deploy releases'
                        sh './gradlew publishToCurseforge'
                    }
                }
            }
        }
    }
    post {
        always {
            archiveArtifacts 'Common/build/libs/**.jar'
            archiveArtifacts 'Forge/build/libs/**.jar'
            archiveArtifacts 'Fabric/build/libs/**.jar'
        }
    }
}