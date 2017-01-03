package com.mg.mi.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.mg.frame.mobi.Helper;

public class FirstPage {
	public String getData(HttpServletRequest request) throws Exception{
		
		
		//Connection dbc = Helper.getConnection();
		
		String ss = "";
		
		ss= "{'a':'1'}";
		
		return ss;
		
	}
	
	public String gglist(HttpServletRequest request) throws Exception{
		Connection dbc = Helper.getConnection();
		
		Statement stm = dbc.createStatement();
		
		ResultSet rs = stm.executeQuery("select url,concat('"+Helper.url+"',image) image from t_adv where state=0 order by order_num");
		
		String ret = Helper.rsToJson(rs);
		
		rs.close();
		stm.close();
		//dbc.close();
		
		return ret;
		
		
		
		
		
	}
}
