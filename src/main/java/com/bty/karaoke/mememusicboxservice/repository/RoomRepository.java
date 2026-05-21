package com.bty.karaoke.mememusicboxservice.repository;

import com.bty.karaoke.mememusicboxservice.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByArea_IdAndIsActive(Long areaId, Boolean isActive);

    boolean existsByRoomNumber(Integer roomNumber);

    Page<Room> findByArea_Id(Long areaId, Pageable pageable);

    boolean existsByRoomNumberAndIdIsNot(Integer roomNumber, Long id);

    Page<Room> findByRoomNumberOrCapacity(Integer roomNumber, Integer capacity, Pageable pageable);

    Page<Room> findByArea_IdAndIsActive(Long areaId, Boolean isActive, Pageable pageable);

    Page<Room> findByRoomNumberAndIsActiveOrCapacityAndIsActive(Integer roomNumber, Boolean isActive, Integer capacity, Boolean isActive1, Pageable pageable);
}
