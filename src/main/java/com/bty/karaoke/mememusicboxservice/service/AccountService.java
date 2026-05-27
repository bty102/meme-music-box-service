package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.AccRegisVerificationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.MemberAccountRegisRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.AccRegisVerificationResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.AccountResponse;
import jakarta.validation.Valid;
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
}
