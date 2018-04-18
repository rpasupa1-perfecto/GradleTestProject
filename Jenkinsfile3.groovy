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
		stage('Installing iOS/Android Builds') {
	
				echo 'Installs Builds on iOS Perfecto Devices.....'			
				iOSLoad()
		}
			
		/* Install Android builds & Restart Device */
//		stage('Installing Android Builds') {
//			echo 'Installs Builds on Android Perfecto Devices.....'
//				
//				androidLoad()
//		}
		

		/* Running Test Cases */
		try {
			stage('Execute Test Cases') {
				echo 'Running Test Cases.....'
			}
		} catch (all) {
			echo 'Stage Running Test Case FAILED.....'
			println all
		}
		
		/* Publish Test Cases */
		try {
			stage('Publish Test Cases') {
				echo 'Publish Test Cases.....'
			}
		} catch (all) {
			echo 'Stage Publish Test Cases FAILED.....'
			println all
		}
		
    } catch (all) {
        println all
    }
	
	

}

def iOSLoad() {
	def iOSDeviceList = ["41EEF156EA10EDAB41632651F7AD2A4C4CB502ED","0C2210C8EBD9A1FB421A8D0A692E6C72F85E4C9E",
		"3133BB296C46FA2250362A227BA462A56ED11A45","DD992AFA0B69A5E2C2006A7A657690476B0086FE","C37BAE1934AE7DD0AE3355F77146C7A65579CAA3"]

	parallel (
		"Device: ${iOSDeviceList[0]}": {	
			iosInstall(iOSDeviceList[0])
		},
		"Device: ${iOSDeviceList[1]}": {
			iosInstall(iOSDeviceList[1])
		}
	//	,
//		"Device: ${iOSDeviceList[2]}": {
//			iosInstall(iOSDeviceList[2])
//		},
//		"Device: ${iOSDeviceList[3]}": {
//			iosInstall(iOSDeviceList[3])
//		},
//		"Device: ${iOSDeviceList[4]}": {
//			iosInstall(iOSDeviceList[4])
//		}
	)
}

def androidLoad() {
	def androidDeviceList = ["CE091609AB24A50901","1115FBD16FEF0303","04157DF43A656B1A","05157DF5399ED633",
		"03157DF3800C0537", "30E9D3E3", "LGUS99185B89D4C"]
	
	parallel (
		"Device: ${androidDeviceList[0]}": {	
			println "${androidDeviceList[0]}"
			androidInstall(androidDeviceList[0])
		},
		"Device: ${androidDeviceList[1]}": {
			println "${androidDeviceList[1]}"
			androidInstall(androidDeviceList[1])
		},
		"Device: ${androidDeviceList[2]}": {
			println "${androidDeviceList[2]}"
			androidInstall(androidDeviceList[2])
		},
		"Device: ${androidDeviceList[3]}": {
			println "${androidDeviceList[3]}"
			androidInstall(androidDeviceList[3])
		},
		"Device: ${androidDeviceList[4]}": {
			println "${androidDeviceList[4]}"
			androidInstall(androidDeviceList[4])
		},
		"Device: ${androidDeviceList[5]}": {
			println "${androidDeviceList[5]}"
			androidInstall(androidDeviceList[5])
		},
		"Device: ${androidDeviceList[6]}": {
			println "${androidDeviceList[6]}"
			androidInstall(androidDeviceList[6])
		}
	)
} 


def androidInstall(deviceList) {
	
	def username = "rajp@perfectomobile.com"
	def password = "Perfecto123"
	def cloudUrl = "ps.perfectomobile.com"
	def	DynamicFields = "Test-Android-Version-Raj.apk"
	def appID = "com.att.mobile.dfw"
	def appLocation = "Raj/dfw-android-1001001108.apk"
	
	/* Device Start */
	println "Start Connection with Perfecto"
	def startResponse = httpRequest url: "https://${cloudUrl}/services/executions?operation=start&user=${username}&password=${password}"
	def executionID = getExecutionID(startResponse)

	/* Make device Reservation */
	
	
	
	/* Open Device Connection */
	try {
		println "Start Device Connection with Perfecto"
		def openResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=open&param.deviceId=" + deviceList
		printResponse(openResponse)
	} catch (all) {
		echo 'Failed to Open Device....Catch'
		println all
	}
	
	/* Set Dynamic Field */
	try {
		println "Setting Dynamic Field for device:  " + deviceList
		def dynamicFiled = httpRequest url:"https://${cloudUrl}/services/handsets/"+deviceList+"?operation=update&user=${username}&password=${password}&dynamicField.ipa=${DynamicFields}"
		printResponse(dynamicFiled)
	} catch (all) {
		echo 'Failed to Set Dynamic Field....Catch'
		println all
	}
	
	/* Uninstall Application */
	try {
		println "Uninstalling App for device:  " + deviceList
		def uninstallApp = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=application&subcommand=uninstall&param.deviceId=" + deviceList + "&param.identifier=${appID}"
		printResponse(uninstallApp)
	} catch (all) {
		echo 'Failed to Uninstall Application....Catch'
		println all
	}
		
	/* Reboot Phone */
	try {
		println "Rebooting Device:  " + deviceList
		def rebootResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=reboot&param.deviceId=" + deviceList
		printResponse(rebootResponse)
	} catch (all) {
		echo 'Failed to Reboot Phone....Catch'
		println all
	}
	
	
	/* Install Application */
	try {
		println "Installing " + "${appLocation}" + " on " + deviceList
		def installResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=application&subcommand=install&param.deviceId=" + deviceList + "&param.file=PUBLIC:${appLocation}&param.instrument=instrument"
		println installResponse
	} catch (all) {
		echo 'Failed to Install Application....Catch'
		println all
	}
	
	/* Close Device */
	try {
		println "Close Device with Perfecto "
		def closeResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=close&param.deviceId=" + deviceList
		printResponse(closeResponse)
	} catch (all) {
		echo 'Failed to Close Device....Catch'
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


def iosInstall(deviceList) {
	
	def username = "rajp@perfectomobile.com"
	def password = "Perfecto123"
	def cloudUrl = "ps.perfectomobile.com"
	def	DynamicFields = "Test-iOS-Version-Raj.ipa"
	def appID = "com.att.mobile.dfw"
	def appLocation = "Raj/dfw-1.0.5647_ios.ipa"
	def executionID
	
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
	def paramStartTestName = "JenkinsAPIexecutions"
	def TestTagNames="InstallApplication;${appID};${DynamicFields}"
	try {
		def startReportExec = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=test&subcommand=start&param.name=${paramStartTestName}&param.tags=${TestTagNames}"
		println startReportExec
	} catch (all) {
		echo 'Failed to Start Reportium Test....Catch Block !!!!'
		println all
	}
	
	/* Make device Reservation */
		 
	/* Open Specific Device Connection */
	try { 
		reportiumStepStart(executionID, "Acquiring Device")	
			println "Start Device Connection with Perfecto"
			def openResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=open&param.deviceId=" + deviceList + "&param.allocation=nowait"
			printResponse(openResponse)
		reportiumAssert(${executionID}, "Acquired Device", true)
	} catch (all) {
		reportiumAssert(${executionID}, "Failed to Acquire Device", false)
		echo 'Device May be in USE ?? !!!! Failed to Open Device....Catch'
		println all
	}
	
	/* Set Dynamic Field */
	try {
		reportiumStepStart(executionID, "Set Dynamic Field")
			println "Setting Dynamic Field for device:  " + deviceList		
			def dynamicFiled = httpRequest url:"https://${cloudUrl}/services/handsets/"+deviceList+"?operation=update&user=${username}&password=${password}&dynamicField.ipa=${DynamicFields}"
			printResponse(dynamicFiled)
		reportiumAssert(${executionID}, "Dynamic Field Set", true)
	} catch (all) {
		reportiumAssert(${executionID}, "Failed to Set Dynamic Field for device", false)
		echo 'Failed to Set Dynamic Field....Catch'
		println all
	}
	
	/* Uninstall Application */
	try {
		reportiumStepStart(executionID, "Uninstall Application")
			println "Uninstalling App for device:  " + deviceList	
			def uninstallApp = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=application&subcommand=uninstall&param.deviceId=" + deviceList + "&param.identifier=${appID}"
			printResponse(uninstallApp)
		reportiumAssert(${executionID}, "Uninstalled Application", true)
	} catch (all) {
		reportiumAssert(${executionID}, "App not Uninstalled on device", false)
		echo 'Failed to Uninstall Application..Check if app was installed..Catch Block'
		println all
	}
		
	/* Reboot Phone */
	try {
	//	println "Rebooting Device:  " + deviceList
	//	def rebootResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=reboot&param.deviceId=" + deviceList
	//	printResponse(rebootResponse)
	} catch (all) {
		echo 'Failed to Reboot Phone....Catch'
		println all
	}
	
	/* Install Application */
	try {
		reportiumStepStart(executionID, "Install Application")
			println "Installing " + "${appLocation}" + " on " + deviceList		
			def installResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=application&subcommand=install&param.deviceId=" + deviceList + "&param.file=PUBLIC:${appLocation}&param.instrument=instrument"
			printResponse(installResponse)
		reportiumAssert(${executionID}, "Installed Application", true)
	} catch (all) {
		reportiumAssert(${executionID}, "App not installed on device", false)
		echo 'Failed to Install Application....Catch Block'
		println all
	}
	
	/* Close Device */
	try {
		reportiumStepStart(executionID, "Close Device")
			println "Close Device with Perfecto "		
			def closeResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=close&param.deviceId=" + deviceList
			printResponse(closeResponse)
		reportiumAssert(${executionID}, "Close Device", true)
	} catch (all) {
		reportiumAssert(${executionID}, "Device not closed", false)
		echo 'Failed to Close Device....Catch Block'
		println all
	}
	
	
	/* Destroy Device Object */
	try {
		reportiumStepStart(executionID, "Quit Driver")
			println "End Device Driver with Perfecto "		
			def stopResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=end&user=${username}&password=${password}"
			printResponse(stopResponse)
		reportiumAssert(${executionID}, "Driver Quit/Destroyed", true)
	} catch (all) {
		reportiumAssert(${executionID}, "Can't Quit Driver", false)
		echo 'Failed to QUIT Device....Catch'
		println all
	}
	
	
	/* Stop Reportium Test Tag */
	try {
		def status = "true"
		def stopReportExec = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=test&subcommand=end&param.success=${status}"
		println stopReportExec
	} catch (all) {
		echo 'Failed to Stop Reportium Test....Catch Block !!!!'
		println all
	}
	
	/* Delete Reservation */
	
}

def reportiumStepStart(executionID, stepStartName) { 
	def username = "rajp@perfectomobile.com"
	def password = "Perfecto123"
	def cloudUrl = "ps.perfectomobile.com"

	
	try {
		def stepStart = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=test&subcommand=step&param.name=\\${stepStartName}\\"
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
	
	try {
		def stepEnd = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=step&subcommand=end&param.message=\\${stepEndName}\\"
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
	
	try {
		def assertStatus = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=status&subcommand=assert&param.message=${message}&param.status=${status}"
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