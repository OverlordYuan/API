import cn.xsshome.taip.nlp.TAipNlp;
import com.API.API_NER;
import com.jd.api.overlord.JDModel;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

//import static com.API.API_NER.readconf;

public class Sample {
	private static Logger logger = LoggerFactory.getLogger(API_NER.class.getName());
	private static Properties properties = readconf();
	private static Properties readconf(){
		Properties properties = new Properties();
		try {
			properties = PropertiesLoaderUtils.loadAllProperties("application.properties");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	public static final String APP_ID = "2123671795";
	public static final String APP_KEY = "NFcVHWLaQL7TUVop";
	private static String JD_APP_KEY = properties.getProperty("JD.APP_KEY");
	private static  String JD_SECRET_KEY = properties.getProperty("JD.SECRET_KEY");

	public static void main(String[] args) throws Exception {
		JDModel MODEL = new JDModel(JD_APP_KEY,JD_SECRET_KEY);
		String text = "霍金在物理学界扮演着先行者和启发者的角色，“如果他是一位导演，我们都欠他一张电影票。”";
		JSONObject o = MODEL.jdner(text);
		System.out.println(o);
	}
}
