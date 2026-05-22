package com.bty.karaoke.mememusicboxservice.repository;

import com.bty.karaoke.mememusicboxservice.entity.PointDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointDiscountRepository extends JpaRepository<PointDiscount, Long> {
    boolean existsByRequiredPoint(Integer requiredPoint);

    boolean existsByRequiredPointAndIdIsNot(Integer requiredPoint, Long id);
}
