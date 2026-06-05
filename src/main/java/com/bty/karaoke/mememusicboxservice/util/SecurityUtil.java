package com.bty.karaoke.mememusicboxservice.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public Long getCurrentAccountId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Jwt jwt = (Jwt) authentication.getPrincipal();

            Long accountId = Long.parseLong(jwt.getClaims().get("accId").toString());
            return accountId;
        } else {
            throw new RuntimeException("Unauthenticated");
        }
    }
}
