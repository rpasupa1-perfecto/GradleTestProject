//package org.perfecto.mobile.excel;
//
//import java.io.*;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.xssf.usermodel.*;
//import org.perfecto.mobile.util.DbHelper;
//import org.testng.annotations.DataProvider;
//
////import cts.qea.automation.*;
////import cts.qea.automation.reports.*;
//
//public class ExcelHelper {
//
//	public final int testDataNoOfRows = 60000;
//	public final int testDataColumncount = 999;
//	public final int testCaseColumn = 0;
//	public final int testDataColumn = 0;
//	public final int iterationColumn = 1;
//	public final int executeTestCaseColumn = 2;
//	public final int driverNameColumn = 3;
//	//	public final int packageName = 2;
//	
//	/**
//	 * <b>Description</b> Finds Iteration Row Number for a Test Case
//	 * @param             sheet Excel tab of test cases to identify
//	 * @param             testcaseRow  Test case Row Number in the Excel Sheet
//	 * @param             testCase   Test Case Id
//	 * @param             iterationRow   Iteration Id
//	 * @return            int (Iteration Id Row number in a Excel sheet)
//	 */
//	public int findDisplaceMentrow(XSSFSheet sheet, int testcaseRow, String testCase, String iterationRow){
//		for (int row = testcaseRow; row < testDataNoOfRows; row++) {
//			String tstCase = getCellData(sheet, row,testCaseColumn).toLowerCase().trim();
//			if(!tstCase.equals(testCase))
//				return -1;
//			String iteration = getCellData(sheet, row,iterationColumn).toLowerCase().trim();
//			if (!iteration.equals(iterationRow))	continue;
//			return  row - testcaseRow;
//		}
//		return -1;
//	}
//
//	/**
//	 * <b>Description</b> Gets a Cell data reference by ROW and COLUMN number. Empty String if fails
//	 * @param             mySheet Excel tab of test cases to identify
//	 * @param             row  Row number of the excel sheet
//	 * @param             col  Column number of the excel sheet
//	 * @return            returnVal  Value of the CELL referenced by ROW and COLUMN number
//	 *
//	 */
//
//	public synchronized String getCellData(XSSFSheet mySheet, int row, int col){
//		String returnVal="";
//		try{
//			XSSFCell cell = mySheet.getRow(row).getCell(col);
//			switch(cell.getCellType()){
//			case Cell.CELL_TYPE_STRING:
//				returnVal = cell.getStringCellValue();
//				if(returnVal.length()>4){
//					if(returnVal.substring(0,4).equalsIgnoreCase("sql:")){
//						System.out.println("we got SQL function ");
//						String [] sqlQuery = returnVal.split("SQL:");
//						DbHelper dbHelper = new DbHelper();
//						dbHelper.connectDatabase();
//						ResultSet rs = dbHelper.executeQuery(sqlQuery[1]);
//						if(rs != null && rs.next()){
//							returnVal = rs.getString(1);
//							System.out.println("SSN retrieved is "+returnVal);
//						}else{
//					//		Report.log("Unable to retrive data from DB for the query  :"+mySheet.getRow(row).getCell(col).getStringCellValue(), Status.FAIL);
//						}
//						dbHelper.closeDatabase();
//					}
//				}
//				break;
//			case Cell.CELL_TYPE_NUMERIC:
//				returnVal = ""+cell.getNumericCellValue();
//				break;
//			case Cell.CELL_TYPE_BOOLEAN:
//				returnVal = ""+cell.getBooleanCellValue();
//				break;
//			default:
//				returnVal = "";
//				break;
//			}
//		} catch(Exception e){
//			returnVal = "";
//		}
//
//		return returnVal;
//	}
//
//	/**
//	 * <b>Description</b> Gets all Cell data referenced by ROW. Empty String if fails
//	 * @param             mySheet Excel tab of test cases to identify
//	 * @param             row  Row number of the excel sheet
//	 * @return 			  deviceInfo List
//	 */
//
//	public synchronized List<String> getRowData(XSSFSheet mySheet, int row){
//		String currentValue="";
//		List<String> returnVal = new ArrayList<String>();
//		try{
//			Iterator<Cell> cellIterator = mySheet.getRow(row).cellIterator();
//			while(cellIterator.hasNext()){
//				XSSFCell cell = (XSSFCell) cellIterator.next();
//				switch(cell.getCellType()){
//				case Cell.CELL_TYPE_STRING:
//					currentValue = cell.getStringCellValue();
//					break;
//				case Cell.CELL_TYPE_NUMERIC:
//					currentValue = ""+cell.getNumericCellValue();
//					break;
//				case Cell.CELL_TYPE_BOOLEAN:
//					currentValue = ""+cell.getBooleanCellValue();
//					break;
//				default:
//					returnVal = null;
//					break;
//				}
//				returnVal.add(currentValue);
//			}
//		} catch(Exception e){
//			returnVal = null;
//		}
//		return returnVal;
//	}
//
//	/**
//	 * <b>Description</b>Load Data Provider from the Test Data Sheet for the respective Test case and Iteration Id
//	 * @param             mySheet Excel tab of test cases to identify
//	 * @param             row  Row number of the excel sheet
//	 * @param             iteration   Iteration Id
//	 * @return            dp  Data Provider for the test case
//	 *
//	 */
//	public synchronized DataProvider load(XSSFSheet mySheet, int row, int iteration) {
//		//DataProvider dp = new DataProvider();
//		for (int column = testDataColumn; column < testDataColumncount; column++) {
//			String groupName = getCellData(mySheet, row, column);
//			String columnName = getCellData(mySheet, row+1, column).toLowerCase();
//			String data = "";
//			// To get Date related data
//			if((!columnName.isEmpty()) && columnName.substring(0,3).equalsIgnoreCase("dt_")){
//		//		data = getDateCellData(mySheet, row+1+iteration,column,dp);
//			}else{
//				data = getCellData(mySheet, row+1+iteration,column);
//			}
//
//			if(!groupName.isEmpty() && !columnName.isEmpty()){
//			//	dp.set(groupName, columnName, data);
//			}
//		}
//	//	return dp;
//		return null;
//	}
//
//	/**
//	 * <b>Description</b> Gets Date Cell data from the referenced Row and column number
//	 *                    PCDate refers to local Desktop Date,  APPDate Application deployed server Date,
//	 *
//	 * @param             sheet Excel tab of test cases to identify
//	 * @param             row  Row number of the excel sheet
//	 * @param             col  Column number of the excel sheet
//	 * @param             iterationRow   Iteration Id
//	 * @return            dp  Data Provider for the test case
//	 *
//	 */
//
//	private String getDateCellData(XSSFSheet mySheet,int row , int col,DataProvider dp){
//		String data = "";
//		try{
//			if(mySheet.getRow(row)!=null){
//				if(mySheet.getRow(row).getCell(col)!=null){
//					if(mySheet.getRow(row).getCell(col).getCellType()==Cell.CELL_TYPE_STRING){
//
//						if(mySheet.getRow(row).getCell(col).getStringCellValue().contains("PCDate")){
//							data =  DateHelper.calculateDate(mySheet.getRow(row).getCell(col).getStringCellValue(),"PCDate");
//
//						}else if(mySheet.getRow(row).getCell(col).getStringCellValue().contains("APPDate")) {
//							data = DateHelper.calculateDate(mySheet.getRow(row).getCell(col).getStringCellValue(),"APPDate");
//
//						}else if (dp.containsGroup(mySheet.getRow(row).getCell(col).getStringCellValue())&&
//								mySheet.getRow(row).getCell(col).getStringCellValue().contains("|")){
//							data = DateHelper.calculateGroupDate(mySheet.getRow(row).getCell(col).getStringCellValue(),dp.clone());
//
//						}else{
//							data = DateHelper.dateConverter(getCellData(mySheet, row,col),Config.get("testData", "dateFormat"));
//						}
//					}else if((HSSFDateUtil.isCellDateFormatted(mySheet.getRow(row).getCell(col)))){
//						data = DateHelper.dateConverter(mySheet.getRow(row).getCell(col).getDateCellValue().toString(),Config.get("testData", "dateFormat"));
//					}else{
//						data = "";
//					}
//				}
//			}
//		}catch(Exception e){
//			data = "";
//		}
//		return data;
//	}
//
//	/**
//	 * <b>Description</b> Finds a Row for the respective Test cases Id supplied. Returns -1 if fails
//	 * @param             sheet Excel tab of test cases to identify
//	 * @param             testcaseID  Test case Id
//	 * @return            int   Test case Id Row Number from the excle sheet
//	 */
//
//	public int findRow(XSSFSheet sheet, String testcaseID){
//		for (int row = 0; row < testDataNoOfRows; row++) {
//			String xlTCName = getCellData(sheet, row,testCaseColumn);
//			if (!xlTCName.toLowerCase().trim().equals(testcaseID))	continue;
//			return row;
//		}
//		return -1;
//	}
//
//	/**
//	 * <b>Description</b> Gets Sheet from the Excel file
//	 * @param             fileName   File Name
//	 * @param             sheetName  Sheet Name
//	 * @return            XSSFSheet  Sheet
//	 * @throws            Exception
//	 */
//	public synchronized XSSFSheet getSheet(String fileName,String sheetName) throws Exception{
//		InputStream myInput;
//		XSSFWorkbook workbook;
//		XSSFSheet mySheet = null;
//		//System.out.println("\n\n\nExcel file - " + "html.xsl");
//		//System.out.println("\n\n\nExcel path - " + ClassLoader.class.getResource().getPath());
//		//myInput = new FileInputStream(getClass().getResource(fileName).getPath());
//		myInput = getClass().getResourceAsStream(fileName);
//
//		workbook = new XSSFWorkbook(myInput);
//		mySheet = workbook.getSheet(sheetName);
//		workbook.close();
//		return mySheet;
//	}
//
//}
