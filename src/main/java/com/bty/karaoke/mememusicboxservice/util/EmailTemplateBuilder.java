package com.bty.karaoke.mememusicboxservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class EmailTemplateBuilder {

    private final TemplateEngine templateEngine;

    public String buildOTPEmailTemplate(
            String recipientName,
            String otpCode
    ) {

        Context context = new Context();

        context.setVariable("recipientName", recipientName);
        context.setVariable("otpCode", otpCode);

        return templateEngine.process(
                "email/otp-email",
                context
        );
    }
}
