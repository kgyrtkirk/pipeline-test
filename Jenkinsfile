
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


podTemplate(workspaceVolume: dynamicPVC(requestsSize: "16Gi"), containers: [
    containerTemplate(name: 'maven', image: 'cloudbees/jnlp-slave-with-java-build-tools', ttyEnabled: true, command: 'cat',
        resourceRequestCpu: '1500m',
        resourceLimitCpu: '4000m',
        resourceRequestMemory: '3000Mi',
        resourceLimitMemory: '8000Mi'
    ),
//    containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat'),
//    containerTemplate(name: 'golang', image: 'golang:1.8.0', ttyEnabled: true, command: 'cat')
  ], yaml:'''
spec:
  securityContext:
    fsGroup: 1000
''') {

properties([
    parameters([
        string(name: 'MULTIPLIER', defaultValue: '1', description: 'Factor by which to artificially slow down tests.'),
        string(name: 'SPLIT', defaultValue: '2', description: 'Number of buckets to split tests into.')
    ])
])

node(POD_LABEL) {
  container('maven') {
    checkout scm
    sh 'git clone https://github.com/apache/hive'
    sh 'dd if=/dev/urandom bs=1M count=3000 of=bloat'
  	stash 'sources'
  }
}


stage('Testing') {
  testInParallel(count(Integer.parseInt(params.SPLIT)), 'inclusions.txt', 'exclusions.txt', 'target/surefire-reports/TEST-*.xml', 'maven:3.5.0-jdk-8', {
//    checkout scm
    unstash 'sources'
  }, {
    configFileProvider([configFile(fileId: 'artifactory', variable: 'SETTINGS')]) {
      withEnv(["MULTIPLIER=$params.MULTIPLIER"]) {
        sh 'echo mvn -s $SETTINGS -B test -Dmaven.test.failure.ignore'
      }
    }
  })
}

}
//jenkins/jnlp-slave:3.27-1
