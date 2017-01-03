import java.net.URLEncoder;
import java.security.KeyFactory;  
import java.security.PrivateKey;  
import java.security.PublicKey;  
import java.security.spec.PKCS8EncodedKeySpec;  
import java.security.spec.X509EncodedKeySpec;  
import java.util.Base64;
  
public class test {

	public static void main(String[] args) throws Exception {
	
		
		String kstr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALwiQSoAK6jmKomVLkzKPU0RVYc74UoAj0HGxxmkh38BvD/I9OdIPuMVV+ZJHe/fjtR+yzxtihu1gpVkOGi01blmEdxMQ89ziPBZb0ngLFHq+8iaQEEjg06GzWjOO1uk/iAEulpsXixHWUCHdoHNa6rIR7UTGe6IGkXaklpDOwr/AgMBAAECgYEAhmL5pWTlywpOzGSR5Cx6yXheXfF6JoBr1JKHWlvapfC5Zd7IggVrVo7CcS89gUFyJ3xC9PFSPCTWGJy0Zqb2GvSEH9HGgGULUuWmiXAmp++G8S2sk/7IqLMDxZkTVHgE/LPqNgwl6PvSKXSTEKqa2YgsJQNU5HTJgT1y4CKLKyECQQDvbG2ZWzjJAAfVvx0tB8EoHW/fLheSLKi18/iXHNgPsjCCEY6DV5KbcYz0bMd5ki+9mlk45PbTxZCuUAHcYFknAkEAySjCyEQ9I6EzDhNrx7FYQptSSRlOVIPJgTRMetXc18CQDyOKowpxogtvr1yZ0ysq5sXqpbtwHElM5TDIbn32aQJBAKqXr1/dx5AwHWUHT1qOc+qJTvTOeq0EdRFwTGgffTNx225R5CFhfGyVc8GU4GyW5L1MBNatDdHGq1gIWcsUO3ECQC/A82yjdngs3nfq+F0xpqg5QzGDRmsd1gpMJhPLDWBSoGBXqaPG6O71FBBnRUUyLD8YoPmzI4wzgoOuLwa79HECQBGiI98XBrXfuFUhWUroPXeiLlApIhGr+Ao+GeeRtvGelQ4rxcygiiT/2W71bmNY0x66R3Tz80KH7p7cqQiyzpU=";
		
		String str ="partner=\"2088021108211374\"&seller_id=\"hljzztech@163.com\"&out_trade_no=\"D7E2ICC89SJFZB5\"&subject=\"1\""
				+ "&body=\"我是测试数据\"&total_fee=\"0.02\"&notify_url=\"http://www.xxx.com\"&service=\"mobile.securitypay.pay\""
				+ "&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&show_url=\"m.alipay.com\"";
		
		String sign="VYN458e4XVcP3Ly9bRdZk4lnBBjKqsYvgobEWlwWKMfE"
				+ "uWmNmvZbxfKoHRHo00CAqlnJiyR8WJGfx83cBHdni2SIge"
				+ "oj8z8%2FWJYwXyr1U5No%2FsSlVRnQBxmN%2FSIsjfKHA6"
				+ "r5s%2BMIb60L8dyljp5OFdNQ%2BGqzcMdjMjbY1bIODsk%3D";
		
		PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec( Base64.getDecoder().decode(kstr));   
        
        KeyFactory keyf                 = KeyFactory.getInstance("RSA");  
        PrivateKey priKey               = keyf.generatePrivate(priPKCS8);  

        java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");  

        signature.initSign(priKey);  
        
        signature.update( str.getBytes("utf-8"));  

        byte[] signed = signature.sign();  
        
        String enstr=new String(signed,"UTF-8");
        
        System.out.println(enstr);
        
        System.out.println("----------------");
        
        byte[] xx= Base64.getEncoder().encode(signed);
        
        String xxs = new String(xx,"utf-8");
        
        String uestr = URLEncoder.encode(xxs,"utf-8");
        
        System.out.println(uestr);
        
        if(uestr.equals(sign)){
        	System.out.println("success");
        }else{
        	System.out.println("fail");
        }
        
        
	}

}
