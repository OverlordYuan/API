package com.API;
import cn.xsshome.taip.nlp.TAipNlp;
import com.baidu.aip.nlp.AipNlp;
import com.baidu.api.overlord.GetClient;

import org.json.JSONArray;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static jodd.util.ThreadUtil.sleep;

public class keyWord {
		private static Logger logger = LoggerFactory.getLogger(com.API.keyWord.class.getName());
		private static Properties properties = readconf();
//		private static String tencent_APP_ID = properties.getProperty("tencent.APP_ID");
//		private static String tencent_API_KEY =properties.getProperty("tencent.API_KEY");
		private static String baidu_APP_ID = properties.getProperty("baidu.APP_ID");
		private static String baidu_API_KEY =properties.getProperty("baidu.API_KEY");
		private static  String baidu_SECRET_KEY = properties.getProperty("baidu.SECRET_KEY");
//		private static String JD_APP_KEY = properties.getProperty("JD.APP_KEY");
//		private static  String JD_SECRET_KEY = properties.getProperty("JD.SECRET_KEY");

		private static AipNlp client = GetClient.getClient(baidu_APP_ID,baidu_API_KEY,baidu_SECRET_KEY);
//		private static TAipNlp aipNlp = new TAipNlp(tencent_APP_ID, tencent_API_KEY);
//		private static  JDModel JDNlp = new JDModel(JD_APP_KEY, JD_SECRET_KEY);

		private static Properties readconf(){
			Properties properties = new Properties();
			try {
				properties = PropertiesLoaderUtils.loadAllProperties("application.properties");

			} catch (IOException e) {
				e.printStackTrace();
			}
			return properties;
		}

		public static String baidukeyword(String title,String text) {
			JSONObject entities = new JSONObject();
			List<String> KeyWords = new ArrayList<String>();
			JSONObject result = new JSONObject();
			try{
				try {
					result = client.keyword(title,text,null);
				}catch (Exception A){
					sleep(1000);
					result = client.keyword(title,text,null);
				}
				if(!result.isNull("items")){
					JSONArray data = result.getJSONArray("items");
					int num = 5;
					if(num>data.length()){num = data.length();}

					for (int i=0;i<num;i++){
						JSONObject item = (JSONObject) data.get(i);
						KeyWords.add(item.get("tag").toString());
					}
				}
			}catch (Exception BError){
				System.out.println(BError.getMessage());
			}
			if(!KeyWords.isEmpty()){
				entities.put("KeyWords",KeyWords);
			}
			return entities.toString();
		}

		public static void main(String[] args) throws Exception {
			String title = "由腾讯基金会发起的中国互联网公益峰会在本月中旬于北京举行";
			String text = "由腾讯基金会发起的中国互联网公益峰会在本月中旬于北京举行，峰会的主题是：互联网改变公益。会上陈一丹宣布，腾讯将出20亿资源助力公益生态。";
			String B_result = baidukeyword(title,text);
			System.out.println(B_result);
		}

	}

