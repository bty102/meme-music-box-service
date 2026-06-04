package com.bty.karaoke.mememusicboxservice.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgotPasswordVerificationResponse {

    private String token;
}
