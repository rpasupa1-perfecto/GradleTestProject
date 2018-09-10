package org.mobile.engine;

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
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.google.common.base.Function;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
import com.perfectomobile.selenium.util.EclipseConnector;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

/**
 * @author rajp
 *
 */
public abstract class BaseClass  {
	/* RemoteWeb Driver */
	public RemoteWebDriver driver;
	/* Appium IOS Driver */
	public static IOSDriver<WebElement> driverIOS;
	/* Appium Android Driver */
	public AndroidDriver<WebElement> driverAndroid;
	
	public AppiumDriver<WebElement> appiumDriver;
	
	public long defaultTimeout = 60;
	protected static Properties prop;
	public static HashMap<String,String> propFile;
	public Map<String, String> testParams;
	private static int[][] CHAR_LIST = new int[][] { { 0, 9 }, { 11, 13 }, {128, 255 }, {38, 39 } };
	/* Reportium Client */
	public ReportiumClient reportiumClient;
	PerfectoExecutionContext perfectoExecutionContext = null;
	public Map<String,String> swipeData;
	String reportURL;
	public DesiredCapabilities capabilitiesIOS = new DesiredCapabilities();
	
	public HashMap<Integer,Long> hm1 = new HashMap<Integer,Long>();
	public HashMap<Integer,Long> hm2 = new HashMap<Integer,Long>();
	public HashMap<Integer,Long> hm3 = new HashMap<Integer,Long>();
	public Float sampleTest1 = 0f;
	public Float sampleTest2 = 0f;
	public Float sampleTest3 = 0f;
	
	long startTime=0;
	long stopTime=0;
	
	@BeforeClass(alwaysRun = true)
    public void setUp(ITestContext context) throws Exception {	
		engineSetup (context);		
		driver = getDriver();
		driverIOS = getDriverIOS();
		driverAndroid = getDriverAndroid();		
		createReportium(context);
		
	}
	
	@BeforeMethod(alwaysRun = true) 
	public void beforeMethod(ITestContext context) {
		
		getReportiumClient().testStart(testParams.get("PerfectoReportTestCaseName"), new TestContext());
	}
	
	

	@AfterMethod(alwaysRun = true)
	public void destroy(ITestContext context, ITestResult result) throws Exception {
			 if ( reportiumClient!= null )
               {
					reportiumClient.testStop( result.getStatus() == ITestResult.SUCCESS ? TestResultFactory.createSuccess() : 
						TestResultFactory.createFailure( result.getThrowable().getMessage(), result.getThrowable() ) );
					
					 // Print the report url to the console		
//				int resultStatus = result.getStatus();
//				switch(resultStatus) {
//					case result.FAILURE:
//						reportiumClient.testStop(TestResultFactory.createFailure("An Error Occured", result.getThrowable()));
//						break;
//					case result.SUCCESS:
//						reportiumClient.testStop(TestResultFactory.createSuccess());
//						break;
//					default:
//						throw new ReportiumException("Unexpected Status: " + resultStatus);
//				}
//				
				//	reportiumClient.testStop(TestResultFactory.createSuccess() : 
				//		TestResultFactory.createFailure( result.getThrowable().getMessage(), result.getThrowable() ) );
               }
	}
	
	@AfterClass(alwaysRun = true)
	public void afterClass() {		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy_hh_mm_SSSa");
		String formattedDate = sdf.format(date);
		System.out.println(formattedDate); 
			
		try {
			if(testParams.get("driver").equals("AppiumIOS")) {
				driverIOS.closeApp();	
				driverIOS.close();
				//downloadReport(driverIOS,"pdf", "C:/eclipse/workspace/GradleSampleProject/perfectoReports/" + context.getName() + "_" + formattedDate);
				//reportURL=reportiumClient.getReportUrl();
			//	System.out.println("\n\nReport url = " + reportiumClient.getReportUrl() +"\n\n");
				 
			} else if (testParams.get("driver").equals("AppiumAndroid")) {
				getDriverAndroid().close();
			} else if (testParams.get("driver").equals("RemoteWebDriver")) {			
				driver.close();					
			}
		} catch (Exception e)  {
			e.printStackTrace();
		} finally {
			if(testParams.get("driver").equals("AppiumIOS")) {
				driverIOS.quit();
			} else if (testParams.get("driver").equals("AppiumAndroid")) {
				driverAndroid.quit();
			} else if (testParams.get("driver").equals("RemoteWebDriver")) {
				driver.quit();	
			}
			
		}
		reportURL=reportiumClient.getReportUrl();
		
		System.out.println("\n\nPerfectoReportUrl = " + reportiumClient.getReportUrl());
	}
	
	
	
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
	        	
	        		//DesiredCapabilities capabilitiesIOS = new DesiredCapabilities();
	        		capabilitiesIOS.setCapability("user", testParams.get("user"));
	        		capabilitiesIOS.setCapability("password", testParams.get("pass"));
	        		capabilitiesIOS.setCapability("deviceName", testParams.get("deviceName"));
	        		capabilitiesIOS.setCapability("bundleId", testParams.get("bundleID"));
	        		//capabilitiesIOS.setCapability("automationName", "XCUITest");
	        		capabilitiesIOS.setCapability("automationInfrastructure", "XCUITest");
	        //		capabilitiesIOS.setCapability("autoWebview", true);
	        //		capabilitiesIOS.setCapability("automationName", "Appium");
//	        		capabilitiesIOS.setCapability("autoLaunch", true);
	        		
	        		/* Install Application */
					if (testParams.get("installApp").equals("true")) {
		        		capabilitiesIOS.setCapability("app", "PUBLIC:Lyric/Lyric_IOS_3_7.ipa");
		        		//capabilitiesIOS.setCapability("app", "Kohls Media:Raj/iPhone-1320-b1320.ipa");
		        		capabilitiesIOS.setCapability("autoInstrument", true);
		        	//	capabilitiesIOS.setCapability("fullReset", true);
		        	
					}
	        		
	        		/* Enable Debug Mode */
					if (testParams.get("RunMode").equals("Debug")) {
						setExecutionIdCapability(capabilitiesIOS, testParams.get("URL"));
					} //else if (testParams.get("RunMode").equals("Automation")) {

					int retry = 60;
	  	       //     int interval = 1000;
					

					//Capabilities capabilities = driver.getCapabilities();
					
					//driverAndroid.getPlatformName().equals(SupportDevice)
					
					
			//		driver = new RemoteWebDriver(new URL("https://" + testParams.get("URL") + "/nexperience/perfectomobile/wd/hub"), capabilitiesIOS);		
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
		  				//	System.out.println("\nAttempted: " + (60-retry) + ". Failure to Acquire device, Retrying.....\n" + e);
		  				//	sleep(interval);
//		  				}
//	  	            }  
	  	    //        driverIOS.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  	    //        driverIOS.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
	  	    //        driverIOS.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
	  	      	    
	  	            
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
					
					//capabilities.setCapability("autoWebview", true);
					driverAndroid = new AndroidDriver<WebElement>(new URL("https://" + testParams.get("URL") + "/nexperience/perfectomobile/wd/hub"), capabilitiesAndroid);
					driverAndroid.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
					driverAndroid.manage().timeouts().pageLoadTimeout(0, TimeUnit.SECONDS);
					driverAndroid.manage().timeouts().setScriptTimeout(0, TimeUnit.SECONDS);
	  	            
	        } 
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void startappAndroid(String appName) {
		Map<String, Object> params1 = new HashMap<>();
		params1.put("identifier", appName);
		Object result1 = driverAndroid.executeScript("mobile:application:open", params1);
	}
	
	public void startappIOS(String appName) {
		Map<String, Object> params1 = new HashMap<>();
		params1.put("identifier", appName);
		Object result1 = driverIOS.executeScript("mobile:application:open", params1);
	}
	
	
	public void closeAppAndroid(String appName) {
		Map<String, Object> params1 = new HashMap<>();
		params1.put("identifier", appName);
		Object result1 = driverAndroid.executeScript("mobile:application:close", params1);
	}
//	public void closeApp() {
//		Map<String, Object> params2 = new HashMap<>();
//		params2.put("identifier", "com.att.mobile.dfw");
//		Object result2 = driverIOS.executeScript("mobile:application:close", params2);
//	}
	
	
	public Float TotalSum(HashMap<Integer, Long> hm3) {	
		for (Long val : hm3.values()){
			sampleTest1 += val;
		}
		return sampleTest1;
	}
	public long startTime() {
		startTime = System.currentTimeMillis();
		return startTime;
	}
	public long stopTime() {
		stopTime = System.currentTimeMillis();
		return stopTime;
	}
	
	public void StartVNetwork() throws InterruptedException {
		reportiumClient.stepStart("StartVNetwork");
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("generateHarFile", "true");
		Object result1 = driverIOS.executeScript("mobile:vnetwork:start", params1);
		reportiumClient.stepEnd("StartVNetwork");
	}
	
	public void StopVNetwork() {
		reportiumClient.stepStart("StopVNetwork");
		Map<String, Object> params3 = new HashMap<String, Object>();
		params3.put("pcapFile", "true");
		Object result3 = driverIOS.executeScript("mobile:vnetwork:stop", params3);	
		reportiumClient.stepEnd("StopVNetwork");
	}
	
	
	
	
	
	  /**
     * @param millis
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }	
	/**
	 * @param locator
	 * @param driver
	 * @param timeout
	 * @description  Waits for objects to load before proceeding !!! 
	 */
	private static WebElement fluentWait(final By locator, AndroidDriver driverAndroid, long timeout) {	 
		try {
			 FluentWait<AndroidDriver> wait = new FluentWait<AndroidDriver>(driverAndroid)
			        .withTimeout(timeout, TimeUnit.SECONDS)
			        .pollingEvery(250, TimeUnit.MILLISECONDS)
			        .ignoring(Exception.class)
			        .ignoring(NoSuchElementException.class);
			       
				  WebElement webelement = wait.until(new Function<AndroidDriver, WebElement>() {
					  public WebElement apply(AndroidDriver driverAndroid) {
			            return driverAndroid.findElement(locator);
					  }
				  });
				  return  webelement;
		} catch (Exception e) {
			return null;
		}	
	  }
	
	
	public void uploadScreenshots(ITestContext context) {
		Date date = new Date();
		
		/* Flag to check if in Automation */
		
		/*Time Stamp */
		SimpleDateFormat timeStamp = new SimpleDateFormat("MMddyyyy_hhmmSSSa");
		String timeStampDate = timeStamp.format(date);
		System.out.println(timeStampDate); 
		
		/* Folder Time Stamp */
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
		String formattedDate = sdf.format(date);
		System.out.println(formattedDate); 
		
		try {
			String command = "mobile:screen:image";
			Map<String, Object> params = new HashMap<>(); 
			params.put("report.resolution", "low"); 
			params.put("format", "jpg"); 
			params.put("key", "GROUP:ScreenShots/" + formattedDate +"/" + context.getName() + "_" + timeStampDate); 	
		//	params.put("key", "PRIVATE:Raj/" + context.getName() + "_" + formattedDate); 
			String report = (String) driverIOS.executeScript(command, params); 
		} catch (Exception e) {
			System.out.println("Exception " + e); 
		}
		
	}
	
	
	
	
	
	private static final String HTTPS = "https://";
	private static final String MEDIA_REPOSITORY = "/services/repositories/media/";
	private static final String UPLOAD_OPERATION = "operation=upload&overwrite=true";
	private static final String UTF_8 = "UTF-8";
	
	/**
	 * Uploads a file to the media repository.
	 * Example:
	 * uploadMedia("demo.perfectomobile.com", "john@perfectomobile.com", "123456", "C:\\test\\ApiDemos.apk", "PRIVATE:apps/ApiDemos.apk");
	 */
	public static void uploadMedia(ITestContext context, String fileLocationSourceDrive, String fileExt) {
		String host = context.getCurrentXmlTest().getParameter("URL");
		String user = context.getCurrentXmlTest().getParameter("user");
		String password = context.getCurrentXmlTest().getParameter("pass");
		String mediaRepo = context.getCurrentXmlTest().getParameter("mediaRepo");
			
		/* Get the latest ipa/apk file in Folder C:/Users/Raj/Downloads/ipaFIle  */
        File iOSipa = getLastModifiedFile(fileLocationSourceDrive, fileExt);
        System.out.println("Latest File Name: " + iOSipa.getName() + "    FilePath: " + iOSipa.toString());    
        String repositoryKey = mediaRepo + iOSipa.getName();

		try {
			File file = new File(iOSipa.toString());
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
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @param locator
	 * @param driver
	 * @param timeout
	 * @description  Waits for objects to load before proceding !!! 
	 */
	private static WebElement fluentWait(final By locator, IOSDriver<WebElement> driverIOS, long timeout) {	 
		try {
			 FluentWait<IOSDriver> wait = new FluentWait<IOSDriver>(driverIOS)
			        .withTimeout(timeout, TimeUnit.SECONDS)
			        .pollingEvery(250, TimeUnit.MILLISECONDS)
			        .ignoring(Exception.class)
			        .ignoring(NoSuchElementException.class);
			       
				  WebElement webelement = wait.until(new Function<IOSDriver, WebElement>() {
					  public WebElement apply(IOSDriver driverIOS) {
			            return driverIOS.findElement(locator);
					  }
				  });
				  return  webelement;
		} catch (Exception e) {
			System.out.println("\n\n****************** FLUENT Wait Failure Exception START **************************");
			e.printStackTrace();
			System.out.println("*************************************************************************************");
			return null;
		}	
	  }
	
	/**
	 * Download report.
	 *
	 * @param driver the driver
	 * @param type the type
	 * @param fileName the file name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void downloadReport(RemoteWebDriver driver, String type, String fileName) throws IOException {
		try { 
					String command = "mobile:report:download"; 
					Map<String, Object> params = new HashMap<>(); 
					params.put("type", type); 
					String report = (String)driver.executeScript(command, params); 
					File reportFile = new File(fileName + "." + type); 
					BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(reportFile)); 
					byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report); 
					output.write(reportBytes); output.close(); 
			} catch (Exception ex) { 
				System.out.println("Got exception " + ex); }
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
					    .withWebDriver(appiumDriver)
					    .build();		
			}
			
			reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
			
			//String executionId = (String) capabilitiesIOS.getCapability("executionId");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
			return reportiumClient;
	}
	
	
	/**
	 * Download report.
	 *
	 * @param driver the driver
	 * @param type the type
	 * @param fileName the file nam
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void downloadReport(IOSDriver iOSdriver, String type, String fileName) throws IOException {
		try { 
			//iOSdriver.getCapabilities().getCapability("reportKey").toString();
				
				String command = "mobile:report:download"; 
				Map<String, Object> params = new HashMap<>(); 
				params.put("type", type); 
				String report = (String)iOSdriver.executeScript(command, params); 
				File reportFile = new File(fileName + "." + type); 
				BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(reportFile)); 
				byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report); 
				output.write(reportBytes); 
				output.close(); 
			} catch (Exception ex) { 
				System.out.println("Got exception " + ex); 
			}

		
	}
	
	
	
	/**
	 * @param locator
	 * @param driver
	 * @param timeout
	 * @description  Waits for objects to load before proceding !!! 
	 */
	private static WebElement fluentWait(final By locator, RemoteWebDriver driver, long timeout) {	 
		try {
			 FluentWait<RemoteWebDriver> wait = new FluentWait<RemoteWebDriver>(driver)
			        .withTimeout(timeout, TimeUnit.SECONDS)
			        .pollingEvery(250, TimeUnit.MILLISECONDS)
			        .ignoring(Exception.class)
			        .ignoring(NoSuchElementException.class);
			       
				  WebElement webelement = wait.until(new Function<RemoteWebDriver, WebElement>() {
					  public WebElement apply(RemoteWebDriver driver) {
			            return driver.findElement(locator);
					  }
				  });
				  return  webelement;
		} catch (Exception e) {
			return null;
		}
		 
		
	  }
	
	
	private WebElement setFluentWaitMethod(final By locator) {
		WebElement webElement = null;
		
		if(testParams.get("driver").equals("AppiumIOS")) {
			webElement = fluentWait(locator, getDriverIOS(), this.defaultTimeout);
		} else if (testParams.get("driver").equals("AppiumAndroid")) {
			webElement = fluentWait(locator, getDriverAndroid(), this.defaultTimeout);
		} else if (testParams.get("driver").equals("RemoteWebDriver")) {
			webElement = fluentWait(locator, getDriver(), this.defaultTimeout);
		}
		return webElement;
	}
	
	private WebElement setFluentWaitMethod(final By locator, long timeOut) {
		WebElement webElement = null;
		
		if(testParams.get("driver").equals("AppiumIOS")) {
			getDriverIOS().manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
			webElement = fluentWait(locator, getDriverIOS(), timeOut);
		} else if (testParams.get("driver").equals("AppiumAndroid")) {
			getDriverAndroid().manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
			webElement = fluentWait(locator, getDriverAndroid(), timeOut);
		} else if (testParams.get("driver").equals("RemoteWebDriver")) {
			getDriver().manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
			webElement = fluentWait(locator, getDriver(), timeOut);
		}
		return webElement;
	}
	/**
	 * @param xpathExpression
	 * @return
	 * @throws Exception
	 */
	public WebElement findElementByXpath(String xpathExpression) throws Exception {
		WebElement webElement = setFluentWaitMethod(By.xpath(xpathExpression));
		if (webElement!=null) {
			if(testParams.get("enableLogs").equals("Enable")) {
				System.out.println("XpathFound: " + xpathExpression);
			}
			return webElement;	
		}
		return null;
	}
	
	/**
	 * @param xpathExpression
	 * @return
	 * @throws Exception
	 */
	public WebElement findElementByXpath(String xpathExpression, long timeOut) throws Exception {
		WebElement webElement = setFluentWaitMethod(By.xpath(xpathExpression), timeOut);
		if (webElement!=null) {
			if(testParams.get("enableLogs").equals("Enable")) {
				System.out.println("XpathFound: " + xpathExpression);
			}
			return webElement;	
		} else if (webElement==null) {
			System.out.println("\n\nXpath Not found!: " + xpathExpression);
		}
		return null;
	}
	
	
	/**
	 * @param xpathExpression
	 * @return
	 * @throws Exception
	 */
	public WebElement findElementByXpath(String description, String xpathExpression, long timeOut) throws Exception {
		long startTime = 0;
		long stopTime=0;
		long totalDuration=0;
		long totalDurationSec=0;
		
		try {
			reportiumClient.stepStart(description);
			startTime = System.currentTimeMillis();
			WebElement webElement = setFluentWaitMethod(By.xpath(xpathExpression), timeOut);
			if (webElement!=null) {
				System.out.println("-------------------------- ELEMENT FOUND -----------------------------");
				System.out.println("XpathFound: " + xpathExpression);
				stopTime = System.currentTimeMillis();
				totalDuration = stopTime-startTime;
			    totalDurationSec = (totalDuration)/100;
				System.out.format("Sec = %s, (Start_Milli : %s,  End_Milli : %s) \n", totalDurationSec, startTime, stopTime );	
				//reportiumClient.stepEnd(description + ": " + totalDurationSec+"s");
				reportiumClient.reportiumAssert(description + ": " + totalDurationSec+" s", true);
				System.out.println("--------------------------------------------------------------------------");
				return webElement;			
			} else if (webElement==null) {		
				System.out.println("------------------------ ELEMENT = Null START-------------------------");
				stopTime = System.currentTimeMillis();
				totalDuration = stopTime-startTime;
			    totalDurationSec = (totalDuration)/100;
			    System.out.println("Xpath Not found within allocated timeout!!!: " + xpathExpression);	
				System.out.format("Sec = %s, (Start_Milli : %s,  End_Milli : %s) \n", totalDurationSec, startTime, stopTime );	
				///reportiumClient.stepEnd(description + " : " + totalDurationSec+" s");
				reportiumClient.reportiumAssert("Xpath Not found within allocated timeout: " + xpathExpression, false);
				System.out.println("--------------------------------------------------------------------------");
			}		
		} catch (Exception e) {
			System.out.println("------------------------EXCEPTION STACK TRACE START---------------------");
			System.out.println("---EXCEPTION on Xpath:  " + xpathExpression + "   With TimeOut: " + timeOut);
			stopTime = System.currentTimeMillis();
			totalDuration = stopTime-startTime;
		    totalDurationSec = (totalDuration)/100;
		    System.out.format("Sec = %s, (Start_Milli : %s,  End_Milli : %s) \n", totalDurationSec, startTime, stopTime );		    
			reportiumClient.stepEnd(description + " : " + totalDurationSec+" s");			
			e.printStackTrace();
			System.out.println("------------------------------------------------------------------------------");
		} finally {
			//reportiumClient.stepEnd(description + " : " + totalDurationSec+"s");
		}
			return null;
	}
	
	
	
	
	
	/**
	 * @param xpathExpression
	 * @return
	 * @throws Exception
	 */
	public WebElement findElementByID(String description, String id, long timeOut) throws Exception {
		long startTime = System.currentTimeMillis();
		long stopTime=0;
		long totalDuration=0;
		long totalDurationSec=0;
		
		try {
			reportiumClient.stepStart(description);
			WebElement webElement = setFluentWaitMethod(By.id(id), timeOut);
			if (webElement!=null) {
				System.out.println("-------------------------- ELEMENT FOUND -----------------------------");
				System.out.println("XpathFound: " + id);
				stopTime = System.currentTimeMillis();
				totalDuration = stopTime-startTime;
			    totalDurationSec = (totalDuration)/100;
				System.out.format("Sec = %s, (Start_Milli : %s,  End_Milli : %s) \n", totalDurationSec, startTime, stopTime );	
				//reportiumClient.stepEnd(description + ": " + totalDurationSec+"s");
				reportiumClient.reportiumAssert(description + ": " + totalDurationSec+" s", true);
				System.out.println("--------------------------------------------------------------------------");
				return webElement;			
			} else if (webElement==null) {		
				System.out.println("------------------------ ELEMENT = Null START-------------------------");
				stopTime = System.currentTimeMillis();
				totalDuration = stopTime-startTime;
			    totalDurationSec = (totalDuration)/100;
			    System.out.println("Xpath Not found within allocated timeout!!!: " + id);	
				System.out.format("Sec = %s, (Start_Milli : %s,  End_Milli : %s) \n", totalDurationSec, startTime, stopTime );	
				reportiumClient.stepEnd(description + " : " + totalDurationSec+" s");
				reportiumClient.reportiumAssert("Id Not found within allocated timeout: " + id, false);
				System.out.println("--------------------------------------------------------------------------");
			}		
		} catch (Exception e) {
			System.out.println("------------------------EXCEPTION STACK TRACE START---------------------");
			System.out.println("---EXCEPTION on id:  " + id + "   With TimeOut: " + timeOut);
			stopTime = System.currentTimeMillis();
			totalDuration = stopTime-startTime;
		    totalDurationSec = (totalDuration)/100;
		    System.out.format("Sec = %s, (Start_Milli : %s,  End_Milli : %s) \n", totalDurationSec, startTime, stopTime );		    
			reportiumClient.stepEnd(description + " : " + totalDurationSec+" s");			
			e.printStackTrace();
			System.out.println("------------------------------------------------------------------------------");
		} finally {
			//reportiumClient.stepEnd(description + " : " + totalDurationSec+"s");
		}
			return null;
	}
	
	

	/**
	 * @param xpathExpression
	 * @return
	 * @throws Exception
	 */
	public WebElement findElementByID(String ID) throws Exception {
		WebElement webElement = setFluentWaitMethod(By.id(ID));
		if (webElement!=null) {
			if(testParams.get("enableLogs").equals("Enable")) {
				System.out.println("XpathFound: " + ID);
				//TODO add logger 
			}
			return webElement;	
		}
		return null;
	}
	
	/**
	 * @param cSSExpression
	 * @return
	 * @throws Exception
	 */
	public WebElement findElementByCSS(String cSSExpression) throws Exception {
		WebElement webElement = setFluentWaitMethod(By.cssSelector(cSSExpression));
		if (webElement!=null) {
			if(testParams.get("enableLogs").equals("Enable")) {
				System.out.println("cssExpression: " + cSSExpression);
			}
			return webElement;	
		}
		return null;
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
	
	public void switchToContext(String context) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driverIOS);
        Map<String,String> params = new HashMap<>();
        params.put("name", context);
        executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
    }
	
	
	public void scrollExpiration(String Month, String Year) throws Exception {
		driverIOS.context("NATIVE_APP");
		findElementByXpath("//UIAPickerWheel[1]").sendKeys(Month);
		findElementByXpath("//UIAPickerWheel[2]").sendKeys(Year);
	}

	
	public void enablePeekViewold (String xpath, String start, String end) throws Exception  {
		WebElement elementLoc = findElementByXpath(xpath);	
		
		if (start.equals(null) || start.equals("") && end.equals(null) || end.equals("")) {
			SwipePeekView(elementLoc);
		} else {
			swipeNew(start, end);
		}
		
	}
	
	
	
	
	
	public void enableLogoutSwipe(String xpath) throws Exception {
		WebElement elementLoc = findElementByXpath(xpath);
		
		String x = getLocationX(elementLoc);
		String y = getLocationY(elementLoc);
		
	}

	public String getLocationX(WebElement me) {
		int x = me.getLocation().x;
		int width = (Integer.parseInt(me.getAttribute("width")) / 2) + x; 
		return width + "";
	}
	public String getLocationY(WebElement me) {
		int y = me.getLocation().y;
		int height = (Integer.parseInt(me.getAttribute("height")) / 2) + y;
		return height + "";
	}
	
	
	
	
	
	
	
	
	public void enablePeekView (String xpath) throws Exception  {
		WebElement elementLoc = findElementByXpath(xpath);	
		SwipePeekView(elementLoc);

	}
	
	public void SwipePeekView(WebElement elementLoc) {
		String x = getLocationX(elementLoc);
		String y = getLocationY(elementLoc);

		swipe2(x, y, true);	
	}
	


	
	
	public void scrollAndSearch(String value, WebElement me, Boolean direction) {
		String x = getLocationX(me);
		String y = getLocationY(me);
		while (!driverIOS.findElementByXPath(getXpathFromElement(me)).getText().contains(value)) {
			swipe(x, y, direction);
		}
	}
	public void swipe2(String start, String end, Boolean up) {
		
		String direction = Integer.parseInt(start) + 300 + "," + end;
		
		System.out.println("Start Coordinate: " + start);
		System.out.println("End Coordinate: " + end );
		System.out.println("Direction: " + direction);
	
		Map<String, Object> params1 = new HashMap<>();
		params1.put("location", start + "," + end);
		params1.put("operation", "down");
		Object result1 = driverIOS.executeScript("mobile:touch:tap", params1);

		Map<String, Object> params2 = new HashMap<>();
		List<String> coordinates2 = new ArrayList<>();

		coordinates2.add(direction);
		params2.put("location", coordinates2);
		params2.put("auxiliary", "notap");
		params2.put("duration", "4");
		Object result2 = driverIOS.executeScript("mobile:touch:drag", params2);

		Map<String, Object> params3 = new HashMap<>();
		params3.put("location", direction);
		params3.put("operation", "up");
		Object result3 = driverIOS.executeScript("mobile:touch:tap", params3);
		
		
	//	Start Coordinate: 333  End Coordinate: 498
	//	Direction: 633,498
		
	//	TouchAction touchAction9 = new TouchAction(driverIOS);
//		touchAction9.press(Integer.parseInt(start),Integer.parseInt(end)).waitAction(1).moveTo(direction,(Integer.parseInt(end))).release();
//		driverIOS.performTouchAction(touchAction9);

//		String startX = start + "," + end;
	//	String endX = direction;
		
	//	swipeData.put("start", startX); 
	//	swipeData.put("end", endX);
		
//		swipeNew(startX, endX);
		
		//swipeNew(start)
	//	388, 560      688,560
		
	}
	
	

	public static void swipeNew(String start, String end)
    {
		
		System.out.println("Start: " + start + "   End: " + end);
		
        Map<String,String> params = new HashMap<String,String>();
        params.put("start", start);  //50%,50%
        params.put("end", end);  //50%,50%
        driverIOS.executeScript("mobile:touch:swipe", params);
    }
	
	
	
	public void swipe(String start, String end, Boolean up) {
		String direction;
		if (up) {
			direction = start + "," + (Integer.parseInt(end) + 70);
		} else {
			direction = start + "," + (Integer.parseInt(end) - 70);
		}

		Map<String, Object> params1 = new HashMap<>();
		params1.put("location", start + "," + end);
		params1.put("operation", "down");
		Object result1 = driverIOS.executeScript("mobile:touch:tap", params1);

		Map<String, Object> params2 = new HashMap<>();
		List<String> coordinates2 = new ArrayList<>();

		coordinates2.add(direction);
		params2.put("location", coordinates2);
		params2.put("auxiliary", "notap");
		params2.put("duration", "10");
		Object result2 = driverIOS.executeScript("mobile:touch:drag", params2);

		Map<String, Object> params3 = new HashMap<>();
		params3.put("location", direction);
		params3.put("operation", "up");
		Object result3 = driverIOS.executeScript("mobile:touch:tap", params3);


	}
	
	public void pickDOB(String month, String day, String year) throws Exception {
		// The scroll and search method will scroll 1 value at a time
		// searching for the appropriate text value for the field
		// it takes a "value", "WebElement", "boolean" (this determines if it should scroll up or down) for input
		//scrollAndSearch(driver, month, me, true);
		//location of element, where and how much to scroll, and value are all dynamically retrieved
		//so for each new pickerwheel you simply need to do the below leg work to determine
		//if you should be scrolling up or down
		
		
		WebElement me = findElementByXpath("//UIAPickerWheel[1]");
		int mget = getMonthInt(me.getText().split(",")[0]);
		
		
		if (mget > getMonthInt(month)) {
			scrollAndSearch(month, me, true);
		} else {
			scrollAndSearch(month, me, false);
		}

		me = findElementByXpath("//UIAPickerWheel[2]");
		if (Integer.parseInt(me.getText().split(",")[0]) > Integer.parseInt(day)) {
			scrollAndSearch(day, me, true);
		} else {
			scrollAndSearch(day, me, false);
		}

		me = findElementByXpath("//UIAPickerWheel[3]");
		if (Integer.parseInt(me.getText().split(",")[0]) > Integer.parseInt(year)) {
			scrollAndSearch(year, me, true);
		} else {
			scrollAndSearch(year, me, false);
		}
	}

	public int getMonthInt(String month) {
		int monthInt = 0;
		switch (month) {
		case "January":
			monthInt = 1;
			break;
		case "February":
			monthInt = 2;
			break;
		case "March":
			monthInt = 3;
			break;
		case "April":
			monthInt = 4;
			break;
		case "May":
			monthInt = 5;
			break;
		case "June":
			monthInt = 6;
			break;
		case "July":
			monthInt = 7;
			break;
		case "August":
			monthInt = 8;
			break;
		case "September":
			monthInt = 9;
			break;
		case "October":
			monthInt = 10;
			break;
		case "November":
			monthInt = 11;
			break;
		case "December":
			monthInt = 12;
			break;
		default:
			monthInt  = Integer.parseInt(month);
		}
		return monthInt;
	}

	

	public String getXpathFromElement(WebElement me) {
		return (me.toString().split("-> xpath: ")[1]).substring(0, (me.toString().split("-> xpath: ")[1]).length() - 1);
	}


	  public static void copyFile(String fileLocationSourceDrive, String fileLocationDestination, String fileExt) {
	    	
    	  /* Get the latest ipa/apk file in Folder C:/Users/Raj/Downloads/ipaFIle   */
        File iOSipa = getLastModifiedFile(fileLocationSourceDrive, fileExt);
        System.out.println("Latest File Name: " + iOSipa.getName() + "    FilePath: " + iOSipa.toString());
    	
    	try {
				String fileName = iOSipa.getName();
             
				String sourceFileName = iOSipa.getAbsolutePath();  System.out.println("\nSourceFileName: " + sourceFileName);
                String destinationFileName = fileLocationDestination + fileName;  System.out.println("DestinationFileName: " + destinationFileName);
                
                System.out.println("Reading......." + sourceFileName);
	            File sourceFile = new File(sourceFileName);
	            File destinationFile = new File(destinationFileName);
	            InputStream in = new FileInputStream(sourceFile);
	            OutputStream out = new FileOutputStream(destinationFile);
	 
	            byte[] buffer = new byte[1024];
	            int length;
	            while ((length = in.read(buffer)) > 0) {
	                out.write(buffer, 0, length);
	            }
	            in.close();
	            out.close();
	            System.out.println("\nCopied: " + sourceFileName);
    		 
    		 System.out.println("\nCompleted Transfer...");
        } catch (Exception ex) {
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
		BaseClass.propFile = propFile;
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
