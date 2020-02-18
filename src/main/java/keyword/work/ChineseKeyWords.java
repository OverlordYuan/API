package keyword.work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ChineseKeyWords {
	private static Logger logger = LoggerFactory.getLogger( ForeignKeyWords.class.getName());
	public static List<String> getKeyWords(String title, String content, int N){
		List<String> keywords = new ArrayList<>();
		if (content.length()>1){
			try{
				content =  content.replaceAll("[^\\u4e00-\\u9fa5.，,。？“”·—：]+", "");//去除文本中的英文及特殊提付
				if (content.length()>0) {
					TextRank.setKeywordNumber(N);//检查文本长度，避免输入为空
					keywords =  TextRank.getKeyword(title,content,1);
				}
			}catch (Exception keysError){
				logger.error("keysError:",keysError);
			}
		}
		return keywords;
	}
}
