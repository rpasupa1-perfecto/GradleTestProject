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
			   bat './gradlew clean build -x test'
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
	def cloudUrl = "ultimate.perfectomobile.com"
	def media = "PUBLIC"
	def mediaFolder = "NU"
	def executionID
	def responseFileData
		
	/* Device Start */
	println "Start Connection with Perfecto"
	try {
		def startResponse = httpRequest url: "https://${cloudUrl}/services/executions?operation=start&user=${username}&password=${password}"	
		executionID = getExecutionID(startResponse)
	} catch (all) {
		echo 'Failed to Start Connection with Perfecto to obtain Execution ID Test....Catch Block !!!!'
		println all
	}
	
	
	/* Start Reportium Test Tag */
	println "Start Reportium Test Tag"
	def paramStartTestName = "JenkinsAPIexecutions"
	def TestTagNames="InstallApplication;${appID};${DynamicFields}"
	try {
		def startReportExec = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=test&subcommand=start&param.name=${paramStartTestName}&param.tags=${TestTagNames}"
		println startReportExec
	} catch (all) {
		echo 'Failed to Start Reportium Test....Catch Block !!!!'
		println all
	}
	


	
	
	
	
	
	
	/* IN PROGRESSSS */
	/* Get List of item from Repository */
	try {
		reportiumStepStart(executionID, "Get List of Items from Repository")
			println "List of items from Repository App for device:  " + deviceList
			
			def listItemRepository = httpRequest url: "https://${cloudUrl}/services/repositories/media/${media}:${mediaFolder}?operation=list&user=${username}&password=${password}"
			
			responseFileData = getFileName(listItemRepository)
			
			printResponse(responseFileData)
	} catch (all) {
		reportiumAssert(executionID, "List Items from Repository ", false)
		echo 'List Items from Repository..Catch Block'
		println all
	}
		

	
	/* Delete File */
//	try {
//		reportiumStepStart(executionID, "Delete File")
//			println "Uninstalling App for device:  " + deviceList	
//			def uninstallApp = httpRequest url: "https://${cloudUrl}/services/repositories/media/${media}:${mediaFolder}?operation=delete&user=${username}&password=${password}&admin=true"
//			printResponse(uninstallApp)
//		reportiumAssert(executionID, "Uninstalled Application", true)
//	} catch (all) {
//		reportiumAssert(executionID, "App not Uninstalled on device", false)
//		echo 'Failed to Uninstall Application..Check if app was installed..Catch Block'
//		println all
//	}
		
	
	
	
	
	
	
	
	
	
	/* Stop Reportium Test Tag */
	println "Stop Reportium Test Tag"
	try {
		def status = "true"
		def stopReportExec = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=test&subcommand=end&param.success=${status}"
		println stopReportExec
	} catch (all) {
		echo 'Failed to Stop Reportium Test....Catch Block !!!!'
		println all
	}
		
	/* Destroy Device Object */
	try {	
			println "End Device Driver with Perfecto "		
			def stopResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=end&user=${username}&password=${password}"
			printResponse(stopResponse)	
	} catch (all) {	
		echo 'Failed to QUIT Device....Catch'
		println all
	}
	
	/* Delete Reservation */
	
} 

def reportiumStepStart(executionID, stepStartName) { 
	def username = "rajp@perfectomobile.com"
	def password = "Perfecto123"
	def cloudUrl = "ps.perfectomobile.com"
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
	def cloudUrl = "ps.perfectomobile.com"
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
	def cloudUrl = "ps.perfectomobile.com" 
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
	def fileItemList = startCommand.items.PUBLIC
	slurper=null
	println "ResponseMsg:  ${startCommand}"
	println "ParsedOutput:  ${fileItemList}"
	return fileItemList
}



