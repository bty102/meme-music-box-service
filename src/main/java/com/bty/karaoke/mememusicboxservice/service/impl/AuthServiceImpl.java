package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.dto.request.AuthenticationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.LogoutRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.AuthenticationResponse;
import com.bty.karaoke.mememusicboxservice.entity.Account;
import com.bty.karaoke.mememusicboxservice.entity.InvalidToken;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.repository.AccountRepository;
import com.bty.karaoke.mememusicboxservice.repository.InvalidTokenRepository;
import com.bty.karaoke.mememusicboxservice.service.AuthService;
import com.bty.karaoke.mememusicboxservice.util.JwtUtil;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final InvalidTokenRepository invalidTokenRepository;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        if(!passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
            throw new AppException(ErrorCode.INCORRECT_PASSWORD);
        }

        try {
            String accessToken = jwtUtil.generateToken(account);
            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .build();
        } catch (Exception e) {
            throw new AppException(ErrorCode.ACCESSTOKEN_GENERATION_FAILED);
        }

    }

    @Override
    public void logout(LogoutRequest request) {
        try {
            if(!jwtUtil.verifyToken(request.getAccessToken())) {
                throw new AppException(ErrorCode.INVALID_ACCESSTOKEN);
            }

            String jwtID = jwtUtil.getJWTId(request.getAccessToken());
            Date expiryTime = jwtUtil.getExpiryTime(request.getAccessToken());
            InvalidToken invalidToken = InvalidToken.builder()
                    .id(jwtID)
                    .expiryTime(expiryTime)
                    .build();
            invalidTokenRepository.save(invalidToken);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.LOGOUT_FAILED);
        }
    }
}
