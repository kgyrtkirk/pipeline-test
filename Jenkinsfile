pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh 'echo Building ${BRANCH_NAME}...xxx'
sh '''#!/bin/bash -e
git config --global user.email "you@example.com"
git config --global user.name "Your Name"
git merge origin/master

git branch -a
cat Jenkinsfile
'''
      }
    }
  }
}
