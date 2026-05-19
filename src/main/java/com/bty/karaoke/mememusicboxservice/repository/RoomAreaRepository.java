package com.bty.karaoke.mememusicboxservice.repository;

import com.bty.karaoke.mememusicboxservice.entity.RoomArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomAreaRepository extends JpaRepository<RoomArea, Long> {
    boolean existsByAreaName(String areaName);
}
