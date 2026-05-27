package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.request.AccRegisVerificationRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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
}
