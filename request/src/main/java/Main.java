import com.google.gson.Gson;

import java.sql.Timestamp;

class Page {
    int num;
    int size;
    int total;
    
    double getTotalPage() {
        return Math.floor(total/size) + 1;
    }
}

class Order {
    public int pub_date;
    public String score;
    public String follow;
    public String play;
}

class Anime {
    public int media_id;
    public String title;
    public String cover;
    public String index_show;
    public int season_id;
    public String link;
    public Order order;
    public String tags;
    
    public MediaInfo mediaInfo;
}

class Result {
    Anime[] data;
    Page page;
}

class Response {
    Result result;
}

class Tag {
    public int id;
    public String name;
}

class MediaInfo {
    public String actors;
    
    public String evaluate;
    
    public String origin_name;
    
    public String staff;
    
    public Tag[] style;
}

class JsonObject {
    public MediaInfo mediaInfo;
}

public class Main {
    public static Gson gson = new Gson();
    public static int requestCount = 0;
    public static Timestamp previous = new Timestamp(System.currentTimeMillis());
    
    public static Anime[] getResultData(String payload) throws Exception {
        Response res = gson.fromJson(payload, Response.class);
        return res.result.data;
    }
    
    public static Page getPageSize(String payload) throws Exception {
        Response res = gson.fromJson(payload, Response.class);
        return res.result.page;
    }
    
    public static void sleepIfCountUpToMax() {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        long offset = current.getTime() - previous.getTime();
        
        System.out.print("offset: " + offset / 1000 + ", ");
        System.out.println("request count: " + requestCount);
        
        if (offset <= 60 * 1000) {
            if (requestCount < 200) {
                return;
            } else {
                try {
                    System.out.println(" sleep...");
                    Thread.sleep(offset);
                    System.out.println("countinue!");
                } catch (InterruptedException e) {
                    System.err.println("sleep err!" + e);
                }
            }
        }
        
        // reset
        previous = current;
        requestCount = 0;
    }
    
    public static void main(String[] args) {
        DB db;
        Page page;
        Parsing parsing = Parsing.singleParsing();
        int totalPages = 0;
        try {
            db = DB.singleDB();
            String payload = Request.requestWithPage(1);
            page = getPageSize(payload);
            totalPages = (int)page.getTotalPage();
        } catch(Exception err) {
            System.err.println("ERROR: " + err);
            return;
        }
        
        System.out.println(totalPages);
        
        int offset = 5;
        
        for (int i = totalPages / 5 * offset; i <= totalPages && i < totalPages / 5 * (offset + 1) + 1; i += 1) {
            System.out.println("NO " + i + " request begin");
            try {
                String payload = Request.requestWithPage(i);
                requestCount++;
                Anime[] animes = getResultData(payload);
                
                for (Anime anime : animes) {
                    String detail = Request.requestWithId(anime.media_id);
                    requestCount++;
                    
                    try {
                        parsing.setHtml(detail);
                        anime.mediaInfo = parsing.getContentFromJS();
                    } catch(Exception err) {
                        System.err.println("prasing error : "+ err);
                        System.err.println("may use: "+ gson.toJson(anime));
                        anime.mediaInfo = null;
                    }
                }
                
                // save to db
                for (Anime anime: animes) {
                    try {
                        db.saveAnime(anime);
                    } catch(Exception err) {
                        System.err.println("save to db error: " + gson.toJson(anime));
                    }
                }
                sleepIfCountUpToMax();
            } catch (Exception err) {
                System.err.println("No " + i + " Error: " + err);
            }
            System.out.println("NO " + i + " request finish");
        }
    }
}
