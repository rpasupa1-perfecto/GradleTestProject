package org.mobile.mobileAssureFramework;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.mobile.engine.BaseClass;
import org.openqa.selenium.Dimension;
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

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;


public class ReportiumTest extends BaseClass {
	
	/* RemoteWeb Driver */
	public RemoteWebDriver driver;
	/* Appium IOS Driver */
	public IOSDriver driverIOS;
	/* Appium Android Driver */
	public AndroidDriver driverAndroid;

	protected WebDriver webDriver;
	
	SoftAssert sAssert = new SoftAssert();
	String reportURL;
	
	@Test
	public void sampleTestCase(ITestContext context) throws MalformedURLException, IOException {
		
		try {
			reportiumClient.testStep("Verify Logo ");	
			
			
			/* handle popups */
//			//TODO move to Popup Handler() method
//			try {
//				findElementByXpath("//*[@text=\"mireya.qa1@gmail.com\"]", 30, "Handle Gmail Popup").click();
//			} catch (Exception e) {
//				System.out.println("Popup Gmail didn't show up");
//			}
//			try {
//				findElementByXpath("//*[@resource-id=\"com.google.android.gms:id/close_button\"]", 30, "Handle Popup Close").click();
//			} catch (Exception e) {
//				System.out.println("Popup Close didn't show up");
//			}
//			
//			
//			/* Start Order */
//			findElementByXpath("//*[@resource-id=\"com.panera.bread.qarc:id/start_order_button\"]",30,"Click Start Order").click();
//			
//			/* Verify Rapid Pick-up String */
//			WebElement VerifyRapidString = findElementByXpath("//*[@resource-id=\"com.panera.bread.qarc:id/rapid_pick_up\"]", 7, "Verify Rapid Pick-up String");
//			if (VerifyRapidString!=null) {			
//				System.out.println("Verify Rapid Pick-up String String Found");		 
//			} else {
//				/* Add Asserts */
//				System.out.println("Verify Rapid Pick-up String String NOT Found");		
//			}
//			
//		
			
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	//@Test
	public void sampleTestCase2(ITestContext context) throws MalformedURLException, IOException {
		
		try {
		
			reportiumClient.testStep("Verify Logo ");			
			/* Verify Kohl's Logo  */
			String logo = "//*[@label='kohls logo']";
			try {
				WebElement logoMainPage = findElementByXpath(logo, 1);
				if (logoMainPage!=null) {
					assertTrue("Logo Loaded !!!", true);
					//sAssert.assertTrue(false, "Dealer Page NOT Loaded !!!"); 				
				} else {
					assertTrue("Logo Not Loaded !!!", false);
				}  
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			reportiumClient.testStep("Verify Menu");
			/* Verify Menu  */
			String menu = "//*[@label='icn menu']";
			try {
				WebElement menuMainPage = findElementByXpath(menu, 1);
				if (menuMainPage!=null) {
					assertTrue("Menu Loaded !!!", true);				
				} else {
					assertTrue("Menu Not Loaded !!!", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
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
