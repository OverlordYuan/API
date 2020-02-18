package com.baidu.api.overlord;

import com.baidu.aip.nlp.AipNlp;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;

import java.util.LinkedList;
import java.util.List;

public class baidu_ai_NER {
	public static final String APP_ID = "17975953";
	public static final String API_KEY = "MzGxXlOgvlQ021nIyQCkAyke";
	public static final String SECRET_KEY = "HSVCEEWzCwINbLh0RoLxhHFh3MCqMKKV";


	public static void main(String args[]){

		AipNlp client = GetClient.getClient(APP_ID,API_KEY,SECRET_KEY);
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);

		String title = "100吨！钦州海关缉私分局现场查获涉嫌走私冻肉";
		String text = "019-11-4-钦州海关缉私分局现场查获涉嫌走私冻肉100吨 点击边框调出视频工具条 点击观看视频 11月1日凌晨3时许，钦州海关缉私分局在钦州市钦州港新基围村沙场码头附近查获一起涉嫌走私冻品案，现场查扣冻鸡爪、冻牛百叶、冻牛筋等约100吨，案值约100万元。 据办案人员介绍，当日凌晨，钦州海关缉私分局成功抓获正在搬运冻品的涉案人员3名，查扣涉案货车4辆、铁壳船1艘、木船2艘。涉案运输工具上满载各类冻品，有冻鸡爪、冻牛百叶、冻牛筋等，均无合法的进境手续。经初步调查查明，该批冻品共计约100吨，其外文包装上均为外文标识，显示为巴拉圭产冻肉产品，系从越南通过海上偷运进境，企图销往内地非法牟利。目前，案件正在进一步办理中。";
		JSONObject res = client.keyword(title,text,null);
		System.out.println(res.toString(2));

	}
}
