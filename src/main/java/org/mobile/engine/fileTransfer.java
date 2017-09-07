package org.mobile.engine;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.testng.ITestContext;
import org.testng.annotations.Test;

public class fileTransfer {
 
    public static void main(String[] args) throws IOException {
      
    	/* Network Directory */
    	String fileLocationSourceNetwork = "/mynetwork/mydata/";
        /* Source Directory */
        String fileLocationSourceDrive = "C:/Users/Raj/Downloads/ipaFIle";    
        /* Destination Directory */
        String fileLocationDestination = "C:/Users/Raj/git/GradleSampleProject/temp/";
        
        /**Copy the files from the network or local to desired location */
        copyFile (fileLocationSourceDrive, fileLocationDestination, "ipa");
        
    }
 

    public static void copyFile(String fileLocationSourceDrive, String fileLocationDestination, String fileExt) {
    	
    	  /* Get the latest ipa/apk file in Folder C:/Users/Raj/Downloads/ipaFIle   */
        File iOSipa = getLastModifiedFile(fileLocationSourceDrive, fileExt);
        System.out.println("Latest File Name: " + iOSipa.getName() + "    FilePath: " + iOSipa.toString());
    	
    	try {
				String fileName = iOSipa.getName();
             
				String sourceFileName = iOSipa.getAbsolutePath();  System.out.println("\nSourceFileName: " + sourceFileName);
                String destinationFileName = fileLocationDestination + fileName;  System.out.println("DestinationFileName: " + destinationFileName);
                
                System.out.println("Reading......." + sourceFileName);
	            File sourceFile = new File(sourceFileName);
	            File destinationFile = new File(destinationFileName);
	            InputStream in = new FileInputStream(sourceFile);
	            OutputStream out = new FileOutputStream(destinationFile);
	 
	            byte[] buffer = new byte[1024];
	            int length;
	            while ((length = in.read(buffer)) > 0) {
	                out.write(buffer, 0, length);
	            }
	            in.close();
	            out.close();
	            System.out.println("\nCopied: " + sourceFileName);
    		 
    		 System.out.println("\nCompleted Transfer...");
        } catch (Exception ex) {
            ex.printStackTrace();
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
    

	

}