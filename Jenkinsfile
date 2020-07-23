pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh 'echo Building ${BRANCH_NAME}...xxx'
        sh 'git merge origin/master'
        sh 'git branch -a'
        sh 'cat Jenkinsfile'
      }
    }
  }
}
