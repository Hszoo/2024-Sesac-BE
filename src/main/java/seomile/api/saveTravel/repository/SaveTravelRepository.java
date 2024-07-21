package seomile.api.saveTravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seomile.api.saveTravel.entity.SaveTravel;

import java.util.List;
import java.util.Optional;

public interface SaveTravelRepository extends JpaRepository<SaveTravel, Long> {
    List<SaveTravel> findByUserId(Long userId);
    Optional<SaveTravel> findByUserIdAndTravCode(Long userId, String travCode);


}