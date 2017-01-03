package com.mg.mi.processor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mg.frame.mobi.Helper;
import com.mg.frame.mobi.SmsUtil;

public class User {

	public String userinfo(HttpServletRequest request) throws Exception {
		String uid = request.getParameter("u");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql = "select nickname,mobile,money,integral from t_user where id='"+uid+"'";
		
		ResultSet rs = stm.executeQuery(sql);
		
		String ret = Helper.rsToJson(rs);
		
		rs.close();
		stm.close();
		return ret;
		
	}
	public String honorinfo(HttpServletRequest request) throws Exception{
		String uid = request.getParameter("u");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		String sql = "SELECT t1.id,t1.name,t1.intro,t1.condition,t1.deal,t1.active_image,t1.noactive_image,(SELECT honorid FROM t_honor_link t2 WHERE t2.userid= '"
					+uid+"' AND t2.honorid = t1.id) AS state FROM t_honor t1";
		
		ResultSet rs = stm.executeQuery(sql);
		
		String ret = "{honorinfo:"+ Helper.rsToJson(rs)+"}";
		rs.close();
		stm.close();
		dbc.close();

		return ret;
	}

	public String userinfoset(HttpServletRequest request) throws Exception{
		String uid = request.getParameter("u");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		String sql = "SELECT t1.id,t1.nickname,t1.vipnumber,t1.viptype,t1.vipidentity,t1.integral,t1.money,t1.mobile,t1.regdate,t1.infoIntegrity,(SELECT count(userid) from t_sale_link ts where ts.userid=t1.id) as salecount,(SELECT tel FROM t_call_center) as callcenter FROM t_user t1 where t1.id='"+uid+"'";
		
		ResultSet rs = stm.executeQuery(sql);
		
		String ret = Helper.rsToJson(rs);
		
		rs.close();
		stm.close();
		dbc.close();

		return ret;
		
	}
	
	public String perinfo(HttpServletRequest request) throws Exception {
		String uid = request.getParameter("u");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		String sql = " SELECT id, infoIntegrity, uname, sex, Date_Format(birth,'%Y/%m/%d') as birth,"
				+ " signature, qq, occupation , company, school, interest, perinfo "
				+ " FROM t_user WHERE id = '" + uid + "'";

		ResultSet rs = stm.executeQuery(sql);
		String ret = Helper.rsToJson(rs);
		
		rs.close();
		stm.close();
		dbc.close();
		
		return ret;
	}

	int infoIntegrity = 0;

	public String updatePerInfo(HttpServletRequest request) throws Exception {
		infoIntegrity = 0;

		String uname = formatString(request.getParameter("uname"));
		String sex = formatString(request.getParameter("sex"));
		String birth = formatString(request.getParameter("birth"));
		String signature = formatString(request.getParameter("signature"));
		String qq = formatString(request.getParameter("qq"));
		String occupation = formatString(request.getParameter("occupation"));
		String company = formatString(request.getParameter("company"));
		String school = formatString(request.getParameter("school"));
		String interest = formatString(request.getParameter("interest"));
		String perinfo = formatString(request.getParameter("perinfo"));

		String uid = request.getParameter("u");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();

		String sql = "UPDATE t_user tu SET " + "tu.infoIntegrity= '"
				+ infoIntegrity + "' ,tu.uname =" + uname + " ,tu.sex= " + sex
				+ " ,tu.birth= " + birth + " ,tu.signature= " + signature
				+ " ,tu.qq= " + qq + " ,tu.occupation= " + occupation
				+ ",tu.company= " + company + ",tu.school= " + school
				+ ",tu.interest= " + interest + "	,tu.perinfo= " + perinfo
				+ "	WHERE tu.id='" + uid + "'";
		System.out.println(sql);
		int res = stm.executeUpdate(sql);
		String ret = "{update:" + res + "}";
		
		stm.close();
		dbc.close();
		
		return ret;
	}

	private String formatString(String key) {
		try {
			key = URLDecoder.decode(key, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (key == null || key.equals("")) {
			key = null;
		} else {
			infoIntegrity += 10;
			key = "'" + key + "'";
		}
		return key;
	}

	public String modifyPassword(HttpServletRequest request) throws Exception {
		String oldpwd = URLDecoder.decode(request.getParameter("oldpwd"),
				"utf8");
		String newpwd = URLDecoder.decode(request.getParameter("newpwd"),
				"utf8");
		String uid = URLDecoder.decode(request.getParameter("userid"), "utf8");
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();

		String sql = "select id from t_user where id='" + uid
				+ "' and passwd='" + oldpwd + "'";
		ResultSet rs = stm.executeQuery(sql);
		int rc = 0;
		while (rs.next()) {
			rc++;
		}
		if (rc < 1) {
			return Helper.sret("1");
		}
		sql = "update t_user set passwd='" + newpwd + "' where id = '" + uid+ "'";
		int res = stm.executeUpdate(sql);
		
		rs.close();
		stm.close();
		dbc.close();
		
		if(res  == 1){
			return Helper.sret("ok");
		}
		
		return Helper.sret("2");
	}
	
	public String insertpush(HttpServletRequest request) throws Exception{
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String userid = request.getParameter("userid");
		String pushid = request.getParameter("pushid");

		String sql ="DELETE FROM t_user_push_link WHERE userid='"+userid
				+ "' OR pushid = '"+pushid+ "'";
		stm.execute(sql);
				
		String insertsql = "INSERT INTO t_user_push_link (userid,pushid) VALUE ('"
				+userid+ "','"+pushid+ "')";

		stm.executeUpdate(insertsql);
		stm.close();
		dbc.close();
		return Helper.sret("");
	}

	public String validateCode(HttpServletRequest request) throws Exception{
		Map<String, Object> res = new HashMap<String, Object>();
		String phone = request.getParameter("phone");
		String ret = "0";
		if(phone != null && !"".equals(phone)){
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("phone", phone);
			p.put("code", Helper.getStringCode(6));
			if(createMsgCode(p) > 0){
				res.put("result", "0");
				SmsUtil.sendSMSMessage(p.get("phone").toString(), p.get("code").toString());
			}else{
				ret = "-1";
			}
		}else{
			ret = "-1";
		}
		

		return ret;
		
	}


	public String register(HttpServletRequest request) throws Exception{
		String mobile = request.getParameter("phone");
		String code = request.getParameter("code");
		String nick = URLDecoder.decode(request.getParameter("nick"),"utf8");
		String passwd = request.getParameter("passwd");
		String info = "mobile:|"+mobile+"|,code:|"+code+"|,nick:|"+nick+"|,passwd:|"+passwd+"|";
		
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql = "select mobile from t_user where mobile='"+mobile+"'";
		ResultSet rs = stm.executeQuery(sql);
		int rc =0;
		while(rs.next()){
			rc ++ ;
		}
		if(rc >0){
			return "{'ret':'1'}";
		}
		
		sql = "select nickname from t_user where nickname='"+nick+"'";
		
		rs = stm.executeQuery(sql);
		rc =0;
		while(rs.next()){
			rc ++ ;
		}
		if(rc >0){
			return "{'ret':'2'}";
		}
		
		
		if(!validMessageCode(mobile,code)){
			return "{'ret':'3'}";
		}
		String vipnumber="";
		sql = "select max(vipnumber)+1 from t_user";
		rs = stm.executeQuery(sql);
		while(rs.next()){
			vipnumber = rs.getString(1);
		}

		sql = "insert into t_user(id,nickname,vipnumber,integral,money,mobile,regdate,del,passwd,vipidentity)"
				+ "values(?,?,?,0,0,?,?,1,?,'1')";
		PreparedStatement pstm = dbc.prepareStatement(sql);
		String userid = Helper.getID();
		pstm.setString(1, userid);
		pstm.setString(2, nick);
		pstm.setString(3, vipnumber);
		pstm.setString(4, mobile);
		pstm.setTimestamp(5, Helper.sqlDate());
		pstm.setString(6, passwd);
		
		pstm.executeUpdate();
		
		stm.execute("commit");
		rs.close();
		stm.close();
		dbc.close();
		
		
		return "{'ret':'"+userid+"'}";
	}
	
	private boolean isNumStr(String str){
		for(int i=0;i<str.length();i++){
			char ch = str.charAt(i);
			if(ch < '0' || ch > '9'){
				return false;
			}
			
		}
		return true;
	}

	
	public String login(HttpServletRequest request) throws Exception{
		String mobile = URLDecoder.decode(request.getParameter("phone"),"utf8");
		String type = request.getParameter("type");
		String info = request.getParameter("info");
		
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		String istr = "mobile:|"+mobile+"|,type:|"+type+"|,info:|"+info+"|";
		System.out.println(istr);
		
		
		String sql = "";
		if((mobile.length() == 11) &&  isNumStr(mobile)){
			sql = "select id from t_user where mobile='"+mobile+"'";
		}else{
			sql = "select id from t_user where nickname='"+mobile+"'";
		}
		ResultSet rs = stm.executeQuery(sql);
		int rc =0;
		String user="";
		while(rs.next()){
			user = rs.getString(1);
		}
		
		if(user.length() == 0){
			sql = "select id from t_user where nickname='"+mobile+"'";
			rs = stm.executeQuery(sql);
			while(rs.next()){
				user = rs.getString(1);
			}
		}
		
		if(user.length() == 0){
			return Helper.sret("1");
		}
		
		if(type.equals("1")){
			if(validMessageCode(mobile,info)){
				return Helper.sret(user);
			}else{
				return Helper.sret("2");
			}
		}
		
		if(type.equals("0")){
			sql = "select passwd from t_user where id='"+user+"'";
			rs = stm.executeQuery(sql);
			while(rs.next()){
				if(info.equals(rs.getString(1))){
					return Helper.sret(user);
				}else{
					return Helper.sret("3");
				}
			}
		}
		rs.close();
		stm.close();
		dbc.close();
		
		return Helper.sret("4");
		
		
		
		
	}	
	
	
	
	
	private int createMsgCode(Map<String, Object> param) throws Exception {
		param.put("id", Helper.getID());
		param.put("create_tm", new Date());
		
		Connection dbc = Helper.getConnection();
		String sql = "insert into t_message_code(id,phone,code,create_tm) values(?,?,?,?)";
		
		PreparedStatement stm = dbc.prepareStatement(sql);
	
		stm.setString(1, (String) param.get("id"));
		stm.setString(2, (String) param.get("phone"));
		stm.setString(3, (String) param.get("code"));
		stm.setTimestamp(4, Helper.sqlDate());

		stm.executeUpdate();
		
		stm.execute("commit");
		stm.close();
		dbc.close();
	
		return 1;
		
	}
	
	private boolean validMessageCode(String mobile,String code) throws Exception{
		
		Connection dbc = Helper.getConnection();
		
		String sql = "select code from t_message_code where code='"+code+"' and phone='"+mobile+"'"
				+ " and ?<date_add(create_tm,interval 10 minute)";
		PreparedStatement pstm = dbc.prepareStatement(sql);
		pstm.setTimestamp(1, Helper.sqlDate());
		ResultSet rs = pstm.executeQuery();
		int rc = 0;
		while(rs.next()){
			rc ++ ;
		}
		
		rs.close();
		pstm.close();
		dbc.close();

		
		if(rc == 0){
			return false;
		}
		return true;

	}


}
