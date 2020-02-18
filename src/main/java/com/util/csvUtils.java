package com.util;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class csvUtils {
	public static char separator = ',';

	public static void main(String[] args) throws Exception {
		// 测试导出
		String filePath = "input/keywords.csv";
		List<List<String>> dataList = new ArrayList<List<String>>();
		dataList = readCSV(filePath);
		System.out.println(1);
	}

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
	return dest;
}

	/**
	 * 读取CSV文件
	 * @param filePath:全路径名
	 */
	public static List<List<String>> readCSV(String filePath){
		CsvReader reader = null;
		List<String> dataList = new ArrayList<String>();
		List<String> titleList = new ArrayList<String>();
		List<List<String>> aaList = new ArrayList<List<String>>();
		try {
			//如果生产文件乱码，windows下用gbk，linux用UTF-8
			reader = new CsvReader(filePath, separator, Charset.forName("UTF-8"));

			// 读取表头
			reader.readHeaders();
			String[] headArray = reader.getHeaders();//获取标题
			// 逐条读取记录，直至读完
			while (reader.readRecord()) {
				String obj = reader.get("content");
				String title = reader.get("title");
//				obj = obj.replaceAll("\\s*", "");
//				title = title.replaceAll("\\s*", "");
				if(obj.length()>10){
					dataList.add(obj);
					titleList.add(title);
				}
//				dataList.add(reader.get(headArray[2]));
//				System.out.println(reader.get(headArray[2]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				reader.close();
			}
		}
		aaList.add(dataList);
		aaList.add(titleList);
		return aaList;
	}

	/**
	 * 生成CSV文件
	 * @param dataList:数据集
	 * @param filePath:全路径名
	 */
	public static boolean createCSV(List<String[]> dataList, String filePath) throws Exception {
		boolean isSuccess = false;
		CsvWriter writer = null;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath, true);
			//如果生产文件乱码，windows下用gbk，linux用UTF-8
			writer = new CsvWriter(out, separator, Charset.forName("GBK"));
			for (String[] strs : dataList) {
				writer.writeRecord(strs);
			}
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				writer.close();
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return isSuccess;
	}
}
