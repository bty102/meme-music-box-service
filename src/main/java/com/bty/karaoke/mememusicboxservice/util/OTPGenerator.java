package com.bty.karaoke.mememusicboxservice.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OTPGenerator {

    private static final SecureRandom secureRandom =
            new SecureRandom();

    public String generateOTP() {

        int otp = secureRandom.nextInt(1_000_000);

        return String.format("%06d", otp);
    }
}
