package maple.search.service;


import maple.search.dao.Animation;
import maple.search.dao.AnimationRepository;
import maple.search.engine.AnimationSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnimationService {
    @Autowired
    private AnimationRepository animationRepository;
    
    private AnimationSearcher animationSearcher = new AnimationSearcher();
    
    private int queryLimit = 30;
    
    public List<Animation> getAnimationList() {
        return animationRepository.findAll(Pageable.unpaged()).getContent();
    }
    
    private String getQueryString(String src) {
        if (src.length() <= queryLimit) {
            return src;
        } else {
            return src.substring(0, queryLimit - 1);
        }
    }
    
    public List<Animation> search(String originalQueryString, int page) throws IOException {
        // limit query string
        var queryString = getQueryString(originalQueryString);
        
        var ids = animationSearcher.searchAnimationWithKeywords(queryString, page).orElse(new ArrayList<>());
        var animations = new ArrayList<Animation>();
        animationRepository.findAllById(ids).forEach(animations::add);
        try {
            return animationSearcher.highlightResult(queryString, animations);
        } catch (Exception err) {
            System.err.println(err);
            return null;
        }
    }
    
    public Animation getAnimationByID(Integer id) {
        return animationRepository.findById(id).orElse(null);
    }
}

