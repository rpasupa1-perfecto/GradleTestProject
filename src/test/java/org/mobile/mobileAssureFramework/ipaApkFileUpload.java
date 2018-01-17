package org.mobile.mobileAssureFramework;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.mobile.engine.PerfectoUploadBuilds;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;



public class ipaApkFileUpload extends PerfectoUploadBuilds {
	public Map<String, String> testParams;
	
	@Test
	public void sampleTestCase(ITestContext context) throws MalformedURLException, IOException {
		testParams = context.getCurrentXmlTest().getAllParameters();
		
		/* Network Directory */
		//testParams.get("iOS_XpathPropfile");
		String fileLocationSourceNetwork = testParams.get("networkDirectory");
    
		/* Source Directory */
		//  String fileLocationSourceDrive = "C:/Users/Raj/Downloads/ipaFIle";    
		
        /* Destination Directory */
		//  String fileLocationDestination = "C:/Users/Raj/git/GradleSampleProject/temp/";
        
        /**Copy the files from the network or local to desired location */
        uploadMedia(context, fileLocationSourceNetwork, "ipa");
	}


	
	@BeforeClass(alwaysRun = true)
    public void setUp(ITestContext context) throws Exception {			
		//createReportium(context);
	}
	
	@BeforeMethod(alwaysRun = true) 
	public void beforeMethod(ITestContext context) {
		//getReportiumClient().testStart(context.getName(), new TestContext());
	}
	
	@AfterMethod(alwaysRun = true)
	public void destroy(ITestContext context, ITestResult result) throws Exception {
//		 if ( reportiumClient!= null )
//	       {
//				reportiumClient.testStop( result.getStatus() == ITestResult.SUCCESS ? TestResultFactory.createSuccess() : 
//					TestResultFactory.createFailure( result.getThrowable().getMessage(), result.getThrowable() ) );
//	       }	
	}
	
	@AfterClass(alwaysRun = true)
	public void afterClass() {		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy_hh_mm_SSSa");
		String formattedDate = sdf.format(date);
		System.out.println(formattedDate); 
		
		try {
				//downloadReport(driverIOS,"pdf", "C:/eclipse/workspace/GradleSampleProject/perfectoReports/" + context.getName() + "_" + formattedDate);
				//System.out.println("\n\nReport url = " + reportiumClient.getReportUrl() +"\n\n");		
		} catch (Exception e)  {
			e.printStackTrace();
		} finally {
			
			
		}
		
	//	 System.out.println("\n\nPerfecto Report url = " + reportiumClient.getReportUrl());
	}
	
	

}
