package com.mg.mi.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import com.mg.frame.mobi.Helper;

public class Eval {
	public String query(HttpServletRequest request) throws Exception{
		
		String goodsid = request.getParameter("i");
		int start = Integer.parseInt(request.getParameter("s"));
		int end = Integer.parseInt(request.getParameter("e"));
		
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();

		String sql="SELECT (SELECT COUNT(te.star_level) FROM t_evaluation te WHERE te.goods_id=tg.id) AS person,"
				+ " (SELECT CEIL(SUM(te.star_level)/person*20) FROM t_evaluation te WHERE te.goods_id=tg.id) AS eve	"
				+ " FROM t_goods tg	WHERE tg.id='"+goodsid+"'";
		
		ResultSet rs = stm.executeQuery(sql);
		
		
		String tg = Helper.rsToJson(rs);
		
		
		
		sql = " SELECT te.star_level*20 starLevel,te.content, Date_Format(te.createtime,'%Y-%m-%d') createtime, "
				+ "te.nickname "
				+ " FROM t_evaluation te "
				+ " WHERE te.goods_id='"+goodsid+"' ORDER BY te.createtime DESC";
		
		rs = stm.executeQuery(sql);
		
		String eva = Helper.rsToJsonRange(rs, start, end);
		String ret = "{tg:"+tg+",eva:"+eva+"}";
		
		rs.close();
		stm.close();
		dbc.close();
	
		return ret;
	}
	
		
		

}
