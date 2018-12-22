package maple.search.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface AnimationRepository extends CrudRepository<Animation, Integer> {
    Page<Animation> findAll(Pageable pageable);
}
