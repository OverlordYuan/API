package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class txtUtils {

	public static List<String> readTxt(String txtPath) {
		File file = new File(txtPath);
		if(file.isFile() && file.exists()){
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

				List<String> sb = new ArrayList<String>();
				String text = null;
				while((text = bufferedReader.readLine()) != null){
					sb.add(text);
				}
				return sb;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
