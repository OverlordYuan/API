//
//import com.API.API_NER;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.util.ExcelUtils;
//import com.util.readfile;
//import com.util.csvUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static java.lang.Thread.*;
//
//public class run_local {
//	private static Logger logger = LoggerFactory.getLogger(run_local.class.getName());
//	public static void main(String[] args) throws InterruptedException {
//
//		ExcelUtils ex = new ExcelUtils();
//		csvUtils cs = new csvUtils();
//		readfile rf = new readfile();
//
//		List<JSONObject> files = rf.read("input");
//		int j = 0;
//		long start = System.currentTimeMillis();
//		int count = 0;
//		for(JSONObject file:files){
////			System.out.println("正在处理第"+j+"/"+files.size()+"个文件。");
//			List<String> content = cs.readCSV(file.get("absolutepath").toString());
//			System.out.println(String.format("%s类文本共有%s条样本。",file.getString("name"),content.size() ));
//			count += content.size();
////			List<String> con = new ArrayList<String>();
////			List<String> person = new ArrayList<String>();
////			List<String> organization = new ArrayList<String>();
////			List<String> location = new ArrayList<String>();
////			JSONObject o = new JSONObject();
////			JSONObject res = new JSONObject();
////			int i = 0;
////			for(String item:content){
////				System.out.println(i++);
//				sleep(10*3);
////				JSONObject obj =  JSONObject.parseObject(API_NER.NER(item));
////				if(obj !=null){
////					JSONArray entitysArray = obj.getJSONArray("entities");
////
////					List<String> PER = new ArrayList<String>();
////					List<String> ORG = new ArrayList<String>();
////					List<String> LOC = new ArrayList<String>();
////					if(entitysArray!=null){
////						for (Object entity_item : entitysArray) {
////	//					for (int k=0;k<entitysArray.length();k++) {
////							JSONObject entity = (JSONObject)entity_item;
////							if(!entity.isEmpty()){
////								if(entity.get("typeName").toString().equals("1-person")){
////									PER.add(entity.get("entity").toString());
////								}else if(entity.get("typeName").toString().equals("2-organization")){
////									ORG.add(entity.get("entity").toString());
////								}else {
////									LOC.add(entity.get("entity").toString());
////								}
////							}
////						}
////						con.add(item);
////						person.add(PER.toString());
////						organization.add(ORG.toString());
////						location.add(LOC.toString());
////					}
////				}
////			}
////			res.put("content",con);
////			res.put("person",person);
////			res.put("organization",organization);
////			res.put("location",location);
////			String[] INFO = {"content","person", "organization", "location"};
////			String outpath ="output/"+file.getString("name")+".xls";
////			j += 1;
////			ex.exportExcel(outpath,res,INFO);
//		}
//		System.out.println(count);
//		long end = System.currentTimeMillis();
//		logger.info("Time elapse = {} ms.",(end - start));
//	}
//}
