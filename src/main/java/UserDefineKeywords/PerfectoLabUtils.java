package UserDefineKeywords;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;

//import com.itextpdf.text.Document;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.pdf.PdfWriter;
import com.perfectomobile.selenium.MobileDriver;
import com.perfectomobile.selenium.util.EclipseConnector;

//import utils.Log;

public class PerfectoLabUtils {


	private static WebDriver driver;
	private static boolean ieFlag;

	private static String      outputDirectory;
	private static String      archiveDir;
	private static String      archiveRunResultsDir;
	private static String      archiveScreenshotsDir;
	private static String      outputScreenshotsDirectory;
	private static String      todaysScreenshotsDir;
	private static String      archiveScreensinDateDir;
	private static String      archiveScreenshotsByTimeDir;

	private static final String HTTPS = "https://";
	private static final String MEDIA_REPOSITORY = "/services/repositories/media/";
	private static final String UPLOAD_OPERATION = "operation=upload&overwrite=true";
	private static final String UTF_8 = "UTF-8";


	//String filePath = PropertyUtils.getProperty("app.test.screenshots.directory");
	Date currentdt = new Date(); 
	private String curentDateTime = new SimpleDateFormat("yyyy-MMM-dd").format(currentdt);

	/**
	 * Download the report. 
	 * type - pdf, html, csv, xml
	 * Example: downloadReport(driver, "pdf", "C:\\test\\report");
	 * 
	 */
	public static void downloadReport(RemoteWebDriver driver, String type, String fileName) throws IOException {
		try { 
			String command = "mobile:report:download"; 
			Map<String, Object> params = new HashMap<>(); 
			params.put("type", type); 
			String report = (String)driver.executeScript(command, params); 
			File reportFile = new File(fileName + "." + type); 
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(reportFile)); 
			byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report); 
			output.write(reportBytes); output.close(); 
		} catch (Exception ex) { 
			System.out.println("Got exception " + ex); }
	}

	/**
	 * Download all the report attachments with a certain type.
	 * type - video, image, vital, network
	 * Examples:
	 * downloadAttachment(driver, "video", "C:\\test\\report\\video", "flv");
	 * downloadAttachment(driver, "image", "C:\\test\\report\\images", "jpg");
	 */
	public static void downloadAttachment(RemoteWebDriver driver, String type, String fileName, String suffix) throws IOException {
		try {
			String command = "mobile:report:attachment";
			boolean done = false;
			int index = 0;
				
			while (!done) {
				Map<String, Object> params = new HashMap<>();	

				params.put("type", type);
				params.put("index", Integer.toString(index));

				String attachment = (String)driver.executeScript(command, params);

				if (attachment == null) { 
					done = true; 
				}
				else { 
					File file = new File(fileName + index + "." + suffix); 
					BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file)); 
					byte[] bytes = OutputType.BYTES.convertFromBase64Png(attachment);	
					output.write(bytes); 
					output.close(); 
					index++; }
			}
		} catch (Exception ex) { 
			System.out.println("Got exception " + ex); 
		}
	}


	/**
	 * Uploads a file to the media repository.
	 * Example:
	 * uploadMedia("demo.perfectomobile.com", "john@perfectomobile.com", "123456", "C:\\test\\ApiDemos.apk", "PRIVATE:apps\\ApiDemos.apk");
	 */
	public static void uploadMedia(String host, String user, String password, String path, String repositoryKey) throws IOException {
		File file = new File(path);
		byte[] content = readFile(file);
		uploadMedia(host, user, password, content, repositoryKey);
	}

	/**
	 * Uploads a file to the media repository.
	 * Example:
	 * URL url = new URL("http://file.appsapk.com/wp-content/uploads/downloads/Sudoku%20Free.apk");
	 * uploadMedia("demo.perfectomobile.com", "john@perfectomobile.com", "123456", url, "PRIVATE:apps\\ApiDemos.apk");
	 */
	public static void uploadMedia(String host, String user, String password, URL mediaURL, String repositoryKey) throws IOException {
		byte[] content = readURL(mediaURL);
		uploadMedia(host, user, password, content, repositoryKey);
	}

	/**
	 * Uploads content to the media repository.
	 * Example:
	 * uploadMedia("demo.perfectomobile.com", "john@perfectomobile.com", "123456", content, "PRIVATE:apps\\ApiDemos.apk");
	 */
	public static void uploadMedia(String host, String user, String password, byte[] content, String repositoryKey) throws UnsupportedEncodingException, MalformedURLException, IOException {
		if (content != null) {
			String encodedUser = URLEncoder.encode(user, "UTF-8");
			String encodedPassword = URLEncoder.encode(password, "UTF-8");
			String urlStr = HTTPS + host + MEDIA_REPOSITORY + repositoryKey + "?" + UPLOAD_OPERATION + "&user=" + encodedUser + "&password=" + encodedPassword;
			URL url = new URL(urlStr);

			sendRequest(content, url);
		}
	}


	private static void sendRequest(byte[] content, URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/octet-stream");
		connection.connect();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		outStream.write(content);
		outStream.writeTo(connection.getOutputStream());
		outStream.close();
		int code = connection.getResponseCode();
		if (code > HttpURLConnection.HTTP_OK) {
			handleError(connection);
		}
	}

	private static void handleError(HttpURLConnection connection) throws IOException {
		String msg = "Failed to upload media.";
		InputStream errorStream = connection.getErrorStream();
		if (errorStream != null) {
			InputStreamReader inputStreamReader = new InputStreamReader(errorStream, UTF_8);
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);
			try {
				StringBuilder builder = new StringBuilder();
				String outputString;
				while ((outputString = bufferReader.readLine()) != null) {
					if (builder.length() != 0) {
						builder.append("\n");
					}
					builder.append(outputString);
				}
				String response = builder.toString();
				msg += "Response: " + response;
			}
			finally {
				bufferReader.close();
			}
		}
		throw new RuntimeException(msg);
	}

	private static byte[] readFile(File path) throws FileNotFoundException, IOException {
		int length = (int)path.length();
		byte[] content = new byte[length];
		InputStream inStream = new FileInputStream(path);
		try {
			inStream.read(content);
		}
		finally {
			inStream.close();
		}
		return content;
	}

	private static byte[] readURL(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setDoOutput(true);
		int code = connection.getResponseCode();
		if (code > HttpURLConnection.HTTP_OK) {
			handleError(connection);
		}
		InputStream stream = connection.getInputStream();

		if (stream == null) {
			throw new RuntimeException("Failed to get content from url " + url + " - no response stream");
		}
		byte[] content = read(stream);
		return content;
	}

	private static byte[] read(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[1024];
			int nBytes = 0;
			while ((nBytes = input.read(buffer)) > 0) {
				output.write(buffer, 0, nBytes);
			}
			byte[] result = output.toByteArray();
			return result;
		} finally {
			try{
				input.close();
			} catch (IOException e){

			}
		}
	}

	

	
	/**
	 * Sets the execution id capability
	 */
	public static void setExecutionIdCapability(DesiredCapabilities capabilities, String host) throws IOException {
		EclipseConnector connector = new EclipseConnector();
		String eclipseHost = connector.getHost();
		if ((eclipseHost == null) || (eclipseHost.equalsIgnoreCase(host))) {
			String executionId = connector.getExecutionId();
			capabilities.setCapability(EclipseConnector.ECLIPSE_EXECUTION_ID, executionId);
		}
		
		/*public void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}

	public void verifyTextCheckpoint(RemoteWebDriver driver, String textChkpoint) {
		Map<String, Object> params = new HashMap<>();
		params.put("content", textChkpoint);
		params.put("timeout", "40");		
		driver.executeScript("mobile:checkpoint:text", params);
	}

	public void scrollToFindCommand(RemoteWebDriver driver, String content) {
		String findCommand = "mobile:text:find"; // The proprietary function call
		HashMap<Object, Object> params = new HashMap<>();
		params.put("content", content);    // The text we're looking for
		params.put("dpi", "300");           // Optional DPI parameter
		params.put("scrolling", "scroll");  // Add the scroll and search
		params.put("next", "SWIPE_UP");     // Next is mandatory if using scroll and search
		// Can also use customized swipe like:
		// "SWIPE=(50%,80%),(50%,60%);WAIT=1000"
		params.put("maxscroll", "3");       // Not mandatory, default is 5
		params.put("threshold", "100");     // Adding threshold			
		driver.executeScript(findCommand, params);	
	}



	//method to archive the previous run HTML results
	public void archiveHTMLResults(){
		try
		{	
			outputDirectory = PropertyUtils.getProperty("app.test.reports.directory");  
			archiveDir = outputDirectory+"//Archive";

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}


		try {
			Calendar curdate = Calendar.getInstance();
			String strcuurrdate = null;
			DateFormat farmatt = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss z");
			strcuurrdate = farmatt.format(curdate.getTime());
			archiveRunResultsDir = archiveDir +"//"+strcuurrdate;

			File archiveDr = new File(archiveDir);
			File archiveRunResultsDr = new File(archiveRunResultsDir);
			if (!archiveDr.exists()) {                            
				try{
					archiveDr.mkdir();                
				} 
				catch(SecurityException se){
					System.out.println("Exception caught during creating Archive Directory in : "+outputDirectory); 
				} 
			}
			try{
				archiveRunResultsDr.mkdir();
			} 
			catch(SecurityException se){
				System.out.println("Exception caught during creating Archive Run results Directory in : "+archiveDir); 
			}             

			try {
				File nefile = new File(outputDirectory);
				File [] files = nefile.listFiles();		
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile()) {	
						String tfName = files[i].getName();				
						files[i].renameTo(new File(archiveRunResultsDir+"\\"+tfName));
						files[i].delete();
					}        		
				}
			} catch (Exception e) {

				System.out.println("Exception caught during renaming and deleting the back up results"+e.toString());
			}

		} catch (Exception e) {
			System.out.println("Exception is caught during backup of results "+e.toString() );
		}	         
	}

	// method to Archive earlier screenshots
	public void archivePreviousScreenshots(){
		try
		{	
			outputScreenshotsDirectory = PropertyUtils.getProperty("app.test.screenshots.directory");
			todaysScreenshotsDir = outputScreenshotsDirectory+"\\"+curentDateTime;
			archiveScreenshotsDir = todaysScreenshotsDir+"\\ARCHIVE_SCREENSHOTS";

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		File todaysDateresulstDir = new File(todaysScreenshotsDir);
		if (todaysDateresulstDir.exists()) {
			try {

				Calendar curdate = Calendar.getInstance();
				String strcuurrTime = null;
				DateFormat farmatt = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss z");
				strcuurrTime = farmatt.format(curdate.getTime());
				archiveScreenshotsByTimeDir = archiveScreenshotsDir+"\\Backup at "+strcuurrTime;


				File archivescreensDr = new File(archiveScreenshotsDir);
				File archiveScreensByTime = new File(archiveScreenshotsByTimeDir);

				if (!archivescreensDr.exists()) {                            
					try{
						archivescreensDr.mkdir();                
					} 
					catch(SecurityException se){
						System.out.println("Exception caught during creating Archive Scerens Directory. "); 
					} 
				}

				try{
					archiveScreensByTime.mkdir();
					Thread.sleep(1000);
				} 
				catch(SecurityException se){
					System.out.println("Exception caught during creating Archive screenshots bt time "); 
				}             

				try {
					File nefile = new File(todaysScreenshotsDir);
					File [] files = nefile.listFiles();		
					for (int i = 0; i < files.length; i++) {
						if (!files[i].getName().equals("ARCHIVE_SCREENSHOTS")) {
							if (files[i].isDirectory()) {	
								String tfName = files[i].getName();				
								files[i].renameTo(new File(archiveScreenshotsByTimeDir+"\\"+tfName));
								Thread.sleep(1000);
								files[i].delete();
							}	         		}
					}
				} catch (Exception e) {

					System.out.println("Exception caught during renaming and deleting the back up results"+e.toString());
				}


			} catch (Exception e) {
				System.out.println("Exception is caught during backup of results "+e.toString() );
			}
		}
	}

	public void captureScreenshot(String screenshotPrefix,String methodname, RemoteWebDriver driver_rm) throws Exception{
		Thread.sleep(1000);
		WebDriver augmentedDriver = (RemoteWebDriver)new Augmenter().augment(driver_rm);

		File scrFile = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
		Date now = new Date();
		String curDate = new SimpleDateFormat("yyyy-MMM-dd").format(now);
		String curTime = new SimpleDateFormat("HH-mm-ss-SSS").format(now);		
		//Commented by Basha on 01-Apr-16
		//The below method will save the screen shot in d drive with test method name 
		//Log.info("***"+filePath+"\\"+curDate+"\\"+screenshotPrefix+"\\"+methodname+"_"+curTime+".png");
		try {
			FileUtils.copyFile(scrFile, new File(filePath+"\\"+curDate+"\\"+screenshotPrefix+"\\"+methodname+"_"+curTime+".PNG"));
			System.out.println("***Placed screen shot in "+filePath+" ***");
			Log.info("***Placed screen shot in "+filePath+" ***");
		} catch (IOException e) {
			Log.info("***Placed screen shot in catch"+filePath+" ***");
			e.printStackTrace();
		}
	}



	// method to convert images in a folder to pdf file
		public void convertImagesToPdf(String resDirName) {	    	
			String resultsDirectory = PropertyUtils.getProperty("app.test.screenshots.directory");
			String todaysResDirectory = resultsDirectory+"\\"+curentDateTime;
			String testResDir = todaysResDirectory+"\\"+resDirName+"\\";

			try {

				File pdffile = new File(testResDir+"ResultsFile"+".pdf");
				FileOutputStream pdfFileout = new FileOutputStream(pdffile);
				Document doc = new Document();
				PdfWriter.getInstance(doc, pdfFileout);

				doc.addAuthor("Automation Test Result");
				doc.addTitle(resDirName);
				doc.open();

				try {
					File neimg = new File(testResDir);
					File [] files = neimg.listFiles();
					//Log.info("files.length====> "+files.length);
					for (int i = 0; i < files.length; i++) {         			
						if (files[i].getName().contains(".PNG")) {	

							Image image = Image.getInstance(files[i].getPath());
							image.setAlignment(Image.DEFAULT);           
							image.scalePercent(35);
							doc.add(image);
							files[i].delete();
						}	         		
					}
				} catch (Exception e) { 				
					System.out.println("Exception caught during converting images to pdf in commonMethods : "+e.toString());
				}
				doc.close();
				pdfFileout.close();

			} catch (Exception e) {
				System.out.println("Exception caught in the method - convertImagesToPdf in commonMethods  : "+e.toString());
			}
		}*/

	}
	}
