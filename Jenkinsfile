pipeline {
    agent any

    options {
        timestamps()
    }

    parameters {
        choice(
            name: 'SUITE',
            choices: ['smoke', 'regression'],
            description: 'Select test suite to execute'
        )
        choice(
            name: 'EXECUTION_MODE',
            choices: ['local', 'grid'],
            description: 'Run tests locally or on Selenium Grid'
        )
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Select browser'
        )
        choice(
            name: 'ENVIRONMENT',
            choices: ['QA', 'Live'],
            description: 'Select environment'
        )
        choice(
            name: 'HEADLESS',
            choices: ['true', 'false'],
            description: 'Run browser in headless mode'
        )
    }

    environment {
        XML_PATH = 'src/test/resources/xml_static_thread_for_jenkins_run'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/sohansarode/Java-Selenium-demo-with-docker-Project.git'
            }
        }

        stage('Start Selenium Grid') {
            when {
                expression { params.EXECUTION_MODE == 'grid' }
            }
            steps {
                bat '''
                docker-compose down || true
                docker-compose up -d
                '''
            }
        }

  stage('Wait for Grid') {
    when {
        expression { params.EXECUTION_MODE == 'grid' }
    }
    steps {
        bat 'ping 127.0.0.1 -n 11 > nul'
    }
}

        stage('Run Tests') {
            steps {
                script {
                    def suiteFile = "${XML_PATH}/${params.SUITE}_jenkins.xml"

                    if (params.EXECUTION_MODE == 'grid') {
                        bat """
                        echo ===== RUNNING ${params.SUITE.toUpperCase()} ON GRID =====
                        echo Browser: ${params.BROWSER}
                        echo Environment: ${params.ENVIRONMENT}
                        echo Headless: ${params.HEADLESS}
                        echo XML: ${suiteFile}

                        mvn clean test ^
                        -DsuiteXmlFile=${suiteFile} ^
                        -Denvironment=${params.ENVIRONMENT} ^
                        -Dbrowser=${params.BROWSER} ^
                        -Dheadless=${params.HEADLESS} ^
                        -DuseSeleniumGrid=yes ^
                        -DreuseBrowser=no
                        """
                    } else {
                        bat """
                        echo ===== RUNNING ${params.SUITE.toUpperCase()} LOCALLY =====
                        echo Browser: ${params.BROWSER}
                        echo Environment: ${params.ENVIRONMENT}
                        echo Headless: ${params.HEADLESS}
                        echo XML: ${suiteFile}

                        mvn clean test ^
                        -DsuiteXmlFile=${suiteFile} ^
                        -Denvironment=${params.ENVIRONMENT} ^
                        -Dbrowser=${params.BROWSER} ^
                        -Dheadless=${params.HEADLESS} ^
                        -DuseSeleniumGrid=no ^
                        -DreuseBrowser=no
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                if (params.EXECUTION_MODE == 'grid') {
                    bat 'docker-compose down || true'
                }
            }
 allure([
                includeProperties: false,
                jdk: '',
                results: [[path: 'allure-results']]
            ])
            archiveArtifacts artifacts: 'Reports/**', fingerprint: true
        }
    }
}
