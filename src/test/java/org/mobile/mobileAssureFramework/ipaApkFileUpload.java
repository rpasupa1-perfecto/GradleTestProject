package org.mobile.mobileAssureFramework;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.mobile.engine.BaseClass;
import org.mobile.engine.BaseClass2;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;


public class ipaApkFileUpload extends BaseClass2 {
	
	@Test
	public void sampleTestCase(ITestContext context) throws MalformedURLException, IOException {
		
		/* Network Directory */
		String fileLocationSourceNetwork = "//XX_BETA_XX/home/downloads/myChevroletCopy.ipa";
    
		/* Source Directory */
      //  String fileLocationSourceDrive = "C:/Users/Raj/Downloads/ipaFIle";    
		
        /* Destination Directory */
      //  String fileLocationDestination = "C:/Users/Raj/git/GradleSampleProject/temp/";
        
        /**Copy the files from the network or local to desired location */
        uploadMedia(context, fileLocationSourceNetwork, "ipa");
	}


	
	@BeforeClass(alwaysRun = true)
    public void setUp(ITestContext context) throws Exception {			
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
	       }	
	}
	
	@AfterClass(alwaysRun = true)
	public void afterClass() {		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy_hh_mm_SSSa");
		String formattedDate = sdf.format(date);
		System.out.println(formattedDate); 
		
		try {
				//downloadReport(driverIOS,"pdf", "C:/eclipse/workspace/GradleSampleProject/perfectoReports/" + context.getName() + "_" + formattedDate);
				System.out.println("\n\nReport url = " + reportiumClient.getReportUrl() +"\n\n");		
		} catch (Exception e)  {
			e.printStackTrace();
		} finally {
			
			
		}
		
		 System.out.println("\n\nPerfecto Report url = " + reportiumClient.getReportUrl());
	}
	
	

}
