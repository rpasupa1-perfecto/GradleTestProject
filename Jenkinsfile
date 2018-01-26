import groovy.json.JsonSlurperClassic

def nodeName = 'raj-test-groovy'
def startTime
def endTime
def name = "dfw"
def VERSION_NUMBER_IOS
def VERSION_NUMBER_ANDROID

pipeline {
    
	stages {
		
	        stage('Build') {
				//checkout scm
	            sh 'mvn clean install'
	        }

		 
    }

	
	
	
//	try {
//		stage("Notification") {
//			def attachment = "";
//			emailext attachmentsPattern: attachment, body: 'Hi,<br/> <br/> Check out the latest Smoke run for:<br/><a href="https://dfw-directv.reporting.perfectomobile.com/dashboard?startExecutionTime[0]='+"${startTime}"+'&startExecutionTime[1]=custom&endExecutionTime[0]='+"${endTime}"+'&xtags[0]=lattice&os[0]=IOS&_groupby[0]=tags">iOS Build- '+"${name}"+'-'+"${VERSION_NUMBER_IOS}"+' on Perfecto</a><br/><a href="https://dfw-directv.reporting.perfectomobile.com/dashboard?startExecutionTime[0]='+"${startTime}"+'&startExecutionTime[1]=custom&endExecutionTime[0]='+"${endTime}"+'&xtags[0]=lattice&os[0]=ANDROID&_groupby[0]=tags">Android Build- '+"${name}"+'-'+"${VERSION_NUMBER_ANDROID}"+' on Perfecto</a> <br/><br/>From,<br/> Raj Test',
//					mimeType: 'text/html',
//					subject: '$PROJECT_NAME - Build #$BUILD_NUMBER',
//					to: '''rajp@perfectomobile.com'''
//            }
//    } catch (all) {
//        println all
//    }
	
	
}

