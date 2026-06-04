package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.*;
import com.bty.karaoke.mememusicboxservice.dto.response.AccRegisVerificationResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.AccountResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.ForgotPasswordVerificationResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    public AccountResponse getAccountById(Long id);

    public BigDecimal getDiscountPercentByMemberAccountId(Long id);

    public void registerAccount(String email);

    public AccRegisVerificationResponse accRegisVerification(AccRegisVerificationRequest request);

    public AccountResponse createMemberAccount(String email, @Valid MemberAccountRegisRequest request);

    @PreAuthorize("""
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
        or
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).EMPLOYEE.name())
    """)
    public List<AccountResponse> getActiveMemberAccounts();

    /**
     *
     * @param pageNumber >= 0
     * @param pageSize >= 1
     * @return
     */
    @PreAuthorize("""
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
        or
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).EMPLOYEE.name())
    """)
    public Page<AccountResponse> getMemberAccounts(int pageNumber, int pageSize);

    @PreAuthorize("""
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
        or
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).EMPLOYEE.name())
    """)
    public AccountResponse getMemberAccountById(Long id);

    /**
     *
     * @param pageNumber >= 0
     * @param pageSize >= 1
     * @return
     */
    @PreAuthorize("""
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
    """)
    public Page<AccountResponse> getEmployeeAccounts(int pageNumber, int pageSize);

    /**
     *
     * @param employeeCode
     * @param employeeFullName
     * @param pageNumber >= 0
     * @param pageSize >= 1
     * @return
     */
    @PreAuthorize("""
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
    """)
    public Page<AccountResponse> findEmployeeAccounts(String employeeCode, String employeeFullName, int  pageNumber, int pageSize);

    @PreAuthorize("""
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
    """)
    public AccountResponse getEmployeeAccountById(Long accId);

    @PreAuthorize("""
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
    """)
    public AccountResponse createEmployeeAccount(@Valid EmployeeCreationRequest request);

    @PreAuthorize("""
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
    """)
    public AccountResponse updateEmployeeAccount(Long accId, @Valid EmployeeAccountUpdateRequest request);

    @PreAuthorize("""
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).MEMBER.name())
    """)
    public void changePassword(Long accId , @Valid PasswordChangeRequest request);

    public void forgotPassword(String email);

    public ForgotPasswordVerificationResponse forgotPasswordVerification(ForgotPasswordVerificationRequest request);

    public void recoverPassword(Long accId, @Valid PasswordRecoveryRequest request);
}
