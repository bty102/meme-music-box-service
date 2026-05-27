package com.bty.karaoke.mememusicboxservice.repository;

import com.bty.karaoke.mememusicboxservice.constant.Role;
import com.bty.karaoke.mememusicboxservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Account> findByIsActive(Boolean isActive);

    List<Account> findByIsActiveAndRole(Boolean isActive, Role role);
}
