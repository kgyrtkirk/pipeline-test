println("HELLO LEO!")
pipeline {
  agent any
  //agent { node { label 'master' } }
  stages {
    stage('build') {
      steps {
        sh 'echo Building ${BRANCH_NAME}...xxx'
println "branch::: ${env.CHANGE_TARGET}"
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
