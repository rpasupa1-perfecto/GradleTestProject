import groovy.json.JsonSlurperClassic

def nodeName = 'las-macbook-pro'
def id = 'lamobilehost1'
def attProjectPath = 'att'

def FAT_builds_iOS = "att/builds/iOS"
def FAT_builds_Android = "att/builds/Android"

def name = "dfw"

def VERSION_NUMBER_ANDROID
def VERSION_NUMBER_IOS

def APP_ID_ANDROID
def APP_ID_IOS

def PLATFROM_ANDROID
def PLATFROM_IOS

def TEMP_FILE = "app_versions"
def startTime
def endTime


node(nodeName) {
    try {
 
		stages {
			stage('Build') {
				sh 'mvn clean install'
			}
	
			 
		}
		
		
    } catch (all) {
        println all
    }
}