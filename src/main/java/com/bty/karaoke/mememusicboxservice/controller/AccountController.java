package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.request.AccRegisVerificationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.EmployeeAccountUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.EmployeeCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.MemberAccountRegisRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.AccRegisVerificationResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.AccountResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.service.AccountService;
import com.bty.karaoke.mememusicboxservice.service.OTPService;
import com.cloudinary.Api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping(path = "/register", produces = "application/json")
    public ResponseEntity<ApiResponse<Void>> sendRegistrationOTP(
            @RequestParam(name = "email") String email
    ) {
        accountService.registerAccount(email);
        return ResponseEntity.accepted()
                .body(ApiResponse.<Void>builder().build());
    }

    @PostMapping(path = "/register/verify", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<AccRegisVerificationResponse>> verifyRegistration(
            @RequestBody AccRegisVerificationRequest request
    ) {
        var response = accountService.accRegisVerification(request);
        return ResponseEntity.ok(
                ApiResponse.<AccRegisVerificationResponse>builder()
                        .result(response)
                        .build()
        );
    }

    @PostMapping(path = "/createMemberAcc", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<AccountResponse>> createMemberAccount(
             @Valid @RequestBody MemberAccountRegisRequest request,
             @AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getSubject();
        var response = accountService.createMemberAccount(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<AccountResponse>builder()
                        .result(response)
                        .build()
        );
    }

    @GetMapping(path = "/activeMembers", produces = "application/json")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getActiveMemberAccounts() {
        var response = accountService.getActiveMemberAccounts();
        return ResponseEntity.ok(
                ApiResponse.<List<AccountResponse>>builder()
                        .result(response)
                        .build()
        );
    }

    @GetMapping(path = "/members", produces = "application/json")
    public ResponseEntity<ApiResponse<Page<AccountResponse>>> getMemberAccounts(

            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "1") int pageSize
    ) {
        var response = accountService.getMemberAccounts(pageNumber, pageSize);
        return ResponseEntity.ok(
                ApiResponse.<Page<AccountResponse>>builder()
                        .result(response)
                        .build()
        );
    }

    @GetMapping(path = "/members/{memberAccId}", produces = "application/json")
    public ResponseEntity<ApiResponse<AccountResponse>> getMemberAccountById(
            @PathVariable("memberAccId") Long memberId
    ) {
        var response = accountService.getMemberAccountById(memberId);
        return ResponseEntity.ok(
                ApiResponse.<AccountResponse>builder()
                        .result(response)
                        .build()
        );
    }

    @GetMapping(path = "/employees", produces = "application/json")
    public ResponseEntity<ApiResponse<Page<AccountResponse>>> getEmployeeAccounts(
            @RequestParam(name = "q", required = false, defaultValue = "") String query,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "1") int pageSize
    ) {
        Page<AccountResponse> response = null;
        if(query.isEmpty()) {
            response = accountService.getEmployeeAccounts(pageNumber, pageSize);
        } else {
            response = accountService.findEmployeeAccounts(query, query, pageNumber, pageSize);
        }
        return ResponseEntity.ok(
                ApiResponse.<Page<AccountResponse>>builder()
                        .result(response)
                        .build()
        );
    }

    @GetMapping(path = "/employees/detail/{accId}", produces = "application/json")
    public ResponseEntity<ApiResponse<AccountResponse>> getEmployeeAccountById(
            @PathVariable("accId")  Long accId
    ) {
        var response = accountService.getEmployeeAccountById(accId);
        return ResponseEntity.ok(
                ApiResponse.<AccountResponse>builder()
                        .result(response)
                        .build()
        );
    }

    @PostMapping(path = "/employees", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<AccountResponse>> createEmployeeAccount(
            @Valid @RequestBody EmployeeCreationRequest request
    ) {
        var response = accountService.createEmployeeAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<AccountResponse>builder()
                        .result(response)
                        .build()
        );
    }

    @PutMapping(path = "/employees/{accId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<AccountResponse>> updateEmployeeAccount(
            @PathVariable("accId") Long accId,
            @Valid @RequestBody EmployeeAccountUpdateRequest request
    ) {
        var response = accountService.updateEmployeeAccount(accId, request);
        return ResponseEntity.ok(
                ApiResponse.<AccountResponse>builder()
                        .result(response)
                        .build()
        );
    }
}
