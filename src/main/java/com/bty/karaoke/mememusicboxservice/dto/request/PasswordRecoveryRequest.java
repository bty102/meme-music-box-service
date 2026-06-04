package com.bty.karaoke.mememusicboxservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordRecoveryRequest {

    @NotNull(message = "ACCOUNT_PASSWORD_NULL")
    @Size(min = 6, message = "ACCOUNT_PASSWORD_SIZE_INVALID")
    private String newPassword;
}
