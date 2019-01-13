package maple.search.dao;


import javax.persistence.*;

@Entity
public class Animation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    private String title;

    @Column(name = "origin_name")
    public String originName;

    @Column(name = "bili_address")
    public String biliAddress;

    public String cover;
    
    @Column(name = "pub_date")
    public long pubDate;
    
    public String score;
    
    private String actors;
    
    private String intro;
    
    private String staff;
    
    public String tags;
    
    public String follow;
    
    public Integer getId() {
        return id;
    }
    
    public String getActors() {
        return actors;
    }
    
    public void setActors(String actors) {
        this.actors = actors;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setStaff(String staff) {
        this.staff = staff;
    }
    
    public String getStaff() {
        return staff;
    }
    
    
    public void setIntro(String intro) {
        this.intro = intro;
    }
    
    public String getIntro() {
        return intro;
    }
}

