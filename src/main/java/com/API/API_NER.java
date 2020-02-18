package com.API;

import cn.xsshome.taip.nlp.TAipNlp;
import com.baidu.aip.nlp.AipNlp;
import com.baidu.api.overlord.GetClient;
//import com.sun.tools.javac.util.List;
//import org.json.JSONArray;
import com.jd.api.overlord.JDModel;
import org.json.JSONArray;
import org.json.JSONObject;
//import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static jodd.util.ThreadUtil.sleep;


public class API_NER {
	private static Logger logger = LoggerFactory.getLogger(API_NER.class.getName());
	private static Properties properties = readconf();
	private static String tencent_APP_ID = properties.getProperty("tencent.APP_ID");
	private static String tencent_API_KEY =properties.getProperty("tencent.API_KEY");
	private static String baidu_APP_ID = properties.getProperty("baidu.APP_ID");
	private static String baidu_API_KEY =properties.getProperty("baidu.API_KEY");
	private static  String baidu_SECRET_KEY = properties.getProperty("baidu.SECRET_KEY");
	private static String JD_APP_KEY = properties.getProperty("JD.APP_KEY");
	private static  String JD_SECRET_KEY = properties.getProperty("JD.SECRET_KEY");

	private static AipNlp client = GetClient.getClient(baidu_APP_ID,baidu_API_KEY,baidu_SECRET_KEY);
	private static  TAipNlp aipNlp = new TAipNlp(tencent_APP_ID, tencent_API_KEY);
	private static  JDModel JDNlp = new JDModel(JD_APP_KEY, JD_SECRET_KEY);

	private static Properties readconf(){
		Properties properties = new Properties();
		try {
			properties = PropertiesLoaderUtils.loadAllProperties("application.properties");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static String NER(String text){
		String o = null;
		try{
			String B_result = baiduNER(text).toString();
			String T_result = tencentNER(text).toString();
			if (B_result.equals( T_result)){
				o = B_result;
			}
		}catch (Exception APIError){
			logger.error("APIError",APIError);
		}
		return o;
	}

	public static JSONObject baiduNER(String text) {
		JSONObject entities = new JSONObject();
		List<JSONObject> entity_list = new ArrayList<JSONObject>();
		JSONObject result = new JSONObject();
		try{
			try {
				result = client.lexer(text, null);
			}catch (Exception A){
				sleep(1000);
				result = client.lexer(text, null);
			}
			if(!result.isNull("items")){
				JSONArray data = result.getJSONArray("items");
				for (int i=0;i<data.length();i++){
					JSONObject item = (JSONObject) data.get(i);
					String label = (String) item.get("ne");
					String word = (String) item.get("item");
					JSONObject entity = generateEntity( word,label);
					if(entity.length()!=0){
						entity_list.add(entity);
					}
				}
			}
		}catch (Exception BError){
			System.out.println(BError.getMessage());
		}
		if(!entity_list.isEmpty()){
			entities.put("entities",entity_list);
		}
		return entities;
	}

	public static JSONObject tencentNER(String text) {
		JSONObject entities = new JSONObject();
		JSONObject result = new JSONObject();
		List<JSONObject> entity_list = new ArrayList<JSONObject>();
		try{
			result = new JSONObject(aipNlp.nlpWordner(text));
			if(result.getInt("ret")!=0){
				sleep(1000);
				result = new JSONObject(aipNlp.nlpWordner(text));
			}
			if(result.getInt("ret")==0){
				JSONObject data = result.getJSONObject("data");
				JSONArray NERS = data.getJSONArray("ner_tokens");
				for (int i=0;i<NERS.length();i++){
					JSONObject item = (JSONObject) NERS.get(i);
					String label = (String) item.get("types").toString();
					String word = (String) item.get("word");
					JSONObject entity = generateEntity( word,label);
					if(entity.length()!=0){
						entity_list.add(entity);
					}
				}
			}
		}catch (Exception BError){
		}
		if(!entity_list.isEmpty()){
			entities.put("entities",entity_list);
		}
		return entities;
	}

	public static JSONObject jdNER(String text) {
		JDModel JDNlp = new JDModel(JD_APP_KEY, JD_SECRET_KEY);
		JSONObject entities = new JSONObject();
		List<JSONObject> entity_list = new ArrayList<JSONObject>();
		JSONObject result = new JSONObject();
		try{
			result = JDNlp.jdner(text);
			if(!result.getString("code").equals("10000")){
				sleep(1000);
				result = JDNlp.jdner(text);
			}
			JSONArray NERS = result.getJSONObject("result").getJSONArray("tokenizedText");
			if(NERS.length()!=0){
				for (int i=0;i<NERS.length();i++){
					JSONObject item = (JSONObject) NERS.get(i);
					String label = (String) item.get("ner");
					String word = (String) item.get("word");
					JSONObject entity = generateEntity( word,label);
					if(entity.length()!=0){
						entity_list.add(entity);
					}
				}
			}
		}catch (Exception BError){
		}
		if(!entity_list.isEmpty()){
			entities.put("entities",entity_list);
		}
		return entities;
	}
	public static String NER_all(String text){
		JSONArray res = new JSONArray();
		res.put(0, baiduNER(text));
		res.put(1, tencentNER(text));
		res.put(2, jdNER(text));
		return res.toString();
	}
	private static JSONObject generateEntity(String word, String label) {
		JSONObject entity = new JSONObject();
		if(label.equals("person")|label.equals("nr")|label.equals("PER")|label.equals("[1000]")|label.equals("PERSON")){
			if ((word.substring(word.length() - 1).equals("某"))
					|| (word.substring(word.length() - 1).equals("工"))
					|| (word.substring(word.length() - 1).equals("总"))
					|| (word.substring(word.length() - 1).equals("黑"))
					|| (word.substring(word.length() - 1).equals("哥"))
					|| (word.substring(word.length() - 1).equals("姐"))
					|| (word.substring(word.length() - 1).equals("董"))
					|| (word.substring(word.length() - 1).equals("兄"))
					|| (word.substring(word.length() - 1).equals("少"))
					|| (word.substring(word.length() - 1).equals("叔"))
					|| (word.substring(word.length() - 1).equals("爷"))
					|| (word.substring(word.length() - 1).equals("弟"))
					|| (word.substring(word.length() - 1).equals("妹"))
					|| (word.substring(word.length() - 2, word.length()).equals("先生"))
					|| (word.substring(word.length() - 2, word.length()).equals("女士"))
					|| (word.substring(word.length() - 2, word.length()).equals("教授"))
					|| (word.substring(word.length() - 2, word.length()).equals("经理"))
					|| (word.substring(word.length() - 2, word.length()).equals("主任"))
					|| (word.substring(word.length() - 2, word.length()).equals("小姐"))
					|| (word.substring(word.length() - 2, word.length()).equals("老师"))
					|| (word.substring(word.length() - 2, word.length()).equals("会长"))
					|| (word.substring(word.length() - 2, word.length()).equals("大少"))
					|| (word.substring(word.length() - 2, word.length()).equals("博士"))
					|| (word.substring(word.length() - 2, word.length()).equals("硕士"))
					|| (word.substring(word.length() - 2, word.length()).equals("阿姨"))
			) {
				label = null;
			} else {
				label = "1-person";
			}
		}else if(label.equals("ORG")|label.equals("company")|label.equals("nt")|label.equals("ni")|label.equals("[1200]")){
			label = "2-organization";
		}else if(label.equals("location")|label.equals("ns")|label.equals("LOC")|label.equals("[1100]")|label.equals("GPE")){
			label = "3-location";
		}else {
			label = null;
		}
		if (null != label){
			entity.put("entity", word);
			entity.put("typeName", label);
		}
		return entity;
	}

	public static void main(String[] args) throws Exception {
		String text = "由腾讯基金会发起的中国互联网公益峰会在本月中旬于北京举行，峰会的主题是：互联网改变公益。会上陈一丹宣布，腾讯将出20亿资源助力公益生态。";
		JSONObject B_result = baiduNER(text);
		JSONObject T_result = tencentNER(text);
		JSONObject J_result = jdNER(text);
	}

}
