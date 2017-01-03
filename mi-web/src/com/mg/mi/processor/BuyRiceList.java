package com.mg.mi.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.mg.frame.mobi.Helper;

public class BuyRiceList {
	public String pageInfo(HttpServletRequest request) throws Exception{
		
		String type = request.getParameter("t");
		
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		//rice1

		String sql=" SELECT tg.id,tg.name,tg.surplus,concat('"+Helper.url+"',tg.image) image,tg.price,tg.introduce,"
				+ "((select SUm(count) from t_doc td where td.goods_id=tg.id)+tg.surplus) as total,"
				+ "(select CEIL((tg.surplus/total*100)) from DUAL) as percent "
				+ "FROM t_goods tg WHERE tg.level='1' AND tg.show_type='1'";
		
		ResultSet rs = stm.executeQuery(sql);
		
		String rice1 = Helper.rsToJson(rs);
		
		sql = " SELECT tg.id,tg.name,tg.surplus,concat('"+Helper.url+"',tg.image) image,tg.price,tg.introduce,"
				+ "((select SUm(count) from t_doc td where td.goods_id=tg.id)+tg.surplus) as total,"
				+ "(select CEIL((tg.surplus/total*100)) from DUAL) as percent "
				+ "FROM t_goods tg WHERE tg.level='1' AND tg.show_type='2'";

		rs = stm.executeQuery(sql);
		
		String other = Helper.rsToJson(rs);
		
		String ret = "{rice1:"+rice1+",other:"+other+"}";
		
		rs.close();
		stm.close();
		//dbc.close();
		
		return ret;
		
		
		
	}

	
	
	
}
