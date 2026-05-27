package com.bty.karaoke.mememusicboxservice.rabbitmq.consumer;

import com.bty.karaoke.mememusicboxservice.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationOTPReceivedEmailConsumer {

    private final OTPService otpService;

    @RabbitListener(queues = "registrationOTPReceivedEmail.queue")
    public void sendRegistrationOTPToEmail(String email) {
        System.out.println("Sending email: " + email);
        try {
            otpService.sendAndSaveRegistrationOTP(email);
        } catch (Exception e) {
            System.out.println("Error sending email: " + email);
        }
    }
}
