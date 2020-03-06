
def testInParallel(parallelism, inclusionsFile, exclusionsFile, results, image, prepare, run) {
  def splits
node(POD_LABEL) {
  container('maven') {
    prepare()
    splits = splitTests parallelism: parallelism, generateInclusions: true, estimateTestsFromFiles: true
  }
}
  def branches = [:]
  for (int i = 0; i < splits.size(); i++) {
    def num = i
    def split = splits[num]
    branches["split${num}"] = {
      stage("Test #${num + 1}") {
node(POD_LABEL) {
  container('maven') {
        //docker.image(image).inside {
//          stage('Preparation') {
            prepare()
            writeFile file: (split.includes ? inclusionsFile : exclusionsFile), text: split.list.join("\n")
            writeFile file: (split.includes ? exclusionsFile : inclusionsFile), text: ''
  //        }
        //  stage('Main') {
            realtimeJUnit(results) {
              run()
            }
//          }
        }
}
      }
    }
  }
  parallel branches
}


podTemplate(
  //workspaceVolume: dynamicPVC(requestsSize: "16Gi"), 
  containers: [
    containerTemplate(name: 'maven', image: 'cloudbees/jnlp-slave-with-java-build-tools', ttyEnabled: true, command: 'cat',
        resourceRequestCpu: '500m',
        resourceLimitCpu: '1000m',
        resourceRequestMemory: '1000Mi',
        resourceLimitMemory: '3000Mi'
    ),
//    containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat'),
//    containerTemplate(name: 'golang', image: 'golang:1.8.0', ttyEnabled: true, command: 'cat')
  ], yaml:'''
spec:
  securityContext:
    fsGroup: 1000
''',
//  volumes:[persistentVolumeClaim(claimName: 'test-dynamic-volume-claim', mountPath: '/persistent')]
) {

properties([
    parameters([
        string(name: 'MULTIPLIER', defaultValue: '1', description: 'Factor by which to artificially slow down tests.'),
        string(name: 'SPLIT', defaultValue: '2', description: 'Number of buckets to split tests into.')
    ])
])

node(POD_LABEL) {
  container('maven') {
    checkout scm
    sh '''printf 'env.S="%s"' "`hostname -i`" >> /home/jenkins/agent/load.props'''
    sh '''cat /home/jenkins/agent/load.props'''
    load '/home/jenkins/agent/load.props'
    sh 'df -h'
    sh '''echo S==$S'''
    sh '''cat << EOF > rsyncd.conf
    [ws]
path = $PWD
comment = RSYNC FILES
read only = true
timeout = 300

EOF
'''
    sh '''nohup rsync --server --config=rsyncd.conf &'''
  //  sh 'ls -la /persistent'
//    sh 'git clone https://github.com/apache/hive'
  //  sh 'dd if=/dev/urandom bs=1M count=3000 of=bloat'
//    sh 'tar cf /persistent/archive.tar .'
  	//stash 'sources'
  }


stage('Testing') {
  testInParallel(count(Integer.parseInt(params.SPLIT)), 'inclusions.txt', 'exclusions.txt', 'target/surefire-reports/TEST-*.xml', 'maven:3.5.0-jdk-8', {
//    checkout scm
    sh  'rsync -rvv --stats rsync://$S/ws .'
//    sh 'tar xf /persistent/archive.tar'
    sh 'du -h --max-depth=1'
//    unstash 'sources'
  }, {
    configFileProvider([configFile(fileId: 'artifactory', variable: 'SETTINGS')]) {
      withEnv(["MULTIPLIER=$params.MULTIPLIER"]) {
        sh 'du -h --max-depth=1'
        sh 'echo mvn -s $SETTINGS -B test -Dmaven.test.failure.ignore'
      }
    }
  })
}
}

}
//jenkins/jnlp-slave:3.27-1
