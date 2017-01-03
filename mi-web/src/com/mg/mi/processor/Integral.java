package com.mg.mi.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import com.mg.frame.mobi.Helper;

public class Integral {

	public String queryinfo(HttpServletRequest request)throws Exception{
		String uid = request.getParameter("u");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql = "SELECT tm.id, tm.integral, DATE_FORMAT(tm.date,'%Y-%m-%d') date, tm.type,"
					+ " tm.linkid, tm.linkname, tm.status FROM t_integral tm"
					+ " WHERE userid = '"+uid+"' ORDER BY tm.date DESC,tm.type DESC";

		ResultSet rs = stm.executeQuery(sql);
		String all = Helper.rsToJson(rs);

		String sql_in = "SELECT tm.id, tm.integral, DATE_FORMAT(tm.date,'%Y-%m-%d') date, tm.type,"
				+ " tm.linkid, tm.linkname, tm.status FROM t_integral tm"
				+ " WHERE userid = '"+uid+"' AND type='1' ORDER BY tm.date DESC,tm.type DESC";

		ResultSet rs_in = stm.executeQuery(sql_in);
		String in = Helper.rsToJson(rs_in);
		
		String sql_out = "SELECT tm.id, tm.integral, DATE_FORMAT(tm.date,'%Y-%m-%d') date, tm.type,"
				+ " tm.linkid, tm.linkname, tm.status FROM t_integral tm"
				+ " WHERE userid = '"+uid+"' AND type='2' ORDER BY tm.date DESC,tm.type DESC";

		ResultSet rs_out = stm.executeQuery(sql_out);
		String out = Helper.rsToJson(rs_out);
	
		rs.close();
		rs_in.close();
		rs_out.close();
		stm.close();
		dbc.close();
	
		String ret = "{allList:"+ all+",inList:"+in+",outList:"+out+"}";
		return ret;
	}
	
}
