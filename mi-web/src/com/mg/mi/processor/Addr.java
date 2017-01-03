package com.mg.mi.processor;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.mg.frame.mobi.Helper;

public class Addr {
	public String addrlist(HttpServletRequest request) throws Exception{
		String uid = request.getParameter("u");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		String sql = "SELECT ta.id addrId,contact, phone,prov,city, dist,addrdetails FROM t_addr ta,t_areacode tac WHERE userid='"+uid+"' AND ta.areaid = tac.id AND del='1'";
		
		ResultSet rs = stm.executeQuery(sql);
		String ret = "{addrlist:"+ Helper.rsToJson(rs)+"}";
		
		rs.close();
		stm.close();
		dbc.close();
		
		return ret;
		
	}
	
	public String getaddrfromid(HttpServletRequest request)throws Exception{
		
		String uid = request.getParameter("u");
		String addrid = request.getParameter("addrid");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql = "SELECT ta.id addrid,ta.areaid,contact, phone,prov,city, dist,addrdetails FROM t_addr ta,t_areacode tac WHERE userid='"
					+uid+"' AND ta.areaid = tac.id AND ta.del='1' AND ta.id='"+addrid+"'";
		ResultSet res = stm.executeQuery(sql);
		String addrJson = Helper.rsToJson(res);
		JSONArray array =  JSONArray.fromObject(addrJson);
		String prov  ="黑龙江省";
		String city  = "哈尔滨市";
		if(array != null && array.size()>0){
			JSONObject jsonObject = array.getJSONObject(0);
			prov=jsonObject.getString("prov");
			city = jsonObject.getString("city");
		}else{
			addrJson = "[{prov:'黑龙江省',city:'哈尔滨市',areaid:'230102',dist:'道里区'}]";
		}
		String sql_prov = "SELECT prov from t_areacode group by prov";
		res = stm.executeQuery(sql_prov);
		String provJson = Helper.rsToJson(res);
		
		String sql_city = "SELECT city from t_areacode where prov='"
				+prov+"' group by city ORDER BY id";
		res = stm.executeQuery(sql_city);
		String cityJson = Helper.rsToJson(res);
		
		String sql_dist = "SELECT id,dist FROM t_areacode WHERE prov='"
				+prov+"' AND city='"
				+city+"' GROUP BY dist ORDER BY id";
		res = stm.executeQuery(sql_dist);
		String distJson = Helper.rsToJson(res);
		
		res.close();
		stm.close();
		dbc.close();
		
		String ret = "{addr:"+addrJson+",prov:"+provJson+",city:"+cityJson+",dist:"+distJson+"}";
		return ret;
	}
	
	public String getcity(HttpServletRequest request)throws Exception{
		
		String prov = request.getParameter("prov");
		prov = URLDecoder.decode(prov,"utf-8");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql_city = "SELECT city from t_areacode where prov='"
				+prov+"' group by city ORDER BY id";
		ResultSet res = stm.executeQuery(sql_city);
		String cityJson = Helper.rsToJson(res);
		
		res.close();
		stm.close();
		dbc.close();
		
		String ret = "{city:"+cityJson+"}";
		return ret;
	}
	
	public String getdist(HttpServletRequest request)throws Exception{
		
		String prov = request.getParameter("prov");
		prov = URLDecoder.decode(prov,"utf-8");
		String city = request.getParameter("city");
		city = URLDecoder.decode(city,"utf-8");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql_dist = "SELECT id,dist FROM t_areacode WHERE prov='"
				+prov+"' AND city='"
				+city+"' GROUP BY dist ORDER BY id";
		ResultSet res = stm.executeQuery(sql_dist);
		String distJson = Helper.rsToJson(res);
		
		res.close();
		stm.close();
		dbc.close();
		
		String ret = "{dist:"+distJson+"}";
		return ret;
	}
	public String updateaddr(HttpServletRequest request)throws Exception{
		
		String uid = URLDecoder.decode(request.getParameter("u"),"utf-8");
		String contact = URLDecoder.decode(request.getParameter("contact"),"utf-8");
		String phone  =  URLDecoder.decode(request.getParameter("phone"),"utf-8");
		String addrdetails = URLDecoder.decode(request.getParameter("addrdetails"),"utf-8");
		String areaid  =  URLDecoder.decode(request.getParameter("areaid"),"utf-8");
		String id = URLDecoder.decode(request.getParameter("id"),"utf-8");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql_exsit = "SELECT	COUNT(*) AS count FROM t_addr WHERE contact = '"+contact+"'";
		sql_exsit += "AND phone = '"+phone+"'";
		sql_exsit += "AND addrdetails = '"+addrdetails+"'";
		sql_exsit += "AND areaid = '"+areaid+"'";
		sql_exsit += "AND userid = '"+uid+"'";
		if(id != null && !id.isEmpty()){
			sql_exsit += "AND id  NOT IN ('"+id+"')";
		}

		ResultSet res = stm.executeQuery(sql_exsit);
		String existJson = Helper.rsToJson(res);
		String count = getJsonValFromJsonArray(existJson, "count");
		String update = "0";
		if(Integer.parseInt(count)<1){
			String sql = "UPDATE t_addr SET	contact = '"+contact+"',phone='"
					+phone+"',areaid='"+areaid+"',addrdetails='"
					+addrdetails+ "' WHERE id='"+id+"' AND del= '1'";
			update = stm.executeUpdate(sql)+"";
		}
		
		res.close();
		stm.close();
		dbc.close();
		
		String ret = "{exist:"+count+",update:"+update+"}";
		return ret;
	}
	
	public String addaddr(HttpServletRequest request)throws Exception{
		
		String uid = URLDecoder.decode(request.getParameter("u"),"utf-8");
		String contact = URLDecoder.decode(request.getParameter("contact"),"utf-8");
		String phone  =  URLDecoder.decode(request.getParameter("phone"),"utf-8");
		String addrdetails = URLDecoder.decode(request.getParameter("addrdetails"),"utf-8");
		String areaid  =  URLDecoder.decode(request.getParameter("areaid"),"utf-8");
		String id = URLDecoder.decode(request.getParameter("id"),"utf-8");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql_exsit = "SELECT	COUNT(*) AS count FROM t_addr WHERE contact = '"+contact+"'";
		sql_exsit += "AND phone = '"+phone+"'";
		sql_exsit += "AND addrdetails = '"+addrdetails+"'";
		sql_exsit += "AND areaid = '"+areaid+"'";
		sql_exsit += "AND userid = '"+uid+"'";
		if(id != null && !id.isEmpty()){
			sql_exsit += "AND id  NOT IN ('"+id+"')";
		}
		
		ResultSet res = stm.executeQuery(sql_exsit);
		String existJson = Helper.rsToJson(res);
		String count = getJsonValFromJsonArray(existJson, "count");
		String update = "0";
		if(Integer.parseInt(count)<1){
			id = Helper.getID();
			String sql = "insert into t_addr (id,contact,phone,areaid,addrdetails,userid) VALUES ('"+id+"','"+contact+"','"
					+phone+"','"+areaid+"','"+addrdetails+"','"+uid +"')";
			update = stm.executeUpdate(sql)+"";
			
		}
		
		res.close();
		stm.close();
		dbc.close();
		
		String ret = "{exist:"+count+",update:"+update+"}";
		return ret;
	}
	
	
	public String deladdr(HttpServletRequest request)throws Exception{
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String addrid = request.getParameter("addrid");
		
		String sql = "UPDATE t_addr SET del = '0' WHERE id = '"+addrid+"'";
		
		stm.executeUpdate(sql);
		
		stm.close();
		dbc.close();
		
		String ret="";
		return ret;
	}
	
	public String  getJsonValFromJsonArray(String json,String key){
		JSONArray array =  JSONArray.fromObject(json);
		JSONObject jsonObject = array.getJSONObject(0);
		return jsonObject.getString(key);
	}
}
