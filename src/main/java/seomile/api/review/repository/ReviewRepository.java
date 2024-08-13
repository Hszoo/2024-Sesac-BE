package seomile.api.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import seomile.api.member.entity.Member;
import seomile.api.review.entity.Review;
import seomile.api.travel.entity.Travel;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByReviewId(Long reviewId);

    Optional<List<Review>> findByMember(Member member); // 사용자가 작성했던 리뷰 전체 조회

    @Query("SELECT r FROM Review r WHERE r.travel.travCode = :travCode")
    List<Review> findByTravelCode(@Param("travCode") String travCode);

    @Query("SELECT r FROM Review r WHERE r.travel.travCode = :travCode AND r.reviewId = :reviewId")
    Optional<Review> findByTravelCodeAndReviewId(@Param("travCode") String travCode, @Param("reviewId") Long reviewId);
}