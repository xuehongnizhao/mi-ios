package com.mg.frame.web;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

public class Dispacher {
	
	
	public Dispacher() throws Exception{
	}
	
	public void release() throws Exception{
	}
	
	
	public String process(HttpServletRequest request) throws Exception
	{
		
		
		String ss = request.getParameter("r");
		
		ss = ">>>>>web request:"+ss;
		
		System.out.println(ss);

		
		String className = "com.mg.jztx.web.service."+ss ;
		Class aClass = Class.forName(className);
		Object aObject = aClass.newInstance();
		
		Method aMethod = aObject.getClass().getMethod("run", 
				new Class[]{HttpServletRequest.class});
		
		String rss = (String) aMethod.invoke(aObject, request);
		
		return rss;
	}
}
