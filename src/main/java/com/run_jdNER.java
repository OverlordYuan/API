//package com;
//
//import com.API.API_NER;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.util.ExcelUtils;
//import com.util.csvUtils;
//import com.util.readfile;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import static java.lang.Thread.sleep;
//
//public class run_jdNER {
//	private static Logger logger = LoggerFactory.getLogger(run_NER.class.getName());
//	private static ExcelUtils ex = new ExcelUtils();
//	private static csvUtils cs = new csvUtils();
//	private static readfile rf = new readfile();
//	private static List<String> con = new ArrayList<String>();
//	private static List<String> b_person = new ArrayList<String>();
//	private static List<String> t_person = new ArrayList<String>();
//	private static List<String> j_person = new ArrayList<String>();
//	private static List<String> b_organization = new ArrayList<String>();
//	private static List<String> t_organization = new ArrayList<String>();
//	private static List<String> j_organization = new ArrayList<String>();
//	private static List<String> b_location = new ArrayList<String>();
//	private static List<String> t_location = new ArrayList<String>();
//	private static List<String> j_location = new ArrayList<String>();
//
//	private static void Analysis(Object item){
//		JSONObject obj = (JSONObject) item;
//		JSONArray entitysArray = obj.getJSONArray("entities");
//		Set<String> PER = new HashSet<String>();
//		Set<String> ORG = new HashSet<String>();
//		Set<String> LOC = new HashSet<String>();
//		if(entitysArray!=null){
//			for (Object entity_item : entitysArray) {
//				JSONObject entity = (JSONObject)entity_item;
//				if(!entity.isEmpty()){
//					if(entity.get("typeName").toString().equals("1-person")){
//						PER.add(entity.get("entity").toString());
//					}else if(entity.get("typeName").toString().equals("2-organization")){
//						ORG.add(entity.get("entity").toString());
//					}else {
//						LOC.add(entity.get("entity").toString());
//					}
//				}
//			}
//		}
//		j_person.add(PER.toString());
//		j_organization.add(ORG.toString());
//		j_location.add(LOC.toString());
//
//	}
//	private static void save(){
////		String[] names ={"baidu","tencent","jd"};
//		JSONObject res = new JSONObject();
//		res.put("content",con);
//		res.put("person",j_person);
//		res.put("organization",j_organization);
//		res.put("location",j_location);
//		String[] INFO = {"content","person", "organization", "location"};
//		String outpath ="output/jd2.xls";
//		ex.exportExcel(outpath,res,INFO);
//	}
//
//	public static void main(String[] args) throws InterruptedException {
//
//		List<JSONObject> files = rf.read("input/data");
//		int j = 0;
//		long start = System.currentTimeMillis();
//		int count = 0;
//		for(JSONObject file:files){
//			List<String> content = cs.readCSV(file.get("absolutepath").toString());
//			count += content.size();
//			int i = 0;
//			for(String item:content){
//				System.out.println(i++);
//				sleep(10*3);
//				if(i>10000){
//					JSONObject obj =  JSONObject.parseObject(API_NER.jdNER(item).toString());
//					Analysis(obj);
//					con.add(item);
//				}
//			}
//			save();
//		}
//		System.out.println(count);
//		long end = System.currentTimeMillis();
//		logger.info("Time elapse = {} ms.",(end - start));
//	}
//}
