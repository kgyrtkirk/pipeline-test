def call(parallelism, inclusionsFile, exclusionsFile, results, image, prepare, run) {
  def splits
  node {
    prepare()
    splits = splitTests parallelism: parallelism, generateInclusions: true, estimateTestsFromFiles: true
  }
  def branches = [:]
  for (int i = 0; i < splits.size(); i++) {
    def num = i
    def split = splits[num]
    branches["split${num}"] = {
      stage("Test Section #${num + 1}") {
node {
        //docker.image(image).inside {
//          stage('Preparation') 
		{
            prepare()
            writeFile file: (split.includes ? inclusionsFile : exclusionsFile), text: split.list.join("\n")
            writeFile file: (split.includes ? exclusionsFile : inclusionsFile), text: ''
          }
        //  stage('Main') 
	{
            realtimeJUnit(results) {
              run()
            }
          }
        }
      }
    }
  }
  parallel branches
}
