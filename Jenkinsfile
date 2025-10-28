pipeline {
    agent {
        kubernetes {
            yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    app: jenkins-pipeline-backend-main
spec:
  containers:
    - name: docker-backend-main
      image: docker:latest
      securityContext:
        privileged: true
"""
        }
    }
    stages {
        stage('Start notify')
        {
            steps {

                withCredentials([
                string(credentialsId: 'bot_token', variable: 'BOT_TOKEN'),
                string(credentialsId: 'chatid', variable: 'CHAT_ID')
                ]) {
                    script {
                        def commitMessage = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
                        def status = 'STARTED' // Jenkins может не задавать результат явно
                        def repoName = env.GIT_URL?.tokenize('/')?.last()?.replace('.git', '') ?: 'unknown'
                        def branchName = env.BRANCH_NAME ?: 'unknown'
                        def buildTime = currentBuild.durationString.replace(' and counting', '')
                        def finishTime = new Date().format("yyyy-MM-dd HH-mm-ss", TimeZone.getTimeZone('UTC'))
                        def buildUrl = env.BUILD_URL ?: 'No URL available'
                        def emoji = '🏃'

                        def message = "${emoji} Build for *${repoName}* branch *${branchName}* was *${status}*${emoji}\n\nCommit name: ${commitMessage}\n\nCheck logs on [Jenkins](${buildUrl})."


                        sh """
                        curl -s -X POST https://api.telegram.org/bot${BOT_TOKEN}/sendMessage -d chat_id=${CHAT_ID} -d parse_mode=markdown -d text="${message}"
                        """
                    }
                }
            }

        }
        
        stage('Select Jenkinsfile') {
            steps {
                container('docker-backend-main'){
                    script {
                        if (env.BRANCH_NAME == 'develop') {
                            // Загружаем Jenkinsfile для стейджинг-среды
                            echo "Loading Jenkinsfile.dev for branch: ${env.BRANCH_NAME}"
                            load 'Jenkinsfile.dev'  // Подгрузка Jenkinsfile.dev
                        } else if (env.BRANCH_NAME == 'master') {
                            // Загружаем Jenkinsfile для продакшн-среды
                            echo "Loading Jenkinsfile.prod for branch: ${env.BRANCH_NAME}"
                            load 'Jenkinsfile.prod'  // Подгрузка Jenkinsfile.prod
                        } else if (env.BRANCH_NAME == 'pipeline_dev') {
                            // Загружаем Jenkinsfile для продакшн-среды
                            echo "Loading Jenkinsfile.check for branch: ${env.BRANCH_NAME}"
                            load './CI/Jenkins/Jenkinsfile.check'  // Подгрузка Jenkinsfile.prod
                        } else {
                            error "Unknown branch: ${env.BRANCH_NAME}"
                        }
                    }
                }   
            }
        }
    }


    post {
        always {
            withCredentials([
                string(credentialsId: 'bot_token', variable: 'BOT_TOKEN'),
                string(credentialsId: 'chatid', variable: 'CHAT_ID')
            ]) {
                script {
                    def commitMessage = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
                    def status = currentBuild.result ?: 'SUCCESS' // Jenkins может не задавать результат явно
                    def repoName = env.GIT_URL?.tokenize('/')?.last()?.replace('.git', '') ?: 'unknown'
                    def branchName = env.BRANCH_NAME ?: 'unknown'
                    def buildTime = currentBuild.durationString.replace(' and counting', '')
                    def finishTime = new Date().format("yyyy-MM-dd HH-mm-ss", TimeZone.getTimeZone('UTC'))
                    def buildUrl = env.BUILD_URL ?: 'No URL available'
                    def emoji = status == 'SUCCESS' ? '✅' : (status == 'ABORTED' ? '⚠️' : '❌')

                    

                    def message = "${emoji} Build for *${repoName}* branch *${branchName}* was *${status}**. ${emoji}\n\nCommit name: ${commitMessage}\n\nCheck logs on [Jenkins](${buildUrl})."

                    sh """
                    curl -s -X POST https://api.telegram.org/bot${BOT_TOKEN}/sendMessage -d chat_id=${CHAT_ID} -d parse_mode=markdown -d text="${message}"
                    """
                }
            }
        }
}
}