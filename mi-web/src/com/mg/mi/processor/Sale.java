package com.mg.mi.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import com.mg.frame.mobi.Helper;

public class Sale {

	public String queryinfo(HttpServletRequest request)throws Exception{
		String uid = request.getParameter("u");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql = "SELECT tsi.id, tsi.content, tsi.name, tsi.value, tsi.unit, "
				+ " tsi.type, "
				+ " DATE_FORMAT(tsl.binddate,'%Y-%m-%d') AS binddate, tsi.status, tsi.total "
				+ " FROM t_sale_info tsi, t_sale_link tsl "
				+ "		WHERE tsi.id = tsl.saleid AND tsl.userid='"+uid
				+ "' ORDER BY tsi.value DESC,tsl.binddate DESC ";

		ResultSet rs = stm.executeQuery(sql);
		String all = Helper.rsToJson(rs);
	
		rs.close();
		stm.close();
		dbc.close();
		String ret = "{allList:"+ all+"}";
		return ret;
	}
	
	public String userSaleInfo(HttpServletRequest request) throws Exception{
		
		String userid = request.getParameter("u");
		String goodsid = request.getParameter("i");
		
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		String sql ="";
		
		sql="SELECT a.userid,a.saleid,a.sour,b.name,b.value,"
				+ " b.unit,b.total,a.binddate,b.type,b.off FROM t_sale_link a,t_sale_info b "
				+ " where a.saleid=b.id and a.userid='"+userid+"' and b.value>0 order by b.type";
		
		ResultSet rs = stm.executeQuery(sql);
		
		
		String sale = Helper.rsToJson(rs);
		
		rs.close();
		stm.close();
		dbc.close();
		if(goodsid.equals("1")){
			return "{sale:"+sale+"}";
		}else{
			return "{sale:[]}";
		}
		
		
	}
	
	
	public String check(HttpServletRequest request) throws Exception{
		
		String sale = request.getParameter("i");
		
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		String sql ="select name,value,unit,type,status,total from t_sale_info where content='"+sale+"'";
		
		ResultSet rs = stm.executeQuery(sql);
		
		
		String sinfo = "";
		sinfo = Helper.rsToJson(rs);
		
		rs.close();
		stm.close();
		dbc.close();
		
		return "{sale:"+sinfo+"}";
	}

	public String bind(HttpServletRequest request) throws Exception{
		
		String sale = request.getParameter("i");
		String userid = request.getParameter("u");
		
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql ="select id from t_user where id='"+userid+"'";
		
		ResultSet rs = stm.executeQuery(sql);
		boolean exist = false;
		
		while(rs.next()){
			exist = true;
		}
		if(!exist){
			rs.close();
			stm.close();
			dbc.close();
			
			return "{ret:'1'}" ;
			
		}
		sql = "select id from t_sale_info where content='"+sale+"' and status='1'";
		rs = stm.executeQuery(sql);
		exist = false;
		String sid="";
		while(rs.next()){
			exist = true;
			sid = rs.getString(1);
		}
		if(!exist){
			rs.close();
			stm.close();
			dbc.close();
			
			return "{ret:'2'}" ;
			
		}
		
		sql = "insert into t_sale_link(userid,saleid,binddate) "
				+ "values('"+userid+"','"+sid+"',now())";
		
		stm.execute(sql);
		
		sql = "update t_sale_info set status='2' where id='"+sid+"'";
		
		stm.execute(sql);
		
		stm.execute("commit");
		
		rs.close();
		stm.close();
		dbc.close();
		
		return "{ret:'0'}";
	}


}
