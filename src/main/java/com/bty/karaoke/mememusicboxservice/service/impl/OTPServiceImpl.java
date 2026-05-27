package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.constant.OTPType;
import com.bty.karaoke.mememusicboxservice.entity.OTP;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.repository.OTPRepository;
import com.bty.karaoke.mememusicboxservice.service.OTPService;
import com.bty.karaoke.mememusicboxservice.util.EmailSender;
import com.bty.karaoke.mememusicboxservice.util.EmailTemplateBuilder;
import com.bty.karaoke.mememusicboxservice.util.OTPGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {

    private final OTPRepository otpRepository;
    private final OTPGenerator otpGenerator;
    private final EmailSender emailSender;
    private final EmailTemplateBuilder emailTemplateBuilder;

    @Override
    public void sendAndSaveRegistrationOTP(String email) {
        String otpCode = otpGenerator.generateOTP();

        OTP otp = OTP.builder()
                .otpCode(otpCode)
                .type(OTPType.REGISTRATION)
                .email(email)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .createdAt(LocalDateTime.now())
                .verified(false)
                .build();

        otpRepository.save(otp);

        String emailContent = emailTemplateBuilder.buildOTPEmailTemplate(email, otpCode);
        try {
            emailSender.sendHtmlEmail(email, "OTP đăng ký tài khoản", emailContent);
        } catch (Exception e) {
            throw new AppException(ErrorCode.SEND_REGISTRATION_OTP_UNSUCCESSFULLY);
        }
    }

    @Override
    public boolean regisOTPVerification(String email, String OTP) {
        try {
            OTP otp = otpRepository.findTopByEmailAndTypeOrderByCreatedAtDesc(email, OTPType.REGISTRATION)
                    .orElseThrow(() -> new AppException(ErrorCode.OTP_INVALID));

            if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
                throw new AppException(ErrorCode.OTP_INVALID);
            }

            if (otp.getVerified()) {
                throw new AppException(ErrorCode.OTP_INVALID);
            }

            if (!otp.getOtpCode().equals(OTP)) {
                throw new AppException(ErrorCode.OTP_INVALID);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
