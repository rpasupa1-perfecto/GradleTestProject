package org.mobile.mobileAssureFramework;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.mobile.engine.BaseClass;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
//import org.testng.asserts.SoftAssert;
import org.testng.asserts.SoftAssert;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;


public class TC_WifiHotspot_ONOFF extends BaseClass {
	
	/* RemoteWeb Driver */
	public RemoteWebDriver driver;
	/* Appium IOS Driver */
	public IOSDriver driverIOS;
	/* Appium Android Driver */
	public AndroidDriver driverAndroid;
	SoftAssert sAssert = new SoftAssert();
	
	@Test
	public void TC_WifiHotSpot_ONOFF(ITestContext context) throws MalformedURLException, IOException {
		
		try {
			
			/* Login */
			long startLogin = System.currentTimeMillis();
			login();
			/* ScreenShot Enable */
			//	uploadScreenshots(context);
			long endLogin = System.currentTimeMillis();
			long LoginTime = endLogin - startLogin;		
			System.out.println("Login Performance: " + LoginTime);
						
			/* Click on WifiHotSpot */
			clickWifiHotSpot();
		
			/* Verify Wifi Hotspot page is loaded */
			String wifihotSpot = "//UIANavigationBar//*[@label='Wi-Fi Hotspot']";
			try {
				WebElement wifiHotSpotheader = findElementByXpath(wifihotSpot, 1);
				if (wifiHotSpotheader!=null) {			
					assertTrue("Wifi HotSpot element present !!!", true);			 
				} else if (wifiHotSpotheader==null)  {
					assertTrue("Wifi HotSpot element present !!!", false);	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
					
			/* Swipe all the way Down */
			swipeDownScreen(1);
			
			/* Check HotSpot Enabled Radio button Status and enable or disable it from its original status */	
			String hotSpotRadioBtnEbl = "//*[@label='Hotspot Enabled' and @value='1' and @visible='true']";
			String hotSpotRadioBtnDis = "//*[@label='Hotspot Disabled' and @value='0' and @visible='true']";			
			try {
				WebElement hotSRadioButtonEbl = findElementByXpath(hotSpotRadioBtnEbl, 1);
				WebElement hotSRadioButtonDis = findElementByXpath(hotSpotRadioBtnDis, 1);			
				/* If Enabled we disable radio btn */
				if (hotSRadioButtonEbl!=null) {	
					/* Disable Radio Button */
					hotSRadioButtonEbl.click();			
				} else if (hotSRadioButtonDis!=null) {
					/* Enable Radio Button */
					hotSRadioButtonDis.click();
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
					
			
			/* Click on Save Button */
			String saveBtn = "//*[@label='Save' and @enabled='true']";
			try {
				WebElement saveButton = findElementByXpath(saveBtn, 1);
				if (saveButton!=null) {
					saveButton.click();
				} else if (saveButton==null) {
					assertTrue("Wifi HotSpot Save Button Not Enabled !!!", false);	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			/* Enter Pin Number */
			enterPin();
				
			/* Wait for Msg Aknowledgement */		
			try {
				
				String msgAknowledgement = "//*[@label='Changes will take effect at the next ignition cycle.' and @visible='true']";
					WebElement msgAkw = findElementByXpath(msgAknowledgement, 25);
					if (msgAkw!=null) {			
						assertTrue("Wifi HotSpot Aknowledgement present !!!", true);						 					
						String okButton = "//*[@label='OK' and @visible='true' and @enabled='true']";
						WebElement okBTN = findElementByXpath(okButton, 0);
						okBTN.click();		
					} else if (msgAkw==null)  {
					//	sAssert.assertTrue(false, "Wifi HotSpot Aknowledgement NOT present !!!");
						//assertTrue("Wifi HotSpot Aknowledgement NOT present !!!", false);	
					}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
				
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
//			uploadScreenshots(context);
			
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
		/* Verify Preferred Dealer */
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
	
	
	public void clickKeyFobLock () {	
		boolean flag = false;
		/* Verify KeyFob  */
		String keyFob = "//*[@label='Key Fob' and @visible='true']";				
		try {
		/* TODO: will need to add a time limit */
			do {		
				WebElement keyFobLock = findElementByXpath(keyFob, 1);	
				if (keyFobLock!=null) {									
					flag = true;
					keyFobLock.click();											
				} else if (keyFobLock==null) {
					/* Scroll to Preferred Dealer */
					swipeDownOneScreen(1);
				}
			} while (!flag);	
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public void clickWifiHotSpot () {	
		boolean flag = false;
		/* Verify Wifi Hotspot  */
		String wifiHotSpt = "//*[@label='Wi-Fi Hotspot' and @visible='true']";				
		try {
		/* TODO: will need to add a time limit */
			do {		
				WebElement wifiHotSpot = findElementByXpath(wifiHotSpt, 1);	
				if (wifiHotSpot!=null) {									
					flag = true;
					wifiHotSpot.click();											
				} else if (wifiHotSpot==null) {
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
			WebElement cancelUp = findElementByXpath(cancelUpgrade, 1);
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
			findElementByXpath(userName, 1).sendKeys("2016voltrocks@gm.com");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*Set Password */
		String pass = "//*[@value='Password']";
		try {
			findElementByXpath(pass, 1).sendKeys("onstar123");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/* Click Login Btn */
		driverIOS.context("NATIVE_APP");
		String btn_LogIn = "//*[@label='Log In']";
		try {
			findElementByXpath(btn_LogIn, 1).click();
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
			WebElement logLogo = findElementByXpath(loginLogo, 2);
			if (logLogo!=null) {
				assertTrue("Login Page Logo Loaded !!!", true);
			} else if (logLogo==null)  {
				assertTrue("Login Page Logo Logo head not Loaded !!!", false);
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
				element = findElementByXpath(xpath, 1);
				if (element!=null) {
					System.out.println("Element is present: " + xpath);				
				} else if (element==null) {
					flag = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				assertTrue("element not Disappeared..Check xpath !!!", false);
				flag = true;
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
		//	driverIOS.swipe(startX, startY, endX, endY, 1);
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
		boolean actFlag = false;
		boolean logoutFlag = false;
		/* Click Account */	
		try {
			String account = "//*[@label='Account' and @visible='true']";		
			do {
				WebElement acct = findElementByXpath(account, 1);
				if (acct!=null) {
					actFlag = true;
					acct.click();
				} else if (acct==null) {
					/* Swipe Down */
					swipeDownScreen(1);
				} 
			} while (!actFlag);
		
		/* Click Logout */
			String logout = "//*[@label='Log Out' and @visible='true']";
			do {
				WebElement Btnlogout = findElementByXpath(logout, 1);
				if (Btnlogout!=null) {
					logoutFlag = true;
					Btnlogout.click();
				} else if (Btnlogout==null) {
					/* Swipe Down */
					swipeDownScreen(1);
				}
			} while (!logoutFlag);
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
	
	public void enterPin() {
		boolean flag = false;
		/* Set pin 4 3 2 1 */
		String pinNum1 = "//*[@label='1']";
		String pinNum2 = "//*[@label='2']";
		String pinNum3 = "//*[@label='3']";
		String pinNum4 = "//*[@label='4']";
		
			try {
				WebElement pinNumber1 = findElementByXpath(pinNum1, 1);		
				if (pinNumber1!=null) {
					pinNumber1.click();
					
					WebElement pinNumber2 = findElementByXpath(pinNum2, 1);
					if (pinNumber2!=null) {
						pinNumber2.click();
					} 			
					WebElement pinNumber3 = findElementByXpath(pinNum3, 1);
					if (pinNumber3!=null) {
						pinNumber3.click();
					} 		
					WebElement pinNumber4 = findElementByXpath(pinNum4, 1);
					if (pinNumber4!=null) {
						pinNumber4.click();
					} 
					
				} else {
					sAssert.assertTrue(false, "Pin Number Page ByPassed !!!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
