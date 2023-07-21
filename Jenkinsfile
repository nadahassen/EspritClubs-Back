pipeline {
    agent any

    stages {
        stage('Azure Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'azure-credentials', usernameVariable: 'AZ_USERNAME', passwordVariable: 'AZ_PASSWORD')]) {
                     sh "az login --username $AZ_USERNAME --password $AZ_PASSWORD"
                }

            }
        }

        stage('Create Resource Group') {
            steps {
              sh 'az group create --name myResourceGroup --location southcentralus'

            }
        }

        stage('Create AKS Cluster') {
            steps {
                 sh 'az aks create --resource-group myResourceGroup --name myAKSCluster --node-count 2'

            }
        }

        stage('Get AKS Cluster Credentials') {
            steps {
                sh 'az aks get-credentials --resource-group myResourceGroup --name myAKSCluster'

            }
        }

        stage('Test Kubernetes Connection') {
            steps {
                sh 'az login'
                sh 'kubectl --kubeconfig=/var/lib/jenkins/config --server=https://myaksclust-myresourcegroup-ce01ff-04nfpebk.hcp.southcentralus.azmk8s.io:443 get nodes'
            }
        }

        stage('GIT') {
            steps {
                git branch: 'gestionuser', url: 'https://github.com/nadahassen/EspritClubs-Back.git'
            }
        }

        stage('MVN COMPILE') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('clean') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('MVN SONAREQUBE') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.login=cff6cb5a7900b77f7b43cbe518da6c059ef2634c'
            }
        }

        stage('testing') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Nexus') {
            steps {
                sh 'mvn deploy'
            }
        }

        stage('DOCKER BUILD IMG STAGE') {
            steps {
                sh 'docker build -t nada11/espritclub-1.0 .'
            }
        }

        stage('Login Dockerhub') {
            steps {
                sh 'docker login -u nada11 -p 230801esprit'
            }
        }

        stage('Push image Backend to Dockerhub') {
            steps {
                sh 'docker push nada11/espritclub-1.0'
            }
        }

       /* stage('DOCKER COMPOSE STAGE') {
            steps {
                sh 'docker-compose up -d'
                echo 'DONE'
            }
        }*/
    }
}
