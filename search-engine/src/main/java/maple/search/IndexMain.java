package maple.search;

import maple.search.dao.Animation;
import maple.search.dao.AnimationIndexField;
import maple.search.dao.AnimationRepository;
import maple.search.engine.CommonIndex;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.valuesource.IntFieldSource;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
public class IndexMain extends CommonIndex implements CommandLineRunner {
    @Autowired
    private AnimationRepository animationRepository;
    
    public void addDocument(Animation animation) {
        Document doc = new Document();
        
        doc.add(new StoredField(AnimationIndexField.id, animation.getId()));
        doc.add(new TextField(AnimationIndexField.defaultIndex, animation.tags, Field.Store.YES));
        doc.add(new TextField(AnimationIndexField.defaultIndex, animation.getIntro(), Field.Store.YES));
        doc.add(new TextField(AnimationIndexField.defaultIndex, animation.getStaff(), Field.Store.YES));
        doc.add(new TextField(AnimationIndexField.defaultIndex, animation.getActors(), Field.Store.YES));
        doc.add(new TextField(AnimationIndexField.defaultIndex, animation.getTitle(), Field.Store.YES));
        
        try {
            writer.addDocument(doc);
        } catch (IOException err) {
            System.err.println(err);
        }
    }
    
    @Override
    public void run(String... args) throws Exception {
        try {
            directory = FSDirectory.open(path);
            writer = new IndexWriter(directory, config);
            animationRepository.findAll().forEach(this::addDocument);
            writer.commit();
        } catch(IOException err) {
            System.err.println(err);
            return;
        }
    }
    
    public static void main(String[] args) {
        new SpringApplicationBuilder(IndexMain.class).web(WebApplicationType.NONE).run(args);
    }
}
