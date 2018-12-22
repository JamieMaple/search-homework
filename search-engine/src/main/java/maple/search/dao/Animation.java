package maple.search.dao;


import javax.persistence.*;

@Entity
public class Animation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    public String title;

    @Column(name = "origin_name")
    public String originName;

    @Column(name = "bili_address")
    public String biliAddress;

    public String cover;
    
    @Column(name = "pub_date")
    public long pubDate;
    
    public String score;
    
    public String actors;
    
    public String intro;
    
    public String staff;
    
    public String tags;
    
    public String follow;
    
    public Integer getId() {
        return id;
    }
}

