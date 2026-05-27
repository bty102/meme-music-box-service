package com.bty.karaoke.mememusicboxservice.repository;

import com.bty.karaoke.mememusicboxservice.constant.RoomBookingStatus;
import com.bty.karaoke.mememusicboxservice.entity.RoomBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
    Optional<RoomBooking> findFirstByRoomIdAndStatusOrderByBookingTimeAsc(
            Long roomId,
            RoomBookingStatus status
    );

    boolean existsByRoom_IdAndBookingTimeBetween(Long roomId, LocalDateTime bookingTimeAfter, LocalDateTime bookingTimeBefore);

    boolean existsByRoom_IdAndStatus(Long roomId, RoomBookingStatus status);

    boolean existsByIdAndMemberAccount_Email(Long id, String memberAccountEmail);

    Page<RoomBooking> findByRoom_IdOrderByCreatedAtDesc(
            Long roomId,
            Pageable pageable
    );

    Page<RoomBooking> findByMemberAccount_IdOrderByCreatedAtDesc(
            Long memberAccountId,
            Pageable pageable
    );
}
