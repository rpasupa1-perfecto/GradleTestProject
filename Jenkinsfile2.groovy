import javax.swing.GroupLayout.ParallelGroup

import org.springframework.jmx.export.assembler.MethodNameBasedMBeanInfoAssembler

import groovy.json.JsonSlurperClassic

//def nodeName = 'las-macbook-pro'
//def id = 'lamobilehost1'
//def attProjectPath = 'att'

//def FAT_builds_iOS = "att/builds/iOS"
//def FAT_builds_Android = "att/builds/Android"

//def name = "dfw"
//def TOKEN = "f68ff2dabda541fc86ef870ea37d8499" //# f68ff2dabda541fc86ef870ea37d8499

//def VERSION_NUMBER_ANDROID
//def VERSION_NUMBER_IOS

//def APP_ID_ANDROID
//def APP_ID_IOS
//
//def PLATFROM_ANDROID
//def PLATFROM_IOS
//
//def TEMP_FILE = "app_versions"
//def startTime
//def endTime



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
				
			androidLoad()
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
		"${iOSDeviceList[0]}": {	
			println "${iOSDeviceList[0]}"
			iosAndroidInstall(iOSDeviceList[0])
		},
		"${iOSDeviceList[1]}": {
			println "${iOSDeviceList[1]}"
			iosAndroidInstall(iOSDeviceList[1])
		},
		"${iOSDeviceList[2]}": {
			println "${iOSDeviceList[2]}"
			iosAndroidInstall(iOSDeviceList[2])
		},
		"${iOSDeviceList[3]}": {
			println "${iOSDeviceList[3]}"
			iosAndroidInstall(iOSDeviceList[3])
		},
		"${iOSDeviceList[4]}": {
			println "${iOSDeviceList[4]}"
			iosAndroidInstall(iOSDeviceList[4])
		}
	)
}

def androidLoad() {
	def androidDeviceList = ["04157DF4E959AA15","CE091609AB24A50901","1115FBD16FEF0303","04157DF43A656B1A","05157DF5399ED633",
		"03157DF3800C0537", "30E9D3E3", "LGUS99185B89D4C"]
	
	parallel (
			"${androidDeviceList[0]}": {	
			println "${androidDeviceList[0]}"
			iosAndroidInstall(androidDeviceList[0])
		},
		"${androidDeviceList[1]}": {
			println "${androidDeviceList[1]}"
			iosAndroidInstall(androidDeviceList[1])
		},
		"${androidDeviceList[2]}": {
			println "${androidDeviceList[2]}"
			iosAndroidInstall(androidDeviceList[2])
		},
		"${androidDeviceList[3]}": {
			println "${androidDeviceList[3]}"
			iosAndroidInstall(androidDeviceList[3])
		},
		"${androidDeviceList[4]}": {
			println "${androidDeviceList[4]}"
			iosAndroidInstall(androidDeviceList[4])
		},
		"${androidDeviceList[5]}": {
			println "${androidDeviceList[5]}"
			iosAndroidInstall(androidDeviceList[5])
		},
		"${androidDeviceList[6]}": {
			println "${androidDeviceList[6]}"
			iosAndroidInstall(androidDeviceList[6])
		},
		"${androidDeviceList[7]}": {
			println "${androidDeviceList[7]}"
			iosAndroidInstall(androidDeviceList[7])
		}
	)
} 


def iosAndroidInstall(deviceList) {
	
	def username = "rajp@perfectomobile.com"
	def password = "Perfecto123"
	def cloudUrl = "ps.perfectomobile.com"
	def	DynamicFields = "Test-Version-Raj.ipa"
	
	/* Device Start */
	def startResponse = httpRequest url: "https://${cloudUrl}/services/executions?operation=start&user=${username}&password=${password}"
	def slurper = new groovy.json.JsonSlurperClassic()
	def startCommand = slurper.parseText(startResponse.content)
	def executionID = startCommand.executionId
	slurper=null
	println "$executionID}"

	/* Open Device Connection */
	try {
		def openResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=open&param.deviceId=" + deviceList
		println openResponse
	} catch (all) {
		echo 'Failed to Open Device....Catch'
		println all
	} 
	
	/* Set Dynamic Field */
	try {
		def dynamicFiled = httpRequest url:"https://${cloudUrl}/services/handsets/"+deviceList+"?operation=update&user=${username}&password=${password}&dynamicField.ipa=${DynamicFields}"
		println dynamicFiled
	} catch (all) {
		echo 'Failed to Set Dynamic Field....Catch'
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
	
}