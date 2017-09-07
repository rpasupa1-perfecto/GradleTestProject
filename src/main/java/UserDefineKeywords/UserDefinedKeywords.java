package UserDefineKeywords;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

//import org.apache.bcel.generic.GETFIELD;
import org.apache.commons.io.FileUtils;
//import org.eclipse.jdt.internal.compiler.ast.AnnotationMethodDeclaration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.perfectomobile.selenium.util.EclipseConnector;

public class UserDefinedKeywords extends RogersBuyFlow {


	public static String Matchstring;
	public static String ActualResults="";
	public static String step_logical_name="";
	static Properties props = new Properties();
	static public Properties Perfecto=new Properties();
	public static HashMap<Integer, String> hm=new HashMap<>();



	public static boolean DevicesetUpForMobileBrowser(By element, String data) throws Exception
	{   
		try{
			
			String fileName = System.getProperty("user.dir") + "/src/test/resources/RogersPerfectoProperties.properties";
			
			Perfecto.load(new FileInputStream(new File(fileName)));
			
			String browserName="safari";
			DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
	
//			System.setProperty("os", Perfecto.getProperty("OS"));
//			System.setProperty("os_version", Perfecto.getProperty("os_version")); 
//			System.setProperty("manufacturer", Perfecto.getProperty("manufacturer"));
//			System.setProperty("model", Perfecto.getProperty("model"));  
//			System.setProperty("perfectoUserName", Perfecto.getProperty("perfectoUserName"));
//			System.setProperty("perfectoPassword", Perfecto.getProperty("perfectoPassword"));
//			System.setProperty("deviceName", Perfecto.getProperty("DeviceName"));

		//	capabilities.setCapability("user", Perfecto.getProperty("perfectoUserName"));
		//	capabilities.setCapability("password",  Perfecto.getProperty("perfectoPassword") );
		//	capabilities.setCapability("deviceName", Perfecto.getProperty("DeviceName"));
			capabilities.setCapability("user", "rajp@perfectomobile.com");
			capabilities.setCapability("password",  "Perfecto123");
			capabilities.setCapability("deviceName", Perfecto.getProperty("DeviceName"));
			String host=Perfecto.getProperty("host2");
			
//			capabilities.setCapability("platformVersion", Perfecto.getProperty("os_version"));
//			capabilities.setCapability("manufacturer",Perfecto.getProperty("manufacturer"));	
//			capabilities.setCapability("model",Perfecto.getProperty("model"));	
			//capabilities.setCapability("automationName", "appium");
	//		capabilities.setCapability("platformName", Perfecto.getProperty("OS"));	
			// Call this method if you want the script to share the devices with the Perfecto Lab plugin.
	//		setExecutionIdCapability(capabilities, host);
	//		capabilities.setCapability("user", Perfecto.getProperty("perfectoUserName"));
	//		capabilities.setCapability("password",  Perfecto.getProperty("perfectoPassword") );
			// Call this method if you want the script to share the devices with the Perfecto Lab plugin.
	//		PerfectoLabUtils.setExecutionIdCapability(capabilities, host);

			
			driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
			// IOSDriver driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
			driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(90, TimeUnit.SECONDS);
			//domDriver.close();
			driver.get(data);

			
			return true;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return false;
		}

	}

	public static void setExecutionIdCapability(DesiredCapabilities capabilities, String host) throws IOException {
		EclipseConnector connector = new EclipseConnector();
		String eclipseHost = connector.getHost();
		if ((eclipseHost == null) || (eclipseHost.equalsIgnoreCase(host))) {
			String executionId = connector.getExecutionId();
			capabilities.setCapability(EclipseConnector.ECLIPSE_EXECUTION_ID, executionId);
		}
	} 

	public static boolean click(By element, String data) throws Exception {

		try {
			driver.findElement(element).click();
			return true;
		}


		catch (Exception e) {

			System.out.println("Exception"+e);
			e.printStackTrace(System.out);
			//KeywordsCoreFunctions.ActualResults="Unable to click on "+KeywordsCoreFunctions.step_logical_name; 
			return false; 
		}
	}

	public static boolean isdisplayed(By element, String data) throws Exception {

		try {

			driver.findElement(element).isDisplayed();
			return true;
		}


		catch (Exception e) {

			System.out.println("Exception"+e);
			e.printStackTrace(System.out);
			//KeywordsCoreFunctions.ActualResults="Unable to find "+KeywordsCoreFunctions.step_logical_name; 
			return false; 
		}
	} 

	public static boolean ClearDriver() {
		try {
			for (int j = 1; j <= 3; ++j) {
				System.out.println(j + " Attempt ...");
				if (!(driver.toString().contains("null"))) {
					System.out.println("Deleting Browser's Cookies...");

					System.out.println("Quiting the Browser...");
					driver.quit();
				} else {
					return true;
				}

			}

			return true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}  


	public static boolean Swipescreens(By element, String data)
	{

		try{
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			for(int i=0;i<Integer.parseInt(data);i++)
			{
				if(element!=null)
					driver.findElement(element);
				((JavascriptExecutor) driver).executeScript("scroll(0,"+i*500+");");	


				takedevicescreenshot(element, data);
			}

		}
		catch(Exception e)
		{
			System.out.println("Unable to swipe");
		}
		if(element!=null)
			driver.findElement(element);
		driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
		return true;
	} 


	public static boolean takedevicescreenshot(By element,String data) throws IOException
	{
		String workingDir = System.getProperty("user.dir");

		Date now = new Date();
		String curDate = new SimpleDateFormat("yyyy-MMM-dd").format(now);
		String curTime = new SimpleDateFormat("HH-mm-ss-SSS").format(now);

		try{


			File scrFile;
			scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+curTime+".png"));	

	

			// Now you can do whatever you need to do with it, for example copy somewhere
			/*String Location=GetRunTimedata("ScreenshotFolderName");
				String sce=GetRunTimedata("Scenario_Name");
				FileUtils.copyFile(scrFile, new File(Location+sce+"_"+os+"_"+"_"+browser+"_"+curTime+".png"));			*/

			/*  * Takes a snapshot of the screen for the specified test.
			 * The output of this function can be used as a parameter for setDescription()


			 * Takes a snapshot of the screen for the specified test.
			 * The output of this function can be used as a parameter for setDescription()

		        HttpResponse<JsonNode> response = Unirest.post("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}/snapshots")
		                .basicAuth(username, authkey)
		                .routeParam("seleniumTestId", driver.getSessionId().toString())
		                .asJson(); 
		        // grab out the snapshot "hash" from the response
		        String snapshotHash = (String) response.getBody().getObject().get("hash");
		        System.out.println(snapshotHash);*/
			return true;}
		catch(Exception e)
		{
			System.out.println(e);
			//KeywordsCoreFunctions.ActualResults="Unable to take Snapshot";
			return false;
		}

	}



	public static boolean scrolltoanelement(By element,String value)

	{ 

		try{


			WebElement ele = driver.findElement(element);
			Actions actions = new Actions(driver);
			actions.moveToElement(ele);
			((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", ele);

			return true;
		}
		catch (Exception e)
		{
			System.out.println(e);
			//KeywordsCoreFunctions.ActualResults="Unable to find "+KeywordsCoreFunctions.step_logical_name;
			return false;
		}

	} 




}


