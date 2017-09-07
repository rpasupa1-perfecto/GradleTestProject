package org.mobile.tests;
//import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mobile.engine.BaseClass;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;


public class demoTestCase extends BaseClass {
	/* RemoteWeb Driver */
	public RemoteWebDriver driver;
	/* Appium IOS Driver */
	public IOSDriver<?> driverIOS;
	/* Appium Android Driver */
	public AndroidDriver<?> driverAndroid;
	
	@Test
	public void sampleTestCase() throws MalformedURLException, IOException {
		
		try {

			String keyFob = "//*[@label='Key Fob']";
			
			//enablePeekView (keyFob, "", "");
			
			driverIOS.context(driverIOS.getContext());
			
			Thread.sleep(7000);
			
			String peekLockDoor = "//*[@label='peek lockDoor']";
			if(findElementByXpath(peekLockDoor, 10)!=null) {
				if(findElementByXpath(peekLockDoor, 10).isDisplayed()) {
					assertTrue("Peek Lock Door is displayed !!! .", true);
				}	else {
					assertTrue("Peek Lock Door is NOT displayed !!!", false);
				}
			} else {
				//assertTrue("Peek Lock Door is NOTTT displayed !!!: " + "/n/n Start: " + swipeData.get("start") + "/n End: " + swipeData.get("end"), false);
				assertTrue("Peek Lock Door is NOTTT displayed !!!", false);
			}
			
		
			String peekStart= "//*[@label='peek start']";
			if(findElementByXpath(peekStart, 10)!=null) {
				if(findElementByXpath(peekStart).isDisplayed()) {
					assertTrue("Peek Start is displayed !!!", true);
				}	else {
					assertTrue("Peek Start is NOT displayed !!!", false);
				}
			} else {
				assertTrue("Peek Start is NOTTT displayed !!!", false);
			}
		
			String peekAlert = "//*[@label='peek alert']";
			if(findElementByXpath(peekAlert, 10)!=null) {
				if(findElementByXpath(peekAlert).isDisplayed()) {
					assertTrue("Peek Alert is displayed !!!", true);
				}	else {
					assertTrue("Peek Alert is NOT displayed !!!", false);
				}
			} else {
				assertTrue("Peek Alert is NOTTT displayed !!!", false);
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
		/* Set Widget Enviornment */
		
		
	}
	
	@AfterClass(alwaysRun = true)
	public void destroy() throws Exception {
		try {
			if(testParams.get("driver").equals("AppiumIOS")) {
				driverIOS.closeApp();			
				//downloadReport(driver,"pdf", "C:/RajReports/test");
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
		
		
	}
	
	
	

}
