package com.bty.karaoke.mememusicboxservice.repository;

import com.bty.karaoke.mememusicboxservice.entity.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByNationalId(String nationalId);

    boolean existsByPhoneNumberAndIdIsNot(String phoneNumber, Long id);

    boolean existsByNationalIdAndIdIsNot(String nationalId, Long id);
}
