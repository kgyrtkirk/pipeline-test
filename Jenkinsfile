pipeline {
  //agent any
  agent { node { label 'master' } }
  stages {
    stage('build') {
      steps {
        sh 'echo Building ${BRANCH_NAME}...'
      }
    }
  }
}
