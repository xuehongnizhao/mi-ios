package com.zz.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

public class Alipay extends CordovaPlugin {
	private Context context;
	private CallbackContext callbackContext;

	public final static int SDK_PAY_FLAG = 1;

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) {
		this.callbackContext = callbackContext;
		this.context = cordova.getActivity().getApplicationContext();
		if(action.equals("mipay")){
		try {
			final String payInfo = args.optString(0);
			Runnable payRunnable = new Runnable() {

				public void run() {
					// 构造PayTask 对象
					PayTask alipay = new PayTask(cordova.getActivity());
					// 调用支付接口，获取支付结果
					String result = alipay.pay(payInfo);
					
					Message msg = new Message();
					msg.what = SDK_PAY_FLAG;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			};

			// 必须异步调用
			Thread payThread = new Thread(payRunnable);
			payThread.start();

		} catch (Exception e) {
			this.callbackContext.error("抛出异常");
		}
		return true;
		}
		return false;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				String temp = "success=\"true\"";
				if (TextUtils.equals(payResult.getResultStatus(), "9000") && payResult.getResult().contains(temp)) {
					callbackContext.success("succ");
				}else{
					callbackContext.success("fail");
				}
				break;
			}
			default:
				break;
			}
		};
	};
	
	public class PayResult {
		private String resultStatus;
		private String result;
		private String memo;

		public PayResult(String rawResult) {

			if (TextUtils.isEmpty(rawResult))
				return;

			String[] resultParams = rawResult.split(";");
			for (String resultParam : resultParams) {
				if (resultParam.startsWith("resultStatus")) {
					resultStatus = gatValue(resultParam, "resultStatus");
				}
				if (resultParam.startsWith("result")) {
					result = gatValue(resultParam, "result");
				}
				if (resultParam.startsWith("memo")) {
					memo = gatValue(resultParam, "memo");
				}
			}
		}

		@Override
		public String toString() {
			return "resultStatus={" + resultStatus + "};memo={" + memo
					+ "};result={" + result + "}";
		}

		private String gatValue(String content, String key) {
			String prefix = key + "={";
			return content.substring(content.indexOf(prefix) + prefix.length(),
					content.lastIndexOf("}"));
		}

		/**
		 * @return the resultStatus
		 */
		public String getResultStatus() {
			return resultStatus;
		}

		/**
		 * @return the memo
		 */
		public String getMemo() {
			return memo;
		}

		/**
		 * @return the result
		 */
		public String getResult() {
			return result;
		}
	}
}
