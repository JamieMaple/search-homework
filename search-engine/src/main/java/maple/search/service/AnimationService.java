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
    
    public List<Animation> getAnimationList() {
        return animationRepository.findAll(Pageable.unpaged()).getContent();
    }
    
    public List<Animation> search(String queryString, int page) throws IOException {
        var ids = animationSearcher.searchAnimationWithKeywords(queryString, page).orElse(new ArrayList<>());
        var animations = new ArrayList<Animation>();
        animationRepository.findAllById(ids).forEach(animations::add);
        return animations;
    }
    
    public Animation getAnimationByID(Integer id) {
        return animationRepository.findById(id).orElse(null);
    }
}

