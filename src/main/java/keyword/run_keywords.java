package keyword;

import com.API.keyWord;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.run_NER;
import com.util.ExcelUtils;
import com.util.csvUtils;
import com.util.readfile;
import keyword.work.ChineseKeyWords;
import keyword.work.ForeignKeyWords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class run_keywords {
		private static Logger logger = LoggerFactory.getLogger(run_keywords.class.getName());
		private static ExcelUtils ex = new ExcelUtils();
		private static csvUtils cs = new csvUtils();
		private static readfile rf = new readfile();
		private static List<String> con = new ArrayList<String>();
		private static List<String> titles_list = new ArrayList<String>();
		private static List<String> keywords = new ArrayList<String>();


		private static void save(){
			JSONObject res = new JSONObject();
			res.put("titles",titles_list);
			res.put("content",con);
			res.put("keywords",keywords);
			String[] INFO = {"title","content","keywords"};
			String outpath ="output/2肺炎（短语）.xls";
			ex.exportExcel(outpath,res,INFO);
		}

		public static void main(String[] args) throws InterruptedException {
			List<JSONObject> files = rf.read("input/data/肺炎.csv");
			int j = 0;
			long start = System.currentTimeMillis();
			int count = 0;
			for(JSONObject file:files){
				List<List<String>> content = cs.readCSV(file.get("absolutepath").toString());
				List<String> contents = content.get(0);
				List<String> titles = content.get(1);
				count += content.size();
				int i = 0;
				for(int k=0;k<contents.size();k++){
					System.out.println(i++);
//					sleep(100*3);
					String title = titles.get(k);
					String item = contents.get(k);
//					JSONObject obj =  JSONObject.parseObject(keyWord.baidukeyword(title,item));
					List obj = ChineseKeyWords.getKeyWords(title,item,5);
//					List obj = ForeignKeyWords.getKeyWords(title,item,5);
					System.out.println(obj);
					con.add(item);
					titles_list.add(title);
//					keywords.add(obj.getString("KeyWords"));
					keywords.add(obj.toString());
				}
			}
			save();
		System.out.println(count);
		long end = System.currentTimeMillis();
		logger.info("Time elapse = {} ms.",(end - start));
	}
}

