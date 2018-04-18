package org.mobile.mobileAssureFramework;

import static org.junit.Assert.assertTrue;

import java.awt.image.SampleModel;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.junit.Assert;
import org.mobile.engine.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

//import io.appium.java_client.SwipeElementDirection;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.HideKeyboardStrategy;



public class XCUITest extends BaseClass {
	
	/* RemoteWeb Driver */
//	public RemoteWebDriver driver;
	/* Appium IOS Driver */
//	public IOSDriver driverIOS;
	/* Appium Android Driver */
//	public AndroidDriver driverAndroid;

	//protected WebDriver webDriver;
	
	SoftAssert sAssert = new SoftAssert();
	String reportURL;
	
	HashMap<Integer,Long> hm1 = new HashMap<Integer,Long>();
	HashMap<Integer,Long> hm2 = new HashMap<Integer,Long>();
	HashMap<Integer,Long> hm3 = new HashMap<Integer,Long>();
	Float sampleTest1 = 0f;
	Float sampleTest2 = 0f;
	Float sampleTest3 = 0f;
	
	long startTime=0;
	long stopTime=0;
	
	@Test
	public void sampleTestCase1(ITestContext context) throws MalformedURLException, IOException {
		try {
						
		/* Start VNetwork */
		//StartVNetwork();
			
			try {
				//driverAndroid.fin
				
				 WebElement test = driverAndroid.findElementByXPath(("//*[@resource-id=\"com.anzenbanking.slota:id/topshadow\"]"));
		//		 test.click();
				
				AndroidElement test33 = (AndroidElement) test;
				//test33.swipe(SwipeElementDirection.UP, 10,-0, 1);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			
//				long totalDuration;   				
//				for (int i=1; i<=250; i++) {	
//					startTime();
//					
//					test();
//				
//					stopTime();
//					totalDuration = stopTime-startTime;
//				    long totalDurationInSec = (totalDuration)/100;
//				    
//				    System.out.format("Sec = %s, (Start_Milli : %s,  End_Milli : %s) \n", totalDuration, startTime, stopTime );	
//					System.out.println("\n Iteration Summary time i:" + i + " > " + totalDurationInSec + "sec");
//					hm1.put(i, totalDurationInSec);	
//				}
//				sampleTest1 = TotalSum(hm1);
//				System.out.println("\n&&&&&&&&&&&&&&&&&&   Performance SUMMARY    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//				System.out.println("sampleTestCaseHashMapSummary: " + sampleTest1 + " secs");
//				System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n");

				
			//	StopVNetwork();
				
			} catch (Exception e) {
				e.printStackTrace();
			//	StopVNetwork();
			} finally {
				//startTime=0;
				//stopTime=0;
				/*Stop VNetwork */
				
			}
	}
	


	public void test() throws Exception {
		/* Verify User Name Text Box */
		try {					
				WebElement UserName = findElementByID("UserId", "User ID", 13);										
				if (UserName!=null) {	
					UserName.clear();
					UserName.sendKeys("dfwprodacct272");
					reportiumClient.reportiumAssert("UserId", true);
				} else {
					/* Add Asserts */
					reportiumClient.reportiumAssert("UserID field didn't load", false);
				}					
			} catch (Exception e) {
				e.printStackTrace();
				reportiumClient.reportiumAssert("UserId didn't load", false);
			}
						
		/* Password Field Text Box */	
			try {
				WebElement passWord = findElementByXpath("Password Verification", "//*[@label='Password']", 13);
				if (passWord!=null) {
					passWord.clear();
					passWord.sendKeys("dfwbeta1_123");
					reportiumClient.reportiumAssert("Password Verification", true);
				} else {
					/* Add Asserts */
					reportiumClient.reportiumAssert("Password Verification", false);
					System.out.println("Password Field Text Box Not Shown");		
				}			
			} catch (Exception e) {
				e.printStackTrace();
				reportiumClient.reportiumAssert("Password Field Didn't load", false);
			}
										
		/* Sign In Button Verification */	
			try {
				WebElement signInBtn = findElementByXpath("SignIn Button", "//*[@label=\"Sign in\"]", 13);
				if (signInBtn!=null) {	
					reportiumClient.reportiumAssert("SignIn Button", true);
					//signInBtn.sendKeys(Keys.RETURN);
					//signInBtn.click();
				} else {
					/* Add Asserts */
					reportiumClient.reportiumAssert("SignIn Button Not Displayed", false);
				}	
			} catch (Exception e) {
				e.printStackTrace();
				reportiumClient.reportiumAssert("SignIn Button", false);
			}
			
			//driverIOS.hideKeyboard(HideKeyboardStrategy.PRESS_KEY);
			//driverIOS.getKeyboard().sendKeys(Keys.RETURN);
		//	driverIOS.hideKeyboard();
			
			//driver.sw
			
			try {
				findElementByID("UserId", "User ID", 3);	
				findElementByXpath("Password Verification", "//*[@label='Password']", 3);
				findElementByXpath("SignIn Button", "//*[@label=\"Sign in\"]", 3);
				findElementByID("UserId", "User ID", 3);	
				findElementByXpath("Password Verification", "//*[@label='Password']", 3);
				findElementByXpath("SignIn Button", "//*[@label=\"Sign in\"]", 3);
				findElementByID("UserId", "User ID", 3);	
				findElementByXpath("Password Verification", "//*[@label='Password']", 3);
				findElementByXpath("SignIn Button", "//*[@label=\"Sign in\"]", 3);
				
				
			} catch(Exception e) {
				e.printStackTrace();
			}
				
			
			closeApp();
			startapp();
	}
	
	
	
	public void startapp() {
		Map<String, Object> params1 = new HashMap<>();
		params1.put("identifier", "com.att.mobile.dfw");
		Object result1 = driverIOS.executeScript("mobile:application:open", params1);
	}
	
	public void closeApp() {
		Map<String, Object> params2 = new HashMap<>();
		params2.put("identifier", "com.att.mobile.dfw");
		Object result2 = driverIOS.executeScript("mobile:application:close", params2);
	}
	
	
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
		getReportiumClient().testStart(context.getName(), new TestContext());
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
				//driverIOS.re
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

}
