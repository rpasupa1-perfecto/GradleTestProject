package org.mobile.engine;

import static org.junit.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.ITestContext;

import com.google.common.base.Function;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfectomobile.selenium.util.EclipseConnector;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

/**
 * @author rajp
 *
 */
public class BaseClass2  {
	/* RemoteWeb Driver */
	public RemoteWebDriver driver;
	/* Appium IOS Driver */
	public static IOSDriver<WebElement> driverIOS;
	/* Appium Android Driver */
	public AndroidDriver<WebElement> driverAndroid;
	public long defaultTimeout = 60;
	protected static Properties prop;
	public static HashMap<String,String> propFile;
	public Map<String, String> testParams;
	private static int[][] CHAR_LIST = new int[][] { { 0, 9 }, { 11, 13 }, {128, 255 }, {38, 39 } };
	/* Reportium Client */
	protected ReportiumClient reportiumClient;
	PerfectoExecutionContext perfectoExecutionContext = null;
	public Map<String,String> swipeData;
	
	/**
	 * @param context
	 * @description Creates the RemoteWebDriver Object
	 */
	public void engineSetup(ITestContext context) {
		
		testParams = context.getCurrentXmlTest().getAllParameters();
		
		try {
			/* Load Prop Data */
			propFileLoder(testParams.get("iOS_XpathPropfile"));
			 		
			if (testParams.get("driver").equals("RemoteWebDriver")) {
					DesiredCapabilities capabilities = new DesiredCapabilities();
					capabilities.setCapability("user", testParams.get("user"));
					capabilities.setCapability("password", testParams.get("pass"));
					capabilities.setCapability("deviceName", testParams.get("deviceName"));		
					capabilities.setCapability("browser", testParams.get("bundleID"));
					
					/* Enable Debug Mode */
					if (testParams.get("RunMode").equals("Debug")) {
						//setExecutionIdCapability(capabilities, testParams.get("URL"));
					}
					
					/* Create RemoteWebDriver Object */
			        driver = new RemoteWebDriver(new URL("https://" + testParams.get("URL") + "/nexperience/perfectomobile/wd/hub"), capabilities);
			        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
			        driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
			        
	        }  else if (testParams.get("driver").equals("AppiumIOS")) {  
	        	
	        		DesiredCapabilities capabilitiesIOS = new DesiredCapabilities();
	        		capabilitiesIOS.setCapability("user", testParams.get("user"));
	        		capabilitiesIOS.setCapability("password", testParams.get("pass"));
	        		capabilitiesIOS.setCapability("deviceName", testParams.get("deviceName"));       		
	        		if (testParams.get("bundleId") !=null && !testParams.get("bundleId").isEmpty()) {
	        			capabilitiesIOS.setCapability("bundleId", testParams.get("bundleID"));
	        		}        		
	        		capabilitiesIOS.setCapability("automationName", "Appium");
	        		        		
	        		/* Install Application */
					if (testParams.get("installApp").equals("true")) {
		        		capabilitiesIOS.setCapability("app", "PRIVATE:Raj/iPhone-1320-b1320.ipa");
		        		//capabilitiesIOS.setCapability("app", "Kohls Media:Raj/iPhone-1320-b1320.ipa");
		        		capabilitiesIOS.setCapability("autoInstrument", true);
		        		capabilitiesIOS.setCapability("fullReset", true);
		        	//	capabilitiesIOS.setCapability("autoLaunch", true);
					}
	        		
	        		/* Enable Debug Mode */
					if (testParams.get("RunMode").equals("Debug")) {
						setExecutionIdCapability(capabilitiesIOS, testParams.get("URL"));
					} //else if (testParams.get("RunMode").equals("Automation")) {

				//	int retry = 60;
	  	       //     int interval = 1000;
	  	          driverIOS = new IOSDriver<WebElement>(new URL("https://" + testParams.get("URL") + "/nexperience/perfectomobile/wd/hub"), capabilitiesIOS); 
	  	          
//	  	            while(retry > 0 && driver==null) {
//		  		    	try {
//		  					if(testParams.get("driver").equals("AppiumIOS")) {
//								driverIOS = new IOSDriver(new URL("https://" + testParams.get("URL") + "/nexperience/perfectomobile/wd/hub"), capabilitiesIOS); 
//		  					} else if (testParams.get("driver").equals("AppiumAndroid")) {
//		  						driverAndroid = new AndroidDriver(new URL("https://" + testParams.get("URL") + "/nexperience/perfectomobile/wd/hub"), capabilitiesIOS);
//		  					} 
//		  				} catch (Exception e) {
//		  					/* Decrement Retry */
//		  					retry--;
//		  					System.out.println("\nAttempted: " + (60-retry) + ". Failure to Acquire device, Retrying.....\n" + e);
//		  					sleep(interval);
//		  				}
//	  	            }  
	  	            driverIOS.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	  	            driverIOS.manage().timeouts().pageLoadTimeout(0, TimeUnit.SECONDS);
	  	            driverIOS.manage().timeouts().setScriptTimeout(0, TimeUnit.SECONDS);
	  	      	    
	  	            
	        } else if (testParams.get("driver").equals("AppiumAndroid")) {
		        	DesiredCapabilities capabilitiesAndroid = new DesiredCapabilities();
		        	capabilitiesAndroid.setCapability("user", testParams.get("user"));
		        	capabilitiesAndroid.setCapability("password", testParams.get("pass"));
		        	capabilitiesAndroid.setCapability("deviceName", testParams.get("deviceName"));
		        	/* Application Name */  
		        	capabilitiesAndroid.setCapability("appPackage", testParams.get("bundleID"));
	  	  
	  		/* Enable Debug Mode */
					if (testParams.get("RunMode").equals("Debug")) {
						setExecutionIdCapability(capabilitiesAndroid, testParams.get("URL"));
					}
					
					driverAndroid = new AndroidDriver(new URL("https://" + testParams.get("URL") + "/nexperience/perfectomobile/wd/hub"), capabilitiesAndroid);
					driverAndroid.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
					driverAndroid.manage().timeouts().pageLoadTimeout(0, TimeUnit.SECONDS);
					driverAndroid.manage().timeouts().setScriptTimeout(0, TimeUnit.SECONDS);
	  	            
	        } 
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	
	
	private static final String HTTPS = "https://";
	private static final String MEDIA_REPOSITORY = "/services/repositories/media/";
	private static final String UPLOAD_OPERATION = "operation=upload&overwrite=true";
	private static final String UTF_8 = "UTF-8";
	
	/**
	 * Uploads a file to the media repository.
	 * Example:
	 * uploadMedia("demo.perfectomobile.com", "rajp@perfectomobile.com", "123456", "C:\\test\\ApiDemos.apk", "PRIVATE:apps/ApiDemos.apk");
	 */
	public static void uploadMedia(ITestContext context, String fileLocationSourceDrive, String fileExt) {
		String host = context.getCurrentXmlTest().getParameter("URL");
		String user = context.getCurrentXmlTest().getParameter("user");
		String password = context.getCurrentXmlTest().getParameter("pass");
		String mediaRepo = context.getCurrentXmlTest().getParameter("mediaRepo");
			
		/* Get the latest ipa/apk file in Folder C:/Users/Raj/Downloads/ipaFIle  */
     //   File iOSipa2 = getLastModifiedFile(fileLocationSourceDrive, fileExt);
        
        File iOSipa2 = new File(fileLocationSourceDrive);
        
        System.out.println("File Name: " + iOSipa2.getName() + "    FilePath: " + iOSipa2.toString());    
        /* PRIVATE:Raj/file.ipa*/
        String repositoryKey = mediaRepo + iOSipa2.getName();

		try {
			File file = new File(iOSipa2.toString());
			byte[] content;
			content = readFile(file);
			uploadMedia(host, user, password, content, repositoryKey);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Uploads a file to the media repository.
	 * Example:
	 * URL url = new URL("http://file.appsapk.com/wp-content/uploads/downloads/Sudoku%20Free.apk");
	 * uploadMedia("demo.perfectomobile.com", "john@perfectomobile.com", "123456", url, "PRIVATE:apps/ApiDemos.apk");
	 */
	public static void uploadMedia(String host, String user, String password, URL mediaURL, String repositoryKey) throws IOException {
		byte[] content = readURL(mediaURL);
		uploadMedia(host, user, password, content, repositoryKey);
	}
	private static byte[] readFile(File path) throws FileNotFoundException, IOException {
		int length = (int)path.length();
		byte[] content = new byte[length];
		InputStream inStream = new FileInputStream(path);
		try {
			inStream.read(content);
		}
		finally {
			inStream.close();
		}
		return content;
	}
	/**
	 * Uploads content to the media repository.
	 * Example:
	 * uploadMedia("demo.perfectomobile.com", "john@perfectomobile.com", "123456", content, "PRIVATE:apps/ApiDemos.apk");
	 */
	public static void uploadMedia(String host, String user, String password, byte[] content, String repositoryKey) throws UnsupportedEncodingException, MalformedURLException, IOException {
		
		if (content != null) {
			String encodedUser = URLEncoder.encode(user, "UTF-8");
			String encodedPassword = URLEncoder.encode(password, "UTF-8");
			String urlStr = HTTPS + host + MEDIA_REPOSITORY + repositoryKey + "?" + UPLOAD_OPERATION + "&user=" + encodedUser + "&password=" + encodedPassword;
			URL url = new URL(urlStr);
			System.out.println(urlStr);
			sendRequest(content, url);
		}
	}
	private static void sendRequest(byte[] content, URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/octet-stream");
		connection.connect();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		outStream.write(content);
		outStream.writeTo(connection.getOutputStream());
		outStream.close();
		int code = connection.getResponseCode();
		if (code > HttpURLConnection.HTTP_OK) {
			handleError(connection);
		}
	}
	private static void handleError(HttpURLConnection connection) throws IOException {
		String msg = "Failed to upload media.";
		InputStream errorStream = connection.getErrorStream();
		if (errorStream != null) {
			InputStreamReader inputStreamReader = new InputStreamReader(errorStream, UTF_8);
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);
			try {
				StringBuilder builder = new StringBuilder();
				String outputString;
				while ((outputString = bufferReader.readLine()) != null) {
					if (builder.length() != 0) {
						builder.append("\n");
					}
					builder.append(outputString);
				}
				String response = builder.toString();
				msg += "Response: " + response;
			}
			finally {
				bufferReader.close();
			}
		}
		throw new RuntimeException(msg);
	}
	private static byte[] readURL(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setDoOutput(true);
		int code = connection.getResponseCode();
		if (code > HttpURLConnection.HTTP_OK) {
			handleError(connection);
		}
		InputStream stream = connection.getInputStream();

		if (stream == null) {
			throw new RuntimeException("Failed to get content from url " + url + " - no response stream");
		}
		byte[] content = read(stream);
		return content;
	}

	private static byte[] read(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[1024];
			int nBytes = 0;
			while ((nBytes = input.read(buffer)) > 0) {
				output.write(buffer, 0, nBytes);
			}
			byte[] result = output.toByteArray();
			return result;
		} finally {
			try{
				input.close();
			} catch (IOException e){

			}
		}
	}
	
	
	
	
	
	
	
	
	
	
		
	public  ReportiumClient createReportium(ITestContext context) {
		try {
			
			if(testParams.get("driver").equals("AppiumIOS")) {
				perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
					    .withProject(new Project("Sample Reportium project", "1.0"))
					    .withJob(new Job("IOS tests", 45))
					    .withContextTags("Raj")
					    .withWebDriver(driverIOS)
					    .build();
			} else if (testParams.get("driver").equals("AppiumAndroid")) {
				perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
					    .withProject(new Project("Sample Reportium project", "1.0"))
					    .withJob(new Job("IOS tests", 45))
					    .withContextTags("Raj")
					    .withWebDriver(driverAndroid)
					    .build();
			} else if (testParams.get("driver").equals("RemoteWebDriver")) {		
				perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
					    .withProject(new Project("Sample Reportium project", "1.0"))
					    .withJob(new Job("IOS tests", 45))
					    .withContextTags("Regression")
					    .withWebDriver(driver)
					    .build();		
			}
			
			reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
			return reportiumClient;
	}
	
	

	
  
	
	/**
	 * @param capabilities
	 * @param host
	 * @throws IOException
	 * @description Only used for debug mode
	 */
	private static void setExecutionIdCapability(DesiredCapabilities capabilities, String host) throws IOException {
	      try {
			EclipseConnector connector = new EclipseConnector();
	        String eclipseHost = connector.getHost();
	        if ((eclipseHost == null) || (eclipseHost.equalsIgnoreCase(host))) {
	            String executionId = connector.getExecutionId();
	            capabilities.setCapability(EclipseConnector.ECLIPSE_EXECUTION_ID, executionId);
	       }
	      } catch (Exception e) {
	    	  System.out.println(e);
	      }
	}
	  
	public void closeApp(String appName) {
		if(testParams.get("driver").equals("AppiumIOS")) {
			getDriverIOS().closeApp();
		} else if (testParams.get("driver").equals("AppiumAndroid")) {
			getDriverAndroid().closeApp();
		} else if (testParams.get("driver").equals("RemoteWebDriver")) {
			Map<String, Object> appParam = new HashMap<>();
			appParam.put("name", appName);
			Object result3 = driver.executeScript("mobile:application:close", appParam);
		}
		
	}
	
	/**
	 * @param propFileLoc
	 * @description Loads prop data into sysProp HashMap
	 */
	public void propFileLoder(String propFileLoc) {
		prop = new Properties();
		
		propFile = new HashMap<String, String>();
		
		try {
			prop.load(new FileInputStream(propFileLoc));
			 for (String key : prop.stringPropertyNames()) {
			      String value = prop.getProperty(key);
			      propFile.put(key, value);
			 }
			 /* Set Hash Map */
			// setPropFile(propFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	
	 
    public static File getLastModifiedFile(String filePath, String ext) {
    	 File theNewestFile = null;
         File dir = new File(filePath);
    	
         try {
	         if (dir.isDirectory()) {
			    	FileFilter fileFilter = new WildcardFileFilter("*." + ext);
			        File[] files = dir.listFiles(fileFilter);
			        
			        if (files.length > 0) {
				        Arrays.sort(files, new Comparator<File>() {
				        	
				            public int compare(File o1, File o2) {
				                return new Long(o2.lastModified()).compareTo(o1.lastModified()); //latest 1st
				            }});
				       
				        theNewestFile = files[0];
			        } else if(files.length ==0) {
			        	//return new Date(dir.lastModified());
			        	return null;
			        }
	         } else {
	        	 System.out.println("SourceInputDirectory is not a Directory....Please Check Source Directory if it exits");
	         }
         } catch (Exception e) {
        	 e.printStackTrace();
		}
         
        return theNewestFile;
    }
    

	
	public RemoteWebDriver getDriver() {
		return driver;
	}
	public void setDriver(RemoteWebDriver driver) {
		this.driver = driver;
	}
	public static HashMap<String, String> getPropFile() {
		return propFile;
	}
	public static void setPropFile(HashMap<String, String> propFile) {
		BaseClass2.propFile = propFile;
	}
	public IOSDriver<WebElement> getDriverIOS() {
		return driverIOS;
	}
	public void setDriverIOS(IOSDriver<WebElement> driverIOS) {
		this.driverIOS = driverIOS;
	}
	public AndroidDriver<WebElement> getDriverAndroid() {
		return driverAndroid;
	}
	public void setDriverAndroid(AndroidDriver<WebElement> driverAndroid) {
		this.driverAndroid = driverAndroid;
	}
	public ReportiumClient getReportiumClient() {
		return reportiumClient;
	}
	public void setReportiumClient(ReportiumClient reportiumClient) {
		this.reportiumClient = reportiumClient;
	}
	
}
