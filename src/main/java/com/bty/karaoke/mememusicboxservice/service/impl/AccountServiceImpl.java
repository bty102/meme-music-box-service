package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.constant.Role;
import com.bty.karaoke.mememusicboxservice.dto.request.*;
import com.bty.karaoke.mememusicboxservice.dto.response.AccRegisVerificationResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.AccountResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.ForgotPasswordVerificationResponse;
import com.bty.karaoke.mememusicboxservice.entity.*;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.AccountMapper;
import com.bty.karaoke.mememusicboxservice.repository.AccountRepository;
import com.bty.karaoke.mememusicboxservice.repository.EmployeeProfileRepository;
import com.bty.karaoke.mememusicboxservice.repository.MemberProfileRepository;
import com.bty.karaoke.mememusicboxservice.repository.PointDiscountRepository;
import com.bty.karaoke.mememusicboxservice.service.AccountService;
import com.bty.karaoke.mememusicboxservice.service.OTPService;
import com.bty.karaoke.mememusicboxservice.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final EmployeeProfileRepository employeeProfileRepository;

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

    @Override
    public Page<AccountResponse> getMemberAccounts(int pageNumber, int pageSize) {

        if(pageNumber < 0) pageNumber = 0;
        if(pageSize < 1) pageSize = 1;


        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Account> accountPage = accountRepository.findByRole(Role.MEMBER, pageable);
        return accountPage.map(account -> accountMapper.toAccountResponse(account));
    }

    @Override
    public AccountResponse getMemberAccountById(Long id) {
        if(id == null) throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        Account account = accountRepository.findByIdAndRole(id, Role.MEMBER)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public Page<AccountResponse> getEmployeeAccounts(int pageNumber, int pageSize) {

        if(pageNumber < 0) pageNumber = 0;
        if(pageSize < 1) pageSize = 1;


        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Account> accountPage = accountRepository.findByRole(Role.EMPLOYEE, pageable);
        return accountPage.map(account -> accountMapper.toAccountResponse(account));
    }

    @Override
    public Page<AccountResponse> findEmployeeAccounts(String employeeCode, String employeeFullName, int pageNumber, int pageSize) {

        if(pageNumber < 0) pageNumber = 0;
        if(pageSize < 1) pageSize = 1;


        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Account> accountPage = accountRepository.findByRoleAndEmployeeProfile_EmployeeCodeContainingIgnoreCaseOrEmployeeProfile_FullNameContainingIgnoreCase(Role.EMPLOYEE, employeeCode, employeeFullName, pageable);
        return accountPage.map(account -> accountMapper.toAccountResponse(account));
    }

    @Override
    public AccountResponse getEmployeeAccountById(Long accId) {
        if(accId == null) throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        Account account = accountRepository.findByIdAndRole(accId, Role.EMPLOYEE)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        return accountMapper.toAccountResponse(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountResponse createEmployeeAccount(@Valid EmployeeCreationRequest request) {
        if(accountRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.ACCOUNT_EXISTED);

        if(employeeProfileRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.EMPLOYEE_PROFILE_PHONE_NUMBER_EXISTED);
        }
        if(employeeProfileRepository.existsByNationalId(request.getNationalId())) {
            throw new AppException(ErrorCode.EMPLOYEE_PROFILE_NATIONAL_ID_EXISTED);
        }

        Account account = Account.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.EMPLOYEE)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();

        EmployeeProfile employeeProfile = EmployeeProfile.builder()
                .employeeCode(generateEmployeeCode())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .nationalId(request.getNationalId())
                .isMale(request.getIsMale())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .imageUrl(null)
                .account(account)
                .build();

        account.setEmployeeProfile(employeeProfile);
        account = accountRepository.save(account);
        employeeProfileRepository.save(employeeProfile);
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public AccountResponse updateEmployeeAccount(Long accId, @Valid EmployeeAccountUpdateRequest request) {
        if(accId == null) throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        Account employeeAccount = accountRepository.findByIdAndRole(accId, Role.EMPLOYEE)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        if(accountRepository.existsByEmailAndIdIsNot(request.getEmail(), accId)) {
            throw new AppException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
        }

        employeeAccount.setEmail(request.getEmail());
        employeeAccount.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        employeeAccount.setIsActive(request.getIsActive());
        employeeAccount = accountRepository.save(employeeAccount);
        return accountMapper.toAccountResponse(employeeAccount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long accId, @Valid PasswordChangeRequest request) {
        if(accId == null) throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        Account account = accountRepository.findById(accId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        if(!passwordEncoder.matches(request.getOldPassword(), account.getPasswordHash())) {
            throw new AppException(ErrorCode.INCORRECT_PASSWORD);
        }

        account.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
    }

    @Override
    public void forgotPassword(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        otpService.sendAndSaveForgotPasswordOTP(email);
    }

    @Override
    public ForgotPasswordVerificationResponse forgotPasswordVerification(ForgotPasswordVerificationRequest request) {
        boolean valid = otpService.forgotPasswordOTPVerification(request.getEmail(), request.getOtp());
        if(!valid) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }

        // Generate token thoi
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        String token = jwtUtil.generateToken(account);
        return ForgotPasswordVerificationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public void recoverPassword(Long accId, @Valid PasswordRecoveryRequest request) {
        if (accId ==  null) throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        Account account = accountRepository.findById(accId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        account.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
    }

    private String generateMemberCode() {
        return "MB" +
                System.currentTimeMillis() +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 4)
                        .toUpperCase();
    }

    private String generateEmployeeCode() {

    String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));

    String random = UUID.randomUUID()
            .toString()
            .replace("-", "")
            .substring(0, 5)
            .toUpperCase();

    return "EMP" + timestamp + random;
}
}
