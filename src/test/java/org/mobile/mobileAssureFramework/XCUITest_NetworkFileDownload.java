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
import org.mobile.engine.ApiExportCodeSample;
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

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.HideKeyboardStrategy;
import net.sf.saxon.expr.sort.IntIterator;


public class XCUITest_NetworkFileDownload extends BaseClass {
	
	/* RemoteWeb Driver */
//	public RemoteWebDriver driver;
	/* Appium IOS Driver */
//	public IOSDriver driverIOS;
	/* Appium Android Driver */
//	public AndroidDriver driverAndroid;

	//protected WebDriver webDriver;
	
	SoftAssert sAssert = new SoftAssert();
	String reportURL;
//	HashMap<Integer,Long> hm1 = new HashMap<Integer,Long>();
//	HashMap<Integer,Long> hm2 = new HashMap<Integer,Long>();
//	HashMap<Integer,Long> hm3 = new HashMap<Integer,Long>();
//	Float sampleTest1 = 0f;
//	Float sampleTest2 = 0f;
//	Float sampleTest3 = 0f;
	
	long startTime=0;
	long stopTime=0;

	
	//@Test
	public static void main (String[] args) { 
		try {
				long totalDuration;  
				ApiExportCodeSample apiExportCodeSample = new ApiExportCodeSample();
				
			//	StartVNetwork();
		
					
				//	String executionId = (String) capabilitiesIOS.getCapability("executionId");
					apiExportCodeSample.downloadA();
					
					
				
				

				
			
				
			} catch (Exception e) {
				e.printStackTrace();
			
			} finally {
			//		StopVNetwork();
			}
	}
	


	
	
	

	
	

	
	
	
}
