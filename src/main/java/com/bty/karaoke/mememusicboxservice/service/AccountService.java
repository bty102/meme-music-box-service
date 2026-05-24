package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.response.AccountResponse;

import java.math.BigDecimal;

public interface AccountService {

    public AccountResponse getAccountById(Long id);

    public BigDecimal getDiscountPercentByMemberAccountId(Long id);
}
