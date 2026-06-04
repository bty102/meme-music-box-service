package com.bty.karaoke.mememusicboxservice.repository;

import com.bty.karaoke.mememusicboxservice.constant.Role;
import com.bty.karaoke.mememusicboxservice.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Account> findByIsActive(Boolean isActive);

    List<Account> findByIsActiveAndRole(Boolean isActive, Role role);

    Page<Account> findByRole(Role role, Pageable pageable);

    Optional<Account> findByIdAndRole(Long id, Role role);

    @Query("""
                SELECT a
                FROM Account a
                JOIN a.employeeProfile ep
                WHERE a.role = com.bty.karaoke.mememusicboxservice.constant.Role.EMPLOYEE
                    AND (
                        :keyword IS NULL
                        OR :keyword = ''
                        OR LOWER(ep.employeeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(ep.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    )
            """)
    Page<Account> searchEmployees(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    Page<Account>
    findByRoleAndEmployeeProfile_EmployeeCodeContainingIgnoreCaseOrEmployeeProfile_FullNameContainingIgnoreCase(
            Role role,
            String employeeCode,
            String fullName,
            Pageable pageable
    );

    boolean existsByEmailAndIdIsNot(String email, Long id);
}
