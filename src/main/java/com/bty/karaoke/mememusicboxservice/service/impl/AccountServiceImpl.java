package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.constant.Role;
import com.bty.karaoke.mememusicboxservice.dto.request.AccRegisVerificationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.MemberAccountRegisRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.AccRegisVerificationResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.AccountResponse;
import com.bty.karaoke.mememusicboxservice.entity.Account;
import com.bty.karaoke.mememusicboxservice.entity.MemberProfile;
import com.bty.karaoke.mememusicboxservice.entity.PointDiscount;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.AccountMapper;
import com.bty.karaoke.mememusicboxservice.repository.AccountRepository;
import com.bty.karaoke.mememusicboxservice.repository.MemberProfileRepository;
import com.bty.karaoke.mememusicboxservice.repository.PointDiscountRepository;
import com.bty.karaoke.mememusicboxservice.service.AccountService;
import com.bty.karaoke.mememusicboxservice.service.OTPService;
import com.bty.karaoke.mememusicboxservice.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PointDiscountRepository pointDiscountRepository;
    private final OTPService otpService;
    private final RabbitTemplate rabbitTemplate;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberProfileRepository memberProfileRepository;

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

        if (account.getMemberProfile() == null) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_MEMBER);
        }

        Integer loyaltyPoint = account.getMemberProfile().getLoyaltyPoint();

        Sort sort = Sort.by("requiredPoint").descending();
        List<PointDiscount> pointDiscountList = pointDiscountRepository.findAll(sort);
        for (PointDiscount pointDiscount : pointDiscountList) {
            if (loyaltyPoint >= pointDiscount.getRequiredPoint()) {
                return pointDiscount.getDiscountPercent();
            }
        }

        return new BigDecimal("0");
    }

    @Override
    public void registerAccount(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.ACCOUNT_EXISTED);
        }

        rabbitTemplate.convertAndSend("meme.exchange", "registrationOTPReceivedEmail.queue", email);
    }

    @Override
    public AccRegisVerificationResponse accRegisVerification(AccRegisVerificationRequest request) {
        boolean valid = otpService.regisOTPVerification(request.getEmail(), request.getOTP());
        if (!valid) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }
        // tao token thoi
        String regisToken = jwtUtil.generateToken(
                Account.builder()
                        .email(request.getEmail())
                        .role(Role.MEMBER)
                        .build()
        );
        return AccRegisVerificationResponse.builder()
                .regisToken(regisToken)
                .build();
    }

    @Override
    public AccountResponse createMemberAccount(String email, @Valid MemberAccountRegisRequest request) {
        if (accountRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.ACCOUNT_EXISTED);
        }

        // Tao thoi
        Account account = Account.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.MEMBER)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();

        MemberProfile memberProfile = MemberProfile.builder()
                .memberCode(generateMemberCode())
                .fullName(request.getFullName())
                .isMale(request.getIsMale())
                .dateOfBirth(request.getDateOfBirth())
                .loyaltyPoint(0)
                .imageUrl(null)
                .build();
        account.setMemberProfile(memberProfile);
        memberProfile.setAccount(account);
        account = accountRepository.save(account);
        memberProfileRepository.save(memberProfile);
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public List<AccountResponse> getActiveMemberAccounts() {
        List<Account> accountList = accountRepository.findByIsActiveAndRole(true, Role.MEMBER);
        return accountList.stream()
                .map(account -> accountMapper.toAccountResponse(account))
                .toList();
    }

    private String generateMemberCode() {
        return "MB" +
                System.currentTimeMillis() +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 4)
                        .toUpperCase();
    }
}
