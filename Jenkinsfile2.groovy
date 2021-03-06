import javax.swing.GroupLayout.ParallelGroup

import org.openqa.selenium.WebElement
import org.springframework.jmx.export.assembler.MethodNameBasedMBeanInfoAssembler

import com.perfecto.reportium.client.ReportiumClient
import com.perfecto.reportium.client.ReportiumClientFactory
import com.perfecto.reportium.model.Job
import com.perfecto.reportium.model.PerfectoExecutionContext
import com.perfecto.reportium.model.Project

import groovy.json.JsonSlurperClassic
import io.appium.java_client.ios.IOSDriver


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
	def iOSDeviceList = ["41EEF156EA10EDAB41632651F7AD2A4C4CB502ED","0C2210C8EBD9A1FB421A8D0A692E6C72F85E4C9E",
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
	
	//public ReportiumClient reportiumClient
	//reportiumClient.
	
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
//		println "Rebooting Device:  " + deviceList
//		def rebootResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=reboot&param.deviceId=" + deviceList
//		printResponse(rebootResponse)
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
	
	System.getProperties().put("proxySet", "true");
	System.getProperties().put("http.proxyHost","webproxy.wlb2.nam.nsroot.net");
	System.getProperties().put("http.proxyPort","8092");
	System.getProperties().put("https.proxyHost","bcproxy.sgp.com");
	System.getProperties().put("https.proxyPort","8080");
	

	 
	/* Device Start */
	println "Start Connection with Perfecto"
	def startResponse = HttpRequest url: "https://${cloudUrl}/services/executions?operation=start&user=${username}&password=${password}"	
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
//		println "Rebooting Device:  " + deviceList
//		def rebootResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=device&subcommand=reboot&param.deviceId=" + deviceList
//		printResponse(rebootResponse)
	} catch (all) {
		echo 'Failed to Reboot Phone....Catch'
		println all
	}
	
	/* Install Application */
	try {
		println "Installing " + "${appLocation}" + " on " + deviceList
		def installResponse = httpRequest url: "https://${cloudUrl}/services/executions/${executionID}?operation=command&user=${username}&password=${password}&command=application&subcommand=install&param.deviceId=" + deviceList + "&param.file=PUBLIC:${appLocation}&param.instrument=instrument"
		printResponse(installResponse)
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


def createReportium() {
	public static IOSDriver<WebElement> driverIOS;
	PerfectoExecutionContext perfectoExecutionContext = null
	
	perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
	.withProject(new Project("Jenkins API Project", "1.0"))
	.withJob(new Job("IOS tests", 45))
	.withContextTags("Raj")
	.withWebDriver(driverIOS)
	.build()
	
	
	
	reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
}






