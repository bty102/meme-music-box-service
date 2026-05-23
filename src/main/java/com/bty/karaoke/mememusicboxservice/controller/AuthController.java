package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.request.AuthenticationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.LogoutRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.AuthenticationResponse;
import com.bty.karaoke.mememusicboxservice.service.AuthService;
import com.cloudinary.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @RequestBody AuthenticationRequest request
    ) {
        var response = authService.authenticate(request);
        return ResponseEntity.ok(ApiResponse.<AuthenticationResponse>builder()
                .result(response)
                .build());
    }

    @PostMapping(path = "/logout", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestBody LogoutRequest request
    ) {
        authService.logout(request);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .build());
    }
}
