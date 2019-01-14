package maple.search.engine;

import maple.search.dao.Animation;
import maple.search.dao.AnimationIndexField;
import org.ansj.library.SynonymsLibrary;
import org.ansj.recognition.impl.SynonymsRecgnition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class AnimationSearcher extends CommonIndex {
    private static final int pageCount = 100;
    
    private SynonymsRecgnition synonyms = new SynonymsRecgnition();
    
    private Formatter formatter = new SimpleHTMLFormatter("<i class='highlight-keywords'>", "</i>");
    
    private Stream<org.ansj.domain.Term> getQueryResultStream(String queryString) {
        var result = ToAnalysis.parse(queryString.trim()).recognition(synonyms);
        var resultStream = StreamSupport.stream(result.spliterator(), false).filter(t -> t.getName().trim() != "");
        return resultStream;
    }
    
    private Query getQuery(String queryString) {
        return getQuery(getQueryResultStream(queryString));
    }
    
    private Query getSimpleQuery(String queryString) {
        var resultStream = getQueryResultStream(queryString);
        var builder = new BooleanQuery.Builder();
        resultStream.forEach(resultTerm -> {
            var term = new Term(AnimationIndexField.defaultIndex, resultTerm.getName());
            var query = new TermQuery(term);
            builder.add(query, BooleanClause.Occur.SHOULD);
        });
        return builder.build();
    }
    
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
        
        return builder.build();
    }
    
    public Animation highlightResult(String queryString, Animation animation) throws InvalidTokenOffsetsException, IOException {
        var query = getSimpleQuery(queryString);
        var scorer = new QueryScorer(query);
        var fragmenter = new SimpleSpanFragmenter(scorer);
        var highlighter = new Highlighter(formatter, scorer);
        highlighter.setTextFragmenter(fragmenter);
        
        var title = highlighter.getBestFragment(analyzer, AnimationIndexField.defaultIndex, animation.getTitle());
        if (title != null) {
            animation.setTitle(title);
        }
        
        var intro = highlighter.getBestFragment(analyzer, AnimationIndexField.defaultIndex, animation.getIntro());
        if (intro != null) {
            animation.setIntro(intro);
        }
        
        var staff = highlighter.getBestFragment(analyzer, AnimationIndexField.defaultIndex, animation.getStaff());
        if (staff != null) {
            animation.setStaff(staff);
        }
        
        var actors = highlighter.getBestFragment(analyzer, AnimationIndexField.defaultIndex, animation.getActors());
        if (actors != null) {
            animation.setActors(actors);
        }
        
        return animation;
    }
    
    public List<Animation> highlightResult(String queryString, List<Animation> animations) {
        return animations.stream()
                .map(animation -> {
                    try {
                        return highlightResult(queryString, animation);
                    } catch (Exception err) {
                        System.err.println(err);
                        return animation;
                    }
                })
                .collect(Collectors.toList());
    }
    
    public Optional<List<Integer>> searchAnimationWithKeywords(Stream<org.ansj.domain.Term> resultStream, int page) throws IOException, NumberFormatException {
        var directory = FSDirectory.open(path);
        var reader = DirectoryReader.open(directory);
        var searcher = new IndexSearcher(reader);
        var topDocs = searcher.search(getQuery(resultStream), pageCount * (page + 1));
        var ids = new ArrayList<Integer>();
    
        for (var i = page * pageCount; i < topDocs.scoreDocs.length; i++) {
            var topScore = topDocs.scoreDocs[i];
            var id = Integer.parseInt(searcher.doc(topScore.doc).get(AnimationIndexField.id));
            ids.add(id);
        }
        
        directory.close();
        return Optional.of(ids);
    }
    
    
    public Optional<List<Integer>> searchAnimationWithKeywords(String queryString, int page) throws IOException, NumberFormatException {
        if (queryString.trim().length() <= 0) {
            return Optional.empty();
        }
        
        var resultStream = getQueryResultStream(queryString);
        
        return searchAnimationWithKeywords(resultStream, page);
    }
}
