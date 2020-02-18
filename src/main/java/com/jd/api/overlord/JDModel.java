package com.jd.api.overlord;

import com.wxapi.WxApiCall.WxApiCall;
import com.wxapi.model.RequestModel;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JDModel {
	RequestModel model = new RequestModel();
	private String APP_KEY;
	private String SECRET_KEY;
	WxApiCall call = new WxApiCall();
	public JDModel(String APP_KEY,String SECRET_KEY) {
		this.APP_KEY = APP_KEY;
		this.SECRET_KEY = SECRET_KEY;
		model.setGwUrl("https://aiapi.jd.com/jdai/lexer");
		model.setAppkey(this.APP_KEY);
		model.setSecretKey(this.SECRET_KEY);
	}
	public JSONObject jdner(String data){
		JSONObject text = new JSONObject();
		text.put("text",data);
		text.put("type",3);
		model.setBodyStr(text.toString());	//body参数
		Map queryMap = new HashMap();
		model.setQueryParams(queryMap);
		call.setModel(model);
		JSONObject result = new JSONObject(call.request());
		return result;
	}

}
