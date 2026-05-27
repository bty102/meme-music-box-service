package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.AccRegisVerificationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.MemberAccountRegisRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.AccRegisVerificationResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.AccountResponse;
import jakarta.validation.Valid;

import java.math.BigDecimal;

public interface AccountService {

    public AccountResponse getAccountById(Long id);

    public BigDecimal getDiscountPercentByMemberAccountId(Long id);

    public void registerAccount(String email);

    public AccRegisVerificationResponse accRegisVerification(AccRegisVerificationRequest request);

    public AccountResponse createMemberAccount(String email, @Valid MemberAccountRegisRequest request);
}
