pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh 'echo Building ${BRANCH_NAME}...xxx'
println "branch::: ${env.BRANCH_NAME}"
sh '''#!/bin/bash -e
set
cat .git/config
git fetch origin master:refs/remotes/origin/master

git config --global user.email "you@example.com"
git config --global user.name "Your Name"
git branch -a
git merge origin/master

git branch -a
cat Jenkinsfile
'''
      }
    }
  }
}
