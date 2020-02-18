package com.util;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import org.json.simple.JSONObject;


public class readfile{

	public static List read(String filepath){
		List files = new ArrayList();
			try {
				File file = new File(filepath);
				if (!file.isDirectory()){
					JSONObject file_name = get_name(file);
					files.add(file_name);
				}
				else if (file.isDirectory()) {
					String[] filelist = file.list();
					for (int i = 0;i<filelist.length; i++) {
						File readfile = new File(filepath + "\\" + filelist[i]);
						if (!readfile.isDirectory()) {
							JSONObject file_name = get_name(readfile);
							files.add(file_name);
						}

					}
				}
			} catch (Exception e) {
				System.out.println("readfile()   Exception:" + e.getMessage());
			}
			return files;
		}
    private static JSONObject get_name(File file){
		JSONObject res = new JSONObject();
		res.put("path",file.getPath());
		res.put("absolutepath",file.getAbsolutePath());
		res.put("name",file.getName());
		return res;
	}

	public static void main(String[] arg){
	readfile rf = new readfile();
	List res = rf.read("input");
	System.out.println(res);
	}
}


