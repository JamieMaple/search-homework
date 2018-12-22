import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.StringReader;
import java.util.Iterator;

public class Parsing {
    private String html = "";
    private Gson gson = new Gson();
    
    public static void main(String[] args) {
        System.out.println("This is test for parsing");
        Parsing parse = new Parsing();
        try {
            String html = Request.requestWithId(6339);
            parse.html = html;
            MediaInfo mediaInfo = parse.getContentFromJS();
            System.out.println(mediaInfo.evaluate);
        } catch(Exception err) {
            System.err.println(err);
        }
    }
    
    public void setHtml(String html) {
        this.html = html;
    }
    
    public static Parsing singleParsing() {
        return new Parsing();
    }
    
    public String getTags(String selector) {
        Document doc = Jsoup.parse(html);
        Iterator<String> elems = doc.select(selector).eachText().iterator();
        boolean first = true;
        StringBuilder builder = new StringBuilder();
        
        while (elems.hasNext()) {
            if (first) {
                builder.append(elems.next());
                first = false;
            } else {
                builder.append(","+elems.next());
            }
        }
        
        return builder.toString();
    }
    
    public MediaInfo getContentFromJS() throws JsonSyntaxException {
        Document doc = Jsoup.parse(html);
        Element elem = doc.selectFirst("body script");
        String data = elem.data();
        
        data = data.split("=")[1].split(";\\(function")[0].trim();
        return gson.fromJson(data, JsonObject.class).mediaInfo;
    }
}
