package com.bty.karaoke.mememusicboxservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAccountRegisRequest {

    @NotNull(message = "ACCOUNT_PASSWORD_NULL")
    @Size(min = 6, message = "ACCOUNT_PASSWORD_SIZE_INVALID")
    private String password;

    @NotNull(message = "MEMBER_PROFILE_FULL_NAME_NULL")
    @Size(min = 1, message = "MEMBER_PROFILE_FULL_NAME_SIZE_INVALID")
    private String fullName;

    @NotNull(message = "MEMBER_PROFILE_IS_MALE_NULL")
    private Boolean isMale;

    @NotNull(message = "MEMBER_PROFILE_DATE_OF_BIRTH_NULL")
    private LocalDate dateOfBirth;
}
