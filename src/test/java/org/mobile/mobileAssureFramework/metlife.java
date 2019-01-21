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

import javax.swing.event.SwingPropertyChangeSupport;

import org.apache.tools.ant.taskdefs.Sleep;
import org.junit.Assert;
import org.mobile.engine.BaseClass;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver.Navigation;
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

import com.google.inject.Key;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResult;
import com.perfecto.reportium.test.result.TestResultFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;


public class metlife extends BaseClass {
	
	/* RemoteWeb Driver */
	//public RemoteWebDriver driver;
	//public AppiumDriver<WebElement>  driver;
	/* Appium IOS Driver */
//	public IOSDriver driverIOS;
	/* Appium Android Driver */
	//public AndroidDriver driverAndroid;

	
	SoftAssert sAssert = new SoftAssert();
	String reportURL;
	
	
	
	@Test
	public void metLifeSampleTestCase(ITestContext context) throws MalformedURLException, IOException {
		
		try {				
			
		//	driverAndroid.get("https://qa3.phoenix.ead.metlife.com/edge/web/public/benefits?source=metonline&grpNumber");
			//System.out.println("CurrentContext: ");

			/* Metlife Page Set Text*/	
			/* Customer Changed to new Xpath */
			String metLifeSetAsso = "//*[@class='dijitReset dijitInputInner']";
			driverAndroid.context("WEB");  // VISUAL/NATIVE_APP
		//	appDriver.context("WEB");  
			
				try {
					WebElement metLifeEmplAssocSet = findElementByXpath("MetlifePageSetText-Perfecto", metLifeSetAsso, 30);
					/* Displayed is onlya for Screenshots - Can be removed for Script Performance Improvements */
					metLifeEmplAssocSet.isDisplayed();
					
					if (metLifeEmplAssocSet!=null) {
						assertTrue("Metlife Page Loaded !!!", true);
						metLifeEmplAssocSet.sendKeys("3D Plastics, Inc");
						
						//metLifeEmplAssocSet.click();
					} 
				} catch (Exception e) {
					reportiumClient.reportiumAssert("Xpath Not found within allocated timeout: ", false);
					assertTrue("Object Not Loaded !!!", false);
					e.printStackTrace();
				}
				
			
				/* Click on next button.  */					
				String nextButton = "//span[@class='dijitReset dijitInline dijitButtonNode']";
				
				try {
					WebElement nextButtonObj = findElementByXpath("MetlifePage-ClickNextBtn-Perfecto", nextButton, 30);		
					nextButtonObj.isDisplayed(); //Takes ScreenShot
					if (nextButtonObj!=null) {
						assertTrue("Next BTN Loaded !!!", true);
						nextButtonObj.click();	
					} 
					
				} catch (Exception e) {
					reportiumClient.reportiumAssert("Xpath Not found within allocated timeout: ", false);
					assertTrue("Exception WatchNow Not Loaded...check string !!!", false);
					e.printStackTrace();
				}
						
				
				
				
				/* Look for Library Tab and click on it */					
				String PlasticPage = "//*[@id='content-header']";
				try {
					WebElement PlasticPageLoad = findElementByXpath("MetlifePage-3D-PlasticPage-Perfecto", PlasticPage, 30);			
					if (PlasticPageLoad!=null) {
						PlasticPageLoad.isDisplayed(); //Takes ScreenShot
						assertTrue("3d Plastic Page Loaded !!!", true);	
					} 
				} catch (Exception e) {
					reportiumClient.reportiumAssert("Xpath Not found within allocated timeout: ", false);
					assertTrue("Not Loaded...check string !!!", false);
					e.printStackTrace();
				}	
				
				
			
			
			
			
	} catch (Exception e) {
		e.printStackTrace();
	}
		
}

	
	
}
