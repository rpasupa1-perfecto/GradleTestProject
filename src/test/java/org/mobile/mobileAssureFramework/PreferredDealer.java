package org.mobile.mobileAssureFramework;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;


public class PreferredDealer extends BaseClass {
	
	/* RemoteWeb Driver */
	public RemoteWebDriver driver;
	/* Appium IOS Driver */
	public IOSDriver driverIOS;
	/* Appium Android Driver */
	public AndroidDriver driverAndroid;

	SoftAssert sAssert = new SoftAssert();
	
	@Test
	public void sampleTestCase(ITestContext context) throws MalformedURLException, IOException {
		
		try {
			
			/* Login */
			long startLogin = System.currentTimeMillis();
			login();
			/* ScreenShot Enable */
		//	uploadScreenshots(context);
			long endLogin = System.currentTimeMillis();
			long LoginTime = endLogin - startLogin;		
			System.out.println("Login Performance: " + LoginTime);
			
			
			/* Click on Preferred Dealer */
			clickPreferredDealer();
		
			/* ScreenShot Enable */
	//		uploadScreenshots(context);
			
			
			/* Verify Dealer Page */
			String prefDealerPage = "//UIANavigationBar//*[@label='Preferred Dealer']";
			try {
				WebElement prefdelPage = findElementByXpath(prefDealerPage, 1);
				if (prefdelPage!=null) {
					assertTrue("Dealer Page Loaded !!!", true);
					//sAssert.assertTrue(false, "Dealer Page NOT Loaded !!!"); 				
				} else {
					assertTrue("Preferred Dealer Page Not Loaded !!!", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
						
			/*Verify Dealer Name */		
			String dealerName = "//*[@label='Dealer Name']";
			try {
				WebElement dlrName = findElementByXpath(dealerName, 1);
				if (dlrName!=null) {
					assertTrue("Dealer Name Loaded !!!", true);
					//sAssert.assertTrue(false, "Dealer Name NOT Loaded !!!"); 					
				} else {
					sAssert.assertTrue(false, "Dealer Name NOT Loaded !!!"); 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			
			/*Verify Dealer Address */	
			String dealerAdd = "//*[@label='Dealer Address']";
			try {
				WebElement dlrAddress = findElementByXpath(dealerAdd, 1);
				if (dlrAddress!=null) {			
					assertTrue("Dealer Address Loaded !!!", true);			
					//sAssert.assertTrue(false, "Dealer Address NOT Loaded !!!"); 
				} else {
					sAssert.assertTrue(false, "Dealer Address NOT Loaded !!!"); 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			/*Verify Dealer PhoneNum */
			String dealerNum = "//*[@label='Dealer Phone Number']";
			try {
				WebElement dlrNumber = findElementByXpath(dealerNum, 1);
				if (dlrNumber!=null) {
					assertTrue("Dealer Number Loaded !!!", true);
					//sAssert.assertTrue(false, "Dealer Number NOT Loaded !!!"); 			
				} else {
					sAssert.assertTrue(false, "Dealer Number NOT Loaded !!!"); 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			/* ScreenShot Enable */
//			uploadScreenshots(context);
				
			/* Back to main page */
			String btnBack = "//*[@label='BACK']";
			try {
				WebElement backButton = findElementByXpath(btnBack, 1);
				if (backButton!=null) {
					backButton.click();
				} else {
					System.out.println("Back Button Not Displayed");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			/* Logout */			
			long startLogout = System.currentTimeMillis();
			logout();
			long endLogout = System.currentTimeMillis();
			long LogoutTime = endLogout - startLogout;		
			System.out.println("Logout Performance: " + LogoutTime);
					
			/* ScreenShot Enable */
			uploadScreenshots(context);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/* 
	 * Objects needs to support the visible attribute or else method will not work.
	 * 
	 * */
	public void clickPreferredDealer() {
	
		boolean flag = false;
		/* Verify if Preferred Dealer */
		String prefDealer = "//*[@label='Preferred Dealer' and @visible='true']";				
		try {
		/* TODO: will need to add a time limit */
			do {		
				WebElement prfDealer = findElementByXpath(prefDealer, 1);	
				if (prfDealer!=null) {									
					flag = true;
					prfDealer.click();											
				} else if (prfDealer==null) {
					/* Scroll to Preferred Dealer */
					swipeDownOneScreen(1);
				}
			} while (!flag);	
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void login() throws IOException {

		System.out.println("\n\n*****Loggin In....: ");
				
		/* Set UserName */
		String cancelUpgrade = "//*[@label='Cancel']";
		try {
			WebElement cancelUp = findElementByXpath(cancelUpgrade, 0);
			if (cancelUp!=null) {
				cancelUp.click();
			} else {
				System.out.println("Cancel Popup Not Displayed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/* Set UserName */
		String userName = "//*[@value='Username']";
		try {
			findElementByXpath(userName, 2).sendKeys("2016voltrocks@gm.com");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*Set Password */
		String pass = "//*[@value='Password']";
		try {
			findElementByXpath(pass, 2).sendKeys("onstar123");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/* Click Login Btn */
		driverIOS.context("NATIVE_APP");
		String btn_LogIn = "//*[@label='Log In']";
		try {
			findElementByXpath(btn_LogIn, 2).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
						
		/* Wait for the Progress spinner to disappear */		
		try {
			String spinner = "//*[@label='In progress']";
				if(elementIsDisappeared(spinner)) {
					System.out.println("Element is Disappeared..Sucessfully");
				} else {
					System.out.println("Element is NOT Disappeared");
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		/* Initial Agree User Terms */
		String agreeUserTerms = "//*[@label='Agree']";
		try {
			WebElement agreeTerms = findElementByXpath(agreeUserTerms, 1);
				if((agreeTerms)!=null) {
					agreeTerms.click();
					agreeTerms.click();
				} else {
					System.out.println("UserAgreement Not Displayed");
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		
		
		String  loginLogo = "//*[@name='app_header_chevrolet']";
		try {
			WebElement logLogo = findElementByXpath(loginLogo, 1);
			if (logLogo!=null) {
				if (logLogo.isDisplayed()) {
					assertTrue("Login Page Logo Loaded !!!", true);
				} else {
					sAssert.assertTrue(false, "Login Page Logo NOT Loaded !!!"); 
				}
			} else {
				sAssert.assertTrue(false, "Login Page Logo NOT Loaded !!!"); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.print("Passed ******\n\n");
	}
	
	
	public boolean elementIsDisappeared(String xpath) {		
		WebElement element;
		boolean flag = false;	
		do {		
			try {
				element = findElementByXpath(xpath, 0);
				if ((element)!=null) {
					if(element.isDisplayed()) {
						flag = false;
					} else if (!element.isDisplayed()) {
						flag = true;
					}	
				} else if (element==null) {
					flag = true;
				}
		
			} catch (Exception e) {
				flag = true;
				e.printStackTrace();
			}			
		}
		while (!flag);		
		return flag;
	}
	
	public void swipeDownScreen(int count) {
		Dimension size = driverIOS.manage().window().getSize();		
		double tempy = 0.70;
		double tempx = 0.65;
		
		int startX = (size.width)/2;
		int startY = (int) (size.height * tempy);	
		int endX = (size.width)/2;		
		int endY = (int) (size.height * tempx);          
		
		for (int i = 1; i <= count; i++) {
			//driverIOS.swipe(startX, startY, endX, endY, 1);
		}
	}
	
	public void swipeDownOneScreen(int count) throws InterruptedException {
		Dimension size = driverIOS.manage().window().getSize();		
		double tempy = 0.70;
		double tempx = 0.23; 
		
		int startX = (size.width)/2;
		int startY = (int) (size.height * tempy);	
		int endX = (size.width)/2;		
		int endY = (int) (size.height * tempx);          
		
		for (int i = 1; i <= count; i++) {
			//driverIOS.swipe(startX, startY, endX, endY, 1);
			Thread.sleep(1000);
		}
	}
	
	
	public void logout() {
		/* Click Account */	
		try {
			String account = "//*[@label='Account']";
			WebElement acct = findElementByXpath(account, 3);
			if (acct!=null) {
				swipeDownScreen(1);
				acct.click();
			} else {
				System.out.println("Account Button Not Displayed");
			}
			String logout = "//*[@label='Log Out']";
			WebElement Btnlogout = findElementByXpath(logout, 3);
			if (Btnlogout!=null) {
				swipeDownScreen(1);
				Btnlogout.click();
			} else {
				System.out.println("Logout Button Not Displayed");
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
	public void destroy(ITestContext context) throws Exception {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy_hh_mm_SSSa");
		String formattedDate = sdf.format(date);
		System.out.println(formattedDate); 
		
		try {
			if(testParams.get("driver").equals("AppiumIOS")) {
				driverIOS.closeApp();	
				driverIOS.close();
				downloadReport(driverIOS,"pdf", "C:/eclipse/workspace/GradleSampleProject/perfectoReports/" + context.getName() + "_" + formattedDate);
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
