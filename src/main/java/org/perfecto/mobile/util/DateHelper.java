//package org.perfecto.mobile.util;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//import cts.qea.automation.DataProvider;
//
//public class DateHelper {
//	/**
//	 * <b>Description</b> Gets Current date
//	 * @param          format format of the date
//	 * @return         date Current date with specified format
//	 */
//	public static String getCurrentDate(String format){		
//		DateFormat dateFormat = new SimpleDateFormat(format);
//		Date date = new Date();
//		return dateFormat.format(date);
//	}
//
//	/**
//	 * <b>Description</b> Gets Current date and time
//	 * @param          format format of the date
//	 * @return         date Current date with specified format
//	 */
//	public static String getCurrentDatenTime(String format){
//		Calendar cal = Calendar.getInstance();
//		SimpleDateFormat sdf = new SimpleDateFormat(format);
//		return sdf.format(cal.getTime());
//	}
//	/**
//	 * <b>Description</b> Gets Formatted time
//	 * @param          time  long time
//	 * @return         time  long time
//	 */
//
//	public static String getFormattedTime(long time){
//		long timeMillis = time;
//		long time1 = timeMillis / 1000;
//		String seconds = Integer.toString((int)(time1 % 60));
//		String minutes = Integer.toString((int)((time1 % 3600) / 60));
//		String hours = Integer.toString((int)(time1 / 3600));
//		for (int i = 0; i < 2; i++) {
//			if (seconds.length() < 2) {
//				seconds = "0" + seconds;
//			}
//			if (minutes.length() < 2) {
//				minutes = "0" + minutes;
//			}
//			if (hours.length() < 2) {
//				hours = "0" + hours;
//			}
//		}
//		return hours+": "+minutes+": "+seconds;
//	}
//
//	/**
//	 * <b>Description</b> Date Converter for excel values to E MMM dd HH:mm:ss Z yyyy format
//	 * @param          excelDate  Date value to convert
//	 * @return         formatedDate  formatted date
//	 */
//	public static String dateConverter(String excelDate){	
//		String formatedDate = null;
//		try{
//			DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
//			Date date = (Date)formatter.parse(excelDate);
//
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(date);
//			formatedDate = (cal.get(Calendar.MONTH) + 1) + "/"+cal.get(Calendar.DATE) + "/" +         cal.get(Calendar.YEAR);
//		}catch(Exception e){
//			e.printStackTrace();	
//		}
//		return formatedDate;
//	}
//
//	/**
//	 * <b>Description</b> Date Converter for excel values with the specified Date Format
//	 * @param          excelDate  Date value to convert
//	 * @param          dateFormat format to convert
//	 * @return         formatedDate  formatted date
//	 */
//	@SuppressWarnings("deprecation")
//	public static String dateConverter(String excelDate,String dateFormat){
//		Date myDate = new Date(excelDate);
//		String date ="";
//		try{
//			DateFormat formatter = new SimpleDateFormat(dateFormat);
//			date = formatter.format(myDate);
//			date = date.replace("-", "/");		
//		}catch(Exception e){
//			return "";
//		}
//
//		return date;
//	}	
//
//	/**
//	 * <b>Description</b> calculates Date with the specified type of Date (APPDATE, PCDATE)
//	 * @param          excelValue  test data
//	 * @param          typeOfDate  type of date (APPDATE, PCDATE)
//	 */
//	public static String calculateDate(String excelValue,String typeOfDate){
//		try{
//			String dateValue = null;
//			String year = "";
//			String month = "";
//			String day = "";  
//			String [] values = {"",""};
//			if(excelValue.contains("+")) {
//				values = excelValue.split("\\+");
//			}
//			if(excelValue.contains("-")){
//				values = excelValue.split("-");
//			}
//			if(values[1].length() == 9){
//				year = values[1].substring(0,2);
//				month = values[1].substring(3,5);
//				day = values[1].substring(6,8);
//			} else if(values[1].length() == 6){
//				String first3 = values[1].substring(0,3);
//				if(first3.contains("Y")){
//					year = values[1].substring(0,2);
//					month = values[1].substring(3,5);                      
//				}else if(first3.contains("M")){
//					month = values[1].substring(0,2);                     
//					day = values[1].substring(3,5);                       
//				}                 
//			} else if(values[1].length() == 3){
//				if(values[1].contains("Y")){
//					year = values[1].substring(0,2);
//				}else if(values[1].contains("M")){         
//					month = values[1].substring(0,2);               
//				}else if (values[1].contains("D")){
//					day =values[1].substring(0,2);
//				}           
//			} else if(values[1].length() == 4){
//				if(values[1].contains("Y")){
//					year = values[1].substring(0,3);
//				}else if(values[1].contains("M")){         
//					month = values[1].substring(0,3);               
//				}else if (values[1].contains("D")){
//					day =values[1].substring(0,3);
//				}           
//			} else if(values[1].length() == 5){
//				if(values[1].contains("Y")){
//					year = values[1].substring(0,4);
//				}else if(values[1].contains("M")){         
//					month = values[1].substring(0,4);               
//				}else if (values[1].contains("D")){
//					day =values[1].substring(0,4);
//				}           
//			} else if(values[1].length() == 2){
//				if(values[1].contains("Y")){
//					year = values[1].substring(0,1);
//				}else if(values[1].contains("M")){         
//					month = values[1].substring(0,1);               
//				}else if (values[1].contains("D")){
//					day =values[1].substring(0,1);
//				}           
//			}
//
//			if(day==""){
//				day="0";
//			}
//			if(month==""){
//				month = "0";
//			}
//			if(year==""){
//				year="0";
//			}
//			int finalDay = Integer.parseInt(day);
//			int finalMonth = Integer.parseInt(month);
//			int finalYear = Integer.parseInt(year);
//			if(excelValue.contains("-")){
//				finalDay = - Integer.parseInt(day);
//				finalMonth = - Integer.parseInt(month);
//				finalYear = - Integer.parseInt(year);
//			}
//			if(typeOfDate.equalsIgnoreCase("PCDate")){                
//				dateValue = addDaysToSysDate(finalDay,finalMonth,finalYear);
//			}else if (typeOfDate.equalsIgnoreCase("FieldName")){
//				dateValue = addDaysToFieldValue("",Integer.parseInt(day),Integer.parseInt(month),Integer.parseInt(year));
//			}
//			return dateValue;
//		}catch(Exception e){
//			//			e.printStackTrace();
//			return "";
//		}
//	}
//
//	/**
//	 * <b>Description</b> Adds no of dates to System Date
//	 * @param          noOfDay  No. of days
//	 * @param          noOfMonths  No. of Months
//	 * @param          noOfYear  No. of Years
//	 */
//	public static String addDaysToSysDate(int noOfDay, int noOfMonths, int noOfYear) {
//		try{
//			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//			Calendar calendar = Calendar.getInstance();
//			calendar.add(Calendar.DATE, noOfDay);		
//			calendar.add(Calendar.MONTH, noOfMonths);
//			calendar.add(Calendar.YEAR, noOfYear);
//			return sdf.format(calendar.getTime());
//		}catch(Exception e){
//			e.printStackTrace();
//			return "";
//		}
//	}
//
//	public  static String addDaysToFieldValue(String fieldDate,int noOfDay, int noOfMonths, int noOfYear) {
//		try{			
//			String [] values = fieldDate.split("/");
//			String day = values[1];
//			String month = values[0];
//			String year = values[2];
//			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//			Calendar calendar = Calendar.getInstance(); 
//			calendar.set(Calendar.DATE, Integer.parseInt(day));		
//			calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);		
//			calendar.set(Calendar.YEAR, Integer.parseInt(year));		
//			calendar.add(Calendar.DATE, noOfDay);		
//			calendar.add(Calendar.MONTH, noOfMonths);
//			calendar.add(Calendar.YEAR, noOfYear);
//			return sdf.format(calendar.getTime());
//		}catch(Exception e){
//			e.printStackTrace();
//			return "";
//		}
//	}
//
//	public static String calculateGroupDate(String excelValue,DataProvider data){
//		try{
//			String dateValue = null;
//			String year = "";
//			String month = "";
//			String day = "";		
//			String exlGroupName ="";
//			String exlfieldName ="";
//			try{
//				String [] values  = excelValue.split("\\|");
//				exlGroupName = values[0];
//				String []values1 = 	values[1].split("\\+");		 
//				exlfieldName = values1[0];
//				if(values1[1].length() == 9){
//					year = values1[1].substring(0,2);
//					month = values1[1].substring(3,5);
//					day = values1[1].substring(6,8);
//				}else if(values1[1].length() == 6){
//					String first3 = values1[1].substring(0,3);
//					if(first3.contains("Y")){
//						year = values1[1].substring(0,2);
//						month = values1[1].substring(3,5);             		 
//					}else if(first3.contains("M")){
//						month = values1[1].substring(0,2);            		
//						day = values1[1].substring(3,5);   
//					}            	
//				}else if(values1[1].length() == 3){
//					if(values1[1].contains("Y")){
//						year = values1[1].substring(0,2);
//					}else if(values1[1].contains("M")){    	 
//						month = values1[1].substring(0,2);   		
//					}else if (values1[1].contains("D")){
//						day =values1[1].substring(0,2);
//					}		
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//				dateValue = "";
//			}
//			if(day==""){
//				day="0";
//			}
//			if(month==""){
//				month = "0";
//			}
//			if(year==""){
//				year="0";
//			}
//			String fieldDate = null;
//			fieldDate = (String) data.get(exlGroupName,exlfieldName); 
//			dateValue =	addDaysToFieldValue( fieldDate, Integer.parseInt(day),Integer.parseInt(month),Integer.parseInt(year));
//			return dateValue;
//		}catch (Exception e){
//			e.printStackTrace();
//			return "";
//		}
//	}
//
//	/**
//	 * <b>Description</b> Gets Latest current time in milli seconds
//	 */
//	public static long getLastsetTimeinmili(){
//		Calendar cal = Calendar.getInstance();
//		return cal.getTimeInMillis();
//	}
//
//	/**
//	 * <b>Description</b> Gets Current Fiscal Year
//	 */
//	public static String getFiscalYear(int Month){
//		int FIRST_FISCAL_MONTH  = Month;
//		Calendar cal = Calendar.getInstance();
//		int month = cal.get(Calendar.MONTH);
//		int year = cal.get(Calendar.YEAR);
//		int fiscalyear= (month >= FIRST_FISCAL_MONTH) ? year : year - 1;
//		return String.valueOf(fiscalyear);
//	}
//
//	/**
//	 * <b>Description</b> Gets the date and time difference
//	 * @param          date endDate
//	 * @param 		   date startDate
//	 * @param 		   String format 
//	 * @return         date difference in date 
//	 */
//	public static String getDateTimeDifference(String endDate, String startDate, String format){
//		DateFormat dateFormat = new SimpleDateFormat(format);
//		Date date1 = null;
//		Date date2 = null;
//		try {
//			date1  = dateFormat.parse(endDate);
//			date2 = dateFormat.parse(startDate);
//			long timeDiff = date1.getTime() - date2.getTime();
//			long diffSeconds = timeDiff / 1000 % 60;
//			long diffMinutes = timeDiff / (60 * 1000) % 60;
//			long diffHours = timeDiff / (60 * 60 * 1000) % 24;
//			long diffDays = timeDiff / (24 * 60 * 60 * 1000);
//			if(diffDays > 0){
//				return String.valueOf(diffDays + " days, " + diffHours + " hours, " + diffMinutes + " minutes, " + diffSeconds + " seconds");
//			}else{
//				return String.valueOf(diffHours + " hours, " + diffMinutes + " minutes, " + diffSeconds + " seconds");
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//			return "";
//		}
//	}	
//}
