package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.dto.response.AccountResponse;
import com.bty.karaoke.mememusicboxservice.entity.Account;
import com.bty.karaoke.mememusicboxservice.entity.PointDiscount;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.AccountMapper;
import com.bty.karaoke.mememusicboxservice.repository.AccountRepository;
import com.bty.karaoke.mememusicboxservice.repository.PointDiscountRepository;
import com.bty.karaoke.mememusicboxservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PointDiscountRepository pointDiscountRepository;

    @Override
    public AccountResponse getAccountById(Long id) {
        if (id == null) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        }
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public BigDecimal getDiscountPercentByMemberAccountId(Long id) {
        if (id == null) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        }
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        if(account.getMemberProfile() == null) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_MEMBER);
        }

        Integer loyaltyPoint = account.getMemberProfile().getLoyaltyPoint();

        Sort sort = Sort.by("requiredPoint").descending();
        List<PointDiscount> pointDiscountList = pointDiscountRepository.findAll(sort);
        for (PointDiscount pointDiscount : pointDiscountList) {
            if(loyaltyPoint >= pointDiscount.getRequiredPoint()) {
                return pointDiscount.getDiscountPercent();
            }
        }

        return new BigDecimal("0");
    }
}
