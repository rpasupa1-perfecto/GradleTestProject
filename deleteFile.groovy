import javax.swing.GroupLayout.ParallelGroup

import org.springframework.jmx.export.assembler.MethodNameBasedMBeanInfoAssembler

import groovy.json.JsonSlurperClassic



node {
	
    try {
    
 		try {
			stage('Checkout') {
				checkout scm
				echo 'Checking Code out.....'
			}
		} catch (all) {
			echo 'Stage Checkout FAILED.....'
	        println all
	    }
	    
	    try {
			stage('Gradle Build') {
			   //bat './gradlew clean install -x test'
				echo 'Building Project.....'
			}
		} catch (all) {
			echo 'Stage Building Project FAILED.....'
	        println all
	    }	
		
		
		/* Install iOS builds & Restart Device */
		stage('Deleting Files from Perfecto Media Repository') {	
			echo 'Deleting Files from Perfecto Media Repository.....'			
			deleteFileFromRepository()
		}
			

		
    } catch (all) {
        println all
    }
	

}





def deleteFileFromRepository() {
	
	def username = "rajp@perfectomobile.com"
	def password = "Perfecto123"
	def cloudUrl = "ebay.perfectomobile.com"
	def media = "PUBLIC"
	//def mediaFolder = "eBayMobile-master-20170224.102121-350-enterprise.ipa"   //eg: Esressop/Folder  or iOS
	def mediaFolder = "testRaj"
	def executionID
	def responseFileData = []
	//def testresponseData = ["PUBLIC:andr/eBayMobile-5.12.0.12-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.17.0.0-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.17.0.1-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.17.0.10-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.17.0.12-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.17.0.14-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.17.0.18-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.17.0.2-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.17.0.5-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.17.0.6-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.17.0.7-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.17.0.8-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.17.0.9-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.17.1.1-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.0-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.1-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.10-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.11-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.12-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.13-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.14-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.15-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.16-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.2-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.3-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.4-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.5-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.7-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.0.9-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.1.1-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.2.0-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.18.2.1-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.0-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.1-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.10-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.11-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.12-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.13-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.14-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.15-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.16-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.17-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.3-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.6-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.7-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.8-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.19.0.9-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.0-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.1-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.10-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.11-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.13-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.14-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.15-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.16-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.17-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.18-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.19-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.2-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.20-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.3-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.4-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.5-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.7-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.8-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.20.0.9-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.1-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.10-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.12-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.13-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.14-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.15-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.16-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.17-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.18-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.19-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.3-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.4-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.7-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.8-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.21.0.9-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.22.0.0-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.22.0.1-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.22.0.10-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.22.0.11-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.22.0.2-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.22.0.3-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.22.0.4-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.22.0.6-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.22.0.7-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.22.0.8-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.22.0.9-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.0-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.1-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.10-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.11-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.12-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.13-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.14-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.16-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.17-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.18-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.2-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.3-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.4-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.5-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.6-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.7-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.8-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.0.9-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.1.0-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.2.0-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.23.2.1-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.0.0-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.0.1-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.0.11-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.0.12-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.0.13-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.0.14-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.0.2-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.0.3-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.0.4-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.0.5-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.0.6-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.0.7-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.0.8-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.0.9-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.1.1-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.2.3-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.2.5-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.24.2.7-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.25.0.0-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.25.0.1-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.25.0.10-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.25.0.11-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.25.0.12-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.25.0.13-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.25.0.15-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.25.0.2-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.25.0.3-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.25.0.4-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.25.0.5-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.25.0.6-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.25.0.8-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.25.0.9-normal-debug.apk", "PUBLIC:andr/eBayMobile-5.8.0.15-normal-debug.apk", "PUBLIC:andr/eBayMobile-feature_searchxp_speed-20180815.161625-2-normal-debug.apk", "PUBLIC:andr/eBayMobile-feature_searchxp_speed-20180815.161625-2-normal-release.apk", "PUBLIC:andr/eBayMobile-master-20180409.081858-603-normal-debug.apk", "PUBLIC:andr/eBayMobile-master-20180615.082159-652-normal-debug.apk", "PUBLIC:andr/eBayMobile-master-20180618.180107-655-normal-debug.apk", "PUBLIC:andr/eBayMobile-master-20180619.223156-657-normal-debug.apk", "PUBLIC:andr/eBayMobile-master-20180620.082352-658-normal-debug.apk", "PUBLIC:andr/eBayMobile-master-20180621.082420-659-normal-debug.apk", "PUBLIC:andr/eBayMobile-PostFix-normal-debug.apk", "PUBLIC:andr/eBayMobile-PostFix0426-normal-debug.apk", "PUBLIC:andr/eBayMobile-PreFix-normal-debug.apk", "PUBLIC:andr/eBayMobile-PreFix0426-normal-debug.apk", "PUBLIC:andr/eBayMobile-SerDeSerFix-normal-debug.apk"]

	def testresponseData = ["PUBLIC:testRaj/Personas111.png", "PUBLIC:testRaj/Personas12211.png"]
	
	/* Device Start */
	println "Start Connection with Perfecto"
	try { 
		def startResponse = httpRequest url: "https://${cloudUrl}/services/executions?operation=start&user=${username}&password=${password}"	
		executionID = getExecutionID(startResponse)
	} catch (all) {
		echo 'Failed to Start Connection with Perfecto to obtain Execution ID Test....Catch Block !!!!'
		println all
	}
	
	
	
 
	
	/* IN PROGRESSSS */
	/* Get List of item from Repository */
//	try {
//		
//			println "List of items from Repository App for device:  "			
//			def listItemRepository = httpRequest url: "https://${cloudUrl}/services/repositories/media/${media}:${mediaFolder}?operation=list&user=${username}&password=${password}"
//		
//			/* Contains a list of all files & folders in PUBLIC MEDIA */ 
//			/*  Example: [PUBLIC:NU, PUBLIC:NU/nu-mobile-app.ipa  */
//			responseFileData = getFileName(listItemRepository)
//			println responseFileData.size()
//			
//			
//					
//	} catch (all) {
//		echo 'List Items from Repository..Catch Block'
//		println all
//	}
		

	

	
//	/* Delete File */
//	for (i=1; i<responseFileData.size(); i++) {
//		try {
//		
//				println "Deleting File: " +	responseFileData[i]
//				def deletefile = httpRequest url: "https://${cloudUrl}/services/repositories/media/${responseFileData[i]}?operation=delete&user=${username}&password=${password}&admin=true"
//				printResponse(deletefile)
//			
//		} catch (all) {
//			
//			echo 'Failed to Delete File....Catch Block'
//			println all
//		}
//		
//	}
	/* Need to Delete the folder */
//	println "Deleting Folder: " +	responseFileData[0]
//	def deletefile = httpRequest url: "https://${cloudUrl}/services/repositories/media/${responseFileData[0]}?operation=delete&user=${username}&password=${password}&admin=true"
//	printResponse(deletefile)
	
	
	
		/* Delete File */
		for (i=0; i<1; i++) {
			try {
	
					println "Deleting File: " +	testresponseData[i]
					def deletefile = httpRequest url: "https://${cloudUrl}/services/repositories/media/${testresponseData[i]}?operation=delete&user=${username}&password=${password}&admin=true"
					printResponse(deletefile)
	
			} catch (all) {
	
				echo 'Failed to Delete File....Catch Block'
				println all
			}
	
		}
	
	
	
	/* Stop Reportium Test Tag */
//	println "Stop Reportium Test Tag"
//	try {
//		def status = "true"
//		def stopReportExec = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=test&subcommand=end&param.success=${status}"
//		println stopReportExec
//	} catch (all) {
//		echo 'Failed to Stop Reportium Test....Catch Block !!!!'
//		println all
//	}
//		
	/* Destroy Device Object */
	try {	
			println "End Driver with Perfecto "		
			def stopResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=end&user=${username}&password=${password}"
			printResponse(stopResponse)	
	} catch (all) {	
		echo 'Failed to QUIT Device....Catch'
		println all
	}
	
	
} 

def reportiumStepStart(executionID, stepStartName) { 
	def username = "rajp@perfectomobile.com"
	def password = "Perfecto123"
	def cloudUrl = "ultimate.perfectomobile.com"
	stepStartName = stepStartName.replaceAll(' ', '%20')
	
	 	try { 
			def stepStart = httpRequest url: "https://"+ cloudUrl + "/services/executions/" + executionID + "?operation=command&user=" + username + "&password=" + password + "&command=test&subcommand=step&param.name=" + stepStartName
			println stepStart	
	} catch (all) {  
		echo 'Failed to Step Start....Catch'    
		println all 
	} 
}
 
def reportiumStepEnd(executionID, stepEndName) {   
	def username = "rajp@perfectomobile.com"
	def password = "Perfecto123"
	def cloudUrl = "ultimate.perfectomobile.com"
	stepEndName = stepEndName.replaceAll(' ', '%20')  
	 
	try {
		def stepEnd = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=step&subcommand=end&param.message=${stepEndName}"
		println stepEnd
	} catch (all) {
		echo 'Failed to Step END....Catch' 
		println all
	}
} 

def reportiumAssert(executionID, message, status) { 
	def username = "rajp@perfectomobile.com" 
	def password = "Perfecto123"
	def cloudUrl = "ultimate.perfectomobile.com" 
	message = message.replaceAll(' ', '%20')
	
	try {
		def assertStatus = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=status&subcommand=assert&param.status=${status}&param.message=${message}"
		println assertStatus
	} catch (all) {
		echo 'Failed in Assert....Catch'
		println all
	}
}


def printResponse (response){
	def Slurper = new groovy.json.JsonSlurperClassic()
	def command = Slurper.parseText(response.content)
	Slurper=null
	println "ResponseMsg: ${command}"
	println "StatusCode:  ${response}"
}

def getExecutionID (response){
	def slurper = new groovy.json.JsonSlurperClassic()
	def startCommand = slurper.parseText(response.content)
	def executionId = startCommand.executionId
	slurper=null
	println "ResponseMsg:  ${startCommand}"
	println "StatusCode:  ${response}"
	return executionId
}


def getFileName (response) {
	def slurper = new groovy.json.JsonSlurperClassic()
	def startCommand = slurper.parseText(response.content)
	def fileItemList = startCommand.items as List
	slurper=null
	println "ResponseMsg:  ${startCommand}"
	println "ParsedOutput:  ${fileItemList}"
	return fileItemList
}



