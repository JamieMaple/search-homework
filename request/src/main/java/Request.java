import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Request {
    private static final String USER_AGENT = "Mozilla/5.0";
    
    public static void main(String[] args) {
        System.out.println("this is get request test");
        String url = urlWithPage(0);
        
        try {
            String data = get(url);
            System.out.println(data);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static String requestWithPage(int page) throws Exception {
        return get(urlWithPage(page));
    }
    
    public static String requestWithId(int id) throws Exception {
        return get(urlWithPageDetail(id));
    }
    
    private static String urlWithPage(int page) {
        return String.format("https://bangumi.bilibili.com/media/web_api/search/result?season_version=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&pub_date=-1&style_id=-1&order=3&st=1&sort=0&page=%d&season_type=1&pagesize=20", page);
    }
    
    private static String urlWithPageDetail(int id) {
        return String.format("https://www.bilibili.com/bangumi/media/md%d/", id);
    }
    
    private static String setUrlPrefix(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        } else {
            return "http://" + url;
        }
    }
    
    public static String get(String url) throws Exception {
        URL urlObj = new URL(
                Request.setUrlPrefix(url)
        );
        HttpURLConnection con = (HttpURLConnection)urlObj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        
        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();
        
        while ((inputLine = in.readLine()) != null) {
            stringBuilder.append(inputLine);
        }
        in.close();
        return stringBuilder.toString();
    }
}

