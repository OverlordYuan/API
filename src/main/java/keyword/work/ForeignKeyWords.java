package keyword.work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ForeignKeyWords {
	public ForeignKeyWords(){}
	private static Logger logger = LoggerFactory.getLogger( ForeignKeyWords.class.getName());

	public static List<String> getKeyWords(String title, String content, int N){
		List<String> keywords = new ArrayList<>();
		title = title.replaceAll("’","'");
		content = content.replaceAll("’","'").replaceAll("‘","");
		if (content.length()>1){
			try{
				TextRank.setKeywordNumber(N);//检查文本长度，避免输入为空
				keywords =  TextRank.getKeyword(title,content,2);
			}catch (Exception keysError){
				logger.error("keysError:",keysError);
			}
		}
		return keywords;
	}
	public static void main(String[] args) throws Exception {
		String title = "Apple's new iPhone series sees surge in advance orders";
		String content = "As one of the major online channels for the pre-sales of Apple's new iPhone 11 series, the Apple flagship store of China's e-commerce giant JD.com on Sunday showed many products in the series have been booked up. JD.com's report showed that the number of pre-orders surged 480 percent year-on-year.\n" +
				"\n" +
				"Global Times reporters found that among the six colors of the iPhone 11 series, yellow, purple and green had been sold out on JD.com as of Sunday afternoon, though there were more than four days of pre-sales left.\n" +
				"\n" +
				"According to the report of JD.com on Saturday, the new series of iPhones attracted more than 16 million online customers' attention. Its first order was completed within one second, and the iPhone 11 Pro series was fully ordered in less than five minutes.\n" +
				"\n" +
				"In the first minute of pre-sales on JD.com, there were orders placed from 341 cities across China, said the report, adding nearly 60 percent of customers were aged from 16 to 29.\n" +
				"\n" +
				"Started from 8 pm on Friday, millions of customers visited Alibaba's Tmall in the first night of pre-sales with orders of more than 100 million yuan ($14.13 million) within one minute, according to huanqiu.com, citing data from Tmall.\n" +
				"\n" +
				"Though the pre-sales performed well, analysts noted the overall sales of the new iPhone line may not see such a massive increase ultimately.\n" +
				"\n" +
				"\"Strong demand always shows up in the secondhand market when customers are willing to pay higher prices, instead of the pre-sale period,\" Ma Jihua, a veteran industry analyst, told the Global Times on Sunday.\n" +
				"\n" +
				"Ma pointed out that digital product customers tend to try new technologies. Work on China's 5G sector is accelerating and this will soon be an attraction for customers.\n" +
				"\n" +
				"\"There is actually nothing that attracts me about the new iPhone,\" a Beijing-based customer surnamed Liu who ordered an iPhone 11, told the Global Times. \"The main reason is that all my online game accounts were set in Apple's iOS systems, and I am paying attention to Huawei's new series to see if there are new technology applications.\"";

		List<String> r = getKeyWords(title,content,10);
		System.out.println(r);
	}
}

