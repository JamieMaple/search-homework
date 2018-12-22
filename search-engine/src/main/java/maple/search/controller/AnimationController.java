package maple.search.controller;

import maple.search.dao.Animation;
import maple.search.service.AnimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AnimationController {
    @Autowired
    AnimationService animationService;
    
    @RequestMapping("/search")
    public ResponseEntity<List<Animation>> searchAnimation(
            @RequestParam(name = "q", defaultValue = "2333 default query") String queryString,
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        try {
            var animations = animationService.search(queryString, page);
            return new ResponseEntity<>(animations, HttpStatus.OK);
        } catch (IOException err) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
