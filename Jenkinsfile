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
                sh './gradlew :Forge:build :Fabric:build'
            }
        }
        stage('Publish') {
            when { branch 'main' }
            steps {
                echo 'Deploying to Maven'
                sh './gradlew publish sendWebhook'
            }
        }
    }
    post {
        always {
            archiveArtifacts 'Forge/build/libs/**.jar'
            archiveArtifacts 'Fabric/build/libs/**.jar'
        }
    }
}