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
		stage('Installing iOS Builds') {
				echo 'Installs Builds on iOS Perfecto Devices.....'			
				iOSLoad()
		}
			
		/* Install Android builds & Restart Device */
		stage('Installing Android Builds') {
			echo 'Installs Builds on Android Perfecto Devices.....'
				
			//androidLoad()
		}
		

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
	def iOSDeviceList = ["41EEF156EA10EDAB41632651F7AD2A4C4CB502ED","1C3B401545D2CDBEC9D323460D914AD7319F31D9",
		"3133BB296C46FA2250362A227BA462A56ED11A45","DD992AFA0B69A5E2C2006A7A657690476B0086FE","C37BAE1934AE7DD0AE3355F77146C7A65579CAA3"]
	
	parallel (
		"Device: ${iOSDeviceList[0]}": {	
			iosInstall(iOSDeviceList[0])
		},
		"Device: ${iOSDeviceList[1]}": {
			iosInstall(iOSDeviceList[1])
		},
		"Device: ${iOSDeviceList[2]}": {
			iosInstall(iOSDeviceList[2])
		},
		"Device: ${iOSDeviceList[3]}": {
			iosInstall(iOSDeviceList[3])
		},
		"Device: ${iOSDeviceList[4]}": {
			iosInstall(iOSDeviceList[4])
		}
	)
}

def androidLoad() {
	def androidDeviceList = ["CE091609AB24A50901","1115FBD16FEF0303","04157DF43A656B1A","05157DF5399ED633",
		"03157DF3800C0537", "30E9D3E3", "LGUS99185B89D4C"]
	
	parallel (
			"${androidDeviceList[0]}": {	
			println "${androidDeviceList[0]}"
			androidInstall(androidDeviceList[0])
		},
		"${androidDeviceList[1]}": {
			println "${androidDeviceList[1]}"
			androidInstall(androidDeviceList[1])
		},
		"${androidDeviceList[2]}": {
			println "${androidDeviceList[2]}"
			androidInstall(androidDeviceList[2])
		},
		"${androidDeviceList[3]}": {
			println "${androidDeviceList[3]}"
			androidInstall(androidDeviceList[3])
		},
		"${androidDeviceList[4]}": {
			println "${androidDeviceList[4]}"
			androidInstall(androidDeviceList[4])
		},
		"${androidDeviceList[5]}": {
			println "${androidDeviceList[5]}"
			androidInstall(androidDeviceList[5])
		},
		"${androidDeviceList[6]}": {
			println "${androidDeviceList[6]}"
			androidInstall(androidDeviceList[6])
		},
		"${androidDeviceList[7]}": {
			println "${androidDeviceList[7]}"
			androidInstall(androidDeviceList[7])
		}
	)
} 


def androidInstall(deviceList) {
	
	def username = "rajp@perfectomobile.com"
	def password = "Perfecto123"
	def cloudUrl = "ps.perfectomobile.com"
	def	DynamicFields = "Test-Version-Raj.ipa"
	def appID = "com.att.mobile.dfw"
	def appLocation = "Raj/dfw-android-1001001108.apk"
	
	/* Device Start */
	def startResponse = httpRequest url: "https://${cloudUrl}/services/executions?operation=start&user=${username}&password=${password}"
	def slurper = new groovy.json.JsonSlurperClassic()
	def startCommand = slurper.parseText(startResponse.content)
	def executionID = startCommand.executionId
	slurper=null
	println "$executionID}"

		
	/* Make device Reservation */
	
	
	
	/* Open Device Connection */
	try {
		def openResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=open&param.deviceId=" + deviceList
		println openResponse.content
	} catch (all) {
		echo 'Failed to Open Device....Catch'
		println all
	} 
	
	/* Set Dynamic Field */
	try {
		println "Setting Dynamic Field ipa/apk File Name for device:  " + deviceList
		def dynamicFiled = httpRequest url:"https://${cloudUrl}/services/handsets/"+deviceList+"?operation=update&user=${username}&password=${password}&dynamicField.ipa=${DynamicFields}"
		println dynamicFiled.content
	} catch (all) {
		echo 'Failed to Set Dynamic Field....Catch'
		println all
	}
	
	/* Uninstall Application */
	try {
	//	println "Uninstalling App for device:  " + deviceList
	//	def uninstallApp = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=application&subcommand=uninstall&param.deviceId=" + deviceList + "&param.identifier=${appID}"
	//	println uninstallApp
	} catch (all) {
		echo 'Failed to Uninstall Application....Catch'
		println all
	}
		
	/* Reboot Phone */
	try {
		println "Rebooting Device:  " + deviceList
		def rebootResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=reboot&param.deviceId=" + deviceList	
		println rebootResponse
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
		def closeResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=close&param.deviceId=" + deviceList
		println closeResponse
	} catch (all) {
		echo 'Failed to Close Device....Catch'
		println all
	}
		
	/* Destroy Device Object */
	try {
		def stopResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=end&user=${username}&password=${password}"
		println stopResponse
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
	def	DynamicFields = "Test-Version-Raj.ipa"
	def appID = "com.att.mobile.dfw"
	def appLocation = "Raj/dfw-1.0.5647_ios.ipa"
	
	/* Device Start */
	def startResponse = httpRequest url: "https://${cloudUrl}/services/executions?operation=start&user=${username}&password=${password}"
	def slurper = new groovy.json.JsonSlurperClassic()
	def startCommand = slurper.parseText(startResponse.content)
	def executionID = startCommand.executionId
	slurper=null
	println "${startCommand}"

		
	/* Make device Reservation */
	
	
	
	/* Open Device Connection */
	try {
		def openResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=open&param.deviceId=" + deviceList
		def openDeviceSlurper = new groovy.json.JsonSlurperClassic()
		def command = openDeviceSlurper.parseText(openResponse.content)
		def returnStatus = command.status
		openDevice=null
		println "${returnStatus}"
		
	} catch (all) {
		echo 'Failed to Open Device....Catch'
		println all
	}
	
	/* Set Dynamic Field */
	try {
		println "Setting Dynamic Field ipa/apk File Name for device:  " + deviceList
		def dynamicFiled = httpRequest url:"https://${cloudUrl}/services/handsets/"+deviceList+"?operation=update&user=${username}&password=${password}&dynamicField.ipa=${DynamicFields}"
		println dynamicFiled
	} catch (all) {
		echo 'Failed to Set Dynamic Field....Catch'
		println all
	}
	
	/* Uninstall Application */
	try {
	//	println "Uninstalling App for device:  " + deviceList
	//	def uninstallApp = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=application&subcommand=uninstall&param.deviceId=" + deviceList + "&param.identifier=${appID}"
	//	println uninstallApp
	} catch (all) {
		echo 'Failed to Uninstall Application....Catch'
		println all
	}
		
	/* Reboot Phone */
	try {
//		println "Rebooting Device:  " + deviceList
//		def rebootResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=reboot&param.deviceId=" + deviceList
//		println rebootResponse
	} catch (all) {
		echo 'Failed to Reboot Phone....Catch'
		println all
	}
	
	/* Install Application */
//	try {
//		println "Installing " + "${appLocation}" + " on " + deviceList
//		def installResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=application&subcommand=install&param.deviceId=" + deviceList + "&param.file=PUBLIC:${appLocation}&param.instrument=instrument"
//		println installResponse
//	} catch (all) {
//		echo 'Failed to Install Application....Catch'
//		println all
//	}
	
	/* Close Device */
	try {
		def closeResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=close&param.deviceId=" + deviceList
		println closeResponse
	} catch (all) {
		echo 'Failed to Close Device....Catch'
		println all
	}
		
	/* Destroy Device Object */
	try {
		def stopResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=end&user=${username}&password=${password}"
		println stopResponse
	} catch (all) {
		echo 'Failed to QUIT Device....Catch'
		println all
	}
	
	
	/* Delete Reservation */
	
}