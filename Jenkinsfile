pipeline {
    agent any

    stages {
      stage('----Git hub pull stage ---- ') {
                steps {
                    script{
                        checkout([$class: 'GitSCM' , branches: [[name: '*/gestionuser']] ,
                           userRemoteConfigs: [[
                               credentialsId: 'Github credentials',
                               url :'https://github.com/nadahassen/EspritClubs-Back.git']]])
                    }

                }
            }

         stage('MVN COMPILE') {
                steps {
                    sh 'mvn clean compile'

                }

            }
       stage('clean'){
                steps{
                    sh 'mvn clean package'

                }

            }
        stage('MVN SONAREQUBE') {
            steps {
                sh'mvn sonar:sonar -Dsonar.login=cff6cb5a7900b77f7b43cbe518da6c059ef2634c'
                }

        }
        stage('testing') {
        steps{
            sh'mvn test'
        }
        }

       stage('Nexus') {
            steps {
		      sh'mvn deploy'

            }
        }

       stage('DOCKER BUILD IMG STAGE'){

                steps{

                    script{

                        sh 'docker build -t nada11/espritclub-1.0 .'

                    }



                }

            }
            stage('Login Dockerhub') {

			steps {
				sh 'docker login -u nada11 -p 230801esprit'
			}
		}
        stage('Push image Backend to Dockerhub') {
            steps {
                  sh 'docker push nada11/espritclub-1.0:latest'

            }
        }

  stage('DOCKER COMPOSE STAGE'){
                steps{
                    script{
                        sh 'docker-compose up -d'
                    }

                }

            }
    }
}