package maple.search.engine;

import maple.search.dao.AnimationIndexField;
import org.ansj.library.SynonymsLibrary;
import org.ansj.recognition.impl.SynonymsRecgnition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class AnimationSearcher extends CommonIndex {
    private static final int pageCount = 20;
    
    private Query getQuery(Stream<org.ansj.domain.Term> resultStream) {
        var builder = new BooleanQuery.Builder();
    
        resultStream.forEach(resultTerm -> {
            
            var synonymous = resultTerm.getSynonyms();
            if (synonymous != null && synonymous.size() >= 1) {
                // synonymous should have
                synonymous.forEach(s -> {
                    var term = new Term(AnimationIndexField.defaultIndex, s);
                    var subQuery = new TermQuery(term);
                    builder.add(subQuery, BooleanClause.Occur.SHOULD);
                });
            } else {
                var term = new Term(AnimationIndexField.defaultIndex, resultTerm.getName());
                var query = new TermQuery(term);
                builder.add(query, BooleanClause.Occur.MUST);
            }
        });
        System.out.println(builder.build().toString());
        
        return builder.build();
    }
    
    public Optional<List<Integer>> searchAnimationWithKeywords(Stream<org.ansj.domain.Term> resultStream, int page) throws IOException {
        try (var directory = FSDirectory.open(path);
             var reader = DirectoryReader.open(directory)
        ) {
            var searcher = new IndexSearcher(reader);
            var topDocs = searcher.search(getQuery(resultStream), pageCount * (page + 1));
            var ids = new ArrayList<Integer>();
            
            for (var i = page * pageCount; i < topDocs.scoreDocs.length; i++) {
                var topScore = topDocs.scoreDocs[i];
                var id = Integer.parseInt(searcher.doc(topScore.doc).get(AnimationIndexField.id));
                ids.add(id);
            }
            
            return Optional.of(ids);
        } catch (IOException | NumberFormatException err) {
            System.err.println(err);
            return Optional.empty();
        }
    }
    
    
    public Optional<List<Integer>> searchAnimationWithKeywords(String queryString, int page) throws IOException {
        if (queryString.trim().length() <= 0) {
            return Optional.empty();
        }
        
        var synonyms = new SynonymsRecgnition();
        var result = ToAnalysis.parse(queryString.trim()).recognition(synonyms);
        var resultStream = StreamSupport.stream(result.spliterator(), false).filter(t -> t.getName().trim() != "");
        
        return searchAnimationWithKeywords(resultStream, page);
    }
}
