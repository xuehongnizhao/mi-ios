package com.mg.frame.mobi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


public class SmsUtil {
	
	private static Logger logger = Logger.getLogger(SmsUtil.class);
		
	public static void sendSMSMessage(String phone, String msg) throws Exception {
		Connection dbc = Helper.getConnection();
		Statement stm = dbc.createStatement();
		
		String sql = "select remarks from t_code where id='200'";
		
		ResultSet rs = stm.executeQuery(sql);
		String uri="";
		while(rs.next()){
			uri = rs.getString(1);
		}
		
		try {
			String s = MessageFormat.format(uri, new Object[] { phone, msg });
			HttpPost post = new HttpPost(s);
			HttpResponse httpResponse = new DefaultHttpClient().execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				String result = EntityUtils.toString(httpEntity);//
				System.out.println(result);
			}
			System.out.println("发送手机验证码功能, 内容为：" + msg );
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("发送手机验证码功能错误, 内容为：" + msg);
			logger.error("发送手机验证码功能错误, 内容为：" + msg , e);
		}
		
	}

	
//	public static String getShopeExamineSuccess(String code){
//		return MessageFormat.format(ProfileUtil.getString("shop.examine.success"), new Object[]{code});
//	}
	
	
}
