package com.bty.karaoke.mememusicboxservice.repository;

import com.bty.karaoke.mememusicboxservice.entity.RoomArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomAreaRepository extends JpaRepository<RoomArea, Long> {
    boolean existsByAreaName(String areaName);

    List<RoomArea> findByIsActive(Boolean isActive);

    boolean existsByAreaNameAndIdIsNot(String areaName, Long id);
    
    boolean existsByIdAndIsActive(Long id, Boolean isActive);
}
