package maple.search.engine;

import org.ansj.lucene7.AnsjAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CommonIndex {
    private String pathString = "./index";
    protected Path path = Paths.get(pathString);
    
    protected Directory directory;
    
    protected Analyzer analyzer = new AnsjAnalyzer(AnsjAnalyzer.TYPE.index_ansj);
    
    protected IndexWriterConfig config = new IndexWriterConfig(analyzer)
                                               .setOpenMode(IndexWriterConfig.OpenMode.CREATE)
                                               .setCommitOnClose(true);
    protected IndexWriter writer;
}
