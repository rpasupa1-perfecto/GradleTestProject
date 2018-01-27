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

def username = "rajp@perfectomobile.com"
def password = "Perfecto123"
def cloudUrl = "ps.perfectomobile.com"
def	DynamicFields = "Test-Version-Raj.ipa"
def iOSDeviceList = ["41EEF156EA10EDAB41632651F7AD2A4C4CB502ED","1C3B401545D2CDBEC9D323460D914AD7319F31D9","3133BB296C46FA2250362A227BA462A56ED11A45","DD992AFA0B69A5E2C2006A7A657690476B0086FE","C37BAE1934AE7DD0AE3355F77146C7A65579CAA3","0C2210C8EBD9A1FB421A8D0A692E6C72F85E4C9E"]

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
	    
	    	
		stage('Installs Builds on iOS Perfecto Devices') {
				echo 'Installs Builds on iOS Perfecto Devices.....'	
			
				parallel (
				    	deviceA: {						
							/* Device Start */
							def startResponse = httpRequest url: "https://${cloudUrl}/services/executions?operation=start&user=${username}&password=${password}"
							def slurper = new groovy.json.JsonSlurperClassic()
							def startCommand = slurper.parseText(startResponse.content)
							def executionID = startCommand.executionId
							slurper=null
							println "$executionID}"
						
							/* Open Device Connection */
							try {
								def openResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=open&param.deviceId=" + iOSDeviceList[1]
								println openResponse
							} catch (all) {
								echo 'Failed to Open Device....Catch'
								println all
							}
							
							/* Set Dynamic Field */
							try {
								def dynamicFiled = httpRequest url:"https://${cloudUrl}/services/handsets/"+iOSDeviceList[1]+"?operation=update&user=${username}&password=${password}&dynamicField.ipa=${DynamicFields}"
								println dynamicFiled
							} catch (all) {
								echo 'Failed to Set Dynamic Field....Catch'
								println all
							}
						
							/* Close Device */
							try {
								def closeResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=close&param.deviceId=" + iOSDeviceList[i]
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
				
				    	},
						deviceB: {
							/* Device Start */
							def startResponse = httpRequest url: "https://${cloudUrl}/services/executions?operation=start&user=${username}&password=${password}"
							def slurper = new groovy.json.JsonSlurperClassic()
							def startCommand = slurper.parseText(startResponse.content)
							def executionID = startCommand.executionId
							slurper=null
							println "$executionID}"
		
							/* Open Device Connection */
							try {
								def openResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=open&param.deviceId=" + iOSDeviceList[0]
								println openResponse
							} catch (all) {
								echo 'Failed to Open Device....Catch'
								println all
							}
							
							/* Set Dynamic Field */
							try {
								def dynamicFiled = httpRequest url:"https://${cloudUrl}/services/handsets/"+iOSDeviceList[0]+"?operation=update&user=${username}&password=${password}&dynamicField.ipa=${DynamicFields}"
								println dynamicFiled
							} catch (all) {
								echo 'Failed to Set Dynamic Field....Catch'
								println all
							}
						
							/* Close Device */
							try {
								def closeResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=close&param.deviceId=" + iOSDeviceList[0]
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
				
				    	    
				  
				    	},
						deviceC: {			
							/* Device Start */
							def startResponse = httpRequest url: "https://${cloudUrl}/services/executions?operation=start&user=${username}&password=${password}"
							def slurper = new groovy.json.JsonSlurperClassic()
							def startCommand = slurper.parseText(startResponse.content)
							def executionID = startCommand.executionId
							slurper=null
							println "$executionID}"
						
							/* Open Device Connection */
							try {
								def openResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=open&param.deviceId=" + iOSDeviceList[2]
								println openResponse
							} catch (all) {
								echo 'Failed to Open Device....Catch'
								println all
							}
							
							/* Set Dynamic Field */
							try {
								def dynamicFiled = httpRequest url:"https://${cloudUrl}/services/handsets/"+iOSDeviceList[2]+"?operation=update&user=${username}&password=${password}&dynamicField.ipa=${DynamicFields}"
								println dynamicFiled
							} catch (all) {
								echo 'Failed to Set Dynamic Field....Catch'
								println all
							}
						
							/* Close Device */
							try {
								def closeResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=close&param.deviceId=" + iOSDeviceList[2]
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
							
							
						},
						deviceD: {
							
						},
						deviceE: {
							
						},
						deviceF: {
							
						}
				    
				    )
				    
				

			}
			
		
			stage('Pre-Production') {
				echo 'Pre-Production.....'
			}
			
			stage('Production') {
				echo 'Production.....'
			}
			

		
		
    } catch (all) {
        println all
    }
}