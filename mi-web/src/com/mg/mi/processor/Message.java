package com.mg.mi.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import com.mg.frame.mobi.Helper;

public class Message {

	public String getMessage(HttpServletRequest request) throws Exception {

		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		int start = Integer.parseInt(request.getParameter("s"));
		int end = Integer.parseInt(request.getParameter("e"));
		String uid = request.getParameter("u");

		String sql = "SELECT tmsg.`ID` AS id, tmsg.`msg_content` AS  content, DATE_FORMAT(tmsg.`msg_time`, '%Y-%m-%d') AS ptime"
				+ ", tmsg.`msg_link_id` AS linkid"
				+ ", tmsg.`msg_link_stat` AS linkstate, tmsg.`msg_read` AS mread, tmsg.msg_type AS type "
				+ " FROM  t_push_message tmsg WHERE tmsg.msg_userid = '"
				+ uid
				+ "' AND tmsg.`msg_del` = '1' "
				+ " ORDER BY tmsg.msg_time DESC";

		System.out.println(sql);
		ResultSet rs = stm.executeQuery(sql);
		String msg = Helper.rsToJsonRange(rs, start, end);
		System.out.println(msg);
		
		rs.close();
		stm.close();
		dbc.close();
		String res  = "{msg:" + msg + "}"; 
		return res;
	}

}
