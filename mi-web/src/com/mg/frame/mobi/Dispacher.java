package com.mg.frame.mobi;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

public class Dispacher {
	
	private Connection dbc;
	
	public Dispacher() throws Exception{
		dbc = Helper.getConnection();
	}
	
	public void release() throws Exception{
		dbc.close();
	}
	
	
	public String process(HttpServletRequest request) throws Exception
	{
		
		
		String ss = request.getParameter("r");
		
		String[] req = ss.split("\\.");
		
		ss = ">>>>>request:"+ss;
		
		System.out.println(ss);

		
		String className = "com.mg.mi.processor."+req[0] ;
		Class aClass = Class.forName(className);
		Object aObject = aClass.newInstance();
		
		String methodName = req[1] ;
		
		Method aMethod = aObject.getClass().getMethod(methodName, 
				new Class[]{HttpServletRequest.class});
		
		String rss = (String) aMethod.invoke(aObject, request);
		
		return rss;
	}
}
