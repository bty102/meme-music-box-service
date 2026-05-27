package com.bty.karaoke.mememusicboxservice.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccRegisVerificationRequest {

    private String email;
    private String OTP;
}
