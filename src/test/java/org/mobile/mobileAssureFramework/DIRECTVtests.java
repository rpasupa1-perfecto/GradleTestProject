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


import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;


public class DIRECTVtests extends BaseClass {
	
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
	public void DIRECTVsampleTestCase(ITestContext context) throws MalformedURLException, IOException {
		
		try {				
				/* Watch View Page */	
			//By.xpath: 
			/* old xpath but still works */	
			//String watchViewPage = "//*[@name= 'carouselCollection-tableView' or @name = 'Iphone-Docked-VideoViewController' or @name = 'VideoViewController-FullScreen-Portrait' or @name = 'VideoPlayerContainerView_VideoPlaceHolderView']";
			/* Customer Changed to new Xpath */
			String watchViewPage = "//*[@name = 'Iphone-Docked-VideoViewController' or @name = 'VideoViewController-FullScreen-Portrait' or @name = 'VideoPlayerContainerView_VideoPlaceHolderView']";
				
				
				try {
					WebElement WatchViewMainPage = findElementByXpath("WatchNowPage.isOnWatchNowPage()-Perfecto", watchViewPage, 30);
					WatchViewMainPage.isDisplayed();
					if (WatchViewMainPage!=null) {
						assertTrue("WatchView Loaded !!!", true);
						
						Point location = WatchViewMainPage.getLocation();
						Dimension size = WatchViewMainPage.getSize();
						
						System.out.println("Location: " + location.toString());
						System.out.println("Size: " + size.toString());
						
					} else if (WatchViewMainPage==null) {
						assertTrue("Page Not Loaded !!!", false);
						System.out.println("Image Not Loaded");
					}
				} catch (Exception e) {
					assertTrue("Logo Not Loaded !!!", false);
					e.printStackTrace();
				}
				
				
				/* Look for WatchNow and click on it */					
				String watchNowTab = "appFooter_watchNowButton";
				
				try {
					WebElement watchNowBTN = findElementByID("NavigationPage.tapWatchNowTab()-Perfecto", watchNowTab, 30);		
					watchNowBTN.isDisplayed(); //Takes ScreenShot
					if (watchNowBTN!=null) {
						watchNowBTN.click();
						assertTrue("watchNow BTN Loaded !!!", true);
						
					} else if (watchNowBTN==null) {
						assertTrue("watchNow Not Loaded !!!", false);
						System.out.println("WatchNOW txt Not Available");
					}
				} catch (Exception e) {
					assertTrue("Exception WatchNow Not Loaded...check string !!!", false);
					e.printStackTrace();
				}
						
				
				/* Look for Library Tab and click on it */					
				String myLibraryTab = "My Library";
				
				try {
					WebElement myLibTabBTN = findElementByID("NavigationPage.tapMyLibraryTab()-Perfecto", myLibraryTab, 30);		
					myLibTabBTN.isDisplayed(); //Takes ScreenShot
					if (myLibTabBTN!=null) {
						myLibTabBTN.click();
						assertTrue("Lib  BTN Loaded !!!", true);
						
					} else if (myLibTabBTN==null) {
						assertTrue("Lib Not Loaded !!!", false);
						System.out.println("Lib  Not Available");
					}
				} catch (Exception e) {
					assertTrue("Lib  Not Loaded...check string !!!", false);
					e.printStackTrace();
				}	
				
				
				
				/* In Library Tab Verify */					
				String myLibraryTabRecBTN = "MyLibrary.dvrRecordingsButton";
				
				try {
					WebElement myLibraryTabRec = findElementByID("MyLibraryPageImpl.isOnLibrayPage()-Perfecto", myLibraryTabRecBTN, 30);		
					myLibraryTabRec.isDisplayed(); //Takes ScreenShot
					if (myLibraryTabRec!=null) {
						
						Point location = myLibraryTabRec.getLocation();
						Dimension size = myLibraryTabRec.getSize();
						
						System.out.println("Location: " + location.toString());
						System.out.println("Size: " + size.toString());
						assertTrue("watchNow BTN Loaded !!!", true);
						
					} else if (myLibraryTabRec==null) {
						assertTrue("watchNow Not Loaded !!!", false);
						System.out.println("WatchNOW txt Not Available");
					}
				} catch (Exception e) {
					assertTrue("Exception WatchNow Not Loaded...check string !!!", false);
					e.printStackTrace();
				}	
		
	
			
		
				/* On Library Page click downloads btn */					
				String myLibraryTabDownloadBtn = "Downloads";
				
				try {
					WebElement myLibTabDownloadBTN = findElementByID("DownloadPage.tabDownloads()-Perfecto", myLibraryTabDownloadBtn, 30);		
					myLibTabDownloadBTN.isDisplayed(); //Takes ScreenShot
					if (myLibTabDownloadBTN!=null) {
						myLibTabDownloadBTN.click();
						assertTrue("Download BTN Loaded !!!", true);
						
					} else if (myLibTabDownloadBTN==null) {
						assertTrue("Download BTN Not Loaded !!!", false);
						System.out.println("Download  Not Available");
					}
				} catch (Exception e) {
					assertTrue("Exception.....DownloadBTN Not Loaded...check locator !!!", false);
					e.printStackTrace();
				}	
				
				
				
				/* On Library Page click downloads btn */					
				String downloadPageBtn = "//XCUIElementTypeStaticText[contains(@label, 'Downloads')]";
				
				try {
					WebElement downloadPageButton = findElementByXpath("DownloadPageImpl.isDownloadsPageEmpty()-Perfecto", downloadPageBtn, 30);		
					downloadPageButton.isDisplayed(); //Takes ScreenShot
					if (downloadPageButton!=null) {
						//downloadPageButton.click();
						
						Point location = downloadPageButton.getLocation();
						Dimension size = downloadPageButton.getSize();
						
						System.out.println("Location: " + location.toString());
						System.out.println("Size: " + size.toString());
						
						
						assertTrue("Download BTN Loaded !!!", true);
						
					} else if (downloadPageButton==null) {
						assertTrue("Download BTN Not Loaded !!!", false);
						System.out.println("Download  Not Available");
					}
				} catch (Exception e) {
					assertTrue("Exception.....DownloadBTN Not Loaded...check locator !!!", false);
					e.printStackTrace();
				}	
			
				
				
				
				/* On Library Page click downloads btn */					
				String downloadPageImp = "//XCUIElementTypeStaticText[contains(@label, 'Movies and TV shows that you download will appear here')]";
				
				try {
					WebElement downloadPageImpli = findElementByXpath("DownloadPageImpl.isDownloadsPageEmpty2()-Perfecto", downloadPageImp, 30);		
					downloadPageImpli.isDisplayed(); //Takes ScreenShot
					if (downloadPageImpli!=null) {
						//myLibTabDownloadBTN.click();
						Point location = downloadPageImpli.getLocation();
						Dimension size = downloadPageImpli.getSize();
						
						System.out.println("Location: " + location.toString());
						System.out.println("Size: " + size.toString());
						
					
						assertTrue("Download BTN Loaded !!!", true);
						
					} else if (downloadPageImpli==null) {
						assertTrue("Download BTN Not Loaded !!!", false);
						System.out.println("Download  Not Available");
					}
				} catch (Exception e) {
					assertTrue("Exception.....DownloadBTN Not Loaded...check locator !!!", false);
					e.printStackTrace();
				}	
			
			
	} catch (Exception e) {
		e.printStackTrace();
	}
		
}

	
	
	
	
//	@BeforeClass(alwaysRun = true)
//    public void setUp(ITestContext context) throws Exception {	
//		engineSetup (context);		
//		driver = getDriver();
//		driverIOS = getDriverIOS();
//		driverAndroid = getDriverAndroid();			
//		createReportium(context);	
//		
//		
//	}
	

	

	
//	@AfterClass(alwaysRun = true)
//	public void afterClass() {		
//		Date date = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy_hh_mm_SSSa");
//		String formattedDate = sdf.format(date);
//		System.out.println(formattedDate); 		
//		
//		try {
//			if(testParams.get("driver").equals("AppiumIOS")) {
//				driverIOS.closeApp();	
//				driverIOS.close();
//				//downloadReport(driverIOS,"pdf", "C:/eclipse/workspace/GradleSampleProject/perfectoReports/" + context.getName() + "_" + formattedDate);
//				//reportURL=reportiumClient.getReportUrl();
//			//	System.out.println("\n\nReport url = " + reportiumClient.getReportUrl() +"\n\n");
//				 
//			} else if (testParams.get("driver").equals("AppiumAndroid")) {
//				driverAndroid.closeApp();
//				driverAndroid.close();
//			} else if (testParams.get("driver").equals("RemoteWebDriver")) {			
//				driver.close();					
//			}
//		} catch (Exception e)  {
//			e.printStackTrace();
//		} finally {
//			if(testParams.get("driver").equals("AppiumIOS")) {
//				driverIOS.quit();
//			} else if (testParams.get("driver").equals("AppiumAndroid")) {
//				driverAndroid.quit();
//			} else if (testParams.get("driver").equals("RemoteWebDriver")) {
//				driver.quit();	
//			}
//			
//		}
//		reportURL=reportiumClient.getReportUrl();
//		System.out.println("\n\nPerfectoReportUrl = \n" + reportiumClient.getReportUrl()+ "\n\n");
//	}

}
