package keyword.work;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import edu.stanford.nlp.util.StringUtils;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.chunker.ChunkerModelLoader;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextRank {
    private static final float d = 0.85f;           //damping factor, default 0.85
    private static final int max_iter = 200;        //max iteration times
    private static final float min_diff = 0.0001f;  //condition to judge whether recurse or not
    private static final float weight = 0.00f;
    private static  int nKeyword = 10;         //number of keywords to extract,default 10
    private static  int coOccuranceWindow = 3; //size of the co-occurance window, default 3

    private static Set<String> stopWords = txtUtils.readTxt("config/stopword.txt");
    private static Set<String> firstWords = txtUtils.readTxt("config/firstword.txt");

    private static  POSModel model = new POSModelLoader().load(new File("config/en-pos-maxent.bin"));
//    private static POSTaggerME tagger = new POSTaggerME(model);

    private static ChunkerModel cModel= new ChunkerModelLoader().load(new File("config/en-chunker.bin"));
//	private static ChunkerME chunkerME = new ChunkerME(cModel);

    public static void setKeywordNumber(int sysKeywordNum){
        /*设置获取的关键词数目*/
        if(sysKeywordNum<10&&sysKeywordNum>0){
            nKeyword = sysKeywordNum;
        }
    }

    public static void setWindowSize(int window) {
        /*设置TextRank的窗口 */
        coOccuranceWindow = window;
    }

    private static boolean shouldInclude(Term term){
        return (CoreStopWordDictionary.shouldInclude(term))&&(term.word.length()>1)&&(term.word.length()<10)&&(checkname(term.word));
    }

    private static boolean checkname(String name) {
        /*检查字符串是否全为中文*/
        int n;
        for(int i = 0; i < name.length(); i++) {
            n = (int)name.charAt(i);
            if(!(19968 <= n && n <40869)) {
                return false;
            }
        }
        return true;
    }

    public static List<String> getKeyword(String title, String content, int type) throws IOException {
        /*获取关键词入口*/
        Map<String, Float>  a = getKeywordWithWeight(title,content,type);
        List<Map.Entry<String, Float>> entryList = new ArrayList<Map.Entry<String, Float>>(a.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Float>>(){
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2){
                return o2.getValue().compareTo(o1.getValue());
            }});
        List<String> sysKeywordList = new ArrayList();
        for (int i = 0; i < nKeyword; ++i){
            try{
                if(entryList.get(i).getValue()>=weight){
                    sysKeywordList.add(entryList.get(i).getKey());
                }
            }catch(IndexOutOfBoundsException e){
                continue;
            }
        }
        return sysKeywordList;
    }

    private static Map<String, Float> getKeywordWithWeight(String title, String content,int type) throws IOException {
        /*
        获取前top10词，并进行权重归一化
        */
        Map<String, Float> score = TextRank.getWordScore(title, content,type);
        //title内词权重加权
        List<Map.Entry<String, Float>> entryList = new ArrayList<Map.Entry<String, Float>>(score.entrySet());
        for (Map.Entry<String, Float> obj:entryList){
            if (title.contains(obj.getKey())){
                obj.setValue(obj.getValue()*2);
            }
        }
        //候选词按权重降序重排
        Collections.sort(entryList, new Comparator<Map.Entry<String, Float>>(){
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2){
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<String, Float> sysKeywordList=new HashMap<>();
        Float sum = 0f;
        for (int i = 0; i < 10; ++i){
            try{
                sum += entryList.get(i).getValue();
            }catch(IndexOutOfBoundsException e){
                continue;
            }
        }

        for (int i = 0; i < 10; ++i){
            try{
                sysKeywordList.put(entryList.get(i).getKey(),entryList.get(i).getValue()/sum);
            }catch(IndexOutOfBoundsException e){
                continue;
            }
        }
        return sysKeywordList;
    }

    private static Map<String, Float> getWordScore(String title, String content, int type) throws IOException {
        /*
        关键词权重计算
         */
        List<String> wordList=new ArrayList<String>();
        //中文关键词候选列表
        if(type==1){
            List<Term> termList = HanLP.segment(title + content);
//            List<Term> termList = Chinesechunk(title + content);
            for (Term t : termList){
                if (shouldInclude(t)){
                    if(t.nature.toString().substring(0,1).equals("n")
                            ||t.nature.toString().equals("j")
                            ||t.nature.toString().equals("vn")){
                        wordList.add(t.word);
                    }
                }
            }
        }else {
            //英文文关键词候选列表
            List<String> termList = null;
            try {
                termList = chunk(title + "."+content);
                for (String t : termList){
                    String[] item = t.split("_");
                    if (!stopWords.contains(item[0])&&item[0].length()>1){
                        if(item[1].equals("NP")){
                            wordList.add(item[0]);}
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ExtractTextRank(wordList);
    }

    private static List<String> chunk(String str) throws IOException {
        /*
        英文短语抽取
         */
        ObjectStream<String> lineStream = new PlainTextByLineStream(new StringReader(str));
        POSTaggerME tagger = new POSTaggerME(model);
        ChunkerME chunkerME = new ChunkerME(cModel);
        String line;
        String[] whitespaceTokenizerLine;
        String[] tags;
        List<String> result = new ArrayList<>();
        while ((line = lineStream.read()) != null) {
//			whitespaceTokenizerLine = line.split("[\\\"|\\?|\\!|\\.|\\@|\\,|\\:|\\(|\\)|\\#|\\“|\\s]+");
            whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE.tokenize(line.replaceAll("[\\\"|\\?|\\!|\\.|\\@|\\,|\\:|\\(|\\)|\\#|\\“|\\s]+"," "));
            tags = tagger.tag(whitespaceTokenizerLine);
            Span[] span = chunkerME.chunkAsSpans(whitespaceTokenizerLine, tags);
            for (Span s : span){
                List<String> temp = new ArrayList<>();
                if(!tags[s.getStart()].equals("POS")&&!firstWords.contains(whitespaceTokenizerLine[s.getStart()])){
                    temp.add(whitespaceTokenizerLine[s.getStart()].replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5']+",""));
                }
                for(int j=s.getStart()+1;j<s.getEnd();j++){
                    temp.add(whitespaceTokenizerLine[j].replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5']+",""));
                }
                result.add(StringUtils.join(temp.toArray()," ")+"_"+s.getType());
            }
        }
        return result;
    }

    private static List<Term> Chinesechunk(String str) throws IOException {
        /*
        中文短语抽取
         */
        Pattern r = Pattern.compile("a*n+");
        List<String> result = new ArrayList<>();
        List<Term> termList = HanLP.segment(str);
        String label = "";
        for (Term t : termList){
            label += t.nature.toString().substring(0,1).replace("j","n");
        }
        Matcher m = r.matcher(label);
        while (m.find()) {
//			System.out.println(termList.subList(m.start(),m.end()))
            termList.get(m.start()).nature =  Nature.fromString("n");
            for(int i = m.start()+1;i<m.end();i++){
                termList.get(m.start()).word += termList.get(i).word;
                termList.get(i).nature = Nature.fromString("s");
            }
        }
        return termList;
    }

    private static Map<String,Float> ExtractTextRank(List<String> wordList){
    	/*
    	使用TextRank计算词权重
    	 */
        Map<String, Set<String>> words = new HashMap<String, Set<String>>();
        Queue<String> que = new LinkedList<String>();
        for (String w : wordList) {
            if (!words.containsKey(w)) {
                words.put(w, new HashSet<String>());
            }
            que.offer(w);    // insert into the end of the queue
            if (que.size() > coOccuranceWindow){
                que.poll();  // pop from the queue
            }
            for (String w1 : que)
                for (String w2 : que){
                    if (w1.equals(w2)){
                        continue;
                    }
                    words.get(w1).add(w2);
                    words.get(w2).add(w1);
                }
        }
        Map<String, Float> score = new HashMap<String, Float>();
        for (int i = 0; i < max_iter; ++i){
            Map<String, Float> m = new HashMap<String, Float>();
            float max_diff = 0;
            for (Map.Entry<String, Set<String>> entry : words.entrySet()){
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                m.put(key, 1 - d);
                for (String other : value){
                    int size = words.get(other).size();
                    if (key.equals(other) || size == 0) continue;
                    m.put(key, m.get(key) + d / size * (score.get(other) == null ? 0 : score.get(other)));
                }
                max_diff = Math.max(max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 1 : score.get(key))));
            }
            score = m;
            if (max_diff <= min_diff) break;
        }
        return score;
    }
}