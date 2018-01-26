import groovy.json.JsonSlurperClassic

def nodeName = 'las-macbook-pro'
def id = 'lamobilehost1'
def attProjectPath = 'att'

def FAT_builds_iOS = "att/builds/iOS"
def FAT_builds_Android = "att/builds/Android"

def name = "dfw"
def TOKEN = "f68ff2dabda541fc86ef870ea37d8499" //# f68ff2dabda541fc86ef870ea37d8499

def VERSION_NUMBER_ANDROID
def VERSION_NUMBER_IOS

def APP_ID_ANDROID
def APP_ID_IOS

def PLATFROM_ANDROID
def PLATFROM_IOS

def TEMP_FILE = "app_versions"
def startTime
def endTime


node {
    try {
    
 		try {
			stage('Checkout') {
				checkout scm
				sh 'mvn clean install'
				echo 'Checking Code out.....'
			}
		} catch (all) {
			echo 'Stage Checkout FAILED.....'
	        println all
	    }
	    
	    try {
			stage('Build') {
			sh 'mvn clean install'
				echo 'Building Project.....'
			}
		} catch (all) {
			echo 'Stage Building Project FAILED.....'
	        println all
	    }
	    
	    	
			stage('Execute Test') {
				echo 'Execute Test.....'
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