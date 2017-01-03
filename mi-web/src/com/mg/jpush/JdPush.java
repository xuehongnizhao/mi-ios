package com.mg.jpush;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

public class JdPush {
	 protected static final Logger LOG = LoggerFactory.getLogger(JdPush.class);

	 // demo App defined in resources/jpush-api.conf 

	public static final String TITLE = "";
    public static final String ALERT = "";
    
    public  static JPushClient jpushClient=null;
    
    private static final String appKey ="73d6c2d5efceaa7b08ce10f1";
	private static final String masterSecret = "72568ff438c80014c7a6e85e";
	
	/**
	 * 广播发送消息
	 * 
	 * @param title
	 */
	public static void sendPushAll(String title){
		 	jpushClient = new JPushClient(masterSecret, appKey, 3);
			
			 //生成推送的内容，这里我们先测试全部推送
	        PushPayload payload=buildPushObject_all_alias_alert(title);
	        
	        
	        try {
	        	System.out.println(payload.toString());
	            PushResult result = jpushClient.sendPush(payload);
	            System.out.println(result+"................................");
	            
	            LOG.info("Got result - " + result);
	            
	        } catch (APIConnectionException e) {
	            LOG.error("Connection error. Should retry later. ", e);
	            
	        } catch (APIRequestException e) {
	            LOG.error("Error response from JPush server. Should review and fix it. ", e);
	            LOG.info("HTTP Status: " + e.getStatus());
	            LOG.info("Error Code: " + e.getErrorCode());
	            LOG.info("Error Message: " + e.getErrorMessage());
	            LOG.info("Msg ID: " + e.getMsgId());
	        }
	}
	/**
	 * 指定用户
	 * @param aliases
	 * @param title
	 */
	public static void sendPushSelect(Set<String> registrationIds, String title){
		jpushClient = new JPushClient(masterSecret, appKey, 3);
		
		 //生成推送的内容，这里我们先测试全部推送
       PushPayload payload=buildPushObject_select_alias_alert(registrationIds,title);
       
       
       try {
       	System.out.println(payload.toString());
           PushResult result = jpushClient.sendPush(payload);
           System.out.println(result+"................................");
           
           LOG.info("Got result - " + result);
           
       } catch (APIConnectionException e) {
           LOG.error("Connection error. Should retry later. ", e);
           
       } catch (APIRequestException e) {
           LOG.error("Error response from JPush server. Should review and fix it. ", e);
           LOG.info("HTTP Status: " + e.getStatus());
           LOG.info("Error Code: " + e.getErrorCode());
           LOG.info("Error Message: " + e.getErrorMessage());
           LOG.info("Msg ID: " + e.getMsgId());
       }
	}
	
	/**
	 * 所有用户广播推送
	 * @return
	 */
    public static PushPayload buildPushObject_all_alias_alert(String title) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())//设置接受的平台
                .setAudience(Audience.all())//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
                .setNotification(Notification.alert(title))
                .build();
    }
    
    /**
	 * 指定用户广播推送
	 * @return
	 */
    public static PushPayload buildPushObject_select_alias_alert(Set<String> registrationIds,String title) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())//设置接受的平台
                .setAudience(Audience.registrationId(registrationIds))//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
                .setNotification(Notification.alert(title))
                .build();
    }
    
    
}