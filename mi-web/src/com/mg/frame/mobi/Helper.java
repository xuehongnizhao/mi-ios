package com.mg.frame.mobi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Helper {
	
	static public String url="http://1.190.175.59/mi/";
	
	static public Connection getConnection() throws Exception{
		System.out.println(">>>>getConnection");
		Context c = new InitialContext();
		DataSource ds = (DataSource)c.lookup("java:comp/env/jdbc/mi");
		Connection conn = ds.getConnection();
		System.out.println(">>>>Connection geted");
		
		return conn;

	}
	
	static public String getUID(){
		UUID uuid = UUID.randomUUID();
		
		return uuid.toString();
		

	}
	public static String getID(Object param){
		String uuid = UUID.randomUUID().toString();
		return uuid.replace("-", "");
	}
	
	public static String getID(){
		return getID(null);
	}
	
	public static String getRandomOneNum(){
		return String.valueOf((int)Math.floor(Math.random()*10));
	}
	public static String getStringCode(int length){
		String r = "";
		for(int i=0;i<length;i++){
			r += getRandomOneNum();
		}
		return r;
	}
	
	
	public static String SetZero(String str,int size){
		String sour = str;
		for(int i=sour.length();i<size;i++){
			sour = "0" + sour;
		}
		return sour;
	}
	
	public static String LimitLength(String str, int length){
		String temp = str;
		if(temp.length() > length){
			temp = temp.substring(temp.length() - length);
		}
		return temp;
	}

	
	static public String rsToJson(ResultSet rs) throws SQLException{
		String json = "";
		ResultSetMetaData rsa = rs.getMetaData();
		
		json="[" ;
		int cn = rsa.getColumnCount() ;
		while(rs.next()){
			json = json+"{";
			for(int i=0;i<cn;i++){
				String xs = rs.getString(i+1);
				if(xs==null){
					xs="";
				}
				json = json+"'"+rsa.getColumnLabel(i+1)+"':'"+xs+"',";
			}
			json = json.substring(0, json.length()-1)+"},";
		}
		if(json.length() > 1)
			json = json.substring(0, json.length()-1)+"]";
		else
			json = json + "]" ;
		
		
		return json;
		
		
	}

	static public String rsToJsonRange(ResultSet rs,int start,int end) throws SQLException{
		String json = "";
		ResultSetMetaData rsa = rs.getMetaData();
		
		json="[" ;
		int cn = rsa.getColumnCount() ;
		
		int r = 1;
		
		while(rs.next()){
			if(r>=start && r<=end){
				json = json+"{";
				for(int i=0;i<cn;i++){
					json = json+"'"+rsa.getColumnLabel(i+1)+"':'"+rs.getString(i+1)+"',";
				}
				json = json.substring(0, json.length()-1)+"},";
			}
			r++;
		}
		if(json.length() > 1)
			json = json.substring(0, json.length()-1)+"]";
		else
			json = json + "]" ;
		
		
		return json;
		
		
	}
	
	
	
	public static java.sql.Timestamp sqlDate(){
		Timestamp d = new Timestamp(System.currentTimeMillis()); 

		
		java.sql.Timestamp ts = new Timestamp(d.getTime());
		return ts;
		
		
	}
	
	
	public static String sret(String val) {
		return "{'ret':'"+val+"'}" ;
	}
	

}
