package com.mg.mi.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.mg.frame.mobi.Helper;

public class BuyRice {
	
	public String pageInfo(HttpServletRequest request) throws Exception{
		
		String goodsid = request.getParameter("i");
		String userid = request.getParameter("u");
		
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql="SELECT tg.id,tg.name,tg.introduce,concat('"+Helper.url+"',tg.image) image,"
				+ " tg.price,tg.surplus,tg.freight,tg.unit,"
				+ " (SELECT COUNT(te.star_level) FROM t_evaluation te WHERE te.goods_id=tg.id) AS person,"
				+ " (SELECT CEIL(SUM(te.star_level)/person*20) FROM t_evaluation te WHERE te.goods_id=tg.id) AS eve	"
				+ " FROM t_goods tg	WHERE tg.id='"+goodsid+"'";
		
		ResultSet rs = stm.executeQuery(sql);
		
		String tg = Helper.rsToJson(rs);
		
		
		String usertype="";
		float off = 0;
		
		sql = "select a.id,a.vipidentity,b.value from t_user a,t_code b where a.vipidentity=b.remarks and "
				+ " b.type='sys' and a.id='"+userid+"'";
		rs = stm.executeQuery(sql);
		
		while(rs.next()){
			usertype=rs.getString(2);
			off = rs.getFloat(3);
		}
		
		String ostr = "";
		
		if(goodsid.equals("1") && usertype.equals("2")){
			ostr="<a><span id=\"e_off\">"+String.valueOf((int)(off*10))+"</span>折专享</a> ";
		}else{
			ostr="<a><span id=\"e_off\"></span></a> ";
		}
		
		String ret = "{tg:"+tg+",ostr:'"+ostr+"'}";
		
		rs.close();
		stm.close();
		dbc.close();
		
		return ret;
		
		
		
	}
	
	
	private boolean saveSale(Connection dbc,String sid,float befnum,float usenum,String did,String uid) throws Exception{
		System.out.println("savasale");
		String sql = "";
		Statement stm = dbc.createStatement();
		
		sql = "update t_sale_info set value="+String.valueOf(befnum-usenum)+" where id='"+sid+"'";
		
		stm.execute(sql);
		
		sql = "insert into t_saleuse(id,saleid,userid,docid,beforenum,usenum,usetime,state) "
				+ " values('"+Helper.getID()+"','"+sid+"','"+uid+"','"+did+"',"
				+ " "+String.valueOf(befnum)+","+String.valueOf(usenum)+",now(),'0')";
		
		stm.execute(sql);
	
		return true;
		
	}
	
	public String tobuy(HttpServletRequest request) throws Exception{
		String reqs = request.getParameter("s");
		System.out.println(reqs);
		
		String[] reqStrs = reqs.split("\\|");
		//------------args--------------------------------
		if(reqStrs.length < 3){
			return "{ret:'1'}" ; //订单错误
		}
		
		String userid = reqStrs[0];
		String goodsid = reqStrs[1];
		int num = 0;
		String viptype ="";
		float off =1;
		
		String goodstype="";
		if(goodsid.equals("1")){
			goodstype = "1";
		}else{
			goodstype = "2";
		}
		
		
		try{
			num = Integer.parseInt(reqStrs[2]);
		}catch(Exception e){
			System.out.println(reqStrs[2]);
			return "{ret:'1'}" ;
		}
		
		ArrayList sales = new ArrayList();
		
		for(int i= 3; i<reqStrs.length;i++){
			sales.add(reqStrs[i]);
		}
		//------args end--------------------------------------
		
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql = "";
		//-------check args-------------------------------------
		sql = "select a.id,a.vipidentity,b.value from t_user a,t_code b where a.vipidentity=b.remarks and "
				+ " b.type='sys' and a.id='"+userid+"'";
		ResultSet rs = stm.executeQuery(sql);
		while(rs.next()){
			viptype = rs.getString(2);
			off = rs.getFloat(3);
		}
		if(viptype.equals("")){
			System.out.println(sql);
			return "{ret:'1'}" ; //order error - user
		}
		
		sql = "select id,price,surplus,freight,name,unit from t_goods where id='"+goodsid+"' and del=1";
		
		float price=0;
		int surplus = 0;
		float freight = 0;
		String name = "";
		String unit = "";
		
		rs = stm.executeQuery(sql);
		while(rs.next()){
			price = rs.getFloat(2);
			surplus = rs.getInt(3);
			freight = rs.getFloat(4);
			name = rs.getString(5);
			unit = rs.getString(6);
		}
		if(price == 0){
			System.out.println(sql);
			return "{ret:'1'}" ; //order error -goods
		}
		if(((surplus == 0) ||(surplus < num)) && surplus!= -1){ //库存不足
			System.out.println(sql);
			return "{ret:'2'}" ; //
		}
		
		
		float totle = num * price ;
		String docId = Helper.getID();
		
		for(int i=0;i<sales.size();i++){
			String sid = (String)sales.get(i);
			sql = "select value,type,off from t_sale_info where id='"+sid+"' and value>0";
			float rest = 0;
			String lx="";
			float soff = 0;
			
			rs = stm.executeQuery(sql);
			
			while(rs.next()){
				rest = rs.getFloat(1);
				lx = rs.getString(2);
				soff = rs.getFloat(3);
			}
			
			if(lx.equals("")){
				return "{ret:'3'}" ; //优惠券无效
			}
			
		}
		
		//--------check end------------------------------------
		
		//--------save------------------------------------
		
		sql = "insert into t_doc(id,user_id,goods_id,price,num,type,count,create_tm,state,del,off,freight) "
				+ " values('"+docId+"','"+userid+"','"+goodsid+"',"
				+ " "+String.valueOf(price)+","+String.valueOf(num)+",'"+goodstype+"',"+String.valueOf(num)+","
				+ " now(),'1','1',"+String.valueOf(off)+",0)";
		System.out.println(sql);
		stm.execute(sql);
		
		if(surplus != -1){
			sql = "update t_goods set surplus="+String.valueOf(surplus - num)+" where id='"+goodsid+"'";
			stm.execute(sql);
		}

		
		for(int i=0;i<sales.size();i++){
			String sid = (String)sales.get(i);
			sql = "select value,type,off from t_sale_info where id='"+sid+"' and value>0";
			float rest = 0;
			String lx="";
			float soff = 0;
			float salerest = 0;
			rs = stm.executeQuery(sql);
			
			while(rs.next()){
				rest = rs.getFloat(1);
				lx = rs.getString(2);
				soff = rs.getFloat(3);
			}
			
			if(lx.equals("101")){
				if(num<=rest){
					salerest = rest-num;
					totle=0;
					num = 0;
					
				}else{
					salerest = 0;
					totle = totle - rest*price ;
					num = num - (int)rest;
				}
			}else if(lx.equals("1000")){
				if(totle <= rest){
					salerest = rest - totle;
					totle = 0;
				}else{
					salerest = 0;
					totle = totle - rest;
				}
			}else{
				if(num<=rest){
					salerest = rest-num;
					totle = totle - (price*(1-soff))*num;
					num = 0;
				}else{
					salerest = 0;
					totle = totle - (price*(1-soff))*rest;
					num = num - (int)rest;
				}
			}
			if((rest-salerest) > 0){
				sql = "update t_sale_info set value="+String.valueOf(salerest)+" where id='"+sid+"'";
				System.out.println(sql);
				stm.execute(sql);
				
				sql = "insert into t_saleuse(id,saleid,userid,docid,beforenum,usenum,usetime,state) "
						+ " values('"+Helper.getID()+"','"+sid+"','"+userid+"','"+docId+"',"
						+ " "+String.valueOf(rest)+","+String.valueOf(rest-salerest)+",now(),'0')";
				System.out.println(sql);
				stm.execute(sql);
			}
			
		}
		
		float ff = freight * num ;
		float payed = totle * off + ff ;
		
		sql = "update t_doc set payed="+String.valueOf(payed)+",freight="+String.valueOf(ff)+" where id='"+docId+"'";
		stm.execute(sql);

		stm.execute("commit");
		
		rs.close();
		stm.close();
		dbc.close();
		

		return "{docid:'"+docId+"'}";
		
		
	}

	//根据订单进行支付
	public String piwdoc(HttpServletRequest request) throws Exception{
		String docid = request.getParameter("d");
		String userid = request.getParameter("u");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql="";
		
		sql = "select goods_id,payed,count,state "
				+ " from t_doc where id='"+docid+"' and user_id='"+userid+"' and del='1'";
		
		ResultSet rs = stm.executeQuery(sql);
		
		String goodsid = "";
		float payed=0;
		float count=0;
		String state="";
		while(rs.next()){
			goodsid = rs.getString(1);
			payed = rs.getFloat(2);
			count = rs.getFloat(3);
			state = rs.getString(4);
		}
		
		if(!state.equals("1")){
			
			rs.close();
			stm.close();
			dbc.close();
			
			return "{ret:'1'}" ;
		}
		
		
		sql = "SELECT name,unit FROM t_goods WHERE id='"+goodsid+"'";
		
		rs = stm.executeQuery(sql);
		
		String desc = "";
		while(rs.next()){
			desc = rs.getString("name")+String.valueOf(count)+rs.getString("unit");
		}
		
	
		rs.close();
		stm.close();
		dbc.close();
		String ret  = AliPay.pay(docid, goodsid, desc, payed);
		
		return "{ret:'"+ret+"'}";
		
	}
	
	//根据订单进行支付
	public String paysucc(HttpServletRequest request) throws Exception{
		String docid = request.getParameter("d");
		
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql="";
		
		sql = "update t_doc set state='2' where id='"+docid+"'";
		stm.execute(sql);
		
		//积分
		sql = "select a.payed,b.name,a.user_id from t_doc a,t_goods b "
				+ "where a.goods_id=b.id and a.id='"+docid+"'";
		ResultSet rs = stm.executeQuery(sql);
		float payed=0;
		String goodsname = "";
		String userid = "";
		while(rs.next()){
			payed = rs.getFloat(1);
			goodsname = rs.getString(2);
			userid = rs.getString(3);
		}

		
		sql = "insert into t_integral(id,integral,date,type,linkid,linkname,status,userid) "
				+ "values('"+Helper.getID()+"',"+String.valueOf(payed)+",now(),'1','"
				+docid+"','"+goodsname+"','交易成功','"+userid+"')";
		
		stm.execute(sql);

		sql = "update t_user set integral=integral+"+String.valueOf(payed)+" where id='"+userid+"'";
		System.out.println(sql);
		stm.execute(sql);
		
		stm.execute("commit");
		
		stm.close();
		dbc.close();
		
		return "{ret:'1'}";
		
	}

	
	//订单信息
	public String piwdid(HttpServletRequest request) throws Exception{
		
		String docid = request.getParameter("d");
		String userid = request.getParameter("u");
		
		
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql="";
		
		sql = "select goods_id,count,date_format(create_tm,'%y-%m-%d %H:%i') tm,payed,off,freight "
				+ " from t_doc where id='"+docid+"' and user_id='"+userid+"' and del='1'";
		
		ResultSet rs = stm.executeQuery(sql);
		
		String goodsid = "";
		while(rs.next()){
			goodsid = rs.getString(1);
		}
		rs.beforeFirst();
		String dinfo=Helper.rsToJson(rs);
		
		sql = "SELECT tg.id,tg.name,tg.introduce,concat('"+Helper.url+"',tg.image) image,"
				+ " tg.price,tg.surplus,tg.freight,tg.unit,"
				+ " (SELECT COUNT(te.star_level) FROM t_evaluation te WHERE te.goods_id=tg.id) AS person,"
				+ " (SELECT CEIL(SUM(te.star_level)/person*20) FROM t_evaluation te WHERE te.goods_id=tg.id) AS eve	"
				+ " FROM t_goods tg	WHERE tg.id='"+goodsid+"'";
		rs = stm.executeQuery(sql);
		String tg = Helper.rsToJson(rs);

		
		sql = "select b.name,a.usenum,b.unit from t_saleuse a,t_sale_info b where a.saleid=b.id and a.docid='"+docid+"'";
		rs = stm.executeQuery(sql);
		
		String sale = Helper.rsToJson(rs);
		
		
		String ret = "{tg:"+tg+",doc:"+dinfo+",sale:"+sale+"}";
		
		rs.close();
		stm.close();
		dbc.close();
		
		return ret;
		
		
		
	}

	


}
