package com.bty.karaoke.mememusicboxservice.service;

public interface OTPService {

    public void sendAndSaveRegistrationOTP(String email);

    public boolean regisOTPVerification(String email, String OTP);
}
