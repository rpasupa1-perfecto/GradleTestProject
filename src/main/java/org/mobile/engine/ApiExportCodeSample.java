package org.mobile.engine;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ApiExportCodeSample {

    // The Perfecto Continuous Quality Lab you work with
    public static final String CQL_NAME = "ps";

    //public static final String REPORTING_SERVER_URL = "https://" + CQL_NAME + ".reporting.perfectomobile.com";
    public static final String REPORTING_SERVER_URL = "https://" + CQL_NAME + ".reporting.perfectomobile.com";

    // See http://developers.perfectomobile.com/display/PD/DigitalZoom+Reporting+Public+API on how to obtain a Security Token
    public static final String PERFECTO_SECUIRTY_TOKEN_KEY = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJPcFJKeVBSeWlLQWRmRGloVkZweE1aTzc5ZktlZllPUEJJbk9CSk9RbkRrIn0.eyJqdGkiOiIzYWRiMjQyYi04NTFiLTRlNjctYWVjNC02ZTA1NWExZmVkMzMiLCJleHAiOjAsIm5iZiI6MCwiaWF0IjoxNTM0MjYyMDcwLCJpc3MiOiJodHRwczovL2F1dGgucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL3BzLXBlcmZlY3RvbW9iaWxlLWNvbSIsImF1ZCI6Im9mZmxpbmUtdG9rZW4tZ2VuZXJhdG9yIiwic3ViIjoiMzkzYjMxYTQtNDJiZS00NmIxLTg5MGUtYmRlNzY3ZWEzYjQ2IiwidHlwIjoiT2ZmbGluZSIsImF6cCI6Im9mZmxpbmUtdG9rZW4tZ2VuZXJhdG9yIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiNGUwNGVhYTgtNjM5NS00OWU2LTlkNDEtNGM0OWFiNmE0ZDZkIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fX0.FnOp1QEsOmEKUrcDr6Cqp6pgNfQZMDFsLow7VmYXUj0cuIh84-sLmCbJYM0yK-inROSDoqSf8tCk-XWRxUn0IkINn4M9FiAFnRAQ5hVlfhtaHYnhihxzQ6gcc8r53sPgL8I5VGVKkLgsev1vhcvZ0R27jPmYN1h7Urt6itO9qXKw_lgjSHrBhD9hXkt6iaD-pDIwguo6ejzdXm0ElbfsT-0lM0CQ7lGrxhdvOElwa5yGZnGMkXRIdIQoODYAm6g-YvZ211ryJ-d3qw9GEHaWhbuWrl-Dmk8-eqLwWuHPLptMwJxRrKjT9tPZuVNBK_-wUTR9Z3vfG-DgFur0YYyiAg";
    
    //  private static final String SECURITY_TOKEN = PERFECTO_SECUIRTY_TOKEN_KEY;
  
    //String executionId = (String) capabilitiesIOS.getCapability("executionId");
    public static String testId;
    
    public static HashMap<String, String> RepMap = new HashMap<String, String>();
    
    
    
    public void downloadA() throws Exception {
        // Retrieve a list of the test executions in your lab (as a json)
    	
    	/* Execution Reports: https://ps.reporting.perfectomobile.com/test/5a9832e84cedfd0007538861 */	
    	//String reportOutput = "https://ps.reporting.perfectomobile.com/library?tags[0]=68c27644-de42-42b7-9ef3-881a17ea2f39";
    	JsonObject executions = retrieveTestExecutions("5b58bbc152faff00093d50bf");
    	
    	
    	String reportURLbase = "https://ps.reporting.perfectomobile.com/test/";
    	
        JsonArray resources = executions.getAsJsonArray("resources");
        System.out.println("Resources Size: " + resources.size());
        
        if (resources.size() == 0) {
            System.out.println("There are no test executions for that period of time");
        } else {
        	
        	for (int i=0; i<resources.size(); i++) {
        		
        		JsonObject testExecution = resources.get(i).getAsJsonObject();
        		retrieveTestCommands(testExecution);
		
        		String testId = testExecution.get("id").getAsString();
        		String testStatus = testExecution.get("status").getAsString();
        		String testName = testExecution.get("name").getAsString();
        		
        		RepMap.put("TestCase"+i, testName);
        		RepMap.put("PerfectoReport"+i, reportURLbase+testId);
        		RepMap.put("Status"+i, testStatus);
        		 
        		 
       
        		 
        		 /* Download Console Logs */
                try {
                	//updateTAGs(testExecution, i);
                	
                	downloadConsoleLogs(testExecution, i);
                	
                	downloadNetworkFiles(testExecution, i);
                	
                	downloadDeviceLogs(testExecution, i);
                	
    	        } catch (Exception e) {
    				throw e;
    			}
        		
        		
        	}
        	
        	
//        	
//        	PerfectoReport: https://ps.reporting.perfectomobile.com/test/5a9832e84cedfd0007538861
//        		Status: FAILED/PASS
//        		DeviceId: 41EEF156EA10EDAB41632651F7AD2A4C4CB502ED
//
//        		Logs:
//        		Device_Logs: https://ps.reporting.perfectomobile.com/test-execution-artifacts/device-logs/ps/rajp%40perfectomobile.com_RemoteWebDriver_18-03-01_17_02_18_15/b275298a9cdd41bc9df2e15ba8f3b05a.zip
//        		Console_Log: https://ps.reporting.perfectomobile.com/test-execution-artifacts/console-log/ps/rajp%40perfectomobile.com_RemoteWebDriver_18-03-01_17_02_18_15/d2cd739b74bf471496ad53110c7ee1a6.log
//
//        		VideoLogs:
//        		VideoStreamURL: https://ps.vod-stream-01.perfectomobile.com/vods3/_definst_/mp4:pm/perfecto-vod-01/ps/ITMS/fe0f1abdeff572563e57abc2c05556853c314a3b15ae22e0713f8757ed4f1a1b.mp4
//        		VideoDownloadURL: https://ps.vod-download-01.perfectomobile.com/ps/ITMS/fe0f1abdeff572563e57abc2c05556853c314a3b15ae22e0713f8757ed4f1a1b.mp4
//
//        		Saved Files:
//        		1 DeviceLogsLoc: C:\Users\Raj\AppData\Local\Temp\5a9832e84cedfd00075388614612703969346410300.zip
//        	
//        	
        	
        	//	https://ps.reporting.perfectomobile.com/test/5a9832e84cedfd0007538861
        	for(int j = 0; j<resources.size(); j++) {
	    		System.out.println("\n\n-------------------------------------------------------------------------------------");
	    		System.out.println("TestCase"+j+": "  + RepMap.get("TestCase"+j));
	    		System.out.println("-------------------------------------------------------------------------------------");
	    		System.out.println("PerfectoReport: " + RepMap.get("PerfectoReport"+j));
	    		System.out.println("Status: " + RepMap.get("Status"+j));
	    		System.out.println("DeviceId: Not Implemented...." +"\n");
	    		
	    		System.out.println("Logs:");
	    		System.out.println("DeviceLogs: " + RepMap.get("DeviceLog"+j));
	    		System.out.println("ConsoleLog: " + RepMap.get("ConsoleLog"+j));
	    		System.out.println("NetworkLog: " + RepMap.get("NetworkLog"+j));
	    		System.out.println("VideoStreamURL: Not Implemented....");
	    		System.out.println("VideoDownloadURL: Not Implemented");
	    		
	    		System.out.println("\nSavedFiles:");
	    		System.out.println("Location: " + RepMap.get("Location"+j));
	    		System.out.println("--------------------------------------------------------------------------------------");
        	}
        }
    }
    
    public static void retrieveTestCommands(JsonObject testExecution) throws URISyntaxException, IOException {
    	
        String testId = testExecution.get("id").getAsString();
        HttpGet getCommands = new HttpGet(new URI(REPORTING_SERVER_URL + "/export/api/v1/test-executions/" + testId + "/commands"));
        
        addDefaultRequestHeaders(getCommands);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse getCommandsResponse = httpClient.execute(getCommands);
        try (InputStreamReader inputStreamReader = new InputStreamReader(getCommandsResponse.getEntity().getContent())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject commands = gson.fromJson(IOUtils.toString(inputStreamReader), JsonObject.class);
            System.out.println("\nList of commands response:\n" + gson.toJson(commands));
        }
    }
  
    
    public static void downloadConsoleLogs(JsonObject testExecution, int count) throws IOException, URISyntaxException {
        // Example for downloading device logs
        JsonArray artifacts = testExecution.getAsJsonArray("artifacts");
        
        for (JsonElement artifactElement : artifacts) {
        	
            JsonObject artifact = artifactElement.getAsJsonObject();
            String artifactType = artifact.get("type").getAsString();
                      
            if (artifactType.equals("CONSOLE_LOG")) {
                String testId = testExecution.get("id").getAsString();
                String path = artifact.get("path").getAsString();
                       
                RepMap.put("ConsoleLog"+count, path);
               // downloadLoc.put(description, file.toFile().getAbsolutePath());
                
                URIBuilder uriBuilder = new URIBuilder(path);
                downloadFile(testId, uriBuilder.build(), ".zip", "Console logs", count);
            }
        }
    }
    

    public static void downloadNetworkFiles(JsonObject testExecution, int count) throws IOException, URISyntaxException {
        // Example for downloading device logs
        JsonArray artifacts = testExecution.getAsJsonArray("artifacts");
        
        for (JsonElement artifactElement : artifacts) {
        	
            JsonObject artifact = artifactElement.getAsJsonObject();
            String artifactType = artifact.get("type").getAsString();
                
                       
            if (artifactType.equals("NETWORK")) {
                String testId = testExecution.get("id").getAsString();
                String path = artifact.get("path").getAsString();
                
                RepMap.put("NetworkLog"+count, path);
                
                URIBuilder uriBuilder = new URIBuilder(path);
                downloadFile(testId, uriBuilder.build(), ".zip", "Network logs", count);
            }
                       
        }
    }
    
    public static void downloadDeviceLogs(JsonObject testExecution, int count) throws IOException, URISyntaxException {
        // Example for downloading device logs
        JsonArray artifacts = testExecution.getAsJsonArray("artifacts");
        
        for (JsonElement artifactElement : artifacts) {
        	
            JsonObject artifact = artifactElement.getAsJsonObject();
            String artifactType = artifact.get("type").getAsString();      
                      
            if (artifactType.equals("DEVICE_LOGS")) {
            	
                testId = testExecution.get("id").getAsString();
                String path = artifact.get("path").getAsString();
                
                RepMap.put("DeviceLog"+count, path);
                
                URIBuilder uriBuilder = new URIBuilder(path);
                downloadFile(testId, uriBuilder.build(), ".zip", "DeviceLogs", count);
            }
            
        }
    }
    
    
    public static void updateTAGs(JsonObject testExecution, int count) throws IOException, URISyntaxException {
        // Example for downloading device logs
        JsonArray tags = testExecution.getAsJsonArray("tags");
        
        
        
        for (JsonElement artifactElement : tags) {
        	
        	
    //        JsonObject tag = artifactElement.getAsJsonObject();
           // String TagType = tag.get("type").getAsString();      
            String output = artifactElement.getAsString();
            
            
            
            System.out.println("Tags: " + output);
//            if (artifactType.equals("DEVICE_LOGS")) {
//            	
//              
//                String path = tag.get("path").getAsString();
//                
//                downloadLoc.put("Device_Log", path);
//                
//                URIBuilder uriBuilder = new URIBuilder(path);
//                downloadFile(testId, uriBuilder.build(), ".zip", "DeviceLogs");
//            }
            
        }
    }
    
    
    private static void downloadFile(String fileName, URI uri, String suffix, String description, int count) throws IOException {
        downloadFileToFS(new HttpGet(uri), fileName, suffix, description, count);
    }


    private static void downloadFileToFS(HttpGet httpGet, String fileName, String suffix, String description, int count) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(httpGet);
        FileOutputStream fileOutputStream = null;
    
        try {
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                Path file = Files.createTempFile(fileName, suffix);
                fileOutputStream = new FileOutputStream(file.toFile());
                IOUtils.copy(response.getEntity().getContent(), fileOutputStream);
                System.out.println("\nSaved " + description + " to: " + file.toFile().getAbsolutePath());
                
                /* Adding Hash Map */
                RepMap.put("Location"+count, file.toFile().getAbsolutePath());
                
            } else {
                String errorMsg = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
                System.err.println("Error downloading file. Status: " + response.getStatusLine() + ".\nInfo: " + errorMsg);
            }
        } finally {
            EntityUtils.consumeQuietly(response.getEntity());
            IOUtils.closeQuietly(fileOutputStream);
           
        }
    }
    
    

    private static JsonObject retrieveTestExecutions(String tags) throws URISyntaxException, IOException {
    	//REPORTING_SERVER_URL=https://ps.reporting.perfectomobile.com/export/api/v1/test-executions
        URIBuilder uriBuilder = new URIBuilder(REPORTING_SERVER_URL + "/export/api/v1/test-executions");

        // Optional: Filter by range. In this example: retrieve test executions of the past month (result may contain tests of multiple driver executions)
        //    uriBuilder.addParameter("startExecutionTime[0]", Long.toString(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(10)));
        //     uriBuilder.addParameter("endExecutionTime[0]", Long.toString(System.currentTimeMillis()));

        // Optional: Filter by a specific driver execution ID that you can obtain at script execution
        //String executionID = "68c27644-de42-42b7-9ef3-881a17ea2f39";
       // String tags = "68c27644-de42-42b7-9ef3-881a17ea2f39";
   
        uriBuilder.addParameter("tags[0]", tags);
        
     //    uriBuilder.addParameter("externalId[0]", "rajp@perfectomobile.com_RemoteWebDriver_18-03-01_17_02_18_15");

        HttpGet getExecutions = new HttpGet(uriBuilder.build());
        addDefaultRequestHeaders(getExecutions);
      
        
        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpResponse getExecutionsResponse = httpClient.execute(getExecutions);
        JsonObject executions;
        try (InputStreamReader inputStreamReader = new InputStreamReader(getExecutionsResponse.getEntity().getContent())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String response = IOUtils.toString(inputStreamReader);
            //System.out.println(response);
            try {
                executions = gson.fromJson(response, JsonObject.class);
            } catch (JsonSyntaxException e) {
                throw new RuntimeException("Unable to parse response: " + response);
            }
            System.out.println("\nList of test executions response:\n" + gson.toJson(executions));
        }
        return executions;
    }


    

    public static void downloadVideo(JsonObject testExecution, int count) throws IOException, URISyntaxException {
        JsonArray videos = testExecution.getAsJsonArray("videos");
        if (videos.size() > 0) {
            JsonObject video = videos.get(0).getAsJsonObject();
            String downloadVideoUrl = video.get("downloadUrl").getAsString();
            String format = "." + video.get("format").getAsString();
            String testId = testExecution.get("id").getAsString();
            downloadFile(testId, URI.create(downloadVideoUrl), format, "video", count);
        } else {
            System.out.println("\nNo videos found for test execution");
        }
    }

    public static void downloadAttachments(JsonObject testExecution, int count) throws IOException, URISyntaxException {
        // Example for downloading device logs
        JsonArray artifacts = testExecution.getAsJsonArray("artifacts");
        
        for (JsonElement artifactElement : artifacts) {
        	
            JsonObject artifact = artifactElement.getAsJsonObject();
            String artifactType = artifact.get("type").getAsString();
                              
            if (artifactType.equals("DEVICE_LOGS")) {
                String testId = testExecution.get("id").getAsString();
                String path = artifact.get("path").getAsString();
                URIBuilder uriBuilder = new URIBuilder(path);
                downloadFile(testId, uriBuilder.build(), ".zip", "device logs", count);
            }
        
        }
    }
    
    
    // Utils

 

    private static void addDefaultRequestHeaders(HttpRequestBase request) {
        request.addHeader("PERFECTO_AUTHORIZATION", PERFECTO_SECUIRTY_TOKEN_KEY);
       // request.addHeader("TENANTID", "dfw-directv.reporting.perfectomobile.com");
    }
}
