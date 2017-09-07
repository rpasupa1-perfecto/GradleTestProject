//package org.perfecto.mobile.util;
//
//import java.sql.*;
//import java.util.Iterator;
//
//import com.sun.rowset.*;
//
//import cts.qea.automation.*;
//import cts.qea.automation.reports.*;
//
//public class DbHelper {
//
//	private  Connection connection = null;
//
//	/**
//	 * <b>Description</b> Connect to the Database with Config parameter values 
//	 *                    serverName, host, username, password, portNumber,
//	 *                    url , schema
//	 * @throws            Exception   Exception
//	 * 
//	 */
//
//	public  void connectDatabase() throws Exception {		
//		String driverName = "oracle.jdbc.driver.OracleDriver";
//		Class.forName(driverName);
//		// Create a connection to the database 
//		String serverName = Config.get("DBConnections", "serverName");
//		String host = Config.get("DBConnections", "hostName");
//		String userName = Config.get("DBConnections", "userName");
//		String passWord = Config.get("DBConnections", "password");
//		String portNumber = Config.get("DBConnections", "port");
//		String schema = Config.get("DBConnections", "schema"); 
//		connectDatabase(serverName, host, userName, passWord, portNumber, schema);
//		
//	}
//
//	public  void connectDatabase(String section) throws Exception {		
//		String driverName = "oracle.jdbc.driver.OracleDriver";
//		Class.forName(driverName);
//		// Create a connection to the database 
//		String serverName = Config.get(section, "serverName");
//		String host = Config.get(section, "hostName");
//		String userName = Config.get(section, "userName");
//		String passWord = Config.get(section, "password");
//		String portNumber = Config.get(section, "port");
//		String schema = Config.get(section, "schema");
//		connectDatabase(serverName, host, userName, passWord, portNumber, schema);
//	}
//
//
//	/**
//	 * <b>Description</b> Connect to the Database with formal parameters values 
//	 *                    serverName, host, username, password, portNumber,
//	 *                    schema
//	 * @param             serverName   Server Name
//	 * @param             host         Host Name
//	 * @param             userName     User Name
//	 * @param             passWord     Password
//	 * @param             portNumber   Port Number
//	 * @param             schema       schema
//	 * @throws            Exception    Exception
//	 * 
//	 */	
//	protected void connectDatabase(String serverName,String host,String userName,String  passWord,String portNumber,String schema) throws Exception {		
//		String driverName = "oracle.jdbc.driver.OracleDriver";
//		Class.forName(driverName);	     
//		String url = "jdbc:oracle:thin:@//" + host + ":" + portNumber + "/" + serverName;            
//		connection = java.sql.DriverManager.getConnection(url, userName, passWord);
//		setDefaultSchema(schema); 
//	}
//
//
//	/**
//	 * <b>Description</b> Sets schema to the Formal parameter schema 
//	 *  
//	 * @param             schemaName  Schema Name
//	 * @throws            Exception   Exception
//	 * 
//	 */	
//	private void setDefaultSchema(String schemaName) throws Exception {		
//		Statement s = connection.createStatement();
//		s.executeQuery("Alter Session Set Current_schema="+schemaName);
//	}
//
//	/**
//	 * <b>Description</b> Executes Query and returns ResultSet. Returns null if fails
//	 *  
//	 * @param             query       Query to Execute
//	 * @return            ResultSet   Current ResultSet
//	 * @throws            Exception   Exception
//	 * 
//	 */	
//	public ResultSet executeQuery(String query) throws Exception {
//		return getResultSet(query);
//	}
//
//	/**
//	 * <b>Description</b> Gets current Result Set after executing Query.  Returns null if fails
//	 *  
//	 * @param             query       Query to Execute
//	 * @return            ResultSet   Current ResultSet
//	 * @exception         Exception   Exception
//	 * 
//	 */
//	private ResultSet getResultSet(String query) {	
//		ResultSet rs = null;
//		if(query == null || 
//				query.equalsIgnoreCase("")){
//			Report.log("No Query is available", Status.DONE);
//			return null;
//		}
//		try {
//			Statement s = connection.createStatement();
//			Report.log("SQL Query used is: "+query, Status.DONE);
//			rs = s.executeQuery(query);	
//			if(rs == null){
//				Report.log("Query-> returned with null values", Status.DONE);
//			}
//		} catch (Exception e) {
//			Report.log("Query-> Due to Syntax error, Query :"+query+"has been failed with Exception: "+e, Status.Fail);
//			e.printStackTrace();		    
//		}
//		return rs;
//	}
//
//
//	/**
//	 * <b>Description</b> Close Database Connection
//	 *  	
//	 * @throws         Exception   Exception
//	 * 
//	 */
//	public void closeDatabase() throws Exception {		
//		connection.close();		
//	}
//
//	/**
//	 * <b>Description</b> Gets current Result Set after executing Query.  Returns false if fails
//	 *  
//	 * @param             dp          Data Provider 
//	 * @param             rs          Current ResultSet
//	 * @param             groupName   Group Name
//	 * @throws            Exception   Exception
//	 * 
//	 */	
////	public boolean saveResultSet(DataProvider dp, ResultSet rs,String groupName) throws Exception {
////		CachedRowSetImpl metaData = null;
////
////		if(rs == null) {
////			Report.log("Data Provider: recordset was null", Status.DONE);
////			return false;
////		}
////		
////		metaData = new CachedRowSetImpl();
////		metaData.populate(rs);
////		
////		if(!metaData.next()){
////			Report.log("Query-> returned with null values", Status.DONE);
////			metaData.close();
////			return false;
//	///	}
//		
////		int colCount = metaData.getMetaData().getColumnCount();
////		
////		for(int i = 1; i <= colCount; i++){				
////			String columnName = metaData.getMetaData().getColumnName(i);
////			String value = metaData.getString(i);
////			dp.set(groupName, columnName, value);
////			Report.log(columnName+" retrieved  from DB is "+value, Status.DONE);
////		}
////		
////		metaData.close();
////		return true;
//	//}	
//
//	public ResultSet executeDependentQuery(String query, DataProvider dp) throws Exception{
//		String query_final=query;
//		Iterator<String> it = dp.columnIterator("PreRequisite");
//		while (it.hasNext()){
//			String columnName = it.next().toString().trim();
//			String value = dp.get("PreRequisite", columnName);
//			if(query_final.contains(columnName))
//				query_final = query_final.replace("{"+ columnName +"}", value);
//		}
//		return getResultSet(query_final);
//	}
//
//	/**
//	 * <b>Description</b> Executes Query and Returns TRUE or FALSE for a Record Present in DB or not respectively 
//	 *  
//	 * @param             query       Query to Execute
//	 * @return            boolean     flag   Returns TRUE if Record Present in DB else FALSE
//	 * @exception         Exception   Exception
//	 * 
//	 */
//	public boolean isRecordPresent(String query) throws Exception {
//		return isPresent(query);
//	}
//
//	/**
//	 * <b>Description</b> Executes Query and Returns TRUE or FALSE for a Record Present in DB or not respectively 
//	 *  
//	 * @param             query       Query to Execute
//	 * @return            boolean     flag   Returns TRUE if Record Present in DB else FALSE
//	 * @exception         Exception   Exception
//	 * 
//	 */
//	private boolean isPresent(String query){
//		System.out.println("Query is    "+query);
//		ResultSet rs = null;
//		if(query == null || 
//				query.equalsIgnoreCase("")){
//			Report.log("No Query is available", Status.DONE);
//			return true;
//		}
//		try {
//			Statement s = connection.createStatement();
//			rs = s.executeQuery(query);	
//			if(!rs.next()){
//				Report.log("Query-> returned Zero Records from DB", Status.DONE);
//				return false;
//			}
//		} catch (Exception e) {
//			Report.log("Query-> Due to Syntax error, Query :"+query+"has been failed with Exception: "+e, Status.Fail);
//			e.printStackTrace();		    
//		}
//		return true;
//	}
//
//}
