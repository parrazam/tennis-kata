pipeline {
  agent any
  tools {
    maven "maven"
    jdk "jdk11"
  }
  options {
    skipStagesAfterUnstable()
  }
  stages {
    stage('Initialize') {
      steps{
        echo "PATH = ${M2_HOME}/bin:${PATH}"
        echo "M2_HOME = /opt/maven"
      }
    }
    stage('Build') {
      steps {
        sh 'mvn clean package -Dmaven.test.skip=true'
      }
    }
    stage('Test') {
      steps {
        sh 'mvn verify'
      }
    }
  }
}
