//package org.perfecto.mobile.reports;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.List;
//
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
//import javax.imageio.ImageIO;
//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.Multipart;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Marshaller;
//import javax.xml.bind.util.JAXBSource;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.stream.StreamResult;
//import javax.xml.transform.stream.StreamSource;
//
//import org.apache.commons.io.FileUtils;
//import org.openqa.selenium.OutputType;
//import org.testng.ITestContext;
//import org.testng.annotations.AfterSuite;
//import org.testng.annotations.BeforeSuite;
//
//import cts.qea.automation.Config;
//import cts.qea.automation.TestCase;
//import cts.qea.automation.WrappedWebDriver;
//import cts.qea.automation.reports.ReportTemplate.Caller;
//import cts.qea.automation.reports.docx.DOCXReport;
//import cts.qea.automation.reports.html.HTMLReport;
//import cts.qea.automation.utils.DateHelper;
//import cts.qea.automation.utils.PdfZipper;
//
//public class Report {
//
//	static HashMap <String, TestCaseElement>testcases = new HashMap <String, TestCaseElement>();
//	static HashMap <String, List<StepElement>>testcases_steps = new HashMap <String, List<StepElement>>();
//	static SummaryElement summary = new SummaryElement();
//	static volatile String resultsPath = null;
//	static volatile Caller caller = null;
//	static volatile boolean initDone=false;
//	static String device = "";	
//
//	private static void initReport(){
//		String runFolder = System.getProperty("report.runfolder", "Run_{DATE}_{TIME}");
//		runFolder = runFolder.replace("{DATE}", DateHelper.getCurrentDatenTime("yyyyMMdd"))
//							.replace("{TIME}", DateHelper.getCurrentDatenTime("HHmmss"));
//		
//		resultsPath = "report/" + runFolder;
//
//		if(!new File(resultsPath).isDirectory())
//			new File(resultsPath).mkdirs();
//	}
//
//	@BeforeSuite (alwaysRun=true)
//	public static synchronized void startSuite(ITestContext ngContext) {
//		if(initDone == true) return;
//
//		initDone = true;
//		if(caller == null) caller = Caller.SUITE;
//
//		initReport();
//
//		summary.setTitle(Config.get("report","title"));
//		summary.setStartTime(DateHelper.getCurrentDatenTime("MM-dd-yyyy")+" "+DateHelper.getCurrentDatenTime("HH:mm:ss"));
//	}
//
//	@AfterSuite (alwaysRun=true)
//	public static void endSuite() {
//		summary.setEndTime(DateHelper.getCurrentDatenTime("MM-dd-yyyy")+" "+DateHelper.getCurrentDatenTime("HH:mm:ss"));
//		summary.setTimeDifference();
//		String reportTypes = Config.get("report", "type");
//
//
//		if ((";" + reportTypes + ";").toUpperCase().contains(";HTML;")) {
//			HTMLReport.endOfSuite(resultsPath, summary, new ArrayList<TestCaseElement>(testcases.values()));
//		}
//
//		//cleanup
//		summary = null;
//		testcases = null;
//		testcases_steps = null;
//	}
//
//	public static synchronized void startTest(TestCase tc) throws FileNotFoundException {
//		if(caller == null) {
//			caller = Caller.TEST;
//			startSuite(null);
//		}
//
//		TestCaseElement tcInfo = addTcInfo(tc);
//
//		Thread.currentThread().setName(tcInfo.tcid);
//		Random uniqueValue = new Random( System.currentTimeMillis() );	    
//
//		tcInfo.resultFolderPath = tcInfo.uname + "_" + tcInfo.starttime.replace(":", "") + "_" + uniqueValue.nextInt(2000);;
//		tcInfo.resultFileName = tcInfo.resultFolderPath;
//
//		if(!new File(resultsPath + "/" + tcInfo.resultFolderPath).isDirectory())
//			new File(resultsPath + "/" + tcInfo.resultFolderPath).mkdirs();
//	}
//
//	/**
//	 * <b>Description</b>Ends Test case Reporting
//	 *  @param  tc  Test case Id
//	 */
//	public static synchronized void endTest(TestCase tc) {
//		TestCaseElement tcInfo = getTcInfo();
//		List<StepElement> steps = getTcSteps();
//		
//		tcInfo.addMetaInfo("Execution Time", DateHelper.getDateTimeDifference(tcInfo.endtime, tcInfo.starttime, "HH:mm:ss"));	
//
//		if(tcInfo.fail>0){
//			tcInfo.status = Status.Fail;
//			summary.incFailCnt();
//
//		}else if(tcInfo.warning>0){
//			tcInfo.status = Status.WARN;
//			summary.incWarnCnt();
//
//		}else if(tcInfo.pass>0){
//			tcInfo.status = Status.Pass;
//			summary.incPassCnt();
//		}
//
//		String reportTypes = Config.get("report", "type");
//
//		if ((";" + reportTypes + ";").toUpperCase().contains(";HTML;")) {
//			System.out.println("calling with root path as " + resultsPath);
//			HTMLReport.endOfTest(resultsPath, tcInfo, steps);
//		}
//
//	
//		if ((";" + reportTypes + ";").toUpperCase().contains(";DOCX;")) {
//			DOCXReport.attachScreenShotToDocx(resultsPath, tcInfo, device, Config.get("report","title") ); 
//		}
//
//		//cleanup
//		removeTCInfo();
//		removeStepInfo();
//
//		if(caller == Caller.TEST) endSuite();
//	}
//
//	public static synchronized void log(String message, Status status) {
//		boolean takeScreenShot = false;
//		String take_Screenshot = null;
//		String screenShot = null, reportStatus = null;
//		TestCaseElement tcInfo = getTcInfo();
//		take_Screenshot = Config.get("report", "take_screenshot", "").toLowerCase();
//		tcInfo.rStepCounter++;
//
//		switch(status){
//		case BUSINESSSTEP: 
//			takeScreenShot = false;
//			tcInfo.bStepCounter++;
//			tcInfo.rStepCounter = 0;
//			reportStatus = "BUSINESSSTEP";
//			break;
//		case Pass: tcInfo.pass++; takeScreenShot = false; reportStatus = "Pass"; break;
//		case PASS: tcInfo.pass++; takeScreenShot = true; reportStatus = "Pass"; break;
//		case FAIL: tcInfo.fail++; takeScreenShot = true; reportStatus = "Fail"; break;
//		case Fail: tcInfo.fail++; takeScreenShot = true; reportStatus = "Fail"; break;
//
//		case DONE: 
//			switch(take_Screenshot){
//			case "all":
//				tcInfo.pass++; takeScreenShot = true; reportStatus = "Done"; 
//				break;
//			default:
//				takeScreenShot = false; reportStatus = "Done"; 
//				break;
//			}					
//			break;
//
//
//		case Navigation: 
//			switch(take_Screenshot){
//			case "prenavigation":
//				tcInfo.pass++; takeScreenShot = true; reportStatus = "Done"; 
//				break;
//			case "all":
//				tcInfo.pass++; takeScreenShot = true; reportStatus = "Pass"; 
//				break;
//			default:
//				takeScreenShot = false; reportStatus = "Done"; 
//				break;
//			}					
//			break;
//
//		case WARN: tcInfo.warning++; takeScreenShot = true; reportStatus = "Warn"; break;
//		case DEBUG: break;
//		}
//
//		if(takeScreenShot){
//			screenShot = "ss_" + tcInfo.bStepCounter + "_" + tcInfo.rStepCounter + ".png";
//			String screenshotLocation = resultsPath+"/" + tcInfo.resultFolderPath + "/" + screenShot;
//			Report.takeScreenShot(screenshotLocation, tcInfo.getWebDriver());			
//		}
//
//		StepElement stp = new StepElement();
//
//		if(tcInfo.rStepCounter==0) {
//			stp.stepid = String.valueOf(tcInfo.bStepCounter);
//		} else {
//			stp.stepid = tcInfo.bStepCounter + "." + tcInfo.rStepCounter;
//		}
//		stp.tcid = tcInfo.tcid;
//		stp.description = message;
//		stp.status = reportStatus;
//		stp.time = DateHelper.getCurrentDatenTime("HH:mm:ss");
//		stp.screenshot = screenShot;
//
//		getTcSteps().add(stp);
//	}
//
//	/**
//	 * <b>Description</b>Custom defined business step for reports
//	 *  @param  message     Message of the custom report
//	 *  @param  filePath    Directory of file.
//	 */
//	public static synchronized void addAttachement (String message, String filePath) {
//		TestCaseElement tcInfo = getTcInfo();
//		tcInfo.rStepCounter++;
//
//		StepElement stp = new StepElement();
//		stp.stepid = tcInfo.bStepCounter + "." + tcInfo.rStepCounter;
//
//		stp.tcid = "";
//		stp.description = message;
//		stp.status = "Done";
//		stp.time = DateHelper.getCurrentDatenTime("HH:mm:ss");
//		stp.screenshot = filePath;
//
//		getTcSteps().add(stp);
//	}
//
//	/**
//	 * <b>Description</b>Adds Footer to the reporting 
//	 *  @param  name     Name of the Step
//	 *  @param  value    value of the step
//	 */
//	public static void addMetaInfo(String key, String value){
//		//footerAttributes.put(name, value);
//		getTcInfo().addMetaInfo(key, value);
//	}
//
//	private static synchronized TestCaseElement addTcInfo(TestCase tc) {
//		/*String key = Thread.currentThread().getName();
//
//		if(testcases.containsKey(key)) {
//			return testcases.get(key);
//		}*/
//
//		TestCaseElement tcInfo = tc.info;	
//		tcInfo.runid=summary.runid;
//		testcases.put(tcInfo.tcid, tcInfo);
//
//		return tcInfo;
//	}
//
//	private static synchronized TestCaseElement getTcInfo() {
//		String key = Thread.currentThread().getName();
//
//		if(testcases.containsKey(key)) {
//			return testcases.get(key);
//		} else {
//			return null;
//		}
//	}
//
//	private static List<StepElement> getTcSteps() {
//		String key = Thread.currentThread().getName();
//
//		if(!testcases_steps.containsKey(key)) {
//			testcases_steps.put(key, new ArrayList<StepElement>());
//		}
//
//		return testcases_steps.get(key);
//	}
//
//	private static synchronized void removeStepInfo() {
//		String key = Thread.currentThread().getName();
//
//		if(!testcases_steps.containsKey(key)) {
//			testcases_steps.get(key).clear();
//			testcases_steps.remove(key);
//		}
//	}
//
//	private static synchronized void removeTCInfo() {
//		String key = Thread.currentThread().getName();
//
//		if(!testcases.containsKey(key)) {
//			testcases.remove(key);
//		}
//	}
//
//
//	/**
//	 * <b>Description</b>Captures web driver screen shot 
//	 *  @param  path    Directory of file.
//	 */
//	public static synchronized void takeScreenShot(String path, WrappedWebDriver driver){
//		try {
//			File scrFile1=null;
//			scrFile1 = driver.getScreenshotAs(OutputType.FILE);
//			FileUtils.copyFile(scrFile1, new File(path));
//		} catch (Exception e) {
//			System.out.println("Error when capturing screenshot " + e.getMessage());
//		}finally{
//			File f=new File(path);
//			if(!(f.exists())){
//				takeScreenShot1(path);
//			}
//		}
//	}
//
//
//	/**
//	 * <b>Description</b>Captures Robot screen shot 
//	 *  @param  path    Directory of file.
//	 */
//	public static void takeScreenShot1(String path){
//		try{  
//			//Get the screen size  
//			Toolkit toolkit = Toolkit.getDefaultToolkit();  
//			Dimension screenSize = toolkit.getScreenSize();  
//			Rectangle rect = new Rectangle(0, 0,screenSize.width,screenSize.height);  
//			Robot robot = new Robot();  
//			BufferedImage image = robot.createScreenCapture(rect);  
//			File file;  
//
//			//Save the screenshot as a png  
//			file = new File(path);  
//			ImageIO.write(image, "png", file);  
//		}catch (Exception e) {  
//		}  
//	}
//
//	@SuppressWarnings("rawtypes")
//	public static void writeToHTML(Class cls, Object obj, String htmlFile, String xslFile) {
//		try {
//			TransformerFactory tf = TransformerFactory.newInstance();
//			StreamSource xslt = new StreamSource(xslFile);	//xslFile = "report/tc.xsl"
//			Transformer transformer = tf.newTransformer(xslt);
//
//			JAXBContext ctx = JAXBContext.newInstance(cls);
//			JAXBSource source = new JAXBSource(ctx, obj);
//
//			PrintStream p = new PrintStream(new FileOutputStream(htmlFile));
//			StreamResult result = new StreamResult(p);
//
//			transformer.transform(source, result);	
//			p.close();
//
//			/*** Code for opening report in browser***/
//			/*
//			try{
//				if(htmlFile.contains("suite")){
//					File suiteReport = new File(htmlFile);
//					Desktop.getDesktop().browse(suiteReport.toURI());
//				}
//			}catch(Exception e){}
//			*/
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@SuppressWarnings({ "rawtypes" })
//	public static void writeToXML(Class cls, Object obj, String htmlFile, String xslFile) {
//		try {
//			JAXBContext ctx = JAXBContext.newInstance(cls);
//			Marshaller m = ctx.createMarshaller();
//			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//			m.setProperty(Marshaller.JAXB_FRAGMENT, true);
//			/*if(xslFile!=null && !xslFile.isEmpty()){
//	        	m.setProperty("com.sun.xml.bind.xmlHeaders", 
//	        	    "<?xml-stylesheet type='text/xsl' href='"+xslFile+"' ?>");
//	        }*/
//
//			StringWriter sw = new StringWriter();
//			sw.write("<?xml version='1.0'?>");
//			sw.write("\n");
//			sw.write("<?xml-stylesheet type='text/xsl' href='"+xslFile+"'?>");
//			sw.write("\n");
//
//			m.marshal(obj, sw);
//			sw.close();
//
//			//System.out.println(sw.toString());
//			PrintStream p = new PrintStream(new FileOutputStream(htmlFile));
//			p.print(sw.toString());	
//			p.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}	
//
//	/**
//	 * <b>Description</b>Returns report location
//	 * @return results folder path
//	 */	
//	public static  String getResultsFolderPath(){
//		return new File(resultsPath).getAbsolutePath();
//	}
//
//	/**
//	 * <b>Description</b>Returns parent report location
//	 * @return results folder path
//	 */	
//	public static  String getResultsParentFolderPath(){
//		return new File(resultsPath).getParentFile().getAbsolutePath();
//	}
//
//
//	/**
//	 * <b>Description</b> zips results
//	 * @param String zipName
//	 */
//	public static  String zipResults(String zipName){	
//		String zipLocation = getZipLocation(zipName);
//		PdfZipper folderZip = new PdfZipper();
//		try {
//			folderZip.zipFolder(getResultsFolderPath(), zipLocation);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return zipLocation;
//	}
//
//	/**
//	 * <b>Description</b> get the zip folder location
//	 * @param String zipName
//	 */
//	public static String getZipLocation(String zipName){			
//		String zipLocation = getResultsParentFolderPath() + "\\" + zipName;		
//		return zipLocation;
//	}
//
//	/**
//	 * <b>Description</b> delete Zip file
//	 * @param String zipName
//	 */
//	public static void deleteZipFile(String zipName){			
//		File file = new File(getZipLocation(zipName));		
//		if(file.exists()){
//			file.delete();
//		}		
//	}
//
//
//	/**
//	 * <b>Description</b> gets suite details 
//	 */
//	public static String getSuiteDetails(){
//		StringBuilder contentBuilder = new StringBuilder();
//		try {
//			BufferedReader in = new BufferedReader(new FileReader(getResultsFolderPath() + "\\suite.html"));
//			String str;
//			while ((str = in.readLine()) != null) {
//				contentBuilder.append(str);
//			}
//			in.close();
//		} catch (IOException e) {
//		}
//		String content = contentBuilder.toString();
//		return content;
//	}
//
//	/**
//	 * <b>Description</b> Prepares mail session
//	 */
//	public static Session mailSession(){
//		if(Config.get("mail", "mail.trigger").toLowerCase().contentEquals("true")){		
//			final String fromEmail = Config.get("mail", "mail.fromEmail");
//			final String emailPassword = Config.get("mail", "mail.password");
//			String smtpHost = Config.get("mail", "mail.smtp.host");		
//			String smtpPort = Config.get("mail", "mail.smtp.port");
//			String smtpAuth = Config.get("mail", "mail.smtp.auth");
//			String smtpStartTlsEnable = Config.get("mail", "mail.smtp.starttls.enable");
//			String ipv4Stack = Config.get("mail", "mail.java.net.preferIPv4Stack");
//
//			System.setProperty("java.net.preferIPv4Stack" , ipv4Stack);
//
//			Properties props = new Properties();
//			props.put("mail.smtp.host", smtpHost);
//			props.put("mail.smtp.port", smtpPort); 
//			props.put("mail.smtp.auth", smtpAuth); 
//			props.put("mail.smtp.starttls.enable", smtpStartTlsEnable);
//
//			Authenticator auth = new Authenticator() {
//				protected PasswordAuthentication getPasswordAuthentication() {
//					return new PasswordAuthentication(fromEmail, emailPassword);
//				}
//			};	
//
//			Session session = Session.getDefaultInstance(props, auth);
//			return session;
//		}else{
//			return null;
//		}
//	}
//
//	/**
//	 * <b>Description</b> Sends mail 
//	 * @param Session session
//	 */
//	public static void sendMail(Session session){
//		final String fromEmail = Config.get("mail", "mail.fromEmail");			
//		final String toEmail = Config.get("mail", "mail.toEmail");
//		String mailSubject = Config.get("mail", "mail.subject");
//		String attachReport = Config.get("mail", "mail.attachReport");
//		String zipName = Config.get("mail", "mail.zipName");
//
//		try {			
//			Message msg = new MimeMessage(session);
//			msg.setFrom(new InternetAddress(fromEmail, fromEmail));
//			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail, toEmail));
//			DateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");			
//			msg.setSubject(mailSubject + date.format(new Date()));			
//			Multipart multipart = new MimeMultipart("alternative");
//			MimeBodyPart htmlPart = new MimeBodyPart();				
//			htmlPart.setContent(getSuiteDetails(), "text/html");
//			multipart.addBodyPart(htmlPart);
//			msg.setContent(multipart);				
//
//			if(attachReport.equalsIgnoreCase("true")){
//				MimeBodyPart attachmentBodyPart = new MimeBodyPart();
//				DataSource source = new FileDataSource(zipResults(zipName));                
//				attachmentBodyPart.setDataHandler(new DataHandler(source));                  
//				attachmentBodyPart.setFileName(zipName);
//				multipart.addBodyPart(attachmentBodyPart);
//				msg.setContent(multipart);
//			}
//			Transport.send(msg);	
//
//			deleteZipFile(zipName);
//
//		} catch (Exception e) {
//			e.printStackTrace();		
//		}
//	}
//}