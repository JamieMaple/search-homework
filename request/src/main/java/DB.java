import java.sql.*;
import java.util.Arrays;
import java.util.Optional;

public class DB {
    static final String driver = "com.mysql.cj.jdbc.Driver";
    static final String url = "jdbc:mysql://localhost:3306/search_engine";
    
    private PreparedStatement SQL;
    
    public DB() throws Exception  {
        Class.forName(DB.driver);
        Connection conn = DriverManager.getConnection(url, "maple", "password");
        SQL = conn.prepareStatement("INSERT INTO animation " +
                                            "(_id, title, cover, index_show, season_id, bili_address, pub_date, score, follow, play, tags, origin_name, intro, actors, staff)" +
                                            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
    }
    
    public static DB singleDB() throws Exception {
        return new DB();
    }
    
    public boolean saveAnime(Anime anime) throws Exception {
        SQL.setInt(1, anime.media_id);
        SQL.setString(2, anime.title);
        SQL.setString(3, anime.cover);
        SQL.setString(4, anime.index_show);
        SQL.setInt(5, anime.season_id);
        SQL.setString(6, anime.link);
        SQL.setInt(7, anime.order.pub_date);
        SQL.setString(8, anime.order.score);
        SQL.setString(9, anime.order.follow);
        SQL.setString(10, anime.order.play);
        MediaInfo mediaInfo = anime.mediaInfo;
        if (mediaInfo != null) {
            Optional<String> tags = Arrays.stream(mediaInfo.style)
                                            .map(item -> item.name)
                                            .reduce((f, s) -> f + "," + s);
            SQL.setString(11, tags.get());
            SQL.setString(12, mediaInfo.origin_name);
            SQL.setString(13, mediaInfo.evaluate);
            SQL.setString(14, mediaInfo.actors);
            SQL.setString(15, mediaInfo.staff);
        } else {
            SQL.setString(11, "");
            SQL.setString(12, "");
            SQL.setString(13, "");
            SQL.setString(14, "");
            SQL.setString(15, "");
        }
        
        return SQL.execute();
    }
    
    public static void main(String[] args) {
        System.out.println("this is DB test");
        try {
            DB db = singleDB();
            System.out.println("success");
        } catch (Exception err) {
            System.err.println("Error happend: " + err);
        }
    }
}


