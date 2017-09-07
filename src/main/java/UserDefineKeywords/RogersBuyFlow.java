package UserDefineKeywords;

import java.io.*;
import java.net.*;


import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;



public class RogersBuyFlow {
	
	public static RemoteWebDriver driver;
	
    public static void main(String[] args) throws MalformedURLException, IOException {
        System.out.println("Run started");
        
       try{
        By element = null; 
        String data = "";       
        
        UserDefinedKeywords.DevicesetUpForMobileBrowser(element, "https://www.rogers.com");
        element = getElement("//a[text()='Internet']"); 
        UserDefinedKeywords.isdisplayed(element, data);
        
        element = getElement("//a[text()='Internet']"); 
        UserDefinedKeywords.Swipescreens(element, "5");
        
        element = getElement("//span[text()='Internet']"); 
        UserDefinedKeywords.click(element, data);
        
       // element = getElement("//span[text()='Shop']/following::span[text()='Internet']");
        element = getElement("//a[text()='Internet Packages']"); 
        UserDefinedKeywords.isdisplayed(element, data);        
        UserDefinedKeywords.Swipescreens(element, "5");
        
        element = getElement("//a[text()='Internet Packages']"); 
        UserDefinedKeywords.click(element, data); 
        
        element = getElement("name=packages"); 
        UserDefinedKeywords.isdisplayed(element, data); 
        
        element = getElement("(//*[@class='rogers-ignite bottom-border'])[4]/div"); 
        UserDefinedKeywords.scrolltoanelement (element, data); 
        UserDefinedKeywords.takedevicescreenshot(element, data);
        
        
      
         
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            	 //driver.manage().deleteAllCookies();
                 driver.close();
              
               
            } catch (Exception e) {
                e.printStackTrace();
            }
            driver.quit();
           
        }
       // System.out.println ("####2 "+driver.getCurrentUrl());
        System.out.println("Run ended");
    }
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
    /* private static void setExecutionIdCapability(DesiredCapabilities capabilities, String host) throws IOException {
        EclipseConnector connector = new EclipseConnector();
        String eclipseHost = connector.getHost();
        if ((eclipseHost == null) || (eclipseHost.equalsIgnoreCase(host))) {
            String executionId = connector.getExecutionId();
            capabilities.setCapability(EclipseConnector.ECLIPSE_EXECUTION_ID, executionId);
        }
        }*/
    
     
     //////////////////
     public static By getElement(String object){ 
    	 By element = null; 
	 	  try 
	 	   { 
	 	 	 if(object.startsWith("id=")) 
	 	 	 	 element = By.id(object.replace("id=", "")); 
	 	 	 else if(object.startsWith("link=")) 
	 	 	 	 element = By.linkText(object.replace("link=", "")); 
	 	 	 else if(object.startsWith("name=")) 
	 	 	 	 element = By.name(object.replace("name=", "")); 
	 	 	 else if(object.startsWith("partiallink=")) 
	 	 	 	 element = By.partialLinkText(object.replace("partiallink=", "")); 
	 	 	 else if(object.startsWith("tag=")) 
	 	 	 	 element = By.tagName(object.replace("tag=", "")); 
	 	 	 else if(object.startsWith("class=")) 
	 	 	 	 element = By.className(object.replace("class=", "")); 
	 	 	 else if(object.startsWith("css=")) 
	 	 	 	 element = By.cssSelector(object.replace("css=", "")); 
	 	 	 else 
	 	 	 	 element = By.xpath(object); 
	 	   } 
	 	  catch (Exception e) 
	 	   { 
	 	 	 System.out.println(e); 
	 	   } 
	 	  return element; 
	 }  
     ////////////
}