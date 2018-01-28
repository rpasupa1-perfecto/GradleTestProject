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
	    
	    	
		parallel firstBranch: {
			stage('Installs Builds on IOS Perfecto Devices') {
				
				echo 'Running IOS InstallProduction.....'
				
			}
		}, secondBranch: {
			stage('Installs Builds on Android Perfecto Devices') {
				
				echo 'Running Android InstallProduction..........'
				
			}
		}
		
		
		stage('Installs Original Builds') {
				echo 'Installs Builds on iOS Perfecto Devices.....'			
				parallel (
				    	deviceA: {						
							echo 'Building AProject.....'	
				    	},
						deviceB: {
							echo 'Building B Project.....'	 
				    	},
						deviceC: {			
							echo 'Building C Project.....'		
						},
						deviceD: {
							echo 'Building D Project.....'
						},
						deviceE: {
							echo 'Building E Project.....'
						},
						deviceF: {
							echo 'Building F Project.....'
						}		    
				    )
			}
			
	
		
		
    } catch (all) {
        println all
    }
	
	

}

def iOSLoad(iosDeviceName) {

}

def androidLoad(iosDeviceName) {

}