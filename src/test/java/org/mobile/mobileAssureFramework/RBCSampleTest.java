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

import org.apache.tools.ant.taskdefs.Sleep;
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
import com.perfecto.reportium.test.result.TestResult;
import com.perfecto.reportium.test.result.TestResultFactory;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;


public class RBCSampleTest extends BaseClass {
	
	/* RemoteWeb Driver */
	public RemoteWebDriver driver;
	/* Appium IOS Driver */
//	public IOSDriver driverIOS;
	/* Appium Android Driver */
	//public AndroidDriver driverAndroid;

	protected WebDriver webDriver;
	
	SoftAssert sAssert = new SoftAssert();
	String reportURL;
	
	@Test
	public void sampleTestCase(ITestContext context) throws MalformedURLException, IOException {
		
		try {				
				/* Verify RBC Image Logo */					
				String rbcImage = "//*[@resource-id=\"android:id/content\"]/android.widget.LinearLayout/android.widget.ImageView";
				//String rbcImage="//*[@label='RBC Wealth Management']";
				
				try {
					WebElement rbcImageMainPage = findElementByXpath("Verify RBC Image Logo", rbcImage, 30);
					if (rbcImageMainPage!=null) {
						assertTrue("Logo Loaded !!!", true);
						
					} else if (rbcImageMainPage==null) {
						assertTrue("Logo Not Loaded !!!", false);
						System.out.println("Image Not Loaded");
					}
				} catch (Exception e) {
					assertTrue("Logo Not Loaded !!!", false);
					e.printStackTrace();
				}
				
				
				/* Verify Link Text */					
				String LinkTextFINRA = "//*[@resource-id=\"com.rbc.clientmobility.us.dev:id/activity_login_foot_note\" and contains(@text, 'FINRA')]";
	
				try {
					WebElement FINRA_Txt = findElementByXpath("Verify FINRA LINK", LinkTextFINRA, 30);		
					if (FINRA_Txt!=null) {
						FINRA_Txt.click();
						assertTrue("Finra txt Loaded !!!", true);
						
					} else if (FINRA_Txt==null) {
						assertTrue("Logo Not Loaded !!!", false);
						System.out.println("FINRA txt Not Available");
					}
				} catch (Exception e) {
					assertTrue("Finra text Not Loaded...check string !!!", false);
					e.printStackTrace();
				}
						
			/* click on link Finra */
			clickLinkFINRA();
				
			/* Go back to App */					
			startappAndroid("com.rbc.clientmobility.us.dev");
			
			/* click on link SIPC */
			clickLinkSIPC();
			
			/* Go back to App */					
			startappAndroid("com.rbc.clientmobility.us.dev");
			
			
			/* Verify Main Page is loaded Again */	
			try {
				WebElement rbcImageMainPage = findElementByXpath("Verify Main Page Load", rbcImage, 30);
				if (rbcImageMainPage!=null) {
					assertTrue("Logo Loaded !!!", true);
					
				} else if (rbcImageMainPage==null) {
					assertTrue("Logo Not Loaded !!!", false);
					System.out.println("Image Not Loaded");
				}
			} catch (Exception e) {
				assertTrue("Logo Not Loaded !!!", false);
				e.printStackTrace();
			}
	
			
			/* Close Samsung Internet */
			closeAppAndroid("com.sec.android.app.sbrowser");
			
			
	} catch (Exception e) {
		e.printStackTrace();
	}
		
}

	public void clickLinkFINRA() throws Exception {
		Map<String, Object> params2 = new HashMap<>();
		params2.put("label", "FINRA");
		params2.put("timeout", "10");
		Object result2 = driverAndroid.executeScript("mobile:button-text:click", params2);
	
		/* Verify FINRA Load */
		//sleep(4000);
		String finText = "//*[contains (@text,'www.finra.org')]";
		findElementByXpath("FINRA Page Load", finText, 30);
	}	
		
	public void clickLinkSIPC() throws Exception {
		Map<String, Object> params1 = new HashMap<>();
		params1.put("label", "SIPC");
		params1.put("timeout", "10");
		Object result2 = driverAndroid.executeScript("mobile:button-text:click", params1);		
		/* Verify SIPC Page Load */
		String sipcText = "//*[contains (@text,'‎www.sipc.org')]";
		findElementByXpath("SIPC Page Load", sipcText, 30);	
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
		getReportiumClient().testStart("Raj_Test_Start_BeforeMethod", new TestContext());	
	}
	
	@AfterMethod(alwaysRun = true)
	public void destroy(ITestContext context, ITestResult result) throws Exception {
			 if ( reportiumClient!= null )
               {
					reportiumClient.testStop( result.getStatus() == ITestResult.SUCCESS ? TestResultFactory.createSuccess() : 
						TestResultFactory.createFailure( result.getThrowable().getMessage(), result.getThrowable() ) );
					
					
					//reportiumClient.testStop(result.getStatus());
					
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
				driverAndroid.closeApp();
				driverAndroid.close();
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
		System.out.println("\n\nPerfectoReportUrl = \n" + reportiumClient.getReportUrl()+ "\n\n");
	}

}
