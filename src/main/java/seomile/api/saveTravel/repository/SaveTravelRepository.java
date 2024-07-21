package seomile.api.saveTravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seomile.api.saveTravel.entity.SaveTravel;

import java.util.List;

public interface SaveTravelRepository extends JpaRepository<SaveTravel, Long> {
    List<SaveTravel> findByUserId(Long userId);
}