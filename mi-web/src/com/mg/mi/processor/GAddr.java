package com.mg.mi.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.mg.frame.mobi.Helper;

public class GAddr {
	public String getList(HttpServletRequest request) throws Exception{
		
		String userid = request.getParameter("u");
		String docid = request.getParameter("d");
		
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		

		String sql="select x.*,ifnull(y.num,0) num from "
				+ "(select a.id,contact,phone,addrdetails,concat(b.prov,b.city,b.dist) adt "
				+ "from t_addr a,t_areacode b where a.areaid=b.id and userid='"+userid+"' and del='1') x "
				+ " left join t_logistics y on x.id=y.address_id and y.doc_id='"+docid+"' "
				+ "and (y.state='1' or y.state='2') and y.del='1' order by contact";
		
		ResultSet rs = stm.executeQuery(sql);
		
		String al = Helper.rsToJson(rs);
		sql = "select count,num,unit from t_doc a,t_goods b where a.goods_id=b.id and a.id='"+docid+"'";
		
		rs = stm.executeQuery(sql);
		String cnt="",num="",unit="";
		while(rs.next()){
			cnt = rs.getString(1);
			num = rs.getString(2);
			unit = rs.getString(3);
		}
		
		
		rs.close();
		stm.close();
		dbc.close();
		String ret = "{al:"+al+",cnt:'"+cnt+"',num:'"+num+"',unit:'"+unit+"'}";
		
		return ret;
		
	}

	public String save(HttpServletRequest request) throws Exception{
		
		String req = request.getParameter("s");
		
		String[] arr = req.split("\\|");
		String docid = "";
		String userid="";
		String goodsid="";
		int num=0;
		
		if(arr.length < 2){
			return "{ret:'1'}" ;
		}
		
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		docid = arr[0];
		
		String sql = "";
		
		sql = "select user_id,goods_id,num from t_doc where id='"+docid+"'";
		ResultSet rs = stm.executeQuery(sql);
		
		while(rs.next()){
			userid = rs.getString(1);
			goodsid = rs.getString(2);
			num = rs.getInt(3);
		}
		
		sql = "delete from t_logistics where doc_id='"+docid+"'";
		
		stm.execute(sql);
		
		int tot = 0;
		for(int i=1;i<arr.length;i++){
			
			String[] var= arr[i].split(",");
			
			sql = "insert into t_logistics(id,doc_id,user_id,address_id,goods_id,num,create_tm,state,del) "
					+ " values('"+Helper.getID()+"','"+docid+"','"+userid+"','"+var[0]+"',"
					+ " '"+goodsid+"',"+var[1]+",now(),'1','1')";
			
			
			stm.execute(sql);
			tot = tot + Integer.valueOf(var[1]) ;
		}
		
		sql = "update t_doc set num=count-"+tot+" where id='"+docid+"'";
		System.out.println(sql);
		stm.execute(sql);
		
		if(num == tot){
			sql = "update t_doc set state='3' where id='"+docid+"'";
			stm.execute(sql);
			
		}
		
		stm.execute("commit");
		
		rs.close();
		stm.close();
		dbc.close();
			
		return "{ret:'0'}";
		
	}
	
	
}
