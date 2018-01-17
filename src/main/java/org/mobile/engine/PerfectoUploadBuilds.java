package org.mobile.engine;

import static org.junit.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.ITestContext;

import com.google.common.base.Function;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfectomobile.selenium.util.EclipseConnector;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

/**
 * @author rajp
 *
 */
public class PerfectoUploadBuilds  {
	
	public static HashMap<String,String> propFile;
	private static final String HTTPS = "https://";
	private static final String MEDIA_REPOSITORY = "/services/repositories/media/";
	private static final String UPLOAD_OPERATION = "operation=upload&overwrite=true";
	private static final String UTF_8 = "UTF-8";
	
	/**
	 * Uploads a file to the media repository.
	 * Example:
	 * uploadMedia("demo.perfectomobile.com", "rajp@perfectomobile.com", "123456", "C:\\test\\ApiDemos.apk", "PRIVATE:apps/ApiDemos.apk");
	 */
	public static void uploadMedia(ITestContext context, String fileLocationSourceDrive, String fileExt) {
		String host = context.getCurrentXmlTest().getParameter("URL");
		String user = context.getCurrentXmlTest().getParameter("user");
		String password = context.getCurrentXmlTest().getParameter("pass");
		String mediaRepo = context.getCurrentXmlTest().getParameter("perfectoDirectory");
			
		/* Get the latest ipa/apk file in Folder C:/Users/Raj/Downloads/ipaFIle  */
        File iOSipa2 = getLastModifiedFile(fileLocationSourceDrive, fileExt);
        
//        File iOSipa2 = new File(fileLocationSourceDrive);
        
        System.out.println("File Name: " + iOSipa2.getName() + "    FilePath: " + iOSipa2.toString());    
        /* PRIVATE:Raj/file.ipa*/
        String repositoryKey = mediaRepo + iOSipa2.getName();

		try {
			File file = new File(iOSipa2.toString());
			byte[] content;
			content = readFile(file);
			uploadMedia(host, user, password, content, repositoryKey);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Uploads a file to the media repository.
	 * Example:
	 * URL url = new URL("http://file.appsapk.com/wp-content/uploads/downloads/Sudoku%20Free.apk");
	 * uploadMedia("demo.perfectomobile.com", "john@perfectomobile.com", "123456", url, "PRIVATE:apps/ApiDemos.apk");
	 */
	public static void uploadMedia(String host, String user, String password, URL mediaURL, String repositoryKey) throws IOException {
		byte[] content = readURL(mediaURL);
		uploadMedia(host, user, password, content, repositoryKey);
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
	/**
	 * Uploads content to the media repository.
	 * Example:
	 * uploadMedia("demo.perfectomobile.com", "john@perfectomobile.com", "123456", content, "PRIVATE:apps/ApiDemos.apk");
	 */
	public static void uploadMedia(String host, String user, String password, byte[] content, String repositoryKey) throws UnsupportedEncodingException, MalformedURLException, IOException {
		
		if (content != null) {
			String encodedUser = URLEncoder.encode(user, "UTF-8");
			String encodedPassword = URLEncoder.encode(password, "UTF-8");
			String urlStr = HTTPS + host + MEDIA_REPOSITORY + repositoryKey + "?" + UPLOAD_OPERATION + "&user=" + encodedUser + "&password=" + encodedPassword;
			URL url = new URL(urlStr);
			System.out.println(urlStr);
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
	
	
	
	
	
	
	
	
	
	
		
	


	
	 
    public static File getLastModifiedFile(String filePath, String ext) {
    	 File theNewestFile = null;
         File dir = new File(filePath);
    	
         try {
	         if (dir.isDirectory()) {
			    	FileFilter fileFilter = new WildcardFileFilter("*." + ext);
			        File[] files = dir.listFiles(fileFilter);
			        
			        if (files.length > 0) {
				        Arrays.sort(files, new Comparator<File>() {
				        	
				            public int compare(File o1, File o2) {
				                return new Long(o2.lastModified()).compareTo(o1.lastModified()); //latest 1st
				            }});
				       
				        theNewestFile = files[0];
			        } else if(files.length ==0) {
			        	//return new Date(dir.lastModified());
			        	return null;
			        }
	         } else {
	        	 System.out.println("SourceInputDirectory is not a Directory....Please Check Source Directory if it exits");
	         }
         } catch (Exception e) {
        	 e.printStackTrace();
		}
         
        return theNewestFile;
    }
    

	
	
	public static HashMap<String, String> getPropFile() {
		return propFile;
	}
	public static void setPropFile(HashMap<String, String> propFile) {
		PerfectoUploadBuilds.propFile = propFile;
	}
	
	
}
