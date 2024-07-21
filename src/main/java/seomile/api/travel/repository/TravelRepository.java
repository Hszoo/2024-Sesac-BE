package seomile.api.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seomile.api.travel.entity.Travel;

import java.util.Optional;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    Optional<Travel> findByTravId(Long travelId);

    // travCode로 여행지 찾기
    Optional<Travel> findByTravCode(String travCode);
}
