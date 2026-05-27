package com.bty.karaoke.mememusicboxservice.repository;

import com.bty.karaoke.mememusicboxservice.constant.OTPType;
import com.bty.karaoke.mememusicboxservice.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findTopByEmailAndTypeOrderByCreatedAtDesc(String email, OTPType type);
}
