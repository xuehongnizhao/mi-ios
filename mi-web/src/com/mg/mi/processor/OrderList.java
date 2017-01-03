package com.mg.mi.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.mg.frame.mobi.Helper;

public class OrderList {
	public String getList(HttpServletRequest request) throws Exception{
		
		String userid = request.getParameter("u");
		
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		//rice1

		String sql="select a.id,goods_id,c.name goodsname,c.unit,concat('"+Helper.url+"',c.image) img,count,num,payed,state,b.name statname,"
				+ " date_format(create_tm,'%y-%m-%d %H:%i') tm "
				+ " from t_doc a,t_code b,t_goods c where a.state=b.value and a.goods_id=c.id and b.type='订单状态' "
				+ " and user_id='"+userid+"' and a.del='1' and state<>'4'  order by tm desc";
		
		ResultSet rs = stm.executeQuery(sql);
		
		String nover = Helper.rsToJson(rs);
		
		sql = "select a.id,goods_id,c.name goodsname,c.unit,concat('"+Helper.url+"',c.image) img,count,num,payed,state,b.name statname,"
				+ " date_format(create_tm,'%y-%m-%d %H:%i') tm "
				+ " from t_doc a,t_code b,t_goods c where a.state=b.value and a.goods_id=c.id and b.type='订单状态' "
				+ " and user_id='"+userid+"' and del='1' and state='4' order by tm desc";
		
		String over = Helper.rsToJson(rs);
	
		
		String ret = "{nover:"+nover+",over:"+over+"}";
		
		rs.close();
		stm.close();
		dbc.close();
		
		return ret;
		
		
		
	}

	
	
	
}
