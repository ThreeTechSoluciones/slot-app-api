pipeline {

    agent any

    stages {
        stage('Debug Branch') {
            steps {
                echo "Branch: ${env.BRANCH_NAME}"
            }
        }

        stage('Clone') {
            when { branch 'dev' }
            steps {
                git branch: 'dev',
                    url: 'https://github.com/ThreeTechSoluciones/slot-app-api'
            }
        }

        stage('Get Version') {
            when { branch 'dev' }
            steps {
                script {
                    VERSION = sh(
                        script: "awk -F'[<>]' '/<parent>/ {p=1} /<\\/parent>/ {p=0} !p && /<version>/ {print \$3; exit}' pom.xml",
                        returnStdout: true
                    ).trim()

                    echo "Project version: ${VERSION}"
                }
            }
        }


        stage('Build Image') {
            when { branch 'dev' }
            steps {
                sh "docker build -t slotapp-api:${VERSION} ."
                sh "docker tag slotapp-api:${VERSION} slotapp-api:latest"
            }
        }

        stage('Deploy Dev') {
            when { branch 'dev' }
            steps {
                sh """
                cd /SlotApp/backend/development
                docker compose up -d --build
                """
            }
        }

        stage('Deploy Prod') {
            when { branch 'main' }
            steps {
                sh """
                cd /SlotApp/backend/production
                docker compose up -d --build
                """
            }
        }
    }
}