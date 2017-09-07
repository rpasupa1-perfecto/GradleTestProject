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


public class demoTestCase2 extends BaseClass {
	/* RemoteWeb Driver */
	public RemoteWebDriver driver;
	/* Appium IOS Driver */
	public IOSDriver driverIOS;
	/* Appium Android Driver */
	public AndroidDriver driverAndroid;
	
	@Test
	public void sampleTestCase() throws MalformedURLException, IOException {
		
		try {

			String keyFob = "//*[@label='Key Fob']";
			
			//enablePeekView (keyFob, "333,498", "633,498");
			enablePeekView(keyFob);
			
			Thread.sleep(7000);
			
			driverIOS.context("VISUAL");
				
			//* load image */
			Map<String, Object> params7 = new HashMap<>();
			params7.put("label", "PRIVATE:script\\Apple_iPhone-6_160826_013717.png");
			params7.put("timeout", "20");
			params7.put("threshold", "95");
			params7.put("operation", "single");
			Object result7 = driverIOS.executeScript("mobile:button-image:click", params7);
			
			Thread.sleep(7000);
	//		driverIOS.context("VISUAL");

			/* Set '4321 for lock */
//			Map<String, Object> params15 = new HashMap<>();
//			params15.put("label", "PRIVATE:script\\Apple_iPhone-6S_160830_140509.png");
//			params15.put("timeout", "20");
//			params15.put("threshold", "100");
//			Object result15 = driver.executeScript("mobile:button-image:click", params15);		
//			
//			Map<String, Object> params9 = new HashMap<>();
//			params9.put("label", "PRIVATE:script\\Apple_iPhone-6S_160830_134900.png");
//			params9.put("timeout", "20");
//			params9.put("threshold", "100");
//			Object result9 = driver.executeScript("mobile:button-image:click", params9);
//					
//			Map<String, Object> params10 = new HashMap<>();
//			params10.put("label", "PRIVATE:script\\Apple_iPhone-6S_160830_135049.png");
//			params10.put("timeout", "20");
//			params10.put("threshold", "100");
//			Object result10 = driver.executeScript("mobile:button-image:click", params10);
//			
//			Map<String, Object> params11 = new HashMap<>();
//			params11.put("label", "PRIVATE:script\\Apple_iPhone-6S_160830_135158.png");
//			params11.put("timeout", "20");
//			params11.put("threshold", "100");
//			Object result11 = driver.executeScript("mobile:button-image:click", params11);
//			
//			Map<String, Object> params12 = new HashMap<>();
//			params12.put("label", "PRIVATE:script\\Apple_iPhone-6S_160830_135323.png");
//			params12.put("timeout", "20");
//			params12.put("threshold", "100");
//			Object result12 = driver.executeScript("mobile:button-image:click", params12);
//			
//			
			
			
			
//			
//			Map<String, Object> params6 = new HashMap<>();
//			params6.put("label", "4");
//			params6.put("source", "primary");
//			params6.put("timeout", "40");
//			params6.put("threshold", "100");
//			Object result6 = driverIOS.executeScript("mobile:button-text:click", params6);
//		//	System.out.println("/n/n result6: /n/n" +result6.toString());
//			Thread.sleep(3000);
//					
//			System.out.println("Current Context:  " + driverIOS.getContext());
//			
//			Map<String, Object> params5 = new HashMap<>();
//			params5.put("label", "3");
//			params5.put("source", "primary");
//			params5.put("timeout", "20");
//			params5.put("threshold", "100");
//			Object result5 = driverIOS.executeScript("mobile:button-text:click", params5);
//			
//			Thread.sleep(3000);
//			
//			Map<String, Object> params4 = new HashMap<>();
//			params4.put("label", "2");
//			params4.put("source", "primary");
//			params4.put("timeout", "20");
//			params4.put("threshold", "100");
//			Object result4 = driverIOS.executeScript("mobile:button-text:click", params4);
//			
//			Thread.sleep(3000);
//			
//			Map<String, Object> params3 = new HashMap<>();
//			params3.put("label", "1");
//			params3.put("source", "primary");
//			params3.put("timeout", "20");
//			params3.put("threshold", "100");
//			Object result3 = driverIOS.executeScript("mobile:button-text:click", params3);
//			
			
			
			driverIOS.context("NATIVE_APP");
			
			
			/* Enter Pin */
			String pinNum1 = "//*[@label='4']";
			String pinNum2 = "//*[@label='3']";
			String pinNum3 = "//*[@label='2']";
			String pinNum4 = "//*[@label='1']";
			findElementByXpath(pinNum1, 30).click();
			findElementByXpath(pinNum2, 30).click();
			findElementByXpath(pinNum3, 30).click();
			findElementByXpath(pinNum4, 30).click();
			
			
			
			
			Thread.sleep(7000);
			
			String peekLockDoor = "//*[@label='peek lockDoor']";
			if(findElementByXpath(peekLockDoor, 30)!=null) {
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
