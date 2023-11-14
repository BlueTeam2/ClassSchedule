def COLOR_MAP = [
    'SUCCESS': 'good', 
    'FAILURE': 'danger',
]

pipeline {
    agent any
    
    triggers {
        GenericTrigger (
            genericHeaderVariables: [
                [ key: 'X-GitHub-Event' ]
            ],
            genericVariables: [
                [ key: 'branch_b', value: '$.ref' ],
                [ key: 'commit_titles', value: '$.commits[*].title' ]
            ],
            token: 'merge',
            // regexpFilterText: 'BRANCH: $ref COMMIT_TITLES: $commit_titles END',
            // regexpFilterExpression: "BRANCH: refs/heads/master COMMIT_TITLES: .*?(Merge pull request).*? END",
            // regexpFilterText: '$branch',
            // regexpFilterExpression: '^main$',
            printContributedVariables: true  // Debug Mode
        )
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: 'test-jenkins-git-class-schedule',
                url: 'git@github.com:BlueTeam2/TestClassSchedule.git'
            }
        }

        stage('GH Poll Variables') {
            steps {
                sh '''
                    echo "GH Ref: ${ref}"
                    echo "GH Commit titles: ${commit_titles}"
                '''
            }   
        }       
        
        // stage('Run on Push (Merge)') {
        //     when {
        //         expression { ref == 'refs/heads/main' }
        //     }
        //     steps {
        //         sh '''
        //             echo 'Push (Merge) Event: Do something...'
        //         '''
        //     }
        // }

        // post {
        //     always {
        //         echo 'Slack Notifications.'
        //         slackSend channel: '#jenkinscicd',
        //             color: COLOR_MAP[currentBuild.currentResult],
        //             message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} \n More info at: ${env.BUILD_URL}, ${committer}"
        //     }
        // }
    }
}