package com.baidu.api.overlord;

import com.baidu.aip.nlp.AipNlp;


public class GetClient {

	private static AipNlp client = null;

	private GetClient() {
	};

	public static AipNlp getClient(String APP_ID, String API_KEY, String SECRET_KEY) {
		if (client == null) {
			client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
			return client;
		}
		return client;
	}
}

