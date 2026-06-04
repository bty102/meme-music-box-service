package com.bty.karaoke.mememusicboxservice.service;

public interface OTPService {

    public void sendAndSaveRegistrationOTP(String email);

    public boolean regisOTPVerification(String email, String OTP);

    public void sendAndSaveForgotPasswordOTP(String email);

    public boolean forgotPasswordOTPVerification(String email, String OTP);
}
